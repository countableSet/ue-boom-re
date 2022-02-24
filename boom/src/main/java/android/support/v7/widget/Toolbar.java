// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.CollapsibleActionView;
import android.support.annotation.ColorInt;
import android.text.TextUtils$TruncateAt;
import android.view.ContextThemeWrapper;
import android.support.annotation.StyleRes;
import android.support.annotation.StringRes;
import android.support.annotation.DrawableRes;
import android.os.Build$VERSION;
import android.support.v4.view.MenuItemCompat;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.text.Layout;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.support.v7.app.ActionBar;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.View$MeasureSpec;
import android.support.v7.view.SupportMenuInflater;
import android.view.MenuInflater;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import java.util.List;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v7.appcompat.R;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.widget.ImageView;
import java.util.ArrayList;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.support.v7.view.menu.MenuPresenter;
import android.view.ViewGroup;

public class Toolbar extends ViewGroup
{
    private static final String TAG = "Toolbar";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private int mButtonGravity;
    private ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private final RtlSpacingHelper mContentInsets;
    private final AppCompatDrawableManager mDrawableManager;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;
    
    public Toolbar(final Context context) {
        this(context, null);
    }
    
    public Toolbar(final Context context, @Nullable final AttributeSet set) {
        this(context, set, R.attr.toolbarStyle);
    }
    
