package com.delaney.httpclient.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.delaney.httpclient.LocationRetrieval;
import com.delaney.httpclient.NavigationDrawerFragment;
import com.delaney.httpclient.R;
import com.delaney.httpclient.UpstreamMessage;
import com.delaney.httpclient.databaseManagement.Database;
import com.delaney.httpclient.errorHandling.ErrorHandling;
import com.delaney.httpclient.registration.Registration1Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String PROPERTY_REG_ID = "registration_id";
    private GoogleMap googleMap;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Context context = getApplicationContext();

            if(checkPlayServices()) {
                String regid = getRegistrationId(context);

                if(regid.isEmpty()) {
                    Intent intent = new Intent(this, Registration1Activity.class);
                    startActivity(intent);
                }

                navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
                mTitle = getTitle();

                createLocationRequest();

                googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
                googleApiClient.connect();

                initialiseMap();

                navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

            } else {
                new ErrorHandling("MainActivity onCreate", "No valid Google Play Services APK found.").execute();
            }
        } catch(Exception e) {
            new ErrorHandling("Failure in MainActivity onCreate", e.toString()).execute();
        }
    }

    void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(300000);
        locationRequest.setFastestInterval(150000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "OnConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        setUpMap();
        String locationUpdate = String.valueOf(currentLocation.getLatitude()) + "," + String.valueOf(currentLocation.getLongitude());
        UpstreamMessage.postUpdate(getRegistrationId(getApplicationContext()), locationUpdate);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if(googleApiClient.isConnected()) {
            startLocationUpdates();
            setUpMap();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
    }

    void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_section1));
    }

    private void initialiseMap() {
        if(googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if(googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        if(currentLocation != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
            friendLocation();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        } else {
            Location newLocation = LocationRetrieval.getLastKnownLocation(getApplicationContext());
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(newLocation.getLatitude(), newLocation.getLongitude())));
            friendLocation();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLocation.getLatitude(), newLocation.getLongitude()), 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        }
    }

    private void friendLocation() {
        List<String> friendLocations = new Database(getApplicationContext()).getContactListDB();
        for(String item : friendLocations) {
            String [] data = item.split(",");
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(data[1]), Double.valueOf(data[2])))).setTitle(data[0]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns Registration ID from the shared preferences.
     *
     * @param context Context
     * @return String
     */
    private String getRegistrationId(Context context) {
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

    /**
     * Check the device to ensure that it has the Google Play Services APK installed. Prompting the user to download the app if it is not present.
     *
     * @return boolean
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 9000).show();
            } else {
                Log.i("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }
}