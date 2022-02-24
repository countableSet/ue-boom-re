// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import android.graphics.Bitmap;
import android.graphics.Typeface;

public abstract class SVGExternalFileResolver
{
    public boolean isFormatSupported(final String s) {
        return false;
    }
    
    public Typeface resolveFont(final String s, final int n, final String s2) {
        return null;
    }
    
    public Bitmap resolveImage(final String s) {
        return null;
    }
}
