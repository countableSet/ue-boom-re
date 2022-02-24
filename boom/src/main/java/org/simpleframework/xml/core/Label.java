// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import org.simpleframework.xml.strategy.Type;
import java.lang.annotation.Annotation;

interface Label
{
    Annotation getAnnotation();
    
    Contact getContact();
    
    Converter getConverter(final Context p0) throws Exception;
    
    Decorator getDecorator() throws Exception;
    
    Type getDependent() throws Exception;
    
    Object getEmpty(final Context p0) throws Exception;
    
    String getEntry() throws Exception;
    
    Expression getExpression() throws Exception;
    
    Object getKey() throws Exception;
    
    Label getLabel(final Class p0) throws Exception;
    
    String getName() throws Exception;
    
    String[] getNames() throws Exception;
    
    String getOverride();
    
    String getPath() throws Exception;
    
    String[] getPaths() throws Exception;
    
    Class getType();
    
    Type getType(final Class p0) throws Exception;
    
    boolean isAttribute();
    
    boolean isCollection();
    
    boolean isData();
    
    boolean isInline();
    
    boolean isRequired();
    
    boolean isText();
    
    boolean isTextList();
    
    boolean isUnion();
    
    String toString();
}
