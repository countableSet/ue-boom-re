// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.sdk;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.List;
import java.net.HttpURLConnection;

public class ku extends mb
{
    static final String e;
    private kt A;
    private int a;
    private int b;
    private final kd<String, String> c;
    private HttpURLConnection d;
    public String f;
    public a g;
    public int h;
    public int i;
    public boolean j;
    public c k;
    public boolean l;
    long m;
    public long n;
    public Exception o;
    public int p;
    public final kd<String, String> q;
    public boolean r;
    public int s;
    public boolean t;
    private boolean x;
    private boolean y;
    private final Object z;
    
    static {
        e = ku.class.getSimpleName();
    }
    
    public ku() {
        this.h = 10000;
        this.i = 15000;
        this.j = true;
        this.c = new kd<String, String>();
        this.m = -1L;
        this.n = -1L;
        this.p = -1;
        this.q = new kd<String, String>();
        this.z = new Object();
        this.s = 25000;
        this.A = new kt(this);
    }
    
    private void i() throws Exception {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aconst_null    
        //     3: astore_2       
        //     4: aload_0        
        //     5: getfield        com/flurry/sdk/ku.y:Z
        //     8: ifeq            12
        //    11: return         
        //    12: aload_0        
        //    13: aload_0        
        //    14: getfield        com/flurry/sdk/ku.f:Ljava/lang/String;
        //    17: invokestatic    com/flurry/sdk/ly.a:(Ljava/lang/String;)Ljava/lang/String;
        //    20: putfield        com/flurry/sdk/ku.f:Ljava/lang/String;
        //    23: new             Ljava/net/URL;
        //    26: dup            
        //    27: aload_0        
        //    28: getfield        com/flurry/sdk/ku.f:Ljava/lang/String;
        //    31: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    34: astore_3       
        //    35: aload_0        
        //    36: aload_3        
        //    37: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    40: checkcast       Ljava/net/HttpURLConnection;
        //    43: putfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //    46: aload_0        
        //    47: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //    50: aload_0        
        //    51: getfield        com/flurry/sdk/ku.h:I
        //    54: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
        //    57: aload_0        
        //    58: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //    61: aload_0        
        //    62: getfield        com/flurry/sdk/ku.i:I
        //    65: invokevirtual   java/net/HttpURLConnection.setReadTimeout:(I)V
        //    68: aload_0        
        //    69: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //    72: aload_0        
        //    73: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //    76: invokevirtual   com/flurry/sdk/ku$a.toString:()Ljava/lang/String;
        //    79: invokevirtual   java/net/HttpURLConnection.setRequestMethod:(Ljava/lang/String;)V
        //    82: aload_0        
        //    83: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //    86: aload_0        
        //    87: getfield        com/flurry/sdk/ku.j:Z
        //    90: invokevirtual   java/net/HttpURLConnection.setInstanceFollowRedirects:(Z)V
        //    93: aload_0        
        //    94: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //    97: getstatic       com/flurry/sdk/ku$a.c:Lcom/flurry/sdk/ku$a;
        //   100: aload_0        
        //   101: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //   104: invokevirtual   com/flurry/sdk/ku$a.equals:(Ljava/lang/Object;)Z
        //   107: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //   110: aload_0        
        //   111: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   114: iconst_1       
        //   115: invokevirtual   java/net/HttpURLConnection.setDoInput:(Z)V
        //   118: aload_0        
        //   119: getfield        com/flurry/sdk/ku.c:Lcom/flurry/sdk/kd;
        //   122: invokevirtual   com/flurry/sdk/kd.b:()Ljava/util/Collection;
        //   125: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //   130: astore_3       
        //   131: aload_3        
        //   132: invokeinterface java/util/Iterator.hasNext:()Z
        //   137: ifeq            188
        //   140: aload_3        
        //   141: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   146: checkcast       Ljava/util/Map$Entry;
        //   149: astore          4
        //   151: aload_0        
        //   152: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   155: aload           4
        //   157: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   162: checkcast       Ljava/lang/String;
        //   165: aload           4
        //   167: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   172: checkcast       Ljava/lang/String;
        //   175: invokevirtual   java/net/HttpURLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   178: goto            131
        //   181: astore_3       
        //   182: aload_0        
        //   183: invokespecial   com/flurry/sdk/ku.j:()V
        //   186: aload_3        
        //   187: athrow         
        //   188: getstatic       com/flurry/sdk/ku$a.b:Lcom/flurry/sdk/ku$a;
        //   191: aload_0        
        //   192: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //   195: invokevirtual   com/flurry/sdk/ku$a.equals:(Ljava/lang/Object;)Z
        //   198: ifne            225
        //   201: getstatic       com/flurry/sdk/ku$a.c:Lcom/flurry/sdk/ku$a;
        //   204: aload_0        
        //   205: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //   208: invokevirtual   com/flurry/sdk/ku$a.equals:(Ljava/lang/Object;)Z
        //   211: ifne            225
        //   214: aload_0        
        //   215: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   218: ldc             "Accept-Encoding"
        //   220: ldc             ""
        //   222: invokevirtual   java/net/HttpURLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   225: aload_0        
        //   226: getfield        com/flurry/sdk/ku.y:Z
        //   229: istore          5
        //   231: iload           5
        //   233: ifeq            243
        //   236: aload_0        
        //   237: invokespecial   com/flurry/sdk/ku.j:()V
        //   240: goto            11
        //   243: getstatic       com/flurry/sdk/ku$a.c:Lcom/flurry/sdk/ku$a;
        //   246: aload_0        
        //   247: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //   250: invokevirtual   com/flurry/sdk/ku$a.equals:(Ljava/lang/Object;)Z
        //   253: istore          5
        //   255: iload           5
        //   257: ifeq            313
        //   260: aload_0        
        //   261: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   264: invokevirtual   java/net/HttpURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //   267: astore_3       
        //   268: new             Ljava/io/BufferedOutputStream;
        //   271: astore          4
        //   273: aload           4
        //   275: aload_3        
        //   276: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;)V
        //   279: aload_0        
        //   280: getfield        com/flurry/sdk/ku.k:Lcom/flurry/sdk/ku$c;
        //   283: ifnull          304
        //   286: aload_0        
        //   287: invokevirtual   com/flurry/sdk/ku.b:()Z
        //   290: ifne            304
        //   293: aload_0        
        //   294: getfield        com/flurry/sdk/ku.k:Lcom/flurry/sdk/ku$c;
        //   297: aload           4
        //   299: invokeinterface com/flurry/sdk/ku$c.a:(Ljava/io/OutputStream;)V
        //   304: aload           4
        //   306: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   309: aload_3        
        //   310: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   313: aload_0        
        //   314: getfield        com/flurry/sdk/ku.l:Z
        //   317: ifeq            327
        //   320: aload_0        
        //   321: invokestatic    java/lang/System.currentTimeMillis:()J
        //   324: putfield        com/flurry/sdk/ku.m:J
        //   327: aload_0        
        //   328: getfield        com/flurry/sdk/ku.r:Z
        //   331: ifeq            346
        //   334: aload_0        
        //   335: getfield        com/flurry/sdk/ku.A:Lcom/flurry/sdk/kt;
        //   338: aload_0        
        //   339: getfield        com/flurry/sdk/ku.s:I
        //   342: i2l            
        //   343: invokevirtual   com/flurry/sdk/kt.a:(J)V
        //   346: aload_0        
        //   347: aload_0        
        //   348: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   351: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   354: putfield        com/flurry/sdk/ku.p:I
        //   357: aload_0        
        //   358: getfield        com/flurry/sdk/ku.l:Z
        //   361: ifeq            387
        //   364: aload_0        
        //   365: getfield        com/flurry/sdk/ku.m:J
        //   368: ldc2_w          -1
        //   371: lcmp           
        //   372: ifeq            387
        //   375: aload_0        
        //   376: invokestatic    java/lang/System.currentTimeMillis:()J
        //   379: aload_0        
        //   380: getfield        com/flurry/sdk/ku.m:J
        //   383: lsub           
        //   384: putfield        com/flurry/sdk/ku.n:J
        //   387: aload_0        
        //   388: getfield        com/flurry/sdk/ku.A:Lcom/flurry/sdk/kt;
        //   391: invokevirtual   com/flurry/sdk/kt.a:()V
        //   394: aload_0        
        //   395: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   398: invokevirtual   java/net/HttpURLConnection.getHeaderFields:()Ljava/util/Map;
        //   401: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   406: invokeinterface java/util/Collection.iterator:()Ljava/util/Iterator;
        //   411: astore          6
        //   413: aload           6
        //   415: invokeinterface java/util/Iterator.hasNext:()Z
        //   420: ifeq            506
        //   423: aload           6
        //   425: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   430: checkcast       Ljava/util/Map$Entry;
        //   433: astore_3       
        //   434: aload_3        
        //   435: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   440: checkcast       Ljava/util/List;
        //   443: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   448: astore_2       
        //   449: aload_2        
        //   450: invokeinterface java/util/Iterator.hasNext:()Z
        //   455: ifeq            413
        //   458: aload_2        
        //   459: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   464: checkcast       Ljava/lang/String;
        //   467: astore          4
        //   469: aload_0        
        //   470: getfield        com/flurry/sdk/ku.q:Lcom/flurry/sdk/kd;
        //   473: aload_3        
        //   474: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   479: aload           4
        //   481: invokevirtual   com/flurry/sdk/kd.a:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   484: goto            449
        //   487: astore_3       
        //   488: aconst_null    
        //   489: astore          4
        //   491: aload_2        
        //   492: astore_1       
        //   493: aload           4
        //   495: astore_2       
        //   496: aload_2        
        //   497: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   500: aload_1        
        //   501: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   504: aload_3        
        //   505: athrow         
        //   506: getstatic       com/flurry/sdk/ku$a.b:Lcom/flurry/sdk/ku$a;
        //   509: aload_0        
        //   510: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //   513: invokevirtual   com/flurry/sdk/ku$a.equals:(Ljava/lang/Object;)Z
        //   516: ifne            543
        //   519: getstatic       com/flurry/sdk/ku$a.c:Lcom/flurry/sdk/ku$a;
        //   522: aload_0        
        //   523: getfield        com/flurry/sdk/ku.g:Lcom/flurry/sdk/ku$a;
        //   526: invokevirtual   com/flurry/sdk/ku$a.equals:(Ljava/lang/Object;)Z
        //   529: istore          5
        //   531: iload           5
        //   533: ifne            543
        //   536: aload_0        
        //   537: invokespecial   com/flurry/sdk/ku.j:()V
        //   540: goto            11
        //   543: aload_0        
        //   544: getfield        com/flurry/sdk/ku.y:Z
        //   547: istore          5
        //   549: iload           5
        //   551: ifeq            561
        //   554: aload_0        
        //   555: invokespecial   com/flurry/sdk/ku.j:()V
        //   558: goto            11
        //   561: aload_0        
        //   562: getfield        com/flurry/sdk/ku.d:Ljava/net/HttpURLConnection;
        //   565: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //   568: astore_3       
        //   569: new             Ljava/io/BufferedInputStream;
        //   572: astore          4
        //   574: aload           4
        //   576: aload_3        
        //   577: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;)V
        //   580: aload_0        
        //   581: getfield        com/flurry/sdk/ku.k:Lcom/flurry/sdk/ku$c;
        //   584: ifnull          606
        //   587: aload_0        
        //   588: invokevirtual   com/flurry/sdk/ku.b:()Z
        //   591: ifne            606
        //   594: aload_0        
        //   595: getfield        com/flurry/sdk/ku.k:Lcom/flurry/sdk/ku$c;
        //   598: aload_0        
        //   599: aload           4
        //   601: invokeinterface com/flurry/sdk/ku$c.a:(Lcom/flurry/sdk/ku;Ljava/io/InputStream;)V
        //   606: aload           4
        //   608: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   611: aload_3        
        //   612: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   615: aload_0        
        //   616: invokespecial   com/flurry/sdk/ku.j:()V
        //   619: goto            11
        //   622: astore_3       
        //   623: aconst_null    
        //   624: astore_2       
        //   625: aload_2        
        //   626: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   629: aload_1        
        //   630: invokestatic    com/flurry/sdk/ly.a:(Ljava/io/Closeable;)V
        //   633: aload_3        
        //   634: athrow         
        //   635: astore_2       
        //   636: aconst_null    
        //   637: astore          4
        //   639: aload_3        
        //   640: astore_1       
        //   641: aload_2        
        //   642: astore_3       
        //   643: aload           4
        //   645: astore_2       
        //   646: goto            625
        //   649: astore_2       
        //   650: aload_3        
        //   651: astore_1       
        //   652: aload_2        
        //   653: astore_3       
        //   654: aload           4
        //   656: astore_2       
        //   657: goto            625
        //   660: astore_2       
        //   661: aconst_null    
        //   662: astore          4
        //   664: aload_3        
        //   665: astore_1       
        //   666: aload_2        
        //   667: astore_3       
        //   668: aload           4
        //   670: astore_2       
        //   671: goto            496
        //   674: astore_2       
        //   675: aload_3        
        //   676: astore_1       
        //   677: aload_2        
        //   678: astore_3       
        //   679: aload           4
        //   681: astore_2       
        //   682: goto            496
        //    Exceptions:
        //  throws java.lang.Exception
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  35     131    181    188    Any
        //  131    178    181    188    Any
        //  188    225    181    188    Any
        //  225    231    181    188    Any
        //  243    255    181    188    Any
        //  260    268    487    496    Any
        //  268    279    660    674    Any
        //  279    304    674    685    Any
        //  304    313    181    188    Any
        //  313    327    181    188    Any
        //  327    346    181    188    Any
        //  346    387    181    188    Any
        //  387    413    181    188    Any
        //  413    449    181    188    Any
        //  449    484    181    188    Any
        //  496    506    181    188    Any
        //  506    531    181    188    Any
        //  543    549    181    188    Any
        //  561    569    622    625    Any
        //  569    580    635    649    Any
        //  580    606    649    660    Any
        //  606    615    181    188    Any
        //  625    635    181    188    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 313 out of bounds for length 313
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3435)
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
    
