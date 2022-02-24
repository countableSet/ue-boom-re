// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.exceptions;

import com.logitech.ue.centurion.utils.UEUtils;

public class UECommandExecutionErrorException extends UEOperationException
{
    private static final long serialVersionUID = -7298318960136215652L;
    private final byte[] mData;
    private final byte mErrorCode;
    
    public UECommandExecutionErrorException(final byte b, final byte[] mData) {
        super("Command " + UEUtils.byteArrayToFancyHexString(mData) + " received unsupported message from speaker.");
        this.mData = mData;
        this.mErrorCode = b;
    }
    
    public byte getErrorCode() {
        return this.mErrorCode;
    }
    
    public byte[] getTransferetData() {
        return this.mData;
    }
}
