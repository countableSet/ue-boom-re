// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.support.v4.os.ParcelableCompat;
import android.os.Parcel;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.content.res.TypedArray;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.annotation.DrawableRes;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.support.annotation.ColorInt;
import android.view.ViewGroup$MarginLayoutParams;
import android.graphics.Bitmap;
import android.util.Log;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.view.ViewConfiguration;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.view.View;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import android.view.ViewGroup;

public class SlidingPaneLayout extends ViewGroup
{
    private static final int DEFAULT_FADE_COLOR = -858993460;
    private static final int DEFAULT_OVERHANG_SIZE = 32;
    static final SlidingPanelLayoutImpl IMPL;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final String TAG = "SlidingPaneLayout";
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    final ViewDragHelper mDragHelper;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    boolean mIsUnableToDrag;
    private final int mOverhangSize;
    private PanelSlideListener mPanelSlideListener;
    private int mParallaxBy;
    private float mParallaxOffset;
    final ArrayList<DisableLayerRunnable> mPostedRunnables;
    boolean mPreservedOpenState;
    private Drawable mShadowDrawableLeft;
    private Drawable mShadowDrawableRight;
    float mSlideOffset;
    int mSlideRange;
    View mSlideableView;
    private int mSliderFadeColor;
    private final Rect mTmpRect;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT >= 17) {
            IMPL = (SlidingPanelLayoutImpl)new SlidingPanelLayoutImplJBMR1();
        }
        else if (sdk_INT >= 16) {
            IMPL = (SlidingPanelLayoutImpl)new SlidingPanelLayoutImplJB();
        }
        else {
            IMPL = (SlidingPanelLayoutImpl)new SlidingPanelLayoutImplBase();
        }
    }
    
    public SlidingPaneLayout(final Context context) {
        this(context, null);
    }
    
    public SlidingPaneLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SlidingPaneLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mSliderFadeColor = -858993460;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        this.mPostedRunnables = new ArrayList<DisableLayerRunnable>();
        final float density = context.getResources().getDisplayMetrics().density;
        this.mOverhangSize = (int)(32.0f * density + 0.5f);
        ViewConfiguration.get(context);
        this.setWillNotDraw(false);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility((View)this, 1);
        (this.mDragHelper = ViewDragHelper.create(this, 0.5f, (ViewDragHelper.Callback)new DragHelperCallback())).setMinVelocity(400.0f * density);
    }
    
    private boolean closePane(final View view, final int n) {
        boolean b = false;
        if (this.mFirstLayout || this.smoothSlideTo(0.0f, n)) {
            this.mPreservedOpenState = false;
            b = true;
        }
        return b;
    }
    
    private void dimChildView(final View view, final float n, final int n2) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (n > 0.0f && n2 != 0) {
            final int n3 = (int)(((0xFF000000 & n2) >>> 24) * n);
            if (layoutParams.dimPaint == null) {
                layoutParams.dimPaint = new Paint();
            }
            layoutParams.dimPaint.setColorFilter((ColorFilter)new PorterDuffColorFilter(n3 << 24 | (0xFFFFFF & n2), PorterDuff$Mode.SRC_OVER));
            if (ViewCompat.getLayerType(view) != 2) {
                ViewCompat.setLayerType(view, 2, layoutParams.dimPaint);
            }
            this.invalidateChildRegion(view);
        }
        else if (ViewCompat.getLayerType(view) != 0) {
            if (layoutParams.dimPaint != null) {
                layoutParams.dimPaint.setColorFilter((ColorFilter)null);
            }
            final DisableLayerRunnable e = new DisableLayerRunnable(view);
            this.mPostedRunnables.add(e);
            ViewCompat.postOnAnimation((View)this, e);
        }
    }
    
    private boolean openPane(final View view, final int n) {
        boolean b = true;
        if (this.mFirstLayout || this.smoothSlideTo(1.0f, n)) {
            this.mPreservedOpenState = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private void parallaxOtherViews(final float mParallaxOffset) {
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        final LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
        while (true) {
            Label_0087: {
                if (!layoutParams.dimWhenOffset) {
                    break Label_0087;
                }
                int n;
                if (layoutRtlSupport) {
                    n = layoutParams.rightMargin;
                }
                else {
                    n = layoutParams.leftMargin;
                }
                if (n > 0) {
                    break Label_0087;
                }
                final boolean b = true;
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    if (child != this.mSlideableView) {
                        final int n2 = (int)((1.0f - this.mParallaxOffset) * this.mParallaxBy);
                        this.mParallaxOffset = mParallaxOffset;
                        int n3 = n2 - (int)((1.0f - mParallaxOffset) * this.mParallaxBy);
                        if (layoutRtlSupport) {
                            n3 = -n3;
                        }
                        child.offsetLeftAndRight(n3);
                        if (b) {
                            float n4;
                            if (layoutRtlSupport) {
                                n4 = this.mParallaxOffset - 1.0f;
                            }
                            else {
                                n4 = 1.0f - this.mParallaxOffset;
                            }
                            this.dimChildView(child, n4, this.mCoveredFadeColor);
                        }
                    }
                }
                return;
            }
            final boolean b = false;
            continue;
        }
    }
    
    private static boolean viewIsOpaque(final View view) {
        boolean b = true;
        if (!view.isOpaque()) {
            if (Build$VERSION.SDK_INT >= 18) {
                b = false;
            }
            else {
                final Drawable background = view.getBackground();
                if (background != null) {
                    if (background.getOpacity() != -1) {
                        b = false;
                    }
                }
                else {
                    b = false;
                }
            }
        }
        return b;
    }
    
    protected boolean canScroll(final View view, final boolean b, int n, final int n2, final int n3) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            final int scrollX = view.getScrollX();
            final int scrollY = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (n2 + scrollX >= child.getLeft() && n2 + scrollX < child.getRight() && n3 + scrollY >= child.getTop() && n3 + scrollY < child.getBottom() && this.canScroll(child, true, n, n2 + scrollX - child.getLeft(), n3 + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        Label_0143: {
            break Label_0143;
        }
        if (b) {
            if (!this.isLayoutRtlSupport()) {
                n = -n;
            }
            if (ViewCompat.canScrollHorizontally(view, n)) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean canSlide() {
        return this.mCanSlide;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public boolean closePane() {
        return this.closePane(this.mSlideableView, 0);
    }
    
    public void computeScroll() {
        if (this.mDragHelper.continueSettling(true)) {
            if (!this.mCanSlide) {
                this.mDragHelper.abort();
            }
            else {
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
    }
    
    void dispatchOnPanelClosed(final View view) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelClosed(view);
        }
        this.sendAccessibilityEvent(32);
    }
    
    void dispatchOnPanelOpened(final View view) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelOpened(view);
        }
        this.sendAccessibilityEvent(32);
    }
    
    void dispatchOnPanelSlide(final View view) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelSlide(view, this.mSlideOffset);
        }
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        Drawable drawable;
        if (this.isLayoutRtlSupport()) {
            drawable = this.mShadowDrawableRight;
        }
        else {
            drawable = this.mShadowDrawableLeft;
        }
        View child;
        if (this.getChildCount() > 1) {
            child = this.getChildAt(1);
        }
        else {
            child = null;
        }
        if (child != null && drawable != null) {
            final int top = child.getTop();
            final int bottom = child.getBottom();
            final int intrinsicWidth = drawable.getIntrinsicWidth();
            int right;
            int left;
            if (this.isLayoutRtlSupport()) {
                right = child.getRight();
                left = right + intrinsicWidth;
            }
            else {
                left = child.getLeft();
                right = left - intrinsicWidth;
            }
            drawable.setBounds(right, top, left, bottom);
            drawable.draw(canvas);
        }
    }
    
    protected boolean drawChild(final Canvas canvas, final View obj, final long n) {
        final LayoutParams layoutParams = (LayoutParams)obj.getLayoutParams();
        final int save = canvas.save(2);
        if (this.mCanSlide && !layoutParams.slideable && this.mSlideableView != null) {
            canvas.getClipBounds(this.mTmpRect);
            if (this.isLayoutRtlSupport()) {
                this.mTmpRect.left = Math.max(this.mTmpRect.left, this.mSlideableView.getRight());
            }
            else {
                this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
            }
            canvas.clipRect(this.mTmpRect);
        }
        boolean b;
        if (Build$VERSION.SDK_INT >= 11) {
            b = super.drawChild(canvas, obj, n);
        }
        else if (layoutParams.dimWhenOffset && this.mSlideOffset > 0.0f) {
            if (!obj.isDrawingCacheEnabled()) {
                obj.setDrawingCacheEnabled(true);
            }
            final Bitmap drawingCache = obj.getDrawingCache();
            if (drawingCache != null) {
                canvas.drawBitmap(drawingCache, (float)obj.getLeft(), (float)obj.getTop(), layoutParams.dimPaint);
                b = false;
            }
            else {
                Log.e("SlidingPaneLayout", "drawChild: child view " + obj + " returned null drawing cache");
                b = super.drawChild(canvas, obj, n);
            }
        }
        else {
            if (obj.isDrawingCacheEnabled()) {
                obj.setDrawingCacheEnabled(false);
            }
            b = super.drawChild(canvas, obj, n);
        }
        canvas.restoreToCount(save);
        return b;
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return (ViewGroup$LayoutParams)new LayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return (ViewGroup$LayoutParams)new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        LayoutParams layoutParams;
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            layoutParams = new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        else {
            layoutParams = new LayoutParams(viewGroup$LayoutParams);
        }
        return (ViewGroup$LayoutParams)layoutParams;
    }
    
    @ColorInt
    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }
    
    public int getParallaxDistance() {
        return this.mParallaxBy;
    }
    
    @ColorInt
    public int getSliderFadeColor() {
        return this.mSliderFadeColor;
    }
    
    void invalidateChildRegion(final View view) {
        SlidingPaneLayout.IMPL.invalidateChildRegion(this, view);
    }
    
    boolean isDimmed(final View view) {
        final boolean b = false;
        boolean b2;
        if (view == null) {
            b2 = b;
        }
        else {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            b2 = b;
            if (this.mCanSlide) {
                b2 = b;
                if (layoutParams.dimWhenOffset) {
                    b2 = b;
                    if (this.mSlideOffset > 0.0f) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    boolean isLayoutRtlSupport() {
        boolean b = true;
        if (ViewCompat.getLayoutDirection((View)this) != 1) {
            b = false;
        }
        return b;
    }
    
    public boolean isOpen() {
        return !this.mCanSlide || this.mSlideOffset == 1.0f;
    }
    
    public boolean isSlideable() {
        return this.mCanSlide;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
        for (int i = 0; i < this.mPostedRunnables.size(); ++i) {
            this.mPostedRunnables.get(i).run();
        }
        this.mPostedRunnables.clear();
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (!this.mCanSlide && actionMasked == 0 && this.getChildCount() > 1) {
            final View child = this.getChildAt(1);
            if (child != null) {
                this.mPreservedOpenState = !this.mDragHelper.isViewUnder(child, (int)motionEvent.getX(), (int)motionEvent.getY());
            }
        }
        boolean onInterceptTouchEvent;
        if (!this.mCanSlide || (this.mIsUnableToDrag && actionMasked != 0)) {
            this.mDragHelper.cancel();
            onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        }
        else if (actionMasked == 3 || actionMasked == 1) {
            this.mDragHelper.cancel();
            onInterceptTouchEvent = false;
        }
        else {
            int n2;
            final int n = n2 = 0;
            while (true) {
                switch (actionMasked) {
                    default: {
                        n2 = n;
                    }
                    case 1: {
                        onInterceptTouchEvent = (this.mDragHelper.shouldInterceptTouchEvent(motionEvent) || n2 != 0);
                        break;
                    }
                    case 0: {
                        this.mIsUnableToDrag = false;
                        final float x = motionEvent.getX();
                        final float y = motionEvent.getY();
                        this.mInitialMotionX = x;
                        this.mInitialMotionY = y;
                        n2 = n;
                        if (!this.mDragHelper.isViewUnder(this.mSlideableView, (int)x, (int)y)) {
                            continue;
                        }
                        n2 = n;
                        if (this.isDimmed(this.mSlideableView)) {
                            n2 = 1;
                        }
                        continue;
                    }
                    case 2: {
                        final float x2 = motionEvent.getX();
                        final float y2 = motionEvent.getY();
                        final float abs = Math.abs(x2 - this.mInitialMotionX);
                        final float abs2 = Math.abs(y2 - this.mInitialMotionY);
                        n2 = n;
                        if (abs <= this.mDragHelper.getTouchSlop()) {
                            continue;
                        }
                        n2 = n;
                        if (abs2 > abs) {
                            this.mDragHelper.cancel();
                            this.mIsUnableToDrag = true;
                            onInterceptTouchEvent = false;
                            break;
                        }
                        continue;
                    }
                }
                break;
            }
        }
        return onInterceptTouchEvent;
    }
    
    protected void onLayout(final boolean b, int i, int n, int n2, int j) {
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        if (layoutRtlSupport) {
            this.mDragHelper.setEdgeTrackingEnabled(2);
        }
        else {
            this.mDragHelper.setEdgeTrackingEnabled(1);
        }
        final int n3 = n2 - i;
        if (layoutRtlSupport) {
            i = this.getPaddingRight();
        }
        else {
            i = this.getPaddingLeft();
        }
        if (layoutRtlSupport) {
            n2 = this.getPaddingLeft();
        }
        else {
            n2 = this.getPaddingRight();
        }
        final int paddingTop = this.getPaddingTop();
        final int childCount = this.getChildCount();
        j = i;
        if (this.mFirstLayout) {
            float mSlideOffset;
            if (this.mCanSlide && this.mPreservedOpenState) {
                mSlideOffset = 1.0f;
            }
            else {
                mSlideOffset = 0.0f;
            }
            this.mSlideOffset = mSlideOffset;
        }
        final int n4 = 0;
        n = i;
        i = j;
        View child;
        LayoutParams layoutParams;
        int measuredWidth;
        int n5;
        int mSlideRange;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        for (j = n4; j < childCount; ++j) {
            child = this.getChildAt(j);
            if (child.getVisibility() != 8) {
                layoutParams = (LayoutParams)child.getLayoutParams();
                measuredWidth = child.getMeasuredWidth();
                n5 = 0;
                if (layoutParams.slideable) {
                    mSlideRange = Math.min(i, n3 - n2 - this.mOverhangSize) - n - (layoutParams.leftMargin + layoutParams.rightMargin);
                    this.mSlideRange = mSlideRange;
                    if (layoutRtlSupport) {
                        n6 = layoutParams.rightMargin;
                    }
                    else {
                        n6 = layoutParams.leftMargin;
                    }
                    layoutParams.dimWhenOffset = (n + n6 + mSlideRange + measuredWidth / 2 > n3 - n2);
                    n7 = (int)(mSlideRange * this.mSlideOffset);
                    n += n7 + n6;
                    this.mSlideOffset = n7 / (float)this.mSlideRange;
                    n8 = n5;
                }
                else if (this.mCanSlide && this.mParallaxBy != 0) {
                    n8 = (int)((1.0f - this.mSlideOffset) * this.mParallaxBy);
                    n = i;
                }
                else {
                    n = i;
                    n8 = n5;
                }
                if (layoutRtlSupport) {
                    n9 = n3 - n + n8;
                    n10 = n9 - measuredWidth;
                }
                else {
                    n10 = n - n8;
                    n9 = n10 + measuredWidth;
                }
                child.layout(n10, paddingTop, n9, paddingTop + child.getMeasuredHeight());
                i += child.getWidth();
            }
        }
        if (this.mFirstLayout) {
            if (this.mCanSlide) {
                if (this.mParallaxBy != 0) {
                    this.parallaxOtherViews(this.mSlideOffset);
                }
                if (((LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
                    this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
                }
            }
            else {
                for (i = 0; i < childCount; ++i) {
                    this.dimChildView(this.getChildAt(i), 0.0f, this.mSliderFadeColor);
                }
            }
            this.updateObscuredViewsVisibility(this.mSlideableView);
        }
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(int n, int size) {
        final int mode = View$MeasureSpec.getMode(n);
        final int size2 = View$MeasureSpec.getSize(n);
        final int mode2 = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        int n2;
        int n3;
        if (mode != 1073741824) {
            if (!this.isInEditMode()) {
                throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
            }
            if (mode == Integer.MIN_VALUE) {
                n2 = size2;
                n = size;
                n3 = mode2;
            }
            else {
                n3 = mode2;
                n = size;
                n2 = size2;
                if (mode == 0) {
                    n2 = 300;
                    n3 = mode2;
                    n = size;
                }
            }
        }
        else {
            n3 = mode2;
            n = size;
            n2 = size2;
            if (mode2 == 0) {
                if (!this.isInEditMode()) {
                    throw new IllegalStateException("Height must not be UNSPECIFIED");
                }
                n3 = mode2;
                n = size;
                n2 = size2;
                if (mode2 == 0) {
                    n3 = Integer.MIN_VALUE;
                    n = 300;
                    n2 = size2;
                }
            }
        }
        int n4 = 0;
        size = -1;
        switch (n3) {
            case 1073741824: {
                size = (n4 = n - this.getPaddingTop() - this.getPaddingBottom());
                break;
            }
            case Integer.MIN_VALUE: {
                size = n - this.getPaddingTop() - this.getPaddingBottom();
                break;
            }
        }
        float n5 = 0.0f;
        boolean mCanSlide = false;
        int b;
        final int n6 = b = n2 - this.getPaddingLeft() - this.getPaddingRight();
        final int childCount = this.getChildCount();
        if (childCount > 2) {
            Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
        }
        this.mSlideableView = null;
        int n7;
        int n8;
        boolean b2;
        for (int i = 0; i < childCount; ++i, mCanSlide = b2, n4 = n8, b = n7) {
            final View child = this.getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (child.getVisibility() == 8) {
                layoutParams.dimWhenOffset = false;
                n7 = b;
                n8 = n4;
                b2 = mCanSlide;
            }
            else {
                float n9 = n5;
                if (layoutParams.weight > 0.0f) {
                    n9 = n5 + layoutParams.weight;
                    b2 = mCanSlide;
                    n8 = n4;
                    n5 = n9;
                    n7 = b;
                    if (layoutParams.width == 0) {
                        continue;
                    }
                }
                n = layoutParams.leftMargin + layoutParams.rightMargin;
                if (layoutParams.width == -2) {
                    n = View$MeasureSpec.makeMeasureSpec(n6 - n, Integer.MIN_VALUE);
                }
                else if (layoutParams.width == -1) {
                    n = View$MeasureSpec.makeMeasureSpec(n6 - n, 1073741824);
                }
                else {
                    n = View$MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
                }
                int n10;
                if (layoutParams.height == -2) {
                    n10 = View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
                }
                else if (layoutParams.height == -1) {
                    n10 = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                }
                else {
                    n10 = View$MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
                }
                child.measure(n, n10);
                final int measuredWidth = child.getMeasuredWidth();
                final int measuredHeight = child.getMeasuredHeight();
                n = n4;
                if (n3 == Integer.MIN_VALUE && measuredHeight > (n = n4)) {
                    n = Math.min(measuredHeight, size);
                }
                final int n11 = b - measuredWidth;
                final boolean slideable = n11 < 0 || false;
                layoutParams.slideable = slideable;
                b2 = (mCanSlide | slideable);
                n8 = n;
                n5 = n9;
                n7 = n11;
                if (layoutParams.slideable) {
                    this.mSlideableView = child;
                    b2 = b2;
                    n8 = n;
                    n5 = n9;
                    n7 = n11;
                }
            }
        }
        if (mCanSlide || n5 > 0.0f) {
            final int n12 = n6 - this.mOverhangSize;
            for (int j = 0; j < childCount; ++j) {
                final View child2 = this.getChildAt(j);
                if (child2.getVisibility() != 8) {
                    final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                    if (child2.getVisibility() != 8) {
                        if (layoutParams2.width == 0 && layoutParams2.weight > 0.0f) {
                            n = 1;
                        }
                        else {
                            n = 0;
                        }
                        int measuredWidth2;
                        if (n != 0) {
                            measuredWidth2 = 0;
                        }
                        else {
                            measuredWidth2 = child2.getMeasuredWidth();
                        }
                        if (mCanSlide && child2 != this.mSlideableView) {
                            if (layoutParams2.width < 0 && (measuredWidth2 > n12 || layoutParams2.weight > 0.0f)) {
                                if (n != 0) {
                                    if (layoutParams2.height == -2) {
                                        n = View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
                                    }
                                    else if (layoutParams2.height == -1) {
                                        n = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                                    }
                                    else {
                                        n = View$MeasureSpec.makeMeasureSpec(layoutParams2.height, 1073741824);
                                    }
                                }
                                else {
                                    n = View$MeasureSpec.makeMeasureSpec(child2.getMeasuredHeight(), 1073741824);
                                }
                                child2.measure(View$MeasureSpec.makeMeasureSpec(n12, 1073741824), n);
                            }
                        }
                        else if (layoutParams2.weight > 0.0f) {
                            if (layoutParams2.width == 0) {
                                if (layoutParams2.height == -2) {
                                    n = View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
                                }
                                else if (layoutParams2.height == -1) {
                                    n = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                                }
                                else {
                                    n = View$MeasureSpec.makeMeasureSpec(layoutParams2.height, 1073741824);
                                }
                            }
                            else {
                                n = View$MeasureSpec.makeMeasureSpec(child2.getMeasuredHeight(), 1073741824);
                            }
                            if (mCanSlide) {
                                final int n13 = n6 - (layoutParams2.leftMargin + layoutParams2.rightMargin);
                                final int measureSpec = View$MeasureSpec.makeMeasureSpec(n13, 1073741824);
                                if (measuredWidth2 != n13) {
                                    child2.measure(measureSpec, n);
                                }
                            }
                            else {
                                child2.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth2 + (int)(layoutParams2.weight * Math.max(0, b) / n5), 1073741824), n);
                            }
                        }
                    }
                }
            }
        }
        this.setMeasuredDimension(n2, this.getPaddingTop() + n4 + this.getPaddingBottom());
        this.mCanSlide = mCanSlide;
        if (this.mDragHelper.getViewDragState() != 0 && !mCanSlide) {
            this.mDragHelper.abort();
        }
    }
    
    void onPanelDragged(int n) {
        if (this.mSlideableView == null) {
            this.mSlideOffset = 0.0f;
        }
        else {
            final boolean layoutRtlSupport = this.isLayoutRtlSupport();
            final LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
            final int width = this.mSlideableView.getWidth();
            if (layoutRtlSupport) {
                n = this.getWidth() - n - width;
            }
            int n2;
            if (layoutRtlSupport) {
                n2 = this.getPaddingRight();
            }
            else {
                n2 = this.getPaddingLeft();
            }
            int n3;
            if (layoutRtlSupport) {
                n3 = layoutParams.rightMargin;
            }
            else {
                n3 = layoutParams.leftMargin;
            }
            this.mSlideOffset = (n - (n2 + n3)) / (float)this.mSlideRange;
            if (this.mParallaxBy != 0) {
                this.parallaxOtherViews(this.mSlideOffset);
            }
            if (layoutParams.dimWhenOffset) {
                this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
            }
            this.dispatchOnPanelSlide(this.mSlideableView);
        }
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final SavedState savedState = (SavedState)parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            if (savedState.isOpen) {
                this.openPane();
            }
            else {
                this.closePane();
            }
            this.mPreservedOpenState = savedState.isOpen;
        }
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        boolean isOpen;
        if (this.isSlideable()) {
            isOpen = this.isOpen();
        }
        else {
            isOpen = this.mPreservedOpenState;
        }
        savedState.isOpen = isOpen;
        return (Parcelable)savedState;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.mFirstLayout = true;
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        boolean onTouchEvent = false;
        if (!this.mCanSlide) {
            onTouchEvent = super.onTouchEvent(motionEvent);
        }
        else {
            this.mDragHelper.processTouchEvent(motionEvent);
            final int action = motionEvent.getAction();
            final boolean b = true;
            switch (action & 0xFF) {
                default: {
                    onTouchEvent = b;
                    break;
                }
                case 0: {
                    final float x = motionEvent.getX();
                    final float y = motionEvent.getY();
                    this.mInitialMotionX = x;
                    this.mInitialMotionY = y;
                    onTouchEvent = b;
                    break;
                }
                case 1: {
                    onTouchEvent = b;
                    if (!this.isDimmed(this.mSlideableView)) {
                        break;
                    }
                    final float x2 = motionEvent.getX();
                    final float y2 = motionEvent.getY();
                    final float n = x2 - this.mInitialMotionX;
                    final float n2 = y2 - this.mInitialMotionY;
                    final int touchSlop = this.mDragHelper.getTouchSlop();
                    onTouchEvent = b;
                    if (n * n + n2 * n2 >= touchSlop * touchSlop) {
                        break;
                    }
                    onTouchEvent = b;
                    if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)x2, (int)y2)) {
                        this.closePane(this.mSlideableView, 0);
                        onTouchEvent = b;
                        break;
                    }
                    break;
                }
            }
        }
        return onTouchEvent;
    }
    
    public boolean openPane() {
        return this.openPane(this.mSlideableView, 0);
    }
    
    public void requestChildFocus(final View view, final View view2) {
        super.requestChildFocus(view, view2);
        if (!this.isInTouchMode() && !this.mCanSlide) {
            this.mPreservedOpenState = (view == this.mSlideableView);
        }
    }
    
    void setAllChildrenVisible() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 4) {
                child.setVisibility(0);
            }
        }
    }
    
    public void setCoveredFadeColor(@ColorInt final int mCoveredFadeColor) {
        this.mCoveredFadeColor = mCoveredFadeColor;
    }
    
    public void setPanelSlideListener(final PanelSlideListener mPanelSlideListener) {
        this.mPanelSlideListener = mPanelSlideListener;
    }
    
    public void setParallaxDistance(final int mParallaxBy) {
        this.mParallaxBy = mParallaxBy;
        this.requestLayout();
    }
    
    @Deprecated
    public void setShadowDrawable(final Drawable shadowDrawableLeft) {
        this.setShadowDrawableLeft(shadowDrawableLeft);
    }
    
    public void setShadowDrawableLeft(final Drawable mShadowDrawableLeft) {
        this.mShadowDrawableLeft = mShadowDrawableLeft;
    }
    
    public void setShadowDrawableRight(final Drawable mShadowDrawableRight) {
        this.mShadowDrawableRight = mShadowDrawableRight;
    }
    
    @Deprecated
    public void setShadowResource(@DrawableRes final int n) {
        this.setShadowDrawable(this.getResources().getDrawable(n));
    }
    
    public void setShadowResourceLeft(final int n) {
        this.setShadowDrawableLeft(this.getResources().getDrawable(n));
    }
    
    public void setShadowResourceRight(final int n) {
        this.setShadowDrawableRight(this.getResources().getDrawable(n));
    }
    
    public void setSliderFadeColor(@ColorInt final int mSliderFadeColor) {
        this.mSliderFadeColor = mSliderFadeColor;
    }
    
    @Deprecated
    public void smoothSlideClosed() {
        this.closePane();
    }
    
    @Deprecated
    public void smoothSlideOpen() {
        this.openPane();
    }
    
    boolean smoothSlideTo(final float n, int paddingRight) {
        boolean b = false;
        if (this.mCanSlide) {
            final boolean layoutRtlSupport = this.isLayoutRtlSupport();
            final LayoutParams layoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
            if (layoutRtlSupport) {
                paddingRight = this.getPaddingRight();
                paddingRight = (int)(this.getWidth() - (paddingRight + layoutParams.rightMargin + this.mSlideRange * n + this.mSlideableView.getWidth()));
            }
            else {
                paddingRight = (int)(this.getPaddingLeft() + layoutParams.leftMargin + this.mSlideRange * n);
            }
            if (this.mDragHelper.smoothSlideViewTo(this.mSlideableView, paddingRight, this.mSlideableView.getTop())) {
                this.setAllChildrenVisible();
                ViewCompat.postInvalidateOnAnimation((View)this);
                b = true;
            }
        }
        return b;
    }
    
    void updateObscuredViewsVisibility(final View view) {
        final boolean layoutRtlSupport = this.isLayoutRtlSupport();
        int paddingLeft;
        if (layoutRtlSupport) {
            paddingLeft = this.getWidth() - this.getPaddingRight();
        }
        else {
            paddingLeft = this.getPaddingLeft();
        }
        int paddingLeft2;
        if (layoutRtlSupport) {
            paddingLeft2 = this.getPaddingLeft();
        }
        else {
            paddingLeft2 = this.getWidth() - this.getPaddingRight();
        }
        final int paddingTop = this.getPaddingTop();
        final int height = this.getHeight();
        final int paddingBottom = this.getPaddingBottom();
        int left;
        int right;
        int top;
        int bottom;
        if (view != null && viewIsOpaque(view)) {
            left = view.getLeft();
            right = view.getRight();
            top = view.getTop();
            bottom = view.getBottom();
        }
        else {
            bottom = 0;
            top = 0;
            right = 0;
            left = 0;
        }
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child == view) {
                break;
            }
            if (child.getVisibility() != 8) {
                int a;
                if (layoutRtlSupport) {
                    a = paddingLeft2;
                }
                else {
                    a = paddingLeft;
                }
                final int max = Math.max(a, child.getLeft());
                final int max2 = Math.max(paddingTop, child.getTop());
                int a2;
                if (layoutRtlSupport) {
                    a2 = paddingLeft;
                }
                else {
                    a2 = paddingLeft2;
                }
                final int min = Math.min(a2, child.getRight());
                final int min2 = Math.min(height - paddingBottom, child.getBottom());
                int visibility;
                if (max >= left && max2 >= top && min <= right && min2 <= bottom) {
                    visibility = 4;
                }
                else {
                    visibility = 0;
                }
                child.setVisibility(visibility);
            }
        }
    }
    
    class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        private final Rect mTmpRect;
        
        AccessibilityDelegate() {
            this.mTmpRect = new Rect();
        }
        
        private void copyNodeInfoNoChildren(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            final Rect mTmpRect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(mTmpRect);
            accessibilityNodeInfoCompat.setBoundsInParent(mTmpRect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(mTmpRect);
            accessibilityNodeInfoCompat.setBoundsInScreen(mTmpRect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
            accessibilityNodeInfoCompat.setMovementGranularities(accessibilityNodeInfoCompat2.getMovementGranularities());
        }
        
        public boolean filter(final View view) {
            return SlidingPaneLayout.this.isDimmed(view);
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)SlidingPaneLayout.class.getName());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(View child, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            final AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
            super.onInitializeAccessibilityNodeInfo(child, obtain);
            this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, obtain);
            obtain.recycle();
            accessibilityNodeInfoCompat.setClassName(SlidingPaneLayout.class.getName());
            accessibilityNodeInfoCompat.setSource(child);
            final ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(child);
            if (parentForAccessibility instanceof View) {
                accessibilityNodeInfoCompat.setParent((View)parentForAccessibility);
            }
            for (int childCount = SlidingPaneLayout.this.getChildCount(), i = 0; i < childCount; ++i) {
                child = SlidingPaneLayout.this.getChildAt(i);
                if (!this.filter(child) && child.getVisibility() == 0) {
                    ViewCompat.setImportantForAccessibility(child, 1);
                    accessibilityNodeInfoCompat.addChild(child);
                }
            }
        }
        
        @Override
        public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
            return !this.filter(view) && super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }
    
    private class DisableLayerRunnable implements Runnable
    {
        final View mChildView;
        
        DisableLayerRunnable(final View mChildView) {
            this.mChildView = mChildView;
        }
        
        @Override
        public void run() {
            if (this.mChildView.getParent() == SlidingPaneLayout.this) {
                ViewCompat.setLayerType(this.mChildView, 0, null);
                SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
            }
            SlidingPaneLayout.this.mPostedRunnables.remove(this);
        }
    }
    
    private class DragHelperCallback extends Callback
    {
        DragHelperCallback() {
        }
        
        @Override
        public int clampViewPositionHorizontal(final View view, int n, int mSlideRange) {
            final LayoutParams layoutParams = (LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
            if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                final int b = SlidingPaneLayout.this.getWidth() - (SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth());
                mSlideRange = SlidingPaneLayout.this.mSlideRange;
                n = Math.max(Math.min(n, b), b - mSlideRange);
            }
            else {
                mSlideRange = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
                n = Math.min(Math.max(n, mSlideRange), mSlideRange + SlidingPaneLayout.this.mSlideRange);
            }
            return n;
        }
        
        @Override
        public int clampViewPositionVertical(final View view, final int n, final int n2) {
            return view.getTop();
        }
        
        @Override
        public int getViewHorizontalDragRange(final View view) {
            return SlidingPaneLayout.this.mSlideRange;
        }
        
        @Override
        public void onEdgeDragStarted(final int n, final int n2) {
            SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, n2);
        }
        
        @Override
        public void onViewCaptured(final View view, final int n) {
            SlidingPaneLayout.this.setAllChildrenVisible();
        }
        
        @Override
        public void onViewDragStateChanged(final int n) {
            if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
                if (SlidingPaneLayout.this.mSlideOffset == 0.0f) {
                    SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
                    SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
                    SlidingPaneLayout.this.mPreservedOpenState = false;
                }
                else {
                    SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
                    SlidingPaneLayout.this.mPreservedOpenState = true;
                }
            }
        }
        
        @Override
        public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
            SlidingPaneLayout.this.onPanelDragged(n);
            SlidingPaneLayout.this.invalidate();
        }
        
        @Override
        public void onViewReleased(final View view, final float n, final float n2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n5 = 0;
            Label_0106: {
                if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
                    final int n3 = SlidingPaneLayout.this.getPaddingRight() + layoutParams.rightMargin;
                    int n4 = 0;
                    Label_0079: {
                        if (n >= 0.0f) {
                            n4 = n3;
                            if (n != 0.0f) {
                                break Label_0079;
                            }
                            n4 = n3;
                            if (SlidingPaneLayout.this.mSlideOffset <= 0.5f) {
                                break Label_0079;
                            }
                        }
                        n4 = n3 + SlidingPaneLayout.this.mSlideRange;
                    }
                    n5 = SlidingPaneLayout.this.getWidth() - n4 - SlidingPaneLayout.this.mSlideableView.getWidth();
                }
                else {
                    final int n6 = SlidingPaneLayout.this.getPaddingLeft() + layoutParams.leftMargin;
                    if (n <= 0.0f) {
                        n5 = n6;
                        if (n != 0.0f) {
                            break Label_0106;
                        }
                        n5 = n6;
                        if (SlidingPaneLayout.this.mSlideOffset <= 0.5f) {
                            break Label_0106;
                        }
                    }
                    n5 = n6 + SlidingPaneLayout.this.mSlideRange;
                }
            }
            SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(n5, view.getTop());
            SlidingPaneLayout.this.invalidate();
        }
        
        @Override
        public boolean tryCaptureView(final View view, final int n) {
            return !SlidingPaneLayout.this.mIsUnableToDrag && ((LayoutParams)view.getLayoutParams()).slideable;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        private static final int[] ATTRS;
        Paint dimPaint;
        boolean dimWhenOffset;
        boolean slideable;
        public float weight;
        
        static {
            ATTRS = new int[] { 16843137 };
        }
        
        public LayoutParams() {
            super(-1, -1);
            this.weight = 0.0f;
        }
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.weight = 0.0f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.weight = 0.0f;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, LayoutParams.ATTRS);
            this.weight = obtainStyledAttributes.getFloat(0, 0.0f);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.weight = 0.0f;
            this.weight = layoutParams.weight;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.weight = 0.0f;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.weight = 0.0f;
        }
    }
    
    public interface PanelSlideListener
    {
        void onPanelClosed(final View p0);
        
        void onPanelOpened(final View p0);
        
        void onPanelSlide(final View p0, final float p1);
    }
    
    static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean isOpen;
        
        static {
            CREATOR = ParcelableCompat.newCreator((ParcelableCompatCreatorCallbacks<SavedState>)new ParcelableCompatCreatorCallbacks<SavedState>() {
                @Override
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                @Override
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            });
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isOpen = (parcel.readInt() != 0);
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            if (this.isOpen) {
                n = 1;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
        }
    }
    
    public static class SimplePanelSlideListener implements PanelSlideListener
    {
        @Override
        public void onPanelClosed(final View view) {
        }
        
        @Override
        public void onPanelOpened(final View view) {
        }
        
        @Override
        public void onPanelSlide(final View view, final float n) {
        }
    }
    
    interface SlidingPanelLayoutImpl
    {
        void invalidateChildRegion(final SlidingPaneLayout p0, final View p1);
    }
    
    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl
    {
        @Override
        public void invalidateChildRegion(final SlidingPaneLayout slidingPaneLayout, final View view) {
            ViewCompat.postInvalidateOnAnimation((View)slidingPaneLayout, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
    }
    
    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase
    {
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;
        
        SlidingPanelLayoutImplJB() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     4: aload_0        
            //     5: ldc             Landroid/view/View;.class
            //     7: ldc             "getDisplayList"
            //     9: aconst_null    
            //    10: checkcast       [Ljava/lang/Class;
            //    13: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
            //    16: putfield        android/support/v4/widget/SlidingPaneLayout$SlidingPanelLayoutImplJB.mGetDisplayList:Ljava/lang/reflect/Method;
            //    19: aload_0        
            //    20: ldc             Landroid/view/View;.class
            //    22: ldc             "mRecreateDisplayList"
            //    24: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
            //    27: putfield        android/support/v4/widget/SlidingPaneLayout$SlidingPanelLayoutImplJB.mRecreateDisplayList:Ljava/lang/reflect/Field;
            //    30: aload_0        
            //    31: getfield        android/support/v4/widget/SlidingPaneLayout$SlidingPanelLayoutImplJB.mRecreateDisplayList:Ljava/lang/reflect/Field;
            //    34: iconst_1       
            //    35: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
            //    38: return         
            //    39: astore_1       
            //    40: ldc             "SlidingPaneLayout"
            //    42: ldc             "Couldn't fetch getDisplayList method; dimming won't work right."
            //    44: aload_1        
            //    45: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //    48: pop            
            //    49: goto            19
            //    52: astore_1       
            //    53: ldc             "SlidingPaneLayout"
            //    55: ldc             "Couldn't fetch mRecreateDisplayList field; dimming will be slow."
            //    57: aload_1        
            //    58: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //    61: pop            
            //    62: goto            38
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                             
            //  -----  -----  -----  -----  ---------------------------------
            //  4      19     39     52     Ljava/lang/NoSuchMethodException;
            //  19     38     52     65     Ljava/lang/NoSuchFieldException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0019:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public void invalidateChildRegion(final SlidingPaneLayout slidingPaneLayout, final View view) {
            Label_0056: {
                if (this.mGetDisplayList == null || this.mRecreateDisplayList == null) {
                    break Label_0056;
                }
                while (true) {
                    try {
                        this.mRecreateDisplayList.setBoolean(view, true);
                        this.mGetDisplayList.invoke(view, (Object[])null);
                        super.invalidateChildRegion(slidingPaneLayout, view);
                        return;
                    }
                    catch (Exception ex) {
                        Log.e("SlidingPaneLayout", "Error refreshing display list state", (Throwable)ex);
                        continue;
                    }
                    break;
                }
            }
            view.invalidate();
        }
    }
    
    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase
    {
        @Override
        public void invalidateChildRegion(final SlidingPaneLayout slidingPaneLayout, final View view) {
            ViewCompat.setLayerPaint(view, ((LayoutParams)view.getLayoutParams()).dimPaint);
        }
    }
}
