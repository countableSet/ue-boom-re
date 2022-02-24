// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.LinkedHashMap;
import android.os.SystemClock;
import android.content.Context;
import java.util.Map;
import java.lang.ref.WeakReference;

public class jx
{
    static final String a;
    WeakReference<lk> b;
    public volatile long c;
    public volatile long d;
    public volatile long e;
    public volatile long f;
    private final kh<ll> g;
    private volatile long h;
    private String i;
    private String j;
    private Map<String, String> k;
    
    static {
        a = jx.class.getSimpleName();
    }
    
    public jx() {
        this.g = new kh<ll>() {};
        this.c = 0L;
        this.d = 0L;
        this.e = -1L;
        this.f = 0L;
        this.h = 0L;
        ki.a().a("com.flurry.android.sdk.FlurrySessionEvent", this.g);
        this.k = new LinkedHashMap<String, String>() {
            @Override
            protected final boolean removeEldestEntry(final Map.Entry<String, String> entry) {
                return this.size() > 10;
            }
        };
    }
    
    public static void b() {
    }
    
    public final void a() {
        synchronized (this) {
            final long a = lm.a().a;
            if (a > 0L) {
                this.f += System.currentTimeMillis() - a;
            }
        }
    }
    
    public final void a(final String i) {
        synchronized (this) {
            this.i = i;
        }
    }
    
    public final void a(final String s, final String s2) {
        synchronized (this) {
            this.k.put(s, s2);
        }
    }
    
    public final void b(final String j) {
        synchronized (this) {
            this.j = j;
        }
    }
    
    public final long c() {
        synchronized (this) {
            long n = SystemClock.elapsedRealtime() - this.d;
            if (n <= this.h) {
                n = this.h + 1L;
                this.h = n;
            }
            return this.h = n;
        }
    }
    
    public final String d() {
        synchronized (this) {
            return this.i;
        }
    }
    
    public final String e() {
        synchronized (this) {
            return this.j;
        }
    }
    
    public final Map<String, String> f() {
        synchronized (this) {
            return this.k;
        }
    }
}
