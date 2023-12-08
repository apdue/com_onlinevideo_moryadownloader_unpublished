package com.video.victusadownloaders.VideoSaver.app;

import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmChannel;
import com.video.victusadownloaders.VideoSaver.model.dailymotion.DmVideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class AVD {
    private static AVD INSTANCE;
    public boolean isMultiSelectedMode = false;
    private HashMap<String, LinkedList<DmVideo>> mChannelVideosMap = new HashMap<>();
    private LinkedList<DmChannel> mDmChannels = new LinkedList<>();
    private Set<String> mSelectedPaths = new HashSet();
    private ArrayList<DmVideo> mTrendingList = new ArrayList<>();

    private AVD() {
    }

    public static AVD getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AVD();
        }
        return INSTANCE;
    }

    public HashMap<String, LinkedList<DmVideo>> getChannelVideosMap() {
        return this.mChannelVideosMap;
    }

    public LinkedList<DmChannel> getDmChannels() {
        return this.mDmChannels;
    }

    public void setDmChannels(LinkedList<DmChannel> linkedList) {
        this.mDmChannels = linkedList;
    }

    public ArrayList<DmVideo> getTrendingList() {
        return this.mTrendingList;
    }

    public Set<String> getSelectedPaths() {
        return this.mSelectedPaths;
    }
}
