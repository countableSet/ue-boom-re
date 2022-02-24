// 
// Decompiled by Procyon v0.5.36
// 

package antistatic.spinnerwheel;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.view.animation.Interpolator;
import android.os.Parcelable;
import java.util.Iterator;
import android.content.res.TypedArray;
import android.view.MotionEvent;
import android.view.ViewGroup;
import java.util.LinkedList;
import android.util.AttributeSet;
import android.content.Context;
import antistatic.spinnerwheel.adapters.WheelViewAdapter;
import android.widget.LinearLayout;
import android.database.DataSetObserver;
import java.util.List;
import android.view.View;

public abstract class AbstractWheel extends View
{
    private static final boolean DEF_IS_CYCLIC = false;
    private static final int DEF_VISIBLE_ITEMS = 4;
    private static int itemID;
    private final String LOG_TAG;
    private List<OnWheelChangedListener> changingListeners;
    private List<OnWheelClickedListener> clickingListeners;
    protected int mCurrentItemIdx;
    private DataSetObserver mDataObserver;
    protected int mFirstItemIdx;
    protected boolean mIsAllVisible;
    protected boolean mIsCyclic;
    protected boolean mIsScrollingPerformed;
    protected LinearLayout mItemsLayout;
    protected int mLayoutHeight;
    protected int mLayoutWidth;
    private WheelRecycler mRecycler;
    protected WheelScroller mScroller;
    protected int mScrollingOffset;
    protected WheelViewAdapter mViewAdapter;
    protected int mVisibleItems;
    private List<OnWheelScrollListener> scrollingListeners;
    
    static {
        AbstractWheel.itemID = -1;
    }
    
    public AbstractWheel(final Context context, final AttributeSet set, final int n) {
        super(context, set);
        this.LOG_TAG = AbstractWheel.class.getName() + " #" + ++AbstractWheel.itemID;
        this.mCurrentItemIdx = 0;
        this.mRecycler = new WheelRecycler(this);
        this.changingListeners = new LinkedList<OnWheelChangedListener>();
        this.scrollingListeners = new LinkedList<OnWheelScrollListener>();
        this.clickingListeners = new LinkedList<OnWheelClickedListener>();
        this.initAttributes(set, n);
        this.initData(context);
    }
    
    private boolean addItemView(final int n, final boolean b) {
        boolean b2 = false;
        final View itemView = this.getItemView(n);
        if (itemView != null) {
            if (b) {
                this.mItemsLayout.addView(itemView, 0);
            }
            else {
                this.mItemsLayout.addView(itemView);
            }
            b2 = true;
        }
        return b2;
    }
    
    private void doScroll(int mCurrentItemIdx) {
        this.mScrollingOffset += mCurrentItemIdx;
        final int itemDimension = this.getItemDimension();
        final int n = this.mScrollingOffset / itemDimension;
        final int n2 = this.mCurrentItemIdx - n;
        final int itemsCount = this.mViewAdapter.getItemsCount();
        int n3;
        mCurrentItemIdx = (n3 = this.mScrollingOffset % itemDimension);
        if (Math.abs(mCurrentItemIdx) <= itemDimension / 2) {
            n3 = 0;
        }
        int n4;
        if (this.mIsCyclic && itemsCount > 0) {
            int i;
            if (n3 > 0) {
                i = n2 - 1;
                mCurrentItemIdx = n + 1;
            }
            else {
                mCurrentItemIdx = n;
                i = n2;
                if (n3 < 0) {
                    i = n2 + 1;
                    mCurrentItemIdx = n - 1;
                }
            }
            while (i < 0) {
                i += itemsCount;
            }
            n4 = i % itemsCount;
        }
        else if (n2 < 0) {
            mCurrentItemIdx = this.mCurrentItemIdx;
            n4 = 0;
        }
        else if (n2 >= itemsCount) {
            mCurrentItemIdx = this.mCurrentItemIdx - itemsCount + 1;
            n4 = itemsCount - 1;
        }
        else if (n2 > 0 && n3 > 0) {
            n4 = n2 - 1;
            mCurrentItemIdx = n + 1;
        }
        else {
            mCurrentItemIdx = n;
            if ((n4 = n2) < itemsCount - 1) {
                mCurrentItemIdx = n;
                n4 = n2;
                if (n3 < 0) {
                    n4 = n2 + 1;
                    mCurrentItemIdx = n - 1;
                }
            }
        }
        final int mScrollingOffset = this.mScrollingOffset;
        if (n4 != this.mCurrentItemIdx) {
            this.setCurrentItem(n4, false);
        }
        else {
            this.invalidate();
        }
        final int baseDimension = this.getBaseDimension();
        this.mScrollingOffset = mScrollingOffset - mCurrentItemIdx * itemDimension;
        if (this.mScrollingOffset > baseDimension) {
            this.mScrollingOffset = this.mScrollingOffset % baseDimension + baseDimension;
        }
    }
    
