// 
// Decompiled by Procyon v0.5.36
// 

package butterknife.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ListenerMethod {
    String defaultReturn() default "null";
    
    String name();
    
    String[] parameters() default {};
    
    String returnType() default "void";
}
