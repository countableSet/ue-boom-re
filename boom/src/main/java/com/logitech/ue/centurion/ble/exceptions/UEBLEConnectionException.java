// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.ble.exceptions;

import com.logitech.ue.centurion.exceptions.UEConnectionException;

public class UEBLEConnectionException extends UEConnectionException
{
    public static final String MESSAGE_CAN_FIND_GATT_SERVICE = "Can't find Gatt service";
    public static final String MESSAGE_FAILED_TO_CONNECT_GATT = "Gatt connection failed";
    public static final String MESSAGE_NO_BLE_DEVICE_IS_CURRENTLY_CONNECTED = "No BLE device is currently connected";
    
    public UEBLEConnectionException(final String s) {
        super(s);
    }
}
