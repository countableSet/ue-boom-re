// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.graphics.Paint;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

public class SoftImageView extends AppCompatImageView
{
    public SoftImageView(final Context context) {
        this(context, null);
    }
    
    public SoftImageView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SoftImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setLayerType(1, (Paint)null);
    }
}
