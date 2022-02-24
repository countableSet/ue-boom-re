// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.graphics.PorterDuff$Mode;
import java.lang.reflect.Field;
import android.util.Log;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable$ConstantState;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.graphics.Rect;

class DrawableUtils
{
    public static final Rect INSETS_NONE;
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;
    
    static {
        INSETS_NONE = new Rect();
        if (Build$VERSION.SDK_INT < 18) {
            return;
        }
        try {
            DrawableUtils.sInsetsClazz = Class.forName("android.graphics.Insets");
        }
        catch (ClassNotFoundException ex) {}
    }
    
    private DrawableUtils() {
    }
    
    static boolean canSafelyMutateDrawable(@NonNull final Drawable drawable) {
        final boolean b = true;
        boolean b2;
        if (drawable instanceof LayerDrawable) {
            b2 = (Build$VERSION.SDK_INT >= 16 && b);
        }
        else if (drawable instanceof InsetDrawable) {
            b2 = b;
            if (Build$VERSION.SDK_INT < 14) {
                b2 = false;
            }
        }
        else if (drawable instanceof StateListDrawable) {
            b2 = b;
            if (Build$VERSION.SDK_INT < 8) {
                b2 = false;
            }
        }
        else if (drawable instanceof GradientDrawable) {
            b2 = b;
            if (Build$VERSION.SDK_INT < 14) {
                b2 = false;
            }
        }
        else if (drawable instanceof DrawableContainer) {
            final Drawable$ConstantState constantState = drawable.getConstantState();
            b2 = b;
            if (constantState instanceof DrawableContainer$DrawableContainerState) {
                final Drawable[] children = ((DrawableContainer$DrawableContainerState)constantState).getChildren();
                final int length = children.length;
                int n = 0;
                while (true) {
                    b2 = b;
                    if (n >= length) {
                        return b2;
                    }
                    if (!canSafelyMutateDrawable(children[n])) {
                        break;
                    }
                    ++n;
                }
                b2 = false;
            }
        }
        else if (drawable instanceof DrawableWrapper) {
            b2 = canSafelyMutateDrawable(((DrawableWrapper)drawable).getWrappedDrawable());
        }
        else {
            b2 = b;
            if (drawable instanceof android.support.v7.graphics.drawable.DrawableWrapper) {
                b2 = canSafelyMutateDrawable(((android.support.v7.graphics.drawable.DrawableWrapper)drawable).getWrappedDrawable());
            }
        }
        return b2;
    }
    
    static void fixDrawable(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName())) {
            fixVectorDrawableTinting(drawable);
        }
    }
    
    private static void fixVectorDrawableTinting(final Drawable drawable) {
        final int[] state = drawable.getState();
        if (state == null || state.length == 0) {
            drawable.setState(ThemeUtils.CHECKED_STATE_SET);
        }
        else {
            drawable.setState(ThemeUtils.EMPTY_STATE_SET);
        }
        drawable.setState(state);
    }
    
    public static Rect getOpticalBounds(Drawable unwrap) {
        if (DrawableUtils.sInsetsClazz != null) {
            while (true) {
                while (true) {
                    Object invoke = null;
                    Rect rect = null;
                    Field field = null;
                    Label_0280: {
                        Label_0267: {
                            Label_0254: {
                                try {
                                    unwrap = DrawableCompat.unwrap(unwrap);
                                    invoke = unwrap.getClass().getMethod("getOpticalInsets", (Class<?>[])new Class[0]).invoke(unwrap, new Object[0]);
                                    if (invoke != null) {
                                        rect = new Rect();
                                        final Field[] fields = DrawableUtils.sInsetsClazz.getFields();
                                        final int length = fields.length;
                                        int n = 0;
                                        while (true) {
                                            final Rect insets_NONE = rect;
                                            if (n >= length) {
                                                return insets_NONE;
                                            }
                                            field = fields[n];
                                            final String name = field.getName();
                                            switch (name) {
                                                case "left": {
                                                    rect.left = field.getInt(invoke);
                                                    break;
                                                }
                                                case "top": {
                                                    break Label_0254;
                                                }
                                                case "right": {
                                                    break Label_0267;
                                                }
                                                case "bottom": {
                                                    break Label_0280;
                                                }
                                            }
                                            ++n;
                                        }
                                    }
                                }
                                catch (Exception ex) {
                                    Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
                                }
                                break;
                            }
                            rect.top = field.getInt(invoke);
                            continue;
                        }
                        rect.right = field.getInt(invoke);
                        continue;
                    }
                    rect.bottom = field.getInt(invoke);
                    continue;
                }
            }
        }
        return DrawableUtils.INSETS_NONE;
    }
    
    static PorterDuff$Mode parseTintMode(final int n, final PorterDuff$Mode porterDuff$Mode) {
        PorterDuff$Mode porterDuff$Mode2 = porterDuff$Mode;
        switch (n) {
            default: {
                porterDuff$Mode2 = porterDuff$Mode;
                return porterDuff$Mode2;
            }
            case 15: {
                porterDuff$Mode2 = PorterDuff$Mode.SCREEN;
                return porterDuff$Mode2;
            }
            case 14: {
                porterDuff$Mode2 = PorterDuff$Mode.MULTIPLY;
                return porterDuff$Mode2;
            }
            case 9: {
                porterDuff$Mode2 = PorterDuff$Mode.SRC_ATOP;
                return porterDuff$Mode2;
            }
            case 5: {
                porterDuff$Mode2 = PorterDuff$Mode.SRC_IN;
                return porterDuff$Mode2;
            }
            case 3: {
                porterDuff$Mode2 = PorterDuff$Mode.SRC_OVER;
            }
            case 4:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13: {
                return porterDuff$Mode2;
            }
            case 16: {
                porterDuff$Mode2 = porterDuff$Mode;
                if (Build$VERSION.SDK_INT >= 11) {
                    porterDuff$Mode2 = PorterDuff$Mode.valueOf("ADD");
                    return porterDuff$Mode2;
                }
                return porterDuff$Mode2;
            }
        }
    }
}
