package com.video.victusadownloaders.VideoSaver.instastorysaver.data.model;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse extends BaseResponse {
    @SerializedName("user")
    private User user;

    public class User {
        @SerializedName("full_name")
        private String fullname;
        @SerializedName("has_anonymous_profile_picture")
        private Boolean hasAnonymousProfilePicture;
        @SerializedName("pk")
        private String pk;
        @SerializedName("profile_pic_url")
        private String profilePicUrl;
        @SerializedName("username")
        private String username;

        public String getUsername() {
            return this.username;
        }

        public String getPk() {
            return this.pk;
        }

        public Boolean getHasAnonymousProfilePicture() {
            return this.hasAnonymousProfilePicture;
        }

        public String getProfilePicUrl() {
            return this.profilePicUrl;
        }

        public String getFullname() {
            return this.fullname;
        }
    }

    public User getUser() {
        return this.user;
    }
}
