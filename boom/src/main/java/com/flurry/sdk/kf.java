// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.File;

public class kf<T>
{
    private static final String a;
    private final File b;
    private final lg<T> c;
    
    static {
        a = kf.class.getSimpleName();
    }
    
    public kf(final File b, final String s, final int n, final lj<T> lj) {
        this.b = b;
        this.c = new le<T>(new li<T>(s, n, lj));
    }
    
    public final T a() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aconst_null    
        //     3: astore_2       
        //     4: aload_0        
        //     5: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //     8: ifnonnull       13
        //    11: aload_2        
        //    12: areturn        
        //    13: aload_0        
        //    14: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //    17: invokevirtual   java/io/File.exists:()Z
        //    20: ifne            55
        //    23: iconst_5       
        //    24: getstatic       com/flurry/sdk/kf.a:Ljava/lang/String;
        //    27: new             Ljava/lang/StringBuilder;
        //    30: dup            
        //    31: ldc             "No data to read for file:"
        //    33: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    36: aload_0        
        //    37: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //    40: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    49: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    52: goto            11
        //    55: iconst_0       
        //    56: istore_3       
        //    57: new             Ljava/io/FileInputStream;
        //    60: astore_2       
        //    61: aload_2        
        //    62: aload_0        
        //    63: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //    66: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    69: aload_2        
        //    70: astore          4
        //    72: aload_0        
        //    73: getfield        com/flurry/sdk/kf.c:Lcom/flurry/sdk/lg;
        //    76: aload_2        
        //    77: invokeinterface com/flurry/sdk/lg.a:(Ljava/io/InputStream;)Ljava/lang/Object;
        //    82: astore          5
        //    84: aload           5
        //    86: astore          4
        //    88: aload_2        
        //    89: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    92: aload           4
        //    94: astore_2       
        //    95: iload_3        
        //    96: ifeq            11
        //    99: iconst_3       
        //   100: getstatic       com/flurry/sdk/kf.a:Ljava/lang/String;
        //   103: new             Ljava/lang/StringBuilder;
        //   106: dup            
        //   107: ldc             "Deleting data file:"
        //   109: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   112: aload_0        
        //   113: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //   116: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   119: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   122: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   125: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   128: aload_0        
        //   129: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //   132: invokevirtual   java/io/File.delete:()Z
        //   135: pop            
        //   136: aload           4
        //   138: astore_2       
        //   139: goto            11
        //   142: astore          5
        //   144: aconst_null    
        //   145: astore_2       
        //   146: aload_2        
        //   147: astore          4
        //   149: getstatic       com/flurry/sdk/kf.a:Ljava/lang/String;
        //   152: astore          6
        //   154: aload_2        
        //   155: astore          4
        //   157: new             Ljava/lang/StringBuilder;
        //   160: astore          7
        //   162: aload_2        
        //   163: astore          4
        //   165: aload           7
        //   167: ldc             "Error reading data file:"
        //   169: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   172: aload_2        
        //   173: astore          4
        //   175: iconst_3       
        //   176: aload           6
        //   178: aload           7
        //   180: aload_0        
        //   181: getfield        com/flurry/sdk/kf.b:Ljava/io/File;
        //   184: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   187: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   190: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   193: aload           5
        //   195: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   198: iconst_1       
        //   199: istore_3       
        //   200: aload_2        
        //   201: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   204: aload_1        
        //   205: astore          4
        //   207: goto            92
        //   210: astore_2       
        //   211: aconst_null    
        //   212: astore          4
        //   214: aload           4
        //   216: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   219: aload_2        
        //   220: athrow         
        //   221: astore_2       
        //   222: goto            214
        //   225: astore          5
        //   227: goto            146
        //    Signature:
        //  ()TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  57     69     142    146    Ljava/lang/Exception;
        //  57     69     210    214    Any
        //  72     84     225    230    Ljava/lang/Exception;
        //  72     84     221    225    Any
        //  149    154    221    225    Any
        //  157    162    221    225    Any
        //  165    172    221    225    Any
        //  175    198    221    225    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0092:
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
    
    public final void a(final T t) {
        int n = 0;
        OutputStream outputStream = null;
        final Closeable closeable = null;
        Label_0042: {
            if (t == null) {
                km.a(3, kf.a, "No data to write for file:" + this.b.getName());
                n = 1;
            }
            else {
                Object o = outputStream;
                Object o2 = null;
                Label_0182: {
                    try {
                        if (!lx.a(this.b)) {
                            o = outputStream;
                            o = outputStream;
                            final IOException ex = new IOException("Cannot create parent directory!");
                            o = outputStream;
                            throw ex;
                        }
                        break Label_0182;
                    }
                    catch (Exception outputStream) {
                        o2 = (o = closeable);
                        final String a = kf.a;
                        o = o2;
                        o = o2;
                        final StringBuilder sb = new StringBuilder("Error writing data file:");
                        o = o2;
                        km.a(3, a, sb.append(this.b.getName()).toString(), (Throwable)outputStream);
                        ly.a((Closeable)o2);
                        n = 1;
                        break Label_0042;
                        o = outputStream;
                        outputStream = new FileOutputStream(this.b);
                        final kf kf = this;
                        final lg<T> lg = kf.c;
                        final OutputStream outputStream2 = outputStream;
                        final Object o3 = o2;
                        lg.a(outputStream2, (T)o3);
                        final OutputStream outputStream3 = outputStream;
                        ly.a(outputStream3);
                        break Label_0042;
                    }
                    finally {
                        final Closeable closeable2;
                        o2 = closeable2;
                    }
                }
                while (true) {
                    try {
                        final kf kf = this;
                        final lg<T> lg = kf.c;
                        final OutputStream outputStream2 = outputStream;
                        final Object o3 = o2;
                        lg.a(outputStream2, (T)o3);
                        final OutputStream outputStream3 = outputStream;
                        ly.a(outputStream3);
                        break Label_0042;
                        ly.a((Closeable)o);
                        throw o2;
                    }
                    catch (Exception ex2) {}
                    finally {
                        o = outputStream;
                        continue;
                    }
                    break;
                }
            }
        }
        if (n != 0) {
            km.a(3, kf.a, "Deleting data file:" + this.b.getName());
            this.b.delete();
        }
    }
    
    public final boolean b() {
        return this.b != null && this.b.delete();
    }
}
