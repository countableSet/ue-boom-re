// 
// Decompiled by Procyon v0.5.36
// 

package me.grantland.widget;

import android.text.Editable;
import java.util.Iterator;
import android.os.Build$VERSION;
import android.text.method.SingleLineTransformationMethod;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import android.util.TypedValue;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.content.Context;
import android.text.method.TransformationMethod;
import android.content.res.Resources;
import android.view.View;
import android.text.TextWatcher;
import android.widget.TextView;
import android.text.TextPaint;
import android.view.View$OnLayoutChangeListener;
import java.util.ArrayList;

public class AutofitHelper
{
    private static final int DEFAULT_MIN_TEXT_SIZE = 8;
    private static final float DEFAULT_PRECISION = 0.5f;
    private static final boolean SPEW = false;
    private static final String TAG = "AutoFitTextHelper";
    private boolean mEnabled;
    private boolean mIsAutofitting;
    private ArrayList<OnTextSizeChangeListener> mListeners;
    private int mMaxLines;
    private float mMaxTextSize;
    private float mMinTextSize;
    private View$OnLayoutChangeListener mOnLayoutChangeListener;
    private TextPaint mPaint;
    private float mPrecision;
    private float mTextSize;
    private TextView mTextView;
    private TextWatcher mTextWatcher;
    
    private AutofitHelper(final TextView mTextView) {
        this.mTextWatcher = (TextWatcher)new AutofitTextWatcher();
        this.mOnLayoutChangeListener = (View$OnLayoutChangeListener)new AutofitOnLayoutChangeListener();
        final float scaledDensity = mTextView.getContext().getResources().getDisplayMetrics().scaledDensity;
        this.mTextView = mTextView;
        this.mPaint = new TextPaint();
        this.setRawTextSize(mTextView.getTextSize());
        this.mMaxLines = getMaxLines(mTextView);
        this.mMinTextSize = 8.0f * scaledDensity;
        this.mMaxTextSize = this.mTextSize;
        this.mPrecision = 0.5f;
    }
    
    private void autofit() {
        final float textSize = this.mTextView.getTextSize();
        this.mIsAutofitting = true;
        autofit(this.mTextView, this.mPaint, this.mMinTextSize, this.mMaxTextSize, this.mMaxLines, this.mPrecision);
        this.mIsAutofitting = false;
        final float textSize2 = this.mTextView.getTextSize();
        if (textSize2 != textSize) {
            this.sendTextSizeChange(textSize2, textSize);
        }
    }
    
    private static void autofit(final TextView textView, final TextPaint textPaint, final float n, float autofitTextSize, final int n2, float n3) {
        if (n2 > 0 && n2 != Integer.MAX_VALUE) {
            final int n4 = textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight();
            if (n4 > 0) {
                final CharSequence text = textView.getText();
                final TransformationMethod transformationMethod = textView.getTransformationMethod();
                CharSequence transformation = text;
                if (transformationMethod != null) {
                    transformation = transformationMethod.getTransformation(text, (View)textView);
                }
                final Context context = textView.getContext();
                Resources resources = Resources.getSystem();
                final float textSize = autofitTextSize;
                if (context != null) {
                    resources = context.getResources();
                }
                final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                textPaint.set(textView.getPaint());
                textPaint.setTextSize(textSize);
                Label_0181: {
                    if (n2 != 1 || textPaint.measureText(transformation, 0, transformation.length()) <= n4) {
                        autofitTextSize = textSize;
                        if (getLineCount(transformation, textPaint, textSize, (float)n4, displayMetrics) <= n2) {
                            break Label_0181;
                        }
                    }
                    autofitTextSize = getAutofitTextSize(transformation, textPaint, (float)n4, n2, 0.0f, textSize, n3, displayMetrics);
                }
                n3 = autofitTextSize;
                if (autofitTextSize < n) {
                    n3 = n;
                }
                textView.setTextSize(0, n3);
            }
        }
    }
    
    public static AutofitHelper create(final TextView textView) {
        return create(textView, null, 0);
    }
    
