package com.video.victusadownloaders.VideoSaver.model.HLS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Info {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("formats")
    @Expose
    private List<Format> formats;

    public String getDescription() {
        return this.description;
    }

    public List<Format> getFormats() {
        return this.formats;
    }
}
