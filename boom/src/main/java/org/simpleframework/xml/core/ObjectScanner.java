// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.List;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.Order;
import java.lang.annotation.Annotation;
import java.util.Iterator;

class ObjectScanner implements Scanner
{
    private StructureBuilder builder;
    private Detail detail;
    private ClassScanner scanner;
    private Structure structure;
    private Support support;
    
    public ObjectScanner(final Detail detail, final Support support) throws Exception {
        this.scanner = new ClassScanner(detail, support);
        this.builder = new StructureBuilder(this, detail, support);
        this.support = support;
        this.scan(this.detail = detail);
    }
    
    private void commit(final Detail detail) throws Exception {
        final Class type = detail.getType();
        if (this.structure == null) {
            this.structure = this.builder.build(type);
        }
        this.builder = null;
    }
    
    private void field(final Detail detail) throws Exception {
        for (final Contact contact : this.support.getFields(detail.getType(), detail.getOverride())) {
            final Annotation annotation = contact.getAnnotation();
            if (annotation != null) {
                this.builder.process(contact, annotation);
            }
        }
    }
    
    private void method(final Detail detail) throws Exception {
        for (final Contact contact : this.support.getMethods(detail.getType(), detail.getOverride())) {
            final Annotation annotation = contact.getAnnotation();
            if (annotation != null) {
                this.builder.process(contact, annotation);
            }
        }
    }
    
    private void order(final Detail detail) throws Exception {
        this.builder.assemble(detail.getType());
    }
    
    private void scan(final Detail detail) throws Exception {
        this.order(detail);
        this.field(detail);
        this.method(detail);
        this.validate(detail);
        this.commit(detail);
    }
    
    private void validate(final Detail detail) throws Exception {
        final Class type = detail.getType();
        this.builder.commit(type);
        this.builder.validate(type);
    }
    
    @Override
    public Caller getCaller(final Context context) {
        return new Caller(this, context);
    }
    
    @Override
    public Function getCommit() {
        return this.scanner.getCommit();
    }
    
    @Override
    public Function getComplete() {
        return this.scanner.getComplete();
    }
    
    @Override
    public Decorator getDecorator() {
        return this.scanner.getDecorator();
    }
    
    @Override
    public Instantiator getInstantiator() {
        return this.structure.getInstantiator();
    }
    
    @Override
    public String getName() {
        return this.detail.getName();
    }
    
    @Override
    public Order getOrder() {
        return this.scanner.getOrder();
    }
    
    @Override
    public ParameterMap getParameters() {
        return this.scanner.getParameters();
    }
    
    @Override
    public Function getPersist() {
        return this.scanner.getPersist();
    }
    
    @Override
    public Function getReplace() {
        return this.scanner.getReplace();
    }
    
    @Override
    public Function getResolve() {
        return this.scanner.getResolve();
    }
    
    @Override
    public Version getRevision() {
        return this.structure.getRevision();
    }
    
    @Override
    public Section getSection() {
        return this.structure.getSection();
    }
    
    @Override
    public Signature getSignature() {
        return this.scanner.getSignature();
    }
    
    @Override
    public List<Signature> getSignatures() {
        return this.scanner.getSignatures();
    }
    
    @Override
    public Label getText() {
        return this.structure.getText();
    }
    
    @Override
    public Class getType() {
        return this.detail.getType();
    }
    
    @Override
    public Function getValidate() {
        return this.scanner.getValidate();
    }
    
    @Override
    public Label getVersion() {
        return this.structure.getVersion();
    }
    
    @Override
    public boolean isEmpty() {
        return this.scanner.getRoot() == null;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.structure.isPrimitive();
    }
    
    @Override
    public boolean isStrict() {
        return this.detail.isStrict();
    }
}
