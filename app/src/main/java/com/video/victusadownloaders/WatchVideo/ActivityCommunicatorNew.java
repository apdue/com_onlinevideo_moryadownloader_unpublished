package com.video.victusadownloaders.WatchVideo;

/**
 * Singleton:
 * Used to send data between certain Activity/Services within the same process.
 * This can be considered as an ugly hack inside the Android universe. **/
public class ActivityCommunicatorNew {

    private static ActivityCommunicatorNew activityCommunicator;

    public static ActivityCommunicatorNew getCommunicator() {
        if(activityCommunicator == null) {
            activityCommunicator = new ActivityCommunicatorNew();
        }
        return activityCommunicator;
    }

    public volatile Class returnActivity;
}
