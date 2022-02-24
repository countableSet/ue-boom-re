// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.util.ConcurrentCache;

class ScannerBuilder extends ConcurrentCache<Scanner>
{
    public ScannerBuilder() {
    }
    
    public Scanner build(final Class<?> clazz) {
        Scanner value;
        if ((value = ((ConcurrentHashMap<K, Scanner>)this).get(clazz)) == null) {
            value = new Entry(clazz);
            ((ConcurrentHashMap<Class<?>, T>)this).put(clazz, (T)value);
        }
        return value;
    }
    
    private static class Entry extends ConcurrentCache<Annotation> implements Scanner
    {
        private final Class root;
        
        public Entry(final Class root) {
            this.root = root;
        }
        
        private <T extends Annotation> T find(final Class<T> annotationClass) {
            for (Class clazz = this.root; clazz != null; clazz = clazz.getSuperclass()) {
                final Annotation annotation = clazz.getAnnotation(annotationClass);
                if (annotation != null) {
                    final Annotation annotation2 = annotation;
                    return (T)annotation2;
                }
            }
            final Annotation annotation2 = null;
            return (T)annotation2;
        }
        
        @Override
        public <T extends Annotation> T scan(final Class<T> clazz) {
            if (!this.contains(clazz)) {
                final Annotation find = this.find((Class<Annotation>)clazz);
                if (clazz != null && find != null) {
                    ((ConcurrentHashMap<Class<Annotation>, T>)this).put(clazz, (T)find);
                }
            }
            return (T)this.get(clazz);
        }
    }
}
