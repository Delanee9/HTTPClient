package com.delaney.httpclient.alarm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.delaney.httpclient.LocationRetrieval;
import com.delaney.httpclient.UpstreamMessage;
import com.delaney.httpclient.errorHandling.ErrorHandling;

public class AlarmService extends IntentService {
    private static final String PROPERTY_REG_ID = "registration_id";

    public AlarmService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        Location location = LocationRetrieval.getLastKnownLocation(context);
        String locationUpdate = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        UpstreamMessage.postUpdate(getRegistrationId(context), locationUpdate);
        new ErrorHandling("update", locationUpdate).execute();

        AlarmReceiver.completeWakefulIntent(intent);
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
        return getSharedPreferences(AlarmService.class.getSimpleName(), Context.MODE_PRIVATE);
    }
}