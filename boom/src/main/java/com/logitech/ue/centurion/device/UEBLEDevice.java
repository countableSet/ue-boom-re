// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import com.logitech.ue.centurion.connection.UEBLEConnector;
import com.logitech.ue.centurion.device.devicedata.UEAlarmInfo;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.connection.IUEConnector;
import com.logitech.ue.centurion.utils.MAC;

public class UEBLEDevice extends UEGenericDevice
{
    private static final String TAG;
    
    static {
        TAG = UEBLEDevice.class.getSimpleName();
    }
    
    public UEBLEDevice(final MAC mac, final IUEConnector iueConnector) {
        super(mac, iueConnector);
    }
    
    @Override
    public void clearAlarm(final MAC mac) throws UEOperationException, UEConnectionException {
        this.setAlarm(0L, mac);
    }
    
    @Override
    public UEAlarmInfo getAlarmInfo() throws UEOperationException, UEConnectionException {
        return UEAlarmInfo.buildFromBLEMessage(this.getBLEConnector().readCharacteristics("CHAR_ALARM_GET"));
    }
    
    @Override
    public int getAlarmVolume() throws UEOperationException, UEConnectionException {
        return this.getBLEConnector().readCharacteristics("CHAR_ALARM_GET")[14];
    }
    
    protected UEBLEConnector getBLEConnector() {
        return (UEBLEConnector)this.getConnector();
    }
    
    @Override
    public int getBatteryLevel() throws UEOperationException, UEConnectionException {
        return this.getBLEConnector().readCharacteristics("CHAR_BATTERY_LEVEL")[0];
    }
    
    @Override
    public String getBluetoothName() throws UEOperationException, UEConnectionException {
        final byte[] characteristics = this.getBLEConnector().readCharacteristics("CHAR_DEVICE_NAME");
        try {
            return new String(characteristics, 0, characteristics.length - 1, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    @Override
    public String getFirmwareVersion() throws UEOperationException, UEConnectionException {
        final byte[] characteristics = this.getBLEConnector().readCharacteristics("CHAR_FW_VERSION");
        return String.format("%d.%d.%d", characteristics[0] & 0xFF, characteristics[1] & 0xFF, characteristics[2] & 0xFF);
    }
    
    @Override
    public boolean getRepeatAlarm() throws UEOperationException, UEConnectionException {
        return this.getBLEConnector().readCharacteristics("CHAR_ALARM_GET")[8] != 0;
    }
    
    @Override
    public String getSerialNumber() throws UEOperationException, UEConnectionException {
        final byte[] characteristics = this.getBLEConnector().readCharacteristics("CHAR_SN");
        try {
            return new String(characteristics, 0, characteristics.length - 1, "US-ASCII");
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean isAlarmSupported() throws UEOperationException, UEConnectionException {
        return true;
    }
    
    @Override
    public void setAlarm(final long n, final MAC mac) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[12];
        System.arraycopy(mac.getBytes(), 0, array, 0, 6);
        array[6] = 1;
        array[7] = (byte)(n >> 24 & 0xFFL);
        array[8] = (byte)(n >> 16 & 0xFFL);
        array[9] = (byte)(n >> 8 & 0xFFL);
        array[10] = (byte)(n & 0xFFL);
        array[11] = 0;
        this.getBLEConnector().writeCharacteristics("CHAR_ALARM_SET", array);
    }
    
    @Override
    public void setAlarmVolume(final int n, final MAC mac) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[8];
        System.arraycopy(mac.getBytes(), 0, array, 0, 6);
        array[6] = 10;
        array[7] = (byte)n;
        this.getBLEConnector().writeCharacteristics("CHAR_ALARM_SET", array);
    }
    
    @Override
    public void setPowerOn(final MAC mac) throws UEOperationException, UEConnectionException {
        Log.d(UEBLEDevice.TAG, "Power on master");
        final byte[] array = new byte[7];
        System.arraycopy(mac.getBytes(), 0, array, 0, 6);
        array[6] = 1;
        this.getBLEConnector().writeCharacteristics("CHAR_POWER_ON", array);
    }
    
    @Override
    public void setRepeatAlarm(final boolean b, final MAC mac) throws UEOperationException, UEConnectionException {
        final byte[] array = new byte[9];
        System.arraycopy(mac.getBytes(), 0, array, 0, 6);
        array[6] = 7;
        int n;
        if (b) {
            n = 127;
        }
        else {
            n = 0;
        }
        array[7] = (byte)n;
        array[8] = 0;
        this.getBLEConnector().writeCharacteristics("CHAR_ALARM_SET", array);
    }
}
