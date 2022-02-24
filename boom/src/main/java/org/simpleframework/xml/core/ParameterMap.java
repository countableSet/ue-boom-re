// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

class ParameterMap extends LinkedHashMap<Object, Parameter> implements Iterable<Parameter>
{
    public ParameterMap() {
    }
    
    public Parameter get(final int n) {
        return this.getAll().get(n);
    }
    
    public List<Parameter> getAll() {
        final Collection<Parameter> values = ((LinkedHashMap<K, Parameter>)this).values();
        List<Parameter> emptyList;
        if (!values.isEmpty()) {
            emptyList = new ArrayList<Parameter>(values);
        }
        else {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    @Override
    public Iterator<Parameter> iterator() {
        return ((LinkedHashMap<K, Parameter>)this).values().iterator();
    }
}
