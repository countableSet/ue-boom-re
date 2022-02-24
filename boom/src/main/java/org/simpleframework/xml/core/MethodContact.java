// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

class MethodContact implements Contact
{
    private MethodPart get;
    private Class item;
    private Class[] items;
    private Annotation label;
    private String name;
    private Class owner;
    private MethodPart set;
    private Class type;
    
    public MethodContact(final MethodPart methodPart) {
        this(methodPart, null);
    }
    
    public MethodContact(final MethodPart get, final MethodPart set) {
        this.owner = get.getDeclaringClass();
        this.label = get.getAnnotation();
        this.items = get.getDependents();
        this.item = get.getDependent();
        this.type = get.getType();
        this.name = get.getName();
        this.set = set;
        this.get = get;
    }
    
    @Override
    public Object get(final Object obj) throws Exception {
        return this.get.getMethod().invoke(obj, new Object[0]);
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label;
    }
    
    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> clazz) {
        final Annotation annotation = this.get.getAnnotation(clazz);
        Annotation annotation2;
        if (clazz == this.label.annotationType()) {
            annotation2 = this.label;
        }
        else if ((annotation2 = annotation) == null) {
            annotation2 = annotation;
            if (this.set != null) {
                annotation2 = this.set.getAnnotation(clazz);
            }
        }
        return (T)annotation2;
    }
    
    @Override
    public Class getDeclaringClass() {
        return this.owner;
    }
    
    @Override
    public Class getDependent() {
        return this.item;
    }
    
    @Override
    public Class[] getDependents() {
        return this.items;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public MethodPart getRead() {
        return this.get;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    public MethodPart getWrite() {
        return this.set;
    }
    
    @Override
    public boolean isReadOnly() {
        return this.set == null;
    }
    
    @Override
    public void set(final Object obj, final Object o) throws Exception {
        final Class<?> declaringClass = this.get.getMethod().getDeclaringClass();
        if (this.set == null) {
            throw new MethodException("Property '%s' is read only in %s", new Object[] { this.name, declaringClass });
        }
        this.set.getMethod().invoke(obj, o);
    }
    
    @Override
    public String toString() {
        return String.format("method '%s'", this.name);
    }
}
