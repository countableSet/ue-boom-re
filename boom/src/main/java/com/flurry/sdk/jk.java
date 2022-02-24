// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;

public class jk implements kp
{
    public static jk a() {
        synchronized (jk.class) {
            return (jk)jy.a().a(jk.class);
        }
    }
    
    public static String b() {
        String string = null;
        final jx h = h();
        if (h != null) {
            string = Long.toString(h.c);
        }
        return string;
    }
    
    public static long c() {
        long c = 0L;
        final jx h = h();
        if (h != null) {
            c = h.c;
        }
        return c;
    }
    
    public static long d() {
        long d = 0L;
        final jx h = h();
        if (h != null) {
            d = h.d;
        }
        return d;
    }
    
    public static long e() {
        long e = -1L;
        final jx h = h();
        if (h != null) {
            e = h.e;
        }
        return e;
    }
    
    public static long f() {
        long c = 0L;
        final jx h = h();
        if (h != null) {
            c = h.c();
        }
        return c;
    }
    
    public static jr.a g() {
        return jr.a().b();
    }
    
    public static jx h() {
        final lk b = lm.a().b();
        jx jx;
        if (b == null) {
            jx = null;
        }
        else {
            jx = (jx)b.b(jx.class);
        }
        return jx;
    }
    
    @Override
    public final void a(final Context context) {
        lk.a(jx.class);
        ki.a();
        lt.a();
        lp.a();
        ka.a();
        jr.a();
        jl.a();
        js.a();
        jp.a();
        jl.a();
        ju.a();
        jo.a();
        jw.a();
    }
}
