// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

public class Registry
{
    private final RegistryBinder binder;
    private final Cache<Converter> cache;
    
    public Registry() {
        this.cache = new ConcurrentCache<Converter>();
        this.binder = new RegistryBinder();
    }
    
    private Converter create(final Class clazz) throws Exception {
        final Converter lookup = this.binder.lookup(clazz);
        if (lookup != null) {
            this.cache.cache(clazz, lookup);
        }
        return lookup;
    }
    
    public Registry bind(final Class clazz, final Class clazz2) throws Exception {
        if (clazz != null) {
            this.binder.bind(clazz, clazz2);
        }
        return this;
    }
    
    public Registry bind(final Class clazz, final Converter converter) throws Exception {
        if (clazz != null) {
            this.cache.cache(clazz, converter);
        }
        return this;
    }
    
    public Converter lookup(final Class clazz) throws Exception {
        Converter create;
        if ((create = this.cache.fetch(clazz)) == null) {
            create = this.create(clazz);
        }
        return create;
    }
}
