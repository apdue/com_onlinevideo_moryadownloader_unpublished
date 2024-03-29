package com.video.victusadownloaders.WatchVideo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.SplashActivityScr;
import com.video.victusadownloaders.additionalscreens.DummyFiveScrRand;
import com.video.victusadownloaders.additionalscreens.DummyFourScrRand;
import com.video.victusadownloaders.additionalscreens.DummyOneScrRand;
import com.video.victusadownloaders.additionalscreens.DummySixScrRand;
import com.video.victusadownloaders.additionalscreens.DummyThreeScrRand;
import com.video.victusadownloaders.additionalscreens.DummyTwoScrRand;
import com.video.victusadownloaders.utilities.AdsWork;
import com.video.victusadownloaders.utilities.NativeAdNew;

import io.awesome.gagtube.R;

public class ExitActivity extends AppCompatActivity {

    Button NoCard, yesCard;
    PrefManagerVideo prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        prf = new PrefManagerVideo(ExitActivity.this);
        initviews();

        if (prf.getString(SplashActivityScr.exit_screen).contains("ad")) {
            findViewById(R.id.ivAd).setVisibility(View.GONE);
            NativeAdNew.showNativeAd(this, findViewById(R.id.nativeAd), R.layout.ad_unified_medium);
        }

        AdsWork.loadNativeAd(this, findViewById(R.id.nativeAdSmall), R.layout.ad_unified_small);

        NoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity();
            }
        });

        yesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity();
    }

    private void startActivity(){
        Intent intent;
        if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_one_enabled).contains("true")) {
            intent = new Intent(this, DummyOneScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_two_enabled).contains("true")) {
            intent = new Intent(this, DummyTwoScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_three_enabled).contains("true")) {
            intent = new Intent(this, DummyThreeScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_four_enabled).contains("true")) {
            intent = new Intent(this, DummyFourScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_five_enabled).contains("true")) {
            intent = new Intent(this, DummyFiveScrRand.class);
        } else if (new PrefManagerVideo(this).getString(SplashActivityScr.status_dummy_six_enabled).contains("true")) {
            intent = new Intent(this, DummySixScrRand.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        AdsWork.showInterAds(this, new AdsWork.AdFinished() {
            @Override
            public void onAdFinished() {
                startActivity(intent);
            }
        });
    }

    private void initviews() {
        yesCard = findViewById(R.id.yesCard);
        NoCard = findViewById(R.id.NoCard);
    }
}