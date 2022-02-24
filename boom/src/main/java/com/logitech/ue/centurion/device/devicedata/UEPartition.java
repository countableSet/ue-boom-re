// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import com.logitech.ue.centurion.utils.UEUtils;

public class UEPartition
{
    public static final int TYPE_RAW_SERIAL_FOR_OTA = 3;
    public static final int TYPE_READ_ONLY_FOR_CUSTOMIZE = 2;
    public static final int TYPE_READ_ONLY_FOR_LANGUAGE = 1;
    public static final int TYPE_READ_ONLY_FOR_MAP = 4;
    private long partitionSize;
    private int type;
    
    public UEPartition(final byte[] array) {
        this.type = array[3];
        this.partitionSize = UEUtils.intToUnsignedLong(UEUtils.byteArrayToInt(array, 0));
    }
    
    public long getSize() {
        return this.partitionSize;
    }
    
    public int getType() {
        return this.type;
    }
}
