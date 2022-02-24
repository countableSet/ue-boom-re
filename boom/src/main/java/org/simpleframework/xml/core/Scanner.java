// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.util.List;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.Order;

interface Scanner extends Policy
{
    Caller getCaller(final Context p0);
    
    Function getCommit();
    
    Function getComplete();
    
    Decorator getDecorator();
    
    Instantiator getInstantiator();
    
    String getName();
    
    Order getOrder();
    
    ParameterMap getParameters();
    
    Function getPersist();
    
    Function getReplace();
    
    Function getResolve();
    
    Version getRevision();
    
    Section getSection();
    
    Signature getSignature();
    
    List<Signature> getSignatures();
    
    Label getText();
    
    Class getType();
    
    Function getValidate();
    
    Label getVersion();
    
    boolean isEmpty();
    
    boolean isPrimitive();
    
    boolean isStrict();
}
