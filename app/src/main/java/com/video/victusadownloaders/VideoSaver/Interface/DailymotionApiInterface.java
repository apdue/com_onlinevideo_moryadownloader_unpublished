package com.video.victusadownloaders.VideoSaver.Interface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.ChannelsList;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.VideosList;

public interface DailymotionApiInterface {
    @GET("channels")
    Call<ChannelsList> getChannels(@QueryMap HashMap<String, String> hashMap);

    @GET("videos?flags=featured,hd,no_live&limit=100")
    Call<VideosList> getFeaturedVideos();

    @GET("videos?sort=trending&limit=100")
    Call<VideosList> getTrendingVideos();

    @GET("channel/videogames/videos?limit=100")
    Call<VideosList> getVideos(@QueryMap HashMap<String, String> hashMap);

    @GET("channel/{channel_id}/videos")
    Call<VideosList> getVideosByChannel(@Path("channel_id") String str, @QueryMap HashMap<String, String> hashMap);
}
