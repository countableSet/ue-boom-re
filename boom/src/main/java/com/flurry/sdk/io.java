// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.Map;
import java.net.SocketTimeoutException;
import java.util.List;

public class io extends kx<ip>
{
    public static long a;
    private static final String e;
    
    static {
        e = io.class.getSimpleName();
    }
    
    public io() {
        kx.b = 30000L;
        super.d = kx.b;
    }
    
    protected final kf<List<ip>> a() {
        return new kf<List<ip>>(jy.a().a.getFileStreamPath(".yflurryanpulsecallbackreporter"), ".yflurryanpulsecallbackreporter", 2, new lj<List<ip>>() {
            @Override
            public final lg<List<ip>> a(final int n) {
                return new lf<ip>(new ip.a());
            }
        });
    }
    
    protected final void a(final List<ip> list) {
        synchronized (this) {
            is.a().c();
        }
    }
    
    protected final void b(final List<ip> list) {
        while (true) {
            synchronized (this) {
                is.a();
                final List<it> d = is.d();
                if (d == null || d.size() == 0) {
                    return;
                }
                km.a(3, io.e, "Restoring " + d.size() + " from report queue.");
                final Iterator<it> iterator = d.iterator();
                while (iterator.hasNext()) {
                    is.a().b(iterator.next());
                }
            }
            is.a();
            final Iterator<it> iterator2 = is.b().iterator();
            while (iterator2.hasNext()) {
                for (final ip ip : iterator2.next().a()) {
                    if (!ip.l) {
                        km.a(3, io.e, "Callback for " + ip.m.c + " to " + ip.k + " not completed.  Adding to reporter queue.");
                        final List<ip> list2;
                        list2.add(ip);
                    }
                }
            }
        }
    }
}
