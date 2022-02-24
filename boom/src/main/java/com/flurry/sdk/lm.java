// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.lang.ref.WeakReference;
import android.app.Activity;
import java.util.WeakHashMap;
import android.content.Context;
import java.util.Map;

public class lm implements a
{
    private static final String b;
    private static lm f;
    public long a;
    private final Map<Context, lk> c;
    private final ln d;
    private final Object e;
    private long g;
    private lk h;
    private kh<lo> i;
    private kh<kb> j;
    
    static {
        b = lm.class.getSimpleName();
    }
    
    private lm() {
        this.c = new WeakHashMap<Context, lk>();
        this.d = new ln();
        this.e = new Object();
        this.i = new kh<lo>() {};
        this.j = new kh<kb>() {};
        final lp a = lp.a();
        this.a = 0L;
        this.g = (long)a.a("ContinueSessionMillis");
        a.a("ContinueSessionMillis", (lq.a)this);
        km.a(4, lm.b, "initSettings, ContinueSessionMillis = " + this.g);
        ki.a().a("com.flurry.android.sdk.ActivityLifecycleEvent", this.j);
        ki.a().a("com.flurry.android.sdk.FlurrySessionTimerEvent", this.i);
    }
    
    public static lm a() {
        synchronized (lm.class) {
            if (lm.f == null) {
                lm.f = new lm();
            }
            return lm.f;
        }
    }
    
    static /* synthetic */ void a(final lm lm, final lk lk) {
        synchronized (lm.e) {
            if (lm.h == lk) {
                lm.h = null;
            }
        }
    }
    
    private void e(final Context context) {
        while (true) {
            synchronized (this) {
                if (this.c.get(context) != null) {
                    if (kc.a().b()) {
                        km.a(3, lm.b, "Session already started with context:" + context);
                    }
                    else {
                        km.e(lm.b, "Session already started with context:" + context);
                    }
                    return;
                }
            }
            this.d.a();
            lk b;
            final Context context2;
            if ((b = this.b()) == null) {
                b = new lk();
                km.e(lm.b, "Flurry session started for context:" + context2);
                final ll ll = new ll();
                ll.a = new WeakReference<Context>(context2);
                ll.b = b;
                ll.c = com.flurry.sdk.ll.a.a;
                ll.b();
            }
            this.c.put(context2, b);
            Object e = this.e;
            synchronized (e) {
                this.h = b;
                // monitorexit(e)
                final String b2 = lm.b;
                e = new StringBuilder("Flurry session resumed for context:");
                km.e(b2, ((StringBuilder)e).append(context2).toString());
                final ll ll2 = new ll();
                e = new WeakReference<Context>(context2);
                ll2.a = (WeakReference<Context>)e;
                ll2.b = b;
                ll2.c = ll.a.c;
                ll2.b();
                this.a = 0L;
            }
        }
    }
    
    private int f() {
        synchronized (this) {
            return this.c.size();
        }
    }
    
    private void g() {
        while (true) {
            Label_0067: {
                synchronized (this) {
                    final int f = this.f();
                    if (f > 0) {
                        km.a(5, lm.b, "Session cannot be finalized, sessionContextCount:" + f);
                    }
                    else {
                        if (this.b() != null) {
                            break Label_0067;
                        }
                        km.a(5, lm.b, "Session cannot be finalized, current session not found");
                    }
                    return;
                }
            }
            km.e(lm.b, "Flurry session ended");
            final ll ll = new ll();
            final lk b;
            ll.b = b;
            ll.c = com.flurry.sdk.ll.a.e;
            jk.a();
            ll.d = jk.c();
            ll.b();
            jy.a().b(new ma() {
                @Override
                public final void a() {
                    lm.a(lm.this, b);
                }
            });
        }
    }
    
    public final void a(final Context obj) {
        synchronized (this) {
            if (obj instanceof Activity && kc.a().b()) {
                km.a(3, lm.b, "bootstrap for context:" + obj);
                this.e(obj);
            }
        }
    }
    
    @Override
    public final void a(final String s, final Object o) {
        if (s.equals("ContinueSessionMillis")) {
            this.g = (long)o;
            km.a(4, lm.b, "onSettingUpdate, ContinueSessionMillis = " + this.g);
        }
        else {
            km.a(6, lm.b, "onSettingUpdate internal error!");
        }
    }
    
    public final lk b() {
        synchronized (this.e) {
            return this.h;
        }
    }
    
    public final void b(final Context obj) {
        synchronized (this) {
            if (!kc.a().b() || !(obj instanceof Activity)) {
                km.a(3, lm.b, "Manual onStartSession for context:" + obj);
                this.e(obj);
            }
        }
    }
    
    public final void c(final Context obj) {
        synchronized (this) {
            if (!kc.a().b() || !(obj instanceof Activity)) {
                km.a(3, lm.b, "Manual onEndSession for context:" + obj);
                this.d(obj);
            }
        }
    }
    
    public final boolean c() {
        synchronized (this) {
            boolean b;
            if (this.b() == null) {
                km.a(2, lm.b, "Session not found. No active session");
                b = false;
            }
            else {
                b = true;
            }
            return b;
        }
    }
    
    public final void d() {
        synchronized (this) {
            for (final Map.Entry<Context, lk> entry : this.c.entrySet()) {
                final ll ll = new ll();
                ll.a = new WeakReference<Context>(entry.getKey());
                ll.b = entry.getValue();
                ll.c = com.flurry.sdk.ll.a.d;
                jk.a();
                ll.d = jk.c();
                ll.b();
            }
        }
        this.c.clear();
        jy.a().b(new ma() {
            @Override
            public final void a() {
                lm.this.g();
            }
        });
    }
    // monitorexit(this)
    
    final void d(final Context context) {
        while (true) {
            Object b;
            synchronized (this) {
                b = this.c.remove(context);
                if (b == null) {
                    if (kc.a().b()) {
                        final String b2 = lm.b;
                        b = new StringBuilder("Session cannot be ended, session not found for context:");
                        km.a(3, b2, ((StringBuilder)b).append(context).toString());
                    }
                    else {
                        final String b3 = lm.b;
                        b = new StringBuilder("Session cannot be ended, session not found for context:");
                        km.e(b3, ((StringBuilder)b).append(context).toString());
                    }
                    return;
                }
            }
            final Context context2;
            km.e(lm.b, "Flurry session paused for context:" + context2);
            final ll ll = new ll();
            ll.a = new WeakReference<Context>(context2);
            ll.b = (lk)b;
            jk.a();
            ll.d = jk.c();
            ll.c = com.flurry.sdk.ll.a.d;
            ll.b();
            if (this.f() == 0) {
                this.d.a(this.g);
                this.a = System.currentTimeMillis();
                return;
            }
            this.a = 0L;
        }
    }
}
