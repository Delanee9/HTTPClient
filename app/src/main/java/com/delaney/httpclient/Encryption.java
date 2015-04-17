package com.delaney.httpclient;

import com.delaney.httpclient.errorHandling.ErrorHandling;

import java.security.MessageDigest;

public class Encryption {

    public static String hashFunction(String input) {
        try {
            MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            byte [] byteDigest = messageDigest.digest();

            StringBuilder hexString = new StringBuilder();
            for(byte token : byteDigest) {
                hexString.append(Integer.toHexString(0xFF & token));
            }

            return hexString.toString();
        } catch (Exception e) {
            new ErrorHandling("Failure in encryption", e.toString()).execute();
            return "";
        }
    }
}