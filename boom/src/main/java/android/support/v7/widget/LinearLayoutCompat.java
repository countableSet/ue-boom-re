// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.View$MeasureSpec;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

public class LinearLayoutCompat extends ViewGroup
{
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;
    
    public LinearLayoutCompat(final Context context) {
        this(context, null);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.LinearLayoutCompat, n, 0);
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (n >= 0) {
            this.setOrientation(n);
        }
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (n >= 0) {
            this.setGravity(n);
        }
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!boolean1) {
            this.setBaselineAligned(boolean1);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        this.setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }
    
    private void forceUniformHeight(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.height == -1) {
                    final int width = layoutParams.width;
                    layoutParams.width = virtualChild.getMeasuredWidth();
                    this.measureChildWithMargins(virtualChild, n2, 0, measureSpec, 0);
                    layoutParams.width = width;
                }
            }
        }
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = virtualChild.getMeasuredHeight();
                    this.measureChildWithMargins(virtualChild, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    void drawDividersHorizontal(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                int n;
                if (layoutRtl) {
                    n = virtualChild.getRight() + layoutParams.rightMargin;
                }
                else {
                    n = virtualChild.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
                }
                this.drawVerticalDivider(canvas, n);
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int paddingLeft;
            if (virtualChild2 == null) {
                if (layoutRtl) {
                    paddingLeft = this.getPaddingLeft();
                }
                else {
                    paddingLeft = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
                }
            }
            else {
                final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                if (layoutRtl) {
                    paddingLeft = virtualChild2.getLeft() - layoutParams2.leftMargin - this.mDividerWidth;
                }
                else {
                    paddingLeft = virtualChild2.getRight() + layoutParams2.rightMargin;
                }
            }
            this.drawVerticalDivider(canvas, paddingLeft);
        }
    }
    
    void drawDividersVertical(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                this.drawHorizontalDivider(canvas, virtualChild.getTop() - ((LayoutParams)virtualChild.getLayoutParams()).topMargin - this.mDividerHeight);
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int n;
            if (virtualChild2 == null) {
                n = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            }
            else {
                n = virtualChild2.getBottom() + ((LayoutParams)virtualChild2.getLayoutParams()).bottomMargin;
            }
            this.drawHorizontalDivider(canvas, n);
        }
    }
    
    void drawHorizontalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }
    
    void drawVerticalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams;
        if (this.mOrientation == 0) {
            layoutParams = new LayoutParams(-2, -2);
        }
        else if (this.mOrientation == 1) {
            layoutParams = new LayoutParams(-1, -2);
        }
        else {
            layoutParams = null;
        }
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getBaseline() {
        int baseline = -1;
        if (this.mBaselineAlignedChildIndex < 0) {
            baseline = super.getBaseline();
        }
        else {
            if (this.getChildCount() <= this.mBaselineAlignedChildIndex) {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
            }
            final View child = this.getChildAt(this.mBaselineAlignedChildIndex);
            final int baseline2 = child.getBaseline();
            if (baseline2 == -1) {
                if (this.mBaselineAlignedChildIndex != 0) {
                    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
                }
            }
            else {
                int mBaselineChildTop;
                final int n = mBaselineChildTop = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    final int n2 = this.mGravity & 0x70;
                    mBaselineChildTop = n;
                    if (n2 != 48) {
                        switch (n2) {
                            default: {
                                mBaselineChildTop = n;
                                break;
                            }
                            case 80: {
                                mBaselineChildTop = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                                break;
                            }
                            case 16: {
                                mBaselineChildTop = n + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                                break;
                            }
                        }
                    }
                }
                baseline = ((LayoutParams)child.getLayoutParams()).topMargin + mBaselineChildTop + baseline2;
            }
        }
        return baseline;
    }
    
    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }
    
    int getChildrenSkipCount(final View view, final int n) {
        return 0;
    }
    
    public Drawable getDividerDrawable() {
        return this.mDivider;
    }
    
    public int getDividerPadding() {
        return this.mDividerPadding;
    }
    
    public int getDividerWidth() {
        return this.mDividerWidth;
    }
    
    int getLocationOffset(final View view) {
        return 0;
    }
    
    int getNextLocationOffset(final View view) {
        return 0;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getShowDividers() {
        return this.mShowDividers;
    }
    
    View getVirtualChildAt(final int n) {
        return this.getChildAt(n);
    }
    
    int getVirtualChildCount() {
        return this.getChildCount();
    }
    
    public float getWeightSum() {
        return this.mWeightSum;
    }
    
    protected boolean hasDividerBeforeChildAt(int n) {
        boolean b = true;
        if (n == 0) {
            if ((this.mShowDividers & 0x1) == 0x0) {
                b = false;
            }
        }
        else if (n == this.getChildCount()) {
            if ((this.mShowDividers & 0x4) == 0x0) {
                b = false;
            }
        }
        else if ((this.mShowDividers & 0x2) != 0x0) {
            final boolean b2 = false;
            --n;
            while (true) {
                b = b2;
                if (n < 0) {
                    break;
                }
                if (this.getChildAt(n).getVisibility() != 8) {
                    b = true;
                    break;
                }
                --n;
            }
        }
        else {
            b = false;
        }
        return b;
    }
    
    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }
    
    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }
    
    void layoutHorizontal(int n, int i, int n2, int n3) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        final int paddingTop = this.getPaddingTop();
        final int n4 = n3 - i;
        final int paddingBottom = this.getPaddingBottom();
        final int paddingBottom2 = this.getPaddingBottom();
        final int virtualChildCount = this.getVirtualChildCount();
        i = this.mGravity;
        final int mGravity = this.mGravity;
        final boolean mBaselineAligned = this.mBaselineAligned;
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        switch (GravityCompat.getAbsoluteGravity(i & 0x800007, ViewCompat.getLayoutDirection((View)this))) {
            default: {
                n = this.getPaddingLeft();
                break;
            }
            case 5: {
                n = this.getPaddingLeft() + n2 - n - this.mTotalLength;
                break;
            }
            case 1: {
                n = this.getPaddingLeft() + (n2 - n - this.mTotalLength) / 2;
                break;
            }
        }
        n3 = 0;
        int n5 = 1;
        if (layoutRtl) {
            n3 = virtualChildCount - 1;
            n5 = -1;
        }
        i = 0;
        n2 = n;
        while (i < virtualChildCount) {
            final int n6 = n3 + n5 * i;
            final View virtualChild = this.getVirtualChildAt(n6);
            int n7;
            if (virtualChild == null) {
                n = n2 + this.measureNullChild(n6);
                n7 = i;
            }
            else {
                n = n2;
                n7 = i;
                if (virtualChild.getVisibility() != 8) {
                    final int measuredWidth = virtualChild.getMeasuredWidth();
                    final int measuredHeight = virtualChild.getMeasuredHeight();
                    n = -1;
                    final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    int baseline = n;
                    if (mBaselineAligned) {
                        baseline = n;
                        if (layoutParams.height != -1) {
                            baseline = virtualChild.getBaseline();
                        }
                    }
                    if ((n = layoutParams.gravity) < 0) {
                        n = (mGravity & 0x70);
                    }
                    switch (n & 0x70) {
                        default: {
                            n = paddingTop;
                            break;
                        }
                        case 48: {
                            final int n8 = n = paddingTop + layoutParams.topMargin;
                            if (baseline != -1) {
                                n = n8 + (mMaxAscent[1] - baseline);
                                break;
                            }
                            break;
                        }
                        case 16: {
                            n = (n4 - paddingTop - paddingBottom2 - measuredHeight) / 2 + paddingTop + layoutParams.topMargin - layoutParams.bottomMargin;
                            break;
                        }
                        case 80: {
                            final int n9 = n = n4 - paddingBottom - measuredHeight - layoutParams.bottomMargin;
                            if (baseline != -1) {
                                n = virtualChild.getMeasuredHeight();
                                n = n9 - (mMaxDescent[2] - (n - baseline));
                                break;
                            }
                            break;
                        }
                    }
                    int n10 = n2;
                    if (this.hasDividerBeforeChildAt(n6)) {
                        n10 = n2 + this.mDividerWidth;
                    }
                    n2 = n10 + layoutParams.leftMargin;
                    this.setChildFrame(virtualChild, n2 + this.getLocationOffset(virtualChild), n, measuredWidth, measuredHeight);
                    n = n2 + (layoutParams.rightMargin + measuredWidth + this.getNextLocationOffset(virtualChild));
                    n7 = i + this.getChildrenSkipCount(virtualChild, n6);
                }
            }
            i = n7 + 1;
            n2 = n;
        }
    }
    
    void layoutVertical(int n, int i, int n2, int gravity) {
        final int paddingLeft = this.getPaddingLeft();
        final int n3 = n2 - n;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        final int virtualChildCount = this.getVirtualChildCount();
        n = this.mGravity;
        final int mGravity = this.mGravity;
        switch (n & 0x70) {
            default: {
                n = this.getPaddingTop();
                break;
            }
            case 80: {
                n = this.getPaddingTop() + gravity - i - this.mTotalLength;
                break;
            }
            case 16: {
                n = this.getPaddingTop() + (gravity - i - this.mTotalLength) / 2;
                break;
            }
        }
        View virtualChild;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        for (i = 0; i < virtualChildCount; i = gravity + 1, n = n2) {
            virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                n2 = n + this.measureNullChild(i);
                gravity = i;
            }
            else {
                n2 = n;
                gravity = i;
                if (virtualChild.getVisibility() != 8) {
                    measuredWidth = virtualChild.getMeasuredWidth();
                    measuredHeight = virtualChild.getMeasuredHeight();
                    layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    gravity = layoutParams.gravity;
                    if ((n2 = gravity) < 0) {
                        n2 = (mGravity & 0x800007);
                    }
                    switch (GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this)) & 0x7) {
                        default: {
                            n2 = paddingLeft + layoutParams.leftMargin;
                            break;
                        }
                        case 1: {
                            n2 = (n3 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                            break;
                        }
                        case 5: {
                            n2 = n3 - paddingRight - measuredWidth - layoutParams.rightMargin;
                            break;
                        }
                    }
                    gravity = n;
                    if (this.hasDividerBeforeChildAt(i)) {
                        gravity = n + this.mDividerHeight;
                    }
                    n = gravity + layoutParams.topMargin;
                    this.setChildFrame(virtualChild, n2, n + this.getLocationOffset(virtualChild), measuredWidth, measuredHeight);
                    n2 = n + (layoutParams.bottomMargin + measuredHeight + this.getNextLocationOffset(virtualChild));
                    gravity = i + this.getChildrenSkipCount(virtualChild, i);
                }
            }
        }
    }
    
    void measureChildBeforeLayout(final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        this.measureChildWithMargins(view, n2, n3, n4, n5);
    }
    
    void measureHorizontal(final int n, final int n2) {
        this.mTotalLength = 0;
        int max = 0;
        int n3 = 0;
        int max2 = 0;
        int max3 = 0;
        int n4 = 1;
        float mWeightSum = 0.0f;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        int n5 = 0;
        int n6 = 0;
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        mMaxAscent[2] = (mMaxAscent[3] = -1);
        mMaxAscent[0] = (mMaxAscent[1] = -1);
        mMaxDescent[2] = (mMaxDescent[3] = -1);
        mMaxDescent[0] = (mMaxDescent[1] = -1);
        final boolean mBaselineAligned = this.mBaselineAligned;
        final boolean mUseLargestChild = this.mUseLargestChild;
        final boolean b = mode == 1073741824;
        int b2 = Integer.MIN_VALUE;
        int max4;
        for (int i = 0; i < virtualChildCount; ++i, b2 = max4) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
                max4 = b2;
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
                max4 = b2;
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                mWeightSum += layoutParams.weight;
                int n7;
                if (mode == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (b) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    }
                    else {
                        final int mTotalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength, layoutParams.leftMargin + mTotalLength + layoutParams.rightMargin);
                    }
                    if (mBaselineAligned) {
                        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChild.measure(measureSpec, measureSpec);
                        n7 = n6;
                        max4 = b2;
                    }
                    else {
                        n7 = 1;
                        max4 = b2;
                    }
                }
                else {
                    int width;
                    final int n8 = width = Integer.MIN_VALUE;
                    if (layoutParams.width == 0) {
                        width = n8;
                        if (layoutParams.weight > 0.0f) {
                            width = 0;
                            layoutParams.width = -2;
                        }
                    }
                    int mTotalLength2;
                    if (mWeightSum == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    this.measureChildBeforeLayout(virtualChild, i, n, mTotalLength2, n2, 0);
                    if (width != Integer.MIN_VALUE) {
                        layoutParams.width = width;
                    }
                    final int measuredWidth = virtualChild.getMeasuredWidth();
                    if (b) {
                        this.mTotalLength += layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + this.getNextLocationOffset(virtualChild);
                    }
                    else {
                        final int mTotalLength3 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + this.getNextLocationOffset(virtualChild));
                    }
                    max4 = b2;
                    n7 = n6;
                    if (mUseLargestChild) {
                        max4 = Math.max(measuredWidth, b2);
                        n7 = n6;
                    }
                }
                final int n9 = 0;
                int n10 = n5;
                int n11 = n9;
                if (mode2 != 1073741824) {
                    n10 = n5;
                    n11 = n9;
                    if (layoutParams.height == -1) {
                        n10 = 1;
                        n11 = 1;
                    }
                }
                int n12 = layoutParams.topMargin + layoutParams.bottomMargin;
                final int b3 = virtualChild.getMeasuredHeight() + n12;
                final int combineMeasuredStates = ViewUtils.combineMeasuredStates(n3, ViewCompat.getMeasuredState(virtualChild));
                if (mBaselineAligned) {
                    final int baseline = virtualChild.getBaseline();
                    if (baseline != -1) {
                        int n13;
                        if (layoutParams.gravity < 0) {
                            n13 = this.mGravity;
                        }
                        else {
                            n13 = layoutParams.gravity;
                        }
                        final int n14 = ((n13 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                        mMaxAscent[n14] = Math.max(mMaxAscent[n14], baseline);
                        mMaxDescent[n14] = Math.max(mMaxDescent[n14], b3 - baseline);
                    }
                }
                max = Math.max(max, b3);
                if (n4 != 0 && layoutParams.height == -1) {
                    n4 = 1;
                }
                else {
                    n4 = 0;
                }
                if (layoutParams.weight > 0.0f) {
                    if (n11 == 0) {
                        n12 = b3;
                    }
                    max3 = Math.max(max3, n12);
                }
                else {
                    if (n11 == 0) {
                        n12 = b3;
                    }
                    max2 = Math.max(max2, n12);
                }
                i += this.getChildrenSkipCount(virtualChild, i);
                n3 = combineMeasuredStates;
                n5 = n10;
                n6 = n7;
            }
        }
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        int max5 = 0;
        Label_1004: {
            if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1) {
                max5 = max;
                if (mMaxAscent[3] == -1) {
                    break Label_1004;
                }
            }
            max5 = Math.max(max, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
        }
        if (mUseLargestChild && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            for (int j = 0; j < virtualChildCount; ++j) {
                final View virtualChild2 = this.getVirtualChildAt(j);
                if (virtualChild2 == null) {
                    this.mTotalLength += this.measureNullChild(j);
                }
                else if (virtualChild2.getVisibility() == 8) {
                    j += this.getChildrenSkipCount(virtualChild2, j);
                }
                else {
                    final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                    if (b) {
                        this.mTotalLength += layoutParams2.leftMargin + b2 + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild2);
                    }
                    else {
                        final int mTotalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + b2 + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild2));
                    }
                }
            }
        }
        this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
        final int resolveSizeAndState = ViewCompat.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), n, 0);
        int n15 = (resolveSizeAndState & 0xFFFFFF) - this.mTotalLength;
        int n34 = 0;
        int n35 = 0;
        int n36 = 0;
        int max8 = 0;
        Label_2095: {
            if (n6 != 0 || (n15 != 0 && mWeightSum > 0.0f)) {
                if (this.mWeightSum > 0.0f) {
                    mWeightSum = this.mWeightSum;
                }
                mMaxAscent[2] = (mMaxAscent[3] = -1);
                mMaxAscent[0] = (mMaxAscent[1] = -1);
                mMaxDescent[2] = (mMaxDescent[3] = -1);
                mMaxDescent[0] = (mMaxDescent[1] = -1);
                int n16 = -1;
                this.mTotalLength = 0;
                int k = 0;
                int a = max2;
                while (k < virtualChildCount) {
                    final View virtualChild3 = this.getVirtualChildAt(k);
                    int n17 = n4;
                    int n18 = a;
                    int n19 = n3;
                    int n20 = n15;
                    int n21 = n16;
                    float n22 = mWeightSum;
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() == 8) {
                            n22 = mWeightSum;
                            n21 = n16;
                            n20 = n15;
                            n19 = n3;
                            n18 = a;
                            n17 = n4;
                        }
                        else {
                            final LayoutParams layoutParams3 = (LayoutParams)virtualChild3.getLayoutParams();
                            final float weight = layoutParams3.weight;
                            int n23 = n3;
                            int n24 = n15;
                            float n25 = mWeightSum;
                            if (weight > 0.0f) {
                                int n26 = (int)(n15 * weight / mWeightSum);
                                n25 = mWeightSum - weight;
                                final int n27 = n15 - n26;
                                final int childMeasureSpec = getChildMeasureSpec(n2, this.getPaddingTop() + this.getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                                if (layoutParams3.width != 0 || mode != 1073741824) {
                                    int n28;
                                    if ((n28 = virtualChild3.getMeasuredWidth() + n26) < 0) {
                                        n28 = 0;
                                    }
                                    virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(n28, 1073741824), childMeasureSpec);
                                }
                                else {
                                    if (n26 <= 0) {
                                        n26 = 0;
                                    }
                                    virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(n26, 1073741824), childMeasureSpec);
                                }
                                final int combineMeasuredStates2 = ViewUtils.combineMeasuredStates(n3, ViewCompat.getMeasuredState(virtualChild3) & 0xFF000000);
                                n24 = n27;
                                n23 = combineMeasuredStates2;
                            }
                            if (b) {
                                this.mTotalLength += virtualChild3.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild3);
                            }
                            else {
                                final int mTotalLength5 = this.mTotalLength;
                                this.mTotalLength = Math.max(mTotalLength5, virtualChild3.getMeasuredWidth() + mTotalLength5 + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild3));
                            }
                            int n29;
                            if (mode2 != 1073741824 && layoutParams3.height == -1) {
                                n29 = 1;
                            }
                            else {
                                n29 = 0;
                            }
                            final int n30 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                            final int b4 = virtualChild3.getMeasuredHeight() + n30;
                            final int max6 = Math.max(n16, b4);
                            int b5;
                            if (n29 != 0) {
                                b5 = n30;
                            }
                            else {
                                b5 = b4;
                            }
                            final int max7 = Math.max(a, b5);
                            int n31;
                            if (n4 != 0 && layoutParams3.height == -1) {
                                n31 = 1;
                            }
                            else {
                                n31 = 0;
                            }
                            n17 = n31;
                            n18 = max7;
                            n19 = n23;
                            n20 = n24;
                            n21 = max6;
                            n22 = n25;
                            if (mBaselineAligned) {
                                final int baseline2 = virtualChild3.getBaseline();
                                n17 = n31;
                                n18 = max7;
                                n19 = n23;
                                n20 = n24;
                                n21 = max6;
                                n22 = n25;
                                if (baseline2 != -1) {
                                    int n32;
                                    if (layoutParams3.gravity < 0) {
                                        n32 = this.mGravity;
                                    }
                                    else {
                                        n32 = layoutParams3.gravity;
                                    }
                                    final int n33 = ((n32 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                                    mMaxAscent[n33] = Math.max(mMaxAscent[n33], baseline2);
                                    mMaxDescent[n33] = Math.max(mMaxDescent[n33], b4 - baseline2);
                                    n17 = n31;
                                    n18 = max7;
                                    n19 = n23;
                                    n20 = n24;
                                    n21 = max6;
                                    n22 = n25;
                                }
                            }
                        }
                    }
                    ++k;
                    n4 = n17;
                    a = n18;
                    n3 = n19;
                    n15 = n20;
                    n16 = n21;
                    mWeightSum = n22;
                }
                this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
                if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1) {
                    n34 = n4;
                    n35 = a;
                    n36 = n3;
                    max8 = n16;
                    if (mMaxAscent[3] == -1) {
                        break Label_2095;
                    }
                }
                max8 = Math.max(n16, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
                n36 = n3;
                n35 = a;
                n34 = n4;
            }
            else {
                final int max9 = Math.max(max2, max3);
                n34 = n4;
                n35 = max9;
                n36 = n3;
                max8 = max5;
                if (mUseLargestChild) {
                    n34 = n4;
                    n35 = max9;
                    n36 = n3;
                    max8 = max5;
                    if (mode != 1073741824) {
                        int n37 = 0;
                        while (true) {
                            n34 = n4;
                            n35 = max9;
                            n36 = n3;
                            max8 = max5;
                            if (n37 >= virtualChildCount) {
                                break;
                            }
                            final View virtualChild4 = this.getVirtualChildAt(n37);
                            if (virtualChild4 != null && virtualChild4.getVisibility() != 8 && ((LayoutParams)virtualChild4.getLayoutParams()).weight > 0.0f) {
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(b2, 1073741824), View$MeasureSpec.makeMeasureSpec(virtualChild4.getMeasuredHeight(), 1073741824));
                            }
                            ++n37;
                        }
                    }
                }
            }
        }
        int n38 = max8;
        if (n34 == 0) {
            n38 = max8;
            if (mode2 != 1073741824) {
                n38 = n35;
            }
        }
        this.setMeasuredDimension((0xFF000000 & n36) | resolveSizeAndState, ViewCompat.resolveSizeAndState(Math.max(n38 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), n2, n36 << 16));
        if (n5 != 0) {
            this.forceUniformHeight(virtualChildCount, n);
        }
    }
    
    int measureNullChild(final int n) {
        return 0;
    }
    
    void measureVertical(final int n, final int n2) {
        this.mTotalLength = 0;
        int max = 0;
        int combineMeasuredStates = 0;
        int max2 = 0;
        int max3 = 0;
        int n3 = 1;
        float mWeightSum = 0.0f;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        int n4 = 0;
        int n5 = 0;
        final int mBaselineAlignedChildIndex = this.mBaselineAlignedChildIndex;
        final boolean mUseLargestChild = this.mUseLargestChild;
        int b = Integer.MIN_VALUE;
        int max4;
        for (int i = 0; i < virtualChildCount; ++i, b = max4) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
                max4 = b;
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
                max4 = b;
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                mWeightSum += layoutParams.weight;
                int n6;
                if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    final int mTotalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength, layoutParams.topMargin + mTotalLength + layoutParams.bottomMargin);
                    n6 = 1;
                    max4 = b;
                }
                else {
                    int height;
                    final int n7 = height = Integer.MIN_VALUE;
                    if (layoutParams.height == 0) {
                        height = n7;
                        if (layoutParams.weight > 0.0f) {
                            height = 0;
                            layoutParams.height = -2;
                        }
                    }
                    int mTotalLength2;
                    if (mWeightSum == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    this.measureChildBeforeLayout(virtualChild, i, n, 0, n2, mTotalLength2);
                    if (height != Integer.MIN_VALUE) {
                        layoutParams.height = height;
                    }
                    final int measuredHeight = virtualChild.getMeasuredHeight();
                    final int mTotalLength3 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + this.getNextLocationOffset(virtualChild));
                    max4 = b;
                    n6 = n5;
                    if (mUseLargestChild) {
                        max4 = Math.max(measuredHeight, b);
                        n6 = n5;
                    }
                }
                if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex == i + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (i < mBaselineAlignedChildIndex && layoutParams.weight > 0.0f) {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
                final int n8 = 0;
                int n9 = n4;
                int n10 = n8;
                if (mode != 1073741824) {
                    n9 = n4;
                    n10 = n8;
                    if (layoutParams.width == -1) {
                        n9 = 1;
                        n10 = 1;
                    }
                }
                int n11 = layoutParams.leftMargin + layoutParams.rightMargin;
                final int b2 = virtualChild.getMeasuredWidth() + n11;
                max = Math.max(max, b2);
                combineMeasuredStates = ViewUtils.combineMeasuredStates(combineMeasuredStates, ViewCompat.getMeasuredState(virtualChild));
                if (n3 != 0 && layoutParams.width == -1) {
                    n3 = 1;
                }
                else {
                    n3 = 0;
                }
                if (layoutParams.weight > 0.0f) {
                    if (n10 == 0) {
                        n11 = b2;
                    }
                    max3 = Math.max(max3, n11);
                }
                else {
                    if (n10 == 0) {
                        n11 = b2;
                    }
                    max2 = Math.max(max2, n11);
                }
                i += this.getChildrenSkipCount(virtualChild, i);
                n4 = n9;
                n5 = n6;
            }
        }
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (mUseLargestChild && (mode2 == Integer.MIN_VALUE || mode2 == 0)) {
            this.mTotalLength = 0;
            for (int j = 0; j < virtualChildCount; ++j) {
                final View virtualChild2 = this.getVirtualChildAt(j);
                if (virtualChild2 == null) {
                    this.mTotalLength += this.measureNullChild(j);
                }
                else if (virtualChild2.getVisibility() == 8) {
                    j += this.getChildrenSkipCount(virtualChild2, j);
                }
                else {
                    final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                    final int mTotalLength4 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + b + layoutParams2.topMargin + layoutParams2.bottomMargin + this.getNextLocationOffset(virtualChild2));
                }
            }
        }
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        final int resolveSizeAndState = ViewCompat.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), n2, 0);
        final int n12 = (resolveSizeAndState & 0xFFFFFF) - this.mTotalLength;
        int max5;
        int n23;
        int n24;
        if (n5 != 0 || (n12 != 0 && mWeightSum > 0.0f)) {
            if (this.mWeightSum > 0.0f) {
                mWeightSum = this.mWeightSum;
            }
            this.mTotalLength = 0;
            int k = 0;
            max5 = max;
            int n13 = n12;
            while (k < virtualChildCount) {
                final View virtualChild3 = this.getVirtualChildAt(k);
                int n14;
                if (virtualChild3.getVisibility() == 8) {
                    n14 = n13;
                }
                else {
                    final LayoutParams layoutParams3 = (LayoutParams)virtualChild3.getLayoutParams();
                    final float weight = layoutParams3.weight;
                    int n15 = combineMeasuredStates;
                    n14 = n13;
                    float n16 = mWeightSum;
                    if (weight > 0.0f) {
                        int n17 = (int)(n13 * weight / mWeightSum);
                        n16 = mWeightSum - weight;
                        final int n18 = n13 - n17;
                        final int childMeasureSpec = getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                        if (layoutParams3.height != 0 || mode2 != 1073741824) {
                            int n19;
                            if ((n19 = virtualChild3.getMeasuredHeight() + n17) < 0) {
                                n19 = 0;
                            }
                            virtualChild3.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n19, 1073741824));
                        }
                        else {
                            if (n17 <= 0) {
                                n17 = 0;
                            }
                            virtualChild3.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n17, 1073741824));
                        }
                        final int combineMeasuredStates2 = ViewUtils.combineMeasuredStates(combineMeasuredStates, ViewCompat.getMeasuredState(virtualChild3) & 0xFFFFFF00);
                        n14 = n18;
                        n15 = combineMeasuredStates2;
                    }
                    final int n20 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                    final int b3 = virtualChild3.getMeasuredWidth() + n20;
                    max5 = Math.max(max5, b3);
                    int n21;
                    if (mode != 1073741824 && layoutParams3.width == -1) {
                        n21 = 1;
                    }
                    else {
                        n21 = 0;
                    }
                    int b4;
                    if (n21 != 0) {
                        b4 = n20;
                    }
                    else {
                        b4 = b3;
                    }
                    final int max6 = Math.max(max2, b4);
                    int n22;
                    if (n3 != 0 && layoutParams3.width == -1) {
                        n22 = 1;
                    }
                    else {
                        n22 = 0;
                    }
                    final int mTotalLength5 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength5, virtualChild3.getMeasuredHeight() + mTotalLength5 + layoutParams3.topMargin + layoutParams3.bottomMargin + this.getNextLocationOffset(virtualChild3));
                    n3 = n22;
                    max2 = max6;
                    combineMeasuredStates = n15;
                    mWeightSum = n16;
                }
                ++k;
                n13 = n14;
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
            n23 = combineMeasuredStates;
            n24 = n3;
        }
        else {
            final int max7 = Math.max(max2, max3);
            n24 = n3;
            max2 = max7;
            n23 = combineMeasuredStates;
            max5 = max;
            if (mUseLargestChild) {
                n24 = n3;
                max2 = max7;
                n23 = combineMeasuredStates;
                max5 = max;
                if (mode2 != 1073741824) {
                    int n25 = 0;
                    while (true) {
                        n24 = n3;
                        max2 = max7;
                        n23 = combineMeasuredStates;
                        max5 = max;
                        if (n25 >= virtualChildCount) {
                            break;
                        }
                        final View virtualChild4 = this.getVirtualChildAt(n25);
                        if (virtualChild4 != null && virtualChild4.getVisibility() != 8 && ((LayoutParams)virtualChild4.getLayoutParams()).weight > 0.0f) {
                            virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(virtualChild4.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(b, 1073741824));
                        }
                        ++n25;
                    }
                }
            }
        }
        int n26 = max5;
        if (n24 == 0) {
            n26 = max5;
            if (mode != 1073741824) {
                n26 = max2;
            }
        }
        this.setMeasuredDimension(ViewCompat.resolveSizeAndState(Math.max(n26 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), n, n23), resolveSizeAndState);
        if (n4 != 0) {
            this.forceUniformWidth(virtualChildCount, n2);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                this.drawDividersVertical(canvas);
            }
            else {
                this.drawDividersHorizontal(canvas);
            }
        }
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.mOrientation == 1) {
            this.layoutVertical(n, n2, n3, n4);
        }
        else {
            this.layoutHorizontal(n, n2, n3, n4);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mOrientation == 1) {
            this.measureVertical(n, n2);
        }
        else {
            this.measureHorizontal(n, n2);
        }
    }
    
    public void setBaselineAligned(final boolean mBaselineAligned) {
        this.mBaselineAligned = mBaselineAligned;
    }
    
    public void setBaselineAlignedChildIndex(final int mBaselineAlignedChildIndex) {
        if (mBaselineAlignedChildIndex < 0 || mBaselineAlignedChildIndex >= this.getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + this.getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = mBaselineAlignedChildIndex;
    }
    
    public void setDividerDrawable(final Drawable mDivider) {
        boolean willNotDraw = false;
        if (mDivider != this.mDivider) {
            if ((this.mDivider = mDivider) != null) {
                this.mDividerWidth = mDivider.getIntrinsicWidth();
                this.mDividerHeight = mDivider.getIntrinsicHeight();
            }
            else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (mDivider == null) {
                willNotDraw = true;
            }
            this.setWillNotDraw(willNotDraw);
            this.requestLayout();
        }
    }
    
    public void setDividerPadding(final int mDividerPadding) {
        this.mDividerPadding = mDividerPadding;
    }
    
    public void setGravity(int mGravity) {
        if (this.mGravity != mGravity) {
            int n = mGravity;
            if ((0x800007 & mGravity) == 0x0) {
                n = (mGravity | 0x800003);
            }
            mGravity = n;
            if ((n & 0x70) == 0x0) {
                mGravity = (n | 0x30);
            }
            this.mGravity = mGravity;
            this.requestLayout();
        }
    }
    
    public void setHorizontalGravity(int n) {
        n &= 0x800007;
        if ((this.mGravity & 0x800007) != n) {
            this.mGravity = ((this.mGravity & 0xFF7FFFF8) | n);
            this.requestLayout();
        }
    }
    
    public void setMeasureWithLargestChildEnabled(final boolean mUseLargestChild) {
        this.mUseLargestChild = mUseLargestChild;
    }
    
    public void setOrientation(final int mOrientation) {
        if (this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
            this.requestLayout();
        }
    }
    
    public void setShowDividers(final int mShowDividers) {
        if (mShowDividers != this.mShowDividers) {
            this.requestLayout();
        }
        this.mShowDividers = mShowDividers;
    }
    
    public void setVerticalGravity(int n) {
        n &= 0x70;
        if ((this.mGravity & 0x70) != n) {
            this.mGravity = ((this.mGravity & 0xFFFFFF8F) | n);
            this.requestLayout();
        }
    }
    
    public void setWeightSum(final float b) {
        this.mWeightSum = Math.max(0.0f, b);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int gravity;
        public float weight;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = -1;
            this.weight = 0.0f;
        }
        
        public LayoutParams(final int n, final int n2, final float weight) {
            super(n, n2);
            this.gravity = -1;
            this.weight = weight;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = -1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = -1;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.gravity = -1;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }
}
