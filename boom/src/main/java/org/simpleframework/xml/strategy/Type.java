// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import java.lang.annotation.Annotation;

public interface Type
{
     <T extends Annotation> T getAnnotation(final Class<T> p0);
    
    Class getType();
    
    String toString();
}
