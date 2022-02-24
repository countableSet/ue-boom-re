// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.text.TextPaint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.MetricAffectingSpan;

public class CalligraphyTypefaceSpan extends MetricAffectingSpan
{
    private final Typeface typeface;
    
    public CalligraphyTypefaceSpan(final Typeface typeface) {
        if (typeface == null) {
            throw new IllegalArgumentException("typeface is null");
        }
        this.typeface = typeface;
    }
    
    private void apply(final Paint paint) {
        final Typeface typeface = paint.getTypeface();
        int style;
        if (typeface != null) {
            style = typeface.getStyle();
        }
        else {
            style = 0;
        }
        final int n = style & ~this.typeface.getStyle();
        if ((n & 0x1) != 0x0) {
            paint.setFakeBoldText(true);
        }
        if ((n & 0x2) != 0x0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(this.typeface);
    }
    
    public void updateDrawState(final TextPaint textPaint) {
        this.apply((Paint)textPaint);
    }
    
    public void updateMeasureState(final TextPaint textPaint) {
        this.apply((Paint)textPaint);
    }
}
