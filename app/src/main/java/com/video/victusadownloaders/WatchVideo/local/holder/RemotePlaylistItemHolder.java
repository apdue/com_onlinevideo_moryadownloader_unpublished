package com.video.victusadownloaders.WatchVideo.local.holder;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.video.victusadownloaders.WatchVideo.AppNew;
import com.video.victusadownloaders.WatchVideo.database.LocalItem;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistRemoteEntity;
import com.video.victusadownloaders.WatchVideo.local.LocalItemBuilder;
import com.video.victusadownloaders.WatchVideo.util.GlideUtils;
import com.video.victusadownloaders.WatchVideo.util.Localization;

import org.schabi.newpipe.extractor.ServiceList;

import java.text.DateFormat;

public class RemotePlaylistItemHolder extends PlaylistItemHolder {
	
	public RemotePlaylistItemHolder(LocalItemBuilder infoItemBuilder, ViewGroup parent) {
		
		super(infoItemBuilder, parent);
	}
	
	@Override
	public void updateFromItem(final LocalItem localItem, final DateFormat dateFormat) {
		
		super.updateFromItem(localItem, dateFormat);
		
		if (!(localItem instanceof PlaylistRemoteEntity)) return;
		final PlaylistRemoteEntity item = (PlaylistRemoteEntity) localItem;
		
		itemTitleView.setText(item.getName());
		itemStreamCountView.setText(Localization.localizeStreamCount(itemStreamCountView.getContext(), item.getStreamCount()));
		if (!TextUtils.isEmpty(item.getUploader())) {
			itemUploaderView.setText(item.getUploader());
		}
		else {
			itemUploaderView.setText(ServiceList.YouTube.getServiceInfo().getName());
		}
		
		GlideUtils.loadThumbnail(AppNew.getAppContext(), itemThumbnailView, item.getThumbnailUrl());
	}
}
