// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Value;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import org.simpleframework.xml.strategy.Type;

class MapFactory extends Factory
{
    public MapFactory(final Context context, final Type type) {
        super(context, type);
    }
    
    private boolean isMap(final Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
    
    public Class getConversion(Class s) throws Exception {
        if (((Class)s).isAssignableFrom(HashMap.class)) {
            s = HashMap.class;
        }
        else {
            if (!((Class)s).isAssignableFrom(TreeMap.class)) {
                throw new InstantiationException("Cannot instantiate %s for %s", new Object[] { s, this.type });
            }
            s = TreeMap.class;
        }
        return (Class)s;
    }
    
    @Override
    public Object getInstance() throws Exception {
        final Class type = this.getType();
        Class<Object> conversion;
        if (!Factory.isInstantiable(conversion = (Class<Object>)type)) {
            conversion = (Class<Object>)this.getConversion(type);
        }
        if (!this.isMap(conversion)) {
            throw new InstantiationException("Invalid map %s for %s", new Object[] { type, this.type });
        }
        return conversion.newInstance();
    }
    
    public Instance getInstance(final Value value) throws Exception {
        Class clazz2;
        final Class clazz = clazz2 = value.getType();
        if (!Factory.isInstantiable(clazz)) {
            clazz2 = this.getConversion(clazz);
        }
        if (!this.isMap(clazz2)) {
            throw new InstantiationException("Invalid map %s for %s", new Object[] { clazz2, this.type });
        }
        return new ConversionInstance(this.context, value, clazz2);
    }
    
    public Instance getInstance(final InputNode inputNode) throws Exception {
        final Value override = this.getOverride(inputNode);
        final Class type = this.getType();
        Instance instance;
        if (override != null) {
            instance = this.getInstance(override);
        }
        else {
            Class conversion = type;
            if (!Factory.isInstantiable(type)) {
                conversion = this.getConversion(type);
            }
            if (!this.isMap(conversion)) {
                throw new InstantiationException("Invalid map %s for %s", new Object[] { conversion, this.type });
            }
            instance = this.context.getInstance(conversion);
        }
        return instance;
    }
}
