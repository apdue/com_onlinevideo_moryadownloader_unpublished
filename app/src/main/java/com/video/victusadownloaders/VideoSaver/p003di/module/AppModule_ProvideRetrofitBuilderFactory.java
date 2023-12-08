package com.video.victusadownloaders.VideoSaver.p003di.module;

import javax.inject.Provider;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/* renamed from: com.downloader.videodownloader.hdvideo.anyvideodownloader.di.module.AppModule_ProvideRetrofitBuilderFactory */
public final class AppModule_ProvideRetrofitBuilderFactory implements Factory<Retrofit> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;
    private final Provider<OkHttpClient> okHttpClientProvider;

    public AppModule_ProvideRetrofitBuilderFactory(AppModule appModule, Provider<OkHttpClient> provider) {
        this.module = appModule;
        this.okHttpClientProvider = provider;
    }

    public Retrofit get() {
        return (Retrofit) Preconditions.checkNotNull(this.module.provideRetrofitBuilder((OkHttpClient) this.okHttpClientProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Retrofit> create(AppModule appModule, Provider<OkHttpClient> provider) {
        return new AppModule_ProvideRetrofitBuilderFactory(appModule, provider);
    }
}
