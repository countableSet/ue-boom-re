// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.stream.Style;

class PrimitiveKey implements Converter
{
    private final Context context;
    private final Entry entry;
    private final PrimitiveFactory factory;
    private final Primitive root;
    private final Style style;
    private final Type type;
    
    public PrimitiveKey(final Context context, final Entry entry, final Type type) {
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
    
    private Object readAttribute(InputNode attribute, final String s) throws Exception {
        attribute = attribute.getAttribute(this.style.getAttribute(s));
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
        next = next.getNext(this.style.getElement(s));
        Object read;
        if (next == null) {
            read = null;
        }
        else {
            read = this.root.read(next);
        }
        return read;
    }
    
    private boolean validateAttribute(InputNode attribute, final String s) throws Exception {
        attribute = attribute.getAttribute(this.style.getElement(s));
        return attribute == null || this.root.validate(attribute);
    }
    
    private boolean validateElement(InputNode next, final String s) throws Exception {
        next = next.getNext(this.style.getElement(s));
        return next == null || this.root.validate(next);
    }
    
    private void writeAttribute(final OutputNode outputNode, final Object o) throws Exception {
        final Class type = this.type.getType();
        final String text = this.factory.getText(o);
        String s;
        if ((s = this.entry.getKey()) == null) {
            s = this.context.getName(type);
        }
        final String attribute = this.style.getAttribute(s);
        if (text != null) {
            outputNode.setAttribute(attribute, text);
        }
    }
    
    private void writeElement(OutputNode child, final Object o) throws Exception {
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getKey()) == null) {
            s = this.context.getName(type);
        }
        child = child.getChild(this.style.getElement(s));
        if (o != null && !this.isOverridden(child, o)) {
            this.root.write(child, o);
        }
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getKey()) == null) {
            s = this.context.getName(type);
        }
        Object o;
        if (!this.entry.isAttribute()) {
            o = this.readElement(inputNode, s);
        }
        else {
            o = this.readAttribute(inputNode, s);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        final Class type = this.type.getType();
        if (o != null) {
            throw new PersistenceException("Can not read key of %s for %s", new Object[] { type, this.entry });
        }
        return this.read(inputNode);
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        final Class type = this.type.getType();
        String s;
        if ((s = this.entry.getKey()) == null) {
            s = this.context.getName(type);
        }
        boolean b;
        if (!this.entry.isAttribute()) {
            b = this.validateElement(inputNode, s);
        }
        else {
            b = this.validateAttribute(inputNode, s);
        }
        return b;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        if (!this.entry.isAttribute()) {
            this.writeElement(outputNode, o);
        }
        else if (o != null) {
            this.writeAttribute(outputNode, o);
        }
    }
}
