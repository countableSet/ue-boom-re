// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.util;

public interface Cache<T>
{
    void cache(final Object p0, final T p1);
    
    boolean contains(final Object p0);
    
    T fetch(final Object p0);
    
    boolean isEmpty();
    
    T take(final Object p0);
}
