package com.delaney.httpclient.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.delaney.httpclient.R;

public class Registration4Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration4);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration4, menu);
        return true;
    }



    /**
     * Onclick method for the continue button.
     *
     * @param view View
     */
    public void next(View view) {
        Intent intent = new Intent(this, Registration5Activity.class);
        startActivity(intent);
    }
}
