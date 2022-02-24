// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import android.support.annotation.WorkerThread;
import android.support.annotation.MainThread;
import android.util.Log;
import android.os.AsyncTask;

public abstract class SafeTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Object>
{
    @SafeVarargs
    protected final Object doInBackground(final Params... array) {
        Log.d(this.getTag(), "Begin background work");
        try {
            return this.work(array);
        }
        catch (Exception work) {
            Log.d(this.getTag(), "Begin background work");
            return work;
        }
    }
    
    @SafeVarargs
    public final void executeOnThreadPoolExecutor(final Params... array) {
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])array);
    }
    
    public abstract String getTag();
    
    @MainThread
    public void onError(final Exception ex) {
    }
    
    protected void onPostExecute(final Object o) {
        super.onPostExecute(o);
        if (o instanceof Exception) {
            Log.w(this.getTag(), "Task error", (Throwable)o);
            this.onError((Exception)o);
        }
        else if (o != null) {
            Log.d(this.getTag(), "Task success");
            this.onSuccess(o);
        }
        else {
            this.onSuccess(null);
        }
    }
    
    protected void onPreExecute() {
        Log.d(this.getTag(), "On pre execute");
        super.onPreExecute();
    }
    
    @MainThread
    public void onSuccess(final Result result) {
    }
    
    @WorkerThread
    public abstract Result work(final Params... p0) throws Exception;
}
