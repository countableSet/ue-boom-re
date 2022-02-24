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

@ListenerClass(callbacks = Callback.class, setter = "setOnPageChangeListener", targetType = "android.support.v4.view.ViewPager", type = "android.support.v4.view.ViewPager.OnPageChangeListener")
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface OnPageChange {
    Callback callback() default Callback.PAGE_SELECTED;
    
    int[] value() default { -1 };
    
    public enum Callback
    {
        @ListenerMethod(name = "onPageScrolled", parameters = { "int", "float", "int" })
        PAGE_SCROLLED, 
        @ListenerMethod(name = "onPageScrollStateChanged", parameters = { "int" })
        PAGE_SCROLL_STATE_CHANGED, 
        @ListenerMethod(name = "onPageSelected", parameters = { "int" })
        PAGE_SELECTED;
    }
}
