// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.concurrent.ThreadFactory;

public final class lr implements ThreadFactory
{
    private final ThreadGroup a;
    private final int b;
    
    public lr(final String name) {
        this.a = new ThreadGroup(name);
        this.b = 1;
    }
    
    @Override
    public final Thread newThread(final Runnable target) {
        final Thread thread = new Thread(this.a, target);
        thread.setName(this.a.getName() + ":" + thread.getId());
        thread.setPriority(this.b);
        return thread;
    }
}
