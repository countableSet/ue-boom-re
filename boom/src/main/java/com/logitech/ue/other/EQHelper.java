// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.centurion.device.devicedata.UEEQSetting;
import android.content.Context;

public class EQHelper
{
    public static String getEQName(final Context context, final UEEQSetting ueeqSetting) {
        String s;
        if (ueeqSetting == UEEQSetting.OUT_LOUD) {
            s = context.getResources().getString(2131165270);
        }
        else if (ueeqSetting == UEEQSetting.VOCAL) {
            s = context.getResources().getString(2131165271);
        }
        else if (ueeqSetting == UEEQSetting.INTIMATE) {
            s = context.getResources().getString(2131165267);
        }
        else if (ueeqSetting == UEEQSetting.BASS_BOOST) {
            s = context.getResources().getString(2131165263);
        }
        else if (ueeqSetting == UEEQSetting.CUSTOM) {
            s = context.getResources().getString(2131165268);
        }
        else {
            s = null;
        }
        return s;
    }
    
    public static String getEQTechnicalName(final Context context, final UEEQSetting ueeqSetting) {
        String s;
        if (ueeqSetting == UEEQSetting.OUT_LOUD) {
            s = context.getResources().getString(2131165483);
        }
        else if (ueeqSetting == UEEQSetting.VOCAL) {
            s = context.getResources().getString(2131165484);
        }
        else if (ueeqSetting == UEEQSetting.INTIMATE) {
            s = context.getResources().getString(2131165482);
        }
        else if (ueeqSetting == UEEQSetting.BASS_BOOST) {
            s = context.getResources().getString(2131165480);
        }
        else if (ueeqSetting == UEEQSetting.CUSTOM) {
            s = context.getResources().getString(2131165481);
        }
        else {
            s = null;
        }
        return s;
    }
}
