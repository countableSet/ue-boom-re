// 
// Decompiled by Procyon v0.5.36
// 

package com.jakewharton.disklrucache;

import java.util.Arrays;
import java.io.FilterOutputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.io.EOFException;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.io.Writer;
import java.util.concurrent.ThreadPoolExecutor;
import java.io.File;
import java.util.concurrent.Callable;
import java.io.OutputStream;
import java.util.regex.Pattern;
import java.io.Closeable;

public final class DiskLruCache implements Closeable
{
    static final long ANY_SEQUENCE_NUMBER = -1L;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final OutputStream NULL_OUTPUT_STREAM;
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;
    
    static {
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,64}");
        NULL_OUTPUT_STREAM = new OutputStream() {
            @Override
            public void write(final int n) throws IOException {
            }
        };
    }
    
    private DiskLruCache(final File file, final int appVersion, final int valueCount, final long maxSize) {
        this.size = 0L;
        this.lruEntries = new LinkedHashMap<String, Entry>(0, 0.75f, true);
        this.nextSequenceNumber = 0L;
        this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.cleanupCallable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                synchronized (DiskLruCache.this) {
                    if (DiskLruCache.this.journalWriter != null) {
                        DiskLruCache.this.trimToSize();
                        if (DiskLruCache.this.journalRebuildRequired()) {
                            DiskLruCache.this.rebuildJournal();
                            DiskLruCache.this.redundantOpCount = 0;
                        }
                    }
                    // monitorexit(this.this$0)
                    return null;
                }
            }
        };
        this.directory = file;
        this.appVersion = appVersion;
        this.journalFile = new File(file, "journal");
        this.journalFileTmp = new File(file, "journal.tmp");
        this.journalFileBackup = new File(file, "journal.bkp");
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }
    
    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }
    
    private void completeEdit(final Editor editor, final boolean b) throws IOException {
        final Entry access$1400;
        synchronized (this) {
            access$1400 = editor.entry;
            if (access$1400.currentEditor != editor) {
                throw new IllegalStateException();
            }
        }
    Label_0115:
        while (true) {
            if (b && !access$1400.readable) {
                for (int i = 0; i < this.valueCount; ++i) {
                    final Editor editor2;
                    if (!editor2.written[i]) {
                        editor2.abort();
                        throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                    }
                    if (!access$1400.getDirtyFile(i).exists()) {
                        editor2.abort();
                        break Label_0115;
                    }
                }
            }
            Label_0124: {
                break Label_0124;
                return;
            }
            for (int j = 0; j < this.valueCount; ++j) {
                final File dirtyFile = access$1400.getDirtyFile(j);
                if (b) {
                    if (dirtyFile.exists()) {
                        final File cleanFile = access$1400.getCleanFile(j);
                        dirtyFile.renameTo(cleanFile);
                        final long n = access$1400.lengths[j];
                        final long length = cleanFile.length();
                        access$1400.lengths[j] = length;
                        this.size = this.size - n + length;
                    }
                }
                else {
                    deleteIfExists(dirtyFile);
                }
            }
            ++this.redundantOpCount;
            access$1400.currentEditor = null;
            if (access$1400.readable | b) {
                access$1400.readable = true;
                this.journalWriter.write("CLEAN " + access$1400.key + access$1400.getLengths() + '\n');
                if (b) {
                    final long nextSequenceNumber = this.nextSequenceNumber;
                    this.nextSequenceNumber = 1L + nextSequenceNumber;
                    access$1400.sequenceNumber = nextSequenceNumber;
                }
            }
            else {
                this.lruEntries.remove(access$1400.key);
                this.journalWriter.write("REMOVE " + access$1400.key + '\n');
            }
            this.journalWriter.flush();
            if (this.size > this.maxSize || this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            continue Label_0115;
        }
    }
    // monitorexit(this)
    
    private static void deleteIfExists(final File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }
    
    private Editor edit(final String str, final long n) throws IOException {
        while (true) {
            Editor editor = null;
            while (true) {
                Object journalWriter;
                synchronized (this) {
                    this.checkNotClosed();
                    this.validateKey(str);
                    journalWriter = this.lruEntries.get(str);
                    Label_0067: {
                        if (n == -1L) {
                            break Label_0067;
                        }
                        Editor editor2 = editor;
                        if (journalWriter != null) {
                            if (((Entry)journalWriter).sequenceNumber == n) {
                                break Label_0067;
                            }
                            editor2 = editor;
                        }
                        return editor2;
                    }
                    if (journalWriter == null) {
                        final Entry value = new Entry(str);
                        this.lruEntries.put(str, value);
                        editor = new Editor(value);
                        value.currentEditor = editor;
                        journalWriter = this.journalWriter;
                        ((Writer)journalWriter).write("DIRTY " + str + '\n');
                        this.journalWriter.flush();
                        return editor;
                    }
                }
                final Editor access$700 = ((Entry)journalWriter).currentEditor;
                final Entry value = (Entry)journalWriter;
                if (access$700 != null) {
                    return editor;
                }
                continue;
            }
        }
    }
    
    private static String inputStreamToString(final InputStream in) throws IOException {
        return Util.readFully(new InputStreamReader(in, Util.UTF_8));
    }
    
    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }
    
    public static DiskLruCache open(File obj, final int n, final int n2, final long n3) throws IOException {
        if (n3 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        final File file = new File((File)obj, "journal.bkp");
        Label_0167: {
            if (file.exists()) {
                final Object o = new File((File)obj, "journal");
                if (!((File)o).exists()) {
                    break Label_0167;
                }
                file.delete();
            }
            while (true) {
                final Object o = new DiskLruCache((File)obj, n, n2, n3);
                if (!((DiskLruCache)o).journalFile.exists()) {
                    break Label_0167;
                }
                try {
                    ((DiskLruCache)o).readJournal();
                    ((DiskLruCache)o).processJournal();
                    ((DiskLruCache)o).journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(((DiskLruCache)o).journalFile, true), Util.US_ASCII));
                    obj = o;
                    return (DiskLruCache)obj;
                    renameTo(file, (File)o, false);
                    continue;
                }
                catch (IOException ex) {
                    System.out.println("DiskLruCache " + obj + " is corrupt: " + ex.getMessage() + ", removing");
                    ((DiskLruCache)o).delete();
                }
                break;
            }
        }
        ((File)obj).mkdirs();
        obj = new DiskLruCache((File)obj, n, n2, n3);
        ((DiskLruCache)obj).rebuildJournal();
        return (DiskLruCache)obj;
    }
    
    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        final Iterator<Entry> iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            final Entry entry = iterator.next();
            if (entry.currentEditor == null) {
                for (int i = 0; i < this.valueCount; ++i) {
                    this.size += entry.lengths[i];
                }
            }
            else {
                entry.currentEditor = null;
                for (int j = 0; j < this.valueCount; ++j) {
                    deleteIfExists(entry.getCleanFile(j));
                    deleteIfExists(entry.getDirtyFile(j));
                }
                iterator.remove();
            }
        }
    }
    
    private void readJournal() throws IOException {
        final StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            final String line = strictLineReader.readLine();
            final String line2 = strictLineReader.readLine();
            final String line3 = strictLineReader.readLine();
            final String line4 = strictLineReader.readLine();
            final String line5 = strictLineReader.readLine();
            if (!"libcore.io.DiskLruCache".equals(line) || !"1".equals(line2) || !Integer.toString(this.appVersion).equals(line3) || !Integer.toString(this.valueCount).equals(line4) || !"".equals(line5)) {
                throw new IOException("unexpected journal header: [" + line + ", " + line2 + ", " + line4 + ", " + line5 + "]");
            }
        }
        finally {
            Util.closeQuietly(strictLineReader);
        }
        int n = 0;
        try {
            while (true) {
                this.readJournalLine(strictLineReader.readLine());
                ++n;
            }
        }
        catch (EOFException ex) {
            this.redundantOpCount = n - this.lruEntries.size();
            Util.closeQuietly(strictLineReader);
        }
    }
    
    private void readJournalLine(final String s) throws IOException {
        final int index = s.indexOf(32);
        if (index == -1) {
            throw new IOException("unexpected journal line: " + s);
        }
        final int beginIndex = index + 1;
        final int index2 = s.indexOf(32, beginIndex);
        String s2 = null;
        Label_0112: {
            if (index2 != -1) {
                s2 = s.substring(beginIndex, index2);
                break Label_0112;
            }
            final String key = s2 = s.substring(beginIndex);
            if (index != "REMOVE".length()) {
                break Label_0112;
            }
            s2 = key;
            if (!s.startsWith("REMOVE")) {
                break Label_0112;
            }
            this.lruEntries.remove(key);
            return;
        }
        Entry value;
        if ((value = this.lruEntries.get(s2)) == null) {
            value = new Entry(s2);
            this.lruEntries.put(s2, value);
        }
        if (index2 != -1 && index == "CLEAN".length() && s.startsWith("CLEAN")) {
            final String[] split = s.substring(index2 + 1).split(" ");
            value.readable = true;
            value.currentEditor = null;
            value.setLengths(split);
            return;
        }
        if (index2 == -1 && index == "DIRTY".length() && s.startsWith("DIRTY")) {
            value.currentEditor = new Editor(value);
            return;
        }
        if (index2 != -1 || index != "READ".length() || !s.startsWith("READ")) {
            throw new IOException("unexpected journal line: " + s);
        }
    }
    
    private void rebuildJournal() throws IOException {
        BufferedWriter bufferedWriter;
        while (true) {
            while (true) {
                Object out;
                synchronized (this) {
                    if (this.journalWriter != null) {
                        this.journalWriter.close();
                    }
                    Object iterator = new(java.io.OutputStreamWriter.class);
                    out = new FileOutputStream(this.journalFileTmp);
                    new OutputStreamWriter((OutputStream)out, Util.US_ASCII);
                    bufferedWriter = new BufferedWriter((Writer)iterator);
                    try {
                        bufferedWriter.write("libcore.io.DiskLruCache");
                        bufferedWriter.write("\n");
                        bufferedWriter.write("1");
                        bufferedWriter.write("\n");
                        bufferedWriter.write(Integer.toString(this.appVersion));
                        bufferedWriter.write("\n");
                        bufferedWriter.write(Integer.toString(this.valueCount));
                        bufferedWriter.write("\n");
                        bufferedWriter.write("\n");
                        iterator = this.lruEntries.values().iterator();
                        while (((Iterator)iterator).hasNext()) {
                            out = ((Iterator<Entry>)iterator).next();
                            if (((Entry)out).currentEditor == null) {
                                break;
                            }
                            bufferedWriter.write("DIRTY " + ((Entry)out).key + '\n');
                        }
                    }
                    finally {
                        bufferedWriter.close();
                    }
                }
                bufferedWriter.write("CLEAN " + ((Entry)out).key + ((Entry)out).getLengths() + '\n');
                continue;
            }
        }
        bufferedWriter.close();
        if (this.journalFile.exists()) {
            renameTo(this.journalFile, this.journalFileBackup, true);
        }
        renameTo(this.journalFileTmp, this.journalFile, false);
        this.journalFileBackup.delete();
        this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
    }
    // monitorexit(this)
    
    private static void renameTo(final File file, final File dest, final boolean b) throws IOException {
        if (b) {
            deleteIfExists(dest);
        }
        if (!file.renameTo(dest)) {
            throw new IOException();
        }
    }
    
    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }
    
    private void validateKey(final String s) {
        if (!DiskLruCache.LEGAL_KEY_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + s + "\"");
        }
    }
    
    @Override
    public void close() throws IOException {
        while (true) {
            synchronized (this) {
                if (this.journalWriter == null) {
                    return;
                }
                for (final Entry entry : new ArrayList<Entry>(this.lruEntries.values())) {
                    if (entry.currentEditor != null) {
                        entry.currentEditor.abort();
                    }
                }
            }
            this.trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }
    
    public void delete() throws IOException {
        this.close();
        Util.deleteContents(this.directory);
    }
    
    public Editor edit(final String s) throws IOException {
        return this.edit(s, -1L);
    }
    
    public void flush() throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            this.trimToSize();
            this.journalWriter.flush();
        }
    }
    
    public Snapshot get(final String s) throws IOException {
        final Snapshot snapshot = null;
        synchronized (this) {
            this.checkNotClosed();
            this.validateKey(s);
            final Entry entry = this.lruEntries.get(s);
            Snapshot snapshot2;
            if (entry == null) {
                snapshot2 = snapshot;
            }
            else {
                snapshot2 = snapshot;
                if (entry.readable) {
                    final InputStream[] array = new InputStream[this.valueCount];
                    int i = 0;
                    try {
                        while (i < this.valueCount) {
                            array[i] = new FileInputStream(entry.getCleanFile(i));
                            ++i;
                        }
                    }
                    catch (FileNotFoundException ex) {
                        int n = 0;
                        while (true) {
                            snapshot2 = snapshot;
                            if (n >= this.valueCount) {
                                return snapshot2;
                            }
                            snapshot2 = snapshot;
                            if (array[n] == null) {
                                return snapshot2;
                            }
                            Util.closeQuietly(array[n]);
                            ++n;
                        }
                    }
                    ++this.redundantOpCount;
                    this.journalWriter.append((CharSequence)("READ " + s + '\n'));
                    if (this.journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                    snapshot2 = new Snapshot(s, entry.sequenceNumber, array, entry.lengths);
                }
            }
            return snapshot2;
        }
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public long getMaxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public boolean isClosed() {
        synchronized (this) {
            return this.journalWriter == null;
        }
    }
    
    public boolean remove(final String key) throws IOException {
        while (true) {
        Label_0144:
            while (true) {
                Object journalWriter;
                int n;
                synchronized (this) {
                    this.checkNotClosed();
                    this.validateKey(key);
                    journalWriter = this.lruEntries.get(key);
                    if (journalWriter == null || ((Entry)journalWriter).currentEditor != null) {
                        return false;
                    }
                    n = 0;
                    if (n >= this.valueCount) {
                        break Label_0144;
                    }
                    final File cleanFile = ((Entry)journalWriter).getCleanFile(n);
                    if (cleanFile.exists() && !cleanFile.delete()) {
                        journalWriter = new StringBuilder();
                        throw new IOException(((StringBuilder)journalWriter).append("failed to delete ").append(cleanFile).toString());
                    }
                }
                this.size -= ((Entry)journalWriter).lengths[n];
                ((Entry)journalWriter).lengths[n] = 0L;
                ++n;
                continue;
            }
            ++this.redundantOpCount;
            Object journalWriter = this.journalWriter;
            final String s;
            ((Writer)journalWriter).append((CharSequence)("REMOVE " + s + '\n'));
            this.lruEntries.remove(s);
            if (this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            return true;
        }
    }
    
    public void setMaxSize(final long maxSize) {
        synchronized (this) {
            this.maxSize = maxSize;
            this.executorService.submit(this.cleanupCallable);
        }
    }
    
    public long size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    public final class Editor
    {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;
        
        private Editor(final Entry entry) {
            this.entry = entry;
            boolean[] written;
            if (entry.readable) {
                written = null;
            }
            else {
                written = new boolean[DiskLruCache.this.valueCount];
            }
            this.written = written;
        }
        
        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }
        
        public void abortUnlessCommitted() {
            if (this.committed) {
                return;
            }
            try {
                this.abort();
            }
            catch (IOException ex) {}
        }
        
        public void commit() throws IOException {
            if (this.hasErrors) {
                DiskLruCache.this.completeEdit(this, false);
                DiskLruCache.this.remove(this.entry.key);
            }
            else {
                DiskLruCache.this.completeEdit(this, true);
            }
            this.committed = true;
        }
        
        public String getString(final int n) throws IOException {
            final InputStream inputStream = this.newInputStream(n);
            String access$1700;
            if (inputStream != null) {
                access$1700 = inputStreamToString(inputStream);
            }
            else {
                access$1700 = null;
            }
            return access$1700;
        }
        
        public InputStream newInputStream(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
            }
            FileInputStream fileInputStream2 = null;
            if (!(!this.entry.readable)) {
                try {
                    final FileInputStream fileInputStream = new FileInputStream(this.entry.getCleanFile(n));
                    // monitorexit(diskLruCache)
                    fileInputStream2 = fileInputStream;
                }
                catch (FileNotFoundException ex) {
                }
                // monitorexit(diskLruCache)
            }
            return fileInputStream2;
        }
        
        public OutputStream newOutputStream(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
            }
            if (!this.entry.readable) {
                this.written[n] = true;
            }
            Object dirtyFile = this.entry.getDirtyFile(n);
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream((File)dirtyFile);
                dirtyFile = new FaultHidingOutputStream((OutputStream)fileOutputStream);
                // monitorexit(diskLruCache)
                final Object access$2000 = dirtyFile;
                return (OutputStream)access$2000;
            }
            catch (FileNotFoundException ex) {
                DiskLruCache.this.directory.mkdirs();
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream((File)dirtyFile);
                }
                catch (FileNotFoundException ex2) {
                    final Object access$2000 = DiskLruCache.NULL_OUTPUT_STREAM;
                }
                // monitorexit(diskLruCache)
            }
        }
        
        public void set(final int n, final String s) throws IOException {
            Closeable closeable = null;
            OutputStreamWriter outputStreamWriter;
            try {
                final Writer writer;
                outputStreamWriter = (OutputStreamWriter)(writer = new OutputStreamWriter(this.newOutputStream(n), Util.UTF_8));
                final String s2 = s;
                writer.write(s2);
                final OutputStreamWriter outputStreamWriter2 = outputStreamWriter;
                Util.closeQuietly(outputStreamWriter2);
                return;
            }
            finally {
                final Object o2;
                final Object o = o2;
            }
            while (true) {
                try {
                    final Writer writer = outputStreamWriter;
                    final String s2 = s;
                    writer.write(s2);
                    final OutputStreamWriter outputStreamWriter2 = outputStreamWriter;
                    Util.closeQuietly(outputStreamWriter2);
                    return;
                    Util.closeQuietly(closeable);
                    throw;
                }
                finally {
                    closeable = outputStreamWriter;
                    continue;
                }
                break;
            }
        }
        
        private class FaultHidingOutputStream extends FilterOutputStream
        {
            private FaultHidingOutputStream(final OutputStream out) {
                super(out);
            }
            
            @Override
            public void close() {
                try {
                    this.out.close();
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
            
            @Override
            public void flush() {
                try {
                    this.out.flush();
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
            
            @Override
            public void write(final int n) {
                try {
                    this.out.write(n);
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
            
            @Override
            public void write(final byte[] b, final int off, final int len) {
                try {
                    this.out.write(b, off, len);
                }
                catch (IOException ex) {
                    Editor.this.hasErrors = true;
                }
            }
        }
    }
    
    private final class Entry
    {
        private Editor currentEditor;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;
        
        private Entry(final String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
        }
        
        private IOException invalidLengths(final String[] a) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(a));
        }
        
        private void setLengths(final String[] array) throws IOException {
            if (array.length != DiskLruCache.this.valueCount) {
                throw this.invalidLengths(array);
            }
            int i = 0;
            try {
                while (i < array.length) {
                    this.lengths[i] = Long.parseLong(array[i]);
                    ++i;
                }
            }
            catch (NumberFormatException ex) {
                throw this.invalidLengths(array);
            }
        }
        
        public File getCleanFile(final int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i);
        }
        
        public File getDirtyFile(final int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i + ".tmp");
        }
        
        public String getLengths() throws IOException {
            final StringBuilder sb = new StringBuilder();
            final long[] lengths = this.lengths;
            for (int length = lengths.length, i = 0; i < length; ++i) {
                sb.append(' ').append(lengths[i]);
            }
            return sb.toString();
        }
    }
    
    public final class Snapshot implements Closeable
    {
        private final InputStream[] ins;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        
        private Snapshot(final String key, final long sequenceNumber, final InputStream[] ins, final long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.ins = ins;
            this.lengths = lengths;
        }
        
        @Override
        public void close() {
            final InputStream[] ins = this.ins;
            for (int length = ins.length, i = 0; i < length; ++i) {
                Util.closeQuietly(ins[i]);
            }
        }
        
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }
        
        public InputStream getInputStream(final int n) {
            return this.ins[n];
        }
        
        public long getLength(final int n) {
            return this.lengths[n];
        }
        
        public String getString(final int n) throws IOException {
            return inputStreamToString(this.getInputStream(n));
        }
    }
}
