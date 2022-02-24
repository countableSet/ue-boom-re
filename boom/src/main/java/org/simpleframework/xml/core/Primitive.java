// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class Primitive implements Converter
{
    private final Context context;
    private final String empty;
    private final Class expect;
    private final PrimitiveFactory factory;
    private final Type type;
    
    public Primitive(final Context context, final Type type) {
        this(context, type, null);
    }
    
    public Primitive(final Context context, final Type type, final String empty) {
        this.factory = new PrimitiveFactory(context, type);
        this.expect = type.getType();
        this.context = context;
        this.empty = empty;
        this.type = type;
    }
    
    private Object readElement(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        Object o;
        if (!instance.isReference()) {
            o = this.readElement(inputNode, instance);
        }
        else {
            o = instance.getInstance();
        }
        return o;
    }
    
    private Object readElement(final InputNode inputNode, final Instance instance) throws Exception {
        final Object read = this.read(inputNode, this.expect);
        if (instance != null) {
            instance.setInstance(read);
        }
        return read;
    }
    
    private Object readTemplate(String property, final Class clazz) throws Exception {
        property = this.context.getProperty(property);
        Object instance;
        if (property != null) {
            instance = this.factory.getInstance(property, clazz);
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    private boolean validateElement(final InputNode inputNode) throws Exception {
        final Instance instance = this.factory.getInstance(inputNode);
        if (!instance.isReference()) {
            instance.setInstance(null);
        }
        return true;
    }
    
    @Override
    public Object read(final InputNode inputNode) throws Exception {
        Object o;
        if (inputNode.isElement()) {
            o = this.readElement(inputNode);
        }
        else {
            o = this.read(inputNode, this.expect);
        }
        return o;
    }
    
    public Object read(final InputNode inputNode, final Class clazz) throws Exception {
        final String value = inputNode.getValue();
        Object o;
        if (value == null) {
            o = null;
        }
        else if (this.empty != null && value.equals(this.empty)) {
            o = this.empty;
        }
        else {
            o = this.readTemplate(value, clazz);
        }
        return o;
    }
    
    @Override
    public Object read(final InputNode inputNode, final Object o) throws Exception {
        if (o != null) {
            throw new PersistenceException("Can not read existing %s for %s", new Object[] { this.expect, this.type });
        }
        return this.read(inputNode);
    }
    
    @Override
    public boolean validate(final InputNode inputNode) throws Exception {
        if (inputNode.isElement()) {
            this.validateElement(inputNode);
        }
        else {
            inputNode.getValue();
        }
        return true;
    }
    
    @Override
    public void write(final OutputNode outputNode, final Object o) throws Exception {
        final String text = this.factory.getText(o);
        if (text != null) {
            outputNode.setValue(text);
        }
    }
}
