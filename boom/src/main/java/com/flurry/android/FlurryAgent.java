// 
// Decompiled by Procyon v0.5.36
// 

package com.flurry.android;

import com.flurry.sdk.ly;
import android.location.Criteria;
import android.location.Location;
import com.flurry.sdk.ki;
import java.util.Date;
import com.flurry.sdk.lm;
import com.flurry.sdk.mf;
import com.flurry.sdk.jz;
import com.flurry.sdk.jh;
import com.flurry.sdk.hr;
import com.flurry.sdk.lp;
import com.flurry.sdk.jx;
import com.flurry.sdk.jk;
import com.flurry.sdk.ka;
import android.text.TextUtils;
import com.flurry.sdk.km;
import android.os.Build$VERSION;
import java.util.Map;
import android.content.Context;
import com.flurry.sdk.jy;
import com.flurry.sdk.kg;
import com.flurry.sdk.ll;
import com.flurry.sdk.kh;

public final class FlurryAgent
{
    private static final String a;
    private static final kh<ll> b;
    private static FlurryAgentListener c;
    private static boolean d;
    private static int e;
    private static long f;
    private static boolean g;
    private static boolean h;
    private static String i;
    
    static {
        a = FlurryAgent.class.getSimpleName();
        b = new kh<ll>() {};
        FlurryAgent.c = null;
        FlurryAgent.d = false;
        FlurryAgent.e = 5;
        FlurryAgent.f = 10000L;
        FlurryAgent.g = true;
        FlurryAgent.h = false;
        FlurryAgent.i = null;
    }
    
    private FlurryAgent() {
    }
    
    static /* synthetic */ void a(final FlurryAgentListener c, final boolean d, final int e, final long f, final boolean g, final boolean h, final Context context, final String i) {
        setFlurryAgentListener(FlurryAgent.c = c);
        setLogEnabled(FlurryAgent.d = d);
        setLogLevel(FlurryAgent.e = e);
        setContinueSessionMillis(FlurryAgent.f = f);
        setCaptureUncaughtExceptions(FlurryAgent.g = g);
        setPulseEnabled(FlurryAgent.h = h);
        init(context, FlurryAgent.i = i);
    }
    
    public static void addOrigin(final String s, final String s2) {
        addOrigin(s, s2, null);
    }
    
