// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.util.Cache;

public class Transformer
{
    private final Cache<Transform> cache;
    private final Cache<Object> error;
    private final Matcher matcher;
    
    public Transformer(final Matcher matcher) {
        this.cache = new ConcurrentCache<Transform>();
        this.error = new ConcurrentCache<Object>();
        this.matcher = new DefaultMatcher(matcher);
    }
    
    private Transform lookup(final Class clazz) throws Exception {
        Transform match;
        if (!this.error.contains(clazz)) {
            final Transform transform = this.cache.fetch(clazz);
            if (transform != null) {
                match = transform;
            }
            else {
                match = this.match(clazz);
            }
        }
        else {
            match = null;
        }
        return match;
    }
    
    private Transform match(final Class clazz) throws Exception {
        final Transform match = this.matcher.match(clazz);
        if (match != null) {
            this.cache.cache(clazz, match);
        }
        else {
            this.error.cache(clazz, this);
        }
        return match;
    }
    
    public Object read(final String s, final Class clazz) throws Exception {
        final Transform lookup = this.lookup(clazz);
        if (lookup == null) {
            throw new TransformException("Transform of %s not supported", new Object[] { clazz });
        }
        return lookup.read(s);
    }
    
    public boolean valid(final Class clazz) throws Exception {
        return this.lookup(clazz) != null;
    }
    
    public String write(final Object o, final Class clazz) throws Exception {
        final Transform lookup = this.lookup(clazz);
        if (lookup == null) {
            throw new TransformException("Transform of %s not supported", new Object[] { clazz });
        }
        return lookup.write(o);
    }
}
