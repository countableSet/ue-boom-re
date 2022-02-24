// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.command;

import android.util.SparseIntArray;

public class UEDeviceCommand extends UEGenericDeviceCommand
{
    public static final int COMMAND_MIN_SIZE = 3;
    public static final int COMMAND_PAYDATA_OFFSET = 3;
    public static final String PROTOCOL_VERSION = "0100";
    protected static final SparseIntArray mReturnCommandsMap;
    private static final long serialVersionUID = 0L;
    
    static {
        (mReturnCommandsMap = new SparseIntArray()).put(UECommand.QueryProtocolVersion.value, UECommand.ReturnProtocolVersion.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryFirmwareVersion.value, UECommand.ReturnFirmwareVersion.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryDeviceId.value, UECommand.ReturnDeviceId.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryLanguageSetting.value, UECommand.ReturnLanguageSetting.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySonfication.value, UECommand.ReturnSonfication.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryDeviceStatus.value, UECommand.ReturnDeviceStatus.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryBluetoothName.value, UECommand.ReturnBluetoothName.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryHardwareId.value, UECommand.ReturnHardwareId.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryAudioRouting.value, UECommand.ReturnAudioRouting.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryChargingInfo.value, UECommand.ReturnChargingInfo.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryEQSetting.value, UECommand.ReturnEQSetting.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryNumberOfOtherConnectedDevices.value, UECommand.ReturnNumberOfOtherConnectedDevices.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySerialNumber.value, UECommand.ReturnSerialNumber.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryAlarm.value, UECommand.ReturnAlarm.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryChargerRegister.value, UECommand.ReturnChargerRegister.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySoCVariables.value, UECommand.ReturnSoCVariables.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryHardwareRevision.value, UECommand.ReturnHardwareRevision.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryEQTable.value, UECommand.ReturnEQTable.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.Query32Volume.value, UECommand.Return32Volume.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryVolume.value, UECommand.ReturnVolume.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryTWSSavePairFlag.value, UECommand.ReturnTWSSavePairFlag.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySupportedLanguage.value, UECommand.ReturnSupportedLanguage.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryCustomState.value, UECommand.ReturnCustomState.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryTWSBalance.value, UECommand.ReturnTWSBalance.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryFeatures.value, UECommand.ReturnFeatures.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryPlaybackMetadata.value, UECommand.ReturnPlaybackMetadata.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySleepTimer.value, UECommand.ReturnSleepTimer.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryConnectedDeviceNameRequest.value, UECommand.ReturnConnectedDeviceName.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryOTAState.value, UECommand.ReturnOTAState.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySlaveBatteryLevel.value, UECommand.ReturnSlaveBatteryLevel.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySourceBluetoothAddress.value, UECommand.ReturnSourceBluetoothAddress.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryDeviceBluetoothAddress.value, UECommand.ReturnDeviceBluetoothAddress.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryTWSSlaveBluetoothAddress.value, UECommand.ReturnTWSSlaveBluetoothAddress.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryAUXLevelInDB.value, UECommand.ReturnAUXLevelInDB.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryBLEState.value, UECommand.ReturnBTLEState.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QuerySlaveVolume.value, UECommand.ReturnSlaveVolume.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryGestureEnable.value, UECommand.ReturnGestureEnable.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.GetPartitionMountState.value, UECommand.ReturnPartitionMountState.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.CheckPartitionState.value, UECommand.ReturnCheckPartitionState.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryEnableNotifications.value, UECommand.ReturnEnableNotifications.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryPartitionFiveInfo.value, UECommand.ReturnPartitionFiveInfo.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.GetSonificationFileSize.value, UECommand.ReturnSonificationFileSize.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryBlockPartyState.value, UECommand.ReturnBlockPartyState.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryBlockPartyInfo.value, UECommand.ReturnBlockPartyInfo.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryBlockPartyCurrentStreamingDeviceMAC.value, UECommand.ReturnBlockPartyCurrentStreamingDeviceMAC.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryBroadcastMode.value, UECommand.ReturnBroadcastMode.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.QueryXUPPromiscuousMode.value, UECommand.ReturnXUPPromiscuousMode.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.GetVoiceControlFlag.value, UECommand.ReturnVoiceControlFlag.value);
        UEDeviceCommand.mReturnCommandsMap.put(UECommand.GetVoiceCapabilities.value, UECommand.ReturnVoiceCapabilities.value);
    }
    
