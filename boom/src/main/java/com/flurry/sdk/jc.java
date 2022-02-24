// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.util.HashMap;
import java.util.Map;

public final class jc
{
    public String a;
    public boolean b;
    public boolean c;
    public long d;
    private final Map<String, String> e;
    private int f;
    private long g;
    
    public jc(final int f, final String a, final Map<String, String> map, final long g, final boolean b) {
        this.e = new HashMap<String, String>();
        this.f = f;
        this.a = a;
        if (map != null) {
            this.e.putAll(map);
        }
        this.g = g;
        this.b = b;
        this.c = !this.b;
    }
    
    public final Map<String, String> a() {
        synchronized (this) {
            return new HashMap<String, String>(this.e);
        }
    }
    
    public final void a(final long n) {
        this.c = true;
        this.d = n - this.g;
        km.a(3, "FlurryAgent", "Ended event '" + this.a + "' (" + this.g + ") after " + this.d + "ms");
    }
    
    public final void a(final Map<String, String> map) {
        // monitorenter(this)
        if (map == null) {
            return;
        }
        try {
            this.e.putAll(map);
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public final void b(final Map<String, String> map) {
        synchronized (this) {
            this.e.clear();
            this.e.putAll(map);
        }
    }
    
    public final byte[] b() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: new             Ljava/io/ByteArrayOutputStream;
        //     5: astore_1       
        //     6: aload_1        
        //     7: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    10: new             Ljava/io/DataOutputStream;
        //    13: astore_2       
        //    14: aload_2        
        //    15: aload_1        
        //    16: invokespecial   java/io/DataOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    19: aload_2        
        //    20: aload_0        
        //    21: getfield        com/flurry/sdk/jc.f:I
        //    24: invokevirtual   java/io/DataOutputStream.writeShort:(I)V
        //    27: aload_2        
        //    28: aload_0        
        //    29: getfield        com/flurry/sdk/jc.a:Ljava/lang/String;
        //    32: invokevirtual   java/io/DataOutputStream.writeUTF:(Ljava/lang/String;)V
        //    35: aload_2        
        //    36: aload_0        
        //    37: getfield        com/flurry/sdk/jc.e:Ljava/util/Map;
        //    40: invokeinterface java/util/Map.size:()I
        //    45: invokevirtual   java/io/DataOutputStream.writeShort:(I)V
        //    48: aload_0        
        //    49: getfield        com/flurry/sdk/jc.e:Ljava/util/Map;
        //    52: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //    57: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    62: astore_3       
        //    63: aload_3        
        //    64: invokeinterface java/util/Iterator.hasNext:()Z
        //    69: ifeq            142
        //    72: aload_3        
        //    73: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    78: checkcast       Ljava/util/Map$Entry;
        //    81: astore          4
        //    83: aload_2        
        //    84: aload           4
        //    86: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //    91: checkcast       Ljava/lang/String;
        //    94: invokestatic    com/flurry/sdk/ly.b:(Ljava/lang/String;)Ljava/lang/String;
        //    97: invokevirtual   java/io/DataOutputStream.writeUTF:(Ljava/lang/String;)V
        //   100: aload_2        
        //   101: aload           4
        //   103: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   108: checkcast       Ljava/lang/String;
        //   111: invokestatic    com/flurry/sdk/ly.b:(Ljava/lang/String;)Ljava/lang/String;
        //   114: invokevirtual   java/io/DataOutputStream.writeUTF:(Ljava/lang/String;)V
        //   117: goto            63
        //   120: astore          4
        //   122: aload_2        
        //   123: astore          4
        //   125: iconst_0       
        //   126: newarray        B
        //   128: astore_2       
        //   129: aload           4
        //   131: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   134: aload_2        
        //   135: astore          4
        //   137: aload_0        
        //   138: monitorexit    
        //   139: aload           4
        //   141: areturn        
        //   142: aload_2        
        //   143: aload_0        
        //   144: getfield        com/flurry/sdk/jc.g:J
        //   147: invokevirtual   java/io/DataOutputStream.writeLong:(J)V
        //   150: aload_2        
        //   151: aload_0        
        //   152: getfield        com/flurry/sdk/jc.d:J
        //   155: invokevirtual   java/io/DataOutputStream.writeLong:(J)V
        //   158: aload_2        
        //   159: invokevirtual   java/io/DataOutputStream.flush:()V
        //   162: aload_1        
        //   163: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //   166: astore          4
        //   168: aload_2        
        //   169: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   172: goto            137
        //   175: astore          4
        //   177: aload_0        
        //   178: monitorexit    
        //   179: aload           4
        //   181: athrow         
        //   182: astore          4
        //   184: aconst_null    
        //   185: astore_2       
        //   186: aload_2        
        //   187: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   190: aload           4
        //   192: athrow         
        //   193: astore          4
        //   195: goto            186
        //   198: astore_2       
        //   199: aload           4
        //   201: astore_1       
        //   202: aload_2        
        //   203: astore          4
        //   205: aload_1        
        //   206: astore_2       
        //   207: goto            186
        //   210: astore          4
        //   212: aconst_null    
        //   213: astore          4
        //   215: goto            125
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      19     210    218    Ljava/io/IOException;
        //  2      19     182    186    Any
        //  19     63     120    125    Ljava/io/IOException;
        //  19     63     193    198    Any
        //  63     117    120    125    Ljava/io/IOException;
        //  63     117    193    198    Any
        //  125    129    198    210    Any
        //  129    134    175    182    Any
        //  142    168    120    125    Ljava/io/IOException;
        //  142    168    193    198    Any
        //  168    172    175    182    Any
        //  186    193    175    182    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0125:
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
}
