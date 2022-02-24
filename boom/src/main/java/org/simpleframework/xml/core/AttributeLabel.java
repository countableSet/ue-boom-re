// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.stream.Format;

class AttributeLabel extends TemplateLabel
{
    private Decorator decorator;
    private Introspector detail;
    private String empty;
    private Format format;
    private Attribute label;
    private String name;
    private Expression path;
    private boolean required;
    private Class type;
    
    public AttributeLabel(final Contact contact, final Attribute label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.empty = label.empty();
        this.name = label.name();
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
        return new Primitive(context, this.getContact(), this.getEmpty(context));
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }
    
    @Override
    public String getEmpty(final Context context) {
        String empty;
        if (this.detail.isEmpty(this.empty)) {
            empty = null;
        }
        else {
            empty = this.empty;
        }
        return empty;
    }
    
    @Override
    public Expression getExpression() throws Exception {
        if (this.path == null) {
            this.path = this.detail.getExpression();
        }
        return this.path;
    }
    
    @Override
    public String getName() throws Exception {
        return this.format.getStyle().getAttribute(this.detail.getName());
    }
    
    @Override
    public String getOverride() {
        return this.name;
    }
    
    @Override
    public String getPath() throws Exception {
        return this.getExpression().getAttribute(this.getName());
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public boolean isAttribute() {
        return true;
    }
    
    @Override
    public boolean isData() {
        return false;
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
