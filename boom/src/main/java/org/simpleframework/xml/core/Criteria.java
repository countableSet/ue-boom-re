// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Criteria extends Iterable<Object>
{
    void commit(final Object p0) throws Exception;
    
    Variable get(final Object p0) throws Exception;
    
    Variable get(final Label p0) throws Exception;
    
    Variable remove(final Object p0) throws Exception;
    
    Variable resolve(final String p0) throws Exception;
    
    void set(final Label p0, final Object p1) throws Exception;
}
