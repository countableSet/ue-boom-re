// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.centurion.device.devicedata;

import android.util.SparseArray;

public enum UEColour
{
    CARIBE_ORANGE_PEACH(68), 
    CARIBE_PINK_TEAL(69), 
    CARIBE_SOLID_BLUE_RED(65), 
    CARIBE_SOLID_CHARCOAL_BLACK(64), 
    CARIBE_SOLID_MEMPHIS_GREEN_GREY(67), 
    CARIBE_SOLID_VIOLET_AQUA(66), 
    KORA_AQUA(29), 
    KORA_BLACK(7), 
    KORA_BLUE(6), 
    KORA_BLUE_STEEL(27), 
    KORA_CITRUS(30), 
    KORA_MOSS(8), 
    KORA_ORCHID(31), 
    KORA_PINK(4), 
    KORA_RED(5), 
    KORA_SUPER_HERO(28), 
    KORA_WHITE(3), 
    MAXIMUS_AVS(130), 
    MAXIMUS_BLACK_BLACK_WHITE(96), 
    MAXIMUS_BLUE_TEAL_RED(101), 
    MAXIMUS_PURPLE_BLACK_WHITE(102), 
    MAXIMUS_RED_PINK_BLUE(100), 
    MAXIMUS_TEAL_GREEN_YELLOW(98), 
    MAXIMUS_VIOLET_ORANGE_YELLOW(99), 
    MAXIMUS_WHITE_WHITE_BLACK(97), 
    NO_SPEAKER(255), 
    RED_ROCK_BLACK_BLACK(11), 
    RED_ROCK_BLACK_RED(10), 
    RED_ROCK_BLACK_YELLOW(12), 
    RED_ROCK_ORANGE_WHITE(14), 
    RED_ROCK_PURPLE_YELLOW(15), 
    TITUS_AVS(63), 
    TITUS_BLACK_BLACK_BLUE(48), 
    TITUS_BLUE_BLUE_RED(49), 
    TITUS_PURPLE_PURPLE_BLUE(51), 
    TITUS_RED_RED_WHITE(50), 
    UNKNOWN_COLOUR(-1);
    
    private static final SparseArray<UEColour> colourMap;
    private static final SparseArray<UEDeviceType> colourToDeviceTypeMap;
    final int colourCode;
    
