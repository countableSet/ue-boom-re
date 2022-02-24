// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import android.text.TextUtils;
import java.util.List;

public final class ki
{
    private static ki a;
    private final kd<String, kv<kh<?>>> b;
    private final kd<kv<kh<?>>, String> c;
    
    static {
        ki.a = null;
    }
    
    private ki() {
        this.b = new kd<String, kv<kh<?>>>();
        this.c = new kd<kv<kh<?>>, String>();
    }
    
    public static ki a() {
        synchronized (ki.class) {
            if (ki.a == null) {
                ki.a = new ki();
            }
            return ki.a;
        }
    }
    
    private List<kh<?>> b(final String s) {
        while (true) {
            ArrayList<kh<?>> list = null;
            while (true) {
                kh<?> kh = null;
                Label_0082: {
                    synchronized (this) {
                        if (TextUtils.isEmpty((CharSequence)s)) {
                            return Collections.emptyList();
                        }
                        list = new ArrayList<kh<?>>();
                        final Iterator<kv<kh<?>>> iterator = this.b.a(s).iterator();
                        while (iterator.hasNext()) {
                            kh = iterator.next().get();
                            if (kh != null) {
                                break Label_0082;
                            }
                            iterator.remove();
                        }
                        return list;
                    }
                }
                list.add(kh);
                continue;
            }
            return list;
        }
    }
    
    public final int a(final String s) {
        synchronized (this) {
            int size;
            if (TextUtils.isEmpty((CharSequence)s)) {
                size = 0;
            }
            else {
                size = this.b.a(s).size();
            }
            return size;
        }
    }
    
    public final void a(final kg kg) {
        if (kg != null) {
            final Iterator<kh<?>> iterator = this.b(kg.a()).iterator();
            while (iterator.hasNext()) {
                jy.a().b(new ma() {
                    final /* synthetic */ kh a = iterator.next();
                    
                    @Override
                    public final void a() {
                        this.a.a(kg);
                    }
                });
            }
        }
    }
    
    public final void a(final kh<?> kh) {
        // monitorenter(this)
        if (kh != null) {
            kv<kh<?>> kv;
            try {
                kv = new kv<kh<?>>(kh);
                final Iterator<String> iterator = this.c.a(kv).iterator();
                while (iterator.hasNext()) {
                    this.b.b(iterator.next(), kv);
                }
            }
            finally {
            }
            // monitorexit(this)
            this.c.b(kv);
        }
    }
    // monitorexit(this)
    
    public final void a(final String s, final kh<?> kh) {
        boolean contains = false;
        synchronized (this) {
            if (!TextUtils.isEmpty((CharSequence)s) && kh != null) {
                final kv<kh<?>> kv = new kv<kh<?>>(kh);
                final List<kv<kh<?>>> a = this.b.a(s, false);
                if (a != null) {
                    contains = a.contains(kv);
                }
                if (!contains) {
                    this.b.a(s, kv);
                    this.c.a(kv, s);
                }
            }
        }
    }
    
    public final void b(final String s, final kh<?> kh) {
        synchronized (this) {
            if (!TextUtils.isEmpty((CharSequence)s)) {
                final kv<kh<?>> kv = new kv<kh<?>>(kh);
                this.b.b(s, kv);
                this.c.b(kv, s);
            }
        }
    }
}
