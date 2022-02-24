// 
// Decompiled by Procyon v0.5.36
// 

package me.grantland.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;

public class AutofitTextView extends TextView implements OnTextSizeChangeListener
{
    private AutofitHelper mHelper;
    
    public AutofitTextView(final Context context) {
        super(context);
        this.init(context, null, 0);
    }
    
    public AutofitTextView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init(context, set, 0);
    }
    
    public AutofitTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init(context, set, n);
    }
    
    private void init(final Context context, final AttributeSet set, final int n) {
        this.mHelper = AutofitHelper.create(this, set, n).addOnTextSizeChangeListener((AutofitHelper.OnTextSizeChangeListener)this);
    }
    
    public AutofitHelper getAutofitHelper() {
        return this.mHelper;
    }
    
    public float getMaxTextSize() {
        return this.mHelper.getMaxTextSize();
    }
    
    public float getMinTextSize() {
        return this.mHelper.getMinTextSize();
    }
    
    public float getPrecision() {
        return this.mHelper.getPrecision();
    }
    
    public boolean isSizeToFit() {
        return this.mHelper.isEnabled();
    }
    
    public void onTextSizeChange(final float n, final float n2) {
    }
    
    public void setLines(final int n) {
        super.setLines(n);
        if (this.mHelper != null) {
            this.mHelper.setMaxLines(n);
        }
    }
    
    public void setMaxLines(final int n) {
        super.setMaxLines(n);
        if (this.mHelper != null) {
            this.mHelper.setMaxLines(n);
        }
    }
    
    public void setMaxTextSize(final float maxTextSize) {
        this.mHelper.setMaxTextSize(maxTextSize);
    }
    
    public void setMaxTextSize(final int n, final float n2) {
        this.mHelper.setMaxTextSize(n, n2);
    }
    
    public void setMinTextSize(final int n) {
        this.mHelper.setMinTextSize(2, (float)n);
    }
    
    public void setMinTextSize(final int n, final float n2) {
        this.mHelper.setMinTextSize(n, n2);
    }
    
    public void setPrecision(final float precision) {
        this.mHelper.setPrecision(precision);
    }
    
    public void setSizeToFit() {
        this.setSizeToFit(true);
    }
    
    public void setSizeToFit(final boolean enabled) {
        this.mHelper.setEnabled(enabled);
    }
    
    public void setTextSize(final int n, final float n2) {
        super.setTextSize(n, n2);
        if (this.mHelper != null) {
            this.mHelper.setTextSize(n, n2);
        }
    }
}
