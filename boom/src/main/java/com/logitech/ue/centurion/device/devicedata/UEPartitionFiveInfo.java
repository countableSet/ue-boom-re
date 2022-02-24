// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import com.logitech.ue.centurion.utils.UEUtils;

public class UEPartitionFiveInfo
{
    public static final int PARTITION_TYPE_RAW_SERIAL = 3;
    public static final int PARTITION_TYPE_READ_ONLY = 1;
    private long sizeInWords;
    private int type;
    
    public UEPartitionFiveInfo(final byte[] array) {
        this.type = array[3];
        this.sizeInWords = UEUtils.intToUnsignedLong(UEUtils.byteArrayToInt(array, 4));
    }
    
    public long getSize() {
        return this.sizeInWords;
    }
    
    public int getType() {
        return this.type;
    }
}
