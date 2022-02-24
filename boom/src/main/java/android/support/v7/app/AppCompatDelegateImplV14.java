// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.app;

import android.support.v7.view.SupportActionModeWrapper;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.view.Window$Callback;
import android.util.Log;
import android.os.Bundle;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.app.UiModeManager;
import android.view.Window;
import android.content.Context;

class AppCompatDelegateImplV14 extends AppCompatDelegateImplV11
{
    private static final String KEY_LOCAL_NIGHT_MODE = "appcompat:local_night_mode";
    private static TwilightManager sTwilightManager;
    private boolean mApplyDayNightCalled;
    private boolean mHandleNativeActionModes;
    private int mLocalNightMode;
    
    AppCompatDelegateImplV14(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
        this.mLocalNightMode = -100;
        this.mHandleNativeActionModes = true;
    }
    
    private int getNightModeToApply() {
        int n;
        if (this.mLocalNightMode == -100) {
            n = AppCompatDelegate.getDefaultNightMode();
        }
        else {
            n = this.mLocalNightMode;
        }
        return this.mapNightModeToYesNo(n);
    }
    
    private TwilightManager getTwilightManager() {
        if (AppCompatDelegateImplV14.sTwilightManager == null) {
            AppCompatDelegateImplV14.sTwilightManager = new TwilightManager(this.mContext.getApplicationContext());
        }
        return AppCompatDelegateImplV14.sTwilightManager;
    }
    
    private int mapNightModeToYesNo(final int n) {
        int n3;
        final int n2 = n3 = 2;
        switch (n) {
            default: {
                n3 = 1;
                return n3;
            }
            case 2: {
                return n3;
            }
            case 0: {
                n3 = n2;
                if (!this.getTwilightManager().isNight()) {
                    n3 = 1;
                    return n3;
                }
                return n3;
            }
            case -1: {
                n3 = n2;
                switch (((UiModeManager)this.mContext.getSystemService("uimode")).getNightMode()) {
                    case 0: {
                        n3 = 0;
                    }
                    case 2: {
                        return n3;
                    }
                    default: {
                        n3 = 1;
                        return n3;
                    }
                }
                break;
            }
        }
    }
    
    private boolean updateConfigurationForNightMode(int n) {
        final Resources resources = this.mContext.getResources();
        final Configuration configuration = resources.getConfiguration();
        final int uiMode = configuration.uiMode;
        final int n2 = 0;
        switch (n) {
            default: {
                n = n2;
                break;
            }
            case 1: {
                n = 16;
                break;
            }
            case 2: {
                n = 32;
                break;
            }
        }
        boolean b;
        if ((uiMode & 0x30) != n) {
            configuration.uiMode = ((configuration.uiMode & 0xFFFFFFCF) | n);
            resources.updateConfiguration(configuration, (DisplayMetrics)null);
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean applyDayNight() {
        this.mApplyDayNightCalled = true;
        return this.updateConfigurationForNightMode(this.getNightModeToApply());
    }
    
    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return this.mHandleNativeActionModes;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null && this.mLocalNightMode == -100) {
            this.mLocalNightMode = bundle.getInt("appcompat:local_night_mode", -100);
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mLocalNightMode != -100) {
            bundle.putInt("appcompat:local_night_mode", this.mLocalNightMode);
        }
    }
    
    @Override
    public void setHandleNativeActionModesEnabled(final boolean mHandleNativeActionModes) {
        this.mHandleNativeActionModes = mHandleNativeActionModes;
    }
    
    @Override
    public void setLocalNightMode(final int mLocalNightMode) {
        switch (mLocalNightMode) {
            default: {
                Log.d("AppCompatDelegate", "setLocalNightMode() called with an unknown mode");
                break;
            }
            case -1:
            case 0:
            case 1:
            case 2: {
                if (this.mLocalNightMode == mLocalNightMode) {
                    break;
                }
                this.mLocalNightMode = mLocalNightMode;
                if (this.mApplyDayNightCalled) {
                    this.applyDayNight();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    Window$Callback wrapWindowCallback(final Window$Callback window$Callback) {
        return (Window$Callback)new AppCompatWindowCallbackV14(window$Callback);
    }
    
    class AppCompatWindowCallbackV14 extends AppCompatWindowCallbackBase
    {
        AppCompatWindowCallbackV14(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback) {
            ActionMode actionMode;
            if (AppCompatDelegateImplV14.this.isHandleNativeActionModesEnabled()) {
                actionMode = this.startAsSupportActionMode(actionMode$Callback);
            }
            else {
                actionMode = super.onWindowStartingActionMode(actionMode$Callback);
            }
            return actionMode;
        }
        
        final ActionMode startAsSupportActionMode(final ActionMode$Callback actionMode$Callback) {
            final SupportActionModeWrapper.CallbackWrapper callbackWrapper = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImplV14.this.mContext, actionMode$Callback);
            final android.support.v7.view.ActionMode startSupportActionMode = AppCompatDelegateImplV14.this.startSupportActionMode(callbackWrapper);
            ActionMode actionModeWrapper;
            if (startSupportActionMode != null) {
                actionModeWrapper = callbackWrapper.getActionModeWrapper(startSupportActionMode);
            }
            else {
                actionModeWrapper = null;
            }
            return actionModeWrapper;
        }
    }
}
