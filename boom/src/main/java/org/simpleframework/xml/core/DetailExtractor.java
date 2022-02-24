// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.util.ConcurrentCache;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.util.Cache;

class DetailExtractor
{
    private final Cache<Detail> details;
    private final Cache<ContactList> fields;
    private final Cache<ContactList> methods;
    private final DefaultType override;
    private final Support support;
    
    public DetailExtractor(final Support support) {
        this(support, null);
    }
    
    public DetailExtractor(final Support support, final DefaultType override) {
        this.methods = new ConcurrentCache<ContactList>();
        this.fields = new ConcurrentCache<ContactList>();
        this.details = new ConcurrentCache<Detail>();
        this.override = override;
        this.support = support;
    }
    
    private ContactList getFields(final Class clazz, final Detail detail) throws Exception {
        final FieldScanner fieldScanner = new FieldScanner(detail, this.support);
        if (detail != null) {
            this.fields.cache(clazz, fieldScanner);
        }
        return fieldScanner;
    }
    
    private ContactList getMethods(final Class clazz, final Detail detail) throws Exception {
        final MethodScanner methodScanner = new MethodScanner(detail, this.support);
        if (detail != null) {
            this.methods.cache(clazz, methodScanner);
        }
        return methodScanner;
    }
    
    public Detail getDetail(final Class clazz) {
        Detail detail;
        if ((detail = this.details.fetch(clazz)) == null) {
            detail = new DetailScanner(clazz, this.override);
            this.details.cache(clazz, detail);
        }
        return detail;
    }
    
    public ContactList getFields(final Class clazz) throws Exception {
        ContactList fields;
        final ContactList list = fields = this.fields.fetch(clazz);
        if (list == null) {
            final Detail detail = this.getDetail(clazz);
            fields = list;
            if (detail != null) {
                fields = this.getFields(clazz, detail);
            }
        }
        return fields;
    }
    
    public ContactList getMethods(final Class clazz) throws Exception {
        ContactList methods;
        final ContactList list = methods = this.methods.fetch(clazz);
        if (list == null) {
            final Detail detail = this.getDetail(clazz);
            methods = list;
            if (detail != null) {
                methods = this.getMethods(clazz, detail);
            }
        }
        return methods;
    }
}
