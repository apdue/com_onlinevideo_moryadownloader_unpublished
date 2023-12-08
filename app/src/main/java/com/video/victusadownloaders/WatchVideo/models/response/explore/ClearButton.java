package com.video.victusadownloaders.WatchVideo.models.response.explore;

import com.google.gson.annotations.SerializedName;

public class ClearButton{

	@SerializedName("buttonRenderer")
	private ButtonRenderer buttonRenderer;

	public ButtonRenderer getButtonRenderer(){
		return buttonRenderer;
	}

	@Override
 	public String toString(){
		return 
			"ClearButton{" + 
			"buttonRenderer = '" + buttonRenderer + '\'' + 
			"}";
		}
}