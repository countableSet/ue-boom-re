// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.Collection;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;

class PrimitiveList implements Converter
{
    private final Type entry;
    private final CollectionFactory factory;
    private final String parent;
    private final Primitive root;
    
    public PrimitiveList(final Context context, final Type type, final Type entry, final String parent) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Primitive(context, entry);
        this.parent = parent;
        this.entry = entry;
    }
    
    private boolean isOverridden(final OutputNode outputNode, final Object o) throws Exception {
        return this.factory.setOverride(this.entry, o, outputNode);
    }
    
    private Object populate(final InputNode inputNode, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        while (true) {
            final InputNode next = inputNode.getNext();
            if (next == null) {
                break;
            }
            collection.add(this.root.read(next));
        }
        return collection;
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
        for (final Object next : (Collection)o) {
            if (next != null) {
                final OutputNode child = outputNode.getChild(this.parent);
                if (this.isOverridden(child, next)) {
                    continue;
                }
                this.root.write(child, next);
            }
        }
    }
}
