// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcel;
import android.os.Parcelable$Creator;

public class UEBLEAvailableNotification extends UENotification
{
    public static final Parcelable$Creator<UEBLEAvailableNotification> CREATOR;
    private int mIsBLEStatus;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEBLEAvailableNotification>() {
            public UEBLEAvailableNotification createFromParcel(final Parcel parcel) {
                return new UEBLEAvailableNotification(parcel);
            }
            
            public UEBLEAvailableNotification[] newArray(final int n) {
                return new UEBLEAvailableNotification[n];
            }
        };
    }
    
    public UEBLEAvailableNotification() {
    }
    
    public UEBLEAvailableNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEBLEAvailableNotification(final byte[] array) {
        super(array);
        this.mIsBLEStatus = array[3];
    }
    
    public int describeContents() {
        return 0;
    }
    
    public boolean isBLEAvaliable() {
        return this.mIsBLEStatus == 0;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mIsBLEStatus = parcel.readInt();
    }
    
    @Override
    public String toString() {
        return "[Notification BLE available. Status: " + String.valueOf(this.mIsBLEStatus) + "]";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mIsBLEStatus);
    }
}
