package com.video.victusadownloaders.VideoSaver.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.DailyQualityList;
import com.video.victusadownloaders.VideoSaver.DownloaderAdapters.DailyQualityList.RecyclerCallBack;
import com.video.victusadownloaders.VideoSaver.model.HLS.Format;

public class BSUtils_D {
    private Context mContext;

    public interface BSCallBack {
        void onBsHidden();

        void onDownloadClicked(Format format);
    }

    public BSUtils_D(Context context) {
        this.mContext = context;
    }

    public void showListSheet(String str, String str2, List<Format> list, String str3, final BSCallBack bSCallBack) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.mContext, R.style.BsDialog);
        View inflate = ((AppCompatActivity) this.mContext).getLayoutInflater().inflate(R.layout.layout_download_sheet_list, null, false);
        bottomSheetDialog.setContentView(inflate);
        TextView textView = (TextView) inflate.findViewById(R.id.tvTitleLayoutDownloadSheetList);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerDownloadSheetList);
        textView.setText(str);
        DailyQualityList qualityList = new DailyQualityList(this.mContext, list, str3, new RecyclerCallBack() {
            public void onItemClicked(Format format) {
                bottomSheetDialog.dismiss();
                bSCallBack.onDownloadClicked(format);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        recyclerView.setAdapter(qualityList);
        bottomSheetDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                bSCallBack.onBsHidden();
            }
        });
        bottomSheetDialog.show();
    }
}
