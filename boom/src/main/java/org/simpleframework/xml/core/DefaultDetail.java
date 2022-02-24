// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Namespace;
import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.DefaultType;

class DefaultDetail implements Detail
{
    private final DefaultType access;
    private final Detail detail;
    
    public DefaultDetail(final Detail detail, final DefaultType access) {
        this.detail = detail;
        this.access = access;
    }
    
    @Override
    public DefaultType getAccess() {
        return this.detail.getAccess();
    }
    
    @Override
    public Annotation[] getAnnotations() {
        return this.detail.getAnnotations();
    }
    
    @Override
    public Constructor[] getConstructors() {
        return this.detail.getConstructors();
    }
    
    @Override
    public List<FieldDetail> getFields() {
        return this.detail.getFields();
    }
    
    @Override
    public List<MethodDetail> getMethods() {
        return this.detail.getMethods();
    }
    
    @Override
    public String getName() {
        return this.detail.getName();
    }
    
    @Override
    public Namespace getNamespace() {
        return this.detail.getNamespace();
    }
    
    @Override
    public NamespaceList getNamespaceList() {
        return this.detail.getNamespaceList();
    }
    
    @Override
    public Order getOrder() {
        return this.detail.getOrder();
    }
    
    @Override
    public DefaultType getOverride() {
        return this.access;
    }
    
    @Override
    public Root getRoot() {
        return this.detail.getRoot();
    }
    
    @Override
    public Class getSuper() {
        return this.detail.getSuper();
    }
    
    @Override
    public Class getType() {
        return this.detail.getType();
    }
    
    @Override
    public boolean isInstantiable() {
        return this.detail.isInstantiable();
    }
    
    @Override
    public boolean isPrimitive() {
        return this.detail.isPrimitive();
    }
    
    @Override
    public boolean isRequired() {
        return this.detail.isRequired();
    }
    
    @Override
    public boolean isStrict() {
        return this.detail.isStrict();
    }
    
    @Override
    public String toString() {
        return this.detail.toString();
    }
}
