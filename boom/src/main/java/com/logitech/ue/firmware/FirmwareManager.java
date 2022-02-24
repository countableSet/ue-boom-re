// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

import com.logitech.ue.service.ServiceInfo;
import java.util.Iterator;
import java.io.IOException;
import com.logitech.ue.download.LoadDataTask;
import com.logitech.ue.download.LoadDataTaskListener;
import com.logitech.ue.download.DownloadManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Parcelable;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayDeque;
import android.os.AsyncTask;
import java.util.Collection;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import com.logitech.ue.util.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import java.util.Vector;
import com.jakewharton.disklrucache.DiskLruCache;
import android.content.Context;
import com.logitech.ue.service.IUEService;

public class FirmwareManager implements IUEService
{
    public static final String ACTION_UPDATE_READY = "com.logitech.ue.firmware.UPDATE_READY";
    public static final String BROADCAST_PARAM_UPDATE_INSTRUCTION_PARAMS = "update_instruction_params";
    private static final long CACHE_LIFE_TIME;
    private static final long CACHE_MAX_SIZE = 52428800L;
    public static final String MANIFEST_KEY = "manifest";
    public static final String PARAM_SERVICE_LOCATION = "service_location";
    private static final String REQUEST_PATH = "/ue/speaker/%s/%s/%s/ota_update/%s/%s/%s/%s/%s";
    private static final String TAG;
    private static volatile FirmwareManager instance;
    private Context mContext;
    private DiskLruCache mDiskLruCache;
    private final Object mDiskLruMonitor;
    private String mServiceLocation;
    private final Vector<UpdateInstructionCacheNode> mUpdateInstructionCache;
    
    static {
        TAG = FirmwareManager.class.getSimpleName();
        CACHE_LIFE_TIME = TimeUnit.DAYS.toMillis(1L);
        FirmwareManager.instance = null;
    }
    
    private FirmwareManager() {
        this.mDiskLruMonitor = new Object();
        this.mUpdateInstructionCache = new Vector<UpdateInstructionCacheNode>();
    }
    
    public static Bundle buildParamBundle(final String s) {
        final Bundle bundle = new Bundle();
        bundle.putString("service_location", s);
        return bundle;
    }
    
    private String buildRequestURL(final UpdateInstructionParams updateInstructionParams) throws UnsupportedEncodingException {
        return this.mServiceLocation + String.format("/ue/speaker/%s/%s/%s/ota_update/%s/%s/%s/%s/%s", URLEncoder.encode(updateInstructionParams.deviceType, "UTF-8"), URLEncoder.encode(updateInstructionParams.hardwareRevision, "UTF-8"), URLEncoder.encode(updateInstructionParams.firmwareVersion, "UTF-8"), URLEncoder.encode(updateInstructionParams.appName, "UTF-8"), URLEncoder.encode(updateInstructionParams.appVersion, "UTF-8"), URLEncoder.encode(updateInstructionParams.clientOS, "UTF-8"), URLEncoder.encode(updateInstructionParams.clientOSVersion, "UTF-8"), URLEncoder.encode(updateInstructionParams.language, "UTF-8"));
    }
    
    private UpdateInstructionCacheNode checkUpdateInstructionInCache(final UpdateInstructionParams updateInstructionParams) {
        final Vector<UpdateInstructionCacheNode> mUpdateInstructionCache = this.mUpdateInstructionCache;
        // monitorenter(mUpdateInstructionCache)
        int i = 0;
        try {
            while (i < this.mUpdateInstructionCache.size()) {
                final UpdateInstructionCacheNode updateInstructionCacheNode = this.mUpdateInstructionCache.get(i);
                if (updateInstructionCacheNode.updateInstructionParams.equals(updateInstructionParams) && System.currentTimeMillis() - updateInstructionCacheNode.time < FirmwareManager.CACHE_LIFE_TIME) {
                    // monitorexit(mUpdateInstructionCache)
                    return updateInstructionCacheNode;
                }
                ++i;
            }
            // monitorexit(mUpdateInstructionCache)
            return null;
        }
        finally {
        }
        // monitorexit(mUpdateInstructionCache)
    }
    
    public static FirmwareManager getInstance() {
        Label_0030: {
            if (FirmwareManager.instance != null) {
                break Label_0030;
            }
            synchronized (FirmwareManager.class) {
                if (FirmwareManager.instance == null) {
                    FirmwareManager.instance = new FirmwareManager();
                }
                return FirmwareManager.instance;
            }
        }
    }
    
    private boolean isFirmwareInCache(final String s) {
        final boolean b = false;
        boolean b2 = false;
        synchronized (this.mDiskLruMonitor) {
            try {
                if (this.mDiskLruCache.get(Utils.MD5(s).toLowerCase()) != null) {
                    b2 = true;
                }
                return b2;
            }
            catch (Exception ex) {
                // monitorexit(this.mDiskLruMonitor)
                b2 = b;
                return b2;
            }
        }
    }
    