    private void j() {
        if (!this.x) {
            this.x = true;
            if (this.d != null) {
                this.d.disconnect();
            }
        }
    }
    
    public final List<String> a(final String s) {
        return this.q.a(s);
    }
    
    @Override
    public void a() {
        try {
            if (this.f != null) {
                if (!jr.a().b) {
                    km.a(3, ku.e, "Network not available, aborting http request: " + this.f);
                    this.A.a();
                    this.h();
                }
                else {
                    if (this.g == null || ku.a.a.equals(this.g)) {
                        this.g = ku.a.b;
                    }
                    this.i();
                    km.a(4, ku.e, "HTTP status: " + this.p + " for url: " + this.f);
                    this.A.a();
                    this.h();
                }
            }
        }
        catch (Exception o) {
            km.a(4, ku.e, "HTTP status: " + this.p + " for url: " + this.f);
            km.a(3, ku.e, "Exception during http request: " + this.f, o);
            this.b = this.d.getReadTimeout();
            this.a = this.d.getConnectTimeout();
            this.o = o;
            this.A.a();
            this.h();
        }
        finally {
            this.A.a();
            this.h();
        }
    }
    
    public final void a(final String s, final String s2) {
        this.c.a(s, s2);
    }
    
    public final boolean b() {
        synchronized (this.z) {
            return this.y;
        }
    }
    
