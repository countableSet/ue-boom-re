// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

public interface Transform<T>
{
    T read(final String p0) throws Exception;
    
    String write(final T p0) throws Exception;
}
