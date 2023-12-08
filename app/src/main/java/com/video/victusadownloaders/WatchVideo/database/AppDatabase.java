package com.video.victusadownloaders.WatchVideo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.video.victusadownloaders.WatchVideo.database.history.dao.SearchHistoryDAO;
import com.video.victusadownloaders.WatchVideo.database.history.dao.StreamHistoryDAO;
import com.video.victusadownloaders.WatchVideo.database.history.model.SearchHistoryEntry;
import com.video.victusadownloaders.WatchVideo.database.history.model.StreamHistoryEntity;
import com.video.victusadownloaders.WatchVideo.database.stream.dao.StreamDAO;
import com.video.victusadownloaders.WatchVideo.database.stream.dao.StreamStateDAO;
import com.video.victusadownloaders.WatchVideo.database.stream.model.StreamEntity;
import com.video.victusadownloaders.WatchVideo.database.stream.model.StreamStateEntity;
import com.video.victusadownloaders.WatchVideo.database.playlist.dao.PlaylistDAO;
import com.video.victusadownloaders.WatchVideo.database.playlist.dao.PlaylistRemoteDAO;
import com.video.victusadownloaders.WatchVideo.database.playlist.dao.PlaylistStreamDAO;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistEntity;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistRemoteEntity;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistStreamEntity;
import com.video.victusadownloaders.WatchVideo.database.subscription.SubscriptionDAO;
import com.video.victusadownloaders.WatchVideo.database.subscription.SubscriptionEntity;

@TypeConverters({Converters.class})
@Database(
        entities = {
                SubscriptionEntity.class, SearchHistoryEntry.class,
                StreamEntity.class, StreamHistoryEntity.class, StreamStateEntity.class,
                PlaylistEntity.class, PlaylistStreamEntity.class, PlaylistRemoteEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "gagtube.db";

    public abstract SubscriptionDAO subscriptionDAO();

    public abstract SearchHistoryDAO searchHistoryDAO();

    public abstract StreamDAO streamDAO();

    public abstract StreamHistoryDAO streamHistoryDAO();

    public abstract StreamStateDAO streamStateDAO();

    public abstract PlaylistDAO playlistDAO();

    public abstract PlaylistStreamDAO playlistStreamDAO();

    public abstract PlaylistRemoteDAO playlistRemoteDAO();
}
