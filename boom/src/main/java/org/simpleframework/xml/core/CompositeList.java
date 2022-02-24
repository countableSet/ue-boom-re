// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.OutputNode;
import java.util.Collection;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class CompositeList implements Converter
{
    private final Type entry;
    private final CollectionFactory factory;
    private final String name;
    private final Traverser root;
    private final Type type;
    
    public CompositeList(final Context context, final Type type, final Type entry, final String name) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Traverser(context);
        this.entry = entry;
        this.type = type;
        this.name = name;
    }
    
    private Object populate(final InputNode inputNode, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        while (true) {
            final InputNode next = inputNode.getNext();
            final Class type = this.entry.getType();
            if (next == null) {
                break;
            }
            collection.add(this.root.read(next, type));
        }
        return collection;
    }
    
    private boolean validate(final InputNode inputNode, Class type) throws Exception {
        while (true) {
            final InputNode next = inputNode.getNext();
            type = this.entry.getType();
            if (next == null) {
                break;
            }
            this.root.validate(next, type);
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
                final Class type = this.entry.getType();
                final Class<?> class1 = next.getClass();
                if (!type.isAssignableFrom(class1)) {
                    throw new PersistenceException("Entry %s does not match %s for %s", new Object[] { class1, this.entry, this.type });
                }
                this.root.write(outputNode, next, type, this.name);
            }
        }
    }
}
