package com.video.victusadownloaders.VideoSaver.downloaderactivities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.video.victusadownloaders.PermissionUtil;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.VideoInfo;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.BaseActivity;
import com.video.victusadownloaders.VideoSaver.Download_HLS.DownloadService;
import com.video.victusadownloaders.VideoSaver.Interface.VideoApiInterface;
import com.video.victusadownloaders.VideoSaver.Network.RetrofitClientInstance;
import com.video.victusadownloaders.VideoSaver.VideosViewDownActivityVideo;
import com.video.victusadownloaders.VideoSaver.VimeoView;
import com.video.victusadownloaders.VideoSaver.entity.VideoEntityJson;
import com.video.victusadownloaders.VideoSaver.model.HLS.Example;
import com.video.victusadownloaders.VideoSaver.model.HLS.Format;
import com.video.victusadownloaders.VideoSaver.presenter.VimeoPresenter;
import com.video.victusadownloaders.VideoSaver.utils.BSUtils_D;
import com.video.victusadownloaders.VideoSaver.utils.DialogUtils;
import com.video.victusadownloaders.VideoSaver.utils.UtilMethods;

public class Download_Auto extends BaseActivity implements OnClickListener, VimeoView {
    String TAG = "Refrance Tag";
    AlertDialog alertDialog;
    VideoApiInterface apiInterface;
    Builder dialogBuilder;
    private String downloadUrl = "";
    private Context mContext;
    String messege;
    String patternInsta = "https://www.instagram.com/p/.";
    ProgressBar progressBarDialoge;
    private ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String trimUrl;
    private boolean type;
    VimeoPresenter vimeoPresenter1;
    WebView webView;


    public class DownloadFileFromURL extends AsyncTask<String, String, String> {
        public DownloadFileFromURL() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strArr) {
            URL url;
            Download_Auto.this.type = false;
            try {
                String document = Jsoup.connect(strArr[0]).get().toString();
                int indexOf = document.indexOf("\"", document.indexOf("\"video_url\"") + 11) + 1;
                String substring = document.substring(indexOf, document.indexOf("\"", indexOf));
                if (substring.equalsIgnoreCase("en")) {
                    int indexOf2 = document.indexOf("\"", document.indexOf("display_url") + 13) + 1;
                    String substring2 = document.substring(indexOf2, document.indexOf("\"", indexOf2));
                    Download_Auto.this.type = false;
                    url = new URL(substring2);
                } else {
                    url = new URL(substring);
                    Download_Auto.this.type = true;
                }
                return url.toString();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                return null;
            }
        }

