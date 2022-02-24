// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Iterator;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Attribute;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

class SignatureScanner
{
    private final SignatureBuilder builder;
    private final Constructor constructor;
    private final ParameterFactory factory;
    private final ParameterMap registry;
    private final Class type;
    
    public SignatureScanner(final Constructor constructor, final ParameterMap registry, final Support support) throws Exception {
        this.builder = new SignatureBuilder(constructor);
        this.factory = new ParameterFactory(support);
        this.type = constructor.getDeclaringClass();
        this.constructor = constructor;
        this.registry = registry;
        this.scan(this.type);
    }
    
    private List<Parameter> create(final Annotation annotation, final int n) throws Exception {
        final Parameter instance = this.factory.getInstance(this.constructor, annotation, n);
        if (instance != null) {
            this.register(instance);
        }
        return Collections.singletonList(instance);
    }
    
    private Annotation[] extract(final Annotation obj) throws Exception {
        final Method[] declaredMethods = obj.annotationType().getDeclaredMethods();
        if (declaredMethods.length != 1) {
            throw new UnionException("Annotation '%s' is not a valid union for %s", new Object[] { obj, this.type });
        }
        return (Annotation[])declaredMethods[0].invoke(obj, new Object[0]);
    }
    
    private List<Parameter> process(final Annotation annotation, final int n) throws Exception {
        List<Parameter> list;
        if (annotation instanceof Attribute) {
            list = this.create(annotation, n);
        }
        else if (annotation instanceof Element) {
            list = this.create(annotation, n);
        }
        else if (annotation instanceof ElementList) {
            list = this.create(annotation, n);
        }
        else if (annotation instanceof ElementArray) {
            list = this.create(annotation, n);
        }
        else if (annotation instanceof ElementMap) {
            list = this.create(annotation, n);
        }
        else if (annotation instanceof ElementListUnion) {
            list = this.union(annotation, n);
        }
        else if (annotation instanceof ElementMapUnion) {
            list = this.union(annotation, n);
        }
        else if (annotation instanceof ElementUnion) {
            list = this.union(annotation, n);
        }
        else if (annotation instanceof Text) {
            list = this.create(annotation, n);
        }
        else {
            list = Collections.emptyList();
        }
        return list;
    }
    
    private void register(final Parameter parameter) throws Exception {
        final String path = parameter.getPath();
        final Object key = parameter.getKey();
        if (this.registry.containsKey(key)) {
            this.validate(parameter, key);
        }
        if (this.registry.containsKey(path)) {
            this.validate(parameter, path);
        }
        ((HashMap<String, Parameter>)this.registry).put(path, parameter);
        this.registry.put(key, parameter);
    }
    
    private void scan(final Class clazz) throws Exception {
        final Class[] parameterTypes = this.constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; ++i) {
            this.scan(parameterTypes[i], i);
        }
    }
    
    private void scan(final Class clazz, final int n) throws Exception {
        final Annotation[][] parameterAnnotations = this.constructor.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations[n].length; ++i) {
            final Iterator<Object> iterator = this.process(parameterAnnotations[n][i], n).iterator();
            while (iterator.hasNext()) {
                this.builder.insert(iterator.next(), n);
            }
        }
    }
    
    private List<Parameter> union(final Annotation annotation, final int n) throws Exception {
        final Signature signature = new Signature(this.constructor);
        final Annotation[] extract = this.extract(annotation);
        for (int length = extract.length, i = 0; i < length; ++i) {
            final Parameter instance = this.factory.getInstance(this.constructor, annotation, extract[i], n);
            final String path = instance.getPath();
            if (signature.contains(path)) {
                throw new UnionException("Annotation name '%s' used more than once in %s for %s", new Object[] { path, annotation, this.type });
            }
            signature.set(path, instance);
            this.register(instance);
        }
        return signature.getAll();
    }
    
    private void validate(final Parameter parameter, final Object key) throws Exception {
        final Parameter parameter2 = ((LinkedHashMap<K, Parameter>)this.registry).get(key);
        if (parameter.isText() != parameter2.isText()) {
            final Annotation annotation = parameter.getAnnotation();
            final Annotation annotation2 = parameter2.getAnnotation();
            final String path = parameter.getPath();
            if (!annotation.equals(annotation2)) {
                throw new ConstructorException("Annotations do not match for '%s' in %s", new Object[] { path, this.type });
            }
            if (parameter2.getType() != parameter.getType()) {
                throw new ConstructorException("Parameter types do not match for '%s' in %s", new Object[] { path, this.type });
            }
        }
    }
    
    public List<Signature> getSignatures() throws Exception {
        return this.builder.build();
    }
    
    public boolean isValid() {
        return this.builder.isValid();
    }
}
