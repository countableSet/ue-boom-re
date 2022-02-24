// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import android.support.annotation.NonNull;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class Utils
{
    protected static final char[] hexArray;
    
    static {
        hexArray = "0123456789ABCDEF".toCharArray();
    }
    
    @NonNull
    public static String MD5(String byteArrayToHexString) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(byteArrayToHexString.getBytes());
            byteArrayToHexString = byteArrayToHexString(instance.digest());
            return byteArrayToHexString;
        }
        catch (NoSuchAlgorithmException ex) {
            byteArrayToHexString = "";
            return byteArrayToHexString;
        }
    }
    
    public static String byteArrayToHexString(final byte[] array) {
        final char[] value = new char[array.length * 2];
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            value[i * 2] = Utils.hexArray[n >>> 4];
            value[i * 2 + 1] = Utils.hexArray[n & 0xF];
        }
        return new String(value);
    }
    
    public static String colorToString(final int i) {
        return String.format("#%06X", i);
    }
    
    public static void copyStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[16384];
        while (true) {
            final int read = inputStream.read(array, 0, 16384);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
        }
        outputStream.flush();
    }
    
    public static byte[] readStreamAsByteArray(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static String readStreamAsString(final InputStream inputStream, final String charsetName) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, byteArrayOutputStream);
        return new String(byteArrayOutputStream.toByteArray(), charsetName);
    }
    
    public static int stringToColor(final String s) {
        int n = 0;
        try {
            if (s.charAt(0) == '#') {
                n = Integer.parseInt(s.substring(1), 16);
            }
            else {
                n = Integer.parseInt(s, 16);
            }
            return n;
        }
        catch (Exception ex) {
            return n;
        }
        return n;
    }
}
