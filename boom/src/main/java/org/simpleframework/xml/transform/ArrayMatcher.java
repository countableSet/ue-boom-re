// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class ArrayMatcher implements Matcher
{
    private final Matcher primary;
    
    public ArrayMatcher(final Matcher primary) {
        this.primary = primary;
    }
    
    private Transform matchArray(final Class clazz) throws Exception {
        final Transform match = this.primary.match(clazz);
        Transform transform;
        if (match == null) {
            transform = null;
        }
        else {
            transform = new ArrayTransform(match, clazz);
        }
        return transform;
    }
    
    @Override
    public Transform match(Class componentType) throws Exception {
        componentType = componentType.getComponentType();
        Transform matchArray;
        if (componentType == Character.TYPE) {
            matchArray = new CharacterArrayTransform(componentType);
        }
        else if (componentType == Character.class) {
            matchArray = new CharacterArrayTransform(componentType);
        }
        else if (componentType == String.class) {
            matchArray = new StringArrayTransform();
        }
        else {
            matchArray = this.matchArray(componentType);
        }
        return matchArray;
    }
}
