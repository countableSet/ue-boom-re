// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.stream.Format;

class VersionLabel extends TemplateLabel
{
    private Decorator decorator;
    private Introspector detail;
    private Format format;
    private Version label;
    private String name;
    private Expression path;
    private boolean required;
    private Class type;
    
    public VersionLabel(final Contact contact, final Version label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
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
        final String empty = this.getEmpty(context);
        final Contact contact = this.getContact();
        if (!context.isFloat(contact)) {
            throw new AttributeException("Cannot use %s to represent %s", new Object[] { this.label, contact });
        }
        return new Primitive(context, contact, empty);
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }
    
    @Override
    public String getEmpty(final Context context) {
        return null;
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
