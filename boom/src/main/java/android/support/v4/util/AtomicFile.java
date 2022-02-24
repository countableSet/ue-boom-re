// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.util;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import android.util.Log;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

public class AtomicFile
{
    private final File mBackupName;
    private final File mBaseName;
    
    public AtomicFile(final File mBaseName) {
        this.mBaseName = mBaseName;
        this.mBackupName = new File(mBaseName.getPath() + ".bak");
    }
    
    static boolean sync(final FileOutputStream fileOutputStream) {
        Label_0011: {
            if (fileOutputStream == null) {
                break Label_0011;
            }
            try {
                fileOutputStream.getFD().sync();
                return true;
            }
            catch (IOException ex) {
                return false;
            }
        }
    }
    
    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }
    
    public void failWrite(final FileOutputStream fileOutputStream) {
        if (fileOutputStream == null) {
            return;
        }
        sync(fileOutputStream);
        try {
            fileOutputStream.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        catch (IOException ex) {
            Log.w("AtomicFile", "failWrite: Got exception:", (Throwable)ex);
        }
    }
    
    public void finishWrite(final FileOutputStream fileOutputStream) {
        if (fileOutputStream == null) {
            return;
        }
        sync(fileOutputStream);
        try {
            fileOutputStream.close();
            this.mBackupName.delete();
        }
        catch (IOException ex) {
            Log.w("AtomicFile", "finishWrite: Got exception:", (Throwable)ex);
        }
    }
    
    public File getBaseFile() {
        return this.mBaseName;
    }
    
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }
    
    public byte[] readFully() throws IOException {
        final FileInputStream openRead = this.openRead();
        int off = 0;
        try {
            byte[] b = new byte[openRead.available()];
            while (true) {
                final int read = openRead.read(b, off, b.length - off);
                if (read <= 0) {
                    break;
                }
                final int n = off + read;
                final int available = openRead.available();
                off = n;
                if (available <= b.length - n) {
                    continue;
                }
                final byte[] array = new byte[n + available];
                System.arraycopy(b, 0, array, 0, n);
                b = array;
                off = n;
            }
            return b;
        }
        finally {
            openRead.close();
        }
    }
    
    public FileOutputStream startWrite() throws IOException {
        Label_0088: {
            if (this.mBaseName.exists()) {
                if (this.mBackupName.exists()) {
                    break Label_0088;
                }
                if (!this.mBaseName.renameTo(this.mBackupName)) {
                    Log.w("AtomicFile", "Couldn't rename file " + this.mBaseName + " to backup file " + this.mBackupName);
                }
            }
            try {
                return new FileOutputStream(this.mBaseName);
                this.mBaseName.delete();
                return new FileOutputStream(this.mBaseName);
            }
            catch (FileNotFoundException ex) {
                if (!this.mBaseName.getParentFile().mkdirs()) {
                    throw new IOException("Couldn't create directory " + this.mBaseName);
                }
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(this.mBaseName);
                }
                catch (FileNotFoundException ex2) {
                    throw new IOException("Couldn't create " + this.mBaseName);
                }
            }
        }
    }
}
