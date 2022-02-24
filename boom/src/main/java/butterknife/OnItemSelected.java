// 
// Decompiled by Procyon v0.5.36
// 

package butterknife;

import butterknife.internal.ListenerMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import butterknife.internal.ListenerClass;
import java.lang.annotation.Annotation;

@ListenerClass(callbacks = Callback.class, setter = "setOnItemSelectedListener", targetType = "android.widget.AdapterView<?>", type = "android.widget.AdapterView.OnItemSelectedListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnItemSelected {
    Callback callback() default Callback.ITEM_SELECTED;
    
    int[] value() default { -1 };
    
    public enum Callback
    {
        @ListenerMethod(name = "onItemSelected", parameters = { "android.widget.AdapterView<?>", "android.view.View", "int", "long" })
        ITEM_SELECTED, 
        @ListenerMethod(name = "onNothingSelected", parameters = { "android.widget.AdapterView<?>" })
        NOTHING_SELECTED;
    }
}
