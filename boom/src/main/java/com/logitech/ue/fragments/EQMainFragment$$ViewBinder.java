// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.SeekBar;
import android.view.View;
import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;

public class EQMainFragment$$ViewBinder<T extends EQMainFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mEQViewPager = finder.castView(finder.findRequiredView(o, 2131624122, "field 'mEQViewPager'"), 2131624122, "field 'mEQViewPager'");
        t.mBassLeftSeekBar = finder.castView(finder.findRequiredView(o, 2131624127, "field 'mBassLeftSeekBar'"), 2131624127, "field 'mBassLeftSeekBar'");
        t.mBassRightSeekBar = finder.castView(finder.findRequiredView(o, 2131624129, "field 'mBassRightSeekBar'"), 2131624129, "field 'mBassRightSeekBar'");
        t.mMidSeekBar = finder.castView(finder.findRequiredView(o, 2131624131, "field 'mMidSeekBar'"), 2131624131, "field 'mMidSeekBar'");
        t.mTrebleLeftSeekBar = finder.castView(finder.findRequiredView(o, 2131624133, "field 'mTrebleLeftSeekBar'"), 2131624133, "field 'mTrebleLeftSeekBar'");
        t.mTrebleRightSeekBar = finder.castView(finder.findRequiredView(o, 2131624135, "field 'mTrebleRightSeekBar'"), 2131624135, "field 'mTrebleRightSeekBar'");
    }
    
    public void unbind(final T t) {
        t.mEQViewPager = null;
        t.mBassLeftSeekBar = null;
        t.mBassRightSeekBar = null;
        t.mMidSeekBar = null;
        t.mTrebleLeftSeekBar = null;
        t.mTrebleRightSeekBar = null;
    }
}
