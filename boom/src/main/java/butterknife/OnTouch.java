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

@ListenerClass(method = { @ListenerMethod(defaultReturn = "false", name = "onTouch", parameters = { "android.view.View", "android.view.MotionEvent" }, returnType = "boolean") }, setter = "setOnTouchListener", targetType = "android.view.View", type = "android.view.View.OnTouchListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnTouch {
    int[] value() default { -1 };
}
