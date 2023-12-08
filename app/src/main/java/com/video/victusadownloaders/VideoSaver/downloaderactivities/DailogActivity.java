package com.video.victusadownloaders.VideoSaver.downloaderactivities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.video.victusadownloaders.VideoSaver.utils.DialogFactory;

public class DailogActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            DialogFactory.showDialog(this, "Do you want to allow Quick Download this app over other applications", "Yes", "Cancel", new OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DailogActivity.lambda$onCreate$0(DailogActivity.this, dialogInterface, i);
                }
            }, new OnClickListener() {
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DailogActivity.this.finish();
                }
            });
        }
    }

    public static /* synthetic */ void lambda$onCreate$0(DailogActivity dailogActivity, DialogInterface dialogInterface, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(dailogActivity.getPackageName());
        dailogActivity.startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())), 121);
        dailogActivity.finish();
    }
}
