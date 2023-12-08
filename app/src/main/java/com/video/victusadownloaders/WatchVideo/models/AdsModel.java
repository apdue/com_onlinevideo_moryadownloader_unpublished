package com.video.victusadownloaders.WatchVideo.models;

import com.google.gson.annotations.SerializedName;

public class AdsModel {

    @SerializedName("Result")
    private String mResult;
    @SerializedName("config")
    private Config config;


    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }

    public Config getConfig() {
        return config;
    }

    public Config setConfig(Config config) {
        this.config = config;
        return config;
    }
}
