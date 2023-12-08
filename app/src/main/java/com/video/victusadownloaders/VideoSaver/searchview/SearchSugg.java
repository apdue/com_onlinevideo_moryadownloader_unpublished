package com.video.victusadownloaders.VideoSaver.searchview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchSugg {
    private boolean mIsHistory = false;
    @SerializedName("query")
    @Expose
    private String mQuery;

    public SearchSugg(String str) {
        this.mQuery = str.toLowerCase();
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    public String getQuery() {
        return this.mQuery;
    }
}
