// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.util.SparseIntArray;
import android.graphics.Rect;

public class GridLayoutManager extends LinearLayoutManager
{
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets;
    boolean mPendingSpanCountChange;
    final SparseIntArray mPreLayoutSpanIndexCache;
    final SparseIntArray mPreLayoutSpanSizeCache;
    View[] mSet;
    int mSpanCount;
    SpanSizeLookup mSpanSizeLookup;
    
    public GridLayoutManager(final Context context, final int spanCount) {
        super(context);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final int spanCount, final int n, final boolean b) {
        super(context, n, b);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(RecyclerView.LayoutManager.getProperties(context, set, n, n2).spanCount);
    }
    
    private void assignSpans(final Recycler recycler, final State state, int i, int n, final boolean b) {
        int n2;
        int n3;
        if (b) {
            n = 0;
            n2 = i;
            n3 = 1;
            i = n;
        }
        else {
            --i;
            n2 = -1;
            n3 = -1;
        }
        int n4;
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            n = this.mSpanCount - 1;
            n4 = -1;
        }
        else {
            n = 0;
            n4 = 1;
        }
        while (i != n2) {
            final View view = this.mSet[i];
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, ((RecyclerView.LayoutManager)this).getPosition(view));
            if (n4 == -1 && layoutParams.mSpanSize > 1) {
                layoutParams.mSpanIndex = n - (layoutParams.mSpanSize - 1);
            }
            else {
                layoutParams.mSpanIndex = n;
            }
            n += layoutParams.mSpanSize * n4;
            i += n3;
        }
    }
    
    private void cachePreLayoutSpanMapping() {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)((RecyclerView.LayoutManager)this).getChildAt(i).getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }
    
    private void calculateItemBorders(final int n) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, n);
    }
    
    static int[] calculateItemBorders(final int[] array, final int n, int n2) {
        int[] array2 = null;
        Label_0030: {
            if (array != null && array.length == n + 1) {
                array2 = array;
                if (array[array.length - 1] == n2) {
                    break Label_0030;
                }
            }
            array2 = new int[n + 1];
        }
        array2[0] = 0;
        final int n3 = n2 / n;
        final int n4 = n2 % n;
        int n5 = 0;
        n2 = 0;
        for (int i = 1; i <= n; ++i) {
            final int n6 = n3;
            final int n7 = n2 += n4;
            int n8 = n6;
            if (n7 > 0) {
                n2 = n7;
                n8 = n6;
                if (n - n7 < n4) {
                    n8 = n6 + 1;
                    n2 = n7 - n;
                }
            }
            n5 += n8;
            array2[i] = n5;
        }
        return array2;
    }
    
    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }
    
    private void ensureAnchorIsInCorrectSpan(final Recycler recycler, final State state, final AnchorInfo anchorInfo, int i) {
        int n = 1;
        if (i != 1) {
            n = 0;
        }
        i = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (n != 0) {
            while (i > 0 && anchorInfo.mPosition > 0) {
                --anchorInfo.mPosition;
                i = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
        }
        else {
            final int itemCount = state.getItemCount();
            final int mPosition = anchorInfo.mPosition;
            int n2 = i;
            int spanIndex;
            for (i = mPosition; i < itemCount - 1; ++i, n2 = spanIndex) {
                spanIndex = this.getSpanIndex(recycler, state, i + 1);
                if (spanIndex <= n2) {
                    break;
                }
            }
            anchorInfo.mPosition = i;
        }
    }
    
    private void ensureViewSet() {
        if (this.mSet == null || this.mSet.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }
    
    private int getSpanGroupIndex(final Recycler recycler, final State state, int i) {
        if (!state.isPreLayout()) {
            i = this.mSpanSizeLookup.getSpanGroupIndex(i, this.mSpanCount);
        }
        else {
            final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
            if (convertPreLayoutPositionToPostLayout == -1) {
                Log.w("GridLayoutManager", "Cannot find span size for pre layout position. " + i);
                i = 0;
            }
            else {
                i = this.mSpanSizeLookup.getSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
            }
        }
        return i;
    }
    
    private int getSpanIndex(final Recycler recycler, final State state, final int i) {
        int n;
        if (!state.isPreLayout()) {
            n = this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
        }
        else if ((n = this.mPreLayoutSpanIndexCache.get(i, -1)) == -1) {
            final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
            if (convertPreLayoutPositionToPostLayout == -1) {
                Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + i);
                n = 0;
            }
            else {
                n = this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
            }
        }
        return n;
    }
    
    private int getSpanSize(final Recycler recycler, final State state, final int i) {
        int n;
        if (!state.isPreLayout()) {
            n = this.mSpanSizeLookup.getSpanSize(i);
        }
        else if ((n = this.mPreLayoutSpanSizeCache.get(i, -1)) == -1) {
            final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
            if (convertPreLayoutPositionToPostLayout == -1) {
                Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + i);
                n = 1;
            }
            else {
                n = this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
            }
        }
        return n;
    }
    
    private void guessMeasurement(final float n, final int b) {
        this.calculateItemBorders(Math.max(Math.round(this.mSpanCount * n), b));
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, int updateSpecWithExtra, final int n, final boolean b, final boolean b2) {
        ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mDecorInsets);
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        int updateSpecWithExtra2 = 0;
        Label_0067: {
            if (!b) {
                updateSpecWithExtra2 = updateSpecWithExtra;
                if (this.mOrientation != 1) {
                    break Label_0067;
                }
            }
            updateSpecWithExtra2 = this.updateSpecWithExtra(updateSpecWithExtra, layoutParams.leftMargin + this.mDecorInsets.left, layoutParams.rightMargin + this.mDecorInsets.right);
        }
        Label_0113: {
            if (!b) {
                updateSpecWithExtra = n;
                if (this.mOrientation != 0) {
                    break Label_0113;
                }
            }
            updateSpecWithExtra = this.updateSpecWithExtra(n, layoutParams.topMargin + this.mDecorInsets.top, layoutParams.bottomMargin + this.mDecorInsets.bottom);
        }
        boolean b3;
        if (b2) {
            b3 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, updateSpecWithExtra2, updateSpecWithExtra, layoutParams);
        }
        else {
            b3 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, updateSpecWithExtra2, updateSpecWithExtra, layoutParams);
        }
        if (b3) {
            view.measure(updateSpecWithExtra2, updateSpecWithExtra);
        }
    }
    
    private void updateMeasurements() {
        int n;
        if (this.getOrientation() == 1) {
            n = ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingRight() - ((RecyclerView.LayoutManager)this).getPaddingLeft();
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getHeight() - ((RecyclerView.LayoutManager)this).getPaddingBottom() - ((RecyclerView.LayoutManager)this).getPaddingTop();
        }
        this.calculateItemBorders(n);
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
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @Override
    View findReferenceChild(final Recycler recycler, final State state, int i, final int n, final int n2) {
        this.ensureLayoutState();
        View view = null;
        View view2 = null;
        final int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        final int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int n3;
        if (n > i) {
            n3 = 1;
        }
        else {
            n3 = -1;
        }
        while (i != n) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int position = ((RecyclerView.LayoutManager)this).getPosition(child);
            View view3 = view;
            View view4 = view2;
            if (position >= 0) {
                view3 = view;
                view4 = view2;
                if (position < n2) {
                    if (this.getSpanIndex(recycler, state, position) != 0) {
                        view4 = view2;
                        view3 = view;
                    }
                    else if (((RecyclerView.LayoutParams)child.getLayoutParams()).isItemRemoved()) {
                        view3 = view;
                        view4 = view2;
                        if (view == null) {
                            view3 = child;
                            view4 = view2;
                        }
                    }
                    else {
                        if (this.mOrientationHelper.getDecoratedStart(child) < endAfterPadding) {
                            final View view5 = child;
                            if (this.mOrientationHelper.getDecoratedEnd(child) >= startAfterPadding) {
                                return view5;
                            }
                        }
                        view3 = view;
                        if ((view4 = view2) == null) {
                            view3 = view;
                            view4 = child;
                        }
                    }
                }
            }
            i += n3;
            view = view3;
            view2 = view4;
        }
        if (view2 == null) {
            view2 = view;
        }
        return view2;
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
        int mSpanCount;
        if (this.mOrientation == 1) {
            mSpanCount = this.mSpanCount;
        }
        else if (state.getItemCount() < 1) {
            mSpanCount = 0;
        }
        else {
            mSpanCount = this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
        }
        return mSpanCount;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        int mSpanCount;
        if (this.mOrientation == 0) {
            mSpanCount = this.mSpanCount;
        }
        else if (state.getItemCount() < 1) {
            mSpanCount = 0;
        }
        else {
            mSpanCount = this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
        }
        return mSpanCount;
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }
    
    @Override
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final int modeInOther = this.mOrientationHelper.getModeInOther();
        boolean b;
        if (modeInOther != 1073741824) {
            b = true;
        }
        else {
            b = false;
        }
        int n;
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            n = this.mCachedBorders[this.mSpanCount];
        }
        else {
            n = 0;
        }
        if (b) {
            this.updateMeasurements();
        }
        final boolean b2 = layoutState.mItemDirection == 1;
        final int n2 = 0;
        final int n3 = 0;
        int mSpanCount = this.mSpanCount;
        int n4 = n2;
        int n5 = n3;
        if (!b2) {
            mSpanCount = this.getSpanIndex(recycler, state, layoutState.mCurrentPosition) + this.getSpanSize(recycler, state, layoutState.mCurrentPosition);
            n5 = n3;
            n4 = n2;
        }
        while (n4 < this.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0) {
            final int mCurrentPosition = layoutState.mCurrentPosition;
            final int spanSize = this.getSpanSize(recycler, state, mCurrentPosition);
            if (spanSize > this.mSpanCount) {
                throw new IllegalArgumentException("Item at position " + mCurrentPosition + " requires " + spanSize + " spans but GridLayoutManager has only " + this.mSpanCount + " spans.");
            }
            mSpanCount -= spanSize;
            if (mSpanCount < 0) {
                break;
            }
            final View next = layoutState.next(recycler);
            if (next == null) {
                break;
            }
            n5 += spanSize;
            this.mSet[n4] = next;
            ++n4;
        }
        if (n4 == 0) {
            layoutChunkResult.mFinished = true;
        }
        else {
            int n6 = 0;
            float n7 = 0.0f;
            this.assignSpans(recycler, state, n4, n5, b2);
            int n12;
            float n14;
            for (int i = 0; i < n4; ++i, n6 = n12, n7 = n14) {
                final View view = this.mSet[i];
                if (layoutState.mScrapList == null) {
                    if (b2) {
                        ((RecyclerView.LayoutManager)this).addView(view);
                    }
                    else {
                        ((RecyclerView.LayoutManager)this).addView(view, 0);
                    }
                }
                else if (b2) {
                    ((RecyclerView.LayoutManager)this).addDisappearingView(view);
                }
                else {
                    ((RecyclerView.LayoutManager)this).addDisappearingView(view, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                final int n8 = this.mCachedBorders[layoutParams.mSpanIndex + layoutParams.mSpanSize];
                final int n9 = this.mCachedBorders[layoutParams.mSpanIndex];
                int n10;
                if (this.mOrientation == 0) {
                    n10 = layoutParams.height;
                }
                else {
                    n10 = layoutParams.width;
                }
                final int childMeasureSpec = RecyclerView.LayoutManager.getChildMeasureSpec(n8 - n9, modeInOther, 0, n10, false);
                final int totalSpace = this.mOrientationHelper.getTotalSpace();
                final int mode = this.mOrientationHelper.getMode();
                int n11;
                if (this.mOrientation == 1) {
                    n11 = layoutParams.height;
                }
                else {
                    n11 = layoutParams.width;
                }
                final int childMeasureSpec2 = RecyclerView.LayoutManager.getChildMeasureSpec(totalSpace, mode, 0, n11, true);
                if (this.mOrientation == 1) {
                    this.measureChildWithDecorationsAndMargin(view, childMeasureSpec, childMeasureSpec2, layoutParams.height == -1, false);
                }
                else {
                    this.measureChildWithDecorationsAndMargin(view, childMeasureSpec2, childMeasureSpec, layoutParams.width == -1, false);
                }
                final int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
                n12 = n6;
                if (decoratedMeasurement > n6) {
                    n12 = decoratedMeasurement;
                }
                final float n13 = 1.0f * this.mOrientationHelper.getDecoratedMeasurementInOther(view) / layoutParams.mSpanSize;
                n14 = n7;
                if (n13 > n7) {
                    n14 = n13;
                }
            }
            int mConsumed = n6;
            if (b) {
                this.guessMeasurement(n7, n);
                int n15 = 0;
                int n16 = 0;
                while (true) {
                    mConsumed = n15;
                    if (n16 >= n4) {
                        break;
                    }
                    final View view2 = this.mSet[n16];
                    final LayoutParams layoutParams2 = (LayoutParams)view2.getLayoutParams();
                    final int n17 = this.mCachedBorders[layoutParams2.mSpanIndex + layoutParams2.mSpanSize];
                    final int n18 = this.mCachedBorders[layoutParams2.mSpanIndex];
                    int n19;
                    if (this.mOrientation == 0) {
                        n19 = layoutParams2.height;
                    }
                    else {
                        n19 = layoutParams2.width;
                    }
                    final int childMeasureSpec3 = RecyclerView.LayoutManager.getChildMeasureSpec(n17 - n18, 1073741824, 0, n19, false);
                    final int totalSpace2 = this.mOrientationHelper.getTotalSpace();
                    final int mode2 = this.mOrientationHelper.getMode();
                    int n20;
                    if (this.mOrientation == 1) {
                        n20 = layoutParams2.height;
                    }
                    else {
                        n20 = layoutParams2.width;
                    }
                    final int childMeasureSpec4 = RecyclerView.LayoutManager.getChildMeasureSpec(totalSpace2, mode2, 0, n20, true);
                    if (this.mOrientation == 1) {
                        this.measureChildWithDecorationsAndMargin(view2, childMeasureSpec3, childMeasureSpec4, false, true);
                    }
                    else {
                        this.measureChildWithDecorationsAndMargin(view2, childMeasureSpec4, childMeasureSpec3, false, true);
                    }
                    final int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view2);
                    int n21 = n15;
                    if (decoratedMeasurement2 > n15) {
                        n21 = decoratedMeasurement2;
                    }
                    ++n16;
                    n15 = n21;
                }
            }
            final int measureSpec = View$MeasureSpec.makeMeasureSpec(mConsumed, 1073741824);
            for (int j = 0; j < n4; ++j) {
                final View view3 = this.mSet[j];
                if (this.mOrientationHelper.getDecoratedMeasurement(view3) != mConsumed) {
                    final LayoutParams layoutParams3 = (LayoutParams)view3.getLayoutParams();
                    final int n22 = this.mCachedBorders[layoutParams3.mSpanIndex + layoutParams3.mSpanSize];
                    final int n23 = this.mCachedBorders[layoutParams3.mSpanIndex];
                    int n24;
                    if (this.mOrientation == 0) {
                        n24 = layoutParams3.height;
                    }
                    else {
                        n24 = layoutParams3.width;
                    }
                    final int childMeasureSpec5 = RecyclerView.LayoutManager.getChildMeasureSpec(n22 - n23, 1073741824, 0, n24, false);
                    if (this.mOrientation == 1) {
                        this.measureChildWithDecorationsAndMargin(view3, childMeasureSpec5, measureSpec, true, true);
                    }
                    else {
                        this.measureChildWithDecorationsAndMargin(view3, measureSpec, childMeasureSpec5, true, true);
                    }
                }
            }
            layoutChunkResult.mConsumed = mConsumed;
            int mOffset = 0;
            int mOffset2 = 0;
            final int n25 = 0;
            int mOffset3 = 0;
            int n26;
            if (this.mOrientation == 1) {
                if (layoutState.mLayoutDirection == -1) {
                    mOffset3 = layoutState.mOffset;
                    n26 = mOffset3 - mConsumed;
                }
                else {
                    final int mOffset4 = layoutState.mOffset;
                    mOffset3 = mOffset4 + mConsumed;
                    n26 = mOffset4;
                }
            }
            else if (layoutState.mLayoutDirection == -1) {
                mOffset2 = layoutState.mOffset;
                mOffset = mOffset2 - mConsumed;
                n26 = n25;
            }
            else {
                mOffset = layoutState.mOffset;
                mOffset2 = mOffset + mConsumed;
                n26 = n25;
            }
            final int n27 = 0;
            int n28 = mOffset2;
            int k = n27;
            int n29 = mOffset3;
            while (k < n4) {
                final View view4 = this.mSet[k];
                final LayoutParams layoutParams4 = (LayoutParams)view4.getLayoutParams();
                int n30;
                if (this.mOrientation == 1) {
                    if (this.isLayoutRTL()) {
                        n30 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[layoutParams4.mSpanIndex + layoutParams4.mSpanSize];
                        mOffset = n30 - this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    }
                    else {
                        mOffset = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[layoutParams4.mSpanIndex];
                        n30 = mOffset + this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    }
                }
                else {
                    n26 = ((RecyclerView.LayoutManager)this).getPaddingTop() + this.mCachedBorders[layoutParams4.mSpanIndex];
                    n29 = n26 + this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    n30 = n28;
                }
                ((RecyclerView.LayoutManager)this).layoutDecorated(view4, mOffset + layoutParams4.leftMargin, n26 + layoutParams4.topMargin, n30 - layoutParams4.rightMargin, n29 - layoutParams4.bottomMargin);
                if (((RecyclerView.LayoutParams)layoutParams4).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams4).isItemChanged()) {
                    layoutChunkResult.mIgnoreConsumed = true;
                }
                layoutChunkResult.mFocusable |= view4.isFocusable();
                ++k;
                n28 = n30;
            }
            Arrays.fill(this.mSet, null);
        }
    }
    
    @Override
    void onAnchorReady(final Recycler recycler, final State state, final AnchorInfo anchorInfo, final int n) {
        super.onAnchorReady(recycler, state, anchorInfo, n);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, n);
        }
        this.ensureViewSet();
    }
    
    @Override
    public View onFocusSearchFailed(View view, int max, final Recycler recycler, final State state) {
        final View containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(view);
        if (containingItemView == null) {
            view = null;
        }
        else {
            final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
            final int access$000 = layoutParams.mSpanIndex;
            final int n = layoutParams.mSpanIndex + layoutParams.mSpanSize;
            if (super.onFocusSearchFailed(view, max, recycler, state) == null) {
                view = null;
            }
            else {
                if (this.convertFocusDirectionToLayoutDirection(max) == 1 != this.mShouldReverseLayout) {
                    max = 1;
                }
                else {
                    max = 0;
                }
                int n2;
                int childCount;
                if (max != 0) {
                    max = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
                    n2 = -1;
                    childCount = -1;
                }
                else {
                    max = 0;
                    n2 = 1;
                    childCount = ((RecyclerView.LayoutManager)this).getChildCount();
                }
                final boolean b = this.mOrientation == 1 && this.isLayoutRTL();
                View view2 = null;
                int access$2 = -1;
                int n3 = 0;
                for (int i = max; i != childCount; i += n2) {
                    final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                    if (child == containingItemView) {
                        break;
                    }
                    if (child.isFocusable()) {
                        final LayoutParams layoutParams2 = (LayoutParams)child.getLayoutParams();
                        final int access$3 = layoutParams2.mSpanIndex;
                        final int n4 = layoutParams2.mSpanIndex + layoutParams2.mSpanSize;
                        if (access$3 == access$000) {
                            view = child;
                            if (n4 == n) {
                                return view;
                            }
                        }
                        final int n5 = 0;
                        if (view2 == null) {
                            max = 1;
                        }
                        else {
                            max = Math.max(access$3, access$000);
                            final int n6 = Math.min(n4, n) - max;
                            if (n6 > n3) {
                                max = 1;
                            }
                            else {
                                max = n5;
                                if (n6 == n3) {
                                    final boolean b2 = access$3 > access$2;
                                    max = n5;
                                    if (b == b2) {
                                        max = 1;
                                    }
                                }
                            }
                        }
                        if (max != 0) {
                            view2 = child;
                            access$2 = layoutParams2.mSpanIndex;
                            n3 = Math.min(n4, n) - Math.max(access$3, access$000);
                        }
                    }
                }
                view = view2;
            }
        }
        return view;
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
        }
        else {
            final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
            final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, ((RecyclerView.LayoutParams)layoutParams2).getViewLayoutPosition());
            if (this.mOrientation == 0) {
                accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), spanGroupIndex, 1, this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount, false));
            }
            else {
                accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount, false));
            }
        }
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
        if (!state.isPreLayout()) {
            this.mPendingSpanCountChange = false;
        }
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollHorizontallyBy(n, recycler, state);
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollVerticallyBy(n, recycler, state);
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int n, int n2) {
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(rect, n, n2);
        }
        final int n3 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n4 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            n2 = RecyclerView.LayoutManager.chooseSize(n2, rect.height() + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            n = RecyclerView.LayoutManager.chooseSize(n, this.mCachedBorders[this.mCachedBorders.length - 1] + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
        }
        else {
            n = RecyclerView.LayoutManager.chooseSize(n, rect.width() + n3, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            n2 = RecyclerView.LayoutManager.chooseSize(n2, this.mCachedBorders[this.mCachedBorders.length - 1] + n4, ((RecyclerView.LayoutManager)this).getMinimumHeight());
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(n, n2);
    }
    
    public void setSpanCount(final int n) {
        if (n != this.mSpanCount) {
            this.mPendingSpanCountChange = true;
            if (n < 1) {
                throw new IllegalArgumentException("Span count should be at least 1. Provided " + n);
            }
            this.mSpanCount = n;
            this.mSpanSizeLookup.invalidateSpanIndexCache();
        }
    }
    
    public void setSpanSizeLookup(final SpanSizeLookup mSpanSizeLookup) {
        this.mSpanSizeLookup = mSpanSizeLookup;
    }
    
    @Override
    public void setStackFromEnd(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }
    
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup
    {
        @Override
        public int getSpanIndex(final int n, final int n2) {
            return n % n2;
        }
        
        @Override
        public int getSpanSize(final int n) {
            return 1;
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        private int mSpanIndex;
        private int mSpanSize;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public int getSpanIndex() {
            return this.mSpanIndex;
        }
        
        public int getSpanSize() {
            return this.mSpanSize;
        }
    }
    
    public abstract static class SpanSizeLookup
    {
        private boolean mCacheSpanIndices;
        final SparseIntArray mSpanIndexCache;
        
        public SpanSizeLookup() {
            this.mSpanIndexCache = new SparseIntArray();
            this.mCacheSpanIndices = false;
        }
        
        int findReferenceIndexFromCache(int key) {
            int i = 0;
            int n = this.mSpanIndexCache.size() - 1;
            while (i <= n) {
                final int n2 = i + n >>> 1;
                if (this.mSpanIndexCache.keyAt(n2) < key) {
                    i = n2 + 1;
                }
                else {
                    n = n2 - 1;
                }
            }
            key = i - 1;
            if (key >= 0 && key < this.mSpanIndexCache.size()) {
                key = this.mSpanIndexCache.keyAt(key);
            }
            else {
                key = -1;
            }
            return key;
        }
        
        int getCachedSpanIndex(final int n, final int n2) {
            int n3;
            if (!this.mCacheSpanIndices) {
                n3 = this.getSpanIndex(n, n2);
            }
            else if ((n3 = this.mSpanIndexCache.get(n, -1)) == -1) {
                n3 = this.getSpanIndex(n, n2);
                this.mSpanIndexCache.put(n, n3);
            }
            return n3;
        }
        
        public int getSpanGroupIndex(int n, final int n2) {
            int n3 = 0;
            int n4 = 0;
            final int spanSize = this.getSpanSize(n);
            int n6;
            for (int i = 0; i < n; ++i, n4 = n6) {
                final int spanSize2 = this.getSpanSize(i);
                final int n5 = n3 + spanSize2;
                if (n5 == n2) {
                    n3 = 0;
                    n6 = n4 + 1;
                }
                else {
                    n6 = n4;
                    if ((n3 = n5) > n2) {
                        n3 = spanSize2;
                        n6 = n4 + 1;
                    }
                }
            }
            n = n4;
            if (n3 + spanSize > n2) {
                n = n4 + 1;
            }
            return n;
        }
        
        public int getSpanIndex(int n, final int n2) {
            final int spanSize = this.getSpanSize(n);
            if (spanSize == n2) {
                n = 0;
            }
            else {
                final int n3 = 0;
                final int n4 = 0;
                int n5 = n3;
                int i = n4;
                if (this.mCacheSpanIndices) {
                    n5 = n3;
                    i = n4;
                    if (this.mSpanIndexCache.size() > 0) {
                        final int referenceIndexFromCache = this.findReferenceIndexFromCache(n);
                        n5 = n3;
                        i = n4;
                        if (referenceIndexFromCache >= 0) {
                            n5 = this.mSpanIndexCache.get(referenceIndexFromCache) + this.getSpanSize(referenceIndexFromCache);
                            i = referenceIndexFromCache + 1;
                        }
                    }
                }
                while (i < n) {
                    final int spanSize2 = this.getSpanSize(i);
                    final int n6 = n5 + spanSize2;
                    if (n6 == n2) {
                        n5 = 0;
                    }
                    else if ((n5 = n6) > n2) {
                        n5 = spanSize2;
                    }
                    ++i;
                }
                n = n5;
                if (n5 + spanSize > n2) {
                    n = 0;
                }
            }
            return n;
        }
        
        public abstract int getSpanSize(final int p0);
        
        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }
        
        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }
        
        public void setSpanIndexCacheEnabled(final boolean mCacheSpanIndices) {
            this.mCacheSpanIndices = mCacheSpanIndices;
        }
    }
}
