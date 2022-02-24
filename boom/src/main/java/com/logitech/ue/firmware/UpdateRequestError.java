// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateRequestError
{
    public int errorCode;
    public String errorMessage;
    
    public static UpdateRequestError buildFromJSONString(final String s) {
        try {
            final UpdateRequestError updateRequestError = new UpdateRequestError();
            final JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("error_code")) {
                updateRequestError.errorCode = jsonObject.getInt("error_code");
            }
            UpdateRequestError updateRequestError2 = updateRequestError;
            if (jsonObject.has("error_msg")) {
                updateRequestError.errorMessage = jsonObject.getString("error_msg");
                updateRequestError2 = updateRequestError;
            }
            return updateRequestError2;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
