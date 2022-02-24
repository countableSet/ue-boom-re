// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.util;

import java.io.PrintWriter;

public final class TimeUtils
{
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;
    
    static {
        sFormatSync = new Object();
        TimeUtils.sFormatStr = new char[24];
    }
    
    private TimeUtils() {
    }
    
    private static int accumField(int n, final int n2, final boolean b, final int n3) {
        if (n > 99 || (b && n3 >= 3)) {
            n = n2 + 3;
        }
        else if (n > 9 || (b && n3 >= 2)) {
            n = n2 + 2;
        }
        else if (b || n > 0) {
            n = n2 + 1;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    public static void formatDuration(final long n, final long n2, final PrintWriter printWriter) {
        if (n == 0L) {
            printWriter.print("--");
        }
        else {
            formatDuration(n - n2, printWriter, 0);
        }
    }
    
    public static void formatDuration(final long n, final PrintWriter printWriter) {
        formatDuration(n, printWriter, 0);
    }
    
    public static void formatDuration(final long n, final PrintWriter printWriter, int formatDurationLocked) {
        synchronized (TimeUtils.sFormatSync) {
            formatDurationLocked = formatDurationLocked(n, formatDurationLocked);
            printWriter.print(new String(TimeUtils.sFormatStr, 0, formatDurationLocked));
        }
    }
    
    public static void formatDuration(final long n, final StringBuilder sb) {
        synchronized (TimeUtils.sFormatSync) {
            sb.append(TimeUtils.sFormatStr, 0, formatDurationLocked(n, 0));
        }
    }
    
    private static int formatDurationLocked(long n, int printField) {
        if (TimeUtils.sFormatStr.length < printField) {
            TimeUtils.sFormatStr = new char[printField];
        }
        final char[] sFormatStr = TimeUtils.sFormatStr;
        if (n == 0L) {
            while (printField - 1 < 0) {
                sFormatStr[0] = 32;
            }
            sFormatStr[0] = 48;
            printField = 1;
        }
        else {
            int n2;
            if (n > 0L) {
                n2 = 43;
            }
            else {
                n2 = 45;
                n = -n;
            }
            final int n3 = (int)(n % 1000L);
            final int n4 = (int)Math.floor((double)(n / 1000L));
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            int n8 = n4;
            if (n4 > 86400) {
                n5 = n4 / 86400;
                n8 = n4 - 86400 * n5;
            }
            int n9;
            if ((n9 = n8) > 3600) {
                n6 = n8 / 3600;
                n9 = n8 - n6 * 3600;
            }
            int n10;
            if ((n10 = n9) > 60) {
                n7 = n9 / 60;
                n10 = n9 - n7 * 60;
            }
            int n11 = 0;
            final int n12 = 0;
            if (printField != 0) {
                final int accumField = accumField(n5, 1, false, 0);
                final int n13 = accumField + accumField(n6, 1, accumField > 0, 2);
                final int n14 = n13 + accumField(n7, 1, n13 > 0, 2);
                final int n15 = n14 + accumField(n10, 1, n14 > 0, 2);
                int n16;
                if (n15 > 0) {
                    n16 = 3;
                }
                else {
                    n16 = 0;
                }
                int n17 = n15 + (accumField(n3, 2, true, n16) + 1);
                int n18 = n12;
                while (true) {
                    n11 = n18;
                    if (n17 >= printField) {
                        break;
                    }
                    sFormatStr[n18] = 32;
                    ++n18;
                    ++n17;
                }
            }
            sFormatStr[n11] = (char)n2;
            final int n19 = n11 + 1;
            if (printField != 0) {
                printField = 1;
            }
            else {
                printField = 0;
            }
            final int printField2 = printField(sFormatStr, n5, 'd', n19, false, 0);
            final boolean b = printField2 != n19;
            int n20;
            if (printField != 0) {
                n20 = 2;
            }
            else {
                n20 = 0;
            }
            final int printField3 = printField(sFormatStr, n6, 'h', printField2, b, n20);
            final boolean b2 = printField3 != n19;
            int n21;
            if (printField != 0) {
                n21 = 2;
            }
            else {
                n21 = 0;
            }
            final int printField4 = printField(sFormatStr, n7, 'm', printField3, b2, n21);
            final boolean b3 = printField4 != n19;
            int n22;
            if (printField != 0) {
                n22 = 2;
            }
            else {
                n22 = 0;
            }
            final int printField5 = printField(sFormatStr, n10, 's', printField4, b3, n22);
            if (printField != 0 && printField5 != n19) {
                printField = 3;
            }
            else {
                printField = 0;
            }
            printField = printField(sFormatStr, n3, 'm', printField5, true, printField);
            sFormatStr[printField] = 115;
            ++printField;
        }
        return printField;
    }
    
    private static int printField(final char[] array, int n, final char c, int n2, final boolean b, int n3) {
        if (!b) {
            final int n4 = n2;
            if (n <= 0) {
                return n4;
            }
        }
        int n5 = 0;
        int n6 = 0;
        Label_0065: {
            if (!b || n3 < 3) {
                n5 = n;
                n6 = n2;
                if (n <= 99) {
                    break Label_0065;
                }
            }
            final int n7 = n / 100;
            array[n2] = (char)(n7 + 48);
            n6 = n2 + 1;
            n5 = n - n7 * 100;
        }
        Label_0126: {
            if ((!b || n3 < 2) && n5 <= 9) {
                n3 = n5;
                if (n2 == (n = n6)) {
                    break Label_0126;
                }
            }
            n2 = n5 / 10;
            array[n6] = (char)(n2 + 48);
            n = n6 + 1;
            n3 = n5 - n2 * 10;
        }
        array[n] = (char)(n3 + 48);
        ++n;
        array[n] = c;
        return n + 1;
    }
}
