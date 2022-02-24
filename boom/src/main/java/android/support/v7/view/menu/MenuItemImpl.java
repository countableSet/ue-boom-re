// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.view.menu;

import android.view.MenuItem$OnActionExpandListener;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.ActivityNotFoundException;
import android.util.Log;
import android.os.Build$VERSION;
import android.view.SubMenu;
import android.view.ViewDebug$CapturedViewProperty;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.MenuItem;
import android.support.v4.view.MenuItemCompat;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.View;
import android.support.v4.view.ActionProvider;
import android.support.v4.internal.view.SupportMenuItem;

public final class MenuItemImpl implements SupportMenuItem
{
    private static final int CHECKABLE = 1;
    private static final int CHECKED = 2;
    private static final int ENABLED = 16;
    private static final int EXCLUSIVE = 4;
    private static final int HIDDEN = 8;
    private static final int IS_ACTION = 32;
    static final int NO_ICON = 0;
    private static final int SHOW_AS_ACTION_MASK = 3;
    private static final String TAG = "MenuItemImpl";
    private static String sDeleteShortcutLabel;
    private static String sEnterShortcutLabel;
    private static String sPrependShortcutLabel;
    private static String sSpaceShortcutLabel;
    private ActionProvider mActionProvider;
    private View mActionView;
    private final int mCategoryOrder;
    private MenuItem$OnMenuItemClickListener mClickListener;
    private int mFlags;
    private final int mGroup;
    private Drawable mIconDrawable;
    private int mIconResId;
    private final int mId;
    private Intent mIntent;
    private boolean mIsActionViewExpanded;
    private Runnable mItemCallback;
    private MenuBuilder mMenu;
    private ContextMenu$ContextMenuInfo mMenuInfo;
    private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;
    private final int mOrdering;
    private char mShortcutAlphabeticChar;
    private char mShortcutNumericChar;
    private int mShowAsAction;
    private SubMenuBuilder mSubMenu;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    
    MenuItemImpl(final MenuBuilder mMenu, final int mGroup, final int mId, final int mCategoryOrder, final int mOrdering, final CharSequence mTitle, final int mShowAsAction) {
        this.mIconResId = 0;
        this.mFlags = 16;
        this.mShowAsAction = 0;
        this.mIsActionViewExpanded = false;
        this.mMenu = mMenu;
        this.mId = mId;
        this.mGroup = mGroup;
        this.mCategoryOrder = mCategoryOrder;
        this.mOrdering = mOrdering;
        this.mTitle = mTitle;
        this.mShowAsAction = mShowAsAction;
    }
    
    public void actionFormatChanged() {
        this.mMenu.onItemActionRequestChanged(this);
    }
    
    @Override
    public boolean collapseActionView() {
        boolean collapseItemActionView = false;
        if ((this.mShowAsAction & 0x8) != 0x0) {
            if (this.mActionView == null) {
                collapseItemActionView = true;
            }
            else if (this.mOnActionExpandListener == null || this.mOnActionExpandListener.onMenuItemActionCollapse((MenuItem)this)) {
                collapseItemActionView = this.mMenu.collapseItemActionView(this);
            }
        }
        return collapseItemActionView;
    }
    
