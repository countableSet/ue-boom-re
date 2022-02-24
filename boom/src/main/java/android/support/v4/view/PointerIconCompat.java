// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.view;

import android.content.res.Resources;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.os.BuildCompat;

public final class PointerIconCompat
{
    static final PointerIconCompatImpl IMPL;
    public static final int TYPE_ALIAS = 1010;
    public static final int TYPE_ALL_SCROLL = 1013;
    public static final int TYPE_ARROW = 1000;
    public static final int TYPE_CELL = 1006;
    public static final int TYPE_CONTEXT_MENU = 1001;
    public static final int TYPE_COPY = 1011;
    public static final int TYPE_CROSSHAIR = 1007;
    public static final int TYPE_DEFAULT = 1000;
    public static final int TYPE_GRAB = 1020;
    public static final int TYPE_GRABBING = 1021;
    public static final int TYPE_HAND = 1002;
    public static final int TYPE_HELP = 1003;
    public static final int TYPE_HORIZONTAL_DOUBLE_ARROW = 1014;
    public static final int TYPE_NO_DROP = 1012;
    public static final int TYPE_NULL = 0;
    public static final int TYPE_TEXT = 1008;
    public static final int TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW = 1017;
    public static final int TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = 1016;
    public static final int TYPE_VERTICAL_DOUBLE_ARROW = 1015;
    public static final int TYPE_VERTICAL_TEXT = 1009;
    public static final int TYPE_WAIT = 1004;
    public static final int TYPE_ZOOM_IN = 1018;
    public static final int TYPE_ZOOM_OUT = 1019;
    private Object mPointerIcon;
    
    static {
        if (BuildCompat.isAtLeastN()) {
            IMPL = (PointerIconCompatImpl)new Api24PointerIconCompatImpl();
        }
        else {
            IMPL = (PointerIconCompatImpl)new BasePointerIconCompatImpl();
        }
    }
    
    private PointerIconCompat(final Object mPointerIcon) {
        this.mPointerIcon = mPointerIcon;
    }
    
    public static PointerIconCompat create(final Bitmap bitmap, final float n, final float n2) {
        return new PointerIconCompat(PointerIconCompat.IMPL.create(bitmap, n, n2));
    }
    
    public static PointerIconCompat getSystemIcon(final Context context, final int n) {
        return new PointerIconCompat(PointerIconCompat.IMPL.getSystemIcon(context, n));
    }
    
    public static PointerIconCompat load(final Resources resources, final int n) {
        return new PointerIconCompat(PointerIconCompat.IMPL.load(resources, n));
    }
    
    public Object getPointerIcon() {
        return this.mPointerIcon;
    }
    
    static class Api24PointerIconCompatImpl extends BasePointerIconCompatImpl
    {
        @Override
        public Object create(final Bitmap bitmap, final float n, final float n2) {
            return PointerIconCompatApi24.create(bitmap, n, n2);
        }
        
        @Override
        public Object getSystemIcon(final Context context, final int n) {
            return PointerIconCompatApi24.getSystemIcon(context, n);
        }
        
        @Override
        public Object load(final Resources resources, final int n) {
            return PointerIconCompatApi24.load(resources, n);
        }
    }
    
    static class BasePointerIconCompatImpl implements PointerIconCompatImpl
    {
        @Override
        public Object create(final Bitmap bitmap, final float n, final float n2) {
            return null;
        }
        
        @Override
        public Object getSystemIcon(final Context context, final int n) {
            return null;
        }
        
        @Override
        public Object load(final Resources resources, final int n) {
            return null;
        }
    }
    
    interface PointerIconCompatImpl
    {
        Object create(final Bitmap p0, final float p1, final float p2);
        
        Object getSystemIcon(final Context p0, final int p1);
        
        Object load(final Resources p0, final int p1);
    }
}
