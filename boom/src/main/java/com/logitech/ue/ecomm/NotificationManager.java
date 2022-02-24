// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.ecomm;

import java.io.InputStream;
import com.logitech.ue.ecomm.model.Notification;
import org.json.JSONObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Iterator;
import com.logitech.ue.ecomm.model.NotificationStory;
import com.logitech.ue.service.ServiceInfo;
import org.json.JSONException;
import org.json.JSONArray;
import android.util.Log;
import android.os.Bundle;
import android.os.AsyncTask;
import com.logitech.ue.ecomm.model.NotificationParams;
import com.logitech.ue.ecomm.model.NotificationHistory;
import android.content.Context;
import com.logitech.ue.service.IUEService;

public class NotificationManager implements IUEService
{
    public static final int HISTORY_MAX_SIZE = 8;
    private static final String PREFERENCES_NAME = "notificationPrefs";
    private static final String TAG;
    private static final String USER_PREFERENCES_HISTORY = "userPreferencesHistory";
    private static volatile NotificationManager instance;
    private Context mContext;
    private NotificationHistory mHistory;
    public String serverURL;
    
    static {
        TAG = NotificationManager.class.getSimpleName();
    }
    
    public NotificationManager() {
        this.serverURL = "http://ec2uebeapp01.idc.logitech.com/c/handler/check/notification";
    }
    
    public static NotificationManager getInstance() {
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
        //    10: ldc             Lcom/logitech/ue/ecomm/NotificationManager;.class
        //    12: monitorenter   
        //    13: getstatic       com/logitech/ue/ecomm/NotificationManager.instance:Lcom/logitech/ue/ecomm/NotificationManager;
        //    16: astore_0       
        //    17: aload_0        
        //    18: astore_1       
        //    19: aload_0        
        //    20: ifnonnull       35
        //    23: new             Lcom/logitech/ue/ecomm/NotificationManager;
        //    26: astore_1       
        //    27: aload_1        
        //    28: invokespecial   com/logitech/ue/ecomm/NotificationManager.<init>:()V
        //    31: aload_1        
        //    32: putstatic       com/logitech/ue/ecomm/NotificationManager.instance:Lcom/logitech/ue/ecomm/NotificationManager;
        //    35: ldc             Lcom/logitech/ue/ecomm/NotificationManager;.class
        //    37: monitorexit    
        //    38: aload_1        
        //    39: areturn        
        //    40: astore_1       
        //    41: ldc             Lcom/logitech/ue/ecomm/NotificationManager;.class
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
    
    private void saveHistory() {
        this.mContext.getSharedPreferences("notificationPrefs", 0).edit().putString("userPreferencesHistory", this.mHistory.toJSONObject().toString()).apply();
        this.mHistory = this.loadHistory();
    }
    
    public void fetchNotification(final NotificationParams notificationParams, final INotificationFetchHandler notificationFetchHandler) {
        new FetchNotificationTask(notificationParams, notificationFetchHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
    }
    
    public NotificationHistory getHistory() {
        return this.mHistory;
    }
    
    @Override
    public String getServiceName() {
        return "notice";
    }
    
    @Override
    public void initService(final Context mContext, final Bundle bundle) {
        this.mContext = mContext;
        this.mHistory = this.loadHistory();
    }
    
    public NotificationHistory loadHistory() {
        final String string = this.mContext.getSharedPreferences("notificationPrefs", 0).getString("userPreferencesHistory", (String)null);
        Log.d(NotificationManager.TAG, "Notification history: " + string);
        if (string == null) {
            return new NotificationHistory(8);
        }
        try {
            final NotificationHistory notificationHistory = new NotificationHistory(new JSONArray(string));
            notificationHistory.setMaxSize(8);
            return notificationHistory;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return new NotificationHistory(8);
    }
    
    @Override
    public void syncService(final ServiceInfo serviceInfo) {
        this.serverURL = serviceInfo.location;
    }
    
    public void updateStory(final NotificationStory notificationStory) {
        final Iterator<NotificationStory> iterator = this.mHistory.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == notificationStory) {
                this.saveHistory();
                return;
            }
        }
        this.mHistory.add(notificationStory);
        this.saveHistory();
    }
    
    class FetchNotificationTask extends AsyncTask<Void, Void, Object>
    {
        INotificationFetchHandler fetchHandler;
        NotificationParams params;
        
        public FetchNotificationTask(final NotificationParams params, final INotificationFetchHandler fetchHandler) {
            this.params = params;
            this.fetchHandler = fetchHandler;
        }
        
        protected Object doInBackground(final Void... array) {
            Object o = null;
            Log.i(NotificationManager.TAG, "Begin fetch notification. Server URL: " + NotificationManager.this.serverURL);
            Label_0346: {
                StringBuilder sb = null;
                Label_0262: {
                    try {
                        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(NotificationManager.this.serverURL).openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("Accept", "application/json");
                        httpURLConnection.setRequestProperty("Content-type", "application/json");
                        httpURLConnection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
                        httpURLConnection.setRequestProperty("Pragma", "no-cache");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);
                        final String string = this.params.toJSONObject().toString();
                        final DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                        Log.i(NotificationManager.TAG, "Sending params: " + string);
                        dataOutputStream.write(string.getBytes("UTF-8"));
                        httpURLConnection.connect();
                        final InputStream inputStream = httpURLConnection.getInputStream();
                        if (inputStream == null || httpURLConnection.getResponseCode() != 200) {
                            break Label_0346;
                        }
                        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        sb = new StringBuilder();
                        while (true) {
                            final String line = bufferedReader.readLine();
                            if (line == null) {
                                break Label_0262;
                            }
                            sb.append(line);
                        }
                    }
                    catch (Exception o) {
                        Log.e(NotificationManager.TAG, "Fetch error");
                        ((Throwable)o).printStackTrace();
                    }
                    return o;
                }
                Log.i(NotificationManager.TAG, "Received fetch result. Result: " + sb.toString());
                final JSONObject jsonObject = new JSONObject(sb.toString());
                if (jsonObject.keys().hasNext()) {
                    o = new Notification(jsonObject);
                    ((Notification)o).params = this.params;
                    return o;
                }
                return o;
            }
            Log.w(NotificationManager.TAG, "Fetch invalid");
            return o;
        }
        
        protected void onPostExecute(final Object o) {
            if (this.fetchHandler != null) {
                if (o instanceof Notification) {
                    this.fetchHandler.onReceiveNotification((Notification)o);
                }
                else if (o instanceof Exception) {
                    this.fetchHandler.onNoNotificationReceived((Exception)o);
                }
                else {
                    this.fetchHandler.onNoNotificationReceived((Exception)o);
                }
            }
        }
    }
}
