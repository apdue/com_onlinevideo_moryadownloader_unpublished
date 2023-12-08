package com.video.victusadownloaders.VideoSaver.instastorysaver.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private static String PREFERENCE_NAME = "com.app.instastory";
    private final SharedPreferences mPref;

    public PreferencesHelper(Context context) {
        this.mPref = context.getSharedPreferences(PREFERENCE_NAME, 0);
    }

    public String getString(String str, String str2) {
        return this.mPref.getString(str, str2);
    }

    public int getInt(String str, int i) {
        return this.mPref.getInt(str, i);
    }

    public boolean getBoolean(String str, boolean z) {
        return this.mPref.getBoolean(str, z);
    }

    public float getFloat(String str, float f) {
        return this.mPref.getFloat(str, f);
    }

    public float getLong(String str, long j) {
        return (float) this.mPref.getLong(str, j);
    }

    @SuppressLint({"CommitPrefEdits"})
    public void putString(String str, String str2) {
        this.mPref.edit().putString(str, str2).commit();
    }

    @SuppressLint({"CommitPrefEdits"})
    public void putInt(String str, int i) {
        this.mPref.edit().putInt(str, i).commit();
    }

    @SuppressLint({"CommitPrefEdits"})
    public void putBoolean(String str, boolean z) {
        this.mPref.edit().putBoolean(str, z).commit();
    }

    @SuppressLint({"CommitPrefEdits"})
    public void putFloat(String str, float f) {
        this.mPref.edit().putFloat(str, f).commit();
    }

    public void clear_all() {
        this.mPref.edit().clear().commit();
    }

    @SuppressLint({"CommitPrefEdits"})
    public void putLong(String str, long j) {
        this.mPref.edit().putLong(str, j).commit();
    }
}
