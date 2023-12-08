package com.video.victusadownloaders.WatchVideo.library;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.WatchVideo.database.AppDatabase;
import com.video.victusadownloaders.WatchVideo.database.GAGTubeDatabase;
import com.video.victusadownloaders.WatchVideo.database.LocalItem;
import com.video.victusadownloaders.WatchVideo.database.playlist.PlaylistLocalItem;
import com.video.victusadownloaders.WatchVideo.database.playlist.PlaylistMetadataEntry;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistRemoteEntity;
import com.video.victusadownloaders.WatchVideo.local.BaseLocalListFragment;
import com.video.victusadownloaders.WatchVideo.local.dialog.PlaylistCreationDialog;
import com.video.victusadownloaders.WatchVideo.local.playlist.LocalPlaylistManager;
import com.video.victusadownloaders.WatchVideo.local.playlist.RemotePlaylistManager;
import com.video.victusadownloaders.WatchVideo.report.UserAction;
import com.video.victusadownloaders.WatchVideo.util.NavigationHelper;
import com.video.victusadownloaders.WatchVideo.util.OnClickGesture;
import com.video.victusadownloaders.WatchVideo.util.ServiceHelper;
import com.video.victusadownloaders.utilities.AdsWork;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.awesome.gagtube.R;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LibraryFragment extends BaseLocalListFragment<List<PlaylistLocalItem>, Void> {

    @BindView(R.id.empty_message)
    TextView emptyMessage;
    PrefManagerVideo prf;

    private Subscription databaseSubscription;
    private CompositeDisposable disposables = new CompositeDisposable();
    private LocalPlaylistManager localPlaylistManager;
    private RemotePlaylistManager remotePlaylistManager;

    public static LibraryFragment getInstance() {
        return new LibraryFragment();
    }

    private static List<PlaylistLocalItem> merge(final List<PlaylistMetadataEntry> localPlaylists, final List<PlaylistRemoteEntity> remotePlaylists) {

        List<PlaylistLocalItem> items = new ArrayList<>(localPlaylists.size() + remotePlaylists.size());
        items.addAll(localPlaylists);
        items.addAll(remotePlaylists);

        return items;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final AppDatabase database = GAGTubeDatabase.getInstance(activity);
        localPlaylistManager = new LocalPlaylistManager(database);
        remotePlaylistManager = new RemotePlaylistManager(database);
        disposables = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library, container, false);
        AdsWork.loadNativeAd(getActivity(), view.findViewById(R.id.template_view), R.layout.ad_unified_medium);

        prf = new PrefManagerVideo(activity);


        AdsWork.showInterAds(getActivity(), new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {

            }
        });


        ButterKnife.bind(this, view);
        if (!prf.getBoolean(SplashActivityScr.TAG_YOUTUBE_SETTING_ENABLED)) {
            ImageButton imageButton = view.findViewById(R.id.action_settings);
            imageButton.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {

        super.initViews(rootView, savedInstanceState);

        // for empty message
        emptyMessage.setText(R.string.no_playlists);

        // show ad
        showBannerAd();
    }

    @Override
    protected void initListeners() {

        super.initListeners();

        itemListAdapter.setSelectedListener(new OnClickGesture<LocalItem>() {

            @Override
            public void selected(LocalItem selectedItem) {
                try {
                    final FragmentManager fragmentManager = getFM();
                    if (selectedItem instanceof PlaylistMetadataEntry) {
                        final PlaylistMetadataEntry entry = ((PlaylistMetadataEntry) selectedItem);
                        NavigationHelper.openLocalPlaylistFragment(fragmentManager, entry.uid, entry.name);
                    } else if (selectedItem instanceof PlaylistRemoteEntity) {
                        final PlaylistRemoteEntity entry = ((PlaylistRemoteEntity) selectedItem);
                        NavigationHelper.openPlaylistFragment(fragmentManager, entry.getServiceId(), entry.getUrl(), entry.getName());
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void more(LocalItem selectedItem, View view) {
                showPopupMenu(selectedItem, view);
            }
        });
    }

    protected void showPopupMenu(final LocalItem localItem, final View view) {

        final Context context = getContext();
        if (context == null || context.getResources() == null || getActivity() == null) return;

        PopupMenu popup = new PopupMenu(getContext(), view, Gravity.END, 0, R.style.mPopupMenu);
        popup.getMenuInflater().inflate(R.menu.menu_remove_playlist_bookmark, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(item -> {

            int id = item.getItemId();
            if (id == R.id.action_remove) {

                if (localItem instanceof PlaylistMetadataEntry) {
                    showLocalDeleteDialog((PlaylistMetadataEntry) localItem);
                } else if (localItem instanceof PlaylistRemoteEntity) {
                    showRemoteDeleteDialog((PlaylistRemoteEntity) localItem);
                }
            }
            return true;
        });
    }

    // Utils
    private void showLocalDeleteDialog(final PlaylistMetadataEntry item) {
        showDeleteDialog(localPlaylistManager.deletePlaylist(item.uid));
    }

    private void showRemoteDeleteDialog(final PlaylistRemoteEntity item) {
        showDeleteDialog(remotePlaylistManager.deletePlaylist(item.getUid()));
    }

    private void showDeleteDialog(final Single<Integer> deleteReactor) {

        if (activity == null || disposables == null) return;

        new MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.dialog_warning_title)
                .setMessage(R.string.delete_playlist_prompt)
                .setCancelable(true)
                .setPositiveButton(R.string.delete, (dialog, i) ->

                        // delete
                        disposables.add(deleteReactor.observeOn(AndroidSchedulers.mainThread()).subscribe(
                                // onNext
                                ignored -> Toast.makeText(activity, R.string.msg_delete_successfully, Toast.LENGTH_SHORT).show(),
                                // onError
                                this::onError))
                )
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // show ad
//		if (adView != null) {
//			adView.resume();
//		}
        showNativeAd();
    }

    // Fragment LifeCycle - Loading
    @Override
    public void startLoading(boolean forceLoad) {

        super.startLoading(forceLoad);

        Flowable.combineLatest(localPlaylistManager.getPlaylists(), remotePlaylistManager.getPlaylists(), LibraryFragment::merge)
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getPlaylistsSubscriber());
    }

    // Subscriptions Loader
    private Subscriber<List<PlaylistLocalItem>> getPlaylistsSubscriber() {

        return new Subscriber<List<PlaylistLocalItem>>() {

            @Override
            public void onSubscribe(Subscription s) {

                showLoading();
                if (databaseSubscription != null) databaseSubscription.cancel();
                databaseSubscription = s;
                databaseSubscription.request(1);
            }

            @Override
            public void onNext(List<PlaylistLocalItem> subscriptions) {

                handleResult(subscriptions);
                if (databaseSubscription != null) databaseSubscription.request(1);
            }

            @Override
            public void onError(Throwable exception) {
                LibraryFragment.this.onError(exception);
            }

            @Override
            public void onComplete() {
            }
        };
    }

    @Override
    public void handleResult(@NonNull List<PlaylistLocalItem> result) {

        super.handleResult(result);

        itemListAdapter.clearStreamItemList();

        if (!result.isEmpty()) {
            // set items to adapter
            itemListAdapter.addItems(result);
            hideLoading();
        } else {
            showEmptyState();
        }
    }

    // Fragment Error Handling
    @Override
    protected boolean onError(Throwable exception) {

        if (super.onError(exception)) return true;

        onUnrecoverableError(exception, UserAction.SOMETHING_ELSE, "none", "Library", R.string.general_error);
        return true;
    }

    @Override
    protected void resetFragment() {

        super.resetFragment();

        if (disposables != null) disposables.clear();
    }

    private void showBannerAd() {
    }

    private void showNativeAd() {
    }

    @OnClick(R.id.action_search)
    void onSearch() {
        // open search
        NavigationHelper.openSearchFragment(getFM(), ServiceHelper.getSelectedServiceId(activity), "");
    }

    @OnClick(R.id.action_settings)
    void onSettings() {
        // open Settings
        NavigationHelper.openSettings(activity);
    }

    @OnClick(R.id.history)
    void onHistory() {
        // open history
        NavigationHelper.openHistoryFragment(getFM());
    }

    @OnClick(R.id.subscription)
    void onSubscription() {
        // open subscription
        NavigationHelper.openSubscriptionFragment(getFM());
    }

    @OnClick(R.id.download)
    void onDownload() {
        // open DownloadActivity
        NavigationHelper.openDownloads(activity);
    }

    @OnClick(R.id.create_new_playlist)
    void onCreateNewPlaylist() {
        if (getFM() != null) {
            PlaylistCreationDialog.newInstance().show(getFM(), LibraryFragment.class.getName());
        }
    }

    // Fragment LifeCycle - Destruction
    @Override
    public void onPause() {
//		if (adView != null) {
//			adView.pause();
//		}
        super.onPause();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        if (itemListAdapter != null) itemListAdapter.unsetSelectedListener();

        if (disposables != null) disposables.clear();
        if (databaseSubscription != null) databaseSubscription.cancel();

        databaseSubscription = null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (disposables != null) disposables.dispose();

        disposables = null;
        localPlaylistManager = null;
        remotePlaylistManager = null;
    }
}
