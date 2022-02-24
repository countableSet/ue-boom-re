// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

class AnnotationHandler implements InvocationHandler
{
    private static final String ATTRIBUTE = "attribute";
    private static final String CLASS = "annotationType";
    private static final String EQUAL = "equals";
    private static final String REQUIRED = "required";
    private static final String STRING = "toString";
    private final boolean attribute;
    private final Comparer comparer;
    private final boolean required;
    private final Class type;
    
    public AnnotationHandler(final Class clazz) {
        this(clazz, true);
    }
    
    public AnnotationHandler(final Class clazz, final boolean b) {
        this(clazz, b, false);
    }
    
    public AnnotationHandler(final Class type, final boolean required, final boolean attribute) {
        this.comparer = new Comparer();
        this.attribute = attribute;
        this.required = required;
        this.type = type;
    }
    
    private void attributes(final StringBuilder sb) {
        final Method[] declaredMethods = this.type.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; ++i) {
            final String name = declaredMethods[i].getName();
            final Object value = this.value(declaredMethods[i]);
            if (i > 0) {
                sb.append(',');
                sb.append(' ');
            }
            sb.append(name);
            sb.append('=');
            sb.append(value);
        }
        sb.append(')');
    }
    
    private boolean equals(final Object o, final Object[] array) throws Throwable {
        final Annotation annotation = (Annotation)o;
        final Annotation annotation2 = (Annotation)array[0];
        if (annotation.annotationType() != annotation2.annotationType()) {
            throw new PersistenceException("Annotation %s is not the same as %s", new Object[] { annotation, annotation2 });
        }
        return this.comparer.equals(annotation, annotation2);
    }
    
    private void name(final StringBuilder sb) {
        final String name = this.type.getName();
        if (name != null) {
            sb.append('@');
            sb.append(name);
            sb.append('(');
        }
    }
    
    private Object value(final Method method) {
        final String name = method.getName();
        Object o;
        if (name.equals("required")) {
            o = this.required;
        }
        else if (name.equals("attribute")) {
            o = this.attribute;
        }
        else {
            o = method.getDefaultValue();
        }
        return o;
    }
    
    @Override
    public Object invoke(Object o, final Method method, final Object[] array) throws Throwable {
        final String name = method.getName();
        if (name.equals("toString")) {
            o = this.toString();
        }
        else if (name.equals("equals")) {
            o = this.equals(o, array);
        }
        else if (name.equals("annotationType")) {
            o = this.type;
        }
        else if (name.equals("required")) {
            o = this.required;
        }
        else if (name.equals("attribute")) {
            o = this.attribute;
        }
        else {
            o = method.getDefaultValue();
        }
        return o;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.type != null) {
            this.name(sb);
            this.attributes(sb);
        }
        return sb.toString();
    }
}
