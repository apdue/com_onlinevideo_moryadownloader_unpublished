package com.video.victusadownloaders.VideoSaver.DownloaderAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;

import java.util.ArrayList;

import io.awesome.gagtube.R;

public class QualityListAdapter extends Adapter<QualityListAdapter.QualityListHolder> {
    public static Context context;
    PrefManagerVideo prf;
    private RecyclerCallBack mCallBack;
    private Context mContext;
    private ArrayList<String> mList;
    private String mTag;

    public QualityListAdapter(Context context, ArrayList<String> arrayList, String str, RecyclerCallBack recyclerCallBack) {
        this.mContext = context;
        this.mList = arrayList;
        this.mCallBack = recyclerCallBack;
        this.mTag = str;
        prf = new PrefManagerVideo(context);
    }

    public static void lambda$onBindViewHolder$0(QualityListAdapter qualityListAdapter, int i, String str, View view) {
        if (qualityListAdapter.mTag.equalsIgnoreCase("tag_vimeo")) {
            qualityListAdapter.mCallBack.onItemClicked(String.valueOf(i));
        } else {
            qualityListAdapter.mCallBack.onItemClicked(str);
        }
    }

    public QualityListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new QualityListHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_quality_list, viewGroup, false));
    }

    public void onBindViewHolder(QualityListHolder qualityListHolder, final int i) {
        final String str = (String) this.mList.get(i);
        qualityListHolder.mTvTitle.setText(str);
        qualityListHolder.mImgIcDownload.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                QualityListAdapter.lambda$onBindViewHolder$0(QualityListAdapter.this, i, str, view);
            }
        });
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public interface RecyclerCallBack {
        void onItemClicked(String str);
    }

    public static class QualityListHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public ImageView mImgIcDownload;
        public TextView mTvTitle;
        private TextView mTvSize;

        public QualityListHolder(View view) {
            super(view);
            this.mTvTitle = (TextView) view.findViewById(R.id.tvQualityTitleItemList);
            this.mTvSize = (TextView) view.findViewById(R.id.tvSizeItemList);
            this.mImgIcDownload = (ImageView) view.findViewById(R.id.imgIcDownloadItemList);
        }
    }
}
