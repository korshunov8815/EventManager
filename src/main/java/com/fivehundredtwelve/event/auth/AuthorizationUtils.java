package com.fivehundredtwelve.event.auth;

/**
 * Created by anna on 27.04.15.
 */
public class AuthorizationUtils {
    public static String encodeMD5(String value) {
        MD5 md5 = new MD5();
        return md5.getHash(value);
    }
}
