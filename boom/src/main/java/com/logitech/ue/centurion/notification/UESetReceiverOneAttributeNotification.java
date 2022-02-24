// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcelable;
import com.logitech.ue.centurion.utils.UEUtils;
import android.os.Parcel;
import com.logitech.ue.centurion.device.devicedata.UEReceiverAttribute;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcelable$Creator;

public class UESetReceiverOneAttributeNotification extends UENotification
{
    public static final Parcelable$Creator<UESetReceiverOneAttributeNotification> CREATOR;
    private MAC mAddress;
    private boolean mIsCommandDelivered;
    private UEReceiverAttribute mReceiverAttribute;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UESetReceiverOneAttributeNotification>() {
            public UESetReceiverOneAttributeNotification createFromParcel(final Parcel parcel) {
                return new UESetReceiverOneAttributeNotification(parcel);
            }
            
            public UESetReceiverOneAttributeNotification[] newArray(final int n) {
                return new UESetReceiverOneAttributeNotification[n];
            }
        };
    }
    
    public UESetReceiverOneAttributeNotification() {
    }
    
    public UESetReceiverOneAttributeNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UESetReceiverOneAttributeNotification(final MAC mAddress, final boolean mIsCommandDelivered) {
        this.mAddress = mAddress;
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    public UESetReceiverOneAttributeNotification(final byte[] array) {
        super(array);
        this.mAddress = new MAC(array, 3);
        this.mReceiverAttribute = UEReceiverAttribute.getReceiverAttribute(UEUtils.combineTwoBytesToOneInteger(array[8], array[9]));
        this.mIsCommandDelivered = (array[10] == 0);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public int getAttributeNumber() {
        return this.mReceiverAttribute.getAttributeCode();
    }
    
    public boolean isCommandDelivered() {
        return this.mIsCommandDelivered;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mReceiverAttribute = UEReceiverAttribute.getReceiverAttribute(parcel.readInt());
        this.mIsCommandDelivered = (parcel.readInt() == 1);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification set one attribute: ").append(String.format("Receiver mac: %s was message deliver %b", this.mAddress, this.mIsCommandDelivered)).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeParcelable((Parcelable)this.mAddress, n);
        parcel.writeInt(this.mReceiverAttribute.getAttributeCode());
        if (this.mIsCommandDelivered) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
    }
}
