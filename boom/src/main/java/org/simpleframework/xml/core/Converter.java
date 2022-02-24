// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;

interface Converter
{
    Object read(final InputNode p0) throws Exception;
    
    Object read(final InputNode p0, final Object p1) throws Exception;
    
    boolean validate(final InputNode p0) throws Exception;
    
    void write(final OutputNode p0, final Object p1) throws Exception;
}
