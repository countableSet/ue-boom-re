// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.util;

import java.util.Map;
import java.util.LinkedHashMap;

public class LimitedCache<T> extends LinkedHashMap<Object, T> implements Cache<T>
{
    private final int capacity;
    
    public LimitedCache() {
        this(50000);
    }
    
    public LimitedCache(final int capacity) {
        this.capacity = capacity;
    }
    
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
    protected boolean removeEldestEntry(final Map.Entry<Object, T> entry) {
        return this.size() > this.capacity;
    }
    
    @Override
    public T take(final Object key) {
        return this.remove(key);
    }
}
