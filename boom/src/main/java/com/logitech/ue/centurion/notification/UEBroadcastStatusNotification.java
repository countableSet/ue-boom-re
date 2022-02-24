// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import java.util.List;
import android.os.Parcel;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverInfo;
import java.util.ArrayList;
import android.os.Parcelable$Creator;

public class UEBroadcastStatusNotification extends UENotification
{
    public static final Parcelable$Creator<UEBroadcastStatusNotification> CREATOR;
    ArrayList<UEBroadcastReceiverInfo> mReceiversList;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEBroadcastStatusNotification>() {
            public UEBroadcastStatusNotification createFromParcel(final Parcel parcel) {
                return new UEBroadcastStatusNotification(parcel);
            }
            
            public UEBroadcastStatusNotification[] newArray(final int n) {
                return new UEBroadcastStatusNotification[n];
            }
        };
    }
    
    public UEBroadcastStatusNotification() {
        this.mReceiversList = new ArrayList<UEBroadcastReceiverInfo>();
    }
    
    public UEBroadcastStatusNotification(final Parcel parcel) {
        this.mReceiversList = new ArrayList<UEBroadcastReceiverInfo>();
        this.readFromParcel(parcel);
    }
    
    public UEBroadcastStatusNotification(final byte[] array) {
        super(array);
        this.mReceiversList = new ArrayList<UEBroadcastReceiverInfo>();
        for (byte b = 0; b < array[3]; ++b) {
            this.mReceiversList.add(new UEBroadcastReceiverInfo(array, b * 10 + 4));
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public List<UEBroadcastReceiverInfo> getReceiversList() {
        return this.mReceiversList;
    }
    
    public void readFromParcel(final Parcel parcel) {
        parcel.readTypedList((List)this.mReceiversList, (Parcelable$Creator)UEBroadcastReceiverInfo.CREATOR);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification broadcast: ").append(String.format("Receiver count %d", this.mReceiversList.size())).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeTypedList((List)this.mReceiversList);
    }
}
