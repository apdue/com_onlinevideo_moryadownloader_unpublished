package com.video.victusadownloaders.WatchVideo.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.video.victusadownloaders.VideoSaver.PrefManagerVideo;
import com.video.victusadownloaders.WatchVideo.models.SplModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import io.awesome.gagtube.R;

public class SplAdapter extends RecyclerView.Adapter<SplAdapter.viewholder>{

    private Context context;
    private List<SplModel> splModelList;

    PrefManagerVideo prf;

    public SplAdapter(Context context, List<SplModel> splModelList) {
        this.context = context;
        this.splModelList = splModelList;
        prf = new PrefManagerVideo(context);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            view = layoutInflater.inflate(R.layout.spl_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        int itemViewType = holder.getItemViewType();

            interest(holder,position);
    }



    private void interest(viewholder holder, int position) {
        SplModel model = splModelList.get(position);
        holder.button.setText(model.getName());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.secondaryColor));
                holder.button.setTextColor(Color.parseColor("#ffffff"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return splModelList.size();
    }
    @Override
    public int getItemViewType(int i) {

            return 1;
    }
    public class viewholder extends RecyclerView.ViewHolder{
        LinearLayout frame_layout;
        MaterialButton button;
        CardView cardView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            frame_layout = itemView.findViewById(R.id.frame_layout);
            button = itemView.findViewById(R.id.btn);
            cardView = itemView.findViewById(R.id.cardV);
        }
    }
}
