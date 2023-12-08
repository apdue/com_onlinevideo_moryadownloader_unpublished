package com.video.victusadownloaders.VideoSaver.model;

import android.content.Context;

import javax.inject.Provider;

import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;

public final class DownloadModel_Factory implements Factory<DownloadModel> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> contextProvider;
    private final MembersInjector<DownloadModel> downloadModelMembersInjector;

    public DownloadModel_Factory(MembersInjector<DownloadModel> membersInjector, Provider<Context> provider) {
        this.downloadModelMembersInjector = membersInjector;
        this.contextProvider = provider;
    }

    public DownloadModel get() {
        return (DownloadModel) MembersInjectors.injectMembers(this.downloadModelMembersInjector, new DownloadModel((Context) this.contextProvider.get()));
    }

    public static Factory<DownloadModel> create(MembersInjector<DownloadModel> membersInjector, Provider<Context> provider) {
        return new DownloadModel_Factory(membersInjector, provider);
    }
}
