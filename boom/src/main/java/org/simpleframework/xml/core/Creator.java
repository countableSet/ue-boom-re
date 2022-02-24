// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

interface Creator
{
    Object getInstance() throws Exception;
    
    Object getInstance(final Criteria p0) throws Exception;
    
    double getScore(final Criteria p0) throws Exception;
    
    Signature getSignature() throws Exception;
    
    Class getType() throws Exception;
}
