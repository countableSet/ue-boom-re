// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UEChargingInfo implements Parcelable
{
    public static final Parcelable$Creator<UEChargingInfo> CREATOR;
    private short mAverageCurrent;
    private byte mCharge;
    private byte mChargingFlags;
    private short mCount;
    private short mTemperature;
    private short mTimeToEmpty;
    private short mVoltage;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEChargingInfo>() {
            public UEChargingInfo createFromParcel(final Parcel parcel) {
                return new UEChargingInfo(parcel);
            }
            
            public UEChargingInfo[] newArray(final int n) {
                return new UEChargingInfo[n];
            }
        };
    }
    
    public UEChargingInfo() {
        this.mTemperature = 0;
        this.mVoltage = 0;
        this.mCharge = 0;
        this.mChargingFlags = 0;
        this.mAverageCurrent = 0;
        this.mTimeToEmpty = 0;
        this.mCount = 0;
    }
    
    public UEChargingInfo(final Parcel parcel) {
        this.mTemperature = 0;
        this.mVoltage = 0;
        this.mCharge = 0;
        this.mChargingFlags = 0;
        this.mAverageCurrent = 0;
        this.mTimeToEmpty = 0;
        this.mCount = 0;
        this.mCharge = (byte)parcel.readInt();
    }
    
    public UEChargingInfo(final byte[] array) {
        this(array, false);
    }
    
    public UEChargingInfo(final byte[] array, final boolean b) {
        this.mTemperature = 0;
        this.mVoltage = 0;
        this.mCharge = 0;
        this.mChargingFlags = 0;
        this.mAverageCurrent = 0;
        this.mTimeToEmpty = 0;
        this.mCount = 0;
        if (b) {
            this.mCharge = array[0];
        }
        else {
            this.mTemperature = (short)((array[3] & 0xFF) << 8 | (array[4] & 0xFF));
            this.mVoltage = (short)((array[5] & 0xFF) << 8 | (array[6] & 0xFF));
            this.mCharge = array[7];
            this.mChargingFlags = array[8];
            this.mAverageCurrent = (short)((array[9] & 0xFF) << 8 | (array[10] & 0xFF));
            this.mTimeToEmpty = (short)((array[11] & 0xFF) << 8 | (array[12] & 0xFF));
            this.mCount = (short)((array[13] & 0xFF) << 8 | (array[14] & 0xFF));
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public short getAverageCurrent() {
        return this.mAverageCurrent;
    }
    
    public int getCharge() {
        return this.mCharge;
    }
    
    public byte getChargingFlags() {
        return this.mChargingFlags;
    }
    
    public int getTWSRole() {
        return (this.mChargingFlags & 0x60) >> 5;
    }
    
    public short getTemperature() {
        return this.mTemperature;
    }
    
    public double getTemperatureC() {
        return (this.mTemperature - 273.15) * 0.1;
    }
    
    public short getTimeToEmpty() {
        return this.mTimeToEmpty;
    }
    
    public short getVoltage() {
        return this.mVoltage;
    }
    
    public boolean isChargeComplete() {
        return (this.mChargingFlags & 0x80) != 0x0;
    }
    
    public boolean isCharging() {
        return this.mChargingFlags > 0;
    }
    
    public boolean isFastCharging() {
        return (this.mChargingFlags & 0x2) != 0x0;
    }
    
    public boolean isLowBatteryShutdown() {
        return (this.mChargingFlags & 0x8) != 0x0;
    }
    
    public boolean isOverTemperatureShutdown() {
        return (this.mChargingFlags & 0x10) != 0x0;
    }
    
    public boolean isUSBAttached() {
        return (this.mChargingFlags & 0x4) != 0x0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append("Charge: ").append(Integer.toString(this.mCharge)).append("%]\n");
        sb.append("[").append("TimeToEmpty: ").append(Integer.toString(this.mTimeToEmpty)).append("s]\n");
        sb.append("[").append("Temperature: ").append(String.format("%.2f", this.getTemperatureC())).append("]\n");
        sb.append("[").append("Voltage: ").append(Integer.toString(this.mVoltage)).append("]\n");
        sb.append("[").append("ChargingFlags: ").append(Integer.toString(this.getChargingFlags())).append("]\n");
        sb.append("[").append("AverageCurrent: ").append(Integer.toString(this.mAverageCurrent)).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt((int)this.mCharge);
    }
}
