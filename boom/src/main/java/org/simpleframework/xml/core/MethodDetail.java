// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

class MethodDetail
{
    private final Annotation[] list;
    private final Method method;
    private final String name;
    
    public MethodDetail(final Method method) {
        this.list = method.getDeclaredAnnotations();
        this.name = method.getName();
        this.method = method;
    }
    
    public Annotation[] getAnnotations() {
        return this.list;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public String getName() {
        return this.name;
    }
}
