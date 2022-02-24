// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.os;

import android.os.Parcelable$Creator;

class ParcelableCompatCreatorHoneycombMR2Stub
{
    static <T> Parcelable$Creator<T> instantiate(final ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks) {
        return (Parcelable$Creator<T>)new ParcelableCompatCreatorHoneycombMR2((ParcelableCompatCreatorCallbacks<Object>)parcelableCompatCreatorCallbacks);
    }
}
