// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.app;

class TwilightCalculator
{
    private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976f;
    private static final float C1 = 0.0334196f;
    private static final float C2 = 3.49066E-4f;
    private static final float C3 = 5.236E-6f;
    public static final int DAY = 0;
    private static final float DEGREES_TO_RADIANS = 0.017453292f;
    private static final float J0 = 9.0E-4f;
    public static final int NIGHT = 1;
    private static final float OBLIQUITY = 0.4092797f;
    private static final long UTC_2000 = 946728000000L;
    private static TwilightCalculator sInstance;
    public int state;
    public long sunrise;
    public long sunset;
    
    static TwilightCalculator getInstance() {
        if (TwilightCalculator.sInstance == null) {
            TwilightCalculator.sInstance = new TwilightCalculator();
        }
        return TwilightCalculator.sInstance;
    }
    
    public void calculateTwilight(final long n, double a, double n2) {
        final float n3 = (n - 946728000000L) / 8.64E7f;
        final float n4 = 6.24006f + 0.01720197f * n3;
        final double a2 = 1.796593063 + (n4 + 0.03341960161924362 * Math.sin(n4) + 3.4906598739326E-4 * Math.sin(2.0f * n4) + 5.236000106378924E-6 * Math.sin(3.0f * n4)) + 3.141592653589793;
        n2 = -n2 / 360.0;
        n2 = 9.0E-4f + Math.round(n3 - 9.0E-4f - n2) + n2 + 0.0053 * Math.sin(n4) + -0.0069 * Math.sin(2.0 * a2);
        final double asin = Math.asin(Math.sin(a2) * Math.sin(0.4092797040939331));
        a *= 0.01745329238474369;
        a = (Math.sin(-0.10471975803375244) - Math.sin(a) * Math.sin(asin)) / (Math.cos(a) * Math.cos(asin));
        if (a >= 1.0) {
            this.state = 1;
            this.sunset = -1L;
            this.sunrise = -1L;
        }
        else if (a <= -1.0) {
            this.state = 0;
            this.sunset = -1L;
            this.sunrise = -1L;
        }
        else {
            final float n5 = (float)(Math.acos(a) / 6.283185307179586);
            this.sunset = Math.round((n5 + n2) * 8.64E7) + 946728000000L;
            this.sunrise = Math.round((n2 - n5) * 8.64E7) + 946728000000L;
            if (this.sunrise < n && this.sunset > n) {
                this.state = 0;
            }
            else {
                this.state = 1;
            }
        }
    }
}
