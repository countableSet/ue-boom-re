// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

final class Session implements Map
{
    private final Map map;
    private final boolean strict;
    
    public Session() {
        this(true);
    }
    
    public Session(final boolean strict) {
        this.map = new HashMap();
        this.strict = strict;
    }
    
    @Override
    public void clear() {
        this.map.clear();
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.map.containsKey(o);
    }
    
    @Override
    public boolean containsValue(final Object o) {
        return this.map.containsValue(o);
    }
    
    @Override
    public Set entrySet() {
        return this.map.entrySet();
    }
    
    @Override
    public Object get(final Object o) {
        return this.map.get(o);
    }
    
    public Map getMap() {
        return this.map;
    }
    
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public boolean isStrict() {
        return this.strict;
    }
    
    @Override
    public Set keySet() {
        return this.map.keySet();
    }
    
    @Override
    public Object put(final Object o, final Object o2) {
        return this.map.put(o, o2);
    }
    
    @Override
    public void putAll(final Map map) {
        this.map.putAll(map);
    }
    
    @Override
    public Object remove(final Object o) {
        return this.map.remove(o);
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public Collection values() {
        return this.map.values();
    }
}
