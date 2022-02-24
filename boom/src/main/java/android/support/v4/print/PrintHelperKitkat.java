// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.print;

import android.os.CancellationSignal$OnCancelListener;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo$Builder;
import android.os.Bundle;
import android.print.PrintDocumentAdapter$LayoutResultCallback;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes$MediaSize;
import android.print.PrintManager;
import android.print.PrintAttributes$Builder;
import android.print.PageRange;
import android.os.AsyncTask;
import android.print.PrintAttributes$Margins;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.print.PrintDocumentAdapter$WriteResultCallback;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.graphics.Bitmap;
import android.print.PrintAttributes;
import android.graphics.BitmapFactory$Options;
import android.content.Context;

class PrintHelperKitkat
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    BitmapFactory$Options mDecodeOptions;
    protected boolean mIsMinMarginsHandlingCorrect;
    private final Object mLock;
    int mOrientation;
    protected boolean mPrintActivityRespectsOrientation;
    int mScaleMode;
    
    PrintHelperKitkat(final Context mContext) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = 2;
        this.mColorMode = 2;
        this.mPrintActivityRespectsOrientation = true;
        this.mIsMinMarginsHandlingCorrect = true;
        this.mContext = mContext;
    }
    
    private Bitmap convertBitmapForColorMode(Bitmap bitmap, final int n) {
        if (n == 1) {
            final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap2);
            final Paint paint = new Paint();
            final ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            canvas.setBitmap((Bitmap)null);
            bitmap = bitmap2;
        }
        return bitmap;
    }
    
    private Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
        final Matrix matrix = new Matrix();
        final float n4 = rectF.width() / n;
        float n5;
        if (n3 == 2) {
            n5 = Math.max(n4, rectF.height() / n2);
        }
        else {
            n5 = Math.min(n4, rectF.height() / n2);
        }
        matrix.postScale(n5, n5);
        matrix.postTranslate((rectF.width() - n * n5) / 2.0f, (rectF.height() - n2 * n5) / 2.0f);
        return matrix;
    }
    
    private static boolean isPortrait(final Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }
    
    private Bitmap loadBitmap(final Uri uri, BitmapFactory$Options decodeStream) throws FileNotFoundException {
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream openInputStream = null;
        try {
            final InputStream inputStream = openInputStream = this.mContext.getContentResolver().openInputStream(uri);
            decodeStream = (BitmapFactory$Options)BitmapFactory.decodeStream(inputStream, (Rect)null, decodeStream);
            if (inputStream == null) {
                return (Bitmap)decodeStream;
            }
            try {
                inputStream.close();
                return (Bitmap)decodeStream;
            }
            catch (IOException ex) {
                Log.w("PrintHelperKitkat", "close fail ", (Throwable)ex);
            }
        }
        finally {
            Label_0076: {
                if (openInputStream == null) {
                    break Label_0076;
                }
                try {
                    openInputStream.close();
                }
                catch (IOException ex2) {
                    Log.w("PrintHelperKitkat", "close fail ", (Throwable)ex2);
                }
            }
        }
    }
    
    private Bitmap loadConstrainedBitmap(Uri uri, final int n) throws FileNotFoundException {
        final Bitmap bitmap = null;
        if (n <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        this.loadBitmap(uri, bitmapFactory$Options);
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        Bitmap bitmap2 = bitmap;
        if (outWidth > 0) {
            if (outHeight <= 0) {
                bitmap2 = bitmap;
            }
            else {
                int i;
                int inSampleSize;
                for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > n; i >>>= 1, inSampleSize <<= 1) {}
                bitmap2 = bitmap;
                if (inSampleSize > 0) {
                    bitmap2 = bitmap;
                    if (Math.min(outWidth, outHeight) / inSampleSize > 0) {
                        final Object mLock = this.mLock;
                        final BitmapFactory$Options mDecodeOptions;
                        synchronized (mLock) {
                            this.mDecodeOptions = new BitmapFactory$Options();
                            this.mDecodeOptions.inMutable = true;
                            this.mDecodeOptions.inSampleSize = inSampleSize;
                            mDecodeOptions = this.mDecodeOptions;
                            // monitorexit(mLock)
                            final PrintHelperKitkat printHelperKitkat = this;
                            final Uri uri2 = uri;
                            final BitmapFactory$Options bitmapFactory$Options2 = mDecodeOptions;
                            printHelperKitkat.loadBitmap(uri2, bitmapFactory$Options2);
                            final PrintHelperKitkat printHelperKitkat2 = this;
                            final Object o = printHelperKitkat2.mLock;
                            final Object o2;
                            uri = (Uri)(o2 = o);
                            synchronized (o2) {
                                final PrintHelperKitkat printHelperKitkat3 = this;
                                final BitmapFactory$Options bitmapFactory$Options3 = null;
                                printHelperKitkat3.mDecodeOptions = bitmapFactory$Options3;
                            }
                        }
                        try {
                            final PrintHelperKitkat printHelperKitkat = this;
                            final Uri uri2 = uri;
                            final BitmapFactory$Options bitmapFactory$Options2 = mDecodeOptions;
                            printHelperKitkat.loadBitmap(uri2, bitmapFactory$Options2);
                            final PrintHelperKitkat printHelperKitkat2 = this;
                            final Object o = printHelperKitkat2.mLock;
                            final Object o2;
                            uri = (Uri)(o2 = o);
                            // monitorenter(o2)
                            final PrintHelperKitkat printHelperKitkat3 = this;
                            final BitmapFactory$Options bitmapFactory$Options3 = null;
                            printHelperKitkat3.mDecodeOptions = bitmapFactory$Options3;
                        }
                        finally {
                            synchronized (this.mLock) {
                                this.mDecodeOptions = null;
                            }
                            // monitorexit(this.mLock)
                        }
                    }
                }
            }
        }
        return bitmap2;
    }
    
    private void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
        PrintAttributes build;
        if (this.mIsMinMarginsHandlingCorrect) {
            build = printAttributes;
        }
        else {
            build = this.copyAttributes(printAttributes).setMinMargins(new PrintAttributes$Margins(0, 0, 0, 0)).build();
        }
        new AsyncTask<Void, Void, Throwable>() {
            protected Throwable doInBackground(final Void... p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: astore_2       
                //     2: aload_0        
                //     3: getfield        android/support/v4/print/PrintHelperKitkat$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                //     6: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //     9: ifeq            16
                //    12: aload_2        
                //    13: astore_1       
                //    14: aload_1        
                //    15: areturn        
                //    16: new             Landroid/print/pdf/PrintedPdfDocument;
                //    19: astore_3       
                //    20: aload_3        
                //    21: aload_0        
                //    22: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //    25: getfield        android/support/v4/print/PrintHelperKitkat.mContext:Landroid/content/Context;
                //    28: aload_0        
                //    29: getfield        android/support/v4/print/PrintHelperKitkat$2.val$pdfAttributes:Landroid/print/PrintAttributes;
                //    32: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                //    35: aload_0        
                //    36: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //    39: aload_0        
                //    40: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //    43: aload_0        
                //    44: getfield        android/support/v4/print/PrintHelperKitkat$2.val$pdfAttributes:Landroid/print/PrintAttributes;
                //    47: invokevirtual   android/print/PrintAttributes.getColorMode:()I
                //    50: invokestatic    android/support/v4/print/PrintHelperKitkat.access$100:(Landroid/support/v4/print/PrintHelperKitkat;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
                //    53: astore          4
                //    55: aload_0        
                //    56: getfield        android/support/v4/print/PrintHelperKitkat$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                //    59: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //    62: istore          5
                //    64: aload_2        
                //    65: astore_1       
                //    66: iload           5
                //    68: ifne            14
                //    71: aload_3        
                //    72: iconst_1       
                //    73: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                //    76: astore          6
                //    78: aload_0        
                //    79: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //    82: getfield        android/support/v4/print/PrintHelperKitkat.mIsMinMarginsHandlingCorrect:Z
                //    85: ifeq            216
                //    88: new             Landroid/graphics/RectF;
                //    91: astore_1       
                //    92: aload_1        
                //    93: aload           6
                //    95: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                //    98: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                //   101: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                //   104: aload_0        
                //   105: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //   108: aload           4
                //   110: invokevirtual   android/graphics/Bitmap.getWidth:()I
                //   113: aload           4
                //   115: invokevirtual   android/graphics/Bitmap.getHeight:()I
                //   118: aload_1        
                //   119: aload_0        
                //   120: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fittingMode:I
                //   123: invokestatic    android/support/v4/print/PrintHelperKitkat.access$200:(Landroid/support/v4/print/PrintHelperKitkat;IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
                //   126: astore          7
                //   128: aload_0        
                //   129: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //   132: getfield        android/support/v4/print/PrintHelperKitkat.mIsMinMarginsHandlingCorrect:Z
                //   135: ifeq            313
                //   138: aload           6
                //   140: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                //   143: aload           4
                //   145: aload           7
                //   147: aconst_null    
                //   148: invokevirtual   android/graphics/Canvas.drawBitmap:(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
                //   151: aload_3        
                //   152: aload           6
                //   154: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                //   157: aload_0        
                //   158: getfield        android/support/v4/print/PrintHelperKitkat$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                //   161: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //   164: istore          5
                //   166: iload           5
                //   168: ifeq            340
                //   171: aload_3        
                //   172: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   175: aload_0        
                //   176: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   179: astore_1       
                //   180: aload_1        
                //   181: ifnull          191
                //   184: aload_0        
                //   185: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   188: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   191: aload_2        
                //   192: astore_1       
                //   193: aload           4
                //   195: aload_0        
                //   196: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //   199: if_acmpeq       14
                //   202: aload           4
                //   204: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   207: aload_2        
                //   208: astore_1       
                //   209: goto            14
                //   212: astore_1       
                //   213: goto            14
                //   216: new             Landroid/print/pdf/PrintedPdfDocument;
                //   219: astore          7
                //   221: aload           7
                //   223: aload_0        
                //   224: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //   227: getfield        android/support/v4/print/PrintHelperKitkat.mContext:Landroid/content/Context;
                //   230: aload_0        
                //   231: getfield        android/support/v4/print/PrintHelperKitkat$2.val$attributes:Landroid/print/PrintAttributes;
                //   234: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                //   237: aload           7
                //   239: iconst_1       
                //   240: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                //   243: astore          8
                //   245: new             Landroid/graphics/RectF;
                //   248: astore_1       
                //   249: aload_1        
                //   250: aload           8
                //   252: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                //   255: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                //   258: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                //   261: aload           7
                //   263: aload           8
                //   265: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                //   268: aload           7
                //   270: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   273: goto            104
                //   276: astore_1       
                //   277: aload_3        
                //   278: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   281: aload_0        
                //   282: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   285: astore_2       
                //   286: aload_2        
                //   287: ifnull          297
                //   290: aload_0        
                //   291: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   294: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   297: aload           4
                //   299: aload_0        
                //   300: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //   303: if_acmpeq       311
                //   306: aload           4
                //   308: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   311: aload_1        
                //   312: athrow         
                //   313: aload           7
                //   315: aload_1        
                //   316: getfield        android/graphics/RectF.left:F
                //   319: aload_1        
                //   320: getfield        android/graphics/RectF.top:F
                //   323: invokevirtual   android/graphics/Matrix.postTranslate:(FF)Z
                //   326: pop            
                //   327: aload           6
                //   329: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                //   332: aload_1        
                //   333: invokevirtual   android/graphics/Canvas.clipRect:(Landroid/graphics/RectF;)Z
                //   336: pop            
                //   337: goto            138
                //   340: new             Ljava/io/FileOutputStream;
                //   343: astore_1       
                //   344: aload_1        
                //   345: aload_0        
                //   346: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   349: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
                //   352: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/FileDescriptor;)V
                //   355: aload_3        
                //   356: aload_1        
                //   357: invokevirtual   android/print/pdf/PrintedPdfDocument.writeTo:(Ljava/io/OutputStream;)V
                //   360: aload_3        
                //   361: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   364: aload_0        
                //   365: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   368: astore_1       
                //   369: aload_1        
                //   370: ifnull          380
                //   373: aload_0        
                //   374: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   377: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   380: aload_2        
                //   381: astore_1       
                //   382: aload           4
                //   384: aload_0        
                //   385: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //   388: if_acmpeq       14
                //   391: aload           4
                //   393: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   396: aload_2        
                //   397: astore_1       
                //   398: goto            14
                //   401: astore_2       
                //   402: goto            297
                //   405: astore_1       
                //   406: goto            380
                //   409: astore_1       
                //   410: goto            191
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  2      12     212    216    Ljava/lang/Throwable;
                //  16     64     212    216    Ljava/lang/Throwable;
                //  71     104    276    313    Any
                //  104    138    276    313    Any
                //  138    166    276    313    Any
                //  171    180    212    216    Ljava/lang/Throwable;
                //  184    191    409    413    Ljava/io/IOException;
                //  184    191    212    216    Ljava/lang/Throwable;
                //  193    207    212    216    Ljava/lang/Throwable;
                //  216    273    276    313    Any
                //  277    286    212    216    Ljava/lang/Throwable;
                //  290    297    401    405    Ljava/io/IOException;
                //  290    297    212    216    Ljava/lang/Throwable;
                //  297    311    212    216    Ljava/lang/Throwable;
                //  311    313    212    216    Ljava/lang/Throwable;
                //  313    337    276    313    Any
                //  340    360    276    313    Any
                //  360    369    212    216    Ljava/lang/Throwable;
                //  373    380    405    409    Ljava/io/IOException;
                //  373    380    212    216    Ljava/lang/Throwable;
                //  382    396    212    216    Ljava/lang/Throwable;
                // 
                // The error that occurred was:
                // 
                // java.lang.IndexOutOfBoundsException: Index 199 out of bounds for length 199
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
            
            protected void onPostExecute(final Throwable t) {
                if (cancellationSignal.isCanceled()) {
                    printDocumentAdapter$WriteResultCallback.onWriteCancelled();
                }
                else if (t == null) {
                    printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                }
                else {
                    Log.e("PrintHelperKitkat", "Error writing printed content", t);
                    printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
                }
            }
        }.execute((Object[])new Void[0]);
    }
    
    protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
        final PrintAttributes$Builder setMinMargins = new PrintAttributes$Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            setMinMargins.setColorMode(printAttributes.getColorMode());
        }
        return setMinMargins;
    }
    
    public int getColorMode() {
        return this.mColorMode;
    }
    
    public int getOrientation() {
        int mOrientation;
        if (this.mOrientation == 0) {
            mOrientation = 1;
        }
        else {
            mOrientation = this.mOrientation;
        }
        return mOrientation;
    }
    
    public int getScaleMode() {
        return this.mScaleMode;
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        if (bitmap != null) {
            final int mScaleMode = this.mScaleMode;
            final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
            PrintAttributes$MediaSize mediaSize;
            if (isPortrait(bitmap)) {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
            }
            else {
                mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(s, (PrintDocumentAdapter)new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;
                
                public void onFinish() {
                    if (onPrintFinishCallback != null) {
                        onPrintFinishCallback.onFinish();
                    }
                }
                
                public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                    boolean b = true;
                    this.mAttributes = mAttributes;
                    final PrintDocumentInfo build = new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build();
                    if (mAttributes.equals((Object)printAttributes)) {
                        b = false;
                    }
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(build, b);
                }
                
                public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                    PrintHelperKitkat.this.writeBitmap(this.mAttributes, mScaleMode, bitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
                }
            }, new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        final PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            Bitmap mBitmap = null;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
            final /* synthetic */ int val$fittingMode = PrintHelperKitkat.this.mScaleMode;
            
            private void cancelLoad() {
                synchronized (PrintHelperKitkat.this.mLock) {
                    if (PrintHelperKitkat.this.mDecodeOptions != null) {
                        PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                        PrintHelperKitkat.this.mDecodeOptions = null;
                    }
                }
            }
            
            public void onFinish() {
                super.onFinish();
                this.cancelLoad();
                if (this.mLoadBitmap != null) {
                    this.mLoadBitmap.cancel(true);
                }
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                    this.mBitmap = null;
                }
            }
            
            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, CancellationSignal build, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                while (true) {
                    boolean b = true;
                    synchronized (this) {
                        this.mAttributes = mAttributes;
                        // monitorexit(this)
                        if (build.isCanceled()) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                            return;
                        }
                    }
                    final PrintAttributes printAttributes2;
                    if (this.mBitmap != null) {
                        build = (CancellationSignal)new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build();
                        if (mAttributes.equals((Object)printAttributes2)) {
                            b = false;
                        }
                        printDocumentAdapter$LayoutResultCallback.onLayoutFinished((PrintDocumentInfo)build, b);
                        return;
                    }
                    this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>() {
                        protected Bitmap doInBackground(final Uri... array) {
                            try {
                                return PrintHelperKitkat.this.loadConstrainedBitmap(uri, 3500);
                            }
                            catch (FileNotFoundException ex) {
                                return null;
                            }
                        }
                        
                        protected void onCancelled(final Bitmap bitmap) {
                            printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                            PrintDocumentAdapter.this.mLoadBitmap = null;
                        }
                        
                        protected void onPostExecute(final Bitmap bitmap) {
                            super.onPostExecute((Object)bitmap);
                            Bitmap bitmap2 = bitmap;
                            Label_0105: {
                                if (bitmap == null) {
                                    break Label_0105;
                                }
                                if (PrintHelperKitkat.this.mPrintActivityRespectsOrientation) {
                                    bitmap2 = bitmap;
                                    if (PrintHelperKitkat.this.mOrientation != 0) {
                                        break Label_0105;
                                    }
                                }
                            Label_0170_Outer:
                                while (true) {
                                    while (true) {
                                        Label_0190: {
                                            while (true) {
                                                synchronized (this) {
                                                    final PrintAttributes$MediaSize mediaSize = PrintDocumentAdapter.this.mAttributes.getMediaSize();
                                                    // monitorexit(this)
                                                    bitmap2 = bitmap;
                                                    if (mediaSize != null) {
                                                        bitmap2 = bitmap;
                                                        if (mediaSize.isPortrait() != isPortrait(bitmap)) {
                                                            final Matrix matrix = new Matrix();
                                                            matrix.postRotate(90.0f);
                                                            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                                        }
                                                    }
                                                    if ((PrintDocumentAdapter.this.mBitmap = bitmap2) == null) {
                                                        break Label_0190;
                                                    }
                                                    final PrintDocumentInfo build = new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build();
                                                    if (!mAttributes.equals((Object)printAttributes2)) {
                                                        final boolean b = true;
                                                        printDocumentAdapter$LayoutResultCallback.onLayoutFinished(build, b);
                                                        PrintDocumentAdapter.this.mLoadBitmap = null;
                                                        return;
                                                    }
                                                }
                                                final boolean b = false;
                                                continue Label_0170_Outer;
                                            }
                                        }
                                        printDocumentAdapter$LayoutResultCallback.onLayoutFailed((CharSequence)null);
                                        continue;
                                    }
                                }
                            }
                        }
                        
                        protected void onPreExecute() {
                            build.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener() {
                                public void onCancel() {
                                    PrintHelperKitkat$3.this.cancelLoad();
                                    AsyncTask.this.cancel(false);
                                }
                            });
                        }
                    }.execute((Object[])new Uri[0]);
                }
            }
            
            public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                PrintHelperKitkat.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
            }
        };
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
        printAttributes$Builder.setColorMode(this.mColorMode);
        if (this.mOrientation == 1 || this.mOrientation == 0) {
            printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE);
        }
        else if (this.mOrientation == 2) {
            printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(s, (PrintDocumentAdapter)printDocumentAdapter, printAttributes$Builder.build());
    }
    
    public void setColorMode(final int mColorMode) {
        this.mColorMode = mColorMode;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }
    
    public void setScaleMode(final int mScaleMode) {
        this.mScaleMode = mScaleMode;
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
}
