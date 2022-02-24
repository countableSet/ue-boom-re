// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.ViewPager;

public class CustomViewPager extends ViewPager
{
    public CustomViewPager(final Context context) {
        super(context);
    }
    
    public CustomViewPager(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    @Override
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.isEnabled() && super.onInterceptTouchEvent(motionEvent);
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.isEnabled() && super.onTouchEvent(motionEvent);
    }
}
