// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import java.io.Serializable;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.util.Log;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import android.app.Activity;
import butterknife.ButterKnife;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import com.logitech.ue.tasks.UpdateFirmwareTask;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.connection.UEConnectionType;
import com.logitech.ue.centurion.UEDeviceManager;
import java.util.concurrent.CancellationException;
import com.logitech.ue.firmware.UpdateStepInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.AsyncTask$Status;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.content.Intent;
import android.content.Context;
import com.logitech.ue.firmware.UpdateInstruction;
import com.logitech.ue.fragments.FirmwareUpdateFragment;
import com.logitech.ue.tasks.DownloadAndUpdateFirmwareTask;
import android.content.BroadcastReceiver;
import com.logitech.ue.fragments.FirmwareUpdateMenuFragment;
import com.logitech.ue.fragments.HoustonErrorDialogFragment;

public class UpdaterActivity extends BaseActivity implements HoustonErrorDialogFragment.Listener, FirmwareUpdateMenuFragment.Listener
{
    public static final String EXTRA_DEVICE_COLOR = "extra_device_color";
    public static final String EXTRA_FIRMWARE_VERSION = "extra_firmware_version";
    public static final String EXTRA_HARDWARE_REVISION = "extra_hardware_revision";
    public static final String EXTRA_OTA_SUPPORTED = "extra_ota_supported";
    public static final String EXTRA_SERIAL_NUMBER = "extra_device_serial";
    public static final String EXTRA_START_FIRMWARE_UPDATE = "extra_start_firmware_update";
    public static final String EXTRA_UPDATE_INSTRUCTIONS = "extra_update_instructions";
    public static final String EXTRA_UPDATE_RESULT = "extra_update_result";
    public static final String TAG;
    private static boolean isOTAOngoing;
    BroadcastReceiver mBroadcastReceiver;
    private int mDeviceColor;
    private String mFirmwareVersion;
    private String mHardwareRevision;
    private boolean mIsOTASupported;
    private FirmwareUpdateMenuFragment mMenu;
    private String mSerialNumber;
    private boolean mStartFirmwareUpdate;
    private DownloadAndUpdateFirmwareTask mUpdateFirmwareTask;
    private FirmwareUpdateFragment mUpdateFragment;
    private UpdateInstruction mUpdateInstruction;
    
    static {
        TAG = UpdaterActivity.class.getSimpleName();
        UpdaterActivity.isOTAOngoing = false;
    }
    
