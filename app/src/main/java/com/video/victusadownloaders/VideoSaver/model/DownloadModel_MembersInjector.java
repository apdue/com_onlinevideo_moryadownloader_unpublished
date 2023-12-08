package com.video.victusadownloaders.VideoSaver.model;

import android.content.SharedPreferences;

import javax.inject.Provider;

import dagger.MembersInjector;
import retrofit2.Retrofit;

public final class DownloadModel_MembersInjector implements MembersInjector<DownloadModel> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Retrofit> retrofitProvider;
    private final Provider<SharedPreferences> sharedPreferencesProvider;

    public DownloadModel_MembersInjector(Provider<SharedPreferences> provider, Provider<Retrofit> provider2) {
        this.sharedPreferencesProvider = provider;
        this.retrofitProvider = provider2;
    }

    public static MembersInjector<DownloadModel> create(Provider<SharedPreferences> provider, Provider<Retrofit> provider2) {
        return new DownloadModel_MembersInjector(provider, provider2);
    }

    public void injectMembers(DownloadModel downloadModel) {
        if (downloadModel != null) {
            downloadModel.sharedPreferences = (SharedPreferences) this.sharedPreferencesProvider.get();
            downloadModel.retrofit = (Retrofit) this.retrofitProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }
}
