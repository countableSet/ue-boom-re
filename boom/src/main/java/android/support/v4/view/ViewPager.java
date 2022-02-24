// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.view;

import android.support.v4.os.ParcelableCompat;
import android.os.Parcel;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.os.Parcelable$Creator;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.content.res.TypedArray;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Inherited;
import java.lang.annotation.Annotation;
import android.support.annotation.DrawableRes;
import android.database.DataSetObserver;
import android.content.res.Resources$NotFoundException;
import android.support.annotation.CallSuper;
import android.view.View$MeasureSpec;
import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import android.graphics.Canvas;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import android.os.SystemClock;
import android.view.SoundEffectConstants;
import android.view.FocusFinder;
import android.util.Log;
import android.view.ViewGroup$LayoutParams;
import java.util.Collections;
import android.view.MotionEvent;
import android.support.annotation.NonNull;
import android.view.ViewParent;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.Rect;
import java.lang.reflect.Method;
import android.widget.Scroller;
import android.os.Parcelable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.view.animation.Interpolator;
import java.util.Comparator;
import android.view.ViewGroup;

public class ViewPager extends ViewGroup
{
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffectCompat mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffectCompat mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private Method mSetChildrenDrawingOrderEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    
    static {
        LAYOUT_ATTRS = new int[] { 16842931 };
        COMPARATOR = new Comparator<ItemInfo>() {
            @Override
            public int compare(final ItemInfo itemInfo, final ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }
    
    public ViewPager(final Context context) {
        super(context);
        this.mItems = new ArrayList<ItemInfo>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            @Override
            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }
    
    public ViewPager(final Context context, final AttributeSet set) {
        super(context, set);
        this.mItems = new ArrayList<ItemInfo>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            @Override
            public void run() {
                ViewPager.this.setScrollState(0);
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }
    
    private void calculatePageOffsets(ItemInfo itemInfo, int i, ItemInfo itemInfo2) {
        final int count = this.mAdapter.getCount();
        final int clientWidth = this.getClientWidth();
        float n;
        if (clientWidth > 0) {
            n = this.mPageMargin / (float)clientWidth;
        }
        else {
            n = 0.0f;
        }
        if (itemInfo2 != null) {
            int position = itemInfo2.position;
            if (position < itemInfo.position) {
                int n2 = 0;
                float n3 = itemInfo2.offset + itemInfo2.widthFactor + n;
                ++position;
                while (position <= itemInfo.position && n2 < this.mItems.size()) {
                    itemInfo2 = this.mItems.get(n2);
                    float offset;
                    int j;
                    while (true) {
                        offset = n3;
                        j = position;
                        if (position <= itemInfo2.position) {
                            break;
                        }
                        offset = n3;
                        j = position;
                        if (n2 >= this.mItems.size() - 1) {
                            break;
                        }
                        ++n2;
                        itemInfo2 = this.mItems.get(n2);
                    }
                    while (j < itemInfo2.position) {
                        offset += this.mAdapter.getPageWidth(j) + n;
                        ++j;
                    }
                    itemInfo2.offset = offset;
                    n3 = offset + (itemInfo2.widthFactor + n);
                    position = j + 1;
                }
            }
            else if (position > itemInfo.position) {
                int n4 = this.mItems.size() - 1;
                float offset2 = itemInfo2.offset;
                --position;
                while (position >= itemInfo.position && n4 >= 0) {
                    itemInfo2 = this.mItems.get(n4);
                    float n5;
                    int k;
                    while (true) {
                        n5 = offset2;
                        k = position;
                        if (position >= itemInfo2.position) {
                            break;
                        }
                        n5 = offset2;
                        k = position;
                        if (n4 <= 0) {
                            break;
                        }
                        --n4;
                        itemInfo2 = this.mItems.get(n4);
                    }
                    while (k > itemInfo2.position) {
                        n5 -= this.mAdapter.getPageWidth(k) + n;
                        --k;
                    }
                    offset2 = n5 - (itemInfo2.widthFactor + n);
                    itemInfo2.offset = offset2;
                    position = k - 1;
                }
            }
        }
        final int size = this.mItems.size();
        final float offset3 = itemInfo.offset;
        int l = itemInfo.position - 1;
        float offset4;
        if (itemInfo.position == 0) {
            offset4 = itemInfo.offset;
        }
        else {
            offset4 = -3.4028235E38f;
        }
        this.mFirstOffset = offset4;
        float mLastOffset;
        if (itemInfo.position == count - 1) {
            mLastOffset = itemInfo.offset + itemInfo.widthFactor - 1.0f;
        }
        else {
            mLastOffset = Float.MAX_VALUE;
        }
        this.mLastOffset = mLastOffset;
        int index = i - 1;
        float n6 = offset3;
        while (index >= 0) {
            for (itemInfo2 = this.mItems.get(index); l > itemInfo2.position; --l) {
                n6 -= this.mAdapter.getPageWidth(l) + n;
            }
            n6 -= itemInfo2.widthFactor + n;
            itemInfo2.offset = n6;
            if (itemInfo2.position == 0) {
                this.mFirstOffset = n6;
            }
            --index;
            --l;
        }
        float offset5 = itemInfo.offset + itemInfo.widthFactor + n;
        final int n7 = itemInfo.position + 1;
        final int n8 = i + 1;
        i = n7;
        for (int index2 = n8; index2 < size; ++index2, ++i) {
            for (itemInfo = this.mItems.get(index2); i < itemInfo.position; ++i) {
                offset5 += this.mAdapter.getPageWidth(i) + n;
            }
            if (itemInfo.position == count - 1) {
                this.mLastOffset = itemInfo.widthFactor + offset5 - 1.0f;
            }
            itemInfo.offset = offset5;
            offset5 += itemInfo.widthFactor + n;
        }
        this.mNeedCalculatePageOffsets = false;
    }
    
    private void completeScroll(final boolean b) {
        int n = 1;
        boolean b2;
        if (this.mScrollState == 2) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        if ((b2 ? 1 : 0) != 0) {
            this.setScrollingCacheEnabled(false);
            if (this.mScroller.isFinished()) {
                n = 0;
            }
            if (n != 0) {
                this.mScroller.abortAnimation();
                final int scrollX = this.getScrollX();
                final int scrollY = this.getScrollY();
                final int currX = this.mScroller.getCurrX();
                final int currY = this.mScroller.getCurrY();
                if (scrollX != currX || scrollY != currY) {
                    this.scrollTo(currX, currY);
                    if (currX != scrollX) {
                        this.pageScrolled(currX);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        final int n2 = 0;
        int n3 = b2 ? 1 : 0;
        for (int i = n2; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.scrolling) {
                n3 = 1;
                itemInfo.scrolling = false;
            }
        }
        if (n3 != 0) {
            if (b) {
                ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
            }
            else {
                this.mEndScrollRunnable.run();
            }
        }
    }
    
    private int determineTargetPage(int a, final float n, int max, final int a2) {
        if (Math.abs(a2) > this.mFlingDistance && Math.abs(max) > this.mMinimumVelocity) {
            if (max <= 0) {
                ++a;
            }
        }
        else {
            float n2;
            if (a >= this.mCurItem) {
                n2 = 0.4f;
            }
            else {
                n2 = 0.6f;
            }
            a += (int)(n + n2);
        }
        max = a;
        if (this.mItems.size() > 0) {
            max = Math.max(this.mItems.get(0).position, Math.min(a, this.mItems.get(this.mItems.size() - 1).position));
        }
        return max;
    }
    
    private void dispatchOnPageScrolled(final int n, final float n2, final int n3) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(n, n2, n3);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int i = 0; i < this.mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(n, n2, n3);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(n, n2, n3);
        }
    }
    
    private void dispatchOnPageSelected(final int n) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(n);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int i = 0; i < this.mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(n);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(n);
        }
    }
    
    private void dispatchOnScrollStateChanged(final int n) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(n);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int i = 0; i < this.mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(n);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(n);
        }
    }
    
