package com.video.victusadownloaders.StatusVideoDownloader.activity;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.video.victusadownloaders.StatusVideoDownloader.StatusMainActivityVideoRand.extractLinks;
import static com.video.victusadownloaders.StatusVideoDownloader.util.Utils.RootDirectoryLikeeShow;
import static com.video.victusadownloaders.StatusVideoDownloader.util.Utils.createFileFolder;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.StatusVideoDownloader.api.CommonClassForAPI;
import com.video.victusadownloaders.StatusVideoDownloader.util.AppLangSessionManager;
import com.video.victusadownloaders.StatusVideoDownloader.util.SharePrefs;
import com.video.victusadownloaders.StatusVideoDownloader.util.Utils;
import com.video.victusadownloaders.utilities.AdsWork;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.awesome.gagtube.R;
import io.awesome.gagtube.databinding.LayoutGlobalUiBinding;

public class LikeeActivity extends AppCompatActivity {
    LayoutGlobalUiBinding binding;
    LikeeActivity activity;
    CommonClassForAPI commonClassForAPI;
    Pattern pattern = Pattern.compile("window\\.data \\s*=\\s*(\\{.+?\\});");
    ProgressDialog progressDialog;
    AppLangSessionManager appLangSessionManager;
    AsyncTask downloadAsyncTask;
    PrefManagerVideo prf;
    private String VideoUrl;
    private ClipboardManager clipBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_global_ui);
        activity = this;
        prf = new PrefManagerVideo(this);

        AdsWork.loadNativeAd(this, (ViewGroup) findViewById(R.id.nativeAd), R.layout.ad_unified_medium);
        AdsWork.loadNativeAd(this, (ViewGroup) findViewById(R.id.nativeAdSmall), R.layout.ad_unified_small);
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

//        binding.imAppIcon.setImageDrawable(getResources().getDrawable(R.drawable.likee_logo));
//        binding.tvAppName.setText(getResources().getString(R.string.likee_app_name));


        //InterstitialAdsINIT();
        //  LoadNativeAd();


        initiliazeDialog();

    }

    void initiliazeDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //progressDialog.setTitle("Please Wait.");
        progressDialog.setMessage(getResources().getString(R.string.downloadin_video));
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();

                if (downloadAsyncTask != null) {
                    progressDialog.setProgress(0);
                    downloadAsyncTask.cancel(true);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdsWork.showInterAds(LikeeActivity.this, new AdsWork.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        onBackPressed();
                    }
                });
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });


        Glide.with(activity)
                .load(R.drawable.likee1_rand)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.likee2_rand)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.likee3_rand)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.likee4_rand)
                .into(binding.layoutHowTo.imHowto4);


        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_likee));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.copy_video_link_from_likee));

        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOLIKEE)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOLIKEE, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                AdsWork.showInterAds(LikeeActivity.this, new AdsWork.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        GetLikeeData();
                    }
                });
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            AdsWork.showInterAds(LikeeActivity.this, new AdsWork.AdFinished() {
                @Override
                public void onAdFinished() {
                    PasteText();
                }
            });
        });

    }

    private void GetLikeeData() {
        try {
            createFileFolder();
            String url = binding.etText.getText().toString();
            if (url.contains("likee")) {
                Utils.showProgressDialog(activity);
                new callGetLikeeData().execute(url);
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");


            if (CopyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("likee")) {
                        binding.etText.setText(extractLinks(clipBoard.getPrimaryClip().getItemAt(0).getText().toString()));
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("likee")) {
                        binding.etText.setText(extractLinks(item.getText().toString()));
                    }

                }
            } else {
                if (CopyIntent.contains("likee")) {
                    CopyIntent = extractLinks(CopyIntent);
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName() + "";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

    class callGetLikeeData extends AsyncTask<String, Void, Document> {
        Document likeeDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                likeeDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return likeeDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                String JSONData = "";
                Matcher matcher = pattern.matcher(result.toString());
                while (matcher.find()) {
                    JSONData = matcher.group().replaceFirst("window.data = ", "").replace(";", "");
                }
                JSONObject jsonObject = new JSONObject(JSONData);
                VideoUrl = jsonObject.getString("video_url").replace("_4", "");
                //VideoUrl = VideoUrl.substring(VideoUrl.indexOf("http"),VideoUrl.indexOf("?"));
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        //startDownload(VideoUrl, RootDirectoryLikee, activity, getFilenameFromURL(VideoUrl));
                        progressDialog.show();
                        downloadAsyncTask = new DownloadFileFromURL().execute(VideoUrl);

                        VideoUrl = "";
                        binding.etText.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(
                        RootDirectoryLikeeShow + "/" + getFilenameFromURL(VideoUrl));
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            progressDialog.dismiss();
            progressDialog.setProgress(0);

            Utils.setToast(activity, getResources().getString(R.string.download_complete));
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    MediaScannerConnection.scanFile(activity, new String[]
                                    {new File(RootDirectoryLikeeShow + "/" + getFilenameFromURL(VideoUrl)).getAbsolutePath()},
                            null, new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });
                } else {
                    activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED",
                            Uri.fromFile(new File(RootDirectoryLikeeShow + "/" + getFilenameFromURL(VideoUrl)))));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.setProgress(0);
            Log.d(TAG, "onCancelled: AysncTask");
        }
    }


}