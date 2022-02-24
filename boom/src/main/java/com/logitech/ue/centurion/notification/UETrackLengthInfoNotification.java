// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import com.logitech.ue.centurion.utils.UEUtils;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class UETrackLengthInfoNotification extends UENotification
{
    public static final Parcelable$Creator<UETrackLengthInfoNotification> CREATOR;
    int mTrackElapsed;
    int mTrackLength;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UETrackLengthInfoNotification>() {
            public UETrackLengthInfoNotification createFromParcel(final Parcel parcel) {
                return new UETrackLengthInfoNotification(parcel);
            }
            
            public UETrackLengthInfoNotification[] newArray(final int n) {
                return new UETrackLengthInfoNotification[n];
            }
        };
    }
    
    public UETrackLengthInfoNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UETrackLengthInfoNotification(final byte[] array) {
        super(array);
        this.mTrackLength = UEUtils.byteArrayToInt(array, 3);
        this.mTrackElapsed = UEUtils.byteArrayToInt(array, 7);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getTrackElapsed() {
        return this.mTrackElapsed;
    }
    
    public int getTrackLength() {
        return this.mTrackLength;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mTrackLength = parcel.readInt();
        this.mTrackElapsed = parcel.readInt();
    }
    
    public void setTrackElapsed(final int mTrackElapsed) {
        this.mTrackElapsed = mTrackElapsed;
    }
    
    public void setTrackLength(final int mTrackLength) {
        this.mTrackLength = mTrackLength;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Track length: ").append(this.mTrackLength);
        sb.append(", Track elapsed: ").append(this.mTrackElapsed).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mTrackLength);
        parcel.writeInt(this.mTrackElapsed);
    }
}
