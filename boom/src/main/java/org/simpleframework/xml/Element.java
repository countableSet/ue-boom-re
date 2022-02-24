// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
    boolean data() default false;
    
    String name() default "";
    
    boolean required() default true;
    
    Class type() default void.class;
}
