// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

class ReflectionUtils
{
    static Field getField(final Class clazz, final String name) {
        try {
            final Field declaredField = clazz.getDeclaredField(name);
            declaredField.setAccessible(true);
            return declaredField;
        }
        catch (NoSuchFieldException ex) {
            return null;
        }
    }
    
    static Method getMethod(final Class clazz, final String anObject) {
        for (final Method method : clazz.getMethods()) {
            if (method.getName().equals(anObject)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
    
    static Object getValue(final Field field, final Object obj) {
        try {
            return field.get(obj);
        }
        catch (IllegalAccessException ex) {
            return null;
        }
    }
    
    static void invokeMethod(final Object obj, final Method method, final Object... args) {
        if (method != null) {
            try {
                method.invoke(obj, args);
            }
            catch (IllegalAccessException ex) {}
            catch (InvocationTargetException obj) {
                goto Label_0016;
            }
        }
    }
    
    static void setValue(final Field field, final Object obj, final Object value) {
        try {
            field.set(obj, value);
        }
        catch (IllegalAccessException ex) {}
    }
}
