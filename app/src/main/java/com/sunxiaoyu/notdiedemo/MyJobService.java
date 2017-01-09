package com.sunxiaoyu.notdiedemo;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by sunxiaoyu on 2017/1/9.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends android.app.job.JobService {

    private int count = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addJob();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.v("sxy", "onStartJob--" + params.getJobId());
        startService(new Intent(this, RemoteService.class));
        startService(new Intent(this, LocalService.class));
        jobFinished(params, true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        addJob();
        return false;
    }

    private void addJob(){
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(count++, componentName);
        builder.setPeriodic(10);
        scheduler.schedule(builder.build());
    }


}
