// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import java.lang.reflect.Constructor;
import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

class ConverterFactory
{
    private final Cache<Converter> cache;
    
    public ConverterFactory() {
        this.cache = new ConcurrentCache<Converter>();
    }
    
    private Constructor getConstructor(final Class clazz) throws Exception {
        final Constructor declaredConstructor = clazz.getDeclaredConstructor((Class[])new Class[0]);
        if (!declaredConstructor.isAccessible()) {
            declaredConstructor.setAccessible(true);
        }
        return declaredConstructor;
    }
    
    private Converter getConverter(final Class clazz) throws Exception {
        final Constructor constructor = this.getConstructor(clazz);
        if (constructor == null) {
            throw new ConvertException("No default constructor for %s", new Object[] { clazz });
        }
        return this.getConverter(clazz, constructor);
    }
    
    private Converter getConverter(final Class clazz, final Constructor constructor) throws Exception {
        final Converter converter = constructor.newInstance(new Object[0]);
        if (converter != null) {
            this.cache.cache(clazz, converter);
        }
        return converter;
    }
    
    public Converter getInstance(final Class clazz) throws Exception {
        Converter converter;
        if ((converter = this.cache.fetch(clazz)) == null) {
            converter = this.getConverter(clazz);
        }
        return converter;
    }
    
    public Converter getInstance(final Convert convert) throws Exception {
        final Class<? extends Converter> value = convert.value();
        if (value.isInterface()) {
            throw new ConvertException("Can not instantiate %s", new Object[] { value });
        }
        return this.getInstance(value);
    }
}
