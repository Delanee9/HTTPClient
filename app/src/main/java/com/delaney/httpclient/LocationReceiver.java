package com.delaney.httpclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.delaney.httpclient.activities.MainActivity;

public class LocationReceiver extends BroadcastReceiver {
    public LocationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        context. startService(new Intent(context, MainActivity.class));
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
