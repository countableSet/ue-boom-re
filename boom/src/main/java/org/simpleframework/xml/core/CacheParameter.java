// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

class CacheParameter implements Parameter
{
    private final Annotation annotation;
    private final boolean attribute;
    private final Expression expression;
    private final int index;
    private final Object key;
    private final String name;
    private final String path;
    private final boolean primitive;
    private final boolean required;
    private final String string;
    private final boolean text;
    private final Class type;
    
    public CacheParameter(final Parameter parameter, final Label label) throws Exception {
        this.annotation = parameter.getAnnotation();
        this.expression = parameter.getExpression();
        this.attribute = parameter.isAttribute();
        this.primitive = parameter.isPrimitive();
        this.required = label.isRequired();
        this.string = parameter.toString();
        this.text = parameter.isText();
        this.index = parameter.getIndex();
        this.name = parameter.getName();
        this.path = parameter.getPath();
        this.type = parameter.getType();
        this.key = label.getKey();
    }
    
    @Override
    public Annotation getAnnotation() {
        return this.annotation;
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
    public boolean isAttribute() {
        return this.attribute;
    }
    
    @Override
    public boolean isPrimitive() {
        return this.primitive;
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
    public String toString() {
        return this.string;
    }
}
