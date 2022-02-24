// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import android.content.SharedPreferences$Editor;
import android.support.annotation.Nullable;
import org.json.JSONException;
import com.logitech.ue.other.AlarmMusicType;
import com.logitech.ue.other.MusicSelection;
import java.sql.Time;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import com.logitech.ue.other.DeviceInfo;

public class UserPreferences
{
    public static final String ALARM_HOST_ADDRESS = "HostAddress";
    public static final String ALARM_IS_ON_KEY = "AlarmIsOn";
    public static final String ALARM_LAST_TIME_KEY = "AlarmLastTime";
    public static final String ALARM_MUSIC_SELECTION_ALBUM = "MusicSelection_album";
    public static final String ALARM_MUSIC_SELECTION_ALBUM_KEY = "MusicSelection_albumKey";
    public static final String ALARM_MUSIC_SELECTION_ARTIST = "MusicSelection_artist";
    public static final String ALARM_MUSIC_SELECTION_ARTIST_KEY = "MusicSelection_artistKey";
    public static final String ALARM_MUSIC_SELECTION_TITLE = "MusicSelection_title";
    public static final String ALARM_MUSIC_SELECTION_TITLE_KEY = "MusicSelection_titleKey";
    public static final String ALARM_MUSIC_TYPE_KEY = "AlarmMusicType";
    public static final String ALARM_REPEAT_KEY = "AlarmRepeat";
    public static final String ALARM_TIME_KEY = "AlarmTime";
    public static final String ALARM_VOLUME = "AlarmVolume";
    public static final String APP_WAS_RAN = "ApplicationWasRun";
    public static final String BLE_SETTING = "BluetoothSmartSetting";
    public static final String ECOMM_IS_NOTIFICATION_ENABLE_KEY = "NotificationsEnable";
    public static final String FIRMWARE_MANIFEST_UPDATE_TIME_KEY = "firmware update";
    public static final String GESTURE_TUTORIAL_COUNTER = "GestureTutorialCounter";
    public static final String GESTURE_TUTORIAL_ON = "GestureTutorialOn";
    public static final String LAST_SEEN_DEVICE = "LastSeenSpeaker";
    public static final String MANIFEST_KEY = "manifest";
    private static final String OFFER_CLICKED = "OfferClicked";
    private static final String OFFER_CLICK_LIMIT = "OfferClickLimit";
    private static final String OFFER_EXPIRE_TIME = "OfferTime";
    private static final String OFFER_URL = "OfferUrl";
    public static final String PARTYUP_WELCOME_SEEN = "PartyUpWelcomeSeen";
    public static final String PHONE_MAC_KEY = "PhoneMAC";
    public static final String SAW_XUP_ONBOARDING = "SawXUPOnBoarding";
    public static final String SPEAKER_FIRMWARE_VERSION = "SpeakerFirmwareVersion";
    private static final String TAG = "UserPreferences";
    public static final String XUP_LAST_SECCION_CONNECTION_COUNT = "XUPLastSessionConnectionCount";
    public static final String X_UP_SPEAKER_WAS_CONNECTED = "XUPSpeakerWasConnected";
    private static UserPreferences mInstance;
    static DeviceInfo mLastSeenDevice;
    final SharedPreferences mPref;
    
    static {
        UserPreferences.mInstance = null;
    }
    
    private UserPreferences(final Context context) {
        this.mPref = context.getSharedPreferences("UserPreferences", 0);
    }
    
    public static UserPreferences getInstance() {
        Label_0041: {
            if (UserPreferences.mInstance != null) {
                break Label_0041;
            }
            synchronized (UserPreferences.class) {
                if (UserPreferences.mInstance == null) {
                    UserPreferences.mInstance = new UserPreferences((Context)App.getInstance());
                    Log.d("UserPreferences", "UserPreferences instance instantiated.");
                }
                return UserPreferences.mInstance;
            }
        }
    }
    
    public Time getAlarmLastTime() {
        final long long1 = this.mPref.getLong("AlarmLastTime", 0L);
        Time time;
        if (long1 != 0L) {
            time = new Time(long1);
        }
        else {
            time = new Time(7, 0, 0);
        }
        return time;
    }
    
