// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.lang.reflect.Constructor;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMapUnion;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Element;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.util.Cache;

class LabelExtractor
{
    private final Cache<LabelGroup> cache;
    private final Format format;
    
    public LabelExtractor(final Format format) {
        this.cache = new ConcurrentCache<LabelGroup>();
        this.format = format;
    }
    
    private Annotation[] getAnnotations(final Annotation obj) throws Exception {
        final Method[] declaredMethods = obj.annotationType().getDeclaredMethods();
        Annotation[] array;
        if (declaredMethods.length > 0) {
            array = (Annotation[])declaredMethods[0].invoke(obj, new Object[0]);
        }
        else {
            array = new Annotation[0];
        }
        return array;
    }
    
    private LabelBuilder getBuilder(final Annotation annotation) throws Exception {
        LabelBuilder labelBuilder;
        if (annotation instanceof Element) {
            labelBuilder = new LabelBuilder(ElementLabel.class, Element.class);
        }
        else if (annotation instanceof ElementList) {
            labelBuilder = new LabelBuilder(ElementListLabel.class, ElementList.class);
        }
        else if (annotation instanceof ElementArray) {
            labelBuilder = new LabelBuilder(ElementArrayLabel.class, ElementArray.class);
        }
        else if (annotation instanceof ElementMap) {
            labelBuilder = new LabelBuilder(ElementMapLabel.class, ElementMap.class);
        }
        else if (annotation instanceof ElementUnion) {
            labelBuilder = new LabelBuilder(ElementUnionLabel.class, ElementUnion.class, Element.class);
        }
        else if (annotation instanceof ElementListUnion) {
            labelBuilder = new LabelBuilder(ElementListUnionLabel.class, ElementListUnion.class, ElementList.class);
        }
        else if (annotation instanceof ElementMapUnion) {
            labelBuilder = new LabelBuilder(ElementMapUnionLabel.class, ElementMapUnion.class, ElementMap.class);
        }
        else if (annotation instanceof Attribute) {
            labelBuilder = new LabelBuilder(AttributeLabel.class, Attribute.class);
        }
        else if (annotation instanceof Version) {
            labelBuilder = new LabelBuilder(VersionLabel.class, Version.class);
        }
        else {
            if (!(annotation instanceof Text)) {
                throw new PersistenceException("Annotation %s not supported", new Object[] { annotation });
            }
            labelBuilder = new LabelBuilder(TextLabel.class, Text.class);
        }
        return labelBuilder;
    }
    
    private Constructor getConstructor(final Annotation annotation) throws Exception {
        final Constructor constructor = this.getBuilder(annotation).getConstructor();
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor;
    }
    
    private LabelGroup getGroup(final Contact contact, final Annotation annotation, final Object o) throws Exception {
        final LabelGroup labelGroup = this.cache.fetch(o);
        LabelGroup labelGroup2;
        if (labelGroup == null) {
            final LabelGroup labels = this.getLabels(contact, annotation);
            if ((labelGroup2 = labels) != null) {
                this.cache.cache(o, labels);
                labelGroup2 = labels;
            }
        }
        else {
            labelGroup2 = labelGroup;
        }
        return labelGroup2;
    }
    
    private Object getKey(final Contact contact, final Annotation annotation) {
        return new LabelKey(contact, annotation);
    }
    
    private Label getLabel(final Contact contact, final Annotation annotation, final Annotation annotation2) throws Exception {
        final Constructor constructor = this.getConstructor(annotation);
        Label label;
        if (annotation2 != null) {
            label = constructor.newInstance(contact, annotation, annotation2, this.format);
        }
        else {
            label = constructor.newInstance(contact, annotation, this.format);
        }
        return label;
    }
    
    private LabelGroup getLabels(final Contact contact, final Annotation annotation) throws Exception {
        LabelGroup labelGroup;
        if (annotation instanceof ElementUnion) {
            labelGroup = this.getUnion(contact, annotation);
        }
        else if (annotation instanceof ElementListUnion) {
            labelGroup = this.getUnion(contact, annotation);
        }
        else if (annotation instanceof ElementMapUnion) {
            labelGroup = this.getUnion(contact, annotation);
        }
        else {
            labelGroup = this.getSingle(contact, annotation);
        }
        return labelGroup;
    }
    
    private LabelGroup getSingle(final Contact contact, final Annotation annotation) throws Exception {
        Label label2;
        final Label label = label2 = this.getLabel(contact, annotation, null);
        if (label != null) {
            label2 = new CacheLabel(label);
        }
        return new LabelGroup(label2);
    }
    
    private LabelGroup getUnion(final Contact contact, final Annotation annotation) throws Exception {
        final Annotation[] annotations = this.getAnnotations(annotation);
        LabelGroup labelGroup;
        if (annotations.length > 0) {
            final LinkedList<Label> list = new LinkedList<Label>();
            for (int length = annotations.length, i = 0; i < length; ++i) {
                final Label label = this.getLabel(contact, annotation, annotations[i]);
                Label label2;
                if ((label2 = label) != null) {
                    label2 = new CacheLabel(label);
                }
                list.add(label2);
            }
            labelGroup = new LabelGroup(list);
        }
        else {
            labelGroup = null;
        }
        return labelGroup;
    }
    
    public Label getLabel(final Contact contact, final Annotation annotation) throws Exception {
        final LabelGroup group = this.getGroup(contact, annotation, this.getKey(contact, annotation));
        Label primary;
        if (group != null) {
            primary = group.getPrimary();
        }
        else {
            primary = null;
        }
        return primary;
    }
    
    public List<Label> getList(final Contact contact, final Annotation annotation) throws Exception {
        final LabelGroup group = this.getGroup(contact, annotation, this.getKey(contact, annotation));
        List<Label> list;
        if (group != null) {
            list = group.getList();
        }
        else {
            list = Collections.emptyList();
        }
        return list;
    }
    
    private static class LabelBuilder
    {
        private final Class entry;
        private final Class label;
        private final Class type;
        
        public LabelBuilder(final Class clazz, final Class clazz2) {
            this(clazz, clazz2, null);
        }
        
        public LabelBuilder(final Class type, final Class label, final Class entry) {
            this.entry = entry;
            this.label = label;
            this.type = type;
        }
        
        private Constructor getConstructor(final Class clazz) throws Exception {
            return this.type.getConstructor(Contact.class, clazz, Format.class);
        }
        
        private Constructor getConstructor(final Class clazz, final Class clazz2) throws Exception {
            return this.type.getConstructor(Contact.class, clazz, clazz2, Format.class);
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
    }
}
