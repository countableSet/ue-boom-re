// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Constructor;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Element;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;

class ParameterFactory
{
    private final Format format;
    
    public ParameterFactory(final Support support) {
        this.format = support.getFormat();
    }
    
    private ParameterBuilder getBuilder(final Annotation annotation) throws Exception {
        ParameterBuilder parameterBuilder;
        if (annotation instanceof Element) {
            parameterBuilder = new ParameterBuilder(ElementParameter.class, Element.class);
        }
        else if (annotation instanceof ElementList) {
            parameterBuilder = new ParameterBuilder(ElementListParameter.class, ElementList.class);
        }
        else if (annotation instanceof ElementArray) {
            parameterBuilder = new ParameterBuilder(ElementArrayParameter.class, ElementArray.class);
        }
        else if (annotation instanceof ElementMapUnion) {
            parameterBuilder = new ParameterBuilder(ElementMapUnionParameter.class, ElementMapUnion.class, ElementMap.class);
        }
        else if (annotation instanceof ElementListUnion) {
            parameterBuilder = new ParameterBuilder(ElementListUnionParameter.class, ElementListUnion.class, ElementList.class);
        }
        else if (annotation instanceof ElementUnion) {
            parameterBuilder = new ParameterBuilder(ElementUnionParameter.class, ElementUnion.class, Element.class);
        }
        else if (annotation instanceof ElementMap) {
            parameterBuilder = new ParameterBuilder(ElementMapParameter.class, ElementMap.class);
        }
        else if (annotation instanceof Attribute) {
            parameterBuilder = new ParameterBuilder(AttributeParameter.class, Attribute.class);
        }
        else {
            if (!(annotation instanceof Text)) {
                throw new PersistenceException("Annotation %s not supported", new Object[] { annotation });
            }
            parameterBuilder = new ParameterBuilder(TextParameter.class, Text.class);
        }
        return parameterBuilder;
    }
    
    private Constructor getConstructor(final Annotation annotation) throws Exception {
        final Constructor constructor = this.getBuilder(annotation).getConstructor();
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor;
    }
    
    public Parameter getInstance(final Constructor constructor, final Annotation annotation, final int n) throws Exception {
        return this.getInstance(constructor, annotation, null, n);
    }
    
    public Parameter getInstance(final Constructor constructor, final Annotation annotation, final Annotation annotation2, final int n) throws Exception {
        final Constructor constructor2 = this.getConstructor(annotation);
        Parameter parameter;
        if (annotation2 != null) {
            parameter = constructor2.newInstance(constructor, annotation, annotation2, this.format, n);
        }
        else {
            parameter = constructor2.newInstance(constructor, annotation, this.format, n);
        }
        return parameter;
    }
    
    private static class ParameterBuilder
    {
        private final Class entry;
        private final Class label;
        private final Class type;
        
        public ParameterBuilder(final Class clazz, final Class clazz2) {
            this(clazz, clazz2, null);
        }
        
        public ParameterBuilder(final Class type, final Class label, final Class entry) {
            this.label = label;
            this.entry = entry;
            this.type = type;
        }
        
        private Constructor getConstructor(final Class... parameterTypes) throws Exception {
            return this.type.getConstructor((Class[])parameterTypes);
        }
        
        public Constructor getConstructor() throws Exception {
            Constructor constructor;
            if (this.entry != null) {
                constructor = this.getConstructor(this.label, this.entry);
            }
            else {
                constructor = this.getConstructor(this.label);
            }
            return constructor;
        }
        
        public Constructor getConstructor(final Class clazz) throws Exception {
            return this.getConstructor(Constructor.class, clazz, Format.class, Integer.TYPE);
        }
        
        public Constructor getConstructor(final Class clazz, final Class clazz2) throws Exception {
            return this.getConstructor(Constructor.class, clazz, clazz2, Format.class, Integer.TYPE);
        }
    }
}
