package com.video.victusadownloaders.VideoSaver.model.dailymotion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DmVideo implements Serializable {
    @SerializedName("country")
    @Expose
    private String mCountry = "";
    @SerializedName("id")
    @Expose
    private String mId;
    private int mItemVieType = 4;
    @SerializedName("language")
    @Expose
    private String mLanguage = "";
    @SerializedName("likes_total")
    @Expose
    private double mLikesTotal;
    @SerializedName("thumbnail_180_url")
    @Expose
    private String mThumbnail_480_url = "";
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("views_total")
    @Expose
    private double mViewsTotal;

    public int getItemVieType() {
        return this.mItemVieType;
    }

    public void setItemVieType(int i) {
        this.mItemVieType = i;
    }

    public String getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getThumbnail_480_url() {
        return this.mThumbnail_480_url;
    }

    public double getLikesTotal() {
        return this.mLikesTotal;
    }

    public double getViewsTotal() {
        return this.mViewsTotal;
    }
}
