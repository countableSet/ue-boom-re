// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.tasks;

import com.logitech.ue.centurion.interfaces.IUEDeviceFactory;
import android.content.Context;
import com.logitech.ue.centurion.UESimpleDeviceFactory;
import com.logitech.ue.App;
import com.logitech.ue.UEKoraDevice;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import java.util.HashMap;
import android.util.Log;
import android.os.Looper;
import com.logitech.ue.centurion.UEDeviceManager;
import android.os.AsyncTask;

public class InitManagerTask extends AsyncTask<Void, Void, UEDeviceManager>
{
    private static final String TAG;
    
    static {
        TAG = InitManagerTask.class.getSimpleName();
    }
    
    protected UEDeviceManager doInBackground(final Void... array) {
        while (true) {
            try {
                Looper.prepare();
                final UEDeviceManager instance = UEDeviceManager.getInstance();
                if (instance.isReady()) {
                    Log.e(InitManagerTask.TAG, "UEDeviceManager is already inited");
                }
                else {
                    final HashMap<String, Class> hashMap = new HashMap<String, Class>();
                    hashMap.put(UEDeviceType.Kora.getDeviceIDPattern(), UEKoraDevice.class);
                    hashMap.put(UEDeviceType.Maximus.getDeviceIDPattern(), UEKoraDevice.class);
                    instance.init((Context)App.getInstance(), new UESimpleDeviceFactory(hashMap), false);
                }
                return instance;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
}
