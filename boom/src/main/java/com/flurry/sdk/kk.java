// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.concurrent.Callable;
import java.lang.ref.WeakReference;
import java.util.concurrent.FutureTask;

public final class kk<V> extends FutureTask<V>
{
    private final WeakReference<Callable<V>> a;
    private final WeakReference<Runnable> b;
    
    public kk(final Runnable runnable, final V result) {
        super(runnable, result);
        this.a = new WeakReference<Callable<V>>(null);
        this.b = new WeakReference<Runnable>(runnable);
    }
    
    public final Runnable a() {
        return this.b.get();
    }
}
