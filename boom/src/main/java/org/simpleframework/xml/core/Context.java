// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.OutputNode;
import org.simpleframework.xml.Version;
import org.simpleframework.xml.stream.Style;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;

interface Context
{
    Object getAttribute(final Object p0);
    
    Caller getCaller(final Class p0) throws Exception;
    
    Decorator getDecorator(final Class p0) throws Exception;
    
    Instance getInstance(final Class p0);
    
    Instance getInstance(final Value p0);
    
    String getName(final Class p0) throws Exception;
    
    Value getOverride(final Type p0, final InputNode p1) throws Exception;
    
    String getProperty(final String p0);
    
    Schema getSchema(final Class p0) throws Exception;
    
    Session getSession();
    
    Style getStyle();
    
    Support getSupport();
    
    Class getType(final Type p0, final Object p1);
    
    Version getVersion(final Class p0) throws Exception;
    
    boolean isFloat(final Class p0) throws Exception;
    
    boolean isFloat(final Type p0) throws Exception;
    
    boolean isPrimitive(final Class p0) throws Exception;
    
    boolean isPrimitive(final Type p0) throws Exception;
    
    boolean isStrict();
    
    boolean setOverride(final Type p0, final Object p1, final OutputNode p2) throws Exception;
}
