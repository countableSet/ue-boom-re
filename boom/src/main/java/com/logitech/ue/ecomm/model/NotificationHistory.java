// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm.model;

import java.util.Iterator;
import com.logitech.ue.ecomm.UserAction;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.ArrayList;

public class NotificationHistory extends ArrayList<NotificationStory>
{
    private int mMaxSize;
    
    public NotificationHistory() {
    }
    
    public NotificationHistory(final int mMaxSize) {
        this.mMaxSize = mMaxSize;
    }
    
    public NotificationHistory(final JSONArray jsonArray) {
        int n = 0;
    Label_0033_Outer:
        while (true) {
            if (n >= jsonArray.length()) {
                return;
            }
            while (true) {
                try {
                    this.add(new NotificationStory(jsonArray.getJSONObject(n)));
                    ++n;
                    continue Label_0033_Outer;
                }
                catch (JSONException ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public boolean add(final NotificationStory e) {
        while (this.size() > this.mMaxSize) {
            this.remove(0);
        }
        return super.add(e);
    }
    
    public NotificationStory getDismissedStory() {
        for (final NotificationStory notificationStory : this) {
            if (notificationStory.userAction == UserAction.Dismiss) {
                return notificationStory;
            }
        }
        return null;
    }
    
    public NotificationStory getLastClickedStory() {
        for (final NotificationStory notificationStory : this) {
            if (notificationStory.userAction == UserAction.UrlClicked) {
                return notificationStory;
            }
        }
        return null;
    }
    
    public NotificationStory getLastStory() {
        NotificationStory notificationStory;
        if (this.size() == 0) {
            notificationStory = null;
        }
        else {
            notificationStory = this.get(this.size() - 1);
        }
        return notificationStory;
    }
    
    public int getMaxSize() {
        return this.mMaxSize;
    }
    
    public void setMaxSize(final int mMaxSize) {
        this.mMaxSize = mMaxSize;
        while (this.size() > this.mMaxSize) {
            this.remove(0);
        }
    }
    
    public JSONArray toJSONObject() {
        final JSONArray jsonArray = new JSONArray();
        final Iterator<NotificationStory> iterator = this.iterator();
        while (iterator.hasNext()) {
            jsonArray.put((Object)iterator.next().toJSONObject());
        }
        return jsonArray;
    }
}
