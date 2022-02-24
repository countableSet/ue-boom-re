// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

class ArrayValue implements Value
{
    private int size;
    private Class type;
    private Object value;
    
    public ArrayValue(final Class type, final int size) {
        this.type = type;
        this.size = size;
    }
    
    @Override
    public int getLength() {
        return this.size;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public boolean isReference() {
        return false;
    }
    
    @Override
    public void setValue(final Object value) {
        this.value = value;
    }
}
