package com.delaney.httpclient.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.delaney.httpclient.Encryption;
import com.delaney.httpclient.NavigationDrawerFragment;
import com.delaney.httpclient.R;
import com.delaney.httpclient.UpstreamMessage;
import com.delaney.httpclient.databaseManagement.Database;
import com.delaney.httpclient.errorHandling.ErrorHandling;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.List;


public class FriendsActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private NavigationDrawerFragment navigationDrawerFragment;
    private List<String> selectedItems = new ArrayList<String>();
    private List<String> storedFriends = new ArrayList<String>();
    private ListView listView;
    private TelephonyManager telephonyManager;
    private Database database;
    private Context context;

    private CharSequence mTitle;

    /**
     * Returns the App version number.
     *
     * @param context Context
     * @return int
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch(PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_friends);

            navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            mTitle = getTitle();

            navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

            context = getApplicationContext();

            listView = (ListView) findViewById(R.id.listView);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(getContacts(this.getContentResolver()));



            database = new Database(context);

            setCheckedItems();

            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    try {
                        String[] number = listView.getAdapter().getItem(position).toString().split("\\n");
                        String formattedNumber = phoneNumberUtil.format(phoneNumberUtil.parse(number[1], telephonyManager.getSimCountryIso()), PhoneNumberUtil.PhoneNumberFormat.E164);
                        if(selectedItems.contains(formattedNumber)) {
                            selectedItems.remove(formattedNumber);
                            database.removeContactDataDB(formattedNumber);
                        } else {
                            selectedItems.add(formattedNumber);
                            database.setNameDB(number[0], formattedNumber, Encryption.hashFunction(formattedNumber));
                        }
                    } catch(Exception e) {
                        Toast.makeText(context, "error - " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch(Exception e) {
        new ErrorHandling("Failure in FriendsActivity onCreate", e.toString()).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_friends, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
    }

    void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_section2));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns a list of contacts stored on the phone.
     *
     * @param contentResolver ContentResolver
     * @return ArrayAdapter<String>
     */
    private ArrayAdapter<String> getContacts(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<String> arrayList = new ArrayList<String>();
        while(cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) + "\n" + cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
        cursor.close();
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayList);
    }

    private void setCheckedItems() {
//        storedFriends = database.getContactListDB();
//        for(int i = 0; i < listView.getCount(); i++) {
//            if(storedFriends.contains(listView.getItemAtPosition(i).toString()))
//            listView.setItemChecked(i, true);
//        }
    }

    public void update(View view) {
        String numberString = "";
        for(String item : selectedItems) {
            numberString = numberString + Encryption.hashFunction(item) + ",";
        }
        UpstreamMessage.postAdd(getRegistrationId(context), numberString);
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
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
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
