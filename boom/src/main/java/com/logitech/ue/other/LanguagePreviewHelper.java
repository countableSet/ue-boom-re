// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.util.Log;
import android.util.SparseIntArray;
import com.logitech.ue.centurion.device.devicedata.UELanguage;
import java.util.HashMap;

public class LanguagePreviewHelper
{
    private static final String TAG;
    private static final HashMap<UELanguage, SparseIntArray> languagePreviewFilesMap;
    
    static {
        TAG = LanguagePreviewHelper.class.getSimpleName();
        languagePreviewFilesMap = new HashMap<UELanguage, SparseIntArray>();
        initLanguage(UELanguage.ENGLISH, 2131099656, 2131099658, 2131099648, 2131099649, 2131099650, 2131099651, 2131099652, 2131099653, 2131099654, 2131099655, 2131099657);
        initLanguage(UELanguage.FRENCH, 2131099667, 2131099669, 2131099659, 2131099660, 2131099661, 2131099662, 2131099663, 2131099664, 2131099665, 2131099666, 2131099668);
        initLanguage(UELanguage.SPANISH, 2131099678, 2131099680, 2131099670, 2131099671, 2131099672, 2131099673, 2131099674, 2131099675, 2131099676, 2131099677, 2131099679);
        initLanguage(UELanguage.GERMAN, 2131099689, 2131099691, 2131099681, 2131099682, 2131099683, 2131099684, 2131099685, 2131099686, 2131099687, 2131099688, 2131099690);
        initLanguage(UELanguage.CHINESE, 2131099700, 2131099702, 2131099692, 2131099693, 2131099694, 2131099695, 2131099696, 2131099697, 2131099698, 2131099699, 2131099701);
        initLanguage(UELanguage.RUSSIAN, 2131099711, 2131099713, 2131099703, 2131099704, 2131099705, 2131099706, 2131099707, 2131099708, 2131099709, 2131099710, 2131099712);
        initLanguage(UELanguage.ITALIAN, 2131099722, 2131099724, 2131099714, 2131099715, 2131099716, 2131099717, 2131099718, 2131099719, 2131099720, 2131099721, 2131099723);
        initLanguage(UELanguage.JAPANESE, 2131099733, 2131099735, 2131099725, 2131099726, 2131099727, 2131099728, 2131099729, 2131099730, 2131099731, 2131099732, 2131099734);
        initLanguage(UELanguage.DUTCH, 2131099744, 2131099746, 2131099736, 2131099737, 2131099738, 2131099739, 2131099740, 2131099741, 2131099742, 2131099743, 2131099745);
    }
    
    public static int fileWithLanguageAndLevel(final UELanguage ueLanguage, final int i) {
        final int value = LanguagePreviewHelper.languagePreviewFilesMap.get(ueLanguage).get(keyFromBatteryLevel(i));
        Log.d(LanguagePreviewHelper.TAG, "Resource ID " + value + " for level " + i + " for language " + ueLanguage);
        return value;
    }
    
    private static void initLanguage(final UELanguage key, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final int n10, final int n11) {
        final SparseIntArray value = new SparseIntArray();
        LanguagePreviewHelper.languagePreviewFilesMap.put(key, value);
        value.put(0, n);
        value.put(10, n2);
        value.put(20, n3);
        value.put(30, n4);
        value.put(40, n5);
        value.put(50, n6);
        value.put(60, n7);
        value.put(70, n8);
        value.put(80, n9);
        value.put(90, n10);
        value.put(100, n11);
    }
    
    private static int keyFromBatteryLevel(int n) {
        if (n > 95) {
            n = 100;
        }
        else if (n > 15 && n < 20) {
            n = 10;
        }
        else if (n <= 15) {
            n = 0;
        }
        else {
            n = n / 10 * 10;
        }
        return n;
    }
}
