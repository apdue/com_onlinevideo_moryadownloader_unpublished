package com.video.victusadownloaders.VideoSaver.instastorysaver.data.model;

public class LocalDbUser {
    private String USERNAME;
    private String USER_PK;
    private long _ID;

    public LocalDbUser(long j, String str, String str2) {
        this._ID = j;
        this.USERNAME = str;
        this.USER_PK = str2;
    }

    public long get_ID() {
        return this._ID;
    }

    public String getUSER_PK() {
        return this.USER_PK;
    }

    public String getUSERNAME() {
        return this.USERNAME;
    }
}
