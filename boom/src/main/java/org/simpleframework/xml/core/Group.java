// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Group
{
    LabelMap getElements() throws Exception;
    
    Label getLabel(final Class p0);
    
    Label getText() throws Exception;
    
    boolean isInline();
    
    boolean isTextList();
    
    String toString();
}
