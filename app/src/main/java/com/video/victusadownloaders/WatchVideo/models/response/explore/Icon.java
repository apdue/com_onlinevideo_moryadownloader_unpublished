package com.video.victusadownloaders.WatchVideo.models.response.explore;

import com.google.gson.annotations.SerializedName;

public class Icon{

	@SerializedName("iconType")
	private String iconType;

	public String getIconType(){
		return iconType;
	}

	@Override
 	public String toString(){
		return 
			"Icon{" + 
			"iconType = '" + iconType + '\'' + 
			"}";
		}
}