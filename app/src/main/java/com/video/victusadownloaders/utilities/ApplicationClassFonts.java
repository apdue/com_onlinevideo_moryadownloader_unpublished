package com.video.victusadownloaders.utilities;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;

public class ApplicationClassFonts extends Application {
    public static void hideStatus(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void moreApps(Activity activity) {
        String url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void rateApp(Activity activity) {
        String url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void shareApp(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "My application name");
            intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            activity.startActivity(Intent.createChooser(intent, "Choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


}
