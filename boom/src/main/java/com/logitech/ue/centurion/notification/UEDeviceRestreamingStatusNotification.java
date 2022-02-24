// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcel;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import android.os.Parcelable$Creator;

public class UEDeviceRestreamingStatusNotification extends UENotification
{
    public static final Parcelable$Creator<UEDeviceRestreamingStatusNotification> CREATOR;
    UEDeviceStreamingStatus mDeviceStreamingStatus;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEDeviceRestreamingStatusNotification>() {
            public UEDeviceRestreamingStatusNotification createFromParcel(final Parcel parcel) {
                return new UEDeviceRestreamingStatusNotification(parcel);
            }
            
            public UEDeviceRestreamingStatusNotification[] newArray(final int n) {
                return new UEDeviceRestreamingStatusNotification[n];
            }
        };
    }
    
    public UEDeviceRestreamingStatusNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEDeviceRestreamingStatusNotification(final byte[] array) {
        super(array);
        this.mDeviceStreamingStatus = UEDeviceStreamingStatus.getStatus(array[3]);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public UEDeviceStreamingStatus getDevicesStreamingStatus() {
        return this.mDeviceStreamingStatus;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mDeviceStreamingStatus = UEDeviceStreamingStatus.getStatus(parcel.readInt());
    }
    
    @Override
    public String toString() {
        return "[Notification device streaming status change: " + this.mDeviceStreamingStatus.name() + "]";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mDeviceStreamingStatus.getCode());
    }
}
