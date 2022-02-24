// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.download;

public interface LoadDataTaskListener
{
    void onBeginTask(final LoadDataTask p0);
    
    void onEndTask(final LoadDataTask p0);
    
    void onError(final LoadDataTask p0, final Throwable p1);
}
