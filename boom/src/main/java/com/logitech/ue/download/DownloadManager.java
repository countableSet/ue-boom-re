// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.download;

import android.net.NetworkInfo;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.io.InterruptedIOException;
import android.net.ConnectivityManager;
import android.content.IntentFilter;
import java.util.concurrent.Executors;
import com.logitech.ue.util.Utils;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import android.util.Log;
import android.content.Intent;
import java.util.concurrent.ExecutorService;
import android.content.Context;
import android.content.BroadcastReceiver;

public class DownloadManager
{
    private static final String TAG;
    private static volatile DownloadManager instance;
    private final BroadcastReceiver _connectionReceiver;
    Context context;
    ExecutorService executorService;
    boolean isRunning;
    final Object syncObject;
    
    static {
        TAG = DownloadManager.class.getSimpleName();
        DownloadManager.instance = null;
    }
    
    private DownloadManager() {
        this.syncObject = new Object();
        this._connectionReceiver = new BroadcastReceiver() {
            public void onReceive(Context syncObject, final Intent intent) {
                final boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
                syncObject = (Context)DownloadManager.this.syncObject;
                // monitorenter(syncObject)
                Label_0044: {
                    if (booleanExtra) {
                        break Label_0044;
                    }
                    try {
                        Log.d(DownloadManager.TAG, "Connection ON");
                        DownloadManager.this.syncObject.notifyAll();
                        return;
                        Log.d(DownloadManager.TAG, "Connection OFF");
                    }
                    finally {
                    }
                    // monitorexit(syncObject)
                }
            }
        };
        this.isRunning = false;
    }
    
    private DownloadManager(final Context context) {
        this.syncObject = new Object();
        this._connectionReceiver = new BroadcastReceiver() {
            public void onReceive(Context syncObject, final Intent intent) {
                final boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
                syncObject = (Context)DownloadManager.this.syncObject;
                // monitorenter(syncObject)
                Label_0044: {
                    if (booleanExtra) {
                        break Label_0044;
                    }
                    try {
                        Log.d(DownloadManager.TAG, "Connection ON");
                        DownloadManager.this.syncObject.notifyAll();
                        return;
                        Log.d(DownloadManager.TAG, "Connection OFF");
                    }
                    finally {
                    }
                    // monitorexit(syncObject)
                }
            }
        };
        this.isRunning = false;
        this.context = context;
        this.startLoader();
    }
    
    public static DownloadManager getInstance(final Context context) {
        Label_0031: {
            if (DownloadManager.instance != null) {
                break Label_0031;
            }
            synchronized (DownloadManager.class) {
                if (DownloadManager.instance == null) {
                    DownloadManager.instance = new DownloadManager(context);
                }
                return DownloadManager.instance;
            }
        }
    }
    
    private byte[] loadData(final String spec) throws IOException {
        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(spec).openConnection();
        httpURLConnection.setInstanceFollowRedirects(true);
        final byte[] streamAsByteArray = Utils.readStreamAsByteArray(httpURLConnection.getInputStream());
        httpURLConnection.disconnect();
        return streamAsByteArray;
    }
    
    private void queueTask(final String str, final String s, final LoadDataTaskListener loadDataTaskListener) {
        Log.d(DownloadManager.TAG, "Queue load task. Url - " + str);
        this.executorService.submit(new LoaderRunnable(new LoadDataTask(str, s, loadDataTaskListener)));
    }
    
    public void loadFile(final String s, final String s2, final LoadDataTaskListener loadDataTaskListener) {
        this.queueTask(s, s2, loadDataTaskListener);
    }
    
    public void loadFile(final URL url, final String s, final LoadDataTaskListener loadDataTaskListener) {
        this.queueTask(url.toString(), s, loadDataTaskListener);
    }
    
    public void shutDown() {
        if (this.isRunning) {
            Log.d(DownloadManager.TAG, "Loader stopped");
            this.isRunning = false;
            this.context.unregisterReceiver(this._connectionReceiver);
            this.executorService.shutdownNow();
            DownloadManager.instance = null;
        }
        else {
            Log.d(DownloadManager.TAG, "Loader already is not running");
        }
    }
    
    public void startLoader() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.executorService = Executors.newFixedThreadPool(5);
            this.context.registerReceiver(this._connectionReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            Log.d(DownloadManager.TAG, "Loader started");
        }
        else {
            Log.d(DownloadManager.TAG, "Loader already is running");
        }
    }
    
    class LoaderRunnable implements Runnable
    {
        final LoadDataTask loadTask;
        
        LoaderRunnable(final LoadDataTask loadTask) {
            this.loadTask = loadTask;
        }
        
        @Override
        public void run() {
            Log.d(DownloadManager.TAG, "Begin loading. URL - " + this.loadTask.url);
            if (this.loadTask.listener != null) {
                this.loadTask.listener.onBeginTask(this.loadTask);
            }
            synchronized (DownloadManager.this.syncObject) {
                while (true) {
                    final NetworkInfo activeNetworkInfo = ((ConnectivityManager)DownloadManager.this.context.getSystemService("connectivity")).getActiveNetworkInfo();
                    if (activeNetworkInfo != null) {
                        if (activeNetworkInfo.isConnectedOrConnecting()) {
                            break;
                        }
                    }
                    try {
                        Log.d(DownloadManager.TAG, "Task waiting for network. URL - " + this.loadTask.url);
                        DownloadManager.this.syncObject.wait();
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // monitorexit(o)
            try {
                final byte[] access$100 = DownloadManager.this.loadData(this.loadTask.url);
                this.loadTask.data = access$100;
                Log.d(DownloadManager.TAG, "Task success. Data loaded, size " + access$100.length / 1024 + " kBytes. URL - " + this.loadTask.url);
                if (this.loadTask.listener != null) {
                    this.loadTask.listener.onEndTask(this.loadTask);
                }
            }
            catch (InterruptedIOException ex2) {
                Log.e(DownloadManager.TAG, "Task failed. Loading interrupted. URL - " + this.loadTask.url + ". Retry again.");
                ex2.printStackTrace();
                DownloadManager.this.loadFile(this.loadTask.url, this.loadTask.tag, this.loadTask.listener);
                if (this.loadTask.listener != null) {
                    this.loadTask.listener.onError(this.loadTask, ex2);
                }
            }
            catch (Exception ex3) {
                if (ex3 instanceof MalformedURLException) {
                    Log.e(DownloadManager.TAG, "Task failed. URL invalid. URL - " + this.loadTask.url);
                }
                else if (ex3 instanceof FileNotFoundException) {
                    Log.e(DownloadManager.TAG, "Task failed. File not found. URL - " + this.loadTask.url);
                }
                else if (ex3 instanceof IOException) {
                    Log.e(DownloadManager.TAG, "Task failed. I/O error. URL - " + this.loadTask.url);
                }
                else {
                    Log.e(DownloadManager.TAG, "Task failed. URL - " + this.loadTask.url);
                }
                ex3.printStackTrace();
                if (this.loadTask.listener != null) {
                    this.loadTask.listener.onError(this.loadTask, ex3);
                }
            }
        }
    }
}
