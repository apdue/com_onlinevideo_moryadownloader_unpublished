package com.video.victusadownloaders.StatusVideoDownloader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.video.victusadownloaders.StatusVideoDownloader.model.FBStoryModel.NodeModel;
import com.bumptech.glide.Glide;
import io.awesome.gagtube.R;
import io.awesome.gagtube.databinding.ItemUserListBinding;

import com.video.victusadownloaders.StatusVideoDownloader.interfaces.UserListInterface;

import java.util.ArrayList;

public class FbStoryUserListAdapter extends RecyclerView.Adapter<FbStoryUserListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NodeModel> trayModelArrayList;
    private UserListInterface userListInterface;

    public FbStoryUserListAdapter(Context context, ArrayList<NodeModel> list, UserListInterface listInterface) {
        this.context = context;
        this.trayModelArrayList = list;
        this.userListInterface = listInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_user_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        NodeModel nodeModel=trayModelArrayList.get(position);
        viewHolder.binding.realName.setText(nodeModel.getNodeDataModel().getStory_bucket_owner().get("name").getAsString());
        String profilePic=nodeModel.getNodeDataModel().getOwner().getAsJsonObject("profile_picture").get("uri").getAsString();
                Glide.with(context).load(profilePic)
                .thumbnail(0.2f).into(viewHolder.binding.storyPc);
        viewHolder.binding.RLStoryLayout.setOnClickListener(view -> userListInterface.fbUserListClick(position, trayModelArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return trayModelArrayList == null ? 0 : trayModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserListBinding binding;

        public ViewHolder(ItemUserListBinding mbinding) {
            super(mbinding.getRoot());
            this.binding = mbinding;
        }
    }
}
