// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.Iterator;

public interface NamespaceMap extends Iterable<String>
{
    String getPrefix();
    
    String getPrefix(final String p0);
    
    String getReference(final String p0);
    
    Iterator<String> iterator();
    
    String setReference(final String p0);
    
    String setReference(final String p0, final String p1);
}
