// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEReceiverAttribute
{
    AutoConnect(42), 
    Battery(26), 
    BroadcasterAddress(38), 
    Color(18), 
    FWversion(20), 
    Flags(28), 
    HWtype(32), 
    HWversion(34), 
    Identify(40), 
    Name(24), 
    Unknown(-1), 
    Volume(30), 
    XUPversion(36);
    
    static final SparseArray<UEReceiverAttribute> attributeNumbersMap;
    final int mAttributeCode;
    
    static {
        (attributeNumbersMap = new SparseArray(13)).put(18, (Object)UEReceiverAttribute.Color);
        UEReceiverAttribute.attributeNumbersMap.put(28, (Object)UEReceiverAttribute.Flags);
        UEReceiverAttribute.attributeNumbersMap.put(30, (Object)UEReceiverAttribute.Volume);
        UEReceiverAttribute.attributeNumbersMap.put(24, (Object)UEReceiverAttribute.Name);
        UEReceiverAttribute.attributeNumbersMap.put(26, (Object)UEReceiverAttribute.Battery);
        UEReceiverAttribute.attributeNumbersMap.put(20, (Object)UEReceiverAttribute.FWversion);
        UEReceiverAttribute.attributeNumbersMap.put(32, (Object)UEReceiverAttribute.HWtype);
        UEReceiverAttribute.attributeNumbersMap.put(34, (Object)UEReceiverAttribute.HWversion);
        UEReceiverAttribute.attributeNumbersMap.put(36, (Object)UEReceiverAttribute.XUPversion);
        UEReceiverAttribute.attributeNumbersMap.put(38, (Object)UEReceiverAttribute.BroadcasterAddress);
        UEReceiverAttribute.attributeNumbersMap.put(40, (Object)UEReceiverAttribute.Identify);
        UEReceiverAttribute.attributeNumbersMap.put(42, (Object)UEReceiverAttribute.AutoConnect);
    }
    
    private UEReceiverAttribute(final int mAttributeCode) {
        this.mAttributeCode = mAttributeCode;
    }
    
    public static int getAttributeCode(final UEReceiverAttribute ueReceiverAttribute) {
        return ueReceiverAttribute.mAttributeCode;
    }
    
    public static UEReceiverAttribute getReceiverAttribute(final int n) {
        return (UEReceiverAttribute)UEReceiverAttribute.attributeNumbersMap.get(n, (Object)UEReceiverAttribute.Unknown);
    }
    
    public int getAttributeCode() {
        return this.mAttributeCode;
    }
}
