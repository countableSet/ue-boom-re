// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import com.flurry.android.FlurryAgent;
import android.util.Log;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class FlurryTracker
{
    public static final String EVENT_ALARM_DISMISS = "alarm_dismiss";
    public static final String EVENT_ALARM_SNOOZE = "alarm_snooze";
    public static final String EVENT_APP_ACTIVATED = "app-activated";
    public static final String EVENT_APP_DEACTIVATED = "app-deactivated";
    public static final String EVENT_BALANCE_CHANGE = "balance-change";
    public static final String EVENT_BLOCK_PARTY_DJ_SWITCH = "bp_dj_switch";
    public static final String EVENT_BLOCK_PARTY_END = "bp_end";
    public static final String EVENT_BLOCK_PARTY_HOST_LEFT = "bp_host_left";
    public static final String EVENT_BLOCK_PARTY_MEMBER_JOIN = "bp_member_join";
    public static final String EVENT_BLOCK_PARTY_MEMBER_REMOVE = "bp_member_remove";
    public static final String EVENT_BLOCK_PARTY_PLAYBACK_PAUSE = "bp_playback_pause";
    public static final String EVENT_BLOCK_PARTY_PLAYBACK_PLAY = "bp_playback_play";
    public static final String EVENT_BLOCK_PARTY_PLAYBACK_SKIP = "bp_playback_skip";
    public static final String EVENT_BLOCK_PARTY_PLAY_PAUSE_PER_SESSION = "bp_play_pause_per_session";
    public static final String EVENT_BLOCK_PARTY_SESSION_DURATION = "bp_session_duration";
    public static final String EVENT_BLOCK_PARTY_START = "bp_start";
    public static final String EVENT_BLOCK_PARTY_UNIQUE_MEMBERS = "bp_unique_members";
    public static final String EVENT_DOUBLE_UP = "doubled_up";
    public static final String EVENT_EQ_CHANGE = "eq-change";
    public static final String EVENT_ERROR = "error";
    public static final String EVENT_OTA_CANCEL = "ota_cancel";
    public static final String EVENT_OTA_SUCCESS = "ota_success";
    public static final String EVENT_OTA_UPDATE = "ota_update";
    public static final String EVENT_PARAM_ALARM_MUSIC = "alarm-music";
    public static final String EVENT_PARAM_ALARM_STATE = "alarm-state";
    public static final String EVENT_PARAM_ALERT_LANGUAGE = "alert-language";
    public static final String EVENT_PARAM_ALERT_SOUNDS = "alert-sounds";
    public static final String EVENT_PARAM_APP_VERSION = "app-version";
    public static final String EVENT_PARAM_BLUETOOTH_SMART = "bluetooth-smart";
    public static final String EVENT_PARAM_COLOR_CODE = "color-code";
    public static final String EVENT_PARAM_COUNT = "count";
    public static final String EVENT_PARAM_DAILY_ALARM = "daily-alarm";
    public static final String EVENT_PARAM_DESCRIPTION = "description";
    public static final String EVENT_PARAM_DEVICE_TYPE = "device-type";
    public static final String EVENT_PARAM_DOUBLE_UP_LOCK = "double-up-lock";
    public static final String EVENT_PARAM_DURATION = "duration";
    public static final String EVENT_PARAM_EQ_PROFILE = "eq-profile";
    public static final String EVENT_PARAM_ERROR = "error";
    public static final String EVENT_PARAM_IN_APP_JOIN = "in-app-join";
    public static final String EVENT_PARAM_MASTER_COLOR = "master-color";
    public static final String EVENT_PARAM_MESSAGES = "messages";
    public static final String EVENT_PARAM_NEW_FIRMWARE = "new-firmware";
    public static final String EVENT_PARAM_OLD_FIRMWARE = "old-firmware";
    public static final String EVENT_PARAM_PLACE = "place";
    public static final String EVENT_PARAM_SLAVE_COLOR = "slave-color";
    public static final String EVENT_PARAM_SPEAKER_ID = "speaker-id";
    public static final String EVENT_PARAM_SPEAKER_VERSION = "speaker-version";
    public static final String EVENT_PARAM_STEREO = "stereo";
    public static final String EVENT_PARAM_UPDATE_STATUS = "update-status";
    public static final String EVENT_PARAM_USER_TOKEN = "user-token";
    public static final String EVENT_POWER_OFF = "remote_power_off";
    public static final String EVENT_POWER_ON = "remote_power_on";
    public static final String EVENT_STEREO_CHANGE = "stereo-change";
    public static final String EVENT_VOICE_SESSION = "voice_session";
    public static final String EVENT_XUP_ERROR = "xup_error";
    public static final String EVENT_XUP_INFO_TOOLTIP_TAPPED = "xup_info_tooltip_tapped";
    public static final String EVENT_XUP_MISSING_SPEAKER_TAPPED = "xup_missing_speaker_tapped";
    public static final String EVENT_XUP_RECEIVER_DISCOVERY = "xup_receiver_discovery";
    public static final String EVENT_XUP_RECEIVER_JOIN = "xup_receiver_join";
    public static final String EVENT_XUP_RECEIVER_LEFT = "xup_receiver_left";
    public static final String EVENT_XUP_SESSION_END = "xup_session_end";
    public static final String EVENT_XUP_SESSION_START = "xup_session_start";
    public static final String EVENT_XUP_VOLUME_SYNC = "xup_volume_sync";
    public static final String PARAM_VOICE_DURATION_1 = "voice_trigger_to_starting_tone_duration";
    public static final String PARAM_VOICE_DURATION_2 = "voice_trigger_to_ending_tone_duration";
    public static final String PARAM_VOICE_ENDING_TONE = "voice_if_ending_tone_detected";
    public static final String PARAM_VOICE_ERROR = "voice_error";
    public static final String PARAM_VOICE_STARTING_TONE = "voice_if_starting_tone_detected";
    public static final String PARAM_VOICE_TIMEOUT = "voice_ending_by_timeout";
    public static final String PARAM_VOICE_WAS_MUSIC_PLAYING = "voice_was_music_playing";
    private static final String PREF_USER_TOKEN = "FLURRY_USER_TOKEN";
    private static final String TAG = "FlurryTracker";
    private static String userToken;
    
    static {
        FlurryTracker.userToken = null;
    }
    
    public static String getUserToken() {
        if (FlurryTracker.userToken == null) {
            FlurryTracker.userToken = App.getInstance().getSharedPreferences(FlurryTracker.class.getSimpleName(), 0).getString("FLURRY_USER_TOKEN", (String)null);
            if (FlurryTracker.userToken == null) {
                FlurryTracker.userToken = UUID.randomUUID().toString();
                App.getInstance().getSharedPreferences(FlurryTracker.class.getSimpleName(), 0).edit().putString("FLURRY_USER_TOKEN", FlurryTracker.userToken).commit();
            }
        }
        return FlurryTracker.userToken;
    }
    
    public static void logAlarmDismiss(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("alarm-music", s);
        logEvent("alarm_dismiss", hashMap);
    }
    
    public static void logAlarmSnooze(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("alarm-music", s);
        logEvent("alarm_snooze", hashMap);
    }
    
    public static void logAppActivated(String s, final Boolean b, final Boolean b2, final boolean b3, final String s2, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("alert-language", s);
        if (b != null) {
            if (b) {
                s = "On";
            }
            else {
                s = "Off";
            }
        }
        else {
            s = "";
        }
        hashMap.put("alert-sounds", s);
        if (b2 != null) {
            if (b2) {
                s = "On";
            }
            else {
                s = "Off";
            }
        }
        else {
            s = "";
        }
        hashMap.put("double-up-lock", s);
        if (b3) {
            s = "On";
        }
        else {
            s = "Off";
        }
        hashMap.put("messages", s);
        hashMap.put("speaker-version", s2);
        hashMap.put("app-version", s3);
        logEvent("app-activated", hashMap);
    }
    
    public static void logAppDeactivated(final Boolean b, final String s, final String s2, final boolean b2, final boolean b3, final Boolean b4, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        String s4;
        if (b != null) {
            if (b) {
                s4 = "On";
            }
            else {
                s4 = "Off";
            }
        }
        else {
            s4 = "";
        }
        hashMap.put("bluetooth-smart", s4);
        hashMap.put("eq-profile", s);
        hashMap.put("alarm-music", s2);
        String s5;
        if (b2) {
            s5 = "On";
        }
        else {
            s5 = "Off";
        }
        hashMap.put("alarm-state", s5);
        String s6;
        if (b3) {
            s6 = "On";
        }
        else {
            s6 = "Off";
        }
        hashMap.put("daily-alarm", s6);
        String s7;
        if (b4 != null) {
            if (b4) {
                s7 = "On";
            }
            else {
                s7 = "Off";
            }
        }
        else {
            s7 = "";
        }
        hashMap.put("stereo", s7);
        hashMap.put("update-status", s3);
        logEvent("app-deactivated", hashMap);
    }
    
    public static void logBalanceChange() {
        logEvent("balance-change");
    }
    
    public static void logBlockPartyDJSwitch(final int i) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("count", String.valueOf(i));
        logEvent("bp_dj_switch", hashMap);
    }
    
    public static void logBlockPartyEnd() {
        logEvent("bp_end");
    }
    
    public static void logBlockPartyHostLeft() {
        logEvent("bp_host_left");
    }
    
    public static void logBlockPartyMemberJoin() {
        logEvent("bp_member_join");
    }
    
    public static void logBlockPartyMemberRemove() {
        logEvent("bp_member_remove");
    }
    
    public static void logBlockPartyPlayPausePerSession(final int i) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("count", String.valueOf(i));
        logEvent("bp_play_pause_per_session", hashMap);
    }
    
    public static void logBlockPartyPlaybackPause() {
        logEvent("bp_playback_pause");
    }
    
    public static void logBlockPartyPlaybackPlay() {
        logEvent("bp_playback_play");
    }
    
    public static void logBlockPartyPlaybackSkip() {
        logEvent("bp_playback_skip");
    }
    
    public static void logBlockPartySessionDuration(final long l) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("duration", String.valueOf(l));
        logEvent("bp_session_duration", hashMap);
    }
    
    public static void logBlockPartyStart() {
        logEvent("bp_start");
    }
    
    public static void logBlockPartyUniqueMembers(final int i) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("count", String.valueOf(i));
        logEvent("bp_unique_members", hashMap);
    }
    
    public static void logDoubleUp(final int n, final int n2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("master-color", String.format("%02X", n & 0xFF));
        hashMap.put("slave-color", String.format("%02X", n2 & 0xFF));
        logEvent("doubled_up", hashMap);
    }
    
    public static void logEQChange(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("eq-profile", s);
        logEvent("eq-change", hashMap);
    }
    
    public static void logError(final String s, final String s2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("place", s);
        hashMap.put("description", s2);
        logEvent("error", hashMap);
    }
    
    private static void logEvent(final String s) {
        logEvent(s, null);
    }
    
    private static void logEvent(final String s, final Map<String, String> map) {
        Map<String, String> map2 = map;
        if (map == null) {
            map2 = new HashMap<String, String>();
        }
        if (map2.size() != 0) {
            Log.d("FlurryTracker", "Logging event \"" + s + "\" to Flurry with value:" + map2.toString());
            map2.put("user-token", getUserToken());
            FlurryAgent.logEvent(s, map2);
        }
        else {
            Log.d("FlurryTracker", "Logging event \"" + s + "\" to Flurry ");
            map2.put("user-token", getUserToken());
            FlurryAgent.logEvent(s);
        }
    }
    
    public static void logOTACancel() {
        logEvent("ota_cancel");
    }
    
    public static void logOTAUpdate() {
        logEvent("ota_update");
    }
    
    public static void logOTAUpdateSuccess(final String s, final String s2, final String s3) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("device-type", s);
        hashMap.put("old-firmware", s2);
        hashMap.put("new-firmware", s3);
        logEvent("ota_success", hashMap);
    }
    
    public static void logRemotePowerOff() {
        logEvent("remote_power_off");
    }
    
    public static void logRemotePowerOn() {
        logEvent("remote_power_on");
    }
    
    public static void logStereoChange(final boolean b) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        String s;
        if (b) {
            s = "On";
        }
        else {
            s = "Off";
        }
        hashMap.put("stereo", s);
        logEvent("stereo-change", hashMap);
    }
    
    public static void logVoiceSession(final boolean b, long n, long n2, final long n3, final boolean b2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        String s;
        if (b) {
            s = "Yes";
        }
        else {
            s = "No";
        }
        hashMap.put("voice_was_music_playing", s);
        Log.d("FlurryTracker", "Flurry logging: voice_was_music_playing: " + b);
        String s2;
        if (n2 > 0L) {
            s2 = "Yes";
        }
        else {
            s2 = "No";
        }
        hashMap.put("voice_if_starting_tone_detected", s2);
        final StringBuilder append = new StringBuilder().append("Flurry logging: voice_if_starting_tone_detected: ");
        String str;
        if (n2 > 0L) {
            str = "Yes";
        }
        else {
            str = "No";
        }
        Log.d("FlurryTracker", append.append(str).toString());
        if (n2 > 0L) {
            n2 -= n;
            hashMap.put("voice_trigger_to_starting_tone_duration", n2 + " ms");
            Log.d("FlurryTracker", "Flurry logging: voice_trigger_to_starting_tone_duration: " + n2);
        }
        String s3;
        if (n3 > 0L) {
            s3 = "Yes";
        }
        else {
            s3 = "No";
        }
        hashMap.put("voice_if_ending_tone_detected", s3);
        final StringBuilder append2 = new StringBuilder().append("Flurry logging: voice_if_ending_tone_detected: ");
        String str2;
        if (n3 > 0L) {
            str2 = "Yes";
        }
        else {
            str2 = "No";
        }
        Log.d("FlurryTracker", append2.append(str2).toString());
        if (n3 > 0L) {
            n = n3 - n;
            hashMap.put("voice_trigger_to_ending_tone_duration", n + " ms");
            Log.d("FlurryTracker", "Flurry logging: voice_trigger_to_ending_tone_duration: " + n);
        }
        String s4;
        if (b2) {
            s4 = "Yes";
        }
        else {
            s4 = "No";
        }
        hashMap.put("voice_ending_by_timeout", s4);
        final StringBuilder append3 = new StringBuilder().append("Flurry logging: voice_ending_by_timeout: ");
        String str3;
        if (b2) {
            str3 = "Yes";
        }
        else {
            str3 = "No";
        }
        Log.d("FlurryTracker", append3.append(str3).toString());
        logEvent("voice_session", hashMap);
    }
    
    public static void logXUPEndSession() {
        logEvent("xup_session_end");
    }
    
    public static void logXUPError(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("error", s);
        logEvent("xup_error", hashMap);
    }
    
    public static void logXUPInfoTooltipTapped() {
        logEvent("xup_info_tooltip_tapped");
    }
    
    public static void logXUPMissingSpeakersTaped() {
        logEvent("xup_missing_speaker_tapped");
    }
    
    public static void logXUPReceiverDiscovery(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("speaker-id", s);
        logEvent("xup_receiver_discovery", hashMap);
    }
    
    public static void logXUPReceiverJoin(final String s, final int n, final String s2, final boolean b) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("speaker-id", s);
        hashMap.put("color-code", String.format("%02X", n & 0xFF));
        hashMap.put("device-type", s2);
        hashMap.put("in-app-join", String.valueOf(b));
        logEvent("xup_receiver_join", hashMap);
    }
    
    public static void logXUPReceiverLeft(final String s) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("speaker-id", s);
        logEvent("xup_receiver_left", hashMap);
    }
    
    public static void logXUPStartSession(final String s, final int n, final String s2) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("speaker-id", s);
        hashMap.put("color-code", String.format("%02X", n & 0xFF));
        hashMap.put("device-type", s2);
        logEvent("xup_session_start", hashMap);
    }
    
    public static void logXUPVolumeSync() {
        logEvent("xup_volume_sync");
    }
}
