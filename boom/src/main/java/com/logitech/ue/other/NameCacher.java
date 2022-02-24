// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import java.util.Iterator;
import org.json.JSONException;
import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.Context;
import android.preference.PreferenceManager;
import com.logitech.ue.App;
import android.content.SharedPreferences;
import com.logitech.ue.centurion.utils.MAC;
import java.util.HashMap;

public class NameCacher
{
    private static final String PREF_SPEAKER_NAME_MAP = "speaker_name_map";
    private static final String TAG;
    public static HashMap<MAC, NameProvision> mNameMap;
    public static SharedPreferences preferences;
    
    static {
        TAG = NameCacher.class.getSimpleName();
        NameCacher.mNameMap = new HashMap<MAC, NameProvision>();
        NameCacher.preferences = PreferenceManager.getDefaultSharedPreferences((Context)App.getInstance());
        loadNames();
    }
    
    private NameCacher() {
    }
    
    public static String checkDeviceName(final MAC key) {
        final NameProvision nameProvision = NameCacher.mNameMap.get(key);
        String name;
        if (nameProvision != null) {
            name = nameProvision.name;
        }
        else {
            name = null;
        }
        return name;
    }
    
    public static NameProvision checkDeviceNameProvision(final MAC key) {
        return NameCacher.mNameMap.get(key);
    }
    
    public static void clearName(final MAC key) {
        NameCacher.mNameMap.remove(key);
    }
    
    private static void loadNames() {
        final String string = NameCacher.preferences.getString("speaker_name_map", (String)null);
        if (string != null) {
            try {
                final JSONArray jsonArray = new JSONArray(string);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    final JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    NameCacher.mNameMap.put(new MAC((String)jsonObject.get("address")), new NameProvision((String)jsonObject.get("name"), (int)jsonObject.get("revision")));
                }
            }
            catch (Exception ex) {
                Log.e(NameCacher.TAG, "Failed to load", (Throwable)ex);
            }
        }
    }
    
    public static void saveDeviceName(final MAC mac, final String s, final int n) {
        if (s == null) {
            Log.w(NameCacher.TAG, "Can't save null name. Address: " + mac);
        }
        else if (!NameCacher.mNameMap.containsKey(mac) || NameCacher.mNameMap.get(mac).revision == n) {
            NameCacher.mNameMap.put(mac, new NameProvision(s, n));
            saveNames();
        }
    }
    
    private static void saveNames() {
        final JSONArray jsonArray = new JSONArray();
        for (final MAC key : NameCacher.mNameMap.keySet()) {
            try {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("address", (Object)key.toString());
                jsonObject.put("name", (Object)NameCacher.mNameMap.get(key).name);
                jsonObject.put("revision", NameCacher.mNameMap.get(key).revision);
                jsonArray.put((Object)jsonObject);
            }
            catch (JSONException ex) {
                Log.e(NameCacher.TAG, String.format("Failed to save. Address: %s Name: %s", key.toString(), NameCacher.mNameMap.get(key)));
            }
        }
        NameCacher.preferences.edit().putString("speaker_name_map", jsonArray.toString()).apply();
    }
    
    public static class NameProvision
    {
        public String name;
        public int revision;
        
        public NameProvision(final String name, final int revision) {
            this.name = name;
            this.revision = revision;
        }
    }
}
