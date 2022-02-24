// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map;

public final class me
{
    private static me c;
    final Thread.UncaughtExceptionHandler a;
    final Map<Thread.UncaughtExceptionHandler, Void> b;
    
    private me() {
        this.b = new WeakHashMap<Thread.UncaughtExceptionHandler, Void>();
        this.a = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new a((byte)0));
    }
    
    public static me a() {
        synchronized (me.class) {
            if (me.c == null) {
                me.c = new me();
            }
            return me.c;
        }
    }
    
    final Set<Thread.UncaughtExceptionHandler> b() {
        synchronized (this.b) {
            return this.b.keySet();
        }
    }
    
    final class a implements UncaughtExceptionHandler
    {
        private a() {
        }
        
        @Override
        public final void uncaughtException(final Thread thread, final Throwable t) {
            for (final UncaughtExceptionHandler uncaughtExceptionHandler : me.this.b()) {
                try {
                    uncaughtExceptionHandler.uncaughtException(thread, t);
                }
                catch (Throwable t2) {}
            }
            final me a = me.this;
            if (a.a == null) {
                return;
            }
            try {
                a.a.uncaughtException(thread, t);
            }
            catch (Throwable t3) {}
        }
    }
}
