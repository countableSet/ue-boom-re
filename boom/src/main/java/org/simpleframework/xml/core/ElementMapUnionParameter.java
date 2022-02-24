// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.ElementMapUnion;
import java.lang.reflect.Constructor;

class ElementMapUnionParameter extends TemplateParameter
{
    private final Contact contact;
    private final Expression expression;
    private final int index;
    private final Object key;
    private final Label label;
    private final String name;
    private final String path;
    private final Class type;
    
    public ElementMapUnionParameter(final Constructor constructor, final ElementMapUnion elementMapUnion, final ElementMap elementMap, final Format format, final int index) throws Exception {
        this.contact = new Contact(elementMap, constructor, index);
        this.label = new ElementMapUnionLabel(this.contact, elementMapUnion, elementMap, format);
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
    
    private static class Contact extends ParameterContact<ElementMap>
    {
        public Contact(final ElementMap elementMap, final Constructor constructor, final int n) {
            super(elementMap, constructor, n);
        }
        
        @Override
        public String getName() {
            return ((ElementMap)this.label).name();
        }
    }
}
