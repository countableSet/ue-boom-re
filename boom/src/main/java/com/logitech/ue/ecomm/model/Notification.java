// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm.model;

import org.json.JSONException;
import com.logitech.ue.ecomm.NotificationPopupBuilder;
import android.content.Context;
import org.json.JSONObject;

public class Notification
{
    public int duration;
    public String notificationID;
    public NotificationParams params;
    public String url;
    public NotificationViewPort viewPort;
    
    public Notification() {
    }
    
    public Notification(final String url, final int duration, final String notificationID, final NotificationParams params) {
        this.url = url;
        this.duration = duration;
        this.notificationID = notificationID;
        this.params = params;
    }
    
    public Notification(final JSONObject jsonObject) {
        final JSONObject optJSONObject = jsonObject.optJSONObject("position");
        this.viewPort = new NotificationViewPort();
        if (optJSONObject != null) {
            this.viewPort.x = (float)optJSONObject.optDouble("start-x");
            this.viewPort.y = (float)optJSONObject.optDouble("start-y");
            this.viewPort.height = (float)optJSONObject.optDouble("height");
            this.viewPort.width = (float)optJSONObject.optDouble("width");
            final JSONObject optJSONObject2 = optJSONObject.optJSONObject("params");
            if (optJSONObject2 != null) {
                this.params = new NotificationParams(optJSONObject2);
            }
        }
        this.url = jsonObject.optString("url");
        this.duration = jsonObject.optInt("display-duration");
        this.notificationID = jsonObject.optString("notification-id");
    }
    
    public NotificationPopupBuilder buildPopup(final Context context) {
        return new NotificationPopupBuilder(this, context);
    }
    
    public JSONObject toJSONObject() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("url", (Object)this.url);
            jsonObject.put("display-duration", this.duration);
            jsonObject.put("notification-id", (Object)this.notificationID);
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("start-x", (double)this.viewPort.x);
            jsonObject2.put("start-y", (double)this.viewPort.y);
            jsonObject2.put("start-width", (double)this.viewPort.width);
            jsonObject2.put("start-height", (double)this.viewPort.height);
            jsonObject.put("position", (Object)jsonObject2);
            if (this.params != null) {
                jsonObject.put("params", (Object)this.params.toJSONObject());
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
}
