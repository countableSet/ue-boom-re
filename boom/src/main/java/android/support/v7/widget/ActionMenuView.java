// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.view.ViewDebug$ExportedProperty;
import android.view.ContextThemeWrapper;
import android.support.annotation.StyleRes;
import android.os.Build$VERSION;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuBuilder;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView
{
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    private Callback mMenuBuilderCallback;
    private int mMinCellSize;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;
    
    public ActionMenuView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuView(final Context mPopupContext, final AttributeSet set) {
        super(mPopupContext, set);
        this.setBaselineAligned(false);
        final float density = mPopupContext.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * density);
        this.mGeneratedItemPadding = (int)(4.0f * density);
        this.mPopupContext = mPopupContext;
        this.mPopupTheme = 0;
    }
    
    static int measureChildForCells(final View view, final int n, int n2, int cellsUsed, int n3) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(cellsUsed) - n3, View$MeasureSpec.getMode(cellsUsed));
        ActionMenuItemView actionMenuItemView;
        if (view instanceof ActionMenuItemView) {
            actionMenuItemView = (ActionMenuItemView)view;
        }
        else {
            actionMenuItemView = null;
        }
        if (actionMenuItemView != null && actionMenuItemView.hasText()) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        final int n4 = cellsUsed = 0;
        Label_0131: {
            if (n2 > 0) {
                if (n3 != 0) {
                    cellsUsed = n4;
                    if (n2 < 2) {
                        break Label_0131;
                    }
                }
                view.measure(View$MeasureSpec.makeMeasureSpec(n * n2, Integer.MIN_VALUE), measureSpec);
                final int measuredWidth = view.getMeasuredWidth();
                cellsUsed = (n2 = measuredWidth / n);
                if (measuredWidth % n != 0) {
                    n2 = cellsUsed + 1;
                }
                cellsUsed = n2;
                if (n3 != 0 && (cellsUsed = n2) < 2) {
                    cellsUsed = 2;
                }
            }
        }
        layoutParams.expandable = (!layoutParams.isOverflowButton && n3 != 0);
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(cellsUsed * n, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int mMinCellSize) {
        final int mode = View$MeasureSpec.getMode(mMinCellSize);
        final int size = View$MeasureSpec.getSize(i);
        final int size2 = View$MeasureSpec.getSize(mMinCellSize);
        i = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int n = this.getPaddingTop() + this.getPaddingBottom();
        final int childMeasureSpec = getChildMeasureSpec(mMinCellSize, n, -2);
        final int n2 = size - (i + paddingRight);
        i = n2 / this.mMinCellSize;
        mMinCellSize = this.mMinCellSize;
        if (i == 0) {
            this.setMeasuredDimension(n2, 0);
        }
        else {
            final int n3 = this.mMinCellSize + n2 % mMinCellSize / i;
            int a = 0;
            int a2 = 0;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            long n7 = 0L;
            final int childCount = this.getChildCount();
            long n8;
            int n9;
            for (int j = 0; j < childCount; ++j, n6 = n9, n7 = n8) {
                final View child = this.getChildAt(j);
                if (child.getVisibility() == 8) {
                    n8 = n7;
                    n9 = n6;
                }
                else {
                    final boolean b = child instanceof ActionMenuItemView;
                    final int n10 = n5 + 1;
                    if (b) {
                        child.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                    }
                    final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                    layoutParams.expanded = false;
                    layoutParams.extraPixels = 0;
                    layoutParams.cellsUsed = 0;
                    layoutParams.expandable = false;
                    layoutParams.leftMargin = 0;
                    layoutParams.rightMargin = 0;
                    layoutParams.preventEdgeOffset = (b && ((ActionMenuItemView)child).hasText());
                    if (layoutParams.isOverflowButton) {
                        mMinCellSize = 1;
                    }
                    else {
                        mMinCellSize = i;
                    }
                    final int measureChildForCells = measureChildForCells(child, n3, mMinCellSize, childMeasureSpec, n);
                    final int max = Math.max(a2, measureChildForCells);
                    mMinCellSize = n4;
                    if (layoutParams.expandable) {
                        mMinCellSize = n4 + 1;
                    }
                    if (layoutParams.isOverflowButton) {
                        n6 = 1;
                    }
                    final int n11 = i - measureChildForCells;
                    final int max2 = Math.max(a, child.getMeasuredHeight());
                    i = n11;
                    n4 = mMinCellSize;
                    n9 = n6;
                    a2 = max;
                    a = max2;
                    n8 = n7;
                    n5 = n10;
                    if (measureChildForCells == 1) {
                        n8 = (n7 | (long)(1 << j));
                        i = n11;
                        n4 = mMinCellSize;
                        n9 = n6;
                        a2 = max;
                        a = max2;
                        n5 = n10;
                    }
                }
            }
            final boolean b2 = n6 != 0 && n5 == 2;
            mMinCellSize = 0;
            int n12 = i;
            long k;
            while (true) {
                k = n7;
                if (n4 <= 0) {
                    break;
                }
                k = n7;
                if (n12 <= 0) {
                    break;
                }
                int n13 = Integer.MAX_VALUE;
                long n14 = 0L;
                int n15 = 0;
                long n16;
                int cellsUsed;
                for (int l = 0; l < childCount; ++l, n13 = cellsUsed, n15 = i, n14 = n16) {
                    final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(l).getLayoutParams();
                    if (!layoutParams2.expandable) {
                        n16 = n14;
                        i = n15;
                        cellsUsed = n13;
                    }
                    else if (layoutParams2.cellsUsed < n13) {
                        cellsUsed = layoutParams2.cellsUsed;
                        n16 = 1 << l;
                        i = 1;
                    }
                    else {
                        cellsUsed = n13;
                        i = n15;
                        n16 = n14;
                        if (layoutParams2.cellsUsed == n13) {
                            n16 = (n14 | (long)(1 << l));
                            i = n15 + 1;
                            cellsUsed = n13;
                        }
                    }
                }
                n7 |= n14;
                if (n15 > n12) {
                    k = n7;
                    break;
                }
                View child2;
                LayoutParams layoutParams3;
                long n17;
                for (i = 0; i < childCount; ++i, n12 = mMinCellSize, n7 = n17) {
                    child2 = this.getChildAt(i);
                    layoutParams3 = (LayoutParams)child2.getLayoutParams();
                    if (((long)(1 << i) & n14) == 0x0L) {
                        mMinCellSize = n12;
                        n17 = n7;
                        if (layoutParams3.cellsUsed == n13 + 1) {
                            n17 = (n7 | (long)(1 << i));
                            mMinCellSize = n12;
                        }
                    }
                    else {
                        if (b2 && layoutParams3.preventEdgeOffset && n12 == 1) {
                            child2.setPadding(this.mGeneratedItemPadding + n3, 0, this.mGeneratedItemPadding, 0);
                        }
                        ++layoutParams3.cellsUsed;
                        layoutParams3.expanded = true;
                        mMinCellSize = n12 - 1;
                        n17 = n7;
                    }
                }
                mMinCellSize = 1;
            }
            boolean b3;
            if (n6 == 0 && n5 == 1) {
                b3 = true;
            }
            else {
                b3 = false;
            }
            i = mMinCellSize;
            Label_1155: {
                if (n12 > 0) {
                    i = mMinCellSize;
                    if (k != 0L) {
                        if (n12 >= n5 - 1 && !b3) {
                            i = mMinCellSize;
                            if (a2 <= 1) {
                                break Label_1155;
                            }
                        }
                        float n19;
                        final float n18 = n19 = (float)Long.bitCount(k);
                        if (!b3) {
                            float n20 = n18;
                            if ((0x1L & k) != 0x0L) {
                                n20 = n18;
                                if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                                    n20 = n18 - 0.5f;
                                }
                            }
                            n19 = n20;
                            if (((long)(1 << childCount - 1) & k) != 0x0L) {
                                n19 = n20;
                                if (!((LayoutParams)this.getChildAt(childCount - 1).getLayoutParams()).preventEdgeOffset) {
                                    n19 = n20 - 0.5f;
                                }
                            }
                        }
                        int n21;
                        if (n19 > 0.0f) {
                            n21 = (int)(n12 * n3 / n19);
                        }
                        else {
                            n21 = 0;
                        }
                        for (int n22 = 0; n22 < childCount; ++n22, mMinCellSize = i) {
                            if (((long)(1 << n22) & k) == 0x0L) {
                                i = mMinCellSize;
                            }
                            else {
                                final View child3 = this.getChildAt(n22);
                                final LayoutParams layoutParams4 = (LayoutParams)child3.getLayoutParams();
                                if (child3 instanceof ActionMenuItemView) {
                                    layoutParams4.extraPixels = n21;
                                    layoutParams4.expanded = true;
                                    if (n22 == 0 && !layoutParams4.preventEdgeOffset) {
                                        layoutParams4.leftMargin = -n21 / 2;
                                    }
                                    i = 1;
                                }
                                else if (layoutParams4.isOverflowButton) {
                                    layoutParams4.extraPixels = n21;
                                    layoutParams4.expanded = true;
                                    layoutParams4.rightMargin = -n21 / 2;
                                    i = 1;
                                }
                                else {
                                    if (n22 != 0) {
                                        layoutParams4.leftMargin = n21 / 2;
                                    }
                                    i = mMinCellSize;
                                    if (n22 != childCount - 1) {
                                        layoutParams4.rightMargin = n21 / 2;
                                        i = mMinCellSize;
                                    }
                                }
                            }
                        }
                        i = mMinCellSize;
                    }
                }
            }
            if (i != 0) {
                View child4;
                LayoutParams layoutParams5;
                for (i = 0; i < childCount; ++i) {
                    child4 = this.getChildAt(i);
                    layoutParams5 = (LayoutParams)child4.getLayoutParams();
                    if (layoutParams5.expanded) {
                        child4.measure(View$MeasureSpec.makeMeasureSpec(layoutParams5.cellsUsed * n3 + layoutParams5.extraPixels, 1073741824), childMeasureSpec);
                    }
                }
            }
            i = size2;
            if (mode != 1073741824) {
                i = a;
            }
            this.setMeasuredDimension(n2, i);
        }
    }
    
    @Override
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams != null && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dismissPopupMenus() {
        if (this.mPresenter != null) {
            this.mPresenter.dismissPopupMenus();
        }
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return false;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        LayoutParams generateDefaultLayoutParams;
        if (viewGroup$LayoutParams != null) {
            LayoutParams layoutParams;
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            else {
                layoutParams = new LayoutParams(viewGroup$LayoutParams);
            }
            generateDefaultLayoutParams = layoutParams;
            if (layoutParams.gravity <= 0) {
                layoutParams.gravity = 16;
                generateDefaultLayoutParams = layoutParams;
            }
        }
        else {
            generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        }
        return generateDefaultLayoutParams;
    }
    
    public LayoutParams generateOverflowButtonLayoutParams() {
        final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }
    
    public Menu getMenu() {
        if (this.mMenu == null) {
            final Context context = this.getContext();
            (this.mMenu = new MenuBuilder(context)).setCallback((MenuBuilder.Callback)new MenuBuilderCallback());
            (this.mPresenter = new ActionMenuPresenter(context)).setReserveOverflow(true);
            final ActionMenuPresenter mPresenter = this.mPresenter;
            MenuPresenter.Callback mActionMenuPresenterCallback;
            if (this.mActionMenuPresenterCallback != null) {
                mActionMenuPresenterCallback = this.mActionMenuPresenterCallback;
            }
            else {
                mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
            }
            mPresenter.setCallback(mActionMenuPresenterCallback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return (Menu)this.mMenu;
    }
    
    @Nullable
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    @Override
    public int getWindowAnimations() {
        return 0;
    }
    
    protected boolean hasSupportDividerBeforeChildAt(final int n) {
        int n2;
        if (n == 0) {
            n2 = 0;
        }
        else {
            final View child = this.getChildAt(n - 1);
            final View child2 = this.getChildAt(n);
            int n4;
            final int n3 = n4 = 0;
            if (n < this.getChildCount()) {
                n4 = n3;
                if (child instanceof ActionMenuChildView) {
                    n4 = ((false | ((ActionMenuChildView)child).needsDividerAfter()) ? 1 : 0);
                }
            }
            n2 = n4;
            if (n > 0) {
                n2 = n4;
                if (child2 instanceof ActionMenuChildView) {
                    n2 = (n4 | (((ActionMenuChildView)child2).needsDividerBefore() ? 1 : 0));
                }
            }
        }
        return n2 != 0;
    }
    
    public boolean hideOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.hideOverflowMenu();
    }
    
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    public boolean isOverflowMenuShowPending() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowing();
    }
    
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (Build$VERSION.SDK_INT >= 8) {
            super.onConfigurationChanged(configuration);
        }
        if (this.mPresenter != null) {
            this.mPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }
    
    @Override
    protected void onLayout(final boolean b, int i, int n, int measuredHeight, int n2) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, n, measuredHeight, n2);
        }
        else {
            final int childCount = this.getChildCount();
            final int n3 = (n2 - n) / 2;
            final int dividerWidth = this.getDividerWidth();
            int n4 = 0;
            n2 = 0;
            n = measuredHeight - i - this.getPaddingRight() - this.getPaddingLeft();
            boolean b2 = false;
            final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
            for (int j = 0; j < childCount; ++j) {
                final View child = this.getChildAt(j);
                if (child.getVisibility() != 8) {
                    final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                    if (layoutParams.isOverflowButton) {
                        int measuredWidth;
                        final int n5 = measuredWidth = child.getMeasuredWidth();
                        if (this.hasSupportDividerBeforeChildAt(j)) {
                            measuredWidth = n5 + dividerWidth;
                        }
                        final int measuredHeight2 = child.getMeasuredHeight();
                        int n6;
                        int n7;
                        if (layoutRtl) {
                            n6 = this.getPaddingLeft() + layoutParams.leftMargin;
                            n7 = n6 + measuredWidth;
                        }
                        else {
                            n7 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                            n6 = n7 - measuredWidth;
                        }
                        final int n8 = n3 - measuredHeight2 / 2;
                        child.layout(n6, n8, n7, n8 + measuredHeight2);
                        n -= measuredWidth;
                        b2 = true;
                    }
                    else {
                        final int n9 = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                        final int n10 = n4 + n9;
                        final int n11 = n - n9;
                        n = n10;
                        if (this.hasSupportDividerBeforeChildAt(j)) {
                            n = n10 + dividerWidth;
                        }
                        ++n2;
                        n4 = n;
                        n = n11;
                    }
                }
            }
            if (childCount == 1 && !b2) {
                final View child2 = this.getChildAt(0);
                n = child2.getMeasuredWidth();
                n2 = child2.getMeasuredHeight();
                i = (measuredHeight - i) / 2 - n / 2;
                measuredHeight = n3 - n2 / 2;
                child2.layout(i, measuredHeight, i + n, measuredHeight + n2);
            }
            else {
                if (b2) {
                    i = 0;
                }
                else {
                    i = 1;
                }
                i = n2 - i;
                if (i > 0) {
                    i = n / i;
                }
                else {
                    i = 0;
                }
                n2 = Math.max(0, i);
                if (layoutRtl) {
                    measuredHeight = this.getWidth() - this.getPaddingRight();
                    View child3;
                    LayoutParams layoutParams2;
                    int measuredWidth2;
                    int n12;
                    for (i = 0; i < childCount; ++i, measuredHeight = n) {
                        child3 = this.getChildAt(i);
                        layoutParams2 = (LayoutParams)child3.getLayoutParams();
                        n = measuredHeight;
                        if (child3.getVisibility() != 8) {
                            if (layoutParams2.isOverflowButton) {
                                n = measuredHeight;
                            }
                            else {
                                n = measuredHeight - layoutParams2.rightMargin;
                                measuredWidth2 = child3.getMeasuredWidth();
                                measuredHeight = child3.getMeasuredHeight();
                                n12 = n3 - measuredHeight / 2;
                                child3.layout(n - measuredWidth2, n12, n, n12 + measuredHeight);
                                n -= layoutParams2.leftMargin + measuredWidth2 + n2;
                            }
                        }
                    }
                }
                else {
                    n = this.getPaddingLeft();
                    View child4;
                    LayoutParams layoutParams3;
                    int measuredHeight3;
                    int n13;
                    for (i = 0; i < childCount; ++i, n = measuredHeight) {
                        child4 = this.getChildAt(i);
                        layoutParams3 = (LayoutParams)child4.getLayoutParams();
                        measuredHeight = n;
                        if (child4.getVisibility() != 8) {
                            if (layoutParams3.isOverflowButton) {
                                measuredHeight = n;
                            }
                            else {
                                measuredHeight = n + layoutParams3.leftMargin;
                                n = child4.getMeasuredWidth();
                                measuredHeight3 = child4.getMeasuredHeight();
                                n13 = n3 - measuredHeight3 / 2;
                                child4.layout(measuredHeight, n13, measuredHeight + n, n13 + measuredHeight3);
                                measuredHeight += layoutParams3.rightMargin + n + n2;
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        final boolean mFormatItems = this.mFormatItems;
        this.mFormatItems = (View$MeasureSpec.getMode(n) == 1073741824);
        if (mFormatItems != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        final int size = View$MeasureSpec.getSize(n);
        if (this.mFormatItems && this.mMenu != null && size != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = size;
            this.mMenu.onItemsChanged(true);
        }
        final int childCount = this.getChildCount();
        if (this.mFormatItems && childCount > 0) {
            this.onMeasureExactFormat(n, n2);
        }
        else {
            for (int i = 0; i < childCount; ++i) {
                final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
                layoutParams.rightMargin = 0;
                layoutParams.leftMargin = 0;
            }
            super.onMeasure(n, n2);
        }
    }
    
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }
    
    public void setExpandedActionViewsExclusive(final boolean expandedActionViewsExclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(expandedActionViewsExclusive);
    }
    
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(@Nullable final Drawable overflowIcon) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(overflowIcon);
    }
    
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
    }
    
    public void setPopupTheme(@StyleRes final int mPopupTheme) {
        if (this.mPopupTheme != mPopupTheme) {
            if ((this.mPopupTheme = mPopupTheme) == 0) {
                this.mPopupContext = this.getContext();
            }
            else {
                this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
            }
        }
    }
    
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        (this.mPresenter = mPresenter).setMenuView(this);
    }
    
    public boolean showOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.showOverflowMenu();
    }
    
    public interface ActionMenuChildView
    {
        boolean needsDividerAfter();
        
        boolean needsDividerBefore();
    }
    
    private class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            return false;
        }
    }
    
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams
    {
        @ViewDebug$ExportedProperty
        public int cellsUsed;
        @ViewDebug$ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug$ExportedProperty
        public int extraPixels;
        @ViewDebug$ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug$ExportedProperty
        public boolean preventEdgeOffset;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }
        
        LayoutParams(final int n, final int n2, final boolean isOverflowButton) {
            super(n, n2);
            this.isOverflowButton = isOverflowButton;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
    }
    
    private class MenuBuilderCallback implements Callback
    {
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
