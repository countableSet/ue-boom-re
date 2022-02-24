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
@Target({ ElementType.ANNOTATION_TYPE })
public @interface ListenerClass {
    Class<? extends Enum<?>> callbacks() default NONE.class;
    
    int genericArguments() default 0;
    
    ListenerMethod[] method() default {};
    
    String setter();
    
    String targetType();
    
    String type();
    
    public enum NONE
    {
    }
}
