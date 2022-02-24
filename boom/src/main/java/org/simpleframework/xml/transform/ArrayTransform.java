// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.lang.reflect.Array;

class ArrayTransform implements Transform
{
    private final Transform delegate;
    private final Class entry;
    private final StringArrayTransform split;
    
    public ArrayTransform(final Transform delegate, final Class entry) {
        this.split = new StringArrayTransform();
        this.delegate = delegate;
        this.entry = entry;
    }
    
    private Object read(final String[] array, final int length) throws Exception {
        final Object instance = Array.newInstance(this.entry, length);
        for (int i = 0; i < length; ++i) {
            final Object read = this.delegate.read(array[i]);
            if (read != null) {
                Array.set(instance, i, read);
            }
        }
        return instance;
    }
    
    private String write(final Object o, final int n) throws Exception {
        final String[] array = new String[n];
        for (int i = 0; i < n; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                array[i] = this.delegate.write(value);
            }
        }
        return this.split.write(array);
    }
    
    @Override
    public Object read(final String s) throws Exception {
        final String[] read = this.split.read(s);
        return this.read(read, read.length);
    }
    
    @Override
    public String write(final Object o) throws Exception {
        return this.write(o, Array.getLength(o));
    }
}
