// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.v4.widget.ScrollerCompat;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import java.util.Collections;
import android.util.SparseIntArray;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.annotation.CallSuper;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.database.Observable;
import android.support.v4.view.ViewConfigurationCompat;
import android.os.SystemClock;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.view.View$MeasureSpec;
import android.view.FocusFinder;
import android.view.ViewParent;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.SparseArray;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import android.support.v4.os.TraceCompat;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.support.v7.recyclerview.R;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.view.ViewConfiguration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.view.VelocityTracker;
import android.graphics.Rect;
import android.support.v4.view.NestedScrollingChildHelper;
import java.util.List;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ScrollingView;
import android.view.ViewGroup;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild
{
    private static final boolean DEBUG = false;
    private static final boolean DISPATCH_TEMP_DETACH = false;
    private static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    public static final int HORIZONTAL = 0;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
    private static final int MAX_SCROLL_DURATION = 2000;
    public static final long NO_ID = -1L;
    public static final int NO_POSITION = -1;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    private static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    private static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    private static final String TRACE_SCROLL_TAG = "RV Scroll";
    public static final int VERTICAL = 1;
    private static final Interpolator sQuinticInterpolator;
    private RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    private Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    private boolean mAdapterUpdateDuringMeasure;
    private EdgeEffectCompat mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    private boolean mClipToPadding;
    private boolean mDataSetHasChangedAfterLayout;
    private int mEatRequestLayout;
    private int mEatenAccessibilityChangeFlags;
    private boolean mFirstLayoutComplete;
    private boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    private final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    @VisibleForTesting
    LayoutManager mLayout;
    private boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter;
    private boolean mLayoutRequestEaten;
    private EdgeEffectCompat mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    private SavedState mPendingSavedState;
    private final boolean mPostUpdatesOnAnimation;
    private boolean mPostedAnimatorRunner;
    final Recycler mRecycler;
    private RecyclerListener mRecyclerListener;
    private EdgeEffectCompat mRightGlow;
    private final int[] mScrollConsumed;
    private float mScrollFactor;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private final NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    private final Rect mTempRect;
    private EdgeEffectCompat mTopGlow;
    private int mTouchSlop;
    private final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    private final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;
    
    static {
        FORCE_INVALIDATE_DISPLAY_LIST = (Build$VERSION.SDK_INT == 18 || Build$VERSION.SDK_INT == 19 || Build$VERSION.SDK_INT == 20);
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE };
        sQuinticInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
    }
    
    public RecyclerView(final Context context) {
        this(context, null);
    }
    
    public RecyclerView(final Context context, @Nullable final AttributeSet set) {
        this(context, set, 0);
    }
    
    public RecyclerView(final Context context, @Nullable final AttributeSet set, final int n) {
        final boolean b = false;
        super(context, set, n);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable() {
            @Override
            public void run() {
                if (RecyclerView.this.mFirstLayoutComplete && !RecyclerView.this.isLayoutRequested()) {
                    if (RecyclerView.this.mLayoutFrozen) {
                        RecyclerView.this.mLayoutRequestEaten = true;
                    }
                    else {
                        RecyclerView.this.consumePendingUpdateOperations();
                    }
                }
            }
        };
        this.mTempRect = new Rect();
        this.mItemDecorations = new ArrayList<ItemDecoration>();
        this.mOnItemTouchListeners = new ArrayList<OnItemTouchListener>();
        this.mEatRequestLayout = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mLayoutOrScrollCounter = 0;
        this.mItemAnimator = (ItemAnimator)new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScrollFactor = Float.MIN_VALUE;
        this.mViewFlinger = new ViewFlinger();
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = (ItemAnimatorListener)new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mItemAnimatorRunner = new Runnable() {
            @Override
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
            @Override
            public void processAppeared(final ViewHolder viewHolder, final ItemHolderInfo itemHolderInfo, final ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }
            
            @Override
            public void processDisappeared(final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @Nullable final ItemHolderInfo itemHolderInfo2) {
                RecyclerView.this.mRecycler.unscrapView(viewHolder);
                RecyclerView.this.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }
            
            @Override
            public void processPersistent(final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2) {
                viewHolder.setIsRecyclable(false);
                if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                    if (RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, itemHolderInfo, itemHolderInfo2)) {
                        RecyclerView.this.postAnimationRunner();
                    }
                }
                else if (RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, itemHolderInfo, itemHolderInfo2)) {
                    RecyclerView.this.postAnimationRunner();
                }
            }
            
            @Override
            public void unused(final ViewHolder viewHolder) {
                RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
            }
        };
        this.setScrollContainer(true);
        this.setFocusableInTouchMode(true);
        this.mPostUpdatesOnAnimation = (Build$VERSION.SDK_INT >= 16);
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinFlingVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = value.getScaledMaximumFlingVelocity();
        boolean willNotDraw = b;
        if (ViewCompat.getOverScrollMode((View)this) == 2) {
            willNotDraw = true;
        }
        this.setWillNotDraw(willNotDraw);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        this.initAdapterManager();
        this.initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
        this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.RecyclerView, n, 0);
            final String string = obtainStyledAttributes.getString(R.styleable.RecyclerView_layoutManager);
            obtainStyledAttributes.recycle();
            this.createLayoutManager(context, string, set, n, 0);
        }
        this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
    }
    
    static /* synthetic */ void access$1300(final RecyclerView recyclerView, final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        recyclerView.attachViewToParent(view, n, viewGroup$LayoutParams);
    }
    
    static /* synthetic */ void access$1400(final RecyclerView recyclerView, final int n) {
        recyclerView.detachViewFromParent(n);
    }
    
    static /* synthetic */ boolean access$3800(final RecyclerView recyclerView) {
        return recyclerView.awakenScrollBars();
    }
    
    static /* synthetic */ void access$5600(final RecyclerView recyclerView, final int n, final int n2) {
        recyclerView.setMeasuredDimension(n, n2);
    }
    
    private void addAnimatingView(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        final boolean b = itemView.getParent() == this;
        this.mRecycler.unscrapView(this.getChildViewHolder(itemView));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(itemView, -1, itemView.getLayoutParams(), true);
        }
        else if (!b) {
            this.mChildHelper.addView(itemView, true);
        }
        else {
            this.mChildHelper.hide(itemView);
        }
    }
    
    private void animateAppearance(@NonNull final ViewHolder viewHolder, @Nullable final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2) {
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    private void animateChange(@NonNull final ViewHolder mShadowingHolder, @NonNull final ViewHolder mShadowedHolder, @NonNull final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2, final boolean b, final boolean b2) {
        mShadowingHolder.setIsRecyclable(false);
        if (b) {
            this.addAnimatingView(mShadowingHolder);
        }
        if (mShadowingHolder != mShadowedHolder) {
            if (b2) {
                this.addAnimatingView(mShadowedHolder);
            }
            mShadowingHolder.mShadowedHolder = mShadowedHolder;
            this.addAnimatingView(mShadowingHolder);
            this.mRecycler.unscrapView(mShadowingHolder);
            mShadowedHolder.setIsRecyclable(false);
            mShadowedHolder.mShadowingHolder = mShadowingHolder;
        }
        if (this.mItemAnimator.animateChange(mShadowingHolder, mShadowedHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    private void animateDisappearance(@NonNull final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @Nullable final ItemHolderInfo itemHolderInfo2) {
        this.addAnimatingView(viewHolder);
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    private boolean canReuseUpdatedViewHolder(final ViewHolder viewHolder) {
        return this.mItemAnimator == null || this.mItemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }
    
    private void cancelTouch() {
        this.resetTouch();
        this.setScrollState(0);
    }
    
    private void considerReleasingGlowsOnScroll(final int n, final int n2) {
        boolean onRelease;
        final boolean b = onRelease = false;
        if (this.mLeftGlow != null) {
            onRelease = b;
            if (!this.mLeftGlow.isFinished()) {
                onRelease = b;
                if (n > 0) {
                    onRelease = this.mLeftGlow.onRelease();
                }
            }
        }
        boolean b2 = onRelease;
        if (this.mRightGlow != null) {
            b2 = onRelease;
            if (!this.mRightGlow.isFinished()) {
                b2 = onRelease;
                if (n < 0) {
                    b2 = (onRelease | this.mRightGlow.onRelease());
                }
            }
        }
        boolean b3 = b2;
        if (this.mTopGlow != null) {
            b3 = b2;
            if (!this.mTopGlow.isFinished()) {
                b3 = b2;
                if (n2 > 0) {
                    b3 = (b2 | this.mTopGlow.onRelease());
                }
            }
        }
        int n3 = b3 ? 1 : 0;
        if (this.mBottomGlow != null) {
            n3 = (b3 ? 1 : 0);
            if (!this.mBottomGlow.isFinished()) {
                n3 = (b3 ? 1 : 0);
                if (n2 < 0) {
                    n3 = ((b3 | this.mBottomGlow.onRelease()) ? 1 : 0);
                }
            }
        }
        if (n3 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private void consumePendingUpdateOperations() {
        if (this.mFirstLayoutComplete) {
            if (this.mDataSetHasChangedAfterLayout) {
                TraceCompat.beginSection("RV FullInvalidate");
                this.dispatchLayout();
                TraceCompat.endSection();
            }
            else if (this.mAdapterHelper.hasPendingUpdates()) {
                if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
                    TraceCompat.beginSection("RV PartialInvalidate");
                    this.eatRequestLayout();
                    this.mAdapterHelper.preProcess();
                    if (!this.mLayoutRequestEaten) {
                        if (this.hasUpdatedView()) {
                            this.dispatchLayout();
                        }
                        else {
                            this.mAdapterHelper.consumePostponedUpdates();
                        }
                    }
                    this.resumeRequestLayout(true);
                    TraceCompat.endSection();
                }
                else if (this.mAdapterHelper.hasPendingUpdates()) {
                    TraceCompat.beginSection("RV FullInvalidate");
                    this.dispatchLayout();
                    TraceCompat.endSection();
                }
            }
        }
    }
    
    private void createLayoutManager(Context ex, String trim, final AttributeSet set, final int i, final int j) {
        if (trim == null) {
            return;
        }
        trim = trim.trim();
        if (trim.length() == 0) {
            return;
        }
        final String fullClassName = this.getFullClassName((Context)ex, trim);
        try {
            Label_0111: {
                if (!this.isInEditMode()) {
                    break Label_0111;
                }
                ClassLoader classLoader = this.getClass().getClassLoader();
                while (true) {
                    final Class<? extends LayoutManager> subclass = classLoader.loadClass(fullClassName).asSubclass(LayoutManager.class);
                    final NoSuchMethodException ex2 = null;
                    try {
                        final Constructor<? extends LayoutManager> constructor = subclass.getConstructor(RecyclerView.LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        ex = (NoSuchMethodException)new Object[] { ex, set, i, j };
                        constructor.setAccessible(true);
                        this.setLayoutManager((LayoutManager)constructor.newInstance((Object[])(Object)ex));
                        return;
                        classLoader = ((Context)ex).getClassLoader();
                    }
                    catch (NoSuchMethodException ex) {
                        try {
                            final Constructor<? extends LayoutManager> constructor = subclass.getConstructor((Class<?>[])new Class[0]);
                            ex = ex2;
                        }
                        catch (NoSuchMethodException cause) {
                            cause.initCause(ex);
                            throw new IllegalStateException(set.getPositionDescription() + ": Error creating LayoutManager " + fullClassName, cause);
                        }
                    }
                }
            }
        }
        catch (ClassNotFoundException cause2) {
            throw new IllegalStateException(set.getPositionDescription() + ": Unable to find LayoutManager " + fullClassName, cause2);
        }
        catch (InvocationTargetException cause3) {
            throw new IllegalStateException(set.getPositionDescription() + ": Could not instantiate the LayoutManager: " + fullClassName, cause3);
        }
        catch (InstantiationException cause4) {
            throw new IllegalStateException(set.getPositionDescription() + ": Could not instantiate the LayoutManager: " + fullClassName, cause4);
        }
        catch (IllegalAccessException cause5) {
            throw new IllegalStateException(set.getPositionDescription() + ": Cannot access non-public constructor " + fullClassName, cause5);
        }
        catch (ClassCastException cause6) {
            throw new IllegalStateException(set.getPositionDescription() + ": Class is not a LayoutManager " + fullClassName, cause6);
        }
    }
    
    private boolean didChildRangeChange(final int n, final int n2) {
        final boolean b = false;
        final int childCount = this.mChildHelper.getChildCount();
        boolean b2;
        if (childCount == 0) {
            if (n == 0) {
                b2 = b;
                if (n2 == 0) {
                    return b2;
                }
            }
            b2 = true;
        }
        else {
            int n3 = 0;
            while (true) {
                b2 = b;
                if (n3 >= childCount) {
                    return b2;
                }
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(n3));
                if (!childViewHolderInt.shouldIgnore()) {
                    final int layoutPosition = childViewHolderInt.getLayoutPosition();
                    if (layoutPosition < n || layoutPosition > n2) {
                        break;
                    }
                }
                ++n3;
            }
            b2 = true;
        }
        return b2;
    }
    
    private void dispatchChildAttached(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        this.onChildAttachedToWindow(view);
        if (this.mAdapter != null && childViewHolderInt != null) {
            this.mAdapter.onViewAttachedToWindow(childViewHolderInt);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(view);
            }
        }
    }
    
    private void dispatchChildDetached(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        this.onChildDetachedFromWindow(view);
        if (this.mAdapter != null && childViewHolderInt != null) {
            this.mAdapter.onViewDetachedFromWindow(childViewHolderInt);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(view);
            }
        }
    }
    
    private void dispatchContentChangedIfNecessary() {
        final int mEatenAccessibilityChangeFlags = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (mEatenAccessibilityChangeFlags != 0 && this.isAccessibilityEnabled()) {
            final AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(obtain, mEatenAccessibilityChangeFlags);
            this.sendAccessibilityEventUnchecked(obtain);
        }
    }
    
    private void dispatchLayoutStep1() {
        this.mState.assertLayoutStep(1);
        this.mState.mIsMeasuring = false;
        this.eatRequestLayout();
        this.mViewInfoStore.clear();
        this.onEnterLayoutOrScroll();
        this.processAdapterUpdatesAndSetAnimationFlags();
        this.mState.mTrackOldChangeHolders = (this.mState.mRunSimpleAnimations && this.mItemsChanged);
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore() && (!childViewHolderInt.isInvalid() || this.mAdapter.hasStableIds())) {
                    this.mViewInfoStore.addToPreLayout(childViewHolderInt, this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt, ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt), childViewHolderInt.getUnmodifiedPayloads()));
                    if (this.mState.mTrackOldChangeHolders && childViewHolderInt.isUpdated() && !childViewHolderInt.isRemoved() && !childViewHolderInt.shouldIgnore() && !childViewHolderInt.isInvalid()) {
                        this.mViewInfoStore.addToOldChangeHolders(this.getChangedHolderKey(childViewHolderInt), childViewHolderInt);
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            this.saveOldPositions();
            final boolean access$1800 = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = access$1800;
            for (int j = 0; j < this.mChildHelper.getChildCount(); ++j) {
                final ViewHolder childViewHolderInt2 = getChildViewHolderInt(this.mChildHelper.getChildAt(j));
                if (!childViewHolderInt2.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(childViewHolderInt2)) {
                    final int buildAdapterChangeFlagsForAnimations = ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt2);
                    final boolean hasAnyOfTheFlags = childViewHolderInt2.hasAnyOfTheFlags(8192);
                    int n = buildAdapterChangeFlagsForAnimations;
                    if (!hasAnyOfTheFlags) {
                        n = (buildAdapterChangeFlagsForAnimations | 0x1000);
                    }
                    final ItemHolderInfo recordPreLayoutInformation = this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt2, n, childViewHolderInt2.getUnmodifiedPayloads());
                    if (hasAnyOfTheFlags) {
                        this.recordAnimationInfoIfBouncedHiddenView(childViewHolderInt2, recordPreLayoutInformation);
                    }
                    else {
                        this.mViewInfoStore.addToAppearedInPreLayoutHolders(childViewHolderInt2, recordPreLayoutInformation);
                    }
                }
            }
            this.clearOldPositions();
        }
        else {
            this.clearOldPositions();
        }
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }
    
    private void dispatchLayoutStep2() {
        this.eatRequestLayout();
        this.onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        this.mState.mRunSimpleAnimations = (this.mState.mRunSimpleAnimations && this.mItemAnimator != null);
        this.mState.mLayoutStep = 4;
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
    }
    
    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        this.eatRequestLayout();
        this.onEnterLayoutOrScroll();
        this.mState.mLayoutStep = 1;
        if (this.mState.mRunSimpleAnimations) {
            for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore()) {
                    final long changedHolderKey = this.getChangedHolderKey(childViewHolderInt);
                    final ItemHolderInfo recordPostLayoutInformation = this.mItemAnimator.recordPostLayoutInformation(this.mState, childViewHolderInt);
                    final ViewHolder fromOldChangeHolders = this.mViewInfoStore.getFromOldChangeHolders(changedHolderKey);
                    if (fromOldChangeHolders != null && !fromOldChangeHolders.shouldIgnore()) {
                        final boolean disappearing = this.mViewInfoStore.isDisappearing(fromOldChangeHolders);
                        final boolean disappearing2 = this.mViewInfoStore.isDisappearing(childViewHolderInt);
                        if (disappearing && fromOldChangeHolders == childViewHolderInt) {
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                        }
                        else {
                            final ItemHolderInfo popFromPreLayout = this.mViewInfoStore.popFromPreLayout(fromOldChangeHolders);
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                            final ItemHolderInfo popFromPostLayout = this.mViewInfoStore.popFromPostLayout(childViewHolderInt);
                            if (popFromPreLayout == null) {
                                this.handleMissingPreInfoForChangeError(changedHolderKey, childViewHolderInt, fromOldChangeHolders);
                            }
                            else {
                                this.animateChange(fromOldChangeHolders, childViewHolderInt, popFromPreLayout, popFromPostLayout, disappearing, disappearing2);
                            }
                        }
                    }
                    else {
                        this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                    }
                }
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        this.onExitLayoutOrScroll();
        this.resumeRequestLayout(false);
        this.mViewInfoStore.clear();
        if (this.didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1])) {
            this.dispatchOnScrolled(0, 0);
        }
    }
    
    private boolean dispatchOnItemTouch(final MotionEvent motionEvent) {
        final boolean b = true;
        final int action = motionEvent.getAction();
        if (this.mActiveOnItemTouchListener != null) {
            if (action != 0) {
                this.mActiveOnItemTouchListener.onTouchEvent(this, motionEvent);
                if (action != 3) {
                    final boolean b2 = b;
                    if (action != 1) {
                        return b2;
                    }
                }
                this.mActiveOnItemTouchListener = null;
                return b;
            }
            this.mActiveOnItemTouchListener = null;
        }
        if (action != 0) {
            for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
                final OnItemTouchListener mActiveOnItemTouchListener = this.mOnItemTouchListeners.get(i);
                if (mActiveOnItemTouchListener.onInterceptTouchEvent(this, motionEvent)) {
                    this.mActiveOnItemTouchListener = mActiveOnItemTouchListener;
                    return b;
                }
            }
        }
        return false;
    }
    
    private boolean dispatchOnItemTouchIntercept(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action == 3 || action == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
            final OnItemTouchListener mActiveOnItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (mActiveOnItemTouchListener.onInterceptTouchEvent(this, motionEvent) && action != 3) {
                this.mActiveOnItemTouchListener = mActiveOnItemTouchListener;
                return true;
            }
        }
        return false;
    }
    
    private void findMinMaxChildLayoutPositions(final int[] array) {
        final int childCount = this.mChildHelper.getChildCount();
        if (childCount == 0) {
            array[1] = (array[0] = 0);
        }
        else {
            int n = Integer.MAX_VALUE;
            int n2 = Integer.MIN_VALUE;
            int n3;
            for (int i = 0; i < childCount; ++i, n2 = n3) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (childViewHolderInt.shouldIgnore()) {
                    n3 = n2;
                }
                else {
                    final int layoutPosition = childViewHolderInt.getLayoutPosition();
                    int n4;
                    if (layoutPosition < (n4 = n)) {
                        n4 = layoutPosition;
                    }
                    n3 = n2;
                    n = n4;
                    if (layoutPosition > n2) {
                        n3 = layoutPosition;
                        n = n4;
                    }
                }
            }
            array[0] = n;
            array[1] = n2;
        }
    }
    
    private int getAdapterPositionFor(final ViewHolder viewHolder) {
        int applyPendingUpdatesToPosition;
        if (viewHolder.hasAnyOfTheFlags(524) || !viewHolder.isBound()) {
            applyPendingUpdatesToPosition = -1;
        }
        else {
            applyPendingUpdatesToPosition = this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
        }
        return applyPendingUpdatesToPosition;
    }
    
    static ViewHolder getChildViewHolderInt(final View view) {
        Object mViewHolder;
        if (view == null) {
            mViewHolder = null;
        }
        else {
            mViewHolder = ((LayoutParams)view.getLayoutParams()).mViewHolder;
        }
        return (ViewHolder)mViewHolder;
    }
    
    private String getFullClassName(final Context context, final String s) {
        String s2;
        if (s.charAt(0) == '.') {
            s2 = context.getPackageName() + s;
        }
        else {
            s2 = s;
            if (!s.contains(".")) {
                s2 = RecyclerView.class.getPackage().getName() + '.' + s;
            }
        }
        return s2;
    }
    
    private float getScrollFactor() {
        if (this.mScrollFactor == Float.MIN_VALUE) {
            final TypedValue typedValue = new TypedValue();
            if (!this.getContext().getTheme().resolveAttribute(16842829, typedValue, true)) {
                return 0.0f;
            }
            this.mScrollFactor = typedValue.getDimension(this.getContext().getResources().getDisplayMetrics());
        }
        return this.mScrollFactor;
    }
    
    private void handleMissingPreInfoForChangeError(final long n, final ViewHolder obj, final ViewHolder obj2) {
        final int childCount = this.mChildHelper.getChildCount();
        int i = 0;
        while (i < childCount) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != obj && this.getChangedHolderKey(childViewHolderInt) == n) {
                if (this.mAdapter != null && this.mAdapter.hasStableIds()) {
                    throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + childViewHolderInt + " \n View Holder 2:" + obj);
                }
                throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + childViewHolderInt + " \n View Holder 2:" + obj);
            }
            else {
                ++i;
            }
        }
        Log.e("RecyclerView", "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + obj2 + " cannot be found but it is necessary for " + obj);
    }
    
    private boolean hasUpdatedView() {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.isUpdated()) {
                return true;
            }
        }
        return false;
    }
    
    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper((ChildHelper.Callback)new ChildHelper.Callback() {
            @Override
            public void addView(final View view, final int n) {
                RecyclerView.this.addView(view, n);
                RecyclerView.this.dispatchChildAttached(view);
            }
            
            @Override
            public void attachViewToParent(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    if (!childViewHolderInt.isTmpDetached() && !childViewHolderInt.shouldIgnore()) {
                        throw new IllegalArgumentException("Called attach on a child which is not detached: " + childViewHolderInt);
                    }
                    childViewHolderInt.clearTmpDetachFlag();
                }
                RecyclerView.access$1300(RecyclerView.this, view, n, viewGroup$LayoutParams);
            }
            
            @Override
            public void detachViewFromParent(final int n) {
                final View child = this.getChildAt(n);
                if (child != null) {
                    final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                    if (childViewHolderInt != null) {
                        if (childViewHolderInt.isTmpDetached() && !childViewHolderInt.shouldIgnore()) {
                            throw new IllegalArgumentException("called detach on an already detached child " + childViewHolderInt);
                        }
                        childViewHolderInt.addFlags(256);
                    }
                }
                RecyclerView.access$1400(RecyclerView.this, n);
            }
            
            @Override
            public View getChildAt(final int n) {
                return RecyclerView.this.getChildAt(n);
            }
            
            @Override
            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }
            
            @Override
            public ViewHolder getChildViewHolder(final View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }
            
            @Override
            public int indexOfChild(final View view) {
                return RecyclerView.this.indexOfChild(view);
            }
            
            @Override
            public void onEnteredHiddenState(final View view) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onEnteredHiddenState();
                }
            }
            
            @Override
            public void onLeftHiddenState(final View view) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onLeftHiddenState();
                }
            }
            
            @Override
            public void removeAllViews() {
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    RecyclerView.this.dispatchChildDetached(this.getChildAt(i));
                }
                RecyclerView.this.removeAllViews();
            }
            
            @Override
            public void removeViewAt(final int n) {
                final View child = RecyclerView.this.getChildAt(n);
                if (child != null) {
                    RecyclerView.this.dispatchChildDetached(child);
                }
                RecyclerView.this.removeViewAt(n);
            }
        });
    }
    
    private void jumpToPositionForSmoothScroller(final int n) {
        if (this.mLayout != null) {
            this.mLayout.scrollToPosition(n);
            this.awakenScrollBars();
        }
    }
    
    private void onEnterLayoutOrScroll() {
        ++this.mLayoutOrScrollCounter;
    }
    
    private void onExitLayoutOrScroll() {
        --this.mLayoutOrScrollCounter;
        if (this.mLayoutOrScrollCounter < 1) {
            this.mLayoutOrScrollCounter = 0;
            this.dispatchContentChangedIfNecessary();
        }
    }
    
    private void onPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, actionIndex) == this.mScrollPointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mScrollPointerId = MotionEventCompat.getPointerId(motionEvent, n);
            final int n2 = (int)(MotionEventCompat.getX(motionEvent, n) + 0.5f);
            this.mLastTouchX = n2;
            this.mInitialTouchX = n2;
            final int n3 = (int)(MotionEventCompat.getY(motionEvent, n) + 0.5f);
            this.mLastTouchY = n3;
            this.mInitialTouchY = n3;
        }
    }
    
    private void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }
    
    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }
    
    private void processAdapterUpdatesAndSetAnimationFlags() {
        final boolean b = true;
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            this.markKnownViewsInvalid();
            this.mLayout.onItemsChanged(this);
        }
        if (this.predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        }
        else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean b2;
        if (this.mItemsAddedOrRemoved || this.mItemsChanged) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        this.mState.mRunSimpleAnimations = (this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || b2 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds()));
        this.mState.mRunPredictiveAnimations = (this.mState.mRunSimpleAnimations && b2 && !this.mDataSetHasChangedAfterLayout && this.predictiveItemAnimationsEnabled() && b);
    }
    
    private void pullGlows(final float n, final float n2, final float n3, final float n4) {
        final int n5 = 0;
        int n6;
        if (n2 < 0.0f) {
            this.ensureLeftGlow();
            n6 = n5;
            if (this.mLeftGlow.onPull(-n2 / this.getWidth(), 1.0f - n3 / this.getHeight())) {
                n6 = 1;
            }
        }
        else {
            n6 = n5;
            if (n2 > 0.0f) {
                this.ensureRightGlow();
                n6 = n5;
                if (this.mRightGlow.onPull(n2 / this.getWidth(), n3 / this.getHeight())) {
                    n6 = 1;
                }
            }
        }
        int n7;
        if (n4 < 0.0f) {
            this.ensureTopGlow();
            n7 = n6;
            if (this.mTopGlow.onPull(-n4 / this.getHeight(), n / this.getWidth())) {
                n7 = 1;
            }
        }
        else {
            n7 = n6;
            if (n4 > 0.0f) {
                this.ensureBottomGlow();
                n7 = n6;
                if (this.mBottomGlow.onPull(n4 / this.getHeight(), 1.0f - n / this.getWidth())) {
                    n7 = 1;
                }
            }
        }
        if (n7 != 0 || n2 != 0.0f || n4 != 0.0f) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private void recordAnimationInfoIfBouncedHiddenView(final ViewHolder viewHolder, final ItemHolderInfo itemHolderInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            this.mViewInfoStore.addToOldChangeHolders(this.getChangedHolderKey(viewHolder), viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
    }
    
    private void releaseGlows() {
        int onRelease = 0;
        if (this.mLeftGlow != null) {
            onRelease = (this.mLeftGlow.onRelease() ? 1 : 0);
        }
        int n = onRelease;
        if (this.mTopGlow != null) {
            n = (onRelease | (this.mTopGlow.onRelease() ? 1 : 0));
        }
        int n2 = n;
        if (this.mRightGlow != null) {
            n2 = (n | (this.mRightGlow.onRelease() ? 1 : 0));
        }
        int n3 = n2;
        if (this.mBottomGlow != null) {
            n3 = (n2 | (this.mBottomGlow.onRelease() ? 1 : 0));
        }
        if (n3 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private boolean removeAnimatingView(final View view) {
        this.eatRequestLayout();
        final boolean removeViewIfHidden = this.mChildHelper.removeViewIfHidden(view);
        if (removeViewIfHidden) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(childViewHolderInt);
            this.mRecycler.recycleViewHolderInternal(childViewHolderInt);
        }
        this.resumeRequestLayout(!removeViewIfHidden);
        return removeViewIfHidden;
    }
    
    private void repositionShadowingViews() {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.mChildHelper.getChildAt(i);
            final ViewHolder childViewHolder = this.getChildViewHolder(child);
            if (childViewHolder != null && childViewHolder.mShadowingHolder != null) {
                final View itemView = childViewHolder.mShadowingHolder.itemView;
                final int left = child.getLeft();
                final int top = child.getTop();
                if (left != itemView.getLeft() || top != itemView.getTop()) {
                    itemView.layout(left, top, itemView.getWidth() + left, itemView.getHeight() + top);
                }
            }
        }
    }
    
    private void resetTouch() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
        }
        this.stopNestedScroll();
        this.releaseGlows();
    }
    
    private void setAdapterInternal(final Adapter mAdapter, final boolean b, final boolean b2) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!b || b2) {
            if (this.mItemAnimator != null) {
                this.mItemAnimator.endAnimations();
            }
            if (this.mLayout != null) {
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            }
            this.mRecycler.clear();
        }
        this.mAdapterHelper.reset();
        final Adapter mAdapter2 = this.mAdapter;
        if ((this.mAdapter = mAdapter) != null) {
            mAdapter.registerAdapterDataObserver(this.mObserver);
            mAdapter.onAttachedToRecyclerView(this);
        }
        if (this.mLayout != null) {
            this.mLayout.onAdapterChanged(mAdapter2, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(mAdapter2, this.mAdapter, b);
        this.mState.mStructureChanged = true;
        this.markKnownViewsInvalid();
    }
    
    private void setDataSetChangedAfterLayout() {
        if (!this.mDataSetHasChangedAfterLayout) {
            this.mDataSetHasChangedAfterLayout = true;
            for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
                if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                    childViewHolderInt.addFlags(512);
                }
            }
            this.mRecycler.setAdapterPositionsAsUnknown();
        }
    }
    
    private void setScrollState(final int mScrollState) {
        if (mScrollState != this.mScrollState) {
            if ((this.mScrollState = mScrollState) != 2) {
                this.stopScrollersInternal();
            }
            this.dispatchOnScrollStateChanged(mScrollState);
        }
    }
    
    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        if (this.mLayout != null) {
            this.mLayout.stopSmoothScroller();
        }
    }
    
    void absorbGlows(final int n, final int n2) {
        if (n < 0) {
            this.ensureLeftGlow();
            this.mLeftGlow.onAbsorb(-n);
        }
        else if (n > 0) {
            this.ensureRightGlow();
            this.mRightGlow.onAbsorb(n);
        }
        if (n2 < 0) {
            this.ensureTopGlow();
            this.mTopGlow.onAbsorb(-n2);
        }
        else if (n2 > 0) {
            this.ensureBottomGlow();
            this.mBottomGlow.onAbsorb(n2);
        }
        if (n != 0 || n2 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        if (this.mLayout == null || !this.mLayout.onAddFocusables(this, list, n, n2)) {
            super.addFocusables((ArrayList)list, n, n2);
        }
    }
    
    public void addItemDecoration(final ItemDecoration itemDecoration) {
        this.addItemDecoration(itemDecoration, -1);
    }
    
    public void addItemDecoration(final ItemDecoration itemDecoration, final int index) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(false);
        }
        if (index < 0) {
            this.mItemDecorations.add(itemDecoration);
        }
        else {
            this.mItemDecorations.add(index, itemDecoration);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    public void addOnChildAttachStateChangeListener(final OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>();
        }
        this.mOnChildAttachStateListeners.add(onChildAttachStateChangeListener);
    }
    
    public void addOnItemTouchListener(final OnItemTouchListener e) {
        this.mOnItemTouchListeners.add(e);
    }
    
    public void addOnScrollListener(final OnScrollListener onScrollListener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList<OnScrollListener>();
        }
        this.mScrollListeners.add(onScrollListener);
    }
    
    void assertInLayoutOrScroll(final String s) {
        if (this.isComputingLayout()) {
            return;
        }
        if (s == null) {
            throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling");
        }
        throw new IllegalStateException(s);
    }
    
    void assertNotInLayoutOrScroll(final String s) {
        if (!this.isComputingLayout()) {
            return;
        }
        if (s == null) {
            throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling");
        }
        throw new IllegalStateException(s);
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)viewGroup$LayoutParams);
    }
    
    void clearOldPositions() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }
    
    public void clearOnChildAttachStateChangeListeners() {
        if (this.mOnChildAttachStateListeners != null) {
            this.mOnChildAttachStateListeners.clear();
        }
    }
    
    public void clearOnScrollListeners() {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.clear();
        }
    }
    
    public int computeHorizontalScrollExtent() {
        int computeHorizontalScrollExtent = 0;
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            computeHorizontalScrollExtent = this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return computeHorizontalScrollExtent;
    }
    
    public int computeHorizontalScrollOffset() {
        int computeHorizontalScrollOffset = 0;
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            computeHorizontalScrollOffset = this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return computeHorizontalScrollOffset;
    }
    
    public int computeHorizontalScrollRange() {
        int computeHorizontalScrollRange = 0;
        if (this.mLayout != null && this.mLayout.canScrollHorizontally()) {
            computeHorizontalScrollRange = this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return computeHorizontalScrollRange;
    }
    
    public int computeVerticalScrollExtent() {
        int computeVerticalScrollExtent = 0;
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            computeVerticalScrollExtent = this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return computeVerticalScrollExtent;
    }
    
    public int computeVerticalScrollOffset() {
        int computeVerticalScrollOffset = 0;
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            computeVerticalScrollOffset = this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return computeVerticalScrollOffset;
    }
    
    public int computeVerticalScrollRange() {
        int computeVerticalScrollRange = 0;
        if (this.mLayout != null && this.mLayout.canScrollVertically()) {
            computeVerticalScrollRange = this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return computeVerticalScrollRange;
    }
    
    void defaultOnMeasure(final int n, final int n2) {
        this.setMeasuredDimension(LayoutManager.chooseSize(n, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth((View)this)), LayoutManager.chooseSize(n2, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
    }
    
    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e("RecyclerView", "No adapter attached; skipping layout");
        }
        else if (this.mLayout == null) {
            Log.e("RecyclerView", "No layout manager attached; skipping layout");
        }
        else {
            this.mState.mIsMeasuring = false;
            if (this.mState.mLayoutStep == 1) {
                this.dispatchLayoutStep1();
                this.mLayout.setExactMeasureSpecsFrom(this);
                this.dispatchLayoutStep2();
            }
            else if (this.mAdapterHelper.hasUpdates() || this.mLayout.getWidth() != this.getWidth() || this.mLayout.getHeight() != this.getHeight()) {
                this.mLayout.setExactMeasureSpecsFrom(this);
                this.dispatchLayoutStep2();
            }
            else {
                this.mLayout.setExactMeasureSpecsFrom(this);
            }
            this.dispatchLayoutStep3();
        }
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.mScrollingChildHelper.dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.mScrollingChildHelper.dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.mScrollingChildHelper.dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.mScrollingChildHelper.dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    void dispatchOnScrollStateChanged(final int n) {
        if (this.mLayout != null) {
            this.mLayout.onScrollStateChanged(n);
        }
        this.onScrollStateChanged(n);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(this, n);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrollStateChanged(this, n);
            }
        }
    }
    
    void dispatchOnScrolled(final int n, final int n2) {
        final int scrollX = this.getScrollX();
        final int scrollY = this.getScrollY();
        this.onScrollChanged(scrollX, scrollY, scrollX, scrollY);
        this.onScrolled(n, n2);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrolled(this, n, n2);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrolled(this, n, n2);
            }
        }
    }
    
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly((SparseArray)sparseArray);
    }
    
    protected void dispatchSaveInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly((SparseArray)sparseArray);
    }
    
    public void draw(final Canvas canvas) {
        final int n = 1;
        super.draw(canvas);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mItemDecorations.get(i).onDrawOver(canvas, this, this.mState);
        }
        int n3;
        final int n2 = n3 = 0;
        if (this.mLeftGlow != null) {
            n3 = n2;
            if (!this.mLeftGlow.isFinished()) {
                final int save = canvas.save();
                int paddingBottom;
                if (this.mClipToPadding) {
                    paddingBottom = this.getPaddingBottom();
                }
                else {
                    paddingBottom = 0;
                }
                canvas.rotate(270.0f);
                canvas.translate((float)(-this.getHeight() + paddingBottom), 0.0f);
                if (this.mLeftGlow != null && this.mLeftGlow.draw(canvas)) {
                    n3 = 1;
                }
                else {
                    n3 = 0;
                }
                canvas.restoreToCount(save);
            }
        }
        int n4 = n3;
        if (this.mTopGlow != null) {
            n4 = n3;
            if (!this.mTopGlow.isFinished()) {
                final int save2 = canvas.save();
                if (this.mClipToPadding) {
                    canvas.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
                }
                boolean b;
                if (this.mTopGlow != null && this.mTopGlow.draw(canvas)) {
                    b = true;
                }
                else {
                    b = false;
                }
                n4 = (n3 | (b ? 1 : 0));
                canvas.restoreToCount(save2);
            }
        }
        int n5 = n4;
        if (this.mRightGlow != null) {
            n5 = n4;
            if (!this.mRightGlow.isFinished()) {
                final int save3 = canvas.save();
                final int width = this.getWidth();
                int paddingTop;
                if (this.mClipToPadding) {
                    paddingTop = this.getPaddingTop();
                }
                else {
                    paddingTop = 0;
                }
                canvas.rotate(90.0f);
                canvas.translate((float)(-paddingTop), (float)(-width));
                boolean b2;
                if (this.mRightGlow != null && this.mRightGlow.draw(canvas)) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                n5 = (n4 | (b2 ? 1 : 0));
                canvas.restoreToCount(save3);
            }
        }
        int n6 = n5;
        if (this.mBottomGlow != null) {
            n6 = n5;
            if (!this.mBottomGlow.isFinished()) {
                final int save4 = canvas.save();
                canvas.rotate(180.0f);
                if (this.mClipToPadding) {
                    canvas.translate((float)(-this.getWidth() + this.getPaddingRight()), (float)(-this.getHeight() + this.getPaddingBottom()));
                }
                else {
                    canvas.translate((float)(-this.getWidth()), (float)(-this.getHeight()));
                }
                int n7;
                if (this.mBottomGlow != null && this.mBottomGlow.draw(canvas)) {
                    n7 = n;
                }
                else {
                    n7 = 0;
                }
                n6 = (n5 | n7);
                canvas.restoreToCount(save4);
            }
        }
        int n8;
        if ((n8 = n6) == 0) {
            n8 = n6;
            if (this.mItemAnimator != null) {
                n8 = n6;
                if (this.mItemDecorations.size() > 0) {
                    n8 = n6;
                    if (this.mItemAnimator.isRunning()) {
                        n8 = 1;
                    }
                }
            }
        }
        if (n8 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean drawChild(final Canvas canvas, final View view, final long n) {
        return super.drawChild(canvas, view, n);
    }
    
    void eatRequestLayout() {
        ++this.mEatRequestLayout;
        if (this.mEatRequestLayout == 1 && !this.mLayoutFrozen) {
            this.mLayoutRequestEaten = false;
        }
    }
    
    void ensureBottomGlow() {
        if (this.mBottomGlow == null) {
            this.mBottomGlow = new EdgeEffectCompat(this.getContext());
            if (this.mClipToPadding) {
                this.mBottomGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
            }
            else {
                this.mBottomGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
            }
        }
    }
    
    void ensureLeftGlow() {
        if (this.mLeftGlow == null) {
            this.mLeftGlow = new EdgeEffectCompat(this.getContext());
            if (this.mClipToPadding) {
                this.mLeftGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
            }
            else {
                this.mLeftGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
            }
        }
    }
    
    void ensureRightGlow() {
        if (this.mRightGlow == null) {
            this.mRightGlow = new EdgeEffectCompat(this.getContext());
            if (this.mClipToPadding) {
                this.mRightGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
            }
            else {
                this.mRightGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
            }
        }
    }
    
    void ensureTopGlow() {
        if (this.mTopGlow == null) {
            this.mTopGlow = new EdgeEffectCompat(this.getContext());
            if (this.mClipToPadding) {
                this.mTopGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
            }
            else {
                this.mTopGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
            }
        }
    }
    
    public View findChildViewUnder(final float n, final float n2) {
        for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
            final View child = this.mChildHelper.getChildAt(i);
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);
            if (n >= child.getLeft() + translationX && n <= child.getRight() + translationX && n2 >= child.getTop() + translationY && n2 <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }
    
    @Nullable
    public View findContainingItemView(View view) {
        ViewParent viewParent;
        for (viewParent = view.getParent(); viewParent != null && viewParent != this && viewParent instanceof View; viewParent = view.getParent()) {
            view = (View)viewParent;
        }
        if (viewParent != this) {
            view = null;
        }
        return view;
    }
    
    @Nullable
    public ViewHolder findContainingViewHolder(View containingItemView) {
        containingItemView = this.findContainingItemView(containingItemView);
        ViewHolder childViewHolder;
        if (containingItemView == null) {
            childViewHolder = null;
        }
        else {
            childViewHolder = this.getChildViewHolder(containingItemView);
        }
        return childViewHolder;
    }
    
    public ViewHolder findViewHolderForAdapterPosition(final int n) {
        ViewHolder viewHolder;
        if (this.mDataSetHasChangedAfterLayout) {
            viewHolder = null;
        }
        else {
            for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
                if (childViewHolderInt != null && !childViewHolderInt.isRemoved()) {
                    viewHolder = childViewHolderInt;
                    if (this.getAdapterPositionFor(childViewHolderInt) == n) {
                        return viewHolder;
                    }
                }
            }
            viewHolder = null;
        }
        return viewHolder;
    }
    
    public ViewHolder findViewHolderForItemId(final long n) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && childViewHolderInt.getItemId() == n) {
                return childViewHolderInt;
            }
        }
        return null;
    }
    
    public ViewHolder findViewHolderForLayoutPosition(final int n) {
        return this.findViewHolderForPosition(n, false);
    }
    
    @Deprecated
    public ViewHolder findViewHolderForPosition(final int n) {
        return this.findViewHolderForPosition(n, false);
    }
    
    ViewHolder findViewHolderForPosition(final int n, final boolean b) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved()) {
                ViewHolder viewHolder;
                if (b) {
                    if (childViewHolderInt.mPosition != n) {
                        continue;
                    }
                    viewHolder = childViewHolderInt;
                }
                else {
                    viewHolder = childViewHolderInt;
                    if (childViewHolderInt.getLayoutPosition() != n) {
                        continue;
                    }
                }
                return viewHolder;
            }
        }
        return null;
    }
    
    public boolean fling(int max, int max2) {
        final boolean b = false;
        boolean b2;
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            b2 = b;
        }
        else {
            b2 = b;
            if (!this.mLayoutFrozen) {
                final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
                final boolean canScrollVertically = this.mLayout.canScrollVertically();
                int a = 0;
                Label_0074: {
                    if (canScrollHorizontally) {
                        a = max;
                        if (Math.abs(max) >= this.mMinFlingVelocity) {
                            break Label_0074;
                        }
                    }
                    a = 0;
                }
                Label_0094: {
                    if (canScrollVertically) {
                        max = max2;
                        if (Math.abs(max2) >= this.mMinFlingVelocity) {
                            break Label_0094;
                        }
                    }
                    max = 0;
                }
                if (a == 0) {
                    b2 = b;
                    if (max == 0) {
                        return b2;
                    }
                }
                b2 = b;
                if (!this.dispatchNestedPreFling((float)a, (float)max)) {
                    final boolean b3 = canScrollHorizontally || canScrollVertically;
                    this.dispatchNestedFling((float)a, (float)max, b3);
                    b2 = b;
                    if (b3) {
                        max2 = Math.max(-this.mMaxFlingVelocity, Math.min(a, this.mMaxFlingVelocity));
                        max = Math.max(-this.mMaxFlingVelocity, Math.min(max, this.mMaxFlingVelocity));
                        this.mViewFlinger.fling(max2, max);
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    public View focusSearch(View focusSearch, final int n) {
        final View onInterceptFocusSearch = this.mLayout.onInterceptFocusSearch(focusSearch, n);
        if (onInterceptFocusSearch != null) {
            focusSearch = onInterceptFocusSearch;
        }
        else {
            final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, focusSearch, n);
            View onFocusSearchFailed;
            if ((onFocusSearchFailed = nextFocus) == null) {
                onFocusSearchFailed = nextFocus;
                if (this.mAdapter != null) {
                    onFocusSearchFailed = nextFocus;
                    if (this.mLayout != null) {
                        onFocusSearchFailed = nextFocus;
                        if (!this.isComputingLayout()) {
                            onFocusSearchFailed = nextFocus;
                            if (!this.mLayoutFrozen) {
                                this.eatRequestLayout();
                                onFocusSearchFailed = this.mLayout.onFocusSearchFailed(focusSearch, n, this.mRecycler, this.mState);
                                this.resumeRequestLayout(false);
                            }
                        }
                    }
                }
            }
            if (onFocusSearchFailed != null) {
                focusSearch = onFocusSearchFailed;
            }
            else {
                focusSearch = super.focusSearch(focusSearch, n);
            }
        }
        return focusSearch;
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return (ViewGroup$LayoutParams)this.mLayout.generateDefaultLayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return (ViewGroup$LayoutParams)this.mLayout.generateLayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return (ViewGroup$LayoutParams)this.mLayout.generateLayoutParams(viewGroup$LayoutParams);
    }
    
    public Adapter getAdapter() {
        return this.mAdapter;
    }
    
    public int getBaseline() {
        int n;
        if (this.mLayout != null) {
            n = this.mLayout.getBaseline();
        }
        else {
            n = super.getBaseline();
        }
        return n;
    }
    
    long getChangedHolderKey(final ViewHolder viewHolder) {
        long itemId;
        if (this.mAdapter.hasStableIds()) {
            itemId = viewHolder.getItemId();
        }
        else {
            itemId = viewHolder.mPosition;
        }
        return itemId;
    }
    
    public int getChildAdapterPosition(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        int adapterPosition;
        if (childViewHolderInt != null) {
            adapterPosition = childViewHolderInt.getAdapterPosition();
        }
        else {
            adapterPosition = -1;
        }
        return adapterPosition;
    }
    
    protected int getChildDrawingOrder(int n, final int n2) {
        if (this.mChildDrawingOrderCallback == null) {
            n = super.getChildDrawingOrder(n, n2);
        }
        else {
            n = this.mChildDrawingOrderCallback.onGetChildDrawingOrder(n, n2);
        }
        return n;
    }
    
    public long getChildItemId(final View view) {
        long itemId;
        final long n = itemId = -1L;
        if (this.mAdapter != null) {
            if (!this.mAdapter.hasStableIds()) {
                itemId = n;
            }
            else {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
                itemId = n;
                if (childViewHolderInt != null) {
                    itemId = childViewHolderInt.getItemId();
                }
            }
        }
        return itemId;
    }
    
    public int getChildLayoutPosition(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        int layoutPosition;
        if (childViewHolderInt != null) {
            layoutPosition = childViewHolderInt.getLayoutPosition();
        }
        else {
            layoutPosition = -1;
        }
        return layoutPosition;
    }
    
    @Deprecated
    public int getChildPosition(final View view) {
        return this.getChildAdapterPosition(view);
    }
    
    public ViewHolder getChildViewHolder(final View obj) {
        final ViewParent parent = obj.getParent();
        if (parent != null && parent != this) {
            throw new IllegalArgumentException("View " + obj + " is not a direct child of " + this);
        }
        return getChildViewHolderInt(obj);
    }
    
    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }
    
    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }
    
    Rect getItemDecorInsetsForChild(final View view) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect mDecorInsets;
        if (!layoutParams.mInsetsDirty) {
            mDecorInsets = layoutParams.mDecorInsets;
        }
        else {
            final Rect mDecorInsets2 = layoutParams.mDecorInsets;
            mDecorInsets2.set(0, 0, 0, 0);
            for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
                this.mTempRect.set(0, 0, 0, 0);
                this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, view, this, this.mState);
                mDecorInsets2.left += this.mTempRect.left;
                mDecorInsets2.top += this.mTempRect.top;
                mDecorInsets2.right += this.mTempRect.right;
                mDecorInsets2.bottom += this.mTempRect.bottom;
            }
            layoutParams.mInsetsDirty = false;
            mDecorInsets = mDecorInsets2;
        }
        return mDecorInsets;
    }
    
    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }
    
    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }
    
    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }
    
    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }
    
    public int getScrollState() {
        return this.mScrollState;
    }
    
    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }
    
    public boolean hasNestedScrollingParent() {
        return this.mScrollingChildHelper.hasNestedScrollingParent();
    }
    
    public boolean hasPendingAdapterUpdates() {
        return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
    }
    
    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper((AdapterHelper.Callback)new AdapterHelper.Callback() {
            void dispatchUpdate(final UpdateOp updateOp) {
                switch (updateOp.cmd) {
                    case 1: {
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                        break;
                    }
                    case 2: {
                        RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount);
                        break;
                    }
                    case 4: {
                        RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                        break;
                    }
                    case 8: {
                        RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, updateOp.positionStart, updateOp.itemCount, 1);
                        break;
                    }
                }
            }
            
            @Override
            public ViewHolder findViewHolder(final int n) {
                final ViewHolder viewHolderForPosition = RecyclerView.this.findViewHolderForPosition(n, true);
                ViewHolder viewHolder;
                if (viewHolderForPosition == null) {
                    viewHolder = null;
                }
                else {
                    viewHolder = viewHolderForPosition;
                    if (RecyclerView.this.mChildHelper.isHidden(viewHolderForPosition.itemView)) {
                        viewHolder = null;
                    }
                }
                return viewHolder;
            }
            
            @Override
            public void markViewHoldersUpdated(final int n, final int n2, final Object o) {
                RecyclerView.this.viewRangeUpdate(n, n2, o);
                RecyclerView.this.mItemsChanged = true;
            }
            
            @Override
            public void offsetPositionsForAdd(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForInsert(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void offsetPositionsForMove(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForMove(n, n2);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void offsetPositionsForRemovingInvisible(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, true);
                RecyclerView.this.mItemsAddedOrRemoved = true;
                State.access$1712(RecyclerView.this.mState, n2);
            }
            
            @Override
            public void offsetPositionsForRemovingLaidOutOrNewView(final int n, final int n2) {
                RecyclerView.this.offsetPositionRecordsForRemove(n, n2, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void onDispatchFirstPass(final UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
            
            @Override
            public void onDispatchSecondPass(final UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
        });
    }
    
    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }
    
    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() != 0) {
            if (this.mLayout != null) {
                this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
            }
            this.markItemDecorInsetsDirty();
            this.requestLayout();
        }
    }
    
    boolean isAccessibilityEnabled() {
        return this.mAccessibilityManager != null && this.mAccessibilityManager.isEnabled();
    }
    
    public boolean isAnimating() {
        return this.mItemAnimator != null && this.mItemAnimator.isRunning();
    }
    
    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }
    
    public boolean isComputingLayout() {
        return this.mLayoutOrScrollCounter > 0;
    }
    
    public boolean isLayoutFrozen() {
        return this.mLayoutFrozen;
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.mScrollingChildHelper.isNestedScrollingEnabled();
    }
    
    void markItemDecorInsetsDirty() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }
    
    void markKnownViewsInvalid() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.addFlags(6);
            }
        }
        this.markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }
    
    public void offsetChildrenHorizontal(final int n) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(n);
        }
    }
    
    public void offsetChildrenVertical(final int n) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(n);
        }
    }
    
    void offsetPositionRecordsForInsert(final int n, final int n2) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= n) {
                childViewHolderInt.offsetPosition(n2, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(n, n2);
        this.requestLayout();
    }
    
    void offsetPositionRecordsForMove(final int n, final int n2) {
        final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        int n3;
        int n4;
        int n5;
        if (n < n2) {
            n3 = n;
            n4 = n2;
            n5 = -1;
        }
        else {
            n3 = n2;
            n4 = n;
            n5 = 1;
        }
        for (int i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && childViewHolderInt.mPosition >= n3 && childViewHolderInt.mPosition <= n4) {
                if (childViewHolderInt.mPosition == n) {
                    childViewHolderInt.offsetPosition(n2 - n, false);
                }
                else {
                    childViewHolderInt.offsetPosition(n5, false);
                }
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(n, n2);
        this.requestLayout();
    }
    
    void offsetPositionRecordsForRemove(final int n, final int n2, final boolean b) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                if (childViewHolderInt.mPosition >= n + n2) {
                    childViewHolderInt.offsetPosition(-n2, b);
                    this.mState.mStructureChanged = true;
                }
                else if (childViewHolderInt.mPosition >= n) {
                    childViewHolderInt.flagRemovedAndOffsetPosition(n - 1, -n2, b);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(n, n2, b);
        this.requestLayout();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        this.mIsAttached = true;
        this.mFirstLayoutComplete = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
    }
    
    public void onChildAttachedToWindow(final View view) {
    }
    
    public void onChildDetachedFromWindow(final View view) {
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        this.mFirstLayoutComplete = false;
        this.stopScroll();
        this.mIsAttached = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mItemDecorations.get(i).onDraw(canvas, this, this.mState);
        }
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        if (this.mLayout != null && !this.mLayoutFrozen && (MotionEventCompat.getSource(motionEvent) & 0x2) != 0x0 && motionEvent.getAction() == 8) {
            float n;
            if (this.mLayout.canScrollVertically()) {
                n = -MotionEventCompat.getAxisValue(motionEvent, 9);
            }
            else {
                n = 0.0f;
            }
            float axisValue;
            if (this.mLayout.canScrollHorizontally()) {
                axisValue = MotionEventCompat.getAxisValue(motionEvent, 10);
            }
            else {
                axisValue = 0.0f;
            }
            if (n != 0.0f || axisValue != 0.0f) {
                final float scrollFactor = this.getScrollFactor();
                this.scrollByInternal((int)(axisValue * scrollFactor), (int)(n * scrollFactor), motionEvent);
            }
        }
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        boolean b;
        if (this.mLayoutFrozen) {
            b = false;
        }
        else if (this.dispatchOnItemTouchIntercept(motionEvent)) {
            this.cancelTouch();
            b = true;
        }
        else if (this.mLayout == null) {
            b = false;
        }
        else {
            final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
            final boolean canScrollVertically = this.mLayout.canScrollVertically();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
            final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
            switch (actionMasked) {
                case 0: {
                    if (this.mIgnoreMotionEventTillDown) {
                        this.mIgnoreMotionEventTillDown = false;
                    }
                    this.mScrollPointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                    final int n = (int)(motionEvent.getX() + 0.5f);
                    this.mLastTouchX = n;
                    this.mInitialTouchX = n;
                    final int n2 = (int)(motionEvent.getY() + 0.5f);
                    this.mLastTouchY = n2;
                    this.mInitialTouchY = n2;
                    if (this.mScrollState == 2) {
                        this.getParent().requestDisallowInterceptTouchEvent(true);
                        this.setScrollState(1);
                    }
                    this.mNestedOffsets[this.mNestedOffsets[1] = 0] = 0;
                    int n3 = 0;
                    if (canScrollHorizontally) {
                        n3 = ((false | true) ? 1 : 0);
                    }
                    int n4 = n3;
                    if (canScrollVertically) {
                        n4 = (n3 | 0x2);
                    }
                    this.startNestedScroll(n4);
                    break;
                }
                case 5: {
                    this.mScrollPointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                    final int n5 = (int)(MotionEventCompat.getX(motionEvent, actionIndex) + 0.5f);
                    this.mLastTouchX = n5;
                    this.mInitialTouchX = n5;
                    final int n6 = (int)(MotionEventCompat.getY(motionEvent, actionIndex) + 0.5f);
                    this.mLastTouchY = n6;
                    this.mInitialTouchY = n6;
                    break;
                }
                case 2: {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mScrollPointerId);
                    if (pointerIndex < 0) {
                        Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                        b = false;
                        return b;
                    }
                    final int n7 = (int)(MotionEventCompat.getX(motionEvent, pointerIndex) + 0.5f);
                    final int n8 = (int)(MotionEventCompat.getY(motionEvent, pointerIndex) + 0.5f);
                    if (this.mScrollState == 1) {
                        break;
                    }
                    final int a = n7 - this.mInitialTouchX;
                    final int a2 = n8 - this.mInitialTouchY;
                    int n10;
                    final int n9 = n10 = 0;
                    if (canScrollHorizontally) {
                        n10 = n9;
                        if (Math.abs(a) > this.mTouchSlop) {
                            final int mInitialTouchX = this.mInitialTouchX;
                            final int mTouchSlop = this.mTouchSlop;
                            int n11;
                            if (a < 0) {
                                n11 = -1;
                            }
                            else {
                                n11 = 1;
                            }
                            this.mLastTouchX = n11 * mTouchSlop + mInitialTouchX;
                            n10 = 1;
                        }
                    }
                    int n12 = n10;
                    if (canScrollVertically) {
                        n12 = n10;
                        if (Math.abs(a2) > this.mTouchSlop) {
                            final int mInitialTouchY = this.mInitialTouchY;
                            final int mTouchSlop2 = this.mTouchSlop;
                            int n13;
                            if (a2 < 0) {
                                n13 = -1;
                            }
                            else {
                                n13 = 1;
                            }
                            this.mLastTouchY = n13 * mTouchSlop2 + mInitialTouchY;
                            n12 = 1;
                        }
                    }
                    if (n12 != 0) {
                        this.setScrollState(1);
                        break;
                    }
                    break;
                }
                case 6: {
                    this.onPointerUp(motionEvent);
                    break;
                }
                case 1: {
                    this.mVelocityTracker.clear();
                    this.stopNestedScroll();
                    break;
                }
                case 3: {
                    this.cancelTouch();
                    break;
                }
            }
            b = (this.mScrollState == 1);
        }
        return b;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        TraceCompat.beginSection("RV OnLayout");
        this.dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int n3 = 0;
        if (this.mLayout == null) {
            this.defaultOnMeasure(n, n2);
        }
        else if (this.mLayout.mAutoMeasure) {
            final int mode = View$MeasureSpec.getMode(n);
            final int mode2 = View$MeasureSpec.getMode(n2);
            int n4 = n3;
            if (mode == 1073741824) {
                n4 = n3;
                if (mode2 == 1073741824) {
                    n4 = 1;
                }
            }
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            if (n4 == 0 && this.mAdapter != null) {
                if (this.mState.mLayoutStep == 1) {
                    this.dispatchLayoutStep1();
                }
                this.mLayout.setMeasureSpecs(n, n2);
                this.mState.mIsMeasuring = true;
                this.dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(n, n2);
                if (this.mLayout.shouldMeasureTwice()) {
                    this.mLayout.setMeasureSpecs(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
                    this.mState.mIsMeasuring = true;
                    this.dispatchLayoutStep2();
                    this.mLayout.setMeasuredDimensionFromChildren(n, n2);
                }
            }
        }
        else if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
        }
        else {
            if (this.mAdapterUpdateDuringMeasure) {
                this.eatRequestLayout();
                this.processAdapterUpdatesAndSetAnimationFlags();
                if (this.mState.mRunPredictiveAnimations) {
                    this.mState.mInPreLayout = true;
                }
                else {
                    this.mAdapterHelper.consumeUpdatesInOnePass();
                    this.mState.mInPreLayout = false;
                }
                this.resumeRequestLayout(this.mAdapterUpdateDuringMeasure = false);
            }
            if (this.mAdapter != null) {
                this.mState.mItemCount = this.mAdapter.getItemCount();
            }
            else {
                this.mState.mItemCount = 0;
            }
            this.eatRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            this.resumeRequestLayout(false);
            this.mState.mInPreLayout = false;
        }
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        this.mPendingSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState != null) {
            savedState.copyFrom(this.mPendingSavedState);
        }
        else if (this.mLayout != null) {
            savedState.mLayoutState = this.mLayout.onSaveInstanceState();
        }
        else {
            savedState.mLayoutState = null;
        }
        return (Parcelable)savedState;
    }
    
    public void onScrollStateChanged(final int n) {
    }
    
    public void onScrolled(final int n, final int n2) {
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3 || n2 != n4) {
            this.invalidateGlows();
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        boolean b;
        if (this.mLayoutFrozen || this.mIgnoreMotionEventTillDown) {
            b = false;
        }
        else if (this.dispatchOnItemTouch(motionEvent)) {
            this.cancelTouch();
            b = true;
        }
        else if (this.mLayout == null) {
            b = false;
        }
        else {
            final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
            final boolean canScrollVertically = this.mLayout.canScrollVertically();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            final int n = 0;
            final MotionEvent obtain = MotionEvent.obtain(motionEvent);
            final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
            final int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
            if (actionMasked == 0) {
                this.mNestedOffsets[this.mNestedOffsets[1] = 0] = 0;
            }
            obtain.offsetLocation((float)this.mNestedOffsets[0], (float)this.mNestedOffsets[1]);
            int n2 = n;
            while (true) {
                switch (actionMasked) {
                    default: {
                        n2 = n;
                        break Label_0192;
                    }
                    case 3: {
                        this.cancelTouch();
                        n2 = n;
                        break Label_0192;
                    }
                    case 1: {
                        this.mVelocityTracker.addMovement(obtain);
                        n2 = 1;
                        this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                        float n3;
                        if (canScrollHorizontally) {
                            n3 = -VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mScrollPointerId);
                        }
                        else {
                            n3 = 0.0f;
                        }
                        float n4;
                        if (canScrollVertically) {
                            n4 = -VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mScrollPointerId);
                        }
                        else {
                            n4 = 0.0f;
                        }
                        if ((n3 == 0.0f && n4 == 0.0f) || !this.fling((int)n3, (int)n4)) {
                            this.setScrollState(0);
                        }
                        this.resetTouch();
                        break Label_0192;
                    }
                    case 6: {
                        this.onPointerUp(motionEvent);
                        n2 = n;
                        break Label_0192;
                    }
                    case 5: {
                        this.mScrollPointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                        final int n5 = (int)(MotionEventCompat.getX(motionEvent, actionIndex) + 0.5f);
                        this.mLastTouchX = n5;
                        this.mInitialTouchX = n5;
                        final int n6 = (int)(MotionEventCompat.getY(motionEvent, actionIndex) + 0.5f);
                        this.mLastTouchY = n6;
                        this.mInitialTouchY = n6;
                        n2 = n;
                        break Label_0192;
                    }
                    case 0: {
                        this.mScrollPointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                        final int n7 = (int)(motionEvent.getX() + 0.5f);
                        this.mLastTouchX = n7;
                        this.mInitialTouchX = n7;
                        final int n8 = (int)(motionEvent.getY() + 0.5f);
                        this.mLastTouchY = n8;
                        this.mInitialTouchY = n8;
                        int n9 = 0;
                        if (canScrollHorizontally) {
                            n9 = ((false | true) ? 1 : 0);
                        }
                        int n10 = n9;
                        if (canScrollVertically) {
                            n10 = (n9 | 0x2);
                        }
                        this.startNestedScroll(n10);
                        n2 = n;
                    }
                    case 4: {
                        if (n2 == 0) {
                            this.mVelocityTracker.addMovement(obtain);
                        }
                        obtain.recycle();
                        b = true;
                        break;
                    }
                    case 2: {
                        final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mScrollPointerId);
                        if (pointerIndex < 0) {
                            Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                            b = false;
                            break;
                        }
                        final int n11 = (int)(MotionEventCompat.getX(motionEvent, pointerIndex) + 0.5f);
                        final int n12 = (int)(MotionEventCompat.getY(motionEvent, pointerIndex) + 0.5f);
                        final int n13 = this.mLastTouchX - n11;
                        final int n14 = this.mLastTouchY - n12;
                        int a = n13;
                        int a2 = n14;
                        if (this.dispatchNestedPreScroll(n13, n14, this.mScrollConsumed, this.mScrollOffset)) {
                            a = n13 - this.mScrollConsumed[0];
                            a2 = n14 - this.mScrollConsumed[1];
                            obtain.offsetLocation((float)this.mScrollOffset[0], (float)this.mScrollOffset[1]);
                            final int[] mNestedOffsets = this.mNestedOffsets;
                            mNestedOffsets[0] += this.mScrollOffset[0];
                            final int[] mNestedOffsets2 = this.mNestedOffsets;
                            mNestedOffsets2[1] += this.mScrollOffset[1];
                        }
                        int n15 = a;
                        int n16 = a2;
                        if (this.mScrollState != 1) {
                            final int n17 = 0;
                            int n18 = a;
                            int n19 = n17;
                            if (canScrollHorizontally) {
                                n18 = a;
                                n19 = n17;
                                if (Math.abs(a) > this.mTouchSlop) {
                                    if (a > 0) {
                                        n18 = a - this.mTouchSlop;
                                    }
                                    else {
                                        n18 = a + this.mTouchSlop;
                                    }
                                    n19 = 1;
                                }
                            }
                            int n20 = a2;
                            int n21 = n19;
                            if (canScrollVertically) {
                                n20 = a2;
                                n21 = n19;
                                if (Math.abs(a2) > this.mTouchSlop) {
                                    if (a2 > 0) {
                                        n20 = a2 - this.mTouchSlop;
                                    }
                                    else {
                                        n20 = a2 + this.mTouchSlop;
                                    }
                                    n21 = 1;
                                }
                            }
                            n15 = n18;
                            n16 = n20;
                            if (n21 != 0) {
                                this.setScrollState(1);
                                n16 = n20;
                                n15 = n18;
                            }
                        }
                        n2 = n;
                        if (this.mScrollState != 1) {
                            continue;
                        }
                        this.mLastTouchX = n11 - this.mScrollOffset[0];
                        this.mLastTouchY = n12 - this.mScrollOffset[1];
                        if (!canScrollHorizontally) {
                            n15 = 0;
                        }
                        if (!canScrollVertically) {
                            n16 = 0;
                        }
                        n2 = n;
                        if (this.scrollByInternal(n15, n16, obtain)) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                            n2 = n;
                        }
                        continue;
                    }
                }
                break;
            }
        }
        return b;
    }
    
    protected void removeDetachedView(final View view, final boolean b) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            if (childViewHolderInt.isTmpDetached()) {
                childViewHolderInt.clearTmpDetachFlag();
            }
            else if (!childViewHolderInt.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + childViewHolderInt);
            }
        }
        this.dispatchChildDetached(view);
        super.removeDetachedView(view, b);
    }
    
    public void removeItemDecoration(final ItemDecoration o) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(o);
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(ViewCompat.getOverScrollMode((View)this) == 2);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    public void removeOnChildAttachStateChangeListener(final OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners != null) {
            this.mOnChildAttachStateListeners.remove(onChildAttachStateChangeListener);
        }
    }
    
    public void removeOnItemTouchListener(final OnItemTouchListener o) {
        this.mOnItemTouchListeners.remove(o);
        if (this.mActiveOnItemTouchListener == o) {
            this.mActiveOnItemTouchListener = null;
        }
    }
    
    public void removeOnScrollListener(final OnScrollListener onScrollListener) {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.remove(onScrollListener);
        }
    }
    
    public void requestChildFocus(final View view, final View view2) {
        boolean b = false;
        if (!this.mLayout.onRequestChildFocus(this, this.mState, view, view2) && view2 != null) {
            this.mTempRect.set(0, 0, view2.getWidth(), view2.getHeight());
            final ViewGroup$LayoutParams layoutParams = view2.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
                if (!layoutParams2.mInsetsDirty) {
                    final Rect mDecorInsets = layoutParams2.mDecorInsets;
                    final Rect mTempRect = this.mTempRect;
                    mTempRect.left -= mDecorInsets.left;
                    final Rect mTempRect2 = this.mTempRect;
                    mTempRect2.right += mDecorInsets.right;
                    final Rect mTempRect3 = this.mTempRect;
                    mTempRect3.top -= mDecorInsets.top;
                    final Rect mTempRect4 = this.mTempRect;
                    mTempRect4.bottom += mDecorInsets.bottom;
                }
            }
            this.offsetDescendantRectToMyCoords(view2, this.mTempRect);
            this.offsetRectIntoDescendantCoords(view, this.mTempRect);
            final Rect mTempRect5 = this.mTempRect;
            if (!this.mFirstLayoutComplete) {
                b = true;
            }
            this.requestChildRectangleOnScreen(view, mTempRect5, b);
        }
        super.requestChildFocus(view, view2);
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        return this.mLayout.requestChildRectangleOnScreen(this, view, rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(b);
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        if (this.mEatRequestLayout == 0 && !this.mLayoutFrozen) {
            super.requestLayout();
        }
        else {
            this.mLayoutRequestEaten = true;
        }
    }
    
    void resumeRequestLayout(final boolean b) {
        if (this.mEatRequestLayout < 1) {
            this.mEatRequestLayout = 1;
        }
        if (!b) {
            this.mLayoutRequestEaten = false;
        }
        if (this.mEatRequestLayout == 1) {
            if (b && this.mLayoutRequestEaten && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null) {
                this.dispatchLayout();
            }
            if (!this.mLayoutFrozen) {
                this.mLayoutRequestEaten = false;
            }
        }
        --this.mEatRequestLayout;
    }
    
    void saveOldPositions() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.saveOldPosition();
            }
        }
    }
    
    public void scrollBy(int n, int n2) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        }
        else if (!this.mLayoutFrozen) {
            final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
            final boolean canScrollVertically = this.mLayout.canScrollVertically();
            if (canScrollHorizontally || canScrollVertically) {
                if (!canScrollHorizontally) {
                    n = 0;
                }
                if (!canScrollVertically) {
                    n2 = 0;
                }
                this.scrollByInternal(n, n2, null);
            }
        }
    }
    
    boolean scrollByInternal(final int n, final int n2, final MotionEvent motionEvent) {
        int n3 = 0;
        final int n4 = 0;
        int n5 = 0;
        final int n6 = 0;
        int scrollHorizontallyBy = 0;
        final int n7 = 0;
        int scrollVerticallyBy = 0;
        final int n8 = 0;
        this.consumePendingUpdateOperations();
        if (this.mAdapter != null) {
            this.eatRequestLayout();
            this.onEnterLayoutOrScroll();
            TraceCompat.beginSection("RV Scroll");
            scrollHorizontallyBy = n7;
            n3 = n4;
            if (n != 0) {
                scrollHorizontallyBy = this.mLayout.scrollHorizontallyBy(n, this.mRecycler, this.mState);
                n3 = n - scrollHorizontallyBy;
            }
            scrollVerticallyBy = n8;
            n5 = n6;
            if (n2 != 0) {
                scrollVerticallyBy = this.mLayout.scrollVerticallyBy(n2, this.mRecycler, this.mState);
                n5 = n2 - scrollVerticallyBy;
            }
            TraceCompat.endSection();
            this.repositionShadowingViews();
            this.onExitLayoutOrScroll();
            this.resumeRequestLayout(false);
        }
        if (!this.mItemDecorations.isEmpty()) {
            this.invalidate();
        }
        if (this.dispatchNestedScroll(scrollHorizontallyBy, scrollVerticallyBy, n3, n5, this.mScrollOffset)) {
            this.mLastTouchX -= this.mScrollOffset[0];
            this.mLastTouchY -= this.mScrollOffset[1];
            if (motionEvent != null) {
                motionEvent.offsetLocation((float)this.mScrollOffset[0], (float)this.mScrollOffset[1]);
            }
            final int[] mNestedOffsets = this.mNestedOffsets;
            mNestedOffsets[0] += this.mScrollOffset[0];
            final int[] mNestedOffsets2 = this.mNestedOffsets;
            mNestedOffsets2[1] += this.mScrollOffset[1];
        }
        else if (ViewCompat.getOverScrollMode((View)this) != 2) {
            if (motionEvent != null) {
                this.pullGlows(motionEvent.getX(), (float)n3, motionEvent.getY(), (float)n5);
            }
            this.considerReleasingGlowsOnScroll(n, n2);
        }
        if (scrollHorizontallyBy != 0 || scrollVerticallyBy != 0) {
            this.dispatchOnScrolled(scrollHorizontallyBy, scrollVerticallyBy);
        }
        if (!this.awakenScrollBars()) {
            this.invalidate();
        }
        return scrollHorizontallyBy != 0 || scrollVerticallyBy != 0;
    }
    
    public void scrollTo(final int n, final int n2) {
        Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }
    
    public void scrollToPosition(final int n) {
        if (!this.mLayoutFrozen) {
            this.stopScroll();
            if (this.mLayout == null) {
                Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
            }
            else {
                this.mLayout.scrollToPosition(n);
                this.awakenScrollBars();
            }
        }
    }
    
    public void sendAccessibilityEventUnchecked(final AccessibilityEvent accessibilityEvent) {
        if (!this.shouldDeferAccessibilityEvent(accessibilityEvent)) {
            super.sendAccessibilityEventUnchecked(accessibilityEvent);
        }
    }
    
    public void setAccessibilityDelegateCompat(final RecyclerViewAccessibilityDelegate mAccessibilityDelegate) {
        ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate = mAccessibilityDelegate);
    }
    
    public void setAdapter(final Adapter adapter) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, false, true);
        this.requestLayout();
    }
    
    public void setChildDrawingOrderCallback(final ChildDrawingOrderCallback mChildDrawingOrderCallback) {
        if (mChildDrawingOrderCallback != this.mChildDrawingOrderCallback) {
            this.mChildDrawingOrderCallback = mChildDrawingOrderCallback;
            this.setChildrenDrawingOrderEnabled(this.mChildDrawingOrderCallback != null);
        }
    }
    
    public void setClipToPadding(final boolean mClipToPadding) {
        if (mClipToPadding != this.mClipToPadding) {
            this.invalidateGlows();
        }
        super.setClipToPadding(this.mClipToPadding = mClipToPadding);
        if (this.mFirstLayoutComplete) {
            this.requestLayout();
        }
    }
    
    public void setHasFixedSize(final boolean mHasFixedSize) {
        this.mHasFixedSize = mHasFixedSize;
    }
    
    public void setItemAnimator(final ItemAnimator mItemAnimator) {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = mItemAnimator;
        if (this.mItemAnimator != null) {
            this.mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }
    
    public void setItemViewCacheSize(final int viewCacheSize) {
        this.mRecycler.setViewCacheSize(viewCacheSize);
    }
    
    public void setLayoutFrozen(final boolean b) {
        if (b != this.mLayoutFrozen) {
            this.assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
            if (!b) {
                this.mLayoutFrozen = false;
                if (this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null) {
                    this.requestLayout();
                }
                this.mLayoutRequestEaten = false;
            }
            else {
                final long uptimeMillis = SystemClock.uptimeMillis();
                this.onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
                this.mLayoutFrozen = true;
                this.mIgnoreMotionEventTillDown = true;
                this.stopScroll();
            }
        }
    }
    
    public void setLayoutManager(final LayoutManager layoutManager) {
        if (layoutManager != this.mLayout) {
            this.stopScroll();
            if (this.mLayout != null) {
                if (this.mIsAttached) {
                    this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
                }
                this.mLayout.setRecyclerView(null);
            }
            this.mRecycler.clear();
            this.mChildHelper.removeAllViewsUnfiltered();
            if ((this.mLayout = layoutManager) != null) {
                if (layoutManager.mRecyclerView != null) {
                    throw new IllegalArgumentException("LayoutManager " + layoutManager + " is already attached to a RecyclerView: " + layoutManager.mRecyclerView);
                }
                this.mLayout.setRecyclerView(this);
                if (this.mIsAttached) {
                    this.mLayout.dispatchAttachedToWindow(this);
                }
            }
            this.requestLayout();
        }
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.mScrollingChildHelper.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    @Deprecated
    public void setOnScrollListener(final OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }
    
    public void setRecycledViewPool(final RecycledViewPool recycledViewPool) {
        this.mRecycler.setRecycledViewPool(recycledViewPool);
    }
    
    public void setRecyclerListener(final RecyclerListener mRecyclerListener) {
        this.mRecyclerListener = mRecyclerListener;
    }
    
    public void setScrollingTouchSlop(final int i) {
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        switch (i) {
            default: {
                Log.w("RecyclerView", "setScrollingTouchSlop(): bad argument constant " + i + "; using default value");
            }
            case 0: {
                this.mTouchSlop = value.getScaledTouchSlop();
                break;
            }
            case 1: {
                this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(value);
                break;
            }
        }
    }
    
    public void setViewCacheExtension(final ViewCacheExtension viewCacheExtension) {
        this.mRecycler.setViewCacheExtension(viewCacheExtension);
    }
    
    boolean shouldDeferAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        boolean b;
        if (this.isComputingLayout()) {
            int contentChangeTypes = 0;
            if (accessibilityEvent != null) {
                contentChangeTypes = AccessibilityEventCompat.getContentChangeTypes(accessibilityEvent);
            }
            int n;
            if ((n = contentChangeTypes) == 0) {
                n = 0;
            }
            this.mEatenAccessibilityChangeFlags |= n;
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public void smoothScrollBy(int n, int n2) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        }
        else if (!this.mLayoutFrozen) {
            if (!this.mLayout.canScrollHorizontally()) {
                n = 0;
            }
            if (!this.mLayout.canScrollVertically()) {
                n2 = 0;
            }
            if (n != 0 || n2 != 0) {
                this.mViewFlinger.smoothScrollBy(n, n2);
            }
        }
    }
    
    public void smoothScrollToPosition(final int n) {
        if (!this.mLayoutFrozen) {
            if (this.mLayout == null) {
                Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            }
            else {
                this.mLayout.smoothScrollToPosition(this, this.mState, n);
            }
        }
    }
    
    public boolean startNestedScroll(final int n) {
        return this.mScrollingChildHelper.startNestedScroll(n);
    }
    
    public void stopNestedScroll() {
        this.mScrollingChildHelper.stopNestedScroll();
    }
    
    public void stopScroll() {
        this.setScrollState(0);
        this.stopScrollersInternal();
    }
    
    public void swapAdapter(final Adapter adapter, final boolean b) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, true, b);
        this.setDataSetChangedAfterLayout();
        this.requestLayout();
    }
    
    void viewRangeUpdate(final int n, final int n2, final Object o) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final View unfilteredChild = this.mChildHelper.getUnfilteredChildAt(i);
            final ViewHolder childViewHolderInt = getChildViewHolderInt(unfilteredChild);
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= n && childViewHolderInt.mPosition < n + n2) {
                childViewHolderInt.addFlags(2);
                childViewHolderInt.addChangePayload(o);
                ((LayoutParams)unfilteredChild.getLayoutParams()).mInsetsDirty = true;
            }
        }
        this.mRecycler.viewRangeUpdate(n, n2);
    }
    
    public abstract static class Adapter<VH extends ViewHolder>
    {
        private boolean mHasStableIds;
        private final AdapterDataObservable mObservable;
        
        public Adapter() {
            this.mObservable = new AdapterDataObservable();
            this.mHasStableIds = false;
        }
        
        public final void bindViewHolder(final VH vh, final int mPosition) {
            vh.mPosition = mPosition;
            if (this.hasStableIds()) {
                vh.mItemId = this.getItemId(mPosition);
            }
            ((ViewHolder)vh).setFlags(1, 519);
            TraceCompat.beginSection("RV OnBindView");
            this.onBindViewHolder(vh, mPosition, ((ViewHolder)vh).getUnmodifiedPayloads());
            ((ViewHolder)vh).clearPayload();
            TraceCompat.endSection();
        }
        
        public final VH createViewHolder(final ViewGroup viewGroup, final int mItemViewType) {
            TraceCompat.beginSection("RV CreateView");
            final ViewHolder onCreateViewHolder = this.onCreateViewHolder(viewGroup, mItemViewType);
            onCreateViewHolder.mItemViewType = mItemViewType;
            TraceCompat.endSection();
            return (VH)onCreateViewHolder;
        }
        
        public abstract int getItemCount();
        
        public long getItemId(final int n) {
            return -1L;
        }
        
        public int getItemViewType(final int n) {
            return 0;
        }
        
        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }
        
        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }
        
        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }
        
        public final void notifyItemChanged(final int n) {
            this.mObservable.notifyItemRangeChanged(n, 1);
        }
        
        public final void notifyItemChanged(final int n, final Object o) {
            this.mObservable.notifyItemRangeChanged(n, 1, o);
        }
        
        public final void notifyItemInserted(final int n) {
            this.mObservable.notifyItemRangeInserted(n, 1);
        }
        
        public final void notifyItemMoved(final int n, final int n2) {
            this.mObservable.notifyItemMoved(n, n2);
        }
        
        public final void notifyItemRangeChanged(final int n, final int n2) {
            this.mObservable.notifyItemRangeChanged(n, n2);
        }
        
        public final void notifyItemRangeChanged(final int n, final int n2, final Object o) {
            this.mObservable.notifyItemRangeChanged(n, n2, o);
        }
        
        public final void notifyItemRangeInserted(final int n, final int n2) {
            this.mObservable.notifyItemRangeInserted(n, n2);
        }
        
        public final void notifyItemRangeRemoved(final int n, final int n2) {
            this.mObservable.notifyItemRangeRemoved(n, n2);
        }
        
        public final void notifyItemRemoved(final int n) {
            this.mObservable.notifyItemRangeRemoved(n, 1);
        }
        
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        }
        
        public abstract void onBindViewHolder(final VH p0, final int p1);
        
        public void onBindViewHolder(final VH vh, final int n, final List<Object> list) {
            this.onBindViewHolder(vh, n);
        }
        
        public abstract VH onCreateViewHolder(final ViewGroup p0, final int p1);
        
        public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        }
        
        public boolean onFailedToRecycleView(final VH vh) {
            return false;
        }
        
        public void onViewAttachedToWindow(final VH vh) {
        }
        
        public void onViewDetachedFromWindow(final VH vh) {
        }
        
        public void onViewRecycled(final VH vh) {
        }
        
        public void registerAdapterDataObserver(final AdapterDataObserver adapterDataObserver) {
            this.mObservable.registerObserver((Object)adapterDataObserver);
        }
        
        public void setHasStableIds(final boolean mHasStableIds) {
            if (this.hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            this.mHasStableIds = mHasStableIds;
        }
        
        public void unregisterAdapterDataObserver(final AdapterDataObserver adapterDataObserver) {
            this.mObservable.unregisterObserver((Object)adapterDataObserver);
        }
    }
    
    static class AdapterDataObservable extends Observable<AdapterDataObserver>
    {
        public boolean hasObservers() {
            return !this.mObservers.isEmpty();
        }
        
        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onChanged();
            }
        }
        
        public void notifyItemMoved(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(n, n2, 1);
            }
        }
        
        public void notifyItemRangeChanged(final int n, final int n2) {
            this.notifyItemRangeChanged(n, n2, null);
        }
        
        public void notifyItemRangeChanged(final int n, final int n2, final Object o) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(n, n2, o);
            }
        }
        
        public void notifyItemRangeInserted(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(n, n2);
            }
        }
        
        public void notifyItemRangeRemoved(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(n, n2);
            }
        }
    }
    
    public abstract static class AdapterDataObserver
    {
        public void onChanged() {
        }
        
        public void onItemRangeChanged(final int n, final int n2) {
        }
        
        public void onItemRangeChanged(final int n, final int n2, final Object o) {
            this.onItemRangeChanged(n, n2);
        }
        
        public void onItemRangeInserted(final int n, final int n2) {
        }
        
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
        }
        
        public void onItemRangeRemoved(final int n, final int n2) {
        }
    }
    
    public interface ChildDrawingOrderCallback
    {
        int onGetChildDrawingOrder(final int p0, final int p1);
    }
    
    public abstract static class ItemAnimator
    {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration;
        private long mChangeDuration;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners;
        private ItemAnimatorListener mListener;
        private long mMoveDuration;
        private long mRemoveDuration;
        
        public ItemAnimator() {
            this.mListener = null;
            this.mFinishedListeners = new ArrayList<ItemAnimatorFinishedListener>();
            this.mAddDuration = 120L;
            this.mRemoveDuration = 120L;
            this.mMoveDuration = 250L;
            this.mChangeDuration = 250L;
        }
        
        static int buildAdapterChangeFlagsForAnimations(final ViewHolder viewHolder) {
            final int n = viewHolder.mFlags & 0xE;
            int n2;
            if (viewHolder.isInvalid()) {
                n2 = 4;
            }
            else {
                n2 = n;
                if ((n & 0x4) == 0x0) {
                    final int oldPosition = viewHolder.getOldPosition();
                    final int adapterPosition = viewHolder.getAdapterPosition();
                    n2 = n;
                    if (oldPosition != -1) {
                        n2 = n;
                        if (adapterPosition != -1) {
                            n2 = n;
                            if (oldPosition != adapterPosition) {
                                n2 = (n | 0x800);
                            }
                        }
                    }
                }
            }
            return n2;
        }
        
        public abstract boolean animateAppearance(@NonNull final ViewHolder p0, @Nullable final ItemHolderInfo p1, @NonNull final ItemHolderInfo p2);
        
        public abstract boolean animateChange(@NonNull final ViewHolder p0, @NonNull final ViewHolder p1, @NonNull final ItemHolderInfo p2, @NonNull final ItemHolderInfo p3);
        
        public abstract boolean animateDisappearance(@NonNull final ViewHolder p0, @NonNull final ItemHolderInfo p1, @Nullable final ItemHolderInfo p2);
        
        public abstract boolean animatePersistence(@NonNull final ViewHolder p0, @NonNull final ItemHolderInfo p1, @NonNull final ItemHolderInfo p2);
        
        public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder) {
            return true;
        }
        
        public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder, @NonNull final List<Object> list) {
            return this.canReuseUpdatedViewHolder(viewHolder);
        }
        
        public final void dispatchAnimationFinished(final ViewHolder viewHolder) {
            this.onAnimationFinished(viewHolder);
            if (this.mListener != null) {
                this.mListener.onAnimationFinished(viewHolder);
            }
        }
        
        public final void dispatchAnimationStarted(final ViewHolder viewHolder) {
            this.onAnimationStarted(viewHolder);
        }
        
        public final void dispatchAnimationsFinished() {
            for (int size = this.mFinishedListeners.size(), i = 0; i < size; ++i) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }
        
        public abstract void endAnimation(final ViewHolder p0);
        
        public abstract void endAnimations();
        
        public long getAddDuration() {
            return this.mAddDuration;
        }
        
        public long getChangeDuration() {
            return this.mChangeDuration;
        }
        
        public long getMoveDuration() {
            return this.mMoveDuration;
        }
        
        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }
        
        public abstract boolean isRunning();
        
        public final boolean isRunning(final ItemAnimatorFinishedListener e) {
            final boolean running = this.isRunning();
            if (e != null) {
                if (!running) {
                    e.onAnimationsFinished();
                }
                else {
                    this.mFinishedListeners.add(e);
                }
            }
            return running;
        }
        
        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }
        
        public void onAnimationFinished(final ViewHolder viewHolder) {
        }
        
        public void onAnimationStarted(final ViewHolder viewHolder) {
        }
        
        @NonNull
        public ItemHolderInfo recordPostLayoutInformation(@NonNull final State state, @NonNull final ViewHolder from) {
            return this.obtainHolderInfo().setFrom(from);
        }
        
        @NonNull
        public ItemHolderInfo recordPreLayoutInformation(@NonNull final State state, @NonNull final ViewHolder from, final int n, @NonNull final List<Object> list) {
            return this.obtainHolderInfo().setFrom(from);
        }
        
        public abstract void runPendingAnimations();
        
        public void setAddDuration(final long mAddDuration) {
            this.mAddDuration = mAddDuration;
        }
        
        public void setChangeDuration(final long mChangeDuration) {
            this.mChangeDuration = mChangeDuration;
        }
        
        void setListener(final ItemAnimatorListener mListener) {
            this.mListener = mListener;
        }
        
        public void setMoveDuration(final long mMoveDuration) {
            this.mMoveDuration = mMoveDuration;
        }
        
        public void setRemoveDuration(final long mRemoveDuration) {
            this.mRemoveDuration = mRemoveDuration;
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges {
        }
        
        public interface ItemAnimatorFinishedListener
        {
            void onAnimationsFinished();
        }
        
        interface ItemAnimatorListener
        {
            void onAnimationFinished(final ViewHolder p0);
        }
        
        public static class ItemHolderInfo
        {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;
            
            public ItemHolderInfo setFrom(final ViewHolder viewHolder) {
                return this.setFrom(viewHolder, 0);
            }
            
            public ItemHolderInfo setFrom(final ViewHolder viewHolder, final int n) {
                final View itemView = viewHolder.itemView;
                this.left = itemView.getLeft();
                this.top = itemView.getTop();
                this.right = itemView.getRight();
                this.bottom = itemView.getBottom();
                return this;
            }
        }
    }
    
    private class ItemAnimatorRestoreListener implements ItemAnimatorListener
    {
        @Override
        public void onAnimationFinished(final ViewHolder viewHolder) {
            viewHolder.setIsRecyclable(true);
            if (viewHolder.mShadowedHolder != null && viewHolder.mShadowingHolder == null) {
                viewHolder.mShadowedHolder = null;
            }
            viewHolder.mShadowingHolder = null;
            if (!viewHolder.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(viewHolder.itemView) && viewHolder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
            }
        }
    }
    
    public abstract static class ItemDecoration
    {
        @Deprecated
        public void getItemOffsets(final Rect rect, final int n, final RecyclerView recyclerView) {
            rect.set(0, 0, 0, 0);
        }
        
        public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
            this.getItemOffsets(rect, ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition(), recyclerView);
        }
        
        @Deprecated
        public void onDraw(final Canvas canvas, final RecyclerView recyclerView) {
        }
        
        public void onDraw(final Canvas canvas, final RecyclerView recyclerView, final State state) {
            this.onDraw(canvas, recyclerView);
        }
        
        @Deprecated
        public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView) {
        }
        
        public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final State state) {
            this.onDrawOver(canvas, recyclerView);
        }
    }
    
    public abstract static class LayoutManager
    {
        private boolean mAutoMeasure;
        ChildHelper mChildHelper;
        private int mHeightSpec;
        boolean mIsAttachedToWindow;
        private boolean mMeasurementCacheEnabled;
        RecyclerView mRecyclerView;
        private boolean mRequestedSimpleAnimations;
        @Nullable
        SmoothScroller mSmoothScroller;
        private int mWidthSpec;
        
        public LayoutManager() {
            this.mRequestedSimpleAnimations = false;
            this.mIsAttachedToWindow = false;
            this.mAutoMeasure = false;
            this.mMeasurementCacheEnabled = true;
        }
        
        private void addViewInt(final View view, final int n, final boolean b) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (b || childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            }
            else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (childViewHolderInt.wasReturnedFromScrap() || childViewHolderInt.isScrap()) {
                if (childViewHolderInt.isScrap()) {
                    childViewHolderInt.unScrap();
                }
                else {
                    childViewHolderInt.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(view, n, view.getLayoutParams(), false);
            }
            else if (view.getParent() == this.mRecyclerView) {
                final int indexOfChild = this.mChildHelper.indexOfChild(view);
                int childCount;
                if ((childCount = n) == -1) {
                    childCount = this.mChildHelper.getChildCount();
                }
                if (indexOfChild == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(view));
                }
                if (indexOfChild != childCount) {
                    this.mRecyclerView.mLayout.moveView(indexOfChild, childCount);
                }
            }
            else {
                this.mChildHelper.addView(view, n, false);
                layoutParams.mInsetsDirty = true;
                if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                    this.mSmoothScroller.onChildAttachedToWindow(view);
                }
            }
            if (layoutParams.mPendingInvalidate) {
                childViewHolderInt.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
            }
        }
        
        public static int chooseSize(int n, final int n2, final int n3) {
            final int mode = View$MeasureSpec.getMode(n);
            final int a = n = View$MeasureSpec.getSize(n);
            switch (mode) {
                default: {
                    n = Math.max(n2, n3);
                    return n;
                }
                case Integer.MIN_VALUE: {
                    n = Math.min(a, Math.max(n2, n3));
                }
                case 1073741824: {
                    return n;
                }
            }
        }
        
        private void detachViewInternal(final int n, final View view) {
            this.mChildHelper.detachViewFromParent(n);
        }
        
        public static int getChildMeasureSpec(int n, final int n2, int n3, final int n4, final boolean b) {
            final int max = Math.max(0, n - n3);
            n3 = 0;
            n = 0;
            if (b) {
                if (n4 >= 0) {
                    n3 = n4;
                    n = 1073741824;
                }
                else if (n4 == -1) {
                    switch (n2) {
                        case Integer.MIN_VALUE:
                        case 1073741824: {
                            n3 = max;
                            n = n2;
                            break;
                        }
                        case 0: {
                            n3 = 0;
                            n = 0;
                            break;
                        }
                    }
                }
                else if (n4 == -2) {
                    n3 = 0;
                    n = 0;
                }
            }
            else if (n4 >= 0) {
                n3 = n4;
                n = 1073741824;
            }
            else if (n4 == -1) {
                n3 = max;
                n = n2;
            }
            else if (n4 == -2) {
                n3 = max;
                if (n2 == Integer.MIN_VALUE || n2 == 1073741824) {
                    n = Integer.MIN_VALUE;
                }
                else {
                    n = 0;
                }
            }
            return View$MeasureSpec.makeMeasureSpec(n3, n);
        }
        
        @Deprecated
        public static int getChildMeasureSpec(int n, int n2, final int n3, final boolean b) {
            final int max = Math.max(0, n - n2);
            n2 = 0;
            n = 0;
            if (b) {
                if (n3 >= 0) {
                    n2 = n3;
                    n = 1073741824;
                }
                else {
                    n2 = 0;
                    n = 0;
                }
            }
            else if (n3 >= 0) {
                n2 = n3;
                n = 1073741824;
            }
            else if (n3 == -1) {
                n2 = max;
                n = 1073741824;
            }
            else if (n3 == -2) {
                n2 = max;
                n = Integer.MIN_VALUE;
            }
            return View$MeasureSpec.makeMeasureSpec(n2, n);
        }
        
        public static Properties getProperties(final Context context, final AttributeSet set, final int n, final int n2) {
            final Properties properties = new Properties();
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.RecyclerView, n, n2);
            properties.orientation = obtainStyledAttributes.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = obtainStyledAttributes.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            obtainStyledAttributes.recycle();
            return properties;
        }
        
        private static boolean isMeasurementUpToDate(final int n, int size, final int n2) {
            final boolean b = true;
            final int mode = View$MeasureSpec.getMode(size);
            size = View$MeasureSpec.getSize(size);
            boolean b2;
            if (n2 > 0 && n != n2) {
                b2 = false;
            }
            else {
                b2 = b;
                switch (mode) {
                    case 0: {
                        break;
                    }
                    default: {
                        b2 = false;
                        break;
                    }
                    case Integer.MIN_VALUE: {
                        b2 = b;
                        if (size < n) {
                            b2 = false;
                            break;
                        }
                        break;
                    }
                    case 1073741824: {
                        b2 = b;
                        if (size != n) {
                            b2 = false;
                            break;
                        }
                        break;
                    }
                }
            }
            return b2;
        }
        
        private void onSmoothScrollerStopped(final SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }
        
        private void scrapOrRecycleView(final Recycler recycler, final int n, final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!childViewHolderInt.shouldIgnore()) {
                if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                    this.removeViewAt(n);
                    recycler.recycleViewHolderInternal(childViewHolderInt);
                }
                else {
                    this.detachViewAt(n);
                    recycler.scrapView(view);
                    this.mRecyclerView.mViewInfoStore.onViewDetached(childViewHolderInt);
                }
            }
        }
        
        public void addDisappearingView(final View view) {
            this.addDisappearingView(view, -1);
        }
        
        public void addDisappearingView(final View view, final int n) {
            this.addViewInt(view, n, true);
        }
        
        public void addView(final View view) {
            this.addView(view, -1);
        }
        
        public void addView(final View view, final int n) {
            this.addViewInt(view, n, false);
        }
        
        public void assertInLayoutOrScroll(final String s) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertInLayoutOrScroll(s);
            }
        }
        
        public void assertNotInLayoutOrScroll(final String s) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertNotInLayoutOrScroll(s);
            }
        }
        
        public void attachView(final View view) {
            this.attachView(view, -1);
        }
        
        public void attachView(final View view, final int n) {
            this.attachView(view, n, (LayoutParams)view.getLayoutParams());
        }
        
        public void attachView(final View view, final int n, final LayoutParams layoutParams) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            }
            else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            this.mChildHelper.attachViewToParent(view, n, (ViewGroup$LayoutParams)layoutParams, childViewHolderInt.isRemoved());
        }
        
        public void calculateItemDecorationsForChild(final View view, final Rect rect) {
            if (this.mRecyclerView == null) {
                rect.set(0, 0, 0, 0);
            }
            else {
                rect.set(this.mRecyclerView.getItemDecorInsetsForChild(view));
            }
        }
        
        public boolean canScrollHorizontally() {
            return false;
        }
        
        public boolean canScrollVertically() {
            return false;
        }
        
        public boolean checkLayoutParams(final LayoutParams layoutParams) {
            return layoutParams != null;
        }
        
        public int computeHorizontalScrollExtent(final State state) {
            return 0;
        }
        
        public int computeHorizontalScrollOffset(final State state) {
            return 0;
        }
        
        public int computeHorizontalScrollRange(final State state) {
            return 0;
        }
        
        public int computeVerticalScrollExtent(final State state) {
            return 0;
        }
        
        public int computeVerticalScrollOffset(final State state) {
            return 0;
        }
        
        public int computeVerticalScrollRange(final State state) {
            return 0;
        }
        
        public void detachAndScrapAttachedViews(final Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.scrapOrRecycleView(recycler, i, this.getChildAt(i));
            }
        }
        
        public void detachAndScrapView(final View view, final Recycler recycler) {
            this.scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(view), view);
        }
        
        public void detachAndScrapViewAt(final int n, final Recycler recycler) {
            this.scrapOrRecycleView(recycler, n, this.getChildAt(n));
        }
        
        public void detachView(final View view) {
            final int indexOfChild = this.mChildHelper.indexOfChild(view);
            if (indexOfChild >= 0) {
                this.detachViewInternal(indexOfChild, view);
            }
        }
        
        public void detachViewAt(final int n) {
            this.detachViewInternal(n, this.getChildAt(n));
        }
        
        void dispatchAttachedToWindow(final RecyclerView recyclerView) {
            this.mIsAttachedToWindow = true;
            this.onAttachedToWindow(recyclerView);
        }
        
        void dispatchDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
            this.mIsAttachedToWindow = false;
            this.onDetachedFromWindow(recyclerView, recycler);
        }
        
        public void endAnimation(final View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }
        
        @Nullable
        public View findContainingItemView(View view) {
            if (this.mRecyclerView == null) {
                view = null;
            }
            else {
                final View containingItemView = this.mRecyclerView.findContainingItemView(view);
                if (containingItemView == null) {
                    view = null;
                }
                else {
                    view = containingItemView;
                    if (this.mChildHelper.isHidden(containingItemView)) {
                        view = null;
                    }
                }
            }
            return view;
        }
        
        public View findViewByPosition(final int n) {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                if (childViewHolderInt != null && childViewHolderInt.getLayoutPosition() == n && !childViewHolderInt.shouldIgnore()) {
                    View view = child;
                    if (!this.mRecyclerView.mState.isPreLayout()) {
                        if (childViewHolderInt.isRemoved()) {
                            continue;
                        }
                        view = child;
                    }
                    return view;
                }
            }
            return null;
        }
        
        public abstract LayoutParams generateDefaultLayoutParams();
        
        public LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
            return new LayoutParams(context, set);
        }
        
        public LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            LayoutParams layoutParams;
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            else if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
                layoutParams = new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
            }
            else {
                layoutParams = new LayoutParams(viewGroup$LayoutParams);
            }
            return layoutParams;
        }
        
        public int getBaseline() {
            return -1;
        }
        
        public int getBottomDecorationHeight(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.bottom;
        }
        
        public View getChildAt(final int n) {
            View child;
            if (this.mChildHelper != null) {
                child = this.mChildHelper.getChildAt(n);
            }
            else {
                child = null;
            }
            return child;
        }
        
        public int getChildCount() {
            int childCount;
            if (this.mChildHelper != null) {
                childCount = this.mChildHelper.getChildCount();
            }
            else {
                childCount = 0;
            }
            return childCount;
        }
        
        public boolean getClipToPadding() {
            return this.mRecyclerView != null && this.mRecyclerView.mClipToPadding;
        }
        
        public int getColumnCountForAccessibility(final Recycler recycler, final State state) {
            int itemCount;
            final int n = itemCount = 1;
            if (this.mRecyclerView != null) {
                if (this.mRecyclerView.mAdapter == null) {
                    itemCount = n;
                }
                else {
                    itemCount = n;
                    if (this.canScrollHorizontally()) {
                        itemCount = this.mRecyclerView.mAdapter.getItemCount();
                    }
                }
            }
            return itemCount;
        }
        
        public int getDecoratedBottom(final View view) {
            return view.getBottom() + this.getBottomDecorationHeight(view);
        }
        
        public int getDecoratedLeft(final View view) {
            return view.getLeft() - this.getLeftDecorationWidth(view);
        }
        
        public int getDecoratedMeasuredHeight(final View view) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredHeight() + mDecorInsets.top + mDecorInsets.bottom;
        }
        
        public int getDecoratedMeasuredWidth(final View view) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredWidth() + mDecorInsets.left + mDecorInsets.right;
        }
        
        public int getDecoratedRight(final View view) {
            return view.getRight() + this.getRightDecorationWidth(view);
        }
        
        public int getDecoratedTop(final View view) {
            return view.getTop() - this.getTopDecorationHeight(view);
        }
        
        public View getFocusedChild() {
            View view;
            if (this.mRecyclerView == null) {
                view = null;
            }
            else {
                final View focusedChild = this.mRecyclerView.getFocusedChild();
                if (focusedChild != null) {
                    view = focusedChild;
                    if (!this.mChildHelper.isHidden(focusedChild)) {
                        return view;
                    }
                }
                view = null;
            }
            return view;
        }
        
        public int getHeight() {
            return View$MeasureSpec.getSize(this.mHeightSpec);
        }
        
        public int getHeightMode() {
            return View$MeasureSpec.getMode(this.mHeightSpec);
        }
        
        public int getItemCount() {
            Adapter adapter;
            if (this.mRecyclerView != null) {
                adapter = this.mRecyclerView.getAdapter();
            }
            else {
                adapter = null;
            }
            int itemCount;
            if (adapter != null) {
                itemCount = adapter.getItemCount();
            }
            else {
                itemCount = 0;
            }
            return itemCount;
        }
        
        public int getItemViewType(final View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }
        
        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
        }
        
        public int getLeftDecorationWidth(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.left;
        }
        
        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
        }
        
        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
        }
        
        public int getPaddingBottom() {
            int paddingBottom;
            if (this.mRecyclerView != null) {
                paddingBottom = this.mRecyclerView.getPaddingBottom();
            }
            else {
                paddingBottom = 0;
            }
            return paddingBottom;
        }
        
        public int getPaddingEnd() {
            int paddingEnd;
            if (this.mRecyclerView != null) {
                paddingEnd = ViewCompat.getPaddingEnd((View)this.mRecyclerView);
            }
            else {
                paddingEnd = 0;
            }
            return paddingEnd;
        }
        
        public int getPaddingLeft() {
            int paddingLeft;
            if (this.mRecyclerView != null) {
                paddingLeft = this.mRecyclerView.getPaddingLeft();
            }
            else {
                paddingLeft = 0;
            }
            return paddingLeft;
        }
        
        public int getPaddingRight() {
            int paddingRight;
            if (this.mRecyclerView != null) {
                paddingRight = this.mRecyclerView.getPaddingRight();
            }
            else {
                paddingRight = 0;
            }
            return paddingRight;
        }
        
        public int getPaddingStart() {
            int paddingStart;
            if (this.mRecyclerView != null) {
                paddingStart = ViewCompat.getPaddingStart((View)this.mRecyclerView);
            }
            else {
                paddingStart = 0;
            }
            return paddingStart;
        }
        
        public int getPaddingTop() {
            int paddingTop;
            if (this.mRecyclerView != null) {
                paddingTop = this.mRecyclerView.getPaddingTop();
            }
            else {
                paddingTop = 0;
            }
            return paddingTop;
        }
        
        public int getPosition(final View view) {
            return ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }
        
        public int getRightDecorationWidth(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.right;
        }
        
        public int getRowCountForAccessibility(final Recycler recycler, final State state) {
            int itemCount;
            final int n = itemCount = 1;
            if (this.mRecyclerView != null) {
                if (this.mRecyclerView.mAdapter == null) {
                    itemCount = n;
                }
                else {
                    itemCount = n;
                    if (this.canScrollVertically()) {
                        itemCount = this.mRecyclerView.mAdapter.getItemCount();
                    }
                }
            }
            return itemCount;
        }
        
        public int getSelectionModeForAccessibility(final Recycler recycler, final State state) {
            return 0;
        }
        
        public int getTopDecorationHeight(final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.top;
        }
        
        public int getWidth() {
            return View$MeasureSpec.getSize(this.mWidthSpec);
        }
        
        public int getWidthMode() {
            return View$MeasureSpec.getMode(this.mWidthSpec);
        }
        
        boolean hasFlexibleChildInBothOrientations() {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final ViewGroup$LayoutParams layoutParams = this.getChildAt(i).getLayoutParams();
                if (layoutParams.width < 0 && layoutParams.height < 0) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean hasFocus() {
            return this.mRecyclerView != null && this.mRecyclerView.hasFocus();
        }
        
        public void ignoreView(final View view) {
            if (view.getParent() != this.mRecyclerView || this.mRecyclerView.indexOfChild(view) == -1) {
                throw new IllegalArgumentException("View should be fully attached to be ignored");
            }
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder(childViewHolderInt);
        }
        
        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }
        
        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }
        
        public boolean isFocused() {
            return this.mRecyclerView != null && this.mRecyclerView.isFocused();
        }
        
        public boolean isLayoutHierarchical(final Recycler recycler, final State state) {
            return false;
        }
        
        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }
        
        public boolean isSmoothScrolling() {
            return this.mSmoothScroller != null && this.mSmoothScroller.isRunning();
        }
        
        public void layoutDecorated(final View view, final int n, final int n2, final int n3, final int n4) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            view.layout(mDecorInsets.left + n, mDecorInsets.top + n2, n3 - mDecorInsets.right, n4 - mDecorInsets.bottom);
        }
        
        public void measureChild(final View view, int childMeasureSpec, int childMeasureSpec2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(view);
            final int left = itemDecorInsetsForChild.left;
            final int right = itemDecorInsetsForChild.right;
            final int top = itemDecorInsetsForChild.top;
            final int bottom = itemDecorInsetsForChild.bottom;
            childMeasureSpec = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + (childMeasureSpec + (left + right)), layoutParams.width, this.canScrollHorizontally());
            childMeasureSpec2 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + (childMeasureSpec2 + (top + bottom)), layoutParams.height, this.canScrollVertically());
            if (this.shouldMeasureChild(view, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                view.measure(childMeasureSpec, childMeasureSpec2);
            }
        }
        
        public void measureChildWithMargins(final View view, int childMeasureSpec, int childMeasureSpec2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(view);
            final int left = itemDecorInsetsForChild.left;
            final int right = itemDecorInsetsForChild.right;
            final int top = itemDecorInsetsForChild.top;
            final int bottom = itemDecorInsetsForChild.bottom;
            childMeasureSpec = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + (childMeasureSpec + (left + right)), layoutParams.width, this.canScrollHorizontally());
            childMeasureSpec2 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + (childMeasureSpec2 + (top + bottom)), layoutParams.height, this.canScrollVertically());
            if (this.shouldMeasureChild(view, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                view.measure(childMeasureSpec, childMeasureSpec2);
            }
        }
        
        public void moveView(final int i, final int n) {
            final View child = this.getChildAt(i);
            if (child == null) {
                throw new IllegalArgumentException("Cannot move a child from non-existing index:" + i);
            }
            this.detachViewAt(i);
            this.attachView(child, n);
        }
        
        public void offsetChildrenHorizontal(final int n) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenHorizontal(n);
            }
        }
        
        public void offsetChildrenVertical(final int n) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenVertical(n);
            }
        }
        
        public void onAdapterChanged(final Adapter adapter, final Adapter adapter2) {
        }
        
        public boolean onAddFocusables(final RecyclerView recyclerView, final ArrayList<View> list, final int n, final int n2) {
            return false;
        }
        
        @CallSuper
        public void onAttachedToWindow(final RecyclerView recyclerView) {
        }
        
        @Deprecated
        public void onDetachedFromWindow(final RecyclerView recyclerView) {
        }
        
        @CallSuper
        public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
            this.onDetachedFromWindow(recyclerView);
        }
        
        @Nullable
        public View onFocusSearchFailed(final View view, final int n, final Recycler recycler, final State state) {
            return null;
        }
        
        public void onInitializeAccessibilityEvent(final Recycler recycler, final State state, final AccessibilityEvent accessibilityEvent) {
            final boolean b = true;
            final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(accessibilityEvent);
            if (this.mRecyclerView != null && record != null) {
                boolean scrollable = b;
                if (!ViewCompat.canScrollVertically((View)this.mRecyclerView, 1)) {
                    scrollable = b;
                    if (!ViewCompat.canScrollVertically((View)this.mRecyclerView, -1)) {
                        scrollable = b;
                        if (!ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) {
                            scrollable = (ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1) && b);
                        }
                    }
                }
                record.setScrollable(scrollable);
                if (this.mRecyclerView.mAdapter != null) {
                    record.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                }
            }
        }
        
        public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
            this.onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityEvent);
        }
        
        void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityNodeInfoCompat);
        }
        
        public void onInitializeAccessibilityNodeInfo(final Recycler recycler, final State state, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (ViewCompat.canScrollVertically((View)this.mRecyclerView, -1) || ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) {
                accessibilityNodeInfoCompat.addAction(8192);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            if (ViewCompat.canScrollVertically((View)this.mRecyclerView, 1) || ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1)) {
                accessibilityNodeInfoCompat.addAction(4096);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(this.getRowCountForAccessibility(recycler, state), this.getColumnCountForAccessibility(recycler, state), this.isLayoutHierarchical(recycler, state), this.getSelectionModeForAccessibility(recycler, state)));
        }
        
        public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            int position;
            if (this.canScrollVertically()) {
                position = this.getPosition(view);
            }
            else {
                position = 0;
            }
            int position2;
            if (this.canScrollHorizontally()) {
                position2 = this.getPosition(view);
            }
            else {
                position2 = 0;
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(position, 1, position2, 1, false, false));
        }
        
        void onInitializeAccessibilityNodeInfoForItem(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && !this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                this.onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, accessibilityNodeInfoCompat);
            }
        }
        
        public View onInterceptFocusSearch(final View view, final int n) {
            return null;
        }
        
        public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsChanged(final RecyclerView recyclerView) {
        }
        
        public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        }
        
        public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
            this.onItemsUpdated(recyclerView, n, n2);
        }
        
        public void onLayoutChildren(final Recycler recycler, final State state) {
            Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
        }
        
        public void onMeasure(final Recycler recycler, final State state, final int n, final int n2) {
            this.mRecyclerView.defaultOnMeasure(n, n2);
        }
        
        public boolean onRequestChildFocus(final RecyclerView recyclerView, final State state, final View view, final View view2) {
            return this.onRequestChildFocus(recyclerView, view, view2);
        }
        
        @Deprecated
        public boolean onRequestChildFocus(final RecyclerView recyclerView, final View view, final View view2) {
            return this.isSmoothScrolling() || recyclerView.isComputingLayout();
        }
        
        public void onRestoreInstanceState(final Parcelable parcelable) {
        }
        
        public Parcelable onSaveInstanceState() {
            return null;
        }
        
        public void onScrollStateChanged(final int n) {
        }
        
        boolean performAccessibilityAction(final int n, final Bundle bundle) {
            return this.performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, n, bundle);
        }
        
        public boolean performAccessibilityAction(final Recycler recycler, final State state, int n, final Bundle bundle) {
            boolean b = false;
            if (this.mRecyclerView != null) {
                final int n2 = 0;
                final int n3 = 0;
                final int n4 = 0;
                int n5 = 0;
                switch (n) {
                    default: {
                        n = n4;
                        break;
                    }
                    case 8192: {
                        int n6 = n2;
                        if (ViewCompat.canScrollVertically((View)this.mRecyclerView, -1)) {
                            n6 = -(this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
                        }
                        n = n6;
                        if (ViewCompat.canScrollHorizontally((View)this.mRecyclerView, -1)) {
                            n5 = -(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
                            n = n6;
                            break;
                        }
                        break;
                    }
                    case 4096: {
                        int n7 = n3;
                        if (ViewCompat.canScrollVertically((View)this.mRecyclerView, 1)) {
                            n7 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                        }
                        n = n7;
                        if (ViewCompat.canScrollHorizontally((View)this.mRecyclerView, 1)) {
                            n5 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                            n = n7;
                            break;
                        }
                        break;
                    }
                }
                if (n != 0 || n5 != 0) {
                    this.mRecyclerView.scrollBy(n5, n);
                    b = true;
                }
            }
            return b;
        }
        
        public boolean performAccessibilityActionForItem(final Recycler recycler, final State state, final View view, final int n, final Bundle bundle) {
            return false;
        }
        
        boolean performAccessibilityActionForItem(final View view, final int n, final Bundle bundle) {
            return this.performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, n, bundle);
        }
        
        public void postOnAnimation(final Runnable runnable) {
            if (this.mRecyclerView != null) {
                ViewCompat.postOnAnimation((View)this.mRecyclerView, runnable);
            }
        }
        
        public void removeAllViews() {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.mChildHelper.removeViewAt(i);
            }
        }
        
        public void removeAndRecycleAllViews(final Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                if (!RecyclerView.getChildViewHolderInt(this.getChildAt(i)).shouldIgnore()) {
                    this.removeAndRecycleViewAt(i, recycler);
                }
            }
        }
        
        void removeAndRecycleScrapInt(final Recycler recycler) {
            final int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; --i) {
                final View scrapView = recycler.getScrapViewAt(i);
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(scrapView);
                if (!childViewHolderInt.shouldIgnore()) {
                    childViewHolderInt.setIsRecyclable(false);
                    if (childViewHolderInt.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrapView, false);
                    }
                    if (this.mRecyclerView.mItemAnimator != null) {
                        this.mRecyclerView.mItemAnimator.endAnimation(childViewHolderInt);
                    }
                    childViewHolderInt.setIsRecyclable(true);
                    recycler.quickRecycleScrapView(scrapView);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }
        
        public void removeAndRecycleView(final View view, final Recycler recycler) {
            this.removeView(view);
            recycler.recycleView(view);
        }
        
        public void removeAndRecycleViewAt(final int n, final Recycler recycler) {
            final View child = this.getChildAt(n);
            this.removeViewAt(n);
            recycler.recycleView(child);
        }
        
        public boolean removeCallbacks(final Runnable runnable) {
            return this.mRecyclerView != null && this.mRecyclerView.removeCallbacks(runnable);
        }
        
        public void removeDetachedView(final View view) {
            this.mRecyclerView.removeDetachedView(view, false);
        }
        
        public void removeView(final View view) {
            this.mChildHelper.removeView(view);
        }
        
        public void removeViewAt(final int n) {
            if (this.getChildAt(n) != null) {
                this.mChildHelper.removeViewAt(n);
            }
        }
        
        public boolean requestChildRectangleOnScreen(final RecyclerView recyclerView, final View view, final Rect rect, final boolean b) {
            final int paddingLeft = this.getPaddingLeft();
            final int paddingTop = this.getPaddingTop();
            final int n = this.getWidth() - this.getPaddingRight();
            final int height = this.getHeight();
            final int paddingBottom = this.getPaddingBottom();
            final int n2 = view.getLeft() + rect.left - view.getScrollX();
            final int n3 = view.getTop() + rect.top - view.getScrollY();
            final int n4 = n2 + rect.width();
            final int height2 = rect.height();
            int a = Math.min(0, n2 - paddingLeft);
            int n5 = Math.min(0, n3 - paddingTop);
            final int max = Math.max(0, n4 - n);
            final int max2 = Math.max(0, n3 + height2 - (height - paddingBottom));
            if (this.getLayoutDirection() == 1) {
                if (max != 0) {
                    a = max;
                }
                else {
                    a = Math.max(a, n4 - n);
                }
            }
            else if (a == 0) {
                a = Math.min(n2 - paddingLeft, max);
            }
            if (n5 == 0) {
                n5 = Math.min(n3 - paddingTop, max2);
            }
            boolean b2;
            if (a != 0 || n5 != 0) {
                if (b) {
                    recyclerView.scrollBy(a, n5);
                }
                else {
                    recyclerView.smoothScrollBy(a, n5);
                }
                b2 = true;
            }
            else {
                b2 = false;
            }
            return b2;
        }
        
        public void requestLayout() {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.requestLayout();
            }
        }
        
        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }
        
        public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
            return 0;
        }
        
        public void scrollToPosition(final int n) {
        }
        
        public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
            return 0;
        }
        
        public void setAutoMeasureEnabled(final boolean mAutoMeasure) {
            this.mAutoMeasure = mAutoMeasure;
        }
        
        void setExactMeasureSpecsFrom(final RecyclerView recyclerView) {
            this.setMeasureSpecs(View$MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }
        
        void setMeasureSpecs(final int mWidthSpec, final int mHeightSpec) {
            this.mWidthSpec = mWidthSpec;
            this.mHeightSpec = mHeightSpec;
        }
        
        public void setMeasuredDimension(final int n, final int n2) {
            RecyclerView.access$5600(this.mRecyclerView, n, n2);
        }
        
        public void setMeasuredDimension(final Rect rect, final int n, final int n2) {
            this.setMeasuredDimension(chooseSize(n, rect.width() + this.getPaddingLeft() + this.getPaddingRight(), this.getMinimumWidth()), chooseSize(n2, rect.height() + this.getPaddingTop() + this.getPaddingBottom(), this.getMinimumHeight()));
        }
        
        void setMeasuredDimensionFromChildren(final int n, final int n2) {
            final int childCount = this.getChildCount();
            if (childCount == 0) {
                this.mRecyclerView.defaultOnMeasure(n, n2);
            }
            else {
                int n3 = Integer.MAX_VALUE;
                int n4 = Integer.MAX_VALUE;
                int n5 = Integer.MIN_VALUE;
                int n6 = Integer.MIN_VALUE;
                int n11;
                int n12;
                int n13;
                int n14;
                for (int i = 0; i < childCount; ++i, n5 = n12, n6 = n14, n3 = n11, n4 = n13) {
                    final View child = this.getChildAt(i);
                    final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                    final int n7 = this.getDecoratedLeft(child) - layoutParams.leftMargin;
                    final int n8 = this.getDecoratedRight(child) + layoutParams.rightMargin;
                    final int n9 = this.getDecoratedTop(child) - layoutParams.topMargin;
                    final int n10 = this.getDecoratedBottom(child) + layoutParams.bottomMargin;
                    if (n7 < (n11 = n3)) {
                        n11 = n7;
                    }
                    if (n8 > (n12 = n5)) {
                        n12 = n8;
                    }
                    if (n9 < (n13 = n4)) {
                        n13 = n9;
                    }
                    if (n10 > (n14 = n6)) {
                        n14 = n10;
                    }
                }
                this.mRecyclerView.mTempRect.set(n3, n4, n5, n6);
                this.setMeasuredDimension(this.mRecyclerView.mTempRect, n, n2);
            }
        }
        
        public void setMeasurementCacheEnabled(final boolean mMeasurementCacheEnabled) {
            this.mMeasurementCacheEnabled = mMeasurementCacheEnabled;
        }
        
        void setRecyclerView(final RecyclerView mRecyclerView) {
            if (mRecyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidthSpec = View$MeasureSpec.makeMeasureSpec(0, 1073741824);
                this.mHeightSpec = View$MeasureSpec.makeMeasureSpec(0, 1073741824);
            }
            else {
                this.mRecyclerView = mRecyclerView;
                this.mChildHelper = mRecyclerView.mChildHelper;
                this.mWidthSpec = View$MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), 1073741824);
                this.mHeightSpec = View$MeasureSpec.makeMeasureSpec(mRecyclerView.getHeight(), 1073741824);
            }
        }
        
        boolean shouldMeasureChild(final View view, final int n, final int n2, final LayoutParams layoutParams) {
            return view.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(view.getWidth(), n, layoutParams.width) || !isMeasurementUpToDate(view.getHeight(), n2, layoutParams.height);
        }
        
        boolean shouldMeasureTwice() {
            return false;
        }
        
        boolean shouldReMeasureChild(final View view, final int n, final int n2, final LayoutParams layoutParams) {
            return !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(view.getMeasuredWidth(), n, layoutParams.width) || !isMeasurementUpToDate(view.getMeasuredHeight(), n2, layoutParams.height);
        }
        
        public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int n) {
            Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
        }
        
        public void startSmoothScroll(final SmoothScroller mSmoothScroller) {
            if (this.mSmoothScroller != null && mSmoothScroller != this.mSmoothScroller && this.mSmoothScroller.isRunning()) {
                this.mSmoothScroller.stop();
            }
            (this.mSmoothScroller = mSmoothScroller).start(this.mRecyclerView, this);
        }
        
        public void stopIgnoringView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.stopIgnoring();
            childViewHolderInt.resetInternal();
            childViewHolderInt.addFlags(4);
        }
        
        void stopSmoothScroller() {
            if (this.mSmoothScroller != null) {
                this.mSmoothScroller.stop();
            }
        }
        
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
        
        public static class Properties
        {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        final Rect mDecorInsets;
        boolean mInsetsDirty;
        boolean mPendingInvalidate;
        ViewHolder mViewHolder;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public int getViewAdapterPosition() {
            return this.mViewHolder.getAdapterPosition();
        }
        
        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }
        
        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }
        
        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }
        
        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }
        
        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }
        
        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }
    }
    
    public interface OnChildAttachStateChangeListener
    {
        void onChildViewAttachedToWindow(final View p0);
        
        void onChildViewDetachedFromWindow(final View p0);
    }
    
    public interface OnItemTouchListener
    {
        boolean onInterceptTouchEvent(final RecyclerView p0, final MotionEvent p1);
        
        void onRequestDisallowInterceptTouchEvent(final boolean p0);
        
        void onTouchEvent(final RecyclerView p0, final MotionEvent p1);
    }
    
    public abstract static class OnScrollListener
    {
        public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
        }
        
        public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
        }
    }
    
    public static class RecycledViewPool
    {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount;
        private SparseIntArray mMaxScrap;
        private SparseArray<ArrayList<ViewHolder>> mScrap;
        
        public RecycledViewPool() {
            this.mScrap = (SparseArray<ArrayList<ViewHolder>>)new SparseArray();
            this.mMaxScrap = new SparseIntArray();
            this.mAttachCount = 0;
        }
        
        private ArrayList<ViewHolder> getScrapHeapForType(final int n) {
            ArrayList<ViewHolder> list;
            if ((list = (ArrayList<ViewHolder>)this.mScrap.get(n)) == null) {
                final ArrayList<ViewHolder> list2 = new ArrayList<ViewHolder>();
                this.mScrap.put(n, (Object)list2);
                list = list2;
                if (this.mMaxScrap.indexOfKey(n) < 0) {
                    this.mMaxScrap.put(n, 5);
                    list = list2;
                }
            }
            return list;
        }
        
        void attach(final Adapter adapter) {
            ++this.mAttachCount;
        }
        
        public void clear() {
            this.mScrap.clear();
        }
        
        void detach() {
            --this.mAttachCount;
        }
        
        public ViewHolder getRecycledView(int n) {
            final ArrayList list = (ArrayList)this.mScrap.get(n);
            ViewHolder viewHolder;
            if (list != null && !list.isEmpty()) {
                n = list.size() - 1;
                viewHolder = list.get(n);
                list.remove(n);
            }
            else {
                viewHolder = null;
            }
            return viewHolder;
        }
        
        void onAdapterChanged(final Adapter adapter, final Adapter adapter2, final boolean b) {
            if (adapter != null) {
                this.detach();
            }
            if (!b && this.mAttachCount == 0) {
                this.clear();
            }
            if (adapter2 != null) {
                this.attach(adapter2);
            }
        }
        
        public void putRecycledView(final ViewHolder e) {
            final int itemViewType = e.getItemViewType();
            final ArrayList<ViewHolder> scrapHeapForType = this.getScrapHeapForType(itemViewType);
            if (this.mMaxScrap.get(itemViewType) > scrapHeapForType.size()) {
                e.resetInternal();
                scrapHeapForType.add(e);
            }
        }
        
        public void setMaxRecycledViews(final int n, final int n2) {
            this.mMaxScrap.put(n, n2);
            final ArrayList list = (ArrayList)this.mScrap.get(n);
            if (list != null) {
                while (list.size() > n2) {
                    list.remove(list.size() - 1);
                }
            }
        }
        
        int size() {
            int n = 0;
            int n2;
            for (int i = 0; i < this.mScrap.size(); ++i, n = n2) {
                final ArrayList list = (ArrayList)this.mScrap.valueAt(i);
                n2 = n;
                if (list != null) {
                    n2 = n + list.size();
                }
            }
            return n;
        }
    }
    
    public final class Recycler
    {
        private static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap;
        final ArrayList<ViewHolder> mCachedViews;
        private ArrayList<ViewHolder> mChangedScrap;
        private RecycledViewPool mRecyclerPool;
        private final List<ViewHolder> mUnmodifiableAttachedScrap;
        private ViewCacheExtension mViewCacheExtension;
        private int mViewCacheMax;
        
        public Recycler() {
            this.mAttachedScrap = new ArrayList<ViewHolder>();
            this.mChangedScrap = null;
            this.mCachedViews = new ArrayList<ViewHolder>();
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList((List<? extends ViewHolder>)this.mAttachedScrap);
            this.mViewCacheMax = 2;
        }
        
        private void attachAccessibilityDelegate(final View view) {
            if (RecyclerView.this.isAccessibilityEnabled()) {
                if (ViewCompat.getImportantForAccessibility(view) == 0) {
                    ViewCompat.setImportantForAccessibility(view, 1);
                }
                if (!ViewCompat.hasAccessibilityDelegate(view)) {
                    ViewCompat.setAccessibilityDelegate(view, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
                }
            }
        }
        
        private void invalidateDisplayListInt(final ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                this.invalidateDisplayListInt((ViewGroup)viewHolder.itemView, false);
            }
        }
        
        private void invalidateDisplayListInt(final ViewGroup viewGroup, final boolean b) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    this.invalidateDisplayListInt((ViewGroup)child, true);
                }
            }
            if (b) {
                if (viewGroup.getVisibility() == 4) {
                    viewGroup.setVisibility(0);
                    viewGroup.setVisibility(4);
                }
                else {
                    final int visibility = viewGroup.getVisibility();
                    viewGroup.setVisibility(4);
                    viewGroup.setVisibility(visibility);
                }
            }
        }
        
        void addViewHolderToRecycledViewPool(final ViewHolder viewHolder) {
            ViewCompat.setAccessibilityDelegate(viewHolder.itemView, null);
            this.dispatchViewRecycled(viewHolder);
            viewHolder.mOwnerRecyclerView = null;
            this.getRecycledViewPool().putRecycledView(viewHolder);
        }
        
        public void bindViewToPosition(final View view, final int n) {
            boolean mPendingInvalidate = true;
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt == null) {
                throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
            }
            final int positionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(n);
            if (positionOffset < 0 || positionOffset >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + n + "(offset:" + positionOffset + ")." + "state:" + RecyclerView.this.mState.getItemCount());
            }
            childViewHolderInt.mOwnerRecyclerView = RecyclerView.this;
            RecyclerView.this.mAdapter.bindViewHolder(childViewHolderInt, positionOffset);
            this.attachAccessibilityDelegate(view);
            if (RecyclerView.this.mState.isPreLayout()) {
                childViewHolderInt.mPreLayoutPosition = n;
            }
            final ViewGroup$LayoutParams layoutParams = childViewHolderInt.itemView.getLayoutParams();
            LayoutParams layoutParams2;
            if (layoutParams == null) {
                layoutParams2 = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                childViewHolderInt.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            else if (!RecyclerView.this.checkLayoutParams(layoutParams)) {
                layoutParams2 = (LayoutParams)RecyclerView.this.generateLayoutParams(layoutParams);
                childViewHolderInt.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            else {
                layoutParams2 = (LayoutParams)layoutParams;
            }
            layoutParams2.mInsetsDirty = true;
            layoutParams2.mViewHolder = childViewHolderInt;
            if (childViewHolderInt.itemView.getParent() != null) {
                mPendingInvalidate = false;
            }
            layoutParams2.mPendingInvalidate = mPendingInvalidate;
        }
        
        public void clear() {
            this.mAttachedScrap.clear();
            this.recycleAndClearCachedViews();
        }
        
        void clearOldPositions() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                this.mCachedViews.get(i).clearOldPosition();
            }
            for (int size2 = this.mAttachedScrap.size(), j = 0; j < size2; ++j) {
                this.mAttachedScrap.get(j).clearOldPosition();
            }
            if (this.mChangedScrap != null) {
                for (int size3 = this.mChangedScrap.size(), k = 0; k < size3; ++k) {
                    this.mChangedScrap.get(k).clearOldPosition();
                }
            }
        }
        
        void clearScrap() {
            this.mAttachedScrap.clear();
            if (this.mChangedScrap != null) {
                this.mChangedScrap.clear();
            }
        }
        
        public int convertPreLayoutPositionToPostLayout(int positionOffset) {
            if (positionOffset < 0 || positionOffset >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("invalid position " + positionOffset + ". State " + "item count is " + RecyclerView.this.mState.getItemCount());
            }
            if (RecyclerView.this.mState.isPreLayout()) {
                positionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(positionOffset);
            }
            return positionOffset;
        }
        
        void dispatchViewRecycled(final ViewHolder viewHolder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(viewHolder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(viewHolder);
            }
        }
        
        ViewHolder getChangedScrapViewForPosition(int i) {
            if (this.mChangedScrap != null) {
                final int size = this.mChangedScrap.size();
                if (size != 0) {
                    for (int j = 0; j < size; ++j) {
                        final Object o = this.mChangedScrap.get(j);
                        if (!((ViewHolder)o).wasReturnedFromScrap() && ((ViewHolder)o).getLayoutPosition() == i) {
                            ((ViewHolder)o).addFlags(32);
                            return (ViewHolder)o;
                        }
                    }
                    if (RecyclerView.this.mAdapter.hasStableIds()) {
                        i = RecyclerView.this.mAdapterHelper.findPositionOffset(i);
                        if (i > 0 && i < RecyclerView.this.mAdapter.getItemCount()) {
                            final long itemId = RecyclerView.this.mAdapter.getItemId(i);
                            Object o;
                            for (i = 0; i < size; ++i) {
                                o = this.mChangedScrap.get(i);
                                if (!((ViewHolder)o).wasReturnedFromScrap() && ((ViewHolder)o).getItemId() == itemId) {
                                    ((ViewHolder)o).addFlags(32);
                                    return (ViewHolder)o;
                                }
                            }
                        }
                    }
                    Object o = null;
                    return (ViewHolder)o;
                }
            }
            Object o = null;
            return (ViewHolder)o;
        }
        
        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }
        
        int getScrapCount() {
            return this.mAttachedScrap.size();
        }
        
        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }
        
        View getScrapViewAt(final int index) {
            return this.mAttachedScrap.get(index).itemView;
        }
        
        ViewHolder getScrapViewForId(final long n, final int n2, final boolean b) {
            for (int i = this.mAttachedScrap.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mAttachedScrap.get(i);
                if (viewHolder.getItemId() == n && !viewHolder.wasReturnedFromScrap()) {
                    if (n2 == viewHolder.getItemViewType()) {
                        viewHolder.addFlags(32);
                        ViewHolder viewHolder2 = viewHolder;
                        if (viewHolder.isRemoved()) {
                            viewHolder2 = viewHolder;
                            if (!RecyclerView.this.mState.isPreLayout()) {
                                viewHolder.setFlags(2, 14);
                                viewHolder2 = viewHolder;
                            }
                        }
                        return viewHolder2;
                    }
                    if (!b) {
                        this.mAttachedScrap.remove(i);
                        RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                        this.quickRecycleScrapView(viewHolder.itemView);
                    }
                }
            }
            for (int j = this.mCachedViews.size() - 1; j >= 0; --j) {
                final ViewHolder viewHolder3 = this.mCachedViews.get(j);
                if (viewHolder3.getItemId() == n) {
                    if (n2 == viewHolder3.getItemViewType()) {
                        ViewHolder viewHolder2 = viewHolder3;
                        if (!b) {
                            this.mCachedViews.remove(j);
                            viewHolder2 = viewHolder3;
                            return viewHolder2;
                        }
                        return viewHolder2;
                    }
                    else if (!b) {
                        this.recycleCachedViewAt(j);
                    }
                }
            }
            return null;
        }
        
        ViewHolder getScrapViewForPosition(int indexOfChild, int i, final boolean b) {
            final int size = this.mAttachedScrap.size();
            int j = 0;
            while (j < size) {
                final Object childViewHolderInt = this.mAttachedScrap.get(j);
                if (!((ViewHolder)childViewHolderInt).wasReturnedFromScrap() && ((ViewHolder)childViewHolderInt).getLayoutPosition() == indexOfChild && !((ViewHolder)childViewHolderInt).isInvalid() && (RecyclerView.this.mState.mInPreLayout || !((ViewHolder)childViewHolderInt).isRemoved())) {
                    if (i != -1 && ((ViewHolder)childViewHolderInt).getItemViewType() != i) {
                        Log.e("RecyclerView", "Scrap view for position " + indexOfChild + " isn't dirty but has" + " wrong view type! (found " + ((ViewHolder)childViewHolderInt).getItemViewType() + " but expected " + i + ")");
                        break;
                    }
                    ((ViewHolder)childViewHolderInt).addFlags(32);
                    return (ViewHolder)childViewHolderInt;
                }
                else {
                    ++j;
                }
            }
            if (!b) {
                final View hiddenNonRemovedView = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(indexOfChild, i);
                if (hiddenNonRemovedView != null) {
                    final Object childViewHolderInt = RecyclerView.getChildViewHolderInt(hiddenNonRemovedView);
                    RecyclerView.this.mChildHelper.unhide(hiddenNonRemovedView);
                    indexOfChild = RecyclerView.this.mChildHelper.indexOfChild(hiddenNonRemovedView);
                    if (indexOfChild == -1) {
                        throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + childViewHolderInt);
                    }
                    RecyclerView.this.mChildHelper.detachViewFromParent(indexOfChild);
                    this.scrapView(hiddenNonRemovedView);
                    ((ViewHolder)childViewHolderInt).addFlags(8224);
                    return (ViewHolder)childViewHolderInt;
                }
            }
            final int size2 = this.mCachedViews.size();
            i = 0;
            while (i < size2) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (!viewHolder.isInvalid() && viewHolder.getLayoutPosition() == indexOfChild) {
                    Object childViewHolderInt = viewHolder;
                    if (!b) {
                        this.mCachedViews.remove(i);
                        childViewHolderInt = viewHolder;
                        return (ViewHolder)childViewHolderInt;
                    }
                    return (ViewHolder)childViewHolderInt;
                }
                else {
                    ++i;
                }
            }
            Object childViewHolderInt = null;
            return (ViewHolder)childViewHolderInt;
        }
        
        public View getViewForPosition(final int n) {
            return this.getViewForPosition(n, false);
        }
        
        View getViewForPosition(final int mPreLayoutPosition, final boolean b) {
            if (mPreLayoutPosition < 0 || mPreLayoutPosition >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + mPreLayoutPosition + "(" + mPreLayoutPosition + "). Item count:" + RecyclerView.this.mState.getItemCount());
            }
            int n = 0;
            ViewHolder changedScrapViewForPosition = null;
            if (RecyclerView.this.mState.isPreLayout()) {
                changedScrapViewForPosition = this.getChangedScrapViewForPosition(mPreLayoutPosition);
                if (changedScrapViewForPosition != null) {
                    n = 1;
                }
                else {
                    n = 0;
                }
            }
            int n2 = n;
            ViewHolder viewHolder;
            if ((viewHolder = changedScrapViewForPosition) == null) {
                final ViewHolder scrapViewForPosition = this.getScrapViewForPosition(mPreLayoutPosition, -1, b);
                n2 = n;
                if ((viewHolder = scrapViewForPosition) != null) {
                    if (!this.validateViewHolderForOffsetPosition(scrapViewForPosition)) {
                        if (!b) {
                            scrapViewForPosition.addFlags(4);
                            if (scrapViewForPosition.isScrap()) {
                                RecyclerView.this.removeDetachedView(scrapViewForPosition.itemView, false);
                                scrapViewForPosition.unScrap();
                            }
                            else if (scrapViewForPosition.wasReturnedFromScrap()) {
                                scrapViewForPosition.clearReturnedFromScrapFlag();
                            }
                            this.recycleViewHolderInternal(scrapViewForPosition);
                        }
                        viewHolder = null;
                        n2 = n;
                    }
                    else {
                        n2 = 1;
                        viewHolder = scrapViewForPosition;
                    }
                }
            }
            int n3 = n2;
            ViewHolder viewHolder2;
            if ((viewHolder2 = viewHolder) == null) {
                final int positionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(mPreLayoutPosition);
                if (positionOffset < 0 || positionOffset >= RecyclerView.this.mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + mPreLayoutPosition + "(offset:" + positionOffset + ")." + "state:" + RecyclerView.this.mState.getItemCount());
                }
                final int itemViewType = RecyclerView.this.mAdapter.getItemViewType(positionOffset);
                int n4 = n2;
                ViewHolder viewHolder3 = viewHolder;
                if (RecyclerView.this.mAdapter.hasStableIds()) {
                    final ViewHolder scrapViewForId = this.getScrapViewForId(RecyclerView.this.mAdapter.getItemId(positionOffset), itemViewType, b);
                    n4 = n2;
                    if ((viewHolder3 = scrapViewForId) != null) {
                        scrapViewForId.mPosition = positionOffset;
                        n4 = 1;
                        viewHolder3 = scrapViewForId;
                    }
                }
                ViewHolder viewHolder4;
                if ((viewHolder4 = viewHolder3) == null) {
                    viewHolder4 = viewHolder3;
                    if (this.mViewCacheExtension != null) {
                        final View viewForPositionAndType = this.mViewCacheExtension.getViewForPositionAndType(this, mPreLayoutPosition, itemViewType);
                        viewHolder4 = viewHolder3;
                        if (viewForPositionAndType != null) {
                            final ViewHolder childViewHolder = RecyclerView.this.getChildViewHolder(viewForPositionAndType);
                            if (childViewHolder == null) {
                                throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder");
                            }
                            viewHolder4 = childViewHolder;
                            if (childViewHolder.shouldIgnore()) {
                                throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                            }
                        }
                    }
                }
                ViewHolder viewHolder5;
                if ((viewHolder5 = viewHolder4) == null) {
                    final ViewHolder recycledView = this.getRecycledViewPool().getRecycledView(itemViewType);
                    if ((viewHolder5 = recycledView) != null) {
                        recycledView.resetInternal();
                        viewHolder5 = recycledView;
                        if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                            this.invalidateDisplayListInt(recycledView);
                            viewHolder5 = recycledView;
                        }
                    }
                }
                n3 = n4;
                if ((viewHolder2 = viewHolder5) == null) {
                    viewHolder2 = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, itemViewType);
                    n3 = n4;
                }
            }
            if (n3 != 0 && !RecyclerView.this.mState.isPreLayout() && viewHolder2.hasAnyOfTheFlags(8192)) {
                viewHolder2.setFlags(0, 8192);
                if (RecyclerView.this.mState.mRunSimpleAnimations) {
                    RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(viewHolder2, RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, viewHolder2, ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder2) | 0x1000, viewHolder2.getUnmodifiedPayloads()));
                }
            }
            int n5 = 0;
            if (RecyclerView.this.mState.isPreLayout() && viewHolder2.isBound()) {
                viewHolder2.mPreLayoutPosition = mPreLayoutPosition;
            }
            else if (!viewHolder2.isBound() || viewHolder2.needsUpdate() || viewHolder2.isInvalid()) {
                final int positionOffset2 = RecyclerView.this.mAdapterHelper.findPositionOffset(mPreLayoutPosition);
                viewHolder2.mOwnerRecyclerView = RecyclerView.this;
                RecyclerView.this.mAdapter.bindViewHolder(viewHolder2, positionOffset2);
                this.attachAccessibilityDelegate(viewHolder2.itemView);
                n5 = 1;
                if (RecyclerView.this.mState.isPreLayout()) {
                    viewHolder2.mPreLayoutPosition = mPreLayoutPosition;
                    n5 = n5;
                }
            }
            final ViewGroup$LayoutParams layoutParams = viewHolder2.itemView.getLayoutParams();
            LayoutParams layoutParams2;
            if (layoutParams == null) {
                layoutParams2 = (LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
                viewHolder2.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            else if (!RecyclerView.this.checkLayoutParams(layoutParams)) {
                layoutParams2 = (LayoutParams)RecyclerView.this.generateLayoutParams(layoutParams);
                viewHolder2.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            else {
                layoutParams2 = (LayoutParams)layoutParams;
            }
            layoutParams2.mViewHolder = viewHolder2;
            layoutParams2.mPendingInvalidate = (n3 != 0 && n5 != 0);
            return viewHolder2.itemView;
        }
        
        void markItemDecorInsetsDirty() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final LayoutParams layoutParams = (LayoutParams)this.mCachedViews.get(i).itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
        
        void markKnownViewsInvalid() {
            if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
                for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                    final ViewHolder viewHolder = this.mCachedViews.get(i);
                    if (viewHolder != null) {
                        viewHolder.addFlags(6);
                        viewHolder.addChangePayload(null);
                    }
                }
            }
            else {
                this.recycleAndClearCachedViews();
            }
        }
        
        void offsetPositionRecordsForInsert(final int n, final int n2) {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= n) {
                    viewHolder.offsetPosition(n2, true);
                }
            }
        }
        
        void offsetPositionRecordsForMove(final int n, final int n2) {
            int n3;
            int n4;
            int n5;
            if (n < n2) {
                n3 = n;
                n4 = n2;
                n5 = -1;
            }
            else {
                n3 = n2;
                n4 = n;
                n5 = 1;
            }
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= n3 && viewHolder.mPosition <= n4) {
                    if (viewHolder.mPosition == n) {
                        viewHolder.offsetPosition(n2 - n, false);
                    }
                    else {
                        viewHolder.offsetPosition(n5, false);
                    }
                }
            }
        }
        
        void offsetPositionRecordsForRemove(final int n, final int n2, final boolean b) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    if (viewHolder.mPosition >= n + n2) {
                        viewHolder.offsetPosition(-n2, b);
                    }
                    else if (viewHolder.mPosition >= n) {
                        viewHolder.addFlags(8);
                        this.recycleCachedViewAt(i);
                    }
                }
            }
        }
        
        void onAdapterChanged(final Adapter adapter, final Adapter adapter2, final boolean b) {
            this.clear();
            this.getRecycledViewPool().onAdapterChanged(adapter, adapter2, b);
        }
        
        void quickRecycleScrapView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.mScrapContainer = null;
            childViewHolderInt.mInChangeScrap = false;
            childViewHolderInt.clearReturnedFromScrapFlag();
            this.recycleViewHolderInternal(childViewHolderInt);
        }
        
        void recycleAndClearCachedViews() {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                this.recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
        }
        
        void recycleCachedViewAt(final int n) {
            this.addViewHolderToRecycledViewPool(this.mCachedViews.get(n));
            this.mCachedViews.remove(n);
        }
        
        public void recycleView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (childViewHolderInt.isScrap()) {
                childViewHolderInt.unScrap();
            }
            else if (childViewHolderInt.wasReturnedFromScrap()) {
                childViewHolderInt.clearReturnedFromScrapFlag();
            }
            this.recycleViewHolderInternal(childViewHolderInt);
        }
        
        void recycleViewHolderInternal(final ViewHolder viewHolder) {
            boolean b = true;
            if (viewHolder.isScrap() || viewHolder.itemView.getParent() != null) {
                final StringBuilder append = new StringBuilder().append("Scrapped or attached views may not be recycled. isScrap:").append(viewHolder.isScrap()).append(" isAttached:");
                if (viewHolder.itemView.getParent() == null) {
                    b = false;
                }
                throw new IllegalArgumentException(append.append(b).toString());
            }
            if (viewHolder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + viewHolder);
            }
            if (viewHolder.shouldIgnore()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
            }
            final boolean access$4900 = viewHolder.doesTransientStatePreventRecycling();
            int n;
            if (RecyclerView.this.mAdapter != null && access$4900 && RecyclerView.this.mAdapter.onFailedToRecycleView(viewHolder)) {
                n = 1;
            }
            else {
                n = 0;
            }
            int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            int n5 = 0;
            Label_0282: {
                if (n == 0) {
                    n5 = n4;
                    if (!viewHolder.isRecyclable()) {
                        break Label_0282;
                    }
                }
                int n6 = n3;
                if (!viewHolder.hasAnyOfTheFlags(14)) {
                    final int size = this.mCachedViews.size();
                    if (size == this.mViewCacheMax && size > 0) {
                        this.recycleCachedViewAt(0);
                    }
                    n6 = n3;
                    if (size < this.mViewCacheMax) {
                        this.mCachedViews.add(viewHolder);
                        n6 = 1;
                    }
                }
                n2 = n6;
                n5 = n4;
                if (n6 == 0) {
                    this.addViewHolderToRecycledViewPool(viewHolder);
                    n5 = 1;
                    n2 = n6;
                }
            }
            RecyclerView.this.mViewInfoStore.removeViewHolder(viewHolder);
            if (n2 == 0 && n5 == 0 && access$4900) {
                viewHolder.mOwnerRecyclerView = null;
            }
        }
        
        void recycleViewInternal(final View view) {
            this.recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }
        
        void scrapView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.hasAnyOfTheFlags(12) || !childViewHolderInt.isUpdated() || RecyclerView.this.canReuseUpdatedViewHolder(childViewHolderInt)) {
                if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
                    throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                }
                childViewHolderInt.setScrapContainer(this, false);
                this.mAttachedScrap.add(childViewHolderInt);
            }
            else {
                if (this.mChangedScrap == null) {
                    this.mChangedScrap = new ArrayList<ViewHolder>();
                }
                childViewHolderInt.setScrapContainer(this, true);
                this.mChangedScrap.add(childViewHolderInt);
            }
        }
        
        void setAdapterPositionsAsUnknown() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    viewHolder.addFlags(512);
                }
            }
        }
        
        void setRecycledViewPool(final RecycledViewPool mRecyclerPool) {
            if (this.mRecyclerPool != null) {
                this.mRecyclerPool.detach();
            }
            if ((this.mRecyclerPool = mRecyclerPool) != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
            }
        }
        
        void setViewCacheExtension(final ViewCacheExtension mViewCacheExtension) {
            this.mViewCacheExtension = mViewCacheExtension;
        }
        
        public void setViewCacheSize(final int mViewCacheMax) {
            this.mViewCacheMax = mViewCacheMax;
            for (int n = this.mCachedViews.size() - 1; n >= 0 && this.mCachedViews.size() > mViewCacheMax; --n) {
                this.recycleCachedViewAt(n);
            }
        }
        
        void unscrapView(final ViewHolder viewHolder) {
            if (viewHolder.mInChangeScrap) {
                this.mChangedScrap.remove(viewHolder);
            }
            else {
                this.mAttachedScrap.remove(viewHolder);
            }
            viewHolder.mScrapContainer = null;
            viewHolder.mInChangeScrap = false;
            viewHolder.clearReturnedFromScrapFlag();
        }
        
        boolean validateViewHolderForOffsetPosition(final ViewHolder obj) {
            final boolean b = true;
            boolean preLayout;
            if (obj.isRemoved()) {
                preLayout = RecyclerView.this.mState.isPreLayout();
            }
            else {
                if (obj.mPosition < 0 || obj.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + obj);
                }
                if (!RecyclerView.this.mState.isPreLayout() && RecyclerView.this.mAdapter.getItemViewType(obj.mPosition) != obj.getItemViewType()) {
                    preLayout = false;
                }
                else {
                    preLayout = b;
                    if (RecyclerView.this.mAdapter.hasStableIds()) {
                        preLayout = b;
                        if (obj.getItemId() != RecyclerView.this.mAdapter.getItemId(obj.mPosition)) {
                            preLayout = false;
                        }
                    }
                }
            }
            return preLayout;
        }
        
        void viewRangeUpdate(final int n, final int n2) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    final int layoutPosition = viewHolder.getLayoutPosition();
                    if (layoutPosition >= n && layoutPosition < n + n2) {
                        viewHolder.addFlags(2);
                        this.recycleCachedViewAt(i);
                    }
                }
            }
        }
    }
    
    public interface RecyclerListener
    {
        void onViewRecycled(final ViewHolder p0);
    }
    
    private class RecyclerViewDataObserver extends AdapterDataObserver
    {
        @Override
        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapter.hasStableIds()) {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.setDataSetChangedAfterLayout();
            }
            else {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.setDataSetChangedAfterLayout();
            }
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }
        
        @Override
        public void onItemRangeChanged(final int n, final int n2, final Object o) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(n, n2, o)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeInserted(final int n, final int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(n, n2, n3)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeRemoved(final int n, final int n2) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }
        
        void triggerUpdateProcessor() {
            if (RecyclerView.this.mPostUpdatesOnAnimation && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
                ViewCompat.postOnAnimation((View)RecyclerView.this, RecyclerView.this.mUpdateChildViewsRunnable);
            }
            else {
                RecyclerView.this.mAdapterUpdateDuringMeasure = true;
                RecyclerView.this.requestLayout();
            }
        }
    }
    
    public static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        Parcelable mLayoutState;
        
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
            this.mLayoutState = parcel.readParcelable(LayoutManager.class.getClassLoader());
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        private void copyFrom(final SavedState savedState) {
            this.mLayoutState = savedState.mLayoutState;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeParcelable(this.mLayoutState, 0);
        }
    }
    
    public static class SimpleOnItemTouchListener implements OnItemTouchListener
    {
        @Override
        public boolean onInterceptTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
            return false;
        }
        
        @Override
        public void onRequestDisallowInterceptTouchEvent(final boolean b) {
        }
        
        @Override
        public void onTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
        }
    }
    
    public abstract static class SmoothScroller
    {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction;
        private boolean mRunning;
        private int mTargetPosition;
        private View mTargetView;
        
        public SmoothScroller() {
            this.mTargetPosition = -1;
            this.mRecyclingAction = new Action(0, 0);
        }
        
        private void onAnimation(final int n, final int n2) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || mRecyclerView == null) {
                this.stop();
            }
            this.mPendingInitialRun = false;
            if (this.mTargetView != null) {
                if (this.getChildPosition(this.mTargetView) == this.mTargetPosition) {
                    this.onTargetFound(this.mTargetView, mRecyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(mRecyclerView);
                    this.stop();
                }
                else {
                    Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                this.onSeekTargetStep(n, n2, mRecyclerView.mState, this.mRecyclingAction);
                final boolean hasJumpTarget = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(mRecyclerView);
                if (hasJumpTarget) {
                    if (this.mRunning) {
                        this.mPendingInitialRun = true;
                        mRecyclerView.mViewFlinger.postOnAnimation();
                    }
                    else {
                        this.stop();
                    }
                }
            }
        }
        
        public View findViewByPosition(final int n) {
            return this.mRecyclerView.mLayout.findViewByPosition(n);
        }
        
        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }
        
        public int getChildPosition(final View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }
        
        @Nullable
        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }
        
        public int getTargetPosition() {
            return this.mTargetPosition;
        }
        
        @Deprecated
        public void instantScrollToPosition(final int n) {
            this.mRecyclerView.scrollToPosition(n);
        }
        
        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }
        
        public boolean isRunning() {
            return this.mRunning;
        }
        
        protected void normalize(final PointF pointF) {
            final double sqrt = Math.sqrt(pointF.x * pointF.x + pointF.y * pointF.y);
            pointF.x /= (float)sqrt;
            pointF.y /= (float)sqrt;
        }
        
        protected void onChildAttachedToWindow(final View mTargetView) {
            if (this.getChildPosition(mTargetView) == this.getTargetPosition()) {
                this.mTargetView = mTargetView;
            }
        }
        
        protected abstract void onSeekTargetStep(final int p0, final int p1, final State p2, final Action p3);
        
        protected abstract void onStart();
        
        protected abstract void onStop();
        
        protected abstract void onTargetFound(final View p0, final State p1, final Action p2);
        
        public void setTargetPosition(final int mTargetPosition) {
            this.mTargetPosition = mTargetPosition;
        }
        
        void start(final RecyclerView mRecyclerView, final LayoutManager mLayoutManager) {
            this.mRecyclerView = mRecyclerView;
            this.mLayoutManager = mLayoutManager;
            if (this.mTargetPosition == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = this.findViewByPosition(this.getTargetPosition());
            this.onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
        }
        
        protected final void stop() {
            if (this.mRunning) {
                this.onStop();
                this.mRecyclerView.mState.mTargetPosition = -1;
                this.mTargetView = null;
                this.mTargetPosition = -1;
                this.mPendingInitialRun = false;
                this.mRunning = false;
                this.mLayoutManager.onSmoothScrollerStopped(this);
                this.mLayoutManager = null;
                this.mRecyclerView = null;
            }
        }
        
        public static class Action
        {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean changed;
            private int consecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;
            
            public Action(final int n, final int n2) {
                this(n, n2, Integer.MIN_VALUE, null);
            }
            
            public Action(final int n, final int n2, final int n3) {
                this(n, n2, n3, null);
            }
            
            public Action(final int mDx, final int mDy, final int mDuration, final Interpolator mInterpolator) {
                this.mJumpToPosition = -1;
                this.changed = false;
                this.consecutiveUpdates = 0;
                this.mDx = mDx;
                this.mDy = mDy;
                this.mDuration = mDuration;
                this.mInterpolator = mInterpolator;
            }
            
            private void runIfNecessary(final RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    final int mJumpToPosition = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(mJumpToPosition);
                    this.changed = false;
                }
                else if (this.changed) {
                    this.validate();
                    if (this.mInterpolator == null) {
                        if (this.mDuration == Integer.MIN_VALUE) {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                        }
                        else {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                        }
                    }
                    else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    }
                    ++this.consecutiveUpdates;
                    if (this.consecutiveUpdates > 10) {
                        Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.changed = false;
                }
                else {
                    this.consecutiveUpdates = 0;
                }
            }
            
            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }
            
            public int getDuration() {
                return this.mDuration;
            }
            
            public int getDx() {
                return this.mDx;
            }
            
            public int getDy() {
                return this.mDy;
            }
            
            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }
            
            boolean hasJumpTarget() {
                return this.mJumpToPosition >= 0;
            }
            
            public void jumpTo(final int mJumpToPosition) {
                this.mJumpToPosition = mJumpToPosition;
            }
            
            public void setDuration(final int mDuration) {
                this.changed = true;
                this.mDuration = mDuration;
            }
            
            public void setDx(final int mDx) {
                this.changed = true;
                this.mDx = mDx;
            }
            
            public void setDy(final int mDy) {
                this.changed = true;
                this.mDy = mDy;
            }
            
            public void setInterpolator(final Interpolator mInterpolator) {
                this.changed = true;
                this.mInterpolator = mInterpolator;
            }
            
            public void update(final int mDx, final int mDy, final int mDuration, final Interpolator mInterpolator) {
                this.mDx = mDx;
                this.mDy = mDy;
                this.mDuration = mDuration;
                this.mInterpolator = mInterpolator;
                this.changed = true;
            }
        }
    }
    
    public static class State
    {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        private int mDeletedInvisibleItemCountSincePreviousLayout;
        private boolean mInPreLayout;
        private boolean mIsMeasuring;
        int mItemCount;
        private int mLayoutStep;
        private int mPreviousLayoutItemCount;
        private boolean mRunPredictiveAnimations;
        private boolean mRunSimpleAnimations;
        private boolean mStructureChanged;
        private int mTargetPosition;
        private boolean mTrackOldChangeHolders;
        
        public State() {
            this.mTargetPosition = -1;
            this.mLayoutStep = 1;
            this.mItemCount = 0;
            this.mPreviousLayoutItemCount = 0;
            this.mDeletedInvisibleItemCountSincePreviousLayout = 0;
            this.mStructureChanged = false;
            this.mInPreLayout = false;
            this.mRunSimpleAnimations = false;
            this.mRunPredictiveAnimations = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }
        
        static /* synthetic */ int access$1712(final State state, int mDeletedInvisibleItemCountSincePreviousLayout) {
            mDeletedInvisibleItemCountSincePreviousLayout += state.mDeletedInvisibleItemCountSincePreviousLayout;
            return state.mDeletedInvisibleItemCountSincePreviousLayout = mDeletedInvisibleItemCountSincePreviousLayout;
        }
        
        void assertLayoutStep(final int i) {
            if ((this.mLayoutStep & i) == 0x0) {
                throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(i) + " but it is " + Integer.toBinaryString(this.mLayoutStep));
            }
        }
        
        public boolean didStructureChange() {
            return this.mStructureChanged;
        }
        
        public <T> T get(final int n) {
            Object value;
            if (this.mData == null) {
                value = null;
            }
            else {
                value = this.mData.get(n);
            }
            return (T)value;
        }
        
        public int getItemCount() {
            int mItemCount;
            if (this.mInPreLayout) {
                mItemCount = this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
            }
            else {
                mItemCount = this.mItemCount;
            }
            return mItemCount;
        }
        
        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }
        
        public boolean hasTargetScrollPosition() {
            return this.mTargetPosition != -1;
        }
        
        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }
        
        public boolean isPreLayout() {
            return this.mInPreLayout;
        }
        
        public void put(final int n, final Object o) {
            if (this.mData == null) {
                this.mData = (SparseArray<Object>)new SparseArray();
            }
            this.mData.put(n, o);
        }
        
        public void remove(final int n) {
            if (this.mData != null) {
                this.mData.remove(n);
            }
        }
        
        State reset() {
            this.mTargetPosition = -1;
            if (this.mData != null) {
                this.mData.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mIsMeasuring = false;
            return this;
        }
        
        @Override
        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }
        
        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }
        
        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }
    }
    
    public abstract static class ViewCacheExtension
    {
        public abstract View getViewForPositionAndType(final Recycler p0, final int p1, final int p2);
    }
    
    private class ViewFlinger implements Runnable
    {
        private boolean mEatRunOnAnimationRequest;
        private Interpolator mInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        private boolean mReSchedulePostAnimationCallback;
        private ScrollerCompat mScroller;
        
        public ViewFlinger() {
            this.mInterpolator = RecyclerView.sQuinticInterpolator;
            this.mEatRunOnAnimationRequest = false;
            this.mReSchedulePostAnimationCallback = false;
            this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }
        
        private int computeScrollDuration(int n, int a, int n2, int n3) {
            final int abs = Math.abs(n);
            final int abs2 = Math.abs(a);
            boolean b;
            if (abs > abs2) {
                b = true;
            }
            else {
                b = false;
            }
            n2 = (int)Math.sqrt(n2 * n2 + n3 * n3);
            a = (int)Math.sqrt(n * n + a * a);
            if (b) {
                n = RecyclerView.this.getWidth();
            }
            else {
                n = RecyclerView.this.getHeight();
            }
            n3 = n / 2;
            final float min = Math.min(1.0f, 1.0f * a / n);
            final float n4 = (float)n3;
            final float n5 = (float)n3;
            final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
            if (n2 > 0) {
                n = Math.round(1000.0f * Math.abs((n4 + n5 * distanceInfluenceForSnapDuration) / n2)) * 4;
            }
            else {
                if (b) {
                    a = abs;
                }
                else {
                    a = abs2;
                }
                n = (int)((a / (float)n + 1.0f) * 300.0f);
            }
            return Math.min(n, 2000);
        }
        
        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }
        
        private float distanceInfluenceForSnapDuration(final float n) {
            return (float)Math.sin((float)((n - 0.5f) * 0.4712389167638204));
        }
        
        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                this.postOnAnimation();
            }
        }
        
        public void fling(final int n, final int n2) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, n, n2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.postOnAnimation();
        }
        
        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
            }
            else {
                RecyclerView.this.removeCallbacks((Runnable)this);
                ViewCompat.postOnAnimation((View)RecyclerView.this, this);
            }
        }
        
        @Override
        public void run() {
            if (RecyclerView.this.mLayout == null) {
                this.stop();
            }
            else {
                this.disableRunOnAnimationRequests();
                RecyclerView.this.consumePendingUpdateOperations();
                final ScrollerCompat mScroller = this.mScroller;
                final SmoothScroller mSmoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
                if (mScroller.computeScrollOffset()) {
                    final int currX = mScroller.getCurrX();
                    final int currY = mScroller.getCurrY();
                    final int n = currX - this.mLastFlingX;
                    final int n2 = currY - this.mLastFlingY;
                    int n3 = 0;
                    int scrollHorizontallyBy = 0;
                    int n4 = 0;
                    int scrollVerticallyBy = 0;
                    this.mLastFlingX = currX;
                    this.mLastFlingY = currY;
                    int n5 = 0;
                    int n6 = 0;
                    int n7 = 0;
                    int n8 = 0;
                    if (RecyclerView.this.mAdapter != null) {
                        RecyclerView.this.eatRequestLayout();
                        RecyclerView.this.onEnterLayoutOrScroll();
                        TraceCompat.beginSection("RV Scroll");
                        if (n != 0) {
                            scrollHorizontallyBy = RecyclerView.this.mLayout.scrollHorizontallyBy(n, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                            n6 = n - scrollHorizontallyBy;
                        }
                        if (n2 != 0) {
                            scrollVerticallyBy = RecyclerView.this.mLayout.scrollVerticallyBy(n2, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                            n8 = n2 - scrollVerticallyBy;
                        }
                        TraceCompat.endSection();
                        RecyclerView.this.repositionShadowingViews();
                        RecyclerView.this.onExitLayoutOrScroll();
                        RecyclerView.this.resumeRequestLayout(false);
                        n3 = scrollHorizontallyBy;
                        n5 = n6;
                        n7 = n8;
                        n4 = scrollVerticallyBy;
                        if (mSmoothScroller != null) {
                            n3 = scrollHorizontallyBy;
                            n5 = n6;
                            n7 = n8;
                            n4 = scrollVerticallyBy;
                            if (!mSmoothScroller.isPendingInitialRun()) {
                                n3 = scrollHorizontallyBy;
                                n5 = n6;
                                n7 = n8;
                                n4 = scrollVerticallyBy;
                                if (mSmoothScroller.isRunning()) {
                                    final int itemCount = RecyclerView.this.mState.getItemCount();
                                    if (itemCount == 0) {
                                        mSmoothScroller.stop();
                                        n4 = scrollVerticallyBy;
                                        n7 = n8;
                                        n5 = n6;
                                        n3 = scrollHorizontallyBy;
                                    }
                                    else if (mSmoothScroller.getTargetPosition() >= itemCount) {
                                        mSmoothScroller.setTargetPosition(itemCount - 1);
                                        mSmoothScroller.onAnimation(n - n6, n2 - n8);
                                        n3 = scrollHorizontallyBy;
                                        n5 = n6;
                                        n7 = n8;
                                        n4 = scrollVerticallyBy;
                                    }
                                    else {
                                        mSmoothScroller.onAnimation(n - n6, n2 - n8);
                                        n3 = scrollHorizontallyBy;
                                        n5 = n6;
                                        n7 = n8;
                                        n4 = scrollVerticallyBy;
                                    }
                                }
                            }
                        }
                    }
                    if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                        RecyclerView.this.invalidate();
                    }
                    if (ViewCompat.getOverScrollMode((View)RecyclerView.this) != 2) {
                        RecyclerView.this.considerReleasingGlowsOnScroll(n, n2);
                    }
                    if (n5 != 0 || n7 != 0) {
                        final int n9 = (int)mScroller.getCurrVelocity();
                        int n10 = 0;
                        if (n5 != currX) {
                            if (n5 < 0) {
                                n10 = -n9;
                            }
                            else if (n5 > 0) {
                                n10 = n9;
                            }
                            else {
                                n10 = 0;
                            }
                        }
                        int n11 = 0;
                        if (n7 != currY) {
                            if (n7 < 0) {
                                n11 = -n9;
                            }
                            else if (n7 > 0) {
                                n11 = n9;
                            }
                            else {
                                n11 = 0;
                            }
                        }
                        if (ViewCompat.getOverScrollMode((View)RecyclerView.this) != 2) {
                            RecyclerView.this.absorbGlows(n10, n11);
                        }
                        if ((n10 != 0 || n5 == currX || mScroller.getFinalX() == 0) && (n11 != 0 || n7 == currY || mScroller.getFinalY() == 0)) {
                            mScroller.abortAnimation();
                        }
                    }
                    if (n3 != 0 || n4 != 0) {
                        RecyclerView.this.dispatchOnScrolled(n3, n4);
                    }
                    if (!RecyclerView.access$3800(RecyclerView.this)) {
                        RecyclerView.this.invalidate();
                    }
                    boolean b;
                    if (n2 != 0 && RecyclerView.this.mLayout.canScrollVertically() && n4 == n2) {
                        b = true;
                    }
                    else {
                        b = false;
                    }
                    boolean b2;
                    if (n != 0 && RecyclerView.this.mLayout.canScrollHorizontally() && n3 == n) {
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    boolean b3;
                    if ((n == 0 && n2 == 0) || b2 || b) {
                        b3 = true;
                    }
                    else {
                        b3 = false;
                    }
                    if (mScroller.isFinished() || !b3) {
                        RecyclerView.this.setScrollState(0);
                    }
                    else {
                        this.postOnAnimation();
                    }
                }
                if (mSmoothScroller != null) {
                    if (mSmoothScroller.isPendingInitialRun()) {
                        mSmoothScroller.onAnimation(0, 0);
                    }
                    if (!this.mReSchedulePostAnimationCallback) {
                        mSmoothScroller.stop();
                    }
                }
                this.enableRunOnAnimationRequests();
            }
        }
        
        public void smoothScrollBy(final int n, final int n2) {
            this.smoothScrollBy(n, n2, 0, 0);
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3) {
            this.smoothScrollBy(n, n2, n3, RecyclerView.sQuinticInterpolator);
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3, final int n4) {
            this.smoothScrollBy(n, n2, this.computeScrollDuration(n, n2, n3, n4));
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3, final Interpolator mInterpolator) {
            if (this.mInterpolator != mInterpolator) {
                this.mInterpolator = mInterpolator;
                this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), mInterpolator);
            }
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, n, n2, n3);
            this.postOnAnimation();
        }
        
        public void stop() {
            RecyclerView.this.removeCallbacks((Runnable)this);
            this.mScroller.abortAnimation();
        }
    }
    
    public abstract static class ViewHolder
    {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS;
        public final View itemView;
        private int mFlags;
        private boolean mInChangeScrap;
        private int mIsRecyclableCount;
        long mItemId;
        int mItemViewType;
        int mOldPosition;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads;
        int mPosition;
        int mPreLayoutPosition;
        private Recycler mScrapContainer;
        ViewHolder mShadowedHolder;
        ViewHolder mShadowingHolder;
        List<Object> mUnmodifiedPayloads;
        private int mWasImportantForAccessibilityBeforeHidden;
        
        static {
            FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST;
        }
        
        public ViewHolder(final View itemView) {
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mItemViewType = -1;
            this.mPreLayoutPosition = -1;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.mPayloads = null;
            this.mUnmodifiedPayloads = null;
            this.mIsRecyclableCount = 0;
            this.mScrapContainer = null;
            this.mInChangeScrap = false;
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }
        
        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList<Object>();
                this.mUnmodifiedPayloads = Collections.unmodifiableList((List<?>)this.mPayloads);
            }
        }
        
        private boolean doesTransientStatePreventRecycling() {
            return (this.mFlags & 0x10) == 0x0 && ViewCompat.hasTransientState(this.itemView);
        }
        
        private void onEnteredHiddenState() {
            this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            ViewCompat.setImportantForAccessibility(this.itemView, 4);
        }
        
        private void onLeftHiddenState() {
            ViewCompat.setImportantForAccessibility(this.itemView, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }
        
        private boolean shouldBeKeptAsChild() {
            return (this.mFlags & 0x10) != 0x0;
        }
        
        void addChangePayload(final Object o) {
            if (o == null) {
                this.addFlags(1024);
            }
            else if ((this.mFlags & 0x400) == 0x0) {
                this.createPayloadsIfNeeded();
                this.mPayloads.add(o);
            }
        }
        
        void addFlags(final int n) {
            this.mFlags |= n;
        }
        
        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }
        
        void clearPayload() {
            if (this.mPayloads != null) {
                this.mPayloads.clear();
            }
            this.mFlags &= 0xFFFFFBFF;
        }
        
        void clearReturnedFromScrapFlag() {
            this.mFlags &= 0xFFFFFFDF;
        }
        
        void clearTmpDetachFlag() {
            this.mFlags &= 0xFFFFFEFF;
        }
        
        void flagRemovedAndOffsetPosition(final int mPosition, final int n, final boolean b) {
            this.addFlags(8);
            this.offsetPosition(n, b);
            this.mPosition = mPosition;
        }
        
        public final int getAdapterPosition() {
            int access$5700;
            if (this.mOwnerRecyclerView == null) {
                access$5700 = -1;
            }
            else {
                access$5700 = this.mOwnerRecyclerView.getAdapterPositionFor(this);
            }
            return access$5700;
        }
        
        public final long getItemId() {
            return this.mItemId;
        }
        
        public final int getItemViewType() {
            return this.mItemViewType;
        }
        
        public final int getLayoutPosition() {
            int n;
            if (this.mPreLayoutPosition == -1) {
                n = this.mPosition;
            }
            else {
                n = this.mPreLayoutPosition;
            }
            return n;
        }
        
        public final int getOldPosition() {
            return this.mOldPosition;
        }
        
        @Deprecated
        public final int getPosition() {
            int n;
            if (this.mPreLayoutPosition == -1) {
                n = this.mPosition;
            }
            else {
                n = this.mPreLayoutPosition;
            }
            return n;
        }
        
        List<Object> getUnmodifiedPayloads() {
            List<Object> list;
            if ((this.mFlags & 0x400) == 0x0) {
                if (this.mPayloads == null || this.mPayloads.size() == 0) {
                    list = ViewHolder.FULLUPDATE_PAYLOADS;
                }
                else {
                    list = this.mUnmodifiedPayloads;
                }
            }
            else {
                list = ViewHolder.FULLUPDATE_PAYLOADS;
            }
            return list;
        }
        
        boolean hasAnyOfTheFlags(final int n) {
            return (this.mFlags & n) != 0x0;
        }
        
        boolean isAdapterPositionUnknown() {
            return (this.mFlags & 0x200) != 0x0 || this.isInvalid();
        }
        
        boolean isBound() {
            return (this.mFlags & 0x1) != 0x0;
        }
        
        boolean isInvalid() {
            return (this.mFlags & 0x4) != 0x0;
        }
        
        public final boolean isRecyclable() {
            return (this.mFlags & 0x10) == 0x0 && !ViewCompat.hasTransientState(this.itemView);
        }
        
        boolean isRemoved() {
            return (this.mFlags & 0x8) != 0x0;
        }
        
        boolean isScrap() {
            return this.mScrapContainer != null;
        }
        
        boolean isTmpDetached() {
            return (this.mFlags & 0x100) != 0x0;
        }
        
        boolean isUpdated() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        boolean needsUpdate() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        void offsetPosition(final int n, final boolean b) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (b) {
                this.mPreLayoutPosition += n;
            }
            this.mPosition += n;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }
        
        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }
        
        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }
        
        void setFlags(final int n, final int n2) {
            this.mFlags = ((this.mFlags & ~n2) | (n & n2));
        }
        
        public final void setIsRecyclable(final boolean b) {
            int mIsRecyclableCount;
            if (b) {
                mIsRecyclableCount = this.mIsRecyclableCount - 1;
            }
            else {
                mIsRecyclableCount = this.mIsRecyclableCount + 1;
            }
            this.mIsRecyclableCount = mIsRecyclableCount;
            if (this.mIsRecyclableCount < 0) {
                this.mIsRecyclableCount = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            }
            else if (!b && this.mIsRecyclableCount == 1) {
                this.mFlags |= 0x10;
            }
            else if (b && this.mIsRecyclableCount == 0) {
                this.mFlags &= 0xFFFFFFEF;
            }
        }
        
        void setScrapContainer(final Recycler mScrapContainer, final boolean mInChangeScrap) {
            this.mScrapContainer = mScrapContainer;
            this.mInChangeScrap = mInChangeScrap;
        }
        
        boolean shouldIgnore() {
            return (this.mFlags & 0x80) != 0x0;
        }
        
        void stopIgnoring() {
            this.mFlags &= 0xFFFFFF7F;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ViewHolder{" + Integer.toHexString(this.hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
            if (this.isScrap()) {
                final StringBuilder append = sb.append(" scrap ");
                String str;
                if (this.mInChangeScrap) {
                    str = "[changeScrap]";
                }
                else {
                    str = "[attachedScrap]";
                }
                append.append(str);
            }
            if (this.isInvalid()) {
                sb.append(" invalid");
            }
            if (!this.isBound()) {
                sb.append(" unbound");
            }
            if (this.needsUpdate()) {
                sb.append(" update");
            }
            if (this.isRemoved()) {
                sb.append(" removed");
            }
            if (this.shouldIgnore()) {
                sb.append(" ignored");
            }
            if (this.isTmpDetached()) {
                sb.append(" tmpDetached");
            }
            if (!this.isRecyclable()) {
                sb.append(" not recyclable(" + this.mIsRecyclableCount + ")");
            }
            if (this.isAdapterPositionUnknown()) {
                sb.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }
        
        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }
        
        boolean wasReturnedFromScrap() {
            return (this.mFlags & 0x20) != 0x0;
        }
    }
}
