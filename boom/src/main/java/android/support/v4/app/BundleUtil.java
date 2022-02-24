// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.app;

import android.os.Parcelable;
import java.util.Arrays;
import android.os.Bundle;

class BundleUtil
{
    public static Bundle[] getBundleArrayFromBundle(final Bundle bundle, final String s) {
        final Parcelable[] parcelableArray = bundle.getParcelableArray(s);
        Bundle[] array;
        if (parcelableArray instanceof Bundle[] || parcelableArray == null) {
            array = (Bundle[])parcelableArray;
        }
        else {
            final Bundle[] array2 = Arrays.copyOf((Bundle[])parcelableArray, ((Bundle[])parcelableArray).length, (Class<? extends Bundle[]>)Bundle[].class);
            bundle.putParcelableArray(s, (Parcelable[])array2);
            array = array2;
        }
        return array;
    }
}
