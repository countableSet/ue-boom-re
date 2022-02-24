// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.view.MotionEvent;
import android.content.Context;

public class WheelHorizontalScroller extends WheelScroller
{
    public WheelHorizontalScroller(final Context context, final ScrollingListener scrollingListener) {
        super(context, scrollingListener);
    }
    
    @Override
    protected int getCurrentScrollerPosition() {
        return this.scroller.getCurrX();
    }
    
    @Override
    protected int getFinalScrollerPosition() {
        return this.scroller.getFinalX();
    }
    
    @Override
    protected float getMotionEventPosition(final MotionEvent motionEvent) {
        return motionEvent.getX();
    }
    
    @Override
    protected void scrollerFling(final int n, final int n2, final int n3) {
        this.scroller.fling(n, 0, -n2, 0, -2147483647, Integer.MAX_VALUE, 0, 0);
    }
    
    @Override
    protected void scrollerStartScroll(final int n, final int n2) {
        this.scroller.startScroll(0, 0, n, 0, n2);
    }
}
