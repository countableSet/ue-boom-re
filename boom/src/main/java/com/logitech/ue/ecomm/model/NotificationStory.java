// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm.model;

import org.json.JSONException;
import java.util.Iterator;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import com.logitech.ue.ecomm.UserAction;
import java.util.Map;

public class NotificationStory
{
    public Map<String, Object> additionParams;
    public Notification notification;
    public long timestamp;
    public UserAction userAction;
    
    public NotificationStory(final Notification notification, final UserAction userAction) {
        this.userAction = UserAction.None;
        this.additionParams = new HashMap<String, Object>();
        this.notification = notification;
        this.userAction = userAction;
        this.timestamp = Calendar.getInstance().getTimeInMillis() / 1000L;
    }
    
    public NotificationStory(final JSONObject jsonObject) {
        this.userAction = UserAction.None;
        this.additionParams = new HashMap<String, Object>();
        this.notification = new Notification(jsonObject.optJSONObject("notification"));
        this.userAction = UserAction.fromString(jsonObject.optString("userAction"));
        this.timestamp = jsonObject.optLong("timestamp");
        final JSONObject optJSONObject = jsonObject.optJSONObject("params");
        if (optJSONObject != null) {
            final HashMap<String, Object> additionParams = new HashMap<String, Object>();
            final Iterator keys = optJSONObject.keys();
            while (keys.hasNext()) {
                final String key = keys.next();
                additionParams.put(key, optJSONObject.opt(key));
            }
            this.additionParams = additionParams;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        boolean b;
        if (obj instanceof NotificationStory) {
            b = this.notification.notificationID.equals(((NotificationStory)obj).notification.notificationID);
        }
        else {
            b = super.equals(obj);
        }
        return b;
    }
    
    public JSONObject toJSONObject() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("notification", (Object)this.notification.toJSONObject());
            String s;
            if (this.userAction != null) {
                s = this.userAction.toString();
            }
            else {
                s = UserAction.None.toString();
            }
            jsonObject.put("userAction", (Object)s);
            jsonObject.put("timestamp", this.timestamp);
            jsonObject.put("params", (Object)new JSONObject((Map)this.additionParams));
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
}
