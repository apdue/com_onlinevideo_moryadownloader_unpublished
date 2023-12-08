package com.video.victusadownloaders.VideoSaver.instastorysaver.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE = "create table Users(_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, pk TEXT NOT NULL);";
    static final String DB_NAME = "Storygram.DB1";
    static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Users";
    public static final String USERNAME = "username";
    public static final String USER_PK = "pk";
    public static final String _ID = "_id";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(sQLiteDatabase);
    }
}
