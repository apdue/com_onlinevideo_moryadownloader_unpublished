package com.video.victusadownloaders.StatusVideoDownloader.activity;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.video.victusadownloaders.StatusVideoDownloader.util.Utils.ROOTDIRECTORYJOSH;
import static com.video.victusadownloaders.StatusVideoDownloader.util.Utils.createFileFolder;
import static com.video.victusadownloaders.StatusVideoDownloader.util.Utils.startDownload;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.bumptech.glide.Glide;
import io.awesome.gagtube.R;
import io.awesome.gagtube.databinding.LayoutGlobalUiBinding;

import com.video.victusadownloaders.StatusVideoDownloader.api.CommonClassForAPI;
import com.video.victusadownloaders.StatusVideoDownloader.util.AppLangSessionManager;
import com.video.victusadownloaders.StatusVideoDownloader.util.SharePrefs;
import com.video.victusadownloaders.StatusVideoDownloader.util.Utils;
import com.video.victusadownloaders.utilities.AdsWork;


import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

public class JoshActivity extends AppCompatActivity {
    LayoutGlobalUiBinding binding;
    JoshActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;
    PrefManagerVideo prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_global_ui);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        prf = new PrefManagerVideo(this);

        AdsWork.loadNativeAd(this, (ViewGroup) findViewById(R.id.nativeAd), R.layout.ad_unified_medium);
        AdsWork.loadNativeAd(this, (ViewGroup) findViewById(R.id.nativeAdSmall), R.layout.ad_unified_small);
        createFileFolder();
        initViews();
//        binding.imAppIcon.setImageDrawable(getResources().getDrawable(R.drawable.josh_logo));
//        binding.tvAppName.setText(getResources().getString(R.string.josh_app_name));


        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());


        //LoadNativeAd();
      //  InterstitialAdsINIT();

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
                AdsWork.showInterAds(JoshActivity.this, new AdsWork.AdFinished() {
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
                .load(R.drawable.sc1_rand)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.sc2_rand)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.sc1_rand)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.jo2_rand)
                .into(binding.layoutHowTo.imHowto4);

        binding.layoutHowTo.tvHowToHeadOne.setVisibility(View.GONE);
        binding.layoutHowTo.LLHowToOne.setVisibility(View.GONE);
        binding.layoutHowTo.tvHowToHeadTwo.setText(getResources().getString(R.string.how_to_download));

        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_josh));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.cop_link_from_josh));
        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOJOSH)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOJOSH, true);
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
                Utils.showProgressDialog(activity);
                getJoshData();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            AdsWork.showInterAds(JoshActivity.this, new AdsWork.AdFinished() {
                @Override
                public void onAdFinished() {
                    PasteText();
                }
            });
        });

//        binding.LLOpenApp.setOnClickListener(v -> {
//            Utils.OpenApp(activity, "com.eterno.shortvideos");
//        });
    }

    private void getJoshData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("myjosh")) {
                AdsWork.showInterAds(JoshActivity.this, new AdsWork.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        Utils.showProgressDialog(activity);
                        new callGetJoshData().execute(binding.etText.getText().toString());
                    }
                });

            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
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
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("myjosh")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("myjosh")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("myjosh")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetJoshData extends AsyncTask<String, Void, Document> {
        Document JoshDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                JoshDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return JoshDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {
                String url = result.select("script[id=\"__NEXT_DATA__\"]").last().html();

                if (!url.equals("")) {
                    JSONObject jsonObject = new JSONObject(url);
                    VideoUrl = String.valueOf(jsonObject.getJSONObject("props")
                            .getJSONObject("pageProps").getJSONObject("detail")
                            .getJSONObject("data").
                                    getString("download_url"));
                    //Mp3 Available
                    String mp3Url = String.valueOf(jsonObject.getJSONObject("props")
                            .getJSONObject("pageProps").getJSONObject("detail")
                            .getJSONObject("data").getJSONObject("audio_track_meta").
                                    getString("url"));

                    VideoUrl = VideoUrl.replace("r4_wmj_480.mp4","r4_480.mp4");
                    startDownload(VideoUrl, ROOTDIRECTORYJOSH, activity, "josh_" + System.currentTimeMillis() + ".mp4");
                    VideoUrl = "";
                    binding.etText.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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





}