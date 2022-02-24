// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.SeekBar;

public class AppCompatSeekBar extends SeekBar
{
    private AppCompatSeekBarHelper mAppCompatSeekBarHelper;
    private AppCompatDrawableManager mDrawableManager;
    
    public AppCompatSeekBar(final Context context) {
        this(context, null);
    }
    
    public AppCompatSeekBar(final Context context, final AttributeSet set) {
        this(context, set, R.attr.seekBarStyle);
    }
    
    public AppCompatSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mDrawableManager = AppCompatDrawableManager.get();
        (this.mAppCompatSeekBarHelper = new AppCompatSeekBarHelper(this, this.mDrawableManager)).loadFromAttributes(set, n);
    }
}
