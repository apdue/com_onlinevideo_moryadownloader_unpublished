package com.video.victusadownloaders.WatchVideo.database.history.dao;

import com.video.victusadownloaders.WatchVideo.database.BasicDAO;

public interface HistoryDAO<T> extends BasicDAO<T> {
    T getLatestEntry();
}
