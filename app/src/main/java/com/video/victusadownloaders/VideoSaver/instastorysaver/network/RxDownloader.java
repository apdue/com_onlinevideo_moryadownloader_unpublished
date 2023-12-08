package com.video.victusadownloaders.VideoSaver.instastorysaver.network;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import androidx.collection.LongSparseArray;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RxDownloader {
    private static RxDownloader mRxDownloader;
    private Context mContext;
    private DownloadManager mDownloadManager;
    private LongSparseArray<PublishSubject<String>> mSubjectMap = new LongSparseArray();

    private class DownloadStatusReceiver extends BroadcastReceiver {
        private DownloadStatusReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            long longExtra = intent.getLongExtra("extra_download_id", 0);
            PublishSubject publishSubject = (PublishSubject) RxDownloader.this.mSubjectMap.get(longExtra);
            if (publishSubject != null) {
                Query query = new Query();
                query.setFilterById(new long[]{longExtra});
                Cursor query2 = RxDownloader.this.mDownloadManager.query(query);
                if (!query2.moveToFirst()) {
                    RxDownloader.this.mDownloadManager.remove(new long[]{longExtra});
                    publishSubject.onError(new IllegalStateException("Cursor empty, this shouldn't happened"));
                    RxDownloader.this.mSubjectMap.remove(longExtra);
                } else if (8 != query2.getInt(query2.getColumnIndex("status"))) {
                    RxDownloader.this.mDownloadManager.remove(new long[]{longExtra});
                    publishSubject.onError(new IllegalStateException("Download Failed"));
                    RxDownloader.this.mSubjectMap.remove(longExtra);
                } else {
                    publishSubject.onNext(query2.getString(query2.getColumnIndex("local_uri")));
                    publishSubject.onCompleted();
                    RxDownloader.this.mSubjectMap.remove(longExtra);
                    query2.close();
                }
            }
        }
    }

    public static RxDownloader getInstance(Context context) {
        if (mRxDownloader == null) {
            mRxDownloader = new RxDownloader(context.getApplicationContext());
        }
        return mRxDownloader;
    }

    private RxDownloader(Context context) {
        this.mContext = context;
        context.registerReceiver(new DownloadStatusReceiver(), new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
    }

    public Observable<String> download(String str, String str2, int i, String str3) {
        return download(str, str2, "*/*", i, str3);
    }

    public Observable<String> download(String str, String str2, String str3, int i, String str4) {
        return download(getDefaultRequest(str, str2, str3, i, str4));
    }

    public Observable<String> download(Request request) {
        if (this.mDownloadManager == null) {
            this.mDownloadManager = (DownloadManager) this.mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        long enqueue = this.mDownloadManager.enqueue(request);
        PublishSubject create = PublishSubject.create();
        this.mSubjectMap.put(enqueue, create);
        return create;
    }

    private Request getDefaultRequest(String str, String str2, String str3, int i, String str4) {
        Request request = new Request(Uri.parse(str));
        request.setDescription("Downloading file...");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(str4, str2);
        request.setNotificationVisibility(i);
        return request;
    }
}
