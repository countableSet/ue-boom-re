// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.views;

import android.view.View;
import android.view.KeyEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View$OnKeyListener;
import android.widget.EditText;

public class EditTextWithPreIme extends EditText
{
    private View$OnKeyListener mPreImeListener;
    
    public EditTextWithPreIme(final Context context) {
        super(context);
    }
    
    public EditTextWithPreIme(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public EditTextWithPreIme(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
        boolean b;
        if (n == 4 && this.mPreImeListener != null) {
            b = this.mPreImeListener.onKey((View)this, n, keyEvent);
        }
        else {
            b = super.onKeyPreIme(n, keyEvent);
        }
        return b;
    }
    
    public void setPreImeListener(final View$OnKeyListener mPreImeListener) {
        this.mPreImeListener = mPreImeListener;
    }
}
