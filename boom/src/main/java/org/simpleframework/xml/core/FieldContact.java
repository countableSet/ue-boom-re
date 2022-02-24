// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Modifier;
import org.simpleframework.xml.util.ConcurrentCache;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.util.Cache;

class FieldContact implements Contact
{
    private final Cache<Annotation> cache;
    private final Field field;
    private final Annotation label;
    private final Annotation[] list;
    private final int modifier;
    private final String name;
    
    public FieldContact(final Field field, final Annotation label, final Annotation[] list) {
        this.cache = new ConcurrentCache<Annotation>();
        this.modifier = field.getModifiers();
        this.name = field.getName();
        this.label = label;
        this.field = field;
        this.list = list;
    }
    
    private <T extends Annotation> T getCache(final Class<T> clazz) {
        if (this.cache.isEmpty()) {
            for (final Annotation annotation : this.list) {
                this.cache.cache(annotation.annotationType(), annotation);
            }
        }
        return (T)this.cache.fetch(clazz);
    }
    
    @Override
    public Object get(final Object obj) throws Exception {
        return this.field.get(obj);
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label;
    }
    
    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> clazz) {
        Annotation annotation;
        if (clazz == this.label.annotationType()) {
            annotation = this.label;
        }
        else {
            annotation = this.getCache((Class<Annotation>)clazz);
        }
        return (T)annotation;
    }
    
    @Override
    public Class getDeclaringClass() {
        return this.field.getDeclaringClass();
    }
    
    @Override
    public Class getDependent() {
        return Reflector.getDependent(this.field);
    }
    
    @Override
    public Class[] getDependents() {
        return Reflector.getDependents(this.field);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Class getType() {
        return this.field.getType();
    }
    
    public boolean isFinal() {
        return Modifier.isFinal(this.modifier);
    }
    
    @Override
    public boolean isReadOnly() {
        return !this.isStatic() && this.isFinal();
    }
    
    public boolean isStatic() {
        return Modifier.isStatic(this.modifier);
    }
    
    @Override
    public void set(final Object obj, final Object value) throws Exception {
        if (!this.isFinal()) {
            this.field.set(obj, value);
        }
    }
    
    @Override
    public String toString() {
        return String.format("field '%s' %s", this.getName(), this.field.toString());
    }
}