    protected UEDeviceCommand(final UECommand ueCommand, final byte[] array) {
        super((short)ueCommand.value, array, (short)UEDeviceCommand.mReturnCommandsMap.get(ueCommand.value, UECommand.Acknowledge.getCode()), ueCommand.name());
    }
    
    protected UEDeviceCommand(final short n, final byte[] array, final short n2, final String s) {
        super(n, array, n2, s);
    }
    
    public static UEDeviceCommand newCommand(final UECommand ueCommand) {
        return new UEDeviceCommand(ueCommand, null);
    }
    
    public static UEDeviceCommand newCommand(final UECommand ueCommand, final byte[] array) {
        return new UEDeviceCommand(ueCommand, array);
    }
    
    public enum UECommand
    {
        Acknowledge(0), 
        AddReceiverToBroadcast(1047), 
        AdjustVolume(443), 
        AnnounceBatteryLevel(363), 
        BLEAvailableNotification(1075), 
        BeginDeviceDiscovery(361), 
        BeginDeviceInquery(360), 
        BlockPartyNotification(1040), 
        BroadcastStatusNotification(1046), 
        CheckPartitionState(517), 
        FixedReceiverAttributesNotification(1055), 
        GetOneReceiverAttributeNotification(1057), 
        GetPartitionMountState(514), 
        GetSonificationFileSize(525), 
        GetVoiceCapabilities(1076), 
        GetVoiceControlFlag(1070), 
        KickBlockPartyMember(1031), 
        MasterRemoteOff(438), 
        MountPartition(513), 
        PartitionSerialFlash(512), 
        PlaySound(364), 
        Query32Volume(398), 
        QueryAUXLevelInDB(436), 
        QueryAlarm(382), 
        QueryAudioRouting(370), 
        QueryBLEState(439), 
        QueryBlockPartyCurrentStreamingDeviceMAC(1032), 
        QueryBlockPartyInfo(1027), 
        QueryBlockPartyState(1025), 
        QueryBluetoothName(365), 
        QueryBroadcastMode(1044), 
        QueryChargerRegister(385), 
        QueryChargingInfo(373), 
        QueryConnectedDeviceNameRequest(421), 
        QueryCustomState(408), 
        QueryDeviceBluetoothAddress(430), 
        QueryDeviceId(6), 
        QueryDeviceStatus(358), 
        QueryEQSetting(375), 
        QueryEQTable(392), 
        QueryEnableNotifications(520), 
        QueryFeatures(414), 
        QueryFirmwareVersion(3), 
        QueryGestureEnable(452), 
        QueryHardwareId(368), 
        QueryHardwareRevision(389), 
        QueryLanguageSetting(352), 
        QueryNumberOfOtherConnectedDevices(378), 
        QueryOTAState(424), 
        QueryPartitionFiveInfo(522), 
        QueryPlaybackMetadata(416), 
        QueryProtocolVersion(1), 
        QueryReceiverFixedAttributes(1054), 
        QueryReceiverOneAttribute(1056), 
        QueryReceiverVariableAttributes(1052), 
        QuerySerialNumber(380), 
        QuerySlaveBatteryLevel(426), 
        QuerySlaveVolume(444), 
        QuerySleepTimer(418), 
        QuerySoCVariables(387), 
        QuerySonfication(355), 
        QuerySourceBluetoothAddress(428), 
        QuerySupportedLanguage(406), 
        QueryTWSBalance(411), 
        QueryTWSSavePairFlag(403), 
        QueryTWSSlaveBluetoothAddress(432), 
        QueryVolume(401), 
        QueryXUPPromiscuousMode(1066), 
        ReceiverAddedToBroadcastNotification(1048), 
        ReceiverRemovedFromBroadcastNotification(1050), 
        RemoveReceiverFromBroadcast(1049), 
        Return32Volume(399), 
        ReturnAUXLevelInDB(437), 
        ReturnAlarm(383), 
        ReturnAudioRouting(371), 
        ReturnBTLEState(440), 
        ReturnBlockPartyCurrentStreamingDeviceMAC(1033), 
        ReturnBlockPartyInfo(1028), 
        ReturnBlockPartyState(1026), 
        ReturnBluetoothName(366), 
        ReturnBroadcastMode(1045), 
        ReturnChargerRegister(386), 
        ReturnChargingInfo(374), 
        ReturnCheckPartitionState(518), 
        ReturnConnectedDeviceName(422), 
        ReturnCustomState(409), 
        ReturnDeviceBluetoothAddress(431), 
        ReturnDeviceId(7), 
        ReturnDeviceStatus(359), 
        ReturnEQSetting(376), 
        ReturnEQTable(393), 
        ReturnEnableNotifications(521), 
        ReturnFeatures(415), 
        ReturnFirmwareVersion(4), 
        ReturnGestureEnable(453), 
        ReturnHardwareId(369), 
        ReturnHardwareRevision(390), 
        ReturnLanguageSetting(353), 
        ReturnNumberOfOtherConnectedDevices(379), 
        ReturnOTAState(425), 
        ReturnPartitionFiveInfo(523), 
        ReturnPartitionMountState(515), 
        ReturnPlaybackMetadata(417), 
        ReturnProtocolVersion(2), 
        ReturnSerialNumber(381), 
        ReturnSlaveBatteryLevel(427), 
        ReturnSlaveVolume(445), 
        ReturnSleepTimer(419), 
        ReturnSoCVariables(388), 
        ReturnSonfication(356), 
        ReturnSonificationFileSize(526), 
        ReturnSourceBluetoothAddress(429), 
        ReturnSupportedLanguage(407), 
        ReturnTWSBalance(412), 
        ReturnTWSSavePairFlag(404), 
        ReturnTWSSlaveBluetoothAddress(433), 
        ReturnVoiceCapabilities(1077), 
        ReturnVoiceControlFlag(1071), 
        ReturnVolume(402), 
        ReturnXUPPromiscuousMode(1067), 
        SendAVRCPCommand(391), 
        Set32Volume(397), 
        SetAlarm(384), 
        SetAudioRouting(372), 
        SetBTLEState(441), 
        SetBlockPartyState(1024), 
        SetBluetoothName(367), 
        SetBroadcastMode(1043), 
        SetBroadcastSyncVolume(1051), 
        SetCustomState(410), 
        SetEQSetting(377), 
        SetEQTable(394), 
        SetEnableNotifications(519), 
        SetGestureEnable(454), 
        SetLanguage(354), 
        SetOTAState(423), 
        SetOneReceiverAttributeNotification(1059), 
        SetPartitionMountState(516), 
        SetReceiverIdentificationSignal(1060), 
        SetReceiverIdentificationSignalNotification(1061), 
        SetReceiverOneAttribute(1058), 
        SetSleepTimer(420), 
        SetSonfication(357), 
        SetTWSBalance(413), 
        SetTWSSavePairFlag(405), 
        SetVoiceControlFlag(1072), 
        SetVoiceLEDState(1069), 
        SetVolume(400), 
        SetXUPPromiscuousMode(1062), 
        SlaveRemoteOff(434), 
        StopRestreaming(362), 
        TrackLengthInfoNotification(1042), 
        VariableReceiverAttributesNotification(1053), 
        VoiceNotification(1068);
        
        public final int value;
        
        private UECommand(final int value) {
            this.value = value;
        }
        
        public int getCode() {
            return this.value;
        }
        
        public byte getLeastSignificantByte() {
            return (byte)(this.value & 0xFF);
        }
        
        public byte getMostSignificantByte() {
            return (byte)(this.value >> 8 & 0xFF);
        }
    }
}
