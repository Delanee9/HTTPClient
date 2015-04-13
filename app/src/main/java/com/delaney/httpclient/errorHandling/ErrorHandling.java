package com.delaney.httpclient.errorHandling;

import android.os.AsyncTask;
import android.util.Log;

//import javax.mail.AuthenticationFailedException;
//
//public class ErrorHandling extends AsyncTask<String, Integer, Boolean> implements IMail{
//
//    private final Mail mail = new Mail(EMAIL, PASSWORD);
//
//    public ErrorHandling(String subject, String exception) {
//        String toArr[] = {EMAIL};
//        mail.setTo(toArr);
//        mail.setFrom(EMAIL);
//        mail.setSubject(subject);
//        mail.setBody(subject + " - " + exception);
//    }
//
//    protected Boolean doInBackground(String... params) {
//        try {
//            mail.send();
//            return true;
//        } catch(AuthenticationFailedException e) {
//            Log.e(ErrorHandling.class.getName(), "Bad account details");
//            e.printStackTrace();
//            return false;
//        } catch(Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}