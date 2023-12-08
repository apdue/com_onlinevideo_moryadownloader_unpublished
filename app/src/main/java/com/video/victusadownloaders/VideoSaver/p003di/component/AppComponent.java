package com.video.victusadownloaders.VideoSaver.p003di.component;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;

/* renamed from: com.downloader.videodownloader.hdvideo.anyvideodownloader.di.component.AppComponent */
public interface AppComponent {
    Context context();

    Retrofit retrofit();

    SharedPreferences sharedPreferences();
}
