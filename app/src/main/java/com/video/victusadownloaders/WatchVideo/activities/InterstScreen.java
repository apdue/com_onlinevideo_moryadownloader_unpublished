package com.video.victusadownloaders.WatchVideo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import io.awesome.gagtube.R;

public class InterstScreen extends AppCompatActivity {
    private Button getstarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interst_screen);
    }
    private void initViews() {
        getstarted = findViewById(R.id.getstarted);
    }

}