    static {
        (colourMap = new SparseArray(50)).put(UEColour.KORA_WHITE.getCode(), (Object)UEColour.KORA_WHITE);
        UEColour.colourMap.put(UEColour.KORA_PINK.getCode(), (Object)UEColour.KORA_PINK);
        UEColour.colourMap.put(UEColour.KORA_RED.getCode(), (Object)UEColour.KORA_RED);
        UEColour.colourMap.put(UEColour.KORA_BLUE.getCode(), (Object)UEColour.KORA_BLUE);
        UEColour.colourMap.put(UEColour.KORA_BLACK.getCode(), (Object)UEColour.KORA_BLACK);
        UEColour.colourMap.put(UEColour.KORA_MOSS.getCode(), (Object)UEColour.KORA_MOSS);
        UEColour.colourMap.put(UEColour.KORA_BLUE_STEEL.getCode(), (Object)UEColour.KORA_BLUE_STEEL);
        UEColour.colourMap.put(UEColour.KORA_SUPER_HERO.getCode(), (Object)UEColour.KORA_SUPER_HERO);
        UEColour.colourMap.put(UEColour.KORA_AQUA.getCode(), (Object)UEColour.KORA_AQUA);
        UEColour.colourMap.put(UEColour.KORA_CITRUS.getCode(), (Object)UEColour.KORA_CITRUS);
        UEColour.colourMap.put(UEColour.KORA_ORCHID.getCode(), (Object)UEColour.KORA_ORCHID);
        UEColour.colourMap.put(UEColour.RED_ROCK_BLACK_RED.getCode(), (Object)UEColour.RED_ROCK_BLACK_RED);
        UEColour.colourMap.put(UEColour.RED_ROCK_BLACK_BLACK.getCode(), (Object)UEColour.RED_ROCK_BLACK_BLACK);
        UEColour.colourMap.put(UEColour.RED_ROCK_BLACK_YELLOW.getCode(), (Object)UEColour.RED_ROCK_BLACK_YELLOW);
        UEColour.colourMap.put(UEColour.RED_ROCK_ORANGE_WHITE.getCode(), (Object)UEColour.RED_ROCK_ORANGE_WHITE);
        UEColour.colourMap.put(UEColour.RED_ROCK_PURPLE_YELLOW.getCode(), (Object)UEColour.RED_ROCK_PURPLE_YELLOW);
        UEColour.colourMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), (Object)UEColour.TITUS_BLACK_BLACK_BLUE);
        UEColour.colourMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), (Object)UEColour.TITUS_BLUE_BLUE_RED);
        UEColour.colourMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), (Object)UEColour.TITUS_RED_RED_WHITE);
        UEColour.colourMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), (Object)UEColour.TITUS_PURPLE_PURPLE_BLUE);
        UEColour.colourMap.put(UEColour.TITUS_AVS.getCode(), (Object)UEColour.TITUS_AVS);
        UEColour.colourMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), (Object)UEColour.CARIBE_SOLID_CHARCOAL_BLACK);
        UEColour.colourMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), (Object)UEColour.CARIBE_SOLID_BLUE_RED);
        UEColour.colourMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), (Object)UEColour.CARIBE_SOLID_VIOLET_AQUA);
        UEColour.colourMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), (Object)UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY);
        UEColour.colourMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), (Object)UEColour.CARIBE_ORANGE_PEACH);
        UEColour.colourMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), (Object)UEColour.CARIBE_PINK_TEAL);
        UEColour.colourMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), (Object)UEColour.MAXIMUS_BLACK_BLACK_WHITE);
        UEColour.colourMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), (Object)UEColour.MAXIMUS_WHITE_WHITE_BLACK);
        UEColour.colourMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), (Object)UEColour.MAXIMUS_TEAL_GREEN_YELLOW);
        UEColour.colourMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), (Object)UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW);
        UEColour.colourMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), (Object)UEColour.MAXIMUS_RED_PINK_BLUE);
        UEColour.colourMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), (Object)UEColour.MAXIMUS_BLUE_TEAL_RED);
        UEColour.colourMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), (Object)UEColour.MAXIMUS_PURPLE_BLACK_WHITE);
        UEColour.colourMap.put(UEColour.MAXIMUS_AVS.getCode(), (Object)UEColour.MAXIMUS_AVS);
        UEColour.colourMap.put(-1, (Object)UEColour.UNKNOWN_COLOUR);
        UEColour.colourMap.put(255, (Object)UEColour.NO_SPEAKER);
        (colourToDeviceTypeMap = new SparseArray(50)).put(UEColour.KORA_WHITE.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_PINK.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_RED.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_BLUE.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_BLACK.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_MOSS.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_BLUE_STEEL.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_SUPER_HERO.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_AQUA.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_CITRUS.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.KORA_ORCHID.getCode(), (Object)UEDeviceType.Kora);
        UEColour.colourToDeviceTypeMap.put(UEColour.RED_ROCK_BLACK_RED.getCode(), (Object)UEDeviceType.RedRocks);
        UEColour.colourToDeviceTypeMap.put(UEColour.RED_ROCK_BLACK_BLACK.getCode(), (Object)UEDeviceType.RedRocks);
        UEColour.colourToDeviceTypeMap.put(UEColour.RED_ROCK_BLACK_YELLOW.getCode(), (Object)UEDeviceType.RedRocks);
        UEColour.colourToDeviceTypeMap.put(UEColour.RED_ROCK_ORANGE_WHITE.getCode(), (Object)UEDeviceType.RedRocks);
        UEColour.colourToDeviceTypeMap.put(UEColour.RED_ROCK_PURPLE_YELLOW.getCode(), (Object)UEDeviceType.RedRocks);
        UEColour.colourToDeviceTypeMap.put(UEColour.TITUS_BLACK_BLACK_BLUE.getCode(), (Object)UEDeviceType.Titus);
        UEColour.colourToDeviceTypeMap.put(UEColour.TITUS_BLUE_BLUE_RED.getCode(), (Object)UEDeviceType.Titus);
        UEColour.colourToDeviceTypeMap.put(UEColour.TITUS_RED_RED_WHITE.getCode(), (Object)UEDeviceType.Titus);
        UEColour.colourToDeviceTypeMap.put(UEColour.TITUS_PURPLE_PURPLE_BLUE.getCode(), (Object)UEDeviceType.Titus);
        UEColour.colourToDeviceTypeMap.put(UEColour.TITUS_AVS.getCode(), (Object)UEDeviceType.Titus);
        UEColour.colourToDeviceTypeMap.put(UEColour.CARIBE_SOLID_CHARCOAL_BLACK.getCode(), (Object)UEDeviceType.Caribe);
        UEColour.colourToDeviceTypeMap.put(UEColour.CARIBE_SOLID_BLUE_RED.getCode(), (Object)UEDeviceType.Caribe);
        UEColour.colourToDeviceTypeMap.put(UEColour.CARIBE_SOLID_VIOLET_AQUA.getCode(), (Object)UEDeviceType.Caribe);
        UEColour.colourToDeviceTypeMap.put(UEColour.CARIBE_SOLID_MEMPHIS_GREEN_GREY.getCode(), (Object)UEDeviceType.Caribe);
        UEColour.colourToDeviceTypeMap.put(UEColour.CARIBE_ORANGE_PEACH.getCode(), (Object)UEDeviceType.Caribe);
        UEColour.colourToDeviceTypeMap.put(UEColour.CARIBE_PINK_TEAL.getCode(), (Object)UEDeviceType.Caribe);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_BLACK_BLACK_WHITE.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_WHITE_WHITE_BLACK.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_TEAL_GREEN_YELLOW.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_VIOLET_ORANGE_YELLOW.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_RED_PINK_BLUE.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_BLUE_TEAL_RED.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_PURPLE_BLACK_WHITE.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.MAXIMUS_AVS.getCode(), (Object)UEDeviceType.Maximus);
        UEColour.colourToDeviceTypeMap.put(UEColour.UNKNOWN_COLOUR.getCode(), (Object)UEDeviceType.Unknown);
        UEColour.colourToDeviceTypeMap.put(UEColour.NO_SPEAKER.getCode(), (Object)UEDeviceType.Unknown);
    }
    
    private UEColour(final int colourCode) {
        this.colourCode = colourCode;
    }
    
    public static int getCode(final UEColour ueColour) {
        return ueColour.colourCode;
    }
    
    public static UEColour getDeviceColour(final int n) {
        UEColour no_SPEAKER;
        final UEColour ueColour = no_SPEAKER = (UEColour)UEColour.colourMap.get(n);
        if (ueColour == null) {
            no_SPEAKER = ueColour;
            if (n != 255) {
                no_SPEAKER = UEColour.NO_SPEAKER;
            }
        }
        return no_SPEAKER;
    }
    
    public static UEDeviceType getDeviceTypeByColour(final int n) {
        return (UEDeviceType)UEColour.colourToDeviceTypeMap.get(n, (Object)UEDeviceType.Unknown);
    }
    
    public int getCode() {
        return this.colourCode;
    }
}
