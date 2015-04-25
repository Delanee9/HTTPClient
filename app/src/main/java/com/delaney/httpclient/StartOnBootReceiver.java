package com.delaney.httpclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.delaney.httpclient.activities.MainActivity;

/**
 * Class to handle starting the application on system startup.
 */
public class StartOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MainActivity.class);
            context.startService(serviceIntent);
        }
    } 
}