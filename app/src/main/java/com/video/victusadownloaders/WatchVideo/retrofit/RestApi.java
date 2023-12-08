package com.video.victusadownloaders.WatchVideo.retrofit;

import com.video.victusadownloaders.WatchVideo.fragments.discover.model.VideoListResponse;
import com.video.victusadownloaders.WatchVideo.models.request.explore.ExploreRequest;
import com.video.victusadownloaders.WatchVideo.models.response.explore.ExResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface RestApi {

    @GET("videos?part=snippet,contentDetails,statistics&chart=mostPopular")
    Observable<VideoListResponse> getVideosByCategory(@Query("key") String key,
                                                      @Query("regionCode") String regionCode,
                                                      @Query("videoCategoryId") int videoCategoryId,
                                                      @Query("maxResults") int maxResults);

    @POST("youtubei/v1/browse?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
    Observable<ExResponse> explore(@Body ExploreRequest request);
}
