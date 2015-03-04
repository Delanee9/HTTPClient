package com.delaney.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UpstreamMessage {

    private static final String PARAMETER_REG_ID = "regId";
    private static final String PARAMETER_MOBILE = "mobile";
    private static final String PARAMETER_FRIENDS_LIST = "friendsList";
    private static final Logger logger = Logger.getLogger(UpstreamMessage.class.getName());

    public static void postRegister(String gcmId, String mobile) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/register");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_MOBILE, mobile));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {

        }
    }

    public static void postUnregister(String gcmId) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/unregister");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {

        }
    }

    public static void postAdd(String gcmId, String friendsAdded) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/add");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_FRIENDS_LIST, friendsAdded));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {

        }
    }

    public static void postRemove(String gcmId, String friendsRemoved) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/remove");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_FRIENDS_LIST, friendsRemoved));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {

        }
    }

    public static void postVisibility(String gcmId, String visibility, String friendIds) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/visibility");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_FRIENDS_LIST, friendIds));
            nameValuePairs.add(new BasicNameValuePair("visibility", visibility));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {

        }
    }

    public static void postUpdate(String gcmId, String location) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://www.igneous-equinox-653.appspot.com/update");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair("location", location));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {

        }
    }
}