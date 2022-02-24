// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import android.content.res.TypedArray;
import android.animation.Animator;
import android.os.Build$VERSION;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ArgbEvaluator;
import android.graphics.drawable.ColorDrawable;
import android.animation.AnimatorSet;
import android.util.Log;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import com.logitech.ue.fragments.NavigatableFragment;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.LinearLayout;

public abstract class NavigatorActivity extends BaseActivity
{
    public static final String ACTION_INITIAL_FRAGMENT_ARGS = "initial_fragment_args";
    public static final String ACTION_INITIAL_FRAGMENT_CLASS = "initial_fragment";
    public static final int MENU_ITEM_ID_CLOSE = 1000;
    private static final String TAG;
    protected LinearLayout mContainer;
    protected TextView mTitleText;
    private int mTitleTextColor;
    protected Toolbar mToolbar;
    
    static {
        TAG = NavigatorActivity.class.getSimpleName();
    }
    
    public void goBack() {
        super.onBackPressed();
    }
    
    public void gotoFragment(final NavigatableFragment navigatableFragment) {
        this.gotoFragment(navigatableFragment, navigatableFragment.getClass().getSimpleName());
    }
    
    public void gotoFragment(final NavigatableFragment navigatableFragment, final String s) {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        final FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        if (supportFragmentManager.findFragmentById(2131624045) == null) {
            beginTransaction.add(2131624045, navigatableFragment, s);
        }
        else {
            beginTransaction.setCustomAnimations(2131034126, 2131034129, 2131034125, 2131034130);
            beginTransaction.replace(2131624045, navigatableFragment, s);
            beginTransaction.addToBackStack(s);
        }
        beginTransaction.commitAllowingStateLoss();
        if (navigatableFragment != null) {
            this.setToolbarColorsTo(navigatableFragment.getToolbarBackgroundColor(), navigatableFragment.getToolbarTextColor(), navigatableFragment.getStatusBarColor(), true);
        }
    }
    
    public void gotoFragment(final Class<? extends NavigatableFragment> clazz, final Bundle bundle) {
        this.gotoFragment(clazz, bundle, clazz.getSimpleName());
    }
    
    public void gotoFragment(final Class<? extends NavigatableFragment> clazz, final Bundle bundle, final String s) {
        this.gotoFragment((NavigatableFragment)Fragment.instantiate((Context)this, clazz.getName(), bundle), s);
    }
    
    @Override
    public void onBackPressed() {
        final Fragment fragmentById = this.getSupportFragmentManager().findFragmentById(2131624045);
        if (fragmentById != null && fragmentById instanceof NavigatableFragment) {
            if (((NavigatableFragment)fragmentById).onBack()) {
                super.onBackPressed();
            }
        }
        else {
            super.onBackPressed();
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968656);
        this.setSupportActionBar(this.mToolbar = (Toolbar)this.findViewById(2131624031));
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(2130837614);
        this.mTitleText = (TextView)this.findViewById(16908310);
        this.mContainer = (LinearLayout)this.findViewById(2131624209);
        if (this.getIntent().getExtras() != null) {
            final Class clazz = (Class)this.getIntent().getSerializableExtra("initial_fragment");
            if (clazz != null) {
                this.gotoFragment(clazz, this.getIntent().getBundleExtra("initial_fragment_args"));
            }
        }
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final Drawable drawable = ContextCompat.getDrawable((Context)this, 2130837616);
        drawable.setColorFilter(this.mTitleTextColor, PorterDuff$Mode.SRC_ATOP);
        menu.add(0, 2130837616, 1000, (CharSequence)null).setIcon(drawable).setTitle((CharSequence)"Close").setShowAsActionFlags(6);
        return true;
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean onOptionsItemSelected = true;
        switch (menuItem.getItemId()) {
            default: {
                onOptionsItemSelected = super.onOptionsItemSelected(menuItem);
                break;
            }
            case 2130837616: {
                this.finish();
                break;
            }
            case 16908332: {
                this.onBackPressed();
                break;
            }
        }
        return onOptionsItemSelected;
    }
    
