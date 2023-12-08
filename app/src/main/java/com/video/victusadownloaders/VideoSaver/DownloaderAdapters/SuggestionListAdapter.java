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

import java.util.List;

import io.awesome.gagtube.R;
import com.video.victusadownloaders.VideoSaver.searchview.SearchSugg;

public class SuggestionListAdapter extends Adapter<SuggestionListAdapter.SuggestionListHolder> {
    private RecyclerCallBack mCallBack;
    private Context mContext;
    private List<SearchSugg> mList;

    public interface RecyclerCallBack {
        void onItemClicked(String str);

        void onMoveUpClicked(String str);
    }

    public class SuggestionListHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public ImageView mImgIcMove;
        /* access modifiers changed from: private */
        public TextView mTvSubTitle;
        public TextView mTvTitle;

        public SuggestionListHolder(View view) {
            super(view);
            this.mTvTitle = (TextView) view.findViewById(R.id.tvTitleSuggestionList);
            this.mTvSubTitle = (TextView) view.findViewById(R.id.tvSubTitleSuggestionList);
            this.mImgIcMove = (ImageView) view.findViewById(R.id.imgIcMoveUpSuggestionListList);
        }
    }

    public SuggestionListAdapter(Context context, List<SearchSugg> list, RecyclerCallBack recyclerCallBack) {
        this.mContext = context;
        this.mList = list;
        this.mCallBack = recyclerCallBack;
    }

    public SuggestionListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SuggestionListHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_suggestion_list, viewGroup, false));
    }

    public void onBindViewHolder(SuggestionListHolder suggestionListHolder, int i) {
        final SearchSugg searchSugg = (SearchSugg) this.mList.get(i);
        suggestionListHolder.mTvTitle.setText(searchSugg.getQuery());
        TextView access$000 = suggestionListHolder.mTvSubTitle;
        StringBuilder sb = new StringBuilder();
        sb.append("Search for: ");
        sb.append(searchSugg.getQuery());
        access$000.setText(sb.toString());
        suggestionListHolder.mImgIcMove.setOnClickListener(new OnClickListener() {
//            private final /* synthetic */ SearchSugg f$1;
//
//            {
//                this.f$1 = r2;
//            }

            public final void onClick(View view) {
                SuggestionListAdapter.this.mCallBack.onMoveUpClicked(searchSugg.getQuery());
            }
        });
        suggestionListHolder.itemView.setOnClickListener(new OnClickListener() {
//            private final /* synthetic */ SearchSugg f$1;
//
//            {
//                this.f$1 = r2;
//            }

            public final void onClick(View view) {
                SuggestionListAdapter.this.mCallBack.onItemClicked(searchSugg.getQuery());
            }
        });
    }

    public int getItemCount() {
        return this.mList.size();
    }
}
