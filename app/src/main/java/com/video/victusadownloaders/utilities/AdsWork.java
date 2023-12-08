package com.video.victusadownloaders.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;

public class AdsWork {

    private static boolean isLoadedPre = false;
    private static boolean isLoaded = false;
    private static boolean isLoading = false;
    private static boolean isFBLoading = false;
    private static InterstitialAd interstitialAd;
    private static InterstitialAd interstitialAdPre;
    public static String interstitial = "_Interstitial_Ad";
    public static String native_ad = "_Native_Ad";
    public static String small_native = "_Small_Native_Ad";
    public static String displayed = "_Displayed";
    public static String clicked = "_Clicked";
    public static String closed = "_Closed";

    public static com.facebook.ads.InterstitialAd fbInterstitialAd;

    private static int adCount = 1;

    private static int type = 0;

    public static void showInterAds(final Activity activity, AdFinished adFinished) {

        int target = Integer.parseInt(new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_ADMOB_INTERSTITIAL_FREQUENCY));
        if (adCount == target) {
            adCount = 1;

            if (new PrefManagerVideo(activity).getString(SplashActivityScr.interstitial_ad_network).contains("facebook")) {
                showFacebookInterstitialAd(activity, adFinished);
            } else {

                if (new PrefManagerVideo(activity).getString(SplashActivityScr.inter_ad_type).contains("open")) {
                    if (type == 0) {
                        AppOpenManagerTwo.fetchAd(activity, adFinished);
                        type = 1;
                    } else {
                        showDelayedInterstitial(activity, adFinished);
                        type = 0;
                    }
                } else {
                    showDelayedInterstitial(activity, adFinished);
                }
            }


        } else {
            adCount++;
            adFinished.onAdFinished();
        }
        Log.d("ADMOB", ": " + adCount);
    }

    public static void showDelayedInterstitial(final Activity activity, AdFinished adFinished) {

        if (isLoadedPre) {
            Utils.logEvents(activity, interstitial, displayed);
            interstitialAdPre.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    Utils.logEvents(activity, interstitial, closed);

                    adFinished.onAdFinished();
                    isLoadedPre = false;
                    loadInterstitialAd(activity);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    Utils.logEvents(activity, interstitial, clicked);

                }
            });
            interstitialAdPre.show(activity);

        } else {
            isLoadedPre = false;
            loadInterstitialAd(activity);

            if (!isLoading) {
                isLoading = true;

                ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage("Loading Ad...");
                if (!((Activity) activity).isFinishing()) {
                    progressDialog.show();
                }
                progressDialog.setCancelable(false);

                AdRequest adRequestNormal = new AdRequest.Builder().build();
                InterstitialAd.load(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.TAG_INTERSTITIALMAIN), adRequestNormal,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                AdsWork.interstitialAd = interstitialAd;
                                isLoaded = true;
                                if (progressDialog!=null && progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                interstitialAd.show(activity);
                                Utils.logEvents(activity, interstitial, displayed);

                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        adFinished.onAdFinished();
                                        Utils.logEvents(activity, interstitial, closed);

                                        isLoaded = false;
                                        isLoading = false;
                                    }

                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                        Utils.logEvents(activity, interstitial, clicked);

                                    }
                                });

                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                interstitialAd = null;
                                isLoading = false;
                                if (progressDialog!=null && progressDialog.isShowing()){
                                   // progressDialog.dismiss();
                                }
                                Log.d("ADMOB", "Failed to load because: " + loadAdError.getMessage());
                            }
                        });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isLoaded) {
                            if (interstitialAd != null) {
                                progressDialog.dismiss();
                                interstitialAd.show(activity);
                                Utils.logEvents(activity, interstitial, displayed);

                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent();
                                        Utils.logEvents(activity, interstitial, closed);

                                        adFinished.onAdFinished();
                                        isLoading = false;
                                    }

                                    @Override
                                    public void onAdClicked() {
                                        super.onAdClicked();
                                        Utils.logEvents(activity, interstitial, clicked);

                                    }
                                });
                            } else {
                                if (new PrefManagerVideo(activity).getString(SplashActivityScr.admob_failed_facebook_enabled).contains("true")) {
                                    showFacebookInterstitialAd(activity, new AdFinished() {
                                        @Override
                                        public void onAdFinished() {
                                            isLoading = false;
                                            if (progressDialog!=null && progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            adFinished.onAdFinished();
                                        }
                                    });
                                } else {
                                    isLoading = false;
                                    if (progressDialog!=null && progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    adFinished.onAdFinished();
                                }


                            }
                        }
                    }
                }, 1000 * new PrefManagerVideo(activity).getInt(SplashActivityScr.interstitial_loader));
            }
        }

    }

    public static void loadInterstitialAd(final Context context) {
        AdRequest adRequestNormal = new AdRequest.Builder().build();
        InterstitialAd.load(context, new PrefManagerVideo(context).getString(SplashActivityScr.TAG_INTERSTITIALMAIN), adRequestNormal,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        AdsWork.interstitialAdPre = interstitialAd;
                        isLoadedPre = true;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        interstitialAdPre = null;
                        Log.d("ADMOB", "Failed to load because: " + loadAdError.getMessage());
                        isLoadedPre = false;
                    }
                });

    }

    public static void loadNativeAd(Activity activity, ViewGroup viewById, int layout) {
        NativeAdNew.showNativeAd(activity, viewById, layout);
    }

    public static void showFacebookInterstitialAd(Activity activity, AdFinished adFinished) {
        if (!isFBLoading) {
            isFBLoading = true;

            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Loading Ad...");
            if (!((Activity) activity).isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setCancelable(false);

            fbInterstitialAd = new com.facebook.ads.InterstitialAd(activity, new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_interstitial_id));
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.d("TAGFB", "Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.d("TAGFB", "Interstitial ad dismissed.");
                    adFinished.onAdFinished();
                    isFBLoading = false;
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    isFBLoading = false;
                    if (new PrefManagerVideo(activity).getString(SplashActivityScr.facebook_failed_admob_enabled).contains("true")) {
                        showDelayedInterstitial(activity, adFinished);
                    } else {
                        adFinished.onAdFinished();
                    }

                    Log.d("TAGFB", "Interstitial ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d("TAGFB", "Interstitial ad is loaded and ready to be displayed!");
                    // Show the ad
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }




                    fbInterstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                    Log.d("TAGFB", "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                    Log.d("TAGFB", "Interstitial ad impression logged!");
                }
            };

            // For auto play vid ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            fbInterstitialAd.loadAd(
                    fbInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());
        }


    }

    public interface AdFinished {
        void onAdFinished();
    }

}


