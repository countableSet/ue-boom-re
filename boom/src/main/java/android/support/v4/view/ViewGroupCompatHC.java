// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.view;

import android.view.ViewGroup;

class ViewGroupCompatHC
{
    private ViewGroupCompatHC() {
    }
    
    public static void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean motionEventSplittingEnabled) {
        viewGroup.setMotionEventSplittingEnabled(motionEventSplittingEnabled);
    }
}
