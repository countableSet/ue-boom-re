// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.centurion.device.devicedata.UEBlockPartyState;

public class BlockPartyUtils
{
    public static void broadcastBlackPartyStateChanged(final UEBlockPartyState ueBlockPartyState, final String s, final Context context) {
        final Intent intent = new Intent("com.logitech.ue.fragments.PARTY_MODE_STATE_CHANGED");
        intent.putExtra("state", ueBlockPartyState.getCode());
        intent.putExtra("source", s);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
