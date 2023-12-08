package com.video.victusadownloaders.VideoSaver.p003di.module;

import android.content.Context;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.downloader.videodownloader.hdvideo.anyvideodownloader.di.module.AppModule_ProvideApplicationFactory */
public final class AppModule_ProvideApplicationFactory implements Factory<Context> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideApplicationFactory(AppModule appModule) {
        this.module = appModule;
    }

    public Context get() {
        return (Context) Preconditions.checkNotNull(this.module.provideApplication(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Context> create(AppModule appModule) {
        return new AppModule_ProvideApplicationFactory(appModule);
    }
}
