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

public class UEGetReceiverOneAttributeNotification extends UENotification
{
    public static final Parcelable$Creator<UEGetReceiverOneAttributeNotification> CREATOR;
    private MAC mAddress;
    private UEReceiverAttribute mAttribute;
    private byte[] mAttributeValue;
    private boolean mIsCommandDelivered;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEGetReceiverOneAttributeNotification>() {
            public UEGetReceiverOneAttributeNotification createFromParcel(final Parcel parcel) {
                return new UEGetReceiverOneAttributeNotification(parcel);
            }
            
            public UEGetReceiverOneAttributeNotification[] newArray(final int n) {
                return new UEGetReceiverOneAttributeNotification[n];
            }
        };
    }
    
    public UEGetReceiverOneAttributeNotification() {
    }
    
    public UEGetReceiverOneAttributeNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEGetReceiverOneAttributeNotification(final MAC mAddress, final int n, final byte[] array, final boolean mIsCommandDelivered) {
        this.mAddress = mAddress;
        this.mAttribute = UEReceiverAttribute.getReceiverAttribute(n);
        this.mIsCommandDelivered = mIsCommandDelivered;
        if (this.mIsCommandDelivered) {
            System.arraycopy(array, 0, this.mAttributeValue = new byte[array.length], 0, this.mAttributeValue.length);
        }
    }
    
    public UEGetReceiverOneAttributeNotification(final byte[] array) {
        super(array);
        this.mAddress = new MAC(array, 3);
        this.mAttribute = UEReceiverAttribute.getReceiverAttribute(UEUtils.combineTwoBytesToOneInteger(array[8], array[9]));
        this.mIsCommandDelivered = (array[10] == 0);
        if (this.mIsCommandDelivered) {
            System.arraycopy(array, 11, this.mAttributeValue = new byte[array.length - 11], 0, this.mAttributeValue.length);
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public int getAttributeCode() {
        return this.mAttribute.getAttributeCode();
    }
    
    public byte[] getAttributeValue() {
        return this.mAttributeValue;
    }
    
    public boolean isCommandDelivered() {
        return this.mIsCommandDelivered;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mAttribute = UEReceiverAttribute.getReceiverAttribute(parcel.readInt());
        this.mIsCommandDelivered = (parcel.readInt() == 1);
        if (this.mIsCommandDelivered) {
            parcel.readByteArray(this.mAttributeValue = new byte[parcel.readInt()]);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification get one variable attribute: ").append(String.format("Receiver mac: %s was message deliver %b", this.mAddress, this.mIsCommandDelivered)).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeParcelable((Parcelable)this.mAddress, n);
        parcel.writeInt(this.mAttribute.getAttributeCode());
        if (this.mIsCommandDelivered) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        if (this.mIsCommandDelivered) {
            parcel.writeInt(this.mAttributeValue.length);
            parcel.writeByteArray(this.mAttributeValue);
        }
    }
}
