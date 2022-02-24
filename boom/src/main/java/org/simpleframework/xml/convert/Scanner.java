// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml.convert;

import java.lang.annotation.Annotation;

interface Scanner
{
     <T extends Annotation> T scan(final Class<T> p0);
}
