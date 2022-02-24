// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.view.menu;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ActionProvider;
import java.util.Collection;
import android.view.KeyCharacterMap$KeyData;
import android.view.KeyEvent;
import android.view.SubMenu;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.content.ComponentName;
import android.view.MenuItem;
import android.support.v7.appcompat.R;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import java.util.Iterator;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Context;
import java.util.ArrayList;
import android.support.v4.internal.view.SupportMenu;

public class MenuBuilder implements SupportMenu
{
    private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
    private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
    private static final String PRESENTER_KEY = "android:menu:presenters";
    private static final String TAG = "MenuBuilder";
    private static final int[] sCategoryToOrder;
    private ArrayList<MenuItemImpl> mActionItems;
    private Callback mCallback;
    private final Context mContext;
    private ContextMenu$ContextMenuInfo mCurrentMenuInfo;
    private int mDefaultShowAsAction;
    private MenuItemImpl mExpandedItem;
    private SparseArray<Parcelable> mFrozenViewStates;
    Drawable mHeaderIcon;
    CharSequence mHeaderTitle;
    View mHeaderView;
    private boolean mIsActionItemsStale;
    private boolean mIsClosing;
    private boolean mIsVisibleItemsStale;
    private ArrayList<MenuItemImpl> mItems;
    private boolean mItemsChangedWhileDispatchPrevented;
    private ArrayList<MenuItemImpl> mNonActionItems;
    private boolean mOptionalIconsVisible;
    private boolean mOverrideVisibleItems;
    private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters;
    private boolean mPreventDispatchingItemsChanged;
    private boolean mQwertyMode;
    private final Resources mResources;
    private boolean mShortcutsVisible;
    private ArrayList<MenuItemImpl> mTempShortcutItemList;
    private ArrayList<MenuItemImpl> mVisibleItems;
    
    static {
        sCategoryToOrder = new int[] { 1, 4, 5, 3, 2, 0 };
    }
    
    public MenuBuilder(final Context mContext) {
        this.mDefaultShowAsAction = 0;
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.mOptionalIconsVisible = false;
        this.mIsClosing = false;
        this.mTempShortcutItemList = new ArrayList<MenuItemImpl>();
        this.mPresenters = new CopyOnWriteArrayList<WeakReference<MenuPresenter>>();
        this.mContext = mContext;
        this.mResources = mContext.getResources();
        this.mItems = new ArrayList<MenuItemImpl>();
        this.mVisibleItems = new ArrayList<MenuItemImpl>();
        this.mIsVisibleItemsStale = true;
        this.mActionItems = new ArrayList<MenuItemImpl>();
        this.mNonActionItems = new ArrayList<MenuItemImpl>();
        this.setShortcutsVisibleInner(this.mIsActionItemsStale = true);
    }
    
    private MenuItemImpl createNewMenuItem(final int n, final int n2, final int n3, final int n4, final CharSequence charSequence, final int n5) {
        return new MenuItemImpl(this, n, n2, n3, n4, charSequence, n5);
    }
    
