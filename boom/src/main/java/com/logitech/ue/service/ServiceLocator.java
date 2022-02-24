// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.service;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import android.content.SharedPreferences$Editor;
import javax.xml.xpath.XPathExpressionException;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public class ServiceLocator implements IUEService
{
    public static final String MANIFEST_XML_KEY = "manifest_xml";
    public static final String PARAM_URL_KEY = "url";
    public static final String TAG;
    private static volatile ServiceLocator instance;
    private Context mContext;
    private String mServiceUrl;
    private ServiceLocatorManifest manifest;
    private ManifestSyncTask manifestSyncTask;
    public List<IUEService> serviceSyncList;
    
    static {
        TAG = ServiceLocator.class.getSimpleName();
    }
    
    public ServiceLocator() {
        this.serviceSyncList = new ArrayList<IUEService>();
    }
    
    public static Bundle buildParamBundle(final String s) {
        final Bundle bundle = new Bundle();
        bundle.putString("url", s);
        return bundle;
    }
    
    public static ServiceLocator getInstance() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_0       
        //     4: aload_0        
        //     5: astore_1       
        //     6: aload_0        
        //     7: ifnonnull       38
        //    10: ldc             Landroid/app/NotificationManager;.class
        //    12: monitorenter   
        //    13: getstatic       com/logitech/ue/service/ServiceLocator.instance:Lcom/logitech/ue/service/ServiceLocator;
        //    16: astore_0       
        //    17: aload_0        
        //    18: astore_1       
        //    19: aload_0        
        //    20: ifnonnull       35
        //    23: new             Lcom/logitech/ue/service/ServiceLocator;
        //    26: astore_1       
        //    27: aload_1        
        //    28: invokespecial   com/logitech/ue/service/ServiceLocator.<init>:()V
        //    31: aload_1        
        //    32: putstatic       com/logitech/ue/service/ServiceLocator.instance:Lcom/logitech/ue/service/ServiceLocator;
        //    35: ldc             Landroid/app/NotificationManager;.class
        //    37: monitorexit    
        //    38: aload_1        
        //    39: areturn        
        //    40: astore_1       
        //    41: ldc             Landroid/app/NotificationManager;.class
        //    43: monitorexit    
        //    44: aload_1        
        //    45: athrow         
        //    46: astore_1       
        //    47: goto            41
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  13     17     40     41     Any
        //  23     31     40     41     Any
        //  31     35     46     50     Any
        //  35     38     40     41     Any
        //  41     44     40     41     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0035:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void beginSync() {
        while (true) {
            if (this.manifest != null) {
                break Label_0024;
            }
            final String manifestXML = this.getManifestXML();
            if (manifestXML == null) {
                break Label_0024;
            }
            try {
                this.syncServices(ServiceLocatorManifest.readFromXML(manifestXML));
                if (this.manifestSyncTask == null) {
                    (this.manifestSyncTask = new ManifestSyncTask()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
                }
            }
            catch (XPathExpressionException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }
    
    public String getManifestXML() {
        return this.mContext.getSharedPreferences(ServiceLocator.TAG, 0).getString("manifest_xml", (String)null);
    }
    
    @Override
    public String getServiceName() {
        return "com.logitech.ue.service locator";
    }
    
    @Override
    public void initService(final Context mContext, final Bundle bundle) {
        this.mContext = mContext;
        this.mServiceUrl = bundle.getString("url");
    }
    
    public void registerServiceForSync(final IUEService iueService) {
        this.serviceSyncList.add(iueService);
    }
    
    public void setManifestXML(final String s) {
        final SharedPreferences$Editor edit = this.mContext.getSharedPreferences(ServiceLocator.TAG, 0).edit();
        edit.putString("manifest_xml", s);
        edit.apply();
    }
    
    @Override
    public void syncService(final ServiceInfo serviceInfo) {
    }
    
    public void syncServices(final ServiceLocatorManifest manifest) {
        for (final ServiceInfo serviceInfo : manifest.serviceInfoList) {
            for (final IUEService iueService : this.serviceSyncList) {
                if (serviceInfo.name != null && serviceInfo.name.equals(iueService.getServiceName())) {
                    iueService.syncService(serviceInfo);
                }
            }
        }
        this.manifest = manifest;
    }
    
    public class ManifestSyncTask extends AsyncTask<Void, Void, HashMap<String, Object>>
    {
        public static final String MANIFEST_KEY = "manifest";
        public static final String MANIFEST_XML_KEY = "manifest xml";
        
        protected HashMap<String, Object> doInBackground(final Void... array) {
            Log.i(ServiceLocator.TAG, "Begin fetch notification. Server URL: " + ServiceLocator.this.mServiceUrl);
            Label_0224: {
                StringBuilder sb = null;
                Label_0157: {
                    HashMap<String, Object> hashMap;
                    try {
                        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(ServiceLocator.this.mServiceUrl).openConnection();
                        httpURLConnection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
                        httpURLConnection.setRequestProperty("Pragma", "no-cache");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.connect();
                        final InputStream inputStream = httpURLConnection.getInputStream();
                        if (inputStream == null || httpURLConnection.getResponseCode() != 200) {
                            break Label_0224;
                        }
                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        sb = new StringBuilder();
                        while (true) {
                            final String line = bufferedReader.readLine();
                            if (line == null) {
                                break Label_0157;
                            }
                            sb.append(line);
                        }
                    }
                    catch (Exception ex) {
                        Log.e(ServiceLocator.TAG, "Sync fetch failed");
                        hashMap = null;
                    }
                    return hashMap;
                }
                final String string = sb.toString();
                Log.i(ServiceLocator.TAG, "Received HTTP result. Result: " + string);
                final ServiceLocatorManifest fromXML = ServiceLocatorManifest.readFromXML(string);
                HashMap<String, Object> hashMap = (HashMap<String, Object>)new HashMap<String, String>();
                hashMap.put("manifest", fromXML);
                hashMap.put("manifest xml", string);
                return hashMap;
            }
            Log.w(ServiceLocator.TAG, "Sync fetch invalid");
            return null;
        }
        
        protected void onPostExecute(final HashMap<String, Object> hashMap) {
            if (hashMap != null) {
                ServiceLocator.this.setManifestXML(hashMap.get("manifest xml"));
                ServiceLocator.this.syncServices((ServiceLocatorManifest)hashMap.get("manifest"));
            }
            ServiceLocator.this.manifestSyncTask = null;
        }
    }
}
