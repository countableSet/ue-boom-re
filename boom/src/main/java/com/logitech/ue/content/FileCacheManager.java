// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.content;

import java.io.IOException;
import java.io.FileOutputStream;
import android.content.Context;
import java.io.File;

public class FileCacheManager
{
    private static volatile FileCacheManager instance;
    private final File cacheDir;
    final Context context;
    
    static {
        FileCacheManager.instance = null;
    }
    
    private FileCacheManager(final Context context) {
        this.context = context;
        this.cacheDir = context.getFilesDir();
        if (!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }
    }
    
    public static FileCacheManager getInstance(final Context context) {
        Label_0031: {
            if (FileCacheManager.instance != null) {
                break Label_0031;
            }
            synchronized (FileCacheManager.class) {
                if (FileCacheManager.instance == null) {
                    FileCacheManager.instance = new FileCacheManager(context);
                }
                return FileCacheManager.instance;
            }
        }
    }
    
    public void cacheDataToFile(final String s, final byte[] b) throws IOException {
        if (b != null) {
            final FileOutputStream fileOutputStream = new FileOutputStream(this.getFile(s));
            fileOutputStream.write(b);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }
    
    public void clear() {
        final File[] listFiles = this.cacheDir.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                listFiles[i].delete();
            }
        }
    }
    
    public File getFile(final String s) {
        return new File(this.cacheDir, String.valueOf(s.hashCode()));
    }
}
