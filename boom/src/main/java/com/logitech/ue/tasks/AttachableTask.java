// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import android.util.Log;
import android.os.AsyncTask;
import com.logitech.ue.App;
import com.logitech.ue.activities.BaseActivity;

public abstract class AttachableTask<T> extends SafeTask<Void, Void, T>
{
    protected BaseActivity mHostActivity;
    
    public AttachableTask() {
        this(true);
    }
    
    public AttachableTask(final boolean b) {
        if (b) {
            this.mHostActivity = (BaseActivity)App.getInstance().getCurrentActivity();
            if (this.mHostActivity != null) {
                this.mHostActivity.registerTask(this);
                Log.d(this.getTag(), "Attache task to activity " + this.mHostActivity.getClass().getSimpleName());
            }
            else {
                Log.w(this.getTag(), "Task  can't attach task to activity. There is no current activity");
            }
        }
    }
    
    @Override
    protected void onPostExecute(final Object o) {
        super.onPostExecute(o);
        if (this.mHostActivity != null) {
            Log.d(this.getTag(), "Detach task from activity " + this.mHostActivity.getClass().getSimpleName());
            this.mHostActivity.unregisterTask(this);
        }
    }
}
