// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import java.lang.reflect.Array;

class CharacterArrayTransform implements Transform
{
    private final Class entry;
    
    public CharacterArrayTransform(final Class entry) {
        this.entry = entry;
    }
    
    private Object read(final char[] array, final int length) throws Exception {
        final Object instance = Array.newInstance(this.entry, length);
        for (int i = 0; i < length; ++i) {
            Array.set(instance, i, array[i]);
        }
        return instance;
    }
    
    private String write(final Object o, final int capacity) throws Exception {
        final StringBuilder sb = new StringBuilder(capacity);
        for (int i = 0; i < capacity; ++i) {
            final Object value = Array.get(o, i);
            if (value != null) {
                sb.append(value);
            }
        }
        return sb.toString();
    }
    
    @Override
    public Object read(final String s) throws Exception {
        Object o = s.toCharArray();
        final int length = ((char[])o).length;
        if (this.entry != Character.TYPE) {
            o = this.read((char[])o, length);
        }
        return o;
    }
    
    @Override
    public String write(final Object o) throws Exception {
        final int length = Array.getLength(o);
        String write;
        if (this.entry == Character.TYPE) {
            write = new String((char[])o);
        }
        else {
            write = this.write(o, length);
        }
        return write;
    }
}
