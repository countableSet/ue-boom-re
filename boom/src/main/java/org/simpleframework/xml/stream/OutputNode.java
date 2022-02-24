// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

public interface OutputNode extends Node
{
    void commit() throws Exception;
    
    NodeMap<OutputNode> getAttributes();
    
    OutputNode getChild(final String p0) throws Exception;
    
    String getComment();
    
    Mode getMode();
    
    NamespaceMap getNamespaces();
    
    OutputNode getParent();
    
    String getPrefix();
    
    String getPrefix(final boolean p0);
    
    String getReference();
    
    boolean isCommitted();
    
    boolean isRoot();
    
    void remove() throws Exception;
    
    OutputNode setAttribute(final String p0, final String p1);
    
    void setComment(final String p0);
    
    void setData(final boolean p0);
    
    void setMode(final Mode p0);
    
    void setName(final String p0);
    
    void setReference(final String p0);
    
    void setValue(final String p0);
}