    public MusicSelection getAlarmMusicSelection() {
        Log.d("UserPreferences", "Load Music Selection from preferences");
        final MusicSelection musicSelection = new MusicSelection();
        musicSelection.titleName = this.mPref.getString("MusicSelection_title", (String)null);
        musicSelection.titleKey = this.mPref.getString("MusicSelection_titleKey", (String)null);
        musicSelection.albumName = this.mPref.getString("MusicSelection_album", (String)null);
        musicSelection.albumKey = this.mPref.getString("MusicSelection_albumKey", (String)null);
        musicSelection.artistName = this.mPref.getString("MusicSelection_artist", (String)null);
        musicSelection.artistKey = this.mPref.getString("MusicSelection_artistKey", (String)null);
        return musicSelection;
    }
    
    public AlarmMusicType getAlarmMusicType() {
        return AlarmMusicType.getAlarmByCode(this.mPref.getInt("AlarmMusicType", AlarmMusicType.LAST_PLAY.getCode()));
    }
    
    public int getAlarmVolume() {
        return this.mPref.getInt("AlarmVolume", -1);
    }
    
    public boolean getBleSetting() {
        return this.mPref.getBoolean("BluetoothSmartSetting", true);
    }
    
    public int getGestureCounter() {
        return this.mPref.getInt("GestureTutorialCounter", 0);
    }
    
    public String getHostAddress() {
        return this.mPref.getString("HostAddress", (String)null);
    }
    
    public boolean getIsAlarmOn() {
        return this.mPref.getBoolean("AlarmIsOn", false);
    }
    
    public boolean getIsAlarmRepeat() {
        return this.mPref.getBoolean("AlarmRepeat", false);
    }
    
    public long getLastManifestSyncTime() {
        return this.mPref.getLong("firmware update", 0L);
    }
    
    @Nullable
    public DeviceInfo getLastSeenDevice() {
        Label_0030: {
            if (UserPreferences.mLastSeenDevice != null) {
                break Label_0030;
            }
            final String string = this.mPref.getString("LastSeenSpeaker", (String)null);
            if (string == null) {
                break Label_0030;
            }
            try {
                UserPreferences.mLastSeenDevice = DeviceInfo.fromJSONObject(string);
                return UserPreferences.mLastSeenDevice;
            }
            catch (JSONException ex) {
                return UserPreferences.mLastSeenDevice;
            }
        }
    }
    
    public String getLastSeenSpeakerVersion() {
        return this.mPref.getString("SpeakerFirmwareVersion", "");
    }
    
    public String getManifest() {
        return this.mPref.getString("manifest", (String)null);
    }
    
    public int getOfferClicked() {
        return this.mPref.getInt("OfferClicked", 0);
    }
    
    public long getOfferExpireTime() {
        return this.mPref.getLong("OfferTime", -1L);
    }
    
    public int getOfferLimit() {
        return this.mPref.getInt("OfferClickLimit", -1);
    }
    
    public String getOfferUrl() {
        return this.mPref.getString("OfferUrl", (String)null);
    }
    
    public String getPhoneMAC() {
        return this.mPref.getString("PhoneMAC", (String)null);
    }
    
    public int getXUPLastSessionConnectionCount() {
        return this.mPref.getInt("XUPLastSessionConnectionCount", 0);
    }
    
    public boolean isAppWasRun() {
        return this.mPref.getBoolean("ApplicationWasRun", false);
    }
    
    public boolean isGestureOn() {
        return this.mPref.getBoolean("GestureTutorialOn", true);
    }
    
    public Boolean isNotificationsEnable() {
        return this.mPref.getBoolean("NotificationsEnable", true);
    }
    
    public boolean isPartyUpWelcomeDialogSeen() {
        return this.mPref.getBoolean("PartyUpWelcomeSeen", false);
    }
    
    public Boolean isXUPOnBoardingWasSeen() {
        return this.mPref.getBoolean("SawXUPOnBoarding", false);
    }
    
    public boolean isXUPSpeakerWasConnected() {
        return this.mPref.getBoolean("XUPSpeakerWasConnected", false);
    }
    
