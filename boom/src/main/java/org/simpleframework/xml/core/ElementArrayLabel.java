// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.Style;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.stream.Format;

class ElementArrayLabel extends TemplateLabel
{
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private String entry;
    private Format format;
    private ElementArray label;
    private String name;
    private Expression path;
    private boolean required;
    private Class type;
    
    public ElementArrayLabel(final Contact contact, final ElementArray label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.entry = label.entry();
        this.data = label.data();
        this.name = label.name();
        this.format = format;
        this.label = label;
    }
    
    private Converter getConverter(final Context context, final String s) throws Exception {
        final Type dependent = this.getDependent();
        final Contact contact = this.getContact();
        Converter converter;
        if (!context.isPrimitive(dependent)) {
            converter = new CompositeArray(context, contact, dependent, s);
        }
        else {
            converter = new PrimitiveArray(context, contact, dependent, s);
        }
        return converter;
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
        final String entry = this.getEntry();
        if (!this.type.isArray()) {
            throw new InstantiationException("Type is not an array %s for %s", new Object[] { this.type, contact });
        }
        return this.getConverter(context, entry);
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }
    
    @Override
    public Type getDependent() {
        final Class componentType = this.type.getComponentType();
        ClassType classType;
        if (componentType == null) {
            classType = new ClassType(this.type);
        }
        else {
            classType = new ClassType(componentType);
        }
        return classType;
    }
    
    @Override
    public Object getEmpty(final Context context) throws Exception {
        final ArrayFactory arrayFactory = new ArrayFactory(context, new ClassType(this.type));
        Object instance;
        if (!this.label.empty()) {
            instance = arrayFactory.getInstance();
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    @Override
    public String getEntry() throws Exception {
        final Style style = this.format.getStyle();
        if (this.detail.isEmpty(this.entry)) {
            this.entry = this.detail.getEntry();
        }
        return style.getElement(this.entry);
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
        return this.format.getStyle().getElement(this.detail.getName());
    }
    
    @Override
    public String getOverride() {
        return this.name;
    }
    
    @Override
    public String getPath() throws Exception {
        return this.getExpression().getElement(this.getName());
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
    public boolean isRequired() {
        return this.required;
    }
    
    @Override
    public String toString() {
        return this.detail.toString();
    }
}
