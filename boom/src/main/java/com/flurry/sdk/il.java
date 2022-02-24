// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.location.Location;
import java.util.Map;

public class il extends kr
{
    private static final String a;
    
    static {
        a = il.class.getSimpleName();
    }
    
    public final String a(String target, final Map<String, String> map) {
        final String a = this.a(target);
        String s = target;
        String value;
        String string;
        String string2;
        String b;
        Location g;
        String string3;
        Location g2;
        String string4;
        String str;
        String a2;
        for (target = a; target != null; target = a2) {
            if (kr.a("timestamp_epoch_millis", target)) {
                value = String.valueOf(System.currentTimeMillis());
                km.a(3, il.a, "Replacing param timestamp_epoch_millis with: " + value);
                target = s.replace(target, ly.c(value));
            }
            else if (kr.a("session_duration_millis", target)) {
                jk.a();
                string = Long.toString(jk.e());
                km.a(3, il.a, "Replacing param session_duration_millis with: " + string);
                target = s.replace(target, ly.c(string));
            }
            else if (kr.a("fg_timespent_millis", target)) {
                jk.a();
                string2 = Long.toString(jk.e());
                km.a(3, il.a, "Replacing param fg_timespent_millis with: " + string2);
                target = s.replace(target, ly.c(string2));
            }
            else if (kr.a("install_referrer", target)) {
                if ((b = new hs().b()) == null) {
                    b = "";
                }
                km.a(3, il.a, "Replacing param install_referrer with: " + b);
                target = s.replace(target, ly.c(b));
            }
            else if (kr.a("geo_latitude", target)) {
                g = jp.a().g();
                string3 = "";
                if (g != null) {
                    string3 = "" + ly.a(g.getLatitude(), jp.d());
                }
                km.a(3, il.a, "Replacing param geo_latitude with: " + string3);
                target = s.replace(target, ly.c(string3));
            }
            else if (kr.a("geo_longitude", target)) {
                g2 = jp.a().g();
                string4 = "";
                if (g2 != null) {
                    string4 = "" + ly.a(g2.getLongitude(), jp.d());
                }
                km.a(3, il.a, "Replacing param geo_longitude with: " + string4);
                target = s.replace(target, ly.c(string4));
            }
            else if (kr.a("publisher_user_id", target)) {
                str = (String)lp.a().a("UserId");
                km.a(3, il.a, "Replacing param publisher_user_id with: " + str);
                target = s.replace(target, ly.c(str));
            }
            else if (kr.a("event_name", target)) {
                if (map.containsKey("event_name")) {
                    km.a(3, il.a, "Replacing param event_name with: " + map.get("event_name"));
                    target = s.replace(target, ly.c(map.get("event_name")));
                }
                else {
                    km.a(3, il.a, "Replacing param event_name with empty string");
                    target = s.replace(target, "");
                }
            }
            else if (kr.a("event_time_millis", target)) {
                if (map.containsKey("event_time_millis")) {
                    km.a(3, il.a, "Replacing param event_time_millis with: " + map.get("event_time_millis"));
                    target = s.replace(target, ly.c(map.get("event_time_millis")));
                }
                else {
                    km.a(3, il.a, "Replacing param event_time_millis with empty string");
                    target = s.replace(target, "");
                }
            }
            else {
                km.a(3, il.a, "Unknown param: " + target);
                target = s.replace(target, "");
            }
            a2 = this.a(target);
            s = target;
        }
        return s;
    }
}
