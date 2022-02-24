// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.HashMap;
import java.util.Map;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import android.content.Intent;
import android.content.Context;
import java.io.Closeable;
import android.os.Looper;
import android.net.Uri;
import android.text.TextUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public final class ly
{
    private static final String a;
    
    static {
        a = ly.class.getSimpleName();
    }
    
    public static double a(double n, final int n2) {
        if (n2 != -1) {
            n = Math.round(Math.pow(10.0, n2) * n) / Math.pow(10.0, n2);
        }
        return n;
    }
    
    public static long a(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[1024];
        long n = 0L;
        while (true) {
            final int read = inputStream.read(array);
            if (read < 0) {
                break;
            }
            outputStream.write(array, 0, read);
            n += read;
        }
        return n;
    }
    
    public static String a(final String str) {
        String string;
        if (TextUtils.isEmpty((CharSequence)str)) {
            string = str;
        }
        else {
            final Uri parse = Uri.parse(str);
            string = str;
            if (parse != null) {
                string = str;
                if (parse.getScheme() == null) {
                    string = "http://" + str;
                }
            }
        }
        return string;
    }
    
    public static String a(final byte[] array) {
        final StringBuilder sb = new StringBuilder(array.length * 2);
        final char[] array3;
        final char[] array2 = array3 = new char[16];
        array3[0] = '0';
        array3[1] = '1';
        array3[2] = '2';
        array3[3] = '3';
        array3[4] = '4';
        array3[5] = '5';
        array3[6] = '6';
        array3[7] = '7';
        array3[8] = '8';
        array3[9] = '9';
        array3[10] = 'a';
        array3[11] = 'b';
        array3[12] = 'c';
        array3[13] = 'd';
        array3[14] = 'e';
        array3[15] = 'f';
        for (final byte b : array) {
            final byte b2 = (byte)(b & 0xF);
            sb.append(array2[(byte)((b & 0xF0) >> 4)]);
            sb.append(array2[b2]);
        }
        return sb.toString();
    }
    
    public static void a() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Must be called from the main thread!");
        }
    }
    
    public static void a(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (Throwable t) {}
    }
    
    public static boolean a(final long n) {
        boolean b = false;
        if (n == 0L || System.currentTimeMillis() <= n) {
            b = true;
        }
        return b;
    }
    
    public static boolean a(final Context context, final String s) {
        boolean b2;
        final boolean b = b2 = false;
        if (context != null) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                b2 = b;
            }
            else {
                try {
                    final int checkPermission = context.getPackageManager().checkPermission(s, context.getPackageName());
                    b2 = b;
                    if (checkPermission == 0) {
                        b2 = true;
                    }
                }
                catch (Exception ex) {
                    km.a(6, ly.a, "Error occured when checking if app has permission.  Error: " + ex.getMessage());
                    b2 = b;
                }
            }
        }
        return b2;
    }
    
    public static boolean a(final Intent intent) {
        return jy.a().a.getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }
    
    public static byte[] a(final InputStream inputStream) throws IOException {
        byte[] byteArray;
        if (inputStream == null) {
            byteArray = null;
        }
        else {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            a(inputStream, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        }
        return byteArray;
    }
    
    public static String b(final String s) {
        String substring;
        if (s == null) {
            substring = "";
        }
        else {
            substring = s;
            if (s.length() > 255) {
                substring = s.substring(0, 255);
            }
        }
        return substring;
    }
    
    public static String b(final byte[] bytes) {
        final String s = null;
        String s2;
        if (bytes == null) {
            s2 = s;
        }
        else {
            try {
                s2 = new String(bytes, "ISO-8859-1");
            }
            catch (UnsupportedEncodingException ex) {
                km.a(5, ly.a, "Unsupported ISO-8859-1:" + ex.getMessage());
                s2 = s;
            }
        }
        return s2;
    }
    
    public static void b() {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            throw new IllegalStateException("Must be called from a background thread!");
        }
    }
    
    public static String c(String encode) {
        try {
            encode = URLEncoder.encode(encode, "UTF-8");
            return encode;
        }
        catch (UnsupportedEncodingException ex) {
            km.a(5, ly.a, "Cannot encode '" + encode + "'");
            encode = "";
            return encode;
        }
    }
    
    public static String d(String decode) {
        try {
            decode = URLDecoder.decode(decode, "UTF-8");
            return decode;
        }
        catch (UnsupportedEncodingException ex) {
            km.a(5, ly.a, "Cannot decode '" + decode + "'");
            decode = "";
            return decode;
        }
    }
    
    public static byte[] e(final String s) {
        final byte[] array = null;
        byte[] bytes;
        if (TextUtils.isEmpty((CharSequence)s)) {
            bytes = array;
        }
        else {
            try {
                bytes = s.getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException ex) {
                km.a(5, ly.a, "Unsupported UTF-8: " + ex.getMessage());
                bytes = array;
            }
        }
        return bytes;
    }
    
    public static byte[] f(final String s) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(s.getBytes(), 0, s.length());
            return instance.digest();
        }
        catch (NoSuchAlgorithmException ex) {
            km.a(6, ly.a, "Unsupported SHA1: " + ex.getMessage());
            return null;
        }
    }
    
    public static String g(final String s) {
        return s.replace("\\b", "").replace("\\n", "").replace("\\r", "").replace("\\t", "").replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
    }
    
    public static Map<String, String> h(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        if (!TextUtils.isEmpty((CharSequence)s)) {
            final String[] split = s.split("&");
            for (int length = split.length, i = 0; i < length; ++i) {
                final String[] split2 = split[i].split("=");
                if (!split2[0].equals("event")) {
                    hashMap.put(d(split2[0]), d(split2[1]));
                }
            }
        }
        return hashMap;
    }
    
    public static long i(final String s) {
        long n;
        if (s == null) {
            n = 0L;
        }
        else {
            final int length = s.length();
            long n2 = 1125899906842597L;
            int index = 0;
            while (true) {
                n = n2;
                if (index >= length) {
                    break;
                }
                final long n3 = s.charAt(index);
                ++index;
                n2 = n3 + n2 * 31L;
            }
        }
        return n;
    }
}
