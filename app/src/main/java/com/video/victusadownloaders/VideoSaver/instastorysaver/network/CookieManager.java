package com.video.victusadownloaders.VideoSaver.instastorysaver.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.webkit.CookieSyncManager;

import java.lang.ref.WeakReference;

import com.video.victusadownloaders.VideoSaver.instastorysaver.InstaStoryApplication;
import com.video.victusadownloaders.VideoSaver.instastorysaver.local.PreferencesHelper;

public class CookieManager {
    private static String COOKIE_KEY = "com.app.instastory.cookie";
    private final WeakReference<Context> mContext;
    private final PreferencesHelper mPreferencesHelper = InstaStoryApplication.getInstance().getPreferencesHelper();

    public CookieManager(Context context) {
        this.mContext = new WeakReference(context);
    }

    public String getCookie() {
        return this.mPreferencesHelper.getString(COOKIE_KEY, "");
    }

    public void setCookie(String str) {
        this.mPreferencesHelper.putString(COOKIE_KEY, str);
    }

    public void clearCookie() {
        clearDefaultCookie((Context) this.mContext.get());
        this.mPreferencesHelper.putString(COOKIE_KEY, "");
    }

    @SuppressLint({"Deprecated"})
    public void clearDefaultCookie(Context context) {
        if (VERSION.SDK_INT >= 22) {
            android.webkit.CookieManager.getInstance().removeAllCookies(null);
            android.webkit.CookieManager.getInstance().flush();
            return;
        }
        CookieSyncManager createInstance = CookieSyncManager.createInstance(context);
        createInstance.startSync();
        android.webkit.CookieManager instance = android.webkit.CookieManager.getInstance();
        instance.removeAllCookie();
        instance.removeSessionCookie();
        createInstance.stopSync();
        createInstance.sync();
    }

    public boolean isValid() {
        return isValid(getCookie());
    }

    public boolean isValid(String str) {
        return str != null && str.contains("sessionid");
    }
}
