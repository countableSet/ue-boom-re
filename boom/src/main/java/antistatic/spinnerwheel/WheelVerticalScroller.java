// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.view.MotionEvent;
import android.content.Context;

public class WheelVerticalScroller extends WheelScroller
{
    public WheelVerticalScroller(final Context context, final ScrollingListener scrollingListener) {
        super(context, scrollingListener);
    }
    
    @Override
    protected int getCurrentScrollerPosition() {
        return this.scroller.getCurrY();
    }
    
    @Override
    protected int getFinalScrollerPosition() {
        return this.scroller.getFinalY();
    }
    
    @Override
    protected float getMotionEventPosition(final MotionEvent motionEvent) {
        return motionEvent.getY();
    }
    
    @Override
    protected void scrollerFling(final int n, final int n2, final int n3) {
        this.scroller.fling(0, n, 0, -n3, 0, 0, -2147483647, Integer.MAX_VALUE);
    }
    
    @Override
    protected void scrollerStartScroll(final int n, final int n2) {
        this.scroller.startScroll(0, 0, 0, n, n2);
    }
}
