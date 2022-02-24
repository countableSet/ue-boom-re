// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.other;

import android.os.AsyncTask;
import android.util.Log;
import android.os.Looper;
import android.os.Handler;

public abstract class ValueUpdater<T>
{
    public static final String TAG;
    protected Handler mHandler;
    protected final Object mMonitorObject;
    protected State mState;
    protected WorkerTask mTask;
    protected T mValue;
    
    static {
        TAG = ValueUpdater.class.getSimpleName();
    }
    
    public ValueUpdater() {
        this.mState = State.Idle;
        this.mMonitorObject = new Object();
        this.mHandler = new Handler(Looper.getMainLooper());
    }
    
    public void onValueUpdated(final T t) {
    }
    
    protected void startWorkerTask() {
        Log.d(ValueUpdater.TAG, "Start new worker task");
        this.mState = State.Busy;
        (this.mTask = new WorkerTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
    }
    
    public void stop() {
        Log.d(ValueUpdater.TAG, "Stop");
        synchronized (this.mMonitorObject) {
            this.mValue = null;
            this.mTask = null;
            this.mState = State.Idle;
        }
    }
    
    public void update(final T t) {
        Log.d(ValueUpdater.TAG, "Set new value (" + t + ")");
        synchronized (this.mMonitorObject) {
            this.mValue = t;
            if (this.mState == State.Idle) {
                this.startWorkerTask();
            }
        }
    }
    
    protected abstract void updateValue(final T p0) throws Exception;
    
    public enum State
    {
        Busy, 
        Idle;
    }
    
    public class WorkerTask extends AsyncTask<Void, Void, Void>
    {
        private T value;
        
        public WorkerTask() {
            this.value = null;
        }
        
        protected Void doInBackground(final Void... array) {
            while (true) {
                Label_0039: {
                    if (this.value == ValueUpdater.this.mValue) {
                        break Label_0039;
                    }
                    Object mMonitorObject = ValueUpdater.this.mMonitorObject;
                    synchronized (mMonitorObject) {
                        if (ValueUpdater.this.mState == State.Idle) {
                            return null;
                        }
                        this.value = ValueUpdater.this.mValue;
                        // monitorexit(mMonitorObject)
                        try {
                            final String tag = ValueUpdater.TAG;
                            mMonitorObject = new StringBuilder();
                            Log.d(tag, ((StringBuilder)mMonitorObject).append("Update to value ").append(this.value).toString());
                            ValueUpdater.this.updateValue(this.value);
                            final T value = this.value;
                            final Handler mHandler = ValueUpdater.this.mHandler;
                            mMonitorObject = new Runnable() {
                                @Override
                                public void run() {
                                    ValueUpdater.this.onValueUpdated(value);
                                }
                            };
                            mHandler.post((Runnable)mMonitorObject);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        
        protected void onPostExecute(final Void void1) {
            super.onPostExecute((Object)void1);
            Log.d(ValueUpdater.TAG, "Worker task finished");
            synchronized (ValueUpdater.this.mMonitorObject) {
                if (this.value != ValueUpdater.this.mValue && ValueUpdater.this.mState == State.Busy) {
                    ValueUpdater.this.startWorkerTask();
                }
                else {
                    ValueUpdater.this.mState = State.Idle;
                    ValueUpdater.this.mTask = null;
                    ValueUpdater.this.mValue = null;
                }
            }
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(ValueUpdater.TAG, "Worker task started");
        }
    }
}
