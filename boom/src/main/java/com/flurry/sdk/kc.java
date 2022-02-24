// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;
import android.os.Bundle;
import java.lang.ref.WeakReference;
import android.app.Activity;
import android.app.Application$ActivityLifecycleCallbacks;
import android.app.Application;
import android.os.Build$VERSION;

public class kc
{
    private static kc a;
    private static final String b;
    private Object c;
    
    static {
        b = kc.class.getSimpleName();
    }
    
    private kc() {
        if (Build$VERSION.SDK_INT >= 14 && this.c == null) {
            final Context a = jy.a().a;
            if (a instanceof Application) {
                this.c = new Application$ActivityLifecycleCallbacks() {
                    private static void a(final Activity referent, final kb.a b) {
                        final kb kb = new kb();
                        kb.a = new WeakReference<Activity>(referent);
                        kb.b = b;
                        kb.b();
                    }
                    
                    public final void onActivityCreated(final Activity obj, final Bundle bundle) {
                        km.a(3, kc.b, "onActivityCreated for activity:" + obj);
                        a(obj, kb.a.a);
                    }
                    
                    public final void onActivityDestroyed(final Activity obj) {
                        km.a(3, kc.b, "onActivityDestroyed for activity:" + obj);
                        a(obj, kb.a.b);
                    }
                    
                    public final void onActivityPaused(final Activity obj) {
                        km.a(3, kc.b, "onActivityPaused for activity:" + obj);
                        a(obj, kb.a.c);
                    }
                    
                    public final void onActivityResumed(final Activity obj) {
                        km.a(3, kc.b, "onActivityResumed for activity:" + obj);
                        a(obj, kb.a.d);
                    }
                    
                    public final void onActivitySaveInstanceState(final Activity obj, final Bundle bundle) {
                        km.a(3, kc.b, "onActivitySaveInstanceState for activity:" + obj);
                        a(obj, kb.a.g);
                    }
                    
                    public final void onActivityStarted(final Activity obj) {
                        km.a(3, kc.b, "onActivityStarted for activity:" + obj);
                        a(obj, kb.a.e);
                    }
                    
                    public final void onActivityStopped(final Activity obj) {
                        km.a(3, kc.b, "onActivityStopped for activity:" + obj);
                        a(obj, kb.a.f);
                    }
                };
                ((Application)a).registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)this.c);
            }
        }
    }
    
    public static kc a() {
        synchronized (kc.class) {
            if (kc.a == null) {
                kc.a = new kc();
            }
            return kc.a;
        }
    }
    
    public final boolean b() {
        return this.c != null;
    }
}
