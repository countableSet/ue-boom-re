// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;
import java.util.List;
import java.lang.reflect.Method;
import org.simpleframework.xml.DefaultType;
import java.lang.annotation.Annotation;
import java.util.Iterator;

class MethodScanner extends ContactList
{
    private final Detail detail;
    private final MethodPartFactory factory;
    private final PartMap read;
    private final Support support;
    private final PartMap write;
    
    public MethodScanner(final Detail detail, final Support support) throws Exception {
        this.factory = new MethodPartFactory(detail, support);
        this.write = new PartMap();
        this.read = new PartMap();
        this.support = support;
        this.scan(this.detail = detail);
    }
    
    private void build() throws Exception {
        for (final String key : this.read) {
            final MethodPart methodPart = ((LinkedHashMap<K, MethodPart>)this.read).get(key);
            if (methodPart != null) {
                this.build(methodPart, key);
            }
        }
    }
    
    private void build(final MethodPart methodPart) throws Exception {
        ((ArrayList<MethodContact>)this).add(new MethodContact(methodPart));
    }
    
    private void build(final MethodPart methodPart, final String s) throws Exception {
        final MethodPart take = this.write.take(s);
        if (take != null) {
            this.build(methodPart, take);
        }
        else {
            this.build(methodPart);
        }
    }
    
    private void build(final MethodPart methodPart, final MethodPart methodPart2) throws Exception {
        final Annotation annotation = methodPart.getAnnotation();
        final String name = methodPart.getName();
        if (!methodPart2.getAnnotation().equals(annotation)) {
            throw new MethodException("Annotations do not match for '%s' in %s", new Object[] { name, this.detail });
        }
        final Class type = methodPart.getType();
        if (type != methodPart2.getType()) {
            throw new MethodException("Method types do not match for %s in %s", new Object[] { name, type });
        }
        ((ArrayList<MethodContact>)this).add(new MethodContact(methodPart, methodPart2));
    }
    
    private void extend(final Class clazz, final DefaultType defaultType) throws Exception {
        final Iterator<Contact> iterator = this.support.getMethods(clazz, defaultType).iterator();
        while (iterator.hasNext()) {
            this.process((MethodContact)iterator.next());
        }
    }
    
    private void extract(final Detail detail) throws Exception {
        for (final MethodDetail methodDetail : detail.getMethods()) {
            final Annotation[] annotations = methodDetail.getAnnotations();
            final Method method = methodDetail.getMethod();
            for (int length = annotations.length, i = 0; i < length; ++i) {
                this.scan(method, annotations[i], annotations);
            }
        }
    }
    
    private void extract(final Detail detail, final DefaultType defaultType) throws Exception {
        final List<MethodDetail> methods = detail.getMethods();
        if (defaultType == DefaultType.PROPERTY) {
            for (final MethodDetail methodDetail : methods) {
                final Annotation[] annotations = methodDetail.getAnnotations();
                final Method method = methodDetail.getMethod();
                if (this.factory.getType(method) != null) {
                    this.process(method, annotations);
                }
            }
        }
    }
    
    private void insert(final MethodPart methodPart, final PartMap partMap) {
        final String name = methodPart.getName();
        final MethodPart methodPart2 = ((HashMap<K, MethodPart>)partMap).remove(name);
        MethodPart value = methodPart;
        if (methodPart2 != null) {
            value = methodPart;
            if (this.isText(methodPart)) {
                value = methodPart2;
            }
        }
        partMap.put(name, value);
    }
    
    private boolean isText(final MethodPart methodPart) {
        return methodPart.getAnnotation() instanceof Text;
    }
    
    private void process(final Method method, final Annotation annotation, final Annotation[] array) throws Exception {
        final MethodPart instance = this.factory.getInstance(method, annotation, array);
        final MethodType methodType = instance.getMethodType();
        if (methodType == MethodType.GET) {
            this.process(instance, this.read);
        }
        if (methodType == MethodType.IS) {
            this.process(instance, this.read);
        }
        if (methodType == MethodType.SET) {
            this.process(instance, this.write);
        }
    }
    
    private void process(final Method method, final Annotation[] array) throws Exception {
        final MethodPart instance = this.factory.getInstance(method, array);
        final MethodType methodType = instance.getMethodType();
        if (methodType == MethodType.GET) {
            this.process(instance, this.read);
        }
        if (methodType == MethodType.IS) {
            this.process(instance, this.read);
        }
        if (methodType == MethodType.SET) {
            this.process(instance, this.write);
        }
    }
    
    private void process(final MethodContact methodContact) {
        final MethodPart read = methodContact.getRead();
        final MethodPart write = methodContact.getWrite();
        if (write != null) {
            this.insert(write, this.write);
        }
        this.insert(read, this.read);
    }
    
    private void process(final MethodPart value, final PartMap partMap) {
        final String name = value.getName();
        if (name != null) {
            partMap.put(name, value);
        }
    }
    
    private void remove(final Method method, final Annotation annotation, final Annotation[] array) throws Exception {
        final MethodPart instance = this.factory.getInstance(method, annotation, array);
        final MethodType methodType = instance.getMethodType();
        if (methodType == MethodType.GET) {
            this.remove(instance, this.read);
        }
        if (methodType == MethodType.IS) {
            this.remove(instance, this.read);
        }
        if (methodType == MethodType.SET) {
            this.remove(instance, this.write);
        }
    }
    
    private void remove(final MethodPart methodPart, final PartMap partMap) throws Exception {
        final String name = methodPart.getName();
        if (name != null) {
            partMap.remove(name);
        }
    }
    
    private void scan(final Method method, final Annotation annotation, final Annotation[] array) throws Exception {
        if (annotation instanceof Attribute) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof ElementUnion) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof ElementListUnion) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof ElementMapUnion) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof ElementList) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof ElementArray) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof ElementMap) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof Element) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof Version) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof Text) {
            this.process(method, annotation, array);
        }
        if (annotation instanceof Transient) {
            this.remove(method, annotation, array);
        }
    }
    
    private void scan(final Detail detail) throws Exception {
        final DefaultType override = detail.getOverride();
        final DefaultType access = detail.getAccess();
        final Class super1 = detail.getSuper();
        if (super1 != null) {
            this.extend(super1, override);
        }
        this.extract(detail, access);
        this.extract(detail);
        this.build();
        this.validate();
    }
    
    private void validate() throws Exception {
        for (final String key : this.write) {
            final MethodPart methodPart = ((LinkedHashMap<K, MethodPart>)this.write).get(key);
            if (methodPart != null) {
                this.validate(methodPart, key);
            }
        }
    }
    
    private void validate(final MethodPart methodPart, final String s) throws Exception {
        final MethodPart take = this.read.take(s);
        final Method method = methodPart.getMethod();
        if (take == null) {
            throw new MethodException("No matching get method for %s in %s", new Object[] { method, this.detail });
        }
    }
    
    private static class PartMap extends LinkedHashMap<String, MethodPart> implements Iterable<String>
    {
        @Override
        public Iterator<String> iterator() {
            return ((LinkedHashMap<String, V>)this).keySet().iterator();
        }
        
        public MethodPart take(final String key) {
            return ((HashMap<K, MethodPart>)this).remove(key);
        }
    }
}
