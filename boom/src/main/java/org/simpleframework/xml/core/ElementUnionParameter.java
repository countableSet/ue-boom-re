// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import java.lang.reflect.Constructor;

class ElementUnionParameter extends TemplateParameter
{
    private final Contact contact;
    private final Expression expression;
    private final int index;
    private final Object key;
    private final Label label;
    private final String name;
    private final String path;
    private final Class type;
    
    public ElementUnionParameter(final Constructor constructor, final ElementUnion elementUnion, final Element element, final Format format, final int index) throws Exception {
        this.contact = new Contact(element, constructor, index);
        this.label = new ElementUnionLabel(this.contact, elementUnion, element, format);
        this.expression = this.label.getExpression();
        this.path = this.label.getPath();
        this.type = this.label.getType();
        this.name = this.label.getName();
        this.key = this.label.getKey();
        this.index = index;
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.contact.getAnnotation();
    }
    
    @Override
    public Expression getExpression() {
        return this.expression;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public Object getKey() {
        return this.key;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getPath() {
        return this.path;
    }
    
    @Override
    public Class getType() {
        return this.type;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }
    
    @Override
    public boolean isRequired() {
        return this.label.isRequired();
    }
    
    @Override
    public String toString() {
        return this.contact.toString();
    }
    
    private static class Contact extends ParameterContact<Element>
    {
        public Contact(final Element element, final Constructor constructor, final int n) {
            super(element, constructor, n);
        }
        
        @Override
        public String getName() {
            return ((Element)this.label).name();
        }
    }
}
