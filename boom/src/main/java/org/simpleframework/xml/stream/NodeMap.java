// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.stream;

import java.util.Iterator;

public interface NodeMap<T extends Node> extends Iterable<String>
{
    T get(final String p0);
    
    String getName();
    
    T getNode();
    
    Iterator<String> iterator();
    
    T put(final String p0, final String p1);
    
    T remove(final String p0);
}
