// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.FilterOutputStream;
import java.util.Iterator;
import org.json.JSONArray;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONObject;

public class iy implements lg<hy>
{
    private static final String a;
    
    static {
        a = iy.class.getSimpleName();
    }
    
    private static void a(final JSONObject jsonObject, final String s, final String s2) throws IOException, JSONException {
        if (s2 != null) {
            jsonObject.put(s, (Object)s2);
        }
        else {
            jsonObject.put(s, JSONObject.NULL);
        }
    }
}
