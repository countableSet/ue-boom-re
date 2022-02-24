// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcelable;
import java.io.UnsupportedEncodingException;
import com.logitech.ue.centurion.utils.UEUtils;
import java.util.Locale;
import android.os.Parcel;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcelable$Creator;

public class UEReceiverFixedAttributesNotification extends UENotification
{
    public static final Parcelable$Creator<UEReceiverFixedAttributesNotification> CREATOR;
    private MAC mAddress;
    private String mDeviceName;
    private UEDeviceType mDeviceType;
    private String mFirmwareVersion;
    private int mHardwareRevision;
    private boolean mIsCommandDelivered;
    private int mXupProtocolVersion;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEReceiverFixedAttributesNotification>() {
            public UEReceiverFixedAttributesNotification createFromParcel(final Parcel parcel) {
                return new UEReceiverFixedAttributesNotification(parcel);
            }
            
            public UEReceiverFixedAttributesNotification[] newArray(final int n) {
                return new UEReceiverFixedAttributesNotification[n];
            }
        };
    }
    
    public UEReceiverFixedAttributesNotification() {
        this.mIsCommandDelivered = false;
    }
    
    public UEReceiverFixedAttributesNotification(final Parcel parcel) {
        this.mIsCommandDelivered = false;
        this.readFromParcel(parcel);
    }
    
    public UEReceiverFixedAttributesNotification(final MAC mAddress, final boolean mIsCommandDelivered) {
        this.mIsCommandDelivered = false;
        this.mAddress = mAddress;
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    public UEReceiverFixedAttributesNotification(final byte[] bytes) {
        super(bytes);
        this.mIsCommandDelivered = false;
        this.mAddress = new MAC(bytes, 3);
        Label_0173: {
            if (bytes[9] != 0) {
                break Label_0173;
            }
            boolean mIsCommandDelivered = true;
        Label_0132_Outer:
            while (true) {
                this.mIsCommandDelivered = mIsCommandDelivered;
                if (!this.mIsCommandDelivered) {
                    return;
                }
                this.mFirmwareVersion = String.format(Locale.getDefault(), "%d.%d.%d", bytes[10], bytes[11], UEUtils.combineTwoBytesToOneInteger(bytes[12], bytes[13]));
                Label_0208: {
                    switch (bytes[14]) {
                        case 1: {
                            break Label_0208;
                        }
                        case 2: {
                            break Label_0208;
                        }
                        case 3: {
                            break Label_0208;
                        }
                        case 4: {
                            break Label_0208;
                        }
                    }
                    while (true) {
                        this.mHardwareRevision = bytes[15];
                        this.mXupProtocolVersion = bytes[16];
                        int n = 17;
                        try {
                            while (n < bytes.length && bytes[n] != 0) {
                                ++n;
                            }
                            this.mDeviceName = new String(bytes, 17, n - 17, "UTF-8");
                            return;
                            mIsCommandDelivered = false;
                            continue Label_0132_Outer;
                            this.mDeviceType = UEDeviceType.Caribe;
                            continue;
                            this.mDeviceType = UEDeviceType.Kora;
                            continue;
                            this.mDeviceType = UEDeviceType.Titus;
                            continue;
                            this.mDeviceType = UEDeviceType.Maximus;
                            continue;
                        }
                        catch (UnsupportedEncodingException ex) {
                            this.mDeviceName = "";
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public String getDeviceName() {
        return this.mDeviceName;
    }
    
    public UEDeviceType getDeviceType() {
        return this.mDeviceType;
    }
    
    public String getFirmwareVersion() {
        return this.mFirmwareVersion;
    }
    
    public int getHardwareRevision() {
        return this.mHardwareRevision;
    }
    
    public int getXupProtocolVersion() {
        return this.mXupProtocolVersion;
    }
    
    public boolean isCommandDelivered() {
        return this.mIsCommandDelivered;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mIsCommandDelivered = (parcel.readInt() == 1);
        if (this.mIsCommandDelivered) {
            this.mFirmwareVersion = parcel.readString();
            this.mDeviceType = UEDeviceType.values()[parcel.readInt()];
            this.mHardwareRevision = parcel.readInt();
            this.mXupProtocolVersion = parcel.readInt();
            this.mDeviceName = parcel.readString();
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification fixed attributes: ").append(String.format("Receiver mac: %s was message deliver %b", this.mAddress, this.mIsCommandDelivered)).append("]");
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
        if (this.mIsCommandDelivered) {
            parcel.writeString(this.mFirmwareVersion);
            parcel.writeInt(this.mDeviceType.ordinal());
            parcel.writeInt(this.mHardwareRevision);
            parcel.writeInt(this.mXupProtocolVersion);
            parcel.writeString(this.mDeviceName);
        }
    }
}
