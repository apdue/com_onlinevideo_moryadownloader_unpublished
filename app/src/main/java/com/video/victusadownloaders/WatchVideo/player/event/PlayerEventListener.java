package com.video.victusadownloaders.WatchVideo.player.event;

import com.video.victusadownloaders.WatchVideo.player.playqueue.PlayQueue;
import com.google.android.exoplayer2.PlaybackParameters;

import org.schabi.newpipe.extractor.stream.StreamInfo;

public interface PlayerEventListener {

    void onQueueUpdate(PlayQueue queue);

    void onPlaybackUpdate(int state, int repeatMode, boolean shuffled, PlaybackParameters parameters);

    void onProgressUpdate(int currentProgress, int duration, int bufferPercent);

    void onMetadataUpdate(StreamInfo info, PlayQueue queue);

    void onServiceStopped();
}
