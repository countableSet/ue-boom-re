// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UpdateStepInfo implements Parcelable
{
    public static final Parcelable$Creator<UpdateStepInfo> CREATOR;
    public String firmwareDownloadURL;
    public String firmwareVersion;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UpdateStepInfo>() {
            public UpdateStepInfo createFromParcel(final Parcel parcel) {
                return new UpdateStepInfo(parcel);
            }
            
            public UpdateStepInfo[] newArray(final int n) {
                return new UpdateStepInfo[n];
            }
        };
    }
    
    public UpdateStepInfo() {
    }
    
    public UpdateStepInfo(final Parcel parcel) {
        this.firmwareVersion = parcel.readString();
        this.firmwareDownloadURL = parcel.readString();
    }
    
    public UpdateStepInfo(final String firmwareVersion, final String firmwareDownloadURL) {
        this.firmwareVersion = firmwareVersion;
        this.firmwareDownloadURL = firmwareDownloadURL;
    }
    
    public static UpdateStepInfo buildFromJSONString(final JSONObject jsonObject) {
        try {
            final UpdateStepInfo updateStepInfo = new UpdateStepInfo();
            if (jsonObject.has("fw_version")) {
                updateStepInfo.firmwareVersion = jsonObject.getString("fw_version");
            }
            UpdateStepInfo updateStepInfo2 = updateStepInfo;
            if (jsonObject.has("fw_download_url")) {
                updateStepInfo.firmwareDownloadURL = jsonObject.getString("fw_download_url");
                updateStepInfo2 = updateStepInfo;
            }
            return updateStepInfo2;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.firmwareVersion);
        parcel.writeString(this.firmwareDownloadURL);
    }
}
