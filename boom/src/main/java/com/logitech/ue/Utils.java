// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import android.content.res.TypedArray;
import android.animation.AnimatorSet;
import android.animation.Animator;
import com.logitech.ue.firmware.FirmwareManager;
import com.logitech.ue.firmware.UpdateInstruction;
import android.support.v4.content.ContextCompat;
import android.support.annotation.ColorInt;
import java.util.ArrayList;
import android.view.View;
import android.content.res.Resources;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import com.caverock.androidsvg.SVG;
import java.util.Iterator;
import java.util.List;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;
import android.os.AsyncTask$Status;
import android.os.AsyncTask;
import java.util.Locale;
import android.os.Build$VERSION;
import com.logitech.ue.firmware.UpdateInstructionParams;
import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import android.util.Log;

public class Utils
{
    public static final int BATTERY_LOW_THRESHHOLD = 20;
    public static final int BATTERY_UPPER_THRESHHOLD = 96;
    private static final double EPSILON = 1.0E-5;
    
    public static byte adjustedBatteryLevel(int n) {
        byte b;
        if (n == 0) {
            Log.e("Getting battery level", "Master is reporting a faulty battery level 0");
            n = (b = -1);
        }
        else if (n < 20) {
            n = (b = 0);
        }
        else if (n < 96) {
            n = (b = (byte)(n / 10 * 10));
        }
        else {
            n = (b = 100);
        }
        return b;
    }
    
    public static boolean areSpeakersInPerfectDoubleUpState(final int n, final int n2) {
        final UEDeviceType deviceTypeByColour = UEColourHelper.getDeviceTypeByColour(n);
        return deviceTypeByColour == UEColourHelper.getDeviceTypeByColour(n2) && deviceTypeByColour != UEDeviceType.Unknown;
    }
    
    public static UpdateInstructionParams buildUpdateInstructionPamars(final int n, final String s, final String s2) {
        return new UpdateInstructionParams("orpheum", "5.0.166", UEColourHelper.getDeviceTypeByColour(n).getDeviceTypeName().toLowerCase(), s, s2, "Android", Build$VERSION.RELEASE, Locale.getDefault().getLanguage());
    }
    
    public static void cancelTask(final AsyncTask<?, ?, ?> asyncTask) {
        if (asyncTask != null && (asyncTask.getStatus() == AsyncTask$Status.RUNNING || asyncTask.getStatus() == AsyncTask$Status.PENDING)) {
            asyncTask.cancel(true);
        }
    }
    
    public static void checkCoarseLocationPermission(final Activity activity, final int n) {
        checkPermission(activity, "android.permission.ACCESS_COARSE_LOCATION", n);
    }
    
