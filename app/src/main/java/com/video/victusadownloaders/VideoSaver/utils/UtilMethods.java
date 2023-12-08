package com.video.victusadownloaders.VideoSaver.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.app.Constants;
import com.video.victusadownloaders.VideoSaver.database.SharedPref;

public class UtilMethods {
    private Context mContext;

    public UtilMethods(Context context) {
        this.mContext = context;
    }

    public boolean shouldShowRateUsDialog() {
        if (SharedPref.getInstance(this.mContext).getSpBoolean("is_rate_us_clicked", false)) {
            return false;
        }
        int spInt = SharedPref.getInstance(this.mContext).getSpInt("rate_us_count", 0);
//        if (((double) spInt) < FirebaseRemoteConfig.getInstance().getDouble("rate_us_dialog_count")) {
//            SharedPref.getInstance(this.mContext).setSpInt("rate_us_count", spInt + 1);
//            return false;
//        }
        SharedPref.getInstance(this.mContext).setSpInt("rate_us_count", 0);
        return true;
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void downloadFile(String str, String str2) {
        FileChannel fileChannel;
        File file = new File(str);
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.SAVED_MEDIA_PATH);
        sb.append(File.separator);
        File file2 = new File(sb.toString());
        if (!file2.exists()) {
            file2.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Constants.SAVED_MEDIA_PATH);
        sb2.append(File.separator);
        sb2.append(str2);
        File file3 = new File(sb2.toString());
        if (file3.exists()) {
            Toast.makeText(this.mContext, "Already Saved", Toast.LENGTH_SHORT).show();
            return;
        }
        FileChannel fileChannel2 = null;
        try {
            fileChannel = new FileInputStream(file).getChannel();
            try {
                fileChannel2 = new FileOutputStream(file3).getChannel();
                fileChannel2.transferFrom(fileChannel, 0, fileChannel.size());
                if (fileChannel != null) {
                    fileChannel.close();
                }
                if (fileChannel2 != null) {
                    fileChannel2.close();
                }
                refreshGallery(file3.getAbsolutePath());
                Toast.makeText(this.mContext, "Saved", Toast.LENGTH_SHORT).show();
            } catch (Throwable th) {
                th = th;
                if (fileChannel != null) {
                }
                if (fileChannel2 != null) {
                }
                refreshGallery(file3.getAbsolutePath());
                Toast.makeText(this.mContext, "Saved", Toast.LENGTH_SHORT).show();
                throw th;
            }
        } catch (Throwable th2) {
            try {
                fileChannel = null;
                if (fileChannel != null) {
                    fileChannel.close();
                }
                if (fileChannel2 != null) {
                    fileChannel2.close();
                }
                refreshGallery(file3.getAbsolutePath());
                Toast.makeText(this.mContext, "Saved", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshGallery(String str) {
        MediaScannerConnection.scanFile(this.mContext, new String[]{str}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
            }
        });
    }

    public static String getValidUrlVimeo(String str) {
        return new String(str.trim().replace("%20", " ").replace("%26", "&").replace("%2c", ",").replace("%28", "(").replace("%29", ")").replace("%21", "!").replace("%3D", "=").replace("%3C", "<").replace("%3E", ">").replace("%23", "#").replace("%24", "$").replace("%27", "'").replace("%2A", "*").replace("%2D", "-").replace("%2E", ".").replace("%2F", "/").replace("%3A", ":").replace("%3B", ";").replace("%3F", "?").replace("%40", "@").replace("%5B", "[").replace("%5C", "\\").replace("%5D", "]").replace("%5F", "_").replace("%60", "`").replace("%7B", "{").replace("%7C", "|").replace("%7D", "}"));
    }

    public void showCustomToast(String str, int i) {
        View inflate = ((AppCompatActivity) this.mContext).getLayoutInflater().inflate(R.layout.toast_layout, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image);
        ((TextView) inflate.findViewById(R.id.tvTitleToastLayout)).setText(str);
        Toast toast = new Toast(this.mContext);
        toast.setGravity(16, 0, 0);
        toast.setDuration(i);
        toast.setView(inflate);
        toast.show();
//        if (!SharedPref.getInstance(this.mContext).getSpBoolean("is_ad_show", false)) {
//            AdsUtil.getInstance(this.mContext).loadInterstitialAd("", "ad_download_button", new AdsCallBack() {
//                public void onAdFailedToLoad(String str, String str2) {
//                }
//            });
//            SharedPref.getInstance(this.mContext).setSpBoolean("is_ad_show", true);
//            return;
//        }
        SharedPref.getInstance(this.mContext).setSpBoolean("is_ad_show", false);
    }
}
