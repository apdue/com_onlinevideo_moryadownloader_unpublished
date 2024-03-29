package com.video.victusadownloaders.WatchVideo.local.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.DateFormat;

import androidx.recyclerview.widget.RecyclerView;

import com.video.victusadownloaders.WatchVideo.database.LocalItem;
import com.video.victusadownloaders.WatchVideo.local.LocalItemBuilder;

public abstract class LocalItemHolder extends RecyclerView.ViewHolder {
    
    protected final LocalItemBuilder itemBuilder;

    public LocalItemHolder(LocalItemBuilder itemBuilder, int layoutId, ViewGroup parent) {
        
        super(LayoutInflater.from(itemBuilder.getContext()).inflate(layoutId, parent, false));
        this.itemBuilder = itemBuilder;
    }

    public abstract void updateFromItem(final LocalItem item, final DateFormat dateFormat);
}
