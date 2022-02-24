// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.simpleframework.xml.Default;
import java.util.LinkedList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Namespace;
import java.lang.annotation.Annotation;
import java.util.List;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.DefaultType;

class DetailScanner implements Detail
{
    private DefaultType access;
    private NamespaceList declaration;
    private List<FieldDetail> fields;
    private Annotation[] labels;
    private List<MethodDetail> methods;
    private String name;
    private Namespace namespace;
    private Order order;
    private DefaultType override;
    private boolean required;
    private Root root;
    private boolean strict;
    private Class type;
    
    public DetailScanner(final Class clazz) {
        this(clazz, null);
    }
    
    public DetailScanner(final Class type, final DefaultType override) {
        this.methods = new LinkedList<MethodDetail>();
        this.fields = new LinkedList<FieldDetail>();
        this.labels = type.getDeclaredAnnotations();
        this.override = override;
        this.strict = true;
        this.scan(this.type = type);
    }
    
    private void access(final Annotation annotation) {
        if (annotation != null) {
            final Default default1 = (Default)annotation;
            this.required = default1.required();
            this.access = default1.value();
        }
    }
    
    private void extract(final Class clazz) {
        for (final Annotation annotation : this.labels) {
            if (annotation instanceof Namespace) {
                this.namespace(annotation);
            }
            if (annotation instanceof NamespaceList) {
                this.scope(annotation);
            }
            if (annotation instanceof Root) {
                this.root(annotation);
            }
            if (annotation instanceof Order) {
                this.order(annotation);
            }
            if (annotation instanceof Default) {
                this.access(annotation);
            }
        }
    }
    
    private void fields(final Class clazz) {
        final Field[] declaredFields = clazz.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            this.fields.add(new FieldDetail(declaredFields[i]));
        }
    }
    
    private boolean isEmpty(final String s) {
        return s.length() == 0;
    }
    
    private void methods(final Class clazz) {
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int length = declaredMethods.length, i = 0; i < length; ++i) {
            this.methods.add(new MethodDetail(declaredMethods[i]));
        }
    }
    
    private void namespace(final Annotation annotation) {
        if (annotation != null) {
            this.namespace = (Namespace)annotation;
        }
    }
    
    private void order(final Annotation annotation) {
        if (annotation != null) {
            this.order = (Order)annotation;
        }
    }
    
    private void root(final Annotation annotation) {
        if (annotation != null) {
            final Root root = (Root)annotation;
            final String simpleName = this.type.getSimpleName();
            if (root != null) {
                String name;
                if (this.isEmpty(name = root.name())) {
                    name = Reflector.getName(simpleName);
                }
                this.strict = root.strict();
                this.root = root;
                this.name = name;
            }
        }
    }
    
    private void scan(final Class clazz) {
        this.methods(clazz);
        this.fields(clazz);
        this.extract(clazz);
    }
    
    private void scope(final Annotation annotation) {
        if (annotation != null) {
            this.declaration = (NamespaceList)annotation;
        }
    }
    
    @Override
    public DefaultType getAccess() {
        DefaultType defaultType;
        if (this.override != null) {
            defaultType = this.override;
        }
        else {
            defaultType = this.access;
        }
        return defaultType;
    }
    
    @Override
    public Annotation[] getAnnotations() {
        return this.labels;
    }
    
    @Override
    public Constructor[] getConstructors() {
        return this.type.getDeclaredConstructors();
    }
    
    @Override
    public List<FieldDetail> getFields() {
        return this.fields;
    }
    
    @Override
    public List<MethodDetail> getMethods() {
        return this.methods;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Namespace getNamespace() {
        return this.namespace;
    }
    
    @Override
    public NamespaceList getNamespaceList() {
        return this.declaration;
    }
    
    @Override
    public Order getOrder() {
        return this.order;
    }
    
    @Override
    public DefaultType getOverride() {
        return this.override;
    }
    
    @Override
    public Root getRoot() {
        return this.root;
    }
    
    @Override
    public Class getSuper() {
        Class superclass;
        if ((superclass = this.type.getSuperclass()) == Object.class) {
            superclass = null;
        }
        return superclass;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public boolean isInstantiable() {
        boolean b = true;
        if (!Modifier.isStatic(this.type.getModifiers()) && this.type.isMemberClass()) {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }
    
    @Override
    public boolean isRequired() {
        return this.required;
    }
    
    @Override
    public boolean isStrict() {
        return this.strict;
    }
    
    @Override
    public String toString() {
        return this.type.toString();
    }
}
