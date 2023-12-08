package com.video.victusadownloaders.VideoSaver.model.dailymotion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChannelsList {
    @SerializedName("list")
    @Expose
    private List<DmChannel> mChannelsList = new ArrayList();

    public List<DmChannel> getChannelsList() {
        return this.mChannelsList;
    }
}
