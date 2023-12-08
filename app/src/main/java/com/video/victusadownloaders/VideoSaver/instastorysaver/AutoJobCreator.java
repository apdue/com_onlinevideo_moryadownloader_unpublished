package com.video.victusadownloaders.VideoSaver.instastorysaver;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

class AutoJobCreator implements JobCreator {
    AutoJobCreator() {
    }

    @Nullable
    public Job create(@NonNull String str) {
        Log.d("Auto Job creator ", "on create called");
        Object obj = (str.hashCode() == 572127529 && str.equals(AutoSyncJob.TAG)) ? null : -1;
        if (obj != null) {
            return null;
        }
        Log.d("Auto Job creator ", "on create called job_auto_sync");
        return new AutoSyncJob();
    }
}
