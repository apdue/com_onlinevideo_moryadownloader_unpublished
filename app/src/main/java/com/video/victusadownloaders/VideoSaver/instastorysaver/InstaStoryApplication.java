package com.video.victusadownloaders.VideoSaver.instastorysaver;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.evernote.android.job.JobManager;

import com.video.victusadownloaders.VideoSaver.instastorysaver.data.DataManager;
import com.video.victusadownloaders.VideoSaver.instastorysaver.local.PreferencesHelper;
import com.video.victusadownloaders.VideoSaver.instastorysaver.network.CookieManager;
import com.video.victusadownloaders.VideoSaver.p003di.component.AppComponent;
import com.video.victusadownloaders.VideoSaver.p003di.component.DaggerAppComponent;
import com.video.victusadownloaders.VideoSaver.p003di.module.AppModule;


public class InstaStoryApplication extends Application {
    public static final String TAG = "InstaStoryApplication";
    private static InstaStoryApplication instance;
    private static CookieManager mCookieManager;
    private static PreferencesHelper mPreferencesHelper;
    private RequestQueue mRequestQueue;
    private AppComponent appComponent;

    public static InstaStoryApplication getInstance() {
        return instance;
    }

    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        DataManager.init();
        JobManager.create(this).addJobCreator(new AutoJobCreator());
        initAppComponent();
        createNotificationChannel();


    }


    private void initAppComponent() {
        this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(InstaStoryApplication.this)).build();
    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel("ServiceChannel", "Service Channel", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    public synchronized CookieManager getCookieManager() {
        if (mCookieManager == null) {
            mCookieManager = new CookieManager(getApplicationContext());
        }
        return mCookieManager;
    }

    public synchronized PreferencesHelper getPreferencesHelper() {
        if (mPreferencesHelper == null) {
            mPreferencesHelper = new PreferencesHelper(getApplicationContext());
        }
        return mPreferencesHelper;
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object obj) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(obj);
        }
    }
}
