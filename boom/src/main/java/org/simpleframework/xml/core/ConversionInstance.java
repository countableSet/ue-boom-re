// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;

class ConversionInstance implements Instance
{
    private final Context context;
    private final Class convert;
    private final Value value;
    
    public ConversionInstance(final Context context, final Value value, final Class convert) throws Exception {
        this.context = context;
        this.convert = convert;
        this.value = value;
    }
    
    @Override
    public Object getInstance() throws Exception {
        Object value;
        if (this.value.isReference()) {
            value = this.value.getValue();
        }
        else {
            final Object instance = this.getInstance(this.convert);
            if ((value = instance) != null) {
                this.setInstance(instance);
                value = instance;
            }
        }
        return value;
    }
    
    public Object getInstance(final Class clazz) throws Exception {
        return this.context.getInstance(clazz).getInstance();
    }
    
    @Override
    public Class getType() {
        return this.convert;
    }
    
    @Override
    public boolean isReference() {
        return this.value.isReference();
    }
    
    @Override
    public Object setInstance(final Object value) throws Exception {
        if (this.value != null) {
            this.value.setValue(value);
        }
        return value;
    }
}
