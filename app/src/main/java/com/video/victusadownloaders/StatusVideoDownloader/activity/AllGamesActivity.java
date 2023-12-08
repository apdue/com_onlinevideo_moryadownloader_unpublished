package com.video.victusadownloaders.StatusVideoDownloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import io.awesome.gagtube.R;
import io.awesome.gagtube.databinding.ActivityAllGamesBinding;


public class AllGamesActivity extends AppCompatActivity {
    ActivityAllGamesBinding binding;
    AllGamesActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_games);
        activity = this;
     //   LoadNativeAd();
        initViews();
    }



    private void initViews() {
        binding.imBack.setOnClickListener(v -> onBackPressed());
        binding.RL2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, GamesPlayActivity.class);
                i.putExtra("url","2048");
                startActivity(i);
            }
        });

    }
}