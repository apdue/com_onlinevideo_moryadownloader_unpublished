package com.video.victusadownloaders.VideoSaver.model.HLS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("info")
    @Expose
    private Info info;

    public Info getInfo() {
        return this.info;
    }
}
