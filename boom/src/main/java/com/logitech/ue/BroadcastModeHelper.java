// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import com.logitech.ue.centurion.device.devicedata.UEAckResponse;
import com.logitech.ue.centurion.exceptions.UEErrorResultException;
import com.logitech.ue.centurion.UEDeviceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.os.Looper;
import java.util.Iterator;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.device.devicedata.UEBroadcastAudioMode;
import com.logitech.ue.centurion.utils.MAC;
import android.os.HandlerThread;
import com.logitech.ue.centurion.notification.UEReceiverFixedAttributesNotification;
import com.logitech.ue.centurion.notification.UEReceiverRemovedNotification;
import com.logitech.ue.centurion.notification.UEReceiverAddedNotification;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import android.util.Log;
import android.content.Intent;
import android.os.Handler;
import android.content.Context;
import java.util.LinkedList;
import android.content.BroadcastReceiver;

public class BroadcastModeHelper
{
    public static final int BLE_TIMEOUT = 60000;
    public static final int MESSAGE_DELAY = 5000;
    public static final int PRIORITY_HIGH = 10;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 5;
    public static final int REQUEST_TIMEOUT = 20000;
    private static final String TAG;
    BroadcastReceiver mBroadcastReceiver;
    private final LinkedList<BroadcastCommand> mCommandQueue;
    private Context mContext;
    private Handler mHandler;
    private long mLastSendMessage;
    private ExecutionListener mListener;
    private volatile State mState;
    private Handler mWorkerHandler;
    
    static {
        TAG = BroadcastModeHelper.class.getSimpleName();
    }
    
