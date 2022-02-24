// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

public interface Visitor
{
    void read(final Type p0, final NodeMap<InputNode> p1) throws Exception;
    
    void write(final Type p0, final NodeMap<OutputNode> p1) throws Exception;
}