    private View getItemView(final int n) {
        View view;
        if (this.mViewAdapter == null || this.mViewAdapter.getItemsCount() == 0) {
            view = null;
        }
        else {
            final int itemsCount = this.mViewAdapter.getItemsCount();
            int i = n;
            if (!this.isValidItemIndex(n)) {
                view = this.mViewAdapter.getEmptyItem(this.mRecycler.getEmptyItem(), (ViewGroup)this.mItemsLayout);
            }
            else {
                while (i < 0) {
                    i += itemsCount;
                }
                view = this.mViewAdapter.getItem(i % itemsCount, this.mRecycler.getItem(), (ViewGroup)this.mItemsLayout);
            }
        }
        return view;
    }
    
    private ItemsRange getItemsRange() {
        if (this.mIsAllVisible) {
            final int baseDimension = this.getBaseDimension();
            final int itemDimension = this.getItemDimension();
            if (itemDimension != 0) {
                this.mVisibleItems = baseDimension / itemDimension + 1;
            }
        }
        final int n = this.mCurrentItemIdx - this.mVisibleItems / 2;
        final int mVisibleItems = this.mVisibleItems;
        int n2;
        if (this.mVisibleItems % 2 == 0) {
            n2 = 0;
        }
        else {
            n2 = 1;
        }
        int n4;
        final int n3 = n4 = n + mVisibleItems - n2;
        int n5 = n;
        if (this.mScrollingOffset != 0) {
            if (this.mScrollingOffset > 0) {
                n5 = n - 1;
                n4 = n3;
            }
            else {
                n4 = n3 + 1;
                n5 = n;
            }
        }
        int itemsCount = n4;
        int n6 = n5;
        if (!this.isCyclic()) {
            int n7;
            if ((n7 = n5) < 0) {
                n7 = 0;
            }
            if (this.mViewAdapter == null) {
                itemsCount = 0;
                n6 = n7;
            }
            else {
                itemsCount = n4;
                n6 = n7;
                if (n4 > this.mViewAdapter.getItemsCount()) {
                    itemsCount = this.mViewAdapter.getItemsCount();
                    n6 = n7;
                }
            }
        }
        return new ItemsRange(n6, itemsCount - n6 + 1);
    }
    
    public void addChangingListener(final OnWheelChangedListener onWheelChangedListener) {
        this.changingListeners.add(onWheelChangedListener);
    }
    
    public void addClickingListener(final OnWheelClickedListener onWheelClickedListener) {
        this.clickingListeners.add(onWheelClickedListener);
    }
    
    public void addScrollingListener(final OnWheelScrollListener onWheelScrollListener) {
        this.scrollingListeners.add(onWheelScrollListener);
    }
    
    protected abstract void createItemsLayout();
    
    protected abstract WheelScroller createScroller(final WheelScroller.ScrollingListener p0);
    
    protected abstract void doItemsLayout();
    
    protected abstract int getBaseDimension();
    
    public int getCurrentItem() {
        return this.mCurrentItemIdx;
    }
    
    protected abstract int getItemDimension();
    
    protected abstract float getMotionEventPosition(final MotionEvent p0);
    
