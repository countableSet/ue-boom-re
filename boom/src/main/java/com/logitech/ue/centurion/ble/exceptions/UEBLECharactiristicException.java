// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.ble.exceptions;

import com.logitech.ue.centurion.exceptions.UEOperationException;

public class UEBLECharactiristicException extends UEOperationException
{
    public static final String MESSAGE_CHARACTERISTIC_DOES_NOT_EXIST = "Characteristic(%s) doesn't exist";
    
    public UEBLECharactiristicException(final String s) {
        super(String.format("Characteristic(%s) doesn't exist", s));
    }
}