    public UpdaterActivity() {
        this.mStartFirmwareUpdate = false;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    final UEDeviceStatus status = UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED)));
                    if ((status == UEDeviceStatus.DISCONNECTING || status == UEDeviceStatus.DISCONNECTED) && !UpdaterActivity.this.isUpdatingFirmware()) {
                        UpdaterActivity.this.finish();
                    }
                }
            }
        };
    }
    
    private void beginOTAInfo() {
        this.showUpdaterMenuFragment(this.mUpdateInstruction.detailsURL);
    }
    
    public static boolean isOTAOngoing() {
        return UpdaterActivity.isOTAOngoing;
    }
    
    private boolean isUpdatingFirmware() {
        return this.mUpdateFirmwareTask != null && this.mUpdateFirmwareTask.getStatus() != AsyncTask$Status.FINISHED;
    }
    
    private void showHoustonError(final boolean b) {
        final HoustonErrorDialogFragment houstonErrorDialogFragment = (HoustonErrorDialogFragment)this.getSupportFragmentManager().findFragmentByTag(HoustonErrorDialogFragment.TAG);
        if (houstonErrorDialogFragment == null || houstonErrorDialogFragment.isHidden()) {
            HoustonErrorDialogFragment.getInstance(b).show(this.getSupportFragmentManager(), HoustonErrorDialogFragment.TAG);
        }
    }
    
    private void showNewUpdaterFragment(final int n, final String s) {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        final Fragment fragmentById = supportFragmentManager.findFragmentById(16908290);
        if (fragmentById == null || !(fragmentById instanceof FirmwareUpdateFragment)) {
            this.mUpdateFragment = FirmwareUpdateFragment.getInstance(n, s);
            supportFragmentManager.beginTransaction().replace(16908290, this.mUpdateFragment).commit();
        }
    }
    
    private void showUpdaterMenuFragment(final String s) {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        final Fragment fragmentById = supportFragmentManager.findFragmentById(16908290);
        if (fragmentById == null || !(fragmentById instanceof FirmwareUpdateMenuFragment)) {
            this.mMenu = FirmwareUpdateMenuFragment.getInstance(s);
            supportFragmentManager.beginTransaction().replace(16908290, this.mMenu).commit();
        }
    }
    
    private void startOTAUpdate(final int n, final UpdateInstruction updateInstruction, final String s) {
        this.showNewUpdaterFragment(n, updateInstruction.updateStepInfoList.get(updateInstruction.updateStepInfoList.size() - 1).firmwareVersion);
        UpdaterActivity.isOTAOngoing = true;
        (this.mUpdateFirmwareTask = new DownloadAndUpdateFirmwareTask(updateInstruction, s, this) {
            @Override
            protected void onCancelled(final Object o) {
                super.onCancelled(o);
                UpdaterActivity.this.setCancelResult(new CancellationException());
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                UpdaterActivity.this.showHoustonError(connectedDevice != null && connectedDevice.getConnectionType() == UEConnectionType.SPP);
                UpdaterActivity.this.mUpdateFirmwareTask = null;
                UpdaterActivity.isOTAOngoing = false;
            }
            
            @Override
            public void onError(final Exception cancelResult) {
                super.onError(cancelResult);
                UpdaterActivity.this.setCancelResult(cancelResult);
                final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
                UpdaterActivity.this.showHoustonError(connectedDevice != null && connectedDevice.getConnectionType() == UEConnectionType.SPP);
                UpdaterActivity.this.mUpdateFirmwareTask = null;
                UpdaterActivity.isOTAOngoing = false;
            }
            
            protected void onProgressUpdate(final Float... array) {
                super.onProgressUpdate((Object[])array);
                UpdaterActivity.this.mUpdateFragment.setProgress(1.0f + array[0]);
            }
            
            @Override
            public void onSuccess(final FirmwareUpdateReport firmwareUpdateReport) {
                super.onSuccess(firmwareUpdateReport);
                new Handler().postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        UpdaterActivity.this.setResult(-1);
                        UpdaterActivity.this.finish();
                    }
                }, 3000L);
                UpdaterActivity.this.mUpdateFirmwareTask = null;
                UpdaterActivity.isOTAOngoing = false;
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
    }
    
    public void beginOTAUpdate() {
        this.startOTAUpdate(this.mDeviceColor, this.mUpdateInstruction, this.mSerialNumber);
    }
    
    public void finish() {
        super.finish();
        this.overridePendingTransition(2131034132, 2131034128);
    }
    
    @Override
    public void onAskLaterClicked() {
        this.setCancelResult(null);
        this.finish();
    }
    
    @Override
    public void onBackPressed() {
        if (!this.isUpdatingFirmware()) {
            this.setCancelResult(null);
            this.finish();
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        ButterKnife.bind(this);
        final Intent intent = this.getIntent();
        if (intent != null) {
            this.mStartFirmwareUpdate = intent.getBooleanExtra("extra_start_firmware_update", false);
            this.mDeviceColor = intent.getIntExtra("extra_device_color", UEColour.UNKNOWN_COLOUR.getCode());
            this.mIsOTASupported = intent.getBooleanExtra("extra_ota_supported", false);
            this.mHardwareRevision = intent.getStringExtra("extra_hardware_revision");
            this.mFirmwareVersion = intent.getStringExtra("extra_firmware_version");
            this.mUpdateInstruction = (UpdateInstruction)intent.getParcelableExtra("extra_update_instructions");
            this.mSerialNumber = intent.getStringExtra("extra_device_serial");
            if (this.mStartFirmwareUpdate) {
                this.beginOTAUpdate();
            }
            else {
                this.beginOTAInfo();
            }
        }
        else {
            Log.d(UpdaterActivity.TAG, "No data");
            this.finish();
        }
    }
    
    @Override
    public void onDoLaterClicked() {
        this.finish();
    }
    
    @Override
    public void onRetryUpdateClicked() {
        this.beginOTAUpdate();
    }
    
    public void onStart() {
        super.onStart();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    public void onStop() {
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mBroadcastReceiver);
        super.onStop();
    }
    
    @Override
    public void onUpdateClicked() {
        this.beginOTAUpdate();
    }
    
    public void setCancelResult(final Exception ex) {
        final Intent intent = new Intent();
        intent.putExtra("extra_update_result", (Serializable)ex);
        this.setResult(0, intent);
    }
}
