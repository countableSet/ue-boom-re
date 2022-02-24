// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;

interface Contact extends Type
{
    Object get(final Object p0) throws Exception;
    
    Annotation getAnnotation();
    
    Class getDeclaringClass();
    
    Class getDependent();
    
    Class[] getDependents();
    
    String getName();
    
    boolean isReadOnly();
    
    void set(final Object p0, final Object p1) throws Exception;
    
    String toString();
}
