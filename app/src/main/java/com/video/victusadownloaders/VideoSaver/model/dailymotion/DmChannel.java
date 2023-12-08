package com.video.victusadownloaders.VideoSaver.model.dailymotion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DmChannel implements Serializable {
    @SerializedName("id")
    @Expose
    private String mId = "";
    @SerializedName("name")
    @Expose
    private String mName = "";

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }
}
