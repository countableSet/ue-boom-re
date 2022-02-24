// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

class Reference implements Value
{
    private Class type;
    private Object value;
    
    public Reference(final Object value, final Class type) {
        this.value = value;
        this.type = type;
    }
    
    @Override
    public int getLength() {
        return 0;
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
        return true;
    }
    
    @Override
    public void setValue(final Object value) {
        this.value = value;
    }
}
