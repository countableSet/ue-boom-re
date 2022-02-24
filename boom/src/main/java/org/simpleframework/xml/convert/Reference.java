// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import org.simpleframework.xml.strategy.Value;

class Reference implements Value
{
    private Class actual;
    private Object data;
    private Value value;
    
    public Reference(final Value value, final Object data, final Class actual) {
        this.actual = actual;
        this.value = value;
        this.data = data;
    }
    
    @Override
    public int getLength() {
        return 0;
    }
    
    @Override
    public Class getType() {
        Class<?> clazz;
        if (this.data != null) {
            clazz = this.data.getClass();
        }
        else {
            clazz = (Class<?>)this.actual;
        }
        return clazz;
    }
    
    @Override
    public Object getValue() {
        return this.data;
    }
    
    @Override
    public boolean isReference() {
        return true;
    }
    
    @Override
    public void setValue(final Object o) {
        if (this.value != null) {
            this.value.setValue(o);
        }
        this.data = o;
    }
}
