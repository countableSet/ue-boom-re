// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.util.ConcurrentCache;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.util.Cache;

class SetPart implements MethodPart
{
    private final Cache<Annotation> cache;
    private final Annotation label;
    private final Annotation[] list;
    private final Method method;
    private final String name;
    private final MethodType type;
    
    public SetPart(final MethodName methodName, final Annotation label, final Annotation[] list) {
        this.cache = new ConcurrentCache<Annotation>();
        this.method = methodName.getMethod();
        this.name = methodName.getName();
        this.type = methodName.getType();
        this.label = label;
        this.list = list;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label;
    }
    
    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> clazz) {
        if (this.cache.isEmpty()) {
            for (final Annotation annotation : this.list) {
                this.cache.cache(annotation.annotationType(), annotation);
            }
        }
        return (T)this.cache.fetch(clazz);
    }
    
    @Override
    public Class getDeclaringClass() {
        return this.method.getDeclaringClass();
    }
    
    @Override
    public Class getDependent() {
        return Reflector.getParameterDependent(this.method, 0);
    }
    
    @Override
    public Class[] getDependents() {
        return Reflector.getParameterDependents(this.method, 0);
    }
    
    @Override
    public Method getMethod() {
        if (!this.method.isAccessible()) {
            this.method.setAccessible(true);
        }
        return this.method;
    }
    
    @Override
    public MethodType getMethodType() {
        return this.type;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Class getType() {
        return this.method.getParameterTypes()[0];
    }
    
    @Override
    public String toString() {
        return this.method.toGenericString();
    }
}
