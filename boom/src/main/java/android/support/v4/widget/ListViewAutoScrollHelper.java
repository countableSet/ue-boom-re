// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper
{
    private final ListView mTarget;
    
    public ListViewAutoScrollHelper(final ListView mTarget) {
        super((View)mTarget);
        this.mTarget = mTarget;
    }
    
    @Override
    public boolean canTargetScrollHorizontally(final int n) {
        return false;
    }
    
    @Override
    public boolean canTargetScrollVertically(final int n) {
        final boolean b = false;
        final ListView mTarget = this.mTarget;
        final int count = mTarget.getCount();
        boolean b2;
        if (count == 0) {
            b2 = b;
        }
        else {
            final int childCount = mTarget.getChildCount();
            final int firstVisiblePosition = mTarget.getFirstVisiblePosition();
            if (n > 0) {
                if (firstVisiblePosition + childCount >= count) {
                    b2 = b;
                    if (mTarget.getChildAt(childCount - 1).getBottom() <= mTarget.getHeight()) {
                        return b2;
                    }
                }
            }
            else {
                b2 = b;
                if (n >= 0) {
                    return b2;
                }
                if (firstVisiblePosition <= 0 && mTarget.getChildAt(0).getTop() >= 0) {
                    b2 = b;
                    return b2;
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    @Override
    public void scrollTargetBy(final int n, final int n2) {
        ListViewCompat.scrollListBy(this.mTarget, n2);
    }
}
