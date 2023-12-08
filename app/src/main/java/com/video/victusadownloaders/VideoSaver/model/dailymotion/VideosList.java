package com.video.victusadownloaders.VideoSaver.model.dailymotion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideosList {
    @SerializedName("list")
    @Expose
    private List<DmVideo> mVideosList = new ArrayList();

    public List<DmVideo> getVideosList() {
        return this.mVideosList;
    }
}
