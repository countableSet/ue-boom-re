// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.graphics.Bitmap$Config;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.animation.Animator;

public abstract class AbstractWheelView extends AbstractWheel
{
    protected static final int DEF_ITEMS_DIMMED_ALPHA = 50;
    protected static final int DEF_ITEM_OFFSET_PERCENT = 10;
    protected static final int DEF_ITEM_PADDING = 10;
    protected static final int DEF_SELECTION_DIVIDER_ACTIVE_ALPHA = 70;
    protected static final int DEF_SELECTION_DIVIDER_DIMMED_ALPHA = 70;
    protected static final int DEF_SELECTION_DIVIDER_SIZE = 2;
    protected static final String PROPERTY_SELECTOR_PAINT_COEFF = "selectorPaintCoeff";
    protected static final String PROPERTY_SEPARATORS_PAINT_ALPHA = "separatorsPaintAlpha";
    private static int itemID;
    private final String LOG_TAG;
    protected Animator mDimSelectorWheelAnimator;
    protected Animator mDimSeparatorsAnimator;
    protected int mItemOffsetPercent;
    protected int mItemsDimmedAlpha;
    protected int mItemsPadding;
    protected Drawable mSelectionDivider;
    protected int mSelectionDividerActiveAlpha;
    protected int mSelectionDividerDimmedAlpha;
    protected Paint mSelectorWheelPaint;
    protected Bitmap mSeparatorsBitmap;
    protected Paint mSeparatorsPaint;
    protected Bitmap mSpinBitmap;
    
    static {
        AbstractWheelView.itemID = -1;
    }
    
    public AbstractWheelView(final Context context, final AttributeSet set, int i) {
        super(context, set, i);
        final StringBuilder append = new StringBuilder().append(AbstractWheelView.class.getName()).append(" #");
        i = ++AbstractWheelView.itemID;
        this.LOG_TAG = append.append(i).toString();
    }
    
    private void fadeSelectorWheel(final long duration) {
        this.mDimSelectorWheelAnimator.setDuration(duration);
        this.mDimSelectorWheelAnimator.start();
    }
    
    private void lightSeparators(final long duration) {
        this.mDimSeparatorsAnimator.setDuration(duration);
        this.mDimSeparatorsAnimator.start();
    }
    
    protected abstract void drawItems(final Canvas p0);
    
    @Override
    protected void initAttributes(final AttributeSet set, final int n) {
        super.initAttributes(set, n);
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.AbstractWheelView, n, 0);
        this.mItemsDimmedAlpha = obtainStyledAttributes.getInt(R.styleable.AbstractWheelView_itemsDimmedAlpha, 50);
        this.mSelectionDividerActiveAlpha = obtainStyledAttributes.getInt(R.styleable.AbstractWheelView_selectionDividerActiveAlpha, 70);
        this.mSelectionDividerDimmedAlpha = obtainStyledAttributes.getInt(R.styleable.AbstractWheelView_selectionDividerDimmedAlpha, 70);
        this.mItemOffsetPercent = obtainStyledAttributes.getInt(R.styleable.AbstractWheelView_itemOffsetPercent, 10);
        this.mItemsPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AbstractWheelView_itemsPadding, 10);
        this.mSelectionDivider = obtainStyledAttributes.getDrawable(R.styleable.AbstractWheelView_selectionDivider);
        obtainStyledAttributes.recycle();
    }
    
    @Override
    protected void initData(final Context context) {
        super.initData(context);
        this.mDimSelectorWheelAnimator = (Animator)ObjectAnimator.ofFloat((Object)this, "selectorPaintCoeff", new float[] { 1.0f, 0.0f });
        this.mDimSeparatorsAnimator = (Animator)ObjectAnimator.ofInt((Object)this, "separatorsPaintAlpha", new int[] { this.mSelectionDividerActiveAlpha, this.mSelectionDividerDimmedAlpha });
        (this.mSeparatorsPaint = new Paint()).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_IN));
        this.mSeparatorsPaint.setAlpha(this.mSelectionDividerDimmedAlpha);
        (this.mSelectorWheelPaint = new Paint()).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_IN));
    }
    
    protected abstract void measureLayout();
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mViewAdapter != null && this.mViewAdapter.getItemsCount() > 0) {
            if (this.rebuildItems()) {
                this.measureLayout();
            }
            this.doItemsLayout();
            this.drawItems(canvas);
        }
    }
    
    @Override
    protected void onScrollFinished() {
        this.fadeSelectorWheel(500L);
        this.lightSeparators(500L);
    }
    
    @Override
    protected void onScrollTouched() {
        this.mDimSelectorWheelAnimator.cancel();
        this.mDimSeparatorsAnimator.cancel();
        this.setSelectorPaintCoeff(1.0f);
        this.setSeparatorsPaintAlpha(this.mSelectionDividerActiveAlpha);
    }
    
    @Override
    protected void onScrollTouchedUp() {
        super.onScrollTouchedUp();
        this.fadeSelectorWheel(750L);
        this.lightSeparators(750L);
    }
    
    @Override
    protected void recreateAssets(final int n, final int n2) {
        this.mSpinBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
        this.mSeparatorsBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
        this.setSelectorPaintCoeff(0.0f);
    }
    
    public void setSelectionDivider(final Drawable mSelectionDivider) {
        this.mSelectionDivider = mSelectionDivider;
    }
    
    public abstract void setSelectorPaintCoeff(final float p0);
    
    public void setSeparatorsPaintAlpha(final int alpha) {
        this.mSeparatorsPaint.setAlpha(alpha);
        this.invalidate();
    }
}
