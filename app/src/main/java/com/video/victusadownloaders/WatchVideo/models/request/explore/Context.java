package com.video.victusadownloaders.WatchVideo.models.request.explore;

import android.app.Activity;

import com.google.gson.annotations.SerializedName;

public class Context extends Activity {

	@SerializedName("client")
	public Client client;
}