    @Override
    public void onRequestPermissionsResult(final int n, @NonNull final String[] array, @NonNull final int[] array2) {
        switch (n) {
            case 2: {
                if (array2.length > 0 && array2[0] == 0) {
                    Log.i(NavigatorActivity.TAG, "EXTERNAL STORAGE READ GRANTED");
                    break;
                }
                Log.w(NavigatorActivity.TAG, "EXTERNAL STORAGE READ DENIED");
                break;
            }
        }
    }
    
    public void setTitle(final CharSequence charSequence) {
        Log.d(NavigatorActivity.TAG, "Set title " + (Object)charSequence);
        this.mTitleText.setText(charSequence);
    }
    
    public void setToolbarColorsTo(int n, final int n2, final int i, final boolean b) {
        this.mTitleTextColor = n2;
        final Drawable navigationIcon = this.mToolbar.getNavigationIcon();
        if (b) {
            final AnimatorSet set = new AnimatorSet();
            final ColorDrawable colorDrawable = (ColorDrawable)this.mToolbar.getBackground();
            while (true) {
                Label_0302: {
                    int j = 0;
                    Label_0052: {
                        if (colorDrawable == null) {
                            final TypedArray obtainStyledAttributes = this.obtainStyledAttributes(new int[] { 2130771984 });
                            try {
                                j = obtainStyledAttributes.getColor(0, 0);
                                break Label_0052;
                            }
                            catch (Exception ex) {
                                j = -16777216;
                                break Label_0052;
                            }
                            finally {
                                obtainStyledAttributes.recycle();
                            }
                            break Label_0302;
                        }
                        j = colorDrawable.getColor();
                    }
                    final ValueAnimator ofObject = ValueAnimator.ofObject((TypeEvaluator)new ArgbEvaluator(), new Object[] { j, n });
                    ofObject.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                            final int intValue = (int)valueAnimator.getAnimatedValue();
                            NavigatorActivity.this.mToolbar.setBackgroundColor(intValue);
                            NavigatorActivity.this.mContainer.setBackgroundColor(intValue);
                        }
                    });
                    final ColorDrawable colorDrawable2 = (ColorDrawable)this.mToolbar.getBackground();
                    if (colorDrawable2 == null) {
                        break Label_0302;
                    }
                    n = colorDrawable2.getColor();
                    final ValueAnimator ofObject2 = ValueAnimator.ofObject((TypeEvaluator)new ArgbEvaluator(), new Object[] { n, i });
                    ofObject2.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                            final int intValue = (int)valueAnimator.getAnimatedValue();
                            if (Build$VERSION.SDK_INT >= 21) {
                                NavigatorActivity.this.getWindow().setStatusBarColor(intValue);
                            }
                        }
                    });
                    n = this.mTitleText.getCurrentTextColor();
                    final ValueAnimator ofObject3 = ValueAnimator.ofObject((TypeEvaluator)new ArgbEvaluator(), new Object[] { n, n2 });
                    ofObject3.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                            final int intValue = (int)valueAnimator.getAnimatedValue();
                            navigationIcon.setColorFilter(intValue, PorterDuff$Mode.SRC_ATOP);
                            NavigatorActivity.this.mToolbar.setNavigationIcon(navigationIcon);
                            NavigatorActivity.this.mTitleText.setTextColor(intValue);
                        }
                    });
                    set.playTogether(new Animator[] { (Animator)ofObject, (Animator)ofObject2, (Animator)ofObject3 });
                    set.start();
                    return;
                }
                final TypedArray obtainStyledAttributes2 = this.obtainStyledAttributes(new int[] { 2130771982 });
                try {
                    n = obtainStyledAttributes2.getColor(0, 0);
                    continue;
                }
                catch (Exception ex2) {
                    n = -16777216;
                    continue;
                }
                finally {
                    obtainStyledAttributes2.recycle();
                }
                break;
            }
        }
        Label_0357: {
            break Label_0357;
        }
        this.mToolbar.setBackgroundColor(n);
        navigationIcon.setColorFilter(n2, PorterDuff$Mode.SRC_ATOP);
        this.mToolbar.setNavigationIcon(navigationIcon);
        this.mContainer.setBackgroundColor(n);
        if (Build$VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(n);
        }
    }
}
