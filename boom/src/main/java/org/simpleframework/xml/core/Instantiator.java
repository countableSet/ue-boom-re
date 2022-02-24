// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.List;

interface Instantiator
{
    List<Creator> getCreators();
    
    Object getInstance() throws Exception;
    
    Object getInstance(final Criteria p0) throws Exception;
    
    Parameter getParameter(final String p0);
    
    List<Parameter> getParameters();
    
    boolean isDefault();
}
