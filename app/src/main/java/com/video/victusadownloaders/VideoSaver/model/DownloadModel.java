package com.video.victusadownloaders.VideoSaver.model;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.entity.VideoEntityJson;

public class DownloadModel {

    public static String DOWNLOAD_PATH = "/AllVideoDownloads/";
    private static DatabaseModel databaseModel;
    /* access modifiers changed from: private */
    public Context context;
    private long downloadId;
    public DownloadManager downloadManager;
    private String fieldResponse = "title,thumbnail_url,owner,views_total";
    Retrofit retrofit;
    SharedPreferences sharedPreferences;

    @SuppressLint("WrongConstant")
    public DownloadModel(Context context2) {
        this.context = context2;
        this.downloadManager = (DownloadManager) context2.getSystemService("download");
        databaseModel = DatabaseModel.getInstance(context2);
    }

    public void downloadVideo(String str, String str2, String str3) {
        Request request = new Request(Uri.parse(str));
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(DOWNLOAD_PATH, str2);
        this.downloadId = this.downloadManager.enqueue(request);
    }

    public Observable<List<VideoEntityJson>> getVideoList(final String str) {
        return Observable.fromCallable(new Callable<List<VideoEntityJson>>() {
            public List<VideoEntityJson> call() throws Exception {
                List<VideoEntityJson> list;
                Exception e;
                ExceptionInInitializerError e2;
                List<VideoEntityJson> arrayList = new ArrayList<>();
                try {
                    String element = Jsoup.connect(str).get().body().toString();
                    Matcher matcher = Pattern.compile("\"progressive\":(.*)\\}\\,\\\"lang\\\"").matcher(element);
                    Matcher matcher2 = Pattern.compile("\\\"title\\\"\\:\\\"(.*?)\\\"\\,").matcher(element);
                    Matcher matcher3 = Pattern.compile("\\\"base\\\"\\:\\\"(.*?)\\}").matcher(element);
                    if (matcher.find() && matcher2.find() && matcher3.find()) {
                        list = (List) new Gson().fromJson(matcher.group(1), new TypeToken<ArrayList<VideoEntityJson>>() {
                        }.getType());
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss");
                            StringBuilder sb = new StringBuilder();
                            sb.append("vimeo-");
                            sb.append(simpleDateFormat.format(new Date()));
                            sb.append(".mp4");
                            DownloadModel.this.sharedPreferences.edit().putString(DownloadModel.this.context.getString(R.string.video_title_key), sb.toString()).apply();
                            DownloadModel.this.sharedPreferences.edit().putString(DownloadModel.this.context.getString(R.string.video_thumbnail_key), matcher3.group(1)).apply();
                            arrayList = list;
                        } catch (Exception e3) {
                            e = e3;
                            e.printStackTrace();
                            return list;
                        } catch (ExceptionInInitializerError e4) {
                            e2 = e4;
                            e2.printStackTrace();
                            return list;
                        }
                    }
                    return arrayList;
                } catch (Exception e5) {
                    list = arrayList;
                    e = e5;
                    e.printStackTrace();
                    return list;
                } catch (ExceptionInInitializerError e6) {
                    list = arrayList;
                    e2 = e6;
                    e2.printStackTrace();
                    return list;
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
