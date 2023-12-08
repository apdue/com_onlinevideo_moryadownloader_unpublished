package com.video.victusadownloaders.WatchVideo.models.response.explore;

import com.google.gson.annotations.SerializedName;

public class OptionsItem{

	@SerializedName("hotkeyDialogSectionOptionRenderer")
	private HotkeyDialogSectionOptionRenderer hotkeyDialogSectionOptionRenderer;

	public HotkeyDialogSectionOptionRenderer getHotkeyDialogSectionOptionRenderer(){
		return hotkeyDialogSectionOptionRenderer;
	}

	@Override
 	public String toString(){
		return 
			"OptionsItem{" + 
			"hotkeyDialogSectionOptionRenderer = '" + hotkeyDialogSectionOptionRenderer + '\'' + 
			"}";
		}
}