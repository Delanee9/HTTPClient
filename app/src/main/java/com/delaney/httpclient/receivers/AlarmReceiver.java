package com.delaney.httpclient.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.delaney.httpclient.receivers.intentService.AlarmIntentService;

import java.util.Calendar;

/**
 * Created by Emmet on 20/05/2015.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmIntentService.class);

        startWakefulService(context, service);
    }

    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        // Set the alarm's trigger time to 8:30 a.m.
        calendar.set(Calendar.MILLISECOND,((int)System.currentTimeMillis()) + 100);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Cancels the alarm.
     *
     * @param context Context
     */
    public void cancelAlarm(Context context) {

        if(alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}