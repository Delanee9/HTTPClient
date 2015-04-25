package com.delaney.httpclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.delaney.httpclient.activities.MainActivity;
import com.delaney.httpclient.databaseManagement.Database;
import com.delaney.httpclient.errorHandling.ErrorHandling;

public class GcmIntentService extends IntentService implements ICommon {
    private static final int NOTIFICATION_ID = 1;
    private static final String LOCATION = "location";
    private static final String MOBILE = "mobile";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle extras = intent.getExtras();

            if(!extras.isEmpty()) {
                Database database = new Database(getApplicationContext());
                String from = database.getMobileNumberDB(extras.getString(MOBILE));
                database.setLocationDB(extras.getString(LOCATION), from);
                sendNotification("Friend near your location");
            }
        } catch(Exception e) {
            new ErrorHandling("Failure in onHandleIntent", e.toString());
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String message) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Pal-2-Pal Notification").setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}