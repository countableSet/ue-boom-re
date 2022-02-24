// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.TimerTask;
import java.util.Timer;

public class kt
{
    private static final String a;
    private Timer b;
    private a c;
    private ku d;
    
    static {
        a = kt.class.getSimpleName();
    }
    
    public kt(final ku d) {
        this.d = d;
    }
    
    public final void a() {
        synchronized (this) {
            if (this.b != null) {
                this.b.cancel();
                this.b = null;
                km.a(3, kt.a, "HttpRequestTimeoutTimer stopped.");
            }
            this.c = null;
        }
    }
    
    public final void a(final long n) {
        boolean b = false;
        synchronized (this) {
            if (this.b != null) {
                b = true;
            }
            if (b) {
                this.a();
            }
            this.b = new Timer("HttpRequestTimeoutTimer");
            this.c = new a((byte)0);
            this.b.schedule(this.c, n);
            km.a(3, kt.a, "HttpRequestTimeoutTimer started: " + n + "MS");
        }
    }
    
    final class a extends TimerTask
    {
        private a() {
        }
        
        @Override
        public final void run() {
            km.a(3, kt.a, "HttpRequest timed out. Cancelling.");
            final ku a = kt.this.d;
            km.a(3, ku.e, "Timeout (" + (System.currentTimeMillis() - a.m) + "MS) for url: " + a.f);
            a.p = 629;
            a.t = true;
            a.h();
            a.f();
        }
    }
}
