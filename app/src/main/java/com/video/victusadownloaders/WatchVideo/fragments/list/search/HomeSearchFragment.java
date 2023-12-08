package com.video.victusadownloaders.WatchVideo.fragments.list.search;

import static java.util.Arrays.asList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.WatchVideo.activities.ReCaptchaActivity;
import com.video.victusadownloaders.WatchVideo.database.history.model.SearchHistoryEntry;
import com.video.victusadownloaders.WatchVideo.fragments.BackPressable;
import com.video.victusadownloaders.WatchVideo.fragments.list.BaseListFragment;
import com.video.victusadownloaders.WatchVideo.local.history.HistoryRecordManager;
import com.video.victusadownloaders.WatchVideo.report.UserAction;
import com.video.victusadownloaders.WatchVideo.util.AnimationUtils;
import com.video.victusadownloaders.WatchVideo.util.Constants;
import com.video.victusadownloaders.WatchVideo.util.ExtractorHelper;
import com.video.victusadownloaders.WatchVideo.util.LayoutManagerSmoothScroller;
import com.video.victusadownloaders.WatchVideo.util.NavigationHelper;
import com.video.victusadownloaders.WatchVideo.util.ServiceHelper;
import com.video.victusadownloaders.utilities.NativeAdNew;

import org.jetbrains.annotations.NotNull;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.search.SearchInfo;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;
import io.awesome.gagtube.R;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class HomeSearchFragment extends BaseListFragment<SearchInfo, ListExtractor.InfoItemsPage> implements BackPressable {

    /**
     * The suggestions will only be fetched from network if the query meet this threshold (>=).
     * (local ones will be fetched regardless of the length)
     */
    private static final int THRESHOLD_NETWORK_SUGGESTION = 1;

    /**
     * How much time have to pass without emitting a item (i.e. the user stop typing) to fetch/show the suggestions, in milliseconds.
     */
    private static final int SUGGESTIONS_DEBOUNCE = 120;

    @State
    protected int filterItemCheckedId = -1;

    @State
    protected int serviceId = Constants.YOUTUBE_SERVICE_ID;

    @State
    protected String searchString;
    @State
    protected String[] contentFilter;
    @State
    protected String sortFilter;

    @State
    protected String lastSearchedString;

    @State
    protected boolean wasSearchFocused = false;

    private Page nextPage;
    private boolean isSuggestionsEnabled = true;

    private PublishSubject<String> suggestionPublisher = PublishSubject.create();
    private Disposable searchDisposable;
    private Disposable suggestionDisposable;
    private CompositeDisposable disposables = new CompositeDisposable();

    private SuggestionListAdapter suggestionListAdapter;
    private HistoryRecordManager historyRecordManager;

    private View suggestionsPanel;
    private RecyclerView suggestionsRecyclerView;

    public static HomeSearchFragment getInstance(int serviceId, String searchString) {

        HomeSearchFragment searchFragment = new HomeSearchFragment();
        searchFragment.setQuery(serviceId, searchString, new String[0], "");
        if (!TextUtils.isEmpty(searchString)) {
            searchFragment.setSearchOnResume();
        }
        return searchFragment;
    }

    private void setSearchOnResume() {
        wasLoading.set(true);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        suggestionListAdapter = new SuggestionListAdapter(activity);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isSearchHistoryEnabled = preferences.getBoolean(getString(R.string.enable_search_history_key), true);
        suggestionListAdapter.setShowSuggestionHistory(isSearchHistoryEnabled);

        historyRecordManager = new HistoryRecordManager(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        isSuggestionsEnabled = preferences.getBoolean(getString(R.string.show_search_suggestions_key), true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_home, container, false);
        ButterKnife.bind(this, view);
        NativeAdNew.showMediumNative(getActivity(), view.findViewById(R.id.nativeAd));

        PrefManagerVideo prf = new PrefManagerVideo(getActivity());
        if (!prf.getBoolean(SplashActivityScr.TAG_YOUTUBE_SETTING_ENABLED)) {
            ImageButton imageButton = view.findViewById(R.id.action_settings);
            imageButton.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, Bundle savedInstanceState) {

        super.onViewCreated(rootView, savedInstanceState);
        String keyword = new PrefManagerVideo(getActivity()).getString(SplashActivityScr.search_keyword);
        search(keyword, contentFilter, sortFilter);


    }

    @Override
    public void onPause() {

        super.onPause();

        if (searchDisposable != null) searchDisposable.dispose();
        if (suggestionDisposable != null) suggestionDisposable.dispose();
        if (disposables != null) disposables.clear();
    }

    @Override
    public void onResume() {

        super.onResume();

        // show ad
        showNativeAd();

        // search by keyword
        if (!TextUtils.isEmpty(searchString)) {
            if (wasLoading.getAndSet(false)) {
                search(searchString, contentFilter, sortFilter);
            } else if (infoListAdapter.getItemsList().size() == 0) {
                search(searchString, contentFilter, sortFilter);
            }
        }

        if (suggestionDisposable == null || suggestionDisposable.isDisposed())
            initSuggestionObserver();

        if (TextUtils.isEmpty(searchString) || wasSearchFocused) {
            showSuggestionsPanel();
        } else {
            hideSuggestionsPanel();
        }
        wasSearchFocused = false;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (searchDisposable != null) searchDisposable.dispose();
        if (suggestionDisposable != null) suggestionDisposable.dispose();
        if (disposables != null) disposables.clear();
    }

    // Init
    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {

        super.initViews(rootView, savedInstanceState);

        suggestionsPanel = rootView.findViewById(R.id.suggestions_panel);
        suggestionsRecyclerView = rootView.findViewById(R.id.suggestions_list);
        suggestionsRecyclerView.setAdapter(suggestionListAdapter);
        suggestionsRecyclerView.setLayoutManager(new LayoutManagerSmoothScroller(activity));


    }

    // State Saving
    @Override
    public void writeTo(Queue<Object> objectsToSave) {
        super.writeTo(objectsToSave);
        objectsToSave.add(nextPage);
    }

    @OnClick(R.id.action_search)
    void onSearch() {
        // open SearchFragment
        NavigationHelper.openSearchFragment(getFM(), ServiceHelper.getSelectedServiceId(activity), "");
    }

    @OnClick(R.id.action_settings)
    void onSettings() {
        // open Settings
        NavigationHelper.openSettings(activity);
    }


    @Override
    public void readFrom(@NonNull Queue<Object> savedObjects) throws Exception {
        super.readFrom(savedObjects);
        nextPage = (Page) savedObjects.poll();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void reloadContent() {

        if (!TextUtils.isEmpty(searchString)) {
        } else {
            AnimationUtils.animateView(errorPanelRoot, false, 200);
        }
    }


    private void showSuggestionsPanel() {
        AnimationUtils.animateView(suggestionsPanel, AnimationUtils.Type.LIGHT_SLIDE_AND_ALPHA, true, 200);
    }

    private void hideSuggestionsPanel() {
        AnimationUtils.animateView(suggestionsPanel, AnimationUtils.Type.LIGHT_SLIDE_AND_ALPHA, false, 200);
    }


    @Override
    public boolean onBackPressed() {

        onPopBackStack();

        return false;
    }

    private void initSuggestionObserver() {

        if (suggestionDisposable != null) suggestionDisposable.dispose();

        final Observable<String> observable = suggestionPublisher
                .debounce(SUGGESTIONS_DEBOUNCE, TimeUnit.MILLISECONDS)
                .startWith(searchString != null ? searchString : "")
                .filter(searchString -> isSuggestionsEnabled);

        suggestionDisposable = observable.switchMap(query -> {

                    final Flowable<List<SearchHistoryEntry>> flowable = historyRecordManager.getRelatedSearches(query, 3, 25);

                    final Observable<List<SuggestionItem>> local = flowable.toObservable().map(searchHistoryEntries -> {
                        List<SuggestionItem> result = new ArrayList<>();
                        for (SearchHistoryEntry entry : searchHistoryEntries)
                            result.add(new SuggestionItem(true, entry.getSearch()));
                        return result;
                    });

                    if (query.length() < THRESHOLD_NETWORK_SUGGESTION) {
                        // Only pass through if the query length is equal or greater than THRESHOLD_NETWORK_SUGGESTION
                        return local.materialize();
                    }

                    final Observable<List<SuggestionItem>> network = ExtractorHelper
                            .suggestionsFor(serviceId, query)
                            .toObservable()
                            .map(strings -> {
                                List<SuggestionItem> result = new ArrayList<>();
                                for (String entry : strings) {
                                    result.add(new SuggestionItem(false, entry));
                                }
                                return result;
                            });

                    return Observable.zip(local, network, (localResult, networkResult) -> {

                        List<SuggestionItem> result = new ArrayList<>();
                        if (localResult.size() > 0) result.addAll(localResult);

                        // Remove duplicates
                        final Iterator<SuggestionItem> iterator = networkResult.iterator();
                        while (iterator.hasNext() && localResult.size() > 0) {

                            final SuggestionItem next = iterator.next();
                            for (SuggestionItem item : localResult) {

                                if (item.query.equals(next.query)) {
                                    iterator.remove();
                                    break;
                                }
                            }
                        }

                        if (networkResult.size() > 0) result.addAll(networkResult);
                        return result;
                    }).materialize();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listNotification -> {
                    if (listNotification.isOnNext()) {
                        handleSuggestions(listNotification.getValue());
                    } else if (listNotification.isOnError()) {
                        Throwable error = listNotification.getError();
                        if (!ExtractorHelper.hasAssignableCauseThrowable(error,
                                IOException.class, SocketException.class,
                                InterruptedException.class, InterruptedIOException.class)) {
                            onSuggestionError(error);
                        }
                    }
                });
    }

    @Override
    protected void doInitialLoadLogic() {
        // no-op
    }

    @SuppressLint("CheckResult")
    private void search(final String searchString, String[] contentFilter, String sortFilter) {

        if (searchString.isEmpty()) return;

        try {
            final StreamingService service = NewPipe.getServiceByUrl(searchString);
            if (service != null) {
                showLoading();
                disposables.add(Observable.fromCallable(() -> NavigationHelper.getIntentByLink(activity, service, searchString))
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                // onNext
                                intent -> {
                                    if (getFragmentManager() != null) {
                                        getFragmentManager().popBackStackImmediate();
                                    }
                                    activity.startActivity(intent);
                                },
                                // onError
                                throwable -> showError(getString(R.string.url_not_supported_toast), false)));
                return;
            }
        } catch (Exception e) {
            // Exception occurred, it's not a url
        }

        lastSearchedString = this.searchString;
        this.searchString = searchString;
        infoListAdapter.clearStreamItemList();
        hideSuggestionsPanel();

        historyRecordManager.onSearched(serviceId, searchString)
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
        suggestionPublisher.onNext(searchString);
        startLoading(false);
    }

    @Override
    public void startLoading(boolean forceLoad) {

        super.startLoading(forceLoad);

        if (disposables != null) disposables.clear();
        if (searchDisposable != null) searchDisposable.dispose();
        searchDisposable = ExtractorHelper.searchFor(serviceId, searchString, Arrays.asList(contentFilter), sortFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((searchResult, throwable) -> isLoading.set(false))
                .subscribe(this::handleResult, this::onError);

    }

    @Override
    protected void loadMoreItems() {
        if (!Page.isValid(nextPage)) return;
        isLoading.set(true);
        showListFooter(true);
        if (searchDisposable != null) searchDisposable.dispose();
        searchDisposable = ExtractorHelper.getMoreSearchItems(serviceId, searchString, asList(contentFilter), sortFilter, nextPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent((nextItemsResult, throwable) -> isLoading.set(false))
                .subscribe(this::handleNextItems, this::onError);
    }

    @Override
    protected boolean hasMoreItems() {
        return true;
    }

    @Override
    protected void onItemSelected(InfoItem selectedItem) {
        super.onItemSelected(selectedItem);
    }


    private void setQuery(int serviceId, String searchString, String[] contentFilter, String sortFilter) {

        this.serviceId = serviceId;
        this.searchString = searchString;
        this.contentFilter = contentFilter;
        this.sortFilter = sortFilter;
    }

    // Suggestion Results
    public void handleSuggestions(@NonNull final List<SuggestionItem> suggestions) {

        suggestionsRecyclerView.smoothScrollToPosition(0);
        suggestionsRecyclerView.post(() -> suggestionListAdapter.setItems(suggestions));

        if (errorPanelRoot.getVisibility() == View.VISIBLE) {
            hideLoading();
        }
    }

    public void onSuggestionError(Throwable exception) {

        if (super.onError(exception)) return;

        int errorId = exception instanceof ParsingException ? R.string.parsing_error : R.string.general_error;
        onUnrecoverableError(exception, UserAction.GET_SUGGESTIONS, ServiceList.YouTube.getServiceInfo().getName(), searchString, errorId);
    }

    // Contract
    @Override
    public void hideLoading() {

        super.hideLoading();
        showListFooter(false);
    }

    @Override
    public void showError(String message, boolean showRetryButton) {
        super.showError(message, showRetryButton);
        hideSuggestionsPanel();
    }

    // Search Results
    @Override
    public void handleResult(@NonNull SearchInfo result) {

        lastSearchedString = searchString;
        nextPage = result.getNextPage();

        if (infoListAdapter.getItemsList().size() == 0) {
            if (!result.getRelatedItems().isEmpty()) {
                infoListAdapter.addInfoItemList(result.getRelatedItems());
            } else {
                infoListAdapter.clearStreamItemList();
                showEmptyState();
                return;
            }
        }
        super.handleResult(result);
    }

    @Override
    public void handleNextItems(ListExtractor.InfoItemsPage result) {

        showListFooter(false);

        infoListAdapter.addInfoItemList(result.getItems());
        nextPage = result.getNextPage();

        if (!result.getErrors().isEmpty()) {
            showSnackBarError(result.getErrors(), UserAction.SEARCHED, ServiceList.YouTube.getServiceInfo().getName(),
                    "\"" + searchString + "\" → pageUrl: " + nextPage.getUrl() + ", "
                            + "pageIds: " + nextPage.getIds() + ", "
                            + "pageCookies: " + nextPage.getCookies(), 0);
        }
        super.handleNextItems(result);
    }

    @Override
    protected boolean onError(Throwable exception) {

        if (super.onError(exception)) return true;

        if (exception instanceof SearchExtractor.NothingFoundException) {
            infoListAdapter.clearStreamItemList();
            showEmptyState();
        } else {
            int errorId = exception instanceof ParsingException ? R.string.parsing_error : R.string.general_error;
            onUnrecoverableError(exception, UserAction.SEARCHED, ServiceList.YouTube.getServiceInfo().getName(), searchString, errorId);
        }
        return true;
    }

    private void onPopBackStack() {

        // pop back stack
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    private void showNativeAd() {
        //ADZ
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ReCaptchaActivity.RECAPTCHA_REQUEST && resultCode == Activity.RESULT_OK && !TextUtils.isEmpty(searchString)) {
            search(searchString, contentFilter, sortFilter);
        }
    }
}
