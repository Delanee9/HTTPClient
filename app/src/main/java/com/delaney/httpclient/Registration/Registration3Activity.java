package com.delaney.httpclient.Registration;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.delaney.httpclient.ICommon;
import com.delaney.httpclient.MainActivity;
import com.delaney.httpclient.R;
import com.delaney.httpclient.UpstreamMessage;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.List;

public class Registration3Activity extends ActionBarActivity implements ICommon {
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private GoogleCloudMessaging gcm;
    private  TelephonyManager telephonyManager;
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private Context context;
    private String regid;
    private String selectedContacts;
    private List<String> selectedItems = new ArrayList<String>();
    private ListView listView;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration3);

        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(getContacts(this.getContentResolver()));
        gcm = GoogleCloudMessaging.getInstance(this);
        context = getApplicationContext();
        registerInBackground();

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                try {
                    String[] number = listView.getAdapter().getItem(position).toString().split("\\n");
                    String formattedNumber = phoneNumberUtil.format(phoneNumberUtil.parse(number[1], telephonyManager.getSimCountryIso()), PhoneNumberUtil.PhoneNumberFormat.E164);
                    if(selectedItems.contains(formattedNumber)) {
                        selectedItems.remove(formattedNumber);
                        Toast.makeText(context, "removed - " + selectedItems.size(), Toast.LENGTH_SHORT).show();
                    } else {
                        selectedItems.add(formattedNumber);
                        Toast.makeText(context, "added - " + selectedItems.size(), Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e) {
                    Toast.makeText(context, "error - " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Registers the application with GCM servers asynchronously.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if(gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    storeRegistrationId(context, regid);

                    sendRegistrationIdToBackend();
                } catch(Exception e) {
                    msg = "Error :" + e.getMessage();
                }
                return msg;
            }
        }.execute();
    }

    /**
     * Bundles the gcm ID and the users mobile number and sends it to the server for registration.
     */
    private void sendRegistrationIdToBackend() {
        UpstreamMessage.postRegister(getRegistrationId(context), getMobileNumber(context));
    }

    /**
     * Stores the unique gcm registration ID in the shared preferences.
     *
     * @param context Context
     * @param regId   String
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
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
     * Returns Registration ID from the shared preferences.
     *
     * @param context Context
     * @return String
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if(registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Returns the users mobile number from the shared preferences.
     *
     * @param context Context
     * @return String
     */
    private String getMobileNumber(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String mobileNumber = prefs.getString(PROPERTY_MOBILE_NUMBER, "");
        if(mobileNumber.isEmpty()) {
            Log.i(TAG, "Mobile number not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if(registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return mobileNumber;
    }

    private ArrayAdapter<String> getContacts(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<String> arrayList = new ArrayList<String>();
        while(cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) + "\n" + cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
        List<String> contacts = arrayList;
        cursor.close();
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayList);
    }

    /**
     * Onclick method for the continue button.
     *
     * @param view View
     */
    public void next(View view) {
        String numberString = "";
        Intent intent = new Intent(this, Registration4Activity.class);
        for(String item : selectedItems) {
            numberString = numberString + item + ",";
        }
        UpstreamMessage.postAdd(getRegistrationId(context), numberString);
        startActivity(intent);
    }
}