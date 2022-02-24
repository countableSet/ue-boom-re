// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import java.util.HashMap;
import android.util.SparseArray;

public enum UELanguage
{
    CHINESE(4), 
    DUTCH(8), 
    ENGLISH(0), 
    FRENCH(1), 
    GERMAN(3), 
    ITALIAN(6), 
    JAPANESE(7), 
    RUSSIAN(5), 
    SPANISH(2);
    
    private static final SparseArray<UELanguage> langMap;
    private static final HashMap<String, UELanguage> langNameMap;
    private final int mCode;
    
    static {
        langMap = new SparseArray(9);
        langNameMap = new HashMap<String, UELanguage>(9);
        UELanguage.langMap.put(UELanguage.ENGLISH.getCode(), (Object)UELanguage.ENGLISH);
        UELanguage.langMap.put(UELanguage.FRENCH.getCode(), (Object)UELanguage.FRENCH);
        UELanguage.langMap.put(UELanguage.SPANISH.getCode(), (Object)UELanguage.SPANISH);
        UELanguage.langMap.put(UELanguage.GERMAN.getCode(), (Object)UELanguage.GERMAN);
        UELanguage.langMap.put(UELanguage.CHINESE.getCode(), (Object)UELanguage.CHINESE);
        UELanguage.langMap.put(UELanguage.RUSSIAN.getCode(), (Object)UELanguage.RUSSIAN);
        UELanguage.langMap.put(UELanguage.ITALIAN.getCode(), (Object)UELanguage.ITALIAN);
        UELanguage.langMap.put(UELanguage.JAPANESE.getCode(), (Object)UELanguage.JAPANESE);
        UELanguage.langMap.put(UELanguage.DUTCH.getCode(), (Object)UELanguage.DUTCH);
        UELanguage.langNameMap.put("en", UELanguage.ENGLISH);
        UELanguage.langNameMap.put("fr", UELanguage.FRENCH);
        UELanguage.langNameMap.put("es", UELanguage.SPANISH);
        UELanguage.langNameMap.put("de", UELanguage.GERMAN);
        UELanguage.langNameMap.put("zh", UELanguage.CHINESE);
        UELanguage.langNameMap.put("ru", UELanguage.RUSSIAN);
        UELanguage.langNameMap.put("it", UELanguage.ITALIAN);
        UELanguage.langNameMap.put("ja", UELanguage.JAPANESE);
        UELanguage.langNameMap.put("da", UELanguage.DUTCH);
    }
    
    private UELanguage(final int mCode) {
        this.mCode = mCode;
    }
    
    public static UELanguage getLanguage(final int n) {
        return (UELanguage)UELanguage.langMap.get(n);
    }
    
    public static UELanguage getLanguage(final String key) {
        return UELanguage.langNameMap.get(key);
    }
    
    public int getCode() {
        return this.mCode;
    }
}
