// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue;

import com.logitech.ue.centurion.device.devicedata.UEDeviceType;
import android.content.Context;
import com.logitech.ue.manifest.DeviceInfo;
import com.logitech.ue.manifest.DeviceColorScheme;
import com.logitech.ue.manifest.ManifestManager;
import com.logitech.ue.centurion.device.devicedata.UEColour;
import android.util.SparseArray;
import android.util.SparseIntArray;

public class UEColourHelper
{
    private static final SparseIntArray accentColourMap;
    private static final SparseIntArray buttonsColourMap;
    private static final SparseArray<UEColour> colourMap;
    private static final SparseIntArray fabricColourMap;
    private static final SparseIntArray spineColourMap;
    
    static {
        (colourMap = new SparseArray(50)).put(UEColour.KORA_WHITE.getCode(), (Object)UEColour.KORA_WHITE);
        UEColourHelper.colourMap.put(UEColour.KORA_PINK.getCode(), (Object)UEColour.KORA_PINK);
        UEColourHelper.colourMap.put(UEColour.KORA_RED.getCode(), (Object)UEColour.KORA_RED);
        UEColourHelper.colourMap.put(UEColour.KORA_BLUE.getCode(), (Object)UEColour.KORA_BLUE);
        UEColourHelper.colourMap.put(UEColour.KORA_BLACK.getCode(), (Object)UEColour.KORA_BLACK);
        UEColourHelper.colourMap.put(UEColour.KORA_MOSS.getCode(), (Object)UEColour.KORA_MOSS);
        UEColourHelper.colourMap.put(UEColour.KORA_BLUE_STEEL.getCode(), (Object)UEColour.KORA_BLUE_STEEL);
        UEColourHelper.colourMap.put(UEColour.KORA_SUPER_HERO.getCode(), (Object)UEColour.KORA_SUPER_HERO);
        UEColourHelper.colourMap.put(UEColour.KORA_AQUA.getCode(), (Object)UEColour.KORA_AQUA);
        UEColourHelper.colourMap.put(UEColour.KORA_CITRUS.getCode(), (Object)UEColour.KORA_CITRUS);
        UEColourHelper.colourMap.put(UEColour.KORA_ORCHID.getCode(), (Object)UEColour.KORA_ORCHID);
        UEColourHelper.colourMap.put(UEColour.RED_ROCK_BLACK_RED.getCode(), (Object)UEColour.RED_ROCK_BLACK_RED);
        UEColourHelper.colourMap.put(UEColour.RED_ROCK_BLACK_BLACK.getCode(), (Object)UEColour.RED_ROCK_BLACK_BLACK);
        UEColourHelper.colourMap.put(UEColour.RED_ROCK_BLACK_YELLOW.getCode(), (Object)UEColour.RED_ROCK_BLACK_YELLOW);
        UEColourHelper.colourMap.put(UEColour.RED_ROCK_ORANGE_WHITE.getCode(), (Object)UEColour.RED_ROCK_ORANGE_WHITE);
        UEColourHelper.colourMap.put(UEColour.RED_ROCK_PURPLE_YELLOW.getCode(), (Object)UEColour.RED_ROCK_PURPLE_YELLOW);
        UEColourHelper.colourMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), (Object)UEColour.TITUS_BLACK_BLACK_BLUE);
        UEColourHelper.colourMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), (Object)UEColour.TITUS_BLUE_BLUE_RED);
        UEColourHelper.colourMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), (Object)UEColour.TITUS_RED_RED_WHITE);
        UEColourHelper.colourMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), (Object)UEColour.TITUS_PURPLE_PURPLE_BLUE);
        UEColourHelper.colourMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), (Object)UEColour.CARIBE_SOLID_CHARCOAL_BLACK);
        UEColourHelper.colourMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), (Object)UEColour.CARIBE_SOLID_BLUE_RED);
        UEColourHelper.colourMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), (Object)UEColour.CARIBE_SOLID_VIOLET_AQUA);
        UEColourHelper.colourMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), (Object)UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY);
        UEColourHelper.colourMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), (Object)UEColour.CARIBE_ORANGE_PEACH);
        UEColourHelper.colourMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), (Object)UEColour.CARIBE_PINK_TEAL);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), (Object)UEColour.MAXIMUS_BLACK_BLACK_WHITE);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), (Object)UEColour.MAXIMUS_WHITE_WHITE_BLACK);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), (Object)UEColour.MAXIMUS_TEAL_GREEN_YELLOW);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), (Object)UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), (Object)UEColour.MAXIMUS_RED_PINK_BLUE);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), (Object)UEColour.MAXIMUS_BLUE_TEAL_RED);
        UEColourHelper.colourMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), (Object)UEColour.MAXIMUS_PURPLE_BLACK_WHITE);
        UEColourHelper.colourMap.put(UEColour.UNKNOWN_COLOUR.getCode(), (Object)UEColour.UNKNOWN_COLOUR);
        UEColourHelper.colourMap.put(UEColour.NO_SPEAKER.getCode(), (Object)UEColour.NO_SPEAKER);
        (accentColourMap = new SparseIntArray(50)).put(UEColour.KORA_WHITE.getCode(), -2635471);
        UEColourHelper.accentColourMap.put(UEColour.KORA_PINK.getCode(), -1441653);
        UEColourHelper.accentColourMap.put(UEColour.KORA_RED.getCode(), -3005654);
        UEColourHelper.accentColourMap.put(UEColour.KORA_BLUE.getCode(), -16732433);
        UEColourHelper.accentColourMap.put(UEColour.KORA_BLACK.getCode(), -1441653);
        UEColourHelper.accentColourMap.put(UEColour.KORA_MOSS.getCode(), -2635471);
        UEColourHelper.accentColourMap.put(UEColour.KORA_BLUE_STEEL.getCode(), -3005654);
        UEColourHelper.accentColourMap.put(UEColour.KORA_SUPER_HERO.getCode(), -3005654);
        UEColourHelper.accentColourMap.put(UEColour.KORA_AQUA.getCode(), -16737715);
        UEColourHelper.accentColourMap.put(UEColour.KORA_CITRUS.getCode(), -16737715);
        UEColourHelper.accentColourMap.put(UEColour.KORA_ORCHID.getCode(), -6532475);
        UEColourHelper.accentColourMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), -16759109);
        UEColourHelper.accentColourMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), -16759109);
        UEColourHelper.accentColourMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), -3665874);
        UEColourHelper.accentColourMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), -4511301);
        UEColourHelper.accentColourMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), -9935261);
        UEColourHelper.accentColourMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), -16758853);
        UEColourHelper.accentColourMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), -6808169);
        UEColourHelper.accentColourMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), -16740694);
        UEColourHelper.accentColourMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), -110592);
        UEColourHelper.accentColourMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), -1686661);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), -14540253);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), -11315622);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), -16738759);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), -375274);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), -2079365);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), -16742966);
        UEColourHelper.accentColourMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), -13817562);
        UEColourHelper.accentColourMap.put(UEColour.RED_ROCK_BLACK_RED.getCode(), -16777216);
        UEColourHelper.accentColourMap.put(UEColour.RED_ROCK_BLACK_BLACK.getCode(), -16777216);
        UEColourHelper.accentColourMap.put(UEColour.RED_ROCK_BLACK_YELLOW.getCode(), -16777216);
        UEColourHelper.accentColourMap.put(UEColour.RED_ROCK_ORANGE_WHITE.getCode(), -61688);
        UEColourHelper.accentColourMap.put(UEColour.RED_ROCK_PURPLE_YELLOW.getCode(), -65281);
        (buttonsColourMap = new SparseIntArray(50)).put(UEColour.KORA_WHITE.getCode(), -2635471);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_PINK.getCode(), -2631721);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_RED.getCode(), -9535610);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_BLUE.getCode(), -2635471);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_BLACK.getCode(), -6750208);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_MOSS.getCode(), -2635471);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_BLUE_STEEL.getCode(), -3727823);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_SUPER_HERO.getCode(), -16777216);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_AQUA.getCode(), -1);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_CITRUS.getCode(), -16738485);
        UEColourHelper.buttonsColourMap.put(UEColour.KORA_ORCHID.getCode(), -3727823);
        UEColourHelper.buttonsColourMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), -16759109);
        UEColourHelper.buttonsColourMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), -3665874);
        UEColourHelper.buttonsColourMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), -1717986919);
        UEColourHelper.buttonsColourMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), -16732979);
        UEColourHelper.buttonsColourMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), -1092825);
        UEColourHelper.buttonsColourMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), -1092825);
        UEColourHelper.buttonsColourMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), -1979136);
        UEColourHelper.buttonsColourMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), -1092825);
        UEColourHelper.buttonsColourMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), -8401459);
        UEColourHelper.buttonsColourMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), -1979136);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), -3092274);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), -11315622);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), -1979136);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), -3092274);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), -16740694);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), -3665874);
        UEColourHelper.buttonsColourMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), -3092274);
        (spineColourMap = new SparseIntArray(50)).put(UEColour.KORA_WHITE.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.KORA_PINK.getCode(), -2339758);
        UEColourHelper.spineColourMap.put(UEColour.KORA_RED.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.KORA_BLUE.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.KORA_BLACK.getCode(), -16777216);
        UEColourHelper.spineColourMap.put(UEColour.KORA_MOSS.getCode(), -16361611);
        UEColourHelper.spineColourMap.put(UEColour.KORA_BLUE_STEEL.getCode(), -16777216);
        UEColourHelper.spineColourMap.put(UEColour.KORA_SUPER_HERO.getCode(), -3727823);
        UEColourHelper.spineColourMap.put(UEColour.KORA_AQUA.getCode(), -16738485);
        UEColourHelper.spineColourMap.put(UEColour.KORA_CITRUS.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.KORA_ORCHID.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), -13817562);
        UEColourHelper.spineColourMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), -16765582);
        UEColourHelper.spineColourMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), -6544843);
        UEColourHelper.spineColourMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), -10083484);
        UEColourHelper.spineColourMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), -9935261);
        UEColourHelper.spineColourMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), -16758853);
        UEColourHelper.spineColourMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), -6808169);
        UEColourHelper.spineColourMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), -16740694);
        UEColourHelper.spineColourMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), -110592);
        UEColourHelper.spineColourMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), -1686661);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), -14540254);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), -16738759);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), -375274);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), -2079365);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), -16742966);
        UEColourHelper.spineColourMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), -12567968);
        UEColourHelper.spineColourMap.put(UEColour.RED_ROCK_BLACK_RED.getCode(), -65536);
        UEColourHelper.spineColourMap.put(UEColour.RED_ROCK_BLACK_BLACK.getCode(), -16777216);
        UEColourHelper.spineColourMap.put(UEColour.RED_ROCK_BLACK_YELLOW.getCode(), -256);
        UEColourHelper.spineColourMap.put(UEColour.RED_ROCK_ORANGE_WHITE.getCode(), -1);
        UEColourHelper.spineColourMap.put(UEColour.RED_ROCK_PURPLE_YELLOW.getCode(), -256);
        (fabricColourMap = new SparseIntArray(50)).put(UEColour.KORA_WHITE.getCode(), -1);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_PINK.getCode(), -4311190);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_RED.getCode(), -4964797);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_BLUE.getCode(), -16751738);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_BLACK.getCode(), -14737633);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_MOSS.getCode(), -7700183);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_BLUE_STEEL.getCode(), -13485990);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_SUPER_HERO.getCode(), -12957561);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_AQUA.getCode(), -16221552);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_CITRUS.getCode(), -4608462);
        UEColourHelper.fabricColourMap.put(UEColour.KORA_ORCHID.getCode(), -7513977);
        UEColourHelper.fabricColourMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), -11315622);
        UEColourHelper.fabricColourMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), -16759109);
        UEColourHelper.fabricColourMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), -3665874);
        UEColourHelper.fabricColourMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), -4511301);
        UEColourHelper.fabricColourMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), -9935261);
        UEColourHelper.fabricColourMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), -16758853);
        UEColourHelper.fabricColourMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), -6808169);
        UEColourHelper.fabricColourMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), -16740694);
        UEColourHelper.fabricColourMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), -110592);
        UEColourHelper.fabricColourMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), -1686661);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), -11315622);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), -1513240);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), -16740694);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), -6808169);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), -3665874);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), -16758853);
        UEColourHelper.fabricColourMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), -12502689);
        UEColourHelper.fabricColourMap.put(UEColour.RED_ROCK_BLACK_RED.getCode(), -65536);
        UEColourHelper.fabricColourMap.put(UEColour.RED_ROCK_BLACK_BLACK.getCode(), -16777216);
        UEColourHelper.fabricColourMap.put(UEColour.RED_ROCK_BLACK_YELLOW.getCode(), -256);
        UEColourHelper.fabricColourMap.put(UEColour.RED_ROCK_ORANGE_WHITE.getCode(), -1);
        UEColourHelper.fabricColourMap.put(UEColour.RED_ROCK_PURPLE_YELLOW.getCode(), -256);
    }
    
    public static String deviceColorToHEX(final int n) {
        return deviceColorToHEX(n, true);
    }
    
    public static String deviceColorToHEX(final int n, final boolean b) {
        String format;
        if (b) {
            format = "%02X";
        }
        else {
            format = "%02x";
        }
        return String.format(format, n & 0xFF);
    }
    
    public static int getDeviceAccentColor(int i) {
        int n = i;
        if (!isValidColour(i)) {
            n = 0;
        }
        if (getNativeDeviceAccentColor(n) != 0) {
            i = getNativeDeviceAccentColor(n);
        }
        else {
            final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(String.format("%02X", n & 0xFF));
            if (deviceInfo != null) {
                for (i = 0; i < deviceInfo.deviceColorSchemeMap.size(); ++i) {
                    if (deviceInfo.deviceColorSchemeMap.get(i).name.equals("UEPartyUp")) {
                        i = (deviceInfo.deviceColorSchemeMap.get(i).accentColor | 0xFF000000);
                        return i;
                    }
                }
                i = -16776961;
            }
            else {
                i = -16776961;
            }
        }
        return i;
    }
    
    public static int getDeviceButtonsColor(int i) {
        int n = i;
        if (!isValidColour(i)) {
            n = 0;
        }
        if (getNativeDeviceButtonsColor(n) != 0) {
            i = getNativeDeviceButtonsColor(n);
        }
        else {
            final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(String.format("%02X", n & 0xFF));
            if (deviceInfo != null) {
                for (i = 0; i < deviceInfo.deviceColorSchemeMap.size(); ++i) {
                    if (deviceInfo.deviceColorSchemeMap.get(i).name.equals("UEPartyUp")) {
                        i = (deviceInfo.deviceColorSchemeMap.get(i).buttonColor | 0xFF000000);
                        return i;
                    }
                }
                i = -16777216;
            }
            else {
                i = -16777216;
            }
        }
        return i;
    }
    
    public static int getDeviceFabricColor(int i) {
        int n = i;
        if (!isValidColour(i)) {
            n = 0;
        }
        if (getNativeDeviceFabricColor(n) != 0) {
            i = getNativeDeviceFabricColor(n);
        }
        else {
            final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(String.format("%02X", n & 0xFF));
            if (deviceInfo != null) {
                i = 0;
                while (i < deviceInfo.deviceColorSchemeMap.size()) {
                    if (deviceInfo.deviceColorSchemeMap.get(i).name.equals("UEPartyUp")) {
                        if (deviceInfo.deviceColorSchemeMap.get(i).fabricColor != null) {
                            i = (deviceInfo.deviceColorSchemeMap.get(i).fabricColor | 0xFF000000);
                            return i;
                        }
                        i = -16777216;
                        return i;
                    }
                    else {
                        ++i;
                    }
                }
                i = -16777216;
            }
            else {
                i = -16777216;
            }
        }
        return i;
    }
    
    public static String getDeviceSpecificName(final int n, final Context context) {
        return context.getString(getDeviceSpecificNameResource(getDeviceTypeByColour(n)));
    }
    
    public static String getDeviceSpecificName(final UEDeviceType ueDeviceType, final Context context) {
        return context.getString(getDeviceSpecificNameResource(ueDeviceType));
    }
    
    public static int getDeviceSpecificNameResource(final UEDeviceType ueDeviceType) {
        int n = 2131165431;
        switch (ueDeviceType) {
            default: {
                n = n;
                return n;
            }
            case Maximus: {
                n = 2131165432;
                return n;
            }
            case Caribe: {
                n = 2131165434;
                return n;
            }
            case Titus: {
                n = 2131165433;
            }
            case Kora: {
                return n;
            }
        }
    }
    
    public static int getDeviceSpineColor(int i) {
        int n = i;
        if (!isValidColour(i)) {
            n = 0;
        }
        if (getNativeDeviceSpineColor(n) != 0) {
            i = getNativeDeviceSpineColor(n);
        }
        else {
            final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(String.format("%02X", n & 0xFF));
            if (deviceInfo != null) {
                for (i = 0; i < deviceInfo.deviceColorSchemeMap.size(); ++i) {
                    if (deviceInfo.deviceColorSchemeMap.get(i).name.equals("UEPartyUp")) {
                        i = (deviceInfo.deviceColorSchemeMap.get(i).strapColor | 0xFF000000);
                        return i;
                    }
                }
                i = -1;
            }
            else {
                i = -1;
            }
        }
        return i;
    }
    
    public static UEDeviceType getDeviceTypeByColour(final int n) {
        UEDeviceType ueDeviceType;
        if (!isValidColour(n)) {
            ueDeviceType = UEDeviceType.Unknown;
        }
        else {
            ueDeviceType = UEColour.getDeviceTypeByColour(n);
            if (ueDeviceType == UEDeviceType.Unknown) {
                final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(String.format("%02X", n & 0xFF));
                if (deviceInfo == null) {
                    ueDeviceType = UEDeviceType.Unknown;
                }
                else if ((ueDeviceType = UEDeviceType.getDeviceTypeByName(deviceInfo.type)) == null) {
                    ueDeviceType = UEDeviceType.Unknown;
                }
            }
        }
        return ueDeviceType;
    }
    
    public static int getNativeDeviceAccentColor(final int n) {
        return UEColourHelper.accentColourMap.get(n);
    }
    
    public static int getNativeDeviceButtonsColor(final int n) {
        return UEColourHelper.buttonsColourMap.get(n);
    }
    
    public static int getNativeDeviceFabricColor(final int n) {
        return UEColourHelper.fabricColourMap.get(n);
    }
    
    public static int getNativeDeviceSpineColor(final int n) {
        return UEColourHelper.spineColourMap.get(n);
    }
    
    public static boolean isDeviceLimitedEdition(int n) {
        final boolean b = false;
        boolean b2;
        if (!isValidColour(n)) {
            b2 = b;
        }
        else {
            b2 = b;
            if (getNativeDeviceFabricColor(n) == 0) {
                final DeviceInfo deviceInfo = ManifestManager.getInstance().getManifest().getDeviceInfo(deviceColorToHEX(n));
                b2 = b;
                if (deviceInfo != null) {
                    n = 0;
                    while (true) {
                        b2 = b;
                        if (n >= deviceInfo.deviceColorSchemeMap.size()) {
                            return b2;
                        }
                        if (deviceInfo.deviceColorSchemeMap.get(n).name.equals("UEPartyUp")) {
                            break;
                        }
                        ++n;
                    }
                    b2 = (deviceInfo.deviceColorSchemeMap.get(n).fabricColor == null);
                }
            }
        }
        return b2;
    }
    
    public static boolean isValidColour(final int n) {
        return n != UEColour.UNKNOWN_COLOUR.getCode() && n != UEColour.NO_SPEAKER.getCode();
    }
}
