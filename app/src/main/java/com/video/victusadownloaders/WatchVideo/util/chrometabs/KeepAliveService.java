package com.video.victusadownloaders.WatchVideo.util.chrometabs;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class KeepAliveService extends Service {
	
	private static final Binder sBinder = new Binder();
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return sBinder;
	}
}