// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

class RegistryBinder
{
    private final Cache<Class> cache;
    private final ConverterFactory factory;
    
    public RegistryBinder() {
        this.cache = new ConcurrentCache<Class>();
        this.factory = new ConverterFactory();
    }
    
    private Converter create(final Class clazz) throws Exception {
        return this.factory.getInstance(clazz);
    }
    
    public void bind(final Class clazz, final Class clazz2) throws Exception {
        this.cache.cache(clazz, clazz2);
    }
    
    public Converter lookup(Class clazz) throws Exception {
        clazz = this.cache.fetch(clazz);
        Converter create;
        if (clazz != null) {
            create = this.create(clazz);
        }
        else {
            create = null;
        }
        return create;
    }
}
