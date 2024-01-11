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

public class DummyThreeScrRand extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_three);

        if (new PrefManagerVideo(this).getString(SplashActivityScr.dummy_three_screen).contains("ad")) {
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
        if (new PrefManagerVideo(DummyThreeScrRand.this).getString(SplashActivityScr.status_dummy_four_enabled).contains("true")) {
            intent = new Intent(DummyThreeScrRand.this, DummyFourScrRand.class);
        }
        else if (new PrefManagerVideo(DummyThreeScrRand.this).getString(SplashActivityScr.status_dummy_five_enabled).contains("true")) {
            intent = new Intent(DummyThreeScrRand.this, DummyFiveScrRand.class);
        }
        else if (new PrefManagerVideo(DummyThreeScrRand.this).getString(SplashActivityScr.status_dummy_six_enabled).contains("true")) {
            intent = new Intent(DummyThreeScrRand.this, DummySixScrRand.class);
        }else {
            intent = new Intent(DummyThreeScrRand.this, HomeActivityScrRand.class);
        }
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
        if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_two_back_enabled).contains("true")) {
            intent = new Intent(this, DummyTwoScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_one_back_enabled).contains("true")) {
            intent = new Intent(this, DummyOneScrRand.class);
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