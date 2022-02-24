// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import java.util.Map;
import java.lang.reflect.Array;
import org.simpleframework.xml.stream.Node;
import org.simpleframework.xml.stream.NodeMap;

public class TreeStrategy implements Strategy
{
    private final String label;
    private final String length;
    private final Loader loader;
    
    public TreeStrategy() {
        this("class", "length");
    }
    
    public TreeStrategy(final String label, final String length) {
        this.loader = new Loader();
        this.length = length;
        this.label = label;
    }
    
    private Value readArray(final Class clazz, final NodeMap nodeMap) throws Exception {
        final Node remove = nodeMap.remove(this.length);
        int int1 = 0;
        if (remove != null) {
            int1 = Integer.parseInt(remove.getValue());
        }
        return new ArrayValue(clazz, int1);
    }
    
    private Class readValue(final Type type, final NodeMap nodeMap) throws Exception {
        final Node remove = nodeMap.remove(this.label);
        Class clazz2;
        final Class clazz = clazz2 = type.getType();
        if (clazz.isArray()) {
            clazz2 = clazz.getComponentType();
        }
        if (remove != null) {
            clazz2 = this.loader.load(remove.getValue());
        }
        return clazz2;
    }
    
    private Class writeArray(final Class clazz, final Object o, final NodeMap nodeMap) {
        final int length = Array.getLength(o);
        if (this.length != null) {
            nodeMap.put(this.length, String.valueOf(length));
        }
        return clazz.getComponentType();
    }
    
    @Override
    public Value read(final Type type, final NodeMap nodeMap, final Map map) throws Exception {
        final Class value = this.readValue(type, nodeMap);
        final Class type2 = type.getType();
        Value array;
        if (type2.isArray()) {
            array = this.readArray(value, nodeMap);
        }
        else if (type2 != value) {
            array = new ObjectValue(value);
        }
        else {
            array = null;
        }
        return array;
    }
    
    @Override
    public boolean write(final Type type, final Object o, final NodeMap nodeMap, final Map map) {
        final Class<?> class1 = o.getClass();
        final Class type2 = type.getType();
        Class<?> writeArray = class1;
        if (class1.isArray()) {
            writeArray = (Class<?>)this.writeArray(type2, o, nodeMap);
        }
        if (class1 != type2) {
            nodeMap.put(this.label, writeArray.getName());
        }
        return false;
    }
}
