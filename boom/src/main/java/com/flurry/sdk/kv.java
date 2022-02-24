// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public final class kv<T> extends WeakReference<T>
{
    public kv(final T referent) {
        super(referent);
    }
    
    @Override
    public final boolean equals(final Object obj) {
        final Object value = this.get();
        boolean b;
        if (obj instanceof Reference) {
            b = value.equals(((Reference)obj).get());
        }
        else {
            b = value.equals(obj);
        }
        return b;
    }
    
    @Override
    public final int hashCode() {
        final Object value = this.get();
        int n;
        if (value == null) {
            n = super.hashCode();
        }
        else {
            n = value.hashCode();
        }
        return n;
    }
}
