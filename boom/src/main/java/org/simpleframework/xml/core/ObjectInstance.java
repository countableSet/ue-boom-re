// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;

class ObjectInstance implements Instance
{
    private final Context context;
    private final Class type;
    private final Value value;
    
    public ObjectInstance(final Context context, final Value value) {
        this.type = value.getType();
        this.context = context;
        this.value = value;
    }
    
    @Override
    public Object getInstance() throws Exception {
        Object o;
        if (this.value.isReference()) {
            o = this.value.getValue();
        }
        else {
            final Object value = o = this.getInstance(this.type);
            if (this.value != null) {
                this.value.setValue(value);
                o = value;
            }
        }
        return o;
    }
    
    public Object getInstance(final Class clazz) throws Exception {
        return this.context.getInstance(clazz).getInstance();
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
