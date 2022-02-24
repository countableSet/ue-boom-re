// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.Style;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.stream.Format;

class ElementListLabel extends TemplateLabel
{
    private Expression cache;
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private String entry;
    private Format format;
    private boolean inline;
    private Class item;
    private ElementList label;
    private String name;
    private String override;
    private String path;
    private boolean required;
    private Class type;
    
    public ElementListLabel(final Contact contact, final ElementList label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.required = label.required();
        this.type = contact.getType();
        this.override = label.name();
        this.inline = label.inline();
        this.entry = label.entry();
        this.data = label.data();
        this.item = label.type();
        this.format = format;
        this.label = label;
    }
    
    private Converter getConverter(final Context context, final String s) throws Exception {
        final Type dependent = this.getDependent();
        final Contact contact = this.getContact();
        Converter converter;
        if (!context.isPrimitive(dependent)) {
            converter = new CompositeList(context, contact, dependent, s);
        }
        else {
            converter = new PrimitiveList(context, contact, dependent, s);
        }
        return converter;
    }
    
    private Converter getInlineConverter(final Context context, final String s) throws Exception {
        final Type dependent = this.getDependent();
        final Contact contact = this.getContact();
        Repeater repeater;
        if (!context.isPrimitive(dependent)) {
            repeater = new CompositeInlineList(context, contact, dependent, s);
        }
        else {
            repeater = new PrimitiveInlineList(context, contact, dependent, s);
        }
        return repeater;
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
        final String entry = this.getEntry();
        Converter converter;
        if (!this.label.inline()) {
            converter = this.getConverter(context, entry);
        }
        else {
            converter = this.getInlineConverter(context, entry);
        }
        return converter;
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }
    
    @Override
    public Type getDependent() throws Exception {
        final Contact contact = this.getContact();
        if (this.item == Void.TYPE) {
            this.item = contact.getDependent();
        }
        if (this.item == null) {
            throw new ElementException("Unable to determine generic type for %s", new Object[] { contact });
        }
        return new ClassType(this.item);
    }
    
    @Override
    public Object getEmpty(final Context context) throws Exception {
        final CollectionFactory collectionFactory = new CollectionFactory(context, new ClassType(this.type));
        Object instance;
        if (!this.label.empty()) {
            instance = collectionFactory.getInstance();
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
        return this.type;
    }
    
    @Override
    public boolean isCollection() {
        return true;
    }
    
    @Override
    public boolean isData() {
        return this.data;
    }
    
    @Override
    public boolean isInline() {
        return this.inline;
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
