// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.OutputNode;
import java.util.Collection;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class CompositeInlineList implements Repeater
{
    private final Type entry;
    private final CollectionFactory factory;
    private final String name;
    private final Traverser root;
    private final Type type;
    
    public CompositeInlineList(final Context context, final Type type, final Type entry, final String name) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Traverser(context);
        this.entry = entry;
        this.type = type;
        this.name = name;
    }
    
    private Object read(final InputNode inputNode, final Class clazz) throws Exception {
        final Object read = this.root.read(inputNode, clazz);
        final Class<?> class1 = read.getClass();
        if (!this.entry.getType().isAssignableFrom(class1)) {
            throw new PersistenceException("Entry %s does not match %s for %s", new Object[] { class1, this.entry, this.type });
        }
        return read;
    }
    
    private Object read(InputNode next, final Collection collection) throws Exception {
        final InputNode parent = next.getParent();
        for (String name = next.getName(); next != null; next = parent.getNext(name)) {
            final Object read = this.read(next, this.entry.getType());
            if (read != null) {
                collection.add(read);
            }
        }
        return collection;
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Collection collection = (Collection)this.factory.getInstance();
        Object read;
        if (collection != null) {
            read = this.read(inputNode, collection);
        }
        else {
            read = null;
        }
        return read;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        Object o2;
        if (collection != null) {
            o2 = this.read(inputNode, collection);
        }
        else {
            o2 = this.read(inputNode);
        }
        return o2;
    }
    
    @Override
    public boolean validate(InputNode next) throws Exception {
        final InputNode parent = next.getParent();
        final Class type = this.entry.getType();
        for (String name = next.getName(); next != null; next = parent.getNext(name)) {
            if (!this.root.validate(next, type)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Collection collection = (Collection)o;
        final OutputNode parent = outputNode.getParent();
        if (!outputNode.isCommitted()) {
            outputNode.remove();
        }
        this.write(parent, collection);
    }
    
    public void write(final OutputNode outputNode, final Collection collection) throws Exception {
        for (final Object next : collection) {
            if (next != null) {
                final Class type = this.entry.getType();
                final Class<?> class1 = next.getClass();
                if (!type.isAssignableFrom(class1)) {
                    throw new PersistenceException("Entry %s does not match %s for %s", new Object[] { class1, type, this.type });
                }
                this.root.write(outputNode, next, type, this.name);
            }
        }
    }
}
