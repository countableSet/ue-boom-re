// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import com.logitech.ue.Utils;
import com.logitech.ue.App;
import android.os.AsyncTask;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.caverock.androidsvg.SVG;
import android.support.v4.util.LruCache;

public class SVGRenderCacher
{
    private static final int CACHE_SIZE = 16777216;
    private static final LruCache<SVG, Drawable> sSVGCache;
    
    static {
        sSVGCache = new LruCache<SVG, Drawable>(16777216) {
            @Override
            protected void entryRemoved(final boolean b, final SVG svg, final Drawable drawable, final Drawable drawable2) {
                super.entryRemoved(b, svg, drawable, drawable2);
                if (drawable instanceof BitmapDrawable) {
                    ((BitmapDrawable)drawable).getBitmap().recycle();
                }
            }
            
            @Override
            protected int sizeOf(final SVG svg, final Drawable drawable) {
                int n;
                if (drawable instanceof BitmapDrawable) {
                    n = ((BitmapDrawable)drawable).getBitmap().getByteCount();
                }
                else {
                    n = super.sizeOf(svg, drawable);
                }
                return n;
            }
        };
    }
    
    public static Bitmap drawableToBitmap(final Drawable drawable, final Rect rect) {
        Label_0026: {
            if (!(drawable instanceof BitmapDrawable)) {
                break Label_0026;
            }
            final BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            if (bitmapDrawable.getBitmap() == null) {
                break Label_0026;
            }
            return bitmapDrawable.getBitmap();
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0 || rect.width() <= 0 || rect.height() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap$Config.ARGB_8888);
        }
        else {
            bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap$Config.ARGB_8888);
        }
        final float n = rect.width() / (float)drawable.getIntrinsicWidth();
        float n2 = rect.height() / (float)drawable.getIntrinsicHeight();
        if (n >= n2) {
            n2 = n;
        }
        final Matrix matrix = new Matrix();
        matrix.postScale(n2, n2);
        final Canvas canvas = new Canvas(bitmap);
        canvas.setMatrix(matrix);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
    public static Drawable getDrawable(final SVG svg) {
        Drawable renderSVG;
        if ((renderSVG = SVGRenderCacher.sSVGCache.get(svg)) == null) {
            renderSVG = renderSVG(svg);
            SVGRenderCacher.sSVGCache.put(svg, renderSVG);
        }
        return renderSVG;
    }
    
    public static Drawable getDrawable(final SVG svg, final Rect rect, final OnSVGReady onSVGReady) {
        final Drawable svgFromCache = getSVGFromCache(svg);
        if (svgFromCache == null) {
            new AsyncTask<Void, Void, Drawable>() {
                protected Drawable doInBackground(final Void... array) {
                    final BitmapDrawable bitmapDrawable = new BitmapDrawable(App.getInstance().getResources(), SVGRenderCacher.drawableToBitmap(renderSVG(svg), rect));
                    SVGRenderCacher.putSVGToCache(svg, (Drawable)bitmapDrawable);
                    return (Drawable)bitmapDrawable;
                }
                
                protected void onPostExecute(final Drawable drawable) {
                    super.onPostExecute((Object)drawable);
                    if (onSVGReady != null) {
                        onSVGReady.onReady(drawable);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
        return svgFromCache;
    }
    
    public static Drawable getSVGFromCache(final SVG svg) {
        synchronized (SVGRenderCacher.sSVGCache) {
            return SVGRenderCacher.sSVGCache.get(svg);
        }
    }
    
    public static void putSVGToCache(final SVG svg, final Drawable drawable) {
        synchronized (SVGRenderCacher.sSVGCache) {
            SVGRenderCacher.sSVGCache.put(svg, drawable);
        }
    }
    
    private static Drawable renderSVG(final SVG svg) {
        return (Drawable)new BitmapDrawable(App.getInstance().getResources(), Utils.drawSVGToBitmap(svg));
    }
    
    public interface OnSVGReady
    {
        void onReady(final Drawable p0);
    }
}
