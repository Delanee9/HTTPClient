package com.delaney.httpclient;

import com.delaney.httpclient.errorHandling.ErrorHandling;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UpstreamMessage {
    private static final String PARAMETER_REG_ID = "regId";
    private static final String PARAMETER_MOBILE = "mobile";
    private static final String PARAMETER_FRIENDS_LIST = "friendsList";
    private static final Logger logger = Logger.getLogger(UpstreamMessage.class.getName());

    /**
     * Sends a HTTP POST request to the server to register the users details.
     *
     * @param gcmId  String
     * @param mobile String
     */
    public static void postRegister(String gcmId, String mobile) {
        HttpPost httpPost = new HttpPost("http://www.igneous-equinox-653.appspot.com/register");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_MOBILE, mobile));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            new Uplink().execute(httpPost);
        } catch(Exception e) {
            logger.warning("Failed to POST Register - " + e);
            new ErrorHandling("Failure in POST Register", e.toString()).execute();
        }
    }

    /**
     * Sends a HTTP POST request to the server to unregister the user.
     *
     * @param gcmId String
     */
    public static void postUnregister(String gcmId) {
        HttpPost httpPost = new HttpPost("http://www.igneous-equinox-653.appspot.com/unregister");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            new Uplink().execute(httpPost);
        } catch(Exception e) {
            logger.warning("Failed to POST Unregister - " + e);
            new ErrorHandling("Failure in POST Unregister", e.toString()).execute();
        }
    }

    /**
     * Sends a HTTP POST request to the server to add additional contacts to a users profile.
     *
     * @param gcmId        String
     * @param friendsAdded String
     */
    public static void postAdd(String gcmId, String friendsAdded) {
        HttpPost httpPost = new HttpPost("http://www.igneous-equinox-653.appspot.com/add");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_FRIENDS_LIST, friendsAdded));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            new Uplink().execute(httpPost);
        } catch(Exception e) {
            logger.warning("Failed to POST Add - " + e);
            new ErrorHandling("Failure in POST Add", e.toString()).execute();
        }
    }

    /**
     * Sends a HTTP POST request to the server to remove contacts from a users profile.
     *
     * @param gcmId          String
     * @param friendsRemoved String
     */
    public static void postRemove(String gcmId, String friendsRemoved) {
        HttpPost httpPost = new HttpPost("https://www.igneous-equinox-653.appspot.com/remove");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_FRIENDS_LIST, friendsRemoved));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            new Uplink().execute(httpPost);
        } catch(Exception e) {
            logger.warning("Failed to POST Remove - " + e);
            new ErrorHandling("Failure in POST Remove", e.toString()).execute();
        }
    }

    /**
     * Sends a HTTP POST request to the server to set the visibility of a user.
     *
     * @param gcmId      String
     * @param visibility String
     * @param friendIds  String
     */
    public static void postVisibility(String gcmId, String visibility, String friendIds) {
        HttpPost httpPost = new HttpPost("https://www.igneous-equinox-653.appspot.com/visibility");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_FRIENDS_LIST, friendIds));
            nameValuePairs.add(new BasicNameValuePair("visibility", visibility));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            new Uplink().execute(httpPost);
        } catch(Exception e) {
            logger.warning("Failed to POST Visibility - " + e);
            new ErrorHandling("Failure in POST Visibility", e.toString()).execute();
        }
    }

    /**
     * Sends a HTTP POST request to the server to update a contacts location details.
     *
     * @param gcmId    String
     * @param location String
     */
    public static void postUpdate(String gcmId, String location) {
        HttpPost httpPost = new HttpPost("https://www.igneous-equinox-653.appspot.com/update");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(PARAMETER_REG_ID, gcmId));
            nameValuePairs.add(new BasicNameValuePair("location", location));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            new Uplink().execute(httpPost);
        } catch(Exception e) {
            logger.warning("Failed to POST Update - " + e);
            new ErrorHandling("Failure in POST Update", e.toString()).execute();
        }
    }
}