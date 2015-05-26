package com.delaney.httpclient.receivers.intentService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.delaney.httpclient.LocationRetrieval;
import com.delaney.httpclient.UpstreamMessage;
import com.delaney.httpclient.errorHandling.ErrorHandling;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Class to handle the periodic wakeup calls to post location updates
 */
public class LocationIntentService extends IntentService {
    private static final String PROPERTY_REG_ID = "registration_id";
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LocationIntentService() {
        super("LocationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
//            final Runnable beeper = new Runnable() {
//                public void run() {
//                    Context context = getApplicationContext();
//                    Location location = LocationRetrieval.getLastKnownLocation(context);
//                    String locationUpdate = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
//                    UpstreamMessage.postUpdate(getRegistrationId(context), locationUpdate);
//                    new ErrorHandling("update", locationUpdate).execute();
//                }};
//
//            scheduler.scheduleAtFixedRate(beeper, 0, 1, TimeUnit.MINUTES);

            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                Context context = getApplicationContext();
                                Location location = LocationRetrieval.getLastKnownLocation(context);
                                String locationUpdate = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                                UpstreamMessage.postUpdate(getRegistrationId(context), locationUpdate);
                                new ErrorHandling("update", locationUpdate).execute();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 60000); //execute in every 50000 ms

        } catch(Exception e) {
            new ErrorHandling("Failure in onHandleIntent", e.toString());
        }
    }

    /**
     * Returns Registration ID from the shared preferences.
     *
     * @param context Context
     * @return String
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if(registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }
        return registrationId;
    }

    /**
     * Returns the shared preferences stored on the device.
     *
     * @param context Context
     * @return SharedPreferences
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(LocationIntentService.class.getSimpleName(), Context.MODE_PRIVATE);
    }
}
