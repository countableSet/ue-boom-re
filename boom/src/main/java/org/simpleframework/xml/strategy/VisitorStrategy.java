// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import org.simpleframework.xml.stream.OutputNode;
import java.util.Map;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

public class VisitorStrategy implements Strategy
{
    private final Strategy strategy;
    private final Visitor visitor;
    
    public VisitorStrategy(final Visitor visitor) {
        this(visitor, new TreeStrategy());
    }
    
    public VisitorStrategy(final Visitor visitor, final Strategy strategy) {
        this.strategy = strategy;
        this.visitor = visitor;
    }
    
    @Override
    public Value read(final Type type, final NodeMap<InputNode> nodeMap, final Map map) throws Exception {
        if (this.visitor != null) {
            this.visitor.read(type, nodeMap);
        }
        return this.strategy.read(type, nodeMap, map);
    }
    
    @Override
    public boolean write(final Type type, final Object o, final NodeMap<OutputNode> nodeMap, final Map map) throws Exception {
        final boolean write = this.strategy.write(type, o, nodeMap, map);
        if (this.visitor != null) {
            this.visitor.write(type, nodeMap);
        }
        return write;
    }
}
