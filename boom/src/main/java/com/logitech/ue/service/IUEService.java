// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.service;

import android.os.Bundle;
import android.content.Context;

public interface IUEService
{
    String getServiceName();
    
    void initService(final Context p0, final Bundle p1);
    
    void syncService(final ServiceInfo p0);
}
