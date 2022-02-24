// 
// Decompiled by Procyon v0.5.36
// 

package org.simpleframework.xml;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface ElementMap {
    boolean attribute() default false;
    
    boolean data() default false;
    
    boolean empty() default true;
    
    String entry() default "";
    
    boolean inline() default false;
    
    String key() default "";
    
    Class keyType() default void.class;
    
    String name() default "";
    
    boolean required() default true;
    
    String value() default "";
    
    Class valueType() default void.class;
}
