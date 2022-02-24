// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.app;

import android.support.v4.os.ParcelableCompat;
import android.os.Parcel;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.os.Parcelable$Creator;
import android.os.Parcelable;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.MotionEvent;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.widget.ViewStubCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.res.Configuration;
import android.support.v4.view.LayoutInflaterCompat;
import android.app.Dialog;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater$Factory;
import android.util.AttributeSet;
import android.view.ViewGroup$MarginLayoutParams;
import android.util.AndroidRuntimeException;
import android.view.KeyCharacterMap;
import android.view.ViewParent;
import android.view.WindowManager$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.util.Log;
import android.media.AudioManager;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import android.content.res.Resources$Theme;
import android.support.v7.view.menu.MenuPresenter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.support.v7.widget.ViewUtils;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.os.Build$VERSION;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.Window$Callback;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ContentFrameLayout;
import android.view.Menu;
import android.view.Window;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.View;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.ActionBarContextView;
import android.widget.PopupWindow;
import android.support.v7.view.ActionMode;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.view.menu.MenuBuilder;

class AppCompatDelegateImplV7 extends AppCompatDelegateImplBase implements Callback, LayoutInflaterFactory
{
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    ActionMode mActionMode;
    PopupWindow mActionModePopup;
    ActionBarContextView mActionModeView;
    private AppCompatViewInflater mAppCompatViewInflater;
    private boolean mClosingActionMenu;
    private DecorContentParent mDecorContentParent;
    private boolean mEnableDefaultActionBarUp;
    ViewPropertyAnimatorCompat mFadeAnim;
    private boolean mFeatureIndeterminateProgress;
    private boolean mFeatureProgress;
    private int mInvalidatePanelMenuFeatures;
    private boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable;
    private boolean mLongPressBackDown;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    Runnable mShowActionModePopup;
    private View mStatusGuard;
    private ViewGroup mSubDecor;
    private boolean mSubDecorInstalled;
    private Rect mTempRect1;
    private Rect mTempRect2;
    private TextView mTitleView;
    
    AppCompatDelegateImplV7(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
        this.mFadeAnim = null;
        this.mInvalidatePanelMenuRunnable = new Runnable() {
            @Override
            public void run() {
                if ((AppCompatDelegateImplV7.this.mInvalidatePanelMenuFeatures & 0x1) != 0x0) {
                    AppCompatDelegateImplV7.this.doInvalidatePanelMenu(0);
                }
                if ((AppCompatDelegateImplV7.this.mInvalidatePanelMenuFeatures & 0x1000) != 0x0) {
                    AppCompatDelegateImplV7.this.doInvalidatePanelMenu(108);
                }
                AppCompatDelegateImplV7.this.mInvalidatePanelMenuPosted = false;
                AppCompatDelegateImplV7.this.mInvalidatePanelMenuFeatures = 0;
            }
        };
    }
    
