package com.video.victusadownloaders.VideoSaver.searchview;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchSuggWraper implements Parcelable {
    public static final Creator<SearchSuggWraper> CREATOR = new Creator<SearchSuggWraper>() {
        public SearchSuggWraper createFromParcel(Parcel parcel) {
            return new SearchSuggWraper(parcel);
        }

        public SearchSuggWraper[] newArray(int i) {
            return new SearchSuggWraper[i];
        }
    };
    @SerializedName("query")
    @Expose
    private String query;

    public int describeContents() {
        return 0;
    }

    private SearchSuggWraper(Parcel parcel) {
        this.query = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.query);
    }
}
