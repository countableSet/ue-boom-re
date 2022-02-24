// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;

class ElementUnionLabel extends TemplateLabel
{
    private Contact contact;
    private GroupExtractor extractor;
    private Label label;
    private Expression path;
    private ElementUnion union;
    
    public ElementUnionLabel(final Contact contact, final ElementUnion union, final Element element, final Format format) throws Exception {
        this.extractor = new GroupExtractor(contact, union, format);
        this.label = new ElementLabel(contact, element, format);
        this.contact = contact;
        this.union = union;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.label.getAnnotation();
    }
    
    @Override
    public Contact getContact() {
        return this.contact;
    }
    
    @Override
    public Converter getConverter(final Context context) throws Exception {
        final Expression expression = this.getExpression();
        final Contact contact = this.getContact();
        if (contact == null) {
            throw new UnionException("Union %s was not declared on a field or method", new Object[] { this.label });
        }
        return new CompositeUnion(context, this.extractor, expression, contact);
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.label.getDecorator();
    }
    
    @Override
    public Type getDependent() throws Exception {
        return this.label.getDependent();
    }
    
    @Override
    public Object getEmpty(final Context context) throws Exception {
        return this.label.getEmpty(context);
    }
    
    @Override
    public String getEntry() throws Exception {
        return this.label.getEntry();
    }
    
    @Override
    public Expression getExpression() throws Exception {
        if (this.path == null) {
            this.path = this.label.getExpression();
        }
        return this.path;
    }
    
    @Override
    public Label getLabel(final Class clazz) throws Exception {
        final Contact contact = this.getContact();
        if (!this.extractor.isValid(clazz)) {
            throw new UnionException("No type matches %s in %s for %s", new Object[] { clazz, this.union, contact });
        }
        return this.extractor.getLabel(clazz);
    }
    
    @Override
    public String getName() throws Exception {
        return this.label.getName();
    }
    
    @Override
    public String[] getNames() throws Exception {
        return this.extractor.getNames();
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
        return this.extractor.getPaths();
    }
    
    @Override
    public Class getType() {
        return this.label.getType();
    }
    
    @Override
    public Type getType(final Class clazz) throws Exception {
        final Contact contact = this.getContact();
        if (!this.extractor.isValid(clazz)) {
            throw new UnionException("No type matches %s in %s for %s", new Object[] { clazz, this.union, contact });
        }
        Object o = contact;
        if (this.extractor.isDeclared(clazz)) {
            o = new OverrideType(contact, clazz);
        }
        return (Type)o;
    }
    
    @Override
    public boolean isCollection() {
        return this.label.isCollection();
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
    public boolean isUnion() {
        return true;
    }
    
    @Override
    public String toString() {
        return this.label.toString();
    }
}
