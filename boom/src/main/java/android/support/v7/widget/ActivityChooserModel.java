// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import java.util.Collection;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
    private static final String ATTRIBUTE_ACTIVITY = "activity";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_WEIGHT = "weight";
    private static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    private static final String LOG_TAG;
    private static final String TAG_HISTORICAL_RECORD = "historical-record";
    private static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    private boolean mCanReadHistoricalData;
    private final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    private final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;
    
    static {
        LOG_TAG = ActivityChooserModel.class.getSimpleName();
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }
    
    private ActivityChooserModel(final Context context, final String s) {
        this.mInstanceLock = new Object();
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = (ActivitySorter)new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)s) && !s.endsWith(".xml")) {
            this.mHistoryFileName = s + ".xml";
        }
        else {
            this.mHistoryFileName = s;
        }
    }
    
    private boolean addHisoricalRecord(final HistoricalRecord historicalRecord) {
        final boolean add = this.mHistoricalRecords.add(historicalRecord);
        if (add) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return add;
    }
    
    private void ensureConsistentState() {
        final boolean loadActivitiesIfNeeded = this.loadActivitiesIfNeeded();
        final boolean historicalDataIfNeeded = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (loadActivitiesIfNeeded | historicalDataIfNeeded) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }
    
    public static ActivityChooserModel get(final Context context, final String s) {
        synchronized (ActivityChooserModel.sRegistryLock) {
            ActivityChooserModel activityChooserModel;
            if ((activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s)) == null) {
                activityChooserModel = new ActivityChooserModel(context, s);
                ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel);
            }
            return activityChooserModel;
        }
    }
    
    private boolean loadActivitiesIfNeeded() {
        boolean b = false;
        if (this.mReloadActivities) {
            b = b;
            if (this.mIntent != null) {
                this.mReloadActivities = false;
                this.mActivities.clear();
                final List queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
                for (int size = queryIntentActivities.size(), i = 0; i < size; ++i) {
                    this.mActivities.add(new ActivityResolveInfo(queryIntentActivities.get(i)));
                }
                b = true;
            }
        }
        return b;
    }
    
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
                AsyncTaskCompat.executeParallel((android.os.AsyncTask<Object, Object, Object>)new PersistHistoryAsyncTask(), new ArrayList(this.mHistoricalRecords), this.mHistoryFileName);
            }
        }
    }
    
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n > 0) {
            this.mHistoricalRecordsChanged = true;
            for (int i = 0; i < n; ++i) {
                final HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
            }
        }
    }
    
    private boolean readHistoricalDataIfNeeded() {
        boolean b = true;
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
        }
        else {
            b = false;
        }
        return b;
    }
    
    private void readHistoricalDataImpl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //     8: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
        //    11: astore_1       
        //    12: invokestatic    android/util/Xml.newPullParser:()Lorg/xmlpull/v1/XmlPullParser;
        //    15: astore_2       
        //    16: aload_2        
        //    17: aload_1        
        //    18: ldc_w           "UTF-8"
        //    21: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    26: iconst_0       
        //    27: istore_3       
        //    28: iload_3        
        //    29: iconst_1       
        //    30: if_icmpeq       50
        //    33: iload_3        
        //    34: iconst_2       
        //    35: if_icmpeq       50
        //    38: aload_2        
        //    39: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    44: istore_3       
        //    45: goto            28
        //    48: astore_1       
        //    49: return         
        //    50: ldc             "historical-records"
        //    52: aload_2        
        //    53: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    58: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    61: ifne            136
        //    64: new             Lorg/xmlpull/v1/XmlPullParserException;
        //    67: astore          4
        //    69: aload           4
        //    71: ldc_w           "Share records file does not start with historical-records tag."
        //    74: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //    77: aload           4
        //    79: athrow         
        //    80: astore          4
        //    82: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //    85: astore_2       
        //    86: new             Ljava/lang/StringBuilder;
        //    89: astore          5
        //    91: aload           5
        //    93: invokespecial   java/lang/StringBuilder.<init>:()V
        //    96: aload_2        
        //    97: aload           5
        //    99: ldc_w           "Error reading historical recrod file: "
        //   102: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   105: aload_0        
        //   106: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   109: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   112: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   115: aload           4
        //   117: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   120: pop            
        //   121: aload_1        
        //   122: ifnull          49
        //   125: aload_1        
        //   126: invokevirtual   java/io/FileInputStream.close:()V
        //   129: goto            49
        //   132: astore_1       
        //   133: goto            49
        //   136: aload_0        
        //   137: getfield        android/support/v7/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //   140: astore          6
        //   142: aload           6
        //   144: invokeinterface java/util/List.clear:()V
        //   149: aload_2        
        //   150: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //   155: istore_3       
        //   156: iload_3        
        //   157: iconst_1       
        //   158: if_icmpne       176
        //   161: aload_1        
        //   162: ifnull          49
        //   165: aload_1        
        //   166: invokevirtual   java/io/FileInputStream.close:()V
        //   169: goto            49
        //   172: astore_1       
        //   173: goto            49
        //   176: iload_3        
        //   177: iconst_3       
        //   178: if_icmpeq       149
        //   181: iload_3        
        //   182: iconst_4       
        //   183: if_icmpeq       149
        //   186: ldc             "historical-record"
        //   188: aload_2        
        //   189: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   194: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   197: ifne            272
        //   200: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   203: astore          4
        //   205: aload           4
        //   207: ldc_w           "Share records file not well-formed."
        //   210: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   213: aload           4
        //   215: athrow         
        //   216: astore_2       
        //   217: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   220: astore          5
        //   222: new             Ljava/lang/StringBuilder;
        //   225: astore          4
        //   227: aload           4
        //   229: invokespecial   java/lang/StringBuilder.<init>:()V
        //   232: aload           5
        //   234: aload           4
        //   236: ldc_w           "Error reading historical recrod file: "
        //   239: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   242: aload_0        
        //   243: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   246: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   249: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   252: aload_2        
        //   253: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   256: pop            
        //   257: aload_1        
        //   258: ifnull          49
        //   261: aload_1        
        //   262: invokevirtual   java/io/FileInputStream.close:()V
        //   265: goto            49
        //   268: astore_1       
        //   269: goto            49
        //   272: aload_2        
        //   273: aconst_null    
        //   274: ldc             "activity"
        //   276: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   281: astore          5
        //   283: aload_2        
        //   284: aconst_null    
        //   285: ldc             "time"
        //   287: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   292: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   295: lstore          7
        //   297: aload_2        
        //   298: aconst_null    
        //   299: ldc             "weight"
        //   301: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   306: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   309: fstore          9
        //   311: new             Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
        //   314: astore          4
        //   316: aload           4
        //   318: aload           5
        //   320: lload           7
        //   322: fload           9
        //   324: invokespecial   android/support/v7/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   327: aload           6
        //   329: aload           4
        //   331: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   336: pop            
        //   337: goto            149
        //   340: astore          4
        //   342: aload_1        
        //   343: ifnull          350
        //   346: aload_1        
        //   347: invokevirtual   java/io/FileInputStream.close:()V
        //   350: aload           4
        //   352: athrow         
        //   353: astore_1       
        //   354: goto            350
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     48     49     Ljava/io/FileNotFoundException;
        //  12     26     80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     216    272    Ljava/io/IOException;
        //  12     26     340    357    Any
        //  38     45     80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     216    272    Ljava/io/IOException;
        //  38     45     340    357    Any
        //  50     80     80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  50     80     216    272    Ljava/io/IOException;
        //  50     80     340    357    Any
        //  82     121    340    357    Any
        //  125    129    132    136    Ljava/io/IOException;
        //  136    149    80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  136    149    216    272    Ljava/io/IOException;
        //  136    149    340    357    Any
        //  149    156    80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  149    156    216    272    Ljava/io/IOException;
        //  149    156    340    357    Any
        //  165    169    172    176    Ljava/io/IOException;
        //  186    216    80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  186    216    216    272    Ljava/io/IOException;
        //  186    216    340    357    Any
        //  217    257    340    357    Any
        //  261    265    268    272    Ljava/io/IOException;
        //  272    337    80     136    Lorg/xmlpull/v1/XmlPullParserException;
        //  272    337    216    272    Ljava/io/IOException;
        //  272    337    340    357    Any
        //  346    350    353    357    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0028:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean sortActivitiesIfNeeded() {
        boolean b;
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public Intent chooseActivity(final int n) {
        synchronized (this.mInstanceLock) {
            Intent intent;
            if (this.mIntent == null) {
                // monitorexit(this.mInstanceLock)
                intent = null;
            }
            else {
                this.ensureConsistentState();
                final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
                final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
                intent = new Intent(this.mIntent);
                intent.setComponent(component);
                if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                    // monitorexit(this.mInstanceLock)
                    intent = null;
                }
                else {
                    this.addHisoricalRecord(new HistoricalRecord(component, System.currentTimeMillis(), 1.0f));
                }
                // monitorexit(this.mInstanceLock)
            }
            return intent;
        }
    }
    
    public ResolveInfo getActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.get(n).resolveInfo;
        }
    }
    
    public int getActivityCount() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }
    
    public int getActivityIndex(final ResolveInfo resolveInfo) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final List<ActivityResolveInfo> mActivities = this.mActivities;
            for (int size = mActivities.size(), i = 0; i < size; ++i) {
                if (mActivities.get(i).resolveInfo == resolveInfo) {
                    return i;
                }
            }
            // monitorexit(this.mInstanceLock)
            return -1;
        }
    }
    
    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            ResolveInfo resolveInfo;
            if (!this.mActivities.isEmpty()) {
                resolveInfo = this.mActivities.get(0).resolveInfo;
            }
            else {
                // monitorexit(this.mInstanceLock)
                resolveInfo = null;
            }
            return resolveInfo;
        }
    }
    
    public int getHistoryMaxSize() {
        synchronized (this.mInstanceLock) {
            return this.mHistoryMaxSize;
        }
    }
    
    public int getHistorySize() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }
    
    public Intent getIntent() {
        synchronized (this.mInstanceLock) {
            return this.mIntent;
        }
    }
    
    public void setActivitySorter(final ActivitySorter mActivitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter != mActivitySorter) {
                this.mActivitySorter = mActivitySorter;
                if (this.sortActivitiesIfNeeded()) {
                    this.notifyChanged();
                }
            }
            // monitorexit(this.mInstanceLock)
        }
    }
    
    public void setDefaultActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
            float n2;
            if (activityResolveInfo2 != null) {
                n2 = activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f;
            }
            else {
                n2 = 1.0f;
            }
            this.addHisoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), n2));
        }
    }
    
    public void setHistoryMaxSize(final int mHistoryMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize != mHistoryMaxSize) {
                this.mHistoryMaxSize = mHistoryMaxSize;
                this.pruneExcessiveHistoricalRecordsIfNeeded();
                if (this.sortActivitiesIfNeeded()) {
                    this.notifyChanged();
                }
            }
            // monitorexit(this.mInstanceLock)
        }
    }
    
    public void setIntent(final Intent mIntent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent != mIntent) {
                this.mIntent = mIntent;
                this.mReloadActivities = true;
                this.ensureConsistentState();
            }
            // monitorexit(this.mInstanceLock)
        }
    }
    
    public void setOnChooseActivityListener(final OnChooseActivityListener mActivityChoserModelPolicy) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = mActivityChoserModelPolicy;
        }
    }
    
    public interface ActivityChooserModelClient
    {
        void setActivityChooserModel(final ActivityChooserModel p0);
    }
    
    public final class ActivityResolveInfo implements Comparable<ActivityResolveInfo>
    {
        public final ResolveInfo resolveInfo;
        public float weight;
        
        public ActivityResolveInfo(final ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }
        
        @Override
        public int compareTo(final ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this != o) {
                if (o == null) {
                    b = false;
                }
                else if (this.getClass() != o.getClass()) {
                    b = false;
                }
                else if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo)o).weight)) {
                    b = false;
                }
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("resolveInfo:").append(this.resolveInfo.toString());
            sb.append("; weight:").append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface ActivitySorter
    {
        void sort(final Intent p0, final List<ActivityResolveInfo> p1, final List<HistoricalRecord> p2);
    }
    
    private final class DefaultSorter implements ActivitySorter
    {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;
        
        private DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }
        
        @Override
        public void sort(final Intent intent, final List<ActivityResolveInfo> list, final List<HistoricalRecord> list2) {
            final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = this.mPackageNameToActivityMap;
            mPackageNameToActivityMap.clear();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                mPackageNameToActivityMap.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            int j = list2.size();
            float n = 1.0f;
            --j;
            while (j >= 0) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                float n2 = n;
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n2 = n * 0.95f;
                }
                --j;
                n = n2;
            }
            Collections.sort((List<Comparable>)list);
        }
    }
    
    public static final class HistoricalRecord
    {
        public final ComponentName activity;
        public final long time;
        public final float weight;
        
        public HistoricalRecord(final ComponentName activity, final long time, final float weight) {
            this.activity = activity;
            this.time = time;
            this.weight = weight;
        }
        
        public HistoricalRecord(final String s, final long n, final float n2) {
            this(ComponentName.unflattenFromString(s), n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this != o) {
                if (o == null) {
                    b = false;
                }
                else if (this.getClass() != o.getClass()) {
                    b = false;
                }
                else {
                    final HistoricalRecord historicalRecord = (HistoricalRecord)o;
                    if (this.activity == null) {
                        if (historicalRecord.activity != null) {
                            b = false;
                            return b;
                        }
                    }
                    else if (!this.activity.equals((Object)historicalRecord.activity)) {
                        b = false;
                        return b;
                    }
                    if (this.time != historicalRecord.time) {
                        b = false;
                    }
                    else if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(historicalRecord.weight)) {
                        b = false;
                    }
                }
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            int hashCode;
            if (this.activity == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.activity.hashCode();
            }
            return ((hashCode + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("; activity:").append(this.activity);
            sb.append("; time:").append(this.time);
            sb.append("; weight:").append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface OnChooseActivityListener
    {
        boolean onChooseActivity(final ActivityChooserModel p0, final Intent p1);
    }
    
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
    {
        public Void doInBackground(final Object... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: iconst_0       
            //     2: aaload         
            //     3: checkcast       Ljava/util/List;
            //     6: astore_2       
            //     7: aload_1        
            //     8: iconst_1       
            //     9: aaload         
            //    10: checkcast       Ljava/lang/String;
            //    13: astore_3       
            //    14: aload_0        
            //    15: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //    18: invokestatic    android/support/v7/widget/ActivityChooserModel.access$200:(Landroid/support/v7/widget/ActivityChooserModel;)Landroid/content/Context;
            //    21: aload_3        
            //    22: iconst_0       
            //    23: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    26: astore_1       
            //    27: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    30: astore          4
            //    32: aload           4
            //    34: aload_1        
            //    35: aconst_null    
            //    36: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    41: aload           4
            //    43: ldc             "UTF-8"
            //    45: iconst_1       
            //    46: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    49: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    54: aload           4
            //    56: aconst_null    
            //    57: ldc             "historical-records"
            //    59: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //    64: pop            
            //    65: aload_2        
            //    66: invokeinterface java/util/List.size:()I
            //    71: istore          5
            //    73: iconst_0       
            //    74: istore          6
            //    76: iload           6
            //    78: iload           5
            //    80: if_icmpge       206
            //    83: aload_2        
            //    84: iconst_0       
            //    85: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    90: checkcast       Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
            //    93: astore_3       
            //    94: aload           4
            //    96: aconst_null    
            //    97: ldc             "historical-record"
            //    99: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   104: pop            
            //   105: aload           4
            //   107: aconst_null    
            //   108: ldc             "activity"
            //   110: aload_3        
            //   111: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   114: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   117: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   122: pop            
            //   123: aload           4
            //   125: aconst_null    
            //   126: ldc             "time"
            //   128: aload_3        
            //   129: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   132: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   135: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   140: pop            
            //   141: aload           4
            //   143: aconst_null    
            //   144: ldc             "weight"
            //   146: aload_3        
            //   147: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   150: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   153: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   158: pop            
            //   159: aload           4
            //   161: aconst_null    
            //   162: ldc             "historical-record"
            //   164: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   169: pop            
            //   170: iinc            6, 1
            //   173: goto            76
            //   176: astore_1       
            //   177: invokestatic    android/support/v7/widget/ActivityChooserModel.access$300:()Ljava/lang/String;
            //   180: new             Ljava/lang/StringBuilder;
            //   183: dup            
            //   184: invokespecial   java/lang/StringBuilder.<init>:()V
            //   187: ldc             "Error writing historical recrod file: "
            //   189: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   192: aload_3        
            //   193: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   196: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   199: aload_1        
            //   200: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   203: pop            
            //   204: aconst_null    
            //   205: areturn        
            //   206: aload           4
            //   208: aconst_null    
            //   209: ldc             "historical-records"
            //   211: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   216: pop            
            //   217: aload           4
            //   219: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   224: aload_0        
            //   225: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   228: iconst_1       
            //   229: invokestatic    android/support/v7/widget/ActivityChooserModel.access$502:(Landroid/support/v7/widget/ActivityChooserModel;Z)Z
            //   232: pop            
            //   233: aload_1        
            //   234: ifnull          241
            //   237: aload_1        
            //   238: invokevirtual   java/io/FileOutputStream.close:()V
            //   241: goto            204
            //   244: astore_3       
            //   245: invokestatic    android/support/v7/widget/ActivityChooserModel.access$300:()Ljava/lang/String;
            //   248: astore_2       
            //   249: new             Ljava/lang/StringBuilder;
            //   252: astore          4
            //   254: aload           4
            //   256: invokespecial   java/lang/StringBuilder.<init>:()V
            //   259: aload_2        
            //   260: aload           4
            //   262: ldc             "Error writing historical recrod file: "
            //   264: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   267: aload_0        
            //   268: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   271: invokestatic    android/support/v7/widget/ActivityChooserModel.access$400:(Landroid/support/v7/widget/ActivityChooserModel;)Ljava/lang/String;
            //   274: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   277: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   280: aload_3        
            //   281: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   284: pop            
            //   285: aload_0        
            //   286: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   289: iconst_1       
            //   290: invokestatic    android/support/v7/widget/ActivityChooserModel.access$502:(Landroid/support/v7/widget/ActivityChooserModel;Z)Z
            //   293: pop            
            //   294: aload_1        
            //   295: ifnull          241
            //   298: aload_1        
            //   299: invokevirtual   java/io/FileOutputStream.close:()V
            //   302: goto            241
            //   305: astore_1       
            //   306: goto            241
            //   309: astore          4
            //   311: invokestatic    android/support/v7/widget/ActivityChooserModel.access$300:()Ljava/lang/String;
            //   314: astore_2       
            //   315: new             Ljava/lang/StringBuilder;
            //   318: astore_3       
            //   319: aload_3        
            //   320: invokespecial   java/lang/StringBuilder.<init>:()V
            //   323: aload_2        
            //   324: aload_3        
            //   325: ldc             "Error writing historical recrod file: "
            //   327: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   330: aload_0        
            //   331: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   334: invokestatic    android/support/v7/widget/ActivityChooserModel.access$400:(Landroid/support/v7/widget/ActivityChooserModel;)Ljava/lang/String;
            //   337: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   340: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   343: aload           4
            //   345: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   348: pop            
            //   349: aload_0        
            //   350: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   353: iconst_1       
            //   354: invokestatic    android/support/v7/widget/ActivityChooserModel.access$502:(Landroid/support/v7/widget/ActivityChooserModel;Z)Z
            //   357: pop            
            //   358: aload_1        
            //   359: ifnull          241
            //   362: aload_1        
            //   363: invokevirtual   java/io/FileOutputStream.close:()V
            //   366: goto            241
            //   369: astore_1       
            //   370: goto            241
            //   373: astore_3       
            //   374: invokestatic    android/support/v7/widget/ActivityChooserModel.access$300:()Ljava/lang/String;
            //   377: astore          4
            //   379: new             Ljava/lang/StringBuilder;
            //   382: astore_2       
            //   383: aload_2        
            //   384: invokespecial   java/lang/StringBuilder.<init>:()V
            //   387: aload           4
            //   389: aload_2        
            //   390: ldc             "Error writing historical recrod file: "
            //   392: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   395: aload_0        
            //   396: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   399: invokestatic    android/support/v7/widget/ActivityChooserModel.access$400:(Landroid/support/v7/widget/ActivityChooserModel;)Ljava/lang/String;
            //   402: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   405: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   408: aload_3        
            //   409: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   412: pop            
            //   413: aload_0        
            //   414: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   417: iconst_1       
            //   418: invokestatic    android/support/v7/widget/ActivityChooserModel.access$502:(Landroid/support/v7/widget/ActivityChooserModel;Z)Z
            //   421: pop            
            //   422: aload_1        
            //   423: ifnull          241
            //   426: aload_1        
            //   427: invokevirtual   java/io/FileOutputStream.close:()V
            //   430: goto            241
            //   433: astore_1       
            //   434: goto            241
            //   437: astore_2       
            //   438: aload_0        
            //   439: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   442: iconst_1       
            //   443: invokestatic    android/support/v7/widget/ActivityChooserModel.access$502:(Landroid/support/v7/widget/ActivityChooserModel;Z)Z
            //   446: pop            
            //   447: aload_1        
            //   448: ifnull          455
            //   451: aload_1        
            //   452: invokevirtual   java/io/FileOutputStream.close:()V
            //   455: aload_2        
            //   456: athrow         
            //   457: astore_1       
            //   458: goto            241
            //   461: astore_1       
            //   462: goto            455
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  14     27     176    204    Ljava/io/FileNotFoundException;
            //  32     73     244    309    Ljava/lang/IllegalArgumentException;
            //  32     73     309    373    Ljava/lang/IllegalStateException;
            //  32     73     373    437    Ljava/io/IOException;
            //  32     73     437    457    Any
            //  83     170    244    309    Ljava/lang/IllegalArgumentException;
            //  83     170    309    373    Ljava/lang/IllegalStateException;
            //  83     170    373    437    Ljava/io/IOException;
            //  83     170    437    457    Any
            //  206    224    244    309    Ljava/lang/IllegalArgumentException;
            //  206    224    309    373    Ljava/lang/IllegalStateException;
            //  206    224    373    437    Ljava/io/IOException;
            //  206    224    437    457    Any
            //  237    241    457    461    Ljava/io/IOException;
            //  245    285    437    457    Any
            //  298    302    305    309    Ljava/io/IOException;
            //  311    349    437    457    Any
            //  362    366    369    373    Ljava/io/IOException;
            //  374    413    437    457    Any
            //  426    430    433    437    Ljava/io/IOException;
            //  451    455    461    465    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 223 out of bounds for length 223
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
}
