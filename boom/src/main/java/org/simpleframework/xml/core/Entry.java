// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.ElementMap;

class Entry
{
    private static final String DEFAULT_NAME = "entry";
    private boolean attribute;
    private Contact contact;
    private String entry;
    private String key;
    private Class keyType;
    private ElementMap label;
    private String value;
    private Class valueType;
    
    public Entry(final Contact contact, final ElementMap label) {
        this.attribute = label.attribute();
        this.entry = label.entry();
        this.value = label.value();
        this.key = label.key();
        this.contact = contact;
        this.label = label;
    }
    
    private Class getDependent(final int n) throws Exception {
        final Class[] dependents = this.contact.getDependents();
        Class<Object> clazz;
        if (dependents.length < n) {
            clazz = Object.class;
        }
        else if (dependents.length == 0) {
            clazz = Object.class;
        }
        else {
            clazz = (Class<Object>)dependents[n];
        }
        return clazz;
    }
    
    private boolean isEmpty(final String s) {
        return s.length() == 0;
    }
    
    public Contact getContact() {
        return this.contact;
    }
    
    public String getEntry() throws Exception {
        String s;
        if (this.entry == null) {
            s = this.entry;
        }
        else {
            if (this.isEmpty(this.entry)) {
                this.entry = "entry";
            }
            s = this.entry;
        }
        return s;
    }
    
    public String getKey() throws Exception {
        String s;
        if (this.key == null) {
            s = this.key;
        }
        else {
            if (this.isEmpty(this.key)) {
                this.key = null;
            }
            s = this.key;
        }
        return s;
    }
    
    public Converter getKey(final Context context) throws Exception {
        final Type keyType = this.getKeyType();
        Converter converter;
        if (context.isPrimitive(keyType)) {
            converter = new PrimitiveKey(context, this, keyType);
        }
        else {
            converter = new CompositeKey(context, this, keyType);
        }
        return converter;
    }
    
    protected Type getKeyType() throws Exception {
        if (this.keyType == null) {
            this.keyType = this.label.keyType();
            if (this.keyType == Void.TYPE) {
                this.keyType = this.getDependent(0);
            }
        }
        return new ClassType(this.keyType);
    }
    
    public String getValue() throws Exception {
        String s;
        if (this.value == null) {
            s = this.value;
        }
        else {
            if (this.isEmpty(this.value)) {
                this.value = null;
            }
            s = this.value;
        }
        return s;
    }
    
    public Converter getValue(final Context context) throws Exception {
        final Type valueType = this.getValueType();
        Converter converter;
        if (context.isPrimitive(valueType)) {
            converter = new PrimitiveValue(context, this, valueType);
        }
        else {
            converter = new CompositeValue(context, this, valueType);
        }
        return converter;
    }
    
    protected Type getValueType() throws Exception {
        if (this.valueType == null) {
            this.valueType = this.label.valueType();
            if (this.valueType == Void.TYPE) {
                this.valueType = this.getDependent(1);
            }
        }
        return new ClassType(this.valueType);
    }
    
    public boolean isAttribute() {
        return this.attribute;
    }
    
    public boolean isInline() throws Exception {
        return this.isAttribute();
    }
    
    @Override
    public String toString() {
        return String.format("%s on %s", this.label, this.contact);
    }
}
