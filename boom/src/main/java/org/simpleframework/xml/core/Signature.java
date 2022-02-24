// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.lang.reflect.Constructor;

class Signature implements Iterable<Parameter>
{
    private final Constructor factory;
    private final ParameterMap parameters;
    private final Class type;
    
    public Signature(final Constructor constructor) {
        this(constructor, constructor.getDeclaringClass());
    }
    
    public Signature(final Constructor factory, final Class type) {
        this.parameters = new ParameterMap();
        this.factory = factory;
        this.type = type;
    }
    
    public Signature(final Signature signature) {
        this(signature.factory, signature.type);
    }
    
    public void add(final Parameter value) {
        final Object key = value.getKey();
        if (key != null) {
            this.parameters.put(key, value);
        }
    }
    
    public boolean contains(final Object key) {
        return this.parameters.containsKey(key);
    }
    
    public Signature copy() throws Exception {
        final Signature signature = new Signature(this);
        final Iterator<Parameter> iterator = this.iterator();
        while (iterator.hasNext()) {
            signature.add(iterator.next());
        }
        return signature;
    }
    
    public Object create() throws Exception {
        if (!this.factory.isAccessible()) {
            this.factory.setAccessible(true);
        }
        return this.factory.newInstance(new Object[0]);
    }
    
    public Object create(final Object[] initargs) throws Exception {
        if (!this.factory.isAccessible()) {
            this.factory.setAccessible(true);
        }
        return this.factory.newInstance(initargs);
    }
    
    public Parameter get(final int n) {
        return this.parameters.get(n);
    }
    
    public Parameter get(final Object key) {
        return ((LinkedHashMap<K, Parameter>)this.parameters).get(key);
    }
    
    public List<Parameter> getAll() {
        return this.parameters.getAll();
    }
    
    public Class getType() {
        return this.type;
    }
    
    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }
    
    @Override
    public Iterator<Parameter> iterator() {
        return this.parameters.iterator();
    }
    
    public Parameter remove(final Object key) {
        return ((HashMap<K, Parameter>)this.parameters).remove(key);
    }
    
    public void set(final Object key, final Parameter value) {
        this.parameters.put(key, value);
    }
    
    public int size() {
        return this.parameters.size();
    }
    
    @Override
    public String toString() {
        return this.factory.toString();
    }
}
