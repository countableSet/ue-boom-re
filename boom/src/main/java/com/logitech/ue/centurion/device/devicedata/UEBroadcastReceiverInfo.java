// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.support.annotation.NonNull;
import com.logitech.ue.centurion.utils.UEUtils;
import android.os.Parcel;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UEBroadcastReceiverInfo implements Parcelable
{
    public static final Parcelable$Creator<UEBroadcastReceiverInfo> CREATOR;
    public MAC mAddress;
    public int mBatteryLevel;
    public int mDeviceColor;
    public int mNameRevision;
    public boolean mNonzeroVolumeOffset;
    public UEBroadcastReceiverStatus mStatus;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEBroadcastReceiverInfo>() {
            public UEBroadcastReceiverInfo createFromParcel(final Parcel parcel) {
                return new UEBroadcastReceiverInfo(parcel);
            }
            
            public UEBroadcastReceiverInfo[] newArray(final int n) {
                return new UEBroadcastReceiverInfo[n];
            }
        };
    }
    
    public UEBroadcastReceiverInfo(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEBroadcastReceiverInfo(final MAC mAddress, final int mDeviceColor, final boolean mNonzeroVolumeOffset, final int mBatteryLevel, final int mNameRevision, final UEBroadcastReceiverStatus mStatus) {
        this.mAddress = mAddress;
        this.mDeviceColor = mDeviceColor;
        this.mNonzeroVolumeOffset = mNonzeroVolumeOffset;
        this.mBatteryLevel = mBatteryLevel;
        this.mNameRevision = mNameRevision;
        this.mStatus = mStatus;
    }
    
    public UEBroadcastReceiverInfo(final byte[] array, int n) {
        this.mAddress = new MAC(array, n);
        n += 6;
        final int combineTwoBytesToOneInteger = UEUtils.combineTwoBytesToOneInteger(array[n], array[n + 1]);
        this.mNonzeroVolumeOffset = ((0x8000 & combineTwoBytesToOneInteger) != 0x0);
        this.mBatteryLevel = (combineTwoBytesToOneInteger >> 8 & 0x7F);
        this.mNameRevision = (combineTwoBytesToOneInteger >> 4 & 0xF);
        this.mStatus = UEBroadcastReceiverStatus.getStatus(combineTwoBytesToOneInteger & 0xF);
        n += 2;
        this.mDeviceColor = UEUtils.combineTwoBytesToOneInteger(array[n], array[n + 1]);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mDeviceColor = parcel.readInt();
        this.mNonzeroVolumeOffset = (parcel.readInt() == 1);
        this.mBatteryLevel = parcel.readInt();
        this.mNameRevision = parcel.readInt();
        this.mStatus = UEBroadcastReceiverStatus.getStatus(parcel.readInt());
    }
    
    public void writeToParcel(@NonNull final Parcel parcel, int n) {
        parcel.writeParcelable((Parcelable)this.mAddress, n);
        parcel.writeInt(this.mDeviceColor);
        if (this.mNonzeroVolumeOffset) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeInt(this.mBatteryLevel);
        parcel.writeInt(this.mNameRevision);
        parcel.writeInt(this.mStatus.getCode());
    }
}
