// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;

class CacheLabel implements Label
{
    private final Annotation annotation;
    private final boolean attribute;
    private final boolean collection;
    private final Contact contact;
    private final boolean data;
    private final Decorator decorator;
    private final Type depend;
    private final String entry;
    private final Expression expression;
    private final boolean inline;
    private final Object key;
    private final Label label;
    private final boolean list;
    private final String name;
    private final String[] names;
    private final String override;
    private final String path;
    private final String[] paths;
    private final boolean required;
    private final boolean text;
    private final Class type;
    private final boolean union;
    
    public CacheLabel(final Label label) throws Exception {
        this.annotation = label.getAnnotation();
        this.expression = label.getExpression();
        this.decorator = label.getDecorator();
        this.attribute = label.isAttribute();
        this.collection = label.isCollection();
        this.contact = label.getContact();
        this.depend = label.getDependent();
        this.required = label.isRequired();
        this.override = label.getOverride();
        this.list = label.isTextList();
        this.inline = label.isInline();
        this.union = label.isUnion();
        this.names = label.getNames();
        this.paths = label.getPaths();
        this.path = label.getPath();
        this.type = label.getType();
        this.name = label.getName();
        this.entry = label.getEntry();
        this.data = label.isData();
        this.text = label.isText();
        this.key = label.getKey();
        this.label = label;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.annotation;
    }
    
    @Override
    public Contact getContact() {
        return this.contact;
    }
    
    @Override
    public Converter getConverter(final Context context) throws Exception {
        return this.label.getConverter(context);
    }
    
    @Override
    public Decorator getDecorator() throws Exception {
        return this.decorator;
    }
    
    @Override
    public Type getDependent() throws Exception {
        return this.depend;
    }
    
    @Override
    public Object getEmpty(final Context context) throws Exception {
        return this.label.getEmpty(context);
    }
    
    @Override
    public String getEntry() throws Exception {
        return this.entry;
    }
    
    @Override
    public Expression getExpression() throws Exception {
        return this.expression;
    }
    
    @Override
    public Object getKey() throws Exception {
        return this.key;
    }
    
    @Override
    public Label getLabel(final Class clazz) throws Exception {
        return this.label.getLabel(clazz);
    }
    
    @Override
    public String getName() throws Exception {
        return this.name;
    }
    
    @Override
    public String[] getNames() throws Exception {
        return this.names;
    }
    
    @Override
    public String getOverride() {
        return this.override;
    }
    
    @Override
    public String getPath() throws Exception {
        return this.path;
    }
    
    @Override
    public String[] getPaths() throws Exception {
        return this.paths;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public Type getType(final Class clazz) throws Exception {
        return this.label.getType(clazz);
    }
    
    @Override
    public boolean isAttribute() {
        return this.attribute;
    }
    
    @Override
    public boolean isCollection() {
        return this.collection;
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
    public boolean isText() {
        return this.text;
    }
    
    @Override
    public boolean isTextList() {
        return this.list;
    }
    
    @Override
    public boolean isUnion() {
        return this.union;
    }
    
    @Override
    public String toString() {
        return this.label.toString();
    }
}
