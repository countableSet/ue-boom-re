// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class lk
{
    private static final List<Class<?>> b;
    private final String a;
    private final Map<Class<?>, Object> c;
    
    static {
        b = new ArrayList<Class<?>>();
    }
    
    public lk() {
        this.a = lk.class.getSimpleName();
        this.c = new LinkedHashMap<Class<?>, Object>();
        Object o = lk.b;
        synchronized (o) {
            final ArrayList<Object> list = new ArrayList<Object>(lk.b);
            // monitorexit(o)
            o = list.iterator();
            while (((Iterator)o).hasNext()) {
                final Class<Object> obj = ((Iterator<Class<Object>>)o).next();
                try {
                    final Object instance = obj.newInstance();
                    synchronized (this.c) {
                        this.c.put(obj, instance);
                    }
                }
                catch (Exception ex) {
                    km.a(5, this.a, "Module data " + obj + " is not available:", ex);
                }
            }
        }
    }
    
    public static void a(final Class<?> clazz) {
        synchronized (lk.b) {
            lk.b.add(clazz);
        }
    }
    
    public final Object b(final Class<?> clazz) {
        synchronized (this.c) {
            return this.c.get(clazz);
        }
    }
}
