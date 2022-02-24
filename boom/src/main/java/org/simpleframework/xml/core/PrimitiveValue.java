// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class PrimitiveValue implements Converter
{
    private final Context context;
    private final Entry entry;
    private final PrimitiveFactory factory;
    private final Primitive root;
    private final Style style;
    private final Type type;
    
    public PrimitiveValue(final Context context, final Entry entry, final Type type) {
        this.factory = new PrimitiveFactory(context, type);
        this.root = new Primitive(context, type);
        this.style = context.getStyle();
        this.context = context;
        this.entry = entry;
        this.type = type;
    }
    
    private boolean isOverridden(final OutputNode outputNode, final Object o) throws Exception {
        return this.factory.setOverride(this.type, o, outputNode);
    }
    
    private Object readAttribute(final InputNode inputNode, final String s) throws Exception {
        InputNode attribute = inputNode;
        if (s != null) {
            attribute = inputNode.getAttribute(this.style.getAttribute(s));
        }
        Object read;
        if (attribute == null) {
            read = null;
        }
        else {
            read = this.root.read(attribute);
        }
        return read;
    }
    
    private Object readElement(InputNode next, final String s) throws Exception {
        next = next.getNext(this.style.getAttribute(s));
        Object read;
        if (next == null) {
            read = null;
        }
        else {
            read = this.root.read(next);
        }
        return read;
    }
    
    private boolean validateAttribute(final InputNode inputNode, final String s) throws Exception {
        InputNode next = inputNode;
        if (s != null) {
            next = inputNode.getNext(this.style.getAttribute(s));
        }
        return next == null || this.root.validate(next);
    }
    
    private boolean validateElement(final InputNode inputNode, final String s) throws Exception {
        return inputNode.getNext(this.style.getAttribute(s)) == null || this.root.validate(inputNode);
    }
    
    private void writeAttribute(final OutputNode outputNode, final Object o, final String s) throws Exception {
        if (o != null) {
            OutputNode setAttribute = outputNode;
            if (s != null) {
                setAttribute = outputNode.setAttribute(this.style.getAttribute(s), null);
            }
            this.root.write(setAttribute, o);
        }
    }
    
    private void writeElement(OutputNode child, final Object o, final String s) throws Exception {
        child = child.getChild(this.style.getAttribute(s));
        if (o != null && !this.isOverridden(child, o)) {
            this.root.write(child, o);
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Class type = this.type.getType();
        final String value = this.entry.getValue();
        Object o;
        if (!this.entry.isInline()) {
            String name;
            if ((name = value) == null) {
                name = this.context.getName(type);
            }
            o = this.readElement(inputNode, name);
        }
        else {
            o = this.readAttribute(inputNode, value);
        }
        return o;
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
        final String value = this.entry.getValue();
        boolean b;
        if (!this.entry.isInline()) {
            String name;
            if ((name = value) == null) {
                name = this.context.getName(type);
            }
            b = this.validateElement(inputNode, name);
        }
        else {
            b = this.validateAttribute(inputNode, value);
        }
        return b;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final Class type = this.type.getType();
        final String value = this.entry.getValue();
        if (!this.entry.isInline()) {
            String name;
            if ((name = value) == null) {
                name = this.context.getName(type);
            }
            this.writeElement(outputNode, o, name);
        }
        else {
            this.writeAttribute(outputNode, o, value);
        }
    }
}
