// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import org.simpleframework.xml.stream.Mode;
import java.util.Collection;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;

class PrimitiveInlineList implements Repeater
{
    private final Type entry;
    private final CollectionFactory factory;
    private final String parent;
    private final Primitive root;
    
    public PrimitiveInlineList(final Context context, final Type type, final Type entry, final String parent) {
        this.factory = new CollectionFactory(context, type);
        this.root = new Primitive(context, entry);
        this.parent = parent;
        this.entry = entry;
    }
    
    private boolean isOverridden(final OutputNode outputNode, final Object o) throws Exception {
        return this.factory.setOverride(this.entry, o, outputNode);
    }
    
    private Object read(InputNode next, final Collection collection) throws Exception {
        final InputNode parent = next.getParent();
        for (String name = next.getName(); next != null; next = parent.getNext(name)) {
            final Object read = this.root.read(next);
            if (read != null) {
                collection.add(read);
            }
        }
        return collection;
    }
    
    private void write(final OutputNode outputNode, final Object o, final Mode mode) throws Exception {
        for (final Object next : (Collection)o) {
            if (next != null) {
                final OutputNode child = outputNode.getChild(this.parent);
                if (this.isOverridden(child, next)) {
                    continue;
                }
                child.setMode(mode);
                this.root.write(child, next);
            }
        }
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
        for (String name = next.getName(); next != null; next = parent.getNext(name)) {
            if (!this.root.validate(next)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final OutputNode parent = outputNode.getParent();
        final Mode mode = outputNode.getMode();
        if (!outputNode.isCommitted()) {
            outputNode.remove();
        }
        this.write(parent, o, mode);
    }
}
