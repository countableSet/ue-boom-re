// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

public interface Value
{
    int getLength();
    
    Class getType();
    
    Object getValue();
    
    boolean isReference();
    
    void setValue(final Object p0);
}
