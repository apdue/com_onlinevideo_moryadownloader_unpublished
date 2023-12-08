package com.video.victusadownloaders.VideoSaver.app;

import android.os.Environment;

import java.io.File;


public class Constants {
    public static final String SAVED_MEDIA_PATH;
    public static final String WA_STATUSES_PATH;
    public static final String WHATSAPP_ALL_MEDIA_PATH;
    public static final String WHATSAPP_STATUSES_LOCATION;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("/VideoDownloader Statuses");
        sb.append(File.separator);
        sb.append("WhatsappStatus/");
        WA_STATUSES_PATH = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStorageDirectory().toString());
        sb2.append("/WhatsApp/Media/.Statuses");
        WHATSAPP_STATUSES_LOCATION = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(Environment.getExternalStorageDirectory().toString());
        sb3.append("/WhatsApp/Media/WhatsApp Images");
        WHATSAPP_ALL_MEDIA_PATH = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(Environment.getExternalStorageDirectory().toString());
        sb4.append("/AllVideoDownloads");
        SAVED_MEDIA_PATH = sb4.toString();
    }
}
