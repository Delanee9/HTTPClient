package com.delaney.httpclient.Registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.delaney.httpclient.ICommon;
import com.delaney.httpclient.MainActivity;
import com.delaney.httpclient.R;

public class Registration2Activity extends ActionBarActivity implements ICommon {
    private Spinner spinner;
    private EditText editText;
    private String dropdownMenu;
    private Context context;

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
        setContentView(R.layout.activity_registration2);
        context = getApplicationContext();

        setContentView(R.layout.activity_registration2);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText = (EditText) findViewById(R.id.editText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.PhoneCodes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dropdownMenu = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * Onclick method for the continue button.
     *
     * @param view View
     */
    public void next(View view) {
        Intent intent = new Intent(this, Registration3Activity.class);
        storeMobileNumber(context , (dropdownMenu + editText.getText().toString()).trim());
        startActivity(intent);
    }

    /**
     * Stores the Users mobile number in the shared preferences.
     *
     * @param context      Context
     * @param mobileNumber String
     */
    private void storeMobileNumber(Context context, String mobileNumber) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving mobileNumber on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_MOBILE_NUMBER, mobileNumber);
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
}