    @Override
    public boolean expandActionView() {
        boolean expandItemActionView = false;
        if (this.hasCollapsibleActionView() && (this.mOnActionExpandListener == null || this.mOnActionExpandListener.onMenuItemActionExpand((MenuItem)this))) {
            expandItemActionView = this.mMenu.expandItemActionView(this);
        }
        return expandItemActionView;
    }
    
    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }
    
    @Override
    public View getActionView() {
        View view;
        if (this.mActionView != null) {
            view = this.mActionView;
        }
        else if (this.mActionProvider != null) {
            this.mActionView = this.mActionProvider.onCreateActionView((MenuItem)this);
            view = this.mActionView;
        }
        else {
            view = null;
        }
        return view;
    }
    
    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }
    
    Runnable getCallback() {
        return this.mItemCallback;
    }
    
    public int getGroupId() {
        return this.mGroup;
    }
    
    public Drawable getIcon() {
        Drawable mIconDrawable;
        if (this.mIconDrawable != null) {
            mIconDrawable = this.mIconDrawable;
        }
        else if (this.mIconResId != 0) {
            mIconDrawable = AppCompatDrawableManager.get().getDrawable(this.mMenu.getContext(), this.mIconResId);
            this.mIconResId = 0;
            this.mIconDrawable = mIconDrawable;
        }
        else {
            mIconDrawable = null;
        }
        return mIconDrawable;
    }
    
    public Intent getIntent() {
        return this.mIntent;
    }
    
    @ViewDebug$CapturedViewProperty
    public int getItemId() {
        return this.mId;
    }
    
    public ContextMenu$ContextMenuInfo getMenuInfo() {
        return this.mMenuInfo;
    }
    
    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }
    
    public int getOrder() {
        return this.mCategoryOrder;
    }
    
    public int getOrdering() {
        return this.mOrdering;
    }
    
    char getShortcut() {
        char c;
        if (this.mMenu.isQwertyMode()) {
            c = this.mShortcutAlphabeticChar;
        }
        else {
            c = this.mShortcutNumericChar;
        }
        return c;
    }
    
    String getShortcutLabel() {
        final char shortcut = this.getShortcut();
        String string;
        if (shortcut == '\0') {
            string = "";
        }
        else {
            final StringBuilder sb = new StringBuilder(MenuItemImpl.sPrependShortcutLabel);
            switch (shortcut) {
                default: {
                    sb.append(shortcut);
                    break;
                }
                case 10: {
                    sb.append(MenuItemImpl.sEnterShortcutLabel);
                    break;
                }
                case 8: {
                    sb.append(MenuItemImpl.sDeleteShortcutLabel);
                    break;
                }
                case 32: {
                    sb.append(MenuItemImpl.sSpaceShortcutLabel);
                    break;
                }
            }
            string = sb.toString();
        }
        return string;
    }
    
    public SubMenu getSubMenu() {
        return (SubMenu)this.mSubMenu;
    }
    
    @Override
    public ActionProvider getSupportActionProvider() {
        return this.mActionProvider;
    }
    
    @ViewDebug$CapturedViewProperty
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    public CharSequence getTitleCondensed() {
        CharSequence charSequence;
        if (this.mTitleCondensed != null) {
            charSequence = this.mTitleCondensed;
        }
        else {
            charSequence = this.mTitle;
        }
        CharSequence string = charSequence;
        if (Build$VERSION.SDK_INT < 18 && (string = charSequence) != null) {
            string = charSequence;
            if (!(charSequence instanceof String)) {
                string = charSequence.toString();
            }
        }
        return string;
    }
    
    CharSequence getTitleForItemView(final MenuView.ItemView itemView) {
        CharSequence charSequence;
        if (itemView != null && itemView.prefersCondensedTitle()) {
            charSequence = this.getTitleCondensed();
        }
        else {
            charSequence = this.getTitle();
        }
        return charSequence;
    }
    
    public boolean hasCollapsibleActionView() {
        boolean b = false;
        if ((this.mShowAsAction & 0x8) != 0x0) {
            if (this.mActionView == null && this.mActionProvider != null) {
                this.mActionView = this.mActionProvider.onCreateActionView((MenuItem)this);
            }
            b = b;
            if (this.mActionView != null) {
                b = true;
            }
        }
        return b;
    }
    
    public boolean hasSubMenu() {
        return this.mSubMenu != null;
    }
    
    public boolean invoke() {
        final boolean b = true;
        boolean b2;
        if (this.mClickListener != null && this.mClickListener.onMenuItemClick((MenuItem)this)) {
            b2 = b;
        }
        else {
            b2 = b;
            if (!this.mMenu.dispatchMenuItemSelected(this.mMenu.getRootMenu(), (MenuItem)this)) {
                if (this.mItemCallback != null) {
                    this.mItemCallback.run();
                    b2 = b;
                }
                else {
                    if (this.mIntent != null) {
                        try {
                            this.mMenu.getContext().startActivity(this.mIntent);
                            b2 = b;
                            return b2;
                        }
                        catch (ActivityNotFoundException ex) {
                            Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", (Throwable)ex);
                        }
                    }
                    if (this.mActionProvider != null) {
                        b2 = b;
                        if (this.mActionProvider.onPerformDefaultAction()) {
                            return b2;
                        }
                    }
                    b2 = false;
                }
            }
        }
        return b2;
    }
    
    public boolean isActionButton() {
        return (this.mFlags & 0x20) == 0x20;
    }
    
    @Override
    public boolean isActionViewExpanded() {
        return this.mIsActionViewExpanded;
    }
    
    public boolean isCheckable() {
        boolean b = true;
        if ((this.mFlags & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public boolean isChecked() {
        return (this.mFlags & 0x2) == 0x2;
    }
    
    public boolean isEnabled() {
        return (this.mFlags & 0x10) != 0x0;
    }
    
    public boolean isExclusiveCheckable() {
        return (this.mFlags & 0x4) != 0x0;
    }
    
    public boolean isVisible() {
        boolean b = true;
        if (this.mActionProvider != null && this.mActionProvider.overridesItemVisibility()) {
            if ((this.mFlags & 0x8) != 0x0 || !this.mActionProvider.isVisible()) {
                b = false;
            }
        }
        else if ((this.mFlags & 0x8) != 0x0) {
            b = false;
        }
        return b;
    }
    
    public boolean requestsActionButton() {
        boolean b = true;
        if ((this.mShowAsAction & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public boolean requiresActionButton() {
        return (this.mShowAsAction & 0x2) == 0x2;
    }
    
    public MenuItem setActionProvider(final android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }
    
    public SupportMenuItem setActionView(final int n) {
        final Context context = this.mMenu.getContext();
        this.setActionView(LayoutInflater.from(context).inflate(n, (ViewGroup)new LinearLayout(context), false));
        return this;
    }
    
    public SupportMenuItem setActionView(final View mActionView) {
        this.mActionView = mActionView;
        this.mActionProvider = null;
        if (mActionView != null && mActionView.getId() == -1 && this.mId > 0) {
            mActionView.setId(this.mId);
        }
        this.mMenu.onItemActionRequestChanged(this);
        return this;
    }
    
    public void setActionViewExpanded(final boolean mIsActionViewExpanded) {
        this.mIsActionViewExpanded = mIsActionViewExpanded;
        this.mMenu.onItemsChanged(false);
    }
    
    public MenuItem setAlphabeticShortcut(final char ch) {
        if (this.mShortcutAlphabeticChar != ch) {
            this.mShortcutAlphabeticChar = Character.toLowerCase(ch);
            this.mMenu.onItemsChanged(false);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setCallback(final Runnable mItemCallback) {
        this.mItemCallback = mItemCallback;
        return (MenuItem)this;
    }
    
    public MenuItem setCheckable(final boolean b) {
        final int mFlags = this.mFlags;
        final int mFlags2 = this.mFlags;
        boolean b2;
        if (b) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        this.mFlags = ((b2 ? 1 : 0) | (mFlags2 & 0xFFFFFFFE));
        if (mFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setChecked(final boolean checkedInt) {
        if ((this.mFlags & 0x4) != 0x0) {
            this.mMenu.setExclusiveItemChecked((MenuItem)this);
        }
        else {
            this.setCheckedInt(checkedInt);
        }
        return (MenuItem)this;
    }
    
    void setCheckedInt(final boolean b) {
        final int mFlags = this.mFlags;
        final int mFlags2 = this.mFlags;
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.mFlags = (n | (mFlags2 & 0xFFFFFFFD));
        if (mFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
    }
    
    public MenuItem setEnabled(final boolean b) {
        if (b) {
            this.mFlags |= 0x10;
        }
        else {
            this.mFlags &= 0xFFFFFFEF;
        }
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public void setExclusiveCheckable(final boolean b) {
        final int mFlags = this.mFlags;
        int n;
        if (b) {
            n = 4;
        }
        else {
            n = 0;
        }
        this.mFlags = (n | (mFlags & 0xFFFFFFFB));
    }
    
    public MenuItem setIcon(final int mIconResId) {
        this.mIconDrawable = null;
        this.mIconResId = mIconResId;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setIcon(final Drawable mIconDrawable) {
        this.mIconResId = 0;
        this.mIconDrawable = mIconDrawable;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setIntent(final Intent mIntent) {
        this.mIntent = mIntent;
        return (MenuItem)this;
    }
    
    public void setIsActionButton(final boolean b) {
        if (b) {
            this.mFlags |= 0x20;
        }
        else {
            this.mFlags &= 0xFFFFFFDF;
        }
    }
    
    void setMenuInfo(final ContextMenu$ContextMenuInfo mMenuInfo) {
        this.mMenuInfo = mMenuInfo;
    }
    
    public MenuItem setNumericShortcut(final char c) {
        if (this.mShortcutNumericChar != c) {
            this.mShortcutNumericChar = c;
            this.mMenu.onItemsChanged(false);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setOnActionExpandListener(final MenuItem$OnActionExpandListener menuItem$OnActionExpandListener) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setOnActionExpandListener()");
    }
    
    public MenuItem setOnMenuItemClickListener(final MenuItem$OnMenuItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char c, final char ch) {
        this.mShortcutNumericChar = c;
        this.mShortcutAlphabeticChar = Character.toLowerCase(ch);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public void setShowAsAction(final int mShowAsAction) {
        switch (mShowAsAction & 0x3) {
            default: {
                throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
            }
            case 0:
            case 1:
            case 2: {
                this.mShowAsAction = mShowAsAction;
                this.mMenu.onItemActionRequestChanged(this);
            }
        }
    }
    
    public SupportMenuItem setShowAsActionFlags(final int showAsAction) {
        this.setShowAsAction(showAsAction);
        return this;
    }
    
    public void setSubMenu(final SubMenuBuilder mSubMenu) {
        (this.mSubMenu = mSubMenu).setHeaderTitle(this.getTitle());
    }
    
    @Override
    public SupportMenuItem setSupportActionProvider(final ActionProvider mActionProvider) {
        if (this.mActionProvider != null) {
            this.mActionProvider.reset();
        }
        this.mActionView = null;
        this.mActionProvider = mActionProvider;
        this.mMenu.onItemsChanged(true);
        if (this.mActionProvider != null) {
            this.mActionProvider.setVisibilityListener((ActionProvider.VisibilityListener)new ActionProvider.VisibilityListener() {
                @Override
                public void onActionProviderVisibilityChanged(final boolean b) {
                    MenuItemImpl.this.mMenu.onItemVisibleChanged(MenuItemImpl.this);
                }
            });
        }
        return this;
    }
    
    @Override
    public SupportMenuItem setSupportOnActionExpandListener(final MenuItemCompat.OnActionExpandListener mOnActionExpandListener) {
        this.mOnActionExpandListener = mOnActionExpandListener;
        return this;
    }
    
    public MenuItem setTitle(final int n) {
        return this.setTitle(this.mMenu.getContext().getString(n));
    }
    
    public MenuItem setTitle(final CharSequence charSequence) {
        this.mTitle = charSequence;
        this.mMenu.onItemsChanged(false);
        if (this.mSubMenu != null) {
            this.mSubMenu.setHeaderTitle(charSequence);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setTitleCondensed(CharSequence mTitle) {
        this.mTitleCondensed = mTitle;
        if (mTitle == null) {
            mTitle = this.mTitle;
        }
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setVisible(final boolean visibleInt) {
        if (this.setVisibleInt(visibleInt)) {
            this.mMenu.onItemVisibleChanged(this);
        }
        return (MenuItem)this;
    }
    
    boolean setVisibleInt(final boolean b) {
        final boolean b2 = false;
        final int mFlags = this.mFlags;
        final int mFlags2 = this.mFlags;
        int n;
        if (b) {
            n = 0;
        }
        else {
            n = 8;
        }
        this.mFlags = (n | (mFlags2 & 0xFFFFFFF7));
        boolean b3 = b2;
        if (mFlags != this.mFlags) {
            b3 = true;
        }
        return b3;
    }
    
    public boolean shouldShowIcon() {
        return this.mMenu.getOptionalIconsVisible();
    }
    
    boolean shouldShowShortcut() {
        return this.mMenu.isShortcutsVisible() && this.getShortcut() != '\0';
    }
    
    public boolean showsTextAsAction() {
        return (this.mShowAsAction & 0x4) == 0x4;
    }
    
    @Override
    public String toString() {
        String string;
        if (this.mTitle != null) {
            string = this.mTitle.toString();
        }
        else {
            string = null;
        }
        return string;
    }
}
