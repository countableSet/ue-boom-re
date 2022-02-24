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

@ListenerClass(method = { @ListenerMethod(defaultReturn = "false", name = "onLongClick", parameters = { "android.view.View" }, returnType = "boolean") }, setter = "setOnLongClickListener", targetType = "android.view.View", type = "android.view.View.OnLongClickListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnLongClick {
    int[] value() default { -1 };
}
