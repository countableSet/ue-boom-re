// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;

abstract class ParameterContact<T extends Annotation> implements Contact
{
    protected final Constructor factory;
    protected final int index;
    protected final T label;
    protected final Annotation[] labels;
    protected final Class owner;
    
    public ParameterContact(final T label, final Constructor factory, final int index) {
        this.labels = factory.getParameterAnnotations()[index];
        this.owner = factory.getDeclaringClass();
        this.factory = factory;
        this.index = index;
        this.label = label;
    }
    
    @Override
    public Object get(final Object o) {
        return null;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label;
    }
    
    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> obj) {
        for (final Annotation annotation : this.labels) {
            if (annotation.annotationType().equals(obj)) {
                final Annotation annotation2 = annotation;
                return (A)annotation2;
            }
        }
        final Annotation annotation2 = null;
        return (A)annotation2;
    }
    
    @Override
    public Class getDeclaringClass() {
        return this.owner;
    }
    
    @Override
    public Class getDependent() {
        return Reflector.getParameterDependent(this.factory, this.index);
    }
    
    @Override
    public Class[] getDependents() {
        return Reflector.getParameterDependents(this.factory, this.index);
    }
    
    @Override
    public abstract String getName();
    
    @Override
    public Class getType() {
        return this.factory.getParameterTypes()[this.index];
    }
    
    @Override
    public boolean isReadOnly() {
        return false;
    }
    
    @Override
    public void set(final Object o, final Object o2) {
    }
    
    @Override
    public String toString() {
        return String.format("parameter %s of constructor %s", this.index, this.factory);
    }
}
