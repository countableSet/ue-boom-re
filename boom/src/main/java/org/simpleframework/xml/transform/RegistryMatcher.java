// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

public class RegistryMatcher implements Matcher
{
    private final Cache<Transform> transforms;
    private final Cache<Class> types;
    
    public RegistryMatcher() {
        this.transforms = new ConcurrentCache<Transform>();
        this.types = new ConcurrentCache<Class>();
    }
    
    private Transform create(final Class clazz) throws Exception {
        final Class clazz2 = this.types.fetch(clazz);
        Transform create;
        if (clazz2 != null) {
            create = this.create(clazz, clazz2);
        }
        else {
            create = null;
        }
        return create;
    }
    
    private Transform create(final Class clazz, final Class clazz2) throws Exception {
        final Transform transform = clazz2.newInstance();
        if (transform != null) {
            this.transforms.cache(clazz, transform);
        }
        return transform;
    }
    
    public void bind(final Class clazz, final Class clazz2) {
        this.types.cache(clazz, clazz2);
    }
    
    public void bind(final Class clazz, final Transform transform) {
        this.transforms.cache(clazz, transform);
    }
    
    @Override
    public Transform match(final Class clazz) throws Exception {
        Transform create;
        if ((create = this.transforms.fetch(clazz)) == null) {
            create = this.create(clazz);
        }
        return create;
    }
}
