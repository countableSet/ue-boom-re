// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;

public interface Converter<T>
{
    T read(final InputNode p0) throws Exception;
    
    void write(final OutputNode p0, final T p1) throws Exception;
}
