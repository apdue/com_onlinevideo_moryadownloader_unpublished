package com.video.victusadownloaders.WatchVideo.player.event;

import com.video.victusadownloaders.WatchVideo.player.MainPlayer;
import com.video.victusadownloaders.WatchVideo.player.VideoPlayerImpl;

public interface PlayerServiceExtendedEventListener extends PlayerServiceEventListener {

    void onServiceConnected(VideoPlayerImpl player, MainPlayer playerService, boolean playAfterConnect);

    void onServiceDisconnected();
}
