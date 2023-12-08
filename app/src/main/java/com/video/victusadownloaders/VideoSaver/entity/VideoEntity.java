package com.video.victusadownloaders.VideoSaver.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoEntity implements Parcelable {
    public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
        public VideoEntity createFromParcel(Parcel parcel) {
            return new VideoEntity(parcel);
        }

        public VideoEntity[] newArray(int i) {
            return new VideoEntity[i];
        }
    };
    private String thumbnailUrl;
    private String titleVideo;
    private String videoUrl;

    public int describeContents() {
        return 0;
    }

    public VideoEntity(Parcel parcel) {
        this.videoUrl = parcel.readString();
        this.thumbnailUrl = parcel.readString();
        this.titleVideo = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.videoUrl);
        parcel.writeString(this.thumbnailUrl);
        parcel.writeString(this.titleVideo);
    }
}
