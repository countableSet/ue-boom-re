// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class PrimitiveFactory extends Factory
{
    public PrimitiveFactory(final Context context, final Type type) {
        super(context, type);
    }
    
    public PrimitiveFactory(final Context context, final Type type, final Class clazz) {
        super(context, type, clazz);
    }
    
    public Object getInstance(final String s, final Class clazz) throws Exception {
        return this.support.read(s, clazz);
    }
    
    public Instance getInstance(final InputNode inputNode) throws Exception {
        final Value override = this.getOverride(inputNode);
        final Class type = this.getType();
        Instance instance;
        if (override == null) {
            instance = this.context.getInstance(type);
        }
        else {
            instance = new ObjectInstance(this.context, override);
        }
        return instance;
    }
    
    public String getText(final Object o) throws Exception {
        final Class<?> class1 = o.getClass();
        String s;
        if (class1.isEnum()) {
            s = this.support.write(o, class1);
        }
        else {
            s = this.support.write(o, class1);
        }
        return s;
    }
}
