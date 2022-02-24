// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.stream.Format;

class ElementLabel extends TemplateLabel
{
    private Expression cache;
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private Class expect;
    private Format format;
    private Element label;
    private String name;
    private String override;
    private String path;
    private boolean required;
    private Class type;
    
    public ElementLabel(final Contact contact, final Element label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.override = label.name();
        this.expect = label.type();
        this.data = label.data();
        this.format = format;
        this.label = label;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label;
    }
    
    @Override
    public Contact getContact() {
        return this.detail.getContact();
    }
    
    @Override
    public Converter getConverter(final Context context) throws Exception {
        final Contact contact = this.getContact();
        Converter converter;
        if (context.isPrimitive(contact)) {
            converter = new Primitive(context, contact);
        }
        else if (this.expect == Void.TYPE) {
            converter = new Composite(context, contact);
        }
        else {
            converter = new Composite(context, contact, this.expect);
        }
        return converter;
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }
    
    @Override
    public Object getEmpty(final Context context) {
        return null;
    }
    
    @Override
    public Expression getExpression() throws Exception {
        if (this.cache == null) {
            this.cache = this.detail.getExpression();
        }
        return this.cache;
    }
    
    @Override
    public String getName() throws Exception {
        if (this.name == null) {
            this.name = this.format.getStyle().getElement(this.detail.getName());
        }
        return this.name;
    }
    
    @Override
    public String getOverride() {
        return this.override;
    }
    
    @Override
    public String getPath() throws Exception {
        if (this.path == null) {
            this.path = this.getExpression().getElement(this.getName());
        }
        return this.path;
    }
    
    @Override
    public Class getType() {
        Class clazz;
        if (this.expect == Void.TYPE) {
            clazz = this.type;
        }
        else {
            clazz = this.expect;
        }
        return clazz;
    }
    
    @Override
    public Type getType(final Class clazz) {
        Object contact = this.getContact();
        if (this.expect != Void.TYPE) {
            contact = new OverrideType((Type)contact, this.expect);
        }
        return (Type)contact;
    }
    
    @Override
    public boolean isData() {
        return this.data;
    }
    
    @Override
    public boolean isRequired() {
        return this.required;
    }
    
    @Override
    public String toString() {
        return this.detail.toString();
    }
}
