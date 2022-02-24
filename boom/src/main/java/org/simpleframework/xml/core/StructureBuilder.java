// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Order;
import java.util.Iterator;
import java.lang.annotation.Annotation;

class StructureBuilder
{
    private ModelAssembler assembler;
    private LabelMap attributes;
    private ExpressionBuilder builder;
    private LabelMap elements;
    private Instantiator factory;
    private boolean primitive;
    private InstantiatorBuilder resolver;
    private Model root;
    private Scanner scanner;
    private Support support;
    private Label text;
    private LabelMap texts;
    private Label version;
    
    public StructureBuilder(final Scanner scanner, final Detail detail, final Support support) throws Exception {
        this.builder = new ExpressionBuilder(detail, support);
        this.assembler = new ModelAssembler(this.builder, detail, support);
        this.resolver = new InstantiatorBuilder(scanner, detail);
        this.root = new TreeModel(scanner, detail);
        this.attributes = new LabelMap(scanner);
        this.elements = new LabelMap(scanner);
        this.texts = new LabelMap(scanner);
        this.scanner = scanner;
        this.support = support;
    }
    
    private Model create(final Expression expression) throws Exception {
        final Model root = this.root;
        Expression path = expression;
        Model model = root;
        Model register;
        while (true) {
            register = model;
            if (model == null) {
                break;
            }
            final String prefix = path.getPrefix();
            final String first = path.getFirst();
            final int index = path.getIndex();
            register = model;
            if (first != null) {
                register = model.register(first, prefix, index);
            }
            if (!path.isPath()) {
                break;
            }
            path = path.getPath(1);
            model = register;
        }
        return register;
    }
    
    private boolean isAttribute(final String s) throws Exception {
        final Expression build = this.builder.build(s);
        final Model lookup = this.lookup(build);
        boolean b;
        if (lookup != null) {
            final String last = build.getLast();
            if (!build.isPath()) {
                b = lookup.isAttribute(s);
            }
            else {
                b = lookup.isAttribute(last);
            }
        }
        else {
            b = false;
        }
        return b;
    }
    
    private boolean isElement(final String s) throws Exception {
        boolean b = true;
        final Expression build = this.builder.build(s);
        final Model lookup = this.lookup(build);
        if (lookup == null) {
            return false;
        }
        final String last = build.getLast();
        final int index = build.getIndex();
        if (!lookup.isElement(last)) {
            if (!lookup.isModel(last)) {
                return false;
            }
            if (lookup.lookup(last, index).isEmpty()) {
                b = false;
            }
        }
        return b;
        b = false;
        return b;
    }
    
    private boolean isEmpty() {
        return this.text == null && this.root.isEmpty();
    }
    
    private Model lookup(final Expression expression) throws Exception {
        final Expression path = expression.getPath(0, 1);
        Model model;
        if (expression.isPath()) {
            model = this.root.lookup(path);
        }
        else {
            model = this.root;
        }
        return model;
    }
    
    private void process(final Contact contact, final Annotation annotation, final LabelMap labelMap) throws Exception {
        final Label label = this.support.getLabel(contact, annotation);
        final String path = label.getPath();
        final String name = label.getName();
        if (labelMap.get(path) != null) {
            throw new PersistenceException("Duplicate annotation of name '%s' on %s", new Object[] { name, contact });
        }
        this.process(contact, label, labelMap);
    }
    
    private void process(final Contact contact, final Label value, final LabelMap labelMap) throws Exception {
        final Expression expression = value.getExpression();
        final String path = value.getPath();
        Model model = this.root;
        if (!expression.isEmpty()) {
            model = this.register(expression);
        }
        this.resolver.register(value);
        model.register(value);
        labelMap.put(path, value);
    }
    
    private Model register(final Expression expression) throws Exception {
        final Model lookup = this.root.lookup(expression);
        Model create;
        if (lookup != null) {
            create = lookup;
        }
        else {
            create = this.create(expression);
        }
        return create;
    }
    
    private void text(final Contact contact, final Annotation annotation) throws Exception {
        final Label label = this.support.getLabel(contact, annotation);
        final Expression expression = label.getExpression();
        final String path = label.getPath();
        Model model = this.root;
        if (!expression.isEmpty()) {
            model = this.register(expression);
        }
        if (this.texts.get(path) != null) {
            throw new TextException("Multiple text annotations in %s", new Object[] { annotation });
        }
        this.resolver.register(label);
        model.register(label);
        this.texts.put(path, label);
    }
    
    private void union(final Contact contact, final Annotation annotation, final LabelMap labelMap) throws Exception {
        for (final Label label : this.support.getLabels(contact, annotation)) {
            final String path = label.getPath();
            final String name = label.getName();
            if (labelMap.get(path) != null) {
                throw new PersistenceException("Duplicate annotation of name '%s' on %s", new Object[] { name, label });
            }
            this.process(contact, label, labelMap);
        }
    }
    
