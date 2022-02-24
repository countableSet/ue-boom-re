// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.app;

import java.util.Iterator;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.content.Context;
import android.widget.RemoteViews;
import android.app.Notification$Builder;
import android.app.PendingIntent;
import android.app.Notification;
import android.os.Parcelable;
import android.os.Bundle;
import android.app.RemoteInput$Builder;
import android.app.RemoteInput;

class NotificationCompatApi21
{
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ON_READ = "on_read";
    private static final String KEY_ON_REPLY = "on_reply";
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_REMOTE_INPUT = "remote_input";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMESTAMP = "timestamp";
    
    private static RemoteInput fromCompatRemoteInput(final RemoteInputCompatBase.RemoteInput remoteInput) {
        return new RemoteInput$Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }
    
    static Bundle getBundleForUnreadConversation(final NotificationCompatBase.UnreadConversation unreadConversation) {
        Bundle bundle;
        if (unreadConversation == null) {
            bundle = null;
        }
        else {
            final Bundle bundle2 = new Bundle();
            String s = null;
            if (unreadConversation.getParticipants() != null) {
                s = s;
                if (unreadConversation.getParticipants().length > 1) {
                    s = unreadConversation.getParticipants()[0];
                }
            }
            final Parcelable[] array = new Parcelable[unreadConversation.getMessages().length];
            for (int i = 0; i < array.length; ++i) {
                final Bundle bundle3 = new Bundle();
                bundle3.putString("text", unreadConversation.getMessages()[i]);
                bundle3.putString("author", s);
                array[i] = (Parcelable)bundle3;
            }
            bundle2.putParcelableArray("messages", array);
            final RemoteInputCompatBase.RemoteInput remoteInput = unreadConversation.getRemoteInput();
            if (remoteInput != null) {
                bundle2.putParcelable("remote_input", (Parcelable)fromCompatRemoteInput(remoteInput));
            }
            bundle2.putParcelable("on_reply", (Parcelable)unreadConversation.getReplyPendingIntent());
            bundle2.putParcelable("on_read", (Parcelable)unreadConversation.getReadPendingIntent());
            bundle2.putStringArray("participants", unreadConversation.getParticipants());
            bundle2.putLong("timestamp", unreadConversation.getLatestTimestamp());
            bundle = bundle2;
        }
        return bundle;
    }
    
    public static String getCategory(final Notification notification) {
        return notification.category;
    }
    
    static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(final Bundle bundle, final NotificationCompatBase.UnreadConversation.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        final RemoteInputCompatBase.RemoteInput remoteInput = null;
        final NotificationCompatBase.UnreadConversation unreadConversation = null;
        Object build;
        if (bundle == null) {
            build = unreadConversation;
        }
        else {
            final Parcelable[] parcelableArray = bundle.getParcelableArray("messages");
            String[] array = null;
            Label_0081: {
                if (parcelableArray != null) {
                    array = new String[parcelableArray.length];
                    final int n = 1;
                    int n2 = 0;
                    while (true) {
                        Block_8: {
                            int n3;
                            while (true) {
                                n3 = n;
                                if (n2 >= array.length) {
                                    break;
                                }
                                if (!(parcelableArray[n2] instanceof Bundle)) {
                                    n3 = 0;
                                    break;
                                }
                                array[n2] = ((Bundle)parcelableArray[n2]).getString("text");
                                if (array[n2] == null) {
                                    break Block_8;
                                }
                                ++n2;
                            }
                            build = unreadConversation;
                            if (n3 != 0) {
                                break Label_0081;
                            }
                            return (NotificationCompatBase.UnreadConversation)build;
                        }
                        int n3 = 0;
                        continue;
                    }
                }
            }
            final PendingIntent pendingIntent = (PendingIntent)bundle.getParcelable("on_read");
            final PendingIntent pendingIntent2 = (PendingIntent)bundle.getParcelable("on_reply");
            final RemoteInput remoteInput2 = (RemoteInput)bundle.getParcelable("remote_input");
            final String[] stringArray = bundle.getStringArray("participants");
            build = unreadConversation;
            if (stringArray != null) {
                build = unreadConversation;
                if (stringArray.length == 1) {
                    RemoteInputCompatBase.RemoteInput compatRemoteInput = remoteInput;
                    if (remoteInput2 != null) {
                        compatRemoteInput = toCompatRemoteInput(remoteInput2, factory2);
                    }
                    build = factory.build(array, compatRemoteInput, pendingIntent2, pendingIntent, stringArray, bundle.getLong("timestamp"));
                }
            }
        }
        return (NotificationCompatBase.UnreadConversation)build;
    }
    
    private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(final RemoteInput remoteInput, final RemoteInputCompatBase.RemoteInput.Factory factory) {
        return factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras());
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;
        private RemoteViews mHeadsUpContentView;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final String category, final ArrayList<String> list, final Bundle bundle, final int color, final int visibility, final Notification publicVersion, final String group, final boolean groupSummary, final String sortKey, final RemoteViews mContentView, final RemoteViews mBigContentView, final RemoteViews mHeadsUpContentView) {
            this.b = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.b.addPerson((String)iterator.next());
            }
            this.mContentView = mContentView;
            this.mBigContentView = mBigContentView;
            this.mHeadsUpContentView = mHeadsUpContentView;
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi20.addAction(this.b, action);
        }
        
        @Override
        public Notification build() {
            this.b.setExtras(this.mExtras);
            final Notification build = this.b.build();
            if (this.mContentView != null) {
                build.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                build.bigContentView = this.mBigContentView;
            }
            if (this.mHeadsUpContentView != null) {
                build.headsUpContentView = this.mHeadsUpContentView;
            }
            return build;
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
