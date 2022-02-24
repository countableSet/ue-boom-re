// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class Comparer
{
    private static final String NAME = "name";
    private final String[] ignore;
    
    public Comparer() {
        this(new String[] { "name" });
    }
    
    public Comparer(final String... ignore) {
        this.ignore = ignore;
    }
    
    private boolean isIgnore(final Method method) {
        final String name = method.getName();
        if (this.ignore != null) {
            final String[] ignore = this.ignore;
            for (int length = ignore.length, i = 0; i < length; ++i) {
                if (name.equals(ignore[i])) {
                    return true;
                }
            }
            return false;
        }
        return false;
        return false;
    }
    
    public boolean equals(final Annotation obj, final Annotation obj2) throws Exception {
        final boolean b = false;
        final Class<? extends Annotation> annotationType = obj.annotationType();
        final Class<? extends Annotation> annotationType2 = obj2.annotationType();
        final Method[] declaredMethods = annotationType.getDeclaredMethods();
        boolean b2 = b;
        if (annotationType.equals(annotationType2)) {
            for (final Method method : declaredMethods) {
                if (!this.isIgnore(method) && !method.invoke(obj, new Object[0]).equals(method.invoke(obj2, new Object[0]))) {
                    b2 = b;
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
}
