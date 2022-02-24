// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.List;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.NamespaceList;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import org.simpleframework.xml.Namespace;
import java.lang.reflect.Method;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Order;

class ClassScanner
{
    private Function commit;
    private Function complete;
    private NamespaceDecorator decorator;
    private Order order;
    private Function persist;
    private Function replace;
    private Function resolve;
    private Root root;
    private ConstructorScanner scanner;
    private Support support;
    private Function validate;
    
    public ClassScanner(final Detail detail, final Support support) throws Exception {
        this.scanner = new ConstructorScanner(detail, support);
        this.decorator = new NamespaceDecorator();
        this.support = support;
        this.scan(detail);
    }
    
    private void commit(final Method method) {
        if (this.commit == null) {
            this.commit = this.getFunction(method);
        }
    }
    
    private void commit(final Detail detail) {
        final Namespace namespace = detail.getNamespace();
        if (namespace != null) {
            this.decorator.set(namespace);
        }
    }
    
    private void complete(final Method method) {
        if (this.complete == null) {
            this.complete = this.getFunction(method);
        }
    }
    
    private void definition(final Detail detail) throws Exception {
        if (this.root == null) {
            this.root = detail.getRoot();
        }
        if (this.order == null) {
            this.order = detail.getOrder();
        }
    }
    
    private Function getFunction(final Method method) {
        final boolean contextual = this.isContextual(method);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return new Function(method, contextual);
    }
    
    private boolean isContextual(final Method method) {
        boolean equals = false;
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            equals = Map.class.equals(parameterTypes[0]);
        }
        return equals;
    }
    
    private void method(final Detail detail) throws Exception {
        final Iterator<MethodDetail> iterator = detail.getMethods().iterator();
        while (iterator.hasNext()) {
            this.method(iterator.next());
        }
    }
    
    private void method(final MethodDetail methodDetail) {
        final Annotation[] annotations = methodDetail.getAnnotations();
        final Method method = methodDetail.getMethod();
        for (final Annotation annotation : annotations) {
            if (annotation instanceof Commit) {
                this.commit(method);
            }
            if (annotation instanceof Validate) {
                this.validate(method);
            }
            if (annotation instanceof Persist) {
                this.persist(method);
            }
            if (annotation instanceof Complete) {
                this.complete(method);
            }
            if (annotation instanceof Replace) {
                this.replace(method);
            }
            if (annotation instanceof Resolve) {
                this.resolve(method);
            }
        }
    }
    
    private void namespace(final Detail detail) throws Exception {
        final NamespaceList namespaceList = detail.getNamespaceList();
        final Namespace namespace = detail.getNamespace();
        if (namespace != null) {
            this.decorator.add(namespace);
        }
        if (namespaceList != null) {
            final Namespace[] value = namespaceList.value();
            for (int length = value.length, i = 0; i < length; ++i) {
                this.decorator.add(value[i]);
            }
        }
    }
    
    private void persist(final Method method) {
        if (this.persist == null) {
            this.persist = this.getFunction(method);
        }
    }
    
    private void replace(final Method method) {
        if (this.replace == null) {
            this.replace = this.getFunction(method);
        }
    }
    
    private void resolve(final Method method) {
        if (this.resolve == null) {
            this.resolve = this.getFunction(method);
        }
    }
    
    private void scan(final Detail detail) throws Exception {
        final DefaultType override = detail.getOverride();
        Detail detail2;
        for (Class clazz = detail.getType(); clazz != null; clazz = detail2.getSuper()) {
            detail2 = this.support.getDetail(clazz, override);
            this.namespace(detail2);
            this.method(detail2);
            this.definition(detail2);
        }
        this.commit(detail);
    }
    
    private void validate(final Method method) {
        if (this.validate == null) {
            this.validate = this.getFunction(method);
        }
    }
    
    public Function getCommit() {
        return this.commit;
    }
    
    public Function getComplete() {
        return this.complete;
    }
    
    public Decorator getDecorator() {
        return this.decorator;
    }
    
    public Order getOrder() {
        return this.order;
    }
    
    public ParameterMap getParameters() {
        return this.scanner.getParameters();
    }
    
    public Function getPersist() {
        return this.persist;
    }
    
    public Function getReplace() {
        return this.replace;
    }
    
    public Function getResolve() {
        return this.resolve;
    }
    
    public Root getRoot() {
        return this.root;
    }
    
    public Signature getSignature() {
        return this.scanner.getSignature();
    }
    
    public List<Signature> getSignatures() {
        return this.scanner.getSignatures();
    }
    
    public Function getValidate() {
        return this.validate;
    }
}
