// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.os;

import android.os.Build$VERSION;

public class BuildCompat
{
    private BuildCompat() {
    }
    
    public static boolean isAtLeastN() {
        return Build$VERSION.SDK_INT >= 24;
    }
    
    public static boolean isAtLeastNMR1() {
        return Build$VERSION.SDK_INT >= 25;
    }
}
