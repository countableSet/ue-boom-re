// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.content.res.Resources;
import butterknife.ButterKnife;

public class SpiderFrameLayout$$ViewBinder<T extends SpiderFrameLayout> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        final Resources resources = finder.getContext(o).getResources();
        t.mDotSpeakerWidth = resources.getDimension(2131361874);
        t.mDotSpeakerHeight = resources.getDimension(2131361873);
        t.mNormalSpeakerWidth = resources.getDimension(2131361903);
        t.mNormalSpeakerHeight = resources.getDimension(2131361902);
        t.mDoubleUpDeviceMargin = resources.getDimension(2131361867);
    }
    
    public void unbind(final T t) {
    }
}
