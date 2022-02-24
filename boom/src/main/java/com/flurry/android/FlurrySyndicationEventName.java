// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.android;

public enum FlurrySyndicationEventName
{
    FAST_REBLOG("FastReblog"), 
    LIKE("Like"), 
    REBLOG("Reblog"), 
    SOURCE_LINK("SourceClick");
    
    private String a;
    
    private FlurrySyndicationEventName(final String a) {
        this.a = a;
    }
    
    @Override
    public final String toString() {
        return this.a;
    }
}
