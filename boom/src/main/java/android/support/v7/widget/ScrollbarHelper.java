// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.view.View;

class ScrollbarHelper
{
    static int computeScrollExtent(final RecyclerView.State state, final OrientationHelper orientationHelper, final View view, final View view2, final RecyclerView.LayoutManager layoutManager, final boolean b) {
        int min;
        if (layoutManager.getChildCount() == 0 || state.getItemCount() == 0 || view == null || view2 == null) {
            min = 0;
        }
        else if (!b) {
            min = Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1;
        }
        else {
            min = Math.min(orientationHelper.getTotalSpace(), orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view));
        }
        return min;
    }
    
    static int computeScrollOffset(final RecyclerView.State state, final OrientationHelper orientationHelper, final View view, final View view2, final RecyclerView.LayoutManager layoutManager, final boolean b, final boolean b2) {
        int round;
        final int n = round = 0;
        if (layoutManager.getChildCount() != 0) {
            round = n;
            if (state.getItemCount() != 0) {
                round = n;
                if (view != null) {
                    if (view2 == null) {
                        round = n;
                    }
                    else {
                        final int min = Math.min(layoutManager.getPosition(view), layoutManager.getPosition(view2));
                        final int max = Math.max(layoutManager.getPosition(view), layoutManager.getPosition(view2));
                        int n2;
                        if (b2) {
                            n2 = Math.max(0, state.getItemCount() - max - 1);
                        }
                        else {
                            n2 = Math.max(0, min);
                        }
                        round = n2;
                        if (b) {
                            round = Math.round(n2 * (Math.abs(orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view)) / (float)(Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1)) + (orientationHelper.getStartAfterPadding() - orientationHelper.getDecoratedStart(view)));
                        }
                    }
                }
            }
        }
        return round;
    }
    
    static int computeScrollRange(final RecyclerView.State state, final OrientationHelper orientationHelper, final View view, final View view2, final RecyclerView.LayoutManager layoutManager, final boolean b) {
        int itemCount;
        if (layoutManager.getChildCount() == 0 || state.getItemCount() == 0 || view == null || view2 == null) {
            itemCount = 0;
        }
        else if (!b) {
            itemCount = state.getItemCount();
        }
        else {
            itemCount = (int)((orientationHelper.getDecoratedEnd(view2) - orientationHelper.getDecoratedStart(view)) / (float)(Math.abs(layoutManager.getPosition(view) - layoutManager.getPosition(view2)) + 1) * state.getItemCount());
        }
        return itemCount;
    }
}
