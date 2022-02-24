// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.content;

import java.util.Set;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;
import android.os.Message;
import android.os.Looper;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager
{
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;
    
    static {
        mLock = new Object();
    }
    
    private LocalBroadcastManager(final Context mAppContext) {
        this.mReceivers = new HashMap<BroadcastReceiver, ArrayList<IntentFilter>>();
        this.mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
        this.mPendingBroadcasts = new ArrayList<BroadcastRecord>();
        this.mAppContext = mAppContext;
        this.mHandler = new Handler(mAppContext.getMainLooper()) {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        super.handleMessage(message);
                        break;
                    }
                    case 1: {
                        LocalBroadcastManager.this.executePendingBroadcasts();
                        break;
                    }
                }
            }
        };
    }
    
    private void executePendingBroadcasts() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        android/support/v4/content/LocalBroadcastManager.mReceivers:Ljava/util/HashMap;
        //     4: astore_1       
        //     5: aload_1        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        android/support/v4/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    11: invokevirtual   java/util/ArrayList.size:()I
        //    14: istore_2       
        //    15: iload_2        
        //    16: ifgt            22
        //    19: aload_1        
        //    20: monitorexit    
        //    21: return         
        //    22: iload_2        
        //    23: anewarray       Landroid/support/v4/content/LocalBroadcastManager$BroadcastRecord;
        //    26: astore_3       
        //    27: aload_0        
        //    28: getfield        android/support/v4/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    31: aload_3        
        //    32: invokevirtual   java/util/ArrayList.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    35: pop            
        //    36: aload_0        
        //    37: getfield        android/support/v4/content/LocalBroadcastManager.mPendingBroadcasts:Ljava/util/ArrayList;
        //    40: invokevirtual   java/util/ArrayList.clear:()V
        //    43: aload_1        
        //    44: monitorexit    
        //    45: iconst_0       
        //    46: istore_2       
        //    47: iload_2        
        //    48: aload_3        
        //    49: arraylength    
        //    50: if_icmpge       0
        //    53: aload_3        
        //    54: iload_2        
        //    55: aaload         
        //    56: astore_1       
        //    57: iconst_0       
        //    58: istore          4
        //    60: iload           4
        //    62: aload_1        
        //    63: getfield        android/support/v4/content/LocalBroadcastManager$BroadcastRecord.receivers:Ljava/util/ArrayList;
        //    66: invokevirtual   java/util/ArrayList.size:()I
        //    69: if_icmpge       109
        //    72: aload_1        
        //    73: getfield        android/support/v4/content/LocalBroadcastManager$BroadcastRecord.receivers:Ljava/util/ArrayList;
        //    76: iload           4
        //    78: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    81: checkcast       Landroid/support/v4/content/LocalBroadcastManager$ReceiverRecord;
        //    84: getfield        android/support/v4/content/LocalBroadcastManager$ReceiverRecord.receiver:Landroid/content/BroadcastReceiver;
        //    87: aload_0        
        //    88: getfield        android/support/v4/content/LocalBroadcastManager.mAppContext:Landroid/content/Context;
        //    91: aload_1        
        //    92: getfield        android/support/v4/content/LocalBroadcastManager$BroadcastRecord.intent:Landroid/content/Intent;
        //    95: invokevirtual   android/content/BroadcastReceiver.onReceive:(Landroid/content/Context;Landroid/content/Intent;)V
        //    98: iinc            4, 1
        //   101: goto            60
        //   104: astore_3       
        //   105: aload_1        
        //   106: monitorexit    
        //   107: aload_3        
        //   108: athrow         
        //   109: iinc            2, 1
        //   112: goto            47
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      15     104    109    Any
        //  19     21     104    109    Any
        //  22     45     104    109    Any
        //  105    107    104    109    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public static LocalBroadcastManager getInstance(final Context context) {
        synchronized (LocalBroadcastManager.mLock) {
            if (LocalBroadcastManager.mInstance == null) {
                LocalBroadcastManager.mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            return LocalBroadcastManager.mInstance;
        }
    }
    
    public void registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter e) {
        synchronized (this.mReceivers) {
            final ReceiverRecord e2 = new ReceiverRecord(e, broadcastReceiver);
            ArrayList<IntentFilter> value;
            if ((value = this.mReceivers.get(broadcastReceiver)) == null) {
                value = new ArrayList<IntentFilter>(1);
                this.mReceivers.put(broadcastReceiver, value);
            }
            value.add(e);
            for (int i = 0; i < e.countActions(); ++i) {
                final String action = e.getAction(i);
                ArrayList<ReceiverRecord> value2;
                if ((value2 = this.mActions.get(action)) == null) {
                    value2 = new ArrayList<ReceiverRecord>(1);
                    this.mActions.put(action, value2);
                }
                value2.add(e2);
            }
        }
    }
    
    public boolean sendBroadcast(final Intent obj) {
        while (true) {
            ArrayList<ReceiverRecord> list = null;
        Label_0498:
            while (true) {
                boolean b = false;
                int match = 0;
                Label_0386: {
                    synchronized (this.mReceivers) {
                        final String action = obj.getAction();
                        final String resolveTypeIfNeeded = obj.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
                        final Uri data = obj.getData();
                        final String scheme = obj.getScheme();
                        final Set categories = obj.getCategories();
                        if ((obj.getFlags() & 0x8) != 0x0) {
                            b = true;
                        }
                        else {
                            b = false;
                        }
                        if (b) {
                            Log.v("LocalBroadcastManager", "Resolving type " + resolveTypeIfNeeded + " scheme " + scheme + " of intent " + obj);
                        }
                        final ArrayList<ReceiverRecord> obj2 = this.mActions.get(obj.getAction());
                        if (obj2 != null) {
                            if (b) {
                                Log.v("LocalBroadcastManager", "Action list: " + obj2);
                            }
                            list = null;
                            ArrayList<ReceiverRecord> list2;
                            for (int i = 0; i < obj2.size(); ++i, list = list2) {
                                final ReceiverRecord e = obj2.get(i);
                                if (b) {
                                    Log.v("LocalBroadcastManager", "Matching against filter " + e.filter);
                                }
                                if (e.broadcasting) {
                                    list2 = list;
                                    if (b) {
                                        Log.v("LocalBroadcastManager", "  Filter's target already added");
                                        list2 = list;
                                    }
                                }
                                else {
                                    match = e.filter.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                                    if (match < 0) {
                                        break Label_0386;
                                    }
                                    if (b) {
                                        Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(match));
                                    }
                                    if ((list2 = list) == null) {
                                        list2 = new ArrayList<ReceiverRecord>();
                                    }
                                    list2.add(e);
                                    e.broadcasting = true;
                                }
                            }
                            break Label_0498;
                        }
                        break;
                    }
                }
                ArrayList<ReceiverRecord> list2 = list;
                if (b) {
                    String str = null;
                    switch (match) {
                        default: {
                            str = "unknown reason";
                            break;
                        }
                        case -3: {
                            str = "action";
                            break;
                        }
                        case -4: {
                            str = "category";
                            break;
                        }
                        case -2: {
                            str = "data";
                            break;
                        }
                        case -1: {
                            str = "type";
                            break;
                        }
                    }
                    Log.v("LocalBroadcastManager", "  Filter did not match: " + str);
                    list2 = list;
                    continue;
                }
                continue;
            }
            if (list != null) {
                for (int j = 0; j < list.size(); ++j) {
                    list.get(j).broadcasting = false;
                }
                final Intent intent;
                this.mPendingBroadcasts.add(new BroadcastRecord(intent, list));
                if (!this.mHandler.hasMessages(1)) {
                    this.mHandler.sendEmptyMessage(1);
                }
                // monitorexit(hashMap)
                return true;
            }
            break;
        }
        // monitorexit(hashMap)
        return false;
    }
    
    public void sendBroadcastSync(final Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }
    
    public void unregisterReceiver(final BroadcastReceiver key) {
        synchronized (this.mReceivers) {
            final ArrayList<IntentFilter> list = this.mReceivers.remove(key);
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    final IntentFilter intentFilter = list.get(i);
                    for (int j = 0; j < intentFilter.countActions(); ++j) {
                        final String action = intentFilter.getAction(j);
                        final ArrayList<ReceiverRecord> list2 = this.mActions.get(action);
                        if (list2 != null) {
                            int n;
                            for (int k = 0; k < list2.size(); k = n + 1) {
                                n = k;
                                if (list2.get(k).receiver == key) {
                                    list2.remove(k);
                                    n = k - 1;
                                }
                            }
                            if (list2.size() <= 0) {
                                this.mActions.remove(action);
                            }
                        }
                    }
                }
            }
            // monitorexit(this.mReceivers)
        }
    }
    
    private static class BroadcastRecord
    {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;
        
        BroadcastRecord(final Intent intent, final ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }
    
    private static class ReceiverRecord
    {
        boolean broadcasting;
        final IntentFilter filter;
        final BroadcastReceiver receiver;
        
        ReceiverRecord(final IntentFilter filter, final BroadcastReceiver receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            sb.append("}");
            return sb.toString();
        }
    }
}
