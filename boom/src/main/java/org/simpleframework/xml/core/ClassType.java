// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;

class ClassType implements Type
{
    private final Class type;
    
    public ClassType(final Class type) {
        this.type = type;
    }
    
    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> clazz) {
        return null;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return this.type.toString();
    }
}
