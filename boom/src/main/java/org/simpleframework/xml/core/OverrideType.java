// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;

class OverrideType implements Type
{
    private final Class override;
    private final Type type;
    
    public OverrideType(final Type type, final Class override) {
        this.override = override;
        this.type = type;
    }
    
    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> clazz) {
        return this.type.getAnnotation(clazz);
    }
    
    @Override
    public Class getType() {
        return this.override;
    }
    
    @Override
    public String toString() {
        return this.type.toString();
    }
}
