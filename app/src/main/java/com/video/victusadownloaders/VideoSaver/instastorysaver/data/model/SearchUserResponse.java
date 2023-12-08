package com.video.victusadownloaders.VideoSaver.instastorysaver.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserResponse extends BaseResponse {
    @SerializedName("has_more")
    private boolean hasMore;
    @SerializedName("num_results")
    private int numResults;
    @SerializedName("users")
    private List<UserInfoResponse.User> users;

    public boolean isHasMore() {
        return this.hasMore;
    }

    public int getNumResults() {
        return this.numResults;
    }

    public List<UserInfoResponse.User> getUsers() {
        return this.users;
    }
}
