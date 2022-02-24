// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Arrays;

class KeyBuilder
{
    private final Label label;
    
    public KeyBuilder(final Label label) {
        this.label = label;
    }
    
    private Object getKey(final KeyType keyType) throws Exception {
        final String key = this.getKey(this.label.getPaths());
        Object o;
        if (keyType == null) {
            o = key;
        }
        else {
            o = new Key(keyType, key);
        }
        return o;
    }
    
    private String getKey(final String[] a) throws Exception {
        final StringBuilder sb = new StringBuilder();
        if (a.length > 0) {
            Arrays.sort(a);
            for (int length = a.length, i = 0; i < length; ++i) {
                sb.append(a[i]);
                sb.append('>');
            }
        }
        return sb.toString();
    }
    
    public Object getKey() throws Exception {
        Object o;
        if (this.label.isAttribute()) {
            o = this.getKey(KeyType.ATTRIBUTE);
        }
        else {
            o = this.getKey(KeyType.ELEMENT);
        }
        return o;
    }
    
    private static class Key
    {
        private final KeyType type;
        private final String value;
        
        public Key(final KeyType type, final String value) throws Exception {
            this.value = value;
            this.type = type;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Key && this.equals((Key)o);
        }
        
        public boolean equals(final Key key) {
            return this.type == key.type && key.value.equals(this.value);
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
        
        @Override
        public String toString() {
            return this.value;
        }
    }
    
    private enum KeyType
    {
        ATTRIBUTE, 
        ELEMENT, 
        TEXT;
    }
}
