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

@ListenerClass(method = { @ListenerMethod(name = "doClick", parameters = { "android.view.View" }) }, setter = "setOnClickListener", targetType = "android.view.View", type = "butterknife.internal.DebouncingOnClickListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnClick {
    int[] value() default { -1 };
}
