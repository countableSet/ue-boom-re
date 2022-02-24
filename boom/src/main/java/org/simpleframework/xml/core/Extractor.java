// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;

interface Extractor<T extends Annotation>
{
    T[] getAnnotations() throws Exception;
    
    Label getLabel(final T p0) throws Exception;
    
    Class getType(final T p0) throws Exception;
}
