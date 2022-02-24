// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.firmware;

import java.io.InputStream;
import com.logitech.ue.util.Utils;
import java.net.URL;
import java.net.HttpURLConnection;
import android.util.Log;
import android.os.AsyncTask;

public class CheckUpdateInstructionTask extends AsyncTask<Void, Void, Object>
{
    private static final String TAG;
    String requestURL;
    
    static {
        TAG = CheckUpdateInstructionTask.class.getSimpleName();
    }
    
    public CheckUpdateInstructionTask(final String requestURL) {
        this.requestURL = requestURL;
    }
    
    protected Object doInBackground(final Void... array) {
        while (true) {
            try {
                Log.d(CheckUpdateInstructionTask.TAG, "Begin update instruction check task");
                final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(this.requestURL).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                final InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream != null) {
                    final int responseCode = httpURLConnection.getResponseCode();
                    Object o;
                    if (responseCode == 200) {
                        o = UpdateInstruction.buildFromJSONString(Utils.readStreamAsString(inputStream, "UTF-8"));
                    }
                    else if (responseCode == 500) {
                        o = UpdateRequestError.buildFromJSONString(Utils.readStreamAsString(inputStream, "UTF-8"));
                    }
                    else {
                        o = new Exception("HTTP request failed. Response code " + responseCode);
                    }
                    return o;
                }
            }
            catch (Exception o) {
                Log.d(CheckUpdateInstructionTask.TAG, "Firmware check failed", (Throwable)o);
                return o;
            }
            return new Exception("HTTP request failed. Request stream is null");
        }
    }
}
