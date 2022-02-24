// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface Default {
    boolean required() default true;
    
    DefaultType value() default DefaultType.FIELD;
}