    public final boolean c() {
        return !this.e() && this.d();
    }
    
    public final boolean d() {
        return this.p >= 200 && this.p < 400 && !this.t;
    }
    
    public final boolean e() {
        return this.o != null;
    }
    
    public final void f() {
        km.a(3, ku.e, "Cancelling http request: " + this.f);
        synchronized (this.z) {
            this.y = true;
            // monitorexit(this.z)
            if (!this.x) {
                this.x = true;
                if (this.d != null) {
                    new Thread() {
                        @Override
                        public final void run() {
                            try {
                                if (ku.this.d != null) {
                                    ku.this.d.disconnect();
                                }
                            }
                            catch (Throwable t) {}
                        }
                    }.start();
                }
            }
        }
    }
    
    @Override
    public final void g() {
        this.f();
    }
    
    final void h() {
        if (this.k != null && !this.b()) {
            this.k.a(this);
        }
    }
    
    public enum a
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f;
        
        @Override
        public final String toString() {
            String s = null;
            switch (ku$2.a[this.ordinal()]) {
                default: {
                    s = null;
                    break;
                }
                case 1: {
                    s = "POST";
                    break;
                }
                case 2: {
                    s = "PUT";
                    break;
                }
                case 3: {
                    s = "DELETE";
                    break;
                }
                case 4: {
                    s = "HEAD";
                    break;
                }
                case 5: {
                    s = "GET";
                    break;
                }
            }
            return s;
        }
    }
    
    public static class b implements c
    {
        @Override
        public void a(final ku ku) {
        }
        
        @Override
        public void a(final ku ku, final InputStream inputStream) throws Exception {
        }
        
        @Override
        public final void a(final OutputStream outputStream) throws Exception {
        }
    }
    
    public interface c
    {
        void a(final ku p0);
        
        void a(final ku p0, final InputStream p1) throws Exception;
        
        void a(final OutputStream p0) throws Exception;
    }
}
