// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.testing;

import android.util.SparseArray;
import java.util.Collection;
import com.logitech.ue.centurion.notification.UEBroadcastStatusNotification;
import android.util.Log;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.notification.UESetReceiverOneAttributeNotification;
import com.logitech.ue.centurion.notification.UEReceiverRemovedNotification;
import com.logitech.ue.centurion.notification.UEReceiverVariableAttributesNotification;
import com.logitech.ue.centurion.notification.UEGetReceiverOneAttributeNotification;
import com.logitech.ue.centurion.notification.UEReceiverFixedAttributesNotification;
import android.widget.Toast;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Parcelable;
import com.logitech.ue.centurion.notification.UEReceiverAddedNotification;
import android.content.Intent;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverStatus;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.centurion.device.devicedata.UEReceiverAttribute;
import android.os.Looper;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastReceiverInfo;
import com.logitech.ue.centurion.utils.MAC;
import java.util.LinkedHashMap;
import java.util.Random;
import android.os.Handler;
import android.content.Context;

public class BroadcastEmulator
{
    private static final int MAX_ADDED_SPEAKERS_PER_ITERATION = 3;
    private static final int MAX_DELAY_TIME = 30000;
    private static final int MAX_DEVICES = 20;
    private static final int MIN_DELAY_TIME = 2000;
    private static final float SUCCESS_RATE = 0.5f;
    private static final String TAG;
    private static volatile BroadcastEmulator mInstance;
    Context mContext;
    volatile int mDevicesCounter;
    Handler mEventHandler;
    final Handler mUIThreadHandler;
    Random random;
    LinkedHashMap<MAC, ReceiverAttributes> receiverAttributesList;
    LinkedHashMap<MAC, UEBroadcastReceiverInfo> receiverInfoList;
    
    static {
        TAG = BroadcastEmulator.class.getSimpleName();
        BroadcastEmulator.mInstance = null;
    }
    
    protected BroadcastEmulator() {
        this.mUIThreadHandler = new Handler(Looper.getMainLooper());
        this.mDevicesCounter = 0;
        this.random = new Random();
        this.receiverInfoList = new LinkedHashMap<MAC, UEBroadcastReceiverInfo>();
        this.receiverAttributesList = new LinkedHashMap<MAC, ReceiverAttributes>();
    }
    
