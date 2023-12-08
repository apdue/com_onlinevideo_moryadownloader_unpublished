package com.video.victusadownloaders.VideoSaver.instastorysaver;

import android.util.Log;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest.Builder;
import com.evernote.android.job.JobRequest.NetworkType;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.video.victusadownloaders.VideoSaver.instastorysaver.local.DBManager;

public class AutoSyncJob extends Job {
    public static final String TAG = "job_auto_sync";

    /* Access modifiers changed, original: protected */
    @NonNull
    public Result onRunJob(@NonNull Params params) {
        DBManager dBManager = new DBManager(getContext());
        dBManager.open();
        List fetchAll = dBManager.fetchAll();
        dBManager.close();
        if (fetchAll.size() > 0) {
            new AutoSynApi().synchronise(fetchAll);
        }
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        Log.d("Job scheduler", "Job secheduled ******");
        if (JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            new Builder(TAG).setPeriodic(TimeUnit.MINUTES.toMillis(180), TimeUnit.MINUTES.toMillis(150)).setUpdateCurrent(true).setRequiredNetworkType(NetworkType.CONNECTED).setRequirementsEnforced(true).build().schedule();
        }
    }
}
