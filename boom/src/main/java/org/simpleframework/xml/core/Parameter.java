// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

interface Parameter
{
    Annotation getAnnotation();
    
    Expression getExpression();
    
    int getIndex();
    
    Object getKey();
    
    String getName();
    
    String getPath();
    
    Class getType();
    
    boolean isAttribute();
    
    boolean isPrimitive();
    
    boolean isRequired();
    
    boolean isText();
    
    String toString();
}
