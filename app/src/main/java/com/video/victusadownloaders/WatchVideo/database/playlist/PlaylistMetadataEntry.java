package com.video.victusadownloaders.WatchVideo.database.playlist;

import androidx.room.ColumnInfo;

import com.video.victusadownloaders.WatchVideo.database.LocalItem;

import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistEntity;

public class PlaylistMetadataEntry implements PlaylistLocalItem {
    
    final public static String PLAYLIST_STREAM_COUNT = "streamCount";

    @ColumnInfo(name = PlaylistEntity.PLAYLIST_ID)
    final public long uid;
    @ColumnInfo(name = PlaylistEntity.PLAYLIST_NAME)
    final public String name;
    @ColumnInfo(name = PlaylistEntity.PLAYLIST_THUMBNAIL_URL)
    final public String thumbnailUrl;
    @ColumnInfo(name = PLAYLIST_STREAM_COUNT)
    final public long streamCount;

    public PlaylistMetadataEntry(long uid, String name, String thumbnailUrl, long streamCount) {
        this.uid = uid;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.streamCount = streamCount;
    }

    @Override
    public LocalItemType getLocalItemType() {
        return LocalItem.LocalItemType.PLAYLIST_LOCAL_ITEM;
    }

    @Override
    public String getOrderingName() {
        return name;
    }
}
