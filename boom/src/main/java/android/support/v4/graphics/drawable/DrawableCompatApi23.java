// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;

class DrawableCompatApi23
{
    public static int getLayoutDirection(final Drawable drawable) {
        return drawable.getLayoutDirection();
    }
    
    public static boolean setLayoutDirection(final Drawable drawable, final int layoutDirection) {
        return drawable.setLayoutDirection(layoutDirection);
    }
}
