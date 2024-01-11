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
import com.video.victusadownloaders.utilities.ApplicationClassFonts;
import com.video.victusadownloaders.utilities.NativeAdNew;

import io.awesome.gagtube.R;

public class DummyOneScrRand extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ApplicationClassFonts.hideStatus(this);
        setContentView(R.layout.activity_screen_one);

        if (new PrefManagerVideo(this).getString(SplashActivityScr.dummy_one_screen).contains("ad")){
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

    private void startActivity(){
        Intent intent;
        if (new PrefManagerVideo(DummyOneScrRand.this).getString(SplashActivityScr.status_dummy_two_enabled).contains("true")) {
            intent = new Intent(DummyOneScrRand.this, DummyTwoScrRand.class);
        } else if (new PrefManagerVideo(DummyOneScrRand.this).getString(SplashActivityScr.status_dummy_three_enabled).contains("true")) {
            intent = new Intent(DummyOneScrRand.this, DummyThreeScrRand.class);
        } else if (new PrefManagerVideo(DummyOneScrRand.this).getString(SplashActivityScr.status_dummy_four_enabled).contains("true")) {
            intent = new Intent(DummyOneScrRand.this, DummyFourScrRand.class);
        }
        else if (new PrefManagerVideo(DummyOneScrRand.this).getString(SplashActivityScr.status_dummy_five_enabled).contains("true")) {
            intent = new Intent(DummyOneScrRand.this, DummyFiveScrRand.class);
        }
        else if (new PrefManagerVideo(DummyOneScrRand.this).getString(SplashActivityScr.status_dummy_six_enabled).contains("true")) {
            intent = new Intent(DummyOneScrRand.this, DummySixScrRand.class);
        }
        else {
            intent = new Intent(DummyOneScrRand.this, HomeActivityScrRand.class);
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

        AdsWork.showInterAds(this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                startActivity(new Intent(DummyOneScrRand.this, ExitActivity.class));
                finish();
            }
        });

    }
}