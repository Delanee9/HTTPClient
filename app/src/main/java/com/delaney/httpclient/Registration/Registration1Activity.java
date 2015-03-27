package com.delaney.httpclient.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.delaney.httpclient.R;

public class Registration1Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration1);
    }

    /**
     * Onclick method for the continue button.
     *
     * @param view View
     */
    public void next(View view) {
        Intent intent = new Intent(this, Registration2Activity.class);
        startActivity(intent);
    }
}