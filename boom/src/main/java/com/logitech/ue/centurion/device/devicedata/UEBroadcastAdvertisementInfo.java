// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import com.logitech.ue.centurion.utils.UEUtils;
import com.logitech.ue.centurion.utils.MAC;

public class UEBroadcastAdvertisementInfo
{
    public static final int ADVERTISEMENT_PACKAGE_SIZE = 27;
    public static final int ADVERTISEMENT_PACKAGE_VENDOR_DATA_OFFSET = 14;
    public static final int FLAG_AUDIO_CONFIGURATION = 3;
    public static final int FLAG_AUTO_CONNECT = 8;
    public static final int FLAG_CONNECTED_NOT_STREAMING = 64;
    public static final int FLAG_CONNECT_BUTTON = 16;
    public static final int FLAG_KNOWN_MASK = 4;
    public static final int FLAG_NOT_CONNECTED_NOT_STREAMING = 32;
    public static final int FLAG_POWERED_OFF = 0;
    public static final int FLAG_STREAMING_BROADCAST = 224;
    public static final int FLAG_STREAMING_LOCAL = 96;
    public static final int FLAG_STREAMING_MASK = 224;
    public static final int SCAN_PACKAGE_SIZE = 23;
    public static final int SCAN_PACKAGE_VENDOR_DATA_OFFSET = 4;
    public static final int UUID_OFFSET = 6;
    public static final byte VENDOR_DATA_FLAG = 16;
    public byte batteryLevel;
    public MAC broadcastHostAddress;
    public int deviceColor;
    public byte events;
    public int extraFlags;
    public byte flags;
    public int nameRevision;
    public int volume;
    
    public UEBroadcastAdvertisementInfo(final byte[] array) {
        this.deviceColor = UEUtils.combineTwoBytesToOneInteger(array[14], array[15]);
        final int n = 14 + 2;
        final int n2 = n + 1;
        this.batteryLevel = array[n];
        final int n3 = n2 + 1;
        this.volume = array[n2];
        final int n4 = n3 + 1;
        this.flags = array[n3];
        this.nameRevision = (this.flags & 0xF);
        final int n5 = n4 + 1;
        this.events = array[n4];
        this.broadcastHostAddress = new MAC(array, n5);
        this.extraFlags = array[n5 + 6];
    }
    
    private static boolean checkUUID(final byte[] array) {
        return UEUtils.byteArrayToInt(array, 6) == 50553342;
    }
    
    public static boolean isValidatePackage(final byte[] array) {
        return array != null && array.length > 50 && checkUUID(array) && array[10] == 16;
    }
    
    public UEBroadcastReceiverInfo buildBroadcastReceiverInfo(final MAC mac, final MAC mac2) {
        final int deviceColor = this.deviceColor;
        final byte batteryLevel = this.batteryLevel;
        final int nameRevision = this.nameRevision;
        UEBroadcastReceiverStatus ueBroadcastReceiverStatus;
        if ((this.flags & 0xE0) == 0xE0) {
            if (this.broadcastHostAddress.equals(mac2)) {
                ueBroadcastReceiverStatus = UEBroadcastReceiverStatus.PLAYING_THIS_BROADCAST;
            }
            else {
                ueBroadcastReceiverStatus = UEBroadcastReceiverStatus.PLAYING_ANOTHER_BROADCAST;
            }
        }
        else {
            ueBroadcastReceiverStatus = UEBroadcastReceiverStatus.CONNECTED_NO_STREAMING;
        }
        return new UEBroadcastReceiverInfo(mac, deviceColor, true, batteryLevel, nameRevision, ueBroadcastReceiverStatus);
    }
}
