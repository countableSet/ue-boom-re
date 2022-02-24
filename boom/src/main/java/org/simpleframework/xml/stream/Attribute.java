// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

interface Attribute
{
    String getName();
    
    String getPrefix();
    
    String getReference();
    
    Object getSource();
    
    String getValue();
    
    boolean isReserved();
}
