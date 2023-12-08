package com.video.victusadownloaders.VideoSaver.instastorysaver.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.video.victusadownloaders.VideoSaver.instastorysaver.data.model.LocalDbUser;

public class DBManager {
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager open() throws SQLException {
        this.dbHelper = new DatabaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public long insert(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERNAME, str);
        contentValues.put(DatabaseHelper.USER_PK, str2);
        return this.database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public Cursor fetch() {
        Cursor query = this.database.query(DatabaseHelper.TABLE_NAME, new String[]{"_id", DatabaseHelper.USERNAME, DatabaseHelper.USER_PK}, null, null, null, null, null);
        if (query != null) {
            query.moveToFirst();
        }
        return query;
    }

    public List<String> fetchAllUsername() {
        ArrayList arrayList = new ArrayList();
        System.out.println("Rajan1");

        Cursor query = this.database.query(DatabaseHelper.TABLE_NAME, new String[]{"_id", DatabaseHelper.USERNAME, DatabaseHelper.USER_PK}, null, null, null, null, null);
        System.out.println("Rajan"+ query);
        if (query != null && query.moveToFirst()) {
            while (!query.isAfterLast()) {
                arrayList.add(query.getString(query.getColumnIndex(DatabaseHelper.USERNAME)));
                query.moveToNext();
            }
        }
        return arrayList;
    }

    public List<LocalDbUser> fetchAll() {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.database.query(DatabaseHelper.TABLE_NAME, new String[]{"_id", DatabaseHelper.USERNAME, DatabaseHelper.USER_PK}, null, null, null, null, null);
        if (query != null && query.moveToFirst()) {
            while (!query.isAfterLast()) {
                arrayList.add(new LocalDbUser(Long.parseLong(query.getString(query.getColumnIndex("_id"))), query.getString(query.getColumnIndex(DatabaseHelper.USERNAME)), query.getString(query.getColumnIndex(DatabaseHelper.USER_PK))));
                query.moveToNext();
            }
        }
        return arrayList;
    }

    public int update(long j, String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERNAME, str);
        contentValues.put(DatabaseHelper.USER_PK, str2);
        SQLiteDatabase sQLiteDatabase = this.database;
        str2 = DatabaseHelper.TABLE_NAME;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_id = ");
        stringBuilder.append(j);
        return sQLiteDatabase.update(str2, contentValues, stringBuilder.toString(), null);
    }

    public void delete(long j) {
        SQLiteDatabase sQLiteDatabase = this.database;
        String str = DatabaseHelper.TABLE_NAME;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_id=");
        stringBuilder.append(j);
        sQLiteDatabase.delete(str, stringBuilder.toString(), null);
    }
}
