// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.OutputNode;
import java.util.Map;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class CompositeMap implements Converter
{
    private final Entry entry;
    private final MapFactory factory;
    private final Converter key;
    private final Style style;
    private final Converter value;
    
    public CompositeMap(final Context context, final Entry entry, final Type type) throws Exception {
        this.factory = new MapFactory(context, type);
        this.value = entry.getValue(context);
        this.key = entry.getKey(context);
        this.style = context.getStyle();
        this.entry = entry;
    }
    
    private Object populate(final InputNode inputNode, final Object o) throws Exception {
        final Map map = (Map)o;
        while (true) {
            final InputNode next = inputNode.getNext();
            if (next == null) {
                break;
            }
            map.put(this.key.read(next), this.value.read(next));
        }
        return map;
    }
    
    private boolean validate(final InputNode inputNode, final Class clazz) throws Exception {
        final boolean b = false;
        InputNode next;
        do {
            next = inputNode.getNext();
            boolean b2;
            if (next == null) {
                b2 = true;
            }
            else {
                b2 = b;
                if (this.key.validate(next)) {
                    continue;
                }
            }
            return b2;
        } while (this.value.validate(next));
        return b;
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        Object o = instance.getInstance();
        if (!instance.isReference()) {
            o = this.populate(inputNode, o);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object instance) throws Exception {
        final Instance instance2 = this.factory.getInstance(inputNode);
        Object o;
        if (instance2.isReference()) {
            o = instance2.getInstance();
        }
        else {
            instance2.setInstance(instance);
            if ((o = instance) != null) {
                o = this.populate(inputNode, instance);
            }
        }
        return o;
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        boolean validate;
        if (!instance.isReference()) {
            instance.setInstance(null);
            validate = this.validate(inputNode, instance.getType());
        }
        else {
            validate = true;
        }
        return validate;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Map map = (Map)o;
        for (final Object next : map.keySet()) {
            final OutputNode child = outputNode.getChild(this.style.getElement(this.entry.getEntry()));
            final V value = map.get(next);
            this.key.write(child, next);
            this.value.write(child, value);
        }
    }
}
