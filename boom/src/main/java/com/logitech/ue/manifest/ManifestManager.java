// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.manifest;

import com.logitech.ue.service.ServiceInfo;
import android.support.annotation.NonNull;
import android.os.AsyncTask;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.os.AsyncTask$Status;
import android.os.Bundle;
import android.content.Context;
import com.logitech.ue.service.IUEService;

public class ManifestManager implements IUEService
{
    public static final String ACTION_MANIFEST_UPDATED = "com.logitech.ue.manifest.MANIFEST_UPDATED";
    private static final String MANIFEST_URL_KEY = "manifest_url";
    private static final String TAG;
    private static volatile ManifestManager instance;
    private Context mContext;
    private UEManifest mManifest;
    private String mManifestURL;
    private String mServiceLocationURL;
    private DownloadManifestTask mUpdateManifestTask;
    
    static {
        TAG = ManifestManager.class.getSimpleName();
        ManifestManager.instance = null;
    }
    
    private ManifestManager() {
        this.mManifest = new UEManifest();
    }
    
    public static Bundle buildParamBundle(final String s) {
        final Bundle bundle = new Bundle();
        bundle.putString("manifest_url", s);
        return bundle;
    }
    
    public static ManifestManager getInstance() {
        Label_0030: {
            if (ManifestManager.instance != null) {
                break Label_0030;
            }
            synchronized (ManifestManager.class) {
                if (ManifestManager.instance == null) {
                    ManifestManager.instance = new ManifestManager();
                }
                return ManifestManager.instance;
            }
        }
    }
    
    public void beginUpdateManifest() {
        if (this.mUpdateManifestTask == null || this.mUpdateManifestTask.getStatus() == AsyncTask$Status.FINISHED) {
            Log.d(ManifestManager.TAG, "Begin manifest sync");
            (this.mUpdateManifestTask = new DownloadManifestTask(this.mManifestURL) {
                protected void onPostExecute(final Object o) {
                    super.onPostExecute(o);
                    if (o instanceof UEManifest) {
                        Log.d(ManifestManager.TAG, "Manifest sync success");
                        ManifestManager.this.mManifest = (UEManifest)o;
                        LocalBroadcastManager.getInstance(ManifestManager.this.mContext).sendBroadcast(new Intent("com.logitech.ue.manifest.MANIFEST_UPDATED"));
                    }
                    else if (o instanceof Exception) {
                        Log.w(ManifestManager.TAG, "Manifest sync failed", (Throwable)o);
                    }
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
    }
    
    @NonNull
    public UEManifest getManifest() {
        return this.mManifest;
    }
    
    @Override
    public String getServiceName() {
        return "firmware";
    }
    
    @Override
    public void initService(final Context mContext, final Bundle bundle) {
        this.mContext = mContext;
        if (bundle != null && bundle.containsKey("manifest_url")) {
            this.mManifestURL = bundle.getString("manifest_url", (String)null);
        }
    }
    
    public void setManifest(final UEManifest mManifest) {
        this.mManifest = mManifest;
    }
    
    public void setManifest(final String s) {
        Label_0021: {
            if (s == null) {
                break Label_0021;
            }
            try {
                this.mManifest = UEManifest.readFromXML(s);
                return;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
        Log.e(ManifestManager.TAG, "UEManifest XML is not populated");
    }
    
    @Override
    public void syncService(final ServiceInfo serviceInfo) {
        this.mServiceLocationURL = serviceInfo.location;
    }
}
