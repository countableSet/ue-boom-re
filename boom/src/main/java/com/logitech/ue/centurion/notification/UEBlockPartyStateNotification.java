// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcel;
import android.os.Parcelable$Creator;

public class UEBlockPartyStateNotification extends UENotification
{
    public static final int CONNECT_STATUS_FLAG = 1;
    public static final Parcelable$Creator<UEBlockPartyStateNotification> CREATOR;
    public static final int PLAY_STATUS_FLAG = 2;
    public static final int TRACK_CHANGE_FLAG = 4;
    private int mNotificationStatusMask;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEBlockPartyStateNotification>() {
            public UEBlockPartyStateNotification createFromParcel(final Parcel parcel) {
                return new UEBlockPartyStateNotification(parcel);
            }
            
            public UEBlockPartyStateNotification[] newArray(final int n) {
                return new UEBlockPartyStateNotification[n];
            }
        };
    }
    
    public UEBlockPartyStateNotification() {
    }
    
    public UEBlockPartyStateNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEBlockPartyStateNotification(final byte[] array) {
        super(array);
        this.mNotificationStatusMask = array[3];
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getNotificationStatusChangeMask() {
        return this.mNotificationStatusMask;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mNotificationStatusMask = parcel.readInt();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification change status mask: ").append(String.valueOf(this.mNotificationStatusMask)).append("\n");
        sb.deleteCharAt(sb.length() - 1).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mNotificationStatusMask);
    }
}
