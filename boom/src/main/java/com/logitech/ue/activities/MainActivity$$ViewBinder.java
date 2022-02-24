// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextSwitcher;
import com.logitech.ue.views.TabIndicator;
import android.view.View;
import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;

public class MainActivity$$ViewBinder<T extends MainActivity> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mViewPager = finder.castView(finder.findRequiredView(o, 2131624043, "field 'mViewPager'"), 2131624043, "field 'mViewPager'");
        t.mTabIndicator = finder.castView(finder.findRequiredView(o, 16908306, "field 'mTabIndicator'"), 16908306, "field 'mTabIndicator'");
        t.mTitleSwitcher = finder.castView(finder.findRequiredView(o, 16908310, "field 'mTitleSwitcher'"), 16908310, "field 'mTitleSwitcher'");
        t.mToolbar = finder.castView(finder.findRequiredView(o, 2131624031, "field 'mToolbar'"), 2131624031, "field 'mToolbar'");
        t.mToolbarBackground = finder.castView(finder.findRequiredView(o, 2131624030, "field 'mToolbarBackground'"), 2131624030, "field 'mToolbarBackground'");
    }
    
    public void unbind(final T t) {
        t.mViewPager = null;
        t.mTabIndicator = null;
        t.mTitleSwitcher = null;
        t.mToolbar = null;
        t.mToolbarBackground = null;
    }
}
