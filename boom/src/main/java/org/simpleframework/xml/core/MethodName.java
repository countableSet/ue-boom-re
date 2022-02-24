// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Method;

class MethodName
{
    private Method method;
    private String name;
    private MethodType type;
    
    public MethodName(final Method method, final MethodType type, final String name) {
        this.method = method;
        this.type = type;
        this.name = name;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public String getName() {
        return this.name;
    }
    
    public MethodType getType() {
        return this.type;
    }
}
