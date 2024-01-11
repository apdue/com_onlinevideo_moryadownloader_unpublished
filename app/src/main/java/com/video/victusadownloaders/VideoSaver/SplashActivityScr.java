package com.video.victusadownloaders.VideoSaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.video.victusadownloaders.WatchVideo.AppNew;
import com.video.victusadownloaders.additionalscreens.DummyFiveScrRand;
import com.video.victusadownloaders.additionalscreens.DummyFourScrRand;
import com.video.victusadownloaders.additionalscreens.DummyOneScrRand;
import com.video.victusadownloaders.additionalscreens.DummySixScrRand;
import com.video.victusadownloaders.additionalscreens.DummyThreeScrRand;
import com.video.victusadownloaders.additionalscreens.DummyTwoScrRand;
import com.video.victusadownloaders.additionalscreens.HomeActivityScrRand;
import com.video.victusadownloaders.utilities.AdsWork;
import com.video.victusadownloaders.utilities.AppOpenManagerFonts;
import com.video.victusadownloaders.utilities.AppOpenManagerTwo;
import com.video.victusadownloaders.utilities.NativeAdNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.awesome.gagtube.R;

public class SplashActivityScr extends Activity {


    private static final String url = "https://myapp.myappadmin.xyz/gb/" + "downloader1.php";

    public static final String top_ad_type = "top_ad_type";
    public static final String facebook_mrec_id = "facebook_mrec_id";
    public static final String facebook_native_banner = "facebook_native_banner";

    public static final String interstitial_ad_network = "interstitial_ad_network";
    public static final String banner_ad_network = "banner_ad_network";
    public static final String native_ad_network = "native_ad_network";

    public static final String facebook_interstitial_id = "facebook_interstitial_id";
    public static final String facebook_banner_id = "facebook_banner_id";
    public static final String facebook_native_id = "facebook_native_id";
    public static final String facebook_failed_admob_enabled = "facebook_failed_admob_enabled";
    public static final String admob_failed_facebook_enabled = "admob_failed_facebook_enabled";

    public static final String youtube_downloader = "youtube_downloader";
    public static final String social_downloader = "social_downloader";
    public static final String instagram_downloader = "instagram_downloader";
    public static final String inter_ad_type = "inter_ad_type";
    public static final String search_keyword = "search_keyword";
    public static final String yt_button_name = "yt_button_name";
    public static final String social_button_name = "social_button_name";
    public static final String insta_button_name = "insta_button_name";

    public static final String interstitial_loader = "interstitial_loader";
    public static final String native_size = "native_size";
    public static final String nativeid_small_enabled = "nativeid_small_enabled";
    public static final String TAG_NATIVEIDSMALL = "nativeid_small";
    public static final String status_dummy_one_enabled = "status_dummy_one_enabled";
    public static final String status_dummy_two_enabled = "status_dummy_two_enabled";
    public static final String status_dummy_three_enabled = "status_dummy_three_enabled";
    public static final String status_dummy_four_enabled = "status_dummy_four_enabled";
    public static final String status_dummy_five_enabled = "status_dummy_five_enabled";
    public static final String status_dummy_six_enabled = "status_dummy_six_enabled";
    public static final String status_dummy_one_back_enabled = "status_dummy_one_back_enabled";
    public static final String status_dummy_two_back_enabled = "status_dummy_two_back_enabled";
    public static final String status_dummy_three_back_enabled = "status_dummy_three_back_enabled";
    public static final String status_dummy_four_back_enabled = "status_dummy_four_back_enabled";
    public static final String status_dummy_five_back_enabled = "status_dummy_five_back_enabled";
    public static final String status_dummy_six_back_enabled = "status_dummy_six_back_enabled";
    public static final String dummy_one_screen = "dummy_one_screen";
    public static final String dummy_two_screen = "dummy_two_screen";
    public static final String dummy_three_screen = "dummy_three_screen";
    public static final String dummy_four_screen = "dummy_four_screen";
    public static final String dummy_five_screen = "dummy_five_screen";
    public static final String dummy_six_screen = "dummy_six_screen";
    public static final String exit_screen = "exit_screen";
    public static final String home_screen = "home_screen";