    public void setAlarmLastTime(final Time time) {
        this.mPref.edit().putLong("AlarmLastTime", time.getTime()).apply();
    }
    
    public void setAlarmMusicSelection(final MusicSelection musicSelection) {
        Log.d("UserPreferences", "Save Music Selection to preferences");
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putString("MusicSelection_title", musicSelection.titleName);
        edit.putString("MusicSelection_titleKey", musicSelection.titleKey);
        edit.putString("MusicSelection_album", musicSelection.albumName);
        edit.putString("MusicSelection_albumKey", musicSelection.albumKey);
        edit.putString("MusicSelection_artist", musicSelection.artistName);
        edit.putString("MusicSelection_artistKey", musicSelection.artistKey);
        edit.apply();
    }
    
    public void setAlarmMusicType(final AlarmMusicType alarmMusicType) {
        if (alarmMusicType != null) {
            this.mPref.edit().putInt("AlarmMusicType", alarmMusicType.getCode()).apply();
        }
        else {
            this.mPref.edit().remove("AlarmMusicType");
        }
    }
    
    public void setAlarmVolume(final int n) {
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putInt("AlarmVolume", n);
        edit.apply();
    }
    
    public void setAppWasRan(final boolean b) {
        this.mPref.edit().putBoolean("ApplicationWasRun", b).apply();
    }
    
    public void setBleSetting(final boolean b) {
        this.mPref.edit().putBoolean("BluetoothSmartSetting", b).apply();
    }
    
    public void setGestureCounter(final int n) {
        this.mPref.edit().putInt("GestureTutorialCounter", n).apply();
    }
    
    public void setGestureOn(final boolean b) {
        this.mPref.edit().putBoolean("GestureTutorialOn", b).apply();
    }
    
    public void setHostAddress(final String s) {
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putString("HostAddress", s);
        edit.apply();
    }
    
    public void setIsAlarmOn(final boolean b) {
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putBoolean("AlarmIsOn", b);
        edit.apply();
    }
    
    public void setIsAlarmRepeat(final boolean b) {
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putBoolean("AlarmRepeat", b);
        edit.apply();
    }
    
    public void setLastManifestSyncUpdate(final long n) {
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putLong("firmware update", n);
        edit.apply();
    }
    
    public void setLastSeenDevice(final DeviceInfo mLastSeenDevice) {
        UserPreferences.mLastSeenDevice = mLastSeenDevice;
        try {
            this.mPref.edit().putString("LastSeenSpeaker", mLastSeenDevice.toJSONObject().toString()).apply();
        }
        catch (JSONException ex) {}
    }
    
    public void setLastSeenSpeakerVersion(final String s) {
        this.mPref.edit().putString("SpeakerFirmwareVersion", s).apply();
    }
    
    public void setManifest(final String s) {
        if (s != null) {
            this.mPref.edit().putString("manifest", s).apply();
            Log.i("UserPreferences", "User preference manifest updated");
        }
    }
    
    public void setNotificationsEnable(final boolean b) {
        final SharedPreferences$Editor edit = this.mPref.edit();
        edit.putBoolean("NotificationsEnable", b);
        edit.apply();
    }
    
    public void setOfferClicked(final int n) {
        this.mPref.edit().putInt("OfferClicked", n).apply();
    }
    
    public void setPartyUpWelcomeDialogSeen(final boolean b) {
        this.mPref.edit().putBoolean("PartyUpWelcomeSeen", b).apply();
    }
    
    public void setPhoneMAC(final String s) {
        this.mPref.edit().putString("PhoneMAC", s).apply();
    }
    
    public void setXUPLastSessionConnectionCount(final int n) {
        this.mPref.edit().putInt("XUPLastSessionConnectionCount", n).apply();
    }
    
    public boolean setXUPOnBoardingWasSeen(final boolean b) {
        return this.mPref.edit().putBoolean("SawXUPOnBoarding", b).commit();
    }
    
    public void setXUPSpeakerWasConnected(final boolean b) {
        this.mPref.edit().putBoolean("XUPSpeakerWasConnected", b).apply();
    }
}
