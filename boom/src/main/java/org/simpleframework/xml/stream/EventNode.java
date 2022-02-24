// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

interface EventNode extends Iterable<Attribute>
{
    int getLine();
    
    String getName();
    
    String getPrefix();
    
    String getReference();
    
    Object getSource();
    
    String getValue();
    
    boolean isEnd();
    
    boolean isStart();
    
    boolean isText();
}