    public BroadcastModeHelper() {
        this.mState = State.NOT_STARTED;
        this.mCommandQueue = new LinkedList<BroadcastCommand>();
        this.mLastSendMessage = 0L;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(BroadcastModeHelper.TAG, "Receive broadcast " + intent.getAction());
                if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION")) {
                    Log.d(BroadcastModeHelper.TAG, "ACK. Member was added to broadcast");
                    BroadcastModeHelper.this.processResultNotification(UEDeviceCommand.UECommand.AddReceiverToBroadcast, ((UEReceiverAddedNotification)intent.getParcelableExtra("notification")).getAddress());
                }
                else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION")) {
                    Log.d(BroadcastModeHelper.TAG, "ACK. Member was removed to broadcast");
                    BroadcastModeHelper.this.processResultNotification(UEDeviceCommand.UECommand.RemoveReceiverFromBroadcast, ((UEReceiverRemovedNotification)intent.getParcelableExtra("notification")).getAddress());
                }
                else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION")) {
                    Log.d(BroadcastModeHelper.TAG, "ACK. Get fixed attributes");
                    BroadcastModeHelper.this.processResultNotification(UEDeviceCommand.UECommand.FixedReceiverAttributesNotification, ((UEReceiverFixedAttributesNotification)intent.getParcelableExtra("notification")).getAddress());
                }
                else if (intent.getAction().equals("com.logitech.ue.centurion.ACTION_BLE_AVAILABLE_NOTIFICATION")) {
                    if (BroadcastModeHelper.this.mState == State.WAITING_FOR_BLE) {
                        Log.d(BroadcastModeHelper.TAG, "Broadcaster is now idle. Re-queue current message");
                        BroadcastModeHelper.this.setState(State.IDLE);
                        BroadcastModeHelper.this.add((BroadcastCommand)BroadcastModeHelper.this.mCommandQueue.pop());
                    }
                    else {
                        Log.d(BroadcastModeHelper.TAG, "Broadcaster is now idle. But we are not waiting for him");
                    }
                }
            }
        };
        final HandlerThread handlerThread = new HandlerThread("broadcast_worker");
        handlerThread.start();
        this.mWorkerHandler = new Handler(handlerThread.getLooper());
    }
    
    private void add(final BroadcastCommand broadcastCommand) {
        Log.d(BroadcastModeHelper.TAG, String.format("Add command: %s Address: %s", broadcastCommand.command.name(), broadcastCommand.targetAddress));
        this.insertCommand(broadcastCommand);
        this.checkWorker();
    }
    
    private void checkWorker() {
        if (this.getState() == State.IDLE) {
            if (this.mCommandQueue.isEmpty()) {
                Log.d(BroadcastModeHelper.TAG, "No message found to execute");
            }
            else {
                final long n = System.currentTimeMillis() - this.mLastSendMessage;
                if (n > 5000L) {
                    Log.d(BroadcastModeHelper.TAG, String.format("Sending next message %s", this.getCurrentCommand().command));
                    this.setState(State.SENDING);
                    this.mWorkerHandler.post((Runnable)new WorkerRunnable());
                }
                else {
                    this.sendNextWithDelay(5000L - n);
                }
            }
        }
    }
    
    private BroadcastCommand getCurrentCommand() {
        return this.mCommandQueue.peek();
    }
    
    private void insertCommand(final BroadcastCommand broadcastCommand) {
        for (int i = 1; i < this.mCommandQueue.size(); ++i) {
            if (broadcastCommand.priority > this.mCommandQueue.get(i).priority) {
                this.mCommandQueue.add(i, broadcastCommand);
                return;
            }
        }
        this.mCommandQueue.add(broadcastCommand);
    }
    
    private void sendNext() {
        if (this.getState() == State.WAITING_FOR_RESPONSE || this.getState() == State.WAITING_FOR_BLE) {
            this.mCommandQueue.remove();
            if (this.mCommandQueue.isEmpty()) {
                Log.d(BroadcastModeHelper.TAG, "No messages to send");
                this.setState(State.IDLE);
            }
            else {
                this.sendNextWithDelay(5000L);
            }
        }
        else {
            Log.e(BroadcastModeHelper.TAG, "Invalid state to send next message. State: " + this.getState());
        }
    }
    
    private void sendNextWithDelay(final long l) {
        Log.d(BroadcastModeHelper.TAG, String.format("Sending next message %s with delay %d", this.getCurrentCommand().command, l));
        this.setState(State.PENDING);
        this.mHandler.postDelayed((Runnable)new SendDelayRunnable(), l);
    }
    
    private void setState(final State state) {
        synchronized (this) {
            Log.d(BroadcastModeHelper.TAG, "Change state to " + state);
            this.mState = state;
        }
    }
    
    public void addReceiverToBroadcast(final MAC mac, final UEBroadcastAudioMode ueBroadcastAudioMode) {
        this.removeCommandsForDevice(mac, UEDeviceCommand.UECommand.RemoveReceiverFromBroadcast, UEDeviceCommand.UECommand.AddReceiverToBroadcast);
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.AddReceiverToBroadcast, mac, new Object[] { ueBroadcastAudioMode }));
    }
    
    void executeCommand(final UEGenericDevice ueGenericDevice, final BroadcastCommand broadcastCommand) throws UEConnectionException, UEOperationException {
        Log.d(BroadcastModeHelper.TAG, String.format("Execute command: %s Address: %s", broadcastCommand.command.name(), broadcastCommand.targetAddress));
        switch (broadcastCommand.command) {
            case AddReceiverToBroadcast: {
                ueGenericDevice.addReceiverToBroadcast(broadcastCommand.targetAddress, (UEBroadcastAudioMode)broadcastCommand.params[0]);
                break;
            }
            case RemoveReceiverFromBroadcast: {
                ueGenericDevice.removeReceiverFromBroadcast(broadcastCommand.targetAddress);
                break;
            }
            case QueryReceiverVariableAttributes: {
                ueGenericDevice.getReceiverVariableAttributes(broadcastCommand.targetAddress);
                break;
            }
            case QueryReceiverFixedAttributes: {
                ueGenericDevice.getReceiverFixedAttributes(broadcastCommand.targetAddress);
                break;
            }
            case QueryReceiverOneAttribute: {
                ueGenericDevice.getReceiverOneAttribute(broadcastCommand.targetAddress, (int)broadcastCommand.params[0]);
                break;
            }
            case SetReceiverOneAttribute: {
                ueGenericDevice.setReceiverOneAttribute(broadcastCommand.targetAddress, (int)broadcastCommand.params[0], (byte[])broadcastCommand.params[1]);
                break;
            }
        }
    }
    
    public int getCommandPriority(final UEDeviceCommand.UECommand ueCommand) {
        int n = 10;
        switch (ueCommand) {
            default: {
                n = 1;
                return n;
            }
            case SetReceiverOneAttribute: {
                n = 5;
                return n;
            }
            case QueryReceiverOneAttribute: {
                n = 5;
                return n;
            }
            case QueryReceiverFixedAttributes: {
                n = 5;
                return n;
            }
            case QueryReceiverVariableAttributes: {
                n = 5;
            }
            case AddReceiverToBroadcast:
            case RemoveReceiverFromBroadcast: {
                return n;
            }
        }
    }
    
    public void getReceiverFixedAttributes(final MAC mac) {
        this.removeCommandsForDevice(mac, UEDeviceCommand.UECommand.QueryReceiverFixedAttributes);
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.QueryReceiverFixedAttributes, mac, null));
    }
    
    public void getReceiverOneAttribute(final MAC mac, final int i) {
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.QueryReceiverVariableAttributes, mac, new Object[] { i }));
    }
    
    public void getReceiverVariableAttributes(final MAC mac) {
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.QueryReceiverVariableAttributes, mac, null));
    }
    
    public State getState() {
        return this.mState;
    }
    
    protected void processResultNotification(final UEDeviceCommand.UECommand ueCommand, final MAC mac) {
        if (!this.mCommandQueue.isEmpty() && this.getCurrentCommand().command == ueCommand && this.getCurrentCommand().targetAddress.equals(mac)) {
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.mLastSendMessage = System.currentTimeMillis();
            this.sendNext();
        }
    }
    
    protected void removeCommandsForDevice(final MAC mac, final UEDeviceCommand.UECommand... array) {
        final Iterator<Object> iterator = this.mCommandQueue.iterator();
        if (iterator.hasNext()) {
            iterator.next();
            while (iterator.hasNext()) {
                final BroadcastCommand broadcastCommand = iterator.next();
                if (mac.equals(broadcastCommand.targetAddress)) {
                    for (int length = array.length, i = 0; i < length; ++i) {
                        if (broadcastCommand.command == array[i]) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }
    
    public void removeReceiverFromBroadcast(final MAC mac) {
        this.removeCommandsForDevice(mac, UEDeviceCommand.UECommand.RemoveReceiverFromBroadcast, UEDeviceCommand.UECommand.AddReceiverToBroadcast, UEDeviceCommand.UECommand.QueryReceiverFixedAttributes);
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.RemoveReceiverFromBroadcast, mac, null));
    }
    
    public void setListener(final ExecutionListener mListener) {
        this.mListener = mListener;
    }
    
    public void setReceiverIdentificationSignal(final MAC mac, final boolean b) {
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.SetReceiverIdentificationSignal, mac, new Object[] { b }));
    }
    
    public void setReceiverOneAttribute(final MAC mac, final int i, final byte[] array) {
        this.add(new BroadcastCommand(UEDeviceCommand.UECommand.SetReceiverOneAttribute, mac, new Object[] { i, array }));
    }
    
    public void start(final Context mContext) {
        this.setState(State.IDLE);
        this.mContext = mContext;
        this.mHandler = new Handler(Looper.getMainLooper());
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_ADDED_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_REMOVED_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_VARIABLE_ATTRIBUTES_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_RECEIVER_FIXED_ATTRIBUTES_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_GET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_SET_RECEIVER_ONE_ATTRIBUTE_NOTIFICATION");
        intentFilter.addAction("com.logitech.ue.centurion.ACTION_BLE_AVAILABLE_NOTIFICATION");
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    public void stop() {
        if (this.getState() != State.NOT_STARTED) {
            this.setState(State.NOT_STARTED);
            this.mCommandQueue.clear();
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mBroadcastReceiver);
            this.mWorkerHandler.removeCallbacksAndMessages((Object)null);
            this.mHandler.removeCallbacksAndMessages((Object)null);
            this.mHandler = null;
            this.mContext = null;
        }
    }
    
    private class BroadcastCommand
    {
        UEDeviceCommand.UECommand command;
        Object[] params;
        int priority;
        MAC targetAddress;
        
        public BroadcastCommand(final UEDeviceCommand.UECommand command, final MAC targetAddress, final Object[] params) {
            this.command = command;
            this.targetAddress = targetAddress;
            this.params = params;
            this.priority = BroadcastModeHelper.this.getCommandPriority(command);
        }
    }
    
    public interface ExecutionListener
    {
        void onRequestSendFail(final Exception p0, final UEDeviceCommand.UECommand p1, final MAC p2, final Object[] p3);
        
        void onRequestSendSuccess(final UEDeviceCommand.UECommand p0, final MAC p1, final Object[] p2);
        
        void onRequestTimeout(final UEDeviceCommand.UECommand p0, final MAC p1, final Object[] p2);
    }
    
    public class SendDelayRunnable implements Runnable
    {
        @Override
        public void run() {
            BroadcastModeHelper.this.setState(State.IDLE);
            BroadcastModeHelper.this.checkWorker();
        }
    }
    
    public enum State
    {
        IDLE, 
        NOT_STARTED, 
        PENDING, 
        SENDING, 
        WAITING_FOR_BLE, 
        WAITING_FOR_RESPONSE;
    }
    
    public class TimeoutRunnable implements Runnable
    {
        @Override
        public void run() {
            final BroadcastCommand access$000 = BroadcastModeHelper.this.getCurrentCommand();
            if (access$000 != null) {
                Log.d(BroadcastModeHelper.TAG, String.format("Command timeout: %s Address: %s", access$000.command.name(), access$000.targetAddress));
                if (BroadcastModeHelper.this.mListener != null) {
                    BroadcastModeHelper.this.mListener.onRequestTimeout(access$000.command, access$000.targetAddress, access$000.params);
                }
                BroadcastModeHelper.this.sendNext();
            }
            else {
                Log.w(BroadcastModeHelper.TAG, "There is no current message");
                BroadcastModeHelper.this.setState(State.IDLE);
            }
        }
    }
    
    public class WorkerRunnable implements Runnable
    {
        @Override
        public void run() {
            final BroadcastCommand access$000 = BroadcastModeHelper.this.getCurrentCommand();
            BroadcastModeHelper.this.mHandler.postDelayed((Runnable)new TimeoutRunnable(), 20000L);
            try {
                BroadcastModeHelper.this.mLastSendMessage = System.currentTimeMillis();
                BroadcastModeHelper.this.executeCommand(UEDeviceManager.getInstance().getConnectedDevice(), access$000);
                BroadcastModeHelper.this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (BroadcastModeHelper.this.mListener != null) {
                            BroadcastModeHelper.this.mListener.onRequestSendSuccess(access$000.command, access$000.targetAddress, access$000.params);
                        }
                    }
                });
                BroadcastModeHelper.this.setState(State.WAITING_FOR_RESPONSE);
            }
            catch (Exception ex) {
                if (!(ex instanceof UEErrorResultException)) {
                    Log.w(BroadcastModeHelper.TAG, String.format("Message failed. Message: %s Error: %s", access$000.command, ex.getMessage()));
                    BroadcastModeHelper.this.mHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (BroadcastModeHelper.this.mListener != null) {
                                BroadcastModeHelper.this.mListener.onRequestSendFail(ex, access$000.command, access$000.targetAddress, access$000.params);
                            }
                        }
                    });
                    BroadcastModeHelper.this.sendNext();
                    return;
                }
                Log.w(BroadcastModeHelper.TAG, String.format("Message failed. Device is busy. Re-queue the command. Message: %s Error: %s", access$000.command, ex.getMessage()));
                final UEErrorResultException ex2 = (UEErrorResultException)ex;
                if (ex2.getResponseCode() == UEAckResponse.COMMAND_FAIL || ex2.getResponseCode() == UEAckResponse.BUSY) {
                    BroadcastModeHelper.this.setState(State.WAITING_FOR_BLE);
                    BroadcastModeHelper.this.mHandler.removeCallbacksAndMessages((Object)null);
                    BroadcastModeHelper.this.mHandler.postDelayed((Runnable)new TimeoutRunnable(), 60000L);
                    return;
                }
                BroadcastModeHelper.this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (BroadcastModeHelper.this.mListener != null) {
                            BroadcastModeHelper.this.mListener.onRequestSendFail(ex, access$000.command, access$000.targetAddress, access$000.params);
                        }
                    }
                });
                BroadcastModeHelper.this.sendNext();
            }
        }
    }
}
