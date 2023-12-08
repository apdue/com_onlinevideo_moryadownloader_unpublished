package com.video.victusadownloaders.VideoSaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadReceiverVideo extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        intent.getLongExtra("extra_download_id", -1);
    }
}
