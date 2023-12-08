package com.video.victusadownloaders.VideoSaver.Interface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.video.victusadownloaders.VideoSaver.model.HLS.Example;

public interface VideoApiInterface {
    @GET("/api/info?")
    Call<Example> getUrl(@Query("url") String str);

   // topvideodownloader.html
}
