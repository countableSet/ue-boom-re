// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import com.logitech.ue.centurion.utils.UEUtils;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UEHardwareInfo implements Parcelable
{
    public static final Parcelable$Creator<UEHardwareInfo> CREATOR;
    public static final int INVALID_HARDWARE_ID = 255;
    private int mPrimaryDeviceColour;
    private int mPrimaryDeviceHardwareId;
    private int mSecondaryDeviceColour;
    private int mSecondaryDeviceHardwareId;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEHardwareInfo>() {
            public UEHardwareInfo createFromParcel(final Parcel parcel) {
                return new UEHardwareInfo(parcel);
            }
            
            public UEHardwareInfo[] newArray(final int n) {
                return new UEHardwareInfo[n];
            }
        };
    }
    
    public UEHardwareInfo(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEHardwareInfo(final byte[] array) {
        this.mPrimaryDeviceHardwareId = UEUtils.byteToUnsigned(array[3]);
        this.mPrimaryDeviceColour = UEUtils.byteToUnsigned(array[4]);
        this.mSecondaryDeviceHardwareId = UEUtils.byteToUnsigned(array[5]);
        this.mSecondaryDeviceColour = UEUtils.byteToUnsigned(array[6]);
    }
    
    private void readFromParcel(final Parcel parcel) {
        this.mPrimaryDeviceColour = parcel.readInt();
        this.mSecondaryDeviceColour = parcel.readInt();
        this.mPrimaryDeviceHardwareId = parcel.readInt();
        this.mSecondaryDeviceHardwareId = parcel.readInt();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getPrimaryDeviceColour() {
        return this.mPrimaryDeviceColour;
    }
    
    public int getPrimaryDeviceHardwareId() {
        return this.mPrimaryDeviceHardwareId;
    }
    
    public int getSecondaryDeviceColour() {
        return this.mSecondaryDeviceColour;
    }
    
    public int getSecondaryDeviceHardwareId() {
        return this.mSecondaryDeviceHardwareId;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append("PrimaryDeviceColour: ").append(UEColour.getDeviceColour(this.mPrimaryDeviceColour)).append(String.format("(0x%02X)", this.mPrimaryDeviceColour)).append("]\n");
        sb.append("[").append("PrimaryDeviceHardwareId: ").append(this.mPrimaryDeviceHardwareId).append("]\n");
        sb.append("[").append("SecondaryDeviceColour: ").append(UEColour.getDeviceColour(this.mSecondaryDeviceColour)).append(String.format("(0x%02X)", this.mPrimaryDeviceColour)).append("]\n");
        sb.append("[").append("SecondaryDeviceHardwareId: ").append(this.mSecondaryDeviceHardwareId).append("]\n");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mPrimaryDeviceColour);
        parcel.writeInt(this.mSecondaryDeviceColour);
        parcel.writeInt(this.mPrimaryDeviceHardwareId);
        parcel.writeInt(this.mSecondaryDeviceHardwareId);
    }
}
