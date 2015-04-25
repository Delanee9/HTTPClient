package com.delaney.httpclient;

import android.app.Application;
import android.os.AsyncTask;
import android.test.ApplicationTestCase;

import com.delaney.httpclient.databaseManagement.Database;
import com.delaney.httpclient.errorHandling.ErrorHandling;

import org.apache.http.client.methods.HttpPost;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        new Database(getContext()).setNameDB("Person", "+123456789", "qwerty");
        new Database(getContext()).setLocationDB("123,456", "+123456789");
        new Database(getContext()).setTimeAddedDB("13.10", "+123456789");
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetNameDB() {
        assertEquals("Person", new Database(getContext()).getNameDB("+123456789"));
        assertEquals(null, new Database(getContext()).getNameDB(null));
    }

    public void testGetHashedNumberDB() {
        assertEquals("qwerty", new Database(getContext()).getHashedMobileNumberDB("+123456789"));
        assertEquals(null, new Database(getContext()).getHashedMobileNumberDB(null));
    }

    public void testGetLocationDB() {
        assertEquals("123,456", new Database(getContext()).getLocationDB("+123456789"));
        assertEquals(null, new Database(getContext()).getLocationDB(null));
    }

    public void testGetTimeAddedDB() {
        assertEquals("13.10", new Database(getContext()).getTimeAddedDB("+123456789"));
        assertEquals(null, new Database(getContext()).getTimeAddedDB(null));
    }

    public void testGetContactListDB() {
        assertEquals("Person\n+123456789", new Database(getContext()).getContactListDB().toString());
    }

    public void testErrorHandling() {
        assertEquals(AsyncTask.Status.RUNNING, new ErrorHandling("test", "input").execute().getStatus());
        assertEquals(AsyncTask.Status.RUNNING, new ErrorHandling(null, "input").execute().getStatus());
        assertEquals(AsyncTask.Status.RUNNING, new ErrorHandling("test", null).execute().getStatus());
        assertEquals(AsyncTask.Status.RUNNING, new ErrorHandling(null, null).execute().getStatus());
    }

    public void testUplink() {
        HttpPost httpPost = new HttpPost("http://www.igneous-equinox-653.appspot.com/register");
        assertEquals(AsyncTask.Status.RUNNING, new Uplink().execute(httpPost).getStatus());
    }

    public void testHashFunction() {
        assertEquals("8f689525ab248afba0a5533b86872ac", Encryption.hashFunction("+353831054523"));
        assertEquals("", Encryption.hashFunction(""));
        assertEquals("", Encryption.hashFunction(null));
    }
}