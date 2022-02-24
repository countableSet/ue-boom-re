// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.util.SparseArray;

public class UECustomSonificationHelper
{
    public static final int COLOR_CODE_ALL_BLACKS = 23;
    public static final int COLOR_CODE_RED_BULL = 20;
    public static final int COLOR_CODE_RED_BULL_INFINITY = 24;
    public static final int COLOR_CODE_RED_BULL_TORO_ROSSO = 7;
    public static final int COLOR_CODE_SKRILLEX = 40;
    public static final SparseArray<Integer> mCustomDeviceMap;
    
    static {
        (mCustomDeviceMap = new SparseArray()).put(7, (Object)2131099749);
        UECustomSonificationHelper.mCustomDeviceMap.put(20, (Object)2131099749);
        UECustomSonificationHelper.mCustomDeviceMap.put(23, (Object)2131099748);
        UECustomSonificationHelper.mCustomDeviceMap.put(24, (Object)2131099749);
        UECustomSonificationHelper.mCustomDeviceMap.put(40, (Object)2131099750);
    }
    
    public static int getCustomSoundResourceForDevice(final int n) {
        return (int)UECustomSonificationHelper.mCustomDeviceMap.get(n, (Object)0);
    }
    
    public static boolean isDeviceCustom(final int n) {
        return (int)UECustomSonificationHelper.mCustomDeviceMap.get(n, (Object)0) != 0;
    }
}
