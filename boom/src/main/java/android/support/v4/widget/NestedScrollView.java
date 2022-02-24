// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.widget.ScrollView;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.support.v4.view.VelocityTrackerCompat;
import android.os.Parcelable;
import android.widget.FrameLayout$LayoutParams;
import android.util.Log;
import android.support.v4.view.MotionEventCompat;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.FocusFinder;
import android.view.ViewGroup$LayoutParams;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.util.TypedValue;
import java.util.ArrayList;
import android.content.res.TypedArray;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.Rect;
import android.support.v4.view.NestedScrollingParentHelper;
import android.view.View;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.widget.FrameLayout;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent, NestedScrollingChild, ScrollingView
{
    private static final AccessibilityDelegate ACCESSIBILITY_DELEGATE;
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int[] SCROLLVIEW_STYLEABLE;
    private static final String TAG = "NestedScrollView";
    private int mActivePointerId;
    private final NestedScrollingChildHelper mChildHelper;
    private View mChildToScrollTo;
    private EdgeEffectCompat mEdgeGlowBottom;
    private EdgeEffectCompat mEdgeGlowTop;
    private boolean mFillViewport;
    private boolean mIsBeingDragged;
    private boolean mIsLaidOut;
    private boolean mIsLayoutDirty;
    private int mLastMotionY;
    private long mLastScroll;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int mNestedYOffset;
    private OnScrollChangeListener mOnScrollChangeListener;
    private final NestedScrollingParentHelper mParentHelper;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private ScrollerCompat mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;
    
    static {
        ACCESSIBILITY_DELEGATE = new AccessibilityDelegate();
        SCROLLVIEW_STYLEABLE = new int[] { 16843130 };
    }
    
    public NestedScrollView(final Context context) {
        this(context, null);
    }
    
    public NestedScrollView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NestedScrollView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mIsLaidOut = false;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.initScrollView();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, NestedScrollView.SCROLLVIEW_STYLEABLE, n, 0);
        this.setFillViewport(obtainStyledAttributes.getBoolean(0, false));
        obtainStyledAttributes.recycle();
        this.mParentHelper = new NestedScrollingParentHelper((ViewGroup)this);
        this.mChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)this, NestedScrollView.ACCESSIBILITY_DELEGATE);
    }
    
    private boolean canScroll() {
        final boolean b = false;
        final View child = this.getChildAt(0);
        boolean b2 = b;
        if (child != null) {
            final int height = child.getHeight();
            b2 = b;
            if (this.getHeight() < this.getPaddingTop() + height + this.getPaddingBottom()) {
                b2 = true;
            }
        }
        return b2;
    }
    
    private static int clamp(final int n, final int n2, final int n3) {
        int n4;
        if (n2 >= n3 || n < 0) {
            n4 = 0;
        }
        else {
            n4 = n;
            if (n2 + n > n3) {
                n4 = n3 - n2;
            }
        }
        return n4;
    }
    
    private void doScrollY(final int n) {
        if (n != 0) {
            if (this.mSmoothScrollingEnabled) {
                this.smoothScrollBy(0, n);
            }
            else {
                this.scrollBy(0, n);
            }
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.recycleVelocityTracker();
        this.stopNestedScroll();
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
    }
    
    private void ensureGlows() {
        if (this.getOverScrollMode() != 2) {
            if (this.mEdgeGlowTop == null) {
                final Context context = this.getContext();
                this.mEdgeGlowTop = new EdgeEffectCompat(context);
                this.mEdgeGlowBottom = new EdgeEffectCompat(context);
            }
        }
        else {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        }
    }
    
    private View findFocusableViewInBounds(final boolean b, final int n, final int n2) {
        final ArrayList focusables = this.getFocusables(2);
        View view = null;
        int n3 = 0;
        View view3;
        int n4;
        for (int size = focusables.size(), i = 0; i < size; ++i, view = view3, n3 = n4) {
            final View view2 = focusables.get(i);
            final int top = view2.getTop();
            final int bottom = view2.getBottom();
            view3 = view;
            n4 = n3;
            if (n < bottom) {
                view3 = view;
                n4 = n3;
                if (top < n2) {
                    int n5;
                    if (n < top && bottom < n2) {
                        n5 = 1;
                    }
                    else {
                        n5 = 0;
                    }
                    if (view == null) {
                        view3 = view2;
                        n4 = n5;
                    }
                    else {
                        boolean b2;
                        if ((b && top < view.getTop()) || (!b && bottom > view.getBottom())) {
                            b2 = true;
                        }
                        else {
                            b2 = false;
                        }
                        if (n3 != 0) {
                            view3 = view;
                            n4 = n3;
                            if (n5 != 0) {
                                view3 = view;
                                n4 = n3;
                                if (b2) {
                                    view3 = view2;
                                    n4 = n3;
                                }
                            }
                        }
                        else if (n5 != 0) {
                            view3 = view2;
                            n4 = 1;
                        }
                        else {
                            view3 = view;
                            n4 = n3;
                            if (b2) {
                                view3 = view2;
                                n4 = n3;
                            }
                        }
                    }
                }
            }
        }
        return view;
    }
    
    private void flingWithNestedDispatch(final int n) {
        final int scrollY = this.getScrollY();
        final boolean b = (scrollY > 0 || n > 0) && (scrollY < this.getScrollRange() || n < 0);
        if (!this.dispatchNestedPreFling(0.0f, (float)n)) {
            this.dispatchNestedFling(0.0f, (float)n, b);
            if (b) {
                this.fling(n);
            }
        }
    }
    
    private float getVerticalScrollFactorCompat() {
        if (this.mVerticalScrollFactor == 0.0f) {
            final TypedValue typedValue = new TypedValue();
            final Context context = this.getContext();
            if (!context.getTheme().resolveAttribute(16842829, typedValue, true)) {
                throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
            }
            this.mVerticalScrollFactor = typedValue.getDimension(context.getResources().getDisplayMetrics());
        }
        return this.mVerticalScrollFactor;
    }
    
    private boolean inChild(final int n, final int n2) {
        boolean b2;
        final boolean b = b2 = false;
        if (this.getChildCount() > 0) {
            final int scrollY = this.getScrollY();
            final View child = this.getChildAt(0);
            b2 = b;
            if (n2 >= child.getTop() - scrollY) {
                b2 = b;
                if (n2 < child.getBottom() - scrollY) {
                    b2 = b;
                    if (n >= child.getLeft()) {
                        b2 = b;
                        if (n < child.getRight()) {
                            b2 = true;
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            this.mVelocityTracker.clear();
        }
    }
    
    private void initScrollView() {
        this.mScroller = ScrollerCompat.create(this.getContext(), null);
        this.setFocusable(true);
        this.setDescendantFocusability(262144);
        this.setWillNotDraw(false);
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinimumVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = value.getScaledMaximumFlingVelocity();
    }
    
    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    private boolean isOffScreen(final View view) {
        boolean b = false;
        if (!this.isWithinDeltaOfScreen(view, 0, this.getHeight())) {
            b = true;
        }
        return b;
    }
    
    private static boolean isViewDescendantOf(final View view, final View view2) {
        boolean b = true;
        if (view != view2) {
            final ViewParent parent = view.getParent();
            if (!(parent instanceof ViewGroup) || !isViewDescendantOf((View)parent, view2)) {
                b = false;
            }
        }
        return b;
    }
    
    private boolean isWithinDeltaOfScreen(final View view, final int n, final int n2) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        return this.mTempRect.bottom + n >= this.getScrollY() && this.mTempRect.top - n <= this.getScrollY() + n2;
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int n = (motionEvent.getAction() & 0xFF00) >> 8;
        if (motionEvent.getPointerId(n) == this.mActivePointerId) {
            int n2;
            if (n == 0) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            this.mLastMotionY = (int)motionEvent.getY(n2);
            this.mActivePointerId = motionEvent.getPointerId(n2);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }
    
    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private boolean scrollAndFocus(final int n, int n2, final int n3) {
        final boolean b = true;
        final int height = this.getHeight();
        final int scrollY = this.getScrollY();
        final int n4 = scrollY + height;
        final boolean b2 = n == 33;
        Object focusableViewInBounds;
        if ((focusableViewInBounds = this.findFocusableViewInBounds(b2, n2, n3)) == null) {
            focusableViewInBounds = this;
        }
        boolean b3;
        if (n2 >= scrollY && n3 <= n4) {
            b3 = false;
        }
        else {
            if (b2) {
                n2 -= scrollY;
            }
            else {
                n2 = n3 - n4;
            }
            this.doScrollY(n2);
            b3 = b;
        }
        if (focusableViewInBounds != this.findFocus()) {
            ((View)focusableViewInBounds).requestFocus(n);
        }
        return b3;
    }
    
    private void scrollToChild(final View view) {
        view.getDrawingRect(this.mTempRect);
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        final int computeScrollDeltaToGetChildRectOnScreen = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (computeScrollDeltaToGetChildRectOnScreen != 0) {
            this.scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
        }
    }
    
    private boolean scrollToChildRect(final Rect rect, final boolean b) {
        final int computeScrollDeltaToGetChildRectOnScreen = this.computeScrollDeltaToGetChildRectOnScreen(rect);
        final boolean b2 = computeScrollDeltaToGetChildRectOnScreen != 0;
        if (b2) {
            if (b) {
                this.scrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
            }
            else {
                this.smoothScrollBy(0, computeScrollDeltaToGetChildRectOnScreen);
            }
        }
        return b2;
    }
    
    public void addView(final View view) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view);
    }
    
    public void addView(final View view, final int n) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, n);
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, n, viewGroup$LayoutParams);
    }
    
    public void addView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(view, viewGroup$LayoutParams);
    }
    
    public boolean arrowScroll(int descendantFocusability) {
        boolean b = false;
        View focus;
        if ((focus = this.findFocus()) == this) {
            focus = null;
        }
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focus, descendantFocusability);
        final int maxScrollAmount = this.getMaxScrollAmount();
        if (nextFocus != null && this.isWithinDeltaOfScreen(nextFocus, maxScrollAmount, this.getHeight())) {
            nextFocus.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(nextFocus, this.mTempRect);
            this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            nextFocus.requestFocus(descendantFocusability);
        }
        else {
            final int n = maxScrollAmount;
            int scrollY;
            if (descendantFocusability == 33 && this.getScrollY() < n) {
                scrollY = this.getScrollY();
            }
            else {
                scrollY = n;
                if (descendantFocusability == 130) {
                    scrollY = n;
                    if (this.getChildCount() > 0) {
                        final int bottom = this.getChildAt(0).getBottom();
                        final int n2 = this.getScrollY() + this.getHeight() - this.getPaddingBottom();
                        scrollY = n;
                        if (bottom - n2 < maxScrollAmount) {
                            scrollY = bottom - n2;
                        }
                    }
                }
            }
            if (scrollY == 0) {
                return b;
            }
            if (descendantFocusability == 130) {
                descendantFocusability = scrollY;
            }
            else {
                descendantFocusability = -scrollY;
            }
            this.doScrollY(descendantFocusability);
        }
        if (focus != null && focus.isFocused() && this.isOffScreen(focus)) {
            descendantFocusability = this.getDescendantFocusability();
            this.setDescendantFocusability(131072);
            this.requestFocus();
            this.setDescendantFocusability(descendantFocusability);
        }
        b = true;
        return b;
    }
    
    public int computeHorizontalScrollExtent() {
        return super.computeHorizontalScrollExtent();
    }
    
    public int computeHorizontalScrollOffset() {
        return super.computeHorizontalScrollOffset();
    }
    
    public int computeHorizontalScrollRange() {
        return super.computeHorizontalScrollRange();
    }
    
    public void computeScroll() {
        final boolean b = true;
        if (this.mScroller.computeScrollOffset()) {
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                final int scrollRange = this.getScrollRange();
                final int overScrollMode = this.getOverScrollMode();
                int n = b ? 1 : 0;
                if (overScrollMode != 0) {
                    if (overScrollMode == 1 && scrollRange > 0) {
                        n = (b ? 1 : 0);
                    }
                    else {
                        n = 0;
                    }
                }
                this.overScrollByCompat(currX - scrollX, currY - scrollY, scrollX, scrollY, 0, scrollRange, 0, 0, false);
                if (n != 0) {
                    this.ensureGlows();
                    if (currY <= 0 && scrollY > 0) {
                        this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
                    }
                    else if (currY >= scrollRange && scrollY < scrollRange) {
                        this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
                    }
                }
            }
        }
    }
    
    protected int computeScrollDeltaToGetChildRectOnScreen(final Rect rect) {
        int n;
        if (this.getChildCount() == 0) {
            n = 0;
        }
        else {
            final int height = this.getHeight();
            final int scrollY = this.getScrollY();
            final int n2 = scrollY + height;
            final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
            int n3 = scrollY;
            if (rect.top > 0) {
                n3 = scrollY + verticalFadingEdgeLength;
            }
            int n4 = n2;
            if (rect.bottom < this.getChildAt(0).getHeight()) {
                n4 = n2 - verticalFadingEdgeLength;
            }
            final boolean b = false;
            if (rect.bottom > n4 && rect.top > n3) {
                int a;
                if (rect.height() > height) {
                    a = 0 + (rect.top - n3);
                }
                else {
                    a = 0 + (rect.bottom - n4);
                }
                n = Math.min(a, this.getChildAt(0).getBottom() - n4);
            }
            else {
                n = (b ? 1 : 0);
                if (rect.top < n3) {
                    n = (b ? 1 : 0);
                    if (rect.bottom < n4) {
                        int a2;
                        if (rect.height() > height) {
                            a2 = 0 - (n4 - rect.bottom);
                        }
                        else {
                            a2 = 0 - (n3 - rect.top);
                        }
                        n = Math.max(a2, -this.getScrollY());
                    }
                }
            }
        }
        return n;
    }
    
    public int computeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }
    
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }
    
    public int computeVerticalScrollRange() {
        final int childCount = this.getChildCount();
        int n = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
        if (childCount != 0) {
            final int bottom = this.getChildAt(0).getBottom();
            final int scrollY = this.getScrollY();
            final int max = Math.max(0, bottom - n);
            if (scrollY < 0) {
                n = bottom - scrollY;
            }
            else {
                n = bottom;
                if (scrollY > max) {
                    n = bottom + (scrollY - max);
                }
            }
        }
        return n;
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.mChildHelper.dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.mChildHelper.dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.mChildHelper.dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.mChildHelper.dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowTop != null) {
            final int scrollY = this.getScrollY();
            if (!this.mEdgeGlowTop.isFinished()) {
                final int save = canvas.save();
                final int width = this.getWidth();
                final int paddingLeft = this.getPaddingLeft();
                final int paddingRight = this.getPaddingRight();
                canvas.translate((float)this.getPaddingLeft(), (float)Math.min(0, scrollY));
                this.mEdgeGlowTop.setSize(width - paddingLeft - paddingRight, this.getHeight());
                if (this.mEdgeGlowTop.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                canvas.restoreToCount(save);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                final int save2 = canvas.save();
                final int n = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                final int height = this.getHeight();
                canvas.translate((float)(-n + this.getPaddingLeft()), (float)(Math.max(this.getScrollRange(), scrollY) + height));
                canvas.rotate(180.0f, (float)n, 0.0f);
                this.mEdgeGlowBottom.setSize(n, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                canvas.restoreToCount(save2);
            }
        }
    }
    
    public boolean executeKeyEvent(final KeyEvent keyEvent) {
        final boolean b = false;
        this.mTempRect.setEmpty();
        boolean b2;
        if (!this.canScroll()) {
            b2 = b;
            if (this.isFocused()) {
                b2 = b;
                if (keyEvent.getKeyCode() != 4) {
                    View focus;
                    if ((focus = this.findFocus()) == this) {
                        focus = null;
                    }
                    final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focus, 130);
                    b2 = b;
                    if (nextFocus != null) {
                        b2 = b;
                        if (nextFocus != this) {
                            b2 = b;
                            if (nextFocus.requestFocus(130)) {
                                b2 = true;
                            }
                        }
                    }
                }
            }
        }
        else {
            final boolean b3 = b2 = false;
            if (keyEvent.getAction() == 0) {
                switch (keyEvent.getKeyCode()) {
                    default: {
                        b2 = b3;
                        break;
                    }
                    case 19: {
                        if (!keyEvent.isAltPressed()) {
                            b2 = this.arrowScroll(33);
                            break;
                        }
                        b2 = this.fullScroll(33);
                        break;
                    }
                    case 20: {
                        if (!keyEvent.isAltPressed()) {
                            b2 = this.arrowScroll(130);
                            break;
                        }
                        b2 = this.fullScroll(130);
                        break;
                    }
                    case 62: {
                        int n;
                        if (keyEvent.isShiftPressed()) {
                            n = 33;
                        }
                        else {
                            n = 130;
                        }
                        this.pageScroll(n);
                        b2 = b3;
                        break;
                    }
                }
            }
        }
        return b2;
    }
    
    public void fling(final int n) {
        if (this.getChildCount() > 0) {
            final int n2 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
            this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, n, 0, 0, 0, Math.max(0, this.getChildAt(0).getHeight() - n2), 0, n2 / 2);
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean fullScroll(final int n) {
        int n2;
        if (n == 130) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final int height = this.getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = height;
        if (n2 != 0) {
            final int childCount = this.getChildCount();
            if (childCount > 0) {
                this.mTempRect.bottom = this.getChildAt(childCount - 1).getBottom() + this.getPaddingBottom();
                this.mTempRect.top = this.mTempRect.bottom - height;
            }
        }
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }
    
    protected float getBottomFadingEdgeStrength() {
        float n;
        if (this.getChildCount() == 0) {
            n = 0.0f;
        }
        else {
            final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
            final int n2 = this.getChildAt(0).getBottom() - this.getScrollY() - (this.getHeight() - this.getPaddingBottom());
            if (n2 < verticalFadingEdgeLength) {
                n = n2 / (float)verticalFadingEdgeLength;
            }
            else {
                n = 1.0f;
            }
        }
        return n;
    }
    
    public int getMaxScrollAmount() {
        return (int)(0.5f * this.getHeight());
    }
    
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }
    
    int getScrollRange() {
        int max = 0;
        if (this.getChildCount() > 0) {
            max = Math.max(0, this.getChildAt(0).getHeight() - (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()));
        }
        return max;
    }
    
    protected float getTopFadingEdgeStrength() {
        float n;
        if (this.getChildCount() == 0) {
            n = 0.0f;
        }
        else {
            final int verticalFadingEdgeLength = this.getVerticalFadingEdgeLength();
            final int scrollY = this.getScrollY();
            if (scrollY < verticalFadingEdgeLength) {
                n = scrollY / (float)verticalFadingEdgeLength;
            }
            else {
                n = 1.0f;
            }
        }
        return n;
    }
    
    public boolean hasNestedScrollingParent() {
        return this.mChildHelper.hasNestedScrollingParent();
    }
    
    public boolean isFillViewport() {
        return this.mFillViewport;
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.mChildHelper.isNestedScrollingEnabled();
    }
    
    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }
    
    protected void measureChild(final View view, final int n, final int n2) {
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight(), view.getLayoutParams().width), View$MeasureSpec.makeMeasureSpec(0, 0));
    }
    
    protected void measureChildWithMargins(final View view, final int n, final int n2, final int n3, final int n4) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin + n2, viewGroup$MarginLayoutParams.width), View$MeasureSpec.makeMeasureSpec(viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin, 0));
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsLaidOut = false;
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 0x2) != 0x0) {
            switch (motionEvent.getAction()) {
                case 8: {
                    if (this.mIsBeingDragged) {
                        break;
                    }
                    final float axisValue = MotionEventCompat.getAxisValue(motionEvent, 9);
                    if (axisValue == 0.0f) {
                        break;
                    }
                    final int n = (int)(this.getVerticalScrollFactorCompat() * axisValue);
                    final int scrollRange = this.getScrollRange();
                    final int scrollY = this.getScrollY();
                    final int n2 = scrollY - n;
                    int n3;
                    if (n2 < 0) {
                        n3 = 0;
                    }
                    else if ((n3 = n2) > scrollRange) {
                        n3 = scrollRange;
                    }
                    if (n3 != scrollY) {
                        super.scrollTo(this.getScrollX(), n3);
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final boolean b = true;
        boolean mIsBeingDragged = false;
        final int action = motionEvent.getAction();
        boolean mIsBeingDragged2;
        if (action == 2 && this.mIsBeingDragged) {
            mIsBeingDragged2 = b;
        }
        else {
            switch (action & 0xFF) {
                case 2: {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId == -1) {
                        break;
                    }
                    final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                    if (pointerIndex == -1) {
                        Log.e("NestedScrollView", "Invalid pointerId=" + mActivePointerId + " in onInterceptTouchEvent");
                        break;
                    }
                    final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                    if (Math.abs(mLastMotionY - this.mLastMotionY) <= this.mTouchSlop || (this.getNestedScrollAxes() & 0x2) != 0x0) {
                        break;
                    }
                    this.mIsBeingDragged = true;
                    this.mLastMotionY = mLastMotionY;
                    this.initVelocityTrackerIfNotExists();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mNestedYOffset = 0;
                    final ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    break;
                }
                case 0: {
                    final int mLastMotionY2 = (int)motionEvent.getY();
                    if (!this.inChild((int)motionEvent.getX(), mLastMotionY2)) {
                        this.mIsBeingDragged = false;
                        this.recycleVelocityTracker();
                        break;
                    }
                    this.mLastMotionY = mLastMotionY2;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.initOrResetVelocityTracker();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mScroller.computeScrollOffset();
                    if (!this.mScroller.isFinished()) {
                        mIsBeingDragged = true;
                    }
                    this.mIsBeingDragged = mIsBeingDragged;
                    this.startNestedScroll(2);
                    break;
                }
                case 1:
                case 3: {
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    this.recycleVelocityTracker();
                    if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                        ViewCompat.postInvalidateOnAnimation((View)this);
                    }
                    this.stopNestedScroll();
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    break;
                }
            }
            mIsBeingDragged2 = this.mIsBeingDragged;
        }
        return mIsBeingDragged2;
    }
    
    protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, (View)this)) {
            this.scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!this.mIsLaidOut) {
            if (this.mSavedState != null) {
                this.scrollTo(this.getScrollX(), this.mSavedState.scrollPosition);
                this.mSavedState = null;
            }
            if (this.getChildCount() > 0) {
                n = this.getChildAt(0).getMeasuredHeight();
            }
            else {
                n = 0;
            }
            n = Math.max(0, n - (n4 - n2 - this.getPaddingBottom() - this.getPaddingTop()));
            if (this.getScrollY() > n) {
                this.scrollTo(this.getScrollX(), n);
            }
            else if (this.getScrollY() < 0) {
                this.scrollTo(this.getScrollX(), 0);
            }
        }
        this.scrollTo(this.getScrollX(), this.getScrollY());
        this.mIsLaidOut = true;
    }
    
    protected void onMeasure(final int n, int measuredHeight) {
        super.onMeasure(n, measuredHeight);
        if (this.mFillViewport && View$MeasureSpec.getMode(measuredHeight) != 0 && this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            measuredHeight = this.getMeasuredHeight();
            if (child.getMeasuredHeight() < measuredHeight) {
                child.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight(), ((FrameLayout$LayoutParams)child.getLayoutParams()).width), View$MeasureSpec.makeMeasureSpec(measuredHeight - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
            }
        }
    }
    
    public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
        boolean b2;
        if (!b) {
            this.flingWithNestedDispatch((int)n2);
            b2 = true;
        }
        else {
            b2 = false;
        }
        return b2;
    }
    
    public boolean onNestedPreFling(final View view, final float n, final float n2) {
        return this.dispatchNestedPreFling(n, n2);
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
        this.dispatchNestedPreScroll(n, n2, array, null);
    }
    
    public void onNestedScroll(final View view, int scrollY, final int n, final int n2, final int n3) {
        scrollY = this.getScrollY();
        this.scrollBy(0, n3);
        scrollY = this.getScrollY() - scrollY;
        this.dispatchNestedScroll(0, scrollY, 0, n3 - scrollY, null);
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.mParentHelper.onNestedScrollAccepted(view, view2, n);
        this.startNestedScroll(2);
    }
    
    protected void onOverScrolled(final int n, final int n2, final boolean b, final boolean b2) {
        super.scrollTo(n, n2);
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        boolean requestFocus = false;
        int n2;
        if (n == 2) {
            n2 = 130;
        }
        else if ((n2 = n) == 1) {
            n2 = 33;
        }
        View view;
        if (rect == null) {
            view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)null, n2);
        }
        else {
            view = FocusFinder.getInstance().findNextFocusFromRect((ViewGroup)this, rect, n2);
        }
        if (view != null && !this.isOffScreen(view)) {
            requestFocus = view.requestFocus(n2, rect);
        }
        return requestFocus;
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final SavedState mSavedState = (SavedState)parcelable;
            super.onRestoreInstanceState(mSavedState.getSuperState());
            this.mSavedState = mSavedState;
            this.requestLayout();
        }
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.scrollPosition = this.getScrollY();
        return (Parcelable)savedState;
    }
    
    protected void onScrollChanged(final int n, final int n2, final int n3, final int n4) {
        super.onScrollChanged(n, n2, n3, n4);
        if (this.mOnScrollChangeListener != null) {
            this.mOnScrollChangeListener.onScrollChange(this, n, n2, n3, n4);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        final View focus = this.findFocus();
        if (focus != null && this != focus && this.isWithinDeltaOfScreen(focus, 0, n4)) {
            focus.getDrawingRect(this.mTempRect);
            this.offsetDescendantRectToMyCoords(focus, this.mTempRect);
            this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
        }
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n) {
        return (n & 0x2) != 0x0;
    }
    
    public void onStopNestedScroll(final View view) {
        this.mParentHelper.onStopNestedScroll(view);
        this.stopNestedScroll();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.initVelocityTrackerIfNotExists();
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 0) {
            this.mNestedYOffset = 0;
        }
        obtain.offsetLocation(0.0f, (float)this.mNestedYOffset);
        switch (actionMasked) {
            case 0: {
                if (this.getChildCount() == 0) {
                    return false;
                }
                final boolean mIsBeingDragged = !this.mScroller.isFinished();
                this.mIsBeingDragged = mIsBeingDragged;
                if (mIsBeingDragged) {
                    final ViewParent parent = this.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.mLastMotionY = (int)motionEvent.getY();
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.startNestedScroll(2);
                break;
            }
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (pointerIndex == -1) {
                    Log.e("NestedScrollView", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                    break;
                }
                final int n = (int)motionEvent.getY(pointerIndex);
                int a;
                final int n2 = a = this.mLastMotionY - n;
                if (this.dispatchNestedPreScroll(0, n2, this.mScrollConsumed, this.mScrollOffset)) {
                    a = n2 - this.mScrollConsumed[1];
                    obtain.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                    this.mNestedYOffset += this.mScrollOffset[1];
                }
                int n3 = a;
                if (!this.mIsBeingDragged) {
                    n3 = a;
                    if (Math.abs(a) > this.mTouchSlop) {
                        final ViewParent parent2 = this.getParent();
                        if (parent2 != null) {
                            parent2.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        if (a > 0) {
                            n3 = a - this.mTouchSlop;
                        }
                        else {
                            n3 = a + this.mTouchSlop;
                        }
                    }
                }
                if (!this.mIsBeingDragged) {
                    break;
                }
                this.mLastMotionY = n - this.mScrollOffset[1];
                final int scrollY = this.getScrollY();
                final int scrollRange = this.getScrollRange();
                final int overScrollMode = this.getOverScrollMode();
                final boolean b2 = overScrollMode == 0 || (overScrollMode == 1 && scrollRange > 0);
                if (this.overScrollByCompat(0, n3, 0, this.getScrollY(), 0, scrollRange, 0, 0, true) && !this.hasNestedScrollingParent()) {
                    this.mVelocityTracker.clear();
                }
                final int n4 = this.getScrollY() - scrollY;
                if (this.dispatchNestedScroll(0, n4, 0, n3 - n4, this.mScrollOffset)) {
                    this.mLastMotionY -= this.mScrollOffset[1];
                    obtain.offsetLocation(0.0f, (float)this.mScrollOffset[1]);
                    this.mNestedYOffset += this.mScrollOffset[1];
                    break;
                }
                if (!b2) {
                    break;
                }
                this.ensureGlows();
                final int n5 = scrollY + n3;
                if (n5 < 0) {
                    this.mEdgeGlowTop.onPull(n3 / (float)this.getHeight(), motionEvent.getX(pointerIndex) / this.getWidth());
                    if (!this.mEdgeGlowBottom.isFinished()) {
                        this.mEdgeGlowBottom.onRelease();
                    }
                }
                else if (n5 > scrollRange) {
                    this.mEdgeGlowBottom.onPull(n3 / (float)this.getHeight(), 1.0f - motionEvent.getX(pointerIndex) / this.getWidth());
                    if (!this.mEdgeGlowTop.isFinished()) {
                        this.mEdgeGlowTop.onRelease();
                    }
                }
                if (this.mEdgeGlowTop != null && (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                    break;
                }
                break;
            }
            case 1: {
                if (this.mIsBeingDragged) {
                    final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                    mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                    final int a2 = (int)VelocityTrackerCompat.getYVelocity(mVelocityTracker, this.mActivePointerId);
                    if (Math.abs(a2) > this.mMinimumVelocity) {
                        this.flingWithNestedDispatch(-a2);
                    }
                    else if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                        ViewCompat.postInvalidateOnAnimation((View)this);
                    }
                }
                this.mActivePointerId = -1;
                this.endDrag();
                break;
            }
            case 3: {
                if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
                    ViewCompat.postInvalidateOnAnimation((View)this);
                }
                this.mActivePointerId = -1;
                this.endDrag();
                break;
            }
            case 5: {
                final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionY = (int)motionEvent.getY(actionIndex);
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                break;
            }
            case 6: {
                this.onSecondaryPointerUp(motionEvent);
                this.mLastMotionY = (int)motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                break;
            }
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }
    
    boolean overScrollByCompat(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, final boolean b) {
        final int overScrollMode = this.getOverScrollMode();
        boolean b2;
        if (this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent()) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        boolean b3;
        if (this.computeVerticalScrollRange() > this.computeVerticalScrollExtent()) {
            b3 = true;
        }
        else {
            b3 = false;
        }
        int n9;
        if (overScrollMode == 0 || (overScrollMode == 1 && b2)) {
            n9 = 1;
        }
        else {
            n9 = 0;
        }
        int n10;
        if (overScrollMode == 0 || (overScrollMode == 1 && b3)) {
            n10 = 1;
        }
        else {
            n10 = 0;
        }
        n3 += n;
        if (n9 == 0) {
            n7 = 0;
        }
        n4 += n2;
        if (n10 == 0) {
            n8 = 0;
        }
        n2 = -n7;
        n = n7 + n5;
        n5 = -n8;
        n6 += n8;
        boolean b4 = false;
        if (n3 > n) {
            b4 = true;
        }
        else if ((n = n3) < n2) {
            n = n2;
            b4 = true;
        }
        boolean b5 = false;
        if (n4 > n6) {
            n2 = n6;
            b5 = true;
        }
        else if ((n2 = n4) < n5) {
            n2 = n5;
            b5 = true;
        }
        if (b5) {
            this.mScroller.springBack(n, n2, 0, 0, 0, this.getScrollRange());
        }
        this.onOverScrolled(n, n2, b4, b5);
        return b4 || b5;
    }
    
    public boolean pageScroll(final int n) {
        int n2;
        if (n == 130) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final int height = this.getHeight();
        if (n2 != 0) {
            this.mTempRect.top = this.getScrollY() + height;
            final int childCount = this.getChildCount();
            if (childCount > 0) {
                final View child = this.getChildAt(childCount - 1);
                if (this.mTempRect.top + height > child.getBottom()) {
                    this.mTempRect.top = child.getBottom() - height;
                }
            }
        }
        else {
            this.mTempRect.top = this.getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        this.mTempRect.bottom = this.mTempRect.top + height;
        return this.scrollAndFocus(n, this.mTempRect.top, this.mTempRect.bottom);
    }
    
    public void requestChildFocus(final View view, final View mChildToScrollTo) {
        if (!this.mIsLayoutDirty) {
            this.scrollToChild(mChildToScrollTo);
        }
        else {
            this.mChildToScrollTo = mChildToScrollTo;
        }
        super.requestChildFocus(view, mChildToScrollTo);
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
        return this.scrollToChildRect(rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        if (b) {
            this.recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }
    
    public void scrollTo(int clamp, int clamp2) {
        if (this.getChildCount() > 0) {
            final View child = this.getChildAt(0);
            clamp = clamp(clamp, this.getWidth() - this.getPaddingRight() - this.getPaddingLeft(), child.getWidth());
            clamp2 = clamp(clamp2, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop(), child.getHeight());
            if (clamp != this.getScrollX() || clamp2 != this.getScrollY()) {
                super.scrollTo(clamp, clamp2);
            }
        }
    }
    
    public void setFillViewport(final boolean mFillViewport) {
        if (mFillViewport != this.mFillViewport) {
            this.mFillViewport = mFillViewport;
            this.requestLayout();
        }
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.mChildHelper.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public void setOnScrollChangeListener(final OnScrollChangeListener mOnScrollChangeListener) {
        this.mOnScrollChangeListener = mOnScrollChangeListener;
    }
    
    public void setSmoothScrollingEnabled(final boolean mSmoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = mSmoothScrollingEnabled;
    }
    
    public boolean shouldDelayChildPressedState() {
        return true;
    }
    
    public final void smoothScrollBy(int n, int max) {
        if (this.getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
                n = this.getHeight();
                final int max2 = Math.max(0, this.getChildAt(0).getHeight() - (n - this.getPaddingBottom() - this.getPaddingTop()));
                n = this.getScrollY();
                max = Math.max(0, Math.min(n + max, max2));
                this.mScroller.startScroll(this.getScrollX(), n, 0, max - n);
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
            else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                this.scrollBy(n, max);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }
    
    public final void smoothScrollTo(final int n, final int n2) {
        this.smoothScrollBy(n - this.getScrollX(), n2 - this.getScrollY());
    }
    
    public boolean startNestedScroll(final int n) {
        return this.mChildHelper.startNestedScroll(n);
    }
    
    public void stopNestedScroll() {
        this.mChildHelper.stopNestedScroll();
    }
    
    static class AccessibilityDelegate extends AccessibilityDelegateCompat
    {
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            accessibilityEvent.setClassName((CharSequence)ScrollView.class.getName());
            final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(accessibilityEvent);
            record.setScrollable(nestedScrollView.getScrollRange() > 0);
            record.setScrollX(nestedScrollView.getScrollX());
            record.setScrollY(nestedScrollView.getScrollY());
            record.setMaxScrollX(nestedScrollView.getScrollX());
            record.setMaxScrollY(nestedScrollView.getScrollRange());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            final NestedScrollView nestedScrollView = (NestedScrollView)view;
            accessibilityNodeInfoCompat.setClassName(ScrollView.class.getName());
            if (nestedScrollView.isEnabled()) {
                final int scrollRange = nestedScrollView.getScrollRange();
                if (scrollRange > 0) {
                    accessibilityNodeInfoCompat.setScrollable(true);
                    if (nestedScrollView.getScrollY() > 0) {
                        accessibilityNodeInfoCompat.addAction(8192);
                    }
                    if (nestedScrollView.getScrollY() < scrollRange) {
                        accessibilityNodeInfoCompat.addAction(4096);
                    }
                }
            }
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, int n, final Bundle bundle) {
            boolean b = true;
            if (!super.performAccessibilityAction(view, n, bundle)) {
                final NestedScrollView nestedScrollView = (NestedScrollView)view;
                if (!nestedScrollView.isEnabled()) {
                    b = false;
                }
                else {
                    switch (n) {
                        default: {
                            b = false;
                            break;
                        }
                        case 4096: {
                            final int height = nestedScrollView.getHeight();
                            n = nestedScrollView.getPaddingBottom();
                            n = Math.min(nestedScrollView.getScrollY() + (height - n - nestedScrollView.getPaddingTop()), nestedScrollView.getScrollRange());
                            if (n != nestedScrollView.getScrollY()) {
                                nestedScrollView.smoothScrollTo(0, n);
                                break;
                            }
                            b = false;
                            break;
                        }
                        case 8192: {
                            final int height2 = nestedScrollView.getHeight();
                            n = nestedScrollView.getPaddingBottom();
                            n = Math.max(nestedScrollView.getScrollY() - (height2 - n - nestedScrollView.getPaddingTop()), 0);
                            if (n != nestedScrollView.getScrollY()) {
                                nestedScrollView.smoothScrollTo(0, n);
                                break;
                            }
                            b = false;
                            break;
                        }
                    }
                }
            }
            return b;
        }
    }
    
    public interface OnScrollChangeListener
    {
        void onScrollChange(final NestedScrollView p0, final int p1, final int p2, final int p3, final int p4);
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public int scrollPosition;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel) {
            super(parcel);
            this.scrollPosition = parcel.readInt();
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollPosition + "}";
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.scrollPosition);
        }
    }
}
