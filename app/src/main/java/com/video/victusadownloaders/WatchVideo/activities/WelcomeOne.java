package com.video.victusadownloaders.WatchVideo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import io.awesome.gagtube.R;
public class WelcomeOne extends AppCompatActivity {
    private View getstarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_one);
    }


//    View.OnClickListener(new View.OnClickListener) {
//        startActivity(new Intent(WelcomeOne.this, InterstScreen.class));
//    }
    private void initViews() {
        getstarted = findViewById(R.id.getstarted);
    }
}