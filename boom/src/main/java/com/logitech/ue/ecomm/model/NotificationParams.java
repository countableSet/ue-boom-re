// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm.model;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import com.logitech.ue.ecomm.UserAction;
import java.util.List;

public class NotificationParams
{
    public String appName;
    public String appVersion;
    public List<Speaker> connectedSpeakers;
    public int displayDpi;
    public int displayHeight;
    public int displayWidth;
    public String inAppPosition;
    public String lastClickedId;
    public long lastClickedTime;
    public String lastClickedURL;
    public String lastDismissedNotificationID;
    public long lastDismissedTime;
    public UserAction lastNoticeAction;
    public String lastNoticeNotificationId;
    public long lastNoticeTime;
    public String locale;
    public String phoneMaker;
    public String phoneModel;
    public String phoneOS;
    public String phoneOSVersion;
    public double phoneTimeZone;
    public String promotionAllowed;
    public int totalSpeakersSeen;
    
    public NotificationParams() {
        this.connectedSpeakers = new ArrayList<Speaker>();
        this.lastNoticeTime = -1L;
        this.lastClickedTime = -1L;
        this.lastDismissedTime = -1L;
    }
    
    public NotificationParams(JSONObject optJSONObject) {
        this.connectedSpeakers = new ArrayList<Speaker>();
        this.lastNoticeTime = -1L;
        this.lastClickedTime = -1L;
        this.lastDismissedTime = -1L;
        this.promotionAllowed = optJSONObject.optString("promotion-allowed");
        this.appName = optJSONObject.optString("app-name");
        this.appVersion = optJSONObject.optString("app-version");
        this.inAppPosition = optJSONObject.optString("in-app-position");
        final JSONArray optJSONArray = optJSONObject.optJSONArray("connected-speakers");
        if (optJSONArray != null) {
            int i = 0;
        Label_0128_Outer:
            while (i < optJSONArray.length()) {
                while (true) {
                    try {
                        this.connectedSpeakers.add(new Speaker(optJSONArray.getJSONObject(i)));
                        ++i;
                        continue Label_0128_Outer;
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    break;
                }
                break;
            }
        }
        this.totalSpeakersSeen = optJSONObject.optInt("total-speakers-seen");
        this.locale = optJSONObject.optString("locale");
        this.phoneMaker = optJSONObject.optString("phone-maker");
        this.phoneModel = optJSONObject.optString("phone-model");
        this.phoneOS = optJSONObject.optString("phone-os");
        this.phoneOSVersion = optJSONObject.optString("phone-os-version");
        this.displayWidth = optJSONObject.optInt("display-width");
        this.displayHeight = optJSONObject.optInt("display-height");
        this.displayDpi = optJSONObject.optInt("display-dpi");
        this.phoneTimeZone = optJSONObject.optDouble("phone-time-zone");
        final JSONObject optJSONObject2 = optJSONObject.optJSONObject("last-notice");
        if (optJSONObject2 != null) {
            this.lastNoticeTime = optJSONObject2.optLong("time");
            this.lastNoticeNotificationId = optJSONObject2.optString("id");
            this.lastNoticeAction = UserAction.fromString(optJSONObject2.optString("action"));
        }
        final JSONObject optJSONObject3 = optJSONObject.optJSONObject("last-clicked");
        if (optJSONObject3 != null) {
            this.lastClickedTime = optJSONObject3.optLong("time");
            this.lastClickedId = optJSONObject3.optString("id");
            this.lastClickedURL = optJSONObject3.optString("url");
        }
        optJSONObject = optJSONObject.optJSONObject("last-dismissed");
        if (optJSONObject != null) {
            this.lastDismissedTime = optJSONObject.optLong("time");
            this.lastDismissedNotificationID = optJSONObject.optString("id");
        }
    }
    
    public NotificationParams(final boolean b, final List<Speaker> connectedSpeakers, final String inAppPosition, final int displayWidth, final int displayHeight, final int displayDpi, final String phoneMaker, final String phoneModel, final String phoneOS, final String phoneOSVersion, final float n, final long lastNoticeTime, final String lastNoticeNotificationId, final UserAction lastNoticeAction, final long lastClickedTime, final String lastClickedId, final String lastClickedURL, final long lastDismissedTime, final String lastDismissedNotificationID) {
        this.connectedSpeakers = new ArrayList<Speaker>();
        this.lastNoticeTime = -1L;
        this.lastClickedTime = -1L;
        this.lastDismissedTime = -1L;
        this.promotionAllowed = Boolean.toString(b);
        this.connectedSpeakers = connectedSpeakers;
        this.inAppPosition = inAppPosition;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
        this.displayDpi = displayDpi;
        this.phoneMaker = phoneMaker;
        this.phoneModel = phoneModel;
        this.phoneOS = phoneOS;
        this.phoneOSVersion = phoneOSVersion;
        this.phoneTimeZone = n;
        this.lastNoticeTime = lastNoticeTime;
        this.lastNoticeNotificationId = lastNoticeNotificationId;
        this.lastNoticeAction = lastNoticeAction;
        this.lastClickedId = lastClickedId;
        this.lastClickedTime = lastClickedTime;
        this.lastClickedURL = lastClickedURL;
        this.lastDismissedTime = lastDismissedTime;
        this.lastDismissedNotificationID = lastDismissedNotificationID;
    }
    
    public JSONObject toJSONObject() {
        final JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = null;
        Label_0102: {
            try {
                jsonObject.put("promotion-allowed", (Object)this.promotionAllowed);
                jsonObject.put("app-name", (Object)this.appName);
                jsonObject.put("app-version", (Object)this.appVersion);
                jsonObject.put("in-app-position", (Object)this.inAppPosition);
                jsonArray = new JSONArray();
                final Iterator<Speaker> iterator = this.connectedSpeakers.iterator();
                while (iterator.hasNext()) {
                    jsonArray.put((Object)iterator.next().toJSONObject());
                }
                break Label_0102;
            }
            catch (JSONException ex) {}
            return jsonObject;
        }
        jsonObject.put("connected-speakers", (Object)jsonArray);
        jsonObject.put("total-speakers-seen", this.totalSpeakersSeen);
        jsonObject.put("locale", (Object)this.locale);
        jsonObject.put("phone-maker", (Object)this.phoneMaker);
        jsonObject.put("phone-model", (Object)this.phoneModel);
        jsonObject.put("phone-os", (Object)this.phoneOS);
        jsonObject.put("phone-os-version", (Object)this.phoneOSVersion);
        jsonObject.put("display-width", this.displayWidth);
        jsonObject.put("display-height", this.displayHeight);
        jsonObject.put("display-dpi", this.displayDpi);
        jsonObject.put("phone-time-zone", this.phoneTimeZone);
        final JSONObject jsonObject2 = new JSONObject();
        if (this.lastNoticeTime != -1L) {
            jsonObject2.put("time", this.lastNoticeTime);
            String lastNoticeNotificationId;
            if (this.lastNoticeNotificationId != null) {
                lastNoticeNotificationId = this.lastNoticeNotificationId;
            }
            else {
                lastNoticeNotificationId = "";
            }
            jsonObject2.put("id", (Object)lastNoticeNotificationId);
            String s;
            if (this.lastNoticeAction != null) {
                s = this.lastNoticeAction.toString();
            }
            else {
                s = UserAction.None.toString();
            }
            jsonObject2.put("action", (Object)s);
        }
        jsonObject.put("last-notice", (Object)jsonObject2);
        final JSONObject jsonObject3 = new JSONObject();
        if (this.lastClickedTime != -1L) {
            jsonObject3.put("time", this.lastClickedTime);
            String lastClickedId;
            if (this.lastClickedId != null) {
                lastClickedId = this.lastClickedId;
            }
            else {
                lastClickedId = "";
            }
            jsonObject3.put("id", (Object)lastClickedId);
            String lastClickedURL;
            if (this.lastClickedURL != null) {
                lastClickedURL = this.lastClickedURL;
            }
            else {
                lastClickedURL = "";
            }
            jsonObject3.put("url", (Object)lastClickedURL);
        }
        jsonObject.put("last-clicked", (Object)jsonObject3);
        final JSONObject jsonObject4 = new JSONObject();
        if (this.lastDismissedTime != -1L) {
            jsonObject4.put("time", this.lastDismissedTime);
            String lastDismissedNotificationID;
            if (this.lastDismissedNotificationID != null) {
                lastDismissedNotificationID = this.lastDismissedNotificationID;
            }
            else {
                lastDismissedNotificationID = "";
            }
            jsonObject4.put("id", (Object)lastDismissedNotificationID);
        }
        jsonObject.put("last-dismissed", (Object)jsonObject4);
        return jsonObject;
    }
}
