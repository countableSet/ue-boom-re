// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

class InstantiatorBuilder
{
    private LabelMap attributes;
    private Comparer comparer;
    private Detail detail;
    private LabelMap elements;
    private Instantiator factory;
    private List<Creator> options;
    private Scanner scanner;
    private LabelMap texts;
    
    public InstantiatorBuilder(final Scanner scanner, final Detail detail) {
        this.options = new ArrayList<Creator>();
        this.comparer = new Comparer();
        this.attributes = new LabelMap();
        this.elements = new LabelMap();
        this.texts = new LabelMap();
        this.scanner = scanner;
        this.detail = detail;
    }
    
    private Instantiator build(final Detail detail) throws Exception {
        if (this.factory == null) {
            this.factory = this.create(detail);
        }
        return this.factory;
    }
    
    private boolean contains(final String[] array, final String anObject) throws Exception {
        final boolean b = true;
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final String s = array[i];
            boolean b2;
            if (s == anObject) {
                b2 = b;
            }
            else {
                b2 = b;
                if (!s.equals(anObject)) {
                    ++i;
                    continue;
                }
            }
            return b2;
        }
        return false;
    }
    
    private Creator create(final Signature signature) {
        final SignatureCreator signatureCreator = new SignatureCreator(signature);
        if (signature != null) {
            this.options.add(signatureCreator);
        }
        return signatureCreator;
    }
    
    private Instantiator create(final Detail detail) throws Exception {
        final Signature signature = this.scanner.getSignature();
        final ParameterMap parameters = this.scanner.getParameters();
        Creator creator = null;
        if (signature != null) {
            creator = new SignatureCreator(signature);
        }
        return new ClassInstantiator(this.options, creator, parameters, detail);
    }
    
    private Parameter create(final Parameter parameter) throws Exception {
        final Label resolve = this.resolve(parameter);
        CacheParameter cacheParameter;
        if (resolve != null) {
            cacheParameter = new CacheParameter(parameter, resolve);
        }
        else {
            cacheParameter = null;
        }
        return cacheParameter;
    }
    
    private void populate(final Detail detail) throws Exception {
        final Iterator<Signature> iterator = this.scanner.getSignatures().iterator();
        while (iterator.hasNext()) {
            this.populate(iterator.next());
        }
    }
    
    private void populate(final Signature signature) throws Exception {
        final Signature signature2 = new Signature(signature);
        final Iterator<Parameter> iterator = signature.iterator();
        while (iterator.hasNext()) {
            final Parameter create = this.create(iterator.next());
            if (create != null) {
                signature2.add(create);
            }
        }
        this.create(signature2);
    }
    
    private void register(final Label label, final LabelMap labelMap) throws Exception {
        final String name = label.getName();
        final String path = label.getPath();
        if (labelMap.containsKey(name)) {
            if (!labelMap.get(name).getPath().equals(name)) {
                labelMap.remove(name);
            }
        }
        else {
            labelMap.put(name, label);
        }
        labelMap.put(path, label);
    }
    
    private Label resolve(final Parameter parameter) throws Exception {
        Label label;
        if (parameter.isAttribute()) {
            label = this.resolve(parameter, this.attributes);
        }
        else if (parameter.isText()) {
            label = this.resolve(parameter, this.texts);
        }
        else {
            label = this.resolve(parameter, this.elements);
        }
        return label;
    }
    
    private Label resolve(final Parameter parameter, final LabelMap labelMap) throws Exception {
        final String name = parameter.getName();
        Label label = ((LinkedHashMap<K, Label>)labelMap).get(parameter.getPath());
        if (label == null) {
            label = ((LinkedHashMap<K, Label>)labelMap).get(name);
        }
        return label;
    }
    
    private void validate(final Detail detail) throws Exception {
        for (final Parameter parameter : this.scanner.getParameters().getAll()) {
            final Label resolve = this.resolve(parameter);
            final String path = parameter.getPath();
            if (resolve == null) {
                throw new ConstructorException("Parameter '%s' does not have a match in %s", new Object[] { path, detail });
            }
            this.validateParameter(resolve, parameter);
        }
        this.validateConstructors();
    }
    
    private void validateAnnotations(final Label label, final Parameter parameter) throws Exception {
        final Annotation annotation = label.getAnnotation();
        final Annotation annotation2 = parameter.getAnnotation();
        final String name = parameter.getName();
        if (!this.comparer.equals(annotation, annotation2)) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            final Class<? extends Annotation> annotationType2 = annotation2.annotationType();
            if (!annotationType.equals(annotationType2)) {
                throw new ConstructorException("Annotation %s does not match %s for '%s' in %s", new Object[] { annotationType2, annotationType, name, parameter });
            }
        }
    }
    
    private void validateConstructor(final Label label, final List<Creator> list) throws Exception {
        final Iterator<Creator> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Signature signature = iterator.next().getSignature();
            final Contact contact = label.getContact();
            final Object key = label.getKey();
            if (contact.isReadOnly() && signature.get(key) == null) {
                iterator.remove();
            }
        }
    }
    
    private void validateConstructors() throws Exception {
        final List<Creator> creators = this.factory.getCreators();
        if (this.factory.isDefault()) {
            this.validateConstructors(this.elements);
            this.validateConstructors(this.attributes);
        }
        if (!creators.isEmpty()) {
            this.validateConstructors(this.elements, creators);
            this.validateConstructors(this.attributes, creators);
        }
    }
    
    private void validateConstructors(final LabelMap labelMap) throws Exception {
        for (final Label label : labelMap) {
            if (label != null && label.getContact().isReadOnly()) {
                throw new ConstructorException("Default constructor can not accept read only %s in %s", new Object[] { label, this.detail });
            }
        }
    }
    
    private void validateConstructors(final LabelMap labelMap, final List<Creator> list) throws Exception {
        for (final Label label : labelMap) {
            if (label != null) {
                this.validateConstructor(label, list);
            }
        }
        if (list.isEmpty()) {
            throw new ConstructorException("No constructor accepts all read only values in %s", new Object[] { this.detail });
        }
    }
    
    private void validateNames(final Label label, final Parameter parameter) throws Exception {
        final String[] names = label.getNames();
        final String name = parameter.getName();
        if (!this.contains(names, name)) {
            final String name2 = label.getName();
            if (name != name2) {
                if (name == null || name2 == null) {
                    throw new ConstructorException("Annotation does not match %s for '%s' in %s", new Object[] { label, name, parameter });
                }
                if (!name.equals(name2)) {
                    throw new ConstructorException("Annotation does not match %s for '%s' in %s", new Object[] { label, name, parameter });
                }
            }
        }
    }
    
    private void validateParameter(final Label label, final Parameter parameter) throws Exception {
        final Contact contact = label.getContact();
        final String name = parameter.getName();
        if (!Support.isAssignable(parameter.getType(), contact.getType())) {
            throw new ConstructorException("Type is not compatible with %s for '%s' in %s", new Object[] { label, name, parameter });
        }
        this.validateNames(label, parameter);
        this.validateAnnotations(label, parameter);
    }
    
    public Instantiator build() throws Exception {
        if (this.factory == null) {
            this.populate(this.detail);
            this.build(this.detail);
            this.validate(this.detail);
        }
        return this.factory;
    }
    
    public void register(final Label label) throws Exception {
        if (label.isAttribute()) {
            this.register(label, this.attributes);
        }
        else if (label.isText()) {
            this.register(label, this.texts);
        }
        else {
            this.register(label, this.elements);
        }
    }
}
