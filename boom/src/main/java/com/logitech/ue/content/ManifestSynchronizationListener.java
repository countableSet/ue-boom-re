// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.content;

public interface ManifestSynchronizationListener
{
    void onSyncFail(final Exception p0);
    
    void onSyncSuccess(final String p0);
}
