// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import java.util.Map;
import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.TreeStrategy;
import org.simpleframework.xml.strategy.Strategy;

public class RegistryStrategy implements Strategy
{
    private final Registry registry;
    private final Strategy strategy;
    
    public RegistryStrategy(final Registry registry) {
        this(registry, new TreeStrategy());
    }
    
    public RegistryStrategy(final Registry registry, final Strategy strategy) {
        this.registry = registry;
        this.strategy = strategy;
    }
    
    private boolean isReference(final Value value) {
        return value != null && value.isReference();
    }
    
    private Converter lookup(final Type type, final Object o) throws Exception {
        Class<?> clazz = (Class<?>)type.getType();
        if (o != null) {
            clazz = o.getClass();
        }
        return this.registry.lookup(clazz);
    }
    
    private Converter lookup(final Type type, final Value value) throws Exception {
        Class clazz = type.getType();
        if (value != null) {
            clazz = value.getType();
        }
        return this.registry.lookup(clazz);
    }
    
    private Value read(final Type type, final NodeMap<InputNode> nodeMap, final Value value) throws Exception {
        final Converter lookup = this.lookup(type, value);
        final InputNode inputNode = nodeMap.getNode();
        Value value2 = value;
        if (lookup != null) {
            final Object read = lookup.read(inputNode);
            final Class type2 = type.getType();
            if (value != null) {
                value.setValue(read);
            }
            value2 = new Reference(value, read, type2);
        }
        return value2;
    }
    
    private boolean write(final Type type, final Object o, final NodeMap<OutputNode> nodeMap) throws Exception {
        final Converter lookup = this.lookup(type, o);
        final OutputNode outputNode = nodeMap.getNode();
        boolean b;
        if (lookup != null) {
            lookup.write(outputNode, o);
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    @Override
    public Value read(final Type type, final NodeMap<InputNode> nodeMap, final Map map) throws Exception {
        final Value read = this.strategy.read(type, nodeMap, map);
        Value read2;
        if (this.isReference(read)) {
            read2 = read;
        }
        else {
            read2 = this.read(type, nodeMap, read);
        }
        return read2;
    }
    
    @Override
    public boolean write(final Type type, final Object o, final NodeMap<OutputNode> nodeMap, final Map map) throws Exception {
        boolean b;
        if (!(b = this.strategy.write(type, o, nodeMap, map))) {
            b = this.write(type, o, nodeMap);
        }
        return b;
    }
}
