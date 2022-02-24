// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.app;

import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.view.Window$Callback;
import android.view.Window;
import android.content.Context;

class AppCompatDelegateImplV23 extends AppCompatDelegateImplV14
{
    AppCompatDelegateImplV23(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }
    
    @Override
    Window$Callback wrapWindowCallback(final Window$Callback window$Callback) {
        return (Window$Callback)new AppCompatWindowCallbackV23(window$Callback);
    }
    
    class AppCompatWindowCallbackV23 extends AppCompatWindowCallbackV14
    {
        AppCompatWindowCallbackV23(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback) {
            return null;
        }
        
        @Override
        public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback, final int n) {
            if (AppCompatDelegateImplV23.this.isHandleNativeActionModesEnabled()) {
                switch (n) {
                    case 0: {
                        return ((AppCompatWindowCallbackV14)this).startAsSupportActionMode(actionMode$Callback);
                    }
                }
            }
            return super.onWindowStartingActionMode(actionMode$Callback, n);
        }
    }
}
