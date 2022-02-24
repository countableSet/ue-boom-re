// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ClassInstantiator implements Instantiator
{
    private final List<Creator> creators;
    private final Detail detail;
    private final Creator primary;
    private final ParameterMap registry;
    
    public ClassInstantiator(final List<Creator> creators, final Creator primary, final ParameterMap registry, final Detail detail) {
        this.creators = creators;
        this.registry = registry;
        this.primary = primary;
        this.detail = detail;
    }
    
    private Creator getCreator(final Criteria criteria) throws Exception {
        Creator primary = this.primary;
        double n = 0.0;
        for (final Creator creator : this.creators) {
            final double score = creator.getScore(criteria);
            if (score > n) {
                primary = creator;
                n = score;
            }
        }
        return primary;
    }
    
    @Override
    public List<Creator> getCreators() {
        return new ArrayList<Creator>(this.creators);
    }
    
    @Override
    public Object getInstance() throws Exception {
        return this.primary.getInstance();
    }
    
    @Override
    public Object getInstance(final Criteria criteria) throws Exception {
        final Creator creator = this.getCreator(criteria);
        if (creator == null) {
            throw new PersistenceException("Constructor not matched for %s", new Object[] { this.detail });
        }
        return creator.getInstance(criteria);
    }
    
    @Override
    public Parameter getParameter(final String key) {
        return ((LinkedHashMap<K, Parameter>)this.registry).get(key);
    }
    
    @Override
    public List<Parameter> getParameters() {
        return this.registry.getAll();
    }
    
    @Override
    public boolean isDefault() {
        boolean b = true;
        if (this.creators.size() <= 1) {
            if (this.primary == null) {
                b = false;
            }
        }
        else {
            b = false;
        }
        return b;
    }
    
    @Override
    public String toString() {
        return String.format("creator for %s", this.detail);
    }
}
