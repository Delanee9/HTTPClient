package com.delaney.httpclient;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

/**
 * Class to retrieve the location of the device.
 */
public class LocationRetrieval {
    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for(String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if(location == null) {
                continue;
            }
            if(bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }
}