// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

final class Reflector
{
    private static Class getArrayClass(final Type type) {
        final Class class1 = getClass(((GenericArrayType)type).getGenericComponentType());
        Class<?> class2;
        if (class1 != null) {
            class2 = Array.newInstance(class1, 0).getClass();
        }
        else {
            class2 = null;
        }
        return class2;
    }
    
    private static Class getClass(final ParameterizedType parameterizedType) {
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class class1;
        if (actualTypeArguments.length > 0) {
            class1 = getClass(actualTypeArguments[0]);
        }
        else {
            class1 = null;
        }
        return class1;
    }
    
    private static Class getClass(final Type type) {
        Class genericClass;
        if (type instanceof Class) {
            genericClass = (Class)type;
        }
        else {
            genericClass = getGenericClass(type);
        }
        return genericClass;
    }
    
    private static Class[] getClasses(final ParameterizedType parameterizedType) {
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        final Class[] array = new Class[actualTypeArguments.length];
        for (int i = 0; i < actualTypeArguments.length; ++i) {
            array[i] = getClass(actualTypeArguments[i]);
        }
        return array;
    }
    
    public static Class getDependent(final Field field) {
        final ParameterizedType type = getType(field);
        Class<Object> class1;
        if (type != null) {
            class1 = (Class<Object>)getClass(type);
        }
        else {
            class1 = Object.class;
        }
        return class1;
    }
    
    public static Class[] getDependents(final Field field) {
        final ParameterizedType type = getType(field);
        Class[] classes;
        if (type != null) {
            classes = getClasses(type);
        }
        else {
            classes = new Class[0];
        }
        return classes;
    }
    
    private static Class getGenericClass(final Type type) {
        Class<Object> arrayClass;
        if (type instanceof GenericArrayType) {
            arrayClass = (Class<Object>)getArrayClass(type);
        }
        else {
            arrayClass = Object.class;
        }
        return arrayClass;
    }
    
    public static String getName(final String s) {
        String s2 = s;
        if (s.length() > 0) {
            final char[] charArray = s.toCharArray();
            final char c = charArray[0];
            if (!isAcronym(charArray)) {
                charArray[0] = toLowerCase(c);
            }
            s2 = new String(charArray);
        }
        return s2;
    }
    
    public static Class getParameterDependent(final Constructor constructor, final int n) {
        final ParameterizedType parameterType = getParameterType(constructor, n);
        Class<Object> class1;
        if (parameterType != null) {
            class1 = (Class<Object>)getClass(parameterType);
        }
        else {
            class1 = Object.class;
        }
        return class1;
    }
    
    public static Class getParameterDependent(final Method method, final int n) {
        final ParameterizedType parameterType = getParameterType(method, n);
        Class<Object> class1;
        if (parameterType != null) {
            class1 = (Class<Object>)getClass(parameterType);
        }
        else {
            class1 = Object.class;
        }
        return class1;
    }
    
    public static Class[] getParameterDependents(final Constructor constructor, final int n) {
        final ParameterizedType parameterType = getParameterType(constructor, n);
        Class[] classes;
        if (parameterType != null) {
            classes = getClasses(parameterType);
        }
        else {
            classes = new Class[0];
        }
        return classes;
    }
    
    public static Class[] getParameterDependents(final Method method, final int n) {
        final ParameterizedType parameterType = getParameterType(method, n);
        Class[] classes;
        if (parameterType != null) {
            classes = getClasses(parameterType);
        }
        else {
            classes = new Class[0];
        }
        return classes;
    }
    
    private static ParameterizedType getParameterType(final Constructor constructor, final int n) {
        final Type[] genericParameterTypes = constructor.getGenericParameterTypes();
        if (genericParameterTypes.length <= n) {
            return null;
        }
        final Type type = genericParameterTypes[n];
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        return (ParameterizedType)type;
        parameterizedType = null;
        return parameterizedType;
    }
    
    private static ParameterizedType getParameterType(final Method method, final int n) {
        final Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length <= n) {
            return null;
        }
        final Type type = genericParameterTypes[n];
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        return (ParameterizedType)type;
        parameterizedType = null;
        return parameterizedType;
    }
    
    public static Class getReturnDependent(final Method method) {
        final ParameterizedType returnType = getReturnType(method);
        Class<Object> class1;
        if (returnType != null) {
            class1 = (Class<Object>)getClass(returnType);
        }
        else {
            class1 = Object.class;
        }
        return class1;
    }
    
    public static Class[] getReturnDependents(final Method method) {
        final ParameterizedType returnType = getReturnType(method);
        Class[] classes;
        if (returnType != null) {
            classes = getClasses(returnType);
        }
        else {
            classes = new Class[0];
        }
        return classes;
    }
    
    private static ParameterizedType getReturnType(final Method method) {
        final Type genericReturnType = method.getGenericReturnType();
        ParameterizedType parameterizedType;
        if (genericReturnType instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType)genericReturnType;
        }
        else {
            parameterizedType = null;
        }
        return parameterizedType;
    }
    
    private static ParameterizedType getType(final Field field) {
        final Type genericType = field.getGenericType();
        ParameterizedType parameterizedType;
        if (genericType instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType)genericType;
        }
        else {
            parameterizedType = null;
        }
        return parameterizedType;
    }
    
    private static boolean isAcronym(final char[] array) {
        boolean upperCase = false;
        if (array.length >= 2 && isUpperCase(array[0])) {
            upperCase = isUpperCase(array[1]);
        }
        return upperCase;
    }
    
    private static boolean isUpperCase(final char ch) {
        return Character.isUpperCase(ch);
    }
    
    private static char toLowerCase(final char ch) {
        return Character.toLowerCase(ch);
    }
}
