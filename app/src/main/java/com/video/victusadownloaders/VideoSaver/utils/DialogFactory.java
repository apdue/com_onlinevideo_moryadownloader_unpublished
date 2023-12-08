package com.video.victusadownloaders.VideoSaver.utils;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;

public class DialogFactory {
    public static AlertDialog showDialog(Context context, String str, String str2, String str3, OnClickListener onClickListener, OnClickListener onClickListener2) {
        Builder builder = new Builder(context);
        builder.setMessage(str).setCancelable(false).setPositiveButton(str2, onClickListener);
        if (!(str3 == null || onClickListener2 == null)) {
            builder.setNegativeButton(str3, onClickListener2);
        }
        AlertDialog create = builder.create();
        create.show();
        return create;
    }
}
