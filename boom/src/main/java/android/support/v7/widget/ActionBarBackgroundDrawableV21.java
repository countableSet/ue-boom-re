// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.graphics.Outline;

class ActionBarBackgroundDrawableV21 extends ActionBarBackgroundDrawable
{
    public ActionBarBackgroundDrawableV21(final ActionBarContainer actionBarContainer) {
        super(actionBarContainer);
    }
    
    public void getOutline(@NonNull final Outline outline) {
        if (this.mContainer.mIsSplit) {
            if (this.mContainer.mSplitBackground != null) {
                this.mContainer.mSplitBackground.getOutline(outline);
            }
        }
        else if (this.mContainer.mBackground != null) {
            this.mContainer.mBackground.getOutline(outline);
        }
    }
}
