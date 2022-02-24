// 
// Decompiled by Procyon v0.5.36
// 

package butterknife;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import butterknife.internal.ListenerMethod;
import butterknife.internal.ListenerClass;
import java.lang.annotation.Annotation;

@ListenerClass(method = { @ListenerMethod(name = "onFocusChange", parameters = { "android.view.View", "boolean" }) }, setter = "setOnFocusChangeListener", targetType = "android.view.View", type = "android.view.View.OnFocusChangeListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnFocusChange {
    int[] value() default { -1 };
}
