// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.content.res.Resources;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import com.logitech.ue.views.UEDeviceView;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;

public class XUPTutorialActivity$$ViewBinder<T extends XUPTutorialActivity> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mTutorialMainView = finder.castView(finder.findRequiredView(o, 2131624074, "field 'mTutorialMainView'"), 2131624074, "field 'mTutorialMainView'");
        t.mTutorialViewPager = finder.castView(finder.findRequiredView(o, 2131624078, "field 'mTutorialViewPager'"), 2131624078, "field 'mTutorialViewPager'");
        t.mMasterDeviceView = finder.castView(finder.findRequiredView(o, 2131624079, "field 'mMasterDeviceView'"), 2131624079, "field 'mMasterDeviceView'");
        t.mFirstRightDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624082, "field 'mFirstRightDrawerDeviceView'"), 2131624082, "field 'mFirstRightDrawerDeviceView'");
        t.mFirstLeftDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624083, "field 'mFirstLeftDrawerDeviceView'"), 2131624083, "field 'mFirstLeftDrawerDeviceView'");
        t.mSecondRightDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624084, "field 'mSecondRightDrawerDeviceView'"), 2131624084, "field 'mSecondRightDrawerDeviceView'");
        t.mSecondLeftDrawerDeviceView = finder.castView(finder.findRequiredView(o, 2131624085, "field 'mSecondLeftDrawerDeviceView'"), 2131624085, "field 'mSecondLeftDrawerDeviceView'");
        finder.findRequiredView(o, 2131624077, "method 'onCloseClicked'").setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onCloseClicked(view);
            }
        });
        final Resources resources = finder.getContext(o).getResources();
        t.mPageIndicatorDotSize = resources.getDimensionPixelSize(2131361907);
        t.mDrawerSpeakerWidth = resources.getDimensionPixelSize(2131361915);
        t.mDrawerSpeakerSpacing = resources.getDimensionPixelSize(2131361870);
        t.mDotDeviceSize = resources.getDimensionPixelSize(2131361873);
    }
    
    public void unbind(final T t) {
        t.mTutorialMainView = null;
        t.mTutorialViewPager = null;
        t.mMasterDeviceView = null;
        t.mFirstRightDrawerDeviceView = null;
        t.mFirstLeftDrawerDeviceView = null;
        t.mSecondRightDrawerDeviceView = null;
        t.mSecondLeftDrawerDeviceView = null;
    }
}
