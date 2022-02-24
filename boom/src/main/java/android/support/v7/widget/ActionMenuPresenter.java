// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View$OnTouchListener;
import android.util.AttributeSet;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.transition.ActionBarTransition;
import android.support.v7.view.menu.SubMenuBuilder;
import android.os.Parcelable;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.view.ActionBarPolicy;
import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuView;
import android.support.v7.appcompat.R;
import android.content.Context;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.support.v4.view.ActionProvider;
import android.support.v7.view.menu.BaseMenuPresenter;

class ActionMenuPresenter extends BaseMenuPresenter implements SubUiVisibilityListener
{
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups;
    private ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    private OverflowMenuButton mOverflowButton;
    private OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback;
    private OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;
    
    public ActionMenuPresenter(final Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
        this.mActionButtonGroups = new SparseBooleanArray();
        this.mPopupPresenterCallback = new PopupPresenterCallback();
    }
    
    private View findViewForItem(final MenuItem menuItem) {
        final ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        View view;
        if (viewGroup == null) {
            view = null;
        }
        else {
            for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof MenuView.ItemView) {
                    view = child;
                    if (((MenuView.ItemView)child).getItemData() == menuItem) {
                        return view;
                    }
                }
            }
            view = null;
        }
        return view;
    }
    
    @Override
    public void bindItemView(final MenuItemImpl menuItemImpl, final MenuView.ItemView itemView) {
        itemView.initialize(menuItemImpl, 0);
        final ActionMenuView itemInvoker = (ActionMenuView)this.mMenuView;
        final ActionMenuItemView actionMenuItemView = (ActionMenuItemView)itemView;
        actionMenuItemView.setItemInvoker(itemInvoker);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionMenuItemView.setPopupCallback((ActionMenuItemView.PopupCallback)this.mPopupCallback);
    }
    
    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }
    
    public boolean filterLeftoverView(final ViewGroup viewGroup, final int n) {
        return viewGroup.getChildAt(n) != this.mOverflowButton && super.filterLeftoverView(viewGroup, n);
    }
    
    @Override
    public boolean flagActionItems() {
        final ArrayList<MenuItemImpl> visibleItems = this.mMenu.getVisibleItems();
        final int size = visibleItems.size();
        int mMaxItems = this.mMaxItems;
        final int mActionItemWidthLimit = this.mActionItemWidthLimit;
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
        final ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        int n = 0;
        int n2 = 0;
        final int n3 = 0;
        boolean b = false;
        int n4;
        for (int i = 0; i < size; ++i, mMaxItems = n4) {
            final MenuItemImpl menuItemImpl = visibleItems.get(i);
            if (menuItemImpl.requiresActionButton()) {
                ++n;
            }
            else if (menuItemImpl.requestsActionButton()) {
                ++n2;
            }
            else {
                b = true;
            }
            n4 = mMaxItems;
            if (this.mExpandedActionViewsExclusive) {
                n4 = mMaxItems;
                if (menuItemImpl.isActionViewExpanded()) {
                    n4 = 0;
                }
            }
        }
        int n5 = mMaxItems;
        if (this.mReserveOverflow && (b || n + n2 > (n5 = mMaxItems))) {
            n5 = mMaxItems - 1;
        }
        int n6 = n5 - n;
        final SparseBooleanArray mActionButtonGroups = this.mActionButtonGroups;
        mActionButtonGroups.clear();
        int n7 = 0;
        int n8 = 0;
        if (this.mStrictWidthLimit) {
            n8 = mActionItemWidthLimit / this.mMinCellSize;
            n7 = this.mMinCellSize + mActionItemWidthLimit % this.mMinCellSize / n8;
        }
        final int n9 = 0;
        int n10 = mActionItemWidthLimit;
        int j = n9;
        int n11 = n3;
        while (j < size) {
            final MenuItemImpl menuItemImpl2 = visibleItems.get(j);
            int n12;
            if (menuItemImpl2.requiresActionButton()) {
                final View itemView = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                if (this.mScrapActionButtonView == null) {
                    this.mScrapActionButtonView = itemView;
                }
                if (this.mStrictWidthLimit) {
                    n8 -= ActionMenuView.measureChildForCells(itemView, n7, n8, measureSpec, 0);
                }
                else {
                    itemView.measure(measureSpec, measureSpec);
                }
                final int measuredWidth = itemView.getMeasuredWidth();
                n12 = n10 - measuredWidth;
                int n13 = n11;
                if (n11 == 0) {
                    n13 = measuredWidth;
                }
                final int groupId = menuItemImpl2.getGroupId();
                if (groupId != 0) {
                    mActionButtonGroups.put(groupId, true);
                }
                menuItemImpl2.setIsActionButton(true);
                n11 = n13;
            }
            else if (menuItemImpl2.requestsActionButton()) {
                final int groupId2 = menuItemImpl2.getGroupId();
                final boolean value = mActionButtonGroups.get(groupId2);
                boolean b2 = (n6 > 0 || value) && n10 > 0 && (!this.mStrictWidthLimit || n8 > 0);
                int n14 = n8;
                int n15 = n11;
                boolean isActionButton = b2;
                n12 = n10;
                if (b2) {
                    final View itemView2 = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                    if (this.mScrapActionButtonView == null) {
                        this.mScrapActionButtonView = itemView2;
                    }
                    if (this.mStrictWidthLimit) {
                        final int measureChildForCells = ActionMenuView.measureChildForCells(itemView2, n7, n8, measureSpec, 0);
                        final int n16 = n8 -= measureChildForCells;
                        if (measureChildForCells == 0) {
                            b2 = false;
                            n8 = n16;
                        }
                    }
                    else {
                        itemView2.measure(measureSpec, measureSpec);
                    }
                    final int measuredWidth2 = itemView2.getMeasuredWidth();
                    n12 = n10 - measuredWidth2;
                    if ((n15 = n11) == 0) {
                        n15 = measuredWidth2;
                    }
                    if (this.mStrictWidthLimit) {
                        isActionButton = (b2 & n12 >= 0);
                        n14 = n8;
                    }
                    else {
                        isActionButton = (b2 & n12 + n15 > 0);
                        n14 = n8;
                    }
                }
                int n17;
                if (isActionButton && groupId2 != 0) {
                    mActionButtonGroups.put(groupId2, true);
                    n17 = n6;
                }
                else {
                    n17 = n6;
                    if (value) {
                        mActionButtonGroups.put(groupId2, false);
                        int index = 0;
                        while (true) {
                            n17 = n6;
                            if (index >= j) {
                                break;
                            }
                            final MenuItemImpl menuItemImpl3 = visibleItems.get(index);
                            int n18 = n6;
                            if (menuItemImpl3.getGroupId() == groupId2) {
                                n18 = n6;
                                if (menuItemImpl3.isActionButton()) {
                                    n18 = n6 + 1;
                                }
                                menuItemImpl3.setIsActionButton(false);
                            }
                            ++index;
                            n6 = n18;
                        }
                    }
                }
                n6 = n17;
                if (isActionButton) {
                    n6 = n17 - 1;
                }
                menuItemImpl2.setIsActionButton(isActionButton);
                n8 = n14;
                n11 = n15;
            }
            else {
                menuItemImpl2.setIsActionButton(false);
                n12 = n10;
            }
            ++j;
            n10 = n12;
        }
        return true;
    }
    
    @Override
    public View getItemView(final MenuItemImpl menuItemImpl, final View view, final ViewGroup viewGroup) {
        View view2 = menuItemImpl.getActionView();
        if (view2 == null || menuItemImpl.hasCollapsibleActionView()) {
            view2 = super.getItemView(menuItemImpl, view, viewGroup);
        }
        int visibility;
        if (menuItemImpl.isActionViewExpanded()) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        view2.setVisibility(visibility);
        final ActionMenuView actionMenuView = (ActionMenuView)viewGroup;
        final ViewGroup$LayoutParams layoutParams = view2.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            view2.setLayoutParams((ViewGroup$LayoutParams)actionMenuView.generateLayoutParams(layoutParams));
        }
        return view2;
    }
    
    @Override
    public MenuView getMenuView(final ViewGroup viewGroup) {
        final MenuView menuView = super.getMenuView(viewGroup);
        ((ActionMenuView)menuView).setPresenter(this);
        return menuView;
    }
    
    public Drawable getOverflowIcon() {
        Drawable drawable;
        if (this.mOverflowButton != null) {
            drawable = this.mOverflowButton.getDrawable();
        }
        else if (this.mPendingOverflowIconSet) {
            drawable = this.mPendingOverflowIcon;
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    public boolean hideOverflowMenu() {
        boolean b;
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            ((View)this.mMenuView).removeCallbacks((Runnable)this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            b = true;
        }
        else {
            final OverflowPopup mOverflowPopup = this.mOverflowPopup;
            if (mOverflowPopup != null) {
                mOverflowPopup.dismiss();
                b = true;
            }
            else {
                b = false;
            }
        }
        return b;
    }
    
    public boolean hideSubMenus() {
        boolean b;
        if (this.mActionButtonPopup != null) {
            this.mActionButtonPopup.dismiss();
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    @Override
    public void initForMenu(final Context context, final MenuBuilder menuBuilder) {
        super.initForMenu(context, menuBuilder);
        final Resources resources = context.getResources();
        final ActionBarPolicy value = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = value.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = value.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = value.getMaxActionButtons();
        }
        int mWidthLimit = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(measureSpec, measureSpec);
            }
            mWidthLimit -= this.mOverflowButton.getMeasuredWidth();
        }
        else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = mWidthLimit;
        this.mMinCellSize = (int)(56.0f * resources.getDisplayMetrics().density);
        this.mScrapActionButtonView = null;
    }
    
    public boolean isOverflowMenuShowPending() {
        return this.mPostedOpenRunnable != null || this.isOverflowMenuShowing();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mOverflowPopup != null && this.mOverflowPopup.isShowing();
    }
    
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, b);
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = this.mContext.getResources().getInteger(R.integer.abc_max_action_buttons);
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        if (savedState.openSubMenuId > 0) {
            final MenuItem item = this.mMenu.findItem(savedState.openSubMenuId);
            if (item != null) {
                this.onSubMenuSelected((SubMenuBuilder)item.getSubMenu());
            }
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return (Parcelable)savedState;
    }
    
    @Override
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        boolean b = false;
        if (subMenuBuilder.hasVisibleItems()) {
            SubMenuBuilder subMenuBuilder2;
            for (subMenuBuilder2 = subMenuBuilder; subMenuBuilder2.getParentMenu() != this.mMenu; subMenuBuilder2 = (SubMenuBuilder)subMenuBuilder2.getParentMenu()) {}
            Object anchorView;
            if ((anchorView = this.findViewForItem(subMenuBuilder2.getItem())) == null) {
                if (this.mOverflowButton == null) {
                    return b;
                }
                anchorView = this.mOverflowButton;
            }
            this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
            (this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenuBuilder)).setAnchorView((View)anchorView);
            this.mActionButtonPopup.show();
            super.onSubMenuSelected(subMenuBuilder);
            b = true;
        }
        return b;
    }
    
    @Override
    public void onSubUiVisibilityChanged(final boolean b) {
        if (b) {
            super.onSubMenuSelected(null);
        }
        else {
            this.mMenu.close(false);
        }
    }
    
    public void setExpandedActionViewsExclusive(final boolean mExpandedActionViewsExclusive) {
        this.mExpandedActionViewsExclusive = mExpandedActionViewsExclusive;
    }
    
    public void setItemLimit(final int mMaxItems) {
        this.mMaxItems = mMaxItems;
        this.mMaxItemsSet = true;
    }
    
    public void setMenuView(final ActionMenuView mMenuView) {
        ((ActionMenuView)(this.mMenuView = mMenuView)).initialize(this.mMenu);
    }
    
    public void setOverflowIcon(final Drawable drawable) {
        if (this.mOverflowButton != null) {
            this.mOverflowButton.setImageDrawable(drawable);
        }
        else {
            this.mPendingOverflowIconSet = true;
            this.mPendingOverflowIcon = drawable;
        }
    }
    
    public void setReserveOverflow(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
        this.mReserveOverflowSet = true;
    }
    
    public void setWidthLimit(final int mWidthLimit, final boolean mStrictWidthLimit) {
        this.mWidthLimit = mWidthLimit;
        this.mStrictWidthLimit = mStrictWidthLimit;
        this.mWidthLimitSet = true;
    }
    
    @Override
    public boolean shouldIncludeItem(final int n, final MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }
    
    public boolean showOverflowMenu() {
        boolean b = true;
        if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
            this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
            ((View)this.mMenuView).post((Runnable)this.mPostedOpenRunnable);
            super.onSubMenuSelected(null);
        }
        else {
            b = false;
        }
        return b;
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        final ViewGroup viewGroup = (ViewGroup)((View)this.mMenuView).getParent();
        if (viewGroup != null) {
            ActionBarTransition.beginDelayedTransition(viewGroup);
        }
        super.updateMenuView(b);
        ((View)this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            final ArrayList<MenuItemImpl> actionItems = this.mMenu.getActionItems();
            for (int size = actionItems.size(), i = 0; i < size; ++i) {
                final ActionProvider supportActionProvider = actionItems.get(i).getSupportActionProvider();
                if (supportActionProvider != null) {
                    supportActionProvider.setSubUiVisibilityListener((ActionProvider.SubUiVisibilityListener)this);
                }
            }
        }
        ArrayList<MenuItemImpl> nonActionItems;
        if (this.mMenu != null) {
            nonActionItems = this.mMenu.getNonActionItems();
        }
        else {
            nonActionItems = null;
        }
        int n = 0;
        if (this.mReserveOverflow) {
            n = n;
            if (nonActionItems != null) {
                final int size2 = nonActionItems.size();
                if (size2 == 1) {
                    if (!nonActionItems.get(0).isActionViewExpanded()) {
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                }
                else if (size2 > 0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
            }
        }
        if (n != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            final ViewGroup viewGroup2 = (ViewGroup)this.mOverflowButton.getParent();
            if (viewGroup2 != this.mMenuView) {
                if (viewGroup2 != null) {
                    viewGroup2.removeView((View)this.mOverflowButton);
                }
                final ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
                actionMenuView.addView((View)this.mOverflowButton, (ViewGroup$LayoutParams)actionMenuView.generateOverflowButtonLayoutParams());
            }
        }
        else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
        }
        ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }
    
    private class ActionButtonSubmenu extends MenuPopupHelper
    {
        private SubMenuBuilder mSubMenu;
        
        public ActionButtonSubmenu(final Context context, final SubMenuBuilder mSubMenu) {
            super(context, mSubMenu, null, false, R.attr.actionOverflowMenuStyle);
            this.mSubMenu = mSubMenu;
            if (!((MenuItemImpl)mSubMenu.getItem()).isActionButton()) {
                Object access$600;
                if (ActionMenuPresenter.this.mOverflowButton == null) {
                    access$600 = ActionMenuPresenter.this.mMenuView;
                }
                else {
                    access$600 = ActionMenuPresenter.this.mOverflowButton;
                }
                this.setAnchorView((View)access$600);
            }
            this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
            final boolean b = false;
            final int size = mSubMenu.size();
            int n = 0;
            boolean forceShowIcon;
            while (true) {
                forceShowIcon = b;
                if (n >= size) {
                    break;
                }
                final MenuItem item = mSubMenu.getItem(n);
                if (item.isVisible() && item.getIcon() != null) {
                    forceShowIcon = true;
                    break;
                }
                ++n;
            }
            this.setForceShowIcon(forceShowIcon);
        }
        
        @Override
        public void onDismiss() {
            super.onDismiss();
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
        }
    }
    
    private class ActionMenuPopupCallback extends PopupCallback
    {
        @Override
        public ListPopupWindow getPopup() {
            ListPopupWindow popup;
            if (ActionMenuPresenter.this.mActionButtonPopup != null) {
                popup = ActionMenuPresenter.this.mActionButtonPopup.getPopup();
            }
            else {
                popup = null;
            }
            return popup;
        }
    }
    
    private class OpenOverflowRunnable implements Runnable
    {
        private OverflowPopup mPopup;
        
        public OpenOverflowRunnable(final OverflowPopup mPopup) {
            this.mPopup = mPopup;
        }
        
        @Override
        public void run() {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
            final View view = (View)ActionMenuPresenter.this.mMenuView;
            if (view != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }
    
    private class OverflowMenuButton extends AppCompatImageView implements ActionMenuChildView
    {
        private final float[] mTempPts;
        
        public OverflowMenuButton(final Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            this.mTempPts = new float[2];
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
            this.setOnTouchListener((View$OnTouchListener)new ListPopupWindow.ForwardingListener(this) {
                @Override
                public ListPopupWindow getPopup() {
                    ListPopupWindow popup;
                    if (ActionMenuPresenter.this.mOverflowPopup == null) {
                        popup = null;
                    }
                    else {
                        popup = ActionMenuPresenter.this.mOverflowPopup.getPopup();
                    }
                    return popup;
                }
                
                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }
                
                public boolean onForwardingStopped() {
                    boolean b;
                    if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                        b = false;
                    }
                    else {
                        ActionMenuPresenter.this.hideOverflowMenu();
                        b = true;
                    }
                    return b;
                }
            });
        }
        
        @Override
        public boolean needsDividerAfter() {
            return false;
        }
        
        @Override
        public boolean needsDividerBefore() {
            return false;
        }
        
        public boolean performClick() {
            if (!super.performClick()) {
                this.playSoundEffect(0);
                ActionMenuPresenter.this.showOverflowMenu();
            }
            return true;
        }
        
        protected boolean setFrame(int n, int paddingTop, int height, int paddingBottom) {
            final boolean setFrame = super.setFrame(n, paddingTop, height, paddingBottom);
            final Drawable drawable = this.getDrawable();
            final Drawable background = this.getBackground();
            if (drawable != null && background != null) {
                final int width = this.getWidth();
                height = this.getHeight();
                n = Math.max(width, height) / 2;
                final int paddingLeft = this.getPaddingLeft();
                final int paddingRight = this.getPaddingRight();
                paddingTop = this.getPaddingTop();
                paddingBottom = this.getPaddingBottom();
                final int n2 = (width + (paddingLeft - paddingRight)) / 2;
                paddingTop = (height + (paddingTop - paddingBottom)) / 2;
                DrawableCompat.setHotspotBounds(background, n2 - n, paddingTop - n, n2 + n, paddingTop + n);
            }
            return setFrame;
        }
    }
    
    private class OverflowPopup extends MenuPopupHelper
    {
        public OverflowPopup(final Context context, final MenuBuilder menuBuilder, final View view, final boolean b) {
            super(context, menuBuilder, view, b, R.attr.actionOverflowMenuStyle);
            this.setGravity(8388613);
            this.setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }
        
        @Override
        public void onDismiss() {
            super.onDismiss();
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
        }
    }
    
    private class PopupPresenterCallback implements Callback
    {
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            if (menuBuilder instanceof SubMenuBuilder) {
                ((SubMenuBuilder)menuBuilder).getRootMenu().close(false);
            }
            final Callback callback = ActionMenuPresenter.this.getCallback();
            if (callback != null) {
                callback.onCloseMenu(menuBuilder, b);
            }
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            boolean b = false;
            if (menuBuilder != null) {
                ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
                final Callback callback = ActionMenuPresenter.this.getCallback();
                b = (callback != null && callback.onOpenSubMenu(menuBuilder));
            }
            return b;
        }
    }
    
    private static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public int openSubMenuId;
        
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
        
        SavedState() {
        }
        
        SavedState(final Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.openSubMenuId);
        }
    }
}
