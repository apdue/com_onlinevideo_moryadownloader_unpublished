package com.video.victusadownloaders.WatchVideo.fragments.list.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.WatchVideo.fragments.list.BaseListInfoFragment;
import com.video.victusadownloaders.WatchVideo.report.UserAction;
import com.video.victusadownloaders.WatchVideo.util.AnimationUtils;
import com.video.victusadownloaders.WatchVideo.util.ExtractorHelper;
import com.video.victusadownloaders.WatchVideo.util.NavigationHelper;
import com.video.victusadownloaders.WatchVideo.util.ServiceHelper;
import com.video.victusadownloaders.utilities.NativeAdNew;


import org.jetbrains.annotations.NotNull;
import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.ServiceList;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskInfo;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.awesome.gagtube.R;
import io.reactivex.Single;

public class TrendingFragment extends BaseListInfoFragment<KioskInfo> {

    PrefManagerVideo prf;

    @NonNull
    public static TrendingFragment getInstance(int serviceId) {
        try {
            return getInstance(serviceId, NewPipe.getService(serviceId).getKioskList().getDefaultKioskId());
        } catch (ExtractionException e) {
            return new TrendingFragment();
        }
    }

    @NonNull
    public static TrendingFragment getInstance(int serviceId, String kioskId) {
        try {
            TrendingFragment instance = new TrendingFragment();
            StreamingService service = NewPipe.getService(serviceId);

            ListLinkHandlerFactory kioskLinkHandlerFactory = service.getKioskList().getListLinkHandlerFactoryByType(kioskId);
            instance.setInitialData(serviceId, kioskLinkHandlerFactory.fromId(kioskId).getUrl(), kioskId);

            return instance;
        } catch (ExtractionException e) {
            return new TrendingFragment();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getString(R.string.trending);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        NativeAdNew.showMediumNative(getActivity(), view.findViewById(R.id.nativeAd));

        ButterKnife.bind(this, view);
        prf = new PrefManagerVideo(getActivity());
        if (!prf.getBoolean(SplashActivityScr.TAG_YOUTUBE_SETTING_ENABLED)) {
            ImageButton imageButton = view.findViewById(R.id.action_settings);
            imageButton.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        super.initViews(rootView, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        showNativeAd();
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    // Load and handle
    @Override
    public Single<KioskInfo> loadResult(boolean forceReload) {

        return ExtractorHelper.getKioskInfo(serviceId, url, forceReload);
    }

    @Override
    public Single<ListExtractor.InfoItemsPage> loadMoreItemsLogic() {

        return ExtractorHelper.getMoreKioskItems(serviceId, url, currentNextPage);
    }

    // Contract
    @Override
    public void showLoading() {

        super.showLoading();

        AnimationUtils.animateView(itemsList, false, 100);
    }

    @Override
    public void handleResult(@NonNull final KioskInfo result) {
        super.handleResult(result);
        if (!result.getErrors().isEmpty()) {
            showSnackBarError(result.getErrors(), UserAction.REQUESTED_MAIN_CONTENT, ServiceList.YouTube.getServiceInfo().getName(), result.getUrl(), 0);
        }
    }

    @Override
    public void handleNextItems(ListExtractor.InfoItemsPage result) {
        super.handleNextItems(result);
        if (!result.getErrors().isEmpty()) {
            showSnackBarError(result.getErrors(), UserAction.REQUESTED_PLAYLIST, ServiceList.YouTube.getServiceInfo().getName(), "Get next page of: " + url, 0);
        }
    }

    private void showNativeAd() {
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
    public void onDestroy() {
        super.onDestroy();
    }
}
