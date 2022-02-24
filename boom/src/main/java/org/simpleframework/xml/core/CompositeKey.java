// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class CompositeKey implements Converter
{
    private final Context context;
    private final Entry entry;
    private final Traverser root;
    private final Style style;
    private final Type type;
    
    public CompositeKey(final Context context, final Entry entry, final Type type) throws Exception {
        this.root = new Traverser(context);
        this.style = context.getStyle();
        this.context = context;
        this.entry = entry;
        this.type = type;
    }
    
    private Object read(final InputNode inputNode, final String s) throws Exception {
        final Object o = null;
        final String element = this.style.getElement(s);
        final Class type = this.type.getType();
        InputNode next = inputNode;
        if (element != null) {
            next = inputNode.getNext(element);
        }
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
    
    private boolean validate(InputNode next, final String s) throws Exception {
        boolean validate = true;
        next = next.getNext(this.style.getElement(s));
        final Class type = this.type.getType();
        if (next != null && !next.isEmpty()) {
            validate = this.root.validate(next, type);
        }
        return validate;
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Position position = inputNode.getPosition();
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getKey()) == null) {
            s = this.context.getName(type);
        }
        if (this.entry.isAttribute()) {
            throw new AttributeException("Can not have %s as an attribute for %s at %s", new Object[] { type, this.entry, position });
        }
        return this.read(inputNode, s);
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Position position = inputNode.getPosition();
        final Class type = this.type.getType();
        if (o != null) {
            throw new PersistenceException("Can not read key of %s for %s at %s", new Object[] { type, this.entry, position });
        }
        return this.read(inputNode);
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        final Position position = inputNode.getPosition();
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getKey()) == null) {
            s = this.context.getName(type);
        }
        if (this.entry.isAttribute()) {
            throw new ElementException("Can not have %s as an attribute for %s at %s", new Object[] { type, this.entry, position });
        }
        return this.validate(inputNode, s);
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Class type = this.type.getType();
        final String key = this.entry.getKey();
        if (this.entry.isAttribute()) {
            throw new ElementException("Can not have %s as an attribute for %s", new Object[] { type, this.entry });
        }
        String name;
        if ((name = key) == null) {
            name = this.context.getName(type);
        }
        this.root.write(outputNode, o, type, this.style.getElement(name));
    }
}
