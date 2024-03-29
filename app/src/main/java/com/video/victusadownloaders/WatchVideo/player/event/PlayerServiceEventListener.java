package com.video.victusadownloaders.WatchVideo.player.event;

import com.google.android.exoplayer2.ExoPlaybackException;

public interface PlayerServiceEventListener extends PlayerEventListener {

    void onFullscreenStateChanged(boolean fullscreen);

    void onScreenRotationButtonClicked();

    void onPlayerError(ExoPlaybackException error);

    void hideSystemUiIfNeeded();
}