    public static final String TAG_VPN = "vpn";
    public static final String TAG_YOUTUBE_DISCOVER_ENABLED = "youtube_discover_enable";
    public static final String TAG_YOUTUBE_LIBRARY_ENABLED = "youtube_library_enable";
    public static final String TAG_YOUTUBE_SETTING_ENABLED = "youtube_setting_enable";
    public static final String TAG_YOUTUBE_DOWNLOAD_ENABLED = "youtube_download_enabled";
    public static final String TAG_DOWNLOAD_ENABLED = "youtube_download_enabled";
    public static final String TAG_DOWNLOAD_BUTTON_ENABLED = "dowload_btn_enabled";
    public static final String TAG_YOUTUBE_BUTTON_ENABLED = "youtube_btn_enabled";
    public static final String TAG_PLAYER_BUTTON_ENABLED = "player_btn_enabled";
    public static final String TAG_STATUS_BTN_ENABLED = "status_btn_enabled";
    public static final String TAG_JOSH = "josh";
    public static final String TAG_CHINGARI = "chingari";
    public static final String TAG_MITRON = "mitron";
    public static final String TAG_SHARE_CHAT = "sharechat";
    public static final String TAG_ROPOSO = "roposo";
    public static final String TAG_MOJ = "moj";
    public static final String TAG_LIKEE = "likee";
    public static final String TAG_INSTAGRAM = "instagram";
    public static final String TAG_WHATSAPP = "whatsapp";
    public static final String TAG_TIKTOK = "tiktok";
    public static final String TAG_FACEBOOK = "facebook";
    public static final String TAG_TAKATAK = "takatak";
    //user
    public static final String TAG_PKG = "pkg";
    public static final String TAG_NEWVERSION = "newversion";
    public static final String TAG_NEWAPPURL = "newappurl";
    public static final String TAG_APP_ID_AD_UNIT_ID = "app_id_ad_unit_id";
    public static final String TAG_BANNERMAIN = "bannermain";
    public static final String TAG_INTERSTITIALMAIN = "interstitialmain";
    public static final String TAG_SPLASH = "splash";
    public static final String TAG_INTERSTITIALSPLASH = "interstitialsplash";
    public static final String TAG_OPENAPP_ADS_ENABLED = "openapp_ads_enabled";
    public static final String TAG_OPENAPPID = "openappid";
    public static final String TAG_NATIVEID = "nativeid";
    public static final String TAG_ADMOB_INTERSTITIAL_FREQUENCY = "ADMOB_INTERSTITIAL_FREQUENCY";
    private static final int SPLASH_SHOW_TIME = 1000;
    private static final String TAG = "SplashActivity";
    // url to get all products list
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RAJANR = "rajanr";
    //vpn
    private static final String CHANNEL_ID = "vpn";
    //Prefrance
    public static PrefManagerVideo prf;
    public static int rajan = 0;
    // Create a new boolean and preference and set it to true
    public static boolean isFirstStart;
    public static int frequency = 1;
    public static int counter = 1;
    //back
    private static int backbackexit = 1;
    private static AppOpenManagerFonts appOpenManagerFonts;
    // Creating JSON Parser object
    private final JSONParserVideo jsonParser = new JSONParserVideo();
    // TODO: Change this to use whatever preferences are appropriate. The install referrer should
    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    public AppOpenAd appOpenAd;
    // Building Parameters
    Map<String, String> params = new HashMap<>();
    private JSONArray jsonarray = null;
    private int success = 0;
    private String newversion;
    private int rds = 0;
    private String rUrl = "sandeep";
    private String sf1, sf2, sf3, sf4, sf5, sf6;
    private String st1, st2, st3, st4, st5, st6;

    public static Map<String, String> getQueryMap(String query) {
        String[] par = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : par) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        prf = new PrefManagerVideo(this);
        initialization();


        isFirstStart = prf.getBoolean("firstStart");
        if (isFirstStart) {

            System.out.println("Rajan_isFirstStart" + isFirstStart);

            prf.setBoolean("firstStart", false);

            prf.setString("rUrl", "notset");

        } else {

        }

