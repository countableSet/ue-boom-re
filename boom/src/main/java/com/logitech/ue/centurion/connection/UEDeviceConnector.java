// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.connection;

import java.util.concurrent.TimeUnit;
import java.util.List;
import com.logitech.ue.centurion.exceptions.UEOTAException;
import com.logitech.ue.centurion.exceptions.UEOperationException;
import com.logitech.ue.centurion.utils.UEUtils;
import com.logitech.ue.centurion.exceptions.UECommandExecutionErrorException;
import android.os.SystemClock;
import com.logitech.ue.centurion.device.command.UEOTADeviceCommand;
import android.support.annotation.NonNull;
import com.logitech.ue.centurion.exceptions.UEErrorResultException;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.centurion.exceptions.UEOperationTimeOutException;
import com.logitech.ue.centurion.interfaces.IUEDeviceCommand;
import com.logitech.ue.centurion.device.devicedata.UEOTAState;
import com.logitech.ue.centurion.device.command.UEOTACommand;
import java.util.Iterator;
import com.logitech.ue.centurion.exceptions.UEConnectionException;
import android.util.Log;
import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.device.devicedata.UEAckResponse;
import com.logitech.ue.centurion.utils.MAC;
import com.logitech.ue.centurion.interfaces.IUEMessageFilter;
import com.logitech.ue.centurion.notification.notificator.IUENotificator;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class UEDeviceConnector extends UEBluetoothConnector
{
    public static final int CENTURION_TIMEOUT = 1000;
    public static final int OTA_LONG_TIMEOUT = 30000;
    public static final int OTA_SHORT_TIMEOUT = 1000;
    public static final int RETRY_ATTEMPTS = 2;
    private static final String TAG;
    private byte[] mCommandData;
    private byte mErrorCode;
    private volatile boolean mIsOTAMessage;
    private Semaphore mLockForWaitingResponse;
    private volatile Mode mMode;
    private final ArrayList<IUENotificator> mNotificatorList;
    private byte[] mReturnCommandData;
    private byte[] mReturnCommandExpectedData;
    private IUEMessageFilter mmFilter;
    
    static {
        TAG = UEDeviceConnector.class.getSimpleName();
    }
    
    public UEDeviceConnector(final MAC mac) {
        super(mac);
        this.mLockForWaitingResponse = new Semaphore(0, true);
        this.mNotificatorList = new ArrayList<IUENotificator>();
        this.mMode = Mode.Centurion;
        this.mCommandData = null;
        this.mReturnCommandData = null;
        this.mReturnCommandExpectedData = null;
        this.mmFilter = null;
        this.mIsOTAMessage = false;
        this.mErrorCode = UEAckResponse.OK.getValue();
    }
    
    private boolean processExpectedMessage(final byte[] mReturnCommandData) throws UEConnectionException {
        boolean b = true;
        if (this.mReturnCommandExpectedData == null) {
            return false;
        }
        if (this.mReturnCommandExpectedData[0] == mReturnCommandData[1] && (this.mReturnCommandExpectedData[1] == mReturnCommandData[2] || (this.mReturnCommandExpectedData[1] == UEDeviceCommand.UECommand.ReturnSerialNumber.getLeastSignificantByte() && mReturnCommandData[2] == 126))) {
            if (this.mReturnCommandExpectedData[0] == UEDeviceCommand.UECommand.Acknowledge.getMostSignificantByte() && this.mReturnCommandExpectedData[1] == UEDeviceCommand.UECommand.Acknowledge.getLeastSignificantByte()) {
                if (mReturnCommandData[3] != this.mCommandData[1] || (mReturnCommandData[4] != this.mCommandData[2] && (this.mReturnCommandExpectedData[1] != UEDeviceCommand.UECommand.ReturnSerialNumber.getLeastSignificantByte() || mReturnCommandData[2] != 126))) {
                    return false;
                }
                if (mReturnCommandData[5] == UEAckResponse.OK.getValue()) {
                    Log.d(UEDeviceConnector.TAG, "Command finished OK");
                }
                else {
                    Log.w(UEDeviceConnector.TAG, "Command finished with error(" + mReturnCommandData[5] + ")");
                    this.mErrorCode = mReturnCommandData[5];
                }
                this.mReturnCommandData = mReturnCommandData;
                this.mLockForWaitingResponse.release(1);
            }
            else {
                if (this.mmFilter != null && !this.mmFilter.filter(mReturnCommandData)) {
                    Log.w(UEDeviceConnector.TAG, "Filter not passed");
                    return false;
                }
                Log.d(UEDeviceConnector.TAG, "Filter passed. Send ACK");
                this.sendAck(mReturnCommandData[1], mReturnCommandData[2]);
                this.mReturnCommandData = mReturnCommandData;
                this.mLockForWaitingResponse.release(1);
            }
        }
        else {
            if (mReturnCommandData[1] != UEDeviceCommand.UECommand.Acknowledge.getMostSignificantByte() || mReturnCommandData[2] != UEDeviceCommand.UECommand.Acknowledge.getLeastSignificantByte() || mReturnCommandData[3] != this.mCommandData[1] || (mReturnCommandData[4] != this.mCommandData[2] && (this.mReturnCommandExpectedData[1] != UEDeviceCommand.UECommand.ReturnSerialNumber.getLeastSignificantByte() || mReturnCommandData[2] != 126))) {
                return false;
            }
            if (mReturnCommandData[5] == UEAckResponse.OK.getValue()) {
                Log.d(UEDeviceConnector.TAG, "Command finished OK");
            }
            else {
                Log.w(UEDeviceConnector.TAG, "Command finished with error(" + mReturnCommandData[5] + ")");
                this.mErrorCode = mReturnCommandData[5];
            }
            this.mReturnCommandData = mReturnCommandData;
            this.mLockForWaitingResponse.release(1);
        }
        return b;
        b = false;
        return b;
    }
    
    private boolean processNotifications(final byte[] array) throws UEConnectionException {
        boolean b = true;
        final Iterator<IUENotificator> iterator = this.mNotificatorList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().processNotification(array)) {
                this.sendAck(array[1], array[2]);
                return b;
            }
        }
        b = false;
        return b;
    }
    
    private void processOTAMessage(final byte[] array) {
        if (this.mReturnCommandExpectedData == null) {
            Log.wtf(UEDeviceConnector.TAG, "OTA mode without expected result");
        }
        else if (this.mReturnCommandExpectedData[1] == UEOTACommand.RUN_DFU.getCode()) {
            this.mReturnCommandData = array;
            this.mLockForWaitingResponse.release();
        }
        else if (array[0] == this.mReturnCommandExpectedData[1]) {
            if (array[1] == UEOTAState.STATUS_CMD_IN_PROGRESS.getCode()) {
                Log.w(UEDeviceConnector.TAG, "Ignoring IN PROGRESS response");
            }
            else {
                this.mReturnCommandData = array;
                this.mLockForWaitingResponse.release();
            }
        }
    }
    
    private void processUnexpectedMessage(final byte[] array) throws UEConnectionException {
        if (array[1] == UEDeviceCommand.UECommand.Acknowledge.getMostSignificantByte() && array[2] == UEDeviceCommand.UECommand.Acknowledge.getLeastSignificantByte()) {
            Log.w(UEDeviceConnector.TAG, "Received unexpected ACK message. Ignore it");
        }
        else {
            Log.w(UEDeviceConnector.TAG, "Received unexpected message. Ignore it, but send ACK");
            this.sendAck(array[1], array[2]);
        }
    }
    
    private void sendAck(final byte b, final byte b2) throws UEConnectionException {
        final byte mostSignificantByte = UEDeviceCommand.UECommand.Acknowledge.getMostSignificantByte();
        final byte leastSignificantByte = UEDeviceCommand.UECommand.Acknowledge.getLeastSignificantByte();
        final byte value = UEAckResponse.OK.getValue();
        Log.d(UEDeviceConnector.TAG, "Send ACK");
        this.sendData(new byte[] { 5, mostSignificantByte, leastSignificantByte, b, b2, value });
    }
    
    public void addNotificator(final IUENotificator e) {
        this.mNotificatorList.add(e);
    }
    
    public byte[] executeMessage(final IUEDeviceCommand iueDeviceCommand) throws UEOperationTimeOutException, UEUnrecognisedCommandException, UEConnectionException, UEErrorResultException {
        this.mThreadSafeWorkLock.lock();
        try {
            return this.executeMessage(iueDeviceCommand, null, 1000);
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
    }
    
    @NonNull
    public byte[] executeMessage(final IUEDeviceCommand iueDeviceCommand, final int n) throws UEOperationTimeOutException, UEUnrecognisedCommandException, UEConnectionException, UEErrorResultException {
        this.mThreadSafeWorkLock.lock();
        try {
            return this.executeMessage(iueDeviceCommand, null, n);
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
    }
    
    @NonNull
    public byte[] executeMessage(final IUEDeviceCommand iueDeviceCommand, final IUEMessageFilter iueMessageFilter) throws UEOperationTimeOutException, UEConnectionException, UEUnrecognisedCommandException, UEErrorResultException {
        this.mThreadSafeWorkLock.lock();
        try {
            return this.executeMessage(iueDeviceCommand, iueMessageFilter, 1000);
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
    }
    
    @NonNull
    public byte[] executeMessage(final IUEDeviceCommand iueDeviceCommand, final IUEMessageFilter iueMessageFilter, final int n) throws UEOperationTimeOutException, UEUnrecognisedCommandException, UEConnectionException, UEErrorResultException {
        this.mThreadSafeWorkLock.lock();
        try {
            if ((this.mMode == Mode.Centurion && !(iueDeviceCommand instanceof UEDeviceCommand)) || (this.mMode == Mode.OTA && !(iueDeviceCommand instanceof UEOTADeviceCommand))) {
                Log.w(UEDeviceConnector.TAG, String.format("Can't execute command due to wrong mode. Mode: %s Command name: %s", this.mMode.name(), iueDeviceCommand.getCommandName()));
                throw new UEConnectionException("Connector is not in necessary mode to process this message");
            }
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
        byte[] writeAndWaitResponse = null;
        int i = 0;
        if (this.mSessionThread == null) {
            throw new UEConnectionException("Connector is not connected. Call open connection first");
        }
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        Log.d(UEDeviceConnector.TAG, "Executing command. Command name: " + iueDeviceCommand.getCommandName());
    Label_0255_Outer:
        while (writeAndWaitResponse == null && i <= 2) {
            if (i > 0) {
                Log.d(UEDeviceConnector.TAG, "Retrying " + i + " time(s)");
            }
            while (true) {
                try {
                    writeAndWaitResponse = this.writeAndWaitResponse(iueDeviceCommand.buildCommandData(), iueDeviceCommand.getReturnCommand(), iueMessageFilter, n);
                    i = (short)(i + 1);
                    continue Label_0255_Outer;
                }
                catch (UEOperationTimeOutException ex) {
                    if (i >= 2) {
                        throw ex;
                    }
                    continue;
                }
                catch (UECommandExecutionErrorException ex2) {
                    if (ex2.getErrorCode() == UEAckResponse.UNRECOGNIZED_COMMAND.getValue()) {
                        Log.w(UEDeviceConnector.TAG, String.format("Execution failed. Command name: %s . Not supported. Time elapsed: %d ms", iueDeviceCommand.getCommandName(), SystemClock.elapsedRealtime() - elapsedRealtime));
                        throw new UEUnrecognisedCommandException(iueDeviceCommand);
                    }
                    Log.w(UEDeviceConnector.TAG, String.format("Execution failed. Command name: %s . Error %d. Time elapsed: %d ms", iueDeviceCommand.getCommandName(), ex2.getErrorCode(), SystemClock.elapsedRealtime() - elapsedRealtime));
                    throw new UEErrorResultException(iueDeviceCommand, UEAckResponse.getAckResponse(ex2.getErrorCode()));
                }
                break;
            }
            break;
        }
        Log.d(UEDeviceConnector.TAG, String.format("Execution success. Command name: %s. Time elapsed: %d ms", iueDeviceCommand.getCommandName(), SystemClock.elapsedRealtime() - elapsedRealtime));
        this.mThreadSafeWorkLock.unlock();
        return writeAndWaitResponse;
    }
    
    public byte[] executeMessage(final byte[] array, final int n) throws UEOperationException, UEConnectionException {
        this.mThreadSafeWorkLock.lock();
        byte[] writeAndWaitResponse = null;
        int n2 = 0;
        try {
            if (this.mSessionThread == null) {
                throw new UEConnectionException("Connector is not connected. Call open connection first");
            }
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
        while (true) {
            try {
                final byte[] writeAndWaitResponse2 = this.writeAndWaitResponse(array, n);
                this.mThreadSafeWorkLock.unlock();
                return writeAndWaitResponse2;
            }
            catch (UEOperationTimeOutException ex5) {
                byte[] writeAndWaitResponse2;
                while ((writeAndWaitResponse2 = writeAndWaitResponse) == null) {
                    writeAndWaitResponse2 = writeAndWaitResponse;
                    if (n2 >= 2) {
                        break;
                    }
                    final short i = (short)(n2 + 1);
                    Log.d(UEDeviceConnector.TAG, "Retrying " + i + " time(s)");
                    try {
                        writeAndWaitResponse = this.writeAndWaitResponse(array, n);
                        n2 = i;
                    }
                    catch (UEOperationTimeOutException ex) {
                        n2 = i;
                        if (i >= 2) {
                            throw ex;
                        }
                        continue;
                    }
                    catch (UECommandExecutionErrorException ex2) {
                        Log.w(UEDeviceConnector.TAG, "Message [" + UEUtils.byteArrayToHexString(array) + "] ended with error(" + ex2.getErrorCode() + ")");
                        throw ex2;
                    }
                    catch (UEConnectionException ex3) {
                        Log.e(UEDeviceConnector.TAG, "Failed to write to stream");
                        throw ex3;
                    }
                }
                continue;
            }
            catch (UECommandExecutionErrorException ex4) {
                Log.w(UEDeviceConnector.TAG, "Message [" + UEUtils.byteArrayToHexString(array) + "] ended with error(" + ex4.getErrorCode() + ")");
                throw ex4;
            }
            catch (UEConnectionException ex6) {
                Log.e(UEDeviceConnector.TAG, "Failed to write to stream");
            }
            break;
        }
    }
    
    public byte[] executeOTAMessage(final IUEDeviceCommand iueDeviceCommand) throws UEOperationTimeOutException, UEUnrecognisedCommandException, UEConnectionException, UEOTAException, UEErrorResultException {
        return this.executeOTAMessage(iueDeviceCommand, 1000);
    }
    
    public byte[] executeOTAMessage(final IUEDeviceCommand iueDeviceCommand, final int n) throws UEOperationTimeOutException, UEUnrecognisedCommandException, UEConnectionException, UEOTAException, UEErrorResultException {
        this.mThreadSafeWorkLock.lock();
        byte[] executeMessage;
        try {
            this.mIsOTAMessage = true;
            executeMessage = this.executeMessage(iueDeviceCommand, n);
            if (executeMessage[0] == UEOTACommand.RUN_DFU.getCode() && executeMessage[1] != UEOTAState.STATUS_CMD_COMPLETED_OK.getCode() && executeMessage[1] != UEOTAState.STATUS_CMD_IN_PROGRESS.getCode()) {
                throw new UEOTAException(iueDeviceCommand, executeMessage[1]);
            }
        }
        finally {
            this.mIsOTAMessage = false;
            this.mThreadSafeWorkLock.unlock();
        }
        this.mIsOTAMessage = false;
        this.mThreadSafeWorkLock.unlock();
        return executeMessage;
    }
    
    public byte[] executeOTAMessage(final IUEDeviceCommand iueDeviceCommand, final boolean b) throws UEOperationTimeOutException, UEUnrecognisedCommandException, UEConnectionException, UEOTAException, UEErrorResultException {
        int n;
        if (b) {
            n = 30000;
        }
        else {
            n = 1000;
        }
        return this.executeOTAMessage(iueDeviceCommand, n);
    }
    
    public List<IUENotificator> getNotificators() {
        return this.mNotificatorList;
    }
    
    @Override
    protected void onDataReceived(final byte[] array) throws UEConnectionException {
        super.onDataReceived(array);
        if (!this.mIsOTAMessage) {
            if (array.length < 3) {
                Log.e(UEDeviceConnector.TAG, "Insufficient return length: " + UEUtils.byteArrayToFancyHexString(array));
            }
            else if (array[0] < 2) {
                Log.e(UEDeviceConnector.TAG, "Incorrect length: " + UEUtils.byteArrayToFancyHexString(array));
            }
            else {
                Log.i(UEDeviceConnector.TAG, "received data check whether  it is a response for a request");
                if (!this.processExpectedMessage(array)) {
                    Log.i(UEDeviceConnector.TAG, "not a message, check whether  it is a notification");
                    if (!this.processNotifications(array)) {
                        Log.i(UEDeviceConnector.TAG, "not a notification, it is unexpected message");
                        this.processUnexpectedMessage(array);
                    }
                }
            }
        }
        else {
            this.processOTAMessage(array);
        }
    }
    
    @Override
    protected void onDataSent(final byte[] array) {
        super.onDataSent(array);
    }
    
    public void switchMode(final Mode mMode) {
        Log.d(UEDeviceConnector.TAG, "Switch connector mode to " + mMode.name());
        this.mMode = mMode;
    }
    
    @NonNull
    public byte[] writeAndWaitResponse(byte[] mCommandData, final int n) throws UEOperationTimeOutException, UECommandExecutionErrorException, UEConnectionException {
        while (true) {
            this.mThreadSafeWorkLock.lock();
            Label_0108: {
                try {
                    this.mLockForWaitingResponse.drainPermits();
                    this.sendData(this.mCommandData = mCommandData);
                    if (!this.mLockForWaitingResponse.tryAcquire(1, n, TimeUnit.MILLISECONDS)) {
                        throw new UEOperationTimeOutException(mCommandData);
                    }
                    break Label_0108;
                }
                catch (InterruptedException ex) {
                    try {
                        ex.printStackTrace();
                        Log.e(UEDeviceConnector.TAG, "Session thread failed. Still connected?", (Throwable)ex);
                        return mCommandData;
                        // iftrue(Label_0180:, this.mErrorCode == UEAckResponse.OK.getValue())
                        throw new UECommandExecutionErrorException(this.mErrorCode, this.mCommandData);
                    }
                    finally {
                        this.mErrorCode = 0;
                        this.mCommandData = null;
                        this.mReturnCommandExpectedData = null;
                        mCommandData = this.mReturnCommandData;
                        this.mReturnCommandData = null;
                        this.mmFilter = null;
                        this.mThreadSafeWorkLock.unlock();
                    }
                }
            }
            Label_0180: {
                this.mErrorCode = 0;
            }
            this.mCommandData = null;
            this.mReturnCommandExpectedData = null;
            mCommandData = this.mReturnCommandData;
            this.mReturnCommandData = null;
            this.mmFilter = null;
            this.mThreadSafeWorkLock.unlock();
            return mCommandData;
        }
    }
    
    public byte[] writeAndWaitResponse(byte[] writeAndWaitResponse, final int n, final int n2) throws UEOperationTimeOutException, UECommandExecutionErrorException, UEConnectionException {
        this.mThreadSafeWorkLock.lock();
        try {
            writeAndWaitResponse = this.writeAndWaitResponse(writeAndWaitResponse, n, null, n2);
            return writeAndWaitResponse;
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
    }
    
    @NonNull
    public byte[] writeAndWaitResponse(byte[] writeAndWaitResponse, final int n, final IUEMessageFilter mmFilter, final int n2) throws UEOperationTimeOutException, UECommandExecutionErrorException, UEConnectionException {
        this.mThreadSafeWorkLock.lock();
        try {
            (this.mReturnCommandExpectedData = new byte[2])[0] = (byte)(n >> 8 & 0xFF);
            this.mReturnCommandExpectedData[1] = (byte)(n & 0xFF);
            this.mmFilter = mmFilter;
            writeAndWaitResponse = this.writeAndWaitResponse(writeAndWaitResponse, n2);
            return writeAndWaitResponse;
        }
        finally {
            this.mThreadSafeWorkLock.unlock();
        }
    }
    
    public enum Mode
    {
        Centurion, 
        OTA;
    }
}
