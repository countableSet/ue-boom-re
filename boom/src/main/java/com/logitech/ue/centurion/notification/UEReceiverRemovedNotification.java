// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcelable;
import android.os.Parcel;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcelable$Creator;

public class UEReceiverRemovedNotification extends UENotification
{
    public static final Parcelable$Creator<UEReceiverRemovedNotification> CREATOR;
    private MAC mAddress;
    private boolean mIsCommandDelivered;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEReceiverRemovedNotification>() {
            public UEReceiverRemovedNotification createFromParcel(final Parcel parcel) {
                return new UEReceiverRemovedNotification(parcel);
            }
            
            public UEReceiverRemovedNotification[] newArray(final int n) {
                return new UEReceiverRemovedNotification[n];
            }
        };
    }
    
    public UEReceiverRemovedNotification() {
        this.mIsCommandDelivered = false;
    }
    
    public UEReceiverRemovedNotification(final Parcel parcel) {
        this.mIsCommandDelivered = false;
        this.readFromParcel(parcel);
    }
    
    public UEReceiverRemovedNotification(final MAC mAddress, final boolean mIsCommandDelivered) {
        this.mIsCommandDelivered = false;
        this.mAddress = mAddress;
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    public UEReceiverRemovedNotification(final byte[] array) {
        boolean mIsCommandDelivered = false;
        super(array);
        this.mIsCommandDelivered = false;
        this.mAddress = new MAC(array, 3);
        if (array[9] == 0) {
            mIsCommandDelivered = true;
        }
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public boolean isCommandDelivered() {
        return this.mIsCommandDelivered;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mIsCommandDelivered = (parcel.readInt() == 1);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification receiver remove from broadcast: ").append(String.format("Receiver mac: %s was message deliver %b", this.mAddress, this.mIsCommandDelivered)).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeParcelable((Parcelable)this.mAddress, n);
        if (this.mIsCommandDelivered) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
    }
}
