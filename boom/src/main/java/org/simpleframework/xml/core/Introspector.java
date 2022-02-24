// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Path;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.Root;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;

class Introspector
{
    private final Contact contact;
    private final Format format;
    private final Label label;
    private final Annotation marker;
    
    public Introspector(final Contact contact, final Label label, final Format format) {
        this.marker = contact.getAnnotation();
        this.contact = contact;
        this.format = format;
        this.label = label;
    }
    
    private String getDefault() throws Exception {
        String s = this.label.getOverride();
        if (this.isEmpty(s)) {
            s = this.contact.getName();
        }
        return s;
    }
    
    private String getName(final Class clazz) throws Exception {
        final String root = this.getRoot(clazz);
        String name;
        if (root != null) {
            name = root;
        }
        else {
            name = Reflector.getName(clazz.getSimpleName());
        }
        return name;
    }
    
    private String getRoot(final Class clazz) {
        Class<?> superclass = (Class<?>)clazz;
        String s;
        while (true) {
            final Class<?> clazz2 = superclass;
            if (clazz2 == null) {
                s = null;
                break;
            }
            final String root = this.getRoot(clazz, clazz2);
            if (root != null) {
                s = root;
                break;
            }
            superclass = clazz2.getSuperclass();
        }
        return s;
    }
    
    private String getRoot(final Class<?> clazz, final Class<?> clazz2) {
        final String simpleName = clazz2.getSimpleName();
        final Root root = clazz2.getAnnotation(Root.class);
        String s;
        if (root != null) {
            s = root.name();
            if (this.isEmpty(s)) {
                s = Reflector.getName(simpleName);
            }
        }
        else {
            s = null;
        }
        return s;
    }
    
    public Contact getContact() {
        return this.contact;
    }
    
    public Type getDependent() throws Exception {
        return this.label.getDependent();
    }
    
    public String getEntry() throws Exception {
        Class clazz2;
        final Class clazz = clazz2 = this.getDependent().getType();
        if (clazz.isArray()) {
            clazz2 = clazz.getComponentType();
        }
        return this.getName(clazz2);
    }
    
    public Expression getExpression() throws Exception {
        final String path = this.getPath();
        Expression expression;
        if (path != null) {
            expression = new PathParser(path, this.contact, this.format);
        }
        else {
            expression = new EmptyExpression(this.format);
        }
        return expression;
    }
    
    public String getName() throws Exception {
        String s = this.label.getEntry();
        if (!this.label.isInline()) {
            s = this.getDefault();
        }
        return s;
    }
    
    public String getPath() throws Exception {
        final Path path = this.contact.getAnnotation(Path.class);
        String value;
        if (path == null) {
            value = null;
        }
        else {
            value = path.value();
        }
        return value;
    }
    
    public boolean isEmpty(final String s) {
        boolean b = true;
        if (s != null) {
            b = (s.length() == 0 && b);
        }
        return b;
    }
    
    @Override
    public String toString() {
        return String.format("%s on %s", this.marker, this.contact);
    }
}
