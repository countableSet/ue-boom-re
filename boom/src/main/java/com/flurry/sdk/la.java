// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.Serializable;
import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.LinkedHashMap;

public class la
{
    public static final Integer a;
    private static final String d;
    String b;
    LinkedHashMap<String, List<String>> c;
    
    static {
        d = la.class.getSimpleName();
        a = 50;
    }
    
    public la(final String str) {
        this.b(this.b = str + "Main");
    }
    
    private void a() {
        synchronized (this) {
            final LinkedList<String> list = new LinkedList<String>(this.c.keySet());
            new kf(jy.a().a.getFileStreamPath(d(this.b)), ".YFlurrySenderIndex.info.", 1, new lj<List<lb>>() {
                @Override
                public final lg<List<lb>> a(final int n) {
                    return new lf<lb>(new lb.a());
                }
            }).b();
            if (!list.isEmpty()) {
                this.a(this.b, list, this.b);
            }
        }
    }
    
    private void a(final String str, final List<String> list, final String s) {
        Object o;
        synchronized (this) {
            ly.b();
            final String d = la.d;
            o = new StringBuilder("Saving Index File for ");
            km.a(5, d, ((StringBuilder)o).append(str).append(" file name:").append(jy.a().a.getFileStreamPath(d(str))).toString());
            o = new kf<Throwable>(jy.a().a.getFileStreamPath(d(str)), s, 1, (lj<Object>)new lj<List<lb>>() {
                @Override
                public final lg<List<lb>> a(final int n) {
                    return new lf<lb>(new lb.a());
                }
            });
            final ArrayList<lb> list2 = new ArrayList<lb>();
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                list2.add(new lb(iterator.next()));
            }
        }
        final Throwable t;
        ((kf<Throwable>)o).a(t);
    }
    // monitorexit(this)
    
    private void a(final String str, final byte[] array) {
        synchronized (this) {
            ly.b();
            km.a(5, la.d, "Saving Block File for " + str + " file name:" + jy.a().a.getFileStreamPath(kz.a(str)));
            new kf<kz>(jy.a().a.getFileStreamPath(kz.a(str)), ".yflurrydatasenderblock.", 1, new lj<kz>() {
                @Override
                public final lg<kz> a(final int n) {
                    return new kz.a();
                }
            }).a(new kz(array));
        }
    }
    
    private void b(final String s) {
        this.c = new LinkedHashMap<String, List<String>>();
        final ArrayList<String> list = new ArrayList<String>();
        if (this.i(s)) {
            final List<String> j = this.j(s);
            if (j != null && j.size() > 0) {
                list.addAll((Collection<?>)j);
                final Iterator<Object> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this.c(iterator.next());
                }
            }
            h(s);
        }
        else {
            final List<lb> list2 = new kf<List<lb>>(jy.a().a.getFileStreamPath(d(this.b)), s, 1, new lj<List<lb>>() {
                @Override
                public final lg<List<lb>> a(final int n) {
                    return new lf<lb>(new lb.a());
                }
            }).a();
            if (list2 == null) {
                km.c(la.d, "New main file also not found. returning..");
                return;
            }
            final Iterator<lb> iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                list.add(iterator2.next().a);
            }
        }
        for (final String key : list) {
            this.c.put(key, this.g(key));
        }
    }
    
    private void c(final String s) {
        final List<String> j = this.j(s);
        if (j == null) {
            km.c(la.d, "No old file to replace");
        }
        else {
            for (final String s2 : j) {
                final byte[] k = k(s2);
                if (k == null) {
                    km.a(6, la.d, "File does not exist");
                }
                else {
                    this.a(s2, k);
                    ly.b();
                    km.a(5, la.d, "Deleting  block File for " + s2 + " file name:" + jy.a().a.getFileStreamPath(".flurrydatasenderblock." + s2));
                    final File fileStreamPath = jy.a().a.getFileStreamPath(".flurrydatasenderblock." + s2);
                    if (!fileStreamPath.exists()) {
                        continue;
                    }
                    km.a(5, la.d, "Found file for " + s2 + ". Deleted - " + fileStreamPath.delete());
                }
            }
            if (j != null) {
                this.a(s, j, ".YFlurrySenderIndex.info.");
                h(s);
            }
        }
    }
    
    private static String d(final String str) {
        return ".YFlurrySenderIndex.info." + str;
    }
    
    private boolean e(final String s) {
        return new kf(jy.a().a.getFileStreamPath(kz.a(s)), ".yflurrydatasenderblock.", 1, new lj<kz>() {
            @Override
            public final lg<kz> a(final int n) {
                return new kz.a();
            }
        }).b();
    }
    
    private boolean f(final String s) {
        final kf kf;
        synchronized (this) {
            ly.b();
            kf = new kf(jy.a().a.getFileStreamPath(d(s)), ".YFlurrySenderIndex.info.", 1, new lj<List<lb>>() {
                @Override
                public final lg<List<lb>> a(final int n) {
                    return new lf<lb>(new lb.a());
                }
            });
            final List<String> a = this.a(s);
            if (a != null) {
                km.a(4, la.d, "discardOutdatedBlocksForDataKey: notSentBlocks = " + a.size());
                for (final String str : a) {
                    this.e(str);
                    km.a(4, la.d, "discardOutdatedBlocksForDataKey: removed block = " + str);
                }
            }
        }
        final Throwable key;
        this.c.remove(key);
        final boolean b = kf.b();
        this.a();
        // monitorexit(this)
        return b;
    }
    
    private List<String> g(final String str) {
        synchronized (this) {
            ly.b();
            km.a(5, la.d, "Reading Index File for " + str + " file name:" + jy.a().a.getFileStreamPath(d(str)));
            final List<lb> list = new kf<List<lb>>(jy.a().a.getFileStreamPath(d(str)), ".YFlurrySenderIndex.info.", 1, new lj<List<lb>>() {
                @Override
                public final lg<List<lb>> a(final int n) {
                    return new lf<lb>(new lb.a());
                }
            }).a();
            final ArrayList<String> list2 = new ArrayList<String>();
            final Iterator<lb> iterator = list.iterator();
            while (iterator.hasNext()) {
                list2.add(iterator.next().a);
            }
        }
        // monitorexit(this)
        return;
    }
    
    private static void h(final String s) {
        ly.b();
        km.a(5, la.d, "Deleting Index File for " + s + " file name:" + jy.a().a.getFileStreamPath(".FlurrySenderIndex.info." + s));
        final File fileStreamPath = jy.a().a.getFileStreamPath(".FlurrySenderIndex.info." + s);
        if (fileStreamPath.exists()) {
            km.a(5, la.d, "Found file for " + s + ". Deleted - " + fileStreamPath.delete());
        }
    }
    
    private boolean i(final String s) {
        synchronized (this) {
            final File fileStreamPath = jy.a().a.getFileStreamPath(".FlurrySenderIndex.info." + s);
            km.a(5, la.d, "isOldIndexFilePresent: for " + s + fileStreamPath.exists());
            return fileStreamPath.exists();
        }
    }
    
    private List<String> j(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aload_0        
        //     3: monitorenter   
        //     4: invokestatic    com/flurry/sdk/ly.b:()V
        //     7: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //    10: astore_3       
        //    11: new             Ljava/lang/StringBuilder;
        //    14: astore          4
        //    16: aload           4
        //    18: ldc_w           "Reading Index File for "
        //    21: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    24: aload           4
        //    26: aload_1        
        //    27: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    30: ldc             " file name:"
        //    32: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    35: astore          4
        //    37: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //    40: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    43: astore          5
        //    45: new             Ljava/lang/StringBuilder;
        //    48: astore          6
        //    50: aload           6
        //    52: ldc_w           ".FlurrySenderIndex.info."
        //    55: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    58: iconst_5       
        //    59: aload_3        
        //    60: aload           4
        //    62: aload           5
        //    64: aload           6
        //    66: aload_1        
        //    67: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    70: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    73: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
        //    76: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    79: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    82: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    85: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //    88: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    91: astore_3       
        //    92: new             Ljava/lang/StringBuilder;
        //    95: astore          4
        //    97: aload           4
        //    99: ldc_w           ".FlurrySenderIndex.info."
        //   102: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   105: aload_3        
        //   106: aload           4
        //   108: aload_1        
        //   109: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   112: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   115: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
        //   118: astore_3       
        //   119: aload_3        
        //   120: invokevirtual   java/io/File.exists:()Z
        //   123: ifeq            413
        //   126: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   129: astore          6
        //   131: new             Ljava/lang/StringBuilder;
        //   134: astore          4
        //   136: aload           4
        //   138: ldc_w           "Reading Index File for "
        //   141: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   144: iconst_5       
        //   145: aload           6
        //   147: aload           4
        //   149: aload_1        
        //   150: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   153: ldc_w           " Found file."
        //   156: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   159: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   162: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   165: new             Ljava/io/FileInputStream;
        //   168: astore_1       
        //   169: aload_1        
        //   170: aload_3        
        //   171: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   174: new             Ljava/io/DataInputStream;
        //   177: astore          4
        //   179: aload           4
        //   181: aload_1        
        //   182: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //   185: aload           4
        //   187: astore_3       
        //   188: aload           4
        //   190: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //   193: istore          7
        //   195: iload           7
        //   197: ifne            211
        //   200: aload           4
        //   202: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   205: aload_2        
        //   206: astore_1       
        //   207: aload_0        
        //   208: monitorexit    
        //   209: aload_1        
        //   210: areturn        
        //   211: aload           4
        //   213: astore_3       
        //   214: new             Ljava/util/ArrayList;
        //   217: astore_1       
        //   218: aload           4
        //   220: astore_3       
        //   221: aload_1        
        //   222: iload           7
        //   224: invokespecial   java/util/ArrayList.<init>:(I)V
        //   227: iconst_0       
        //   228: istore          8
        //   230: iload           8
        //   232: iload           7
        //   234: if_icmpge       353
        //   237: aload           4
        //   239: astore_3       
        //   240: aload           4
        //   242: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //   245: istore          9
        //   247: aload           4
        //   249: astore_3       
        //   250: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   253: astore          6
        //   255: aload           4
        //   257: astore_3       
        //   258: new             Ljava/lang/StringBuilder;
        //   261: astore_2       
        //   262: aload           4
        //   264: astore_3       
        //   265: aload_2        
        //   266: ldc_w           "read iter "
        //   269: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   272: aload           4
        //   274: astore_3       
        //   275: iconst_4       
        //   276: aload           6
        //   278: aload_2        
        //   279: iload           8
        //   281: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   284: ldc_w           " dataLength = "
        //   287: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   290: iload           9
        //   292: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   295: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   298: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   301: aload           4
        //   303: astore_3       
        //   304: iload           9
        //   306: newarray        B
        //   308: astore          6
        //   310: aload           4
        //   312: astore_3       
        //   313: aload           4
        //   315: aload           6
        //   317: invokevirtual   java/io/DataInputStream.readFully:([B)V
        //   320: aload           4
        //   322: astore_3       
        //   323: new             Ljava/lang/String;
        //   326: astore_2       
        //   327: aload           4
        //   329: astore_3       
        //   330: aload_2        
        //   331: aload           6
        //   333: invokespecial   java/lang/String.<init>:([B)V
        //   336: aload           4
        //   338: astore_3       
        //   339: aload_1        
        //   340: aload_2        
        //   341: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   346: pop            
        //   347: iinc            8, 1
        //   350: goto            230
        //   353: aload           4
        //   355: astore_3       
        //   356: aload           4
        //   358: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //   361: pop            
        //   362: aload           4
        //   364: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   367: goto            207
        //   370: astore_2       
        //   371: aconst_null    
        //   372: astore          4
        //   374: aconst_null    
        //   375: astore_1       
        //   376: aload           4
        //   378: astore_3       
        //   379: bipush          6
        //   381: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   384: ldc_w           "Error when loading persistent file"
        //   387: aload_2        
        //   388: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   391: aload           4
        //   393: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   396: goto            367
        //   399: astore_1       
        //   400: aload_0        
        //   401: monitorexit    
        //   402: aload_1        
        //   403: athrow         
        //   404: astore_1       
        //   405: aconst_null    
        //   406: astore_3       
        //   407: aload_3        
        //   408: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   411: aload_1        
        //   412: athrow         
        //   413: iconst_5       
        //   414: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   417: ldc_w           "Agent cache file doesn't exist."
        //   420: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   423: aconst_null    
        //   424: astore_1       
        //   425: goto            367
        //   428: astore_1       
        //   429: goto            407
        //   432: astore_2       
        //   433: aconst_null    
        //   434: astore_1       
        //   435: goto            376
        //   438: astore_2       
        //   439: goto            376
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/List<Ljava/lang/String;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      165    399    404    Any
        //  165    185    370    376    Ljava/lang/Throwable;
        //  165    185    404    407    Any
        //  188    195    432    438    Ljava/lang/Throwable;
        //  188    195    428    432    Any
        //  200    205    399    404    Any
        //  214    218    432    438    Ljava/lang/Throwable;
        //  214    218    428    432    Any
        //  221    227    432    438    Ljava/lang/Throwable;
        //  221    227    428    432    Any
        //  240    247    438    442    Ljava/lang/Throwable;
        //  240    247    428    432    Any
        //  250    255    438    442    Ljava/lang/Throwable;
        //  250    255    428    432    Any
        //  258    262    438    442    Ljava/lang/Throwable;
        //  258    262    428    432    Any
        //  265    272    438    442    Ljava/lang/Throwable;
        //  265    272    428    432    Any
        //  275    301    438    442    Ljava/lang/Throwable;
        //  275    301    428    432    Any
        //  304    310    438    442    Ljava/lang/Throwable;
        //  304    310    428    432    Any
        //  313    320    438    442    Ljava/lang/Throwable;
        //  313    320    428    432    Any
        //  323    327    438    442    Ljava/lang/Throwable;
        //  323    327    428    432    Any
        //  330    336    438    442    Ljava/lang/Throwable;
        //  330    336    428    432    Any
        //  339    347    438    442    Ljava/lang/Throwable;
        //  339    347    428    432    Any
        //  356    362    438    442    Ljava/lang/Throwable;
        //  356    362    428    432    Any
        //  362    367    399    404    Any
        //  379    391    428    432    Any
        //  391    396    399    404    Any
        //  407    413    399    404    Any
        //  413    423    399    404    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 219 out of bounds for length 219
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private static byte[] k(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aconst_null    
        //     3: astore_2       
        //     4: aconst_null    
        //     5: astore_3       
        //     6: invokestatic    com/flurry/sdk/ly.b:()V
        //     9: iconst_5       
        //    10: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //    13: new             Ljava/lang/StringBuilder;
        //    16: dup            
        //    17: ldc_w           "Reading block File for "
        //    20: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    23: aload_0        
        //    24: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    27: ldc             " file name:"
        //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    32: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //    35: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    38: new             Ljava/lang/StringBuilder;
        //    41: dup            
        //    42: ldc             ".flurrydatasenderblock."
        //    44: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    47: aload_0        
        //    48: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    51: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    54: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
        //    57: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    60: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    63: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //    66: invokestatic    com/flurry/sdk/jy.a:()Lcom/flurry/sdk/jy;
        //    69: getfield        com/flurry/sdk/jy.a:Landroid/content/Context;
        //    72: new             Ljava/lang/StringBuilder;
        //    75: dup            
        //    76: ldc             ".flurrydatasenderblock."
        //    78: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    81: aload_0        
        //    82: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    85: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    88: invokevirtual   android/content/Context.getFileStreamPath:(Ljava/lang/String;)Ljava/io/File;
        //    91: astore          4
        //    93: aload           4
        //    95: invokevirtual   java/io/File.exists:()Z
        //    98: ifeq            256
        //   101: iconst_5       
        //   102: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   105: new             Ljava/lang/StringBuilder;
        //   108: dup            
        //   109: ldc_w           "Reading Index File for "
        //   112: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   115: aload_0        
        //   116: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   119: ldc_w           " Found file."
        //   122: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   125: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   128: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   131: new             Ljava/io/FileInputStream;
        //   134: astore_0       
        //   135: aload_0        
        //   136: aload           4
        //   138: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   141: new             Ljava/io/DataInputStream;
        //   144: astore          4
        //   146: aload           4
        //   148: aload_0        
        //   149: invokespecial   java/io/DataInputStream.<init>:(Ljava/io/InputStream;)V
        //   152: aload           4
        //   154: astore_0       
        //   155: aload_2        
        //   156: astore_1       
        //   157: aload           4
        //   159: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //   162: istore          5
        //   164: iload           5
        //   166: ifne            178
        //   169: aload           4
        //   171: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   174: aload_3        
        //   175: astore_1       
        //   176: aload_1        
        //   177: areturn        
        //   178: aload           4
        //   180: astore_0       
        //   181: aload_2        
        //   182: astore_1       
        //   183: iload           5
        //   185: newarray        B
        //   187: astore_3       
        //   188: aload           4
        //   190: astore_0       
        //   191: aload_3        
        //   192: astore_1       
        //   193: aload           4
        //   195: aload_3        
        //   196: invokevirtual   java/io/DataInputStream.readFully:([B)V
        //   199: aload           4
        //   201: astore_0       
        //   202: aload_3        
        //   203: astore_1       
        //   204: aload           4
        //   206: invokevirtual   java/io/DataInputStream.readUnsignedShort:()I
        //   209: pop            
        //   210: aload           4
        //   212: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   215: aload_3        
        //   216: astore_1       
        //   217: goto            176
        //   220: astore_3       
        //   221: aconst_null    
        //   222: astore          4
        //   224: aload           4
        //   226: astore_0       
        //   227: bipush          6
        //   229: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   232: ldc_w           "Error when loading persistent file"
        //   235: aload_3        
        //   236: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   239: aload           4
        //   241: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   244: goto            176
        //   247: astore_1       
        //   248: aconst_null    
        //   249: astore_0       
        //   250: aload_0        
        //   251: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   254: aload_1        
        //   255: athrow         
        //   256: iconst_4       
        //   257: getstatic       com/flurry/sdk/la.d:Ljava/lang/String;
        //   260: ldc_w           "Agent cache file doesn't exist."
        //   263: invokestatic    com/flurry/sdk/km.a:(ILjava/lang/String;Ljava/lang/String;)V
        //   266: aload_3        
        //   267: astore_1       
        //   268: goto            176
        //   271: astore_1       
        //   272: goto            250
        //   275: astore_3       
        //   276: goto            224
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  131    152    220    224    Ljava/lang/Throwable;
        //  131    152    247    250    Any
        //  157    164    275    279    Ljava/lang/Throwable;
        //  157    164    271    275    Any
        //  183    188    275    279    Ljava/lang/Throwable;
        //  183    188    271    275    Any
        //  193    199    275    279    Ljava/lang/Throwable;
        //  193    199    271    275    Any
        //  204    210    275    279    Ljava/lang/Throwable;
        //  204    210    271    275    Any
        //  227    239    271    275    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0176:
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
    
    public final List<String> a(final String key) {
        return this.c.get(key);
    }
    
    public final void a(final kz kz, final String key) {
        while (true) {
            boolean b = false;
            while (true) {
                Label_0160: {
                    synchronized (this) {
                        final String d = la.d;
                        Serializable a = new StringBuilder("addBlockInfo");
                        km.a(4, d, ((StringBuilder)a).append(key).toString());
                        a = kz.a;
                        if (this.c.get(key) == null) {
                            km.a(4, la.d, "New Data Key");
                            final LinkedList<Object> value = new LinkedList<Object>();
                            b = true;
                            value.add(a);
                            if (value.size() > la.a) {
                                this.e((String)value.get(0));
                                value.remove(0);
                            }
                            this.c.put(key, (LinkedList<String>)value);
                            this.a(key, (List<String>)value, ".YFlurrySenderIndex.info.");
                            if (b) {
                                this.a();
                            }
                            return;
                        }
                        break Label_0160;
                    }
                }
                continue;
            }
        }
    }
    
    public final boolean a(final String s, final String s2) {
        final List<String> value = this.c.get(s2);
        boolean remove = false;
        if (value != null) {
            this.e(s);
            remove = value.remove(s);
        }
        if (value != null && !value.isEmpty()) {
            this.c.put(s2, value);
            this.a(s2, value, ".YFlurrySenderIndex.info.");
        }
        else {
            this.f(s2);
        }
        return remove;
    }
}
