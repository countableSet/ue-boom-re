// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcelable;
import android.os.Parcel;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcelable$Creator;

public class UEReceiverVariableAttributesNotification extends UENotification
{
    public static final Parcelable$Creator<UEReceiverVariableAttributesNotification> CREATOR;
    private MAC mAddress;
    private int mBatteryLevel;
    private boolean mIsCommandDelivered;
    private int mRSSI;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEReceiverVariableAttributesNotification>() {
            public UEReceiverVariableAttributesNotification createFromParcel(final Parcel parcel) {
                return new UEReceiverVariableAttributesNotification(parcel);
            }
            
            public UEReceiverVariableAttributesNotification[] newArray(final int n) {
                return new UEReceiverVariableAttributesNotification[n];
            }
        };
    }
    
    public UEReceiverVariableAttributesNotification() {
    }
    
    public UEReceiverVariableAttributesNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEReceiverVariableAttributesNotification(final MAC mAddress, final boolean mIsCommandDelivered) {
        this.mAddress = mAddress;
        this.mIsCommandDelivered = mIsCommandDelivered;
    }
    
    public UEReceiverVariableAttributesNotification(final byte[] array) {
        super(array);
        this.mAddress = new MAC(array, 3);
        this.mIsCommandDelivered = (array[9] == 0);
        if (this.mIsCommandDelivered) {
            this.mBatteryLevel = array[10];
            this.mRSSI = array[11];
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public int getBatteryLevel() {
        return this.mBatteryLevel;
    }
    
    public int getRSSI() {
        return this.mRSSI;
    }
    
    public boolean isCommandDelivered() {
        return this.mIsCommandDelivered;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mIsCommandDelivered = (parcel.readInt() == 1);
        if (this.mIsCommandDelivered) {
            this.mBatteryLevel = parcel.readInt();
            this.mRSSI = parcel.readInt();
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification variable attributes: ").append(String.format("Receiver mac: %s was message deliver %b", this.mAddress, this.mIsCommandDelivered)).append("]");
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
            parcel.writeInt(this.mBatteryLevel);
            parcel.writeInt(this.mRSSI);
        }
    }
}