    public static void checkPermission(final Activity activity, final String s, final int n) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, s)) {
            ActivityCompat.requestPermissions(activity, new String[] { s }, n);
        }
    }
    
    public static void checkReadExternalStoragePermission(final Activity activity, final int n) {
        if (Build$VERSION.SDK_INT >= 16) {
            checkPermission(activity, "android.permission.READ_EXTERNAL_STORAGE", n);
        }
    }
    
    public static int compareFloat(final float f1, final float f2) {
        int compare;
        if (Math.abs(f1 - f2) < 1.0E-5) {
            compare = 0;
        }
        else {
            compare = Float.compare(f1, f2);
        }
        return compare;
    }
    
    public static int[] convertIntegers(final List<Integer> list) {
        final int[] array = new int[list.size()];
        final Iterator<Integer> iterator = list.iterator();
        for (int i = 0; i < array.length; ++i) {
            array[i] = iterator.next();
        }
        return array;
    }
    
    private int dpToPx(final int n) {
        return Math.round(n * App.getInstance().getResources().getDisplayMetrics().density);
    }
    
    public static Bitmap drawSVGToBitmap(final SVG svg) {
        final Bitmap bitmap = Bitmap.createBitmap(Math.round(svg.getDocumentWidth()), Math.round(svg.getDocumentHeight()), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        svg.renderToCanvas(canvas);
        return bitmap;
    }
    
    public static boolean equalsFloat(final float n, final float n2) {
        return compareFloat(n, n2) == 0;
    }
    
    public static int getAndroidLongAnimationTime(final Context context) {
        int androidLongAnimationTime;
        if (context != null) {
            androidLongAnimationTime = getAndroidLongAnimationTime(context.getResources());
        }
        else {
            androidLongAnimationTime = 600;
        }
        return androidLongAnimationTime;
    }
    
    public static int getAndroidLongAnimationTime(final Resources resources) {
        int integer;
        if (resources != null) {
            integer = resources.getInteger(17694722);
        }
        else {
            integer = 600;
        }
        return integer;
    }
    
    public static int getAndroidMediumAnimationTime(final Context context) {
        int androidMediumAnimationTime;
        if (context != null) {
            androidMediumAnimationTime = getAndroidMediumAnimationTime(context.getResources());
        }
        else {
            androidMediumAnimationTime = 400;
        }
        return androidMediumAnimationTime;
    }
    
    public static int getAndroidMediumAnimationTime(final Resources resources) {
        int integer;
        if (resources != null) {
            integer = resources.getInteger(17694721);
        }
        else {
            integer = 400;
        }
        return integer;
    }
    
    public static int getAndroidShortAnimationTime(final Context context) {
        int androidShortAnimationTime;
        if (context != null) {
            androidShortAnimationTime = getAndroidShortAnimationTime(context.getResources());
        }
        else {
            androidShortAnimationTime = 200;
        }
        return androidShortAnimationTime;
    }
    
    public static int getAndroidShortAnimationTime(final Resources resources) {
        int integer;
        if (resources != null) {
            integer = resources.getInteger(17694720);
        }
        else {
            integer = 200;
        }
        return integer;
    }
    
    public static UEDeviceType getAppDefaultDeviceType() {
        UEDeviceType ueDeviceType;
        if ("orpheum".equals("orpheum")) {
            ueDeviceType = UEDeviceType.Kora;
        }
        else if ("orpheum".equals("shoreline")) {
            ueDeviceType = UEDeviceType.Titus;
        }
        else if ("orpheum".equals("voldemort")) {
            ueDeviceType = UEDeviceType.Caribe;
        }
        else {
            ueDeviceType = UEDeviceType.Unknown;
        }
        return ueDeviceType;
    }
    
    public static float getViewCenterX(final View view) {
        return view.getX() + view.getWidth() / 2;
    }
    
    public static float getViewCenterY(final View view) {
        return view.getY() + view.getHeight() / 2;
    }
    
    public static <T> int indexOf(final T[] array, final T t) {
        for (int i = 0; i < array.length; ++i) {
            if (t == null) {
                if (array[i] == null) {
                    return i;
                }
            }
            else {
                final int n = i;
                if (t.equals(array[i])) {
                    return n;
                }
            }
        }
        return -1;
    }
    
    public static ArrayList<Integer> intArrayToArrayList(final int... array) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(array[i]);
        }
        return list;
    }
    
    public static boolean isCoarseLocationPermissionGranted() {
        return isPermissionGranted("android.permission.ACCESS_COARSE_LOCATION");
    }
    
    public static boolean isColorBright(@ColorInt final int n) {
        return 0.2126 * (n >> 16 & 0xFF) + 0.7152 * (n >> 8 & 0xFF) + 0.0722 * (n >> 0 & 0xFF) > 120.0;
    }
    
    public static boolean isPermissionGranted(final String s) {
        return ContextCompat.checkSelfPermission((Context)App.getInstance(), s) == 0;
    }
    
    public static boolean isReadExternalStoragePermissionGranted() {
        return Build$VERSION.SDK_INT < 16 || isPermissionGranted("android.permission.READ_EXTERNAL_STORAGE");
    }
    
    public static boolean isUpdateAvailable(final boolean b, final UpdateInstruction updateInstruction) {
        return b && updateInstruction != null && FirmwareManager.getInstance().isUpdateReady(updateInstruction);
    }
    
    public static boolean isViewAttached(final View view) {
        boolean attachedToWindow;
        if (Build$VERSION.SDK_INT < 19) {
            attachedToWindow = (view.getWindowToken() != null);
        }
        else {
            attachedToWindow = view.isAttachedToWindow();
        }
        return attachedToWindow;
    }
    
    public static void setViewCenterX(final View view, final float n) {
        view.setX(n - view.getWidth() / 2);
    }
    
    public static void setViewCenterY(final View view, final float n) {
        view.setY(n - view.getHeight() / 2);
    }
    
    public static void stopAnimator(final Animator animator) {
        if (!animator.isStarted()) {
            animator.start();
        }
        if (animator instanceof AnimatorSet) {
            final Iterator<Animator> iterator = ((AnimatorSet)animator).getChildAnimations().iterator();
            while (iterator.hasNext()) {
                stopAnimator(iterator.next());
            }
        }
        if (animator.isRunning()) {
            animator.end();
        }
    }
    
    public static ArrayList<Integer> typedArrayToArrayList(final Context context, final int n) {
        final TypedArray obtainTypedArray = context.getResources().obtainTypedArray(n);
        final ArrayList<Integer> typedArrayToArrayList = typedArrayToArrayList(obtainTypedArray);
        obtainTypedArray.recycle();
        return typedArrayToArrayList;
    }
    
    public static ArrayList<Integer> typedArrayToArrayList(final TypedArray typedArray) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < typedArray.length(); ++i) {
            list.add(typedArray.getResourceId(i, 0));
        }
        return list;
    }
    
    public static int[] typedArrayToIntArray(final Context context, final int n) {
        final TypedArray obtainTypedArray = context.getResources().obtainTypedArray(n);
        final int[] typedArrayToIntArray = typedArrayToIntArray(obtainTypedArray);
        obtainTypedArray.recycle();
        return typedArrayToIntArray;
    }
    
    public static int[] typedArrayToIntArray(final TypedArray typedArray) {
        final int[] array = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); ++i) {
            array[i] = typedArray.getResourceId(i, 0);
        }
        return array;
    }
}
