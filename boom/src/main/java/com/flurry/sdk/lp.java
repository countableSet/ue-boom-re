// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.location.Location;
import android.location.Criteria;

public final class lp extends lq
{
    public static final Integer a;
    public static final Integer b;
    public static final Integer c;
    public static final Integer d;
    public static final String e;
    public static final Boolean f;
    public static final Boolean g;
    public static final String h;
    public static final Boolean i;
    public static final Criteria j;
    public static final Location k;
    public static final Long l;
    public static final Boolean m;
    public static final Long n;
    public static final Byte o;
    public static final Boolean p;
    public static final String q;
    public static final Boolean r;
    private static lp s;
    
    static {
        a = 228;
        b = 6;
        c = 4;
        d = 0;
        e = null;
        f = true;
        g = true;
        h = null;
        i = true;
        j = null;
        k = null;
        l = 10000L;
        m = true;
        n = null;
        o = -1;
        p = false;
        q = null;
        r = true;
    }
    
    private lp() {
        this.a("AgentVersion", lp.a);
        this.a("ReleaseMajorVersion", lp.b);
        this.a("ReleaseMinorVersion", lp.c);
        this.a("ReleasePatchVersion", lp.d);
        this.a("ReleaseBetaVersion", "");
        this.a("VersionName", lp.e);
        this.a("CaptureUncaughtExceptions", lp.f);
        this.a("UseHttps", lp.g);
        this.a("ReportUrl", lp.h);
        this.a("ReportLocation", lp.i);
        this.a("ExplicitLocation", lp.k);
        this.a("ContinueSessionMillis", lp.l);
        this.a("LogEvents", lp.m);
        this.a("Age", lp.n);
        this.a("Gender", lp.o);
        this.a("UserId", "");
        this.a("ProtonEnabled", lp.p);
        this.a("ProtonConfigUrl", lp.q);
        this.a("analyticsEnabled", lp.r);
    }
    
    public static lp a() {
        synchronized (lp.class) {
            if (lp.s == null) {
                lp.s = new lp();
            }
            return lp.s;
        }
    }
}
