package com.video.victusadownloaders.VideoSaver.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class DownloadMissionItemDao extends AbstractDao<Object, Long> {
    public DownloadMissionItemDao(DaoConfig config) {
        super(config);
    }

    public static void createTable(Database database, boolean z) {
        String str = z ? "IF NOT EXISTS " : "";
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(str);
        sb.append("\"DOWNLOAD_MISSION_ITEM\" (\"_id\" INTEGER PRIMARY KEY NOT NULL ,\"URL\" TEXT,\"NAME\" TEXT,\"THUMBNAIL\" TEXT,\"RESULT\" INTEGER NOT NULL );");
        database.execSQL(sb.toString());
    }

    public static void dropTable(Database database, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ");
        sb.append(z ? "IF EXISTS " : "");
        sb.append("\"DOWNLOAD_MISSION_ITEM\"");
        database.execSQL(sb.toString());
    }

    @Override
    protected Object readEntity(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected Long readKey(Cursor cursor, int offset) {
        return null;
    }

    @Override
    protected void readEntity(Cursor cursor, Object entity, int offset) {

    }

    @Override
    protected void bindValues(DatabaseStatement stmt, Object entity) {

    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Object entity) {

    }

    @Override
    protected Long updateKeyAfterInsert(Object entity, long rowId) {
        return null;
    }

    @Override
    protected Long getKey(Object entity) {
        return null;
    }

    @Override
    protected boolean hasKey(Object entity) {
        return false;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return false;
    }
}
