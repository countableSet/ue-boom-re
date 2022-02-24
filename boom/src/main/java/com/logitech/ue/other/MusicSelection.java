// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.text.TextUtils;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class MusicSelection implements Parcelable
{
    public static final Parcelable$Creator<MusicSelection> CREATOR;
    public String albumKey;
    public String albumName;
    public String artistKey;
    public String artistName;
    public String titleKey;
    public String titleName;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MusicSelection>() {
            public MusicSelection createFromParcel(final Parcel parcel) {
                return new MusicSelection(parcel);
            }
            
            public MusicSelection[] newArray(final int n) {
                return new MusicSelection[n];
            }
        };
    }
    
    public MusicSelection() {
        this.artistName = null;
        this.artistKey = null;
        this.albumName = null;
        this.albumKey = null;
        this.titleName = null;
        this.titleKey = null;
    }
    
    public MusicSelection(final Parcel parcel) {
        this.artistName = null;
        this.artistKey = null;
        this.albumName = null;
        this.albumKey = null;
        this.titleName = null;
        this.titleKey = null;
        final String[] array = new String[6];
        parcel.readStringArray(array);
        this.artistName = array[0];
        this.artistKey = array[1];
        this.albumName = array[2];
        this.albumKey = array[3];
        this.titleName = array[4];
        this.titleKey = array[5];
    }
    
    public MusicSelection(final MusicSelection musicSelection) {
        this.artistName = null;
        this.artistKey = null;
        this.albumName = null;
        this.albumKey = null;
        this.titleName = null;
        this.titleKey = null;
        this.artistName = musicSelection.artistName;
        this.artistKey = musicSelection.artistKey;
        this.albumName = musicSelection.albumName;
        this.albumKey = musicSelection.albumKey;
        this.titleName = musicSelection.titleName;
        this.titleKey = musicSelection.titleKey;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public boolean isEmpty() {
        return TextUtils.isEmpty((CharSequence)this.artistKey) && TextUtils.isEmpty((CharSequence)this.albumKey) && TextUtils.isEmpty((CharSequence)this.artistKey);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeStringArray(new String[] { this.artistName, this.artistKey, this.albumName, this.albumKey, this.titleName, this.titleKey });
    }
}