    public static void addOrigin(final String s, final String s2, final Map<String, String> map) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("originName not specified");
            }
            if (TextUtils.isEmpty((CharSequence)s2)) {
                throw new IllegalArgumentException("originVersion not specified");
            }
            try {
                ka.a().a(s, s2, map);
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    public static void addSessionProperty(final String s, final String s2) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (TextUtils.isEmpty((CharSequence)s) || TextUtils.isEmpty((CharSequence)s2)) {
            km.b(FlurryAgent.a, "String name or value passed to addSessionProperty was null or empty.");
        }
        else {
            if (jy.a() == null) {
                throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
            }
            jk.a();
            final jx h = jk.h();
            if (h != null) {
                h.a(s, s2);
            }
        }
    }
    
    public static void clearLocation() {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            lp.a().a("ExplicitLocation", (Object)null);
        }
    }
    
    public static void endTimedEvent(final String str) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (str == null) {
            km.b(FlurryAgent.a, "String eventId passed to endTimedEvent was null.");
        }
        else {
            try {
                hr.a();
                final jh b = hr.b();
                if (b != null) {
                    b.a(str, null);
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "Failed to signify the end of event: " + str, t);
            }
        }
    }
    
    public static void endTimedEvent(final String str, final Map<String, String> map) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (str == null) {
            km.b(FlurryAgent.a, "String eventId passed to endTimedEvent was null.");
        }
        else if (map == null) {
            km.b(FlurryAgent.a, "String eventId passed to endTimedEvent was null.");
        }
        else {
            try {
                hr.a();
                final jh b = hr.b();
                if (b != null) {
                    b.a(str, map);
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "Failed to signify the end of event: " + str, t);
            }
        }
    }
    
    public static int getAgentVersion() {
        return jz.a();
    }
    
    public static String getReleaseVersion() {
        return jz.b();
    }
    
    public static String getSessionId() {
        String b = null;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            if (jy.a() == null) {
                throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
            }
            try {
                jk.a();
                b = jk.b();
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
        return b;
    }
    
    @Deprecated
    public static void init(final Context context, final String s) {
        while (true) {
            synchronized (FlurryAgent.class) {
                if (Build$VERSION.SDK_INT < 10) {
                    km.b(FlurryAgent.a, "Device SDK Version older than 10");
                    return;
                }
                if (context == null) {
                    throw new NullPointerException("Null context");
                }
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                break;
            }
            if (jy.a() != null) {
                km.e(FlurryAgent.a, "Flurry is already initialized");
            }
            try {
                mf.a();
                final Context context2;
                jy.a(context2, s);
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
            return;
        }
        throw new IllegalArgumentException("API key not specified");
    }
    
    public static boolean isSessionActive() {
        boolean c = false;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            try {
                c = lm.a().c();
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
        return c;
    }
    
    public static FlurryEventRecordStatus logEvent(final FlurrySyndicationEventName flurrySyndicationEventName, final String s, final Map<String, String> map) {
        final FlurryEventRecordStatus kFlurryEventFailed = FlurryEventRecordStatus.kFlurryEventFailed;
        FlurryEventRecordStatus flurryEventRecordStatus;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else if (flurrySyndicationEventName == null) {
            km.b(FlurryAgent.a, "String eventName passed to logEvent was null.");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else if (TextUtils.isEmpty((CharSequence)s)) {
            km.b(FlurryAgent.a, "String syndicationId passed to logEvent was null or empty.");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else if (map == null) {
            km.b(FlurryAgent.a, "String parameters passed to logEvent was null.");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else {
            try {
                hr.a();
                final String string = flurrySyndicationEventName.toString();
                final jh b = hr.b();
                flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
                if (b != null) {
                    flurryEventRecordStatus = b.a(string, s, map);
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "Failed to log event: " + flurrySyndicationEventName.toString(), t);
                flurryEventRecordStatus = kFlurryEventFailed;
            }
        }
        return flurryEventRecordStatus;
    }
    
    public static FlurryEventRecordStatus logEvent(final String str) {
        FlurryEventRecordStatus kFlurryEventFailed = FlurryEventRecordStatus.kFlurryEventFailed;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (str == null) {
            km.b(FlurryAgent.a, "String eventId passed to logEvent was null.");
        }
        else {
            while (true) {
                try {
                    hr.a();
                    final jh b = hr.b();
                    FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
                    if (b != null) {
                        flurryEventRecordStatus = b.a(str, null, false);
                    }
                    kFlurryEventFailed = flurryEventRecordStatus;
                }
                catch (Throwable t) {
                    km.a(FlurryAgent.a, "Failed to log event: " + str, t);
                    final FlurryEventRecordStatus flurryEventRecordStatus = kFlurryEventFailed;
                    continue;
                }
                break;
            }
        }
        return kFlurryEventFailed;
    }
    
    public static FlurryEventRecordStatus logEvent(final String str, final Map<String, String> map) {
        final FlurryEventRecordStatus kFlurryEventFailed = FlurryEventRecordStatus.kFlurryEventFailed;
        FlurryEventRecordStatus flurryEventRecordStatus;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else if (str == null) {
            km.b(FlurryAgent.a, "String eventId passed to logEvent was null.");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else if (map == null) {
            km.b(FlurryAgent.a, "String parameters passed to logEvent was null.");
            flurryEventRecordStatus = kFlurryEventFailed;
        }
        else {
            try {
                hr.a();
                final jh b = hr.b();
                flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
                if (b != null) {
                    flurryEventRecordStatus = b.a(str, map, false);
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "Failed to log event: " + str, t);
                flurryEventRecordStatus = kFlurryEventFailed;
            }
        }
        return flurryEventRecordStatus;
    }
    
    public static FlurryEventRecordStatus logEvent(final String str, final Map<String, String> map, final boolean b) {
        FlurryEventRecordStatus kFlurryEventFailed = FlurryEventRecordStatus.kFlurryEventFailed;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (str == null) {
            km.b(FlurryAgent.a, "String eventId passed to logEvent was null.");
        }
        else if (map == null) {
            km.b(FlurryAgent.a, "String parameters passed to logEvent was null.");
        }
        else {
            while (true) {
                try {
                    hr.a();
                    final jh b2 = hr.b();
                    FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
                    if (b2 != null) {
                        flurryEventRecordStatus = b2.a(str, map, b);
                    }
                    kFlurryEventFailed = flurryEventRecordStatus;
                }
                catch (Throwable t) {
                    km.a(FlurryAgent.a, "Failed to log event: " + str, t);
                    final FlurryEventRecordStatus flurryEventRecordStatus = kFlurryEventFailed;
                    continue;
                }
                break;
            }
        }
        return kFlurryEventFailed;
    }
    
    public static FlurryEventRecordStatus logEvent(final String str, final boolean b) {
        FlurryEventRecordStatus kFlurryEventFailed = FlurryEventRecordStatus.kFlurryEventFailed;
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (str == null) {
            km.b(FlurryAgent.a, "String eventId passed to logEvent was null.");
        }
        else {
            while (true) {
                try {
                    hr.a();
                    final jh b2 = hr.b();
                    FlurryEventRecordStatus flurryEventRecordStatus = FlurryEventRecordStatus.kFlurryEventFailed;
                    if (b2 != null) {
                        flurryEventRecordStatus = b2.a(str, null, b);
                    }
                    kFlurryEventFailed = flurryEventRecordStatus;
                }
                catch (Throwable t) {
                    km.a(FlurryAgent.a, "Failed to log event: " + str, t);
                    final FlurryEventRecordStatus flurryEventRecordStatus = kFlurryEventFailed;
                    continue;
                }
                break;
            }
        }
        return kFlurryEventFailed;
    }
    
    public static void onEndSession(final Context context) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            if (context == null) {
                throw new NullPointerException("Null context");
            }
            if (jy.a() == null) {
                throw new IllegalStateException("Flurry SDK must be initialized before ending a session");
            }
            try {
                lm.a().c(context);
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    @Deprecated
    public static void onError(final String s, final String message, final String s2) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (s == null) {
            km.b(FlurryAgent.a, "String errorId passed to onError was null.");
        }
        else if (message == null) {
            km.b(FlurryAgent.a, "String message passed to onError was null.");
        }
        else if (s2 == null) {
            km.b(FlurryAgent.a, "String errorClass passed to onError was null.");
        }
        else {
            while (true) {
                while (true) {
                    Label_0159: {
                        try {
                            hr.a();
                            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                            if (stackTrace == null || stackTrace.length <= 2) {
                                break Label_0159;
                            }
                            final StackTraceElement[] array = new StackTraceElement[stackTrace.length - 2];
                            System.arraycopy(stackTrace, 2, array, 0, array.length);
                            stackTrace = array;
                            final Throwable t = new Throwable(message);
                            t.setStackTrace(stackTrace);
                            final jh b = hr.b();
                            if (b != null) {
                                b.a(s, message, s2, t);
                            }
                        }
                        catch (Throwable t2) {
                            km.a(FlurryAgent.a, "", t2);
                        }
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public static void onError(final String s, final String s2, final Throwable t) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (s == null) {
            km.b(FlurryAgent.a, "String errorId passed to onError was null.");
        }
        else if (s2 == null) {
            km.b(FlurryAgent.a, "String message passed to onError was null.");
        }
        else if (t == null) {
            km.b(FlurryAgent.a, "Throwable passed to onError was null.");
        }
        else {
            try {
                hr.a();
                hr.a(s, s2, t);
            }
            catch (Throwable t2) {
                km.a(FlurryAgent.a, "", t2);
            }
        }
    }
    
    @Deprecated
    public static void onEvent(final String s) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (s == null) {
            km.b(FlurryAgent.a, "String eventId passed to onEvent was null.");
        }
        else {
            try {
                hr.a();
                final jh b = hr.b();
                if (b != null) {
                    b.a(s, null, false);
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    @Deprecated
    public static void onEvent(final String s, final Map<String, String> map) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (s == null) {
            km.b(FlurryAgent.a, "String eventId passed to onEvent was null.");
        }
        else if (map == null) {
            km.b(FlurryAgent.a, "Parameters Map passed to onEvent was null.");
        }
        else {
            try {
                hr.a();
                final jh b = hr.b();
                if (b != null) {
                    b.a(s, map, false);
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    public static void onPageView() {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            try {
                hr.a();
                final jh b = hr.b();
                if (b != null) {
                    b.c();
                }
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    public static void onStartSession(final Context context) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            if (context == null) {
                throw new NullPointerException("Null context");
            }
            if (jy.a() == null) {
                throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
            }
            try {
                lm.a().b(context);
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    @Deprecated
    public static void onStartSession(final Context context, final String s) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            if (context == null) {
                throw new NullPointerException("Null context");
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("Api key not specified");
            }
            if (jy.a() == null) {
                throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
            }
            try {
                lm.a().b(context);
            }
            catch (Throwable t) {
                km.a(FlurryAgent.a, "", t);
            }
        }
    }
    
    public static void setAge(final int n) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (n > 0 && n < 110) {
            lp.a().a("Age", new Date(new Date(System.currentTimeMillis() - n * 31449600000L).getYear(), 1, 1).getTime());
        }
    }
    
    @Deprecated
    public static void setCaptureUncaughtExceptions(final boolean b) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            lp.a().a("CaptureUncaughtExceptions", b);
        }
    }
    
    @Deprecated
    public static void setContinueSessionMillis(final long n) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (n < 5000L) {
            km.b(FlurryAgent.a, "Invalid time set for session resumption: " + n);
        }
        else {
            lp.a().a("ContinueSessionMillis", n);
        }
    }
    
    @Deprecated
    public static void setFlurryAgentListener(final FlurryAgentListener c) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (c == null) {
            km.b(FlurryAgent.a, "Listener cannot be null");
            ki.a().b("com.flurry.android.sdk.FlurrySessionEvent", FlurryAgent.b);
        }
        else {
            FlurryAgent.c = c;
            ki.a().a("com.flurry.android.sdk.FlurrySessionEvent", FlurryAgent.b);
        }
    }
    
    public static void setGender(final byte b) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            switch (b) {
                default: {
                    lp.a().a("Gender", -1);
                    break;
                }
                case 0:
                case 1: {
                    lp.a().a("Gender", b);
                    break;
                }
            }
        }
    }
    
    public static void setLocation(final float n, final float n2) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            final Location location = new Location("Explicit");
            location.setLatitude((double)n);
            location.setLongitude((double)n2);
            lp.a().a("ExplicitLocation", location);
        }
    }
    
    @Deprecated
    public static void setLocationCriteria(final Criteria criteria) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
    }
    
    @Deprecated
    public static void setLogEnabled(final boolean b) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (b) {
            km.b();
        }
        else {
            km.a();
        }
    }
    
    public static void setLogEvents(final boolean b) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            lp.a().a("LogEvents", b);
        }
    }
    
    @Deprecated
    public static void setLogLevel(final int n) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            km.a(n);
        }
    }
    
    @Deprecated
    public static void setPulseEnabled(final boolean b) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            lp.a().a("ProtonEnabled", b);
            if (!b) {
                lp.a().a("analyticsEnabled", true);
            }
        }
    }
    
    public static void setReportLocation(final boolean b) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else {
            lp.a().a("ReportLocation", b);
        }
    }
    
    public static void setSessionOrigin(final String s, final String s2) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (TextUtils.isEmpty((CharSequence)s)) {
            km.b(FlurryAgent.a, "String originName passed to setSessionOrigin was null or empty.");
        }
        else {
            if (jy.a() == null) {
                throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
            }
            jk.a();
            final jx h = jk.h();
            if (h != null) {
                h.a(s);
            }
            jk.a();
            final jx h2 = jk.h();
            if (h2 != null) {
                h2.b(s2);
            }
        }
    }
    
    public static void setUserId(final String s) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (s == null) {
            km.b(FlurryAgent.a, "String userId passed to setUserId was null.");
        }
        else {
            lp.a().a("UserId", ly.b(s));
        }
    }
    
    public static void setVersionName(final String s) {
        if (Build$VERSION.SDK_INT < 10) {
            km.b(FlurryAgent.a, "Device SDK Version older than 10");
        }
        else if (s == null) {
            km.b(FlurryAgent.a, "String versionName passed to setVersionName was null.");
        }
        else {
            lp.a().a("VersionName", s);
        }
    }
    
    public static class Builder
    {
        private static FlurryAgentListener a;
        private boolean b;
        private int c;
        private long d;
        private boolean e;
        private boolean f;
        
        static {
            Builder.a = null;
        }
        
        public Builder() {
            this.b = false;
            this.c = 5;
            this.d = 10000L;
            this.e = true;
            this.f = false;
        }
        
        public void build(final Context context, final String s) {
            FlurryAgent.a(Builder.a, this.b, this.c, this.d, this.e, this.f, context, s);
        }
        
        public Builder withCaptureUncaughtExceptions(final boolean e) {
            this.e = e;
            return this;
        }
        
        public Builder withContinueSessionMillis(final long d) {
            this.d = d;
            return this;
        }
        
        public Builder withListener(final FlurryAgentListener a) {
            Builder.a = a;
            return this;
        }
        
        public Builder withLogEnabled(final boolean b) {
            this.b = b;
            return this;
        }
        
        public Builder withLogLevel(final int c) {
            this.c = c;
            return this;
        }
        
        public Builder withPulseEnabled(final boolean f) {
            this.f = f;
            return this;
        }
    }
}
