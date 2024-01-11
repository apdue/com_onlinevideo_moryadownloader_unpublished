package com.video.victusadownloaders.VideoSaver.Download_HLS;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

import io.awesome.gagtube.R;

public class DownloadService extends Service implements DownloadTask.DownloadingListener {
    static int sTaskId;
    String chanelId = "com.downloader.videodownloader.hdvideo.anyvideodownloader";
    HashMap<String, DownloadTask> mTaskMap = new HashMap<>();
    Notification notification;
    Builder notificationBuilder;
    Integer notificationID = Integer.valueOf(100);
    NotificationManager notificationManager;
    File path;
    String title;
    String url;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(this.chanelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }
        startForeground(this.notificationID.intValue(), createNotification());
    }

    private Notification createNotification() {
        if (VERSION.SDK_INT >= 26) {
            this.notificationBuilder = new Builder(getApplicationContext(), this.chanelId);
        } else {
            this.notificationBuilder = new Builder(getApplicationContext());
        }
        this.notificationBuilder.setOngoing(true).setSmallIcon(R.drawable.download_icon_view_rand).setContentTitle("Downloading...").setContentText("Downloading Start").setPriority(Notification.PRIORITY_HIGH).setProgress(100, 0, false);
        return this.notificationBuilder.build();
    }

    private void createCompleteNotification(String str) {
        Builder builder;
        if (VERSION.SDK_INT >= 26) {
            builder = new Builder(getApplicationContext(), this.chanelId);
        } else {
            builder = new Builder(getApplicationContext());
        }
        Builder smallIcon = builder.setSmallIcon(R.drawable.download_icon_view_rand);
        StringBuilder sb = new StringBuilder();
        sb.append(this.title);
        sb.append(".mp4");
        smallIcon.setContentTitle(sb.toString()).setContentText(str).setPriority(Notification.PRIORITY_HIGH);
        this.notificationManager.notify(200, builder.build());
    }

    public void updateNotification(int i, int i2, String str) {
        this.notificationBuilder.setProgress(i2, i, false);
        Builder builder = this.notificationBuilder;
        StringBuilder sb = new StringBuilder();
        sb.append(this.title);
        sb.append(".mp4");
        builder.setContentTitle(sb.toString());
        this.notificationBuilder.setContentText(str);
        this.notification = this.notificationBuilder.build();
        this.notificationManager.notify(this.notificationID.intValue(), this.notification);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            this.url = intent.getStringExtra("URL");
            this.title = intent.getStringExtra("TITLE");
            this.path = new File(intent.getStringExtra("PATH"));
            String str = this.url;
            if (!(str == null || this.title == null || this.path == null || ((DownloadTask) this.mTaskMap.get(str)) != null)) {
                int i3 = sTaskId;
                sTaskId = i3 + 1;
                DownloadTask downloadTask = new DownloadTask(this, i3, this.url, this.title, this.path, this);
                this.mTaskMap.put(this.url, downloadTask);
                downloadTask.execute(new Object[0]);
            }
            stopOrContinue();
        }
        return 1;
    }

    /* access modifiers changed from: 0000 */
    public void stopOrContinue() {
        if (this.mTaskMap.size() == 0) {
            stopSelf();
        }
    }

    public void onDownloadingStart() {
        Log.d("DownloadingCheck", "onDownloadingStart: ");
    }

    public void onDownloadingProgress(int i, int i2) {
        int i3 = (i * 100) / i2;
        StringBuilder sb = new StringBuilder();
        sb.append("Downloading progress ");
        sb.append(i3);
        sb.append("%");
        updateNotification(i, i2, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onDownloadingProgress: ");
        sb2.append(i);
        sb2.append(" ");
        sb2.append(i2);
        Log.d("DownloadingCheck", sb2.toString());
    }

    public void onDownloadingFinish() {
        Log.d("DownloadingCheck", "onDownloadingFinish: ");
        createCompleteNotification("Downloading Completed");
        this.mTaskMap.remove(this.url);
        stopOrContinue();
    }

    public void onDownloadingCancel() {
        Log.e("DownloadingCheck", "onDownloadingCancel: ");
        createCompleteNotification("Downloading Canceled");
        this.mTaskMap.remove(this.url);
        stopOrContinue();
    }

    public void onDownloadingFailed(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDownloadingFailed: ");
        sb.append(str);
        Log.e("DownloadingCheck", sb.toString());
        createCompleteNotification("Downloading Failed");
        this.mTaskMap.remove(this.url);
        stopOrContinue();
    }
}
