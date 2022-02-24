// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.strategy;

import java.lang.reflect.Array;
import org.simpleframework.xml.stream.NodeMap;
import java.util.IdentityHashMap;

class WriteGraph extends IdentityHashMap<Object, String>
{
    private final String label;
    private final String length;
    private final String mark;
    private final String refer;
    
    public WriteGraph(final Contract contract) {
        this.refer = contract.getReference();
        this.mark = contract.getIdentity();
        this.length = contract.getLength();
        this.label = contract.getLabel();
    }
    
    private Class writeArray(final Class clazz, final Object key, final NodeMap nodeMap) {
        final int length = Array.getLength(key);
        if (!this.containsKey(key)) {
            nodeMap.put(this.length, String.valueOf(length));
        }
        return clazz.getComponentType();
    }
    
    private boolean writeReference(final Object o, final NodeMap nodeMap) {
        final String s = ((IdentityHashMap<K, String>)this).get(o);
        final int size = this.size();
        boolean b;
        if (s != null) {
            nodeMap.put(this.refer, s);
            b = true;
        }
        else {
            final String value = String.valueOf(size);
            nodeMap.put(this.mark, value);
            this.put(o, value);
            b = false;
        }
        return b;
    }
    
    public boolean write(final Type type, final Object o, final NodeMap nodeMap) {
        final Class<?> class1 = o.getClass();
        final Class type2 = type.getType();
        Class<?> writeArray = class1;
        if (class1.isArray()) {
            writeArray = (Class<?>)this.writeArray(class1, o, nodeMap);
        }
        if (class1 != type2) {
            nodeMap.put(this.label, writeArray.getName());
        }
        return this.writeReference(o, nodeMap);
    }
}
