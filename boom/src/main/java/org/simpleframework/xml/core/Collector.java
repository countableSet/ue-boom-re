// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;

class Collector implements Criteria
{
    private final Registry alias;
    private final Registry registry;
    
    public Collector() {
        this.registry = new Registry();
        this.alias = new Registry();
    }
    
    @Override
    public void commit(final Object o) throws Exception {
        for (final Variable variable : ((LinkedHashMap<K, Variable>)this.registry).values()) {
            variable.getContact().set(o, variable.getValue());
        }
    }
    
    @Override
    public Variable get(final Object key) {
        return ((LinkedHashMap<K, Variable>)this.registry).get(key);
    }
    
    @Override
    public Variable get(final Label label) throws Exception {
        Variable variable;
        if (label != null) {
            variable = ((LinkedHashMap<K, Variable>)this.registry).get(label.getKey());
        }
        else {
            variable = null;
        }
        return variable;
    }
    
    @Override
    public Iterator<Object> iterator() {
        return this.registry.iterator();
    }
    
    @Override
    public Variable remove(final Object key) throws Exception {
        return ((HashMap<K, Variable>)this.registry).remove(key);
    }
    
    @Override
    public Variable resolve(final String key) {
        return ((LinkedHashMap<K, Variable>)this.alias).get(key);
    }
    
    @Override
    public void set(final Label label, final Object o) throws Exception {
        final Variable variable = new Variable(label, o);
        if (label != null) {
            final String[] paths = label.getPaths();
            final Object key = label.getKey();
            for (int length = paths.length, i = 0; i < length; ++i) {
                ((HashMap<String, Variable>)this.alias).put(paths[i], variable);
            }
            this.registry.put(key, variable);
        }
    }
    
    private static class Registry extends LinkedHashMap<Object, Variable>
    {
        public Iterator<Object> iterator() {
            return ((LinkedHashMap<Object, V>)this).keySet().iterator();
        }
    }
}
