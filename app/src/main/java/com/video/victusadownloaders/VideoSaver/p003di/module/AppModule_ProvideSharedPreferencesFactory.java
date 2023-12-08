package com.video.victusadownloaders.VideoSaver.p003di.module;

import android.content.SharedPreferences;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideSharedPreferencesFactory implements Factory<SharedPreferences> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideSharedPreferencesFactory(AppModule appModule) {
        this.module = appModule;
    }

    public SharedPreferences get() {
        return (SharedPreferences) Preconditions.checkNotNull(this.module.provideSharedPreferences(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SharedPreferences> create(AppModule appModule) {
        return new AppModule_ProvideSharedPreferencesFactory(appModule);
    }
}
