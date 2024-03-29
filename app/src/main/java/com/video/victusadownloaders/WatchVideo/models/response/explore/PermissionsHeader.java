package com.video.victusadownloaders.WatchVideo.models.response.explore;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PermissionsHeader{

	@SerializedName("runs")
	private List<RunsItem> runs;

	public List<RunsItem> getRuns(){
		return runs;
	}

	@Override
 	public String toString(){
		return 
			"PermissionsHeader{" + 
			"runs = '" + runs + '\'' + 
			"}";
		}
}