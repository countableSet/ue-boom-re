// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.ArrayList;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ht
{
    private static final String a;
    
    static {
        a = ht.class.getSimpleName();
    }
    
    public static Map<String, List<String>> a(final String str) {
        km.a(3, ht.a, "Parsing referrer map");
        Map<String, List<String>> emptyMap;
        if (str == null) {
            emptyMap = Collections.emptyMap();
        }
        else {
            final HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
            for (final String str2 : str.split("&")) {
                final String[] split2 = str2.split("=");
                if (split2.length != 2) {
                    km.a(5, ht.a, "Invalid referrer Element: " + str2 + " in referrer tag " + str);
                }
                else {
                    final String decode = URLDecoder.decode(split2[0]);
                    final String decode2 = URLDecoder.decode(split2[1]);
                    if (hashMap.get(decode) == null) {
                        hashMap.put(decode, new ArrayList<String>());
                    }
                    hashMap.get(decode).add(decode2);
                }
            }
            for (final Map.Entry<String, ArrayList<String>> entry : hashMap.entrySet()) {
                km.a(3, ht.a, "entry: " + entry.getKey() + "=" + entry.getValue());
            }
            final StringBuilder sb = new StringBuilder();
            if (hashMap.get("utm_source") == null) {
                sb.append("Campaign Source is missing.\n");
            }
            if (hashMap.get("utm_medium") == null) {
                sb.append("Campaign Medium is missing.\n");
            }
            if (hashMap.get("utm_campaign") == null) {
                sb.append("Campaign Name is missing.\n");
            }
            if (sb.length() > 0) {
                km.a(5, ht.a, "Detected missing referrer keys : " + sb.toString());
            }
            emptyMap = hashMap;
        }
        return emptyMap;
    }
}
