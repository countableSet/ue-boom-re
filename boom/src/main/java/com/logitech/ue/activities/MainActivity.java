// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.activities;

import com.logitech.ue.tasks.SafeTask;
import com.logitech.ue.fragments.HomeMainFragment;
import com.logitech.ue.fragments.DoubleXUPFragment;
import com.logitech.ue.fragments.BlockPartyFragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.logitech.ue.centurion.exceptions.UEUnrecognisedCommandException;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import com.logitech.ue.firmware.FirmwareManager;
import com.logitech.ue.tasks.AttachableTask;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.logitech.ue.other.SVGRenderCacher;
import android.graphics.Rect;
import com.caverock.androidsvg.SVG;
import com.logitech.ue.content.ContentLoadListener;
import com.logitech.ue.content.ContentManager;
import com.logitech.ue.UEColourHelper;
import com.logitech.ue.other.DeviceInfo;
import com.logitech.ue.manifest.ManifestManager;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import java.io.Serializable;
import com.logitech.ue.services.UEVoiceService;
import com.logitech.ue.interfaces.IPage;
import android.support.v4.view.PagerAdapter;
import android.app.Activity;
import butterknife.ButterKnife;
import android.os.Bundle;
import android.os.Parcelable;
import com.logitech.ue.fragments.PartyUpWelcomeDialogFragment;
import com.logitech.ue.UserPreferences;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import com.logitech.ue.centurion.device.UEGenericDevice;
import com.logitech.ue.centurion.UEDeviceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import com.logitech.ue.fragments.LandingFragment;
import com.logitech.ue.ecomm.INotificationFetchHandler;
import com.logitech.ue.ecomm.NotificationManager;
import android.os.AsyncTask;
import com.logitech.ue.Utils;
import com.logitech.ue.App;
import com.logitech.ue.fragments.IntroFragment;
import android.content.Intent;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.util.Log;
import android.support.v4.view.ViewPager;
import com.logitech.ue.firmware.UpdateInstruction;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextSwitcher;
import butterknife.Bind;
import com.logitech.ue.views.TabIndicator;
import android.os.Handler;
import com.logitech.ue.ecomm.model.Notification;
import com.logitech.ue.centurion.device.devicedata.UEDeviceStatus;
import android.widget.ViewSwitcher$ViewFactory;
import com.logitech.ue.ecomm.NotificationPopup;
import android.content.BroadcastReceiver;

public class MainActivity extends BaseActivity
{
    public static final String EXTRA_DISCONNECTED = "disconnected";
    public static final String EXTRA_ERROR = "error";
    public static final String EXTRA_SELECT_TAB = "select_tab";
    private static final int REQUEST_FIRMWARE_UPDATE = 1002;
    private static final int REQUEST_XUP_ONBOARDING = 1000;
    private static final int REQUEST_XUP_TUTORIAL = 1001;
    public static final int TAB_POSITION_BLOCKPARTY = 2;
    public static final int TAB_POSITION_HOME = 0;
    public static final int TAB_POSITION_X_UP = 1;
    private static final String TAG;
    BroadcastReceiver mBroadcastReceiver;
    public NotificationPopup mCurrentNotification;
    int mDeviceColor;
    private ViewSwitcher$ViewFactory mFactory;
    String mFirmwareVersion;
    String mHardwareRevision;
    boolean mIsOTASupported;
    private boolean mIsWaitingForReconnect;
    private UEDeviceStatus mLastStatusOfDevice;
    public Notification mNotification;
    HomePageAdapter mPageAdapter;
    private Handler mReconnectEventHandler;
    String mSerialNumber;
    @Bind({ 16908306 })
    TabIndicator mTabIndicator;
    @Bind({ 16908310 })
    TextSwitcher mTitleSwitcher;
    @Bind({ 2131624031 })
    Toolbar mToolbar;
    @Bind({ 2131624030 })
    ImageView mToolbarBackground;
    private int mToolbarFrontColor;
    UpdateInstruction mUpdateInstruction;
    UpdateTask mUpdateTask;
    @Bind({ 2131624043 })
    ViewPager mViewPager;
    NotificationPopup.NotificationPopupListener notificationPopupListener;
    
    static {
        TAG = MainActivity.class.getSimpleName();
    }
    