    private byte[] createAttributeValue(int mNameRevision, final UEBroadcastReceiverInfo ueBroadcastReceiverInfo) {
        final int n = 1;
        final byte[] array = null;
        byte[] bytes = null;
        Label_0079: {
            switch (UEReceiverAttribute.getReceiverAttribute(mNameRevision)) {
                default: {
                    bytes = array;
                    break;
                }
                case Color: {
                    bytes = new byte[] { (byte)(ueBroadcastReceiverInfo.mDeviceColor >>> 8), (byte)ueBroadcastReceiverInfo.mDeviceColor };
                    break;
                }
                case Flags: {
                    final byte b = (byte)ueBroadcastReceiverInfo.mStatus.getCode();
                    mNameRevision = ueBroadcastReceiverInfo.mNameRevision;
                    bytes = new byte[] { (byte)(mNameRevision << 4 | b) };
                    break;
                }
                case Volume: {
                    final byte[] array2 = { 0 };
                    if (ueBroadcastReceiverInfo.mNonzeroVolumeOffset) {
                        mNameRevision = n;
                    }
                    else {
                        mNameRevision = 0;
                    }
                    array2[0] = (byte)mNameRevision;
                    bytes = array2;
                    break;
                }
                case Name: {
                    bytes = ("test" + ueBroadcastReceiverInfo.mDeviceColor).getBytes();
                    break;
                }
                case Battery: {
                    bytes = new byte[] { (byte)ueBroadcastReceiverInfo.mBatteryLevel };
                    break;
                }
                case FWversion: {
                    bytes = new byte[] { 1, 0, 0, 0, 1, 0 };
                    break;
                }
                case HWtype: {
                    switch (UEColour.getDeviceTypeByColour(ueBroadcastReceiverInfo.mDeviceColor)) {
                        default: {
                            bytes = new byte[] { 1 };
                            break Label_0079;
                        }
                        case Kora: {
                            bytes = new byte[] { 1 };
                            break Label_0079;
                        }
                        case Maximus: {
                            bytes = new byte[] { 4 };
                            break Label_0079;
                        }
                        case Titus: {
                            bytes = new byte[] { 2 };
                            break Label_0079;
                        }
                        case Caribe: {
                            bytes = new byte[] { 3 };
                            break Label_0079;
                        }
                    }
                    break;
                }
                case HWversion: {
                    switch (UEColour.getDeviceTypeByColour(ueBroadcastReceiverInfo.mDeviceColor)) {
                        default: {
                            bytes = new byte[] { (byte)this.random.nextInt(2) };
                            break Label_0079;
                        }
                        case Kora: {
                            bytes = new byte[] { (byte)this.random.nextInt(2) };
                            break Label_0079;
                        }
                        case Maximus: {
                            bytes = new byte[] { (byte)this.random.nextInt(3) };
                            break Label_0079;
                        }
                        case Titus: {
                            bytes = new byte[] { 0 };
                            break Label_0079;
                        }
                        case Caribe: {
                            bytes = new byte[] { (byte)this.random.nextInt(2) };
                            break Label_0079;
                        }
                    }
                    break;
                }
                case XUPversion: {
                    bytes = new byte[] { 1 };
                    break;
                }
                case BroadcasterAddress: {
                    final byte[] array3 = new byte[ueBroadcastReceiverInfo.mAddress.getBytes().length];
                    System.arraycopy(ueBroadcastReceiverInfo.mAddress.getBytes(), 0, array3, 0, ueBroadcastReceiverInfo.mAddress.getBytes().length);
                    bytes = array3;
                    break;
                }
                case AutoConnect: {
                    final byte[] array4 = new byte[7];
                    System.arraycopy(ueBroadcastReceiverInfo.mAddress.getBytes(), 0, array4, 0, ueBroadcastReceiverInfo.mAddress.getBytes().length);
                    array4[6] = (byte)this.random.nextInt(2);
                    bytes = array4;
                    break;
                }
            }
        }
        return bytes;
    }
    
    private int getConfirmationDelay() {
        return this.random.nextInt(28000) + 2000;
    }
    
    public static BroadcastEmulator getInstance() {
        Label_0030: {
            if (BroadcastEmulator.mInstance != null) {
                break Label_0030;
            }
            synchronized (BroadcastEmulator.class) {
                if (BroadcastEmulator.mInstance == null) {
                    BroadcastEmulator.mInstance = new BroadcastEmulator();
                }
                return BroadcastEmulator.mInstance;
            }
        }
    }
    
    private boolean isSuccess() {
        return this.random.nextFloat() > 0.5f;
    }
    
