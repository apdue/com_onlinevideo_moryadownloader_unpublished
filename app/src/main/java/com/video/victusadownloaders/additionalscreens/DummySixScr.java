package com.video.victusadownloaders.additionalscreens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.WatchVideo.activities.ExitActivity;
import com.video.victusadownloaders.utilities.AdsWork;
import com.video.victusadownloaders.utilities.NativeAdNew;

import io.awesome.gagtube.R;


public class DummySixScr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_six);

        if (new PrefManagerVideo(this).getString(SplashActivityScr.dummy_six_screen).contains("ad")) {
            NativeAdNew.showNativeAd(this, findViewById(R.id.nativeAd), R.layout.ad_unified_medium);
        }
        AdsWork.loadNativeAd(this, findViewById(R.id.nativeAdSmall), R.layout.ad_unified_small);

        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity();
            }
        });

    }

    private void startActivity() {
        Intent intent;

        intent = new Intent(DummySixScr.this, HomeActivityScr.class);
        AdsWork.showInterAds(this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_five_enabled).contains("true")) {
            intent = new Intent(this, DummyFiveScr.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_four_back_enabled).contains("true")) {
            intent = new Intent(this, DummyFourScr.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_three_back_enabled).contains("true")) {
            intent = new Intent(this, DummyThreeScr.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_two_back_enabled).contains("true")) {
            intent = new Intent(this, DummyTwoScr.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_one_back_enabled).contains("true")) {
            intent = new Intent(this, DummyOneScr.class);
        } else {
            intent = new Intent(this, ExitActivity.class);
        }
        AdsWork.showInterAds(this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                startActivity(intent);

            }
        });

    }
}