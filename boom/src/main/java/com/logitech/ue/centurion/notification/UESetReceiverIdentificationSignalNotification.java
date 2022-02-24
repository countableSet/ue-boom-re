// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import com.logitech.ue.centurion.utils.UEUtils;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class UESetReceiverIdentificationSignalNotification extends UENotification
{
    public static final Parcelable$Creator<UESetReceiverIdentificationSignalNotification> CREATOR;
    private boolean mIsCommandDelivered;
    private final byte[] mMac;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UESetReceiverIdentificationSignalNotification>() {
            public UESetReceiverIdentificationSignalNotification createFromParcel(final Parcel parcel) {
                return new UESetReceiverIdentificationSignalNotification(parcel);
            }
            
            public UESetReceiverIdentificationSignalNotification[] newArray(final int n) {
                return new UESetReceiverIdentificationSignalNotification[n];
            }
        };
    }
    
    public UESetReceiverIdentificationSignalNotification() {
        this.mMac = new byte[6];
    }
    
    public UESetReceiverIdentificationSignalNotification(final Parcel parcel) {
        this.mMac = new byte[6];
        this.readFromParcel(parcel);
    }
    
    public UESetReceiverIdentificationSignalNotification(final byte[] array) {
        boolean mIsCommandDelivered = false;
        super(array);
        System.arraycopy(array, 3, this.mMac = new byte[6], 0, array.length);
        if (array[10] == 0) {
            mIsCommandDelivered = true;
        }
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public byte[] getMac() {
        return this.mMac;
    }
    
    public boolean isCommandDelivered() {
        return this.mIsCommandDelivered;
    }
    
    public void readFromParcel(final Parcel parcel) {
        boolean mIsCommandDelivered = true;
        parcel.readByteArray(this.mMac);
        if (parcel.readInt() != 1) {
            mIsCommandDelivered = false;
        }
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification set receiver identification signal: ").append(String.format("Receiver mac: %s was message deliver %b", UEUtils.byteArrayToFancyHexString(this.mMac), this.mIsCommandDelivered)).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeByteArray(this.mMac);
        if (this.mIsCommandDelivered) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
    }
}
