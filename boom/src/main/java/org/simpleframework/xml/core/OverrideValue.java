// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;

class OverrideValue implements Value
{
    private final Class type;
    private final Value value;
    
    public OverrideValue(final Value value, final Class type) {
        this.value = value;
        this.type = type;
    }
    
    @Override
    public int getLength() {
        return this.value.getLength();
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public Object getValue() {
        return this.value.getValue();
    }
    
    @Override
    public boolean isReference() {
        return this.value.isReference();
    }
    
    @Override
    public void setValue(final Object value) {
        this.value.setValue(value);
    }
}
