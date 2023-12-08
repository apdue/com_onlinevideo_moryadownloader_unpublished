package com.video.victusadownloaders.WatchVideo.fragments.list;

import com.video.victusadownloaders.WatchVideo.fragments.ViewContract;

public interface ListViewContract<I, N> extends ViewContract<I> {
    void showListFooter(boolean show);

    void handleNextItems(N result);
}
