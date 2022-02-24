// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.graphics.Shader;
import android.graphics.LinearGradient;
import android.graphics.Shader$TileMode;
import android.view.View;
import android.util.Log;
import android.content.res.TypedArray;
import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.util.AttributeSet;
import android.content.Context;

public class WheelHorizontalView extends AbstractWheelView
{
    private static int itemID;
    private final String LOG_TAG;
    private int itemWidth;
    protected int mSelectionDividerWidth;
    
    static {
        WheelHorizontalView.itemID = -1;
    }
    
    public WheelHorizontalView(final Context context) {
        this(context, null);
    }
    
    public WheelHorizontalView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.abstractWheelViewStyle);
    }
    
    public WheelHorizontalView(final Context context, final AttributeSet set, int i) {
        super(context, set, i);
        final StringBuilder append = new StringBuilder().append(WheelVerticalView.class.getName()).append(" #");
        i = ++WheelHorizontalView.itemID;
        this.LOG_TAG = append.append(i).toString();
        this.itemWidth = 0;
    }
    
    private int calculateLayoutHeight(final int n, final int n2) {
        this.mItemsLayout.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
        this.mItemsLayout.measure(View$MeasureSpec.makeMeasureSpec(0, 0), View$MeasureSpec.makeMeasureSpec(n, 0));
        final int measuredHeight = this.mItemsLayout.getMeasuredHeight();
        int max;
        if (n2 == 1073741824) {
            max = n;
        }
        else {
            final int n3 = max = Math.max(measuredHeight + this.mItemsPadding * 2, this.getSuggestedMinimumHeight());
            if (n2 == Integer.MIN_VALUE && n < (max = n3)) {
                max = n;
            }
        }
        this.mItemsLayout.measure(View$MeasureSpec.makeMeasureSpec(400, 1073741824), View$MeasureSpec.makeMeasureSpec(max - this.mItemsPadding * 2, 1073741824));
        return max;
    }
    
    @Override
    protected void createItemsLayout() {
        if (this.mItemsLayout == null) {
            (this.mItemsLayout = new LinearLayout(this.getContext())).setOrientation(0);
        }
    }
    
    @Override
    protected WheelScroller createScroller(final WheelScroller.ScrollingListener scrollingListener) {
        return new WheelHorizontalScroller(this.getContext(), scrollingListener);
    }
    
    @Override
    protected void doItemsLayout() {
        this.mItemsLayout.layout(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight() - this.mItemsPadding * 2);
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
        canvas2.translate((float)(-((this.mCurrentItemIdx - this.mFirstItemIdx) * itemDimension + (itemDimension - this.getWidth()) / 2) + this.mScrollingOffset), (float)this.mItemsPadding);
        this.mItemsLayout.draw(canvas2);
        this.mSeparatorsBitmap.eraseColor(0);
        final Canvas canvas4 = new Canvas(this.mSeparatorsBitmap);
        if (this.mSelectionDivider != null) {
            final int n = (this.getWidth() - itemDimension - this.mSelectionDividerWidth) / 2;
            final int n2 = n + this.mSelectionDividerWidth;
            canvas4.save();
            canvas4.clipRect(n, 0, n2, measuredHeight);
            this.mSelectionDivider.setBounds(n, 0, n2, measuredHeight);
            this.mSelectionDivider.draw(canvas4);
            canvas4.restore();
            canvas4.save();
            final int n3 = n + itemDimension;
            final int n4 = n2 + itemDimension;
            canvas4.clipRect(n3, 0, n4, measuredHeight);
            this.mSelectionDivider.setBounds(n3, 0, n4, measuredHeight);
            this.mSelectionDivider.draw(canvas4);
            canvas4.restore();
        }
        canvas3.drawRect(0.0f, 0.0f, (float)measuredWidth, (float)measuredHeight, this.mSelectorWheelPaint);
        canvas4.drawRect(0.0f, 0.0f, (float)measuredWidth, (float)measuredHeight, this.mSeparatorsPaint);
        canvas.drawBitmap(this.mSpinBitmap, 0.0f, 0.0f, (Paint)null);
        canvas.drawBitmap(this.mSeparatorsBitmap, 0.0f, 0.0f, (Paint)null);
        canvas.restore();
    }
    
    @Override
    protected int getBaseDimension() {
        return this.getWidth();
    }
    
    @Override
    protected int getItemDimension() {
        int n;
        if (this.itemWidth != 0) {
            n = this.itemWidth;
        }
        else if (this.mItemsLayout != null && this.mItemsLayout.getChildAt(0) != null) {
            this.itemWidth = this.mItemsLayout.getChildAt(0).getMeasuredWidth();
            n = this.itemWidth;
        }
        else {
            n = this.getBaseDimension() / this.mVisibleItems;
        }
        return n;
    }
    
    @Override
    protected float getMotionEventPosition(final MotionEvent motionEvent) {
        return motionEvent.getX();
    }
    
    @Override
    protected void initAttributes(final AttributeSet set, final int n) {
        super.initAttributes(set, n);
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.WheelHorizontalView, n, 0);
        this.mSelectionDividerWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WheelHorizontalView_selectionDividerWidth, 2);
        obtainStyledAttributes.recycle();
    }
    
    @Override
    protected void measureLayout() {
        this.mItemsLayout.setLayoutParams(new ViewGroup$LayoutParams(-2, -2));
        this.mItemsLayout.measure(View$MeasureSpec.makeMeasureSpec(this.getWidth() + this.getItemDimension(), 0), View$MeasureSpec.makeMeasureSpec(this.getHeight(), Integer.MIN_VALUE));
    }
    
    protected void onMeasure(int n, int a) {
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(a);
        final int size = View$MeasureSpec.getSize(n);
        n = View$MeasureSpec.getSize(a);
        this.rebuildItems();
        final int calculateLayoutHeight = this.calculateLayoutHeight(n, mode2);
        if (mode == 1073741824) {
            n = size;
        }
        else {
            a = (n = Math.max(this.getItemDimension() * (this.mVisibleItems - this.mItemOffsetPercent / 100), this.getSuggestedMinimumWidth()));
            if (mode == Integer.MIN_VALUE) {
                n = Math.min(a, size);
            }
        }
        this.setMeasuredDimension(n, calculateLayoutHeight);
    }
    
    @Override
    protected void onScrollTouchedUp() {
        super.onScrollTouchedUp();
        final int childCount = this.mItemsLayout.getChildCount();
        Log.e(this.LOG_TAG, " ----- layout: " + this.mItemsLayout.getMeasuredWidth() + this.mItemsLayout.getMeasuredHeight());
        Log.e(this.LOG_TAG, " -------- dumping " + childCount + " items");
        for (int i = 0; i < childCount; ++i) {
            final View child = this.mItemsLayout.getChildAt(i);
            Log.e(this.LOG_TAG, " item #" + i + ": " + child.getWidth() + "x" + child.getHeight());
            child.forceLayout();
        }
        Log.e(this.LOG_TAG, " ---------- dumping finished ");
    }
    
    public void setSelectionDividerWidth(final int mSelectionDividerWidth) {
        this.mSelectionDividerWidth = mSelectionDividerWidth;
    }
    
    @Override
    public void setSelectorPaintCoeff(float a) {
        if (this.mItemsDimmedAlpha < 100) {
            final int measuredWidth = this.getMeasuredWidth();
            final int itemDimension = this.getItemDimension();
            final float n = (1.0f - itemDimension / (float)measuredWidth) / 2.0f;
            final float n2 = (1.0f + itemDimension / (float)measuredWidth) / 2.0f;
            final float a2 = this.mItemsDimmedAlpha * (1.0f - a);
            final float n3 = a2 + 255.0f * a;
            LinearGradient shader;
            if (this.mVisibleItems == 2) {
                final int n4 = Math.round(n3) << 24;
                final int n5 = Math.round(a2) << 24;
                a = (float)measuredWidth;
                shader = new LinearGradient(0.0f, 0.0f, a, 0.0f, new int[] { n5, n4, -16777216, -16777216, n4, n5 }, new float[] { 0.0f, n, n, n2, n2, 1.0f }, Shader$TileMode.CLAMP);
            }
            else {
                final float n6 = (1.0f - itemDimension * 3 / (float)measuredWidth) / 2.0f;
                final float n7 = (1.0f + itemDimension * 3 / (float)measuredWidth) / 2.0f;
                a *= 255.0f * n6 / n;
                Math.round(n3);
                final int n8 = Math.round(a2 + a) << 24;
                Math.round(a);
                a = (float)measuredWidth;
                shader = new LinearGradient(0.0f, 0.0f, a, 0.0f, new int[] { n8, n8, n8, n8, -16777216, -16777216, n8, n8, n8, n8 }, new float[] { 0.0f, n6, n6, n, n, n2, n2, n7, n7, 1.0f }, Shader$TileMode.CLAMP);
            }
            this.mSelectorWheelPaint.setShader((Shader)shader);
            this.invalidate();
        }
    }
}
