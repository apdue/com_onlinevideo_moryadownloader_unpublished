package com.video.victusadownloaders.VideoSaver.Network;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static Retrofit retrofit1;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Builder().baseUrl("http://youtube-advance-dll.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitxzyInstance() {

        if (retrofit1 == null) {
            retrofit1 = new Builder().baseUrl("http://thevideostatus.xyz/topvideo.html/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit1;
    }

//    http://youtube-advance-dll.herokuapp.com
//    https://youtube-dll.herokuapp.com
}
