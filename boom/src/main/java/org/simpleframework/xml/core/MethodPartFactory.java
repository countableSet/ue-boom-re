// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class MethodPartFactory
{
    private final AnnotationFactory factory;
    
    public MethodPartFactory(final Detail detail, final Support support) {
        this.factory = new AnnotationFactory(detail, support);
    }
    
    private Annotation getAnnotation(final Method method) throws Exception {
        final Class[] dependents = this.getDependents(method);
        final Class type = this.getType(method);
        Annotation instance;
        if (type != null) {
            instance = this.factory.getInstance(type, dependents);
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    private Class[] getDependents(final Method method) throws Exception {
        final MethodType methodType = this.getMethodType(method);
        Class[] array;
        if (methodType == MethodType.SET) {
            array = Reflector.getParameterDependents(method, 0);
        }
        else if (methodType == MethodType.GET) {
            array = Reflector.getReturnDependents(method);
        }
        else if (methodType == MethodType.IS) {
            array = Reflector.getReturnDependents(method);
        }
        else {
            array = null;
        }
        return array;
    }
    
    private MethodType getMethodType(final Method method) {
        final String name = method.getName();
        MethodType methodType;
        if (name.startsWith("get")) {
            methodType = MethodType.GET;
        }
        else if (name.startsWith("is")) {
            methodType = MethodType.IS;
        }
        else if (name.startsWith("set")) {
            methodType = MethodType.SET;
        }
        else {
            methodType = MethodType.NONE;
        }
        return methodType;
    }
    
    private MethodName getName(final Method method, final Annotation annotation) throws Exception {
        final MethodType methodType = this.getMethodType(method);
        MethodName methodName;
        if (methodType == MethodType.GET) {
            methodName = this.getRead(method, methodType);
        }
        else if (methodType == MethodType.IS) {
            methodName = this.getRead(method, methodType);
        }
        else {
            if (methodType != MethodType.SET) {
                throw new MethodException("Annotation %s must mark a set or get method", new Object[] { annotation });
            }
            methodName = this.getWrite(method, methodType);
        }
        return methodName;
    }
    
    private Class getParameterType(final Method method) throws Exception {
        Class<?> clazz;
        if (method.getParameterTypes().length == 1) {
            clazz = method.getParameterTypes()[0];
        }
        else {
            clazz = null;
        }
        return clazz;
    }
    
    private MethodName getRead(final Method method, final MethodType methodType) throws Exception {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String name = method.getName();
        if (parameterTypes.length != 0) {
            throw new MethodException("Get method %s is not a valid property", new Object[] { method });
        }
        final String typeName = this.getTypeName(name, methodType);
        if (typeName == null) {
            throw new MethodException("Could not get name for %s", new Object[] { method });
        }
        return new MethodName(method, methodType, typeName);
    }
    
    private Class getReturnType(final Method method) throws Exception {
        Class<?> returnType;
        if (method.getParameterTypes().length == 0) {
            returnType = method.getReturnType();
        }
        else {
            returnType = null;
        }
        return returnType;
    }
    
    private String getTypeName(final String s, final MethodType methodType) {
        final int prefix = methodType.getPrefix();
        final int length = s.length();
        String substring = s;
        if (length > prefix) {
            substring = s.substring(prefix, length);
        }
        return Reflector.getName(substring);
    }
    
    private MethodName getWrite(final Method method, final MethodType methodType) throws Exception {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String name = method.getName();
        if (parameterTypes.length != 1) {
            throw new MethodException("Set method %s is not a valid property", new Object[] { method });
        }
        final String typeName = this.getTypeName(name, methodType);
        if (typeName == null) {
            throw new MethodException("Could not get name for %s", new Object[] { method });
        }
        return new MethodName(method, methodType, typeName);
    }
    
    public MethodPart getInstance(final Method method, final Annotation annotation, final Annotation[] array) throws Exception {
        final MethodName name = this.getName(method, annotation);
        MethodPart methodPart;
        if (name.getType() == MethodType.SET) {
            methodPart = new SetPart(name, annotation, array);
        }
        else {
            methodPart = new GetPart(name, annotation, array);
        }
        return methodPart;
    }
    
    public MethodPart getInstance(final Method method, final Annotation[] array) throws Exception {
        final Annotation annotation = this.getAnnotation(method);
        MethodPart instance;
        if (annotation != null) {
            instance = this.getInstance(method, annotation, array);
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    public Class getType(final Method method) throws Exception {
        final MethodType methodType = this.getMethodType(method);
        Class clazz;
        if (methodType == MethodType.SET) {
            clazz = this.getParameterType(method);
        }
        else if (methodType == MethodType.GET) {
            clazz = this.getReturnType(method);
        }
        else if (methodType == MethodType.IS) {
            clazz = this.getReturnType(method);
        }
        else {
            clazz = null;
        }
        return clazz;
    }
}
