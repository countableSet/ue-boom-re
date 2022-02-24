// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import android.content.Context;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ko
{
    private static final String a;
    private static final Map<Class<? extends kp>, kn> b;
    private final Map<Class<? extends kp>, kp> c;
    
    static {
        a = ko.class.getSimpleName();
        b = new LinkedHashMap<Class<? extends kp>, kn>();
    }
    
    public ko() {
        this.c = new LinkedHashMap<Class<? extends kp>, kp>();
    }
    
    public static void a(final Class<? extends kp> clazz) {
        if (clazz != null) {
            synchronized (ko.b) {
                ko.b.put(clazz, new kn(clazz));
            }
        }
    }
    
    public final void a(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_1        
        //     3: ifnonnull       18
        //     6: iconst_5       
        //     7: getstatic       com/flurry/sdk/ko.a:Ljava/lang/String;
        //    10: ldc             "Null context."
        //    12: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    15: aload_0        
        //    16: monitorexit    
        //    17: return         
        //    18: getstatic       com/flurry/sdk/ko.b:Ljava/util/Map;
        //    21: astore_2       
        //    22: aload_2        
        //    23: monitorenter   
        //    24: new             Ljava/util/ArrayList;
        //    27: astore_3       
        //    28: aload_3        
        //    29: getstatic       com/flurry/sdk/ko.b:Ljava/util/Map;
        //    32: invokeinterface java/util/Map.values:()Ljava/util/Collection;
        //    37: invokespecial   java/util/ArrayList.<init>:(Ljava/util/Collection;)V
        //    40: aload_2        
        //    41: monitorexit    
        //    42: aload_3        
        //    43: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    48: astore_3       
        //    49: aload_3        
        //    50: invokeinterface java/util/Iterator.hasNext:()Z
        //    55: ifeq            195
        //    58: aload_3        
        //    59: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    64: checkcast       Lcom/flurry/sdk/kn;
        //    67: astore_2       
        //    68: aload_2        
        //    69: getfield        com/flurry/sdk/kn.a:Ljava/lang/Class;
        //    72: ifnull          189
        //    75: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    78: aload_2        
        //    79: getfield        com/flurry/sdk/kn.b:I
        //    82: if_icmplt       189
        //    85: iconst_1       
        //    86: istore          4
        //    88: iload           4
        //    90: ifeq            49
        //    93: aload_2        
        //    94: getfield        com/flurry/sdk/kn.a:Ljava/lang/Class;
        //    97: invokevirtual   java/lang/Class.newInstance:()Ljava/lang/Object;
        //   100: checkcast       Lcom/flurry/sdk/kp;
        //   103: astore          5
        //   105: aload           5
        //   107: aload_1        
        //   108: invokeinterface com/flurry/sdk/kp.a:(Landroid/content/Context;)V
        //   113: aload_0        
        //   114: getfield        com/flurry/sdk/ko.c:Ljava/util/Map;
        //   117: aload_2        
        //   118: getfield        com/flurry/sdk/kn.a:Ljava/lang/Class;
        //   121: aload           5
        //   123: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   128: pop            
        //   129: goto            49
        //   132: astore          5
        //   134: getstatic       com/flurry/sdk/ko.a:Ljava/lang/String;
        //   137: astore          6
        //   139: new             Ljava/lang/StringBuilder;
        //   142: astore          7
        //   144: aload           7
        //   146: ldc             "Flurry Module for class "
        //   148: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   151: iconst_5       
        //   152: aload           6
        //   154: aload           7
        //   156: aload_2        
        //   157: getfield        com/flurry/sdk/kn.a:Ljava/lang/Class;
        //   160: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   163: ldc             " is not available:"
        //   165: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   168: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   171: aload           5
        //   173: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   176: goto            49
        //   179: astore_1       
        //   180: aload_0        
        //   181: monitorexit    
        //   182: aload_1        
        //   183: athrow         
        //   184: astore_1       
        //   185: aload_2        
        //   186: monitorexit    
        //   187: aload_1        
        //   188: athrow         
        //   189: iconst_0       
        //   190: istore          4
        //   192: goto            88
        //   195: invokestatic    com/flurry/sdk/lm.a:()Lcom/flurry/sdk/lm;
        //   198: aload_1        
        //   199: invokevirtual   com/flurry/sdk/lm.a:(Landroid/content/Context;)V
        //   202: invokestatic    com/flurry/sdk/kc.a:()Lcom/flurry/sdk/kc;
        //   205: pop            
        //   206: goto            15
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      15     179    184    Any
        //  18     24     179    184    Any
        //  24     42     184    189    Any
        //  42     49     179    184    Any
        //  49     68     179    184    Any
        //  68     85     132    179    Ljava/lang/Exception;
        //  68     85     179    184    Any
        //  93     129    132    179    Ljava/lang/Exception;
        //  93     129    179    184    Any
        //  134    176    179    184    Any
        //  185    187    184    189    Any
        //  187    189    179    184    Any
        //  195    206    179    184    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0049:
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
    
    public final kp b(final Class<? extends kp> obj) {
        kp kp;
        if (obj == null) {
            kp = null;
        }
        else {
            synchronized (this.c) {
                final kp kp2 = this.c.get(obj);
                // monitorexit(this.c)
                kp = kp2;
                if (kp2 == null) {
                    throw new IllegalStateException("Module was not registered/initialized. " + obj);
                }
            }
        }
        return kp;
    }
}
