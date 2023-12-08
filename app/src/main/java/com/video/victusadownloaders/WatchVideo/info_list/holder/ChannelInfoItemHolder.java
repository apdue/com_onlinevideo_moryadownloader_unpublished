package com.video.victusadownloaders.WatchVideo.info_list.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.video.victusadownloaders.WatchVideo.info_list.InfoItemBuilder;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.channel.ChannelInfoItem;

import io.awesome.gagtube.R;

import com.video.victusadownloaders.WatchVideo.util.Localization;

public class ChannelInfoItemHolder extends ChannelMiniInfoItemHolder {
    
    public final TextView itemChannelDescriptionView;

    public ChannelInfoItemHolder(InfoItemBuilder infoItemBuilder, ViewGroup parent) {
       
        super(infoItemBuilder, R.layout.list_channel_item, parent);
        itemChannelDescriptionView = itemView.findViewById(R.id.itemChannelDescriptionView);
    }

    @Override
    public void updateFromItem(final InfoItem infoItem) {
        super.updateFromItem(infoItem);

        if (!(infoItem instanceof ChannelInfoItem)) return;
        final ChannelInfoItem item = (ChannelInfoItem) infoItem;

        itemChannelDescriptionView.setText(item.getDescription());
    }

    @Override
    protected String getDetailLine(final ChannelInfoItem item) {
        String details = super.getDetailLine(item);

        if (item.getStreamCount() >= 0) {
            String formattedVideoAmount = Localization.localizeStreamCount(itemBuilder.getContext(), item.getStreamCount());

            if (!details.isEmpty()) {
                details += " • " + formattedVideoAmount;
            } else {
                details = formattedVideoAmount;
            }
        }
        return details;
    }
}
