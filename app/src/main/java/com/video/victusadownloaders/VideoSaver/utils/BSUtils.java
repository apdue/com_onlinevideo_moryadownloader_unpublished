package com.video.victusadownloaders.VideoSaver.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.QualityListAdapter;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.QualityListAdapter.RecyclerCallBack;

public class BSUtils {
    private Context mContext;

    public interface BSCallBack {
        void onBsHidden();

        void onDownloadClicked(String str);
    }

    public BSUtils(Context context) {
        this.mContext = context;
    }

    public void showSimpleSheet(String str, String str2, final BSCallBack bSCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.mContext);
        View inflate = ((AppCompatActivity) this.mContext).getLayoutInflater().inflate(R.layout.layout_download_sheet_simple, null, false);
        bottomSheetDialog.setContentView(inflate);
        Button button = (Button) inflate.findViewById(R.id.btnDownloadSheetSimple);
        TextView textView = (TextView) inflate.findViewById(R.id.tvTitleLayoutDownloadSheetSimple);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tvSizeDownloadSheetSimple);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.layoutFbBannerContainerSheetSingle);
        textView.setText(str);
        if (str2 != null && !TextUtils.isEmpty(str2)) {
            textView2.setText(str2);
        }
        button.setOnClickListener(new OnClickListener() {
            @Override
            public final void onClick(View view) {
                bottomSheetDialog.dismiss();
                bSCallBack.onDownloadClicked("");
            }
        });
        bottomSheetDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                bSCallBack.onBsHidden();
            }
        });
//        if (FirebaseRemoteConfig.getInstance().getBoolean("fb_banner_quality_list")) {
//            loadFbBannerAd(linearLayout);
//        } else {
//            loadAdmobBanner(adView);
//        }
        bottomSheetDialog.show();
    }



    public void showListSheet(String str, String str2, ArrayList<String> arrayList, String str3, final BSCallBack bSCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.mContext, R.style.BsDialog);
        View inflate = ((AppCompatActivity) this.mContext).getLayoutInflater().inflate(R.layout.layout_download_sheet_list, null, false);
        bottomSheetDialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.tvTitleLayoutDownloadSheetList);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerDownloadSheetList);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.layoutFbBannerContainerSheetList);
        textView.setText(str);
        QualityListAdapter qualityListAdapter = new QualityListAdapter(this.mContext, arrayList, str3, new RecyclerCallBack() {
            public void onItemClicked(String str) {
                bottomSheetDialog.dismiss();
                bSCallBack.onDownloadClicked(str);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        recyclerView.setAdapter(qualityListAdapter);
        bottomSheetDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                bSCallBack.onBsHidden();
            }
        });
//        if (FirebaseRemoteConfig.getInstance().getBoolean("fb_banner_quality_list")) {
//            loadFbBannerAd(linearLayout);
//        } else {
//            loadAdmobBanner(adView);
//        }
        bottomSheetDialog.show();
    }


}
