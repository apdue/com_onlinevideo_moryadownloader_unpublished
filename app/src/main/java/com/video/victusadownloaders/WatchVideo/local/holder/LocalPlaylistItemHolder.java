package com.video.victusadownloaders.WatchVideo.local.holder;

import android.view.View;
import android.view.ViewGroup;

import com.video.victusadownloaders.WatchVideo.AppNew;
import com.video.victusadownloaders.WatchVideo.database.LocalItem;
import com.video.victusadownloaders.WatchVideo.database.playlist.PlaylistMetadataEntry;
import com.video.victusadownloaders.WatchVideo.local.LocalItemBuilder;
import com.video.victusadownloaders.WatchVideo.util.GlideUtils;
import com.video.victusadownloaders.WatchVideo.util.Localization;

import java.text.DateFormat;

import io.awesome.gagtube.R;

public class LocalPlaylistItemHolder extends PlaylistItemHolder {
	
	public LocalPlaylistItemHolder(LocalItemBuilder infoItemBuilder, ViewGroup parent) {
		
		super(infoItemBuilder, parent);
	}
	
	@Override
	public void updateFromItem(final LocalItem localItem, final DateFormat dateFormat) {
		
		super.updateFromItem(localItem, dateFormat);
		
		if (!(localItem instanceof PlaylistMetadataEntry)) return;
		final PlaylistMetadataEntry item = (PlaylistMetadataEntry) localItem;
		
		itemTitleView.setText(item.name);
		itemStreamCountView.setText(Localization.localizeStreamCount(itemStreamCountView.getContext(), item.streamCount));
		itemUploaderView.setText(R.string.me);
		
		GlideUtils.loadThumbnail(AppNew.getAppContext(), itemThumbnailView, item.thumbnailUrl);
		itemMoreActions.setVisibility(itemBuilder.isShowOptionMenu() ? View.VISIBLE : View.GONE);
	}
}