    private void enableLayers(final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            int n;
            if (b) {
                n = 2;
            }
            else {
                n = 0;
            }
            ViewCompat.setLayerType(this.getChildAt(i), n, null);
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private Rect getChildRectInPagerCoordinates(final Rect rect, final View view) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        if (view == null) {
            rect2.set(0, 0, 0, 0);
        }
        else {
            rect2.left = view.getLeft();
            rect2.right = view.getRight();
            rect2.top = view.getTop();
            rect2.bottom = view.getBottom();
            ViewPager viewPager;
            for (ViewParent viewParent = view.getParent(); viewParent instanceof ViewGroup && viewParent != this; viewParent = viewPager.getParent()) {
                viewPager = (ViewPager)viewParent;
                rect2.left += viewPager.getLeft();
                rect2.right += viewPager.getRight();
                rect2.top += viewPager.getTop();
                rect2.bottom += viewPager.getBottom();
            }
        }
        return rect2;
    }
    
    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }
    
    private ItemInfo infoForCurrentScrollPosition() {
        float n = 0.0f;
        final int clientWidth = this.getClientWidth();
        float n2;
        if (clientWidth > 0) {
            n2 = this.getScrollX() / (float)clientWidth;
        }
        else {
            n2 = 0.0f;
        }
        if (clientWidth > 0) {
            n = this.mPageMargin / (float)clientWidth;
        }
        int position = -1;
        float offset = 0.0f;
        float widthFactor = 0.0f;
        int n3 = 1;
        ItemInfo itemInfo = null;
        int index = 0;
        ItemInfo itemInfo2;
        while (true) {
            itemInfo2 = itemInfo;
            if (index >= this.mItems.size()) {
                break;
            }
            final ItemInfo itemInfo3 = this.mItems.get(index);
            int n4 = index;
            ItemInfo mTempItem = itemInfo3;
            if (n3 == 0) {
                n4 = index;
                mTempItem = itemInfo3;
                if (itemInfo3.position != position + 1) {
                    mTempItem = this.mTempItem;
                    mTempItem.offset = offset + widthFactor + n;
                    mTempItem.position = position + 1;
                    mTempItem.widthFactor = this.mAdapter.getPageWidth(mTempItem.position);
                    n4 = index - 1;
                }
            }
            offset = mTempItem.offset;
            final float widthFactor2 = mTempItem.widthFactor;
            if (n3 == 0) {
                itemInfo2 = itemInfo;
                if (n2 < offset) {
                    break;
                }
            }
            if (n2 < widthFactor2 + offset + n || n4 == this.mItems.size() - 1) {
                itemInfo2 = mTempItem;
                break;
            }
            n3 = 0;
            position = mTempItem.position;
            widthFactor = mTempItem.widthFactor;
            index = n4 + 1;
            itemInfo = mTempItem;
        }
        return itemInfo2;
    }
    
    private static boolean isDecorView(@NonNull final View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }
    
    private boolean isGutterDrag(final float n, final float n2) {
        return (n < this.mGutterSize && n2 > 0.0f) || (n > this.getWidth() - this.mGutterSize && n2 < 0.0f);
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mLastMotionX = motionEvent.getX(n);
            this.mActivePointerId = motionEvent.getPointerId(n);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }
    
    private boolean pageScrolled(int n) {
        boolean b = false;
        if (this.mItems.size() == 0) {
            if (!this.mFirstLayout) {
                this.mCalledSuper = false;
                this.onPageScrolled(0, 0.0f, 0);
                if (!this.mCalledSuper) {
                    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
                }
            }
        }
        else {
            final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
            final int clientWidth = this.getClientWidth();
            final int mPageMargin = this.mPageMargin;
            final float n2 = this.mPageMargin / (float)clientWidth;
            final int position = infoForCurrentScrollPosition.position;
            final float n3 = (n / (float)clientWidth - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + n2);
            n = (int)((clientWidth + mPageMargin) * n3);
            this.mCalledSuper = false;
            this.onPageScrolled(position, n3, n);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            b = true;
        }
        return b;
    }
    
    private boolean performDrag(float mLastMotionX) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        final float mLastMotionX2 = this.mLastMotionX;
        this.mLastMotionX = mLastMotionX;
        final float n = this.getScrollX() + (mLastMotionX2 - mLastMotionX);
        final int clientWidth = this.getClientWidth();
        mLastMotionX = clientWidth * this.mFirstOffset;
        float n2 = clientWidth * this.mLastOffset;
        boolean b4 = true;
        boolean b5 = true;
        final ItemInfo itemInfo = this.mItems.get(0);
        final ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
        if (itemInfo.position != 0) {
            b4 = false;
            mLastMotionX = itemInfo.offset * clientWidth;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            b5 = false;
            n2 = itemInfo2.offset * clientWidth;
        }
        if (n < mLastMotionX) {
            if (b4) {
                b3 = this.mLeftEdge.onPull(Math.abs(mLastMotionX - n) / clientWidth);
            }
        }
        else {
            b3 = b;
            mLastMotionX = n;
            if (n > n2) {
                b3 = b2;
                if (b5) {
                    b3 = this.mRightEdge.onPull(Math.abs(n - n2) / clientWidth);
                }
                mLastMotionX = n2;
            }
        }
        this.mLastMotionX += mLastMotionX - (int)mLastMotionX;
        this.scrollTo((int)mLastMotionX, this.getScrollY());
        this.pageScrolled((int)mLastMotionX);
        return b3;
    }
    
    private void recomputeScrollPosition(int n, final int n2, final int n3, final int n4) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
            }
            else {
                this.scrollTo((int)((n - this.getPaddingLeft() - this.getPaddingRight() + n3) * (this.getScrollX() / (float)(n2 - this.getPaddingLeft() - this.getPaddingRight() + n4))), this.getScrollY());
            }
        }
        else {
            final ItemInfo infoForPosition = this.infoForPosition(this.mCurItem);
            float min;
            if (infoForPosition != null) {
                min = Math.min(infoForPosition.offset, this.mLastOffset);
            }
            else {
                min = 0.0f;
            }
            n = (int)((n - this.getPaddingLeft() - this.getPaddingRight()) * min);
            if (n != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(n, this.getScrollY());
            }
        }
    }
    
    private void removeNonDecorViews() {
        int n;
        for (int i = 0; i < this.getChildCount(); i = n + 1) {
            n = i;
            if (!((LayoutParams)this.getChildAt(i).getLayoutParams()).isDecor) {
                this.removeViewAt(i);
                n = i - 1;
            }
        }
    }
    
    private void requestParentDisallowInterceptTouchEvent(final boolean b) {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        return this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
    }
    
    private void scrollToItem(final int n, final boolean b, final int n2, final boolean b2) {
        final ItemInfo infoForPosition = this.infoForPosition(n);
        int n3 = 0;
        if (infoForPosition != null) {
            n3 = (int)(this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)));
        }
        if (b) {
            this.smoothScrollTo(n3, 0, n2);
            if (b2) {
                this.dispatchOnPageSelected(n);
            }
        }
        else {
            if (b2) {
                this.dispatchOnPageSelected(n);
            }
            this.completeScroll(false);
            this.scrollTo(n3, 0);
            this.pageScrolled(n3);
        }
    }
    
    private void setScrollingCacheEnabled(final boolean mScrollingCacheEnabled) {
        if (this.mScrollingCacheEnabled != mScrollingCacheEnabled) {
            this.mScrollingCacheEnabled = mScrollingCacheEnabled;
        }
    }
    
    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList<View>();
            }
            else {
                this.mDrawingOrderedChildren.clear();
            }
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                this.mDrawingOrderedChildren.add(this.getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, ViewPager.sPositionComparator);
        }
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        final int size = list.size();
        final int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    final ItemInfo infoForChild = this.infoForChild(child);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        child.addFocusables((ArrayList)list, n, n2);
                    }
                }
            }
        }
        if ((descendantFocusability != 262144 || size == list.size()) && this.isFocusable() && ((n2 & 0x1) != 0x1 || !this.isInTouchMode() || this.isFocusableInTouchMode()) && list != null) {
            list.add((View)this);
        }
    }
    
    ItemInfo addNewItem(final int position, final int index) {
        final ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = position;
        itemInfo.object = this.mAdapter.instantiateItem(this, position);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(position);
        if (index < 0 || index >= this.mItems.size()) {
            this.mItems.add(itemInfo);
        }
        else {
            this.mItems.add(index, itemInfo);
        }
        return itemInfo;
    }
    
    public void addOnAdapterChangeListener(@NonNull final OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>();
        }
        this.mAdapterChangeListeners.add(onAdapterChangeListener);
    }
    
    public void addOnPageChangeListener(final OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>();
        }
        this.mOnPageChangeListeners.add(onPageChangeListener);
    }
    
    public void addTouchables(final ArrayList<View> list) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    child.addTouchables((ArrayList)list);
                }
            }
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        ViewGroup$LayoutParams generateLayoutParams = viewGroup$LayoutParams;
        if (!this.checkLayoutParams(viewGroup$LayoutParams)) {
            generateLayoutParams = this.generateLayoutParams(viewGroup$LayoutParams);
        }
        final LayoutParams layoutParams = (LayoutParams)generateLayoutParams;
        layoutParams.isDecor |= isDecorView(view);
        if (this.mInLayout) {
            if (layoutParams != null && layoutParams.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            layoutParams.needsMeasure = true;
            this.addViewInLayout(view, n, generateLayoutParams);
        }
        else {
            super.addView(view, n, generateLayoutParams);
        }
    }
    
    public boolean arrowScroll(final int n) {
        final View focus = this.findFocus();
        View view;
        if (focus == this) {
            view = null;
        }
        else if ((view = focus) != null) {
            final int n2 = 0;
            ViewParent viewParent = focus.getParent();
            int n3;
            while (true) {
                n3 = n2;
                if (!(viewParent instanceof ViewGroup)) {
                    break;
                }
                if (viewParent == this) {
                    n3 = 1;
                    break;
                }
                viewParent = viewParent.getParent();
            }
            view = focus;
            if (n3 == 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(focus.getClass().getSimpleName());
                for (ViewParent viewParent2 = focus.getParent(); viewParent2 instanceof ViewGroup; viewParent2 = viewParent2.getParent()) {
                    sb.append(" => ").append(viewParent2.getClass().getSimpleName());
                }
                Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + sb.toString());
                view = null;
            }
        }
        boolean b = false;
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view, n);
        if (nextFocus != null && nextFocus != view) {
            if (n == 17) {
                final int left = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                final int left2 = this.getChildRectInPagerCoordinates(this.mTempRect, view).left;
                if (view != null && left >= left2) {
                    b = this.pageLeft();
                }
                else {
                    b = nextFocus.requestFocus();
                }
            }
            else if (n == 66) {
                final int left3 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                final int left4 = this.getChildRectInPagerCoordinates(this.mTempRect, view).left;
                if (view != null && left3 <= left4) {
                    b = this.pageRight();
                }
                else {
                    b = nextFocus.requestFocus();
                }
            }
        }
        else if (n == 17 || n == 1) {
            b = this.pageLeft();
        }
        else if (n == 66 || n == 2) {
            b = this.pageRight();
        }
        if (b) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(n));
        }
        return b;
    }
    
    public boolean beginFakeDrag() {
        boolean b = false;
        if (!this.mIsBeingDragged) {
            this.mFakeDragging = true;
            this.setScrollState(1);
            this.mLastMotionX = 0.0f;
            this.mInitialMotionX = 0.0f;
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            else {
                this.mVelocityTracker.clear();
            }
            final long uptimeMillis = SystemClock.uptimeMillis();
            final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
            this.mFakeDragBeginTime = uptimeMillis;
            b = true;
        }
        return b;
    }
    
    protected boolean canScroll(final View view, final boolean b, final int n, final int n2, final int n3) {
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
            return b && ViewCompat.canScrollHorizontally(view, -n);
        }
        return b && ViewCompat.canScrollHorizontally(view, -n);
        return b && ViewCompat.canScrollHorizontally(view, -n);
    }
    
    public boolean canScrollHorizontally(final int n) {
        final boolean b = true;
        boolean b2 = true;
        final boolean b3 = false;
        if (this.mAdapter == null) {
            b2 = b3;
        }
        else {
            final int clientWidth = this.getClientWidth();
            final int scrollX = this.getScrollX();
            if (n < 0) {
                if (scrollX <= (int)(clientWidth * this.mFirstOffset)) {
                    b2 = false;
                }
            }
            else {
                b2 = b3;
                if (n > 0) {
                    b2 = (scrollX < (int)(clientWidth * this.mLastOffset) && b);
                }
            }
        }
        return b2;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public void clearOnPageChangeListeners() {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.clear();
        }
    }
    
    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                this.scrollTo(currX, currY);
                if (!this.pageScrolled(currX)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, currY);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
        else {
            this.completeScroll(true);
        }
    }
    
    void dataSetChanged() {
        final int count = this.mAdapter.getCount();
        this.mExpectedAdapterCount = count;
        boolean b = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < count;
        int mCurItem = this.mCurItem;
        int n = 0;
        int max;
        int n2;
        int n3;
        for (int i = 0; i < this.mItems.size(); i = n3 + 1, n = n2, mCurItem = max) {
            final ItemInfo itemInfo = this.mItems.get(i);
            final int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition == -1) {
                max = mCurItem;
                n2 = n;
                n3 = i;
            }
            else if (itemPosition == -2) {
                this.mItems.remove(i);
                final int n4 = i - 1;
                int n5;
                if ((n5 = n) == 0) {
                    this.mAdapter.startUpdate(this);
                    n5 = 1;
                }
                this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
                b = true;
                n3 = n4;
                n2 = n5;
                max = mCurItem;
                if (this.mCurItem == itemInfo.position) {
                    max = Math.max(0, Math.min(this.mCurItem, count - 1));
                    b = true;
                    n3 = n4;
                    n2 = n5;
                }
            }
            else {
                n3 = i;
                n2 = n;
                max = mCurItem;
                if (itemInfo.position != itemPosition) {
                    if (itemInfo.position == this.mCurItem) {
                        mCurItem = itemPosition;
                    }
                    itemInfo.position = itemPosition;
                    b = true;
                    n3 = i;
                    n2 = n;
                    max = mCurItem;
                }
            }
        }
        if (n != 0) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, ViewPager.COMPARATOR);
        if (b) {
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
                final LayoutParams layoutParams = (LayoutParams)this.getChildAt(j).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            this.setCurrentItemInternal(mCurItem, false, true);
            this.requestLayout();
        }
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        boolean dispatchPopulateAccessibilityEvent;
        if (accessibilityEvent.getEventType() == 4096) {
            dispatchPopulateAccessibilityEvent = super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        else {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    final ItemInfo infoForChild = this.infoForChild(child);
                    if (infoForChild != null && infoForChild.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                        dispatchPopulateAccessibilityEvent = true;
                        return dispatchPopulateAccessibilityEvent;
                    }
                }
            }
            dispatchPopulateAccessibilityEvent = false;
        }
        return dispatchPopulateAccessibilityEvent;
    }
    
    float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((float)((n - 0.5f) * 0.4712389167638204));
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        int n = 0;
        int n2 = 0;
        final int overScrollMode = this.getOverScrollMode();
        if (overScrollMode == 0 || (overScrollMode == 1 && this.mAdapter != null && this.mAdapter.getCount() > 1)) {
            if (!this.mLeftEdge.isFinished()) {
                final int save = canvas.save();
                final int n3 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                final int width = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(-n3 + this.getPaddingTop()), this.mFirstOffset * width);
                this.mLeftEdge.setSize(n3, width);
                n2 = ((false | this.mLeftEdge.draw(canvas)) ? 1 : 0);
                canvas.restoreToCount(save);
            }
            n = n2;
            if (!this.mRightEdge.isFinished()) {
                final int save2 = canvas.save();
                final int width2 = this.getWidth();
                final int height = this.getHeight();
                final int paddingTop = this.getPaddingTop();
                final int paddingBottom = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0f) * width2);
                this.mRightEdge.setSize(height - paddingTop - paddingBottom, width2);
                n = (n2 | (this.mRightEdge.draw(canvas) ? 1 : 0));
                canvas.restoreToCount(save2);
            }
        }
        else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (n != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable mMarginDrawable = this.mMarginDrawable;
        if (mMarginDrawable != null && mMarginDrawable.isStateful()) {
            mMarginDrawable.setState(this.getDrawableState());
        }
    }
    
    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        if (this.mAdapter != null) {
            final VelocityTracker mVelocityTracker = this.mVelocityTracker;
            mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            final int n = (int)VelocityTrackerCompat.getXVelocity(mVelocityTracker, this.mActivePointerId);
            this.mPopulatePending = true;
            final int clientWidth = this.getClientWidth();
            final int scrollX = this.getScrollX();
            final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
            this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / (float)clientWidth - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, n, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, n);
        }
        this.endDrag();
        this.mFakeDragging = false;
    }
    
    public boolean executeKeyEvent(final KeyEvent keyEvent) {
        boolean b2;
        final boolean b = b2 = false;
        if (keyEvent.getAction() == 0) {
            switch (keyEvent.getKeyCode()) {
                default: {
                    b2 = b;
                    break;
                }
                case 21: {
                    b2 = this.arrowScroll(17);
                    break;
                }
                case 22: {
                    b2 = this.arrowScroll(66);
                    break;
                }
                case 61: {
                    b2 = b;
                    if (Build$VERSION.SDK_INT < 11) {
                        break;
                    }
                    if (KeyEventCompat.hasNoModifiers(keyEvent)) {
                        b2 = this.arrowScroll(2);
                        break;
                    }
                    b2 = b;
                    if (KeyEventCompat.hasModifiers(keyEvent, 1)) {
                        b2 = this.arrowScroll(1);
                        break;
                    }
                    break;
                }
            }
        }
        return b2;
    }
    
    public void fakeDragBy(float n) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        if (this.mAdapter != null) {
            this.mLastMotionX += n;
            final float n2 = this.getScrollX() - n;
            final int clientWidth = this.getClientWidth();
            n = clientWidth * this.mFirstOffset;
            float n3 = clientWidth * this.mLastOffset;
            final ItemInfo itemInfo = this.mItems.get(0);
            final ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
            if (itemInfo.position != 0) {
                n = itemInfo.offset * clientWidth;
            }
            if (itemInfo2.position != this.mAdapter.getCount() - 1) {
                n3 = itemInfo2.offset * clientWidth;
            }
            if (n2 >= n) {
                n = n2;
                if (n2 > n3) {
                    n = n3;
                }
            }
            this.mLastMotionX += n - (int)n;
            this.scrollTo((int)n, this.getScrollY());
            this.pageScrolled((int)n);
            final MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
        }
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return this.generateDefaultLayoutParams();
    }
    
    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }
    
    protected int getChildDrawingOrder(int index, final int n) {
        if (this.mDrawingOrder == 2) {
            index = index - 1 - n;
        }
        else {
            index = n;
        }
        return ((LayoutParams)this.mDrawingOrderedChildren.get(index).getLayoutParams()).childIndex;
    }
    
    public int getCurrentItem() {
        return this.mCurItem;
    }
    
    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }
    
    public int getPageMargin() {
        return this.mPageMargin;
    }
    
    ItemInfo infoForAnyChild(View view) {
        ItemInfo infoForChild;
        while (true) {
            final ViewParent parent = view.getParent();
            if (parent == this) {
                infoForChild = this.infoForChild(view);
                break;
            }
            if (parent == null || !(parent instanceof View)) {
                infoForChild = null;
                break;
            }
            view = (View)parent;
        }
        return infoForChild;
    }
    
    ItemInfo infoForChild(final View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }
    
    ItemInfo infoForPosition(final int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.position == n) {
                return itemInfo;
            }
        }
        return null;
    }
    
    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        final Context context = this.getContext();
        this.mScroller = new Scroller(context, ViewPager.sInterpolator);
        final ViewConfiguration value = ViewConfiguration.get(context);
        final float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = value.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int)(400.0f * density);
        this.mMaximumVelocity = value.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffectCompat(context);
        this.mRightEdge = new EdgeEffectCompat(context);
        this.mFlingDistance = (int)(25.0f * density);
        this.mCloseEnough = (int)(2.0f * density);
        this.mDefaultGutterSize = (int)(16.0f * density);
        ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener() {
            private final Rect mTempRect = new Rect();
            
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                WindowInsetsCompat windowInsetsCompat2 = ViewCompat.onApplyWindowInsets(view, windowInsetsCompat);
                if (!windowInsetsCompat2.isConsumed()) {
                    final Rect mTempRect = this.mTempRect;
                    mTempRect.left = windowInsetsCompat2.getSystemWindowInsetLeft();
                    mTempRect.top = windowInsetsCompat2.getSystemWindowInsetTop();
                    mTempRect.right = windowInsetsCompat2.getSystemWindowInsetRight();
                    mTempRect.bottom = windowInsetsCompat2.getSystemWindowInsetBottom();
                    for (int i = 0; i < ViewPager.this.getChildCount(); ++i) {
                        final WindowInsetsCompat dispatchApplyWindowInsets = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), windowInsetsCompat2);
                        mTempRect.left = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetLeft(), mTempRect.left);
                        mTempRect.top = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetTop(), mTempRect.top);
                        mTempRect.right = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetRight(), mTempRect.right);
                        mTempRect.bottom = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetBottom(), mTempRect.bottom);
                    }
                    windowInsetsCompat2 = windowInsetsCompat2.replaceSystemWindowInsets(mTempRect.left, mTempRect.top, mTempRect.right, mTempRect.bottom);
                }
                return windowInsetsCompat2;
            }
        });
    }
    
    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }
    
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        if (this.mScroller != null && !this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            final int scrollX = this.getScrollX();
            final int width = this.getWidth();
            final float n = this.mPageMargin / (float)width;
            int index = 0;
            ItemInfo itemInfo = this.mItems.get(0);
            float offset = itemInfo.offset;
            for (int size = this.mItems.size(), i = itemInfo.position; i < this.mItems.get(size - 1).position; ++i) {
                while (i > itemInfo.position && index < size) {
                    final ArrayList<ItemInfo> mItems = this.mItems;
                    ++index;
                    itemInfo = mItems.get(index);
                }
                float a;
                if (i == itemInfo.position) {
                    a = (itemInfo.offset + itemInfo.widthFactor) * width;
                    offset = itemInfo.offset + itemInfo.widthFactor + n;
                }
                else {
                    final float pageWidth = this.mAdapter.getPageWidth(i);
                    a = (offset + pageWidth) * width;
                    offset += pageWidth + n;
                }
                if (this.mPageMargin + a > scrollX) {
                    this.mMarginDrawable.setBounds(Math.round(a), this.mTopPageBounds, Math.round(this.mPageMargin + a), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (a > scrollX + width) {
                    break;
                }
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int n = motionEvent.getAction() & 0xFF;
        boolean mIsBeingDragged;
        if (n == 3 || n == 1) {
            this.resetTouch();
            mIsBeingDragged = false;
        }
        else {
            if (n != 0) {
                if (this.mIsBeingDragged) {
                    mIsBeingDragged = true;
                    return mIsBeingDragged;
                }
                if (this.mIsUnableToDrag) {
                    mIsBeingDragged = false;
                    return mIsBeingDragged;
                }
            }
            switch (n) {
                case 2: {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId == -1) {
                        break;
                    }
                    final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                    final float x = motionEvent.getX(pointerIndex);
                    final float a = x - this.mLastMotionX;
                    final float abs = Math.abs(a);
                    final float y = motionEvent.getY(pointerIndex);
                    final float abs2 = Math.abs(y - this.mInitialMotionY);
                    if (a != 0.0f && !this.isGutterDrag(this.mLastMotionX, a) && this.canScroll((View)this, false, (int)a, (int)x, (int)y)) {
                        this.mLastMotionX = x;
                        this.mLastMotionY = y;
                        this.mIsUnableToDrag = true;
                        mIsBeingDragged = false;
                        return mIsBeingDragged;
                    }
                    if (abs > this.mTouchSlop && 0.5f * abs > abs2) {
                        this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                        this.setScrollState(1);
                        float mLastMotionX;
                        if (a > 0.0f) {
                            mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                        }
                        else {
                            mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                        }
                        this.mLastMotionX = mLastMotionX;
                        this.mLastMotionY = y;
                        this.setScrollingCacheEnabled(true);
                    }
                    else if (abs2 > this.mTouchSlop) {
                        this.mIsUnableToDrag = true;
                    }
                    if (this.mIsBeingDragged && this.performDrag(x)) {
                        ViewCompat.postInvalidateOnAnimation((View)this);
                        break;
                    }
                    break;
                }
                case 0: {
                    final float x2 = motionEvent.getX();
                    this.mInitialMotionX = x2;
                    this.mLastMotionX = x2;
                    final float y2 = motionEvent.getY();
                    this.mInitialMotionY = y2;
                    this.mLastMotionY = y2;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mIsUnableToDrag = false;
                    this.mIsScrollStarted = true;
                    this.mScroller.computeScrollOffset();
                    if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                        this.setScrollState(1);
                        break;
                    }
                    this.completeScroll(false);
                    this.mIsBeingDragged = false;
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    break;
                }
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            mIsBeingDragged = this.mIsBeingDragged;
        }
        return mIsBeingDragged;
    }
    
    protected void onLayout(final boolean b, int paddingTop, int b2, int i, int paddingBottom) {
        final int childCount = this.getChildCount();
        final int n = i - paddingTop;
        final int n2 = paddingBottom - b2;
        b2 = this.getPaddingLeft();
        paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        paddingBottom = this.getPaddingBottom();
        final int scrollX = this.getScrollX();
        int mDecorChildCount = 0;
        int n3;
        int n4;
        int n5;
        int n6;
        for (int j = 0; j < childCount; ++j, mDecorChildCount = n3, paddingBottom = n4, b2 = n5, paddingRight = n6, paddingTop = i) {
            final View child = this.getChildAt(j);
            n3 = mDecorChildCount;
            n4 = paddingBottom;
            n5 = b2;
            n6 = paddingRight;
            i = paddingTop;
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                n3 = mDecorChildCount;
                n4 = paddingBottom;
                n5 = b2;
                n6 = paddingRight;
                i = paddingTop;
                if (layoutParams.isDecor) {
                    i = layoutParams.gravity;
                    final int gravity = layoutParams.gravity;
                    switch (i & 0x7) {
                        default: {
                            i = b2;
                            n5 = b2;
                            break;
                        }
                        case 3: {
                            i = b2;
                            n5 = b2 + child.getMeasuredWidth();
                            break;
                        }
                        case 1: {
                            i = Math.max((n - child.getMeasuredWidth()) / 2, b2);
                            n5 = b2;
                            break;
                        }
                        case 5: {
                            i = n - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            n5 = b2;
                            break;
                        }
                    }
                    switch (gravity & 0x70) {
                        default: {
                            b2 = paddingTop;
                            break;
                        }
                        case 48: {
                            b2 = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        }
                        case 16: {
                            b2 = Math.max((n2 - child.getMeasuredHeight()) / 2, paddingTop);
                            break;
                        }
                        case 80: {
                            b2 = n2 - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                        }
                    }
                    i += scrollX;
                    child.layout(i, b2, child.getMeasuredWidth() + i, child.getMeasuredHeight() + b2);
                    n3 = mDecorChildCount + 1;
                    i = paddingTop;
                    n6 = paddingRight;
                    n4 = paddingBottom;
                }
            }
        }
        final int n7 = n - b2 - paddingRight;
        View child2;
        LayoutParams layoutParams2;
        ItemInfo infoForChild;
        int n8;
        for (i = 0; i < childCount; ++i) {
            child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    infoForChild = this.infoForChild(child2);
                    if (infoForChild != null) {
                        n8 = b2 + (int)(n7 * infoForChild.offset);
                        if (layoutParams2.needsMeasure) {
                            layoutParams2.needsMeasure = false;
                            child2.measure(View$MeasureSpec.makeMeasureSpec((int)(n7 * layoutParams2.widthFactor), 1073741824), View$MeasureSpec.makeMeasureSpec(n2 - paddingTop - paddingBottom, 1073741824));
                        }
                        child2.layout(n8, paddingTop, child2.getMeasuredWidth() + n8, child2.getMeasuredHeight() + paddingTop);
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = n2 - paddingBottom;
        this.mDecorChildCount = mDecorChildCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(int measuredWidth, int i) {
        this.setMeasuredDimension(getDefaultSize(0, measuredWidth), getDefaultSize(0, i));
        measuredWidth = this.getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        measuredWidth = measuredWidth - this.getPaddingLeft() - this.getPaddingRight();
        i = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int n;
        int n2;
        for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j, i = n, measuredWidth = n2) {
            final View child = this.getChildAt(j);
            n = i;
            n2 = measuredWidth;
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                n = i;
                n2 = measuredWidth;
                if (layoutParams != null) {
                    n = i;
                    n2 = measuredWidth;
                    if (layoutParams.isDecor) {
                        final int n3 = layoutParams.gravity & 0x7;
                        final int n4 = layoutParams.gravity & 0x70;
                        final int n5 = Integer.MIN_VALUE;
                        int n6 = Integer.MIN_VALUE;
                        boolean b;
                        if (n4 == 48 || n4 == 80) {
                            b = true;
                        }
                        else {
                            b = false;
                        }
                        final boolean b2 = n3 == 3 || n3 == 5;
                        int n7;
                        if (b) {
                            n7 = 1073741824;
                        }
                        else {
                            n7 = n5;
                            if (b2) {
                                n6 = 1073741824;
                                n7 = n5;
                            }
                        }
                        final int n8 = measuredWidth;
                        final int n9 = i;
                        int width = n8;
                        if (layoutParams.width != -2) {
                            n7 = 1073741824;
                            width = n8;
                            if (layoutParams.width != -1) {
                                width = layoutParams.width;
                                n7 = n7;
                            }
                        }
                        int n10 = n6;
                        int height = n9;
                        if (layoutParams.height != -2) {
                            n10 = 1073741824;
                            height = n9;
                            if (layoutParams.height != -1) {
                                height = layoutParams.height;
                                n10 = n10;
                            }
                        }
                        child.measure(View$MeasureSpec.makeMeasureSpec(width, n7), View$MeasureSpec.makeMeasureSpec(height, n10));
                        if (b) {
                            n = i - child.getMeasuredHeight();
                            n2 = measuredWidth;
                        }
                        else {
                            n = i;
                            n2 = measuredWidth;
                            if (b2) {
                                n2 = measuredWidth - child.getMeasuredWidth();
                                n = i;
                            }
                        }
                    }
                }
            }
        }
        this.mChildWidthMeasureSpec = View$MeasureSpec.makeMeasureSpec(measuredWidth, 1073741824);
        this.mChildHeightMeasureSpec = View$MeasureSpec.makeMeasureSpec(i, 1073741824);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        int childCount2;
        View child2;
        LayoutParams layoutParams2;
        for (childCount2 = this.getChildCount(), i = 0; i < childCount2; ++i) {
            child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (layoutParams2 == null || !layoutParams2.isDecor) {
                    child2.measure(View$MeasureSpec.makeMeasureSpec((int)(measuredWidth * layoutParams2.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
        }
    }
    
    @CallSuper
    protected void onPageScrolled(int i, float n, int scrollX) {
        if (this.mDecorChildCount > 0) {
            final int scrollX2 = this.getScrollX();
            int paddingLeft = this.getPaddingLeft();
            int paddingRight = this.getPaddingRight();
            final int width = this.getWidth();
            int n2;
            int n3;
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j, paddingLeft = n3, paddingRight = n2) {
                final View child = this.getChildAt(j);
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (!layoutParams.isDecor) {
                    n2 = paddingRight;
                    n3 = paddingLeft;
                }
                else {
                    int max = 0;
                    switch (layoutParams.gravity & 0x7) {
                        default: {
                            max = paddingLeft;
                            break;
                        }
                        case 3: {
                            max = paddingLeft;
                            paddingLeft += child.getWidth();
                            break;
                        }
                        case 1: {
                            max = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        }
                        case 5: {
                            max = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                        }
                    }
                    final int n4 = max + scrollX2 - child.getLeft();
                    n3 = paddingLeft;
                    n2 = paddingRight;
                    if (n4 != 0) {
                        child.offsetLeftAndRight(n4);
                        n3 = paddingLeft;
                        n2 = paddingRight;
                    }
                }
            }
        }
        this.dispatchOnPageScrolled(i, n, scrollX);
        if (this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            int childCount2;
            View child2;
            for (childCount2 = this.getChildCount(), i = 0; i < childCount2; ++i) {
                child2 = this.getChildAt(i);
                if (!((LayoutParams)child2.getLayoutParams()).isDecor) {
                    n = (child2.getLeft() - scrollX) / (float)this.getClientWidth();
                    this.mPageTransformer.transformPage(child2, n);
                }
            }
        }
        this.mCalledSuper = true;
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        int childCount = this.getChildCount();
        int i;
        int n2;
        if ((n & 0x2) != 0x0) {
            i = 0;
            n2 = 1;
        }
        else {
            i = childCount - 1;
            n2 = -1;
            childCount = -1;
        }
        while (i != childCount) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem && child.requestFocus(n, rect)) {
                    return true;
                }
            }
            i += n2;
        }
        return false;
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final SavedState savedState = (SavedState)parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(savedState.adapterState, savedState.loader);
                this.setCurrentItemInternal(savedState.position, false, true);
            }
            else {
                this.mRestoredCurItem = savedState.position;
                this.mRestoredAdapterState = savedState.adapterState;
                this.mRestoredClassLoader = savedState.loader;
            }
        }
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        if (this.mAdapter != null) {
            savedState.adapterState = this.mAdapter.saveState();
        }
        return (Parcelable)savedState;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.recomputeScrollPosition(n, n3, this.mPageMargin, this.mPageMargin);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        boolean b;
        if (this.mFakeDragging) {
            b = true;
        }
        else if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            b = false;
        }
        else if (this.mAdapter == null || this.mAdapter.getCount() == 0) {
            b = false;
        }
        else {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            final int action = motionEvent.getAction();
            int n2;
            final int n = n2 = 0;
            while (true) {
                switch (action & 0xFF) {
                    default: {
                        n2 = n;
                        break Label_0135;
                    }
                    case 6: {
                        this.onSecondaryPointerUp(motionEvent);
                        this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                        n2 = n;
                        break Label_0135;
                    }
                    case 5: {
                        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                        this.mLastMotionX = motionEvent.getX(actionIndex);
                        this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                        n2 = n;
                        break Label_0135;
                    }
                    case 0: {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        this.populate();
                        final float x = motionEvent.getX();
                        this.mInitialMotionX = x;
                        this.mLastMotionX = x;
                        final float y = motionEvent.getY();
                        this.mInitialMotionY = y;
                        this.mLastMotionY = y;
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        n2 = n;
                    }
                    case 4: {
                        if (n2 != 0) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                        }
                        b = true;
                        break;
                    }
                    case 2: {
                        if (!this.mIsBeingDragged) {
                            final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                            if (pointerIndex == -1) {
                                n2 = (this.resetTouch() ? 1 : 0);
                                continue;
                            }
                            final float x2 = motionEvent.getX(pointerIndex);
                            final float abs = Math.abs(x2 - this.mLastMotionX);
                            final float y2 = motionEvent.getY(pointerIndex);
                            final float abs2 = Math.abs(y2 - this.mLastMotionY);
                            if (abs > this.mTouchSlop && abs > abs2) {
                                this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                                float mLastMotionX;
                                if (x2 - this.mInitialMotionX > 0.0f) {
                                    mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                                }
                                else {
                                    mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                                }
                                this.mLastMotionX = mLastMotionX;
                                this.mLastMotionY = y2;
                                this.setScrollState(1);
                                this.setScrollingCacheEnabled(true);
                                final ViewParent parent = this.getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                        n2 = n;
                        if (this.mIsBeingDragged) {
                            n2 = ((false | this.performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)))) ? 1 : 0);
                        }
                        continue;
                    }
                    case 1: {
                        n2 = n;
                        if (this.mIsBeingDragged) {
                            final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                            mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                            final int n3 = (int)VelocityTrackerCompat.getXVelocity(mVelocityTracker, this.mActivePointerId);
                            this.mPopulatePending = true;
                            final int clientWidth = this.getClientWidth();
                            final int scrollX = this.getScrollX();
                            final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
                            this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / (float)clientWidth - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + this.mPageMargin / (float)clientWidth), n3, (int)(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, n3);
                            n2 = (this.resetTouch() ? 1 : 0);
                        }
                        continue;
                    }
                    case 3: {
                        n2 = n;
                        if (this.mIsBeingDragged) {
                            this.scrollToItem(this.mCurItem, true, 0, false);
                            n2 = (this.resetTouch() ? 1 : 0);
                        }
                        continue;
                    }
                }
                break;
            }
        }
        return b;
    }
    
    boolean pageLeft() {
        boolean b = true;
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
        }
        else {
            b = false;
        }
        return b;
    }
    
    boolean pageRight() {
        boolean b = true;
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
        }
        else {
            b = false;
        }
        return b;
    }
    
    void populate() {
        this.populate(this.mCurItem);
    }
    
    void populate(int i) {
        ItemInfo infoForPosition = null;
        if (this.mCurItem != i) {
            infoForPosition = this.infoForPosition(this.mCurItem);
            this.mCurItem = i;
        }
        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
        }
        else if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
        }
        else if (this.getWindowToken() != null) {
            this.mAdapter.startUpdate(this);
            i = this.mOffscreenPageLimit;
            final int max = Math.max(0, this.mCurItem - i);
            final int count = this.mAdapter.getCount();
            final int min = Math.min(count - 1, this.mCurItem + i);
            if (count != this.mExpectedAdapterCount) {
                try {
                    final String s = this.getResources().getResourceName(this.getId());
                    throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + count + " Pager id: " + s + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
                }
                catch (Resources$NotFoundException ex) {
                    final String s = Integer.toHexString(this.getId());
                    throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + count + " Pager id: " + s + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
                }
            }
            final ItemInfo itemInfo = null;
            i = 0;
            ItemInfo itemInfo2;
            while (true) {
                itemInfo2 = itemInfo;
                if (i >= this.mItems.size()) {
                    break;
                }
                final ItemInfo itemInfo3 = this.mItems.get(i);
                if (itemInfo3.position >= this.mCurItem) {
                    itemInfo2 = itemInfo;
                    if (itemInfo3.position == this.mCurItem) {
                        itemInfo2 = itemInfo3;
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
            ItemInfo addNewItem;
            if ((addNewItem = itemInfo2) == null) {
                addNewItem = itemInfo2;
                if (count > 0) {
                    addNewItem = this.addNewItem(this.mCurItem, i);
                }
            }
            if (addNewItem != null) {
                float n = 0.0f;
                int index = i - 1;
                ItemInfo itemInfo4;
                if (index >= 0) {
                    itemInfo4 = this.mItems.get(index);
                }
                else {
                    itemInfo4 = null;
                }
                final int clientWidth = this.getClientWidth();
                float n2;
                if (clientWidth <= 0) {
                    n2 = 0.0f;
                }
                else {
                    n2 = 2.0f - addNewItem.widthFactor + this.getPaddingLeft() / (float)clientWidth;
                }
                int j = this.mCurItem - 1;
                ItemInfo itemInfo5 = itemInfo4;
                int n3 = i;
                while (j >= 0) {
                    float n4;
                    ItemInfo itemInfo6;
                    int n5;
                    if (n >= n2 && j < max) {
                        if (itemInfo5 == null) {
                            break;
                        }
                        i = n3;
                        n4 = n;
                        itemInfo6 = itemInfo5;
                        n5 = index;
                        if (j == itemInfo5.position) {
                            i = n3;
                            n4 = n;
                            itemInfo6 = itemInfo5;
                            n5 = index;
                            if (!itemInfo5.scrolling) {
                                this.mItems.remove(index);
                                this.mAdapter.destroyItem(this, j, itemInfo5.object);
                                n5 = index - 1;
                                i = n3 - 1;
                                if (n5 >= 0) {
                                    itemInfo6 = this.mItems.get(n5);
                                    n4 = n;
                                }
                                else {
                                    itemInfo6 = null;
                                    n4 = n;
                                }
                            }
                        }
                    }
                    else if (itemInfo5 != null && j == itemInfo5.position) {
                        n4 = n + itemInfo5.widthFactor;
                        n5 = index - 1;
                        if (n5 >= 0) {
                            itemInfo6 = this.mItems.get(n5);
                        }
                        else {
                            itemInfo6 = null;
                        }
                        i = n3;
                    }
                    else {
                        n4 = n + this.addNewItem(j, index + 1).widthFactor;
                        i = n3 + 1;
                        if (index >= 0) {
                            itemInfo6 = this.mItems.get(index);
                        }
                        else {
                            itemInfo6 = null;
                        }
                        n5 = index;
                    }
                    --j;
                    n3 = i;
                    n = n4;
                    itemInfo5 = itemInfo6;
                    index = n5;
                }
                float widthFactor = addNewItem.widthFactor;
                int index2 = n3 + 1;
                if (widthFactor < 2.0f) {
                    ItemInfo itemInfo7;
                    if (index2 < this.mItems.size()) {
                        itemInfo7 = this.mItems.get(index2);
                    }
                    else {
                        itemInfo7 = null;
                    }
                    float n6;
                    if (clientWidth <= 0) {
                        n6 = 0.0f;
                    }
                    else {
                        n6 = this.getPaddingRight() / (float)clientWidth + 2.0f;
                    }
                    int k = this.mCurItem + 1;
                    ItemInfo itemInfo8 = itemInfo7;
                    while (k < count) {
                        float n7;
                        ItemInfo itemInfo9;
                        if (widthFactor >= n6 && k > min) {
                            if (itemInfo8 == null) {
                                break;
                            }
                            n7 = widthFactor;
                            itemInfo9 = itemInfo8;
                            i = index2;
                            if (k == itemInfo8.position) {
                                n7 = widthFactor;
                                itemInfo9 = itemInfo8;
                                i = index2;
                                if (!itemInfo8.scrolling) {
                                    this.mItems.remove(index2);
                                    this.mAdapter.destroyItem(this, k, itemInfo8.object);
                                    if (index2 < this.mItems.size()) {
                                        itemInfo9 = this.mItems.get(index2);
                                        i = index2;
                                        n7 = widthFactor;
                                    }
                                    else {
                                        itemInfo9 = null;
                                        n7 = widthFactor;
                                        i = index2;
                                    }
                                }
                            }
                        }
                        else if (itemInfo8 != null && k == itemInfo8.position) {
                            n7 = widthFactor + itemInfo8.widthFactor;
                            i = index2 + 1;
                            if (i < this.mItems.size()) {
                                itemInfo9 = this.mItems.get(i);
                            }
                            else {
                                itemInfo9 = null;
                            }
                        }
                        else {
                            final ItemInfo addNewItem2 = this.addNewItem(k, index2);
                            i = index2 + 1;
                            n7 = widthFactor + addNewItem2.widthFactor;
                            if (i < this.mItems.size()) {
                                itemInfo9 = this.mItems.get(i);
                            }
                            else {
                                itemInfo9 = null;
                            }
                        }
                        ++k;
                        widthFactor = n7;
                        itemInfo8 = itemInfo9;
                        index2 = i;
                    }
                }
                this.calculatePageOffsets(addNewItem, n3, infoForPosition);
            }
            final PagerAdapter mAdapter = this.mAdapter;
            i = this.mCurItem;
            Object object;
            if (addNewItem != null) {
                object = addNewItem.object;
            }
            else {
                object = null;
            }
            mAdapter.setPrimaryItem(this, i, object);
            this.mAdapter.finishUpdate(this);
            int childCount;
            View child;
            LayoutParams layoutParams;
            ItemInfo infoForChild;
            for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                child = this.getChildAt(i);
                layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.childIndex = i;
                if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0f) {
                    infoForChild = this.infoForChild(child);
                    if (infoForChild != null) {
                        layoutParams.widthFactor = infoForChild.widthFactor;
                        layoutParams.position = infoForChild.position;
                    }
                }
            }
            this.sortChildDrawingOrder();
            if (this.hasFocus()) {
                final View focus = this.findFocus();
                ItemInfo infoForAnyChild;
                if (focus != null) {
                    infoForAnyChild = this.infoForAnyChild(focus);
                }
                else {
                    infoForAnyChild = null;
                }
                if (infoForAnyChild == null || infoForAnyChild.position != this.mCurItem) {
                    View child2;
                    ItemInfo infoForChild2;
                    for (i = 0; i < this.getChildCount(); ++i) {
                        child2 = this.getChildAt(i);
                        infoForChild2 = this.infoForChild(child2);
                        if (infoForChild2 != null && infoForChild2.position == this.mCurItem && child2.requestFocus(2)) {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public void removeOnAdapterChangeListener(@NonNull final OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners != null) {
            this.mAdapterChangeListeners.remove(onAdapterChangeListener);
        }
    }
    
    public void removeOnPageChangeListener(final OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.remove(onPageChangeListener);
        }
    }
    
    public void removeView(final View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
        }
        else {
            super.removeView(view);
        }
    }
    
    public void setAdapter(final PagerAdapter mAdapter) {
        if (this.mAdapter != null) {
            this.mAdapter.setViewPagerObserver(null);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); ++i) {
                final ItemInfo itemInfo = this.mItems.get(i);
                this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.scrollTo(this.mCurItem = 0, 0);
        }
        final PagerAdapter mAdapter2 = this.mAdapter;
        this.mAdapter = mAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            final boolean mFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            }
            else if (!mFirstLayout) {
                this.populate();
            }
            else {
                this.requestLayout();
            }
        }
        if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
            for (int j = 0; j < this.mAdapterChangeListeners.size(); ++j) {
                this.mAdapterChangeListeners.get(j).onAdapterChanged(this, mAdapter2, mAdapter);
            }
        }
    }
    
    void setChildrenDrawingOrderEnabledCompat(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     5: if_icmplt       57
        //     8: aload_0        
        //     9: getfield        android/support/v4/view/ViewPager.mSetChildrenDrawingOrderEnabled:Ljava/lang/reflect/Method;
        //    12: ifnonnull       37
        //    15: aload_0        
        //    16: ldc             Landroid/view/ViewGroup;.class
        //    18: ldc_w           "setChildrenDrawingOrderEnabled"
        //    21: iconst_1       
        //    22: anewarray       Ljava/lang/Class;
        //    25: dup            
        //    26: iconst_0       
        //    27: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //    30: aastore        
        //    31: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    34: putfield        android/support/v4/view/ViewPager.mSetChildrenDrawingOrderEnabled:Ljava/lang/reflect/Method;
        //    37: aload_0        
        //    38: getfield        android/support/v4/view/ViewPager.mSetChildrenDrawingOrderEnabled:Ljava/lang/reflect/Method;
        //    41: aload_0        
        //    42: iconst_1       
        //    43: anewarray       Ljava/lang/Object;
        //    46: dup            
        //    47: iconst_0       
        //    48: iload_1        
        //    49: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //    52: aastore        
        //    53: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    56: pop            
        //    57: return         
        //    58: astore_2       
        //    59: ldc             "ViewPager"
        //    61: ldc_w           "Can't find setChildrenDrawingOrderEnabled"
        //    64: aload_2        
        //    65: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    68: pop            
        //    69: goto            37
        //    72: astore_2       
        //    73: ldc             "ViewPager"
        //    75: ldc_w           "Error changing children drawing order"
        //    78: aload_2        
        //    79: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    82: pop            
        //    83: goto            57
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  15     37     58     72     Ljava/lang/NoSuchMethodException;
        //  37     57     72     86     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0037:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
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
    
    public void setCurrentItem(final int n) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, !this.mFirstLayout, false);
    }
    
    public void setCurrentItem(final int n, final boolean b) {
        this.setCurrentItemInternal(n, b, this.mPopulatePending = false);
    }
    
    void setCurrentItemInternal(final int n, final boolean b, final boolean b2) {
        this.setCurrentItemInternal(n, b, b2, 0);
    }
    
    void setCurrentItemInternal(int i, final boolean b, final boolean b2, final int n) {
        final boolean b3 = true;
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            this.setScrollingCacheEnabled(false);
        }
        else if (!b2 && this.mCurItem == i && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
        }
        else {
            int mCurItem;
            if (i < 0) {
                mCurItem = 0;
            }
            else if ((mCurItem = i) >= this.mAdapter.getCount()) {
                mCurItem = this.mAdapter.getCount() - 1;
            }
            i = this.mOffscreenPageLimit;
            if (mCurItem > this.mCurItem + i || mCurItem < this.mCurItem - i) {
                for (i = 0; i < this.mItems.size(); ++i) {
                    this.mItems.get(i).scrolling = true;
                }
            }
            final boolean b4 = this.mCurItem != mCurItem && b3;
            if (this.mFirstLayout) {
                this.mCurItem = mCurItem;
                if (b4) {
                    this.dispatchOnPageSelected(mCurItem);
                }
                this.requestLayout();
            }
            else {
                this.populate(mCurItem);
                this.scrollToItem(mCurItem, b, n, b4);
            }
        }
    }
    
    OnPageChangeListener setInternalPageChangeListener(final OnPageChangeListener mInternalPageChangeListener) {
        final OnPageChangeListener mInternalPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = mInternalPageChangeListener;
        return mInternalPageChangeListener2;
    }
    
    public void setOffscreenPageLimit(final int i) {
        int mOffscreenPageLimit = i;
        if (i < 1) {
            Log.w("ViewPager", "Requested offscreen page limit " + i + " too small; defaulting to " + 1);
            mOffscreenPageLimit = 1;
        }
        if (mOffscreenPageLimit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = mOffscreenPageLimit;
            this.populate();
        }
    }
    
    @Deprecated
    public void setOnPageChangeListener(final OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }
    
    public void setPageMargin(final int mPageMargin) {
        final int mPageMargin2 = this.mPageMargin;
        this.mPageMargin = mPageMargin;
        final int width = this.getWidth();
        this.recomputeScrollPosition(width, width, mPageMargin, mPageMargin2);
        this.requestLayout();
    }
    
    public void setPageMarginDrawable(@DrawableRes final int n) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(n));
    }
    
    public void setPageMarginDrawable(final Drawable mMarginDrawable) {
        this.mMarginDrawable = mMarginDrawable;
        if (mMarginDrawable != null) {
            this.refreshDrawableState();
        }
        this.setWillNotDraw(mMarginDrawable == null);
        this.invalidate();
    }
    
    public void setPageTransformer(final boolean b, final PageTransformer mPageTransformer) {
        int mDrawingOrder = 1;
        if (Build$VERSION.SDK_INT >= 11) {
            final boolean childrenDrawingOrderEnabledCompat = mPageTransformer != null;
            int n;
            if (childrenDrawingOrderEnabledCompat != (this.mPageTransformer != null)) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mPageTransformer = mPageTransformer;
            this.setChildrenDrawingOrderEnabledCompat(childrenDrawingOrderEnabledCompat);
            if (childrenDrawingOrderEnabledCompat) {
                if (b) {
                    mDrawingOrder = 2;
                }
                this.mDrawingOrder = mDrawingOrder;
            }
            else {
                this.mDrawingOrder = 0;
            }
            if (n != 0) {
                this.populate();
            }
        }
    }
    
    void setScrollState(final int mScrollState) {
        if (this.mScrollState != mScrollState) {
            this.mScrollState = mScrollState;
            if (this.mPageTransformer != null) {
                this.enableLayers(mScrollState != 0);
            }
            this.dispatchOnScrollStateChanged(mScrollState);
        }
    }
    
    void smoothScrollTo(final int n, final int n2) {
        this.smoothScrollTo(n, n2, 0);
    }
    
    void smoothScrollTo(int a, int n, int abs) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
        }
        else {
            int n2;
            if (this.mScroller != null && !this.mScroller.isFinished()) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            int n3;
            if (n2 != 0) {
                if (this.mIsScrollStarted) {
                    n3 = this.mScroller.getCurrX();
                }
                else {
                    n3 = this.mScroller.getStartX();
                }
                this.mScroller.abortAnimation();
                this.setScrollingCacheEnabled(false);
            }
            else {
                n3 = this.getScrollX();
            }
            final int scrollY = this.getScrollY();
            final int n4 = a - n3;
            n -= scrollY;
            if (n4 == 0 && n == 0) {
                this.completeScroll(false);
                this.populate();
                this.setScrollState(0);
            }
            else {
                this.setScrollingCacheEnabled(true);
                this.setScrollState(2);
                a = this.getClientWidth();
                final int n5 = a / 2;
                final float min = Math.min(1.0f, 1.0f * Math.abs(n4) / a);
                final float n6 = (float)n5;
                final float n7 = (float)n5;
                final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
                abs = Math.abs(abs);
                if (abs > 0) {
                    a = Math.round(1000.0f * Math.abs((n6 + n7 * distanceInfluenceForSnapDuration) / abs)) * 4;
                }
                else {
                    a = (int)((1.0f + Math.abs(n4) / (this.mPageMargin + a * this.mAdapter.getPageWidth(this.mCurItem))) * 100.0f);
                }
                a = Math.min(a, 600);
                this.mIsScrollStarted = false;
                this.mScroller.startScroll(n3, scrollY, n4, n, a);
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }
    
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DecorView {
    }
    
    static class ItemInfo
    {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;
    }
    
    public static class LayoutParams extends ViewGroup$LayoutParams
    {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor;
        
        public LayoutParams() {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.widthFactor = 0.0f;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }
    
    class MyAccessibilityDelegate extends AccessibilityDelegateCompat
    {
        private boolean canScroll() {
            boolean b = true;
            if (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ViewPager.class.getName());
            final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(accessibilityEvent);
            record.setScrollable(this.canScroll());
            if (accessibilityEvent.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                record.setItemCount(ViewPager.this.mAdapter.getCount());
                record.setFromIndex(ViewPager.this.mCurItem);
                record.setToIndex(ViewPager.this.mCurItem);
            }
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
            accessibilityNodeInfoCompat.setScrollable(this.canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
            }
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            boolean b = true;
            if (!super.performAccessibilityAction(view, n, bundle)) {
                switch (n) {
                    default: {
                        b = false;
                        break;
                    }
                    case 4096: {
                        if (ViewPager.this.canScrollHorizontally(1)) {
                            ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
                            break;
                        }
                        b = false;
                        break;
                    }
                    case 8192: {
                        if (ViewPager.this.canScrollHorizontally(-1)) {
                            ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
                            break;
                        }
                        b = false;
                        break;
                    }
                }
            }
            return b;
        }
    }
    
    public interface OnAdapterChangeListener
    {
        void onAdapterChanged(@NonNull final ViewPager p0, @Nullable final PagerAdapter p1, @Nullable final PagerAdapter p2);
    }
    
    public interface OnPageChangeListener
    {
        void onPageScrollStateChanged(final int p0);
        
        void onPageScrolled(final int p0, final float p1, final int p2);
        
        void onPageSelected(final int p0);
    }
    
    public interface PageTransformer
    {
        void transformPage(final View p0, final float p1);
    }
    
    private class PagerObserver extends DataSetObserver
    {
        PagerObserver() {
        }
        
        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }
        
        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;
        
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
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader2);
            this.loader = classLoader2;
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, n);
        }
    }
    
    public static class SimpleOnPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
        }
        
        @Override
        public void onPageSelected(final int n) {
        }
    }
    
    static class ViewPositionComparator implements Comparator<View>
    {
        @Override
        public int compare(final View view, final View view2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final LayoutParams layoutParams2 = (LayoutParams)view2.getLayoutParams();
            int n;
            if (layoutParams.isDecor != layoutParams2.isDecor) {
                if (layoutParams.isDecor) {
                    n = 1;
                }
                else {
                    n = -1;
                }
            }
            else {
                n = layoutParams.position - layoutParams2.position;
            }
            return n;
        }
    }
}
