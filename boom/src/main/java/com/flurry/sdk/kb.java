// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.app.Activity;
import java.lang.ref.WeakReference;

public final class kb extends kg
{
    public WeakReference<Activity> a;
    public a b;
    
    public kb() {
        super("com.flurry.android.sdk.ActivityLifecycleEvent");
        this.a = new WeakReference<Activity>(null);
    }
    
    public enum a
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g;
    }
}
