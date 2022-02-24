// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.Version;

interface Schema
{
    Caller getCaller();
    
    Decorator getDecorator();
    
    Instantiator getInstantiator();
    
    Version getRevision();
    
    Section getSection();
    
    Label getText();
    
    Label getVersion();
    
    boolean isPrimitive();
}
