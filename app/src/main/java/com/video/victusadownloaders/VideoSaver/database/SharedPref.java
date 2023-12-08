package com.video.victusadownloaders.VideoSaver.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPref {
    private static SharedPref sInstance;
    private Context mContext;
    private Editor mEditor;
    private SharedPreferences mSharedPref;

    private SharedPref(Context context) {
        this.mContext = context;
        Context context2 = this.mContext;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContext.getPackageName());
        sb.append("_general_preferences");
        this.mSharedPref = context2.getSharedPreferences(sb.toString(), 0);
        this.mEditor = this.mSharedPref.edit();
    }

    public static SharedPref getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPref(context);
        }
        return sInstance;
    }

    public int getSpInt(String str, int i) {
        return this.mSharedPref.getInt(str, i);
    }

    public void setSpInt(String str, int i) {
        this.mEditor.putInt(str, i).commit();
    }

    public String getSpString(String str) {
        return this.mSharedPref.getString(str, "");
    }

    public void setSpString(String str, String str2) {
        this.mEditor.putString(str, str2).commit();
    }

    public boolean getSpBoolean(String str, boolean z) {
        return this.mSharedPref.getBoolean(str, z);
    }

    public void setSpBoolean(String str, boolean z) {
        this.mEditor.putBoolean(str, z).commit();
    }
}