    private void dispatchPresenterUpdate(final boolean b) {
        if (!this.mPresenters.isEmpty()) {
            this.stopDispatchingItemsChanged();
            for (final WeakReference<MenuPresenter> o : this.mPresenters) {
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    menuPresenter.updateMenuView(b);
                }
            }
            this.startDispatchingItemsChanged();
        }
    }
    
    private void dispatchRestoreInstanceState(final Bundle bundle) {
        final SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:presenters");
        if (sparseParcelableArray != null && !this.mPresenters.isEmpty()) {
            for (final WeakReference<MenuPresenter> o : this.mPresenters) {
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    final int id = menuPresenter.getId();
                    if (id <= 0) {
                        continue;
                    }
                    final Parcelable parcelable = (Parcelable)sparseParcelableArray.get(id);
                    if (parcelable == null) {
                        continue;
                    }
                    menuPresenter.onRestoreInstanceState(parcelable);
                }
            }
        }
    }
    
    private void dispatchSaveInstanceState(final Bundle bundle) {
        if (!this.mPresenters.isEmpty()) {
            final SparseArray sparseArray = new SparseArray();
            for (final WeakReference<MenuPresenter> o : this.mPresenters) {
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    final int id = menuPresenter.getId();
                    if (id <= 0) {
                        continue;
                    }
                    final Parcelable onSaveInstanceState = menuPresenter.onSaveInstanceState();
                    if (onSaveInstanceState == null) {
                        continue;
                    }
                    sparseArray.put(id, (Object)onSaveInstanceState);
                }
            }
            bundle.putSparseParcelableArray("android:menu:presenters", sparseArray);
        }
    }
    
    private boolean dispatchSubMenuSelected(final SubMenuBuilder subMenuBuilder, final MenuPresenter menuPresenter) {
        boolean b;
        if (this.mPresenters.isEmpty()) {
            b = false;
        }
        else {
            int n = 0;
            if (menuPresenter != null) {
                n = (menuPresenter.onSubMenuSelected(subMenuBuilder) ? 1 : 0);
            }
            final Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
            while (true) {
                b = (n != 0);
                if (!iterator.hasNext()) {
                    break;
                }
                final WeakReference<MenuPresenter> o = iterator.next();
                final MenuPresenter menuPresenter2 = o.get();
                if (menuPresenter2 == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    if (n != 0) {
                        continue;
                    }
                    n = (menuPresenter2.onSubMenuSelected(subMenuBuilder) ? 1 : 0);
                }
            }
        }
        return b;
    }
    
    private static int findInsertIndex(final ArrayList<MenuItemImpl> list, int n) {
        for (int i = list.size() - 1; i >= 0; --i) {
            if (list.get(i).getOrdering() <= n) {
                n = i + 1;
                return n;
            }
        }
        n = 0;
        return n;
    }
    
    private static int getOrdering(final int n) {
        final int n2 = (0xFFFF0000 & n) >> 16;
        if (n2 < 0 || n2 >= MenuBuilder.sCategoryToOrder.length) {
            throw new IllegalArgumentException("order does not contain a valid category.");
        }
        return MenuBuilder.sCategoryToOrder[n2] << 16 | (0xFFFF & n);
    }
    
    private void removeItemAtInt(final int index, final boolean b) {
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.remove(index);
            if (b) {
                this.onItemsChanged(true);
            }
        }
    }
    
    private void setHeaderInternal(final int n, final CharSequence mHeaderTitle, final int n2, final Drawable mHeaderIcon, final View mHeaderView) {
        final Resources resources = this.getResources();
        if (mHeaderView != null) {
            this.mHeaderView = mHeaderView;
            this.mHeaderTitle = null;
            this.mHeaderIcon = null;
        }
        else {
            if (n > 0) {
                this.mHeaderTitle = resources.getText(n);
            }
            else if (mHeaderTitle != null) {
                this.mHeaderTitle = mHeaderTitle;
            }
            if (n2 > 0) {
                this.mHeaderIcon = ContextCompat.getDrawable(this.getContext(), n2);
            }
            else if (mHeaderIcon != null) {
                this.mHeaderIcon = mHeaderIcon;
            }
            this.mHeaderView = null;
        }
        this.onItemsChanged(false);
    }
    
    private void setShortcutsVisibleInner(final boolean b) {
        final boolean b2 = true;
        this.mShortcutsVisible = (b && this.mResources.getConfiguration().keyboard != 1 && this.mResources.getBoolean(R.bool.abc_config_showMenuShortcutsWhenKeyboardPresent) && b2);
    }
    
    public MenuItem add(final int n) {
        return this.addInternal(0, 0, 0, this.mResources.getString(n));
    }
    
    public MenuItem add(final int n, final int n2, final int n3, final int n4) {
        return this.addInternal(n, n2, n3, this.mResources.getString(n4));
    }
    
    public MenuItem add(final int n, final int n2, final int n3, final CharSequence charSequence) {
        return this.addInternal(n, n2, n3, charSequence);
    }
    
    public MenuItem add(final CharSequence charSequence) {
        return this.addInternal(0, 0, 0, charSequence);
    }
    
    public int addIntentOptions(final int n, final int n2, final int n3, final ComponentName componentName, final Intent[] array, final Intent intent, int i, final MenuItem[] array2) {
        final PackageManager packageManager = this.mContext.getPackageManager();
        final List queryIntentActivityOptions = packageManager.queryIntentActivityOptions(componentName, array, intent, 0);
        int size;
        if (queryIntentActivityOptions != null) {
            size = queryIntentActivityOptions.size();
        }
        else {
            size = 0;
        }
        if ((i & 0x1) == 0x0) {
            this.removeGroup(n);
        }
        ResolveInfo resolveInfo;
        Intent intent2;
        Intent intent3;
        MenuItem setIntent;
        for (i = 0; i < size; ++i) {
            resolveInfo = queryIntentActivityOptions.get(i);
            if (resolveInfo.specificIndex < 0) {
                intent2 = intent;
            }
            else {
                intent2 = array[resolveInfo.specificIndex];
            }
            intent3 = new Intent(intent2);
            intent3.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
            setIntent = this.add(n, n2, n3, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent(intent3);
            if (array2 != null && resolveInfo.specificIndex >= 0) {
                array2[resolveInfo.specificIndex] = setIntent;
            }
        }
        return size;
    }
    
    protected MenuItem addInternal(final int n, final int n2, final int n3, final CharSequence charSequence) {
        final int ordering = getOrdering(n3);
        final MenuItemImpl newMenuItem = this.createNewMenuItem(n, n2, n3, ordering, charSequence, this.mDefaultShowAsAction);
        if (this.mCurrentMenuInfo != null) {
            newMenuItem.setMenuInfo(this.mCurrentMenuInfo);
        }
        this.mItems.add(findInsertIndex(this.mItems, ordering), newMenuItem);
        this.onItemsChanged(true);
        return (MenuItem)newMenuItem;
    }
    
    public void addMenuPresenter(final MenuPresenter menuPresenter) {
        this.addMenuPresenter(menuPresenter, this.mContext);
    }
    
    public void addMenuPresenter(final MenuPresenter referent, final Context context) {
        this.mPresenters.add(new WeakReference<MenuPresenter>(referent));
        referent.initForMenu(context, this);
        this.mIsActionItemsStale = true;
    }
    
    public SubMenu addSubMenu(final int n) {
        return this.addSubMenu(0, 0, 0, this.mResources.getString(n));
    }
    
    public SubMenu addSubMenu(final int n, final int n2, final int n3, final int n4) {
        return this.addSubMenu(n, n2, n3, this.mResources.getString(n4));
    }
    
    public SubMenu addSubMenu(final int n, final int n2, final int n3, final CharSequence charSequence) {
        final MenuItemImpl menuItemImpl = (MenuItemImpl)this.addInternal(n, n2, n3, charSequence);
        final SubMenuBuilder subMenu = new SubMenuBuilder(this.mContext, this, menuItemImpl);
        menuItemImpl.setSubMenu(subMenu);
        return (SubMenu)subMenu;
    }
    
    public SubMenu addSubMenu(final CharSequence charSequence) {
        return this.addSubMenu(0, 0, 0, charSequence);
    }
    
    public void changeMenuMode() {
        if (this.mCallback != null) {
            this.mCallback.onMenuModeChange(this);
        }
    }
    
    public void clear() {
        if (this.mExpandedItem != null) {
            this.collapseItemActionView(this.mExpandedItem);
        }
        this.mItems.clear();
        this.onItemsChanged(true);
    }
    
    public void clearAll() {
        this.mPreventDispatchingItemsChanged = true;
        this.clear();
        this.clearHeader();
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.onItemsChanged(true);
    }
    
    public void clearHeader() {
        this.mHeaderIcon = null;
        this.mHeaderTitle = null;
        this.mHeaderView = null;
        this.onItemsChanged(false);
    }
    
    public void close() {
        this.close(true);
    }
    
    public final void close(final boolean b) {
        if (!this.mIsClosing) {
            this.mIsClosing = true;
            for (final WeakReference<MenuPresenter> o : this.mPresenters) {
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    menuPresenter.onCloseMenu(this, b);
                }
            }
            this.mIsClosing = false;
        }
    }
    
    public boolean collapseItemActionView(final MenuItemImpl menuItemImpl) {
        boolean b;
        if (this.mPresenters.isEmpty() || this.mExpandedItem != menuItemImpl) {
            b = false;
        }
        else {
            int collapseItemActionView = 0;
            this.stopDispatchingItemsChanged();
            final Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
            int n;
            while (true) {
                n = collapseItemActionView;
                if (!iterator.hasNext()) {
                    break;
                }
                final WeakReference<MenuPresenter> o = iterator.next();
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    n = (collapseItemActionView = (menuPresenter.collapseItemActionView(this, menuItemImpl) ? 1 : 0));
                    if (n != 0) {
                        break;
                    }
                    continue;
                }
            }
            this.startDispatchingItemsChanged();
            b = (n != 0);
            if (n != 0) {
                this.mExpandedItem = null;
                b = (n != 0);
            }
        }
        return b;
    }
    
    boolean dispatchMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        return this.mCallback != null && this.mCallback.onMenuItemSelected(menuBuilder, menuItem);
    }
    
    public boolean expandItemActionView(final MenuItemImpl mExpandedItem) {
        boolean b;
        if (this.mPresenters.isEmpty()) {
            b = false;
        }
        else {
            int expandItemActionView = 0;
            this.stopDispatchingItemsChanged();
            final Iterator<WeakReference<MenuPresenter>> iterator = this.mPresenters.iterator();
            int n;
            while (true) {
                n = expandItemActionView;
                if (!iterator.hasNext()) {
                    break;
                }
                final WeakReference<MenuPresenter> o = iterator.next();
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    n = (expandItemActionView = (menuPresenter.expandItemActionView(this, mExpandedItem) ? 1 : 0));
                    if (n != 0) {
                        break;
                    }
                    continue;
                }
            }
            this.startDispatchingItemsChanged();
            b = (n != 0);
            if (n != 0) {
                this.mExpandedItem = mExpandedItem;
                b = (n != 0);
            }
        }
        return b;
    }
    
    public int findGroupIndex(final int n) {
        return this.findGroupIndex(n, 0);
    }
    
    public int findGroupIndex(final int n, int i) {
        final int size = this.size();
        int n2 = i;
        if (i < 0) {
            n2 = 0;
        }
        for (i = n2; i < size; ++i) {
            if (this.mItems.get(i).getGroupId() == n) {
                return i;
            }
        }
        i = -1;
        return i;
    }
    
    public MenuItem findItem(final int n) {
        final int size = this.size();
        int i = 0;
        while (i < size) {
            Object item = this.mItems.get(i);
            if (((MenuItemImpl)item).getItemId() != n) {
                if (((MenuItemImpl)item).hasSubMenu()) {
                    item = ((MenuItemImpl)item).getSubMenu().findItem(n);
                    if (item != null) {
                        return (MenuItem)item;
                    }
                }
                ++i;
                continue;
            }
            return (MenuItem)item;
        }
        Object item = null;
        return (MenuItem)item;
    }
    
    public int findItemIndex(final int n) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.mItems.get(i).getItemId() == n) {
                return i;
            }
        }
        return -1;
    }
    
    MenuItemImpl findItemWithShortcutForKey(final int n, final KeyEvent keyEvent) {
        final MenuItemImpl menuItemImpl = null;
        final ArrayList<MenuItemImpl> mTempShortcutItemList = this.mTempShortcutItemList;
        mTempShortcutItemList.clear();
        this.findItemsWithShortcutForKey(mTempShortcutItemList, n, keyEvent);
        MenuItemImpl menuItemImpl2;
        if (mTempShortcutItemList.isEmpty()) {
            menuItemImpl2 = menuItemImpl;
        }
        else {
            final int metaState = keyEvent.getMetaState();
            final KeyCharacterMap$KeyData keyCharacterMap$KeyData = new KeyCharacterMap$KeyData();
            keyEvent.getKeyData(keyCharacterMap$KeyData);
            final int size = mTempShortcutItemList.size();
            if (size == 1) {
                menuItemImpl2 = mTempShortcutItemList.get(0);
            }
            else {
                final boolean qwertyMode = this.isQwertyMode();
                int index = 0;
                while (true) {
                    menuItemImpl2 = menuItemImpl;
                    if (index >= size) {
                        break;
                    }
                    menuItemImpl2 = mTempShortcutItemList.get(index);
                    char c;
                    if (qwertyMode) {
                        c = menuItemImpl2.getAlphabeticShortcut();
                    }
                    else {
                        c = menuItemImpl2.getNumericShortcut();
                    }
                    if ((c == keyCharacterMap$KeyData.meta[0] && (metaState & 0x2) == 0x0) || (c == keyCharacterMap$KeyData.meta[2] && (metaState & 0x2) != 0x0) || (qwertyMode && c == '\b' && n == 67)) {
                        break;
                    }
                    ++index;
                }
            }
        }
        return menuItemImpl2;
    }
    
    void findItemsWithShortcutForKey(final List<MenuItemImpl> list, final int n, final KeyEvent keyEvent) {
        final boolean qwertyMode = this.isQwertyMode();
        final int metaState = keyEvent.getMetaState();
        final KeyCharacterMap$KeyData keyCharacterMap$KeyData = new KeyCharacterMap$KeyData();
        if (keyEvent.getKeyData(keyCharacterMap$KeyData) || n == 67) {
            for (int size = this.mItems.size(), i = 0; i < size; ++i) {
                final MenuItemImpl menuItemImpl = this.mItems.get(i);
                if (menuItemImpl.hasSubMenu()) {
                    ((MenuBuilder)menuItemImpl.getSubMenu()).findItemsWithShortcutForKey(list, n, keyEvent);
                }
                char c;
                if (qwertyMode) {
                    c = menuItemImpl.getAlphabeticShortcut();
                }
                else {
                    c = menuItemImpl.getNumericShortcut();
                }
                if ((metaState & 0x5) == 0x0 && c != '\0' && (c == keyCharacterMap$KeyData.meta[0] || c == keyCharacterMap$KeyData.meta[2] || (qwertyMode && c == '\b' && n == 67)) && menuItemImpl.isEnabled()) {
                    list.add(menuItemImpl);
                }
            }
        }
    }
    
    public void flagActionItems() {
        final ArrayList<MenuItemImpl> visibleItems = this.getVisibleItems();
        if (this.mIsActionItemsStale) {
            boolean b = false;
            for (final WeakReference<MenuPresenter> o : this.mPresenters) {
                final MenuPresenter menuPresenter = o.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(o);
                }
                else {
                    b |= menuPresenter.flagActionItems();
                }
            }
            if (b) {
                this.mActionItems.clear();
                this.mNonActionItems.clear();
                for (int size = visibleItems.size(), i = 0; i < size; ++i) {
                    final MenuItemImpl menuItemImpl = visibleItems.get(i);
                    if (menuItemImpl.isActionButton()) {
                        this.mActionItems.add(menuItemImpl);
                    }
                    else {
                        this.mNonActionItems.add(menuItemImpl);
                    }
                }
            }
            else {
                this.mActionItems.clear();
                this.mNonActionItems.clear();
                this.mNonActionItems.addAll(this.getVisibleItems());
            }
            this.mIsActionItemsStale = false;
        }
    }
    
    public ArrayList<MenuItemImpl> getActionItems() {
        this.flagActionItems();
        return this.mActionItems;
    }
    
    protected String getActionViewStatesKey() {
        return "android:menu:actionviewstates";
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public MenuItemImpl getExpandedItem() {
        return this.mExpandedItem;
    }
    
    public Drawable getHeaderIcon() {
        return this.mHeaderIcon;
    }
    
    public CharSequence getHeaderTitle() {
        return this.mHeaderTitle;
    }
    
    public View getHeaderView() {
        return this.mHeaderView;
    }
    
    public MenuItem getItem(final int index) {
        return (MenuItem)this.mItems.get(index);
    }
    
    public ArrayList<MenuItemImpl> getNonActionItems() {
        this.flagActionItems();
        return this.mNonActionItems;
    }
    
    boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }
    
    Resources getResources() {
        return this.mResources;
    }
    
    public MenuBuilder getRootMenu() {
        return this;
    }
    
    public ArrayList<MenuItemImpl> getVisibleItems() {
        ArrayList<MenuItemImpl> list;
        if (!this.mIsVisibleItemsStale) {
            list = this.mVisibleItems;
        }
        else {
            this.mVisibleItems.clear();
            for (int size = this.mItems.size(), i = 0; i < size; ++i) {
                final MenuItemImpl e = this.mItems.get(i);
                if (e.isVisible()) {
                    this.mVisibleItems.add(e);
                }
            }
            this.mIsVisibleItemsStale = false;
            this.mIsActionItemsStale = true;
            list = this.mVisibleItems;
        }
        return list;
    }
    
    public boolean hasVisibleItems() {
        final boolean b = true;
        boolean b2;
        if (this.mOverrideVisibleItems) {
            b2 = b;
        }
        else {
            for (int size = this.size(), i = 0; i < size; ++i) {
                b2 = b;
                if (this.mItems.get(i).isVisible()) {
                    return b2;
                }
            }
            b2 = false;
        }
        return b2;
    }
    
    boolean isQwertyMode() {
        return this.mQwertyMode;
    }
    
    public boolean isShortcutKey(final int n, final KeyEvent keyEvent) {
        return this.findItemWithShortcutForKey(n, keyEvent) != null;
    }
    
    public boolean isShortcutsVisible() {
        return this.mShortcutsVisible;
    }
    
    void onItemActionRequestChanged(final MenuItemImpl menuItemImpl) {
        this.onItemsChanged(this.mIsActionItemsStale = true);
    }
    
    void onItemVisibleChanged(final MenuItemImpl menuItemImpl) {
        this.onItemsChanged(this.mIsVisibleItemsStale = true);
    }
    
    public void onItemsChanged(final boolean b) {
        if (!this.mPreventDispatchingItemsChanged) {
            if (b) {
                this.mIsVisibleItemsStale = true;
                this.mIsActionItemsStale = true;
            }
            this.dispatchPresenterUpdate(b);
        }
        else {
            this.mItemsChangedWhileDispatchPrevented = true;
        }
    }
    
    public boolean performIdentifierAction(final int n, final int n2) {
        return this.performItemAction(this.findItem(n), n2);
    }
    
    public boolean performItemAction(final MenuItem menuItem, final int n) {
        return this.performItemAction(menuItem, null, n);
    }
    
    public boolean performItemAction(final MenuItem menuItem, final MenuPresenter menuPresenter, final int n) {
        final MenuItemImpl menuItemImpl = (MenuItemImpl)menuItem;
        boolean b;
        if (menuItemImpl == null || !menuItemImpl.isEnabled()) {
            b = false;
        }
        else {
            final boolean invoke = menuItemImpl.invoke();
            final ActionProvider supportActionProvider = menuItemImpl.getSupportActionProvider();
            final boolean b2 = supportActionProvider != null && supportActionProvider.hasSubMenu();
            if (menuItemImpl.hasCollapsibleActionView()) {
                final boolean b3 = b = (invoke | menuItemImpl.expandActionView());
                if (b3) {
                    this.close(true);
                    b = b3;
                }
            }
            else if (menuItemImpl.hasSubMenu() || b2) {
                this.close(false);
                if (!menuItemImpl.hasSubMenu()) {
                    menuItemImpl.setSubMenu(new SubMenuBuilder(this.getContext(), this, menuItemImpl));
                }
                final SubMenuBuilder subMenuBuilder = (SubMenuBuilder)menuItemImpl.getSubMenu();
                if (b2) {
                    supportActionProvider.onPrepareSubMenu((SubMenu)subMenuBuilder);
                }
                final boolean b4 = b = (invoke | this.dispatchSubMenuSelected(subMenuBuilder, menuPresenter));
                if (!b4) {
                    this.close(true);
                    b = b4;
                }
            }
            else {
                b = invoke;
                if ((n & 0x1) == 0x0) {
                    this.close(true);
                    b = invoke;
                }
            }
        }
        return b;
    }
    
    public boolean performShortcut(final int n, final KeyEvent keyEvent, final int n2) {
        final MenuItemImpl itemWithShortcutForKey = this.findItemWithShortcutForKey(n, keyEvent);
        boolean performItemAction = false;
        if (itemWithShortcutForKey != null) {
            performItemAction = this.performItemAction((MenuItem)itemWithShortcutForKey, n2);
        }
        if ((n2 & 0x2) != 0x0) {
            this.close(true);
        }
        return performItemAction;
    }
    
    public void removeGroup(final int n) {
        final int groupIndex = this.findGroupIndex(n);
        if (groupIndex >= 0) {
            for (int size = this.mItems.size(), n2 = 0; n2 < size - groupIndex && this.mItems.get(groupIndex).getGroupId() == n; ++n2) {
                this.removeItemAtInt(groupIndex, false);
            }
            this.onItemsChanged(true);
        }
    }
    
    public void removeItem(final int n) {
        this.removeItemAtInt(this.findItemIndex(n), true);
    }
    
    public void removeItemAt(final int n) {
        this.removeItemAtInt(n, true);
    }
    
    public void removeMenuPresenter(final MenuPresenter menuPresenter) {
        for (final WeakReference<MenuPresenter> o : this.mPresenters) {
            final MenuPresenter menuPresenter2 = o.get();
            if (menuPresenter2 == null || menuPresenter2 == menuPresenter) {
                this.mPresenters.remove(o);
            }
        }
    }
    
    public void restoreActionViewStates(final Bundle bundle) {
        if (bundle != null) {
            final SparseArray sparseParcelableArray = bundle.getSparseParcelableArray(this.getActionViewStatesKey());
            for (int size = this.size(), i = 0; i < size; ++i) {
                final MenuItem item = this.getItem(i);
                final View actionView = MenuItemCompat.getActionView(item);
                if (actionView != null && actionView.getId() != -1) {
                    actionView.restoreHierarchyState(sparseParcelableArray);
                }
                if (item.hasSubMenu()) {
                    ((SubMenuBuilder)item.getSubMenu()).restoreActionViewStates(bundle);
                }
            }
            final int int1 = bundle.getInt("android:menu:expandedactionview");
            if (int1 > 0) {
                final MenuItem item2 = this.findItem(int1);
                if (item2 != null) {
                    MenuItemCompat.expandActionView(item2);
                }
            }
        }
    }
    
    public void restorePresenterStates(final Bundle bundle) {
        this.dispatchRestoreInstanceState(bundle);
    }
    
    public void saveActionViewStates(final Bundle bundle) {
        SparseArray sparseArray = null;
        SparseArray sparseArray2;
        for (int size = this.size(), i = 0; i < size; ++i, sparseArray = sparseArray2) {
            final MenuItem item = this.getItem(i);
            final View actionView = MenuItemCompat.getActionView(item);
            sparseArray2 = sparseArray;
            if (actionView != null) {
                sparseArray2 = sparseArray;
                if (actionView.getId() != -1) {
                    SparseArray sparseArray3;
                    if ((sparseArray3 = sparseArray) == null) {
                        sparseArray3 = new SparseArray();
                    }
                    actionView.saveHierarchyState(sparseArray3);
                    sparseArray2 = sparseArray3;
                    if (MenuItemCompat.isActionViewExpanded(item)) {
                        bundle.putInt("android:menu:expandedactionview", item.getItemId());
                        sparseArray2 = sparseArray3;
                    }
                }
            }
            if (item.hasSubMenu()) {
                ((SubMenuBuilder)item.getSubMenu()).saveActionViewStates(bundle);
            }
        }
        if (sparseArray != null) {
            bundle.putSparseParcelableArray(this.getActionViewStatesKey(), sparseArray);
        }
    }
    
    public void savePresenterStates(final Bundle bundle) {
        this.dispatchSaveInstanceState(bundle);
    }
    
    public void setCallback(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    public void setCurrentMenuInfo(final ContextMenu$ContextMenuInfo mCurrentMenuInfo) {
        this.mCurrentMenuInfo = mCurrentMenuInfo;
    }
    
    public MenuBuilder setDefaultShowAsAction(final int mDefaultShowAsAction) {
        this.mDefaultShowAsAction = mDefaultShowAsAction;
        return this;
    }
    
    void setExclusiveItemChecked(final MenuItem menuItem) {
        final int groupId = menuItem.getGroupId();
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == groupId && menuItemImpl.isExclusiveCheckable() && menuItemImpl.isCheckable()) {
                menuItemImpl.setCheckedInt(menuItemImpl == menuItem);
            }
        }
    }
    
    public void setGroupCheckable(final int n, final boolean checkable, final boolean exclusiveCheckable) {
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == n) {
                menuItemImpl.setExclusiveCheckable(exclusiveCheckable);
                menuItemImpl.setCheckable(checkable);
            }
        }
    }
    
    public void setGroupEnabled(final int n, final boolean enabled) {
        for (int size = this.mItems.size(), i = 0; i < size; ++i) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == n) {
                menuItemImpl.setEnabled(enabled);
            }
        }
    }
    
    public void setGroupVisible(final int n, final boolean visibleInt) {
        final int size = this.mItems.size();
        int n2 = 0;
        int n3;
        for (int i = 0; i < size; ++i, n2 = n3) {
            final MenuItemImpl menuItemImpl = this.mItems.get(i);
            n3 = n2;
            if (menuItemImpl.getGroupId() == n) {
                n3 = n2;
                if (menuItemImpl.setVisibleInt(visibleInt)) {
                    n3 = 1;
                }
            }
        }
        if (n2 != 0) {
            this.onItemsChanged(true);
        }
    }
    
    protected MenuBuilder setHeaderIconInt(final int n) {
        this.setHeaderInternal(0, null, n, null, null);
        return this;
    }
    
    protected MenuBuilder setHeaderIconInt(final Drawable drawable) {
        this.setHeaderInternal(0, null, 0, drawable, null);
        return this;
    }
    
    protected MenuBuilder setHeaderTitleInt(final int n) {
        this.setHeaderInternal(n, null, 0, null, null);
        return this;
    }
    
    protected MenuBuilder setHeaderTitleInt(final CharSequence charSequence) {
        this.setHeaderInternal(0, charSequence, 0, null, null);
        return this;
    }
    
    protected MenuBuilder setHeaderViewInt(final View view) {
        this.setHeaderInternal(0, null, 0, null, view);
        return this;
    }
    
    void setOptionalIconsVisible(final boolean mOptionalIconsVisible) {
        this.mOptionalIconsVisible = mOptionalIconsVisible;
    }
    
    public void setOverrideVisibleItems(final boolean mOverrideVisibleItems) {
        this.mOverrideVisibleItems = mOverrideVisibleItems;
    }
    
    public void setQwertyMode(final boolean mQwertyMode) {
        this.mQwertyMode = mQwertyMode;
        this.onItemsChanged(false);
    }
    
    public void setShortcutsVisible(final boolean shortcutsVisibleInner) {
        if (this.mShortcutsVisible != shortcutsVisibleInner) {
            this.setShortcutsVisibleInner(shortcutsVisibleInner);
            this.onItemsChanged(false);
        }
    }
    
    public int size() {
        return this.mItems.size();
    }
    
    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            this.onItemsChanged(true);
        }
    }
    
    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
        }
    }
    
    public interface Callback
    {
        boolean onMenuItemSelected(final MenuBuilder p0, final MenuItem p1);
        
        void onMenuModeChange(final MenuBuilder p0);
    }
    
    public interface ItemInvoker
    {
        boolean invokeItem(final MenuItemImpl p0);
    }
}
