// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import android.text.TextUtils;

public class iz implements lg<hz>
{
    private static final String a;
    
    static {
        a = iz.class.getSimpleName();
    }
    
    private static iw a(final String name) {
        while (true) {
            final iw a = iw.a;
            try {
                if (!TextUtils.isEmpty((CharSequence)name)) {
                    return Enum.valueOf(iw.class, name);
                }
            }
            catch (Exception ex) {}
            return a;
        }
    }
    
    private static void a(final hw hw, final JSONArray jsonArray) {
        if (jsonArray != null) {
            List<ic> c = null;
            List<ic> list;
            for (int i = 0; i < jsonArray.length(); ++i, c = list) {
                final JSONObject optJSONObject = jsonArray.optJSONObject(i);
                list = c;
                if (optJSONObject != null) {
                    if (optJSONObject.has("string")) {
                        if ((list = c) == null) {
                            list = (List<ic>)new ArrayList<id>();
                        }
                        final ic ic = new ic();
                        ic.a = optJSONObject.optString("string", "");
                        list.add(ic);
                    }
                    else {
                        list = c;
                        if (optJSONObject.has("com.flurry.proton.generated.avro.v2.EventParameterCallbackTrigger")) {
                            List<ic> list2;
                            if ((list2 = c) == null) {
                                list2 = (List<ic>)new ArrayList<id>();
                            }
                            final JSONObject optJSONObject2 = optJSONObject.optJSONObject("com.flurry.proton.generated.avro.v2.EventParameterCallbackTrigger");
                            list = list2;
                            if (optJSONObject2 != null) {
                                final id id = new id();
                                id.a = optJSONObject2.optString("event_name", "");
                                id.c = optJSONObject2.optString("event_parameter_name", "");
                                final JSONArray optJSONArray = optJSONObject2.optJSONArray("event_parameter_values");
                                String[] d;
                                if (optJSONArray != null) {
                                    d = new String[optJSONArray.length()];
                                    for (int j = 0; j < optJSONArray.length(); ++j) {
                                        d[j] = optJSONArray.optString(j, "");
                                    }
                                }
                                else {
                                    d = new String[0];
                                }
                                id.d = d;
                                list2.add(id);
                                list = list2;
                            }
                        }
                    }
                }
            }
            hw.c = c;
        }
    }
    
    private static void a(final hx hx, final JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            final ArrayList<hw> a = new ArrayList<hw>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                final JSONObject optJSONObject = jsonArray.optJSONObject(i);
                if (optJSONObject != null) {
                    final hw hw = new hw();
                    hw.b = optJSONObject.optString("partner", "");
                    a(hw, optJSONObject.optJSONArray("events"));
                    hw.d = a(optJSONObject.optString("method"));
                    hw.e = optJSONObject.optString("uri_template", "");
                    final JSONObject optJSONObject2 = optJSONObject.optJSONObject("body_template");
                    if (optJSONObject2 != null) {
                        final String optString = optJSONObject2.optString("string", "null");
                        if (!optString.equals("null")) {
                            hw.f = optString;
                        }
                    }
                    hw.g = optJSONObject.optInt("max_redirects", 5);
                    hw.h = optJSONObject.optInt("connect_timeout", 20);
                    hw.i = optJSONObject.optInt("request_timeout", 20);
                    hw.a = optJSONObject.optLong("callback_id", -1L);
                    final JSONObject optJSONObject3 = optJSONObject.optJSONObject("headers");
                    if (optJSONObject3 != null) {
                        final JSONObject optJSONObject4 = optJSONObject3.optJSONObject("map");
                        if (optJSONObject4 != null) {
                            hw.j = lz.a(optJSONObject4);
                        }
                    }
                    a.add(hw);
                }
            }
            hx.a = a;
        }
    }
    
    private static hz b(final InputStream inputStream) throws IOException {
        hz hz;
        if (inputStream == null) {
            hz = null;
        }
        else {
            final String str = new String(ly.a(inputStream));
            km.a(5, iz.a, "Proton response string: " + str);
            final hz hz2 = new hz();
            try {
                final JSONObject jsonObject = new JSONObject(str);
                hz2.a = jsonObject.optLong("issued_at", -1L);
                hz2.b = jsonObject.optLong("refresh_ttl", 3600L);
                hz2.c = jsonObject.optLong("expiration_ttl", 86400L);
                final JSONObject optJSONObject = jsonObject.optJSONObject("global_settings");
                hz2.d = new ig();
                if (optJSONObject != null) {
                    hz2.d.a = b(optJSONObject.optString("log_level"));
                }
                final JSONObject optJSONObject2 = jsonObject.optJSONObject("pulse");
                final hx e = new hx();
                if (optJSONObject2 != null) {
                    a(e, optJSONObject2.optJSONArray("callbacks"));
                    e.b = optJSONObject2.optInt("max_callback_retries", 3);
                    e.c = optJSONObject2.optInt("max_callback_attempts_per_report", 15);
                    e.d = optJSONObject2.optInt("max_report_delay_seconds", 600);
                    e.e = optJSONObject2.optString("agent_report_url", "");
                }
                hz2.e = e;
                final JSONObject optJSONObject3 = jsonObject.optJSONObject("analytics");
                hz2.f = new ij();
                hz = hz2;
                if (optJSONObject3 != null) {
                    hz2.f.b = optJSONObject3.optBoolean("analytics_enabled", true);
                    hz2.f.a = optJSONObject3.optInt("max_session_properties", 10);
                    hz = hz2;
                }
            }
            catch (JSONException cause) {
                throw new IOException("Exception while deserialize: ", (Throwable)cause);
            }
        }
        return hz;
    }
    
    private static ih b(final String name) {
        while (true) {
            final ih f = ih.f;
            try {
                if (!TextUtils.isEmpty((CharSequence)name)) {
                    return Enum.valueOf(ih.class, name);
                }
            }
            catch (Exception ex) {}
            return f;
        }
    }
}
