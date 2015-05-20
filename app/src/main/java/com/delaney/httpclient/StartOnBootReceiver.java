package com.delaney.httpclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class to handle starting the application on system startup.
 */
public class StartOnBootReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ComponentName componentName = new ComponentName(context.getPackageName(), LocationIntentService.class.getName());
            startWakefulService(context, (intent.setComponent(componentName)));
            setResultCode(Activity.RESULT_OK);
        }
    }
}