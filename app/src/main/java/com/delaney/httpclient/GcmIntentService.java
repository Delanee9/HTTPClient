package com.delaney.httpclient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.delaney.httpclient.activities.MainActivity;
import com.delaney.httpclient.errorHandling.ErrorHandling;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Logger;

public class GcmIntentService extends IntentService implements ICommon {
    private static final int NOTIFICATION_ID = 1;
    private static final Logger logger = Logger.getLogger(GcmIntentService.class.getName());
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()) {
            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
                new ErrorHandling("message", "messageTypeSendError");
            } else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                new ErrorHandling("message", "messageTypeDeleted");
            } else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                new ErrorHandling("message", "messageTypeMessage");
            } else {
                new ErrorHandling("message", "other");
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("GCM Notification").setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}