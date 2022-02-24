// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import java.util.List;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcel;
import java.util.ArrayList;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UEBlockPartyInfo implements Parcelable
{
    public static final Parcelable$Creator<UEBlockPartyInfo> CREATOR;
    private final ArrayList<UEPartyMemberInfo> mGuests;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEBlockPartyInfo>() {
            public UEBlockPartyInfo createFromParcel(final Parcel parcel) {
                return new UEBlockPartyInfo(parcel);
            }
            
            public UEBlockPartyInfo[] newArray(final int n) {
                return new UEBlockPartyInfo[n];
            }
        };
    }
    
    public UEBlockPartyInfo() {
        this.mGuests = new ArrayList<UEPartyMemberInfo>();
    }
    
    public UEBlockPartyInfo(final Parcel parcel) {
        this.mGuests = new ArrayList<UEPartyMemberInfo>();
    }
    
    public UEBlockPartyInfo(final byte[] array) {
        this.mGuests = new ArrayList<UEPartyMemberInfo>();
        final byte b = array[3];
        int n = 4;
        for (byte b2 = 0; b2 < b; ++b2) {
            final UEPartyMemberInfo e = new UEPartyMemberInfo();
            e.Address = new MAC(array, n);
            n += 6;
            e.mConnectionStatus = UEPartyConnectionStatus.getPartyConnectionStatus(array[n]);
            ++n;
            this.mGuests.add(e);
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getGuestCount() {
        return this.mGuests.size();
    }
    
    public UEPartyMemberInfo getGuestInfo(final int index) {
        return this.mGuests.get(index);
    }
    
    public List<UEPartyMemberInfo> getGuests() {
        return this.mGuests;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Guest count: ").append(String.valueOf(this.mGuests.size())).append("\n");
        for (int i = 0; i < this.mGuests.size(); ++i) {
            sb.append(" Guest ").append(String.valueOf(i)).append(" address:").append(this.mGuests.get(i).Address).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
    }
    
    public static class UEPartyMemberInfo
    {
        public MAC Address;
        public UEPartyConnectionStatus mConnectionStatus;
        
        public UEPartyMemberInfo() {
            this.mConnectionStatus = UEPartyConnectionStatus.UNKNOWN;
        }
    }
}
