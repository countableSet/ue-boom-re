// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.os;

import android.os.Build$VERSION;

public final class CancellationSignal
{
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
    
    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                this.wait();
            }
            catch (InterruptedException ex) {}
        }
    }
    
    public void cancel() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        android/support/v4/os/CancellationSignal.mIsCanceled:Z
        //     6: ifeq            12
        //     9: aload_0        
        //    10: monitorexit    
        //    11: return         
        //    12: aload_0        
        //    13: iconst_1       
        //    14: putfield        android/support/v4/os/CancellationSignal.mIsCanceled:Z
        //    17: aload_0        
        //    18: iconst_1       
        //    19: putfield        android/support/v4/os/CancellationSignal.mCancelInProgress:Z
        //    22: aload_0        
        //    23: getfield        android/support/v4/os/CancellationSignal.mOnCancelListener:Landroid/support/v4/os/CancellationSignal$OnCancelListener;
        //    26: astore_1       
        //    27: aload_0        
        //    28: getfield        android/support/v4/os/CancellationSignal.mCancellationSignalObj:Ljava/lang/Object;
        //    31: astore_2       
        //    32: aload_0        
        //    33: monitorexit    
        //    34: aload_1        
        //    35: ifnull          44
        //    38: aload_1        
        //    39: invokeinterface android/support/v4/os/CancellationSignal$OnCancelListener.onCancel:()V
        //    44: aload_2        
        //    45: ifnull          52
        //    48: aload_2        
        //    49: invokestatic    android/support/v4/os/CancellationSignalCompatJellybean.cancel:(Ljava/lang/Object;)V
        //    52: aload_0        
        //    53: monitorenter   
        //    54: aload_0        
        //    55: iconst_0       
        //    56: putfield        android/support/v4/os/CancellationSignal.mCancelInProgress:Z
        //    59: aload_0        
        //    60: invokevirtual   java/lang/Object.notifyAll:()V
        //    63: aload_0        
        //    64: monitorexit    
        //    65: goto            11
        //    68: astore_1       
        //    69: aload_0        
        //    70: monitorexit    
        //    71: aload_1        
        //    72: athrow         
        //    73: astore_1       
        //    74: aload_0        
        //    75: monitorexit    
        //    76: aload_1        
        //    77: athrow         
        //    78: astore_1       
        //    79: aload_0        
        //    80: monitorenter   
        //    81: aload_0        
        //    82: iconst_0       
        //    83: putfield        android/support/v4/os/CancellationSignal.mCancelInProgress:Z
        //    86: aload_0        
        //    87: invokevirtual   java/lang/Object.notifyAll:()V
        //    90: aload_0        
        //    91: monitorexit    
        //    92: aload_1        
        //    93: athrow         
        //    94: astore_1       
        //    95: aload_0        
        //    96: monitorexit    
        //    97: aload_1        
        //    98: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      11     73     78     Any
        //  12     34     73     78     Any
        //  38     44     78     99     Any
        //  48     52     78     99     Any
        //  54     65     68     73     Any
        //  69     71     68     73     Any
        //  74     76     73     78     Any
        //  81     92     94     99     Any
        //  95     97     94     99     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0044:
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
    
    public Object getCancellationSignalObject() {
        Object o = null;
        if (Build$VERSION.SDK_INT < 16) {
            o = null;
        }
        else {
            synchronized (this) {
                if (this.mCancellationSignalObj == null) {
                    this.mCancellationSignalObj = CancellationSignalCompatJellybean.create();
                    if (this.mIsCanceled) {
                        CancellationSignalCompatJellybean.cancel(this.mCancellationSignalObj);
                    }
                }
                final Object mCancellationSignalObj = this.mCancellationSignalObj;
            }
        }
        return o;
    }
    
    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }
    
    public void setOnCancelListener(final OnCancelListener mOnCancelListener) {
        while (true) {
            Label_0043: {
                synchronized (this) {
                    this.waitForCancelFinishedLocked();
                    if (this.mOnCancelListener != mOnCancelListener) {
                        this.mOnCancelListener = mOnCancelListener;
                        if (this.mIsCanceled && mOnCancelListener != null) {
                            break Label_0043;
                        }
                    }
                    return;
                }
            }
            // monitorexit(this)
            final OnCancelListener onCancelListener;
            onCancelListener.onCancel();
        }
    }
    
    public void throwIfCanceled() {
        if (this.isCanceled()) {
            throw new OperationCanceledException();
        }
    }
    
    public interface OnCancelListener
    {
        void onCancel();
    }
}
