// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.ElementList;
import java.util.Collection;
import org.simpleframework.xml.ElementMap;
import java.util.Map;
import org.simpleframework.xml.stream.Verbosity;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Element;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;

class AnnotationFactory
{
    private final Format format;
    private final boolean required;
    
    public AnnotationFactory(final Detail detail, final Support support) {
        this.required = detail.isRequired();
        this.format = support.getFormat();
    }
    
    private ClassLoader getClassLoader() throws Exception {
        return AnnotationFactory.class.getClassLoader();
    }
    
    private Annotation getInstance(final Class clazz) throws Exception {
        final ClassLoader classLoader = this.getClassLoader();
        final Class componentType = clazz.getComponentType();
        Annotation annotation;
        if (clazz.isArray()) {
            if (this.isPrimitive(componentType)) {
                annotation = this.getInstance(classLoader, Element.class);
            }
            else {
                annotation = this.getInstance(classLoader, ElementArray.class);
            }
        }
        else if (this.isPrimitive(clazz) && this.isAttribute()) {
            annotation = this.getInstance(classLoader, Attribute.class);
        }
        else {
            annotation = this.getInstance(classLoader, Element.class);
        }
        return annotation;
    }
    
    private Annotation getInstance(final ClassLoader classLoader, final Class clazz) throws Exception {
        return this.getInstance(classLoader, clazz, false);
    }
    
    private Annotation getInstance(final ClassLoader loader, final Class clazz, final boolean b) throws Exception {
        return (Annotation)Proxy.newProxyInstance(loader, new Class[] { clazz }, new AnnotationHandler(clazz, this.required, b));
    }
    
    private boolean isAttribute() {
        final boolean b = false;
        final Verbosity verbosity = this.format.getVerbosity();
        boolean b2 = b;
        if (verbosity != null) {
            b2 = b;
            if (verbosity == Verbosity.LOW) {
                b2 = true;
            }
        }
        return b2;
    }
    
    private boolean isPrimitive(final Class clazz) {
        final boolean b = true;
        boolean primitive;
        if (Number.class.isAssignableFrom(clazz)) {
            primitive = b;
        }
        else {
            primitive = b;
            if (clazz != Boolean.class) {
                primitive = b;
                if (clazz != Character.class) {
                    primitive = clazz.isPrimitive();
                }
            }
        }
        return primitive;
    }
    
    private boolean isPrimitiveKey(Class[] clazz) {
        boolean primitive = true;
        if (clazz != null && clazz.length > 0) {
            final Class superclass = clazz[0].getSuperclass();
            clazz = clazz[0];
            if (superclass == null || (!superclass.isEnum() && !clazz.isEnum())) {
                primitive = this.isPrimitive(clazz);
            }
        }
        else {
            primitive = false;
        }
        return primitive;
    }
    
    public Annotation getInstance(final Class clazz, final Class[] array) throws Exception {
        final ClassLoader classLoader = this.getClassLoader();
        Annotation annotation;
        if (Map.class.isAssignableFrom(clazz)) {
            if (this.isPrimitiveKey(array) && this.isAttribute()) {
                annotation = this.getInstance(classLoader, ElementMap.class, true);
            }
            else {
                annotation = this.getInstance(classLoader, ElementMap.class);
            }
        }
        else if (Collection.class.isAssignableFrom(clazz)) {
            annotation = this.getInstance(classLoader, ElementList.class);
        }
        else {
            annotation = this.getInstance(clazz);
        }
        return annotation;
    }
}
