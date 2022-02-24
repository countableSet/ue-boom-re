// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.os.Parcel;
import android.os.Parcelable$Creator;

public class UEVoiceNotification extends UENotification
{
    public static final Parcelable$Creator<UEVoiceNotification> CREATOR;
    int cueState;
    int state;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEVoiceNotification>() {
            public UEVoiceNotification createFromParcel(final Parcel parcel) {
                return new UEVoiceNotification(parcel);
            }
            
            public UEVoiceNotification[] newArray(final int n) {
                return new UEVoiceNotification[n];
            }
        };
    }
    
    public UEVoiceNotification(final Parcel parcel) {
        this.readFromParcel(parcel);
    }
    
    public UEVoiceNotification(final byte[] array) {
        this.state = array[3];
        this.cueState = array[4];
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getCueState() {
        return this.cueState;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.state = parcel.readInt();
        this.cueState = parcel.readInt();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.state);
        parcel.writeInt(this.cueState);
    }
}
