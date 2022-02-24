// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import java.util.Map;
import org.simpleframework.xml.stream.Node;
import org.simpleframework.xml.stream.NodeMap;
import java.util.HashMap;

class ReadGraph extends HashMap
{
    private final String label;
    private final String length;
    private final Loader loader;
    private final String mark;
    private final String refer;
    
    public ReadGraph(final Contract contract, final Loader loader) {
        this.refer = contract.getReference();
        this.mark = contract.getIdentity();
        this.length = contract.getLength();
        this.label = contract.getLabel();
        this.loader = loader;
    }
    
    private Value readArray(final Type type, final Class clazz, final NodeMap nodeMap) throws Exception {
        final org.simpleframework.xml.stream.Node remove = nodeMap.remove(this.length);
        int int1 = 0;
        if (remove != null) {
            int1 = Integer.parseInt(remove.getValue());
        }
        return new ArrayValue(clazz, int1);
    }
    
    private Value readInstance(final Type type, final Class clazz, final NodeMap nodeMap) throws Exception {
        final org.simpleframework.xml.stream.Node remove = nodeMap.remove(this.mark);
        Value value;
        if (remove == null) {
            value = this.readReference(type, clazz, nodeMap);
        }
        else {
            final String value2 = remove.getValue();
            if (this.containsKey(value2)) {
                throw new CycleException("Element '%s' already exists", new Object[] { value2 });
            }
            value = this.readValue(type, clazz, nodeMap, value2);
        }
        return value;
    }
    
    private Value readReference(final Type type, final Class clazz, final NodeMap nodeMap) throws Exception {
        final org.simpleframework.xml.stream.Node remove = nodeMap.remove(this.refer);
        Value value;
        if (remove == null) {
            value = this.readValue(type, clazz, nodeMap);
        }
        else {
            final String value2 = remove.getValue();
            final Object value3 = this.get(value2);
            if (!this.containsKey(value2)) {
                throw new CycleException("Invalid reference '%s' found", new Object[] { value2 });
            }
            value = new Reference(value3, clazz);
        }
        return value;
    }
    
    private Value readValue(final Type type, final Class clazz, final NodeMap nodeMap) throws Exception {
        Value array;
        if (type.getType().isArray()) {
            array = this.readArray(type, clazz, nodeMap);
        }
        else {
            array = new ObjectValue(clazz);
        }
        return array;
    }
    
    private Value readValue(final Type type, final Class clazz, final NodeMap nodeMap, final String s) throws Exception {
        Value value = this.readValue(type, clazz, nodeMap);
        if (s != null) {
            value = new Allocate(value, this, s);
        }
        return value;
    }
    
    public Value read(final Type type, final NodeMap nodeMap) throws Exception {
        final org.simpleframework.xml.stream.Node remove = nodeMap.remove(this.label);
        Class clazz2;
        final Class clazz = clazz2 = type.getType();
        if (clazz.isArray()) {
            clazz2 = clazz.getComponentType();
        }
        if (remove != null) {
            clazz2 = this.loader.load(remove.getValue());
        }
        return this.readInstance(type, clazz2, nodeMap);
    }
}
