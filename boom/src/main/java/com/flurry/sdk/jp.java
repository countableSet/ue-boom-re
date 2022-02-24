// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.os.Bundle;
import android.os.Looper;
import android.location.LocationListener;
import android.content.Context;
import android.text.TextUtils;
import android.location.LocationManager;
import android.location.Location;
import android.annotation.SuppressLint;

@SuppressLint({ "MissingPermission" })
public class jp implements lq.a
{
    private static int a;
    private static int b;
    private static int c;
    private static jp d;
    private static final String e;
    private boolean f;
    private Location g;
    private long h;
    private LocationManager i;
    private a j;
    private Location k;
    private boolean l;
    private int m;
    private kh<ls> n;
    
    static {
        jp.a = -1;
        jp.b = -1;
        jp.c = -1;
        e = jp.class.getSimpleName();
    }
    
    private jp() {
        this.h = 0L;
        this.l = false;
        this.m = 0;
        this.n = new kh<ls>() {};
        this.i = (LocationManager)jy.a().a.getSystemService("location");
        this.j = new a();
        final lp a = lp.a();
        this.f = (boolean)a.a("ReportLocation");
        a.a("ReportLocation", (lq.a)this);
        km.a(4, jp.e, "initSettings, ReportLocation = " + this.f);
        this.g = (Location)a.a("ExplicitLocation");
        a.a("ExplicitLocation", (lq.a)this);
        km.a(4, jp.e, "initSettings, ExplicitLocation = " + this.g);
    }
    
    private Location a(final String s) {
        Location lastKnownLocation = null;
        if (!TextUtils.isEmpty((CharSequence)s)) {
            lastKnownLocation = this.i.getLastKnownLocation(s);
        }
        return lastKnownLocation;
    }
    
    public static jp a() {
        synchronized (jp.class) {
            if (jp.d == null) {
                jp.d = new jp();
            }
            return jp.d;
        }
    }
    
    private static boolean a(final Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
    }
    
    public static int b() {
        return jp.a;
    }
    
    private static boolean b(final Context context) {
        return context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0;
    }
    
    public static int c() {
        return jp.b;
    }
    
    public static int d() {
        return jp.c;
    }
    
    private void i() {
        if (this.l) {
            this.i.removeUpdates((LocationListener)this.j);
            this.m = 0;
            this.h = 0L;
            km.a(4, jp.e, "Unregister location timer");
            lt.a().b(this.n);
            this.l = false;
            km.a(4, jp.e, "LocationProvider stopped");
        }
    }
    
    @Override
    public final void a(final String s, final Object o) {
        switch (s) {
            default: {
                km.a(6, jp.e, "LocationProvider internal error! Had to be LocationCriteria, ReportLocation or ExplicitLocation key.");
                break;
            }
            case "ReportLocation": {
                this.f = (boolean)o;
                km.a(4, jp.e, "onSettingUpdate, ReportLocation = " + this.f);
                break;
            }
            case "ExplicitLocation": {
                this.g = (Location)o;
                km.a(4, jp.e, "onSettingUpdate, ExplicitLocation = " + this.g);
                break;
            }
        }
    }
    
    public final void e() {
        synchronized (this) {
            km.a(4, jp.e, "Location update requested");
            if (this.m < 3 && !this.l && this.f && this.g == null) {
                final Context a = jy.a().a;
                if (a.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0 || a.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
                    this.m = 0;
                    String s = null;
                    if (a(a)) {
                        s = "passive";
                    }
                    else if (b(a)) {
                        s = "network";
                    }
                    if (!TextUtils.isEmpty((CharSequence)s)) {
                        this.i.requestLocationUpdates(s, 10000L, 0.0f, (LocationListener)this.j, Looper.getMainLooper());
                    }
                    this.k = this.a(s);
                    this.h = System.currentTimeMillis() + 90000L;
                    km.a(4, jp.e, "Register location timer");
                    lt.a().a(this.n);
                    this.l = true;
                    km.a(4, jp.e, "LocationProvider started");
                }
            }
        }
    }
    
    public final void f() {
        synchronized (this) {
            km.a(4, jp.e, "Stop update location requested");
            this.i();
        }
    }
    
    public final Location g() {
        final Location location = null;
        final Location location2 = null;
        Location obj;
        if (this.g != null) {
            obj = this.g;
        }
        else {
            obj = location;
            if (this.f) {
                final Context a = jy.a().a;
                if (!a(a)) {
                    obj = location2;
                    if (!b(a)) {
                        return obj;
                    }
                }
                String s;
                if (a(a)) {
                    s = "passive";
                }
                else if (b(a)) {
                    s = "network";
                }
                else {
                    s = null;
                }
                obj = location;
                if (s != null) {
                    final Location a2 = this.a(s);
                    if (a2 != null) {
                        this.k = a2;
                    }
                    obj = this.k;
                }
            }
            km.a(4, jp.e, "getLocation() = " + obj);
        }
        return obj;
    }
    
    final class a implements LocationListener
    {
        public a() {
        }
        
        public final void onLocationChanged(final Location location) {
            if (location != null) {
                jp.this.k = location;
            }
            if (++jp.this.m >= 3) {
                km.a(4, jp.e, "Max location reports reached, stopping");
                jp.this.i();
            }
        }
        
        public final void onProviderDisabled(final String s) {
        }
        
        public final void onProviderEnabled(final String s) {
        }
        
        public final void onStatusChanged(final String s, final int n, final Bundle bundle) {
        }
    }
}
