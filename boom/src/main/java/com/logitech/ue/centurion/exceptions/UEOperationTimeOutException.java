// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

import com.logitech.ue.centurion.utils.UEUtils;

public class UEOperationTimeOutException extends UEOperationException
{
    private static final long serialVersionUID = -7298318960136215652L;
    private final byte[] mData;
    
    public UEOperationTimeOutException(final byte[] mData) {
        super("Command " + UEUtils.byteArrayToFancyHexString(mData) + " time out.");
        this.mData = mData;
    }
    
    public byte[] getTransferetData() {
        return this.mData;
    }
}
