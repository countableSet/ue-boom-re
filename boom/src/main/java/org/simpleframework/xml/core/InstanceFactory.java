// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.util.ConcurrentCache;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.util.Cache;

class InstanceFactory
{
    private final Cache<Constructor> cache;
    
    public InstanceFactory() {
        this.cache = new ConcurrentCache<Constructor>();
    }
    
    public Instance getInstance(final Class clazz) {
        return new ClassInstance(clazz);
    }
    
    public Instance getInstance(final Value value) {
        return new ValueInstance(value);
    }
    
    protected Object getObject(final Class clazz) throws Exception {
        Constructor<Object> declaredConstructor;
        if ((declaredConstructor = this.cache.fetch(clazz)) == null) {
            declaredConstructor = clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            this.cache.cache(clazz, declaredConstructor);
        }
        return declaredConstructor.newInstance(new Object[0]);
    }
    
    private class ClassInstance implements Instance
    {
        private Class type;
        private Object value;
        
        public ClassInstance(final Class type) {
            this.type = type;
        }
        
        @Override
        public Object getInstance() throws Exception {
            if (this.value == null) {
                this.value = InstanceFactory.this.getObject(this.type);
            }
            return this.value;
        }
        
        @Override
        public Class getType() {
            return this.type;
        }
        
        @Override
        public boolean isReference() {
            return false;
        }
        
        @Override
        public Object setInstance(final Object value) throws Exception {
            return this.value = value;
        }
    }
    
    private class ValueInstance implements Instance
    {
        private final Class type;
        private final Value value;
        
        public ValueInstance(final Value value) {
            this.type = value.getType();
            this.value = value;
        }
        
        @Override
        public Object getInstance() throws Exception {
            Object o;
            if (this.value.isReference()) {
                o = this.value.getValue();
            }
            else {
                final Object value = o = InstanceFactory.this.getObject(this.type);
                if (this.value != null) {
                    this.value.setValue(value);
                    o = value;
                }
            }
            return o;
        }
        
        @Override
        public Class getType() {
            return this.type;
        }
        
        @Override
        public boolean isReference() {
            return this.value.isReference();
        }
        
        @Override
        public Object setInstance(final Object value) {
            if (this.value != null) {
                this.value.setValue(value);
            }
            return value;
        }
    }
}
