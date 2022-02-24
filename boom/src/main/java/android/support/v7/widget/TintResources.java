// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.content.res.Resources$NotFoundException;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.content.res.Resources;

public class TintResources extends Resources
{
    private final WeakReference<Context> mContextRef;
    
    public TintResources(@NonNull final Context referent, @NonNull final Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference<Context>(referent);
    }
    
    public Drawable getDrawable(final int n) throws Resources$NotFoundException {
        final Context context = this.mContextRef.get();
        Drawable drawable;
        if (context != null) {
            drawable = AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, n);
        }
        else {
            drawable = super.getDrawable(n);
        }
        return drawable;
    }
    
    final Drawable superGetDrawable(final int n) {
        return super.getDrawable(n);
    }
}
