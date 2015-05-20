package com.delaney.httpclient.errorHandling;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Class to send error messages in email form for bug logging.
 */
public class ErrorHandling extends AsyncTask<String, Integer, Boolean> implements IMail{

    private final Mail mail = new Mail(EMAIL, PASSWORD);

    public ErrorHandling(String subject, String exception) {
        mail.setTo(EMAIL);
        mail.setFrom(EMAIL);
        mail.setSubject(subject);
        mail.setBody(subject + " - " + exception);
    }

    protected Boolean doInBackground(String... params) {
        try {
            mail.send();
            return true;
        } catch(Exception e) {
            Log.e(ErrorHandling.class.getName(), "Bad account details");

            return false;
        }
    }
}