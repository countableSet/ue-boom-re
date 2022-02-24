// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.view.View;
import butterknife.ButterKnife;

public class IntroFragment$$ViewBinder<T extends IntroFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mPlusVerticalLine = finder.findRequiredView(o, 2131624157, "field 'mPlusVerticalLine'");
        t.mPlusHorizontalLine = finder.findRequiredView(o, 2131624158, "field 'mPlusHorizontalLine'");
        t.mMinusVerticalLine = finder.findRequiredView(o, 2131624159, "field 'mMinusVerticalLine'");
        t.mBackground = finder.findRequiredView(o, 2131624030, "field 'mBackground'");
    }
    
    public void unbind(final T t) {
        t.mPlusVerticalLine = null;
        t.mPlusHorizontalLine = null;
        t.mMinusVerticalLine = null;
        t.mBackground = null;
    }
}
