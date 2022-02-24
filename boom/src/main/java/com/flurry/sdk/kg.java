// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.text.TextUtils;

public abstract class kg
{
    protected String g;
    
    public kg(final String g) {
        this.g = "com.flurry.android.sdk.ReplaceMeWithAProperEventName";
        if (TextUtils.isEmpty((CharSequence)g)) {
            throw new IllegalArgumentException("Event must have a name!");
        }
        this.g = g;
    }
    
    public final String a() {
        return this.g;
    }
    
    public final void b() {
        ki.a().a(this);
    }
}
