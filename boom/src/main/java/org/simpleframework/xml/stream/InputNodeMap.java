// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

class InputNodeMap extends LinkedHashMap<String, InputNode> implements NodeMap<InputNode>
{
    private final InputNode source;
    
    protected InputNodeMap(final InputNode source) {
        this.source = source;
    }
    
    public InputNodeMap(final InputNode source, final EventNode eventNode) {
        this.source = source;
        this.build(eventNode);
    }
    
    private void build(final EventNode eventNode) {
        for (final Attribute attribute : eventNode) {
            final InputAttribute value = new InputAttribute(this.source, attribute);
            if (!attribute.isReserved()) {
                ((HashMap<String, InputAttribute>)this).put(value.getName(), value);
            }
        }
    }
    
    @Override
    public InputNode get(final String key) {
        return super.get(key);
    }
    
    @Override
    public String getName() {
        return this.source.getName();
    }
    
    @Override
    public InputNode getNode() {
        return this.source;
    }
    
    @Override
    public Iterator<String> iterator() {
        return ((LinkedHashMap<String, V>)this).keySet().iterator();
    }
    
    @Override
    public InputNode put(final String key, final String s) {
        final InputAttribute value = new InputAttribute(this.source, key, s);
        if (key != null) {
            ((HashMap<String, InputAttribute>)this).put(key, value);
        }
        return value;
    }
    
    @Override
    public InputNode remove(final String key) {
        return super.remove(key);
    }
}
