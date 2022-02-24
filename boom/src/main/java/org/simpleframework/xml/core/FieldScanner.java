// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.HashMap;
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
import java.lang.reflect.Modifier;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import java.util.Collection;
import org.simpleframework.xml.DefaultType;
import java.util.Iterator;

class FieldScanner extends ContactList
{
    private final ContactMap done;
    private final AnnotationFactory factory;
    private final Support support;
    
    public FieldScanner(final Detail detail, final Support support) throws Exception {
        this.factory = new AnnotationFactory(detail, support);
        this.done = new ContactMap();
        this.support = support;
        this.scan(detail);
    }
    
    private void build() {
        final Iterator<Contact> iterator = this.done.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
    }
    
    private void extend(final Class clazz, final DefaultType defaultType) throws Exception {
        final ContactList fields = this.support.getFields(clazz, defaultType);
        if (fields != null) {
            this.addAll(fields);
        }
    }
    
    private void extract(final Detail detail) {
        for (final FieldDetail fieldDetail : detail.getFields()) {
            final Annotation[] annotations = fieldDetail.getAnnotations();
            final Field field = fieldDetail.getField();
            for (int length = annotations.length, i = 0; i < length; ++i) {
                this.scan(field, annotations[i], annotations);
            }
        }
    }
    
    private void extract(final Detail detail, final DefaultType defaultType) throws Exception {
        final List<FieldDetail> fields = detail.getFields();
        if (defaultType == DefaultType.FIELD) {
            for (final FieldDetail fieldDetail : fields) {
                final Annotation[] annotations = fieldDetail.getAnnotations();
                final Field field = fieldDetail.getField();
                final Class<?> type = field.getType();
                if (!this.isStatic(field) && !this.isTransient(field)) {
                    this.process(field, type, annotations);
                }
            }
        }
    }
    
    private void insert(final Object o, final Contact contact) {
        final Contact contact2 = ((HashMap<K, Contact>)this.done).remove(o);
        Contact value = contact;
        if (contact2 != null) {
            value = contact;
            if (this.isText(contact)) {
                value = contact2;
            }
        }
        this.done.put(o, value);
    }
    
    private boolean isStatic(final Field field) {
        return Modifier.isStatic(field.getModifiers());
    }
    
    private boolean isText(final Contact contact) {
        return contact.getAnnotation() instanceof Text;
    }
    
    private boolean isTransient(final Field field) {
        return Modifier.isTransient(field.getModifiers());
    }
    
    private void process(final Field field, final Class clazz, final Annotation[] array) throws Exception {
        final Annotation instance = this.factory.getInstance(clazz, Reflector.getDependents(field));
        if (instance != null) {
            this.process(field, instance, array);
        }
    }
    
    private void process(final Field field, final Annotation annotation, final Annotation[] array) {
        final FieldContact fieldContact = new FieldContact(field, annotation, array);
        final FieldKey fieldKey = new FieldKey(field);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        this.insert(fieldKey, fieldContact);
    }
    
    private void remove(final Field field, final Annotation annotation) {
        this.done.remove(new FieldKey(field));
    }
    
    private void scan(final Field field, final Annotation annotation, final Annotation[] array) {
        if (annotation instanceof Attribute) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof ElementUnion) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof ElementListUnion) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof ElementMapUnion) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof ElementList) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof ElementArray) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof ElementMap) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof Element) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof Version) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof Text) {
            this.process(field, annotation, array);
        }
        if (annotation instanceof Transient) {
            this.remove(field, annotation);
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
    }
    
    private static class FieldKey
    {
        private final String name;
        private final Class type;
        
        public FieldKey(final Field field) {
            this.type = field.getDeclaringClass();
            this.name = field.getName();
        }
        
        private boolean equals(final FieldKey fieldKey) {
            return fieldKey.type == this.type && fieldKey.name.equals(this.name);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof FieldKey && this.equals((FieldKey)o);
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }
}
