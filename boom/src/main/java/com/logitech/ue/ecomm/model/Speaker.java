// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Speaker
{
    public int colorCode;
    public String firmwareVersion;
    public String speakerModel;
    
    public Speaker() {
        this.colorCode = -1;
    }
    
    public Speaker(final JSONObject jsonObject) {
        this.colorCode = -1;
        this.speakerModel = jsonObject.optString("model");
        this.colorCode = jsonObject.optInt("color-code");
        this.firmwareVersion = jsonObject.optString("firmware");
    }
    
    public JSONObject toJSONObject() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", (Object)this.speakerModel);
            jsonObject.put("color-code", this.colorCode);
            jsonObject.put("firmware", (Object)this.firmwareVersion);
            return jsonObject;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return jsonObject;
        }
    }
}
