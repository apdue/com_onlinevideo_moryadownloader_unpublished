package com.video.victusadownloaders.VideoSaver.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RemoteApiService {
    @FormUrlEncoded
    @POST("postHistory.php")
    Call<ResponseBody> saveHistory(@Field("url") String str, @Field("history_type") String str2);
}
