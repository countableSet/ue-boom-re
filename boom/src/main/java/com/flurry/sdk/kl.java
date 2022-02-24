// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Future;
import java.util.HashMap;

public class kl<T extends mb>
{
    private static final String a;
    private final kd<Object, T> b;
    private final HashMap<T, Object> c;
    private final HashMap<T, Future<?>> d;
    private final ThreadPoolExecutor e;
    
    static {
        a = kl.class.getSimpleName();
    }
    
    public kl(final String s, final TimeUnit timeUnit, final BlockingQueue<Runnable> blockingQueue) {
        this.b = new kd<Object, T>();
        this.c = new HashMap<T, Object>();
        this.d = new HashMap<T, Future<?>>();
        (this.e = new ThreadPoolExecutor(timeUnit, blockingQueue) {
            @Override
            protected final void afterExecute(final Runnable r, final Throwable t) {
                super.afterExecute(r, t);
                final mb a = kl.a(r);
                if (a != null) {
                    synchronized (kl.this.d) {
                        kl.this.d.remove(a);
                        // monitorexit(kl.a(this.a))
                        kl.this.b(a);
                        new ma() {
                            @Override
                            public final void a() {
                            }
                        }.run();
                    }
                }
            }
            
            @Override
            protected final void beforeExecute(final Thread t, final Runnable r) {
                super.beforeExecute(t, r);
                final mb a = kl.a(r);
                if (a != null) {
                    new ma() {
                        @Override
                        public final void a() {
                        }
                    }.run();
                }
            }
            
            @Override
            protected final <V> RunnableFuture<V> newTaskFor(final Runnable runnable, final V v) {
                final kk<V> value = new kk<V>(runnable, v);
                synchronized (kl.this.d) {
                    kl.this.d.put(runnable, value);
                    return value;
                }
            }
            
            @Override
            protected final <V> RunnableFuture<V> newTaskFor(final Callable<V> callable) {
                throw new UnsupportedOperationException("Callable not supported");
            }
        }).setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy() {
            @Override
            public final void rejectedExecution(final Runnable r, final ThreadPoolExecutor e) {
                super.rejectedExecution(r, e);
                final mb a = kl.a(r);
                if (a != null) {
                    synchronized (kl.this.d) {
                        kl.this.d.remove(a);
                        // monitorexit(kl.a(this.a))
                        kl.this.b(a);
                        new ma() {
                            @Override
                            public final void a() {
                            }
                        }.run();
                    }
                }
            }
        });
        this.e.setThreadFactory(new lr(s));
    }
    
    static /* synthetic */ mb a(final Runnable runnable) {
        final mb mb = null;
        mb mb2;
        if (runnable instanceof kk) {
            mb2 = (mb)((kk)runnable).a();
        }
        else if (runnable instanceof mb) {
            mb2 = (mb)runnable;
        }
        else {
            km.a(6, kl.a, "Unknown runnable class: " + runnable.getClass().getName());
            mb2 = mb;
        }
        return mb2;
    }
    
    private void a(final T p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_1        
        //     3: ifnonnull       9
        //     6: aload_0        
        //     7: monitorexit    
        //     8: return         
        //     9: aload_0        
        //    10: getfield        com/flurry/sdk/kl.d:Ljava/util/HashMap;
        //    13: astore_2       
        //    14: aload_2        
        //    15: monitorenter   
        //    16: aload_0        
        //    17: getfield        com/flurry/sdk/kl.d:Ljava/util/HashMap;
        //    20: aload_1        
        //    21: invokevirtual   java/util/HashMap.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    24: checkcast       Ljava/util/concurrent/Future;
        //    27: astore_3       
        //    28: aload_2        
        //    29: monitorexit    
        //    30: aload_0        
        //    31: aload_1        
        //    32: invokespecial   com/flurry/sdk/kl.b:(Lcom/flurry/sdk/mb;)V
        //    35: aload_3        
        //    36: ifnull          47
        //    39: aload_3        
        //    40: iconst_1       
        //    41: invokeinterface java/util/concurrent/Future.cancel:(Z)Z
        //    46: pop            
        //    47: new             Lcom/flurry/sdk/kl$3;
        //    50: astore_2       
        //    51: aload_2        
        //    52: aload_0        
        //    53: aload_1        
        //    54: invokespecial   com/flurry/sdk/kl$3.<init>:(Lcom/flurry/sdk/kl;Lcom/flurry/sdk/mb;)V
        //    57: aload_2        
        //    58: invokevirtual   com/flurry/sdk/kl$3.run:()V
        //    61: goto            6
        //    64: astore_1       
        //    65: aload_0        
        //    66: monitorexit    
        //    67: aload_1        
        //    68: athrow         
        //    69: astore_1       
        //    70: aload_2        
        //    71: monitorexit    
        //    72: aload_1        
        //    73: athrow         
        //    Signature:
        //  (TT;)V
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  9      16     64     69     Any
        //  16     30     69     74     Any
        //  30     35     64     69     Any
        //  39     47     64     69     Any
        //  47     61     64     69     Any
        //  70     72     69     74     Any
        //  72     74     64     69     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0047:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void b(final T key) {
        synchronized (this) {
            this.c(this.c.get(key), key);
        }
    }
    
    private void b(final Object value, final T key) {
        synchronized (this) {
            this.b.a(value, key);
            this.c.put(key, value);
        }
    }
    
    private void c(final Object o, final T key) {
        synchronized (this) {
            this.b.b(o, key);
            this.c.remove(key);
        }
    }
    
    public final void a(final Object o) {
        // monitorenter(this)
        if (o != null) {
            try {
                final HashSet<T> set = new HashSet<T>();
                set.addAll((Collection<?>)this.b.a(o));
                final Iterator<Object> iterator = set.iterator();
                while (iterator.hasNext()) {
                    this.a(iterator.next());
                }
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    // monitorexit(this)
    
    public final void a(final Object o, final T task) {
        // monitorenter(this)
        if (o != null && task != null) {
            try {
                this.b(o, task);
                this.e.submit(task);
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    // monitorexit(this)
    
    public final long b(final Object o) {
        // monitorenter(this)
        long n;
        if (o == null) {
            n = 0L;
        }
        else {
            try {
                n = this.b.a(o).size();
            }
            finally {
            }
            // monitorexit(this)
        }
        // monitorexit(this)
        return n;
    }
}
