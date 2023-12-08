package com.video.victusadownloaders.VideoSaver.model.HLS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Format {
    @SerializedName("format_id")
    @Expose
    private String formatId;
    @SerializedName("url")
    @Expose
    private String url;

    public String getFormatId() {
        return this.formatId;
    }

    public void setFormatId(String str) {
        this.formatId = str;
    }

    public String getUrl() {
        return this.url;
    }
}