    private void validateAttributes(final Class clazz, final Order order) throws Exception {
        if (order != null) {
            for (final String s : order.attributes()) {
                if (!this.isAttribute(s)) {
                    throw new AttributeException("Ordered attribute '%s' missing in %s", new Object[] { s, clazz });
                }
            }
        }
    }
    
    private void validateElements(final Class clazz, final Order order) throws Exception {
        if (order != null) {
            for (final String s : order.elements()) {
                if (!this.isElement(s)) {
                    throw new ElementException("Ordered element '%s' missing for %s", new Object[] { s, clazz });
                }
            }
        }
    }
    
    private void validateModel(final Class clazz) throws Exception {
        if (!this.root.isEmpty()) {
            this.root.validate(clazz);
        }
    }
    
    private void validateText(final Class clazz) throws Exception {
        final Label text = this.root.getText();
        if (text != null) {
            if (!text.isTextList()) {
                if (!this.elements.isEmpty()) {
                    throw new TextException("Elements used with %s in %s", new Object[] { text, clazz });
                }
                if (this.root.isComposite()) {
                    throw new TextException("Paths used with %s in %s", new Object[] { text, clazz });
                }
            }
        }
        else if (this.scanner.isEmpty()) {
            this.primitive = this.isEmpty();
        }
    }
    
    private void validateTextList(final Class clazz) throws Exception {
        final Label text = this.root.getText();
        if (text != null && text.isTextList()) {
            final Object key = text.getKey();
            for (final Label label : this.elements) {
                if (!label.getKey().equals(key)) {
                    throw new TextException("Elements used with %s in %s", new Object[] { text, clazz });
                }
                final Class type = label.getDependent().getType();
                if (type == String.class) {
                    throw new TextException("Illegal entry of %s with text annotations on %s in %s", new Object[] { type, text, clazz });
                }
            }
            if (this.root.isComposite()) {
                throw new TextException("Paths used with %s in %s", new Object[] { text, clazz });
            }
        }
    }
    
    private void validateUnions(final Class clazz) throws Exception {
        for (final Label label : this.elements) {
            final String[] paths = label.getPaths();
            final Contact contact = label.getContact();
            for (final String key : paths) {
                final Annotation annotation = contact.getAnnotation();
                final Label label2 = ((LinkedHashMap<K, Label>)this.elements).get(key);
                if (label.isInline() != label2.isInline()) {
                    throw new UnionException("Inline must be consistent in %s for %s", new Object[] { annotation, contact });
                }
                if (label.isRequired() != label2.isRequired()) {
                    throw new UnionException("Required must be consistent in %s for %s", new Object[] { annotation, contact });
                }
            }
        }
    }
    
    private void version(final Contact contact, final Annotation annotation) throws Exception {
        final Label label = this.support.getLabel(contact, annotation);
        if (this.version != null) {
            throw new AttributeException("Multiple version annotations in %s", new Object[] { annotation });
        }
        this.version = label;
    }
    
    public void assemble(final Class clazz) throws Exception {
        final Order order = this.scanner.getOrder();
        if (order != null) {
            this.assembler.assemble(this.root, order);
        }
    }
    
    public Structure build(final Class clazz) throws Exception {
        return new Structure(this.factory, this.root, this.version, this.text, this.primitive);
    }
    
    public void commit(final Class clazz) throws Exception {
        if (this.factory == null) {
            this.factory = this.resolver.build();
        }
    }
    
    public void process(final Contact contact, final Annotation annotation) throws Exception {
        if (annotation instanceof Attribute) {
            this.process(contact, annotation, this.attributes);
        }
        if (annotation instanceof ElementUnion) {
            this.union(contact, annotation, this.elements);
        }
        if (annotation instanceof ElementListUnion) {
            this.union(contact, annotation, this.elements);
        }
        if (annotation instanceof ElementMapUnion) {
            this.union(contact, annotation, this.elements);
        }
        if (annotation instanceof ElementList) {
            this.process(contact, annotation, this.elements);
        }
        if (annotation instanceof ElementArray) {
            this.process(contact, annotation, this.elements);
        }
        if (annotation instanceof ElementMap) {
            this.process(contact, annotation, this.elements);
        }
        if (annotation instanceof Element) {
            this.process(contact, annotation, this.elements);
        }
        if (annotation instanceof Version) {
            this.version(contact, annotation);
        }
        if (annotation instanceof Text) {
            this.text(contact, annotation);
        }
    }
    
    public void validate(final Class clazz) throws Exception {
        final Order order = this.scanner.getOrder();
        this.validateUnions(clazz);
        this.validateElements(clazz, order);
        this.validateAttributes(clazz, order);
        this.validateModel(clazz);
        this.validateText(clazz);
        this.validateTextList(clazz);
    }
}