    private void applyFixedSizeWindow() {
        final ContentFrameLayout contentFrameLayout = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
        final View decorView = this.mWindow.getDecorView();
        contentFrameLayout.setDecorPadding(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, contentFrameLayout.getMinWidthMajor());
        obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, contentFrameLayout.getMinWidthMinor());
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, contentFrameLayout.getFixedWidthMajor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, contentFrameLayout.getFixedWidthMinor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, contentFrameLayout.getFixedHeightMajor());
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
            obtainStyledAttributes.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, contentFrameLayout.getFixedHeightMinor());
        }
        obtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }
    
    private void callOnPanelClosed(final int n, final PanelFeatureState panelFeatureState, final Menu menu) {
        PanelFeatureState panelFeatureState2 = panelFeatureState;
        Object menu2 = menu;
        if (menu == null) {
            PanelFeatureState panelFeatureState3;
            if ((panelFeatureState3 = panelFeatureState) == null) {
                panelFeatureState3 = panelFeatureState;
                if (n >= 0) {
                    panelFeatureState3 = panelFeatureState;
                    if (n < this.mPanels.length) {
                        panelFeatureState3 = this.mPanels[n];
                    }
                }
            }
            panelFeatureState2 = panelFeatureState3;
            menu2 = menu;
            if (panelFeatureState3 != null) {
                menu2 = panelFeatureState3.menu;
                panelFeatureState2 = panelFeatureState3;
            }
        }
        if ((panelFeatureState2 == null || panelFeatureState2.isOpen) && !this.isDestroyed()) {
            this.mOriginalWindowCallback.onPanelClosed(n, (Menu)menu2);
        }
    }
    
    private void checkCloseActionMenu(final MenuBuilder menuBuilder) {
        if (!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            this.mDecorContentParent.dismissPopups();
            final Window$Callback windowCallback = this.getWindowCallback();
            if (windowCallback != null && !this.isDestroyed()) {
                windowCallback.onPanelClosed(108, (Menu)menuBuilder);
            }
            this.mClosingActionMenu = false;
        }
    }
    
    private void closePanel(final int n) {
        this.closePanel(this.getPanelState(n, true), true);
    }
    
    private void closePanel(final PanelFeatureState panelFeatureState, final boolean b) {
        if (b && panelFeatureState.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
            this.checkCloseActionMenu(panelFeatureState.menu);
        }
        else {
            final WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
            if (windowManager != null && panelFeatureState.isOpen && panelFeatureState.decorView != null) {
                windowManager.removeView((View)panelFeatureState.decorView);
                if (b) {
                    this.callOnPanelClosed(panelFeatureState.featureId, panelFeatureState, null);
                }
            }
            panelFeatureState.isPrepared = false;
            panelFeatureState.isHandled = false;
            panelFeatureState.isOpen = false;
            panelFeatureState.shownPanelView = null;
            panelFeatureState.refreshDecorView = true;
            if (this.mPreparedPanel == panelFeatureState) {
                this.mPreparedPanel = null;
            }
        }
    }
    
    private ViewGroup createSubDecor() {
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
        if (!obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
            this.requestWindowFeature(1);
        }
        else if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
            this.requestWindowFeature(108);
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            this.requestWindowFeature(109);
        }
        if (obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            this.requestWindowFeature(10);
        }
        this.mIsFloating = obtainStyledAttributes.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
        obtainStyledAttributes.recycle();
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        ViewGroup contentView = null;
        if (!this.mWindowNoTitle) {
            if (this.mIsFloating) {
                contentView = (ViewGroup)from.inflate(R.layout.abc_dialog_title_material, (ViewGroup)null);
                this.mOverlayActionBar = false;
                this.mHasActionBar = false;
            }
            else if (this.mHasActionBar) {
                final TypedValue typedValue = new TypedValue();
                this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                Object mContext;
                if (typedValue.resourceId != 0) {
                    mContext = new ContextThemeWrapper(this.mContext, typedValue.resourceId);
                }
                else {
                    mContext = this.mContext;
                }
                final ViewGroup viewGroup = (ViewGroup)LayoutInflater.from((Context)mContext).inflate(R.layout.abc_screen_toolbar, (ViewGroup)null);
                (this.mDecorContentParent = (DecorContentParent)viewGroup.findViewById(R.id.decor_content_parent)).setWindowCallback(this.getWindowCallback());
                if (this.mOverlayActionBar) {
                    this.mDecorContentParent.initFeature(109);
                }
                if (this.mFeatureProgress) {
                    this.mDecorContentParent.initFeature(2);
                }
                contentView = viewGroup;
                if (this.mFeatureIndeterminateProgress) {
                    this.mDecorContentParent.initFeature(5);
                    contentView = viewGroup;
                }
            }
        }
        else {
            if (this.mOverlayActionMode) {
                contentView = (ViewGroup)from.inflate(R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
            }
            else {
                contentView = (ViewGroup)from.inflate(R.layout.abc_screen_simple, (ViewGroup)null);
            }
            if (Build$VERSION.SDK_INT >= 21) {
                ViewCompat.setOnApplyWindowInsetsListener((View)contentView, new OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
                        final int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
                        final int access$300 = AppCompatDelegateImplV7.this.updateStatusGuard(systemWindowInsetTop);
                        WindowInsetsCompat replaceSystemWindowInsets = windowInsetsCompat;
                        if (systemWindowInsetTop != access$300) {
                            replaceSystemWindowInsets = windowInsetsCompat.replaceSystemWindowInsets(windowInsetsCompat.getSystemWindowInsetLeft(), access$300, windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                        }
                        return ViewCompat.onApplyWindowInsets(view, replaceSystemWindowInsets);
                    }
                });
            }
            else {
                ((FitWindowsViewGroup)contentView).setOnFitSystemWindowsListener((FitWindowsViewGroup.OnFitSystemWindowsListener)new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                    @Override
                    public void onFitSystemWindows(final Rect rect) {
                        rect.top = AppCompatDelegateImplV7.this.updateStatusGuard(rect.top);
                    }
                });
            }
        }
        if (contentView == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.mHasActionBar + ", windowActionBarOverlay: " + this.mOverlayActionBar + ", android:windowIsFloating: " + this.mIsFloating + ", windowActionModeOverlay: " + this.mOverlayActionMode + ", windowNoTitle: " + this.mWindowNoTitle + " }");
        }
        if (this.mDecorContentParent == null) {
            this.mTitleView = (TextView)contentView.findViewById(R.id.title);
        }
        ViewUtils.makeOptionalFitsSystemWindows((View)contentView);
        final ViewGroup viewGroup2 = (ViewGroup)this.mWindow.findViewById(16908290);
        final ContentFrameLayout contentFrameLayout = (ContentFrameLayout)contentView.findViewById(R.id.action_bar_activity_content);
        while (viewGroup2.getChildCount() > 0) {
            final View child = viewGroup2.getChildAt(0);
            viewGroup2.removeViewAt(0);
            contentFrameLayout.addView(child);
        }
        this.mWindow.setContentView((View)contentView);
        viewGroup2.setId(-1);
        contentFrameLayout.setId(16908290);
        if (viewGroup2 instanceof FrameLayout) {
            ((FrameLayout)viewGroup2).setForeground((Drawable)null);
        }
        contentFrameLayout.setAttachListener((ContentFrameLayout.OnAttachListener)new ContentFrameLayout.OnAttachListener() {
            @Override
            public void onAttachedFromWindow() {
            }
            
            @Override
            public void onDetachedFromWindow() {
                AppCompatDelegateImplV7.this.dismissPopups();
            }
        });
        return contentView;
    }
    
    private void dismissPopups() {
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.dismissPopups();
        }
        Label_0060: {
            if (this.mActionModePopup == null) {
                break Label_0060;
            }
            this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
            while (true) {
                if (!this.mActionModePopup.isShowing()) {
                    break Label_0055;
                }
                try {
                    this.mActionModePopup.dismiss();
                    this.mActionModePopup = null;
                    this.endOnGoingFadeAnimation();
                    final PanelFeatureState panelState = this.getPanelState(0, false);
                    if (panelState != null && panelState.menu != null) {
                        panelState.menu.close();
                    }
                }
                catch (IllegalArgumentException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    private void doInvalidatePanelMenu(final int n) {
        final PanelFeatureState panelState = this.getPanelState(n, true);
        if (panelState.menu != null) {
            final Bundle frozenActionViewState = new Bundle();
            panelState.menu.saveActionViewStates(frozenActionViewState);
            if (frozenActionViewState.size() > 0) {
                panelState.frozenActionViewState = frozenActionViewState;
            }
            panelState.menu.stopDispatchingItemsChanged();
            panelState.menu.clear();
        }
        panelState.refreshMenuContent = true;
        panelState.refreshDecorView = true;
        if ((n == 108 || n == 0) && this.mDecorContentParent != null) {
            final PanelFeatureState panelState2 = this.getPanelState(0, false);
            if (panelState2 != null) {
                panelState2.isPrepared = false;
                this.preparePanel(panelState2, null);
            }
        }
    }
    
    private void endOnGoingFadeAnimation() {
        if (this.mFadeAnim != null) {
            this.mFadeAnim.cancel();
        }
    }
    
    private void ensureSubDecor() {
        if (!this.mSubDecorInstalled) {
            this.mSubDecor = this.createSubDecor();
            final CharSequence title = this.getTitle();
            if (!TextUtils.isEmpty(title)) {
                this.onTitleChanged(title);
            }
            this.applyFixedSizeWindow();
            this.onSubDecorInstalled(this.mSubDecor);
            this.mSubDecorInstalled = true;
            final PanelFeatureState panelState = this.getPanelState(0, false);
            if (!this.isDestroyed() && (panelState == null || panelState.menu == null)) {
                this.invalidatePanelMenu(108);
            }
        }
    }
    
    private PanelFeatureState findMenuPanel(final Menu menu) {
        final PanelFeatureState[] mPanels = this.mPanels;
        if (mPanels != null) {
            final int length = mPanels.length;
        }
        else {
            final int length = 0;
        }
        for (final PanelFeatureState panelFeatureState : mPanels) {
            if (panelFeatureState != null && panelFeatureState.menu == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }
    
    private PanelFeatureState getPanelState(final int n, final boolean b) {
        final PanelFeatureState[] mPanels = this.mPanels;
        PanelFeatureState[] array = null;
        Label_0050: {
            if (mPanels != null) {
                array = mPanels;
                if (mPanels.length > n) {
                    break Label_0050;
                }
            }
            final PanelFeatureState[] mPanels2 = new PanelFeatureState[n + 1];
            if (mPanels != null) {
                System.arraycopy(mPanels, 0, mPanels2, 0, mPanels.length);
            }
            array = mPanels2;
            this.mPanels = mPanels2;
        }
        PanelFeatureState panelFeatureState;
        if ((panelFeatureState = array[n]) == null) {
            panelFeatureState = new PanelFeatureState(n);
            array[n] = panelFeatureState;
        }
        return panelFeatureState;
    }
    
    private boolean initializePanelContent(final PanelFeatureState panelFeatureState) {
        boolean b = true;
        if (panelFeatureState.createdPanelView != null) {
            panelFeatureState.shownPanelView = panelFeatureState.createdPanelView;
        }
        else if (panelFeatureState.menu == null) {
            b = false;
        }
        else {
            if (this.mPanelMenuPresenterCallback == null) {
                this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
            }
            panelFeatureState.shownPanelView = (View)panelFeatureState.getListMenuView(this.mPanelMenuPresenterCallback);
            if (panelFeatureState.shownPanelView == null) {
                b = false;
            }
        }
        return b;
    }
    
    private boolean initializePanelDecor(final PanelFeatureState panelFeatureState) {
        panelFeatureState.setStyle(this.getActionBarThemedContext());
        panelFeatureState.decorView = (ViewGroup)new ListMenuDecorView(panelFeatureState.listPresenterContext);
        panelFeatureState.gravity = 81;
        return true;
    }
    
    private boolean initializePanelMenu(final PanelFeatureState panelFeatureState) {
        final Context mContext = this.mContext;
        Object o = null;
        Label_0175: {
            if (panelFeatureState.featureId != 0) {
                o = mContext;
                if (panelFeatureState.featureId != 108) {
                    break Label_0175;
                }
            }
            o = mContext;
            if (this.mDecorContentParent != null) {
                final TypedValue typedValue = new TypedValue();
                final Resources$Theme theme = mContext.getTheme();
                theme.resolveAttribute(R.attr.actionBarTheme, typedValue, true);
                Resources$Theme theme2 = null;
                if (typedValue.resourceId != 0) {
                    theme2 = mContext.getResources().newTheme();
                    theme2.setTo(theme);
                    theme2.applyStyle(typedValue.resourceId, true);
                    theme2.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                else {
                    theme.resolveAttribute(R.attr.actionBarWidgetTheme, typedValue, true);
                }
                Resources$Theme theme3 = theme2;
                if (typedValue.resourceId != 0) {
                    if ((theme3 = theme2) == null) {
                        theme3 = mContext.getResources().newTheme();
                        theme3.setTo(theme);
                    }
                    theme3.applyStyle(typedValue.resourceId, true);
                }
                o = mContext;
                if (theme3 != null) {
                    o = new ContextThemeWrapper(mContext, 0);
                    ((Context)o).getTheme().setTo(theme3);
                }
            }
        }
        final MenuBuilder menu = new MenuBuilder((Context)o);
        menu.setCallback((MenuBuilder.Callback)this);
        panelFeatureState.setMenu(menu);
        return true;
    }
    
    private void invalidatePanelMenu(final int n) {
        this.mInvalidatePanelMenuFeatures |= 1 << n;
        if (!this.mInvalidatePanelMenuPosted) {
            ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }
    
    private boolean onKeyDownPanel(final int n, final KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() != 0) {
            return false;
        }
        final PanelFeatureState panelState = this.getPanelState(n, true);
        if (panelState.isOpen) {
            return false;
        }
        return this.preparePanel(panelState, keyEvent);
        preparePanel = false;
        return preparePanel;
    }
    
    private boolean onKeyUpPanel(final int n, final KeyEvent keyEvent) {
        boolean b;
        if (this.mActionMode != null) {
            b = false;
        }
        else {
            final boolean b2 = false;
            final PanelFeatureState panelState = this.getPanelState(n, true);
            boolean b3;
            if (n == 0 && this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && !ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext))) {
                if (!this.mDecorContentParent.isOverflowMenuShowing()) {
                    b3 = b2;
                    if (!this.isDestroyed()) {
                        b3 = b2;
                        if (this.preparePanel(panelState, keyEvent)) {
                            b3 = this.mDecorContentParent.showOverflowMenu();
                        }
                    }
                }
                else {
                    b3 = this.mDecorContentParent.hideOverflowMenu();
                }
            }
            else if (panelState.isOpen || panelState.isHandled) {
                b3 = panelState.isOpen;
                this.closePanel(panelState, true);
            }
            else {
                b3 = b2;
                if (panelState.isPrepared) {
                    boolean preparePanel = true;
                    if (panelState.refreshMenuContent) {
                        panelState.isPrepared = false;
                        preparePanel = this.preparePanel(panelState, keyEvent);
                    }
                    b3 = b2;
                    if (preparePanel) {
                        this.openPanel(panelState, keyEvent);
                        b3 = true;
                    }
                }
            }
            if (b = b3) {
                final AudioManager audioManager = (AudioManager)this.mContext.getSystemService("audio");
                if (audioManager != null) {
                    audioManager.playSoundEffect(0);
                    b = b3;
                }
                else {
                    Log.w("AppCompatDelegate", "Couldn't get audio manager");
                    b = b3;
                }
            }
        }
        return b;
    }
    
    private void openPanel(final PanelFeatureState panelFeatureState, final KeyEvent keyEvent) {
        if (!panelFeatureState.isOpen && !this.isDestroyed()) {
            if (panelFeatureState.featureId == 0) {
                final Context mContext = this.mContext;
                boolean b;
                if ((mContext.getResources().getConfiguration().screenLayout & 0xF) == 0x4) {
                    b = true;
                }
                else {
                    b = false;
                }
                boolean b2;
                if (mContext.getApplicationInfo().targetSdkVersion >= 11) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                if (b && b2) {
                    return;
                }
            }
            final Window$Callback windowCallback = this.getWindowCallback();
            if (windowCallback != null && !windowCallback.onMenuOpened(panelFeatureState.featureId, (Menu)panelFeatureState.menu)) {
                this.closePanel(panelFeatureState, true);
            }
            else {
                final WindowManager windowManager = (WindowManager)this.mContext.getSystemService("window");
                if (windowManager != null && this.preparePanel(panelFeatureState, keyEvent)) {
                    final int n = -2;
                    int n2;
                    if (panelFeatureState.decorView == null || panelFeatureState.refreshDecorView) {
                        if (panelFeatureState.decorView == null) {
                            if (!this.initializePanelDecor(panelFeatureState) || panelFeatureState.decorView == null) {
                                return;
                            }
                        }
                        else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                            panelFeatureState.decorView.removeAllViews();
                        }
                        if (!this.initializePanelContent(panelFeatureState) || !panelFeatureState.hasPanelItems()) {
                            return;
                        }
                        ViewGroup$LayoutParams layoutParams;
                        if ((layoutParams = panelFeatureState.shownPanelView.getLayoutParams()) == null) {
                            layoutParams = new ViewGroup$LayoutParams(-2, -2);
                        }
                        panelFeatureState.decorView.setBackgroundResource(panelFeatureState.background);
                        final ViewParent parent = panelFeatureState.shownPanelView.getParent();
                        if (parent != null && parent instanceof ViewGroup) {
                            ((ViewGroup)parent).removeView(panelFeatureState.shownPanelView);
                        }
                        panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, layoutParams);
                        n2 = n;
                        if (!panelFeatureState.shownPanelView.hasFocus()) {
                            panelFeatureState.shownPanelView.requestFocus();
                            n2 = n;
                        }
                    }
                    else {
                        n2 = n;
                        if (panelFeatureState.createdPanelView != null) {
                            final ViewGroup$LayoutParams layoutParams2 = panelFeatureState.createdPanelView.getLayoutParams();
                            n2 = n;
                            if (layoutParams2 != null) {
                                n2 = n;
                                if (layoutParams2.width == -1) {
                                    n2 = -1;
                                }
                            }
                        }
                    }
                    panelFeatureState.isHandled = false;
                    final WindowManager$LayoutParams windowManager$LayoutParams = new WindowManager$LayoutParams(n2, -2, panelFeatureState.x, panelFeatureState.y, 1002, 8519680, -3);
                    windowManager$LayoutParams.gravity = panelFeatureState.gravity;
                    windowManager$LayoutParams.windowAnimations = panelFeatureState.windowAnimations;
                    windowManager.addView((View)panelFeatureState.decorView, (ViewGroup$LayoutParams)windowManager$LayoutParams);
                    panelFeatureState.isOpen = true;
                }
            }
        }
    }
    
    private boolean performPanelShortcut(final PanelFeatureState panelFeatureState, final int n, final KeyEvent keyEvent, final int n2) {
        boolean b;
        if (keyEvent.isSystem()) {
            b = false;
        }
        else {
            final boolean b2 = false;
            boolean performShortcut = false;
            Label_0060: {
                if (!panelFeatureState.isPrepared) {
                    performShortcut = b2;
                    if (!this.preparePanel(panelFeatureState, keyEvent)) {
                        break Label_0060;
                    }
                }
                performShortcut = b2;
                if (panelFeatureState.menu != null) {
                    performShortcut = panelFeatureState.menu.performShortcut(n, keyEvent, n2);
                }
            }
            b = performShortcut;
            if (performShortcut) {
                b = performShortcut;
                if ((n2 & 0x1) == 0x0) {
                    b = performShortcut;
                    if (this.mDecorContentParent == null) {
                        this.closePanel(panelFeatureState, true);
                        b = performShortcut;
                    }
                }
            }
        }
        return b;
    }
    
    private boolean preparePanel(final PanelFeatureState mPreparedPanel, final KeyEvent keyEvent) {
        final boolean b = false;
        boolean b2;
        if (this.isDestroyed()) {
            b2 = b;
        }
        else if (mPreparedPanel.isPrepared) {
            b2 = true;
        }
        else {
            if (this.mPreparedPanel != null && this.mPreparedPanel != mPreparedPanel) {
                this.closePanel(this.mPreparedPanel, false);
            }
            final Window$Callback windowCallback = this.getWindowCallback();
            if (windowCallback != null) {
                mPreparedPanel.createdPanelView = windowCallback.onCreatePanelView(mPreparedPanel.featureId);
            }
            boolean b3;
            if (mPreparedPanel.featureId == 0 || mPreparedPanel.featureId == 108) {
                b3 = true;
            }
            else {
                b3 = false;
            }
            if (b3 && this.mDecorContentParent != null) {
                this.mDecorContentParent.setMenuPrepared();
            }
            if (mPreparedPanel.createdPanelView == null && (!b3 || !(this.peekSupportActionBar() instanceof ToolbarActionBar))) {
                if (mPreparedPanel.menu == null || mPreparedPanel.refreshMenuContent) {
                    if (mPreparedPanel.menu == null) {
                        b2 = b;
                        if (!this.initializePanelMenu(mPreparedPanel)) {
                            return b2;
                        }
                        b2 = b;
                        if (mPreparedPanel.menu == null) {
                            return b2;
                        }
                    }
                    if (b3 && this.mDecorContentParent != null) {
                        if (this.mActionMenuPresenterCallback == null) {
                            this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                        }
                        this.mDecorContentParent.setMenu((Menu)mPreparedPanel.menu, this.mActionMenuPresenterCallback);
                    }
                    mPreparedPanel.menu.stopDispatchingItemsChanged();
                    if (!windowCallback.onCreatePanelMenu(mPreparedPanel.featureId, (Menu)mPreparedPanel.menu)) {
                        mPreparedPanel.setMenu(null);
                        b2 = b;
                        if (!b3) {
                            return b2;
                        }
                        b2 = b;
                        if (this.mDecorContentParent != null) {
                            this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                            b2 = b;
                            return b2;
                        }
                        return b2;
                    }
                    else {
                        mPreparedPanel.refreshMenuContent = false;
                    }
                }
                mPreparedPanel.menu.stopDispatchingItemsChanged();
                if (mPreparedPanel.frozenActionViewState != null) {
                    mPreparedPanel.menu.restoreActionViewStates(mPreparedPanel.frozenActionViewState);
                    mPreparedPanel.frozenActionViewState = null;
                }
                if (!windowCallback.onPreparePanel(0, mPreparedPanel.createdPanelView, (Menu)mPreparedPanel.menu)) {
                    if (b3 && this.mDecorContentParent != null) {
                        this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                    mPreparedPanel.menu.startDispatchingItemsChanged();
                    b2 = b;
                    return b2;
                }
                int deviceId;
                if (keyEvent != null) {
                    deviceId = keyEvent.getDeviceId();
                }
                else {
                    deviceId = -1;
                }
                mPreparedPanel.qwertyMode = (KeyCharacterMap.load(deviceId).getKeyboardType() != 1);
                mPreparedPanel.menu.setQwertyMode(mPreparedPanel.qwertyMode);
                mPreparedPanel.menu.startDispatchingItemsChanged();
            }
            mPreparedPanel.isPrepared = true;
            mPreparedPanel.isHandled = false;
            this.mPreparedPanel = mPreparedPanel;
            b2 = true;
        }
        return b2;
    }
    
    private void reopenMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && (!ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext)) || this.mDecorContentParent.isOverflowMenuShowPending())) {
            final Window$Callback windowCallback = this.getWindowCallback();
            if (!this.mDecorContentParent.isOverflowMenuShowing() || !b) {
                if (windowCallback != null && !this.isDestroyed()) {
                    if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 0x1) != 0x0) {
                        this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
                        this.mInvalidatePanelMenuRunnable.run();
                    }
                    final PanelFeatureState panelState = this.getPanelState(0, true);
                    if (panelState.menu != null && !panelState.refreshMenuContent && windowCallback.onPreparePanel(0, panelState.createdPanelView, (Menu)panelState.menu)) {
                        windowCallback.onMenuOpened(108, (Menu)panelState.menu);
                        this.mDecorContentParent.showOverflowMenu();
                    }
                }
            }
            else {
                this.mDecorContentParent.hideOverflowMenu();
                if (!this.isDestroyed()) {
                    windowCallback.onPanelClosed(108, (Menu)this.getPanelState(0, true).menu);
                }
            }
        }
        else {
            final PanelFeatureState panelState2 = this.getPanelState(0, true);
            panelState2.refreshDecorView = true;
            this.closePanel(panelState2, false);
            this.openPanel(panelState2, null);
        }
    }
    
    private int sanitizeWindowFeatureId(final int n) {
        int n2;
        if (n == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            n2 = 108;
        }
        else if ((n2 = n) == 9) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            n2 = 109;
        }
        return n2;
    }
    
    private boolean shouldInheritContext(ViewParent parent) {
        boolean b;
        if (parent == null) {
            b = false;
        }
        else {
            final View decorView = this.mWindow.getDecorView();
            while (parent != null) {
                if (parent == decorView || !(parent instanceof View) || ViewCompat.isAttachedToWindow((View)parent)) {
                    b = false;
                    return b;
                }
                parent = parent.getParent();
            }
            b = true;
        }
        return b;
    }
    
    private void throwFeatureRequestIfSubDecorInstalled() {
        if (this.mSubDecorInstalled) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }
    
    private int updateStatusGuard(int visibility) {
        final int n = 0;
        final boolean b = false;
        final int n2 = 0;
        int n3 = b ? 1 : 0;
        int n4 = visibility;
        if (this.mActionModeView != null) {
            n3 = (b ? 1 : 0);
            n4 = visibility;
            if (this.mActionModeView.getLayoutParams() instanceof ViewGroup$MarginLayoutParams) {
                final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)this.mActionModeView.getLayoutParams();
                int n5 = 0;
                int n6 = 0;
                int n10;
                int n11;
                if (this.mActionModeView.isShown()) {
                    if (this.mTempRect1 == null) {
                        this.mTempRect1 = new Rect();
                        this.mTempRect2 = new Rect();
                    }
                    final Rect mTempRect1 = this.mTempRect1;
                    final Rect mTempRect2 = this.mTempRect2;
                    mTempRect1.set(0, visibility, 0, 0);
                    ViewUtils.computeFitSystemWindows((View)this.mSubDecor, mTempRect1, mTempRect2);
                    int n7;
                    if (mTempRect2.top == 0) {
                        n7 = visibility;
                    }
                    else {
                        n7 = 0;
                    }
                    if (layoutParams.topMargin != n7) {
                        final int n8 = 1;
                        layoutParams.topMargin = visibility;
                        if (this.mStatusGuard == null) {
                            (this.mStatusGuard = new View(this.mContext)).setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                            this.mSubDecor.addView(this.mStatusGuard, -1, new ViewGroup$LayoutParams(-1, visibility));
                            n6 = n8;
                        }
                        else {
                            final ViewGroup$LayoutParams layoutParams2 = this.mStatusGuard.getLayoutParams();
                            n6 = n8;
                            if (layoutParams2.height != visibility) {
                                layoutParams2.height = visibility;
                                this.mStatusGuard.setLayoutParams(layoutParams2);
                                n6 = n8;
                            }
                        }
                    }
                    int n9;
                    if (this.mStatusGuard != null) {
                        n9 = 1;
                    }
                    else {
                        n9 = 0;
                    }
                    n5 = n6;
                    n10 = n9;
                    n11 = visibility;
                    if (!this.mOverlayActionMode) {
                        n5 = n6;
                        n10 = n9;
                        n11 = visibility;
                        if (n9 != 0) {
                            n11 = 0;
                            n10 = n9;
                            n5 = n6;
                        }
                    }
                }
                else {
                    n10 = n2;
                    n11 = visibility;
                    if (layoutParams.topMargin != 0) {
                        n5 = 1;
                        layoutParams.topMargin = 0;
                        n10 = n2;
                        n11 = visibility;
                    }
                }
                n3 = n10;
                n4 = n11;
                if (n5 != 0) {
                    this.mActionModeView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    n4 = n11;
                    n3 = n10;
                }
            }
        }
        if (this.mStatusGuard != null) {
            final View mStatusGuard = this.mStatusGuard;
            if (n3 != 0) {
                visibility = n;
            }
            else {
                visibility = 8;
            }
            mStatusGuard.setVisibility(visibility);
        }
        return n4;
    }
    
    @Override
    public void addContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(view, viewGroup$LayoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    View callActivityOnCreateView(View onCreateView, final String s, final Context context, final AttributeSet set) {
        if (!(this.mOriginalWindowCallback instanceof LayoutInflater$Factory)) {
            return null;
        }
        onCreateView = ((LayoutInflater$Factory)this.mOriginalWindowCallback).onCreateView(s, context, set);
        if (onCreateView == null) {
            return null;
        }
        return onCreateView;
        onCreateView = null;
        return onCreateView;
    }
    
    @Override
    public View createView(final View view, final String s, @NonNull final Context context, @NonNull final AttributeSet set) {
        final boolean b = Build$VERSION.SDK_INT < 21;
        if (this.mAppCompatViewInflater == null) {
            this.mAppCompatViewInflater = new AppCompatViewInflater();
        }
        return this.mAppCompatViewInflater.createView(view, s, context, set, b && this.shouldInheritContext((ViewParent)view), b, true, b);
    }
    
    @Override
    boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        boolean b = true;
        if (keyEvent.getKeyCode() != 82 || !this.mOriginalWindowCallback.dispatchKeyEvent(keyEvent)) {
            final int keyCode = keyEvent.getKeyCode();
            int n;
            if (keyEvent.getAction() == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            if (n != 0) {
                b = this.onKeyDown(keyCode, keyEvent);
            }
            else {
                b = this.onKeyUp(keyCode, keyEvent);
            }
        }
        return b;
    }
    
    @Nullable
    @Override
    public View findViewById(@IdRes final int n) {
        this.ensureSubDecor();
        return this.mWindow.findViewById(n);
    }
    
    ViewGroup getSubDecor() {
        return this.mSubDecor;
    }
    
    @Override
    public boolean hasWindowFeature(int sanitizeWindowFeatureId) {
        sanitizeWindowFeatureId = this.sanitizeWindowFeatureId(sanitizeWindowFeatureId);
        boolean b = false;
        switch (sanitizeWindowFeatureId) {
            default: {
                b = this.mWindow.hasFeature(sanitizeWindowFeatureId);
                break;
            }
            case 108: {
                b = this.mHasActionBar;
                break;
            }
            case 109: {
                b = this.mOverlayActionBar;
                break;
            }
            case 10: {
                b = this.mOverlayActionMode;
                break;
            }
            case 2: {
                b = this.mFeatureProgress;
                break;
            }
            case 5: {
                b = this.mFeatureIndeterminateProgress;
                break;
            }
            case 1: {
                b = this.mWindowNoTitle;
                break;
            }
        }
        return b;
    }
    
    public void initWindowDecorActionBar() {
        this.ensureSubDecor();
        if (this.mHasActionBar && this.mActionBar == null) {
            if (this.mOriginalWindowCallback instanceof Activity) {
                this.mActionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
            }
            else if (this.mOriginalWindowCallback instanceof Dialog) {
                this.mActionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
            }
            if (this.mActionBar != null) {
                this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            }
        }
    }
    
    @Override
    public void installViewFactory() {
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        if (from.getFactory() == null) {
            LayoutInflaterCompat.setFactory(from, this);
        }
        else if (!(LayoutInflaterCompat.getFactory(from) instanceof AppCompatDelegateImplV7)) {
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }
    
    @Override
    public void invalidateOptionsMenu() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar == null || !supportActionBar.invalidateOptionsMenu()) {
            this.invalidatePanelMenu(0);
        }
    }
    
    boolean onBackPressed() {
        boolean b = true;
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        else {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar == null || !supportActionBar.collapseActionView()) {
                b = false;
            }
        }
        return b;
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.mHasActionBar && this.mSubDecorInstalled) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.onConfigurationChanged(configuration);
            }
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        if (this.mOriginalWindowCallback instanceof Activity && NavUtils.getParentActivityName((Activity)this.mOriginalWindowCallback) != null) {
            final ActionBar peekSupportActionBar = this.peekSupportActionBar();
            if (peekSupportActionBar == null) {
                this.mEnableDefaultActionBarUp = true;
            }
            else {
                peekSupportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
            }
        }
    }
    
    @Override
    public final View onCreateView(View view, final String s, final Context context, final AttributeSet set) {
        final View callActivityOnCreateView = this.callActivityOnCreateView(view, s, context, set);
        if (callActivityOnCreateView != null) {
            view = callActivityOnCreateView;
        }
        else {
            view = this.createView(view, s, context, set);
        }
        return view;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mActionBar != null) {
            this.mActionBar.onDestroy();
            this.mActionBar = null;
        }
    }
    
    boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        final boolean b = true;
        boolean b2 = true;
        switch (n) {
            case 82: {
                this.onKeyDownPanel(0, keyEvent);
                return b2;
            }
            case 4: {
                this.mLongPressBackDown = ((keyEvent.getFlags() & 0x80) != 0x0 && b);
                break;
            }
        }
        if (Build$VERSION.SDK_INT < 11) {
            this.onKeyShortcut(n, keyEvent);
        }
        b2 = false;
        return b2;
    }
    
    @Override
    boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        final boolean b = true;
        final ActionBar supportActionBar = this.getSupportActionBar();
        boolean b2;
        if (supportActionBar != null && supportActionBar.onKeyShortcut(n, keyEvent)) {
            b2 = b;
        }
        else if (this.mPreparedPanel != null && this.performPanelShortcut(this.mPreparedPanel, keyEvent.getKeyCode(), keyEvent, 1)) {
            b2 = b;
            if (this.mPreparedPanel != null) {
                this.mPreparedPanel.isHandled = true;
                b2 = b;
            }
        }
        else {
            if (this.mPreparedPanel == null) {
                final PanelFeatureState panelState = this.getPanelState(0, true);
                this.preparePanel(panelState, keyEvent);
                final boolean performPanelShortcut = this.performPanelShortcut(panelState, keyEvent.getKeyCode(), keyEvent, 1);
                panelState.isPrepared = false;
                b2 = b;
                if (performPanelShortcut) {
                    return b2;
                }
            }
            b2 = false;
        }
        return b2;
    }
    
    boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        final boolean b = true;
        switch (n) {
            case 82: {
                this.onKeyUpPanel(0, keyEvent);
                return b;
            }
            case 4: {
                final boolean mLongPressBackDown = this.mLongPressBackDown;
                this.mLongPressBackDown = false;
                final PanelFeatureState panelState = this.getPanelState(0, false);
                if (panelState != null && panelState.isOpen) {
                    boolean b2 = b;
                    if (!mLongPressBackDown) {
                        this.closePanel(panelState, true);
                        b2 = b;
                        return b2;
                    }
                    return b2;
                }
                else {
                    if (this.onBackPressed()) {
                        return b;
                    }
                    break;
                }
                break;
            }
        }
        return false;
    }
    
    @Override
    public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
        final Window$Callback windowCallback = this.getWindowCallback();
        if (windowCallback == null || this.isDestroyed()) {
            return false;
        }
        final PanelFeatureState menuPanel = this.findMenuPanel((Menu)menuBuilder.getRootMenu());
        if (menuPanel == null) {
            return false;
        }
        return windowCallback.onMenuItemSelected(menuPanel.featureId, menuItem);
        onMenuItemSelected = false;
        return onMenuItemSelected;
    }
    
    @Override
    public void onMenuModeChange(final MenuBuilder menuBuilder) {
        this.reopenMenu(menuBuilder, true);
    }
    
    @Override
    boolean onMenuOpened(final int n, final Menu menu) {
        final boolean b = true;
        boolean b2;
        if (n == 108) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            b2 = b;
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(true);
                b2 = b;
            }
        }
        else {
            b2 = false;
        }
        return b2;
    }
    
    @Override
    void onPanelClosed(final int n, final Menu menu) {
        if (n == 108) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.dispatchMenuVisibilityChanged(false);
            }
        }
        else if (n == 0) {
            final PanelFeatureState panelState = this.getPanelState(n, true);
            if (panelState.isOpen) {
                this.closePanel(panelState, false);
            }
        }
    }
    
    @Override
    public void onPostCreate(final Bundle bundle) {
        this.ensureSubDecor();
    }
    
    @Override
    public void onPostResume() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(true);
        }
    }
    
    @Override
    public void onStop() {
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setShowHideAnimationEnabled(false);
        }
    }
    
    void onSubDecorInstalled(final ViewGroup viewGroup) {
    }
    
    @Override
    void onTitleChanged(final CharSequence text) {
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.setWindowTitle(text);
        }
        else if (this.peekSupportActionBar() != null) {
            this.peekSupportActionBar().setWindowTitle(text);
        }
        else if (this.mTitleView != null) {
            this.mTitleView.setText(text);
        }
    }
    
    @Override
    public boolean requestWindowFeature(int sanitizeWindowFeatureId) {
        boolean requestFeature = false;
        sanitizeWindowFeatureId = this.sanitizeWindowFeatureId(sanitizeWindowFeatureId);
        if (!this.mWindowNoTitle || sanitizeWindowFeatureId != 108) {
            if (this.mHasActionBar && sanitizeWindowFeatureId == 1) {
                this.mHasActionBar = false;
            }
            switch (sanitizeWindowFeatureId) {
                default: {
                    requestFeature = this.mWindow.requestFeature(sanitizeWindowFeatureId);
                    break;
                }
                case 108: {
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mHasActionBar = true;
                    requestFeature = true;
                    break;
                }
                case 109: {
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mOverlayActionBar = true;
                    requestFeature = true;
                    break;
                }
                case 10: {
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mOverlayActionMode = true;
                    requestFeature = true;
                    break;
                }
                case 2: {
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mFeatureProgress = true;
                    requestFeature = true;
                    break;
                }
                case 5: {
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mFeatureIndeterminateProgress = true;
                    requestFeature = true;
                    break;
                }
                case 1: {
                    this.throwFeatureRequestIfSubDecorInstalled();
                    this.mWindowNoTitle = true;
                    requestFeature = true;
                    break;
                }
            }
        }
        return requestFeature;
    }
    
    @Override
    public void setContentView(final int n) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(n, viewGroup);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setContentView(final View view) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.ensureSubDecor();
        final ViewGroup viewGroup = (ViewGroup)this.mSubDecor.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, viewGroup$LayoutParams);
        this.mOriginalWindowCallback.onContentChanged();
    }
    
    @Override
    public void setSupportActionBar(final Toolbar toolbar) {
        if (this.mOriginalWindowCallback instanceof Activity) {
            final ActionBar supportActionBar = this.getSupportActionBar();
            if (supportActionBar instanceof WindowDecorActionBar) {
                throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
            }
            this.mMenuInflater = null;
            if (supportActionBar != null) {
                supportActionBar.onDestroy();
            }
            if (toolbar != null) {
                final ToolbarActionBar mActionBar = new ToolbarActionBar(toolbar, ((Activity)this.mContext).getTitle(), this.mAppCompatWindowCallback);
                this.mActionBar = mActionBar;
                this.mWindow.setCallback(mActionBar.getWrappedWindowCallback());
            }
            else {
                this.mActionBar = null;
                this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }
            this.invalidateOptionsMenu();
        }
    }
    
    @Override
    public ActionMode startSupportActionMode(final ActionMode.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        final ActionModeCallbackWrapperV7 actionModeCallbackWrapperV7 = new ActionModeCallbackWrapperV7(callback);
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            this.mActionMode = supportActionBar.startActionMode(actionModeCallbackWrapperV7);
            if (this.mActionMode != null && this.mAppCompatCallback != null) {
                this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
            }
        }
        if (this.mActionMode == null) {
            this.mActionMode = this.startSupportActionModeFromWindow(actionModeCallbackWrapperV7);
        }
        return this.mActionMode;
    }
    
    @Override
    ActionMode startSupportActionModeFromWindow(final ActionMode.Callback callback) {
        this.endOnGoingFadeAnimation();
        if (this.mActionMode != null) {
            this.mActionMode.finish();
        }
        final ActionModeCallbackWrapperV7 actionModeCallbackWrapperV7 = new ActionModeCallbackWrapperV7(callback);
        ActionMode onWindowStartingSupportActionMode;
        Object context = onWindowStartingSupportActionMode = null;
        while (true) {
            if (this.mAppCompatCallback == null) {
                break Label_0062;
            }
            onWindowStartingSupportActionMode = (ActionMode)context;
            if (this.isDestroyed()) {
                break Label_0062;
            }
            try {
                onWindowStartingSupportActionMode = this.mAppCompatCallback.onWindowStartingSupportActionMode(actionModeCallbackWrapperV7);
                if (onWindowStartingSupportActionMode != null) {
                    this.mActionMode = onWindowStartingSupportActionMode;
                }
                else {
                    if (this.mActionModeView == null) {
                        if (this.mIsFloating) {
                            context = new TypedValue();
                            final Resources$Theme theme = this.mContext.getTheme();
                            theme.resolveAttribute(R.attr.actionBarTheme, (TypedValue)context, true);
                            Object mContext;
                            if (((TypedValue)context).resourceId != 0) {
                                final Resources$Theme theme2 = this.mContext.getResources().newTheme();
                                theme2.setTo(theme);
                                theme2.applyStyle(((TypedValue)context).resourceId, true);
                                mContext = new ContextThemeWrapper(this.mContext, 0);
                                ((Context)mContext).getTheme().setTo(theme2);
                            }
                            else {
                                mContext = this.mContext;
                            }
                            this.mActionModeView = new ActionBarContextView((Context)mContext);
                            PopupWindowCompat.setWindowLayoutType(this.mActionModePopup = new PopupWindow((Context)mContext, (AttributeSet)null, R.attr.actionModePopupWindowStyle), 2);
                            this.mActionModePopup.setContentView((View)this.mActionModeView);
                            this.mActionModePopup.setWidth(-1);
                            ((Context)mContext).getTheme().resolveAttribute(R.attr.actionBarSize, (TypedValue)context, true);
                            this.mActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(((TypedValue)context).data, ((Context)mContext).getResources().getDisplayMetrics()));
                            this.mActionModePopup.setHeight(-2);
                            this.mShowActionModePopup = new Runnable() {
                                @Override
                                public void run() {
                                    AppCompatDelegateImplV7.this.mActionModePopup.showAtLocation((View)AppCompatDelegateImplV7.this.mActionModeView, 55, 0, 0);
                                    AppCompatDelegateImplV7.this.endOnGoingFadeAnimation();
                                    ViewCompat.setAlpha((View)AppCompatDelegateImplV7.this.mActionModeView, 0.0f);
                                    (AppCompatDelegateImplV7.this.mFadeAnim = ViewCompat.animate((View)AppCompatDelegateImplV7.this.mActionModeView).alpha(1.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(final View view) {
                                            ViewCompat.setAlpha((View)AppCompatDelegateImplV7.this.mActionModeView, 1.0f);
                                            AppCompatDelegateImplV7.this.mFadeAnim.setListener(null);
                                            AppCompatDelegateImplV7.this.mFadeAnim = null;
                                        }
                                        
                                        @Override
                                        public void onAnimationStart(final View view) {
                                            AppCompatDelegateImplV7.this.mActionModeView.setVisibility(0);
                                        }
                                    });
                                }
                            };
                        }
                        else {
                            final ViewStubCompat viewStubCompat = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
                            if (viewStubCompat != null) {
                                viewStubCompat.setLayoutInflater(LayoutInflater.from(this.getActionBarThemedContext()));
                                this.mActionModeView = (ActionBarContextView)viewStubCompat.inflate();
                            }
                        }
                    }
                    if (this.mActionModeView != null) {
                        this.endOnGoingFadeAnimation();
                        this.mActionModeView.killMode();
                        context = this.mActionModeView.getContext();
                        final StandaloneActionMode mActionMode = new StandaloneActionMode((Context)context, this.mActionModeView, actionModeCallbackWrapperV7, this.mActionModePopup == null);
                        if (callback.onCreateActionMode(mActionMode, mActionMode.getMenu())) {
                            mActionMode.invalidate();
                            this.mActionModeView.initForMode(mActionMode);
                            this.mActionMode = mActionMode;
                            ViewCompat.setAlpha((View)this.mActionModeView, 0.0f);
                            (this.mFadeAnim = ViewCompat.animate((View)this.mActionModeView).alpha(1.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(final View view) {
                                    ViewCompat.setAlpha((View)AppCompatDelegateImplV7.this.mActionModeView, 1.0f);
                                    AppCompatDelegateImplV7.this.mFadeAnim.setListener(null);
                                    AppCompatDelegateImplV7.this.mFadeAnim = null;
                                }
                                
                                @Override
                                public void onAnimationStart(final View view) {
                                    AppCompatDelegateImplV7.this.mActionModeView.setVisibility(0);
                                    AppCompatDelegateImplV7.this.mActionModeView.sendAccessibilityEvent(32);
                                    if (AppCompatDelegateImplV7.this.mActionModeView.getParent() != null) {
                                        ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV7.this.mActionModeView.getParent());
                                    }
                                }
                            });
                            if (this.mActionModePopup != null) {
                                this.mWindow.getDecorView().post(this.mShowActionModePopup);
                            }
                        }
                        else {
                            this.mActionMode = null;
                        }
                    }
                }
                if (this.mActionMode != null && this.mAppCompatCallback != null) {
                    this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
                }
                return this.mActionMode;
            }
            catch (AbstractMethodError abstractMethodError) {
                onWindowStartingSupportActionMode = (ActionMode)context;
                continue;
            }
            break;
        }
    }
    
    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            AppCompatDelegateImplV7.this.checkCloseActionMenu(menuBuilder);
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            final Window$Callback windowCallback = AppCompatDelegateImplV7.this.getWindowCallback();
            if (windowCallback != null) {
                windowCallback.onMenuOpened(108, (Menu)menuBuilder);
            }
            return true;
        }
    }
    
    class ActionModeCallbackWrapperV7 implements ActionMode.Callback
    {
        private ActionMode.Callback mWrapped;
        
        public ActionModeCallbackWrapperV7(final ActionMode.Callback mWrapped) {
            this.mWrapped = mWrapped;
        }
        
        @Override
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }
        
        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }
        
        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (AppCompatDelegateImplV7.this.mActionModePopup != null) {
                AppCompatDelegateImplV7.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImplV7.this.mShowActionModePopup);
            }
            if (AppCompatDelegateImplV7.this.mActionModeView != null) {
                AppCompatDelegateImplV7.this.endOnGoingFadeAnimation();
                (AppCompatDelegateImplV7.this.mFadeAnim = ViewCompat.animate((View)AppCompatDelegateImplV7.this.mActionModeView).alpha(0.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final View view) {
                        AppCompatDelegateImplV7.this.mActionModeView.setVisibility(8);
                        if (AppCompatDelegateImplV7.this.mActionModePopup != null) {
                            AppCompatDelegateImplV7.this.mActionModePopup.dismiss();
                        }
                        else if (AppCompatDelegateImplV7.this.mActionModeView.getParent() instanceof View) {
                            ViewCompat.requestApplyInsets((View)AppCompatDelegateImplV7.this.mActionModeView.getParent());
                        }
                        AppCompatDelegateImplV7.this.mActionModeView.removeAllViews();
                        AppCompatDelegateImplV7.this.mFadeAnim.setListener(null);
                        AppCompatDelegateImplV7.this.mFadeAnim = null;
                    }
                });
            }
            if (AppCompatDelegateImplV7.this.mAppCompatCallback != null) {
                AppCompatDelegateImplV7.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImplV7.this.mActionMode);
            }
            AppCompatDelegateImplV7.this.mActionMode = null;
        }
        
        @Override
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }
    }
    
    private class ListMenuDecorView extends ContentFrameLayout
    {
        public ListMenuDecorView(final Context context) {
            super(context);
        }
        
        private boolean isOutOfBounds(final int n, final int n2) {
            return n < -5 || n2 < -5 || n > this.getWidth() + 5 || n2 > this.getHeight() + 5;
        }
        
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            return AppCompatDelegateImplV7.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            boolean onInterceptTouchEvent;
            if (motionEvent.getAction() == 0 && this.isOutOfBounds((int)motionEvent.getX(), (int)motionEvent.getY())) {
                AppCompatDelegateImplV7.this.closePanel(0);
                onInterceptTouchEvent = true;
            }
            else {
                onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
            }
            return onInterceptTouchEvent;
        }
        
        public void setBackgroundResource(final int n) {
            this.setBackgroundDrawable(AppCompatDrawableManager.get().getDrawable(this.getContext(), n));
        }
    }
    
    private static final class PanelFeatureState
    {
        int background;
        View createdPanelView;
        ViewGroup decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int gravity;
        boolean isHandled;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        Context listPresenterContext;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;
        
        PanelFeatureState(final int featureId) {
            this.featureId = featureId;
            this.refreshDecorView = false;
        }
        
        void applyFrozenState() {
            if (this.menu != null && this.frozenMenuState != null) {
                this.menu.restorePresenterStates(this.frozenMenuState);
                this.frozenMenuState = null;
            }
        }
        
        public void clearMenuPresenters() {
            if (this.menu != null) {
                this.menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.listMenuPresenter = null;
        }
        
        MenuView getListMenuView(final MenuPresenter.Callback callback) {
            MenuView menuView;
            if (this.menu == null) {
                menuView = null;
            }
            else {
                if (this.listMenuPresenter == null) {
                    (this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout)).setCallback(callback);
                    this.menu.addMenuPresenter(this.listMenuPresenter);
                }
                menuView = this.listMenuPresenter.getMenuView(this.decorView);
            }
            return menuView;
        }
        
        public boolean hasPanelItems() {
            final boolean b = true;
            boolean b2;
            if (this.shownPanelView == null) {
                b2 = false;
            }
            else {
                b2 = b;
                if (this.createdPanelView == null) {
                    b2 = b;
                    if (this.listMenuPresenter.getAdapter().getCount() <= 0) {
                        b2 = false;
                    }
                }
            }
            return b2;
        }
        
        void onRestoreInstanceState(final Parcelable parcelable) {
            final SavedState savedState = (SavedState)parcelable;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.frozenMenuState = savedState.menuState;
            this.shownPanelView = null;
            this.decorView = null;
        }
        
        Parcelable onSaveInstanceState() {
            final SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return (Parcelable)savedState;
        }
        
        void setMenu(final MenuBuilder menu) {
            if (menu != this.menu) {
                if (this.menu != null) {
                    this.menu.removeMenuPresenter(this.listMenuPresenter);
                }
                this.menu = menu;
                if (menu != null && this.listMenuPresenter != null) {
                    menu.addMenuPresenter(this.listMenuPresenter);
                }
            }
        }
        
        void setStyle(final Context context) {
            final TypedValue typedValue = new TypedValue();
            final Resources$Theme theme = context.getResources().newTheme();
            theme.setTo(context.getTheme());
            theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            else {
                theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            final ContextThemeWrapper listPresenterContext = new ContextThemeWrapper(context, 0);
            ((Context)listPresenterContext).getTheme().setTo(theme);
            this.listPresenterContext = (Context)listPresenterContext;
            final TypedArray obtainStyledAttributes = ((Context)listPresenterContext).obtainStyledAttributes(R.styleable.AppCompatTheme);
            this.background = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
            this.windowAnimations = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
            obtainStyledAttributes.recycle();
        }
        
        private static class SavedState implements Parcelable
        {
            public static final Parcelable$Creator<SavedState> CREATOR;
            int featureId;
            boolean isOpen;
            Bundle menuState;
            
            static {
                CREATOR = ParcelableCompat.newCreator((ParcelableCompatCreatorCallbacks<SavedState>)new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                        return readFromParcel(parcel, classLoader);
                    }
                    
                    @Override
                    public SavedState[] newArray(final int n) {
                        return new SavedState[n];
                    }
                });
            }
            
            private static SavedState readFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                boolean isOpen = true;
                final SavedState savedState = new SavedState();
                savedState.featureId = parcel.readInt();
                if (parcel.readInt() != 1) {
                    isOpen = false;
                }
                savedState.isOpen = isOpen;
                if (savedState.isOpen) {
                    savedState.menuState = parcel.readBundle(classLoader);
                }
                return savedState;
            }
            
            public int describeContents() {
                return 0;
            }
            
            public void writeToParcel(final Parcel parcel, int n) {
                parcel.writeInt(this.featureId);
                if (this.isOpen) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                parcel.writeInt(n);
                if (this.isOpen) {
                    parcel.writeBundle(this.menuState);
                }
            }
        }
    }
    
    private final class PanelMenuPresenterCallback implements MenuPresenter.Callback
    {
        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, final boolean b) {
            final Object rootMenu = menuBuilder.getRootMenu();
            boolean b2;
            if (rootMenu != menuBuilder) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            final AppCompatDelegateImplV7 this$0 = AppCompatDelegateImplV7.this;
            if (b2) {
                menuBuilder = (MenuBuilder)rootMenu;
            }
            final PanelFeatureState access$800 = this$0.findMenuPanel((Menu)menuBuilder);
            if (access$800 != null) {
                if (b2) {
                    AppCompatDelegateImplV7.this.callOnPanelClosed(access$800.featureId, access$800, (Menu)rootMenu);
                    AppCompatDelegateImplV7.this.closePanel(access$800, true);
                }
                else {
                    AppCompatDelegateImplV7.this.closePanel(access$800, b);
                }
            }
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            if (menuBuilder == null && AppCompatDelegateImplV7.this.mHasActionBar) {
                final Window$Callback windowCallback = AppCompatDelegateImplV7.this.getWindowCallback();
                if (windowCallback != null && !AppCompatDelegateImplV7.this.isDestroyed()) {
                    windowCallback.onMenuOpened(108, (Menu)menuBuilder);
                }
            }
            return true;
        }
    }
}
