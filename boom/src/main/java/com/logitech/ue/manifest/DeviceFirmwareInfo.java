// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import android.os.Parcel;
import org.simpleframework.xml.Attribute;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class DeviceFirmwareInfo implements Parcelable
{
    public static final Parcelable$Creator<DeviceFirmwareInfo> CREATOR;
    @Attribute(required = false)
    public String currentFirmware;
    @Attribute
    public String device;
    @Attribute(name = "firmwareDetails", required = false)
    public String firmwareDetailsURL;
    @Attribute(required = false)
    public String firmwareDownloadURL;
    @Attribute(required = false)
    public String firmwareInfoURL;
    @Attribute
    public String hardwareRev;
    @Attribute(required = false)
    public String latestFirmware;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<DeviceFirmwareInfo>() {
            public DeviceFirmwareInfo createFromParcel(final Parcel parcel) {
                return new DeviceFirmwareInfo(parcel);
            }
            
            public DeviceFirmwareInfo[] newArray(final int n) {
                return new DeviceFirmwareInfo[n];
            }
        };
    }
    
    public DeviceFirmwareInfo() {
        this.device = "";
        this.hardwareRev = "";
        this.latestFirmware = null;
        this.firmwareInfoURL = "";
        this.firmwareDownloadURL = "";
        this.currentFirmware = null;
    }
    
    public DeviceFirmwareInfo(final Parcel parcel) {
        this.device = "";
        this.hardwareRev = "";
        this.latestFirmware = null;
        this.firmwareInfoURL = "";
        this.firmwareDownloadURL = "";
        this.currentFirmware = null;
        this.readFromParcel(parcel);
    }
    
    public static int compareVersions(final String s, final String s2) {
        final int n = 1;
        int n2;
        if (s == null || s2 == null) {
            n2 = 0;
        }
        else {
            final String[] split = s.split("\\.");
            final String[] split2 = s2.split("\\.");
            for (int n3 = 0; n3 < split.length && n3 < split2.length; ++n3) {
                final int int1 = Integer.parseInt(split[n3]);
                final int int2 = Integer.parseInt(split2[n3]);
                n2 = n;
                if (int1 > int2) {
                    return n2;
                }
                if (int1 < int2) {
                    n2 = -1;
                    return n2;
                }
            }
            n2 = n;
            if (split.length <= split2.length) {
                if (split.length < split2.length) {
                    n2 = -1;
                }
                else {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    public int describeContents() {
        return 0;
    }
    
    protected void readFromParcel(final Parcel parcel) {
        this.device = parcel.readString();
        this.hardwareRev = parcel.readString();
        this.latestFirmware = parcel.readString();
        this.firmwareInfoURL = parcel.readString();
        this.firmwareDownloadURL = parcel.readString();
        this.firmwareDetailsURL = parcel.readString();
        this.currentFirmware = parcel.readString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.device);
        parcel.writeString(this.hardwareRev);
        parcel.writeString(this.latestFirmware);
        parcel.writeString(this.firmwareInfoURL);
        parcel.writeString(this.firmwareDownloadURL);
        parcel.writeString(this.firmwareDetailsURL);
        parcel.writeString(this.currentFirmware);
    }
}
