// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class FieldDetail
{
    private final Field field;
    private final Annotation[] list;
    private final String name;
    
    public FieldDetail(final Field field) {
        this.list = field.getDeclaredAnnotations();
        this.name = field.getName();
        this.field = field;
    }
    
    public Annotation[] getAnnotations() {
        return this.list;
    }
    
    public Field getField() {
        return this.field;
    }
    
    public String getName() {
        return this.name;
    }
}
