// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.util;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCache<T> extends ConcurrentHashMap<Object, T> implements Cache<T>
{
    @Override
    public void cache(final Object key, final T value) {
        this.put(key, value);
    }
    
    @Override
    public boolean contains(final Object key) {
        return this.containsKey(key);
    }
    
    @Override
    public T fetch(final Object key) {
        return this.get(key);
    }
    
    @Override
    public T take(final Object key) {
        return this.remove(key);
    }
}
