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

@ListenerClass(method = { @ListenerMethod(name = "onItemClick", parameters = { "android.widget.AdapterView<?>", "android.view.View", "int", "long" }) }, setter = "setOnItemClickListener", targetType = "android.widget.AdapterView<?>", type = "android.widget.AdapterView.OnItemClickListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnItemClick {
    int[] value() default { -1 };
}
