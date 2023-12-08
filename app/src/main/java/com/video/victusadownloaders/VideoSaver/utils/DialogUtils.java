package com.video.victusadownloaders.VideoSaver.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;


import io.awesome.gagtube.R;


public class DialogUtils {
    private static Dialog simpleProgressDialog;
    private Context mContext;

    public interface DialogCallBack {
        void onNegButtonClicked(Bundle bundle);

        void onPosButtonClicked(Bundle bundle);
    }

    public DialogUtils(Context context) {
        this.mContext = context;
    }

    public void showInfoDialog(String str, String str2, String str3, String str4, String str5, final DialogCallBack dialogCallBack) {
        AlertDialog create = new Builder(this.mContext).create();
        create.setTitle(str);
        create.setMessage(str2);
        create.setCancelable(false);
        create.setButton(-1, str3, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialogCallBack.onPosButtonClicked(new Bundle());
            }
        });
        create.setButton(-2, str4, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                dialogCallBack.onNegButtonClicked(new Bundle());
            }
        });
        create.show();
    }

    public void showNoVideoFound(final DialogCallBack dialogCallBack, boolean z) {
        final Dialog dialog = new Dialog(this.mContext, R.style.FullWidthDialog);
        dialog.setContentView(R.layout.dialog_no_video_found);
        Button button = (Button) dialog.findViewById(R.id.btnOkDialogNoVideoFound);
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog.dismiss();
                dialogCallBack.onPosButtonClicked(new Bundle());

            }
        });

        dialog.show();
    }


    public void showFbHelp(final DialogCallBack dialogCallBack) {
        final Dialog dialog = new Dialog(this.mContext, R.style.FullWidthDialog);
        dialog.setContentView(R.layout.dialog_fb_help);
        Button button = (Button) dialog.findViewById(R.id.btnOkDialogFbHelp);
//        Glide.with(this.mContext).load(Integer.valueOf(R.raw.gif_fb_help)).into(((ImageView) dialog.findViewById(R.id.imgGifFbHelp)));
        button.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                dialog.dismiss();
                dialogCallBack.onPosButtonClicked(new Bundle());

            }
        });
        dialog.show();
    }

    public static void showSimpleProgressDialog(Context context) {
        if (simpleProgressDialog != null) {
            closeProgressDialog();
        }
        if (context != null) {
            simpleProgressDialog = new Dialog(context);
            simpleProgressDialog.requestWindowFeature(1);
            simpleProgressDialog.setContentView(R.layout.progress_view);
            setDialogOpacity(simpleProgressDialog, -1, 0);
            simpleProgressDialog.setCancelable(false);
            simpleProgressDialog.show();
        }
    }

    public static void closeProgressDialog() {
        Dialog dialog = simpleProgressDialog;
        if (dialog != null) {
            try {
                dialog.cancel();
                simpleProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setDialogOpacity(Dialog dialog, int i, int i2) {
        ColorDrawable colorDrawable = new ColorDrawable(i);
        colorDrawable.setAlpha(i2);
        dialog.getWindow().setBackgroundDrawable(colorDrawable);
    }
}
