// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import java.util.Map;

class Allocate implements Value
{
    private String key;
    private Map map;
    private Value value;
    
    public Allocate(final Value value, final Map map, final String key) {
        this.value = value;
        this.map = map;
        this.key = key;
    }
    
    @Override
    public int getLength() {
        return this.value.getLength();
    }
    
    @Override
    public Class getType() {
        return this.value.getType();
    }
    
    @Override
    public Object getValue() {
        return this.map.get(this.key);
    }
    
    @Override
    public boolean isReference() {
        return false;
    }
    
    @Override
    public void setValue(final Object value) {
        if (this.key != null) {
            this.map.put(this.key, value);
        }
        this.value.setValue(value);
    }
}
