// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;

class ObjectFactory extends PrimitiveFactory
{
    public ObjectFactory(final Context context, final Type type, final Class clazz) {
        super(context, type, clazz);
    }
    
    @Override
    public Instance getInstance(final InputNode inputNode) throws Exception {
        final Value override = this.getOverride(inputNode);
        final Class type = this.getType();
        Instance instance;
        if (override == null) {
            if (!Factory.isInstantiable(type)) {
                throw new InstantiationException("Cannot instantiate %s for %s", new Object[] { type, this.type });
            }
            instance = this.context.getInstance(type);
        }
        else {
            instance = new ObjectInstance(this.context, override);
        }
        return instance;
    }
}
