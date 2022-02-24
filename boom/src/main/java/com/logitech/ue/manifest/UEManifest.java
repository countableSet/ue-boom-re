// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import android.text.TextUtils;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.Attribute;
import java.util.List;
import org.simpleframework.xml.ElementList;
import java.util.ArrayList;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class UEManifest
{
    @ElementList(name = "appInfo")
    ArrayList<AppInfo> appInfoMap;
    @ElementList(name = "deviceList")
    ArrayList<DeviceInfo> devices;
    @ElementList(name = "firmwareList")
    List<DeviceFirmwareInfo> knownDevices;
    long updateTime;
    @Attribute
    String version;
    
    public UEManifest() {
        this.version = "";
        this.knownDevices = new ArrayList<DeviceFirmwareInfo>();
        this.devices = new ArrayList<DeviceInfo>();
        this.appInfoMap = new ArrayList<AppInfo>();
    }
    
    public static UEManifest readFromXML(final String s) throws Exception {
        return new Persister(new AnnotationStrategy()).read((Class<? extends UEManifest>)UEManifest.class, s);
    }
    
    public AppInfo getAppInfo(final String anObject) {
        for (int i = 0; i < this.appInfoMap.size(); ++i) {
            if (this.appInfoMap.get(i).name.equals(anObject)) {
                return this.appInfoMap.get(i);
            }
        }
        return null;
    }
    
    public DeviceInfo getDeviceInfo(final String s) {
        for (final DeviceInfo deviceInfo : this.devices) {
            if (TextUtils.equals((CharSequence)deviceInfo.hexCode.toLowerCase(), (CharSequence)s.toLowerCase())) {
                return deviceInfo;
            }
        }
        return null;
    }
    
    public List<DeviceInfo> getDevicesInfo() {
        return this.devices;
    }
    
    public DeviceFirmwareInfo getLatestFirmwareInfo(final String s, final String s2) {
        for (final DeviceFirmwareInfo deviceFirmwareInfo : this.knownDevices) {
            if (deviceFirmwareInfo.latestFirmware != null && TextUtils.equals((CharSequence)s, (CharSequence)deviceFirmwareInfo.device) && TextUtils.equals((CharSequence)s2, (CharSequence)deviceFirmwareInfo.hardwareRev)) {
                return deviceFirmwareInfo;
            }
        }
        return null;
    }
    
    public DeviceFirmwareInfo getNewFirmwareInfoForCurrentDevice(final String s, final String s2, final String s3) {
        DeviceFirmwareInfo deviceFirmwareInfo = null;
        for (final DeviceFirmwareInfo deviceFirmwareInfo2 : this.knownDevices) {
            if (TextUtils.equals((CharSequence)s, (CharSequence)deviceFirmwareInfo2.device) && TextUtils.equals((CharSequence)s2, (CharSequence)deviceFirmwareInfo2.hardwareRev) && TextUtils.equals((CharSequence)s3, (CharSequence)deviceFirmwareInfo2.currentFirmware)) {
                deviceFirmwareInfo = deviceFirmwareInfo2;
            }
        }
        DeviceFirmwareInfo deviceFirmwareInfo3;
        if ((deviceFirmwareInfo3 = deviceFirmwareInfo) == null) {
            final Iterator<DeviceFirmwareInfo> iterator2 = this.knownDevices.iterator();
            while (true) {
                deviceFirmwareInfo3 = deviceFirmwareInfo;
                if (!iterator2.hasNext()) {
                    break;
                }
                final DeviceFirmwareInfo deviceFirmwareInfo4 = iterator2.next();
                if (!TextUtils.equals((CharSequence)s, (CharSequence)deviceFirmwareInfo4.device) || !TextUtils.equals((CharSequence)s2, (CharSequence)deviceFirmwareInfo4.hardwareRev) || !TextUtils.equals((CharSequence)"default", (CharSequence)deviceFirmwareInfo4.currentFirmware)) {
                    continue;
                }
                deviceFirmwareInfo = deviceFirmwareInfo4;
            }
        }
        return deviceFirmwareInfo3;
    }
    
    public long getUpdateTime() {
        return this.updateTime;
    }
    
    public boolean isFresh() {
        return TimeUnit.MILLISECONDS.toDays(new Date().getTime() - this.updateTime) == 0L;
    }
    
    public void setUpdateTime(final long updateTime) {
        this.updateTime = updateTime;
    }
    
    public String toXMLString() throws Exception {
        final Persister persister = new Persister(new AnnotationStrategy());
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        persister.write(this, byteArrayOutputStream);
        return new String(byteArrayOutputStream.toByteArray());
    }
}
