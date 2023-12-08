package com.video.victusadownloaders.VideoSaver;

import android.content.SharedPreferences;

import javax.inject.Provider;

import dagger.MembersInjector;
import com.video.victusadownloaders.VideoSaver.presenter.VimeoPresenter;

public final class BrowserActivity_MembersInjector implements MembersInjector<BrowserVideoActivityVideo> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<SharedPreferences> sharedPreferencesProvider;
    private final Provider<VimeoPresenter> vimeoPresenterProvider;

    public BrowserActivity_MembersInjector(Provider<VimeoPresenter> provider, Provider<SharedPreferences> provider2) {
        this.vimeoPresenterProvider = provider;
        this.sharedPreferencesProvider = provider2;
    }

    public static MembersInjector<BrowserVideoActivityVideo> create(Provider<VimeoPresenter> provider, Provider<SharedPreferences> provider2) {
        return new BrowserActivity_MembersInjector(provider, provider2);
    }

    public void injectMembers(BrowserVideoActivityVideo browserActivity) {
        if (browserActivity != null) {
            browserActivity.vimeoPresenter = (VimeoPresenter) this.vimeoPresenterProvider.get();
            browserActivity.sharedPreferences = (SharedPreferences) this.sharedPreferencesProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }
}
