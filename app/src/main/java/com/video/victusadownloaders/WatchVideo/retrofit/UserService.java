package com.video.victusadownloaders.WatchVideo.retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import com.video.victusadownloaders.WatchVideo.models.AdsModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {



    @GET(APIClient.APPEND_URL)
    Call<JsonObject> getAdids ();

    @GET("/sandeep")
    Call<List<AdsModel>> getads();

}
