package com.delaney.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
;
import java.util.ArrayList;
import java.util.List;

public class UpstreamMessage {

    private static final String PARAMETER_REG_ID = "regId";
    private static final String PARAMETER_MOBILE = "mobile";

    public static void postRegister(String gcmId, String mobile) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/register");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_MOBILE, mobile));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {

        }
    }

    public static void postUnregister(String gcmId) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/unregister");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {

        }
    }

    public static void postAdd(String gcmId, String friendsAdded) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/add");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {

        }
    }

    public static void postRemove() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/remove");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", "12345"));
            nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {

        }
    }

    public static void postVisibility(String visibility, String friendIds) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/script.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", "12345"));
            nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {

        }
    }

    public static void postUpdate() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/add");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", "12345"));
            nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {

        }
    }
}
