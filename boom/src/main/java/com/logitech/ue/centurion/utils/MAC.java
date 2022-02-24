// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.utils;

import java.util.Arrays;
import android.support.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class MAC implements Parcelable
{
    public static final Parcelable$Creator<MAC> CREATOR;
    public static final int MAC_ADDRESS_LENGTH = 6;
    private byte[] mAddressBytes;
    private String mAddressString;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MAC>() {
            public MAC createFromParcel(final Parcel parcel) {
                return new MAC(parcel);
            }
            
            public MAC[] newArray(final int n) {
                return new MAC[n];
            }
        };
    }
    
    public MAC() {
        this(new byte[] { 0, 0, 0, 0, 0, 0 });
    }
    
    protected MAC(final Parcel parcel) {
        this.mAddressBytes = parcel.createByteArray();
    }
    
    public MAC(@NonNull final String mAddressString) {
        this.mAddressString = mAddressString;
        this.mAddressBytes = MACStringToByteArray(mAddressString);
    }
    
    public MAC(@NonNull final byte[] mAddressBytes) {
        if (mAddressBytes.length != 6) {
            throw new IllegalArgumentException(String.format("addressBytes length should be exactly %d bytes", 6));
        }
        this.mAddressBytes = mAddressBytes;
        this.mAddressString = UEUtils.byteArrayToMACString(this.mAddressBytes);
    }
    
    public MAC(@NonNull final byte[] array, final int n) {
        if (array.length - n < 6) {
            throw new IllegalArgumentException(String.format("addressBytes length - offset should be equals or greater then %d", 6));
        }
        System.arraycopy(array, n, this.mAddressBytes = new byte[6], 0, this.mAddressBytes.length);
        this.mAddressString = UEUtils.byteArrayToMACString(this.mAddressBytes);
    }
    
    public static byte[] MACStringToByteArray(final String s) {
        final String[] split = s.replace(" ", "").split(":");
        final byte[] array = new byte[split.length];
        if (array.length != 6) {
            throw new IllegalArgumentException(String.format("address length should be exactly %d bytes", 6));
        }
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte)((Character.digit(split[i].charAt(0), 16) << 4) + Character.digit(split[i].charAt(1), 16));
        }
        return array;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof MAC && Arrays.equals(this.mAddressBytes, ((MAC)o).mAddressBytes);
    }
    
    public String getAddress() {
        return this.mAddressString;
    }
    
    public byte[] getBytes() {
        return this.mAddressBytes;
    }
    
    @Override
    public int hashCode() {
        return this.mAddressString.hashCode();
    }
    
    @Override
    public String toString() {
        return this.mAddressString;
    }
    
    public void writeToParcel(@NonNull final Parcel parcel, final int n) {
        parcel.writeByteArray(this.mAddressBytes);
    }
}
