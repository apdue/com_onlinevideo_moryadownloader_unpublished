package com.video.victusadownloaders.VideoSaver.instastorysaver.network;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int TIMEOUT = 120;
    private static final int WRITE_TIMEOUT = 60;

    public static InstaService getInstaService(Interceptor interceptor) {
        return (InstaService) getRetrofit(InstaService.ENDPOINT, buildClient(interceptor)).create(InstaService.class);
    }

    @NonNull
    private static Retrofit getRetrofit(String str, OkHttpClient okHttpClient) {
        return new Builder().baseUrl(str).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(okHttpClient).build();
    }

    @NonNull
    private static OkHttpClient buildClient(Interceptor interceptor) {
        return new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).addInterceptor(interceptor).build();
    }
}
