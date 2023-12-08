package com.video.victusadownloaders.WatchVideo.models;

import com.google.gson.annotations.SerializedName;

public class Config {


    @SerializedName("ads_enabled")
    private boolean adenabled;

    @SerializedName("banner_id")
    private String bannerid;

    @SerializedName("interstitial_id")
    private String interstitialid;

    @SerializedName("interstitial_id_two")
    private String interstitialidtwo;

    @SerializedName("native_id_up")
    private String NativeUpId;

    @SerializedName("native_id_down")
    private String NativeDownId;


    @SerializedName("open_ad")
    private String open_ad;


    @SerializedName("native_up_enabled")
    private boolean NativeUpEnabled;

    @SerializedName("native_down_enabled")
    private boolean NativeDownEnabled;

    @SerializedName("interstitial_enabled")
    private boolean InterstitialEnabled;

    @SerializedName("youtube_download_enabled")
    private boolean YoutubeDownloadButton;

    @SerializedName("dowload_download_enabled")
    private boolean DownloadDownloadButton;

    @SerializedName("dowload_btn_enabled")
    private boolean DownloadEnabled;

    @SerializedName("youtube_btn_enabled")
    private boolean YoutubeEnabled;

    @SerializedName("player_btn_enabled")
    private boolean PlayerEnabled;

    @SerializedName("status_btn_enabled")
    private boolean StatusEnabled;

    @SerializedName("first_up")
    private boolean FirstUp;

    @SerializedName("first_down")
    private boolean FirstDown;

    @SerializedName("second_up")
    private boolean SecondUp;

    @SerializedName("second_down")
    private boolean SecondDown;

    @SerializedName("third_up")
    private boolean ThirdUp;

    @SerializedName("third_down")
    private boolean ThirdDown;

    @SerializedName("four_up")
    private boolean Fourup;

    @SerializedName("four_down")
    private boolean FourDown;

    @SerializedName("five_up")
    private boolean FiveUp;

    @SerializedName("five_down")
    private boolean FiveDown;

    @SerializedName("six_up")
    private boolean SixUp;

    @SerializedName("six_down")
    private boolean SixDown;

    @SerializedName("josh")
    private boolean Josh;

    @SerializedName("chingari")
    private boolean Chingari;

    @SerializedName("mitron")
    private boolean Mitron;

    @SerializedName("sharechat")
    private boolean Sharechat;


    @SerializedName("roposo")
    private boolean Roposo;

    @SerializedName("moj")
    private boolean Moj;

   @SerializedName("likee")
    private boolean Likee;

   @SerializedName("instagram")
    private boolean Instagram;

   @SerializedName("whatsapp")
    private boolean Whatsapp;

   @SerializedName("tiktok")
    private boolean Tiktok;

   @SerializedName("facebook")
    private boolean Facebook;

   @SerializedName("takatak")
    private boolean Takatak;



    @SerializedName("ads_click_count")
    private int InterstitialClickCount;

    @SerializedName("splash_type")
    private String SplashType;


    @SerializedName("firstscreen_remove")
    private boolean FirstScreen;

    @SerializedName("secondscreen_remove")
    private boolean SecondScreen;


    @SerializedName("youtube_interstitial_enabled")
    private boolean YoutubeInterstitialEnabled;



    @SerializedName("firstscreen_native")
    private boolean FirstScreenNative;

    public boolean isAdenabled() {
        return adenabled;
    }

    public String getBannerid() {
        return bannerid;
    }

    public String getInterstitialid() {
        return interstitialid;
    }

    public String getInterstitialidtwo() {
        return interstitialidtwo;
    }

    public String getNativeUpId() {
        return NativeUpId;
    }

    public String getNativeDownId() {
        return NativeDownId;
    }

    public String getOpen_ad() {
        return open_ad;
    }

    public boolean isNativeUpEnabled() {
        return NativeUpEnabled;
    }

    public boolean isNativeDownEnabled() {
        return NativeDownEnabled;
    }

    public boolean isInterstitialEnabled() {
        return InterstitialEnabled;
    }

    public int getInterstitialClickCount() {
        return InterstitialClickCount;
    }

    public String getSplashType() {
        return SplashType;
    }

    public boolean getFirstScreen() {
        return FirstScreen;
    }


    public boolean isFirstScreenNative() {
        return FirstScreenNative;
    }

    public boolean isYoutubeDownloadButton() {
        return YoutubeDownloadButton;
    }

    public boolean isDownloadDownloadButton() {
        return DownloadDownloadButton;
    }

    public boolean isDownloadEnabled() {
        return DownloadEnabled;
    }

    public boolean isYoutubeEnabled() {
        return YoutubeEnabled;
    }

    public boolean isPlayerEnabled() {
        return PlayerEnabled;
    }

    public boolean isStatusEnabled() {
        return StatusEnabled;
    }

    public boolean isFirstScreen() {
        return FirstScreen;
    }

    public boolean isFirstUp() {
        return FirstUp;
    }

    public boolean isFirstDown() {
        return FirstDown;
    }

    public boolean isSecondUp() {
        return SecondUp;
    }

    public boolean isSecondDown() {
        return SecondDown;
    }

    public boolean isThirdUp() {
        return ThirdUp;
    }

    public boolean isThirdDown() {
        return ThirdDown;
    }

    public boolean isFourup() {
        return Fourup;
    }

    public boolean isFourDown() {
        return FourDown;
    }

    public boolean isFiveUp() {
        return FiveUp;
    }

    public boolean isFiveDown() {
        return FiveDown;
    }

    public boolean isSixUp() {
        return SixUp;
    }

    public boolean isSixDown() {
        return SixDown;
    }

    public boolean isJosh() {
        return Josh;
    }

    public boolean isChingari() {
        return Chingari;
    }

    public boolean isMitron() {
        return Mitron;
    }

    public boolean isSharechat() {
        return Sharechat;
    }

    public boolean isRoposo() {
        return Roposo;
    }

    public boolean isMoj() {
        return Moj;
    }

    public boolean isLikee() {
        return Likee;
    }

    public boolean isInstagram() {
        return Instagram;
    }

    public boolean isWhatsapp() {
        return Whatsapp;
    }

    public boolean isTiktok() {
        return Tiktok;
    }

    public boolean isFacebook() {
        return Facebook;
    }

    public boolean isTakatak() {
        return Takatak;
    }

    public boolean isSecondScreen() {
        return SecondScreen;
    }

    public boolean isYoutubeInterstitialEnabled() {
        return YoutubeInterstitialEnabled;
    }
}
