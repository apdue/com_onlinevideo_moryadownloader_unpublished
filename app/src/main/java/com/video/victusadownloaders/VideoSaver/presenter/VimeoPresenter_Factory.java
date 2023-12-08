package com.video.victusadownloaders.VideoSaver.presenter;

import android.content.Context;

import javax.inject.Provider;

import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;

public final class VimeoPresenter_Factory implements Factory<VimeoPresenter> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> contextProvider;
    private final MembersInjector<VimeoPresenter> vimeoPresenterMembersInjector;

    public VimeoPresenter_Factory(MembersInjector<VimeoPresenter> membersInjector, Provider<Context> provider) {
        this.vimeoPresenterMembersInjector = membersInjector;
        this.contextProvider = provider;
    }

    public VimeoPresenter get() {
        return (VimeoPresenter) MembersInjectors.injectMembers(this.vimeoPresenterMembersInjector, new VimeoPresenter((Context) this.contextProvider.get()));
    }

    public static Factory<VimeoPresenter> create(MembersInjector<VimeoPresenter> membersInjector, Provider<Context> provider) {
        return new VimeoPresenter_Factory(membersInjector, provider);
    }
}
