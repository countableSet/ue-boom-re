// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.utils;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import android.app.ActivityManager$RunningServiceInfo;
import android.app.ActivityManager;
import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.InputStream;

public class UEUtils
{
    protected static final char[] hexArray;
    
    static {
        hexArray = "0123456789ABCDEF".toCharArray();
    }
    
    public static void CopyStream(final InputStream inputStream, final OutputStream outputStream) {
        if (inputStream == null || outputStream == null) {
            throw new IllegalArgumentException("Both stream should not be null");
        }
        try {
            final byte[] array = new byte[131072];
            while (true) {
                final int read = inputStream.read(array, 0, 131072);
                if (read == -1) {
                    break;
                }
                outputStream.write(array, 0, read);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static byte[] MACStringToByteArray(final String s) {
        final String[] split = s.replace(" ", "").split(":");
        final byte[] array = new byte[split.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte)((Character.digit(split[i].charAt(0), 16) << 4) + Character.digit(split[i].charAt(1), 16));
        }
        return array;
    }
    
    public static String byteArrayToFancyHexString(final byte[] array) {
        String string;
        if (array == null) {
            string = null;
        }
        else {
            string = "[" + byteArrayToNormalHexString(array) + ']';
        }
        return string;
    }
    
    public static String byteArrayToHexString(final byte[] array) {
        final char[] value = new char[array.length * 2];
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            value[i * 2] = UEUtils.hexArray[n >>> 4];
            value[i * 2 + 1] = UEUtils.hexArray[n & 0xF];
        }
        return new String(value);
    }
    
    public static String byteArrayToHexString(final byte[] array, final char c) {
        final char[] value = new char[array.length * 2 + array.length - 1];
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            value[i * 2 + i] = UEUtils.hexArray[n >>> 4];
            value[i * 2 + i + 1] = UEUtils.hexArray[n & 0xF];
            if (i + 1 != array.length) {
                value[i * 2 + i + 2] = c;
            }
        }
        return new String(value);
    }
    
    public static int byteArrayToInt(final byte[] array) {
        return byteArrayToInt(array, 0);
    }
    
    public static int byteArrayToInt(final byte[] array, final int n) {
        return array[n] << 24 | (array[n + 2] & 0xFF) << 8 | (array[n + 1] & 0xFF) << 16 | (array[n + 3] & 0xFF);
    }
    
    public static long byteArrayToLong(final byte[] array) {
        return byteArrayToLong(array, 0);
    }
    
    public static long byteArrayToLong(final byte[] array, final int n) {
        long n2 = 0L;
        for (int i = 0; i < 8; ++i) {
            n2 = (n2 << 8) + (array[n + i] & 0xFF);
        }
        return n2;
    }
    
    public static String byteArrayToMACString(final byte[] array) {
        return byteArrayToHexString(array, ':');
    }
    
    public static String byteArrayToNormalHexString(final byte[] array) {
        return byteArrayToHexString(array, ' ');
    }
    
    public static String byteArrayToUTF8String(final byte[] array) {
        return byteArrayToUTF8String(array, 0, array.length);
    }
    
    public static String byteArrayToUTF8String(final byte[] bytes, final int offset, final int length) {
        try {
            return new String(bytes, offset, length, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static int byteToUnsigned(final byte b) {
        return b & 0xFF;
    }
    
    public static int combineTwoBytesToOneInteger(final byte b, final byte b2) {
        return (b & 0xFF) << 8 | (b2 & 0xFF);
    }
    
    public static int compareVersions(final String s, final String s2) {
        final int n = 1;
        int n2;
        if (s == null || s2 == null) {
            n2 = 0;
        }
        else {
            final String[] split = s.split("\\.");
            final String[] split2 = s2.split("\\.");
            for (int n3 = 0; n3 < split.length && n3 < split2.length; ++n3) {
                final int int1 = Integer.parseInt(split[n3]);
                final int int2 = Integer.parseInt(split2[n3]);
                n2 = n;
                if (int1 > int2) {
                    return n2;
                }
                if (int1 < int2) {
                    n2 = -1;
                    return n2;
                }
            }
            n2 = n;
            if (split.length <= split2.length) {
                if (split.length < split2.length) {
                    n2 = -1;
                }
                else {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    public static long getUnsignedIntToLong(final int n) {
        return (long)n & 0xFFFFFFFFL;
    }
    
    public static byte[] hexStringToByteArray(final String s) {
        final String replace = s.replace(" ", "");
        final int length = replace.length();
        final byte[] array = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            array[i / 2] = (byte)((Character.digit(replace.charAt(i), 16) << 4) + Character.digit(replace.charAt(i + 1), 16));
        }
        return array;
    }
    
    public static byte[] intToByteArray(final int n) {
        return new byte[] { (byte)(n >> 24 & 0xFF), (byte)(n >> 16 & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF) };
    }
    
    public static long intToUnsignedLong(final int n) {
        return (long)n & 0xFFFFFFFFL;
    }
    
    public static boolean isServiceRunning(final Context context, final String anObject) {
        final Iterator<ActivityManager$RunningServiceInfo> iterator = ((ActivityManager)context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().service.getClassName().equals(anObject)) {
                return true;
            }
        }
        return false;
    }
    
    public static byte[] readAllBytes(final InputStream inputStream) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CopyStream(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
