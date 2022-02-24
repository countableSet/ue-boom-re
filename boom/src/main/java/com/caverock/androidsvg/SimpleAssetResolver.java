// 
// Decompiled by Procyon v0.5.36
// 

package com.caverock.androidsvg;

import java.io.IOException;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.util.Log;
import android.graphics.Typeface;
import android.os.Build$VERSION;
import java.util.HashSet;
import android.content.res.AssetManager;
import java.util.Set;

public class SimpleAssetResolver extends SVGExternalFileResolver
{
    private static final String TAG;
    private static final Set<String> supportedFormats;
    private AssetManager assetManager;
    
    static {
        TAG = SimpleAssetResolver.class.getSimpleName();
        supportedFormats = new HashSet<String>(8);
    }
    
    public SimpleAssetResolver(final AssetManager assetManager) {
        SimpleAssetResolver.supportedFormats.add("image/svg+xml");
        SimpleAssetResolver.supportedFormats.add("image/jpeg");
        SimpleAssetResolver.supportedFormats.add("image/png");
        SimpleAssetResolver.supportedFormats.add("image/pjpeg");
        SimpleAssetResolver.supportedFormats.add("image/gif");
        SimpleAssetResolver.supportedFormats.add("image/bmp");
        SimpleAssetResolver.supportedFormats.add("image/x-windows-bmp");
        if (Build$VERSION.SDK_INT >= 14) {
            SimpleAssetResolver.supportedFormats.add("image/webp");
        }
        this.assetManager = assetManager;
    }
    
    @Override
    public boolean isFormatSupported(final String s) {
        return SimpleAssetResolver.supportedFormats.contains(s);
    }
    
    @Override
    public Typeface resolveFont(String obj, final int i, final String str) {
        Log.i(SimpleAssetResolver.TAG, "resolveFont(" + (String)obj + "," + i + "," + str + ")");
        try {
            obj = Typeface.createFromAsset(this.assetManager, String.valueOf(obj) + ".ttf");
            return (Typeface)obj;
        }
        catch (Exception ex) {
            try {
                obj = Typeface.createFromAsset(this.assetManager, String.valueOf(obj) + ".otf");
            }
            catch (Exception ex2) {
                obj = null;
            }
        }
    }
    
    @Override
    public Bitmap resolveImage(final String str) {
        Log.i(SimpleAssetResolver.TAG, "resolveImage(" + str + ")");
        try {
            return BitmapFactory.decodeStream(this.assetManager.open(str));
        }
        catch (IOException ex) {
            return null;
        }
    }
}
