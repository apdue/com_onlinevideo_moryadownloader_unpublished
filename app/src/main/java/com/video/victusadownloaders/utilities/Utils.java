package com.video.victusadownloaders.utilities;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Utils {

    public static void logEvents(Activity activity, String ad, String action){
        Bundle params = new Bundle();
        String localClassName = activity.getLocalClassName();
        String className = localClassName.substring(localClassName.lastIndexOf(".") + 1);
        FirebaseAnalytics.getInstance(activity).logEvent(className+ad+action, params);
    }
}