        if (prf.getString("rUrl").contains("notset")) {
            checkIns();
        } else {
            rUrl = prf.getString("rUrl");
            new BackgroundSplashTask().execute();
        }

    }

    private void initialization() {
        Log.e(TAG, "rajaninit: ");

        prf.setString(TAG_VPN, "yes");
        prf.setBoolean(TAG_YOUTUBE_DOWNLOAD_ENABLED, true);
        prf.setBoolean(TAG_DOWNLOAD_ENABLED, true);
        prf.setBoolean(TAG_DOWNLOAD_BUTTON_ENABLED, true);
        prf.setBoolean(TAG_YOUTUBE_BUTTON_ENABLED, true);
        prf.setBoolean(TAG_PLAYER_BUTTON_ENABLED, true);
        prf.setBoolean(TAG_STATUS_BTN_ENABLED, true);

        prf.setBoolean(TAG_JOSH, true);
        prf.setBoolean(TAG_CHINGARI, true);
        prf.setBoolean(TAG_MITRON, true);
        prf.setBoolean(TAG_SHARE_CHAT, true);
        prf.setBoolean(TAG_ROPOSO, true);
        prf.setBoolean(TAG_MOJ, true);
        prf.setBoolean(TAG_LIKEE, true);
        prf.setBoolean(TAG_INSTAGRAM, true);
        prf.setBoolean(TAG_WHATSAPP, true);
        prf.setBoolean(TAG_TIKTOK, true);
        prf.setBoolean(TAG_FACEBOOK, true);
        prf.setBoolean(TAG_TAKATAK, true);


        prf.setBoolean(TAG_YOUTUBE_DISCOVER_ENABLED, true);
        prf.setBoolean(TAG_YOUTUBE_LIBRARY_ENABLED, true);
        prf.setBoolean(TAG_YOUTUBE_SETTING_ENABLED, true);
        prf.setString(nativeid_small_enabled, "true");

        prf.setString(status_dummy_one_enabled, "true");
        prf.setString(status_dummy_two_enabled, "true");
        prf.setString(status_dummy_three_enabled, "true");
        prf.setString(status_dummy_four_enabled, "true");

        // jsonarray found
        // Getting Array of jsonarray
        prf.setString("rd", "0");
        prf.setString(TAG_NEWVERSION, "1.0");
        prf.setString(TAG_NEWAPPURL, "https://play.google.com/store/apps/details?id=freevideodownload");
        prf.setString(TAG_APP_ID_AD_UNIT_ID, "ca-app-pub-3940256099942544~3347511713");
        prf.setString(TAG_BANNERMAIN, "ca-app-pub-3940256099942544/630097811100");
        prf.setString(TAG_INTERSTITIALMAIN, "ca-app-pub-3940256099942544/103317371200");
        prf.setString(TAG_SPLASH, "interstitial");
        prf.setString(TAG_INTERSTITIALSPLASH, "no");

        prf.setString(TAG_OPENAPP_ADS_ENABLED, "no");
        prf.setString(TAG_OPENAPPID, "ca-app-pub-3940256099942544/341983529400");

        prf.setString(TAG_NATIVEID, "c");

        prf.setString(exit_screen, "exit_screen");
        prf.setString(home_screen, "text");


        prf.setString(status_dummy_one_back_enabled, "true");
        prf.setString(status_dummy_two_back_enabled, "true");
        prf.setString(status_dummy_three_back_enabled, "true");
        prf.setString(status_dummy_four_back_enabled, "true");
        prf.setString(status_dummy_five_back_enabled, "true");
        prf.setString(status_dummy_six_back_enabled, "true");

        prf.setString(dummy_one_screen, "ad");
        prf.setString(dummy_two_screen, "ad");
        prf.setString(dummy_three_screen, "ad");
        prf.setString(dummy_four_screen, "ad");
        prf.setString(dummy_five_screen, "ad");
        prf.setString(dummy_six_screen, "ad");




        prf.setString("skipfirstscreen", "1");
        prf.setString("startclicktext", "Start");
        prf.setString("startvisible", "1");
        prf.setString(native_size, "true");
        prf.setString(TAG_NATIVEIDSMALL, "ca-app-pub-3940256099942544/2247696110");

        prf.setInt(interstitial_loader, 5);

        prf.setString(youtube_downloader, "true");
        prf.setString(social_downloader, "true");
        prf.setString(instagram_downloader, "true");
        prf.setString(inter_ad_type, "inter");
        prf.setString(search_keyword, "default");
        prf.setString(yt_button_name, "Start VideoDownloader");
        prf.setString(social_button_name, "Social VideoDownloader");
        prf.setString(insta_button_name, "Insta VideoDownloader");

        prf.setString(TAG_ADMOB_INTERSTITIAL_FREQUENCY, "1");
        prf.setString("SUBSCRIBED", "FALSE");

        //        Facebook

        prf.setString(facebook_mrec_id, "IMG_16_9_APP_INSTALL#549167759165615_549192715829786");
        prf.setString(top_ad_type, "native");


        prf.setString(interstitial_ad_network, "facebook");
        prf.setString(banner_ad_network, "facebook");
        prf.setString(native_ad_network, "facebook");
        prf.setString(facebook_native_banner, "IMG_16_9_APP_INSTALL#549167759165615_549190905829967");

        prf.setString(facebook_interstitial_id, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        prf.setString(facebook_banner_id, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        prf.setString(facebook_native_id, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");

        prf.setString(facebook_failed_admob_enabled, "yes");
        prf.setString(admob_failed_facebook_enabled, "yes");

    }


    void checkIns() {
        InstallReferrerClient rfClient = InstallReferrerClient.newBuilder(this).build();
        backgroundExecutor.execute(() -> getInsRClient(rfClient));
    }

    private void makeallStr() {
        sf1 = makeStrFormat("or", 3, 8);
        sf2 = makeStrFormat("gc", 10, 13);
        sf3 = makeStrConcat("ut", "_m");
        sf4 = makeStrConcat("sou", "ecr");
        sf5 = makeStrConcat("med", "mui");
        sf6 = makeStrConcat("google-", "yalp");
    }

    private void makeAllStrScnd() {
        st1 = "rornd";
        st2 = "sgcl";
        st3 = "drsd";
        st4 = "sucp";
        st5 = "mdup";
        st6 = "rgpy";
    }

    private String makeStrFormat(String str, int i, int j) {
        String rstring = "nicganicidlid";
        StringBuilder sb = new StringBuilder(rstring);
        return str.concat(sb.substring(i, j));
    }

    private String makeStrConcat(String str, String str2) {
        StringBuilder sb2 = new StringBuilder(str2);
        sb2.reverse();
        return str.concat(sb2.toString());
    }

    void getInsRClient(InstallReferrerClient rfClient) {

        rfClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = rfClient.getInstallReferrer();
                            String rUrltemp = response.getInstallReferrer();

                            makeallStr();
                            makeAllStrScnd();

                            //array to hold replacements
                            String[][] replacements = {{sf1, st1},
                                    {sf2, st2},
                                    {sf3, st3},
                                    {sf4, st4},
                                    {sf5, st5},
                                    {sf6, st6}};

                            //loop over the array and replace
                            String strOutput = rUrltemp;
                            for (String[] replacement : replacements) {
                                strOutput = strOutput.replaceAll(replacement[0], replacement[1]);
                            }

                            rUrl = strOutput;

                            prf.setString("rUrl", rUrl);

                            new BackgroundSplashTask().execute();

                        } catch (RemoteException e) {

                            rUrl = "sandeep_exception_notset";
                            prf.setString("rUrl", rUrl);
                            new BackgroundSplashTask().execute();

                            e.printStackTrace();
                            return;
                        }

                        // End the connection
                        rfClient.endConnection();

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:

                        rUrl = "not_supported_notset";
                        prf.setString("rUrl", rUrl);
                        new BackgroundSplashTask().execute();


                        // API not available on the current Play Store app.
                        Log.e(TAG, "FEATURE_NOT_SUPPORTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:

                        rUrl = "unavailable_notset";
                        prf.setString("rUrl", rUrl);
                        new BackgroundSplashTask().execute();
                        // Connection couldn't be established.
                        Log.e(TAG, "SERVICE_UNAVAILABLE");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }

    public SharedPreferences getPrefs() {
        return getSharedPreferences(prf.getString("SHARED_PREFS"), Context.MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private String getisdevmode() {
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() == 16) {
            return String.valueOf(Settings.Secure.getInt(getContentResolver(), "development_settings_enabled", 0));
        }
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() >= 17) {
            return String.valueOf(Settings.Secure.getInt(getContentResolver(), "development_settings_enabled", 0));
        }
        return String.valueOf(0);
    }

    private void startActivity() {

        if (new PrefManagerVideo(SplashActivityScr.this).getString(TAG_OPENAPP_ADS_ENABLED).contains("yes")) {
            appOpenManagerFonts = new AppOpenManagerFonts((AppNew) AppNew.getAppContext());
        }

        Log.e(TAG, "startActivity: ");
        Intent intent;
        if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.status_dummy_one_enabled).contains("true")) {
            intent = new Intent(SplashActivityScr.this, DummyOneScrRand.class);
        } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.status_dummy_two_enabled).contains("true")) {
            intent = new Intent(SplashActivityScr.this, DummyTwoScrRand.class);
        } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.status_dummy_three_enabled).contains("true")) {
            intent = new Intent(SplashActivityScr.this, DummyThreeScrRand.class);
        } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.status_dummy_four_enabled).contains("true")) {
            intent = new Intent(SplashActivityScr.this, DummyFourScrRand.class);
        } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.status_dummy_five_enabled).contains("true")) {
            intent = new Intent(SplashActivityScr.this, DummyFiveScrRand.class);
        } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.status_dummy_six_enabled).contains("true")) {
            intent = new Intent(SplashActivityScr.this, DummySixScrRand.class);
        } else {
            intent = new Intent(SplashActivityScr.this, HomeActivityScrRand.class);
        }

        NativeAdNew.loadNativeAdLarge(SplashActivityScr.this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                Log.e(TAG, "onAdFinished: ");

                NativeAdNew.loadNativeAdSMall(SplashActivityScr.this, new AdsWork.AdFinished() {
                    @Override
                    public void onAdFinished() {
                        if (prf.getString(TAG_INTERSTITIALSPLASH).equalsIgnoreCase("yes")) {
                            Log.e(TAG, "TAG_INTERSTITIALSPLASH: yes");

                            if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.TAG_SPLASH).contains("facebook")) {
                                Log.e(TAG, "TAG_SPLASH: facebook");


                                AdsWork.showFacebookInterstitialAd(SplashActivityScr.this, new AdsWork.AdFinished() {
                                    @Override
                                    public void onAdFinished() {
                                        startActivity(intent);
                                        Log.e(TAG, "startActivity: facebook");

                                    }
                                });

                            } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.TAG_SPLASH).contains("open")) {

                                Log.e(TAG, "TAG_OPENAPP_ADS_ENABLED: yes");

                                appOpenManagerFonts = new AppOpenManagerFonts((AppNew) AppNew.getAppContext());

                                AppOpenAd.AppOpenAdLoadCallback loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                                    @Override
                                    public void onAdLoaded(AppOpenAd ad) {
                                        Log.e(TAG, "onAdLoaded:");

                                        appOpenAd = ad;
                                        appOpenAd.show(SplashActivityScr.this);
                                        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                            @Override
                                            public void onAdDismissedFullScreenContent() {
                                                super.onAdDismissedFullScreenContent();
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        Log.e(TAG, "OPEN onAdFailedToLoad : " + loadAdError.getMessage());
                                        if (new PrefManagerVideo(SplashActivityScr.this).getString(admob_failed_facebook_enabled).contains("true")) {
                                            AdsWork.showFacebookInterstitialAd(SplashActivityScr.this, new AdsWork.AdFinished() {
                                                @Override
                                                public void onAdFinished() {
                                                    startActivity(intent);
                                                    Log.e(TAG, "startActivity: facebook");
                                                }
                                            });
                                        } else {
                                            startActivity(intent);
                                        }

                                    }
                                };
                                AdRequest request = new AdRequest.Builder().build();
                                AppOpenAd.load(SplashActivityScr.this, prf.getString(TAG_OPENAPPID),
                                        request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
                            } else if (new PrefManagerVideo(SplashActivityScr.this).getString(SplashActivityScr.TAG_SPLASH).contains("inter")) {
                                AdsWork.showDelayedInterstitial(SplashActivityScr.this, new AdsWork.AdFinished() {
                                    @Override
                                    public void onAdFinished() {
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "onPreExecute: ");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.e(TAG, "doInBackground: ");

            // Building Parameters
            params.put(TAG_PKG, getApplicationContext().getPackageName());

            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                params.put("version", version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            params.put("isdevmode", getisdevmode());

            params.put("rUrl", prf.getString("rUrl"));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);

            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.e(TAG, "doInBackground: json is " + json);
                    prf.setString("rd", json.getString("rd"));
                    prf.setString(TAG_NEWVERSION, json.getString(TAG_NEWVERSION));
                    prf.setString(TAG_NEWAPPURL, json.getString(TAG_NEWAPPURL));
                    prf.setString(TAG_APP_ID_AD_UNIT_ID, json.getString(TAG_APP_ID_AD_UNIT_ID));
                    prf.setString(TAG_BANNERMAIN, json.getString(TAG_BANNERMAIN));
                    prf.setString(TAG_INTERSTITIALMAIN, json.getString(TAG_INTERSTITIALMAIN));
                    prf.setString(TAG_SPLASH, json.getString(TAG_SPLASH));
                    prf.setString(TAG_INTERSTITIALSPLASH, json.getString(TAG_INTERSTITIALSPLASH));

                    prf.setString(TAG_OPENAPP_ADS_ENABLED, json.getString(TAG_OPENAPP_ADS_ENABLED));
                    prf.setString(TAG_OPENAPPID, json.getString(TAG_OPENAPPID));

                    prf.setString(TAG_NATIVEID, json.getString(TAG_NATIVEID));
                    prf.setString(native_size, json.getString(native_size));

                    prf.setString(interstitial_ad_network, json.getString(interstitial_ad_network));
                    prf.setString(banner_ad_network, json.getString(banner_ad_network));
                    prf.setString(native_ad_network, json.getString(native_ad_network));

                    prf.setString(facebook_interstitial_id, json.getString(facebook_interstitial_id));
                    prf.setString(facebook_banner_id, json.getString(facebook_banner_id));
                    prf.setString(facebook_native_id, json.getString(facebook_native_id));
                    prf.setString(facebook_native_banner, json.getString(facebook_native_banner));

                    prf.setString(facebook_mrec_id, json.getString(facebook_mrec_id));
                    prf.setString(top_ad_type, json.getString(top_ad_type));


                    prf.setString(facebook_failed_admob_enabled, json.getString(facebook_failed_admob_enabled));
                    prf.setString(admob_failed_facebook_enabled, json.getString(admob_failed_facebook_enabled));

                    prf.setString(TAG_ADMOB_INTERSTITIAL_FREQUENCY, json.getString(TAG_ADMOB_INTERSTITIAL_FREQUENCY));


                    prf.setString(TAG_VPN, json.getString(TAG_VPN));

                    prf.setString(TAG_NATIVEIDSMALL, json.getString(TAG_NATIVEIDSMALL));

                    Log.e(TAG, "doInBackground:" + frequency);

                    prf.setBoolean(TAG_YOUTUBE_DISCOVER_ENABLED, json.getBoolean(TAG_YOUTUBE_DISCOVER_ENABLED));
                    prf.setBoolean(TAG_YOUTUBE_LIBRARY_ENABLED, json.getBoolean(TAG_YOUTUBE_LIBRARY_ENABLED));
                    prf.setBoolean(TAG_YOUTUBE_SETTING_ENABLED, json.getBoolean(TAG_YOUTUBE_SETTING_ENABLED));

                    prf.setString(nativeid_small_enabled, json.getString(nativeid_small_enabled));

                    prf.setBoolean(TAG_YOUTUBE_DOWNLOAD_ENABLED, json.getBoolean(TAG_YOUTUBE_DOWNLOAD_ENABLED));
                    prf.setBoolean(TAG_DOWNLOAD_ENABLED, json.getBoolean(TAG_DOWNLOAD_ENABLED));
                    prf.setBoolean(TAG_DOWNLOAD_BUTTON_ENABLED, json.getBoolean(TAG_DOWNLOAD_BUTTON_ENABLED));
                    prf.setBoolean(TAG_YOUTUBE_BUTTON_ENABLED, json.getBoolean(TAG_YOUTUBE_BUTTON_ENABLED));
                    prf.setBoolean(TAG_PLAYER_BUTTON_ENABLED, json.getBoolean(TAG_PLAYER_BUTTON_ENABLED));
                    prf.setBoolean(TAG_STATUS_BTN_ENABLED, json.getBoolean(TAG_STATUS_BTN_ENABLED));
                    prf.setInt(interstitial_loader, json.getInt(interstitial_loader));

                    prf.setString(youtube_downloader, json.getString(youtube_downloader));
                    prf.setString(social_downloader, json.getString(social_downloader));
                    prf.setString(instagram_downloader, json.getString(instagram_downloader));
                    prf.setString(inter_ad_type, json.getString(inter_ad_type));
                    prf.setString(search_keyword, json.getString(search_keyword));
                    prf.setString(yt_button_name, json.getString(yt_button_name));
                    prf.setString(social_button_name, json.getString(social_button_name));
                    prf.setString(insta_button_name, json.getString(social_button_name));

                    prf.setBoolean(TAG_JOSH, json.getBoolean(TAG_JOSH));
                    prf.setBoolean(TAG_CHINGARI, json.getBoolean(TAG_CHINGARI));
                    prf.setBoolean(TAG_MITRON, json.getBoolean(TAG_MITRON));
                    prf.setBoolean(TAG_SHARE_CHAT, json.getBoolean(TAG_SHARE_CHAT));
                    prf.setBoolean(TAG_ROPOSO, json.getBoolean(TAG_ROPOSO));
                    prf.setBoolean(TAG_MOJ, json.getBoolean(TAG_MOJ));
                    prf.setBoolean(TAG_LIKEE, json.getBoolean(TAG_LIKEE));
                    prf.setBoolean(TAG_INSTAGRAM, json.getBoolean(TAG_INSTAGRAM));
                    prf.setBoolean(TAG_WHATSAPP, json.getBoolean(TAG_WHATSAPP));
                    prf.setBoolean(TAG_TIKTOK, json.getBoolean(TAG_TIKTOK));
                    prf.setBoolean(TAG_FACEBOOK, json.getBoolean(TAG_FACEBOOK));
                    prf.setBoolean(TAG_TAKATAK, json.getBoolean(TAG_TAKATAK));

                    prf.setString(status_dummy_one_enabled, json.getString(status_dummy_one_enabled));
                    prf.setString(status_dummy_two_enabled, json.getString(status_dummy_two_enabled));
                    prf.setString(status_dummy_three_enabled, json.getString(status_dummy_three_enabled));
                    prf.setString(status_dummy_four_enabled, json.getString(status_dummy_four_enabled));
                    prf.setString(status_dummy_five_enabled, json.getString(status_dummy_five_enabled));
                    prf.setString(status_dummy_six_enabled, json.getString(status_dummy_six_enabled));
                    prf.setString(exit_screen, json.getString(exit_screen));
                    prf.setString(home_screen, json.getString(home_screen));
                    prf.setString(dummy_one_screen, json.getString(dummy_one_screen));
                    prf.setString(dummy_two_screen, json.getString(dummy_two_screen));
                    prf.setString(dummy_three_screen, json.getString(dummy_three_screen));
                    prf.setString(dummy_four_screen, json.getString(dummy_four_screen));
                    prf.setString(dummy_five_screen, json.getString(dummy_five_screen));
                    prf.setString(dummy_six_screen, json.getString(dummy_six_screen));


                    prf.setString(status_dummy_one_back_enabled, json.getString(status_dummy_one_back_enabled));
                    prf.setString(status_dummy_two_back_enabled, json.getString(status_dummy_two_back_enabled));
                    prf.setString(status_dummy_three_back_enabled, json.getString(status_dummy_three_back_enabled));
                    prf.setString(status_dummy_four_back_enabled, json.getString(status_dummy_four_back_enabled));
                    prf.setString(status_dummy_five_enabled, json.getString(status_dummy_five_enabled));
                    prf.setString(status_dummy_six_back_enabled, json.getString(status_dummy_six_back_enabled));

                } else {
                    Log.e(TAG, "doInBackground: FAILURE");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAGPHP", "doInBackground: " + e.getLocalizedMessage());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAGPHP", "doInBackground: " + e.getLocalizedMessage());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.e(TAG, "onPostExecute: ");
            AdsWork.loadInterstitialAd(SplashActivityScr.this);
            AppOpenManagerTwo.fetchAd(SplashActivityScr.this);
            startActivity();

        }

    }

}
