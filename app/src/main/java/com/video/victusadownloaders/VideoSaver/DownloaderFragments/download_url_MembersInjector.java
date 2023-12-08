package com.video.victusadownloaders.VideoSaver.DownloaderFragments;

import android.content.SharedPreferences;

import javax.inject.Provider;

import dagger.MembersInjector;
import com.video.victusadownloaders.VideoSaver.presenter.VimeoPresenter;


public final class download_url_MembersInjector implements MembersInjector<download_url_fragment> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<SharedPreferences> sharedPreferencesProvider;
    private final Provider<VimeoPresenter> vimeoPresenterProvider;

    public download_url_MembersInjector(Provider<SharedPreferences> provider, Provider<VimeoPresenter> provider2) {
        this.sharedPreferencesProvider = provider;
        this.vimeoPresenterProvider = provider2;
    }

    public static MembersInjector<download_url_fragment> create(Provider<SharedPreferences> provider, Provider<VimeoPresenter> provider2) {
        return new download_url_MembersInjector(provider, provider2);
    }

    public void injectMembers(download_url_fragment download_url) {
        if (download_url != null) {
            download_url.sharedPreferences = (SharedPreferences) this.sharedPreferencesProvider.get();
            download_url.vimeoPresenter = (VimeoPresenter) this.vimeoPresenterProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }
}
