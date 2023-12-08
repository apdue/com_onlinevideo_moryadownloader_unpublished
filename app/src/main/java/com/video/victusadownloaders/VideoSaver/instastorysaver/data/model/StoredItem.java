package com.video.victusadownloaders.VideoSaver.instastorysaver.data.model;

public class StoredItem {
    private String path;
    private String type;

    public StoredItem(String str, String str2) {
        this.path = str;
        this.type = str2;
    }

    public String getPath() {
        return this.path;
    }

    public String getType() {
        return this.type;
    }
}
