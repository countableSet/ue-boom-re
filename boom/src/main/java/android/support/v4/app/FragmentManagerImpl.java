// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.app;

import android.support.annotation.CallSuper;
import android.support.v4.os.BuildCompat;
import android.support.v4.util.DebugUtils;
import android.os.Handler;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.res.Resources$NotFoundException;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.os.Looper;
import java.util.Arrays;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;
import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.view.animation.Animation$AnimationListener;
import android.view.View;
import java.util.List;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import java.util.ArrayList;
import java.lang.reflect.Field;
import android.view.animation.Interpolator;
import android.support.v4.view.LayoutInflaterFactory;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory
{
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    ArrayList<Fragment> mActive;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    FragmentController mController;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<Runnable> mPendingActions;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    Runnable[] mTmpActions;
    
    static {
        boolean honeycomb = false;
        FragmentManagerImpl.DEBUG = false;
        if (Build$VERSION.SDK_INT >= 11) {
            honeycomb = true;
        }
        HONEYCOMB = honeycomb;
        FragmentManagerImpl.sAnimationListenerField = null;
        DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5f);
    }
    
    FragmentManagerImpl() {
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }
    
    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }
    
    static Animation makeFadeAnimation(final Context context, final float n, final float n2) {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return (Animation)alphaAnimation;
    }
    
    static Animation makeOpenCloseAnimation(final Context context, final float n, final float n2, final float n3, final float n4) {
        final AnimationSet set = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(n, n2, n, n2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        set.addAnimation((Animation)scaleAnimation);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n3, n4);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        set.addAnimation((Animation)alphaAnimation);
        return (Animation)set;
    }
    
    static boolean modifiesAlpha(final Animation animation) {
        final boolean b = true;
        boolean b2;
        if (animation instanceof AlphaAnimation) {
            b2 = b;
        }
        else {
            if (animation instanceof AnimationSet) {
                final List animations = ((AnimationSet)animation).getAnimations();
                for (int i = 0; i < animations.size(); ++i) {
                    b2 = b;
                    if (animations.get(i) instanceof AlphaAnimation) {
                        return b2;
                    }
                }
            }
            b2 = false;
        }
        return b2;
    }
    
    public static int reverseTransit(int n) {
        final int n2 = 0;
        switch (n) {
            default: {
                n = n2;
                break;
            }
            case 4097: {
                n = 8194;
                break;
            }
            case 8194: {
                n = 4097;
                break;
            }
            case 4099: {
                n = 4099;
                break;
            }
        }
        return n;
    }
    
    private void setHWLayerAnimListenerIfAlpha(final View view, final Animation obj) {
        if (view != null && obj != null && shouldRunOnHWLayer(view, obj)) {
            Animation$AnimationListener animation$AnimationListener = null;
            while (true) {
                try {
                    if (FragmentManagerImpl.sAnimationListenerField == null) {
                        (FragmentManagerImpl.sAnimationListenerField = Animation.class.getDeclaredField("mListener")).setAccessible(true);
                    }
                    animation$AnimationListener = (Animation$AnimationListener)FragmentManagerImpl.sAnimationListenerField.get(obj);
                    ViewCompat.setLayerType(view, 2, null);
                    obj.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(view, obj, animation$AnimationListener));
                }
                catch (NoSuchFieldException ex) {
                    Log.e("FragmentManager", "No field with the name mListener is found in Animation class", (Throwable)ex);
                    continue;
                }
                catch (IllegalAccessException ex2) {
                    Log.e("FragmentManager", "Cannot access Animation's mListener field", (Throwable)ex2);
                    continue;
                }
                break;
            }
        }
    }
    
    static boolean shouldRunOnHWLayer(final View view, final Animation animation) {
        return Build$VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(view) == 0 && ViewCompat.hasOverlappingRendering(view) && modifiesAlpha(animation);
    }
    
    private void throwException(final RuntimeException ex) {
        Log.e("FragmentManager", ex.getMessage());
        Log.e("FragmentManager", "Activity state:");
        final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        while (true) {
            Label_0075: {
                if (this.mHost == null) {
                    break Label_0075;
                }
                try {
                    this.mHost.onDump("  ", null, printWriter, new String[0]);
                    throw ex;
                }
                catch (Exception ex2) {
                    Log.e("FragmentManager", "Failed dumping state", (Throwable)ex2);
                    throw ex;
                }
                try {
                    this.dump("  ", null, printWriter, new String[0]);
                    continue;
                }
                catch (Exception ex3) {
                    Log.e("FragmentManager", "Failed dumping state", (Throwable)ex3);
                    continue;
                }
            }
            continue;
        }
    }
    
    public static int transitToStyleIndex(int n, final boolean b) {
        final int n2 = -1;
        switch (n) {
            default: {
                n = n2;
                break;
            }
            case 4097: {
                if (b) {
                    n = 1;
                }
                else {
                    n = 2;
                }
                break;
            }
            case 8194: {
                if (b) {
                    n = 3;
                }
                else {
                    n = 4;
                }
                break;
            }
            case 4099: {
                if (b) {
                    n = 5;
                }
                else {
                    n = 6;
                }
                break;
            }
        }
        return n;
    }
    
    void addBackStackState(final BackStackRecord e) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<BackStackRecord>();
        }
        this.mBackStack.add(e);
        this.reportBackStackChanged();
    }
    
    public void addFragment(final Fragment fragment, final boolean b) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList<Fragment>();
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "add: " + fragment);
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                throw new IllegalStateException("Fragment already added: " + fragment);
            }
            this.mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (b) {
                this.moveToState(fragment);
            }
        }
    }
    
    @Override
    public void addOnBackStackChangedListener(final OnBackStackChangedListener e) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<OnBackStackChangedListener>();
        }
        this.mBackStackChangeListeners.add(e);
    }
    
    public int allocBackStackIndex(final BackStackRecord backStackRecord) {
        synchronized (this) {
            int index;
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList<BackStackRecord>();
                }
                index = this.mBackStackIndices.size();
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Setting back stack index " + index + " to " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            }
            else {
                index = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Adding back stack index " + index + " with " + backStackRecord);
                }
                this.mBackStackIndices.set(index, backStackRecord);
            }
            return index;
        }
    }
    
    public void attachController(final FragmentHostCallback mHost, final FragmentContainer mContainer, final Fragment mParent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = mHost;
        this.mContainer = mContainer;
        this.mParent = mParent;
    }
    
    public void attachFragment(final Fragment e, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "attach: " + e);
        }
        if (e.mDetached) {
            e.mDetached = false;
            if (!e.mAdded) {
                if (this.mAdded == null) {
                    this.mAdded = new ArrayList<Fragment>();
                }
                if (this.mAdded.contains(e)) {
                    throw new IllegalStateException("Fragment already added: " + e);
                }
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "add from attach: " + e);
                }
                this.mAdded.add(e);
                e.mAdded = true;
                if (e.mHasMenu && e.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                this.moveToState(e, this.mCurState, n, n2, false);
            }
        }
    }
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }
    
    public void detachFragment(final Fragment o, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "detach: " + o);
        }
        if (!o.mDetached) {
            o.mDetached = true;
            if (o.mAdded) {
                if (this.mAdded != null) {
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "remove from detach: " + o);
                    }
                    this.mAdded.remove(o);
                }
                if (o.mHasMenu && o.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                this.moveToState(o, 1, n, n2, o.mAdded = false);
            }
        }
    }
    
    public void dispatchActivityCreated() {
        this.moveToState(2, this.mStateSaved = false);
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performConfigurationChanged(configuration);
                }
            }
        }
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                    return true;
                }
            }
            return false;
        }
        return false;
        return false;
    }
    
    public void dispatchCreate() {
        this.moveToState(1, this.mStateSaved = false);
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        boolean b = false;
        boolean b2 = false;
        ArrayList<Fragment> mCreatedMenus = null;
        ArrayList<Fragment> list = null;
        if (this.mAdded != null) {
            int index = 0;
            while (true) {
                mCreatedMenus = list;
                b = b2;
                if (index >= this.mAdded.size()) {
                    break;
                }
                final Fragment e = this.mAdded.get(index);
                ArrayList<Fragment> list2 = list;
                boolean b3 = b2;
                if (e != null) {
                    list2 = list;
                    b3 = b2;
                    if (e.performCreateOptionsMenu(menu, menuInflater)) {
                        b3 = true;
                        if ((list2 = list) == null) {
                            list2 = new ArrayList<Fragment>();
                        }
                        list2.add(e);
                    }
                }
                ++index;
                list = list2;
                b2 = b3;
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); ++i) {
                final Fragment o = this.mCreatedMenus.get(i);
                if (mCreatedMenus == null || !mCreatedMenus.contains(o)) {
                    o.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = mCreatedMenus;
        return b;
    }
    
    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.moveToState(0, false);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }
    
    public void dispatchDestroyView() {
        this.moveToState(1, false);
    }
    
    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performLowMemory();
                }
            }
        }
    }
    
    public void dispatchMultiWindowModeChanged(final boolean b) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performMultiWindowModeChanged(b);
                }
            }
        }
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                    return true;
                }
            }
            return false;
        }
        return false;
        return false;
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performOptionsMenuClosed(menu);
                }
            }
        }
    }
    
    public void dispatchPause() {
        this.moveToState(4, false);
    }
    
    public void dispatchPictureInPictureModeChanged(final boolean b) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performPictureInPictureModeChanged(b);
                }
            }
        }
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        boolean b = false;
        boolean b2 = false;
        if (this.mAdded != null) {
            int index = 0;
            while (true) {
                b = b2;
                if (index >= this.mAdded.size()) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(index);
                boolean b3 = b2;
                if (fragment != null) {
                    b3 = b2;
                    if (fragment.performPrepareOptionsMenu(menu)) {
                        b3 = true;
                    }
                }
                ++index;
                b2 = b3;
            }
        }
        return b;
    }
    
    public void dispatchReallyStop() {
        this.moveToState(2, false);
    }
    
    public void dispatchResume() {
        this.moveToState(5, this.mStateSaved = false);
    }
    
    public void dispatchStart() {
        this.moveToState(4, this.mStateSaved = false);
    }
    
    public void dispatchStop() {
        this.mStateSaved = true;
        this.moveToState(3, false);
    }
    
    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            int n = 0;
            boolean b;
            for (int i = 0; i < this.mActive.size(); ++i, n = (b ? 1 : 0)) {
                final Fragment fragment = this.mActive.get(i);
                b = (n != 0);
                if (fragment != null) {
                    b = (n != 0);
                    if (fragment.mLoaderManager != null) {
                        b = ((n | (fragment.mLoaderManager.hasRunningLoaders() ? 1 : 0)) != 0x0);
                    }
                }
            }
            if (n == 0) {
                this.mHavePendingDeferredStart = false;
                this.startPendingDeferredFragments();
            }
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final String string = s + "    ";
        if (this.mActive != null) {
            final int size = this.mActive.size();
            if (size > 0) {
                printWriter.print(s);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; ++i) {
                    final Fragment x = this.mActive.get(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(x);
                    if (x != null) {
                        x.dump(string, fileDescriptor, printWriter, array);
                    }
                }
            }
        }
        if (this.mAdded != null) {
            final int size2 = this.mAdded.size();
            if (size2 > 0) {
                printWriter.print(s);
                printWriter.println("Added Fragments:");
                for (int j = 0; j < size2; ++j) {
                    final Fragment fragment = this.mAdded.get(j);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(j);
                    printWriter.print(": ");
                    printWriter.println(fragment.toString());
                }
            }
        }
        if (this.mCreatedMenus != null) {
            final int size3 = this.mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(s);
                printWriter.println("Fragments Created Menus:");
                for (int k = 0; k < size3; ++k) {
                    final Fragment fragment2 = this.mCreatedMenus.get(k);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(k);
                    printWriter.print(": ");
                    printWriter.println(fragment2.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            final int size4 = this.mBackStack.size();
            if (size4 > 0) {
                printWriter.print(s);
                printWriter.println("Back Stack:");
                for (int l = 0; l < size4; ++l) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(l);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(l);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                final int size5 = this.mBackStackIndices.size();
                if (size5 > 0) {
                    printWriter.print(s);
                    printWriter.println("Back Stack Indices:");
                    for (int n = 0; n < size5; ++n) {
                        final BackStackRecord x2 = this.mBackStackIndices.get(n);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n);
                        printWriter.print(": ");
                        printWriter.println(x2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(s);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            // monitorexit(this)
            if (this.mPendingActions != null) {
                final int size6 = this.mPendingActions.size();
                if (size6 > 0) {
                    printWriter.print(s);
                    printWriter.println("Pending Actions:");
                    for (int n2 = 0; n2 < size6; ++n2) {
                        final Runnable x3 = this.mPendingActions.get(n2);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n2);
                        printWriter.print(": ");
                        printWriter.println(x3);
                    }
                }
            }
        }
        final String s2;
        printWriter.print(s2);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(s2);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(s2);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(s2);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(s2);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(s2);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(s2);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
        }
        if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            printWriter.print(s2);
            printWriter.print("  mAvailIndices: ");
            printWriter.println(Arrays.toString(this.mAvailIndices.toArray()));
        }
    }
    
    public void enqueueAction(final Runnable runnable, final boolean b) {
        if (!b) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (this.mDestroyed || this.mHost == null) {
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
        if (this.mPendingActions == null) {
            this.mPendingActions = new ArrayList<Runnable>();
        }
        final Runnable e;
        this.mPendingActions.add(e);
        if (this.mPendingActions.size() == 1) {
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            this.mHost.getHandler().post(this.mExecCommit);
        }
    }
    // monitorexit(this)
    
    public boolean execPendingActions() {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        boolean b = false;
        while (true) {
            synchronized (this) {
                if (this.mPendingActions == null || this.mPendingActions.size() == 0) {
                    // monitorexit(this)
                    this.doPendingDeferredStart();
                    return b;
                }
                final int size = this.mPendingActions.size();
                if (this.mTmpActions == null || this.mTmpActions.length < size) {
                    this.mTmpActions = new Runnable[size];
                }
                this.mPendingActions.toArray(this.mTmpActions);
                this.mPendingActions.clear();
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                // monitorexit(this)
                this.mExecutingActions = true;
                for (int i = 0; i < size; ++i) {
                    this.mTmpActions[i].run();
                    this.mTmpActions[i] = null;
                }
            }
            this.mExecutingActions = false;
            b = true;
        }
    }
    
    public void execSingleAction(final Runnable runnable, final boolean b) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        if (!b) {
            this.checkStateLoss();
        }
        this.mExecutingActions = true;
        runnable.run();
        this.mExecutingActions = false;
        this.doPendingDeferredStart();
    }
    
    @Override
    public boolean executePendingTransactions() {
        return this.execPendingActions();
    }
    
    @Override
    public Fragment findFragmentById(final int n) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.mFragmentId == n) {
                    return fragment;
                }
            }
        }
        Label_0053: {
            break Label_0053;
        }
        if (this.mActive != null) {
            for (int j = this.mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = this.mActive.get(j);
                if (fragment2 != null) {
                    final Fragment fragment = fragment2;
                    if (fragment2.mFragmentId == n) {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Fragment findFragmentByTag(final String s) {
        if (this.mAdded != null && s != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && s.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        Label_0060: {
            break Label_0060;
        }
        if (this.mActive != null && s != null) {
            for (int j = this.mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = this.mActive.get(j);
                if (fragment2 != null) {
                    final Fragment fragment = fragment2;
                    if (s.equals(fragment2.mTag)) {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }
    
    public Fragment findFragmentByWho(final String s) {
        if (this.mActive != null && s != null) {
            for (int i = this.mActive.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mActive.get(i);
                if (fragment != null) {
                    final Fragment fragmentByWho = fragment.findFragmentByWho(s);
                    if (fragmentByWho != null) {
                        return fragmentByWho;
                    }
                }
            }
            return null;
        }
        return null;
        return null;
    }
    
    public void freeBackStackIndex(final int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<Integer>();
            }
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Freeing back stack index " + i);
            }
            this.mAvailBackStackIndices.add(i);
        }
    }
    
    @Override
    public BackStackEntry getBackStackEntryAt(final int index) {
        return this.mBackStack.get(index);
    }
    
    @Override
    public int getBackStackEntryCount() {
        int size;
        if (this.mBackStack != null) {
            size = this.mBackStack.size();
        }
        else {
            size = 0;
        }
        return size;
    }
    
    @Override
    public Fragment getFragment(final Bundle bundle, final String s) {
        final int int1 = bundle.getInt(s, -1);
        Fragment fragment;
        if (int1 == -1) {
            fragment = null;
        }
        else {
            if (int1 >= this.mActive.size()) {
                this.throwException(new IllegalStateException("Fragment no longer exists for key " + s + ": index " + int1));
            }
            final Fragment fragment2 = this.mActive.get(int1);
            if ((fragment = fragment2) == null) {
                this.throwException(new IllegalStateException("Fragment no longer exists for key " + s + ": index " + int1));
                fragment = fragment2;
            }
        }
        return fragment;
    }
    
    @Override
    public List<Fragment> getFragments() {
        return this.mActive;
    }
    
    LayoutInflaterFactory getLayoutInflaterFactory() {
        return this;
    }
    
    public void hideFragment(final Fragment obj, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "hide: " + obj);
        }
        if (!obj.mHidden) {
            obj.mHidden = true;
            if (obj.mView != null) {
                final Animation loadAnimation = this.loadAnimation(obj, n, false, n2);
                if (loadAnimation != null) {
                    this.setHWLayerAnimListenerIfAlpha(obj.mView, loadAnimation);
                    obj.mView.startAnimation(loadAnimation);
                }
                obj.mView.setVisibility(8);
            }
            if (obj.mAdded && obj.mHasMenu && obj.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            obj.onHiddenChanged(true);
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }
    
    boolean isStateAtLeast(final int n) {
        return this.mCurState >= n;
    }
    
    Animation loadAnimation(final Fragment fragment, int n, final boolean b, final int n2) {
        final Animation onCreateAnimation = fragment.onCreateAnimation(n, b, fragment.mNextAnim);
        Animation animation;
        if (onCreateAnimation != null) {
            animation = onCreateAnimation;
        }
        else {
            if (fragment.mNextAnim != 0) {
                animation = AnimationUtils.loadAnimation(this.mHost.getContext(), fragment.mNextAnim);
                if (animation != null) {
                    return animation;
                }
            }
            if (n == 0) {
                animation = null;
            }
            else {
                n = transitToStyleIndex(n, b);
                if (n < 0) {
                    animation = null;
                }
                else {
                    switch (n) {
                        default: {
                            n = n2;
                            if (n2 == 0) {
                                n = n2;
                                if (this.mHost.onHasWindowAnimations()) {
                                    n = this.mHost.onGetWindowAnimations();
                                }
                            }
                            if (n == 0) {
                                animation = null;
                                break;
                            }
                            animation = null;
                            break;
                        }
                        case 1: {
                            animation = makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
                            break;
                        }
                        case 2: {
                            animation = makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
                            break;
                        }
                        case 3: {
                            animation = makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
                            break;
                        }
                        case 4: {
                            animation = makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
                            break;
                        }
                        case 5: {
                            animation = makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
                            break;
                        }
                        case 6: {
                            animation = makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
                            break;
                        }
                    }
                }
            }
        }
        return animation;
    }
    
    void makeActive(final Fragment obj) {
        if (obj.mIndex < 0) {
            if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
                if (this.mActive == null) {
                    this.mActive = new ArrayList<Fragment>();
                }
                obj.setIndex(this.mActive.size(), this.mParent);
                this.mActive.add(obj);
            }
            else {
                obj.setIndex(this.mAvailIndices.remove(this.mAvailIndices.size() - 1), this.mParent);
                this.mActive.set(obj.mIndex, obj);
            }
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Allocated fragment index " + obj);
            }
        }
    }
    
    void makeInactive(final Fragment obj) {
        if (obj.mIndex >= 0) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Freeing fragment index " + obj);
            }
            this.mActive.set(obj.mIndex, null);
            if (this.mAvailIndices == null) {
                this.mAvailIndices = new ArrayList<Integer>();
            }
            this.mAvailIndices.add(obj.mIndex);
            this.mHost.inactivateFragment(obj.mWho);
            obj.initState();
        }
    }
    
    void moveToState(final int mCurState, final int n, final int n2, final boolean b) {
        if (this.mHost == null && mCurState != 0) {
            throw new IllegalStateException("No host");
        }
        if (b || this.mCurState != mCurState) {
            this.mCurState = mCurState;
            if (this.mActive != null) {
                int n3 = 0;
                boolean b2;
                for (int i = 0; i < this.mActive.size(); ++i, n3 = (b2 ? 1 : 0)) {
                    final Fragment fragment = this.mActive.get(i);
                    b2 = (n3 != 0);
                    if (fragment != null) {
                        this.moveToState(fragment, mCurState, n, n2, false);
                        b2 = (n3 != 0);
                        if (fragment.mLoaderManager != null) {
                            b2 = ((n3 | (fragment.mLoaderManager.hasRunningLoaders() ? 1 : 0)) != 0x0);
                        }
                    }
                }
                if (n3 == 0) {
                    this.startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }
    
    void moveToState(final int n, final boolean b) {
        this.moveToState(n, 0, 0, b);
    }
    
    void moveToState(final Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    void moveToState(final Fragment obj, int n, final int n2, final int n3, final boolean b) {
        int n4 = 0;
        Label_0028: {
            if (obj.mAdded) {
                n4 = n;
                if (!obj.mDetached) {
                    break Label_0028;
                }
            }
            if ((n4 = n) > 1) {
                n4 = 1;
            }
        }
        int mState = n4;
        if (obj.mRemoving && (mState = n4) > obj.mState) {
            mState = obj.mState;
        }
        n = mState;
        if (obj.mDeferStart) {
            n = mState;
            if (obj.mState < 4 && (n = mState) > 3) {
                n = 3;
            }
        }
        int n5 = 0;
        Label_0191: {
            if (obj.mState < n) {
                if (!obj.mFromLayout || obj.mInLayout) {
                    if (obj.mAnimatingAway != null) {
                        obj.mAnimatingAway = null;
                        this.moveToState(obj, obj.mStateAfterAnimating, 0, 0, true);
                    }
                    n5 = n;
                    int n6 = n;
                    int n7 = n;
                    int n8 = n;
                    int n9 = 0;
                    FragmentManagerImpl mFragmentManager;
                    Object mContainer;
                    Object loadAnimation;
                    String resourceName;
                    Label_0649:Label_1191_Outer:
                    while (true) {
                        Label_0617: {
                            Label_1175_Outer:Label_1205_Outer:
                            while (true) {
                                while (true) {
                                    Label_0549: {
                                        while (true) {
                                            Label_1000:Label_1051_Outer:
                                            while (true) {
                                            Label_1101_Outer:
                                                while (true) {
                                                    while (true) {
                                                        switch (obj.mState) {
                                                            default: {
                                                                n5 = n;
                                                                break Label_0191;
                                                            }
                                                            case 0: {
                                                                if (FragmentManagerImpl.DEBUG) {
                                                                    Log.v("FragmentManager", "moveto CREATED: " + obj);
                                                                }
                                                                n9 = n;
                                                                if (obj.mSavedFragmentState != null) {
                                                                    obj.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                                                                    obj.mSavedViewState = (SparseArray<Parcelable>)obj.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                                                                    obj.mTarget = this.getFragment(obj.mSavedFragmentState, "android:target_state");
                                                                    if (obj.mTarget != null) {
                                                                        obj.mTargetRequestCode = obj.mSavedFragmentState.getInt("android:target_req_state", 0);
                                                                    }
                                                                    obj.mUserVisibleHint = obj.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                                                                    n9 = n;
                                                                    if (!obj.mUserVisibleHint) {
                                                                        obj.mDeferStart = true;
                                                                        if ((n9 = n) > 3) {
                                                                            n9 = 3;
                                                                        }
                                                                    }
                                                                }
                                                                obj.mHost = this.mHost;
                                                                obj.mParentFragment = this.mParent;
                                                                if (this.mParent != null) {
                                                                    mFragmentManager = this.mParent.mChildFragmentManager;
                                                                }
                                                                else {
                                                                    mFragmentManager = this.mHost.getFragmentManagerImpl();
                                                                }
                                                                obj.mFragmentManager = mFragmentManager;
                                                                obj.mCalled = false;
                                                                obj.onAttach(this.mHost.getContext());
                                                                if (!obj.mCalled) {
                                                                    throw new SuperNotCalledException("Fragment " + obj + " did not call through to super.onAttach()");
                                                                }
                                                                if (obj.mParentFragment == null) {
                                                                    this.mHost.onAttachFragment(obj);
                                                                    break;
                                                                }
                                                                break Label_1000;
                                                            }
                                                            case 1: {
                                                                if ((n6 = n5) <= 1) {
                                                                    break Label_1000;
                                                                }
                                                                if (FragmentManagerImpl.DEBUG) {
                                                                    Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + obj);
                                                                }
                                                                if (obj.mFromLayout) {
                                                                    break Label_1000;
                                                                }
                                                                mContainer = null;
                                                                if (obj.mContainerId == 0) {
                                                                    break Label_1101_Outer;
                                                                }
                                                                if (obj.mContainerId == -1) {
                                                                    this.throwException(new IllegalArgumentException("Cannot create fragment " + obj + " for a container view with no id"));
                                                                }
                                                                loadAnimation = this.mContainer.onFindViewById(obj.mContainerId);
                                                                if ((mContainer = loadAnimation) != null) {
                                                                    break Label_1101_Outer;
                                                                }
                                                                mContainer = loadAnimation;
                                                                if (!obj.mRestored) {
                                                                    break Label_1101_Outer;
                                                                }
                                                                break Label_1101_Outer;
                                                            }
                                                            case 2: {
                                                                break Label_1000;
                                                            Label_0923_Outer:
                                                                while (true) {
                                                                    Label_1241: {
                                                                        while (true) {
                                                                        Label_1227:
                                                                            while (true) {
                                                                                try {
                                                                                    resourceName = obj.getResources().getResourceName(obj.mContainerId);
                                                                                    this.throwException(new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(obj.mContainerId) + " (" + resourceName + ") for fragment " + obj));
                                                                                    mContainer = loadAnimation;
                                                                                    obj.mContainer = (ViewGroup)mContainer;
                                                                                    obj.mView = obj.performCreateView(obj.getLayoutInflater(obj.mSavedFragmentState), (ViewGroup)mContainer, obj.mSavedFragmentState);
                                                                                    if (obj.mView == null) {
                                                                                        break Label_1241;
                                                                                    }
                                                                                    obj.mInnerView = obj.mView;
                                                                                    if (Build$VERSION.SDK_INT < 11) {
                                                                                        break Label_1227;
                                                                                    }
                                                                                    ViewCompat.setSaveFromParentEnabled(obj.mView, false);
                                                                                    if (mContainer != null) {
                                                                                        loadAnimation = this.loadAnimation(obj, n2, true, n3);
                                                                                        if (loadAnimation != null) {
                                                                                            this.setHWLayerAnimListenerIfAlpha(obj.mView, (Animation)loadAnimation);
                                                                                            obj.mView.startAnimation((Animation)loadAnimation);
                                                                                        }
                                                                                        ((ViewGroup)mContainer).addView(obj.mView);
                                                                                    }
                                                                                    if (obj.mHidden) {
                                                                                        obj.mView.setVisibility(8);
                                                                                    }
                                                                                    obj.onViewCreated(obj.mView, obj.mSavedFragmentState);
                                                                                    obj.performActivityCreated(obj.mSavedFragmentState);
                                                                                    if (obj.mView != null) {
                                                                                        obj.restoreViewState(obj.mSavedFragmentState);
                                                                                    }
                                                                                    obj.mSavedFragmentState = null;
                                                                                    n6 = n5;
                                                                                    if ((n7 = n6) > 2) {
                                                                                        obj.mState = 3;
                                                                                        n7 = n6;
                                                                                    }
                                                                                    if ((n8 = n7) > 3) {
                                                                                        if (FragmentManagerImpl.DEBUG) {
                                                                                            Log.v("FragmentManager", "moveto STARTED: " + obj);
                                                                                        }
                                                                                        obj.performStart();
                                                                                        n8 = n7;
                                                                                    }
                                                                                    if ((n5 = n8) > 4) {
                                                                                        if (FragmentManagerImpl.DEBUG) {
                                                                                            Log.v("FragmentManager", "moveto RESUMED: " + obj);
                                                                                        }
                                                                                        obj.performResume();
                                                                                        obj.mSavedFragmentState = null;
                                                                                        obj.mSavedViewState = null;
                                                                                        n5 = n8;
                                                                                    }
                                                                                    break Label_0191;
                                                                                    obj.mView = (View)NoSaveStateFrameLayout.wrap(obj.mView);
                                                                                    break Label_0617;
                                                                                    obj.mParentFragment.onAttachFragment(obj);
                                                                                    break Label_1000;
                                                                                    obj.restoreChildFragmentState(obj.mSavedFragmentState);
                                                                                    obj.mState = 1;
                                                                                    break Label_0549;
                                                                                    obj.mInnerView = null;
                                                                                    n5 = n9;
                                                                                    continue Label_0649;
                                                                                }
                                                                                catch (Resources$NotFoundException ex) {
                                                                                    resourceName = "unknown";
                                                                                    continue Label_0923_Outer;
                                                                                }
                                                                                break;
                                                                            }
                                                                            obj.mView = (View)NoSaveStateFrameLayout.wrap(obj.mView);
                                                                            continue Label_1051_Outer;
                                                                        }
                                                                    }
                                                                    obj.mInnerView = null;
                                                                    continue Label_1000;
                                                                }
                                                                break;
                                                            }
                                                            case 3: {
                                                                continue Label_1101_Outer;
                                                            }
                                                            case 4: {
                                                                continue Label_1191_Outer;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    break;
                                                }
                                                break;
                                            }
                                            if (obj.mRetaining) {
                                                continue Label_1205_Outer;
                                            }
                                            break;
                                        }
                                        obj.performCreate(obj.mSavedFragmentState);
                                    }
                                    obj.mRetaining = false;
                                    n5 = n9;
                                    if (!obj.mFromLayout) {
                                        continue Label_0649;
                                    }
                                    obj.mView = obj.performCreateView(obj.getLayoutInflater(obj.mSavedFragmentState), null, obj.mSavedFragmentState);
                                    if (obj.mView == null) {
                                        continue;
                                    }
                                    break;
                                }
                                obj.mInnerView = obj.mView;
                                if (Build$VERSION.SDK_INT < 11) {
                                    continue Label_1175_Outer;
                                }
                                break;
                            }
                            ViewCompat.setSaveFromParentEnabled(obj.mView, false);
                        }
                        if (obj.mHidden) {
                            obj.mView.setVisibility(8);
                        }
                        obj.onViewCreated(obj.mView, obj.mSavedFragmentState);
                        n5 = n9;
                        continue Label_0649;
                    }
                }
            }
            else {
                if (obj.mState <= (n5 = n)) {
                    break Label_0191;
                }
                switch (obj.mState) {
                    default: {
                        n5 = n;
                        break Label_0191;
                    }
                    case 3: {
                        if (n < 3) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom STOPPED: " + obj);
                            }
                            obj.performReallyStop();
                        }
                    }
                    case 2: {
                        if (n < 2) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + obj);
                            }
                            if (obj.mView != null && this.mHost.onShouldSaveFragmentState(obj) && obj.mSavedViewState == null) {
                                this.saveFragmentViewState(obj);
                            }
                            obj.performDestroyView();
                            if (obj.mView != null && obj.mContainer != null) {
                                Animation loadAnimation2;
                                final Animation animation = loadAnimation2 = null;
                                if (this.mCurState > 0) {
                                    loadAnimation2 = animation;
                                    if (!this.mDestroyed) {
                                        loadAnimation2 = this.loadAnimation(obj, n2, false, n3);
                                    }
                                }
                                if (loadAnimation2 != null) {
                                    obj.mAnimatingAway = obj.mView;
                                    obj.mStateAfterAnimating = n;
                                    loadAnimation2.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(obj.mView, loadAnimation2) {
                                        @Override
                                        public void onAnimationEnd(final Animation animation) {
                                            super.onAnimationEnd(animation);
                                            if (obj.mAnimatingAway != null) {
                                                obj.mAnimatingAway = null;
                                                FragmentManagerImpl.this.moveToState(obj, obj.mStateAfterAnimating, 0, 0, false);
                                            }
                                        }
                                    });
                                    obj.mView.startAnimation(loadAnimation2);
                                }
                                obj.mContainer.removeView(obj.mView);
                            }
                            obj.mContainer = null;
                            obj.mView = null;
                            obj.mInnerView = null;
                        }
                    }
                    case 1: {
                        if ((n5 = n) >= 1) {
                            break Label_0191;
                        }
                        if (this.mDestroyed && obj.mAnimatingAway != null) {
                            final View mAnimatingAway = obj.mAnimatingAway;
                            obj.mAnimatingAway = null;
                            mAnimatingAway.clearAnimation();
                        }
                        if (obj.mAnimatingAway != null) {
                            obj.mStateAfterAnimating = n;
                            n5 = 1;
                            break Label_0191;
                        }
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "movefrom CREATED: " + obj);
                        }
                        if (!obj.mRetaining) {
                            obj.performDestroy();
                        }
                        else {
                            obj.mState = 0;
                        }
                        obj.performDetach();
                        n5 = n;
                        if (b) {
                            break Label_0191;
                        }
                        if (!obj.mRetaining) {
                            this.makeInactive(obj);
                            n5 = n;
                            break Label_0191;
                        }
                        obj.mHost = null;
                        obj.mParentFragment = null;
                        obj.mFragmentManager = null;
                        n5 = n;
                        break Label_0191;
                    }
                    case 5: {
                        if (n < 5) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom RESUMED: " + obj);
                            }
                            obj.performPause();
                        }
                    }
                    case 4: {
                        if (n < 4) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom STARTED: " + obj);
                            }
                            obj.performStop();
                        }
                    }
                }
            }
            return;
        }
        if (obj.mState != n5) {
            Log.w("FragmentManager", "moveToState: Fragment state for " + obj + " not updated inline; " + "expected state " + n5 + " found " + obj.mState);
            obj.mState = n5;
        }
    }
    
    public void noteStateNotSaved() {
        this.mStateSaved = false;
    }
    
    @Override
    public View onCreateView(final View view, String attributeValue, final Context context, final AttributeSet set) {
        final View view2 = null;
        View mView;
        if (!"fragment".equals(attributeValue)) {
            mView = view2;
        }
        else {
            attributeValue = set.getAttributeValue((String)null, "class");
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FragmentTag.Fragment);
            String string;
            if ((string = attributeValue) == null) {
                string = obtainStyledAttributes.getString(0);
            }
            final int resourceId = obtainStyledAttributes.getResourceId(1, -1);
            final String string2 = obtainStyledAttributes.getString(2);
            obtainStyledAttributes.recycle();
            mView = view2;
            if (Fragment.isSupportFragmentClass(this.mHost.getContext(), string)) {
                int id;
                if (view != null) {
                    id = view.getId();
                }
                else {
                    id = 0;
                }
                if (id == -1 && resourceId == -1 && string2 == null) {
                    throw new IllegalArgumentException(set.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + string);
                }
                Fragment fragmentById;
                if (resourceId != -1) {
                    fragmentById = this.findFragmentById(resourceId);
                }
                else {
                    fragmentById = null;
                }
                Fragment fragmentByTag = fragmentById;
                if (fragmentById == null) {
                    fragmentByTag = fragmentById;
                    if (string2 != null) {
                        fragmentByTag = this.findFragmentByTag(string2);
                    }
                }
                Fragment fragmentById2;
                if ((fragmentById2 = fragmentByTag) == null) {
                    fragmentById2 = fragmentByTag;
                    if (id != -1) {
                        fragmentById2 = this.findFragmentById(id);
                    }
                }
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(resourceId) + " fname=" + string + " existing=" + fragmentById2);
                }
                Fragment instantiate;
                if (fragmentById2 == null) {
                    instantiate = Fragment.instantiate(context, string);
                    instantiate.mFromLayout = true;
                    int mFragmentId;
                    if (resourceId != 0) {
                        mFragmentId = resourceId;
                    }
                    else {
                        mFragmentId = id;
                    }
                    instantiate.mFragmentId = mFragmentId;
                    instantiate.mContainerId = id;
                    instantiate.mTag = string2;
                    instantiate.mInLayout = true;
                    instantiate.mFragmentManager = this;
                    instantiate.mHost = this.mHost;
                    instantiate.onInflate(this.mHost.getContext(), set, instantiate.mSavedFragmentState);
                    this.addFragment(instantiate, true);
                }
                else {
                    if (fragmentById2.mInLayout) {
                        throw new IllegalArgumentException(set.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string2 + ", or parent id 0x" + Integer.toHexString(id) + " with another fragment for " + string);
                    }
                    fragmentById2.mInLayout = true;
                    fragmentById2.mHost = this.mHost;
                    instantiate = fragmentById2;
                    if (!fragmentById2.mRetaining) {
                        fragmentById2.onInflate(this.mHost.getContext(), set, fragmentById2.mSavedFragmentState);
                        instantiate = fragmentById2;
                    }
                }
                if (this.mCurState < 1 && instantiate.mFromLayout) {
                    this.moveToState(instantiate, 1, 0, 0, false);
                }
                else {
                    this.moveToState(instantiate);
                }
                if (instantiate.mView == null) {
                    throw new IllegalStateException("Fragment " + string + " did not create a view.");
                }
                if (resourceId != 0) {
                    instantiate.mView.setId(resourceId);
                }
                if (instantiate.mView.getTag() == null) {
                    instantiate.mView.setTag((Object)string2);
                }
                mView = instantiate.mView;
            }
        }
        return mView;
    }
    
    public void performPendingDeferredStart(final Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
            }
            else {
                fragment.mDeferStart = false;
                this.moveToState(fragment, this.mCurState, 0, 0, false);
            }
        }
    }
    
    @Override
    public void popBackStack() {
        this.enqueueAction(new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, -1, 0);
            }
        }, false);
    }
    
    @Override
    public void popBackStack(final int i, final int n) {
        if (i < 0) {
            throw new IllegalArgumentException("Bad id: " + i);
        }
        this.enqueueAction(new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, i, n);
            }
        }, false);
    }
    
    @Override
    public void popBackStack(final String s, final int n) {
        this.enqueueAction(new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), s, -1, n);
            }
        }, false);
    }
    
    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        this.executePendingTransactions();
        return this.popBackStackState(this.mHost.getHandler(), null, -1, 0);
    }
    
    @Override
    public boolean popBackStackImmediate(final int i, final int n) {
        this.checkStateLoss();
        this.executePendingTransactions();
        if (i < 0) {
            throw new IllegalArgumentException("Bad id: " + i);
        }
        return this.popBackStackState(this.mHost.getHandler(), null, i, n);
    }
    
    @Override
    public boolean popBackStackImmediate(final String s, final int n) {
        this.checkStateLoss();
        this.executePendingTransactions();
        return this.popBackStackState(this.mHost.getHandler(), s, -1, n);
    }
    
    boolean popBackStackState(final Handler handler, final String s, int i, int index) {
        boolean b;
        if (this.mBackStack == null) {
            b = false;
        }
        else {
            if (s == null && i < 0 && (index & 0x1) == 0x0) {
                i = this.mBackStack.size() - 1;
                if (i < 0) {
                    b = false;
                    return b;
                }
                final BackStackRecord backStackRecord = this.mBackStack.remove(i);
                final SparseArray sparseArray = new SparseArray();
                final SparseArray sparseArray2 = new SparseArray();
                if (this.mCurState >= 1) {
                    backStackRecord.calculateBackFragments((SparseArray<Fragment>)sparseArray, (SparseArray<Fragment>)sparseArray2);
                }
                backStackRecord.popFromBackStack(true, null, (SparseArray<Fragment>)sparseArray, (SparseArray<Fragment>)sparseArray2);
                this.reportBackStackChanged();
            }
            else {
                int n = -1;
                if (s != null || i >= 0) {
                    int j;
                    for (j = this.mBackStack.size() - 1; j >= 0; --j) {
                        final BackStackRecord backStackRecord2 = this.mBackStack.get(j);
                        if ((s != null && s.equals(backStackRecord2.getName())) || (i >= 0 && i == backStackRecord2.mIndex)) {
                            break;
                        }
                    }
                    if (j < 0) {
                        b = false;
                        return b;
                    }
                    n = j;
                    if ((index & 0x1) != 0x0) {
                        index = j - 1;
                        while (true) {
                            n = index;
                            if (index < 0) {
                                break;
                            }
                            final BackStackRecord backStackRecord3 = this.mBackStack.get(index);
                            if (s == null || !s.equals(backStackRecord3.getName())) {
                                n = index;
                                if (i < 0) {
                                    break;
                                }
                                n = index;
                                if (i != backStackRecord3.mIndex) {
                                    break;
                                }
                            }
                            --index;
                        }
                    }
                }
                if (n == this.mBackStack.size() - 1) {
                    b = false;
                    return b;
                }
                final ArrayList<BackStackRecord> list = new ArrayList<BackStackRecord>();
                for (i = this.mBackStack.size() - 1; i > n; --i) {
                    list.add(this.mBackStack.remove(i));
                }
                index = list.size() - 1;
                final SparseArray sparseArray3 = new SparseArray();
                final SparseArray sparseArray4 = new SparseArray();
                if (this.mCurState >= 1) {
                    for (i = 0; i <= index; ++i) {
                        list.get(i).calculateBackFragments((SparseArray<Fragment>)sparseArray3, (SparseArray<Fragment>)sparseArray4);
                    }
                }
                BackStackRecord.TransitionState popFromBackStack = null;
                BackStackRecord backStackRecord4;
                boolean b2;
                for (i = 0; i <= index; ++i) {
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "Popping back stack state: " + list.get(i));
                    }
                    backStackRecord4 = list.get(i);
                    if (i == index) {
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    popFromBackStack = backStackRecord4.popFromBackStack(b2, popFromBackStack, (SparseArray<Fragment>)sparseArray3, (SparseArray<Fragment>)sparseArray4);
                }
                this.reportBackStackChanged();
            }
            b = true;
        }
        return b;
    }
    
    @Override
    public void putFragment(final Bundle bundle, final String s, final Fragment obj) {
        if (obj.mIndex < 0) {
            this.throwException(new IllegalStateException("Fragment " + obj + " is not currently in the FragmentManager"));
        }
        bundle.putInt(s, obj.mIndex);
    }
    
    public void removeFragment(final Fragment fragment, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean b;
        if (!fragment.isInBackStack()) {
            b = true;
        }
        else {
            b = false;
        }
        if (!fragment.mDetached || b) {
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
            int n3;
            if (b) {
                n3 = 0;
            }
            else {
                n3 = 1;
            }
            this.moveToState(fragment, n3, n, n2, false);
        }
    }
    
    @Override
    public void removeOnBackStackChangedListener(final OnBackStackChangedListener o) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(o);
        }
    }
    
    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }
    
    void restoreAllState(final Parcelable parcelable, final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (parcelable != null) {
            final FragmentManagerState fragmentManagerState = (FragmentManagerState)parcelable;
            if (fragmentManagerState.mActive != null) {
                List<FragmentManagerNonConfig> list = null;
                if (fragmentManagerNonConfig != null) {
                    final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
                    final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
                    int size;
                    if (fragments != null) {
                        size = fragments.size();
                    }
                    else {
                        size = 0;
                    }
                    int n = 0;
                    while (true) {
                        list = childNonConfigs;
                        if (n >= size) {
                            break;
                        }
                        final Fragment fragment = fragments.get(n);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: re-attaching retained " + fragment);
                        }
                        final FragmentState fragmentState = fragmentManagerState.mActive[fragment.mIndex];
                        fragmentState.mInstance = fragment;
                        fragment.mSavedViewState = null;
                        fragment.mBackStackNesting = 0;
                        fragment.mInLayout = false;
                        fragment.mAdded = false;
                        fragment.mTarget = null;
                        if (fragmentState.mSavedFragmentState != null) {
                            fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            fragment.mSavedViewState = (SparseArray<Parcelable>)fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                            fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
                        }
                        ++n;
                    }
                }
                this.mActive = new ArrayList<Fragment>(fragmentManagerState.mActive.length);
                if (this.mAvailIndices != null) {
                    this.mAvailIndices.clear();
                }
                for (int i = 0; i < fragmentManagerState.mActive.length; ++i) {
                    final FragmentState fragmentState2 = fragmentManagerState.mActive[i];
                    if (fragmentState2 != null) {
                        FragmentManagerNonConfig fragmentManagerNonConfig2 = null;
                        if (list != null) {
                            fragmentManagerNonConfig2 = fragmentManagerNonConfig2;
                            if (i < list.size()) {
                                fragmentManagerNonConfig2 = list.get(i);
                            }
                        }
                        final Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mParent, fragmentManagerNonConfig2);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: active #" + i + ": " + instantiate);
                        }
                        this.mActive.add(instantiate);
                        fragmentState2.mInstance = null;
                    }
                    else {
                        this.mActive.add(null);
                        if (this.mAvailIndices == null) {
                            this.mAvailIndices = new ArrayList<Integer>();
                        }
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: avail #" + i);
                        }
                        this.mAvailIndices.add(i);
                    }
                }
                if (fragmentManagerNonConfig != null) {
                    final List<Fragment> fragments2 = fragmentManagerNonConfig.getFragments();
                    int size2;
                    if (fragments2 != null) {
                        size2 = fragments2.size();
                    }
                    else {
                        size2 = 0;
                    }
                    for (int j = 0; j < size2; ++j) {
                        final Fragment obj = fragments2.get(j);
                        if (obj.mTargetIndex >= 0) {
                            if (obj.mTargetIndex < this.mActive.size()) {
                                obj.mTarget = this.mActive.get(obj.mTargetIndex);
                            }
                            else {
                                Log.w("FragmentManager", "Re-attaching retained fragment " + obj + " target no longer exists: " + obj.mTargetIndex);
                                obj.mTarget = null;
                            }
                        }
                    }
                }
                if (fragmentManagerState.mAdded != null) {
                    this.mAdded = new ArrayList<Fragment>(fragmentManagerState.mAdded.length);
                    for (int k = 0; k < fragmentManagerState.mAdded.length; ++k) {
                        final Fragment e = this.mActive.get(fragmentManagerState.mAdded[k]);
                        if (e == null) {
                            this.throwException(new IllegalStateException("No instantiated fragment for index #" + fragmentManagerState.mAdded[k]));
                        }
                        e.mAdded = true;
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: added #" + k + ": " + e);
                        }
                        if (this.mAdded.contains(e)) {
                            throw new IllegalStateException("Already added!");
                        }
                        this.mAdded.add(e);
                    }
                }
                else {
                    this.mAdded = null;
                }
                if (fragmentManagerState.mBackStack != null) {
                    this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
                    for (int l = 0; l < fragmentManagerState.mBackStack.length; ++l) {
                        final BackStackRecord instantiate2 = fragmentManagerState.mBackStack[l].instantiate(this);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: back stack #" + l + " (index " + instantiate2.mIndex + "): " + instantiate2);
                            instantiate2.dump("  ", new PrintWriter(new LogWriter("FragmentManager")), false);
                        }
                        this.mBackStack.add(instantiate2);
                        if (instantiate2.mIndex >= 0) {
                            this.setBackStackIndex(instantiate2.mIndex, instantiate2);
                        }
                    }
                }
                else {
                    this.mBackStack = null;
                }
            }
        }
    }
    
    FragmentManagerNonConfig retainNonConfig() {
        List<Fragment> list = null;
        ArrayList<Fragment> list2 = null;
        List<FragmentManagerNonConfig> list3 = null;
        ArrayList<FragmentManagerNonConfig> list4 = null;
        if (this.mActive != null) {
            int index = 0;
            while (true) {
                list3 = list4;
                list = list2;
                if (index >= this.mActive.size()) {
                    break;
                }
                final Fragment fragment = this.mActive.get(index);
                ArrayList<FragmentManagerNonConfig> list5 = list4;
                ArrayList<Fragment> list6 = list2;
                if (fragment != null) {
                    ArrayList<Fragment> list7 = list2;
                    if (fragment.mRetainInstance) {
                        ArrayList<Fragment> list8;
                        if ((list8 = list2) == null) {
                            list8 = new ArrayList<Fragment>();
                        }
                        list8.add(fragment);
                        fragment.mRetaining = true;
                        int mIndex;
                        if (fragment.mTarget != null) {
                            mIndex = fragment.mTarget.mIndex;
                        }
                        else {
                            mIndex = -1;
                        }
                        fragment.mTargetIndex = mIndex;
                        list7 = list8;
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "retainNonConfig: keeping retained " + fragment);
                            list7 = list8;
                        }
                    }
                    int n = 0;
                    ArrayList<FragmentManagerNonConfig> list9 = list4;
                    if (fragment.mChildFragmentManager != null) {
                        final FragmentManagerNonConfig retainNonConfig = fragment.mChildFragmentManager.retainNonConfig();
                        n = n;
                        list9 = list4;
                        if (retainNonConfig != null) {
                            if ((list9 = list4) == null) {
                                final ArrayList<FragmentManagerNonConfig> list10 = new ArrayList<FragmentManagerNonConfig>();
                                int n2 = 0;
                                while (true) {
                                    list9 = list10;
                                    if (n2 >= index) {
                                        break;
                                    }
                                    list10.add(null);
                                    ++n2;
                                }
                            }
                            list9.add(retainNonConfig);
                            n = 1;
                        }
                    }
                    list5 = list9;
                    list6 = list7;
                    if (list9 != null) {
                        list5 = list9;
                        list6 = list7;
                        if (n == 0) {
                            list9.add(null);
                            list6 = list7;
                            list5 = list9;
                        }
                    }
                }
                ++index;
                list4 = list5;
                list2 = list6;
            }
        }
        FragmentManagerNonConfig fragmentManagerNonConfig;
        if (list == null && list3 == null) {
            fragmentManagerNonConfig = null;
        }
        else {
            fragmentManagerNonConfig = new FragmentManagerNonConfig(list, list3);
        }
        return fragmentManagerNonConfig;
    }
    
    Parcelable saveAllState() {
        final Parcelable parcelable = null;
        this.execPendingActions();
        if (FragmentManagerImpl.HONEYCOMB) {
            this.mStateSaved = true;
        }
        Object o = parcelable;
        if (this.mActive != null) {
            if (this.mActive.size() <= 0) {
                o = parcelable;
            }
            else {
                final int size = this.mActive.size();
                final FragmentState[] mActive = new FragmentState[size];
                int n = 0;
                for (int i = 0; i < size; ++i) {
                    final Fragment obj = this.mActive.get(i);
                    if (obj != null) {
                        if (obj.mIndex < 0) {
                            this.throwException(new IllegalStateException("Failure saving state: active " + obj + " has cleared index: " + obj.mIndex));
                        }
                        final boolean b = true;
                        final FragmentState fragmentState = new FragmentState(obj);
                        mActive[i] = fragmentState;
                        if (obj.mState > 0 && fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = this.saveFragmentBasicState(obj);
                            if (obj.mTarget != null) {
                                if (obj.mTarget.mIndex < 0) {
                                    this.throwException(new IllegalStateException("Failure saving state: " + obj + " has target not in fragment manager: " + obj.mTarget));
                                }
                                if (fragmentState.mSavedFragmentState == null) {
                                    fragmentState.mSavedFragmentState = new Bundle();
                                }
                                this.putFragment(fragmentState.mSavedFragmentState, "android:target_state", obj.mTarget);
                                if (obj.mTargetRequestCode != 0) {
                                    fragmentState.mSavedFragmentState.putInt("android:target_req_state", obj.mTargetRequestCode);
                                }
                            }
                        }
                        else {
                            fragmentState.mSavedFragmentState = obj.mSavedFragmentState;
                        }
                        n = (b ? 1 : 0);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "Saved state of " + obj + ": " + fragmentState.mSavedFragmentState);
                            n = (b ? 1 : 0);
                        }
                    }
                }
                if (n == 0) {
                    o = parcelable;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "saveAllState: no fragments!");
                        o = parcelable;
                    }
                }
                else {
                    final int[] array = null;
                    final BackStackState[] array2 = null;
                    int[] mAdded = array;
                    if (this.mAdded != null) {
                        final int size2 = this.mAdded.size();
                        mAdded = array;
                        if (size2 > 0) {
                            final int[] array3 = new int[size2];
                            int n2 = 0;
                            while (true) {
                                mAdded = array3;
                                if (n2 >= size2) {
                                    break;
                                }
                                array3[n2] = this.mAdded.get(n2).mIndex;
                                if (array3[n2] < 0) {
                                    this.throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(n2) + " has cleared index: " + array3[n2]));
                                }
                                if (FragmentManagerImpl.DEBUG) {
                                    Log.v("FragmentManager", "saveAllState: adding fragment #" + n2 + ": " + this.mAdded.get(n2));
                                }
                                ++n2;
                            }
                        }
                    }
                    BackStackState[] mBackStack = array2;
                    if (this.mBackStack != null) {
                        final int size3 = this.mBackStack.size();
                        mBackStack = array2;
                        if (size3 > 0) {
                            final BackStackState[] array4 = new BackStackState[size3];
                            int index = 0;
                            while (true) {
                                mBackStack = array4;
                                if (index >= size3) {
                                    break;
                                }
                                array4[index] = new BackStackState(this.mBackStack.get(index));
                                if (FragmentManagerImpl.DEBUG) {
                                    Log.v("FragmentManager", "saveAllState: adding back stack #" + index + ": " + this.mBackStack.get(index));
                                }
                                ++index;
                            }
                        }
                    }
                    final FragmentManagerState fragmentManagerState = new FragmentManagerState();
                    fragmentManagerState.mActive = mActive;
                    fragmentManagerState.mAdded = mAdded;
                    fragmentManagerState.mBackStack = mBackStack;
                    o = fragmentManagerState;
                }
            }
        }
        return (Parcelable)o;
    }
    
    Bundle saveFragmentBasicState(final Fragment fragment) {
        Bundle mStateBundle = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        if (!this.mStateBundle.isEmpty()) {
            mStateBundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        Bundle bundle = mStateBundle;
        if (fragment.mSavedViewState != null) {
            if ((bundle = mStateBundle) == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", (SparseArray)fragment.mSavedViewState);
        }
        Bundle bundle2 = bundle;
        if (!fragment.mUserVisibleHint) {
            if ((bundle2 = bundle) == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle2;
    }
    
    @Override
    public Fragment.SavedState saveFragmentInstanceState(final Fragment obj) {
        final Fragment.SavedState savedState = null;
        if (obj.mIndex < 0) {
            this.throwException(new IllegalStateException("Fragment " + obj + " is not currently in the FragmentManager"));
        }
        Fragment.SavedState savedState2 = savedState;
        if (obj.mState > 0) {
            final Bundle saveFragmentBasicState = this.saveFragmentBasicState(obj);
            savedState2 = savedState;
            if (saveFragmentBasicState != null) {
                savedState2 = new Fragment.SavedState(saveFragmentBasicState);
            }
        }
        return savedState2;
    }
    
    void saveFragmentViewState(final Fragment fragment) {
        if (fragment.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = (SparseArray<Parcelable>)new SparseArray();
            }
            else {
                this.mStateArray.clear();
            }
            fragment.mInnerView.saveHierarchyState((SparseArray)this.mStateArray);
            if (this.mStateArray.size() > 0) {
                fragment.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }
    
    public void setBackStackIndex(final int i, final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            int j;
            if (i < (j = this.mBackStackIndices.size())) {
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Setting back stack index " + i + " to " + backStackRecord);
                }
                this.mBackStackIndices.set(i, backStackRecord);
            }
            else {
                while (j < i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<Integer>();
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "Adding available back stack index " + j);
                    }
                    this.mAvailBackStackIndices.add(j);
                    ++j;
                }
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Adding back stack index " + i + " with " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }
    
    public void showFragment(final Fragment obj, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "show: " + obj);
        }
        if (obj.mHidden) {
            obj.mHidden = false;
            if (obj.mView != null) {
                final Animation loadAnimation = this.loadAnimation(obj, n, true, n2);
                if (loadAnimation != null) {
                    this.setHWLayerAnimListenerIfAlpha(obj.mView, loadAnimation);
                    obj.mView.startAnimation(loadAnimation);
                }
                obj.mView.setVisibility(0);
            }
            if (obj.mAdded && obj.mHasMenu && obj.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            obj.onHiddenChanged(false);
        }
    }
    
    void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); ++i) {
                final Fragment fragment = this.mActive.get(i);
                if (fragment != null) {
                    this.performPendingDeferredStart(fragment);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        }
        else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
    
    static class AnimateOnHWLayerIfNeededListener implements Animation$AnimationListener
    {
        private Animation$AnimationListener mOriginalListener;
        private boolean mShouldRunOnHWLayer;
        View mView;
        
        public AnimateOnHWLayerIfNeededListener(final View mView, final Animation animation) {
            if (mView != null && animation != null) {
                this.mView = mView;
            }
        }
        
        public AnimateOnHWLayerIfNeededListener(final View mView, final Animation animation, final Animation$AnimationListener mOriginalListener) {
            if (mView != null && animation != null) {
                this.mOriginalListener = mOriginalListener;
                this.mView = mView;
                this.mShouldRunOnHWLayer = true;
            }
        }
        
        @CallSuper
        public void onAnimationEnd(final Animation animation) {
            if (this.mView != null && this.mShouldRunOnHWLayer) {
                if (ViewCompat.isAttachedToWindow(this.mView) || BuildCompat.isAtLeastN()) {
                    this.mView.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
                        }
                    });
                }
                else {
                    ViewCompat.setLayerType(this.mView, 0, null);
                }
            }
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationEnd(animation);
            }
        }
        
        public void onAnimationRepeat(final Animation animation) {
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationRepeat(animation);
            }
        }
        
        @CallSuper
        public void onAnimationStart(final Animation animation) {
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationStart(animation);
            }
        }
    }
    
    static class FragmentTag
    {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;
        
        static {
            Fragment = new int[] { 16842755, 16842960, 16842961 };
        }
    }
}
