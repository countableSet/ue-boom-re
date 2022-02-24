// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.stream.Position;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import java.lang.reflect.Modifier;
import org.simpleframework.xml.strategy.Type;

abstract class Factory
{
    protected Context context;
    protected Class override;
    protected Support support;
    protected Type type;
    
    protected Factory(final Context context, final Type type) {
        this(context, type, null);
    }
    
    protected Factory(final Context context, final Type type, final Class override) {
        this.support = context.getSupport();
        this.override = override;
        this.context = context;
        this.type = type;
    }
    
    private Type getPrimitive(final Type type, final Class clazz) throws Exception {
        final Support support = this.support;
        final Class primitive = Support.getPrimitive(clazz);
        Type type2 = type;
        if (primitive != clazz) {
            type2 = new OverrideType(type, primitive);
        }
        return type2;
    }
    
    public static boolean isCompatible(final Class clazz, final Class clazz2) {
        Class componentType = clazz;
        if (clazz.isArray()) {
            componentType = clazz.getComponentType();
        }
        return componentType.isAssignableFrom(clazz2);
    }
    
    public static boolean isInstantiable(final Class clazz) {
        boolean b = false;
        final int modifiers = clazz.getModifiers();
        if (!Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
            b = true;
        }
        return b;
    }
    
    public Value getConversion(final InputNode inputNode) throws Exception {
        Value override;
        final Value value = override = this.context.getOverride(this.type, inputNode);
        if (value != null) {
            override = value;
            if (this.override != null) {
                final Class type = value.getType();
                override = value;
                if (!isCompatible(this.override, type)) {
                    override = new OverrideValue(value, this.override);
                }
            }
        }
        return override;
    }
    
    public Object getInstance() throws Exception {
        final Class type = this.getType();
        if (!isInstantiable(type)) {
            throw new InstantiationException("Type %s can not be instantiated", new Object[] { type });
        }
        return type.newInstance();
    }
    
    protected Value getOverride(final InputNode inputNode) throws Exception {
        final Value conversion = this.getConversion(inputNode);
        if (conversion != null) {
            final Position position = inputNode.getPosition();
            final Class type = conversion.getType();
            if (!isCompatible(this.getType(), type)) {
                throw new InstantiationException("Incompatible %s for %s at %s", new Object[] { type, this.type, position });
            }
        }
        return conversion;
    }
    
    public Class getType() {
        Class clazz;
        if (this.override != null) {
            clazz = this.override;
        }
        else {
            clazz = this.type.getType();
        }
        return clazz;
    }
    
    public boolean setOverride(final Type type, final Object o, final OutputNode outputNode) throws Exception {
        final Class type2 = type.getType();
        Type primitive = type;
        if (type2.isPrimitive()) {
            primitive = this.getPrimitive(type, type2);
        }
        return this.context.setOverride(primitive, o, outputNode);
    }
}
