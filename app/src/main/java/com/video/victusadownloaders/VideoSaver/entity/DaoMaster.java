package com.video.victusadownloaders.VideoSaver.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {

    public DaoMaster(Database db, int schemaVersion) {
        super(db, schemaVersion);
    }

    @Override
    public AbstractDaoSession newSession() {
        return null;
    }

    @Override
    public AbstractDaoSession newSession(IdentityScopeType type) {
        return null;
    }

    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String str, CursorFactory cursorFactory) {
            super(context, str, cursorFactory);
        }

        public void onUpgrade(Database database, int i, int i2) {
            StringBuilder sb = new StringBuilder();
            sb.append("Upgrading schema from version ");
            sb.append(i);
            sb.append(" to ");
            sb.append(i2);
            sb.append(" by dropping all tables");
            Log.i("greenDAO", sb.toString());
            DaoMaster.dropAllTables(database, true);
            onCreate(database);
        }
    }

    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String str, CursorFactory cursorFactory) {
            super(context, str, cursorFactory, 1);
        }

        public void onCreate(Database database) {
            Log.i("greenDAO", "Creating tables for schema version 1");
            DaoMaster.createAllTables(database, false);
        }
    }

    public static void createAllTables(Database database, boolean z) {
        DownloadMissionItemDao.createTable(database, z);
    }

    public static void dropAllTables(Database database, boolean z) {
        DownloadMissionItemDao.dropTable(database, z);
    }
}
