// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEAckResponse
{
    ARGUMENT_OUT_OF_RANGE(3), 
    BUSY(9), 
    COMMAND_FAIL(8), 
    COMMAND_WRONG_LENGTH(2), 
    MESSAGE_HAS_TIMED_OUT(4), 
    NO_PEER(5), 
    OK(0), 
    PEER_NOT_CONNECTED(6), 
    UNKNOWN(-1), 
    UNRECOGNIZED_COMMAND(1), 
    VALUE_NOT_AVAILABLE(7);
    
    static final SparseArray<UEAckResponse> valueMap;
    final byte value;
    
    static {
        int i = 0;
        valueMap = new SparseArray();
        for (UEAckResponse[] values = values(); i < values.length; ++i) {
            final UEAckResponse ueAckResponse = values[i];
            if (ueAckResponse != UEAckResponse.UNKNOWN) {
                UEAckResponse.valueMap.put((int)ueAckResponse.getValue(), (Object)ueAckResponse);
            }
        }
    }
    
    private UEAckResponse(final int n) {
        this.value = (byte)n;
    }
    
    public static UEAckResponse getAckResponse(final byte b) {
        return (UEAckResponse)UEAckResponse.valueMap.get((int)b, (Object)UEAckResponse.UNKNOWN);
    }
    
    public static byte getValue(final UEAckResponse ueAckResponse) {
        return ueAckResponse.value;
    }
    
    public byte getValue() {
        return this.value;
    }
}
