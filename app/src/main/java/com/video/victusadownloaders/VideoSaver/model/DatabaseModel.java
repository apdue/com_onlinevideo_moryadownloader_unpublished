package com.video.victusadownloaders.VideoSaver.model;

import android.content.Context;

import com.video.victusadownloaders.VideoSaver.entity.DaoMaster.DevOpenHelper;

public class DatabaseModel {
    private static DatabaseModel instance;
    private DevOpenHelper openHelper;

    public static DatabaseModel getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseModel.class) {
                if (instance == null) {
                    instance = new DatabaseModel(context);
                }
            }
        }
        return instance;
    }

    private DatabaseModel(Context context) {
        this.openHelper = new DevOpenHelper(context, "downtube_video", null);
    }
}
