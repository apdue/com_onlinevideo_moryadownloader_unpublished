package com.video.victusadownloaders.VideoSaver.DownloaderAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.VideoSaver.model.HLS.Format;

import java.util.List;

import io.awesome.gagtube.R;

public class DailyQualityList extends Adapter<QualityListAdapter.QualityListHolder> {
    /* access modifiers changed from: private */
    public RecyclerCallBack mCallBack;
    private Context mContext;
    private List<Format> mList;
    private String mTag;

    public static Context context;


    public class QualityListHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public ImageView mImgIcDownload;
        private TextView mTvSize;
        public TextView mTvTitle;

        public QualityListHolder(View view) {
            super(view);
            this.mTvTitle = (TextView) view.findViewById(R.id.tvQualityTitleItemList);
            this.mTvSize = (TextView) view.findViewById(R.id.tvSizeItemList);
            this.mImgIcDownload = (ImageView) view.findViewById(R.id.imgIcDownloadItemList);
        }
    }

    public interface RecyclerCallBack {
        void onItemClicked(Format format);
    }

    public DailyQualityList(Context context, List<Format> list, String str, RecyclerCallBack recyclerCallBack) {
        this.mContext = context;
        this.mList = list;
        this.mCallBack = recyclerCallBack;
        this.mTag = str;
    }

    public QualityListAdapter.QualityListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new QualityListAdapter.QualityListHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_quality_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QualityListAdapter.QualityListHolder qualityListHolder, int i) {
        final Format format = (Format) this.mList.get(i);
        qualityListHolder.mTvTitle.setText(format.getFormatId());

        PrefManagerVideo prf = new PrefManagerVideo(context);



        qualityListHolder.mImgIcDownload.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DailyQualityList.this.mCallBack.onItemClicked(format);
            }
        });
    }

    public int getItemCount() {
        return this.mList.size();
    }
}
