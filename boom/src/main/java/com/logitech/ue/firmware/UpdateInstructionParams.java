// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

import java.util.HashMap;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UpdateInstructionParams implements Parcelable
{
    public static final Parcelable$Creator<UpdateInstructionParams> CREATOR;
    public static final String QUERY_PARAM_APP_NAME = "app_name";
    public static final String QUERY_PARAM_APP_VERSION = "app_version";
    public static final String QUERY_PARAM_DEVICE_TYPE = "speaker_type";
    public static final String QUERY_PARAM_FIRMWARE_VERSION = "fw_version";
    public static final String QUERY_PARAM_HARDWARE_REVISION = "hw_revision";
    public static final String QUERY_PARAM_PHONE_OS = "phone_os";
    public static final String QUERY_PARAM_PHONE_OS_VERSION = "fw_version";
    public String appName;
    public String appVersion;
    public String clientOS;
    public String clientOSVersion;
    public String deviceType;
    public String firmwareVersion;
    public String hardwareRevision;
    public String language;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UpdateInstructionParams>() {
            public UpdateInstructionParams createFromParcel(final Parcel parcel) {
                return new UpdateInstructionParams(parcel);
            }
            
            public UpdateInstructionParams[] newArray(final int n) {
                return new UpdateInstructionParams[n];
            }
        };
    }
    
    protected UpdateInstructionParams(final Parcel parcel) {
        this.appName = parcel.readString();
        this.appVersion = parcel.readString();
        this.deviceType = parcel.readString();
        this.hardwareRevision = parcel.readString();
        this.firmwareVersion = parcel.readString();
        this.clientOS = parcel.readString();
        this.clientOSVersion = parcel.readString();
        this.language = parcel.readString();
    }
    
    public UpdateInstructionParams(final String appName, final String appVersion, final String deviceType, final String hardwareRevision, final String firmwareVersion, final String clientOS, final String clientOSVersion, final String language) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.deviceType = deviceType;
        this.hardwareRevision = hardwareRevision;
        this.firmwareVersion = firmwareVersion;
        this.clientOS = clientOS;
        this.clientOSVersion = clientOSVersion;
        this.language = language;
    }
    
    public Map<String, String> buildQueryParamsMap() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("app_name", this.appName);
        hashMap.put("app_version", this.appVersion);
        hashMap.put("speaker_type", this.deviceType);
        hashMap.put("hw_revision", this.hardwareRevision);
        hashMap.put("fw_version", this.firmwareVersion);
        hashMap.put("phone_os", this.clientOS);
        hashMap.put("fw_version", this.clientOSVersion);
        return hashMap;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean equals;
        if (obj instanceof UpdateInstructionParams) {
            final UpdateInstructionParams updateInstructionParams = (UpdateInstructionParams)obj;
            equals = (this.appName.equals(updateInstructionParams.appName) && this.appVersion.equals(updateInstructionParams.appVersion) && this.deviceType.equals(updateInstructionParams.deviceType) && this.hardwareRevision.equals(updateInstructionParams.hardwareRevision) && this.firmwareVersion.equals(updateInstructionParams.firmwareVersion) && this.clientOS.equals(updateInstructionParams.clientOS) && this.clientOSVersion.equals(updateInstructionParams.clientOSVersion));
        }
        else {
            equals = super.equals(obj);
        }
        return equals;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.appName);
        parcel.writeString(this.appVersion);
        parcel.writeString(this.deviceType);
        parcel.writeString(this.hardwareRevision);
        parcel.writeString(this.firmwareVersion);
        parcel.writeString(this.clientOS);
        parcel.writeString(this.clientOSVersion);
        parcel.writeString(this.language);
    }
}