    public MainActivity() {
        this.mToolbarFrontColor = -1;
        this.mIsWaitingForReconnect = false;
        this.mReconnectEventHandler = new Handler();
        this.mLastStatusOfDevice = null;
        this.notificationPopupListener = new NotificationPopup.NotificationPopupListener() {
            @Override
            public void onDismiss(final NotificationPopup notificationPopup, final boolean b) {
                Log.d(MainActivity.this.getTag(), "Dismiss E-comm notification popup");
                if (b) {}
                MainActivity.this.mCurrentNotification = null;
            }
            
            @Override
            public void onError(final NotificationPopup notificationPopup, final Exception ex) {
                if (ex.getMessage().equals("Notification popup out of bounds")) {
                    Log.i(MainActivity.this.getTag(), "Can't show NotificationPopup. Wrong mValue");
                }
            }
            
            @Override
            public void onExternalLinkClicked(final NotificationPopup notificationPopup, final String str) {
                Log.d(MainActivity.this.getTag(), "External link clicked. URL: " + str);
            }
            
            @Override
            public void onShow(final NotificationPopup notificationPopup) {
                Log.d(MainActivity.this.getTag(), "Show e-comm notification popup");
            }
        };
        this.mFactory = (ViewSwitcher$ViewFactory)new ViewSwitcher$ViewFactory() {
            public View makeView() {
                final TextView textView = (TextView)MainActivity.this.getLayoutInflater().inflate(2130968643, (ViewGroup)null);
                textView.setTextColor(MainActivity.this.mToolbarFrontColor);
                return (View)textView;
            }
        };
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                Log.d(MainActivity.TAG, "Receive broadcast " + intent.getAction());
                if (intent.getAction().equals("com.logitech.ue.centurion.CONNECTION_CHANGED")) {
                    final UEDeviceStatus status = UEDeviceStatus.getStatus(intent.getIntExtra("status", UEDeviceStatus.getValue(UEDeviceStatus.DISCONNECTED)));
                    MainActivity.this.mLastStatusOfDevice = status;
                    if (status == UEDeviceStatus.CONNECTING || status == UEDeviceStatus.CONNECTED_OFF || status == UEDeviceStatus.SINGLE_CONNECTED || status == UEDeviceStatus.DOUBLEUP_CONNECTED) {
                        MainActivity.this.mIsWaitingForReconnect = false;
                    }
                    if (!MainActivity.this.mIsWaitingForReconnect && (status == UEDeviceStatus.DISCONNECTING || status == UEDeviceStatus.DISCONNECTED)) {
                        MainActivity.this.waitForSpeakerReconnect(17000);
                    }
                    if (!MainActivity.this.mIsWaitingForReconnect) {
                        MainActivity.this.beginUpdate(status);
                    }
                    else {
                        MainActivity.this.changeTab(0, false);
                        MainActivity.this.setPageCount(1);
                    }
                    if (status == UEDeviceStatus.SINGLE_CONNECTED || status == UEDeviceStatus.DISCONNECTED) {
                        MainActivity.this.checkNotification();
                    }
                }
                else if (intent.getAction().equals("com.logitech.ue.fragments.STATE_CHANGED")) {
                    if (IntroFragment.IntroState.values()[intent.getIntExtra("intro_state", 0)] == IntroFragment.IntroState.finished) {
                        MainActivity.this.checkNotification();
                    }
                }
                else if (intent.getAction().equals("com.logitech.ue.firmware.UPDATE_READY")) {
                    MainActivity.this.beginFirmwareUpdateCheck();
                }
                else if (intent.getAction().equals("com.logitech.ue.manifest.MANIFEST_UPDATED")) {
                    MainActivity.this.beginUpdate();
                }
            }
        };
    }
    
    private void beginFirmwareUpdateCheck() {
        ((SafeTask<Void, Progress, Result>)new FirmwareUpdateCheckTask()).executeOnThreadPoolExecutor(new Void[0]);
    }
    
    private void beginUpdate() {
        this.beginUpdate(App.getDeviceConnectionState());
    }
    
    private void beginUpdate(final UEDeviceStatus ueDeviceStatus) {
        if (ueDeviceStatus != null) {
            Log.d(MainActivity.TAG, "Update. App state: " + ueDeviceStatus.name());
            Utils.cancelTask(this.mUpdateTask);
            this.updateActionBar();
            if (ueDeviceStatus.isBtClassicConnectedState()) {
                this.beginFirmwareUpdateCheck();
                ((SafeTask<Void, Progress, Result>)new UpdateTask()).executeOnThreadPoolExecutor(new Void[0]);
                this.hideLandingFragment();
            }
            else {
                this.changeTab(0, false);
                this.setPageCount(1);
                this.updateFirmwareUpdateIndicator(false);
                if (ueDeviceStatus == UEDeviceStatus.CONNECTED_OFF) {
                    this.hideLandingFragment();
                }
                else {
                    this.showLandingFragment();
                }
            }
        }
    }
    
    private void cancelWaitForReconnect() {
        this.mReconnectEventHandler.removeCallbacksAndMessages((Object)null);
        this.setWaitingForReconnect(false);
    }
    
    private void checkNotification() {
        final IntroFragment introFragment = this.getIntroFragment();
        if (introFragment != null && introFragment.getState() == IntroFragment.IntroState.finished) {
            Log.d(MainActivity.TAG, "Check e-comm notification");
            NotificationManager.getInstance().fetchNotification(App.getInstance().buildNotificationRequestParams("home"), new INotificationFetchHandler() {
                @Override
                public void onNoNotificationReceived(final Exception ex) {
                    Log.d(MainActivity.this.getTag(), "No e-comm notification received");
                }
                
                @Override
                public void onReceiveNotification(final Notification notification) {
                    Log.d(MainActivity.this.getTag(), "Receive e-comm notification");
                    MainActivity.this.showNotification(notification);
                }
            });
        }
    }
    
    private IntroFragment getIntroFragment() {
        return (IntroFragment)this.getSupportFragmentManager().findFragmentByTag(IntroFragment.TAG);
    }
    
    private void hideLandingFragment() {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        final LandingFragment landingFragment = (LandingFragment)supportFragmentManager.findFragmentByTag(LandingFragment.TAG);
        if (landingFragment != null && landingFragment.isVisible()) {
            supportFragmentManager.beginTransaction().setCustomAnimations(2131034122, 2131034123).hide(landingFragment).commitAllowingStateLoss();
        }
    }
    
    private void hideNotification() {
        if (this.mCurrentNotification != null) {
            Log.d(MainActivity.TAG, "Hide e-comm notification");
            this.mCurrentNotification.dismissSafe();
            this.mCurrentNotification = null;
        }
        else {
            Log.d(MainActivity.TAG, "No e-comm notification to hide");
        }
    }
    
    private void setPageCount(final int n) {
        this.mTabIndicator.setTabCount(n);
        if (n == 1) {
            this.mViewPager.setEnabled(false);
        }
        else {
            this.mViewPager.setEnabled(true);
            this.mPageAdapter.setPageCount(n);
        }
    }
    
    private void setTitleTextColor(final int n) {
        Log.d(MainActivity.TAG, "Set title color to" + n);
        for (int i = 0; i < this.mTitleSwitcher.getChildCount(); ++i) {
            ((TextView)this.mTitleSwitcher.getChildAt(i)).setTextColor(n);
        }
    }
    
    private void showErrorHome(final String str) {
        Log.d(MainActivity.TAG, "Show error home. Error: " + str);
        final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
        if (connectedDevice != null) {
            Log.d(MainActivity.TAG, "Show \"Nuclear\" dialog");
            this.changeTab(0, true);
            this.mPageAdapter.mHomeFragment.switchState(UEDeviceStatus.DISCONNECTED, true);
            this.mPageAdapter.mHomeFragment.switchState(UEDeviceStatus.STANDBY, true);
            new Handler().postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    App.getInstance().showErrorDialog((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                        public void onDismiss(final DialogInterface dialogInterface) {
                            connectedDevice.getConnector().disconnectFromDevice();
                            MainActivity.this.mPageAdapter.mHomeFragment.switchState(UEDeviceStatus.DISCONNECTED, true);
                        }
                    });
                }
            }, 900L);
        }
        else {
            Log.d(MainActivity.TAG, "Device is not connected. Don't show error dialog. Go to disconnected state");
        }
    }
    
    private void showIntro() {
        if (this.getIntroFragment() == null) {
            IntroFragment.getInstance().show(this.getSupportFragmentManager(), IntroFragment.TAG);
        }
    }
    
    private void showLandingFragment() {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        final LandingFragment landingFragment = (LandingFragment)supportFragmentManager.findFragmentByTag(LandingFragment.TAG);
        if (landingFragment == null) {
            supportFragmentManager.beginTransaction().setCustomAnimations(2131034122, 2131034123).add(16908290, new LandingFragment(), LandingFragment.TAG).commitAllowingStateLoss();
        }
        else if (!landingFragment.isVisible()) {
            supportFragmentManager.beginTransaction().setCustomAnimations(2131034122, 2131034123).show(landingFragment).commitAllowingStateLoss();
        }
    }
    
    private void showNotification(final Notification notification) {
        if (notification != null && App.getInstance().getCurrentActivity() == this) {
            Log.d(MainActivity.TAG, "Show e-comm notification");
            if (this.mCurrentNotification != null) {
                this.mCurrentNotification.dismissSafe();
            }
            (this.mCurrentNotification = notification.buildPopup((Context)this).setNotificationPopupListener(this.notificationPopupListener).build()).show(this.getWindow().getDecorView().findViewById(16908290));
        }
    }
    
    private void showPartyUpWelcomeFragment(final int n, final boolean b) {
        if (!UserPreferences.getInstance().isPartyUpWelcomeDialogSeen()) {
            UserPreferences.getInstance().setPartyUpWelcomeDialogSeen(true);
            final PartyUpWelcomeDialogFragment partyUpWelcomeDialogFragment = (PartyUpWelcomeDialogFragment)this.getSupportFragmentManager().findFragmentByTag(PartyUpWelcomeDialogFragment.TAG);
            if (partyUpWelcomeDialogFragment == null || partyUpWelcomeDialogFragment.isHidden()) {
                PartyUpWelcomeDialogFragment.getInstance(n, b).show(this.getSupportFragmentManager(), PartyUpWelcomeDialogFragment.TAG);
            }
        }
    }
    
    private void startUpdaterActivity(final boolean b, final int n, final UpdateInstruction updateInstruction, final String s) {
        final Intent intent = new Intent(this.getBaseContext(), (Class)UpdaterActivity.class);
        intent.putExtra("extra_start_firmware_update", b);
        intent.putExtra("extra_device_color", n);
        intent.putExtra("extra_update_instructions", (Parcelable)updateInstruction);
        intent.putExtra("extra_device_serial", s);
        this.startActivityForResult(intent, 1002);
    }
    
    private void startXUPOnBoarding() {
        if (!UserPreferences.getInstance().isXUPOnBoardingWasSeen()) {
            this.startActivityForResult(new Intent((Context)this, (Class)XUPOnBoardingActivity.class), 1000);
        }
    }
    
    private void updateFirmwareUpdateIndicator(final boolean b) {
        this.updateFirmwareUpdateIndicator(b, Utils.isColorBright(this.mToolbarFrontColor));
    }
    
    private void updateFirmwareUpdateIndicator(final boolean b, final UpdateInstruction updateInstruction) {
        this.updateFirmwareUpdateIndicator(Utils.isUpdateAvailable(b, updateInstruction));
    }
    
    private void updateFirmwareUpdateIndicator(final boolean b, final boolean b2) {
        Log.d(MainActivity.TAG, String.format("Update firmware indicator. Update is available %b is front color white %b", b, b2));
        if (b) {
            final Toolbar mToolbar = this.mToolbar;
            int navigationIcon;
            if (b2) {
                navigationIcon = 2130837619;
            }
            else {
                navigationIcon = 2130837620;
            }
            mToolbar.setNavigationIcon(navigationIcon);
        }
        else {
            final Toolbar mToolbar2 = this.mToolbar;
            int navigationIcon2;
            if (b2) {
                navigationIcon2 = 2130837617;
            }
            else {
                navigationIcon2 = 2130837618;
            }
            mToolbar2.setNavigationIcon(navigationIcon2);
        }
    }
    
    private void waitForSpeakerReconnect(final int n) {
        Log.d(MainActivity.TAG, "Wait for speaker reconnect");
        this.setWaitingForReconnect(true);
        this.mReconnectEventHandler.removeCallbacksAndMessages((Object)null);
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                MainActivity.this.mReconnectEventHandler.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.setWaitingForReconnect(false);
                        MainActivity.this.beginUpdate(MainActivity.this.mLastStatusOfDevice);
                    }
                }, (long)n);
            }
        }, 100L);
    }
    
    public void beginOTAUpdate() {
        this.startUpdaterActivity(true, this.mDeviceColor, this.mUpdateInstruction, this.mSerialNumber);
    }
    
    public void changeTab(final int n, final boolean b) {
        if (n < this.mViewPager.getChildCount()) {
            this.mViewPager.setCurrentItem(n, b);
        }
    }
    
    public int getCurrentTabIndex() {
        return this.mViewPager.getCurrentItem();
    }
    
    public Fragment getSelectedTab() {
        return this.mPageAdapter.getItem(this.mViewPager.getCurrentItem());
    }
    
    public Fragment getTab(final int n) {
        return this.mPageAdapter.getItem(n);
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        Label_0036: {
            switch (n) {
                case 1000: {
                    this.beginUpdate();
                    break;
                }
                case 1002: {
                    switch (n2) {
                        default: {
                            break Label_0036;
                        }
                        case -1: {
                            this.startXUPOnBoarding();
                            break Label_0036;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        if (this.mCurrentNotification != null) {
            this.mCurrentNotification.dismissSafe();
            Log.d(this.getTag(), "Dismiss notification popup");
            this.mCurrentNotification = null;
        }
        else {
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130968606);
        ButterKnife.bind(this);
        this.setSupportActionBar(this.mToolbar);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.updateFirmwareUpdateIndicator(false);
        this.mTitleSwitcher.setFactory(this.mFactory);
        this.mPageAdapter = new HomePageAdapter(this.getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mPageAdapter);
        this.mViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.SimpleOnPageChangeListener() {
            int mPreviousItem = -1;
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
                super.onPageScrolled(n, n2, n3);
                MainActivity.this.mTabIndicator.setSlideProgress(n + n2);
                final Fragment tab = MainActivity.this.getTab(n);
                if (tab instanceof IPage) {
                    ((IPage)tab).onTransition(n2);
                }
                final Fragment tab2 = MainActivity.this.getTab(n + 1);
                if (tab2 instanceof IPage) {
                    ((IPage)tab2).onTransition(1.0f - n2);
                }
            }
            
            @Override
            public void onPageSelected(final int mPreviousItem) {
                super.onPageSelected(mPreviousItem);
                final Fragment item = MainActivity.this.mPageAdapter.getItem(mPreviousItem);
                if (item instanceof IPage) {
                    final String title = ((IPage)MainActivity.this.mPageAdapter.getItem(mPreviousItem)).getTitle();
                    Log.d(MainActivity.TAG, "Tab selected " + item.getClass().getSimpleName() + " With title " + title);
                    MainActivity.this.setTitle(title);
                    if (MainActivity.this.mPageAdapter.getItem(this.mPreviousItem) != null && MainActivity.this.mPageAdapter.getItem(this.mPreviousItem) instanceof IPage) {
                        ((IPage)MainActivity.this.mPageAdapter.getItem(this.mPreviousItem)).onDeselected();
                    }
                    ((IPage)MainActivity.this.mPageAdapter.getItem(mPreviousItem)).onSelected();
                    this.mPreviousItem = mPreviousItem;
                }
                else {
                    Log.d(MainActivity.TAG, "Tab selected " + item.getClass().getSimpleName() + ". Doesn't implement IPage interface");
                    MainActivity.this.setTitle(null);
                }
            }
        });
        this.setPageCount(3);
        this.mViewPager.setOffscreenPageLimit(3);
        this.setTitle("HOME");
        this.startService(new Intent((Context)this, (Class)UEVoiceService.class));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            if (intent.hasExtra("error")) {
                final Serializable serializableExtra = intent.getSerializableExtra("error");
                if (serializableExtra instanceof Exception && UEDeviceManager.getInstance().getConnectedDevice() != null) {
                    this.showErrorHome(((Exception)serializableExtra).getMessage());
                }
            }
            else if (intent.hasExtra("select_tab")) {
                this.changeTab(intent.getIntExtra("select_tab", this.mViewPager.getCurrentItem()), false);
            }
            else if (intent.hasExtra("disconnected") && intent.getBooleanExtra("disconnected", true)) {
                this.showLandingFragment();
            }
        }
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        boolean onOptionsItemSelected;
        if (menuItem.getItemId() == 16908332) {
            this.startActivity(new Intent((Context)this, (Class)MenuActivity.class));
            this.overridePendingTransition(2131034125, 2131034132);
            onOptionsItemSelected = true;
        }
        else {
            onOptionsItemSelected = super.onOptionsItemSelected(menuItem);
        }
        return onOptionsItemSelected;
    }
    
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mBroadcastReceiver);
        super.onPause();
    }
    
    @Override
    public void onRequestPermissionsResult(final int n, @NonNull final String[] array, @NonNull final int[] array2) {
        switch (n) {
            case 1: {
                if (array2.length > 0 && array2[0] == 0) {
                    App.getInstance().checkForDevice();
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        this.mNotification = null;
        this.checkNotification();
        final Fragment selectedTab = this.getSelectedTab();
        if (selectedTab != null && selectedTab instanceof IPage) {
            ((IPage)selectedTab).onSelected();
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.logitech.ue.centurion.CONNECTION_CHANGED");
        intentFilter.addAction("com.logitech.ue.fragments.STATE_CHANGED");
        intentFilter.addAction("com.logitech.ue.firmware.UPDATE_READY");
        intentFilter.addAction("com.logitech.ue.manifest.MANIFEST_UPDATED");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if (!UEDeviceManager.getInstance().isReady()) {
            Log.w(MainActivity.TAG, "Device manager is NOT ready");
            this.showLandingFragment();
            this.showIntro();
        }
        else {
            Log.d(MainActivity.TAG, "Device manager is ready");
            App.getInstance().checkForDevice();
            this.beginUpdate();
        }
        this.mNotification = null;
        ManifestManager.getInstance().beginUpdateManifest();
        Utils.checkCoarseLocationPermission(this, 1);
    }
    
    @Override
    protected void onStop() {
        this.cancelWaitForReconnect();
        super.onStop();
    }
    
    public void onTitleChanged(final Fragment fragment, final String title) {
        if (this.mPageAdapter.getItem(this.mViewPager.getCurrentItem()) == fragment) {
            this.setTitle(title);
        }
    }
    
    public void setTitle(final int n) {
        this.setTitle(this.getString(n));
    }
    
    public void setTitle(final CharSequence text) {
        Log.d(MainActivity.TAG, "Set title " + (Object)text);
        if ((this.mTitleSwitcher.getCurrentView() == null || !((TextView)this.mTitleSwitcher.getCurrentView()).getText().toString().equals(text)) && !this.mIsWaitingForReconnect) {
            this.mTitleSwitcher.setText(text);
        }
    }
    
    public void setWaitingForReconnect(final boolean mIsWaitingForReconnect) {
        this.mIsWaitingForReconnect = mIsWaitingForReconnect;
    }
    
    public void updateActionBar() {
        final DeviceInfo lastSeenDevice = UserPreferences.getInstance().getLastSeenDevice();
        int color;
        if (lastSeenDevice != null) {
            color = lastSeenDevice.color;
        }
        else {
            color = 0;
        }
        this.updateActionBar(color);
    }
    
    public void updateActionBar(int deviceFabricColor) {
        Log.d(MainActivity.TAG, String.format("Update action bar. Device color: %02X", deviceFabricColor));
        Drawable drawable = null;
        Object o;
        if (UEColourHelper.isDeviceLimitedEdition(deviceFabricColor)) {
            final int n = -1;
            final SVG svg = ContentManager.getInstance().getSVG(UEColourHelper.deviceColorToHEX(deviceFabricColor, false), "header", new ContentLoadListener<SVG>() {
                @Override
                public void onLoadSuccess(final SVG svg, final String s, final String s2) {
                    MainActivity.this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.updateActionBar();
                        }
                    });
                }
            });
            if (svg != null) {
                drawable = SVGRenderCacher.getDrawable(svg, new Rect(0, 0, this.mToolbarBackground.getWidth(), this.mToolbarBackground.getHeight()), (SVGRenderCacher.OnSVGReady)new SVGRenderCacher.OnSVGReady() {
                    @Override
                    public void onReady(final Drawable drawable) {
                        MainActivity.this.updateActionBar();
                    }
                });
            }
            o = drawable;
            deviceFabricColor = n;
            if (drawable == null) {
                o = new ColorDrawable(-16777216);
                deviceFabricColor = n;
            }
        }
        else {
            deviceFabricColor = UEColourHelper.getDeviceFabricColor(deviceFabricColor);
            o = new ColorDrawable(deviceFabricColor);
            if (Utils.isColorBright(deviceFabricColor)) {
                deviceFabricColor = -16777216;
            }
            else {
                deviceFabricColor = -1;
            }
        }
        this.updateActionBar(deviceFabricColor, (Drawable)o);
    }
    
    public void updateActionBar(final int titleTextColor, final Drawable imageDrawable) {
        Log.d(MainActivity.TAG, "Update action bar. Front color " + titleTextColor);
        this.mToolbarFrontColor = titleTextColor;
        this.updateFirmwareUpdateIndicator(this.mIsOTASupported, this.mUpdateInstruction);
        this.setTitleTextColor(titleTextColor);
        this.mToolbarBackground.setImageDrawable(imageDrawable);
    }
    
    public void updateUI(final UEDeviceStatus ueDeviceStatus) {
        this.beginUpdate(ueDeviceStatus);
    }
    
    public class FirmwareUpdateCheckTask extends AttachableTask<Object[]>
    {
        @Override
        public String getTag() {
            return this.getClass().getSimpleName();
        }
        
        @Override
        public void onError(final Exception ex) {
            super.onError(ex);
            MainActivity.this.updateFirmwareUpdateIndicator(false);
        }
        
        public void onSuccess(final Object[] array) {
            super.onSuccess((T)array);
            MainActivity.this.mDeviceColor = (int)array[0];
            MainActivity.this.mIsOTASupported = (boolean)array[1];
            MainActivity.this.mHardwareRevision = (String)array[2];
            MainActivity.this.mFirmwareVersion = (String)array[3];
            MainActivity.this.mSerialNumber = (String)array[4];
            final boolean booleanValue = (boolean)array[5];
            (boolean)array[6];
            MainActivity.this.mUpdateInstruction = FirmwareManager.getInstance().getUpdateInstructionFromCache(Utils.buildUpdateInstructionPamars(MainActivity.this.mDeviceColor, MainActivity.this.mHardwareRevision, MainActivity.this.mFirmwareVersion));
            MainActivity.this.updateFirmwareUpdateIndicator(MainActivity.this.mIsOTASupported, MainActivity.this.mUpdateInstruction);
            final UEDeviceType deviceTypeByColour = UEColour.getDeviceTypeByColour(MainActivity.this.mDeviceColor);
            if (MainActivity.this.mIsOTASupported) {
                if (booleanValue) {
                    if (!UserPreferences.getInstance().isXUPOnBoardingWasSeen()) {
                        MainActivity.this.startXUPOnBoarding();
                    }
                }
                else if (MainActivity.this.mUpdateInstruction != null && MainActivity.this.mUpdateInstruction.isUpdateAvailable && (deviceTypeByColour == UEDeviceType.Kora || deviceTypeByColour == UEDeviceType.Maximus || deviceTypeByColour == UEDeviceType.Titus) && !UserPreferences.getInstance().isXUPSpeakerWasConnected() && !UserPreferences.getInstance().isAppWasRun()) {
                    MainActivity.this.showPartyUpWelcomeFragment(MainActivity.this.mDeviceColor, MainActivity.this.mIsOTASupported);
                }
            }
            else if (deviceTypeByColour == UEDeviceType.Kora && !UserPreferences.getInstance().isXUPSpeakerWasConnected() && !UserPreferences.getInstance().isAppWasRun()) {
                MainActivity.this.showPartyUpWelcomeFragment(MainActivity.this.mDeviceColor, MainActivity.this.mIsOTASupported);
            }
        }
        
        @Override
        public Object[] work(Void... connectedDevice) throws Exception {
            connectedDevice = (Void[])(Object)UEDeviceManager.getInstance().getConnectedDevice();
            final Object[] array = { ((UEGenericDevice)(Object)connectedDevice).getDeviceColor(), ((UEGenericDevice)(Object)connectedDevice).isOTASupported(), ((UEGenericDevice)(Object)connectedDevice).getHardwareRevision(), ((UEGenericDevice)(Object)connectedDevice).getFirmwareVersion(), null, null, null, null };
            while (true) {
                try {
                    array[4] = ((UEGenericDevice)(Object)connectedDevice).getSerialNumber();
                    array[5] = ((UEGenericDevice)(Object)connectedDevice).isBroadcastModeSupported();
                    array[6] = ((UEGenericDevice)(Object)connectedDevice).isBLESupported();
                    return array;
                }
                catch (UEUnrecognisedCommandException ex) {
                    Log.d(MainActivity.TAG, "Serial not supported");
                    array[4] = "";
                    continue;
                }
                break;
            }
        }
    }
    
    public class HomePageAdapter extends FragmentStatePagerAdapter
    {
        BlockPartyFragment mBlockPartyFragment;
        DoubleXUPFragment mDoubleXUPFragment;
        HomeMainFragment mHomeFragment;
        int mPageCount;
        
        public HomePageAdapter(final FragmentManager fragmentManager) {
            super(fragmentManager);
            this.mPageCount = 3;
            this.mHomeFragment = new HomeMainFragment();
            this.mDoubleXUPFragment = new DoubleXUPFragment();
            this.mBlockPartyFragment = new BlockPartyFragment();
        }
        
        @Override
        public int getCount() {
            return this.mPageCount;
        }
        
        @Override
        public Fragment getItem(final int n) {
            Fragment fragment = null;
            switch (n) {
                default: {
                    fragment = null;
                    break;
                }
                case 0: {
                    fragment = this.mHomeFragment;
                    break;
                }
                case 1: {
                    fragment = this.mDoubleXUPFragment;
                    break;
                }
                case 2: {
                    fragment = this.mBlockPartyFragment;
                    break;
                }
            }
            return fragment;
        }
        
        @Override
        public int getItemPosition(final Object o) {
            int n;
            if (o == this.mBlockPartyFragment && this.mPageCount < 3) {
                n = -2;
            }
            else {
                n = -1;
            }
            return n;
        }
        
        public void setPageCount(final int mPageCount) {
            this.mPageCount = mPageCount;
            this.notifyDataSetChanged();
        }
    }
    
    public class UpdateTask extends AttachableTask<Object[]>
    {
        @Override
        public String getTag() {
            return this.getClass().getSimpleName();
        }
        
        public void onSuccess(final Object[] array) {
            super.onSuccess((T)array);
            MainActivity.this.mDeviceColor = (int)array[0];
            if (App.getDeviceConnectionState().isBtClassicConnectedState()) {
                final UEDeviceType deviceTypeByColour = UEColourHelper.getDeviceTypeByColour(MainActivity.this.mDeviceColor);
                if (deviceTypeByColour == UEDeviceType.Kora) {
                    Log.d(MainActivity.TAG, "Connected device type is Kora");
                    MainActivity.this.setPageCount(2);
                    if (MainActivity.this.getCurrentTabIndex() == 2) {
                        MainActivity.this.changeTab(1, false);
                    }
                }
                else if (deviceTypeByColour == UEDeviceType.Unknown) {
                    if (array[1]) {
                        Log.d(MainActivity.TAG, "Connected device type is Unknown and does support BlockParty");
                        MainActivity.this.setPageCount(3);
                    }
                    else {
                        Log.d(MainActivity.TAG, "Connected device type is Unknown and does not support BlockParty");
                        MainActivity.this.setPageCount(2);
                    }
                }
                else {
                    Log.d(MainActivity.TAG, "Connected device type is type");
                    MainActivity.this.setPageCount(3);
                }
            }
        }
        
        @Override
        public Object[] work(final Void... array) throws Exception {
            final UEGenericDevice connectedDevice = UEDeviceManager.getInstance().getConnectedDevice();
            return new Object[] { connectedDevice.getDeviceColor(), connectedDevice.isBlockPartySupported() };
        }
    }
}
