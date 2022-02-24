// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;
import com.flurry.android.FlurryEventRecordStatus;
import java.util.Map;

public class hr implements kp
{
    private static final String d;
    public im a;
    public je b;
    public io c;
    
    static {
        d = hr.class.getSimpleName();
    }
    
    public static FlurryEventRecordStatus a(final String s, final String s2, final Map<String, String> map) {
        final jh b = b();
        FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
        if (b != null) {
            flurryEventRecordStatus = b.a(s, jj.a(s2), map);
        }
        return flurryEventRecordStatus;
    }
    
    public static hr a() {
        synchronized (hr.class) {
            return (hr)jy.a().a(hr.class);
        }
    }
    
    public static void a(final String s, final String s2, final Throwable t) {
        final jh b = b();
        if (b != null) {
            b.a(s, s2, t.getClass().getName(), t);
        }
    }
    
    public static jh b() {
        final lk b = lm.a().b();
        jh jh;
        if (b == null) {
            jh = null;
        }
        else {
            jh = (jh)b.b(jh.class);
        }
        return jh;
    }
    
    @Override
    public final void a(final Context context) {
        lk.a(jh.class);
        this.b = new je();
        this.a = new im();
        this.c = new io();
        if (!ly.a(context, "android.permission.INTERNET")) {
            km.b(hr.d, "Application must declare permission: android.permission.INTERNET");
        }
        if (!ly.a(context, "android.permission.ACCESS_NETWORK_STATE")) {
            km.e(hr.d, "It is highly recommended that the application declare permission: android.permission.ACCESS_NETWORK_STATE");
        }
    }
}
