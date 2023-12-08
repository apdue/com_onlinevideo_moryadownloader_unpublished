package com.video.victusadownloaders.VideoSaver.p003di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.video.victusadownloaders.VideoSaver.instastorysaver.InstaStoryApplication;
import com.video.victusadownloaders.VideoSaver.model.DatabaseModel;

/* renamed from: com.downloader.videodownloader.hdvideo.anyvideodownloader.di.module.AppModule */
public class AppModule {
    private static InstaStoryApplication application;

    public AppModule(InstaStoryApplication videoApplication) {
        application = videoApplication;
    }

    public Context provideApplication() {
        return application;
    }

    public OkHttpClient provideOkHttpClient() {
        Builder builder = new Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(60000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    public Retrofit provideRetrofitBuilder(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl("https://vimeo.com").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(okHttpClient).build();
    }

    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    public DatabaseModel provideDatabaseModel() {
        return DatabaseModel.getInstance(application);
    }
}
