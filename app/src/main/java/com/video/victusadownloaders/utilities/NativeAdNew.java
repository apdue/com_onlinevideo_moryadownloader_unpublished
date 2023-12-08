package com.video.victusadownloaders.utilities;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;

import java.util.ArrayList;
import java.util.List;

import io.awesome.gagtube.R;

public class NativeAdNew {

    private static NativeBannerAd nativeBannerAd;

    public static NativeAd nativeAdLarge;
    public static NativeAd nativeAdSmall;
    private static NativeAdView FBadView;

    private static boolean isFBNativeLoaded = false;

    private static com.facebook.ads.AdView fbBannerView;
    private static com.facebook.ads.AdView fbBannerViewTop;
    private static com.facebook.ads.NativeAd fbNativeAdNew;
    private static com.facebook.ads.NativeAdLayout nativeAdLayout;

    public static void showMediumNative(Activity activity, ViewGroup viewGroup) {
        if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_ad_network).contains("facebook")) {
            if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("mrec")) {
                Log.d("NativeAdLog", "native_ad_network: facebook mrec");
                showFBMrecAd(activity, viewGroup);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("nativebanner")) {
                Log.d("NativeAdLog", "native_ad_network: facebook nativebanner");
                fbNativeBanner(activity, viewGroup);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("banner")) {
                Log.d("NativeAdLog", "native_ad_network: facebook banner");
                showFBBannerTop(activity, viewGroup);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("native")) {
                Log.d("NativeAdLog", "native_ad_network: facebook native");
                if (isFBNativeLoaded) {
                    Log.d("TAGNATIVEAD", "showNativeAdLarge inflateAd: ");
                    inflateAd(activity, fbNativeAdNew, viewGroup, R.layout.native_ad_layout);
                } else {
                    showAdmobNative(activity, viewGroup);
                }
            }
        } else {
            if (nativeAdLarge != null) {
                try {
                    FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.displayed);
                    populateNativeAdView(activity, nativeAdLarge, FBadView);
                    viewGroup.removeAllViews();
                    viewGroup.addView(FBadView);
                    nativeAdLarge = null;
                    loadNativeAdLarge(activity);
                } catch (Exception e) {
                }
            } else {

                if (new PrefManagerVideo(activity).getString(SplashActivityScr.admob_failed_facebook_enabled).contains("true")) {

                    fbNativeAdNew = new com.facebook.ads.NativeAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_id));

                    NativeAdListener nativeAdListener = new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(Ad ad) {
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            Log.d("TAGNATIVEAD", "onError: " + adError.getErrorMessage());
                            fbNativeAdNew = null;


                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            Log.d("TAGNATIVEAD", "onAdLoaded: ");

                            // Race condition, load() called again before last ad was displayed
                            if (fbNativeAdNew == null || fbNativeAdNew != ad) {
                                return;
                            }
                            inflateAd(activity, fbNativeAdNew, viewGroup, R.layout.native_ad_layout);

                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    };

                    fbNativeAdNew.loadAd(
                            fbNativeAdNew.buildLoadAdConfig()
                                    .withAdListener(nativeAdListener)
                                    .build());
                }
            }
        }
    }

    public static void loadNativeAdLarge(final Activity activity, AdsWork.AdFinished adFinished) {
        if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_ad_network).contains("facebook")) {

            if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("nativebanner")) {
                nativeBannerAd = new NativeBannerAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_banner));
                NativeAdListener nativeAdListener = new NativeAdListener() {

                    @Override
                    public void onMediaDownloaded(Ad ad) {

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                            AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

                            builder.forNativeAd(
                                    new NativeAd.OnNativeAdLoadedListener() {
                                        // OnLoadedListener implementation.
                                        @Override
                                        public void onNativeAdLoaded(NativeAd nativeAd) {
                                            Log.d("TAGNATIVEAD", "native banner error r : onNativeAdLoaded: AdMob");
                                            nativeAdLarge = nativeAd;
                                            adFinished.onAdFinished();
                                        }

                                    }).withAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                                }
                            });

                            AdLoader adLoader = builder.withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    adFinished.onAdFinished();
                                    Log.d("TAGNATIVEAD", "onAdFailedToLoad: AdMob : " + loadAdError.getMessage());

                                }
                            }).build();

                            adLoader.loadAd(new AdRequest.Builder().build());
                        } else {
                            adFinished.onAdFinished();
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Race condition, load() called again before last ad was displayed
                        if (nativeBannerAd == null || nativeBannerAd != ad) {
                            return;
                        }
                        adFinished.onAdFinished();
                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }

                };
                // load the ad
                nativeBannerAd.loadAd(
                        nativeBannerAd.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());

            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("banner")) {
                loadFBBannerTop(activity, adFinished);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("native")) {
                fbNativeAdNew = new com.facebook.ads.NativeAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_id));

                NativeAdListener nativeAdListener = new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        Log.d("TAGNATIVEAD", "onError: " + adError.getErrorMessage());
                        fbNativeAdNew = null;
                        if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                            AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

                            builder.forNativeAd(
                                    new NativeAd.OnNativeAdLoadedListener() {
                                        // OnLoadedListener implementation.
                                        @Override
                                        public void onNativeAdLoaded(NativeAd nativeAd) {
                                            Log.d("TAGNATIVEAD", "root native error onNativeAdLoaded: AdMob");
                                            nativeAdLarge = nativeAd;
                                            adFinished.onAdFinished();
                                        }

                                    }).withAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                                }
                            });

                            AdLoader adLoader = builder.withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    adFinished.onAdFinished();
                                    Log.d("TAGNATIVEAD", "onAdFailedToLoad: AdMob : " + loadAdError.getMessage());

                                }
                            }).build();

                            adLoader.loadAd(new AdRequest.Builder().build());
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        Log.d("TAGNATIVEAD", "onAdLoaded: ");

                        // Race condition, load() called again before last ad was displayed
                        if (fbNativeAdNew == null || fbNativeAdNew != ad) {
                            return;
                        }
                        isFBNativeLoaded = true;
                        adFinished.onAdFinished();

                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                };

                fbNativeAdNew.loadAd(
                        fbNativeAdNew.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("mrec")) {
                fbBannerViewTop = new com.facebook.ads.AdView(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_mrec_id), com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
                com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        Log.d("TAGGGGGETop", "BanneronError: ");
                        fbBannerViewTop = null;
                        isFBNativeLoaded = false;
                        if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                            AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

                            builder.forNativeAd(
                                    new NativeAd.OnNativeAdLoadedListener() {
                                        // OnLoadedListener implementation.
                                        @Override
                                        public void onNativeAdLoaded(NativeAd nativeAd) {
                                            Log.d("TAGNATIVEAD", "root native error onNativeAdLoaded: AdMob");
                                            nativeAdLarge = nativeAd;
                                            adFinished.onAdFinished();
                                        }

                                    }).withAdListener(new AdListener() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                                }
                            });

                            AdLoader adLoader = builder.withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    adFinished.onAdFinished();
                                    Log.d("TAGNATIVEAD", "onAdFailedToLoad: AdMob : " + loadAdError.getMessage());

                                }
                            }).build();

                            adLoader.loadAd(new AdRequest.Builder().build());
                        } else {
                            adFinished.onAdFinished();
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        Log.d("TAGGGGGETop", "BanneronAdLoaded: ");
                        adFinished.onAdFinished();
                        isFBNativeLoaded = true;

                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                };
                fbBannerViewTop.loadAd(fbBannerViewTop.buildLoadAdConfig().withAdListener(adListener).build());
            }
        } else {
            Log.d("TAGNATIVEAD", "loading AdMob only : ");

            AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        // OnLoadedListener implementation.
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            Log.d("TAGNATIVEAD", "onNativeAdLoaded: ");
                            nativeAdLarge = nativeAd;
                            adFinished.onAdFinished();
                        }
                    }).withAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                }
            });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    adFinished.onAdFinished();
                    Log.d("TAGNATIVEAD", "only native failed : " + loadAdError.getMessage());

                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }

    }

    public static void showNativeAdLarge(Activity activity, ViewGroup viewGroup) {
        Log.d("NativeAdLog", "showNativeAdLarge: ");
        if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_ad_network).contains("facebook")) {
            Log.d("NativeAdLog", "native_ad_network: facebook ");

            if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("mrec")) {
                Log.d("NativeAdLog", "native_ad_network: facebook mrec");
                showFBMrecAd(activity, viewGroup);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("nativebanner")) {
                Log.d("NativeAdLog", "native_ad_network: facebook nativebanner");
                fbNativeBanner(activity, viewGroup);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("banner")) {
                Log.d("NativeAdLog", "native_ad_network: facebook banner");
                showFBBannerTop(activity, viewGroup);
            } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.top_ad_type).contains("native")) {
                Log.d("NativeAdLog", "native_ad_network: facebook native");
                if (isFBNativeLoaded) {
                    Log.d("TAGNATIVEAD", "showNativeAdLarge inflateAd: ");
                    inflateAd(activity, fbNativeAdNew, viewGroup, R.layout.native_ad_layout);
                } else {
                    showAdmobNative(activity, viewGroup);
                }
            }
        } else {
            if (nativeAdLarge != null) {
                try {
                    if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("large")) {
                        FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                    } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("med")) {
                        FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                    } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("small")) {
                        FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_small, null);
                    }

                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.displayed);
                    populateNativeAdView(activity, nativeAdLarge, FBadView);
                    viewGroup.removeAllViews();
                    viewGroup.addView(FBadView);
                    nativeAdLarge = null;
                    loadNativeAdLarge(activity);
                } catch (Exception e) {
                }
            } else {
                loadNativeAdLarge(activity);


                if (new PrefManagerVideo(activity).getString(SplashActivityScr.admob_failed_facebook_enabled).contains("true")) {

                    fbNativeAdNew = new com.facebook.ads.NativeAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_id));

                    NativeAdListener nativeAdListener = new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(Ad ad) {
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            Log.d("TAGNATIVEAD", "onError: " + adError.getErrorMessage());
                            fbNativeAdNew = null;


                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            Log.d("TAGNATIVEAD", "onAdLoaded: ");

                            // Race condition, load() called again before last ad was displayed
                            if (fbNativeAdNew == null || fbNativeAdNew != ad) {
                                return;
                            }
                            inflateAd(activity, fbNativeAdNew, viewGroup, R.layout.native_ad_layout);


                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    };

                    fbNativeAdNew.loadAd(
                            fbNativeAdNew.buildLoadAdConfig()
                                    .withAdListener(nativeAdListener)
                                    .build());
                }
            }
        }
    }

    private static void showAdmobNative(Activity activity, ViewGroup viewGroup) {

        if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
            if (nativeAdLarge != null) {
                Log.d("TAGNATIVEAD", "nativeAdLarge populateNativeAdView: ");

                try {

                    if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("large")) {
                        FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                    } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("med")) {
                        FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                    } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("small")) {
                        FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_small, null);
                    }

                    Utils.logEvents(activity, AdsWork.native_ad, AdsWork.displayed);
                    populateNativeAdView(activity, nativeAdLarge, FBadView);
                    viewGroup.removeAllViews();
                    viewGroup.addView(FBadView);
                    nativeAdLarge = null;

                    AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

                    builder.forNativeAd(
                            new NativeAd.OnNativeAdLoadedListener() {
                                // OnLoadedListener implementation.
                                @Override
                                public void onNativeAdLoaded(NativeAd nativeAd) {
                                    Log.d("TAGNATIVEAD", "showAdmobNative onNativeAdLoaded: AdMob");
                                    nativeAdLarge = nativeAd;
                                }

                            }).withAdListener(new AdListener() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                        }
                    });

                    AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d("TAGNATIVEAD", "onAdFailedToLoad: AdMob : " + loadAdError.getMessage());
                        }
                    }).build();

                    adLoader.loadAd(new AdRequest.Builder().build());

                } catch (Exception e) {
                }
            }
        }
    }

    private static void showFBMrecAd(Activity activity, ViewGroup viewGroup) {
        if (isFBNativeLoaded) {
            if (fbBannerViewTop.getParent() != null) {
                ((ViewGroup) fbBannerViewTop.getParent()).removeView(fbBannerViewTop); // <- fix
            }
            viewGroup.removeAllViews();
            viewGroup.addView(fbBannerViewTop);
            loadFBMrec(activity);
        } else {
            showAdmobNative(activity, viewGroup);
        }
    }

    private static void loadFBMrec(Activity activity) {
        fbBannerViewTop = new com.facebook.ads.AdView(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_mrec_id), com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAGGGGGETop", "BanneronError: ");
                fbBannerViewTop = null;
                isFBNativeLoaded = false;

                if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                    AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

                    builder.forNativeAd(
                            new NativeAd.OnNativeAdLoadedListener() {
                                // OnLoadedListener implementation.
                                @Override
                                public void onNativeAdLoaded(NativeAd nativeAd) {
                                    Log.d("TAGNATIVEAD", "loadFBMrec onNativeAdLoaded: AdMob");
                                    nativeAdLarge = nativeAd;
                                }

                            }).withAdListener(new AdListener() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                        }
                    });

                    AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d("TAGNATIVEAD", "onAdFailedToLoad: AdMob : " + loadAdError.getMessage());
                        }
                    }).build();

                    adLoader.loadAd(new AdRequest.Builder().build());
                }

            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAGGGGGETop", "BanneronAdLoaded: ");
                isFBNativeLoaded = true;
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        fbBannerViewTop.loadAd(fbBannerViewTop.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public static void showNativeAdSmall(Activity activity, ViewGroup viewGroup) {
        if (new PrefManagerVideo(activity).getString(SplashActivityScr.banner_ad_network).contains("facebook")) {
            showFBBanner(activity, viewGroup);
        } else {
            if (nativeAdSmall != null) {
                try {
                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_small, null);
                    Utils.logEvents(activity, AdsWork.small_native, AdsWork.displayed);

                    populateNativeAdViewSmall(nativeAdSmall, adView);
                    viewGroup.removeAllViews();
                    viewGroup.addView(adView);
                    nativeAdSmall = null;
                    loadNativeAdSMall(activity);
                } catch (Exception e) {
                }
            } else {
                loadNativeAdSMall(activity);

                if (new PrefManagerVideo(activity).getString(SplashActivityScr.admob_failed_facebook_enabled).contains("true")) {
                    fbBannerView = new com.facebook.ads.AdView(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                    com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                        @Override
                        public void onError(Ad ad, AdError adError) {
                            Log.d("TAGGGGGE", "BanneronError: ");
                            fbBannerView = null;
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            Log.d("TAGGGGGE", "BanneronAdLoaded: ");

                            if (fbBannerView.getParent() != null) {
                                ((ViewGroup) fbBannerView.getParent()).removeView(fbBannerView); // <- fix
                            }
                            viewGroup.removeAllViews();
                            viewGroup.addView(fbBannerView);
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    };
                    fbBannerView.loadAd(fbBannerView.buildLoadAdConfig().withAdListener(adListener).build());
                }


            }
        }

    }

    public static void loadNativeAdSMall(final Activity activity, AdsWork.AdFinished adFinished) {
        Log.d("TAGBANNER", "loadNativeAdSMall: " + new PrefManagerVideo(activity).getString(SplashActivityScr.banner_ad_network));
        if (new PrefManagerVideo(activity).getString(SplashActivityScr.banner_ad_network).contains("facebook")) {
            loadFBBanner(activity, adFinished);
        } else {
            Log.d("TAGBANNER", "loadNativeAdSMall: " + new PrefManagerVideo(activity).getString(SplashActivityScr.banner_ad_network));

            AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEIDSMALL));

            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        // OnLoadedListener implementation.
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            Log.d("TAGGGG", "onNativeAdLoadedSmall: ");
                            nativeAdSmall = nativeAd;
                            adFinished.onAdFinished();
                        }
                    }).withAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    Utils.logEvents(activity, AdsWork.small_native, AdsWork.clicked);
                }
            });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    nativeAdSmall = null;
                    Log.d("TAGGGG", "onAdFailedToLoad: " + loadAdError.getMessage());
                    adFinished.onAdFinished();


                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }

    }

    public static void showFBBannerTop(Activity activity, ViewGroup container) {
        Log.d("NativeAdLog", "showFBBannerTop");

        if (fbBannerViewTop != null) {
            Log.d("NativeAdLog", "showFBBanner Top: fbBannerView showing");

            if (fbBannerViewTop.getParent() != null) {
                ((ViewGroup) fbBannerViewTop.getParent()).removeView(fbBannerViewTop); // <- fix
            }
            container.removeAllViews();
            container.addView(fbBannerViewTop);
        } else {
            showAdmobNative(activity, container);

        }
    }

    public static void showFBBanner(Activity activity, ViewGroup container) {
        Log.d("NativeAdLog", "showFBBanner");

        if (fbBannerView != null) {
            Log.d("NativeAdLog", "showFBBanner : fbBannerView showing");

            if (fbBannerView.getParent() != null) {
                ((ViewGroup) fbBannerView.getParent()).removeView(fbBannerView);
            }
            container.removeAllViews();
            container.addView(fbBannerView);
        } else {

            if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                AdView adView = new AdView(activity);
                adView.setAdUnitId(new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_BANNERMAIN));
                adView.setAdSize(AdSize.BANNER);
                AdRequest.Builder builder = new AdRequest.Builder();
                adView.loadAd(builder.build());
                container.addView(adView);
            }

        }
    }

    public static void showBannerAd(Activity activity, ViewGroup linearLayout) {

        if (new PrefManagerVideo(activity).getString(SplashActivityScr.banner_ad_network).contains("facebook")) {
            showFBBanner(activity, linearLayout);
        } else {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_BANNERMAIN));
            adView.setAdSize(AdSize.BANNER);
            AdRequest.Builder builder = new AdRequest.Builder();
            adView.loadAd(builder.build());
            linearLayout.addView(adView);
        }

    }

    public static void showNativeAd(Activity activity, ViewGroup viewGroup, int layout) {
        if (layout == R.layout.ad_unified_small) {
            if (new PrefManagerVideo(activity).getString(SplashActivityScr.nativeid_small_enabled).contains("true")) {
                showNativeAdSmall(activity, viewGroup);
            } else {
                showBannerAd(activity, viewGroup);
            }
        } else {
            if (!new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID).contains("off")) {
                showNativeAdLarge(activity, viewGroup);
            }
        }

    }

    public static void loadFBBanner(Activity activity, AdsWork.AdFinished adFinished) {
        fbBannerView = new com.facebook.ads.AdView(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAGGGGGE", "BanneronError: ");
                fbBannerView = null;
                adFinished.onAdFinished();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                adFinished.onAdFinished();
                Log.d("TAGGGGGE", "BanneronAdLoaded: ");

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        fbBannerView.loadAd(fbBannerView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public static void loadFBBannerTop(Activity activity, AdsWork.AdFinished adFinished) {
        fbBannerViewTop = new com.facebook.ads.AdView(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAGGGGGETOP", "BanneronError: " + adError.getErrorMessage());
                fbBannerViewTop = null;
                if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                    AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));

                    builder.forNativeAd(
                            new NativeAd.OnNativeAdLoadedListener() {
                                // OnLoadedListener implementation.
                                @Override
                                public void onNativeAdLoaded(NativeAd nativeAd) {
                                    Log.d("TAGNATIVEAD", "loadFBBannerTop onNativeAdLoaded: AdMob");
                                    nativeAdLarge = nativeAd;
                                    adFinished.onAdFinished();
                                }

                            }).withAdListener(new AdListener() {
                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            Utils.logEvents(activity, AdsWork.native_ad, AdsWork.clicked);
                        }
                    });

                    AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            adFinished.onAdFinished();

                            Log.d("TAGNATIVEAD", "onAdFailedToLoad: AdMob : " + loadAdError.getMessage());
                        }
                    }).build();

                    adLoader.loadAd(new AdRequest.Builder().build());

                } else {
                    adFinished.onAdFinished();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                adFinished.onAdFinished();
                Log.d("TAGGGGGETOP", "BanneronAdLoaded: ");

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        fbBannerViewTop.loadAd(fbBannerViewTop.buildLoadAdConfig().withAdListener(adListener).build());
    }

    private static void loadFBNative(Activity activity, ViewGroup viewGroup) {
        fbNativeAdNew = new com.facebook.ads.NativeAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_id));

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d("TAGNATIVEAD", "onError: Loading AdMob Native " + adError.getErrorMessage());
                fbNativeAdNew = null;
                isFBNativeLoaded = false;
                if (nativeAdLarge != null) {
                    try {
                        if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("large")) {
                            FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                        } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("med")) {
                            FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_medium, null);
                        } else if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_size).contains("small")) {
                            FBadView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.ad_unified_small, null);
                        }

                        Utils.logEvents(activity, AdsWork.native_ad, AdsWork.displayed);
                        populateNativeAdView(activity, nativeAdLarge, FBadView);
                        viewGroup.removeAllViews();
                        viewGroup.addView(FBadView);
                        nativeAdLarge = null;
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("TAGNATIVEAD", "onAdLoaded: ");
                isFBNativeLoaded = true;

                // Race condition, load() called again before last ad was displayed
                if (fbNativeAdNew == null || fbNativeAdNew != ad) {
                    return;
                }

            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        fbNativeAdNew.loadAd(
                fbNativeAdNew.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private static void loadNativeAdLarge(final Activity activity) {
        if (new PrefManagerVideo(activity).getString(SplashActivityScr.native_ad_network).contains("facebook")) {

            fbNativeAdNew = new com.facebook.ads.NativeAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_id));

            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    isFBNativeLoaded = false;
                    AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));
                    builder.forNativeAd(
                            new NativeAd.OnNativeAdLoadedListener() {
                                // OnLoadedListener implementation.
                                @Override
                                public void onNativeAdLoaded(NativeAd nativeAd) {
                                    Log.d("TAGNATIVEAD", "onNativeAdLoaded: ");
                                    nativeAdLarge = nativeAd;
                                }
                            });

                    AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                        }
                    }).build();

                    adLoader.loadAd(new AdRequest.Builder().build());
                    Log.d("TAGNATIVEAD", "onError: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d("TAGNATIVEAD", "onAdLoaded: ");
                    isFBNativeLoaded = true;

                    // Race condition, load() called again before last ad was displayed
                    if (fbNativeAdNew == null || fbNativeAdNew != ad) {
                        return;
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };

            fbNativeAdNew.loadAd(
                    fbNativeAdNew.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
        } else {

            AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEID));
            builder.forNativeAd(
                    new NativeAd.OnNativeAdLoadedListener() {
                        // OnLoadedListener implementation.
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            Log.d("TAGNATIVEAD", "onNativeAdLoaded: ");
                            nativeAdLarge = nativeAd;
                        }
                    });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.d("TAGNATIVEAD", "OA - onError: " + loadAdError.getMessage());


                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }


    }

    private static void loadNativeAdSMall(final Activity activity) {
        AdLoader.Builder builder = new AdLoader.Builder(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_NATIVEIDSMALL));

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        Log.d("TAGGGG", "onNativeAdLoaded: ");
                        nativeAdSmall = nativeAd;
                    }
                });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                nativeAdSmall = null;
                Log.d("TAGGGG", "onAdFailedToLoad: " + loadAdError.getMessage());

            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void populateNativeAdViewSmall(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a vid asset.
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete vid playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        }
    }

    private static void populateNativeAdView(Activity activity, NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a vid asset.
        if (vc.hasVideoContent()) {


            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete vid playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        }
    }

    private static void inflateAd(Activity activity, com.facebook.ads.NativeAd nativeAd, ViewGroup container, int layout) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        RelativeLayout lnAdView = (RelativeLayout) inflater.inflate(layout, nativeAdLayout, false);
        container.addView(lnAdView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = lnAdView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        com.facebook.ads.MediaView nativeAdIcon = lnAdView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = lnAdView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = lnAdView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = lnAdView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = lnAdView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = lnAdView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = lnAdView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                lnAdView, nativeAdMedia, nativeAdIcon, clickableViews);

        loadNativeAdLarge(activity);
    }

    public static void fbNativeBanner(final Activity activity, ViewGroup layout) {

        nativeBannerAd = new NativeBannerAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_native_banner));
        NativeAdListener nativeAdListener = new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                bannerInflateAd(activity, nativeBannerAd, layout);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        };
        // load the ad
        nativeBannerAd.loadAd(
                nativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());

    }

    public static void bannerInflateAd(final Activity activity, NativeBannerAd nativeBannerAd, ViewGroup container) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        RelativeLayout nativeBnnaer = (RelativeLayout) inflater.inflate(R.layout.native_banner_ad, nativeAdLayout, false);
        container.addView(nativeBnnaer);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = nativeBnnaer.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = nativeBnnaer.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = nativeBnnaer.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = nativeBnnaer.findViewById(R.id.native_ad_sponsored_label);
        com.facebook.ads.MediaView nativeAdIconView = nativeBnnaer.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = nativeBnnaer.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(nativeBnnaer, nativeAdIconView, clickableViews);
    }
}

