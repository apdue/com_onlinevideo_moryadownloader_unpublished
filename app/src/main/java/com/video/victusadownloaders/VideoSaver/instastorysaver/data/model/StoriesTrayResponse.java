package com.video.victusadownloaders.VideoSaver.instastorysaver.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoriesTrayResponse extends BaseResponse {
    @SerializedName("tray")
    private List<Tray> tray;

    public class Tray {
        @SerializedName("id")
        private String id;
        @SerializedName("items")
        private List<Item> items;
        @SerializedName("user")
        private User user;

        public class Item {
            @SerializedName("client_cache_key")
            public String clientCacheKey;
            @SerializedName("code")
            public String code;
            @SerializedName("id")
            private String id;
            @SerializedName("media_type")
            private Integer mediaType;
            @SerializedName("organic_tracking_token")
            public String organicTrackingToken;
            @SerializedName("pk")
            private String pk;

            public String getPk() {
                return this.pk;
            }

            public String getId() {
                return this.id;
            }

            public Integer getMediaType() {
                return this.mediaType;
            }

            public String getCode() {
                return this.code;
            }

            public String getClientCacheKey() {
                return this.clientCacheKey;
            }

            public String getOrganicTrackingToken() {
                return this.organicTrackingToken;
            }
        }

        public class User {
            @SerializedName("full_name")
            private String fullname;
            @SerializedName("pk")
            private String pk;
            @SerializedName("profile_pic_id")
            private String profilePicId;
            @SerializedName("profile_pic_url")
            private String profilePicUrl;
            @SerializedName("username")
            private String username;

            public String getUsername() {
                return this.username;
            }

            public String getProfilePicUrl() {
                return this.profilePicUrl;
            }

            public String getProfilePicId() {
                return this.profilePicId;
            }

            public String getFullname() {
                return this.fullname;
            }

            public String getPk() {
                return this.pk;
            }
        }

        public String getId() {
            return this.id;
        }

        public User getUser() {
            return this.user;
        }

        public List<Item> getItems() {
            return this.items;
        }
    }

    public List<Tray> getTray() {
        return this.tray;
    }
}
