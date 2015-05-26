package com.delaney.httpclient.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.delaney.httpclient.errorHandling.ErrorHandling;
import com.delaney.httpclient.receivers.intentService.AlarmIntentService;
import com.delaney.httpclient.receivers.intentService.AlarmService;
import com.delaney.httpclient.receivers.intentService.GcmIntentService;

import java.util.Calendar;

/**
 * Created by Emmet on 20/05/2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent service = new Intent(context, AlarmService.class);
//        ComponentName componentName = new ComponentName(context.getPackageName(), AlarmIntentService.class.getName());
//        startWakefulService(context, (intent.setComponent(componentName)));
//        startWakefulService(context, service);

        Intent alarmIntent = new Intent(context, AlarmService.class);
        startWakefulService(context, alarmIntent);
    }

    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarm_mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_mgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1, pending_intent);
    }
}