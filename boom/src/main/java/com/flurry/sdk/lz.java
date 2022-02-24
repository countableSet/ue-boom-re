// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.List;
import org.json.JSONArray;

public final class lz
{
    public static List<JSONObject> a(final JSONArray jsonArray) throws JSONException {
        final ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            final Object value = jsonArray.get(i);
            if (!(value instanceof JSONObject)) {
                throw new JSONException("Array contains unsupported objects. JSONArray param must contain JSON object.");
            }
            list.add((JSONObject)value);
        }
        return list;
    }
    
    public static Map<String, String> a(final JSONObject jsonObject) throws JSONException {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String next = keys.next();
            if (!(next instanceof String)) {
                throw new JSONException("JSONObject contains unsupported type for key in the map.");
            }
            final String s = next;
            final Object value = jsonObject.get(s);
            if (!(value instanceof String)) {
                throw new JSONException("JSONObject contains unsupported type for value in the map.");
            }
            hashMap.put(s, (String)value);
        }
        return hashMap;
    }
    
    public static void a(final JSONObject jsonObject, final String s, final float f) throws IOException, JSONException {
        jsonObject.putOpt(s, (Object)f);
    }
    
    public static void a(final JSONObject jsonObject, final String s, final Object o) throws NullPointerException, JSONException {
        if (o == null) {
            jsonObject.put(s, JSONObject.NULL);
        }
        else {
            jsonObject.put(s, o);
        }
    }
    
    public static void a(final JSONObject jsonObject, final String s, final String s2) throws IOException, JSONException {
        if (s2 != null) {
            jsonObject.put(s, (Object)s2);
        }
        else {
            jsonObject.put(s, JSONObject.NULL);
        }
    }
    
    public static List<String> b(final JSONArray jsonArray) throws JSONException {
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            final Object value = jsonArray.get(i);
            if (!(value instanceof String)) {
                throw new JSONException("Array contains unsupported objects. JSONArray param must contain String object.");
            }
            list.add((String)value);
        }
        return list;
    }
}
