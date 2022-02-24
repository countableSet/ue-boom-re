// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;
import java.lang.reflect.Array;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class CompositeArray implements Converter
{
    private final Type entry;
    private final ArrayFactory factory;
    private final String parent;
    private final Traverser root;
    private final Type type;
    
    public CompositeArray(final Context context, final Type type, final Type entry, final String parent) {
        this.factory = new ArrayFactory(context, type);
        this.root = new Traverser(context);
        this.parent = parent;
        this.entry = entry;
        this.type = type;
    }
    
    private void read(final InputNode inputNode, final Object o, final int n) throws Exception {
        final Class type = this.entry.getType();
        Object read = null;
        if (!inputNode.isEmpty()) {
            read = this.root.read(inputNode, type);
        }
        Array.set(o, n, read);
    }
    
    private boolean validate(final InputNode inputNode, final Class clazz) throws Exception {
        while (true) {
            final InputNode next = inputNode.getNext();
            if (next == null) {
                break;
            }
            if (next.isEmpty()) {
                continue;
            }
            this.root.validate(next, clazz);
        }
        return true;
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
            this.read(next, o, n);
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
            this.root.write(outputNode, Array.get(o, i), this.entry.getType(), this.parent);
        }
        outputNode.commit();
    }
}