    public void addReceiverToBroadcast(final MAC key) {
        final UEBroadcastReceiverInfo ueBroadcastReceiverInfo = this.receiverInfoList.get(key);
        final boolean success = this.isSuccess();
        final int confirmationDelay = this.getConfirmationDelay();
        this.mEventHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (success) {
                    ueBroadcastReceiverInfo.mStatus = UEBroadcastReceiverStatus.PLAYING_THIS_BROADCAST;
                }
                final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION");
                final MAC val$targetAddress = key;
                UEReceiverAddedNotification.ExecutionStatus executionStatus;
                if (success) {
                    executionStatus = UEReceiverAddedNotification.ExecutionStatus.DELIVERED;
                }
                else {
                    executionStatus = UEReceiverAddedNotification.ExecutionStatus.CONNECTION_FAILED;
                }
                intent.putExtra("notification", (Parcelable)new UEReceiverAddedNotification(val$targetAddress, executionStatus));
                LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            }
        }, (long)confirmationDelay);
        this.mUIThreadHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastEmulator.this.mContext, (CharSequence)String.format("Add receiver %s in %d msec", BroadcastEmulator.this.successOrFailString(success), confirmationDelay), 1).show();
            }
        }, 100L);
    }
    
    public void getReceiverFixedAttributes(final MAC key) {
        final UEBroadcastReceiverInfo ueBroadcastReceiverInfo = this.receiverInfoList.get(key);
        final boolean success = this.isSuccess();
        final int confirmationDelay = this.getConfirmationDelay();
        this.mEventHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION");
                intent.putExtra("notification", (Parcelable)new UEReceiverFixedAttributesNotification(key, success));
                LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            }
        }, (long)confirmationDelay);
        this.mUIThreadHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastEmulator.this.mContext, (CharSequence)String.format("Fixed attributes notification %s in %d msec", success, confirmationDelay), 1).show();
            }
        }, 100L);
    }
    
    public void getReceiverOneAttribute(final MAC mac, final int n) {
        final UEBroadcastReceiverInfo ueBroadcastReceiverInfo = this.receiverInfoList.get(mac);
        final boolean success = this.isSuccess();
        final int confirmationDelay = this.getConfirmationDelay();
        byte[] array = null;
        if (success) {
            ReceiverAttributes receiverAttributes;
            if ((receiverAttributes = this.receiverAttributesList.get(mac)) == null) {
                receiverAttributes = new ReceiverAttributes();
            }
            if ((array = receiverAttributes.getAttribute(n)) == null) {
                array = this.createAttributeValue(n, ueBroadcastReceiverInfo);
            }
        }
        this.mEventHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_GET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION");
                intent.putExtra("notification", (Parcelable)new UEGetReceiverOneAttributeNotification(mac, n, array, success));
                LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            }
        }, (long)confirmationDelay);
        this.mUIThreadHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastEmulator.this.mContext, (CharSequence)String.format("One attribute %d received %s in %d msec", n, success, confirmationDelay), 1).show();
            }
        }, 100L);
    }
    
    public void getReceiverVariableAttributes(final MAC key) {
        final UEBroadcastReceiverInfo ueBroadcastReceiverInfo = this.receiverInfoList.get(key);
        final boolean success = this.isSuccess();
        final int confirmationDelay = this.getConfirmationDelay();
        this.mEventHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_RECEIVER_VARIABLE_ATTRIBUTES_NOTIFICATION");
                intent.putExtra("notification", (Parcelable)new UEReceiverVariableAttributesNotification(key, success));
                LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            }
        }, (long)confirmationDelay);
        this.mUIThreadHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastEmulator.this.mContext, (CharSequence)String.format("Variable attributes notification %s in %d msec", success, confirmationDelay), 1).show();
            }
        }, 100L);
    }
    
    public void init(final Context mContext) {
        this.mEventHandler = new Handler(Looper.getMainLooper());
        this.mContext = mContext;
    }
    
    public void removeReceiverFromBroadcast(final MAC key) {
        final UEBroadcastReceiverInfo ueBroadcastReceiverInfo = this.receiverInfoList.get(key);
        final boolean success = this.isSuccess();
        final int confirmationDelay = this.getConfirmationDelay();
        this.mEventHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                if (success) {
                    ueBroadcastReceiverInfo.mStatus = UEBroadcastReceiverStatus.UNKNOWN;
                }
                final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION");
                intent.putExtra("notification", (Parcelable)new UEReceiverRemovedNotification(key, success));
                LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            }
        }, (long)confirmationDelay);
        this.mUIThreadHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastEmulator.this.mContext, (CharSequence)String.format("Remove receiver %s in %d msec", BroadcastEmulator.this.successOrFailString(success), confirmationDelay), 1).show();
            }
        }, 100L);
    }
    
    public void setReceiverOneAttribute(final MAC key, final int n, final byte[] array) {
        final UEBroadcastReceiverInfo ueBroadcastReceiverInfo = this.receiverInfoList.get(key);
        final boolean success = this.isSuccess();
        final int confirmationDelay = this.getConfirmationDelay();
        this.mEventHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_SET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION");
                intent.putExtra("notification", (Parcelable)new UESetReceiverOneAttributeNotification(key, success));
                LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            }
        }, (long)confirmationDelay);
        if (success) {
            ReceiverAttributes value;
            if ((value = this.receiverAttributesList.get(key)) == null) {
                value = new ReceiverAttributes();
            }
            value.setAttribute(n, array);
            this.receiverAttributesList.put(key, value);
        }
        this.mUIThreadHandler.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BroadcastEmulator.this.mContext, (CharSequence)String.format("One attribute %d set %s in %d msec", n, success, confirmationDelay), 1).show();
            }
        }, 100L);
    }
    
    public void startDiscovery() {
        this.stop();
        this.mEventHandler.postDelayed((Runnable)new BroadcastDiscoveryEmulatorTask(), 5000L);
    }
    
    public void stop() {
        this.mEventHandler.removeCallbacksAndMessages((Object)null);
    }
    
    String successOrFailString(final boolean b) {
        String s;
        if (b) {
            s = "SUCCESS";
        }
        else {
            s = "FAIL";
        }
        return s;
    }
    
    class BroadcastDiscoveryEmulatorTask implements Runnable
    {
        public BroadcastDiscoveryEmulatorTask() {
        }
        
        public UEBroadcastReceiverInfo generateRandomReceiver() {
            return new UEBroadcastReceiverInfo(this.getRandomMac(), this.getRandomDeviceColor(), BroadcastEmulator.this.random.nextBoolean(), BroadcastEmulator.this.random.nextInt(), BroadcastEmulator.this.random.nextInt(), UEBroadcastReceiverStatus.CONNECTED_NO_STREAMING);
        }
        
        public int getRandomDeviceColor() {
            final UEColour[] values = UEColour.values();
            UEColour ueColour;
            do {
                ueColour = values[BroadcastEmulator.this.random.nextInt(values.length - 3)];
            } while (UEColour.getDeviceTypeByColour(ueColour.getCode()) != UEDeviceType.Kora && UEColour.getDeviceTypeByColour(ueColour.getCode()) != UEDeviceType.Maximus && UEColour.getDeviceTypeByColour(ueColour.getCode()) != UEDeviceType.Titus);
            return ueColour.getCode();
        }
        
        public MAC getRandomMac() {
            final byte[] bytes = new byte[6];
            BroadcastEmulator.this.random.nextBytes(bytes);
            return new MAC(bytes);
        }
        
        @Override
        public void run() {
            final int nextInt = BroadcastEmulator.this.random.nextInt(3);
            Log.d(BroadcastEmulator.TAG, String.format("Run broadcastDiscovery emulation iteration %d add %d devices", BroadcastEmulator.this.mDevicesCounter, nextInt));
            final UEBroadcastStatusNotification ueBroadcastStatusNotification = new UEBroadcastStatusNotification();
            if (BroadcastEmulator.this.mDevicesCounter < 20) {
                for (int i = 0; i < nextInt; ++i) {
                    if (BroadcastEmulator.this.mDevicesCounter < 20) {
                        final UEBroadcastReceiverInfo generateRandomReceiver = this.generateRandomReceiver();
                        BroadcastEmulator.this.receiverInfoList.put(generateRandomReceiver.mAddress, generateRandomReceiver);
                        final BroadcastEmulator this$0 = BroadcastEmulator.this;
                        ++this$0.mDevicesCounter;
                    }
                }
            }
            ueBroadcastStatusNotification.getReceiversList().addAll(BroadcastEmulator.this.receiverInfoList.values());
            final Intent intent = new Intent("com.logitech.ue.centurion.ACTION_BROADCAST_STATUS_NOTIFICATION");
            intent.putExtra("notification", (Parcelable)ueBroadcastStatusNotification);
            LocalBroadcastManager.getInstance(BroadcastEmulator.this.mContext).sendBroadcast(intent);
            BroadcastEmulator.this.mEventHandler.postDelayed((Runnable)this, (long)((BroadcastEmulator.this.random.nextInt(3) + 2) * 1000));
        }
    }
    
    class ReceiverAttributes
    {
        private SparseArray<byte[]> mAttributes;
        
        ReceiverAttributes() {
            this.mAttributes = (SparseArray<byte[]>)new SparseArray();
        }
        
        public byte[] getAttribute(final int n) {
            return (byte[])this.mAttributes.get(n);
        }
        
        public byte[] getAttribute(final UEReceiverAttribute ueReceiverAttribute) {
            byte[] attribute;
            if (ueReceiverAttribute != null) {
                attribute = this.getAttribute(ueReceiverAttribute.getAttributeCode());
            }
            else {
                attribute = null;
            }
            return attribute;
        }
        
        public void setAttribute(final int n, final byte[] array) {
            this.mAttributes.put(n, (Object)array);
        }
        
        public void setAttribute(final UEReceiverAttribute ueReceiverAttribute, final byte[] array) {
            if (ueReceiverAttribute != null) {
                this.mAttributes.put(ueReceiverAttribute.getAttributeCode(), (Object)array);
            }
        }
    }
}
