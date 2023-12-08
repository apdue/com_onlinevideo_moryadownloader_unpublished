package com.video.victusadownloaders.WatchVideo.local.playlist;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import com.video.victusadownloaders.WatchVideo.database.AppDatabase;
import com.video.victusadownloaders.WatchVideo.database.playlist.PlaylistMetadataEntry;
import com.video.victusadownloaders.WatchVideo.database.playlist.PlaylistStreamEntry;
import com.video.victusadownloaders.WatchVideo.database.playlist.dao.PlaylistDAO;
import com.video.victusadownloaders.WatchVideo.database.playlist.dao.PlaylistStreamDAO;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistEntity;
import com.video.victusadownloaders.WatchVideo.database.playlist.model.PlaylistStreamEntity;
import com.video.victusadownloaders.WatchVideo.database.stream.dao.StreamDAO;
import com.video.victusadownloaders.WatchVideo.database.stream.model.StreamEntity;
import com.video.victusadownloaders.WatchVideo.util.Constants;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class LocalPlaylistManager {
	
	private final AppDatabase database;
	private final StreamDAO streamTable;
	private final PlaylistDAO playlistTable;
	private final PlaylistStreamDAO playlistStreamTable;
	
	public LocalPlaylistManager(final AppDatabase db) {
		
		database = db;
		streamTable = db.streamDAO();
		playlistTable = db.playlistDAO();
		playlistStreamTable = db.playlistStreamDAO();
	}
	
	public Maybe<List<Long>> createPlaylist(final String name, final List<StreamEntity> streams) {
		
		final StreamEntity defaultStream = streams.get(0);
		final PlaylistEntity newPlaylist = new PlaylistEntity(name, defaultStream.getThumbnailUrl());
		
		return Maybe.fromCallable(() -> database.runInTransaction(() -> upsertStreams(playlistTable.insert(newPlaylist), streams, 0)))
				.subscribeOn(Schedulers.io());
	}
	
	public Maybe<Long> createPlaylist(final String name) {
		
		final PlaylistEntity newPlaylist = new PlaylistEntity(name, Constants.THUMBNAIL_URL_DEFAULT);
		
		return Maybe.fromCallable(() -> database.runInTransaction(() -> playlistTable.insert(newPlaylist)))
				.subscribeOn(Schedulers.io());
	}
	
	public Maybe<List<Long>> appendToPlaylist(final long playlistId, final List<StreamEntity> streams) {
		
		return playlistStreamTable.getMaximumIndexOf(playlistId)
				.firstElement()
				.map(maxJoinIndex -> database.runInTransaction(() -> upsertStreams(playlistId, streams, maxJoinIndex + 1)))
				.subscribeOn(Schedulers.io());
	}
	
	private List<Long> upsertStreams(final long playlistId, final List<StreamEntity> streams, final int indexOffset) {
		
		List<PlaylistStreamEntity> joinEntities = new ArrayList<>(streams.size());
		
		final List<Long> streamIds = streamTable.upsertAll(streams);
		for (int index = 0; index < streamIds.size(); index++) {
			joinEntities.add(new PlaylistStreamEntity(playlistId, streamIds.get(index), index + indexOffset));
		}
		return playlistStreamTable.insertAll(joinEntities);
	}
	
	public Completable updateJoin(final long playlistId, final List<Long> streamIds) {
		
		List<PlaylistStreamEntity> joinEntities = new ArrayList<>(streamIds.size());
		for (int i = 0; i < streamIds.size(); i++) {
			joinEntities.add(new PlaylistStreamEntity(playlistId, streamIds.get(i), i));
		}
		
		return Completable.fromRunnable(() -> database.runInTransaction(() -> {
			playlistStreamTable.deleteBatch(playlistId);
			playlistStreamTable.insertAll(joinEntities);
		})).subscribeOn(Schedulers.io());
	}
	
	public Flowable<List<PlaylistMetadataEntry>> getPlaylists() {
		
		return playlistStreamTable.getPlaylistMetadata().subscribeOn(Schedulers.io());
	}
	
	public Flowable<List<PlaylistStreamEntry>> getPlaylistStreams(final long playlistId) {
		
		return playlistStreamTable.getOrderedStreamsOf(playlistId).subscribeOn(Schedulers.io());
	}
	
	public Single<Integer> deletePlaylist(final long playlistId) {
		
		return Single.fromCallable(() -> playlistTable.deletePlaylist(playlistId)).subscribeOn(Schedulers.io());
	}
	
	public Maybe<Integer> renamePlaylist(final long playlistId, final String name) {
		
		return modifyPlaylist(playlistId, name, null);
	}
	
	public Maybe<Integer> changePlaylistThumbnail(final long playlistId, final String thumbnailUrl) {
		
		return modifyPlaylist(playlistId, null, thumbnailUrl);
	}
	
	private Maybe<Integer> modifyPlaylist(final long playlistId, @Nullable final String name, @Nullable final String thumbnailUrl) {
		
		return playlistTable.getPlaylist(playlistId)
				.firstElement()
				.filter(playlistEntities -> !playlistEntities.isEmpty())
				.map(playlistEntities -> {
					
					PlaylistEntity playlist = playlistEntities.get(0);
					if (name != null) playlist.setName(name);
					if (thumbnailUrl != null) playlist.setThumbnailUrl(thumbnailUrl);
					
					return playlistTable.update(playlist);
					
				}).subscribeOn(Schedulers.io());
	}
}
