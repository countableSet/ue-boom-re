// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.android;

import com.flurry.sdk.hs;
import com.flurry.sdk.ly;
import com.flurry.sdk.km;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public final class FlurryInstallReceiver extends BroadcastReceiver
{
    static final String a;
    
    static {
        a = FlurryInstallReceiver.class.getSimpleName();
    }
    
    public final void onReceive(final Context context, final Intent intent) {
        km.a(4, FlurryInstallReceiver.a, "Received an Install nofication of " + intent.getAction());
        final String string = intent.getExtras().getString("referrer");
        km.a(4, FlurryInstallReceiver.a, "Received an Install referrer of " + string);
        if (string == null || !"com.android.vending.INSTALL_REFERRER".equals(intent.getAction())) {
            km.a(5, FlurryInstallReceiver.a, "referrer is null");
        }
        else {
            String d = string;
            if (!string.contains("=")) {
                km.a(4, FlurryInstallReceiver.a, "referrer is before decoding: " + string);
                d = ly.d(string);
                km.a(4, FlurryInstallReceiver.a, "referrer is: " + d);
            }
            new hs(context).a(d);
        }
    }
}
