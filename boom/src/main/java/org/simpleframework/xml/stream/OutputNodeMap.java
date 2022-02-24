// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

class OutputNodeMap extends LinkedHashMap<String, OutputNode> implements NodeMap<OutputNode>
{
    private final OutputNode source;
    
    public OutputNodeMap(final OutputNode source) {
        this.source = source;
    }
    
    @Override
    public OutputNode get(final String key) {
        return super.get(key);
    }
    
    @Override
    public String getName() {
        return this.source.getName();
    }
    
    @Override
    public OutputNode getNode() {
        return this.source;
    }
    
    @Override
    public Iterator<String> iterator() {
        return ((LinkedHashMap<String, V>)this).keySet().iterator();
    }
    
    @Override
    public OutputNode put(final String key, final String s) {
        final OutputAttribute value = new OutputAttribute(this.source, key, s);
        if (this.source != null) {
            ((HashMap<String, OutputAttribute>)this).put(key, value);
        }
        return value;
    }
    
    @Override
    public OutputNode remove(final String key) {
        return super.remove(key);
    }
}
