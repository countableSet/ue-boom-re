// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.content.res.TypedArray;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.util.AttributeSet;
import android.content.Context;

public class WheelVerticalView extends AbstractWheelView
{
    private static int itemID;
    private final String LOG_TAG;
    private int mItemHeight;
    protected int mSelectionDividerHeight;
    
    static {
        WheelVerticalView.itemID = -1;
    }
    
    public WheelVerticalView(final Context context) {
        this(context, null);
    }
    
    public WheelVerticalView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.abstractWheelViewStyle);
    }
    
    public WheelVerticalView(final Context context, final AttributeSet set, int i) {
        super(context, set, i);
        final StringBuilder append = new StringBuilder().append(WheelVerticalView.class.getName()).append(" #");
        i = ++WheelVerticalView.itemID;
        this.LOG_TAG = append.append(i).toString();
        this.mItemHeight = 0;
    }
    
    private int calculateLayoutWidth(final int n, final int n2) {
        this.mItemsLayout.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
        this.mItemsLayout.measure(View$MeasureSpec.makeMeasureSpec(n, 0), View$MeasureSpec.makeMeasureSpec(0, 0));
        final int measuredWidth = this.mItemsLayout.getMeasuredWidth();
        int max;
        if (n2 == 1073741824) {
            max = n;
        }
        else {
            final int n3 = max = Math.max(measuredWidth + this.mItemsPadding * 2, this.getSuggestedMinimumWidth());
            if (n2 == Integer.MIN_VALUE && n < (max = n3)) {
                max = n;
            }
        }
        this.mItemsLayout.measure(View$MeasureSpec.makeMeasureSpec(max - this.mItemsPadding * 2, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        return max;
    }
    
    @Override
    protected void createItemsLayout() {
        if (this.mItemsLayout == null) {
            (this.mItemsLayout = new LinearLayout(this.getContext())).setOrientation(1);
        }
    }
    
    @Override
    protected WheelScroller createScroller(final WheelScroller.ScrollingListener scrollingListener) {
        return new WheelVerticalScroller(this.getContext(), scrollingListener);
    }
    
    @Override
    protected void doItemsLayout() {
        this.mItemsLayout.layout(0, 0, this.getMeasuredWidth() - this.mItemsPadding * 2, this.getMeasuredHeight());
    }
    
    @Override
    protected void drawItems(final Canvas canvas) {
        canvas.save();
        final int measuredWidth = this.getMeasuredWidth();
        final int measuredHeight = this.getMeasuredHeight();
        final int itemDimension = this.getItemDimension();
        this.mSpinBitmap.eraseColor(0);
        final Canvas canvas2 = new Canvas(this.mSpinBitmap);
        final Canvas canvas3 = new Canvas(this.mSpinBitmap);
        canvas2.translate((float)this.mItemsPadding, (float)(-((this.mCurrentItemIdx - this.mFirstItemIdx) * itemDimension + (itemDimension - this.getHeight()) / 2) + this.mScrollingOffset));
        this.mItemsLayout.draw(canvas2);
        this.mSeparatorsBitmap.eraseColor(0);
        final Canvas canvas4 = new Canvas(this.mSeparatorsBitmap);
        if (this.mSelectionDivider != null) {
            final int n = (this.getHeight() - itemDimension - this.mSelectionDividerHeight) / 2;
            final int n2 = n + this.mSelectionDividerHeight;
            this.mSelectionDivider.setBounds(0, n, measuredWidth, n2);
            this.mSelectionDivider.draw(canvas4);
            this.mSelectionDivider.setBounds(0, n + itemDimension, measuredWidth, n2 + itemDimension);
            this.mSelectionDivider.draw(canvas4);
        }
        canvas3.drawRect(0.0f, 0.0f, (float)measuredWidth, (float)measuredHeight, this.mSelectorWheelPaint);
        canvas4.drawRect(0.0f, 0.0f, (float)measuredWidth, (float)measuredHeight, this.mSeparatorsPaint);
        canvas.drawBitmap(this.mSpinBitmap, 0.0f, 0.0f, (Paint)null);
        canvas.drawBitmap(this.mSeparatorsBitmap, 0.0f, 0.0f, (Paint)null);
        canvas.restore();
    }
    
    @Override
    protected int getBaseDimension() {
        return this.getHeight();
    }
    
    @Override
    protected int getItemDimension() {
        int n;
        if (this.mItemHeight != 0) {
            n = this.mItemHeight;
        }
        else if (this.mItemsLayout != null && this.mItemsLayout.getChildAt(0) != null) {
            this.mItemHeight = this.mItemsLayout.getChildAt(0).getMeasuredHeight();
            n = this.mItemHeight;
        }
        else {
            n = this.getBaseDimension() / this.mVisibleItems;
        }
        return n;
    }
    
    @Override
    protected float getMotionEventPosition(final MotionEvent motionEvent) {
        return motionEvent.getY();
    }
    
    @Override
    protected void initAttributes(final AttributeSet set, final int n) {
        super.initAttributes(set, n);
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.WheelVerticalView, n, 0);
        this.mSelectionDividerHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WheelVerticalView_selectionDividerHeight, 2);
        obtainStyledAttributes.recycle();
    }
    
    @Override
    protected void measureLayout() {
        this.mItemsLayout.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
        this.mItemsLayout.measure(View$MeasureSpec.makeMeasureSpec(this.getWidth() - this.mItemsPadding * 2, 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
    }
    
    protected void onMeasure(int n, int size) {
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(size);
        n = View$MeasureSpec.getSize(n);
        size = View$MeasureSpec.getSize(size);
        this.rebuildItems();
        final int calculateLayoutWidth = this.calculateLayoutWidth(n, mode);
        if (mode2 == 1073741824) {
            n = size;
        }
        else {
            final int a = n = Math.max(this.getItemDimension() * (this.mVisibleItems - this.mItemOffsetPercent / 100), this.getSuggestedMinimumHeight());
            if (mode2 == Integer.MIN_VALUE) {
                n = Math.min(a, size);
            }
        }
        this.setMeasuredDimension(calculateLayoutWidth, n);
    }
    
    @Override
    public void setSelectorPaintCoeff(float a) {
        final int measuredHeight = this.getMeasuredHeight();
        final int itemDimension = this.getItemDimension();
        final float n = (1.0f - itemDimension / (float)measuredHeight) / 2.0f;
        final float n2 = (1.0f + itemDimension / (float)measuredHeight) / 2.0f;
        final float a2 = this.mItemsDimmedAlpha * (1.0f - a);
        final float n3 = a2 + 255.0f * a;
        LinearGradient shader;
        if (this.mVisibleItems == 2) {
            final int n4 = Math.round(n3) << 24;
            final int n5 = Math.round(a2) << 24;
            a = (float)measuredHeight;
            shader = new LinearGradient(0.0f, 0.0f, 0.0f, a, new int[] { n5, n4, -16777216, -16777216, n4, n5 }, new float[] { 0.0f, n, n, n2, n2, 1.0f }, Shader$TileMode.CLAMP);
        }
        else {
            final float n6 = (1.0f - itemDimension * 3 / (float)measuredHeight) / 2.0f;
            final float n7 = (1.0f + itemDimension * 3 / (float)measuredHeight) / 2.0f;
            a *= 255.0f * n6 / n;
            final int n8 = Math.round(n3) << 24;
            final int n9 = Math.round(a2 + a) << 24;
            final int n10 = Math.round(a) << 24;
            a = (float)measuredHeight;
            shader = new LinearGradient(0.0f, 0.0f, 0.0f, a, new int[] { 0, n10, n9, n8, -16777216, -16777216, n8, n9, n10, 0 }, new float[] { 0.0f, n6, n6, n, n, n2, n2, n7, n7, 1.0f }, Shader$TileMode.CLAMP);
        }
        this.mSelectorWheelPaint.setShader((Shader)shader);
        this.invalidate();
    }
}
