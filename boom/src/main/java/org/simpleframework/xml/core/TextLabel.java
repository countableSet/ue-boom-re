// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.Text;

class TextLabel extends TemplateLabel
{
    private Contact contact;
    private boolean data;
    private Introspector detail;
    private String empty;
    private Text label;
    private Expression path;
    private boolean required;
    private Class type;
    
    public TextLabel(final Contact contact, final Text label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.required = label.required();
        this.type = contact.getType();
        this.empty = label.empty();
        this.data = label.data();
        this.contact = contact;
        this.label = label;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label;
    }
    
    @Override
    public Contact getContact() {
        return this.contact;
    }
    
    @Override
    public Converter getConverter(final Context context) throws Exception {
        final String empty = this.getEmpty(context);
        final Contact contact = this.getContact();
        if (!context.isPrimitive(contact)) {
            throw new TextException("Cannot use %s to represent %s", new Object[] { contact, this.label });
        }
        return new Primitive(context, contact, empty);
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return null;
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
    public String getName() {
        return "";
    }
    
    @Override
    public String getOverride() {
        return this.contact.toString();
    }
    
    @Override
    public String getPath() throws Exception {
        return this.getExpression().getPath();
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public boolean isData() {
        return this.data;
    }
    
    @Override
    public boolean isInline() {
        return true;
    }
    
    @Override
    public boolean isRequired() {
        return this.required;
    }
    
    @Override
    public boolean isText() {
        return true;
    }
    
    @Override
    public String toString() {
        return this.detail.toString();
    }
}
