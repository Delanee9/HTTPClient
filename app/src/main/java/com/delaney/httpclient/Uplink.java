package com.delaney.httpclient;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

class Uplink extends AsyncTask<HttpPost, Void, Integer> {
    protected Integer doInBackground(HttpPost... httpPosts) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPosts[0]);
            return response.getStatusLine().getStatusCode();
        } catch(Exception e) {
            return 6;
        }
    }
}