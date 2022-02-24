// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

abstract class TemplateParameter implements Parameter
{
    protected TemplateParameter() {
    }
    
    @Override
    public boolean isAttribute() {
        return false;
    }
    
    @Override
    public boolean isText() {
        return false;
    }
}
