package com.video.victusadownloaders.VideoSaver.instastorysaver.network;


import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.SearchUserResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesMediaResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.StoriesTrayResponse;
import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.UserInfoResponse;

public interface InstaService {
    public static final String ENDPOINT = "https://i.instagram.com/api/v1/";

    @GET("feed/user/{user_id}/reel_media/")
    Observable<Response<StoriesMediaResponse>> getReelMedia(@Path("user_id") String str);

    @GET("feed/reels_tray/")
    Observable<Response<StoriesTrayResponse>> getReelsTray();

    @GET("users/{user_id}/info/")
    Observable<Response<UserInfoResponse>> getUserInfo(@Path("user_id") String str);

    @GET("users/search/")
    Observable<Response<SearchUserResponse>> search(@Query("q") String str, @Query("timezone_offset") String str2);

    @GET("users/{username}/usernameinfo/")
    Observable<Response<UserInfoResponse>> searchUsername(@Path("username") String str);
}
