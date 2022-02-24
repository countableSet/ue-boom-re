// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.os.SystemClock;

public class StopWatch
{
    private long mStart;
    
    public StopWatch() {
        this.reset();
    }
    
    public long elapsedTime() {
        return SystemClock.elapsedRealtime() - this.mStart;
    }
    
    public void reset() {
        this.mStart = SystemClock.elapsedRealtime();
    }
}