    public static AutofitHelper create(final TextView textView, final AttributeSet set) {
        return create(textView, set, 0);
    }
    
    public static AutofitHelper create(final TextView textView, final AttributeSet set, int dimensionPixelSize) {
        final AutofitHelper autofitHelper = new AutofitHelper(textView);
        boolean boolean1 = true;
        if (set != null) {
            final Context context = textView.getContext();
            final int n = (int)autofitHelper.getMinTextSize();
            final float precision = autofitHelper.getPrecision();
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AutofitTextView, dimensionPixelSize, 0);
            boolean1 = obtainStyledAttributes.getBoolean(R.styleable.AutofitTextView_sizeToFit, true);
            dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AutofitTextView_minTextSize, n);
            final float float1 = obtainStyledAttributes.getFloat(R.styleable.AutofitTextView_precision, precision);
            obtainStyledAttributes.recycle();
            autofitHelper.setMinTextSize(0, (float)dimensionPixelSize).setPrecision(float1);
        }
        autofitHelper.setEnabled(boolean1);
        return autofitHelper;
    }
    
    private static float getAutofitTextSize(final CharSequence charSequence, final TextPaint textPaint, final float n, final int n2, final float n3, final float n4, final float n5, final DisplayMetrics displayMetrics) {
        final float n6 = (n3 + n4) / 2.0f;
        int lineCount = 1;
        StaticLayout staticLayout = null;
        textPaint.setTextSize(TypedValue.applyDimension(0, n6, displayMetrics));
        if (n2 != 1) {
            staticLayout = new StaticLayout(charSequence, textPaint, (int)n, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            lineCount = staticLayout.getLineCount();
        }
        float n7;
        if (lineCount > n2) {
            if (n4 - n3 < n5) {
                n7 = n3;
            }
            else {
                n7 = getAutofitTextSize(charSequence, textPaint, n, n2, n3, n6, n5, displayMetrics);
            }
        }
        else if (lineCount < n2) {
            n7 = getAutofitTextSize(charSequence, textPaint, n, n2, n6, n4, n5, displayMetrics);
        }
        else {
            float n8 = 0.0f;
            float measureText;
            if (n2 == 1) {
                measureText = textPaint.measureText(charSequence, 0, charSequence.length());
            }
            else {
                int n9 = 0;
                while (true) {
                    measureText = n8;
                    if (n9 >= lineCount) {
                        break;
                    }
                    float lineWidth = n8;
                    if (staticLayout.getLineWidth(n9) > n8) {
                        lineWidth = staticLayout.getLineWidth(n9);
                    }
                    ++n9;
                    n8 = lineWidth;
                }
            }
            n7 = n3;
            if (n4 - n3 >= n5) {
                if (measureText > n) {
                    n7 = getAutofitTextSize(charSequence, textPaint, n, n2, n3, n6, n5, displayMetrics);
                }
                else if (measureText < n) {
                    n7 = getAutofitTextSize(charSequence, textPaint, n, n2, n6, n4, n5, displayMetrics);
                }
                else {
                    n7 = n6;
                }
            }
        }
        return n7;
    }
    
    private static int getLineCount(final CharSequence charSequence, final TextPaint textPaint, final float n, final float n2, final DisplayMetrics displayMetrics) {
        textPaint.setTextSize(TypedValue.applyDimension(0, n, displayMetrics));
        return new StaticLayout(charSequence, textPaint, (int)n2, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true).getLineCount();
    }
    
    private static int getMaxLines(final TextView textView) {
        int maxLines = -1;
        final TransformationMethod transformationMethod = textView.getTransformationMethod();
        if (transformationMethod != null && transformationMethod instanceof SingleLineTransformationMethod) {
            maxLines = 1;
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            maxLines = textView.getMaxLines();
        }
        return maxLines;
    }
    
    private void sendTextSizeChange(final float n, final float n2) {
        if (this.mListeners != null) {
            final Iterator<OnTextSizeChangeListener> iterator = this.mListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onTextSizeChange(n, n2);
            }
        }
    }
    
    private void setRawMaxTextSize(final float mMaxTextSize) {
        if (mMaxTextSize != this.mMaxTextSize) {
            this.mMaxTextSize = mMaxTextSize;
            this.autofit();
        }
    }
    
    private void setRawMinTextSize(final float mMinTextSize) {
        if (mMinTextSize != this.mMinTextSize) {
            this.mMinTextSize = mMinTextSize;
            this.autofit();
        }
    }
    
    private void setRawTextSize(final float mTextSize) {
        if (this.mTextSize != mTextSize) {
            this.mTextSize = mTextSize;
        }
    }
    
    public AutofitHelper addOnTextSizeChangeListener(final OnTextSizeChangeListener e) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<OnTextSizeChangeListener>();
        }
        this.mListeners.add(e);
        return this;
    }
    
    public int getMaxLines() {
        return this.mMaxLines;
    }
    
    public float getMaxTextSize() {
        return this.mMaxTextSize;
    }
    
    public float getMinTextSize() {
        return this.mMinTextSize;
    }
    
    public float getPrecision() {
        return this.mPrecision;
    }
    
    public float getTextSize() {
        return this.mTextSize;
    }
    
    public boolean isEnabled() {
        return this.mEnabled;
    }
    
    public AutofitHelper removeOnTextSizeChangeListener(final OnTextSizeChangeListener o) {
        if (this.mListeners != null) {
            this.mListeners.remove(o);
        }
        return this;
    }
    
    public AutofitHelper setEnabled(final boolean mEnabled) {
        if (this.mEnabled != mEnabled) {
            this.mEnabled = mEnabled;
            if (mEnabled) {
                this.mTextView.addTextChangedListener(this.mTextWatcher);
                this.mTextView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
                this.autofit();
            }
            else {
                this.mTextView.removeTextChangedListener(this.mTextWatcher);
                this.mTextView.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
                this.mTextView.setTextSize(0, this.mTextSize);
            }
        }
        return this;
    }
    
    public AutofitHelper setMaxLines(final int mMaxLines) {
        if (this.mMaxLines != mMaxLines) {
            this.mMaxLines = mMaxLines;
            this.autofit();
        }
        return this;
    }
    
    public AutofitHelper setMaxTextSize(final float n) {
        return this.setMaxTextSize(2, n);
    }
    
    public AutofitHelper setMaxTextSize(final int n, final float n2) {
        final Context context = this.mTextView.getContext();
        Resources resources = Resources.getSystem();
        if (context != null) {
            resources = context.getResources();
        }
        this.setRawMaxTextSize(TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
        return this;
    }
    
    public AutofitHelper setMinTextSize(final float n) {
        return this.setMinTextSize(2, n);
    }
    
    public AutofitHelper setMinTextSize(final int n, final float n2) {
        final Context context = this.mTextView.getContext();
        Resources resources = Resources.getSystem();
        if (context != null) {
            resources = context.getResources();
        }
        this.setRawMinTextSize(TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
        return this;
    }
    
    public AutofitHelper setPrecision(final float mPrecision) {
        if (this.mPrecision != mPrecision) {
            this.mPrecision = mPrecision;
            this.autofit();
        }
        return this;
    }
    
    public void setTextSize(final float n) {
        this.setTextSize(2, n);
    }
    
    public void setTextSize(final int n, final float n2) {
        if (!this.mIsAutofitting) {
            final Context context = this.mTextView.getContext();
            Resources resources = Resources.getSystem();
            if (context != null) {
                resources = context.getResources();
            }
            this.setRawTextSize(TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
        }
    }
    
    private class AutofitOnLayoutChangeListener implements View$OnLayoutChangeListener
    {
        public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
            AutofitHelper.this.autofit();
        }
    }
    
    private class AutofitTextWatcher implements TextWatcher
    {
        public void afterTextChanged(final Editable editable) {
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            AutofitHelper.this.autofit();
        }
    }
    
    public interface OnTextSizeChangeListener
    {
        void onTextSizeChange(final float p0, final float p1);
    }
}
