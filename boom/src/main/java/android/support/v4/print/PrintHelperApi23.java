// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.print;

import android.print.PrintAttributes$Builder;
import android.print.PrintAttributes;
import android.content.Context;

class PrintHelperApi23 extends PrintHelperApi20
{
    PrintHelperApi23(final Context context) {
        super(context);
        this.mIsMinMarginsHandlingCorrect = false;
    }
    
    @Override
    protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
        final PrintAttributes$Builder copyAttributes = super.copyAttributes(printAttributes);
        if (printAttributes.getDuplexMode() != 0) {
            copyAttributes.setDuplexMode(printAttributes.getDuplexMode());
        }
        return copyAttributes;
    }
}
