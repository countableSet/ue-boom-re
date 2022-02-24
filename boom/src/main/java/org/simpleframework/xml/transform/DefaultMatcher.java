// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class DefaultMatcher implements Matcher
{
    private Matcher array;
    private Matcher matcher;
    private Matcher primitive;
    private Matcher stock;
    
    public DefaultMatcher(final Matcher matcher) {
        this.primitive = new PrimitiveMatcher();
        this.stock = new PackageMatcher();
        this.array = new ArrayMatcher(this);
        this.matcher = matcher;
    }
    
    private Transform matchType(final Class clazz) throws Exception {
        Transform transform;
        if (clazz.isArray()) {
            transform = this.array.match(clazz);
        }
        else if (clazz.isPrimitive()) {
            transform = this.primitive.match(clazz);
        }
        else {
            transform = this.stock.match(clazz);
        }
        return transform;
    }
    
    @Override
    public Transform match(final Class clazz) throws Exception {
        final Transform match = this.matcher.match(clazz);
        Transform matchType;
        if (match != null) {
            matchType = match;
        }
        else {
            matchType = this.matchType(clazz);
        }
        return matchType;
    }
}
