package com.video.victusadownloaders.StatusVideoDownloader.interfaces;


import com.video.victusadownloaders.StatusVideoDownloader.model.FBStoryModel.NodeModel;
import com.video.victusadownloaders.StatusVideoDownloader.model.story.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);
    void fbUserListClick(int position, NodeModel trayModel);
}
