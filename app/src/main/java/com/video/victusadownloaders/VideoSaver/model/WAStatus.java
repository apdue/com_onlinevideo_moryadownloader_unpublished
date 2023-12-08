package com.video.victusadownloaders.VideoSaver.model;

import java.io.Serializable;

public class WAStatus implements Serializable {
    private String mDate = "";
    private String mDestPath = "";
    private String mFileName = "";
    private String mFilePath = "";
    private String mFileSize = "";
    private String mStatusType = "";

    public String getDestPath() {
        return this.mDestPath;
    }

    public void setDestPath(String str) {
        this.mDestPath = str;
    }

    public String getFileSize() {
        return this.mFileSize;
    }

    public void setFileSize(String str) {
        this.mFileSize = str;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String str) {
        this.mFilePath = str;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void setFileName(String str) {
        this.mFileName = str;
    }

    public void setDate(String str) {
        this.mDate = str;
    }

    public String getStatusType() {
        return this.mStatusType;
    }

    public void setStatusType(String str) {
        this.mStatusType = str;
    }
}