    private byte[] loadFirmwareFromCache(final String s) {
        synchronized (this.mDiskLruMonitor) {
            try {
                final InputStream inputStream = this.mDiskLruCache.get(Utils.MD5(s).toLowerCase()).getInputStream(0);
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Utils.copyStream(inputStream, byteArrayOutputStream);
                inputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
            catch (Exception ex) {
                // monitorexit(this.mDiskLruMonitor)
                return null;
            }
        }
    }
    
    private InputStream loadFirmwareStreamFromCache(final String s) {
        final InputStream inputStream = null;
        final Object mDiskLruMonitor = this.mDiskLruMonitor;
        // monitorenter(mDiskLruMonitor)
        try {
            try {
                final DiskLruCache.Snapshot value = this.mDiskLruCache.get(Utils.MD5(s).toLowerCase());
                InputStream inputStream2;
                if (value != null) {
                    inputStream2 = value.getInputStream(0);
                }
                else {
                    // monitorexit(mDiskLruMonitor)
                    inputStream2 = inputStream;
                }
                return inputStream2;
            }
            finally {
            }
            // monitorexit(mDiskLruMonitor)
        }
        catch (Exception ex) {
            // monitorexit(mDiskLruMonitor)
            return inputStream;
        }
    }
    
    private void updateInstructionCache(final UpdateInstructionParams updateInstructionParams, final UpdateInstruction updateInstruction) {
        synchronized (this.mUpdateInstructionCache) {
            final long currentTimeMillis = System.currentTimeMillis();
            final int n = 0;
            int index = 0;
            int n2;
            while (true) {
                n2 = n;
                if (index >= this.mUpdateInstructionCache.size()) {
                    break;
                }
                final UpdateInstructionCacheNode updateInstructionCacheNode = this.mUpdateInstructionCache.get(index);
                if (updateInstructionCacheNode.updateInstructionParams.equals(updateInstructionParams)) {
                    n2 = 1;
                    updateInstructionCacheNode.updateInstruction = updateInstruction;
                    updateInstructionCacheNode.time = currentTimeMillis;
                    break;
                }
                ++index;
            }
            if (n2 == 0) {
                this.mUpdateInstructionCache.add(new UpdateInstructionCacheNode(updateInstructionParams, updateInstruction, currentTimeMillis));
            }
            final ArrayList<UpdateInstructionCacheNode> c = new ArrayList<UpdateInstructionCacheNode>();
            for (int i = 0; i < this.mUpdateInstructionCache.size(); ++i) {
                final UpdateInstructionCacheNode e = this.mUpdateInstructionCache.get(i);
                if (currentTimeMillis - e.time > FirmwareManager.CACHE_LIFE_TIME) {
                    c.add(e);
                }
            }
            this.mUpdateInstructionCache.removeAll(c);
        }
    }
    
    public void beginCheckUpdateInstruction(final UpdateInstructionParams updateInstructionParams, final OnCheckUpdateInstructionListener onCheckUpdateInstructionListener) {
        try {
            new CheckUpdateInstructionTask(this.buildRequestURL(updateInstructionParams)) {
                @Override
                protected Object doInBackground(final Void... array) {
                    final UpdateInstructionCacheNode access$000 = FirmwareManager.this.checkUpdateInstructionInCache(updateInstructionParams);
                    Object o;
                    if (access$000 != null) {
                        o = access$000.updateInstruction;
                    }
                    else {
                        final Object o2 = o = super.doInBackground(array);
                        if (o2 instanceof UpdateInstruction) {
                            FirmwareManager.this.updateInstructionCache(updateInstructionParams, (UpdateInstruction)o2);
                            o = o2;
                        }
                    }
                    return o;
                }
                
                protected void onPostExecute(final Object o) {
                    super.onPostExecute(o);
                    if (onCheckUpdateInstructionListener != null) {
                        if (o instanceof UpdateInstruction) {
                            onCheckUpdateInstructionListener.onSuccess((UpdateInstruction)o);
                        }
                        else {
                            onCheckUpdateInstructionListener.onError((Exception)o);
                        }
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
        }
        catch (Exception ex) {
            onCheckUpdateInstructionListener.onError(ex);
        }
    }
    
    public void beginCheckUpdateInstruction(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String s8, final OnCheckUpdateInstructionListener onCheckUpdateInstructionListener) {
        this.beginCheckUpdateInstruction(new UpdateInstructionParams(s, s2, s3, s4, s5, s6, s7, s8), onCheckUpdateInstructionListener);
    }
    
    public void beginUpdatePreparation(final UpdateInstructionParams updateInstructionParams, final UpdateInstruction updateInstruction) {
        final ArrayDeque<UpdateStepInfo> arrayDeque = new ArrayDeque<UpdateStepInfo>(updateInstruction.updateStepInfoList);
        if (arrayDeque.size() < 1) {
            Log.w(FirmwareManager.TAG, "Empty update step queue. Do nothing.");
        }
        else {
            UpdateStepInfo updateStepInfo;
            for (updateStepInfo = arrayDeque.pop(); this.isFirmwareInCache(updateStepInfo.firmwareDownloadURL); updateStepInfo = arrayDeque.pop()) {
                if (arrayDeque.isEmpty()) {
                    final Intent intent = new Intent("com.logitech.ue.firmware.UPDATE_READY");
                    intent.putExtra("update_instruction_params", (Parcelable)updateInstructionParams);
                    LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
                    return;
                }
            }
            DownloadManager.getInstance(this.mContext).loadFile(updateStepInfo.firmwareDownloadURL, "firmware", new LoadDataTaskListener() {
                @Override
                public void onBeginTask(final LoadDataTask loadDataTask) {
                }
                
                @Override
                public void onEndTask(final LoadDataTask loadDataTask) {
                    FirmwareManager.this.saveFirmwareInCache(loadDataTask.url, loadDataTask.data);
                    if (!arrayDeque.isEmpty()) {
                        UpdateStepInfo updateStepInfo;
                        for (updateStepInfo = arrayDeque.pop(); FirmwareManager.this.isFirmwareInCache(updateStepInfo.firmwareDownloadURL); updateStepInfo = arrayDeque.pop()) {
                            if (arrayDeque.isEmpty()) {
                                final Intent intent = new Intent("com.logitech.ue.firmware.UPDATE_READY");
                                intent.putExtra("update_instruction_params", (Parcelable)updateInstructionParams);
                                LocalBroadcastManager.getInstance(FirmwareManager.this.mContext).sendBroadcast(intent);
                                return;
                            }
                        }
                        DownloadManager.getInstance(FirmwareManager.this.mContext).loadFile(updateStepInfo.firmwareDownloadURL, "firmware", this);
                    }
                    else {
                        final Intent intent2 = new Intent("com.logitech.ue.firmware.UPDATE_READY");
                        intent2.putExtra("update_instruction_params", (Parcelable)updateInstructionParams);
                        LocalBroadcastManager.getInstance(FirmwareManager.this.mContext).sendBroadcast(intent2);
                    }
                }
                
                @Override
                public void onError(final LoadDataTask loadDataTask, final Throwable t) {
                    Log.e(FirmwareManager.TAG, t.getMessage());
                }
            });
        }
    }
    
    public UpdateInstructionCacheNode checkUpdateInstructionInCache(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String s8) {
        return this.checkUpdateInstructionInCache(new UpdateInstructionParams(s, s2, s3, s4, s5, s6, s7, s8));
    }
    
    public byte[] getFirmware(final UpdateStepInfo updateStepInfo) {
        return this.loadFirmwareFromCache(updateStepInfo.firmwareDownloadURL);
    }
    
    @Override
    public String getServiceName() {
        return "firmware";
    }
    
    public UpdateInstruction getUpdateInstructionFromCache(final UpdateInstructionParams updateInstructionParams) {
        final UpdateInstructionCacheNode checkUpdateInstructionInCache = this.checkUpdateInstructionInCache(updateInstructionParams);
        UpdateInstruction updateInstruction;
        if (checkUpdateInstructionInCache != null) {
            updateInstruction = checkUpdateInstructionInCache.updateInstruction;
        }
        else {
            updateInstruction = null;
        }
        return updateInstruction;
    }
    
    @Override
    public void initService(final Context mContext, final Bundle bundle) {
        this.mContext = mContext;
        if (bundle != null && bundle.containsKey("service_location")) {
            this.mServiceLocation = bundle.getString("service_location");
        }
        try {
            this.mDiskLruCache = DiskLruCache.open(mContext.getFilesDir(), 0, 1, 52428800L);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void initService(final Context mContext, final String mServiceLocation) {
        this.mContext = mContext;
        this.mServiceLocation = mServiceLocation;
        try {
            this.mDiskLruCache = DiskLruCache.open(mContext.getFilesDir(), 0, 1, 52428800L);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isUpdateReady(final UpdateInstruction updateInstruction) {
        boolean b2;
        final boolean b = b2 = false;
        if (updateInstruction != null) {
            b2 = b;
            if (updateInstruction.isUpdateAvailable) {
                final Iterator<UpdateStepInfo> iterator = updateInstruction.updateStepInfoList.iterator();
                while (iterator.hasNext()) {
                    if (!this.isUpdateStepReady(iterator.next())) {
                        b2 = b;
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    public boolean isUpdateStepReady(final UpdateStepInfo updateStepInfo) {
        return this.isFirmwareInCache(updateStepInfo.firmwareDownloadURL);
    }
    
    public void saveFirmwareInCache(final String s, final byte[] b) {
        final Object mDiskLruMonitor = this.mDiskLruMonitor;
        // monitorenter(mDiskLruMonitor)
        try {
            try {
                final DiskLruCache.Editor edit = this.mDiskLruCache.edit(Utils.MD5(s).toLowerCase());
                edit.newOutputStream(0).write(b);
                edit.commit();
            }
            finally {
            }
            // monitorexit(mDiskLruMonitor)
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void syncService(final ServiceInfo serviceInfo) {
        this.mServiceLocation = serviceInfo.location;
    }
}
