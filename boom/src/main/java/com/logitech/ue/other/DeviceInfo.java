// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import org.json.JSONException;
import org.json.JSONObject;
import com.logitech.ue.centurion.utils.MAC;

public class DeviceInfo
{
    public MAC address;
    public int color;
    public boolean isBlockPartySupported;
    public boolean isXupSupported;
    public String name;
    
    public static DeviceInfo fromJSONObject(final String s) throws JSONException {
        final DeviceInfo deviceInfo = new DeviceInfo();
        final JSONObject jsonObject = new JSONObject(s);
        deviceInfo.color = jsonObject.getInt("color");
        deviceInfo.name = jsonObject.getString("name");
        deviceInfo.address = new MAC(jsonObject.getString("address"));
        deviceInfo.isBlockPartySupported = jsonObject.getBoolean("is_block_party_supported");
        deviceInfo.isXupSupported = jsonObject.getBoolean("is_xup_supported");
        return deviceInfo;
    }
    
    public JSONObject toJSONObject() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("color", this.color);
        jsonObject.put("name", (Object)this.name);
        jsonObject.put("address", (Object)this.address.toString());
        jsonObject.put("is_block_party_supported", this.isBlockPartySupported);
        jsonObject.put("is_xup_supported", this.isXupSupported);
        return jsonObject;
    }
}
