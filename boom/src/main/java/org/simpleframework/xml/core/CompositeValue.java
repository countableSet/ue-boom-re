// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class CompositeValue implements Converter
{
    private final Context context;
    private final Entry entry;
    private final Traverser root;
    private final Style style;
    private final Type type;
    
    public CompositeValue(final Context context, final Entry entry, final Type type) throws Exception {
        this.root = new Traverser(context);
        this.style = context.getStyle();
        this.context = context;
        this.entry = entry;
        this.type = type;
    }
    
    private boolean validate(final InputNode inputNode, final String s) throws Exception {
        boolean validate = true;
        final InputNode next = inputNode.getNext(this.style.getElement(s));
        final Class type = this.type.getType();
        if (next != null && !next.isEmpty()) {
            validate = this.root.validate(next, type);
        }
        return validate;
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Object o = null;
        final InputNode next = inputNode.getNext();
        final Class type = this.type.getType();
        Object read;
        if (next == null) {
            read = o;
        }
        else {
            read = o;
            if (!next.isEmpty()) {
                read = this.root.read(next, type);
            }
        }
        return read;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Class type = this.type.getType();
        if (o != null) {
            throw new PersistenceException("Can not read value of %s for %s", new Object[] { type, this.entry });
        }
        return this.read(inputNode);
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getValue()) == null) {
            s = this.context.getName(type);
        }
        return this.validate(inputNode, s);
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getValue()) == null) {
            s = this.context.getName(type);
        }
        this.root.write(outputNode, o, type, this.style.getElement(s));
    }
}
