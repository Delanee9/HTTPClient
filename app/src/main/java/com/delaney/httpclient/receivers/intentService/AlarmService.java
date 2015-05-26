package com.delaney.httpclient.receivers.intentService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.delaney.httpclient.LocationRetrieval;
import com.delaney.httpclient.UpstreamMessage;
import com.delaney.httpclient.activities.MainActivity;
import com.delaney.httpclient.errorHandling.ErrorHandling;

public class AlarmService extends Service {
    private static final String PROPERTY_REG_ID = "registration_id";

    public AlarmService() {
    }

    @Override
    public void onCreate() {
        Context context = getApplicationContext();
        Location location = LocationRetrieval.getLastKnownLocation(context);
        String locationUpdate = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        UpstreamMessage.postUpdate(getRegistrationId(context), locationUpdate);
        new ErrorHandling("intent", "bullshit").execute();
        Toast.makeText(context, "Repeating alarm received.", Toast.LENGTH_SHORT).show();
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
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
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }
}
