// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

public class UEBroadcastConfiguration
{
    private UEBroadcastAudioOptions mAudioSetting;
    private UEBroadcastState mBroadcastState;
    
    public UEBroadcastConfiguration() {
    }
    
    public UEBroadcastConfiguration(final UEBroadcastState mBroadcastState, final UEBroadcastAudioOptions mAudioSetting) {
        this.mBroadcastState = mBroadcastState;
        this.mAudioSetting = mAudioSetting;
    }
    
    public UEBroadcastAudioOptions getAudioSetting() {
        return this.mAudioSetting;
    }
    
    public UEBroadcastState getBroadcastState() {
        return this.mBroadcastState;
    }
    
    public void setAudioSetttings(final UEBroadcastAudioOptions mAudioSetting) {
        this.mAudioSetting = mAudioSetting;
    }
    
    public void setBroadcastState(final UEBroadcastState mBroadcastState) {
        this.mBroadcastState = mBroadcastState;
    }
}
