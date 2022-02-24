// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Expression extends Iterable<String>
{
    String getAttribute(final String p0);
    
    String getElement(final String p0);
    
    String getFirst();
    
    int getIndex();
    
    String getLast();
    
    String getPath();
    
    Expression getPath(final int p0);
    
    Expression getPath(final int p0, final int p1);
    
    String getPrefix();
    
    boolean isAttribute();
    
    boolean isEmpty();
    
    boolean isPath();
    
    String toString();
}
