// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Model extends Iterable<String>
{
    LabelMap getAttributes() throws Exception;
    
    LabelMap getElements() throws Exception;
    
    Expression getExpression();
    
    int getIndex();
    
    ModelMap getModels() throws Exception;
    
    String getName();
    
    String getPrefix();
    
    Label getText();
    
    boolean isAttribute(final String p0);
    
    boolean isComposite();
    
    boolean isElement(final String p0);
    
    boolean isEmpty();
    
    boolean isModel(final String p0);
    
    Model lookup(final String p0, final int p1);
    
    Model lookup(final Expression p0);
    
    Model register(final String p0, final String p1, final int p2) throws Exception;
    
    void register(final Label p0) throws Exception;
    
    void registerAttribute(final String p0) throws Exception;
    
    void registerAttribute(final Label p0) throws Exception;
    
    void registerElement(final String p0) throws Exception;
    
    void registerElement(final Label p0) throws Exception;
    
    void registerText(final Label p0) throws Exception;
    
    void validate(final Class p0) throws Exception;
}
