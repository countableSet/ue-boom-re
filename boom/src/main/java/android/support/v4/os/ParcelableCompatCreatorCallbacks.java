// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.os;

import android.os.Parcel;

public interface ParcelableCompatCreatorCallbacks<T>
{
    T createFromParcel(final Parcel p0, final ClassLoader p1);
    
    T[] newArray(final int p0);
}
