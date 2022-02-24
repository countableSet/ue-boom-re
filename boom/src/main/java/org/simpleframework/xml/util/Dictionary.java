// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.AbstractSet;

public class Dictionary<T extends Entry> extends AbstractSet<T>
{
    protected final Table<T> map;
    
    public Dictionary() {
        this.map = new Table<T>();
    }
    
    @Override
    public boolean add(final T value) {
        return this.map.put(value.getName(), (T)value) != null;
    }
    
    public T get(final String key) {
        return (T)this.map.get(key);
    }
    
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)this.map.values().iterator();
    }
    
    public T remove(final String key) {
        return (T)this.map.remove(key);
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    private static class Table<T> extends HashMap<String, T>
    {
        public Table() {
        }
    }
}
