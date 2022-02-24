// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import org.simpleframework.xml.stream.OutputNode;
import java.util.Map;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

public interface Strategy
{
    Value read(final Type p0, final NodeMap<InputNode> p1, final Map p2) throws Exception;
    
    boolean write(final Type p0, final Object p1, final NodeMap<OutputNode> p2, final Map p3) throws Exception;
}
