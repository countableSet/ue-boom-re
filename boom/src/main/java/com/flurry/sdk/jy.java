// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.text.TextUtils;
import android.os.Looper;
import android.os.HandlerThread;
import android.os.Handler;
import android.content.Context;

public class jy
{
    private static final String e;
    private static jy f;
    public final Context a;
    public final Handler b;
    public final Handler c;
    public final String d;
    private final HandlerThread g;
    private final ko h;
    
    static {
        e = jy.class.getSimpleName();
    }
    
    private jy(final Context context, final String d) {
        this.a = context.getApplicationContext();
        this.b = new Handler(Looper.getMainLooper());
        (this.g = new HandlerThread("FlurryAgent")).start();
        this.c = new Handler(this.g.getLooper());
        this.d = d;
        this.h = new ko();
    }
    
    public static jy a() {
        return jy.f;
    }
    
    public static void a(final Context context, final String anObject) {
        // monitorexit(jy.class)
        while (true) {
            Label_0052: {
                synchronized (jy.class) {
                    if (jy.f == null) {
                        break Label_0052;
                    }
                    if (!jy.f.d.equals(anObject)) {
                        throw new IllegalStateException("Only one API key per application is supported!");
                    }
                }
                km.e(jy.e, "Flurry is already initialized");
                return;
            }
            final Context context2;
            if (context2 == null) {
                throw new IllegalArgumentException("Context cannot be null");
            }
            if (TextUtils.isEmpty((CharSequence)anObject)) {
                throw new IllegalArgumentException("API key must be specified");
            }
            (jy.f = new jy(context2, anObject)).h.a(context2);
            continue;
        }
    }
    
    public final kp a(final Class<? extends kp> clazz) {
        return this.h.b(clazz);
    }
    
    public final void a(final Runnable runnable) {
        this.b.post(runnable);
    }
    
    public final void a(final Runnable runnable, final long n) {
        if (runnable != null) {
            this.c.postDelayed(runnable, n);
        }
    }
    
    public final void b(final Runnable runnable) {
        this.c.post(runnable);
    }
}
