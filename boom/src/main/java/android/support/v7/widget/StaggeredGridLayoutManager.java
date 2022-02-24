// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import android.graphics.PointF;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.accessibility.AccessibilityEvent;
import android.support.annotation.Nullable;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import java.util.BitSet;

public class StaggeredGridLayoutManager extends LayoutManager
{
    private static final boolean DEBUG = false;
    @Deprecated
    public static final int GAP_HANDLING_LAZY = 1;
    public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
    public static final int GAP_HANDLING_NONE = 0;
    public static final int HORIZONTAL = 0;
    private static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    public static final String TAG = "StaggeredGridLayoutManager";
    public static final int VERTICAL = 1;
    private final AnchorInfo mAnchorInfo;
    private final Runnable mCheckForGapsRunnable;
    private int mFullSizeSpec;
    private int mGapStrategy;
    private boolean mLaidOutInvalidFullSpan;
    private boolean mLastLayoutFromEnd;
    private boolean mLastLayoutRTL;
    private LayoutState mLayoutState;
    LazySpanLookup mLazySpanLookup;
    private int mOrientation;
    private SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    OrientationHelper mPrimaryOrientation;
    private BitSet mRemainingSpans;
    private boolean mReverseLayout;
    OrientationHelper mSecondaryOrientation;
    boolean mShouldReverseLayout;
    private int mSizePerSpan;
    private boolean mSmoothScrollbarEnabled;
    private int mSpanCount;
    private Span[] mSpans;
    private final Rect mTmpRect;
    
    public StaggeredGridLayoutManager(final int spanCount, final int mOrientation) {
        boolean autoMeasureEnabled = true;
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable() {
            @Override
            public void run() {
                StaggeredGridLayoutManager.this.checkForGaps();
            }
        };
        this.mOrientation = mOrientation;
        this.setSpanCount(spanCount);
        if (this.mGapStrategy == 0) {
            autoMeasureEnabled = false;
        }
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(autoMeasureEnabled);
    }
    
