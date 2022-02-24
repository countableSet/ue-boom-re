// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Array;
import org.simpleframework.xml.strategy.Value;

class ArrayInstance implements Instance
{
    private final int length;
    private final Class type;
    private final Value value;
    
    public ArrayInstance(final Value value) {
        this.length = value.getLength();
        this.type = value.getType();
        this.value = value;
    }
    
    @Override
    public Object getInstance() throws Exception {
        Object o;
        if (this.value.isReference()) {
            o = this.value.getValue();
        }
        else {
            final Object value = o = Array.newInstance(this.type, this.length);
            if (this.value != null) {
                this.value.setValue(value);
                o = value;
            }
        }
        return o;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public boolean isReference() {
        return this.value.isReference();
    }
    
    @Override
    public Object setInstance(final Object value) {
        if (this.value != null) {
            this.value.setValue(value);
        }
        return value;
    }
}
