// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import com.google.android.gms.ads.identifier.AdvertisingIdClient$Info;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import java.util.Locale;
import android.text.TextUtils;
import android.provider.Settings$Secure;
import java.io.File;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class jl
{
    private static final String b;
    private static jl c;
    public final Map<jt, byte[]> a;
    private final Set<String> d;
    private final kh<ll> e;
    private a f;
    private jv g;
    private String h;
    
    static {
        b = jl.class.getSimpleName();
    }
    
    private jl() {
        final HashSet<String> s = new HashSet<String>();
        s.add("null");
        s.add("9774d56d682e549c");
        s.add("dead00beef");
        this.d = (Set<String>)Collections.unmodifiableSet((Set<?>)s);
        this.a = new HashMap<jt, byte[]>();
        this.e = new kh<ll>() {};
        this.f = jl.a.a;
        ki.a().a("com.flurry.android.sdk.FlurrySessionEvent", this.e);
        jy.a().b(new ma() {
            @Override
            public final void a() {
                jl.b(jl.this);
            }
        });
    }
    
    public static jl a() {
        synchronized (jl.class) {
            if (jl.c == null) {
                jl.c = new jl();
            }
            return jl.c;
        }
    }
    
    private static void a(final String p0, final File p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_2       
        //     4: new             Ljava/io/FileOutputStream;
        //     7: astore_3       
        //     8: aload_3        
        //     9: aload_1        
        //    10: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    13: aload_2        
        //    14: aload_3        
        //    15: invokespecial   java/io/DataOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    18: aload_2        
        //    19: astore_1       
        //    20: aload_2        
        //    21: iconst_1       
        //    22: invokeinterface java/io/DataOutput.writeInt:(I)V
        //    27: aload_2        
        //    28: astore_1       
        //    29: aload_2        
        //    30: aload_0        
        //    31: invokeinterface java/io/DataOutput.writeUTF:(Ljava/lang/String;)V
        //    36: aload_2        
        //    37: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    40: return         
        //    41: astore_3       
        //    42: aconst_null    
        //    43: astore_0       
        //    44: aload_0        
        //    45: astore_1       
        //    46: bipush          6
        //    48: getstatic       com/flurry/sdk/jl.b:Ljava/lang/String;
        //    51: ldc             "Error when saving deviceId"
        //    53: aload_3        
        //    54: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //    57: aload_0        
        //    58: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    61: goto            40
        //    64: astore_0       
        //    65: aconst_null    
        //    66: astore_1       
        //    67: aload_1        
        //    68: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    71: aload_0        
        //    72: athrow         
        //    73: astore_0       
        //    74: goto            67
        //    77: astore_3       
        //    78: aload_2        
        //    79: astore_0       
        //    80: goto            44
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      18     41     44     Ljava/lang/Throwable;
        //  0      18     64     67     Any
        //  20     27     77     83     Ljava/lang/Throwable;
        //  20     27     73     77     Any
        //  29     36     77     83     Ljava/lang/Throwable;
        //  29     36     73     77     Any
        //  46     57     73     77     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0040:
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
    
    static /* synthetic */ void b(final jl jl) {
        while (!a.e.equals(jl.f)) {
            switch (jl$4.b[jl.f.ordinal()]) {
                case 1: {
                    jl.f = a.b;
                    break;
                }
                case 2: {
                    jl.f = a.c;
                    break;
                }
                case 3: {
                    jl.f = a.d;
                    break;
                }
                case 4: {
                    jl.f = a.e;
                    break;
                }
            }
            Label_0415: {
                Label_0181: {
                    try {
                        switch (jl$4.b[jl.f.ordinal()]) {
                            default: {
                                continue;
                            }
                            case 2: {
                                jl.d();
                                continue;
                            }
                            case 3: {
                                break Label_0181;
                            }
                            case 4: {
                                break Label_0415;
                            }
                        }
                    }
                    catch (Exception obj) {
                        km.a(4, jl.b, "Exception during id fetch:" + jl.f + ", " + obj);
                    }
                    continue;
                }
                ly.b();
                final String string = Settings$Secure.getString(jy.a().a.getContentResolver(), "android_id");
                int n;
                if (!TextUtils.isEmpty((CharSequence)string) && !jl.d.contains(string.toLowerCase(Locale.US))) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                String h;
                if (n == 0) {
                    h = null;
                }
                else {
                    h = "AND" + string;
                }
                if (TextUtils.isEmpty((CharSequence)h) && TextUtils.isEmpty((CharSequence)(h = f()))) {
                    String s;
                    if (TextUtils.isEmpty((CharSequence)(s = jl.g()))) {
                        s = "ID" + Long.toString(Double.doubleToLongBits(Math.random()) + (System.nanoTime() + ly.i(lv.a(jy.a().a)) * 37L) * 37L, 16);
                    }
                    h = s;
                    if (!TextUtils.isEmpty((CharSequence)s)) {
                        final File fileStreamPath = jy.a().a.getFileStreamPath(".flurryb.");
                        h = s;
                        if (lx.a(fileStreamPath)) {
                            a(s, fileStreamPath);
                            h = s;
                        }
                    }
                }
                jl.h = h;
                continue;
            }
            jl.h();
        }
        ki.a().a(new jm());
    }
    
    private void d() {
        ly.b();
        this.g = e();
        if (this.b()) {
            this.h();
            ki.a().a(new jn());
        }
    }
    
    private static jv e() {
        try {
            final AdvertisingIdClient$Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(jy.a().a);
            return new jv(advertisingIdInfo.getId(), advertisingIdInfo.isLimitAdTrackingEnabled());
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            km.b(jl.b, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return null;
        }
        catch (Exception ex) {
            km.b(jl.b, "GOOGLE PLAY SERVICES ERROR: " + ex.getMessage());
            km.b(jl.b, "There is a problem with the Google Play Services library, which is required for Android Advertising ID support. The Google Play Services library should be integrated in any app shipping in the Play Store that uses analytics or advertising.");
            return null;
        }
    }
    
    private static String f() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_0       
        //     2: aconst_null    
        //     3: astore_1       
        //     4: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //     7: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    10: ldc_w           ".flurryb."
        //    13: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
        //    16: astore_2       
        //    17: aload_1        
        //    18: astore_3       
        //    19: aload_2        
        //    20: ifnull          32
        //    23: aload_2        
        //    24: invokevirtual   java/io/File.exists:()Z
        //    27: ifne            34
        //    30: aload_1        
        //    31: astore_3       
        //    32: aload_3        
        //    33: areturn        
        //    34: new             Ljava/io/DataInputStream;
        //    37: astore          4
        //    39: new             Ljava/io/FileInputStream;
        //    42: astore_3       
        //    43: aload_3        
        //    44: aload_2        
        //    45: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //    48: aload           4
        //    50: aload_3        
        //    51: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //    54: aload           4
        //    56: astore_3       
        //    57: aload           4
        //    59: invokeinterface java/io/DataInput.readInt:()I
        //    64: istore          5
        //    66: iconst_1       
        //    67: iload           5
        //    69: if_icmpeq       82
        //    72: aload_0        
        //    73: astore_3       
        //    74: aload           4
        //    76: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //    79: goto            32
        //    82: aload           4
        //    84: astore_3       
        //    85: aload           4
        //    87: invokeinterface java/io/DataInput.readUTF:()Ljava/lang/String;
        //    92: astore_0       
        //    93: aload_0        
        //    94: astore_3       
        //    95: goto            74
        //    98: astore_0       
        //    99: aconst_null    
        //   100: astore          4
        //   102: aload           4
        //   104: astore_3       
        //   105: bipush          6
        //   107: getstatic       com/flurry/sdk/jl.b:Ljava/lang/String;
        //   110: ldc_w           "Error when loading deviceId"
        //   113: aload_0        
        //   114: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   117: aload           4
        //   119: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   122: aload_1        
        //   123: astore_3       
        //   124: goto            32
        //   127: astore          4
        //   129: aconst_null    
        //   130: astore_3       
        //   131: aload_3        
        //   132: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   135: aload           4
        //   137: athrow         
        //   138: astore          4
        //   140: goto            131
        //   143: astore_0       
        //   144: goto            102
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  34     54     98     102    Ljava/lang/Throwable;
        //  34     54     127    131    Any
        //  57     66     143    147    Ljava/lang/Throwable;
        //  57     66     138    143    Any
        //  85     93     143    147    Ljava/lang/Throwable;
        //  85     93     138    143    Any
        //  105    117    138    143    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0074:
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
    
    private String g() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aconst_null    
        //     3: astore_2       
        //     4: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //     7: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    10: invokevirtual   android/content/Context.getFilesDir:()Ljava/io/File;
        //    13: astore_3       
        //    14: aload_3        
        //    15: ifnonnull       24
        //    18: aload_2        
        //    19: astore          4
        //    21: aload           4
        //    23: areturn        
        //    24: aload_3        
        //    25: new             Lcom/flurry/sdk/jl$3;
        //    28: dup            
        //    29: aload_0        
        //    30: invokespecial   com/flurry/sdk/jl$3.<init>:(Lcom/flurry/sdk/jl;)V
        //    33: invokevirtual   java/io/File.list:(Ljava/io/FilenameFilter;)[Ljava/lang/String;
        //    36: astore_3       
        //    37: aload_2        
        //    38: astore          4
        //    40: aload_3        
        //    41: ifnull          21
        //    44: aload_2        
        //    45: astore          4
        //    47: aload_3        
        //    48: arraylength    
        //    49: ifeq            21
        //    52: aload_3        
        //    53: iconst_0       
        //    54: aaload         
        //    55: astore_3       
        //    56: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //    59: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    62: aload_3        
        //    63: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
        //    66: astore_3       
        //    67: aload_2        
        //    68: astore          4
        //    70: aload_3        
        //    71: ifnull          21
        //    74: aload_2        
        //    75: astore          4
        //    77: aload_3        
        //    78: invokevirtual   java/io/File.exists:()Z
        //    81: ifeq            21
        //    84: new             Ljava/io/DataInputStream;
        //    87: astore          5
        //    89: new             Ljava/io/FileInputStream;
        //    92: astore          4
        //    94: aload           4
        //    96: aload_3        
        //    97: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   100: aload           5
        //   102: aload           4
        //   104: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //   107: aload           5
        //   109: astore_3       
        //   110: aload           5
        //   112: invokeinterface java/io/DataInput.readUnsignedShort:()I
        //   117: istore          6
        //   119: ldc_w           46586
        //   122: iload           6
        //   124: if_icmpeq       138
        //   127: aload_1        
        //   128: astore          4
        //   130: aload           5
        //   132: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   135: goto            21
        //   138: aload_1        
        //   139: astore          4
        //   141: aload           5
        //   143: astore_3       
        //   144: iconst_2       
        //   145: aload           5
        //   147: invokeinterface java/io/DataInput.readUnsignedShort:()I
        //   152: if_icmpne       130
        //   155: aload           5
        //   157: astore_3       
        //   158: aload           5
        //   160: invokeinterface java/io/DataInput.readUTF:()Ljava/lang/String;
        //   165: pop            
        //   166: aload           5
        //   168: astore_3       
        //   169: aload           5
        //   171: invokeinterface java/io/DataInput.readUTF:()Ljava/lang/String;
        //   176: astore          4
        //   178: goto            130
        //   181: astore_1       
        //   182: aconst_null    
        //   183: astore          4
        //   185: aload           4
        //   187: astore_3       
        //   188: bipush          6
        //   190: getstatic       com/flurry/sdk/jl.b:Ljava/lang/String;
        //   193: ldc_w           "Error when loading deviceId"
        //   196: aload_1        
        //   197: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   200: aload           4
        //   202: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   205: aload_2        
        //   206: astore          4
        //   208: goto            21
        //   211: astore_3       
        //   212: aconst_null    
        //   213: astore          5
        //   215: aload_3        
        //   216: astore          4
        //   218: aload           5
        //   220: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   223: aload           4
        //   225: athrow         
        //   226: astore          4
        //   228: aload_3        
        //   229: astore          5
        //   231: goto            218
        //   234: astore_1       
        //   235: aload           5
        //   237: astore          4
        //   239: goto            185
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  84     107    181    185    Ljava/lang/Throwable;
        //  84     107    211    218    Any
        //  110    119    234    242    Ljava/lang/Throwable;
        //  110    119    226    234    Any
        //  144    155    234    242    Ljava/lang/Throwable;
        //  144    155    226    234    Any
        //  158    166    234    242    Ljava/lang/Throwable;
        //  158    166    226    234    Any
        //  169    178    234    242    Ljava/lang/Throwable;
        //  169    178    226    234    Any
        //  188    200    226    234    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0130:
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
    
    private void h() {
        String a;
        if (this.g == null) {
            a = null;
        }
        else {
            a = this.g.a;
        }
        if (a != null) {
            km.a(3, jl.b, "Fetched advertising id");
            this.a.put(jt.b, ly.e(a));
        }
        final String h = this.h;
        if (h != null) {
            km.a(3, jl.b, "Fetched device id");
            this.a.put(jt.a, ly.e(h));
        }
    }
    
    public final boolean b() {
        return jl.a.e.equals(this.f);
    }
    
    public final boolean c() {
        boolean b = true;
        if (this.g != null && this.g.b) {
            b = false;
        }
        return b;
    }
    
    enum a
    {
        a, 
        b, 
        c, 
        d, 
        e;
    }
}
