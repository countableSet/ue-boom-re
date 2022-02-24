// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.centurion.utils.MAC;
import java.util.Arrays;
import java.util.Iterator;
import android.support.annotation.NonNull;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.HashMap;

public class MD5Helper
{
    public static final HashMap<Object, byte[]> mCache;
    private static MessageDigest mDigest;
    
    static {
        mCache = new HashMap<Object, byte[]>();
        try {
            MD5Helper.mDigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }
    
    public static byte[] calculateMD5(byte[] digest) {
        synchronized (MD5Helper.class) {
            MD5Helper.mDigest.update(digest);
            digest = MD5Helper.mDigest.digest();
            return digest;
        }
    }
    
    private static byte[] checkInCache(@NonNull final String s) {
        for (final Object next : MD5Helper.mCache.keySet()) {
            if (next instanceof String && next.equals(s)) {
                return MD5Helper.mCache.get(s);
            }
        }
        return null;
    }
    
    private static byte[] checkInCache(@NonNull byte[] array) {
        for (final byte[] next : MD5Helper.mCache.keySet()) {
            if (next instanceof byte[] && Arrays.equals(next, array)) {
                array = MD5Helper.mCache.get(array);
                return array;
            }
        }
        array = null;
        return array;
    }
    
    public static byte[] md5(final MAC mac) {
        return md5(mac.getBytes());
    }
    
    public static byte[] md5(final String key) {
        byte[] value;
        if ((value = checkInCache(key)) == null) {
            value = calculateMD5(key.getBytes());
            MD5Helper.mCache.put(key, value);
        }
        return value;
    }
    
    public static byte[] md5(final byte[] key) {
        byte[] value;
        if ((value = checkInCache(key)) == null) {
            value = calculateMD5(key);
            MD5Helper.mCache.put(key, value);
        }
        return value;
    }
}
