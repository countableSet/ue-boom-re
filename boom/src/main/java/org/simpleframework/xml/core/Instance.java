// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Instance
{
    Object getInstance() throws Exception;
    
    Class getType();
    
    boolean isReference();
    
    Object setInstance(final Object p0) throws Exception;
}
