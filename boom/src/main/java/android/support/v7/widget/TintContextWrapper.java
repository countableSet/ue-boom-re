// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.content.Context;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.content.ContextWrapper;

public class TintContextWrapper extends ContextWrapper
{
    private static final ArrayList<WeakReference<TintContextWrapper>> sCache;
    private Resources mResources;
    private final Resources$Theme mTheme;
    
    static {
        sCache = new ArrayList<WeakReference<TintContextWrapper>>();
    }
    
    private TintContextWrapper(@NonNull final Context context) {
        super(context);
        (this.mTheme = this.getResources().newTheme()).setTo(context.getTheme());
    }
    
    private static boolean shouldWrap(@NonNull final Context context) {
        boolean b = false;
        if (!(context instanceof TintContextWrapper) && !(context.getResources() instanceof TintResources)) {
            b = true;
        }
        return b;
    }
    
    public static Context wrap(@NonNull final Context context) {
        Object referent;
        if (shouldWrap(context)) {
            for (int i = 0; i < TintContextWrapper.sCache.size(); ++i) {
                final WeakReference<TintContextWrapper> weakReference = TintContextWrapper.sCache.get(i);
                if (weakReference != null) {
                    referent = weakReference.get();
                }
                else {
                    referent = null;
                }
                if (referent != null && ((TintContextWrapper)referent).getBaseContext() == context) {
                    return (Context)referent;
                }
            }
            referent = new TintContextWrapper(context);
            TintContextWrapper.sCache.add(new WeakReference<TintContextWrapper>((TintContextWrapper)referent));
        }
        else {
            referent = context;
        }
        return (Context)referent;
    }
    
    public Resources getResources() {
        if (this.mResources == null) {
            this.mResources = new TintResources((Context)this, super.getResources());
        }
        return this.mResources;
    }
    
    public Resources$Theme getTheme() {
        return this.mTheme;
    }
    
    public void setTheme(final int n) {
        this.mTheme.applyStyle(n, true);
    }
}
