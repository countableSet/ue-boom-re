// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.Mode;
import org.simpleframework.xml.stream.OutputNode;
import java.util.Map;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class CompositeInlineMap implements Repeater
{
    private final Entry entry;
    private final MapFactory factory;
    private final Converter key;
    private final Style style;
    private final Converter value;
    
    public CompositeInlineMap(final Context context, final Entry entry, final Type type) throws Exception {
        this.factory = new MapFactory(context, type);
        this.value = entry.getValue(context);
        this.key = entry.getKey(context);
        this.style = context.getStyle();
        this.entry = entry;
    }
    
    private Object read(InputNode next, final Map map) throws Exception {
        final InputNode parent = next.getParent();
        for (String name = next.getName(); next != null; next = parent.getNext(name)) {
            final Object read = this.key.read(next);
            final Object read2 = this.value.read(next);
            if (map != null) {
                map.put(read, read2);
            }
        }
        return map;
    }
    
    private void write(final OutputNode outputNode, final Map map, final Mode mode) throws Exception {
        final String element = this.style.getElement(this.entry.getEntry());
        for (final Object next : map.keySet()) {
            final OutputNode child = outputNode.getChild(element);
            final V value = map.get(next);
            child.setMode(mode);
            this.key.write(child, next);
            this.value.write(child, value);
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Map map = (Map)this.factory.getInstance();
        Object read;
        if (map != null) {
            read = this.read(inputNode, map);
        }
        else {
            read = null;
        }
        return read;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Map map = (Map)o;
        Object o2;
        if (map != null) {
            o2 = this.read(inputNode, map);
        }
        else {
            o2 = this.read(inputNode);
        }
        return o2;
    }
    
    @Override
    public boolean validate(InputNode next) throws Exception {
        final boolean b = false;
        final InputNode parent = next.getParent();
        final String name = next.getName();
        while (next != null) {
            boolean b2;
            if (!this.key.validate(next)) {
                b2 = b;
            }
            else {
                b2 = b;
                if (this.value.validate(next)) {
                    next = parent.getNext(name);
                    continue;
                }
            }
            return b2;
        }
        return true;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final OutputNode parent = outputNode.getParent();
        final Mode mode = outputNode.getMode();
        final Map map = (Map)o;
        if (!outputNode.isCommitted()) {
            outputNode.remove();
        }
        this.write(parent, map, mode);
    }
}
