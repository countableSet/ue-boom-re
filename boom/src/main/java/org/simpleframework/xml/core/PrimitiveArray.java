// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.Position;
import java.lang.reflect.Array;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;

class PrimitiveArray implements Converter
{
    private final Type entry;
    private final ArrayFactory factory;
    private final String parent;
    private final Primitive root;
    private final Type type;
    
    public PrimitiveArray(final Context context, final Type type, final Type entry, final String parent) {
        this.factory = new ArrayFactory(context, type);
        this.root = new Primitive(context, entry);
        this.parent = parent;
        this.entry = entry;
        this.type = type;
    }
    
    private boolean isOverridden(final OutputNode outputNode, final Object o) throws Exception {
        return this.factory.setOverride(this.entry, o, outputNode);
    }
    
    private boolean validate(final InputNode inputNode, final Class clazz) throws Exception {
        while (true) {
            final InputNode next = inputNode.getNext();
            if (next == null) {
                break;
            }
            this.root.validate(next);
        }
        return true;
    }
    
    private void write(final OutputNode outputNode, Object value, final int n) throws Exception {
        value = Array.get(value, n);
        if (value != null && !this.isOverridden(outputNode, value)) {
            this.root.write(outputNode, value);
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        Object o = instance.getInstance();
        if (!instance.isReference()) {
            o = this.read(inputNode, o);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final int length = Array.getLength(o);
        int n = 0;
        while (true) {
            final Position position = inputNode.getPosition();
            final InputNode next = inputNode.getNext();
            if (next == null) {
                return o;
            }
            if (n >= length) {
                throw new ElementException("Array length missing or incorrect for %s at %s", new Object[] { this.type, position });
            }
            Array.set(o, n, this.root.read(next));
            ++n;
        }
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
        for (int length = Array.getLength(o), i = 0; i < length; ++i) {
            final OutputNode child = outputNode.getChild(this.parent);
            if (child == null) {
                break;
            }
            this.write(child, o, i);
        }
    }
}
