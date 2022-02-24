// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Collection;
import java.util.Iterator;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

class ConstructorScanner
{
    private Signature primary;
    private ParameterMap registry;
    private List<Signature> signatures;
    private Support support;
    
    public ConstructorScanner(final Detail detail, final Support support) throws Exception {
        this.signatures = new ArrayList<Signature>();
        this.registry = new ParameterMap();
        this.support = support;
        this.scan(detail);
    }
    
    private void scan(final Constructor constructor) throws Exception {
        final SignatureScanner signatureScanner = new SignatureScanner(constructor, this.registry, this.support);
        if (signatureScanner.isValid()) {
            for (final Signature primary : signatureScanner.getSignatures()) {
                if (primary.size() == 0) {
                    this.primary = primary;
                }
                this.signatures.add(primary);
            }
        }
    }
    
    private void scan(final Detail detail) throws Exception {
        final Constructor[] constructors = detail.getConstructors();
        if (!detail.isInstantiable()) {
            throw new ConstructorException("Can not construct inner %s", new Object[] { detail });
        }
        for (final Constructor constructor : constructors) {
            if (!detail.isPrimitive()) {
                this.scan(constructor);
            }
        }
    }
    
    public ParameterMap getParameters() {
        return this.registry;
    }
    
    public Signature getSignature() {
        return this.primary;
    }
    
    public List<Signature> getSignatures() {
        return new ArrayList<Signature>(this.signatures);
    }
}