    public Toolbar(final Context context, @Nullable final AttributeSet set, int n) {
        super(context, set, n);
        this.mContentInsets = new RtlSpacingHelper();
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<View>();
        this.mHiddenViews = new ArrayList<View>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                return Toolbar.this.mOnMenuItemClickListener != null && Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable() {
            @Override
            public void run() {
                Toolbar.this.showOverflowMenu();
            }
        };
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, R.styleable.Toolbar, n, 0);
        this.mTitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = 48;
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, 0);
        this.mTitleMarginBottom = n;
        this.mTitleMarginTop = n;
        this.mTitleMarginEnd = n;
        this.mTitleMarginStart = n;
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
        if (n >= 0) {
            this.mTitleMarginStart = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
        if (n >= 0) {
            this.mTitleMarginEnd = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
        if (n >= 0) {
            this.mTitleMarginTop = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
        if (n >= 0) {
            this.mTitleMarginBottom = n;
        }
        this.mMaxButtonHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
        final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        this.mContentInsets.setAbsolute(obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0), obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0));
        if (dimensionPixelOffset != Integer.MIN_VALUE || n != Integer.MIN_VALUE) {
            this.mContentInsets.setRelative(dimensionPixelOffset, n);
        }
        this.mCollapseIcon = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = obtainStyledAttributes.getText(R.styleable.Toolbar_collapseContentDescription);
        final CharSequence text = obtainStyledAttributes.getText(R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            this.setTitle(text);
        }
        final CharSequence text2 = obtainStyledAttributes.getText(R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text2)) {
            this.setSubtitle(text2);
        }
        this.mPopupContext = this.getContext();
        this.setPopupTheme(obtainStyledAttributes.getResourceId(R.styleable.Toolbar_popupTheme, 0));
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            this.setNavigationIcon(drawable);
        }
        final CharSequence text3 = obtainStyledAttributes.getText(R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text3)) {
            this.setNavigationContentDescription(text3);
        }
        final Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_logo);
        if (drawable2 != null) {
            this.setLogo(drawable2);
        }
        final CharSequence text4 = obtainStyledAttributes.getText(R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text4)) {
            this.setLogoDescription(text4);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleTextColor)) {
            this.setTitleTextColor(obtainStyledAttributes.getColor(R.styleable.Toolbar_titleTextColor, -1));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            this.setSubtitleTextColor(obtainStyledAttributes.getColor(R.styleable.Toolbar_subtitleTextColor, -1));
        }
        obtainStyledAttributes.recycle();
        this.mDrawableManager = AppCompatDrawableManager.get();
    }
    
    private void addCustomViewsWithGravity(final List<View> list, int i) {
        int n = 1;
        if (ViewCompat.getLayoutDirection((View)this) != 1) {
            n = 0;
        }
        final int childCount = this.getChildCount();
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection((View)this));
        list.clear();
        if (n != 0) {
            View child;
            LayoutParams layoutParams;
            for (i = childCount - 1; i >= 0; --i) {
                child = this.getChildAt(i);
                layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.mViewType == 0 && this.shouldLayout(child) && this.getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                    list.add(child);
                }
            }
        }
        else {
            View child2;
            LayoutParams layoutParams2;
            for (i = 0; i < childCount; ++i) {
                child2 = this.getChildAt(i);
                layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (layoutParams2.mViewType == 0 && this.shouldLayout(child2) && this.getChildHorizontalGravity(layoutParams2.gravity) == absoluteGravity) {
                    list.add(child2);
                }
            }
        }
    }
    
    private void addSystemView(final View e, final boolean b) {
        final ViewGroup$LayoutParams layoutParams = e.getLayoutParams();
        LayoutParams layoutParams2;
        if (layoutParams == null) {
            layoutParams2 = this.generateDefaultLayoutParams();
        }
        else if (!this.checkLayoutParams(layoutParams)) {
            layoutParams2 = this.generateLayoutParams(layoutParams);
        }
        else {
            layoutParams2 = (LayoutParams)layoutParams;
        }
        layoutParams2.mViewType = 1;
        if (b && this.mExpandedActionView != null) {
            e.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            this.mHiddenViews.add(e);
        }
        else {
            this.addView(e, (ViewGroup$LayoutParams)layoutParams2);
        }
    }
    
    private void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            (this.mCollapseButtonView = new ImageButton(this.getContext(), (AttributeSet)null, R.attr.toolbarNavigationButtonStyle)).setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800003 | (this.mButtonGravity & 0x70));
            generateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    Toolbar.this.collapseActionView();
                }
            });
        }
    }
    
    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new ImageView(this.getContext());
        }
    }
    
    private void ensureMenu() {
        this.ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            final MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }
    
    private void ensureMenuView() {
        if (this.mMenuView == null) {
            (this.mMenuView = new ActionMenuView(this.getContext())).setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800005 | (this.mButtonGravity & 0x70));
            this.mMenuView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
            this.addSystemView((View)this.mMenuView, false);
        }
    }
    
    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new ImageButton(this.getContext(), (AttributeSet)null, R.attr.toolbarNavigationButtonStyle);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800003 | (this.mButtonGravity & 0x70));
            this.mNavButtonView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
        }
    }
    
    private int getChildHorizontalGravity(int n) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        switch (n = (GravityCompat.getAbsoluteGravity(n, layoutDirection) & 0x7)) {
            default: {
                if (layoutDirection == 1) {
                    n = 5;
                    return n;
                }
                n = 3;
                return n;
            }
            case 1:
            case 3:
            case 5: {
                return n;
            }
        }
    }
    
    private int getChildTop(final View view, int n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measuredHeight = view.getMeasuredHeight();
        if (n > 0) {
            n = (measuredHeight - n) / 2;
        }
        else {
            n = 0;
        }
        switch (this.getChildVerticalGravity(layoutParams.gravity)) {
            default: {
                final int paddingTop = this.getPaddingTop();
                final int paddingBottom = this.getPaddingBottom();
                n = this.getHeight();
                final int n2 = (n - paddingTop - paddingBottom - measuredHeight) / 2;
                if (n2 < layoutParams.topMargin) {
                    n = layoutParams.topMargin;
                }
                else {
                    final int n3 = n - paddingBottom - measuredHeight - n2 - paddingTop;
                    n = n2;
                    if (n3 < layoutParams.bottomMargin) {
                        n = Math.max(0, n2 - (layoutParams.bottomMargin - n3));
                    }
                }
                n += paddingTop;
                break;
            }
            case 48: {
                n = this.getPaddingTop() - n;
                break;
            }
            case 80: {
                n = this.getHeight() - this.getPaddingBottom() - measuredHeight - layoutParams.bottomMargin - n;
                break;
            }
        }
        return n;
    }
    
    private int getChildVerticalGravity(int n) {
        switch (n &= 0x70) {
            default: {
                n = (this.mGravity & 0x70);
                return n;
            }
            case 16:
            case 48:
            case 80: {
                return n;
            }
        }
    }
    
    private int getHorizontalMargins(final View view) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(viewGroup$MarginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(viewGroup$MarginLayoutParams);
    }
    
    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.getContext());
    }
    
    private int getVerticalMargins(final View view) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        return viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin;
    }
    
    private int getViewListMeasuredWidth(final List<View> list, final int[] array) {
        int max = array[0];
        int max2 = array[1];
        int n = 0;
        for (int size = list.size(), i = 0; i < size; ++i) {
            final View view = list.get(i);
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final int b = layoutParams.leftMargin - max;
            final int b2 = layoutParams.rightMargin - max2;
            final int max3 = Math.max(0, b);
            final int max4 = Math.max(0, b2);
            max = Math.max(0, -b);
            max2 = Math.max(0, -b2);
            n += view.getMeasuredWidth() + max3 + max4;
        }
        return n;
    }
    
    private boolean isChildOrHidden(final View o) {
        return o.getParent() == this || this.mHiddenViews.contains(o);
    }
    
    private static boolean isCustomView(final View view) {
        return ((LayoutParams)view.getLayoutParams()).mViewType == 0;
    }
    
    private int layoutChildLeft(final View view, int n, final int[] array, int childTop) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int b = layoutParams.leftMargin - array[0];
        n += Math.max(0, b);
        array[0] = Math.max(0, -b);
        childTop = this.getChildTop(view, childTop);
        final int measuredWidth = view.getMeasuredWidth();
        view.layout(n, childTop, n + measuredWidth, view.getMeasuredHeight() + childTop);
        return n + (layoutParams.rightMargin + measuredWidth);
    }
    
    private int layoutChildRight(final View view, int n, final int[] array, int childTop) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int b = layoutParams.rightMargin - array[1];
        n -= Math.max(0, b);
        array[1] = Math.max(0, -b);
        childTop = this.getChildTop(view, childTop);
        final int measuredWidth = view.getMeasuredWidth();
        view.layout(n - measuredWidth, childTop, n, view.getMeasuredHeight() + childTop);
        return n - (layoutParams.leftMargin + measuredWidth);
    }
    
    private int measureChildCollapseMargins(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        final int b = viewGroup$MarginLayoutParams.leftMargin - array[0];
        final int b2 = viewGroup$MarginLayoutParams.rightMargin - array[1];
        final int n5 = Math.max(0, b) + Math.max(0, b2);
        array[0] = Math.max(0, -b);
        array[1] = Math.max(0, -b2);
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + n5 + n2, viewGroup$MarginLayoutParams.width), getChildMeasureSpec(n3, this.getPaddingTop() + this.getPaddingBottom() + viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin + n4, viewGroup$MarginLayoutParams.height));
        return view.getMeasuredWidth() + n5;
    }
    
    private void measureChildConstrained(final View view, int measureSpec, int childMeasureSpec, int mode, final int n, int min) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        final int childMeasureSpec2 = getChildMeasureSpec(measureSpec, this.getPaddingLeft() + this.getPaddingRight() + viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin + childMeasureSpec, viewGroup$MarginLayoutParams.width);
        childMeasureSpec = getChildMeasureSpec(mode, this.getPaddingTop() + this.getPaddingBottom() + viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin + n, viewGroup$MarginLayoutParams.height);
        mode = View$MeasureSpec.getMode(childMeasureSpec);
        measureSpec = childMeasureSpec;
        if (mode != 1073741824) {
            measureSpec = childMeasureSpec;
            if (min >= 0) {
                if (mode != 0) {
                    min = Math.min(View$MeasureSpec.getSize(childMeasureSpec), min);
                }
                measureSpec = View$MeasureSpec.makeMeasureSpec(min, 1073741824);
            }
        }
        view.measure(childMeasureSpec2, measureSpec);
    }
    
    private void postShowOverflowMenu() {
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
        this.post(this.mShowOverflowMenuRunnable);
    }
    
    private boolean shouldCollapse() {
        final boolean b = false;
        boolean b2;
        if (!this.mCollapsible) {
            b2 = b;
        }
        else {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                if (this.shouldLayout(child) && child.getMeasuredWidth() > 0) {
                    b2 = b;
                    if (child.getMeasuredHeight() > 0) {
                        return b2;
                    }
                }
            }
            b2 = true;
        }
        return b2;
    }
    
    private boolean shouldLayout(final View view) {
        return view != null && view.getParent() == this && view.getVisibility() != 8;
    }
    
    void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.addView((View)this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }
    
    public boolean canShowOverflowMenu() {
        return this.getVisibility() == 0 && this.mMenuView != null && this.mMenuView.isOverflowReserved();
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return super.checkLayoutParams(viewGroup$LayoutParams) && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void collapseActionView() {
        MenuItemImpl mCurrentExpandedItem;
        if (this.mExpandedMenuPresenter == null) {
            mCurrentExpandedItem = null;
        }
        else {
            mCurrentExpandedItem = this.mExpandedMenuPresenter.mCurrentExpandedItem;
        }
        if (mCurrentExpandedItem != null) {
            mCurrentExpandedItem.collapseActionView();
        }
    }
    
    public void dismissPopupMenus() {
        if (this.mMenuView != null) {
            this.mMenuView.dismissPopupMenus();
        }
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        LayoutParams layoutParams;
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
        }
        else if (viewGroup$LayoutParams instanceof ActionBar.LayoutParams) {
            layoutParams = new LayoutParams((ActionBar.LayoutParams)viewGroup$LayoutParams);
        }
        else if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            layoutParams = new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        else {
            layoutParams = new LayoutParams(viewGroup$LayoutParams);
        }
        return layoutParams;
    }
    
    public int getContentInsetEnd() {
        return this.mContentInsets.getEnd();
    }
    
    public int getContentInsetLeft() {
        return this.mContentInsets.getLeft();
    }
    
    public int getContentInsetRight() {
        return this.mContentInsets.getRight();
    }
    
    public int getContentInsetStart() {
        return this.mContentInsets.getStart();
    }
    
    public Drawable getLogo() {
        Drawable drawable;
        if (this.mLogoView != null) {
            drawable = this.mLogoView.getDrawable();
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    public CharSequence getLogoDescription() {
        CharSequence contentDescription;
        if (this.mLogoView != null) {
            contentDescription = this.mLogoView.getContentDescription();
        }
        else {
            contentDescription = null;
        }
        return contentDescription;
    }
    
    public Menu getMenu() {
        this.ensureMenu();
        return this.mMenuView.getMenu();
    }
    
    @Nullable
    public CharSequence getNavigationContentDescription() {
        CharSequence contentDescription;
        if (this.mNavButtonView != null) {
            contentDescription = this.mNavButtonView.getContentDescription();
        }
        else {
            contentDescription = null;
        }
        return contentDescription;
    }
    
    @Nullable
    public Drawable getNavigationIcon() {
        Drawable drawable;
        if (this.mNavButtonView != null) {
            drawable = this.mNavButtonView.getDrawable();
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    @Nullable
    public Drawable getOverflowIcon() {
        this.ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }
    
    public CharSequence getTitle() {
        return this.mTitleText;
    }
    
    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }
    
    public boolean hasExpandedActionView() {
        return this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null;
    }
    
    public boolean hideOverflowMenu() {
        return this.mMenuView != null && this.mMenuView.hideOverflowMenu();
    }
    
    public void inflateMenu(@MenuRes final int n) {
        this.getMenuInflater().inflate(n, this.getMenu());
    }
    
    public boolean isOverflowMenuShowPending() {
        return this.mMenuView != null && this.mMenuView.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mMenuView != null && this.mMenuView.isOverflowMenuShowing();
    }
    
    public boolean isTitleTruncated() {
        final boolean b = false;
        boolean b2;
        if (this.mTitleTextView == null) {
            b2 = b;
        }
        else {
            final Layout layout = this.mTitleTextView.getLayout();
            b2 = b;
            if (layout != null) {
                final int lineCount = layout.getLineCount();
                int n = 0;
                while (true) {
                    b2 = b;
                    if (n >= lineCount) {
                        return b2;
                    }
                    if (layout.getEllipsisCount(n) > 0) {
                        break;
                    }
                    ++n;
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            final boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }
    
    protected void onLayout(final boolean b, int i, int j, int k, int a) {
        boolean b2;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        i = paddingLeft;
        j = width - paddingRight;
        final int[] mTempMargins = this.mTempMargins;
        mTempMargins[mTempMargins[1] = 0] = 0;
        final int minimumHeight = ViewCompat.getMinimumHeight((View)this);
        k = i;
        a = j;
        if (this.shouldLayout((View)this.mNavButtonView)) {
            if (b2) {
                a = this.layoutChildRight((View)this.mNavButtonView, j, mTempMargins, minimumHeight);
                k = i;
            }
            else {
                k = this.layoutChildLeft((View)this.mNavButtonView, i, mTempMargins, minimumHeight);
                a = j;
            }
        }
        j = k;
        i = a;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            if (b2) {
                i = this.layoutChildRight((View)this.mCollapseButtonView, a, mTempMargins, minimumHeight);
                j = k;
            }
            else {
                j = this.layoutChildLeft((View)this.mCollapseButtonView, k, mTempMargins, minimumHeight);
                i = a;
            }
        }
        a = j;
        k = i;
        if (this.shouldLayout((View)this.mMenuView)) {
            if (b2) {
                a = this.layoutChildLeft((View)this.mMenuView, j, mTempMargins, minimumHeight);
                k = i;
            }
            else {
                k = this.layoutChildRight((View)this.mMenuView, i, mTempMargins, minimumHeight);
                a = j;
            }
        }
        mTempMargins[0] = Math.max(0, this.getContentInsetLeft() - a);
        mTempMargins[1] = Math.max(0, this.getContentInsetRight() - (width - paddingRight - k));
        a = Math.max(a, this.getContentInsetLeft());
        k = Math.min(k, width - paddingRight - this.getContentInsetRight());
        i = a;
        j = k;
        if (this.shouldLayout(this.mExpandedActionView)) {
            if (b2) {
                j = this.layoutChildRight(this.mExpandedActionView, k, mTempMargins, minimumHeight);
                i = a;
            }
            else {
                i = this.layoutChildLeft(this.mExpandedActionView, a, mTempMargins, minimumHeight);
                j = k;
            }
        }
        k = i;
        a = j;
        if (this.shouldLayout((View)this.mLogoView)) {
            if (b2) {
                a = this.layoutChildRight((View)this.mLogoView, j, mTempMargins, minimumHeight);
                k = i;
            }
            else {
                k = this.layoutChildLeft((View)this.mLogoView, i, mTempMargins, minimumHeight);
                a = j;
            }
        }
        final boolean shouldLayout = this.shouldLayout((View)this.mTitleTextView);
        final boolean shouldLayout2 = this.shouldLayout((View)this.mSubtitleTextView);
        i = 0;
        if (shouldLayout) {
            final LayoutParams layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
            i = 0 + (layoutParams.topMargin + this.mTitleTextView.getMeasuredHeight() + layoutParams.bottomMargin);
        }
        int n = i;
        if (shouldLayout2) {
            final LayoutParams layoutParams2 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
            n = i + (layoutParams2.topMargin + this.mSubtitleTextView.getMeasuredHeight() + layoutParams2.bottomMargin);
        }
        Label_0837: {
            if (!shouldLayout) {
                j = k;
                i = a;
                if (!shouldLayout2) {
                    break Label_0837;
                }
            }
            TextView textView;
            if (shouldLayout) {
                textView = this.mTitleTextView;
            }
            else {
                textView = this.mSubtitleTextView;
            }
            TextView textView2;
            if (shouldLayout2) {
                textView2 = this.mSubtitleTextView;
            }
            else {
                textView2 = this.mTitleTextView;
            }
            final LayoutParams layoutParams3 = (LayoutParams)((View)textView).getLayoutParams();
            final LayoutParams layoutParams4 = (LayoutParams)((View)textView2).getLayoutParams();
            boolean b3;
            if ((shouldLayout && this.mTitleTextView.getMeasuredWidth() > 0) || (shouldLayout2 && this.mSubtitleTextView.getMeasuredWidth() > 0)) {
                b3 = true;
            }
            else {
                b3 = false;
            }
            switch (this.mGravity & 0x70) {
                default: {
                    j = (height - paddingTop - paddingBottom - n) / 2;
                    if (j < layoutParams3.topMargin + this.mTitleMarginTop) {
                        i = layoutParams3.topMargin + this.mTitleMarginTop;
                    }
                    else {
                        final int n2 = height - paddingBottom - n - j - paddingTop;
                        i = j;
                        if (n2 < layoutParams3.bottomMargin + this.mTitleMarginBottom) {
                            i = Math.max(0, j - (layoutParams4.bottomMargin + this.mTitleMarginBottom - n2));
                        }
                    }
                    i += paddingTop;
                    break;
                }
                case 48: {
                    i = this.getPaddingTop() + layoutParams3.topMargin + this.mTitleMarginTop;
                    break;
                }
                case 80: {
                    i = height - paddingBottom - layoutParams4.bottomMargin - this.mTitleMarginBottom - n;
                    break;
                }
            }
            if (b2) {
                if (b3) {
                    j = this.mTitleMarginStart;
                }
                else {
                    j = 0;
                }
                j -= mTempMargins[1];
                a -= Math.max(0, j);
                mTempMargins[1] = Math.max(0, -j);
                final int n3 = a;
                j = a;
                int a2 = n3;
                int n4 = i;
                if (shouldLayout) {
                    final LayoutParams layoutParams5 = (LayoutParams)this.mTitleTextView.getLayoutParams();
                    final int n5 = n3 - this.mTitleTextView.getMeasuredWidth();
                    final int n6 = i + this.mTitleTextView.getMeasuredHeight();
                    this.mTitleTextView.layout(n5, i, n3, n6);
                    a2 = n5 - this.mTitleMarginEnd;
                    n4 = n6 + layoutParams5.bottomMargin;
                }
                int b4 = j;
                if (shouldLayout2) {
                    final LayoutParams layoutParams6 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                    final int n7 = n4 + layoutParams6.topMargin;
                    i = this.mSubtitleTextView.getMeasuredWidth();
                    this.mSubtitleTextView.layout(j - i, n7, j, n7 + this.mSubtitleTextView.getMeasuredHeight());
                    b4 = j - this.mTitleMarginEnd;
                    i = layoutParams6.bottomMargin;
                }
                j = k;
                i = a;
                if (b3) {
                    i = Math.min(a2, b4);
                    j = k;
                }
            }
            else {
                if (b3) {
                    j = this.mTitleMarginStart;
                }
                else {
                    j = 0;
                }
                final int b5 = j - mTempMargins[0];
                j = k + Math.max(0, b5);
                mTempMargins[0] = Math.max(0, -b5);
                final int n8 = j;
                final int n9 = j;
                k = n8;
                int n10 = i;
                if (shouldLayout) {
                    final LayoutParams layoutParams7 = (LayoutParams)this.mTitleTextView.getLayoutParams();
                    k = n8 + this.mTitleTextView.getMeasuredWidth();
                    final int n11 = i + this.mTitleTextView.getMeasuredHeight();
                    this.mTitleTextView.layout(n8, i, k, n11);
                    k += this.mTitleMarginEnd;
                    n10 = n11 + layoutParams7.bottomMargin;
                }
                int b6 = n9;
                if (shouldLayout2) {
                    final LayoutParams layoutParams8 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                    final int n12 = n10 + layoutParams8.topMargin;
                    i = n9 + this.mSubtitleTextView.getMeasuredWidth();
                    this.mSubtitleTextView.layout(n9, n12, i, n12 + this.mSubtitleTextView.getMeasuredHeight());
                    b6 = i + this.mTitleMarginEnd;
                    i = layoutParams8.bottomMargin;
                }
                i = a;
                if (b3) {
                    j = Math.max(k, b6);
                    i = a;
                }
            }
        }
        this.addCustomViewsWithGravity(this.mTempViews, 3);
        for (a = this.mTempViews.size(), k = 0; k < a; ++k) {
            j = this.layoutChildLeft(this.mTempViews.get(k), j, mTempMargins, minimumHeight);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 5);
        final int size = this.mTempViews.size();
        a = 0;
        k = i;
        for (i = a; i < size; ++i) {
            k = this.layoutChildRight(this.mTempViews.get(i), k, mTempMargins, minimumHeight);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 1);
        i = this.getViewListMeasuredWidth(this.mTempViews, mTempMargins);
        a = paddingLeft + (width - paddingLeft - paddingRight) / 2 - i / 2;
        final int n13 = a + i;
        if (a < j) {
            i = j;
        }
        else {
            i = a;
            if (n13 > k) {
                i = a - (n13 - k);
            }
        }
        for (k = this.mTempViews.size(), j = 0; j < k; ++j) {
            i = this.layoutChildLeft(this.mTempViews.get(j), i, mTempMargins, minimumHeight);
        }
        this.mTempViews.clear();
    }
    
    protected void onMeasure(int resolveSizeAndState, final int n) {
        int max = 0;
        int combineMeasuredStates = 0;
        final int[] mTempMargins = this.mTempMargins;
        int n2;
        int n3;
        if (ViewUtils.isLayoutRtl((View)this)) {
            n2 = 1;
            n3 = 0;
        }
        else {
            n2 = 0;
            n3 = 1;
        }
        int b = 0;
        if (this.shouldLayout((View)this.mNavButtonView)) {
            this.measureChildConstrained((View)this.mNavButtonView, resolveSizeAndState, 0, n, 0, this.mMaxButtonHeight);
            b = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mNavButtonView);
            max = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mNavButtonView));
            combineMeasuredStates = ViewUtils.combineMeasuredStates(0, ViewCompat.getMeasuredState((View)this.mNavButtonView));
        }
        int combineMeasuredStates2 = combineMeasuredStates;
        int max2 = max;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            this.measureChildConstrained((View)this.mCollapseButtonView, resolveSizeAndState, 0, n, 0, this.mMaxButtonHeight);
            b = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mCollapseButtonView);
            max2 = Math.max(max, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mCollapseButtonView));
            combineMeasuredStates2 = ViewUtils.combineMeasuredStates(combineMeasuredStates, ViewCompat.getMeasuredState((View)this.mCollapseButtonView));
        }
        final int contentInsetStart = this.getContentInsetStart();
        final int n4 = 0 + Math.max(contentInsetStart, b);
        mTempMargins[n2] = Math.max(0, contentInsetStart - b);
        int b2 = 0;
        int combineMeasuredStates3 = combineMeasuredStates2;
        int max3 = max2;
        if (this.shouldLayout((View)this.mMenuView)) {
            this.measureChildConstrained((View)this.mMenuView, resolveSizeAndState, n4, n, 0, this.mMaxButtonHeight);
            b2 = this.mMenuView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mMenuView);
            max3 = Math.max(max2, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins((View)this.mMenuView));
            combineMeasuredStates3 = ViewUtils.combineMeasuredStates(combineMeasuredStates2, ViewCompat.getMeasuredState((View)this.mMenuView));
        }
        final int contentInsetEnd = this.getContentInsetEnd();
        final int n5 = n4 + Math.max(contentInsetEnd, b2);
        mTempMargins[n3] = Math.max(0, contentInsetEnd - b2);
        int n6 = n5;
        int combineMeasuredStates4 = combineMeasuredStates3;
        int max4 = max3;
        if (this.shouldLayout(this.mExpandedActionView)) {
            n6 = n5 + this.measureChildCollapseMargins(this.mExpandedActionView, resolveSizeAndState, n5, n, 0, mTempMargins);
            max4 = Math.max(max3, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
            combineMeasuredStates4 = ViewUtils.combineMeasuredStates(combineMeasuredStates3, ViewCompat.getMeasuredState(this.mExpandedActionView));
        }
        int n7 = n6;
        int combineMeasuredStates5 = combineMeasuredStates4;
        int max5 = max4;
        if (this.shouldLayout((View)this.mLogoView)) {
            n7 = n6 + this.measureChildCollapseMargins((View)this.mLogoView, resolveSizeAndState, n6, n, 0, mTempMargins);
            max5 = Math.max(max4, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins((View)this.mLogoView));
            combineMeasuredStates5 = ViewUtils.combineMeasuredStates(combineMeasuredStates4, ViewCompat.getMeasuredState((View)this.mLogoView));
        }
        final int childCount = this.getChildCount();
        int i = 0;
        int n8 = max5;
        int n9 = combineMeasuredStates5;
        int n10 = n7;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            int n11 = n10;
            int combineMeasuredStates6 = n9;
            int max6 = n8;
            if (((LayoutParams)child.getLayoutParams()).mViewType == 0) {
                if (!this.shouldLayout(child)) {
                    max6 = n8;
                    combineMeasuredStates6 = n9;
                    n11 = n10;
                }
                else {
                    n11 = n10 + this.measureChildCollapseMargins(child, resolveSizeAndState, n10, n, 0, mTempMargins);
                    max6 = Math.max(n8, child.getMeasuredHeight() + this.getVerticalMargins(child));
                    combineMeasuredStates6 = ViewUtils.combineMeasuredStates(n9, ViewCompat.getMeasuredState(child));
                }
            }
            ++i;
            n10 = n11;
            n9 = combineMeasuredStates6;
            n8 = max6;
        }
        int a = 0;
        int n12 = 0;
        final int n13 = this.mTitleMarginTop + this.mTitleMarginBottom;
        final int n14 = this.mTitleMarginStart + this.mTitleMarginEnd;
        int combineMeasuredStates7 = n9;
        if (this.shouldLayout((View)this.mTitleTextView)) {
            this.measureChildCollapseMargins((View)this.mTitleTextView, resolveSizeAndState, n10 + n14, n, n13, mTempMargins);
            a = this.mTitleTextView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mTitleTextView);
            n12 = this.mTitleTextView.getMeasuredHeight() + this.getVerticalMargins((View)this.mTitleTextView);
            combineMeasuredStates7 = ViewUtils.combineMeasuredStates(n9, ViewCompat.getMeasuredState((View)this.mTitleTextView));
        }
        int combineMeasuredStates8 = combineMeasuredStates7;
        int b3 = n12;
        int max7 = a;
        if (this.shouldLayout((View)this.mSubtitleTextView)) {
            max7 = Math.max(a, this.measureChildCollapseMargins((View)this.mSubtitleTextView, resolveSizeAndState, n10 + n14, n, n12 + n13, mTempMargins));
            b3 = n12 + (this.mSubtitleTextView.getMeasuredHeight() + this.getVerticalMargins((View)this.mSubtitleTextView));
            combineMeasuredStates8 = ViewUtils.combineMeasuredStates(combineMeasuredStates7, ViewCompat.getMeasuredState((View)this.mSubtitleTextView));
        }
        final int max8 = Math.max(n8, b3);
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int resolveSizeAndState2 = ViewCompat.resolveSizeAndState(Math.max(n10 + max7 + (paddingLeft + paddingRight), this.getSuggestedMinimumWidth()), resolveSizeAndState, 0xFF000000 & combineMeasuredStates8);
        resolveSizeAndState = ViewCompat.resolveSizeAndState(Math.max(max8 + (paddingTop + paddingBottom), this.getSuggestedMinimumHeight()), n, combineMeasuredStates8 << 16);
        if (this.shouldCollapse()) {
            resolveSizeAndState = 0;
        }
        this.setMeasuredDimension(resolveSizeAndState2, resolveSizeAndState);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        Object peekMenu;
        if (this.mMenuView != null) {
            peekMenu = this.mMenuView.peekMenu();
        }
        else {
            peekMenu = null;
        }
        if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && peekMenu != null) {
            final MenuItem item = ((Menu)peekMenu).findItem(savedState.expandedMenuItemId);
            if (item != null) {
                MenuItemCompat.expandActionView(item);
            }
        }
        if (savedState.isOverflowOpen) {
            this.postShowOverflowMenu();
        }
    }
    
    public void onRtlPropertiesChanged(final int n) {
        boolean direction = true;
        if (Build$VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(n);
        }
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        if (n != 1) {
            direction = false;
        }
        mContentInsets.setDirection(direction);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = this.isOverflowMenuShowing();
        return (Parcelable)savedState;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            final boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }
    
    void removeChildrenForExpandedActionView() {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            final View child = this.getChildAt(i);
            if (((LayoutParams)child.getLayoutParams()).mViewType != 2 && child != this.mMenuView) {
                this.removeViewAt(i);
                this.mHiddenViews.add(child);
            }
        }
    }
    
    public void setCollapsible(final boolean mCollapsible) {
        this.mCollapsible = mCollapsible;
        this.requestLayout();
    }
    
    public void setContentInsetsAbsolute(final int n, final int n2) {
        this.mContentInsets.setAbsolute(n, n2);
    }
    
    public void setContentInsetsRelative(final int n, final int n2) {
        this.mContentInsets.setRelative(n, n2);
    }
    
    public void setLogo(@DrawableRes final int n) {
        this.setLogo(this.mDrawableManager.getDrawable(this.getContext(), n));
    }
    
    public void setLogo(final Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.ensureLogoView();
            if (!this.isChildOrHidden((View)this.mLogoView)) {
                this.addSystemView((View)this.mLogoView, true);
            }
        }
        else if (this.mLogoView != null && this.isChildOrHidden((View)this.mLogoView)) {
            this.removeView((View)this.mLogoView);
            this.mHiddenViews.remove(this.mLogoView);
        }
        if (this.mLogoView != null) {
            this.mLogoView.setImageDrawable(imageDrawable);
        }
    }
    
    public void setLogoDescription(@StringRes final int n) {
        this.setLogoDescription(this.getContext().getText(n));
    }
    
    public void setLogoDescription(final CharSequence contentDescription) {
        if (!TextUtils.isEmpty(contentDescription)) {
            this.ensureLogoView();
        }
        if (this.mLogoView != null) {
            this.mLogoView.setContentDescription(contentDescription);
        }
    }
    
    public void setMenu(final MenuBuilder menuBuilder, final ActionMenuPresenter actionMenuPresenter) {
        if (menuBuilder != null || this.mMenuView != null) {
            this.ensureMenuView();
            final MenuBuilder peekMenu = this.mMenuView.peekMenu();
            if (peekMenu != menuBuilder) {
                if (peekMenu != null) {
                    peekMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
                    peekMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                if (this.mExpandedMenuPresenter == null) {
                    this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
                }
                actionMenuPresenter.setExpandedActionViewsExclusive(true);
                if (menuBuilder != null) {
                    menuBuilder.addMenuPresenter(actionMenuPresenter, this.mPopupContext);
                    menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
                }
                else {
                    actionMenuPresenter.initForMenu(this.mPopupContext, null);
                    this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
                    actionMenuPresenter.updateMenuView(true);
                    this.mExpandedMenuPresenter.updateMenuView(true);
                }
                this.mMenuView.setPopupTheme(this.mPopupTheme);
                this.mMenuView.setPresenter(actionMenuPresenter);
                this.mOuterActionMenuPresenter = actionMenuPresenter;
            }
        }
    }
    
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final MenuBuilder.Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
        if (this.mMenuView != null) {
            this.mMenuView.setMenuCallbacks(mActionMenuPresenterCallback, mMenuBuilderCallback);
        }
    }
    
    public void setNavigationContentDescription(@StringRes final int n) {
        CharSequence text;
        if (n != 0) {
            text = this.getContext().getText(n);
        }
        else {
            text = null;
        }
        this.setNavigationContentDescription(text);
    }
    
    public void setNavigationContentDescription(@Nullable final CharSequence contentDescription) {
        if (!TextUtils.isEmpty(contentDescription)) {
            this.ensureNavButtonView();
        }
        if (this.mNavButtonView != null) {
            this.mNavButtonView.setContentDescription(contentDescription);
        }
    }
    
    public void setNavigationIcon(@DrawableRes final int n) {
        this.setNavigationIcon(this.mDrawableManager.getDrawable(this.getContext(), n));
    }
    
    public void setNavigationIcon(@Nullable final Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.ensureNavButtonView();
            if (!this.isChildOrHidden((View)this.mNavButtonView)) {
                this.addSystemView((View)this.mNavButtonView, true);
            }
        }
        else if (this.mNavButtonView != null && this.isChildOrHidden((View)this.mNavButtonView)) {
            this.removeView((View)this.mNavButtonView);
            this.mHiddenViews.remove(this.mNavButtonView);
        }
        if (this.mNavButtonView != null) {
            this.mNavButtonView.setImageDrawable(imageDrawable);
        }
    }
    
    public void setNavigationOnClickListener(final View$OnClickListener onClickListener) {
        this.ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(@Nullable final Drawable overflowIcon) {
        this.ensureMenu();
        this.mMenuView.setOverflowIcon(overflowIcon);
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
    
    public void setSubtitle(@StringRes final int n) {
        this.setSubtitle(this.getContext().getText(n));
    }
    
    public void setSubtitle(final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mSubtitleTextView == null) {
                final Context context = this.getContext();
                (this.mSubtitleTextView = new TextView(context)).setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils$TruncateAt.END);
                if (this.mSubtitleTextAppearance != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, this.mSubtitleTextAppearance);
                }
                if (this.mSubtitleTextColor != 0) {
                    this.mSubtitleTextView.setTextColor(this.mSubtitleTextColor);
                }
            }
            if (!this.isChildOrHidden((View)this.mSubtitleTextView)) {
                this.addSystemView((View)this.mSubtitleTextView, true);
            }
        }
        else if (this.mSubtitleTextView != null && this.isChildOrHidden((View)this.mSubtitleTextView)) {
            this.removeView((View)this.mSubtitleTextView);
            this.mHiddenViews.remove(this.mSubtitleTextView);
        }
        if (this.mSubtitleTextView != null) {
            this.mSubtitleTextView.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }
    
    public void setSubtitleTextAppearance(final Context context, @StyleRes final int mSubtitleTextAppearance) {
        this.mSubtitleTextAppearance = mSubtitleTextAppearance;
        if (this.mSubtitleTextView != null) {
            this.mSubtitleTextView.setTextAppearance(context, mSubtitleTextAppearance);
        }
    }
    
    public void setSubtitleTextColor(@ColorInt final int n) {
        this.mSubtitleTextColor = n;
        if (this.mSubtitleTextView != null) {
            this.mSubtitleTextView.setTextColor(n);
        }
    }
    
    public void setTitle(@StringRes final int n) {
        this.setTitle(this.getContext().getText(n));
    }
    
    public void setTitle(final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mTitleTextView == null) {
                final Context context = this.getContext();
                (this.mTitleTextView = new TextView(context)).setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils$TruncateAt.END);
                if (this.mTitleTextAppearance != 0) {
                    this.mTitleTextView.setTextAppearance(context, this.mTitleTextAppearance);
                }
                if (this.mTitleTextColor != 0) {
                    this.mTitleTextView.setTextColor(this.mTitleTextColor);
                }
            }
            if (!this.isChildOrHidden((View)this.mTitleTextView)) {
                this.addSystemView((View)this.mTitleTextView, true);
            }
        }
        else if (this.mTitleTextView != null && this.isChildOrHidden((View)this.mTitleTextView)) {
            this.removeView((View)this.mTitleTextView);
            this.mHiddenViews.remove(this.mTitleTextView);
        }
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }
    
    public void setTitleTextAppearance(final Context context, @StyleRes final int mTitleTextAppearance) {
        this.mTitleTextAppearance = mTitleTextAppearance;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
        }
    }
    
    public void setTitleTextColor(@ColorInt final int n) {
        this.mTitleTextColor = n;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextColor(n);
        }
    }
    
    public boolean showOverflowMenu() {
        return this.mMenuView != null && this.mMenuView.showOverflowMenu();
    }
    
    private class ExpandedActionViewMenuPresenter implements MenuPresenter
    {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;
        
        @Override
        public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            Toolbar.this.removeView(Toolbar.this.mExpandedActionView);
            Toolbar.this.removeView((View)Toolbar.this.mCollapseButtonView);
            Toolbar.this.mExpandedActionView = null;
            Toolbar.this.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }
        
        @Override
        public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl mCurrentExpandedItem) {
            Toolbar.this.ensureCollapseButtonView();
            if (Toolbar.this.mCollapseButtonView.getParent() != Toolbar.this) {
                Toolbar.this.addView((View)Toolbar.this.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = mCurrentExpandedItem.getActionView();
            this.mCurrentExpandedItem = mCurrentExpandedItem;
            if (Toolbar.this.mExpandedActionView.getParent() != Toolbar.this) {
                final LayoutParams generateDefaultLayoutParams = Toolbar.this.generateDefaultLayoutParams();
                generateDefaultLayoutParams.gravity = (0x800003 | (Toolbar.this.mButtonGravity & 0x70));
                generateDefaultLayoutParams.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
                Toolbar.this.addView(Toolbar.this.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            mCurrentExpandedItem.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }
        
        @Override
        public boolean flagActionItems() {
            return false;
        }
        
        @Override
        public int getId() {
            return 0;
        }
        
        @Override
        public MenuView getMenuView(final ViewGroup viewGroup) {
            return null;
        }
        
        @Override
        public void initForMenu(final Context context, final MenuBuilder mMenu) {
            if (this.mMenu != null && this.mCurrentExpandedItem != null) {
                this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
            }
            this.mMenu = mMenu;
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public void onRestoreInstanceState(final Parcelable parcelable) {
        }
        
        @Override
        public Parcelable onSaveInstanceState() {
            return null;
        }
        
        @Override
        public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
            return false;
        }
        
        @Override
        public void setCallback(final Callback callback) {
        }
        
        @Override
        public void updateMenuView(final boolean b) {
            if (this.mCurrentExpandedItem != null) {
                int n = 0;
                if (this.mMenu != null) {
                    final int size = this.mMenu.size();
                    int n2 = 0;
                    while (true) {
                        n = n;
                        if (n2 >= size) {
                            break;
                        }
                        if (this.mMenu.getItem(n2) == this.mCurrentExpandedItem) {
                            n = 1;
                            break;
                        }
                        ++n2;
                    }
                }
                if (n == 0) {
                    this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }
    }
    
    public static class LayoutParams extends ActionBar.LayoutParams
    {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;
        
        public LayoutParams(final int n) {
            this(-2, -1, n);
        }
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mViewType = 0;
            this.gravity = 8388627;
        }
        
        public LayoutParams(final int n, final int n2, final int gravity) {
            super(n, n2);
            this.mViewType = 0;
            this.gravity = gravity;
        }
        
        public LayoutParams(@NonNull final Context context, final AttributeSet set) {
            super(context, set);
            this.mViewType = 0;
        }
        
        public LayoutParams(final ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ActionBar.LayoutParams)layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mViewType = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super((ViewGroup$LayoutParams)viewGroup$MarginLayoutParams);
            this.mViewType = 0;
            this.copyMarginsFromCompat(viewGroup$MarginLayoutParams);
        }
        
        void copyMarginsFromCompat(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            this.leftMargin = viewGroup$MarginLayoutParams.leftMargin;
            this.topMargin = viewGroup$MarginLayoutParams.topMargin;
            this.rightMargin = viewGroup$MarginLayoutParams.rightMargin;
            this.bottomMargin = viewGroup$MarginLayoutParams.bottomMargin;
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
    
    public static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int expandedMenuItemId;
        boolean isOverflowOpen;
        
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
        
        public SavedState(final Parcel parcel) {
            super(parcel);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = (parcel.readInt() != 0);
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public void writeToParcel(final Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.expandedMenuItemId);
            if (this.isOverflowOpen) {
                n = 1;
            }
            else {
                n = 0;
            }
            parcel.writeInt(n);
        }
    }
}