    public StaggeredGridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        boolean autoMeasureEnabled = true;
        this.mSpanCount = -1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mLazySpanLookup = new LazySpanLookup();
        this.mGapStrategy = 2;
        this.mTmpRect = new Rect();
        this.mAnchorInfo = new AnchorInfo();
        this.mLaidOutInvalidFullSpan = false;
        this.mSmoothScrollbarEnabled = true;
        this.mCheckForGapsRunnable = new Runnable() {
            @Override
            public void run() {
                StaggeredGridLayoutManager.this.checkForGaps();
            }
        };
        final Properties properties = RecyclerView.LayoutManager.getProperties(context, set, n, n2);
        this.setOrientation(properties.orientation);
        this.setSpanCount(properties.spanCount);
        this.setReverseLayout(properties.reverseLayout);
        if (this.mGapStrategy == 0) {
            autoMeasureEnabled = false;
        }
        ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(autoMeasureEnabled);
    }
    
    private void appendViewToAllSpans(final View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].appendToSpan(view);
        }
    }
    
    private void applyPendingSavedState(final AnchorInfo anchorInfo) {
        if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
            if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    this.mSpans[i].clear();
                    final int n = this.mPendingSavedState.mSpanOffsets[i];
                    int line;
                    if ((line = n) != Integer.MIN_VALUE) {
                        if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
                            line = n + this.mPrimaryOrientation.getEndAfterPadding();
                        }
                        else {
                            line = n + this.mPrimaryOrientation.getStartAfterPadding();
                        }
                    }
                    this.mSpans[i].setLine(line);
                }
            }
            else {
                this.mPendingSavedState.invalidateSpanInfo();
                this.mPendingSavedState.mAnchorPosition = this.mPendingSavedState.mVisibleAnchorPosition;
            }
        }
        this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
        this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
        this.resolveShouldLayoutReverse();
        if (this.mPendingSavedState.mAnchorPosition != -1) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
        }
        else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        }
        if (this.mPendingSavedState.mSpanLookupSize > 1) {
            this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
            this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
        }
    }
    
    private void attachViewToSpans(final View view, final LayoutParams layoutParams, final LayoutState layoutState) {
        if (layoutState.mLayoutDirection == 1) {
            if (layoutParams.mFullSpan) {
                this.appendViewToAllSpans(view);
            }
            else {
                layoutParams.mSpan.appendToSpan(view);
            }
        }
        else if (layoutParams.mFullSpan) {
            this.prependViewToAllSpans(view);
        }
        else {
            layoutParams.mSpan.prependToSpan(view);
        }
    }
    
    private int calculateScrollDirectionForPosition(int n) {
        final int n2 = -1;
        final int n3 = 1;
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            if (this.mShouldReverseLayout) {
                n = n3;
            }
            else {
                n = -1;
            }
        }
        else if (n < this.getFirstChildPosition() != this.mShouldReverseLayout) {
            n = n2;
        }
        else {
            n = 1;
        }
        return n;
    }
    
    private boolean checkForGaps() {
        boolean b = true;
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0 || this.mGapStrategy == 0 || !((RecyclerView.LayoutManager)this).isAttachedToWindow()) {
            b = false;
        }
        else {
            int n;
            int n2;
            if (this.mShouldReverseLayout) {
                n = this.getLastChildPosition();
                n2 = this.getFirstChildPosition();
            }
            else {
                n = this.getFirstChildPosition();
                n2 = this.getLastChildPosition();
            }
            if (n == 0 && this.hasGapsToFix() != null) {
                this.mLazySpanLookup.clear();
                ((RecyclerView.LayoutManager)this).requestSimpleAnimationsInNextLayout();
                ((RecyclerView.LayoutManager)this).requestLayout();
            }
            else if (!this.mLaidOutInvalidFullSpan) {
                b = false;
            }
            else {
                int n3;
                if (this.mShouldReverseLayout) {
                    n3 = -1;
                }
                else {
                    n3 = 1;
                }
                final FullSpanItem firstFullSpanItemInRange = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, n2 + 1, n3, true);
                if (firstFullSpanItemInRange == null) {
                    this.mLaidOutInvalidFullSpan = false;
                    this.mLazySpanLookup.forceInvalidateAfter(n2 + 1);
                    b = false;
                }
                else {
                    final FullSpanItem firstFullSpanItemInRange2 = this.mLazySpanLookup.getFirstFullSpanItemInRange(n, firstFullSpanItemInRange.mPosition, n3 * -1, true);
                    if (firstFullSpanItemInRange2 == null) {
                        this.mLazySpanLookup.forceInvalidateAfter(firstFullSpanItemInRange.mPosition);
                    }
                    else {
                        this.mLazySpanLookup.forceInvalidateAfter(firstFullSpanItemInRange2.mPosition + 1);
                    }
                    ((RecyclerView.LayoutManager)this).requestSimpleAnimationsInNextLayout();
                    ((RecyclerView.LayoutManager)this).requestLayout();
                }
            }
        }
        return b;
    }
    
    private boolean checkSpanForGap(final Span span) {
        boolean b = true;
        if (this.mShouldReverseLayout) {
            if (span.getEndLine() >= this.mPrimaryOrientation.getEndAfterPadding()) {
                return false;
            }
            if (span.getLayoutParams(span.mViews.get(span.mViews.size() - 1)).mFullSpan) {
                b = false;
            }
        }
        else {
            if (span.getStartLine() <= this.mPrimaryOrientation.getStartAfterPadding()) {
                return false;
            }
            if (span.getLayoutParams(span.mViews.get(0)).mFullSpan) {
                b = false;
            }
        }
        return b;
        b = false;
        return b;
    }
    
    private int computeScrollExtent(final State state) {
        final boolean b = false;
        int computeScrollExtent = 0;
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0) {
            this.ensureOrientationHelper();
            final OrientationHelper mPrimaryOrientation = this.mPrimaryOrientation;
            final View firstVisibleItemClosestToStart = this.findFirstVisibleItemClosestToStart(!this.mSmoothScrollbarEnabled, true);
            boolean b2 = b;
            if (!this.mSmoothScrollbarEnabled) {
                b2 = true;
            }
            computeScrollExtent = ScrollbarHelper.computeScrollExtent(state, mPrimaryOrientation, firstVisibleItemClosestToStart, this.findFirstVisibleItemClosestToEnd(b2, true), this, this.mSmoothScrollbarEnabled);
        }
        return computeScrollExtent;
    }
    
    private int computeScrollOffset(final State state) {
        final boolean b = false;
        int computeScrollOffset = 0;
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0) {
            this.ensureOrientationHelper();
            final OrientationHelper mPrimaryOrientation = this.mPrimaryOrientation;
            final View firstVisibleItemClosestToStart = this.findFirstVisibleItemClosestToStart(!this.mSmoothScrollbarEnabled, true);
            boolean b2 = b;
            if (!this.mSmoothScrollbarEnabled) {
                b2 = true;
            }
            computeScrollOffset = ScrollbarHelper.computeScrollOffset(state, mPrimaryOrientation, firstVisibleItemClosestToStart, this.findFirstVisibleItemClosestToEnd(b2, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
        }
        return computeScrollOffset;
    }
    
    private int computeScrollRange(final State state) {
        final boolean b = false;
        int computeScrollRange = 0;
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0) {
            this.ensureOrientationHelper();
            final OrientationHelper mPrimaryOrientation = this.mPrimaryOrientation;
            final View firstVisibleItemClosestToStart = this.findFirstVisibleItemClosestToStart(!this.mSmoothScrollbarEnabled, true);
            boolean b2 = b;
            if (!this.mSmoothScrollbarEnabled) {
                b2 = true;
            }
            computeScrollRange = ScrollbarHelper.computeScrollRange(state, mPrimaryOrientation, firstVisibleItemClosestToStart, this.findFirstVisibleItemClosestToEnd(b2, true), this, this.mSmoothScrollbarEnabled);
        }
        return computeScrollRange;
    }
    
    private int convertFocusDirectionToLayoutDirection(int n) {
        final int n2 = -1;
        final int n3 = 1;
        final int n4 = Integer.MIN_VALUE;
        int n5 = n2;
        switch (n) {
            default: {
                n5 = Integer.MIN_VALUE;
                return n5;
            }
            case 66: {
                if (this.mOrientation == 0) {
                    n = n3;
                }
                else {
                    n = Integer.MIN_VALUE;
                }
                n5 = n;
                return n5;
            }
            case 130: {
                n = n4;
                if (this.mOrientation == 1) {
                    n = 1;
                }
                n5 = n;
                return n5;
            }
            case 2: {
                n5 = 1;
            }
            case 1: {
                return n5;
            }
            case 33: {
                n5 = n2;
                if (this.mOrientation != 1) {
                    n5 = Integer.MIN_VALUE;
                    return n5;
                }
                return n5;
            }
            case 17: {
                n5 = n2;
                if (this.mOrientation != 0) {
                    n5 = Integer.MIN_VALUE;
                    return n5;
                }
                return n5;
            }
        }
    }
    
    private FullSpanItem createFullSpanItemFromEnd(final int n) {
        final FullSpanItem fullSpanItem = new FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = n - this.mSpans[i].getEndLine(n);
        }
        return fullSpanItem;
    }
    
    private FullSpanItem createFullSpanItemFromStart(final int n) {
        final FullSpanItem fullSpanItem = new FullSpanItem();
        fullSpanItem.mGapPerSpan = new int[this.mSpanCount];
        for (int i = 0; i < this.mSpanCount; ++i) {
            fullSpanItem.mGapPerSpan[i] = this.mSpans[i].getStartLine(n) - n;
        }
        return fullSpanItem;
    }
    
    private void ensureOrientationHelper() {
        if (this.mPrimaryOrientation == null) {
            this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
            this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
            this.mLayoutState = new LayoutState();
        }
    }
    
    private int fill(final Recycler recycler, final LayoutState layoutState, final State state) {
        this.mRemainingSpans.set(0, this.mSpanCount, true);
        int n;
        if (this.mLayoutState.mInfinite) {
            if (layoutState.mLayoutDirection == 1) {
                n = Integer.MAX_VALUE;
            }
            else {
                n = Integer.MIN_VALUE;
            }
        }
        else if (layoutState.mLayoutDirection == 1) {
            n = layoutState.mEndLine + layoutState.mAvailable;
        }
        else {
            n = layoutState.mStartLine - layoutState.mAvailable;
        }
        this.updateAllRemainingSpans(layoutState.mLayoutDirection, n);
        int n2;
        if (this.mShouldReverseLayout) {
            n2 = this.mPrimaryOrientation.getEndAfterPadding();
        }
        else {
            n2 = this.mPrimaryOrientation.getStartAfterPadding();
        }
        boolean b = false;
        while (layoutState.hasMore(state) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty())) {
            final View next = layoutState.next(recycler);
            final LayoutParams layoutParams = (LayoutParams)next.getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            final int span = this.mLazySpanLookup.getSpan(viewLayoutPosition);
            boolean b2;
            if (span == -1) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            Span nextSpan;
            if (b2) {
                if (layoutParams.mFullSpan) {
                    nextSpan = this.mSpans[0];
                }
                else {
                    nextSpan = this.getNextSpan(layoutState);
                }
                this.mLazySpanLookup.setSpan(viewLayoutPosition, nextSpan);
            }
            else {
                nextSpan = this.mSpans[span];
            }
            layoutParams.mSpan = nextSpan;
            if (layoutState.mLayoutDirection == 1) {
                ((RecyclerView.LayoutManager)this).addView(next);
            }
            else {
                ((RecyclerView.LayoutManager)this).addView(next, 0);
            }
            this.measureChildWithDecorationsAndMargin(next, layoutParams, false);
            int n5;
            int n6;
            if (layoutState.mLayoutDirection == 1) {
                int n3;
                if (layoutParams.mFullSpan) {
                    n3 = this.getMaxEnd(n2);
                }
                else {
                    n3 = nextSpan.getEndLine(n2);
                }
                final int n4 = n3 + this.mPrimaryOrientation.getDecoratedMeasurement(next);
                n5 = n3;
                n6 = n4;
                if (b2) {
                    n5 = n3;
                    n6 = n4;
                    if (layoutParams.mFullSpan) {
                        final FullSpanItem fullSpanItemFromEnd = this.createFullSpanItemFromEnd(n3);
                        fullSpanItemFromEnd.mGapDir = -1;
                        fullSpanItemFromEnd.mPosition = viewLayoutPosition;
                        this.mLazySpanLookup.addFullSpanItem(fullSpanItemFromEnd);
                        n6 = n4;
                        n5 = n3;
                    }
                }
            }
            else {
                int n7;
                if (layoutParams.mFullSpan) {
                    n7 = this.getMinStart(n2);
                }
                else {
                    n7 = nextSpan.getStartLine(n2);
                }
                final int n8 = n5 = n7 - this.mPrimaryOrientation.getDecoratedMeasurement(next);
                n6 = n7;
                if (b2) {
                    n5 = n8;
                    n6 = n7;
                    if (layoutParams.mFullSpan) {
                        final FullSpanItem fullSpanItemFromStart = this.createFullSpanItemFromStart(n7);
                        fullSpanItemFromStart.mGapDir = 1;
                        fullSpanItemFromStart.mPosition = viewLayoutPosition;
                        this.mLazySpanLookup.addFullSpanItem(fullSpanItemFromStart);
                        n5 = n8;
                        n6 = n7;
                    }
                }
            }
            if (layoutParams.mFullSpan && layoutState.mItemDirection == -1) {
                if (b2) {
                    this.mLaidOutInvalidFullSpan = true;
                }
                else {
                    int n9;
                    if (layoutState.mLayoutDirection == 1) {
                        if (!this.areAllEndsEqual()) {
                            n9 = 1;
                        }
                        else {
                            n9 = 0;
                        }
                    }
                    else if (!this.areAllStartsEqual()) {
                        n9 = 1;
                    }
                    else {
                        n9 = 0;
                    }
                    if (n9 != 0) {
                        final FullSpanItem fullSpanItem = this.mLazySpanLookup.getFullSpanItem(viewLayoutPosition);
                        if (fullSpanItem != null) {
                            fullSpanItem.mHasUnwantedGapAfter = true;
                        }
                        this.mLaidOutInvalidFullSpan = true;
                    }
                }
            }
            this.attachViewToSpans(next, layoutParams, layoutState);
            int endAfterPadding;
            int n10;
            if (this.isLayoutRTL() && this.mOrientation == 1) {
                if (layoutParams.mFullSpan) {
                    endAfterPadding = this.mSecondaryOrientation.getEndAfterPadding();
                }
                else {
                    endAfterPadding = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - nextSpan.mIndex) * this.mSizePerSpan;
                }
                n10 = endAfterPadding - this.mSecondaryOrientation.getDecoratedMeasurement(next);
            }
            else {
                int startAfterPadding;
                if (layoutParams.mFullSpan) {
                    startAfterPadding = this.mSecondaryOrientation.getStartAfterPadding();
                }
                else {
                    startAfterPadding = nextSpan.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
                }
                final int n11 = startAfterPadding + this.mSecondaryOrientation.getDecoratedMeasurement(next);
                n10 = startAfterPadding;
                endAfterPadding = n11;
            }
            if (this.mOrientation == 1) {
                this.layoutDecoratedWithMargins(next, n10, n5, endAfterPadding, n6);
            }
            else {
                this.layoutDecoratedWithMargins(next, n5, n10, n6, endAfterPadding);
            }
            if (layoutParams.mFullSpan) {
                this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, n);
            }
            else {
                this.updateRemainingSpans(nextSpan, this.mLayoutState.mLayoutDirection, n);
            }
            this.recycle(recycler, this.mLayoutState);
            if (this.mLayoutState.mStopInFocusable && next.isFocusable()) {
                if (layoutParams.mFullSpan) {
                    this.mRemainingSpans.clear();
                }
                else {
                    this.mRemainingSpans.set(nextSpan.mIndex, false);
                }
            }
            b = true;
        }
        if (!b) {
            this.recycle(recycler, this.mLayoutState);
        }
        int b3;
        if (this.mLayoutState.mLayoutDirection == -1) {
            b3 = this.mPrimaryOrientation.getStartAfterPadding() - this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
        }
        else {
            b3 = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
        }
        int min;
        if (b3 > 0) {
            min = Math.min(layoutState.mAvailable, b3);
        }
        else {
            min = 0;
        }
        return min;
    }
    
    private int findFirstReferenceChildPosition(int n) {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(i));
            if (position >= 0 && position < n) {
                n = position;
                return n;
            }
        }
        n = 0;
        return n;
    }
    
    private int findLastReferenceChildPosition(int n) {
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final int position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(i));
            if (position >= 0 && position < n) {
                n = position;
                return n;
            }
        }
        n = 0;
        return n;
    }
    
    private void fixEndGap(final Recycler recycler, final State state, final boolean b) {
        final int maxEnd = this.getMaxEnd(Integer.MIN_VALUE);
        if (maxEnd != Integer.MIN_VALUE) {
            final int n = this.mPrimaryOrientation.getEndAfterPadding() - maxEnd;
            if (n > 0) {
                final int n2 = n - -this.scrollBy(-n, recycler, state);
                if (b && n2 > 0) {
                    this.mPrimaryOrientation.offsetChildren(n2);
                }
            }
        }
    }
    
    private void fixStartGap(final Recycler recycler, final State state, final boolean b) {
        final int minStart = this.getMinStart(Integer.MAX_VALUE);
        if (minStart != Integer.MAX_VALUE) {
            final int n = minStart - this.mPrimaryOrientation.getStartAfterPadding();
            if (n > 0) {
                final int n2 = n - this.scrollBy(n, recycler, state);
                if (b && n2 > 0) {
                    this.mPrimaryOrientation.offsetChildren(-n2);
                }
            }
        }
    }
    
    private int getFirstChildPosition() {
        int position = 0;
        if (((RecyclerView.LayoutManager)this).getChildCount() != 0) {
            position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(0));
        }
        return position;
    }
    
    private int getLastChildPosition() {
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        int position;
        if (childCount == 0) {
            position = 0;
        }
        else {
            position = ((RecyclerView.LayoutManager)this).getPosition(((RecyclerView.LayoutManager)this).getChildAt(childCount - 1));
        }
        return position;
    }
    
    private int getMaxEnd(final int n) {
        int endLine = this.mSpans[0].getEndLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, endLine = n2) {
            final int endLine2 = this.mSpans[i].getEndLine(n);
            if (endLine2 > (n2 = endLine)) {
                n2 = endLine2;
            }
        }
        return endLine;
    }
    
    private int getMaxStart(final int n) {
        int startLine = this.mSpans[0].getStartLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, startLine = n2) {
            final int startLine2 = this.mSpans[i].getStartLine(n);
            if (startLine2 > (n2 = startLine)) {
                n2 = startLine2;
            }
        }
        return startLine;
    }
    
    private int getMinEnd(final int n) {
        int endLine = this.mSpans[0].getEndLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, endLine = n2) {
            final int endLine2 = this.mSpans[i].getEndLine(n);
            if (endLine2 < (n2 = endLine)) {
                n2 = endLine2;
            }
        }
        return endLine;
    }
    
    private int getMinStart(final int n) {
        int startLine = this.mSpans[0].getStartLine(n);
        int n2;
        for (int i = 1; i < this.mSpanCount; ++i, startLine = n2) {
            final int startLine2 = this.mSpans[i].getStartLine(n);
            if (startLine2 < (n2 = startLine)) {
                n2 = startLine2;
            }
        }
        return startLine;
    }
    
    private Span getNextSpan(final LayoutState layoutState) {
        int n;
        int mSpanCount;
        int n2;
        if (this.preferLastSpan(layoutState.mLayoutDirection)) {
            n = this.mSpanCount - 1;
            mSpanCount = -1;
            n2 = -1;
        }
        else {
            n = 0;
            mSpanCount = this.mSpanCount;
            n2 = 1;
        }
        Span span2;
        if (layoutState.mLayoutDirection == 1) {
            Span span = null;
            int n3 = Integer.MAX_VALUE;
            final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
            while (true) {
                span2 = span;
                if (n == mSpanCount) {
                    break;
                }
                final Span span3 = this.mSpans[n];
                final int endLine = span3.getEndLine(startAfterPadding);
                int n4;
                if (endLine < (n4 = n3)) {
                    span = span3;
                    n4 = endLine;
                }
                n += n2;
                n3 = n4;
            }
        }
        else {
            Span span4 = null;
            int n5 = Integer.MIN_VALUE;
            final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
            int n6;
            for (int i = n; i != mSpanCount; i += n2, n5 = n6) {
                final Span span5 = this.mSpans[i];
                final int startLine = span5.getStartLine(endAfterPadding);
                if (startLine > (n6 = n5)) {
                    span4 = span5;
                    n6 = startLine;
                }
            }
            span2 = span4;
        }
        return span2;
    }
    
    private void handleUpdate(int n, final int n2, final int n3) {
        int n4;
        if (this.mShouldReverseLayout) {
            n4 = this.getLastChildPosition();
        }
        else {
            n4 = this.getFirstChildPosition();
        }
        int n5;
        int n6;
        if (n3 == 8) {
            if (n < n2) {
                n5 = n2 + 1;
                n6 = n;
            }
            else {
                n5 = n + 1;
                n6 = n2;
            }
        }
        else {
            n6 = n;
            n5 = n + n2;
        }
        this.mLazySpanLookup.invalidateAfter(n6);
        switch (n3) {
            case 1: {
                this.mLazySpanLookup.offsetForAddition(n, n2);
                break;
            }
            case 2: {
                this.mLazySpanLookup.offsetForRemoval(n, n2);
                break;
            }
            case 8: {
                this.mLazySpanLookup.offsetForRemoval(n, 1);
                this.mLazySpanLookup.offsetForAddition(n2, 1);
                break;
            }
        }
        if (n5 > n4) {
            if (this.mShouldReverseLayout) {
                n = this.getFirstChildPosition();
            }
            else {
                n = this.getLastChildPosition();
            }
            if (n6 <= n) {
                ((RecyclerView.LayoutManager)this).requestLayout();
            }
        }
    }
    
    private void layoutDecoratedWithMargins(final View view, final int n, final int n2, final int n3, final int n4) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        ((RecyclerView.LayoutManager)this).layoutDecorated(view, n + layoutParams.leftMargin, n2 + layoutParams.topMargin, n3 - layoutParams.rightMargin, n4 - layoutParams.bottomMargin);
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, int updateSpecWithExtra, int updateSpecWithExtra2, final boolean b) {
        ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mTmpRect);
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        updateSpecWithExtra = this.updateSpecWithExtra(updateSpecWithExtra, layoutParams.leftMargin + this.mTmpRect.left, layoutParams.rightMargin + this.mTmpRect.right);
        updateSpecWithExtra2 = this.updateSpecWithExtra(updateSpecWithExtra2, layoutParams.topMargin + this.mTmpRect.top, layoutParams.bottomMargin + this.mTmpRect.bottom);
        boolean b2;
        if (b) {
            b2 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, updateSpecWithExtra, updateSpecWithExtra2, layoutParams);
        }
        else {
            b2 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, updateSpecWithExtra, updateSpecWithExtra2, layoutParams);
        }
        if (b2) {
            view.measure(updateSpecWithExtra, updateSpecWithExtra2);
        }
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, final LayoutParams layoutParams, final boolean b) {
        if (layoutParams.mFullSpan) {
            if (this.mOrientation == 1) {
                this.measureChildWithDecorationsAndMargin(view, this.mFullSizeSpec, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getHeight(), ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, true), b);
            }
            else {
                this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getWidth(), ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, true), this.mFullSizeSpec, b);
            }
        }
        else if (this.mOrientation == 1) {
            this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(this.mSizePerSpan, ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, false), RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getHeight(), ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, true), b);
        }
        else {
            this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(((RecyclerView.LayoutManager)this).getWidth(), ((RecyclerView.LayoutManager)this).getWidthMode(), 0, layoutParams.width, true), RecyclerView.LayoutManager.getChildMeasureSpec(this.mSizePerSpan, ((RecyclerView.LayoutManager)this).getHeightMode(), 0, layoutParams.height, false), b);
        }
    }
    
    private void onLayoutChildren(final Recycler recycler, final State state, final boolean b) {
        final boolean b2 = true;
        this.ensureOrientationHelper();
        final AnchorInfo mAnchorInfo = this.mAnchorInfo;
        mAnchorInfo.reset();
        if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && state.getItemCount() == 0) {
            ((RecyclerView.LayoutManager)this).removeAndRecycleAllViews(recycler);
        }
        else {
            if (this.mPendingSavedState != null) {
                this.applyPendingSavedState(mAnchorInfo);
            }
            else {
                this.resolveShouldLayoutReverse();
                mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
            }
            this.updateAnchorInfoForLayout(state, mAnchorInfo);
            if (this.mPendingSavedState == null && (mAnchorInfo.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL)) {
                this.mLazySpanLookup.clear();
                mAnchorInfo.mInvalidateOffsets = true;
            }
            if (((RecyclerView.LayoutManager)this).getChildCount() > 0 && (this.mPendingSavedState == null || this.mPendingSavedState.mSpanOffsetsSize < 1)) {
                if (mAnchorInfo.mInvalidateOffsets) {
                    for (int i = 0; i < this.mSpanCount; ++i) {
                        this.mSpans[i].clear();
                        if (mAnchorInfo.mOffset != Integer.MIN_VALUE) {
                            this.mSpans[i].setLine(mAnchorInfo.mOffset);
                        }
                    }
                }
                else {
                    for (int j = 0; j < this.mSpanCount; ++j) {
                        this.mSpans[j].cacheReferenceLineAndClear(this.mShouldReverseLayout, mAnchorInfo.mOffset);
                    }
                }
            }
            ((RecyclerView.LayoutManager)this).detachAndScrapAttachedViews(recycler);
            this.mLayoutState.mRecycle = false;
            this.mLaidOutInvalidFullSpan = false;
            this.updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
            this.updateLayoutState(mAnchorInfo.mPosition, state);
            if (mAnchorInfo.mLayoutFromEnd) {
                this.setLayoutStateDirection(-1);
                this.fill(recycler, this.mLayoutState, state);
                this.setLayoutStateDirection(1);
                this.mLayoutState.mCurrentPosition = mAnchorInfo.mPosition + this.mLayoutState.mItemDirection;
                this.fill(recycler, this.mLayoutState, state);
            }
            else {
                this.setLayoutStateDirection(1);
                this.fill(recycler, this.mLayoutState, state);
                this.setLayoutStateDirection(-1);
                this.mLayoutState.mCurrentPosition = mAnchorInfo.mPosition + this.mLayoutState.mItemDirection;
                this.fill(recycler, this.mLayoutState, state);
            }
            this.repositionToWrapContentIfNecessary();
            if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
                if (this.mShouldReverseLayout) {
                    this.fixEndGap(recycler, state, true);
                    this.fixStartGap(recycler, state, false);
                }
                else {
                    this.fixStartGap(recycler, state, true);
                    this.fixEndGap(recycler, state, false);
                }
            }
            final boolean b3 = false;
            final boolean b4 = false;
            int n = b3 ? 1 : 0;
            Label_0483: {
                if (b) {
                    n = (b3 ? 1 : 0);
                    if (!state.isPreLayout()) {
                        while (true) {
                            Label_0592: {
                                if (this.mGapStrategy == 0 || ((RecyclerView.LayoutManager)this).getChildCount() <= 0) {
                                    break Label_0592;
                                }
                                int n2 = b2 ? 1 : 0;
                                if (!this.mLaidOutInvalidFullSpan) {
                                    if (this.hasGapsToFix() == null) {
                                        break Label_0592;
                                    }
                                    n2 = (b2 ? 1 : 0);
                                }
                                n = (b4 ? 1 : 0);
                                if (n2 != 0) {
                                    ((RecyclerView.LayoutManager)this).removeCallbacks(this.mCheckForGapsRunnable);
                                    n = (b4 ? 1 : 0);
                                    if (this.checkForGaps()) {
                                        n = 1;
                                    }
                                }
                                this.mPendingScrollPosition = -1;
                                this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
                                break Label_0483;
                            }
                            int n2 = 0;
                            continue;
                        }
                    }
                }
            }
            this.mLastLayoutFromEnd = mAnchorInfo.mLayoutFromEnd;
            this.mLastLayoutRTL = this.isLayoutRTL();
            this.mPendingSavedState = null;
            if (n != 0) {
                this.onLayoutChildren(recycler, state, false);
            }
        }
    }
    
    private boolean preferLastSpan(final int n) {
        final boolean b = true;
        boolean b2;
        if (this.mOrientation == 0) {
            b2 = (n == -1 != this.mShouldReverseLayout && b);
        }
        else {
            final boolean b3 = n == -1 == this.mShouldReverseLayout;
            b2 = b;
            if (b3 != this.isLayoutRTL()) {
                b2 = false;
            }
        }
        return b2;
    }
    
    private void prependViewToAllSpans(final View view) {
        for (int i = this.mSpanCount - 1; i >= 0; --i) {
            this.mSpans[i].prependToSpan(view);
        }
    }
    
    private void recycle(final Recycler recycler, final LayoutState layoutState) {
        if (layoutState.mRecycle && !layoutState.mInfinite) {
            if (layoutState.mAvailable == 0) {
                if (layoutState.mLayoutDirection == -1) {
                    this.recycleFromEnd(recycler, layoutState.mEndLine);
                }
                else {
                    this.recycleFromStart(recycler, layoutState.mStartLine);
                }
            }
            else if (layoutState.mLayoutDirection == -1) {
                final int a = layoutState.mStartLine - this.getMaxStart(layoutState.mStartLine);
                int mEndLine;
                if (a < 0) {
                    mEndLine = layoutState.mEndLine;
                }
                else {
                    mEndLine = layoutState.mEndLine - Math.min(a, layoutState.mAvailable);
                }
                this.recycleFromEnd(recycler, mEndLine);
            }
            else {
                final int a2 = this.getMinEnd(layoutState.mEndLine) - layoutState.mEndLine;
                int mStartLine;
                if (a2 < 0) {
                    mStartLine = layoutState.mStartLine;
                }
                else {
                    mStartLine = layoutState.mStartLine + Math.min(a2, layoutState.mAvailable);
                }
                this.recycleFromStart(recycler, mStartLine);
            }
        }
    }
    
    private void recycleFromEnd(final Recycler recycler, final int n) {
    Label_0078:
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (this.mPrimaryOrientation.getDecoratedStart(child) < n) {
                break;
            }
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.mFullSpan) {
                for (int j = 0; j < this.mSpanCount; ++j) {
                    if (this.mSpans[j].mViews.size() == 1) {
                        break Label_0078;
                    }
                }
                for (int k = 0; k < this.mSpanCount; ++k) {
                    this.mSpans[k].popEnd();
                }
            }
            else {
                if (layoutParams.mSpan.mViews.size() == 1) {
                    break;
                }
                layoutParams.mSpan.popEnd();
            }
            ((RecyclerView.LayoutManager)this).removeAndRecycleView(child, recycler);
        }
    }
    
    private void recycleFromStart(final Recycler recycler, final int n) {
    Label_0071:
        while (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(0);
            if (this.mPrimaryOrientation.getDecoratedEnd(child) > n) {
                break;
            }
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (layoutParams.mFullSpan) {
                for (int i = 0; i < this.mSpanCount; ++i) {
                    if (this.mSpans[i].mViews.size() == 1) {
                        break Label_0071;
                    }
                }
                for (int j = 0; j < this.mSpanCount; ++j) {
                    this.mSpans[j].popStart();
                }
            }
            else {
                if (layoutParams.mSpan.mViews.size() == 1) {
                    break;
                }
                layoutParams.mSpan.popStart();
            }
            ((RecyclerView.LayoutManager)this).removeAndRecycleView(child, recycler);
        }
    }
    
    private void repositionToWrapContentIfNecessary() {
        if (this.mSecondaryOrientation.getMode() != 1073741824) {
            float max = 0.0f;
            final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
            for (int i = 0; i < childCount; ++i) {
                final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                final float n = (float)this.mSecondaryOrientation.getDecoratedMeasurement(child);
                if (n >= max) {
                    float b = n;
                    if (((LayoutParams)child.getLayoutParams()).isFullSpan()) {
                        b = 1.0f * n / this.mSpanCount;
                    }
                    max = Math.max(max, b);
                }
            }
            final int mSizePerSpan = this.mSizePerSpan;
            int a = Math.round(this.mSpanCount * max);
            if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE) {
                a = Math.min(a, this.mSecondaryOrientation.getTotalSpace());
            }
            this.updateMeasureSpecs(a);
            if (this.mSizePerSpan != mSizePerSpan) {
                for (int j = 0; j < childCount; ++j) {
                    final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(j);
                    final LayoutParams layoutParams = (LayoutParams)child2.getLayoutParams();
                    if (!layoutParams.mFullSpan) {
                        if (this.isLayoutRTL() && this.mOrientation == 1) {
                            child2.offsetLeftAndRight(-(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - layoutParams.mSpan.mIndex) * mSizePerSpan);
                        }
                        else {
                            final int n2 = layoutParams.mSpan.mIndex * this.mSizePerSpan;
                            final int n3 = layoutParams.mSpan.mIndex * mSizePerSpan;
                            if (this.mOrientation == 1) {
                                child2.offsetLeftAndRight(n2 - n3);
                            }
                            else {
                                child2.offsetTopAndBottom(n2 - n3);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void resolveShouldLayoutReverse() {
        boolean mShouldReverseLayout = true;
        if (this.mOrientation == 1 || !this.isLayoutRTL()) {
            this.mShouldReverseLayout = this.mReverseLayout;
        }
        else {
            if (this.mReverseLayout) {
                mShouldReverseLayout = false;
            }
            this.mShouldReverseLayout = mShouldReverseLayout;
        }
    }
    
    private void setLayoutStateDirection(int n) {
        final int n2 = 1;
        this.mLayoutState.mLayoutDirection = n;
        final LayoutState mLayoutState = this.mLayoutState;
        if (this.mShouldReverseLayout == (n == -1)) {
            n = n2;
        }
        else {
            n = -1;
        }
        mLayoutState.mItemDirection = n;
    }
    
    private void updateAllRemainingSpans(final int n, final int n2) {
        for (int i = 0; i < this.mSpanCount; ++i) {
            if (!this.mSpans[i].mViews.isEmpty()) {
                this.updateRemainingSpans(this.mSpans[i], n, n2);
            }
        }
    }
    
    private boolean updateAnchorFromChildren(final State state, final AnchorInfo anchorInfo) {
        int mPosition;
        if (this.mLastLayoutFromEnd) {
            mPosition = this.findLastReferenceChildPosition(state.getItemCount());
        }
        else {
            mPosition = this.findFirstReferenceChildPosition(state.getItemCount());
        }
        anchorInfo.mPosition = mPosition;
        anchorInfo.mOffset = Integer.MIN_VALUE;
        return true;
    }
    
    private void updateLayoutState(final int mCurrentPosition, final State state) {
        final boolean b = true;
        this.mLayoutState.mAvailable = 0;
        this.mLayoutState.mCurrentPosition = mCurrentPosition;
        final int n = 0;
        int totalSpace;
        final int n2 = totalSpace = 0;
        int totalSpace2 = n;
        if (((RecyclerView.LayoutManager)this).isSmoothScrolling()) {
            final int targetScrollPosition = state.getTargetScrollPosition();
            totalSpace = n2;
            totalSpace2 = n;
            if (targetScrollPosition != -1) {
                if (this.mShouldReverseLayout == targetScrollPosition < mCurrentPosition) {
                    totalSpace = this.mPrimaryOrientation.getTotalSpace();
                    totalSpace2 = n;
                }
                else {
                    totalSpace2 = this.mPrimaryOrientation.getTotalSpace();
                    totalSpace = n2;
                }
            }
        }
        if (((RecyclerView.LayoutManager)this).getClipToPadding()) {
            this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - totalSpace2;
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + totalSpace;
        }
        else {
            this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + totalSpace;
            this.mLayoutState.mStartLine = -totalSpace2;
        }
        this.mLayoutState.mStopInFocusable = false;
        this.mLayoutState.mRecycle = true;
        this.mLayoutState.mInfinite = (this.mPrimaryOrientation.getMode() == 0 && b);
    }
    
    private void updateRemainingSpans(final Span span, final int n, final int n2) {
        final int deletedSize = span.getDeletedSize();
        if (n == -1) {
            if (span.getStartLine() + deletedSize <= n2) {
                this.mRemainingSpans.set(span.mIndex, false);
            }
        }
        else if (span.getEndLine() - deletedSize >= n2) {
            this.mRemainingSpans.set(span.mIndex, false);
        }
    }
    
    private int updateSpecWithExtra(final int n, final int n2, final int n3) {
        int measureSpec;
        if (n2 == 0 && n3 == 0) {
            measureSpec = n;
        }
        else {
            final int mode = View$MeasureSpec.getMode(n);
            if (mode != Integer.MIN_VALUE) {
                measureSpec = n;
                if (mode != 1073741824) {
                    return measureSpec;
                }
            }
            measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max(0, View$MeasureSpec.getSize(n) - n2 - n3), mode);
        }
        return measureSpec;
    }
    
    boolean areAllEndsEqual() {
        boolean b = false;
        final int endLine = this.mSpans[0].getEndLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getEndLine(Integer.MIN_VALUE) != endLine) {
                return b;
            }
        }
        b = true;
        return b;
    }
    
    boolean areAllStartsEqual() {
        boolean b = false;
        final int startLine = this.mSpans[0].getStartLine(Integer.MIN_VALUE);
        for (int i = 1; i < this.mSpanCount; ++i) {
            if (this.mSpans[i].getStartLine(Integer.MIN_VALUE) != startLine) {
                return b;
            }
        }
        b = true;
        return b;
    }
    
    @Override
    public void assertNotInLayoutOrScroll(final String s) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(s);
        }
    }
    
    @Override
    public boolean canScrollHorizontally() {
        return this.mOrientation == 0;
    }
    
    @Override
    public boolean canScrollVertically() {
        boolean b = true;
        if (this.mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @Override
    public int computeHorizontalScrollExtent(final State state) {
        return this.computeScrollExtent(state);
    }
    
    @Override
    public int computeHorizontalScrollOffset(final State state) {
        return this.computeScrollOffset(state);
    }
    
    @Override
    public int computeHorizontalScrollRange(final State state) {
        return this.computeScrollRange(state);
    }
    
    @Override
    public int computeVerticalScrollExtent(final State state) {
        return this.computeScrollExtent(state);
    }
    
    @Override
    public int computeVerticalScrollOffset(final State state) {
        return this.computeScrollOffset(state);
    }
    
    @Override
    public int computeVerticalScrollRange(final State state) {
        return this.computeScrollRange(state);
    }
    
    public int[] findFirstCompletelyVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + array.length);
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findFirstCompletelyVisibleItemPosition();
        }
        return array2;
    }
    
    View findFirstVisibleItemClosestToEnd(final boolean b, final boolean b2) {
        this.ensureOrientationHelper();
        final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        View view = null;
        View view2;
        for (int i = ((RecyclerView.LayoutManager)this).getChildCount() - 1; i >= 0; --i, view = view2) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
            final int decoratedEnd = this.mPrimaryOrientation.getDecoratedEnd(child);
            view2 = view;
            if (decoratedEnd > startAfterPadding) {
                if (decoratedStart < endAfterPadding) {
                    View view3 = child;
                    if (decoratedEnd > endAfterPadding) {
                        if (!b) {
                            view3 = child;
                        }
                        else {
                            view2 = view;
                            if (b2 && (view2 = view) == null) {
                                view2 = child;
                            }
                            continue;
                        }
                    }
                    return view3;
                }
                view2 = view;
            }
        }
        return view;
    }
    
    View findFirstVisibleItemClosestToStart(final boolean b, final boolean b2) {
        this.ensureOrientationHelper();
        final int startAfterPadding = this.mPrimaryOrientation.getStartAfterPadding();
        final int endAfterPadding = this.mPrimaryOrientation.getEndAfterPadding();
        final int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        View view = null;
        View view2;
        for (int i = 0; i < childCount; ++i, view = view2) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
            view2 = view;
            if (this.mPrimaryOrientation.getDecoratedEnd(child) > startAfterPadding) {
                if (decoratedStart < endAfterPadding) {
                    View view3 = child;
                    if (decoratedStart < startAfterPadding) {
                        if (!b) {
                            view3 = child;
                        }
                        else {
                            view2 = view;
                            if (b2 && (view2 = view) == null) {
                                view2 = child;
                            }
                            continue;
                        }
                    }
                    return view3;
                }
                view2 = view;
            }
        }
        return view;
    }
    
    int findFirstVisibleItemPositionInt() {
        View view;
        if (this.mShouldReverseLayout) {
            view = this.findFirstVisibleItemClosestToEnd(true, true);
        }
        else {
            view = this.findFirstVisibleItemClosestToStart(true, true);
        }
        int position;
        if (view == null) {
            position = -1;
        }
        else {
            position = ((RecyclerView.LayoutManager)this).getPosition(view);
        }
        return position;
    }
    
    public int[] findFirstVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + array.length);
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findFirstVisibleItemPosition();
        }
        return array2;
    }
    
    public int[] findLastCompletelyVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + array.length);
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findLastCompletelyVisibleItemPosition();
        }
        return array2;
    }
    
    public int[] findLastVisibleItemPositions(final int[] array) {
        int[] array2;
        if (array == null) {
            array2 = new int[this.mSpanCount];
        }
        else {
            array2 = array;
            if (array.length < this.mSpanCount) {
                throw new IllegalArgumentException("Provided int[]'s size must be more than or equal to span count. Expected:" + this.mSpanCount + ", array size:" + array.length);
            }
        }
        for (int i = 0; i < this.mSpanCount; ++i) {
            array2[i] = this.mSpans[i].findLastVisibleItemPosition();
        }
        return array2;
    }
    
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams;
        if (this.mOrientation == 0) {
            layoutParams = new LayoutParams(-2, -1);
        }
        else {
            layoutParams = new LayoutParams(-1, -2);
        }
        return layoutParams;
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
        return new LayoutParams(context, set);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        LayoutParams layoutParams;
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            layoutParams = new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        else {
            layoutParams = new LayoutParams(viewGroup$LayoutParams);
        }
        return layoutParams;
    }
    
    @Override
    public int getColumnCountForAccessibility(final Recycler recycler, final State state) {
        int n;
        if (this.mOrientation == 1) {
            n = this.mSpanCount;
        }
        else {
            n = super.getColumnCountForAccessibility(recycler, state);
        }
        return n;
    }
    
    public int getGapStrategy() {
        return this.mGapStrategy;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        int n;
        if (this.mOrientation == 0) {
            n = this.mSpanCount;
        }
        else {
            n = super.getRowCountForAccessibility(recycler, state);
        }
        return n;
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    View hasGapsToFix() {
        int n = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
        final BitSet set = new BitSet(this.mSpanCount);
        set.set(0, this.mSpanCount, true);
        int n2;
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            n2 = 1;
        }
        else {
            n2 = -1;
        }
        int n3;
        if (this.mShouldReverseLayout) {
            n3 = n;
            n = 0 - 1;
        }
        else {
            n3 = 0;
            ++n;
        }
        int n4;
        if (n3 < n) {
            n4 = 1;
        }
        else {
            n4 = -1;
        }
        for (int i = n3; i != n; i += n4) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
            if (set.get(layoutParams.mSpan.mIndex)) {
                if (this.checkSpanForGap(layoutParams.mSpan)) {
                    return child;
                }
                set.clear(layoutParams.mSpan.mIndex);
            }
            if (!layoutParams.mFullSpan && i + n4 != n) {
                final View child2 = ((RecyclerView.LayoutManager)this).getChildAt(i + n4);
                int n5 = 0;
                if (this.mShouldReverseLayout) {
                    final int decoratedEnd = this.mPrimaryOrientation.getDecoratedEnd(child);
                    final int decoratedEnd2 = this.mPrimaryOrientation.getDecoratedEnd(child2);
                    final View view = child;
                    if (decoratedEnd < decoratedEnd2) {
                        return view;
                    }
                    if (decoratedEnd == decoratedEnd2) {
                        n5 = 1;
                    }
                }
                else {
                    final int decoratedStart = this.mPrimaryOrientation.getDecoratedStart(child);
                    final int decoratedStart2 = this.mPrimaryOrientation.getDecoratedStart(child2);
                    final View view = child;
                    if (decoratedStart > decoratedStart2) {
                        return view;
                    }
                    if (decoratedStart == decoratedStart2) {
                        n5 = 1;
                    }
                }
                if (n5 != 0) {
                    int n6;
                    if (layoutParams.mSpan.mIndex - ((LayoutParams)child2.getLayoutParams()).mSpan.mIndex < 0) {
                        n6 = 1;
                    }
                    else {
                        n6 = 0;
                    }
                    int n7;
                    if (n2 < 0) {
                        n7 = 1;
                    }
                    else {
                        n7 = 0;
                    }
                    if (n6 != n7) {
                        return child;
                    }
                }
            }
        }
        return null;
    }
    
    public void invalidateSpanAssignments() {
        this.mLazySpanLookup.clear();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    boolean isLayoutRTL() {
        boolean b = true;
        if (((RecyclerView.LayoutManager)this).getLayoutDirection() != 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void offsetChildrenHorizontal(final int n) {
        super.offsetChildrenHorizontal(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }
    
    @Override
    public void offsetChildrenVertical(final int n) {
        super.offsetChildrenVertical(n);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].onOffset(n);
        }
    }
    
    @Override
    public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
        ((RecyclerView.LayoutManager)this).removeCallbacks(this.mCheckForGapsRunnable);
        for (int i = 0; i < this.mSpanCount; ++i) {
            this.mSpans[i].clear();
        }
    }
    
    @Nullable
    @Override
    public View onFocusSearchFailed(View view, int n, final Recycler recycler, final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            view = null;
        }
        else {
            final View containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(view);
            if (containingItemView == null) {
                view = null;
            }
            else {
                this.ensureOrientationHelper();
                this.resolveShouldLayoutReverse();
                final int convertFocusDirectionToLayoutDirection = this.convertFocusDirectionToLayoutDirection(n);
                if (convertFocusDirectionToLayoutDirection == Integer.MIN_VALUE) {
                    view = null;
                }
                else {
                    final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
                    final boolean mFullSpan = layoutParams.mFullSpan;
                    final Span mSpan = layoutParams.mSpan;
                    if (convertFocusDirectionToLayoutDirection == 1) {
                        n = this.getLastChildPosition();
                    }
                    else {
                        n = this.getFirstChildPosition();
                    }
                    this.updateLayoutState(n, state);
                    this.setLayoutStateDirection(convertFocusDirectionToLayoutDirection);
                    this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + n;
                    this.mLayoutState.mAvailable = (int)(0.33333334f * this.mPrimaryOrientation.getTotalSpace());
                    this.mLayoutState.mStopInFocusable = true;
                    this.mLayoutState.mRecycle = false;
                    this.fill(recycler, this.mLayoutState, state);
                    this.mLastLayoutFromEnd = this.mShouldReverseLayout;
                    if (!mFullSpan) {
                        final View focusableViewAfter = mSpan.getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                        if (focusableViewAfter != null && (view = focusableViewAfter) != containingItemView) {
                            return view;
                        }
                    }
                    if (this.preferLastSpan(convertFocusDirectionToLayoutDirection)) {
                        for (int i = this.mSpanCount - 1; i >= 0; --i) {
                            final View focusableViewAfter2 = this.mSpans[i].getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                            if (focusableViewAfter2 != null && (view = focusableViewAfter2) != containingItemView) {
                                return view;
                            }
                        }
                    }
                    else {
                        for (int j = 0; j < this.mSpanCount; ++j) {
                            final View focusableViewAfter3 = this.mSpans[j].getFocusableViewAfter(n, convertFocusDirectionToLayoutDirection);
                            if (focusableViewAfter3 != null && (view = focusableViewAfter3) != containingItemView) {
                                return view;
                            }
                        }
                    }
                    view = null;
                }
            }
        }
        return view;
    }
    
    @Override
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            final AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(accessibilityEvent);
            final View firstVisibleItemClosestToStart = this.findFirstVisibleItemClosestToStart(false, true);
            final View firstVisibleItemClosestToEnd = this.findFirstVisibleItemClosestToEnd(false, true);
            if (firstVisibleItemClosestToStart != null && firstVisibleItemClosestToEnd != null) {
                final int position = ((RecyclerView.LayoutManager)this).getPosition(firstVisibleItemClosestToStart);
                final int position2 = ((RecyclerView.LayoutManager)this).getPosition(firstVisibleItemClosestToEnd);
                if (position < position2) {
                    record.setFromIndex(position);
                    record.setToIndex(position2);
                }
                else {
                    record.setFromIndex(position2);
                    record.setToIndex(position);
                }
            }
        }
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
        }
        else {
            final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
            if (this.mOrientation == 0) {
                final int spanIndex = layoutParams2.getSpanIndex();
                int mSpanCount;
                if (layoutParams2.mFullSpan) {
                    mSpanCount = this.mSpanCount;
                }
                else {
                    mSpanCount = 1;
                }
                accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanIndex, mSpanCount, -1, -1, layoutParams2.mFullSpan, false));
            }
            else {
                final int spanIndex2 = layoutParams2.getSpanIndex();
                int mSpanCount2;
                if (layoutParams2.mFullSpan) {
                    mSpanCount2 = this.mSpanCount;
                }
                else {
                    mSpanCount2 = 1;
                }
                accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, spanIndex2, mSpanCount2, layoutParams2.mFullSpan, false));
            }
        }
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.handleUpdate(n, n2, 1);
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mLazySpanLookup.clear();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.handleUpdate(n, n2, 8);
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.handleUpdate(n, n2, 2);
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.handleUpdate(n, n2, 4);
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        this.onLayoutChildren(recycler, state, true);
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        SavedState savedState;
        if (this.mPendingSavedState != null) {
            savedState = new SavedState(this.mPendingSavedState);
        }
        else {
            final SavedState savedState2 = new SavedState();
            savedState2.mReverseLayout = this.mReverseLayout;
            savedState2.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
            savedState2.mLastLayoutRTL = this.mLastLayoutRTL;
            if (this.mLazySpanLookup != null && this.mLazySpanLookup.mData != null) {
                savedState2.mSpanLookup = this.mLazySpanLookup.mData;
                savedState2.mSpanLookupSize = savedState2.mSpanLookup.length;
                savedState2.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
            }
            else {
                savedState2.mSpanLookupSize = 0;
            }
            if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
                this.ensureOrientationHelper();
                int mAnchorPosition;
                if (this.mLastLayoutFromEnd) {
                    mAnchorPosition = this.getLastChildPosition();
                }
                else {
                    mAnchorPosition = this.getFirstChildPosition();
                }
                savedState2.mAnchorPosition = mAnchorPosition;
                savedState2.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
                savedState2.mSpanOffsetsSize = this.mSpanCount;
                savedState2.mSpanOffsets = new int[this.mSpanCount];
                int n = 0;
                while (true) {
                    savedState = savedState2;
                    if (n >= this.mSpanCount) {
                        break;
                    }
                    int n2;
                    if (this.mLastLayoutFromEnd) {
                        final int endLine = this.mSpans[n].getEndLine(Integer.MIN_VALUE);
                        if ((n2 = endLine) != Integer.MIN_VALUE) {
                            n2 = endLine - this.mPrimaryOrientation.getEndAfterPadding();
                        }
                    }
                    else {
                        final int startLine = this.mSpans[n].getStartLine(Integer.MIN_VALUE);
                        if ((n2 = startLine) != Integer.MIN_VALUE) {
                            n2 = startLine - this.mPrimaryOrientation.getStartAfterPadding();
                        }
                    }
                    savedState2.mSpanOffsets[n] = n2;
                    ++n;
                }
            }
            else {
                savedState2.mAnchorPosition = -1;
                savedState2.mVisibleAnchorPosition = -1;
                savedState2.mSpanOffsetsSize = 0;
                savedState = savedState2;
            }
        }
        return (Parcelable)savedState;
    }
    
    @Override
    public void onScrollStateChanged(final int n) {
        if (n == 0) {
            this.checkForGaps();
        }
    }
    
    int scrollBy(int a, final Recycler recycler, final State state) {
        this.ensureOrientationHelper();
        int layoutStateDirection;
        int n;
        if (a > 0) {
            layoutStateDirection = 1;
            n = this.getLastChildPosition();
        }
        else {
            layoutStateDirection = -1;
            n = this.getFirstChildPosition();
        }
        this.mLayoutState.mRecycle = true;
        this.updateLayoutState(n, state);
        this.setLayoutStateDirection(layoutStateDirection);
        this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + n;
        final int abs = Math.abs(a);
        this.mLayoutState.mAvailable = abs;
        final int fill = this.fill(recycler, this.mLayoutState, state);
        if (abs >= fill) {
            if (a < 0) {
                a = -fill;
            }
            else {
                a = fill;
            }
        }
        this.mPrimaryOrientation.offsetChildren(-a);
        this.mLastLayoutFromEnd = this.mShouldReverseLayout;
        return a;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        return this.scrollBy(n, recycler, state);
    }
    
    @Override
    public void scrollToPosition(final int mPendingScrollPosition) {
        if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != mPendingScrollPosition) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void scrollToPositionWithOffset(final int mPendingScrollPosition, final int mPendingScrollPositionOffset) {
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchorPositionInfo();
        }
        this.mPendingScrollPosition = mPendingScrollPosition;
        this.mPendingScrollPositionOffset = mPendingScrollPositionOffset;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        return this.scrollBy(n, recycler, state);
    }
    
    public void setGapStrategy(final int mGapStrategy) {
        this.assertNotInLayoutOrScroll(null);
        if (mGapStrategy != this.mGapStrategy) {
            if (mGapStrategy != 0 && mGapStrategy != 2) {
                throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
            }
            this.mGapStrategy = mGapStrategy;
            ((RecyclerView.LayoutManager)this).setAutoMeasureEnabled(this.mGapStrategy != 0);
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int n, int n2) {
        final int n3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n4 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = RecyclerView.LayoutManager.chooseSize(n2, rect.height() + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            n = RecyclerView.LayoutManager.chooseSize(n, this.mSizePerSpan * this.mSpanCount + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
        }
        else {
            n = RecyclerView.LayoutManager.chooseSize(n, rect.width() + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            n2 = RecyclerView.LayoutManager.chooseSize(n2, this.mSizePerSpan * this.mSpanCount + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(n, n2);
    }
    
    public void setOrientation(final int mOrientation) {
        if (mOrientation != 0 && mOrientation != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        this.assertNotInLayoutOrScroll(null);
        if (mOrientation != this.mOrientation) {
            this.mOrientation = mOrientation;
            if (this.mPrimaryOrientation != null && this.mSecondaryOrientation != null) {
                final OrientationHelper mPrimaryOrientation = this.mPrimaryOrientation;
                this.mPrimaryOrientation = this.mSecondaryOrientation;
                this.mSecondaryOrientation = mPrimaryOrientation;
            }
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    public void setReverseLayout(final boolean b) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mPendingSavedState != null && this.mPendingSavedState.mReverseLayout != b) {
            this.mPendingSavedState.mReverseLayout = b;
        }
        this.mReverseLayout = b;
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSpanCount(int i) {
        this.assertNotInLayoutOrScroll(null);
        if (i != this.mSpanCount) {
            this.invalidateSpanAssignments();
            this.mSpanCount = i;
            this.mRemainingSpans = new BitSet(this.mSpanCount);
            this.mSpans = new Span[this.mSpanCount];
            for (i = 0; i < this.mSpanCount; ++i) {
                this.mSpans[i] = new Span(i);
            }
            ((RecyclerView.LayoutManager)this).requestLayout();
        }
    }
    
    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int targetPosition) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public PointF computeScrollVectorForPosition(int access$400) {
                access$400 = StaggeredGridLayoutManager.this.calculateScrollDirectionForPosition(access$400);
                PointF pointF;
                if (access$400 == 0) {
                    pointF = null;
                }
                else if (StaggeredGridLayoutManager.this.mOrientation == 0) {
                    pointF = new PointF((float)access$400, 0.0f);
                }
                else {
                    pointF = new PointF(0.0f, (float)access$400);
                }
                return pointF;
            }
        };
        ((RecyclerView.SmoothScroller)linearSmoothScroller).setTargetPosition(targetPosition);
        ((RecyclerView.LayoutManager)this).startSmoothScroll(linearSmoothScroller);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null;
    }
    
    boolean updateAnchorFromPendingData(final State state, final AnchorInfo anchorInfo) {
        boolean mLayoutFromEnd = false;
        final boolean b = true;
        boolean b2;
        if (state.isPreLayout() || this.mPendingScrollPosition == -1) {
            b2 = false;
        }
        else if (this.mPendingScrollPosition < 0 || this.mPendingScrollPosition >= state.getItemCount()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            b2 = false;
        }
        else if (this.mPendingSavedState == null || this.mPendingSavedState.mAnchorPosition == -1 || this.mPendingSavedState.mSpanOffsetsSize < 1) {
            final View viewByPosition = ((RecyclerView.LayoutManager)this).findViewByPosition(this.mPendingScrollPosition);
            if (viewByPosition != null) {
                int mPosition;
                if (this.mShouldReverseLayout) {
                    mPosition = this.getLastChildPosition();
                }
                else {
                    mPosition = this.getFirstChildPosition();
                }
                anchorInfo.mPosition = mPosition;
                if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                    if (anchorInfo.mLayoutFromEnd) {
                        anchorInfo.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(viewByPosition);
                        b2 = b;
                    }
                    else {
                        anchorInfo.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(viewByPosition);
                        b2 = b;
                    }
                }
                else if (this.mPrimaryOrientation.getDecoratedMeasurement(viewByPosition) > this.mPrimaryOrientation.getTotalSpace()) {
                    int mOffset;
                    if (anchorInfo.mLayoutFromEnd) {
                        mOffset = this.mPrimaryOrientation.getEndAfterPadding();
                    }
                    else {
                        mOffset = this.mPrimaryOrientation.getStartAfterPadding();
                    }
                    anchorInfo.mOffset = mOffset;
                    b2 = b;
                }
                else {
                    final int n = this.mPrimaryOrientation.getDecoratedStart(viewByPosition) - this.mPrimaryOrientation.getStartAfterPadding();
                    if (n < 0) {
                        anchorInfo.mOffset = -n;
                        b2 = b;
                    }
                    else {
                        final int mOffset2 = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(viewByPosition);
                        if (mOffset2 < 0) {
                            anchorInfo.mOffset = mOffset2;
                            b2 = b;
                        }
                        else {
                            anchorInfo.mOffset = Integer.MIN_VALUE;
                            b2 = b;
                        }
                    }
                }
            }
            else {
                anchorInfo.mPosition = this.mPendingScrollPosition;
                if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
                    if (this.calculateScrollDirectionForPosition(anchorInfo.mPosition) == 1) {
                        mLayoutFromEnd = true;
                    }
                    anchorInfo.mLayoutFromEnd = mLayoutFromEnd;
                    anchorInfo.assignCoordinateFromPadding();
                }
                else {
                    anchorInfo.assignCoordinateFromPadding(this.mPendingScrollPositionOffset);
                }
                anchorInfo.mInvalidateOffsets = true;
                b2 = b;
            }
        }
        else {
            anchorInfo.mOffset = Integer.MIN_VALUE;
            anchorInfo.mPosition = this.mPendingScrollPosition;
            b2 = b;
        }
        return b2;
    }
    
    void updateAnchorInfoForLayout(final State state, final AnchorInfo anchorInfo) {
        if (!this.updateAnchorFromPendingData(state, anchorInfo) && !this.updateAnchorFromChildren(state, anchorInfo)) {
            anchorInfo.assignCoordinateFromPadding();
            anchorInfo.mPosition = 0;
        }
    }
    
    void updateMeasureSpecs(final int n) {
        this.mSizePerSpan = n / this.mSpanCount;
        this.mFullSizeSpec = View$MeasureSpec.makeMeasureSpec(n, this.mSecondaryOrientation.getMode());
    }
    
    private class AnchorInfo
    {
        boolean mInvalidateOffsets;
        boolean mLayoutFromEnd;
        int mOffset;
        int mPosition;
        
        void assignCoordinateFromPadding() {
            int mOffset;
            if (this.mLayoutFromEnd) {
                mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
            }
            else {
                mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
            }
            this.mOffset = mOffset;
        }
        
        void assignCoordinateFromPadding(final int n) {
            if (this.mLayoutFromEnd) {
                this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - n;
            }
            else {
                this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + n;
            }
        }
        
        void reset() {
            this.mPosition = -1;
            this.mOffset = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mInvalidateOffsets = false;
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        boolean mFullSpan;
        Span mSpan;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
        }
        
        public final int getSpanIndex() {
            int mIndex;
            if (this.mSpan == null) {
                mIndex = -1;
            }
            else {
                mIndex = this.mSpan.mIndex;
            }
            return mIndex;
        }
        
        public boolean isFullSpan() {
            return this.mFullSpan;
        }
        
        public void setFullSpan(final boolean mFullSpan) {
            this.mFullSpan = mFullSpan;
        }
    }
    
    static class LazySpanLookup
    {
        private static final int MIN_SIZE = 10;
        int[] mData;
        List<FullSpanItem> mFullSpanItems;
        
        private int invalidateFullSpansAfter(int mPosition) {
            final int n = -1;
            if (this.mFullSpanItems == null) {
                mPosition = n;
            }
            else {
                final FullSpanItem fullSpanItem = this.getFullSpanItem(mPosition);
                if (fullSpanItem != null) {
                    this.mFullSpanItems.remove(fullSpanItem);
                }
                final int n2 = -1;
                final int size = this.mFullSpanItems.size();
                int n3 = 0;
                int n4;
                while (true) {
                    n4 = n2;
                    if (n3 >= size) {
                        break;
                    }
                    if (this.mFullSpanItems.get(n3).mPosition >= mPosition) {
                        n4 = n3;
                        break;
                    }
                    ++n3;
                }
                mPosition = n;
                if (n4 != -1) {
                    final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(n4);
                    this.mFullSpanItems.remove(n4);
                    mPosition = fullSpanItem2.mPosition;
                }
            }
            return mPosition;
        }
        
        private void offsetFullSpansForAddition(final int n, final int n2) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                    if (fullSpanItem.mPosition >= n) {
                        fullSpanItem.mPosition += n2;
                    }
                }
            }
        }
        
        private void offsetFullSpansForRemoval(final int n, final int n2) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    final FullSpanItem fullSpanItem = this.mFullSpanItems.get(i);
                    if (fullSpanItem.mPosition >= n) {
                        if (fullSpanItem.mPosition < n + n2) {
                            this.mFullSpanItems.remove(i);
                        }
                        else {
                            fullSpanItem.mPosition -= n2;
                        }
                    }
                }
            }
        }
        
        public void addFullSpanItem(final FullSpanItem fullSpanItem) {
            if (this.mFullSpanItems == null) {
                this.mFullSpanItems = new ArrayList<FullSpanItem>();
            }
            for (int size = this.mFullSpanItems.size(), i = 0; i < size; ++i) {
                final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                if (fullSpanItem2.mPosition == fullSpanItem.mPosition) {
                    this.mFullSpanItems.remove(i);
                }
                if (fullSpanItem2.mPosition >= fullSpanItem.mPosition) {
                    this.mFullSpanItems.add(i, fullSpanItem);
                    return;
                }
            }
            this.mFullSpanItems.add(fullSpanItem);
        }
        
        void clear() {
            if (this.mData != null) {
                Arrays.fill(this.mData, -1);
            }
            this.mFullSpanItems = null;
        }
        
        void ensureSize(final int a) {
            if (this.mData == null) {
                Arrays.fill(this.mData = new int[Math.max(a, 10) + 1], -1);
            }
            else if (a >= this.mData.length) {
                final int[] mData = this.mData;
                System.arraycopy(mData, 0, this.mData = new int[this.sizeForPosition(a)], 0, mData.length);
                Arrays.fill(this.mData, mData.length, this.mData.length, -1);
            }
        }
        
        int forceInvalidateAfter(final int n) {
            if (this.mFullSpanItems != null) {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    if (this.mFullSpanItems.get(i).mPosition >= n) {
                        this.mFullSpanItems.remove(i);
                    }
                }
            }
            return this.invalidateAfter(n);
        }
        
        public FullSpanItem getFirstFullSpanItemInRange(final int n, final int n2, final int n3, final boolean b) {
            FullSpanItem fullSpanItem;
            if (this.mFullSpanItems == null) {
                fullSpanItem = null;
            }
            else {
                for (int size = this.mFullSpanItems.size(), i = 0; i < size; ++i) {
                    final FullSpanItem fullSpanItem2 = this.mFullSpanItems.get(i);
                    if (fullSpanItem2.mPosition >= n2) {
                        fullSpanItem = null;
                        return fullSpanItem;
                    }
                    if (fullSpanItem2.mPosition >= n) {
                        fullSpanItem = fullSpanItem2;
                        if (n3 == 0) {
                            return fullSpanItem;
                        }
                        fullSpanItem = fullSpanItem2;
                        if (fullSpanItem2.mGapDir == n3) {
                            return fullSpanItem;
                        }
                        if (b) {
                            fullSpanItem = fullSpanItem2;
                            if (fullSpanItem2.mHasUnwantedGapAfter) {
                                return fullSpanItem;
                            }
                        }
                    }
                }
                fullSpanItem = null;
            }
            return fullSpanItem;
        }
        
        public FullSpanItem getFullSpanItem(final int n) {
            FullSpanItem fullSpanItem;
            if (this.mFullSpanItems == null) {
                fullSpanItem = null;
            }
            else {
                for (int i = this.mFullSpanItems.size() - 1; i >= 0; --i) {
                    if ((fullSpanItem = this.mFullSpanItems.get(i)).mPosition == n) {
                        return fullSpanItem;
                    }
                }
                fullSpanItem = null;
            }
            return fullSpanItem;
        }
        
        int getSpan(int n) {
            if (this.mData == null || n >= this.mData.length) {
                n = -1;
            }
            else {
                n = this.mData[n];
            }
            return n;
        }
        
        int invalidateAfter(final int n) {
            int n2 = -1;
            if (this.mData != null && n < this.mData.length) {
                n2 = this.invalidateFullSpansAfter(n);
                if (n2 == -1) {
                    Arrays.fill(this.mData, n, this.mData.length, -1);
                    n2 = this.mData.length;
                }
                else {
                    Arrays.fill(this.mData, n, n2 + 1, -1);
                    ++n2;
                }
            }
            return n2;
        }
        
        void offsetForAddition(final int fromIndex, final int n) {
            if (this.mData != null && fromIndex < this.mData.length) {
                this.ensureSize(fromIndex + n);
                System.arraycopy(this.mData, fromIndex, this.mData, fromIndex + n, this.mData.length - fromIndex - n);
                Arrays.fill(this.mData, fromIndex, fromIndex + n, -1);
                this.offsetFullSpansForAddition(fromIndex, n);
            }
        }
        
        void offsetForRemoval(final int n, final int n2) {
            if (this.mData != null && n < this.mData.length) {
                this.ensureSize(n + n2);
                System.arraycopy(this.mData, n + n2, this.mData, n, this.mData.length - n - n2);
                Arrays.fill(this.mData, this.mData.length - n2, this.mData.length, -1);
                this.offsetFullSpansForRemoval(n, n2);
            }
        }
        
        void setSpan(final int n, final Span span) {
            this.ensureSize(n);
            this.mData[n] = span.mIndex;
        }
        
        int sizeForPosition(final int n) {
            int i;
            for (i = this.mData.length; i <= n; i *= 2) {}
            return i;
        }
        
        static class FullSpanItem implements Parcelable
        {
            public static final Parcelable$Creator<FullSpanItem> CREATOR;
            int mGapDir;
            int[] mGapPerSpan;
            boolean mHasUnwantedGapAfter;
            int mPosition;
            
            static {
                CREATOR = (Parcelable$Creator)new Parcelable$Creator<FullSpanItem>() {
                    public FullSpanItem createFromParcel(final Parcel parcel) {
                        return new FullSpanItem(parcel);
                    }
                    
                    public FullSpanItem[] newArray(final int n) {
                        return new FullSpanItem[n];
                    }
                };
            }
            
            public FullSpanItem() {
            }
            
            public FullSpanItem(final Parcel parcel) {
                boolean mHasUnwantedGapAfter = true;
                this.mPosition = parcel.readInt();
                this.mGapDir = parcel.readInt();
                if (parcel.readInt() != 1) {
                    mHasUnwantedGapAfter = false;
                }
                this.mHasUnwantedGapAfter = mHasUnwantedGapAfter;
                final int int1 = parcel.readInt();
                if (int1 > 0) {
                    parcel.readIntArray(this.mGapPerSpan = new int[int1]);
                }
            }
            
            public int describeContents() {
                return 0;
            }
            
            int getGapForSpan(int n) {
                if (this.mGapPerSpan == null) {
                    n = 0;
                }
                else {
                    n = this.mGapPerSpan[n];
                }
                return n;
            }
            
            @Override
            public String toString() {
                return "FullSpanItem{mPosition=" + this.mPosition + ", mGapDir=" + this.mGapDir + ", mHasUnwantedGapAfter=" + this.mHasUnwantedGapAfter + ", mGapPerSpan=" + Arrays.toString(this.mGapPerSpan) + '}';
            }
            
            public void writeToParcel(final Parcel parcel, int n) {
                parcel.writeInt(this.mPosition);
                parcel.writeInt(this.mGapDir);
                if (this.mHasUnwantedGapAfter) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                parcel.writeInt(n);
                if (this.mGapPerSpan != null && this.mGapPerSpan.length > 0) {
                    parcel.writeInt(this.mGapPerSpan.length);
                    parcel.writeIntArray(this.mGapPerSpan);
                }
                else {
                    parcel.writeInt(0);
                }
            }
        }
    }
    
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean mAnchorLayoutFromEnd;
        int mAnchorPosition;
        List<FullSpanItem> mFullSpanItems;
        boolean mLastLayoutRTL;
        boolean mReverseLayout;
        int[] mSpanLookup;
        int mSpanLookupSize;
        int[] mSpanOffsets;
        int mSpanOffsetsSize;
        int mVisibleAnchorPosition;
        
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
        
        public SavedState() {
        }
        
        SavedState(final Parcel parcel) {
            final boolean b = true;
            this.mAnchorPosition = parcel.readInt();
            this.mVisibleAnchorPosition = parcel.readInt();
            this.mSpanOffsetsSize = parcel.readInt();
            if (this.mSpanOffsetsSize > 0) {
                parcel.readIntArray(this.mSpanOffsets = new int[this.mSpanOffsetsSize]);
            }
            this.mSpanLookupSize = parcel.readInt();
            if (this.mSpanLookupSize > 0) {
                parcel.readIntArray(this.mSpanLookup = new int[this.mSpanLookupSize]);
            }
            this.mReverseLayout = (parcel.readInt() == 1);
            this.mAnchorLayoutFromEnd = (parcel.readInt() == 1);
            this.mLastLayoutRTL = (parcel.readInt() == 1 && b);
            this.mFullSpanItems = (List<FullSpanItem>)parcel.readArrayList(FullSpanItem.class.getClassLoader());
        }
        
        public SavedState(final SavedState savedState) {
            this.mSpanOffsetsSize = savedState.mSpanOffsetsSize;
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mVisibleAnchorPosition = savedState.mVisibleAnchorPosition;
            this.mSpanOffsets = savedState.mSpanOffsets;
            this.mSpanLookupSize = savedState.mSpanLookupSize;
            this.mSpanLookup = savedState.mSpanLookup;
            this.mReverseLayout = savedState.mReverseLayout;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
            this.mLastLayoutRTL = savedState.mLastLayoutRTL;
            this.mFullSpanItems = savedState.mFullSpanItems;
        }
        
        public int describeContents() {
            return 0;
        }
        
        void invalidateAnchorPositionInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mAnchorPosition = -1;
            this.mVisibleAnchorPosition = -1;
        }
        
        void invalidateSpanInfo() {
            this.mSpanOffsets = null;
            this.mSpanOffsetsSize = 0;
            this.mSpanLookupSize = 0;
            this.mSpanLookup = null;
            this.mFullSpanItems = null;
        }
        
        public void writeToParcel(final Parcel parcel, int n) {
            final int n2 = 1;
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mVisibleAnchorPosition);
            parcel.writeInt(this.mSpanOffsetsSize);
            if (this.mSpanOffsetsSize > 0) {
                parcel.writeIntArray(this.mSpanOffsets);
            }
            parcel.writeInt(this.mSpanLookupSize);
            if (this.mSpanLookupSize > 0) {
                parcel.writeIntArray(this.mSpanLookup);
            }
            if (this.mReverseLayout) {
                n = 1;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
            if (this.mAnchorLayoutFromEnd) {
                n = 1;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
            if (this.mLastLayoutRTL) {
                n = n2;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
            parcel.writeList((List)this.mFullSpanItems);
        }
    }
    
    class Span
    {
        static final int INVALID_LINE = Integer.MIN_VALUE;
        int mCachedEnd;
        int mCachedStart;
        int mDeletedSize;
        final int mIndex;
        private ArrayList<View> mViews;
        
        private Span(final int mIndex) {
            this.mViews = new ArrayList<View>();
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
            this.mDeletedSize = 0;
            this.mIndex = mIndex;
        }
        
        void appendToSpan(final View e) {
            final LayoutParams layoutParams = this.getLayoutParams(e);
            layoutParams.mSpan = this;
            this.mViews.add(e);
            this.mCachedEnd = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(e);
            }
        }
        
        void cacheReferenceLineAndClear(final boolean b, final int n) {
            int n2;
            if (b) {
                n2 = this.getEndLine(Integer.MIN_VALUE);
            }
            else {
                n2 = this.getStartLine(Integer.MIN_VALUE);
            }
            this.clear();
            if (n2 != Integer.MIN_VALUE && (!b || n2 >= StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding()) && (b || n2 <= StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding())) {
                int n3 = n2;
                if (n != Integer.MIN_VALUE) {
                    n3 = n2 + n;
                }
                this.mCachedEnd = n3;
                this.mCachedStart = n3;
            }
        }
        
        void calculateCachedEnd() {
            final View view = this.mViews.get(this.mViews.size() - 1);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
            if (layoutParams.mFullSpan) {
                final FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition());
                if (fullSpanItem != null && fullSpanItem.mGapDir == 1) {
                    this.mCachedEnd += fullSpanItem.getGapForSpan(this.mIndex);
                }
            }
        }
        
        void calculateCachedStart() {
            final View view = this.mViews.get(0);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
            if (layoutParams.mFullSpan) {
                final FullSpanItem fullSpanItem = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition());
                if (fullSpanItem != null && fullSpanItem.mGapDir == -1) {
                    this.mCachedStart -= fullSpanItem.getGapForSpan(this.mIndex);
                }
            }
        }
        
        void clear() {
            this.mViews.clear();
            this.invalidateCache();
            this.mDeletedSize = 0;
        }
        
        public int findFirstCompletelyVisibleItemPosition() {
            int n;
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            }
            else {
                n = this.findOneVisibleChild(0, this.mViews.size(), true);
            }
            return n;
        }
        
        public int findFirstVisibleItemPosition() {
            int n;
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            }
            else {
                n = this.findOneVisibleChild(0, this.mViews.size(), false);
            }
            return n;
        }
        
        public int findLastCompletelyVisibleItemPosition() {
            int n;
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                n = this.findOneVisibleChild(0, this.mViews.size(), true);
            }
            else {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
            }
            return n;
        }
        
        public int findLastVisibleItemPosition() {
            int n;
            if (StaggeredGridLayoutManager.this.mReverseLayout) {
                n = this.findOneVisibleChild(0, this.mViews.size(), false);
            }
            else {
                n = this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
            }
            return n;
        }
        
        int findOneVisibleChild(int n, final int n2, final boolean b) {
            final int n3 = -1;
            final int startAfterPadding = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
            final int endAfterPadding = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
            int n4;
            if (n2 > n) {
                n4 = 1;
            }
            else {
                n4 = -1;
            }
            int index = n;
            while (true) {
                n = n3;
                if (index == n2) {
                    break;
                }
                final View view = this.mViews.get(index);
                n = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(view);
                final int decoratedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(view);
                if (n < endAfterPadding && decoratedEnd > startAfterPadding) {
                    if (!b) {
                        return ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view);
                    }
                    if (n >= startAfterPadding && decoratedEnd <= endAfterPadding) {
                        n = ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view);
                        break;
                    }
                }
                index += n4;
            }
            return n;
            View view = null;
            n = ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view);
            return n;
        }
        
        public int getDeletedSize() {
            return this.mDeletedSize;
        }
        
        int getEndLine() {
            int n;
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                n = this.mCachedEnd;
            }
            else {
                this.calculateCachedEnd();
                n = this.mCachedEnd;
            }
            return n;
        }
        
        int getEndLine(int n) {
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                n = this.mCachedEnd;
            }
            else if (this.mViews.size() != 0) {
                this.calculateCachedEnd();
                n = this.mCachedEnd;
            }
            return n;
        }
        
        public View getFocusableViewAfter(final int n, int n2) {
            final View view = null;
            View view2 = null;
            View view3;
            if (n2 == -1) {
                final int size = this.mViews.size();
                n2 = 0;
                while (true) {
                    view3 = view2;
                    if (n2 >= size) {
                        break;
                    }
                    final View view4 = this.mViews.get(n2);
                    view3 = view2;
                    if (!view4.isFocusable()) {
                        break;
                    }
                    final boolean b = ((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view4) > n;
                    view3 = view2;
                    if (b != StaggeredGridLayoutManager.this.mReverseLayout) {
                        break;
                    }
                    view2 = view4;
                    ++n2;
                }
            }
            else {
                n2 = this.mViews.size() - 1;
                View view5 = view;
                while (true) {
                    view3 = view5;
                    if (n2 < 0) {
                        break;
                    }
                    final View view6 = this.mViews.get(n2);
                    view3 = view5;
                    if (!view6.isFocusable()) {
                        break;
                    }
                    int n3;
                    if (((RecyclerView.LayoutManager)StaggeredGridLayoutManager.this).getPosition(view6) > n) {
                        n3 = 1;
                    }
                    else {
                        n3 = 0;
                    }
                    int n4;
                    if (!StaggeredGridLayoutManager.this.mReverseLayout) {
                        n4 = 1;
                    }
                    else {
                        n4 = 0;
                    }
                    view3 = view5;
                    if (n3 != n4) {
                        break;
                    }
                    view5 = view6;
                    --n2;
                }
            }
            return view3;
        }
        
        LayoutParams getLayoutParams(final View view) {
            return (LayoutParams)view.getLayoutParams();
        }
        
        int getStartLine() {
            int n;
            if (this.mCachedStart != Integer.MIN_VALUE) {
                n = this.mCachedStart;
            }
            else {
                this.calculateCachedStart();
                n = this.mCachedStart;
            }
            return n;
        }
        
        int getStartLine(int n) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                n = this.mCachedStart;
            }
            else if (this.mViews.size() != 0) {
                this.calculateCachedStart();
                n = this.mCachedStart;
            }
            return n;
        }
        
        void invalidateCache() {
            this.mCachedStart = Integer.MIN_VALUE;
            this.mCachedEnd = Integer.MIN_VALUE;
        }
        
        void onOffset(final int n) {
            if (this.mCachedStart != Integer.MIN_VALUE) {
                this.mCachedStart += n;
            }
            if (this.mCachedEnd != Integer.MIN_VALUE) {
                this.mCachedEnd += n;
            }
        }
        
        void popEnd() {
            final int size = this.mViews.size();
            final View view = this.mViews.remove(size - 1);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            if (size == 1) {
                this.mCachedStart = Integer.MIN_VALUE;
            }
            this.mCachedEnd = Integer.MIN_VALUE;
        }
        
        void popStart() {
            final View view = this.mViews.remove(0);
            final LayoutParams layoutParams = this.getLayoutParams(view);
            layoutParams.mSpan = null;
            if (this.mViews.size() == 0) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(view);
            }
            this.mCachedStart = Integer.MIN_VALUE;
        }
        
        void prependToSpan(final View element) {
            final LayoutParams layoutParams = this.getLayoutParams(element);
            layoutParams.mSpan = this;
            this.mViews.add(0, element);
            this.mCachedStart = Integer.MIN_VALUE;
            if (this.mViews.size() == 1) {
                this.mCachedEnd = Integer.MIN_VALUE;
            }
            if (((RecyclerView.LayoutParams)layoutParams).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams).isItemChanged()) {
                this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(element);
            }
        }
        
        void setLine(final int n) {
            this.mCachedStart = n;
            this.mCachedEnd = n;
        }
    }
}
