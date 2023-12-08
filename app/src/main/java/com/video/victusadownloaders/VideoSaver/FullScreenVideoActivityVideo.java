package com.video.victusadownloaders.VideoSaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dailymotion.android.player.sdk.PlayerWebView;
import com.dailymotion.android.player.sdk.PlayerWebView.PlayerEventListener;
import com.dailymotion.android.player.sdk.events.FullScreenToggleRequestedEvent;
import com.dailymotion.android.player.sdk.events.PlayerEvent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmVideo;

public class FullScreenVideoActivityVideo extends AppCompatActivity {
    @BindView(R.id.playerViewFullScreenActivity)
    PlayerWebView mPlayerWebView;
    private DmVideo mVideo;

    public static void start(Context context, DmVideo dmVideo) {
        Intent intent = new Intent(context, FullScreenVideoActivityVideo.class);
        intent.putExtra("video", dmVideo);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.xactivity_full_screen_video);
        ButterKnife.bind(this);
        HashMap hashMap = new HashMap();
        this.mPlayerWebView.setFullscreenButton(true);
        this.mPlayerWebView.load(getVideo().getId(), hashMap);
        this.mPlayerWebView.play();
        this.mPlayerWebView.setPlayerEventListener(new PlayerEventListener() {
            public void onEvent(PlayerEvent playerEvent) {
                if (playerEvent instanceof FullScreenToggleRequestedEvent) {
                    FullScreenVideoActivityVideo.this.onBackPressed();
                }
            }
        });
    }

    public DmVideo getVideo() {
        if (this.mVideo == null) {
            this.mVideo = (DmVideo) getIntent().getSerializableExtra("video");
        }
        return this.mVideo;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
