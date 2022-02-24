// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import android.os.AsyncTask;

class DownloadManifestTask extends AsyncTask<Void, Void, Object>
{
    private static final String TAG;
    private String mManifestURL;
    
    static {
        TAG = DownloadManifestTask.class.getSimpleName();
    }
    
    DownloadManifestTask(final String mManifestURL) {
        this.mManifestURL = mManifestURL;
    }
    
    public static void copyStream(final InputStream inputStream, final OutputStream outputStream) {
        try {
            final byte[] array = new byte[131072];
            while (true) {
                final int read = inputStream.read(array, 0, 131072);
                if (read == -1) {
                    break;
                }
                outputStream.write(array, 0, read);
            }
        }
        catch (IOException ex) {}
    }
    
    public static String readStreamAsString(InputStream inputStream, final String charsetName) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, byteArrayOutputStream);
        final InputStream inputStream2 = null;
        try {
            inputStream = (InputStream)new String(byteArrayOutputStream.toByteArray(), charsetName);
            try {
                byteArrayOutputStream.close();
                return (String)inputStream;
            }
            catch (IOException ex2) {}
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            try {
                byteArrayOutputStream.close();
                inputStream = inputStream2;
                return (String)inputStream;
            }
            catch (IOException ex3) {
                inputStream = inputStream2;
                return (String)inputStream;
            }
            return (String)inputStream;
        }
        finally {
            try {
                byteArrayOutputStream.close();
            }
            catch (IOException ex4) {}
        }
    }
    
    protected Object doInBackground(final Void... p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore_3       
        //     4: aload_3        
        //     5: astore_1       
        //     6: aload_2        
        //     7: astore          4
        //     9: getstatic       com/logitech/ue/manifest/DownloadManifestTask.TAG:Ljava/lang/String;
        //    12: ldc             "Begin manifest synchronization"
        //    14: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    17: pop            
        //    18: aload_3        
        //    19: astore_1       
        //    20: aload_2        
        //    21: astore          4
        //    23: new             Ljava/net/URL;
        //    26: astore          5
        //    28: aload_3        
        //    29: astore_1       
        //    30: aload_2        
        //    31: astore          4
        //    33: aload           5
        //    35: aload_0        
        //    36: getfield        com/logitech/ue/manifest/DownloadManifestTask.mManifestURL:Ljava/lang/String;
        //    39: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    42: aload_3        
        //    43: astore_1       
        //    44: aload_2        
        //    45: astore          4
        //    47: aload           5
        //    49: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    52: astore          5
        //    54: aload_3        
        //    55: astore_1       
        //    56: aload_2        
        //    57: astore          4
        //    59: getstatic       com/logitech/ue/manifest/DownloadManifestTask.TAG:Ljava/lang/String;
        //    62: ldc             "Opening manifest connection"
        //    64: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    67: pop            
        //    68: aload_3        
        //    69: astore_1       
        //    70: aload_2        
        //    71: astore          4
        //    73: aload           5
        //    75: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //    78: astore_3       
        //    79: aload_3        
        //    80: astore_1       
        //    81: aload_3        
        //    82: astore          4
        //    84: getstatic       com/logitech/ue/manifest/DownloadManifestTask.TAG:Ljava/lang/String;
        //    87: ldc             "Loading manifest"
        //    89: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //    92: pop            
        //    93: aload_3        
        //    94: astore_1       
        //    95: aload_3        
        //    96: astore          4
        //    98: aload_3        
        //    99: ldc             "UTF-8"
        //   101: invokestatic    com/logitech/ue/manifest/DownloadManifestTask.readStreamAsString:(Ljava/io/InputStream;Ljava/lang/String;)Ljava/lang/String;
        //   104: astore_2       
        //   105: aload_3        
        //   106: astore_1       
        //   107: aload_3        
        //   108: astore          4
        //   110: getstatic       com/logitech/ue/manifest/DownloadManifestTask.TAG:Ljava/lang/String;
        //   113: ldc             "Building manifest:"
        //   115: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
        //   118: pop            
        //   119: aload_3        
        //   120: astore_1       
        //   121: aload_3        
        //   122: astore          4
        //   124: aload_2        
        //   125: invokestatic    com/logitech/ue/manifest/UEManifest.readFromXML:(Ljava/lang/String;)Lcom/logitech/ue/manifest/UEManifest;
        //   128: astore_2       
        //   129: aload_2        
        //   130: astore_1       
        //   131: aload_1        
        //   132: astore          4
        //   134: aload_3        
        //   135: ifnull          145
        //   138: aload_3        
        //   139: invokevirtual   java/io/InputStream.close:()V
        //   142: aload_1        
        //   143: astore          4
        //   145: aload           4
        //   147: areturn        
        //   148: astore_3       
        //   149: aload_1        
        //   150: astore          4
        //   152: getstatic       com/logitech/ue/manifest/DownloadManifestTask.TAG:Ljava/lang/String;
        //   155: ldc             "Manifest loading failed"
        //   157: aload_3        
        //   158: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   161: pop            
        //   162: aload_3        
        //   163: astore          4
        //   165: aload_1        
        //   166: ifnull          145
        //   169: aload_1        
        //   170: invokevirtual   java/io/InputStream.close:()V
        //   173: aload_3        
        //   174: astore          4
        //   176: goto            145
        //   179: astore_1       
        //   180: aload_3        
        //   181: astore          4
        //   183: goto            145
        //   186: astore_1       
        //   187: aload           4
        //   189: ifnull          197
        //   192: aload           4
        //   194: invokevirtual   java/io/InputStream.close:()V
        //   197: aload_1        
        //   198: athrow         
        //   199: astore          4
        //   201: aload_1        
        //   202: astore          4
        //   204: goto            145
        //   207: astore          4
        //   209: goto            197
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  9      18     148    186    Ljava/lang/Exception;
        //  9      18     186    199    Any
        //  23     28     148    186    Ljava/lang/Exception;
        //  23     28     186    199    Any
        //  33     42     148    186    Ljava/lang/Exception;
        //  33     42     186    199    Any
        //  47     54     148    186    Ljava/lang/Exception;
        //  47     54     186    199    Any
        //  59     68     148    186    Ljava/lang/Exception;
        //  59     68     186    199    Any
        //  73     79     148    186    Ljava/lang/Exception;
        //  73     79     186    199    Any
        //  84     93     148    186    Ljava/lang/Exception;
        //  84     93     186    199    Any
        //  98     105    148    186    Ljava/lang/Exception;
        //  98     105    186    199    Any
        //  110    119    148    186    Ljava/lang/Exception;
        //  110    119    186    199    Any
        //  124    129    148    186    Ljava/lang/Exception;
        //  124    129    186    199    Any
        //  138    142    199    207    Ljava/io/IOException;
        //  152    162    186    199    Any
        //  169    173    179    186    Ljava/io/IOException;
        //  192    197    207    212    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 125 out of bounds for length 125
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
}
