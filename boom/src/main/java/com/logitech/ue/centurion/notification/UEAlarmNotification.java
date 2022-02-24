// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcel;
import com.logitech.ue.centurion.device.devicedata.UEAlarmNotificationFlags;
import android.os.Parcelable$Creator;

public class UEAlarmNotification extends UENotification
{
    public static final Parcelable$Creator<UEAlarmNotification> CREATOR;
    UEAlarmNotificationFlags mAlarmNotificationFlag;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEAlarmNotification>() {
            public UEAlarmNotification createFromParcel(final Parcel parcel) {
                return new UEAlarmNotification(parcel);
            }
            
            public UEAlarmNotification[] newArray(final int n) {
                return new UEAlarmNotification[n];
            }
        };
    }
    
    public UEAlarmNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEAlarmNotification(final byte[] array) {
        super(array);
        this.mAlarmNotificationFlag = UEAlarmNotificationFlags.getStatus(array[4]);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public UEAlarmNotificationFlags getAlarmNotificationFlag() {
        return this.mAlarmNotificationFlag;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAlarmNotificationFlag = UEAlarmNotificationFlags.getStatus(parcel.readInt());
    }
    
    @Override
    public String toString() {
        return "[Notification alarm status change: " + this.mAlarmNotificationFlag.name() + "]";
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mAlarmNotificationFlag.getCode());
    }
}