    public WheelViewAdapter getViewAdapter() {
        return this.mViewAdapter;
    }
    
    public int getVisibleItems() {
        return this.mVisibleItems;
    }
    
    protected void initAttributes(final AttributeSet set, final int n) {
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.AbstractWheelView, n, 0);
        this.mVisibleItems = obtainStyledAttributes.getInt(R.styleable.AbstractWheelView_visibleItems, 4);
        this.mIsAllVisible = obtainStyledAttributes.getBoolean(R.styleable.AbstractWheelView_isAllVisible, false);
        this.mIsCyclic = obtainStyledAttributes.getBoolean(R.styleable.AbstractWheelView_isCyclic, false);
        obtainStyledAttributes.recycle();
    }
    
    protected void initData(final Context context) {
        this.mDataObserver = new DataSetObserver() {
            public void onChanged() {
                AbstractWheel.this.invalidateItemsLayout(false);
            }
            
            public void onInvalidated() {
                AbstractWheel.this.invalidateItemsLayout(true);
            }
        };
        this.mScroller = this.createScroller(new WheelScroller.ScrollingListener() {
            @Override
            public void onFinished() {
                if (AbstractWheel.this.mIsScrollingPerformed) {
                    AbstractWheel.this.notifyScrollingListenersAboutEnd();
                    AbstractWheel.this.mIsScrollingPerformed = false;
                    AbstractWheel.this.onScrollFinished();
                }
                AbstractWheel.this.mScrollingOffset = 0;
                AbstractWheel.this.invalidate();
            }
            
            @Override
            public void onJustify() {
                if (Math.abs(AbstractWheel.this.mScrollingOffset) > 1) {
                    AbstractWheel.this.mScroller.scroll(AbstractWheel.this.mScrollingOffset, 0);
                }
            }
            
            @Override
            public void onScroll(int baseDimension) {
                AbstractWheel.this.doScroll(baseDimension);
                baseDimension = AbstractWheel.this.getBaseDimension();
                if (AbstractWheel.this.mScrollingOffset > baseDimension) {
                    AbstractWheel.this.mScrollingOffset = baseDimension;
                    AbstractWheel.this.mScroller.stopScrolling();
                }
                else if (AbstractWheel.this.mScrollingOffset < -baseDimension) {
                    AbstractWheel.this.mScrollingOffset = -baseDimension;
                    AbstractWheel.this.mScroller.stopScrolling();
                }
            }
            
            @Override
            public void onStarted() {
                AbstractWheel.this.mIsScrollingPerformed = true;
                AbstractWheel.this.notifyScrollingListenersAboutStart();
                AbstractWheel.this.onScrollStarted();
            }
            
            @Override
            public void onTouch() {
                AbstractWheel.this.onScrollTouched();
            }
            
            @Override
            public void onTouchUp() {
                if (!AbstractWheel.this.mIsScrollingPerformed) {
                    AbstractWheel.this.onScrollTouchedUp();
                }
            }
        });
    }
    
    public void invalidateItemsLayout(final boolean b) {
        if (b) {
            this.mRecycler.clearAll();
            if (this.mItemsLayout != null) {
                this.mItemsLayout.removeAllViews();
            }
            this.mScrollingOffset = 0;
        }
        else if (this.mItemsLayout != null) {
            this.mRecycler.recycleItems(this.mItemsLayout, this.mFirstItemIdx, new ItemsRange());
        }
        this.invalidate();
    }
    
    public boolean isCyclic() {
        return this.mIsCyclic;
    }
    
    protected boolean isValidItemIndex(final int n) {
        return this.mViewAdapter != null && this.mViewAdapter.getItemsCount() > 0 && (this.mIsCyclic || (n >= 0 && n < this.mViewAdapter.getItemsCount()));
    }
    
    protected void notifyChangingListeners(final int n, final int n2) {
        final Iterator<OnWheelChangedListener> iterator = this.changingListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onChanged(this, n, n2);
        }
    }
    
    protected void notifyClickListenersAboutClick(final int n) {
        final Iterator<OnWheelClickedListener> iterator = this.clickingListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onItemClicked(this, n);
        }
    }
    
    protected void notifyScrollingListenersAboutEnd() {
        final Iterator<OnWheelScrollListener> iterator = this.scrollingListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onScrollingFinished(this);
        }
    }
    
    protected void notifyScrollingListenersAboutStart() {
        final Iterator<OnWheelScrollListener> iterator = this.scrollingListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onScrollingStarted(this);
        }
    }
    
    protected void onLayout(final boolean b, int mLayoutWidth, int mLayoutHeight, final int n, final int n2) {
        if (b) {
            mLayoutWidth = n - mLayoutWidth;
            mLayoutHeight = n2 - mLayoutHeight;
            this.doItemsLayout();
            if (this.mLayoutWidth != mLayoutWidth || this.mLayoutHeight != mLayoutHeight) {
                this.recreateAssets(this.getMeasuredWidth(), this.getMeasuredHeight());
            }
            this.mLayoutWidth = mLayoutWidth;
            this.mLayoutHeight = mLayoutHeight;
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
        }
        else {
            final SavedState savedState = (SavedState)parcelable;
            super.onRestoreInstanceState(savedState.getSuperState());
            this.mCurrentItemIdx = savedState.currentItem;
            this.postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    AbstractWheel.this.invalidateItemsLayout(false);
                }
            }, 100L);
        }
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentItem = this.getCurrentItem();
        return (Parcelable)savedState;
    }
    
    protected void onScrollFinished() {
    }
    
    protected void onScrollStarted() {
    }
    
    protected void onScrollTouched() {
    }
    
    protected void onScrollTouchedUp() {
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        boolean onTouchEvent = true;
        if (this.isEnabled()) {
            if (this.getViewAdapter() == null) {
                onTouchEvent = onTouchEvent;
            }
            else {
                switch (motionEvent.getAction()) {
                    case 0:
                    case 2: {
                        if (this.getParent() != null) {
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (this.mIsScrollingPerformed) {
                            break;
                        }
                        final int n = (int)this.getMotionEventPosition(motionEvent) - this.getBaseDimension() / 2;
                        int n2;
                        if (n > 0) {
                            n2 = n + this.getItemDimension() / 2;
                        }
                        else {
                            n2 = n - this.getItemDimension() / 2;
                        }
                        final int n3 = n2 / this.getItemDimension();
                        if (n3 != 0 && this.isValidItemIndex(this.mCurrentItemIdx + n3)) {
                            this.notifyClickListenersAboutClick(this.mCurrentItemIdx + n3);
                            break;
                        }
                        break;
                    }
                }
                onTouchEvent = this.mScroller.onTouchEvent(motionEvent);
            }
        }
        return onTouchEvent;
    }
    
    protected boolean rebuildItems() {
        final ItemsRange itemsRange = this.getItemsRange();
        int n;
        if (this.mItemsLayout != null) {
            final int recycleItems = this.mRecycler.recycleItems(this.mItemsLayout, this.mFirstItemIdx, itemsRange);
            n = ((this.mFirstItemIdx != recycleItems) ? 1 : 0);
            this.mFirstItemIdx = recycleItems;
        }
        else {
            this.createItemsLayout();
            n = 1;
        }
        boolean b = n != 0;
        if (n == 0) {
            b = (this.mFirstItemIdx != itemsRange.getFirst() || this.mItemsLayout.getChildCount() != itemsRange.getCount());
        }
        if (this.mFirstItemIdx > itemsRange.getFirst() && this.mFirstItemIdx <= itemsRange.getLast()) {
            for (int mFirstItemIdx = this.mFirstItemIdx - 1; mFirstItemIdx >= itemsRange.getFirst() && this.addItemView(mFirstItemIdx, true); --mFirstItemIdx) {
                this.mFirstItemIdx = mFirstItemIdx;
            }
        }
        else {
            this.mFirstItemIdx = itemsRange.getFirst();
        }
        int mFirstItemIdx2 = this.mFirstItemIdx;
        int n2;
        for (int i = this.mItemsLayout.getChildCount(); i < itemsRange.getCount(); ++i, mFirstItemIdx2 = n2) {
            n2 = mFirstItemIdx2;
            if (!this.addItemView(this.mFirstItemIdx + i, false)) {
                n2 = mFirstItemIdx2;
                if (this.mItemsLayout.getChildCount() == 0) {
                    n2 = mFirstItemIdx2 + 1;
                }
            }
        }
        this.mFirstItemIdx = mFirstItemIdx2;
        return b;
    }
    
    protected abstract void recreateAssets(final int p0, final int p1);
    
    public void removeChangingListener(final OnWheelChangedListener onWheelChangedListener) {
        this.changingListeners.remove(onWheelChangedListener);
    }
    
    public void removeClickingListener(final OnWheelClickedListener onWheelClickedListener) {
        this.clickingListeners.remove(onWheelClickedListener);
    }
    
    public void removeScrollingListener(final OnWheelScrollListener onWheelScrollListener) {
        this.scrollingListeners.remove(onWheelScrollListener);
    }
    
    public void scroll(final int n, final int n2) {
        final int itemDimension = this.getItemDimension();
        final int mScrollingOffset = this.mScrollingOffset;
        this.onScrollTouched();
        this.mScroller.scroll(itemDimension * n - mScrollingOffset, n2);
    }
    
    public void setAllItemsVisible(final boolean mIsAllVisible) {
        this.mIsAllVisible = mIsAllVisible;
        this.invalidateItemsLayout(false);
    }
    
    public void setCurrentItem(final int n) {
        this.setCurrentItem(n, false);
    }
    
    public void setCurrentItem(int i, final boolean b) {
        if (this.mViewAdapter != null && this.mViewAdapter.getItemsCount() != 0) {
            final int itemsCount = this.mViewAdapter.getItemsCount();
            int mCurrentItemIdx;
            if (i < 0 || (mCurrentItemIdx = i) >= itemsCount) {
                if (!this.mIsCyclic) {
                    return;
                }
                while (i < 0) {
                    i += itemsCount;
                }
                mCurrentItemIdx = i % itemsCount;
            }
            if (mCurrentItemIdx != this.mCurrentItemIdx) {
                if (b) {
                    final int a = i = mCurrentItemIdx - this.mCurrentItemIdx;
                    if (this.mIsCyclic) {
                        final int n = Math.min(mCurrentItemIdx, this.mCurrentItemIdx) + itemsCount - Math.max(mCurrentItemIdx, this.mCurrentItemIdx);
                        i = a;
                        if (n < Math.abs(a)) {
                            if (a < 0) {
                                i = n;
                            }
                            else {
                                i = -n;
                            }
                        }
                    }
                    this.scroll(i, 0);
                }
                else {
                    this.mScrollingOffset = 0;
                    i = this.mCurrentItemIdx;
                    this.notifyChangingListeners(i, this.mCurrentItemIdx = mCurrentItemIdx);
                    this.invalidate();
                }
            }
        }
    }
    
    public void setCyclic(final boolean mIsCyclic) {
        this.mIsCyclic = mIsCyclic;
        this.invalidateItemsLayout(false);
    }
    
    public void setInterpolator(final Interpolator interpolator) {
        this.mScroller.setInterpolator(interpolator);
    }
    
    public void setViewAdapter(final WheelViewAdapter mViewAdapter) {
        if (this.mViewAdapter != null) {
            this.mViewAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mViewAdapter = mViewAdapter;
        if (this.mViewAdapter != null) {
            this.mViewAdapter.registerDataSetObserver(this.mDataObserver);
        }
        this.invalidateItemsLayout(true);
    }
    
    public void setVisibleItems(final int mVisibleItems) {
        this.mVisibleItems = mVisibleItems;
    }
    
    public void stopScrolling() {
        this.mScroller.stopScrolling();
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int currentItem;
        
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
        
        private SavedState(final Parcel parcel) {
            super(parcel);
            this.currentItem = parcel.readInt();
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.currentItem);
        }
    }
}
