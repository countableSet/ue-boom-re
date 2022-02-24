// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

class LabelKey
{
    private final Class label;
    private final String name;
    private final Class owner;
    private final Class type;
    
    public LabelKey(final Contact contact, final Annotation annotation) {
        this.owner = contact.getDeclaringClass();
        this.label = annotation.annotationType();
        this.name = contact.getName();
        this.type = contact.getType();
    }
    
    private boolean equals(final LabelKey labelKey) {
        final boolean b = false;
        boolean equals;
        if (labelKey == this) {
            equals = true;
        }
        else {
            equals = b;
            if (labelKey.label == this.label) {
                equals = b;
                if (labelKey.owner == this.owner) {
                    equals = b;
                    if (labelKey.type == this.type) {
                        equals = labelKey.name.equals(this.name);
                    }
                }
            }
        }
        return equals;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof LabelKey && this.equals((LabelKey)o);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() ^ this.owner.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("key '%s' for %s", this.name, this.owner);
    }
}
