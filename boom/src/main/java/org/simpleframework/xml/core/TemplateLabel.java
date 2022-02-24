// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;

abstract class TemplateLabel implements Label
{
    private final KeyBuilder builder;
    
    protected TemplateLabel() {
        this.builder = new KeyBuilder(this);
    }
    
    @Override
    public Type getDependent() throws Exception {
        return null;
    }
    
    @Override
    public String getEntry() throws Exception {
        return null;
    }
    
    @Override
    public Object getKey() throws Exception {
        return this.builder.getKey();
    }
    
    @Override
    public Label getLabel(final Class clazz) throws Exception {
        return this;
    }
    
    @Override
    public String[] getNames() throws Exception {
        return new String[] { this.getPath(), this.getName() };
    }
    
    @Override
    public String[] getPaths() throws Exception {
        return new String[] { this.getPath() };
    }
    
    @Override
    public Type getType(final Class clazz) throws Exception {
        return this.getContact();
    }
    
    @Override
    public boolean isAttribute() {
        return false;
    }
    
    @Override
    public boolean isCollection() {
        return false;
    }
    
    @Override
    public boolean isInline() {
        return false;
    }
    
    @Override
    public boolean isText() {
        return false;
    }
    
    @Override
    public boolean isTextList() {
        return false;
    }
    
    @Override
    public boolean isUnion() {
        return false;
    }
}
