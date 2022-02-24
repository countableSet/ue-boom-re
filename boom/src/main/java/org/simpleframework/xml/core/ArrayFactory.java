// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.Position;
import org.simpleframework.xml.stream.InputNode;
import java.lang.reflect.Array;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.Type;

class ArrayFactory extends Factory
{
    public ArrayFactory(final Context context, final Type type) {
        super(context, type);
    }
    
    private Class getComponentType() throws Exception {
        final Class type = this.getType();
        if (!type.isArray()) {
            throw new InstantiationException("The %s not an array for %s", new Object[] { type, this.type });
        }
        return type.getComponentType();
    }
    
    private Instance getInstance(final Value value, final Class clazz) throws Exception {
        final Class componentType = this.getComponentType();
        if (!componentType.isAssignableFrom(clazz)) {
            throw new InstantiationException("Array of type %s cannot hold %s for %s", new Object[] { componentType, clazz, this.type });
        }
        return new ArrayInstance(value);
    }
    
    @Override
    public Object getInstance() throws Exception {
        final Class componentType = this.getComponentType();
        Object instance;
        if (componentType != null) {
            instance = Array.newInstance(componentType, 0);
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    public Instance getInstance(final InputNode inputNode) throws Exception {
        final Position position = inputNode.getPosition();
        final Value override = this.getOverride(inputNode);
        if (override == null) {
            throw new ElementException("Array length required for %s at %s", new Object[] { this.type, position });
        }
        return this.getInstance(override, override.getType());
    }
}
