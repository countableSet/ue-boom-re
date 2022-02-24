// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

public interface InputNode extends Node
{
    InputNode getAttribute(final String p0);
    
    NodeMap<InputNode> getAttributes();
    
    InputNode getNext() throws Exception;
    
    InputNode getNext(final String p0) throws Exception;
    
    InputNode getParent();
    
    Position getPosition();
    
    String getPrefix();
    
    String getReference();
    
    Object getSource();
    
    boolean isElement();
    
    boolean isEmpty() throws Exception;
    
    boolean isRoot();
    
    void skip() throws Exception;
}
