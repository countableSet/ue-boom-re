// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.lang.reflect.Constructor;
import java.util.Date;

class DateFactory<T extends Date>
{
    private final Constructor<T> factory;
    
    public DateFactory(final Class<T> clazz) throws Exception {
        this(clazz, (Class[])new Class[] { Long.TYPE });
    }
    
    public DateFactory(final Class<T> clazz, final Class... parameterTypes) throws Exception {
        this.factory = clazz.getDeclaredConstructor((Class<?>[])parameterTypes);
    }
    
    public T getInstance(final Object... initargs) throws Exception {
        return this.factory.newInstance(initargs);
    }
}
