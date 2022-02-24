// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

interface MethodPart
{
    Annotation getAnnotation();
    
     <T extends Annotation> T getAnnotation(final Class<T> p0);
    
    Class getDeclaringClass();
    
    Class getDependent();
    
    Class[] getDependents();
    
    Method getMethod();
    
    MethodType getMethodType();
    
    String getName();
    
    Class getType();
    
    String toString();
}
