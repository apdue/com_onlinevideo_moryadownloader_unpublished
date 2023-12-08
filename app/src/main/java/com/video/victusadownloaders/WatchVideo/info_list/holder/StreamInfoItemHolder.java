package com.video.victusadownloaders.WatchVideo.info_list.holder;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.video.victusadownloaders.WatchVideo.info_list.InfoItemBuilder;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

import io.awesome.gagtube.R;

import com.video.victusadownloaders.WatchVideo.util.Localization;

public class StreamInfoItemHolder extends StreamMiniInfoItemHolder {
	
	public final TextView itemAdditionalDetails;
	
	public StreamInfoItemHolder(InfoItemBuilder infoItemBuilder, ViewGroup parent) {
		super(infoItemBuilder, R.layout.list_stream_item_medium, parent);
		itemAdditionalDetails = itemView.findViewById(R.id.itemAdditionalDetails);
	}
	
	@Override
	public void updateFromItem(final InfoItem infoItem) {
		super.updateFromItem(infoItem);
		
		if (!(infoItem instanceof StreamInfoItem)) return;
		final StreamInfoItem item = (StreamInfoItem) infoItem;
		
		itemAdditionalDetails.setText(getStreamInfoDetail(item));
	}
	
	private String getStreamInfoDetail(final StreamInfoItem infoItem) {
		String detailInfo = infoItem.getUploaderName() + Localization.DOT_SEPARATOR;
		if (infoItem.getViewCount() >= 0) {
			detailInfo = detailInfo + Localization.shortViewCount(itemBuilder.getContext(), infoItem.getViewCount());
		}
		
		final String uploadDate = getFormattedRelativeUploadDate(infoItem);
		if (!TextUtils.isEmpty(uploadDate)) {
			return Localization.concatenateStrings(detailInfo, uploadDate);
		}
		return detailInfo;
	}
	
	private String getFormattedRelativeUploadDate(final StreamInfoItem infoItem) {
		if (infoItem.getUploadDate() != null) {
			return Localization.relativeTime(infoItem.getUploadDate().date());
		}
		else {
			return infoItem.getTextualUploadDate();
		}
	}
}
