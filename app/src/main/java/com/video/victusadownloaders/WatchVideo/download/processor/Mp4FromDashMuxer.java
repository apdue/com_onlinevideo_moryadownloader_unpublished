package com.video.victusadownloaders.WatchVideo.download.processor;

import com.video.victusadownloaders.WatchVideo.streams.Mp4FromDashWriter;
import com.video.victusadownloaders.WatchVideo.streams.io.SharpStream;

import java.io.IOException;

class Mp4FromDashMuxer extends PostProcessing {
	
	Mp4FromDashMuxer() {
		super(true, true, ALGORITHM_MP4_FROM_DASH_MUXER);
	}
	
	@Override
	int process(SharpStream out, SharpStream... sources) throws IOException {
		Mp4FromDashWriter muxer = new Mp4FromDashWriter(sources);
		muxer.parseSources();
		muxer.selectTracks(0, 0);
		muxer.build(out);
		
		return OK_RESULT;
	}
	
}
