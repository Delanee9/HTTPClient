package com.delaney.httpclient.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    AlarmReceiver alarm = new AlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            alarm.setAlarm(context);
        }
    }
}