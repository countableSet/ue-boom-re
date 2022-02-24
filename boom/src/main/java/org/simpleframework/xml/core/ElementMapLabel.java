// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.Style;
import java.lang.annotation.Annotation;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.stream.Format;

class ElementMapLabel extends TemplateLabel
{
    private Expression cache;
    private boolean data;
    private Decorator decorator;
    private Introspector detail;
    private Entry entry;
    private Format format;
    private boolean inline;
    private Class[] items;
    private ElementMap label;
    private String name;
    private String override;
    private String parent;
    private String path;
    private boolean required;
    private Class type;
    
    public ElementMapLabel(final Contact contact, final ElementMap label, final Format format) {
        this.detail = new Introspector(contact, this, format);
        this.decorator = new Qualifier(contact);
        this.entry = new Entry(contact, label);
        this.required = label.required();
        this.type = contact.getType();
        this.inline = label.inline();
        this.override = label.name();
        this.data = label.data();
        this.format = format;
        this.label = label;
    }
    
    private Type getMap() {
        return new ClassType(this.type);
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
        final Type map = this.getMap();
        Converter converter;
        if (!this.label.inline()) {
            converter = new CompositeMap(context, this.entry, map);
        }
        else {
            converter = new CompositeInlineMap(context, this.entry, map);
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
        if (this.items == null) {
            this.items = contact.getDependents();
        }
        if (this.items == null) {
            throw new ElementException("Unable to determine type for %s", new Object[] { contact });
        }
        ClassType classType;
        if (this.items.length == 0) {
            classType = new ClassType(Object.class);
        }
        else {
            classType = new ClassType(this.items[0]);
        }
        return classType;
    }
    
    @Override
    public Object getEmpty(final Context context) throws Exception {
        final MapFactory mapFactory = new MapFactory(context, new ClassType(this.type));
        Object instance;
        if (!this.label.empty()) {
            instance = mapFactory.getInstance();
        }
        else {
            instance = null;
        }
        return instance;
    }
    
    @Override
    public String getEntry() throws Exception {
        final Style style = this.format.getStyle();
        if (this.detail.isEmpty(this.parent)) {
            this.parent = this.detail.getEntry();
        }
        return style.getElement(this.parent);
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
            final Style style = this.format.getStyle();
            String s = this.entry.getEntry();
            if (!this.label.inline()) {
                s = this.detail.getName();
            }
            this.name = style.getElement(s);
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
