// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device;

import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioOptions;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastState;
import com.logitech.ue.centurion.device.devicedata.UEAVRCP;
import com.logitech.ue.centurion.device.devicedata.UEVoiceState;
import com.logitech.ue.centurion.device.devicedata.UEVoiceCapabilities;
import java.util.ArrayList;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import com.logitech.ue.centurion.device.devicedata.UEPlaybackMetadataType;
import com.logitech.ue.centurion.device.devicedata.UEPartitionFiveInfo;
import com.logitech.ue.centurion.device.devicedata.UEOTAStatus;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import com.logitech.ue.centurion.device.devicedata.UEHardwareInfo;
import com.logitech.ue.centurion.device.devicedata.UEFeaturesInfo;
import com.logitech.ue.centurion.device.devicedata.UEEQSetting;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStreamingStatus;
import com.logitech.ue.centurion.device.devicedata.UEChargingInfo;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastConfiguration;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyInfo;
import com.logitech.ue.centurion.device.devicedata.UEAudioRouting;
import com.logitech.ue.centurion.device.devicedata.UEAlarmInfo;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioMode;
import android.support.annotation.WorkerThread;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.connection.IUEConnector;
import com.logitech.ue.centurion.utils.MAC;

public class UEGenericDevice extends UEDevice
{
    public UEGenericDevice(final MAC mac, final IUEConnector iueConnector) {
        super(mac, iueConnector);
    }
    
    @WorkerThread
    public void NOP() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void ackAlarm() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void addReceiverToBroadcast(final MAC mac, final UEBroadcastAudioMode ueBroadcastAudioMode) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void adjustVolume(final boolean b, final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void announceBatteryLevel() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void beginDeviceDiscoveryMode() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void cancelOTA() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public byte[] checkPartitionState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void clearAlarm(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void decreaseSlaveVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void decreaseVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void emitSound(final UESoundProfile ueSoundProfile) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void erasePartition(final byte b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int get16Volume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int get32Volume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEAlarmInfo getAlarmInfo() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getAlarmVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEAudioRouting getAudioRouting() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean getBLEState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getBatteryLevel() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEBlockPartyInfo getBlockPartyInfo() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEBlockPartyState getBlockPartyState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public String getBluetoothName() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEBroadcastConfiguration getBroadcastMode() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getChargerRegister() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEChargingInfo getChargingInfo() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public String getConnectedDeviceNameRequest(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public byte[] getCustomEQ() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean getCustomState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getDeviceColor() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public String getDeviceID() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEDeviceStreamingStatus getDeviceStreamingStatus() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEDeviceType getDeviceType() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEEQSetting getEQSetting() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getEnableNotificationsMask() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEFeaturesInfo getFeaturesInfo() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public String getFirmwareVersion() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean getGestureState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEHardwareInfo getHardwareInfo() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public String getHardwareRevision() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UELanguage getLanguage() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEOTAStatus getOTAStatus() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEPartitionFiveInfo getPartitionFiveInfo() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getPartitionState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public MAC getPartyCurrentStreamingDeviceAddress() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public byte[] getPhoneBluetoothAddress() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public Object getPlaybackMetadataRequest(final MAC mac, final UEPlaybackMetadataType uePlaybackMetadataType) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    public String getProtocolVersion() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void getReceiverFixedAttributes(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void getReceiverOneAttribute(final MAC mac, final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void getReceiverVariableAttributes(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean getRepeatAlarm() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public String getSerialNumber() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public byte getSlaveBatteryLevel() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getSlaveVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getSonificationFileSize(final short n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UESonificationProfile getSonificationProfile() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean getStickyTWSOrPartyUpFlag() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public ArrayList<Integer> getSupportedLanguageIndex() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public byte getTWSBalance() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEVoiceCapabilities getVoiceCapabilities() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public UEVoiceState getVoiceState() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public int getVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void increaseSlaveVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void increaseVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean is5BandEQSupported() {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isAlarmSupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isBLESupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isBalanceSupported() {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isBassBoostSupported() {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isBlockPartySupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isBroadcastModeSupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isGestureSupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isOTASupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isPartitionValidForLanguage(final byte[] array) {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isVoiceSupported() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isVolume32Supported() {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public boolean isXUPPromiscuousModelOn() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void kickMemberFromParty(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void mountPartition(final int n, final int n2) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void remoteOff() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void remoteOffSlave() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void removeReceiverFromBroadcast(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void runDFU(final byte b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void sendAVRCPCommand(final UEAVRCP ueavrcp) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void sendAVRCPCommand(final UEAVRCP ueavrcp, final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void set16Volume(final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void set32Volume(final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setAlarm(final long n, final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setAlarmBackupTone(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setAlarmVolume(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setAudioRouting(final UEAudioRouting ueAudioRouting) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setBLEState(final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setBlockPartyState(final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setBluetoothName(final String s) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setBroadcastMode(final UEBroadcastState ueBroadcastState, final UEBroadcastAudioOptions ueBroadcastAudioOptions) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setCustomEQ(final byte[] array) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setCustomState(final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setDeviceStreamingStatus(final UEDeviceStreamingStatus ueDeviceStreamingStatus) {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setEQSetting(final UEEQSetting ueeqSetting) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setEnableNotificationsMask(final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setGestureState(final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setLanguage(final UELanguage ueLanguage) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setOTAStatus(final UEOTAStatus ueotaStatus) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setPartitionState(final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setPowerOn(final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setReceiverIdentificationSignal(final MAC mac, final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setReceiverOneAttribute(final MAC mac, final int n, final byte[] array) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setRepeatAlarm(final boolean b, final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setSnoozeTimeAlarm(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setSonificationProfile(final UESonificationProfile ueSonificationProfile) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setStickyTWSOrPartyUpFlag(final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setTWSBalance(final byte b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setVoiceLEDAndTone(final byte b, final byte b2) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setVoiceState(final UEVoiceState ueVoiceState) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setVolume(final int n) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void setXUPPromiscuousModel(final boolean b) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void snoozeAlarm() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void stopAlarm() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void stopRestreamingMode() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void syncBroadcastVolume() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void validateSQIF() throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
    
    @WorkerThread
    public void writeSQIF(final byte[] array) throws UEOperationException, UEConnectionException {
        throw new UnsupportedOperationException();
    }
}