        protected void onPostExecute(String str) {
            Download_Auto.this.downloadVideo(str);
        }
    }

    public void onClick(View view) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Fabric.with(this, new Twitter(new TwitterAuthConfig("K0w8rlDCB6zBB739TGt1BLY2n", "3dk9oqc7CQoI90fCyk9JcZEvS88bvkP1YHxI3ylyorl1cNaD5H")));
        this.apiInterface = (VideoApiInterface) RetrofitClientInstance.getRetrofitInstance().create(VideoApiInterface.class);
        this.messege = getIntent().getStringExtra("Extra");
        downloadBtnClicked();
        this.mContext = getContext();
    }

    public Context getContext() {
        this.mContext = this;
        return this.mContext;
    }

    private void downloadBtnClicked() {
        if (!storageAllowed()) {
            PermissionUtil.requestAllStoragePermissions(this,123);
        } else {
            String trim = this.messege.toString().trim();
            Long tweetId = getTweetId(this.messege.toString().trim());
            if (trim.contains("facebook.com")) {
                DOwnloadFromFb(trim);
            } else if (trim.contains("vimeo.com")) {
                DOwnloadFromVimeo(trim);
            } else if (trim.contains("dailymotion.com")) {
                DownloadFronDM(trim);
            } else if (trim.contains("twitter.com")) {
                if (trim.length() > 0 && trim.toString().contains("twitter.com/")) {
                    if (trim.length() > 0) {
                        trim.toString().trim();
                    } else {
                        trim = String.valueOf(tweetId);
                    }
                    if (tweetId != null) {
                        getTweet(tweetId, trim);
                    }
                }
            } else if (trim.contains("https://www.instagram.com/p")) {
                DownloadFronInsta(trim);
            } else if (trim.contains("youtu.be")) {
                showYoutubeDialog();
            } else {
                showToast("Invalid url.");
            }
        }
    }

    /* access modifiers changed from: private */
    public void showYoutubeDialog() {
        final Dialog dialog = new Dialog(getContext(), 2131820891);
        dialog.setContentView(R.layout.dialog_youtube);
        ((Button) dialog.findViewById(R.id.btnOkDialogYoutube)).setOnClickListener(new OnClickListener() {
//            private final /* synthetic */ Dialog f$0;
//
//            {
//                this.f$0 = r1;
//            }

            public final void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    @SuppressLint({"JavascriptInterface"})
    public void DOwnloadFromFb(String str) {
        hideKeyboard(this);
        this.dialogBuilder = new Builder(this);
        hideKeyboard(this);
        View inflate = getLayoutInflater().inflate(R.layout.fb_preview_layout, null);
        this.dialogBuilder.setView(inflate);
        this.webView = (WebView) inflate.findViewById(R.id.web_view);
        ((ImageView) inflate.findViewById(R.id.cancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Download_Auto.this.alertDialog.cancel();
                Download_Auto.this.finish();
            }
        });
        this.progressBarDialoge = (ProgressBar) inflate.findViewById(R.id.progressBar);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setPluginState(PluginState.ON);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().supportZoom();
        this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        this.webView.addJavascriptInterface(this, "FBDownloader");
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            }

            public void onPageFinished(WebView webView, String str) {
                Download_Auto.this.webView.loadUrl("javascript:(function() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\");');}}})()");
            }

            public void onLoadResource(WebView webView, String str) {
                Download_Auto.this.progressBarDialoge.setProgress(Download_Auto.this.webView.getProgress());
                Download_Auto.this.webView.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;console.log(i);var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');}}})()");
            }
        });
        hideKeyboard(this);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();
        this.webView.loadUrl(str);
        this.dialogBuilder.setCancelable(false);
        this.dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        this.alertDialog = this.dialogBuilder.create();
        hideKeyboard(this);
        this.alertDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                Download_Auto.this.webView.destroy();
                Download_Auto.this.finish();
            }
        });
        this.alertDialog.show();
        hideKeyboard(this);
    }

    @JavascriptInterface
    public void processVideo(String str, String str2) {
        try {
            showDownloadDialog(str2, str);
        } catch (Exception unused) {
            showToast("Download Failed");
        }
    }

    /* access modifiers changed from: private */
    public void DOwnloadFromVimeo(String str) {
        if (str.substring(18) == null || str.substring(18).isEmpty()) {
            showToast("Invalid url!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://player.vimeo.com/video/");
        sb.append(str.substring(18));
        String sb2 = sb.toString();
        showToast("Loading...");
        this.vimeoPresenter1.getVideoList(sb2);
    }

    public void setVideoList(final List<VideoEntityJson> list) {
        final String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strArr[i] = ((VideoEntityJson) list.get(i)).quality;
        }
        new MaterialDialog.Builder(this).title(this.sharedPreferences.getString(getString(R.string.video_title_key), "")).items(strArr).itemsCallbackSingleChoice(-1, new ListCallbackSingleChoice() {
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
//                String[] strArr = strArr;
                if (strArr.length == 0) {
                    Download_Auto.this.showToast("can't download.");
                    return false;
                }
                String str = (String) Arrays.asList(strArr).get(i);
                for (VideoEntityJson videoEntityJson : list) {
                    if (str.equals(videoEntityJson.quality)) {
                        VimeoPresenter vimeoPresenter = Download_Auto.this.vimeoPresenter1;
                        String str2 = videoEntityJson.url;
                        StringBuilder sb = new StringBuilder();
                        sb.append(Download_Auto.this.sharedPreferences.getString(Download_Auto.this.getString(R.string.video_title_key), ""));
                        sb.append(".mp4");
                        vimeoPresenter.downloadVideo(str2, sb.toString(), Download_Auto.this.sharedPreferences.getString(Download_Auto.this.getString(R.string.video_thumbnail_key), ""));
                        //Download_Auto.this.loadInterstitialAd();
                    }
                }
                return true;
            }
        }).positiveText((int) R.string.download_action).negativeText((int) R.string.cancel_action).show();
    }

    public void DownloadFronDM(String str) {
        if (!str.contains("video")) {
            showToast("invalid url.");
            return;
        }
        if (str.contains("playlist")) {
            this.trimUrl = str.substring(0, str.indexOf("?"));
            str = this.trimUrl;
        }
        DialogUtils.showSimpleProgressDialog(this);
        this.apiInterface.getUrl(str).enqueue(new Callback<Example>() {
            public void onResponse(Call<Example> call, Response<Example> response) {
                DialogUtils.closeProgressDialog();
                if (response != null) {
                    Example example = (Example) response.body();
                    if (example != null) {
                        Download_Auto.this.manageResponse(example);
                    } else {
                        Toast.makeText(Download_Auto.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Download_Auto.this, "Check Internet Connection ", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<Example> call, Throwable th) {
                DialogUtils.closeProgressDialog();
                Log.i("Video ", th.getMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void manageResponse(Example example) {
        if (example != null) {
            List<Format> formats = example.getInfo().getFormats();
            ArrayList arrayList = new ArrayList();
            for (Format format : formats) {
                String formatId = format.getFormatId();
                if (formatId.contains("hls-144-0")) {
                    format.setFormatId("144");
                    arrayList.add(format);
                } else if (formatId.contains("hls-240-0")) {
                    format.setFormatId("240");
                    arrayList.add(format);
                } else if (formatId.contains("hls-380-0")) {
                    format.setFormatId("380");
                    arrayList.add(format);
                } else if (formatId.contains("hls-480-0")) {
                    format.setFormatId("480");
                    arrayList.add(format);
                } else if (formatId.contains("hls-720-0")) {
                    format.setFormatId("720");
                    arrayList.add(format);
                }
            }
            showDM_DownloadSheet(arrayList, example.getInfo().getDescription());
            return;
        }
        Toast.makeText(this.mContext, "SomeThing Wrong Try Again ", Toast.LENGTH_SHORT).show();
    }

    private void showDM_DownloadSheet(List<Format> list, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        StringBuilder sb123 = new StringBuilder();
        sb123.append("Video_");
        sb123.append(simpleDateFormat.format(new Date()));
        final String sb12 = sb123.toString();
        new BSUtils_D(getContext()).showListSheet(str, "", list, this.TAG, new BSUtils_D.BSCallBack() {
            public void onBsHidden() {
            }

            public void onDownloadClicked(Format format) {
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append(File.separator);
                sb.append("AllVideoDownloads");
                sb.append(File.separator);
                String sb2 = sb.toString();
                Intent intent = new Intent(Download_Auto.this.getContext(), DownloadService.class);
                new UtilMethods(Download_Auto.this).showCustomToast("Download Started...", 1);
                intent.putExtra("URL", format.getUrl());
                intent.putExtra("TITLE", sb12);
                intent.putExtra("PATH", sb2);
                if (VERSION.SDK_INT >= 26) {
                    Download_Auto.this.mContext.startForegroundService(intent);
                } else {
                    Download_Auto.this.mContext.startService(intent);
                }
                Intent intent2 = new Intent(Download_Auto.this, VideosViewDownActivityVideo.class);
                intent2.setFlags(335577088);
                Download_Auto.this.startActivity(intent2);
            }
        });
    }

    /* access modifiers changed from: private */
    public void downloadVideo(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append(File.separator);
        sb.append("AllVideoDownloads");
        sb.append(File.separator);
        String sb2 = sb.toString();
        if (!new File(sb2).exists()) {
            new File(sb2).mkdir();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        StringBuilder sb3 = new StringBuilder();
        sb3.append("file://");
        sb3.append(sb2);
        sb3.append("/Video_");
        sb3.append(simpleDateFormat.format(new Date()));
        sb3.append(".mp4");
        String sb4 = sb3.toString();
        if (str == null || TextUtils.isEmpty(str)) {
            showToast("Download url not found!");
            return;
        }
        Uri parse = Uri.parse(str.trim());
        if (parse == null) {
            showToast("Download url not found!");
            return;
        }
        Request request = new Request(parse);
        request.setDestinationUri(Uri.parse(sb4));
        request.setNotificationVisibility(1);
        ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        startActivity(new Intent(this, VideosViewDownActivityVideo.class));
        new UtilMethods(getContext()).showCustomToast("Download Started...", 1);
    }

    /* access modifiers changed from: private */
    public void showToast(String str) {
        try {
            if (getContext() != null) {
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private boolean storageAllowed() {
//        boolean z = false;
//        if (VERSION.SDK_INT < 23) {
//            return false;
//        }
//        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//            z = true;
//        }
//        return z;
        return PermissionUtil.isAllStoragePermissionsGranted(this);
    }

    /* access modifiers changed from: private */
    public void DownloadFronInsta(final String str) {
        if (str.length() <= 27 || !str.contains("https://www.instagram.com/p/")) {
            showToast("Invalid url.");
            return;
        }
        if (!storageAllowed()) {
            PermissionUtil.requestAllStoragePermissions(this,123);
        } else if (checkURL(str)) {
            new DownloadFileFromURL().execute(new String[]{str});
            Intent intent = new Intent(this, VideosViewDownActivityVideo.class);
            intent.setFlags(335577088);
            startActivity(intent);
            finish();
        } else {
            showToast("Wrong URL");
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean checkURL(String str) {
        Matcher matcher = Pattern.compile(this.patternInsta).matcher(str);
        if (matcher.find()) {
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("Found value: ");
            sb.append(matcher.group(0));
            printStream.println(sb.toString());
            return true;
        }
        System.out.println("NO MATCH");
        return false;
    }

    /* access modifiers changed from: private */
    public void downloadVideo(final String str, final String str2) {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setIndeterminate(true);
        if (str2.endsWith(".mp4")) {
            this.progressDialog.setMessage("Fetching video...");
        } else {
            this.progressDialog.setMessage("Fetching gif...");
        }
        if (!storageAllowed()) {
            this.progressDialog.dismiss();
            PermissionUtil.requestAllStoragePermissions(this,123);
            // showToast("Kindly grant the request and try again");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory());
            sb.append(File.separator);
            sb.append("AllVideoDownloads");
            sb.append(File.separator);
            String sb2 = sb.toString();
            if (!new File(sb2).exists()) {
                new File(sb2).mkdir();
            }
            String str3 = this.downloadUrl;
            if (str3 == null || TextUtils.isEmpty(str3)) {
                showToast("Download url not found!");
                return;
            }
            Uri parse = Uri.parse(str.trim());
            if (parse == null) {
                showToast("Download url not found!");
                return;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("file://");
            sb3.append(sb2);
            sb3.append("/");
            sb3.append(str2);
            String sb4 = sb3.toString();
            Request request = new Request(parse);
            request.setDestinationUri(Uri.parse(sb4));
            request.setNotificationVisibility(1);
            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
            this.progressDialog.dismiss();
         //   loadInterstitialAd();
            Intent intent = new Intent(this, VideosViewDownActivityVideo.class);
            intent.setFlags(335577088);
            startActivity(intent);
            finish();
        }
    }

//    public void loadInterstitialAd() {
//        AdsUtil.getInstance(getContext()).loadInterstitialAd("", "ad_download_button", new AdsCallBack() {
//            public void onAdFailedToLoad(String str, String str2) {
//            }
//        });
//    }

    private void showDownloadDialog(final String str, final String str2) throws Exception {
        if (!storageAllowed()) {
            PermissionUtil.requestAllStoragePermissions(this,123);
            return;
        }
        Builder builder = new Builder(this);
        builder.setTitle("Download Video?");
        builder.setMessage("Click download to start downloading.");
        builder.setCancelable(false).setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Download_Auto.this.alertDialog.cancel();
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append(File.separator);
                sb.append("AllVideoDownloads");
                sb.append(File.separator);
                String sb2 = sb.toString();
                if (!new File(sb2).exists()) {
                    new File(sb2).mkdir();
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("file://");
                sb3.append(sb2);
                sb3.append("/");
                sb3.append(str);
                sb3.append(".mp4");
                String sb4 = sb3.toString();
                Request request = new Request(Uri.parse(str2));
                request.setDestinationUri(Uri.parse(sb4));
                request.setNotificationVisibility(1);
                ((DownloadManager) Download_Auto.this.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
                dialogInterface.cancel();
              //  Download_Auto.this.loadInterstitialAd();
                Download_Auto.this.startActivity(new Intent(Download_Auto.this, VideosViewDownActivityVideo.class));
                new UtilMethods(Download_Auto.this.getContext()).showCustomToast("Download Started...", 1);
                Download_Auto.this.finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getApplicationWindowToken(), 0);
        }
        activity.getWindow().setSoftInputMode(3);
    }

    protected void onStop() {
        super.onBackPressed();
        startActivity(new Intent(this, VideosViewDownActivityVideo.class));
        super.onStop();
    }

    public void getTweet(Long l, final String str) {
        TwitterCore.getInstance().getApiClient().getStatusesService().show(l, null, null, null).enqueue(new com.twitter.sdk.android.core.Callback<Tweet>() {
            public void success(Result<Tweet> result) {
                String str31;
                if (((Tweet) result.data).extendedEtities == null && ((Tweet) result.data).entities.media == null) {
                    Download_Auto.this.alertNoMedia();
                } else if (((MediaEntity) ((Tweet) result.data).extendedEtities.media.get(0)).type.equals("video") || ((MediaEntity) ((Tweet) result.data).extendedEtities.media.get(0)).type.equals("animated_gif")) {
                    String str2 = str;
                    if (((MediaEntity) ((Tweet) result.data).extendedEtities.media.get(0)).type.equals("video")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str2);
                        sb.append(".mp4");
                        str31 = sb.toString();
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str2);
                        sb2.append(".gif");
                        str31 = sb2.toString();
                    }
                    String str3 = ((VideoInfo.Variant) ((MediaEntity) ((Tweet) result.data).extendedEtities.media.get(0)).videoInfo.variants.get(0)).url;
                    int i = 0;
                    while (!str3.contains(".mp4")) {
                        try {
                            if (((MediaEntity) ((Tweet) result.data).extendedEtities.media.get(0)).videoInfo.variants.get(i) != null) {
                                str3 = ((VideoInfo.Variant) ((MediaEntity) ((Tweet) result.data).extendedEtities.media.get(0)).videoInfo.variants.get(i)).url;
                                i++;
                            }
                        } catch (IndexOutOfBoundsException unused) {
                            Download_Auto.this.downloadVideo(str3, str31);
                        }
                    }
                    Download_Auto.this.downloadVideo1(str3, str31);
                } else {
                    Download_Auto.this.alertNoVideo();
                }
            }

            public void failure(TwitterException twitterException) {
                Toast.makeText(Download_Auto.this, "Request Failed: Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void downloadVideo1(String str, String str2) {
        if (str2.endsWith(".mp4")) {
            this.progressDialog.setMessage("Fetching video...");
        } else {
            this.progressDialog.setMessage("Fetching gif...");
        }
        if (!storageAllowed()) {
            // ActivityCompat.requestPermissions(this, ConstantsVideo.PERMISSIONS_STORAGE, 1);
            PermissionUtil.requestAllStoragePermissions(this,1);
            this.progressDialog.hide();
            Toast.makeText(this, "Kindly grant the request and try again", Toast.LENGTH_SHORT).show();
            return;
        }
        this.progressDialog.hide();
    }

    /* access modifiers changed from: private */
    public void alertNoMedia() {
        this.progressDialog.hide();
        Toast.makeText(this, "The url entered contains no media file", Toast.LENGTH_LONG).show();
    }

    /* access modifiers changed from: private */
    public void alertNoVideo() {
        this.progressDialog.hide();
        Toast.makeText(this, "The url entered contains no video or gif file", Toast.LENGTH_LONG).show();
    }

    /* access modifiers changed from: private */
    public Long getTweetId(String str) {
        try {
            return Long.valueOf(Long.parseLong(str.split("\\/")[5].split("\\?")[0]));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("getTweetId: ");
            sb.append(e.getLocalizedMessage());
            Log.d("TAG", sb.toString());
            return null;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
