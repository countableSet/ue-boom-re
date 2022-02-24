// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.notification;

import android.util.SparseArray;
import android.os.Parcelable;
import android.os.Parcel;
import com.logitech.ue.centurion.utils.MAC;
import android.os.Parcelable$Creator;

public class UEReceiverAddedNotification extends UENotification
{
    public static final Parcelable$Creator<UEReceiverAddedNotification> CREATOR;
    private MAC mAddress;
    private ExecutionStatus mExecutionStatus;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UEReceiverAddedNotification>() {
            public UEReceiverAddedNotification createFromParcel(final Parcel parcel) {
                return new UEReceiverAddedNotification(parcel);
            }
            
            public UEReceiverAddedNotification[] newArray(final int n) {
                return new UEReceiverAddedNotification[n];
            }
        };
    }
    
    public UEReceiverAddedNotification(final Parcel parcel) {
        this.mExecutionStatus = ExecutionStatus.UNKNOWN;
        this.readFromParcel(parcel);
    }
    
    public UEReceiverAddedNotification(final MAC mAddress, final ExecutionStatus mExecutionStatus) {
        this.mExecutionStatus = ExecutionStatus.UNKNOWN;
        this.mAddress = mAddress;
        this.mExecutionStatus = mExecutionStatus;
    }
    
    public UEReceiverAddedNotification(final byte[] array) {
        super(array);
        this.mExecutionStatus = ExecutionStatus.UNKNOWN;
        this.mAddress = new MAC(array, 3);
        this.mExecutionStatus = ExecutionStatus.getExecutionStatus(array[9]);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public MAC getAddress() {
        return this.mAddress;
    }
    
    public ExecutionStatus getExecutionStatus() {
        return this.mExecutionStatus;
    }
    
    public void readFromParcel(final Parcel parcel) {
        this.mAddress = (MAC)parcel.readParcelable(MAC.class.getClassLoader());
        this.mExecutionStatus = ExecutionStatus.getExecutionStatus(parcel.readInt());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[Notification receiver added to broadcast. ").append(String.format("Receiver MAC: %s ", this.mAddress)).append(String.format("Execution status %s", this.mExecutionStatus)).append("]");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeParcelable((Parcelable)this.mAddress, n);
        parcel.writeInt(this.mExecutionStatus.getCode());
    }
    
    public enum ExecutionStatus
    {
        CONNECTION_FAILED(1), 
        DELIVERED(0), 
        DOUBLE_PRESS_REQUIRED(3), 
        POWER_ON_REQUIRED(4), 
        UNKNOWN(-1);
        
        private static final SparseArray<ExecutionStatus> map;
        final int code;
        
        static {
            int i = 0;
            map = new SparseArray(4);
            for (ExecutionStatus[] values = values(); i < values.length; ++i) {
                final ExecutionStatus executionStatus = values[i];
                if (executionStatus != ExecutionStatus.UNKNOWN) {
                    ExecutionStatus.map.put(executionStatus.getCode(), (Object)executionStatus);
                }
            }
        }
        
        private ExecutionStatus(final int code) {
            this.code = code;
        }
        
        public static int getCode(final ExecutionStatus executionStatus) {
            return executionStatus.code;
        }
        
        public static ExecutionStatus getExecutionStatus(final int n) {
            return (ExecutionStatus)ExecutionStatus.map.get(n, (Object)ExecutionStatus.UNKNOWN);
        }
        
        public int getCode() {
            return this.code;
        }
    }
}
