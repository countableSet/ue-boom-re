// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import android.os.Parcel;
import java.util.ArrayList;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UpdateInstruction implements Parcelable
{
    public static final Parcelable$Creator<UpdateInstruction> CREATOR;
    public String detailsURL;
    public boolean isUpdateAvailable;
    public String postUpdateMessage;
    public String preUpdateMessage;
    public final ArrayList<UpdateStepInfo> updateStepInfoList;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UpdateInstruction>() {
            public UpdateInstruction createFromParcel(final Parcel parcel) {
                return new UpdateInstruction(parcel);
            }
            
            public UpdateInstruction[] newArray(final int n) {
                return new UpdateInstruction[n];
            }
        };
    }
    
    public UpdateInstruction() {
        this.updateStepInfoList = new ArrayList<UpdateStepInfo>();
    }
    
    public UpdateInstruction(final Parcel parcel) {
        boolean isUpdateAvailable = true;
        parcel.readTypedList((List)(this.updateStepInfoList = new ArrayList<UpdateStepInfo>()), (Parcelable$Creator)UpdateStepInfo.CREATOR);
        if (parcel.readInt() != 1) {
            isUpdateAvailable = false;
        }
        this.isUpdateAvailable = isUpdateAvailable;
        this.preUpdateMessage = parcel.readString();
        this.postUpdateMessage = parcel.readString();
        this.detailsURL = parcel.readString();
    }
    
    public static UpdateInstruction buildFromJSONString(final String s) {
        try {
            final UpdateInstruction updateInstruction = new UpdateInstruction();
            final JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("update_available")) {
                updateInstruction.isUpdateAvailable = jsonObject.getBoolean("update_available");
            }
            if (jsonObject.has("firmwares")) {
                final JSONArray jsonArray = jsonObject.getJSONArray("firmwares");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    final UpdateStepInfo buildFromJSONString = UpdateStepInfo.buildFromJSONString((JSONObject)jsonArray.get(i));
                    if (buildFromJSONString != null) {
                        updateInstruction.updateStepInfoList.add(buildFromJSONString);
                    }
                }
            }
            if (jsonObject.has("fw_detail_url")) {
                updateInstruction.detailsURL = jsonObject.getString("fw_detail_url");
            }
            if (jsonObject.has("fw_pre_update_message")) {
                updateInstruction.preUpdateMessage = jsonObject.getString("fw_pre_update_message");
            }
            UpdateInstruction updateInstruction2 = updateInstruction;
            if (jsonObject.has("fw_post_update_message")) {
                updateInstruction.postUpdateMessage = jsonObject.getString("fw_post_update_message");
                updateInstruction2 = updateInstruction;
            }
            return updateInstruction2;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeTypedList((List)this.updateStepInfoList);
        if (this.isUpdateAvailable) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeString(this.preUpdateMessage);
        parcel.writeString(this.postUpdateMessage);
        parcel.writeString(this.detailsURL);
    }
}
