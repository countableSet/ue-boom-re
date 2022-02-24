// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

public interface OnCheckUpdateInstructionListener
{
    void onError(final Exception p0);
    
    void onFail(final UpdateRequestError p0);
    
    void onSuccess(final UpdateInstruction p0);
}
