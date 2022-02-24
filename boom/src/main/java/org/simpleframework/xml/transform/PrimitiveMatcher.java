// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.transform;

class PrimitiveMatcher implements Matcher
{
    public PrimitiveMatcher() {
    }
    
    @Override
    public Transform match(final Class clazz) throws Exception {
        Object o;
        if (clazz == Integer.TYPE) {
            o = new IntegerTransform();
        }
        else if (clazz == Boolean.TYPE) {
            o = new BooleanTransform();
        }
        else if (clazz == Long.TYPE) {
            o = new LongTransform();
        }
        else if (clazz == Double.TYPE) {
            o = new DoubleTransform();
        }
        else if (clazz == Float.TYPE) {
            o = new FloatTransform();
        }
        else if (clazz == Short.TYPE) {
            o = new ShortTransform();
        }
        else if (clazz == Byte.TYPE) {
            o = new ByteTransform();
        }
        else if (clazz == Character.TYPE) {
            o = new CharacterTransform();
        }
        else {
            o = null;
        }
        return (Transform)o;
    }
}
