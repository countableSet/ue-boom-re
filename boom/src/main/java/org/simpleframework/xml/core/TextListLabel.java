// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.Text;

class TextListLabel extends TemplateLabel
{
    private final String empty;
    private final Label label;
    private final Text text;
    
    public TextListLabel(final Label label, final Text text) {
        this.empty = text.empty();
        this.label = label;
        this.text = text;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label.getAnnotation();
    }
    
    @Override
    public Contact getContact() {
        return this.label.getContact();
    }
    
    @Override
    public Converter getConverter(final Context context) throws Exception {
        final Contact contact = this.getContact();
        if (!this.label.isCollection()) {
            throw new TextException("Cannot use %s to represent %s", new Object[] { contact, this.label });
        }
        return new TextList(context, contact, this.label);
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return null;
    }
    
    @Override
    public Type getDependent() throws Exception {
        return this.label.getDependent();
    }
    
    @Override
    public String getEmpty(final Context context) throws Exception {
        return this.empty;
    }
    
    @Override
    public String getEntry() throws Exception {
        return this.label.getEntry();
    }
    
    @Override
    public Expression getExpression() throws Exception {
        return this.label.getExpression();
    }
    
    @Override
    public Object getKey() throws Exception {
        return this.label.getKey();
    }
    
    @Override
    public String getName() throws Exception {
        return this.label.getName();
    }
    
    @Override
    public String[] getNames() throws Exception {
        return this.label.getNames();
    }
    
    @Override
    public String getOverride() {
        return this.label.getOverride();
    }
    
    @Override
    public String getPath() throws Exception {
        return this.label.getPath();
    }
    
    @Override
    public String[] getPaths() throws Exception {
        return this.label.getPaths();
    }
    
    @Override
    public Class getType() {
        return this.label.getType();
    }
    
    @Override
    public boolean isCollection() {
        return true;
    }
    
    @Override
    public boolean isData() {
        return this.label.isData();
    }
    
    @Override
    public boolean isInline() {
        return this.label.isInline();
    }
    
    @Override
    public boolean isRequired() {
        return this.label.isRequired();
    }
    
    @Override
    public boolean isTextList() {
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", this.text, this.label);
    }
}
