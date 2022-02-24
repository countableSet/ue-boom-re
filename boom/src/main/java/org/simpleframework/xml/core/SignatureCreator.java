// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.List;

class SignatureCreator implements Creator
{
    private final List<Parameter> list;
    private final Signature signature;
    private final Class type;
    
    public SignatureCreator(final Signature signature) {
        this.type = signature.getType();
        this.list = signature.getAll();
        this.signature = signature;
    }
    
    private double getAdjustment(double n) {
        final double n2 = this.list.size() / 1000.0;
        if (n > 0.0) {
            n = n / this.list.size() + n2;
        }
        else {
            n /= this.list.size();
        }
        return n;
    }
    
    private double getPercentage(final Criteria criteria) throws Exception {
        final double n = -1.0;
        double n2 = 0.0;
        for (final Parameter parameter : this.list) {
            if (criteria.get(parameter.getKey()) == null) {
                double adjustment;
                if (parameter.isRequired()) {
                    adjustment = n;
                }
                else {
                    if (!parameter.isPrimitive()) {
                        continue;
                    }
                    adjustment = n;
                }
                return adjustment;
            }
            ++n2;
        }
        return this.getAdjustment(n2);
    }
    
    private Object getVariable(final Criteria criteria, final int n) throws Exception {
        final Variable remove = criteria.remove(this.list.get(n).getKey());
        Object value;
        if (remove != null) {
            value = remove.getValue();
        }
        else {
            value = null;
        }
        return value;
    }
    
    @Override
    public Object getInstance() throws Exception {
        return this.signature.create();
    }
    
    @Override
    public Object getInstance(final Criteria criteria) throws Exception {
        final Object[] array = this.list.toArray();
        for (int i = 0; i < this.list.size(); ++i) {
            array[i] = this.getVariable(criteria, i);
        }
        return this.signature.create(array);
    }
    
    @Override
    public double getScore(final Criteria criteria) throws Exception {
        double percentage = -1.0;
        final Signature copy = this.signature.copy();
        for (final Object next : criteria) {
            final Parameter value = copy.get(next);
            final Variable value2 = criteria.get(next);
            final Contact contact = value2.getContact();
            if (value == null || Support.isAssignable(value2.getValue().getClass(), value.getType())) {
                if (!contact.isReadOnly() || value != null) {
                    continue;
                }
            }
            return percentage;
        }
        percentage = this.getPercentage(criteria);
        return percentage;
    }
    
    @Override
    public Signature getSignature() {
        return this.signature;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public String toString() {
        return this.signature.toString();
    }
}
