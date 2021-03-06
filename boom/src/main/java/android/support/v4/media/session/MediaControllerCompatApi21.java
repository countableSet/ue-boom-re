// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v4.media.session;

import android.media.Rating;
import android.media.session.MediaController$TransportControls;
import android.media.AudioAttributes;
import android.media.session.MediaSession$QueueItem;
import android.media.session.PlaybackState;
import android.media.MediaMetadata;
import android.media.session.MediaController$PlaybackInfo;
import android.os.ResultReceiver;
import android.media.session.MediaController$Callback;
import android.os.Handler;
import android.app.PendingIntent;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.media.session.MediaSession$Token;
import android.content.Context;
import android.view.KeyEvent;
import android.media.session.MediaController;

class MediaControllerCompatApi21
{
    public static void adjustVolume(final Object o, final int n, final int n2) {
        ((MediaController)o).adjustVolume(n, n2);
    }
    
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public static boolean dispatchMediaButtonEvent(final Object o, final KeyEvent keyEvent) {
        return ((MediaController)o).dispatchMediaButtonEvent(keyEvent);
    }
    
    public static Object fromToken(final Context context, final Object o) {
        return new MediaController(context, (MediaSession$Token)o);
    }
    
    public static Bundle getExtras(final Object o) {
        return ((MediaController)o).getExtras();
    }
    
    public static long getFlags(final Object o) {
        return ((MediaController)o).getFlags();
    }
    
    public static Object getMetadata(final Object o) {
        return ((MediaController)o).getMetadata();
    }
    
    public static String getPackageName(final Object o) {
        return ((MediaController)o).getPackageName();
    }
    
    public static Object getPlaybackInfo(final Object o) {
        return ((MediaController)o).getPlaybackInfo();
    }
    
    public static Object getPlaybackState(final Object o) {
        return ((MediaController)o).getPlaybackState();
    }
    
    public static List<Object> getQueue(final Object o) {
        final List queue = ((MediaController)o).getQueue();
        ArrayList list;
        if (queue == null) {
            list = null;
        }
        else {
            list = new ArrayList<Object>(queue);
        }
        return (List<Object>)list;
    }
    
    public static CharSequence getQueueTitle(final Object o) {
        return ((MediaController)o).getQueueTitle();
    }
    
    public static int getRatingType(final Object o) {
        return ((MediaController)o).getRatingType();
    }
    
    public static PendingIntent getSessionActivity(final Object o) {
        return ((MediaController)o).getSessionActivity();
    }
    
    public static Object getTransportControls(final Object o) {
        return ((MediaController)o).getTransportControls();
    }
    
    public static void registerCallback(final Object o, final Object o2, final Handler handler) {
        ((MediaController)o).registerCallback((MediaController$Callback)o2, handler);
    }
    
    public static void sendCommand(final Object o, final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
        ((MediaController)o).sendCommand(s, bundle, resultReceiver);
    }
    
    public static void setVolumeTo(final Object o, final int n, final int n2) {
        ((MediaController)o).setVolumeTo(n, n2);
    }
    
    public static void unregisterCallback(final Object o, final Object o2) {
        ((MediaController)o).unregisterCallback((MediaController$Callback)o2);
    }
    
    public interface Callback
    {
        void onAudioInfoChanged(final int p0, final int p1, final int p2, final int p3, final int p4);
        
        void onExtrasChanged(final Bundle p0);
        
        void onMetadataChanged(final Object p0);
        
        void onPlaybackStateChanged(final Object p0);
        
        void onQueueChanged(final List<?> p0);
        
        void onQueueTitleChanged(final CharSequence p0);
        
        void onSessionDestroyed();
        
        void onSessionEvent(final String p0, final Bundle p1);
    }
    
    static class CallbackProxy<T extends Callback> extends MediaController$Callback
    {
        protected final T mCallback;
        
        public CallbackProxy(final T mCallback) {
            this.mCallback = mCallback;
        }
        
        public void onAudioInfoChanged(final MediaController$PlaybackInfo mediaController$PlaybackInfo) {
            ((Callback)this.mCallback).onAudioInfoChanged(mediaController$PlaybackInfo.getPlaybackType(), PlaybackInfo.getLegacyAudioStream(mediaController$PlaybackInfo), mediaController$PlaybackInfo.getVolumeControl(), mediaController$PlaybackInfo.getMaxVolume(), mediaController$PlaybackInfo.getCurrentVolume());
        }
        
        public void onExtrasChanged(final Bundle bundle) {
            ((Callback)this.mCallback).onExtrasChanged(bundle);
        }
        
        public void onMetadataChanged(final MediaMetadata mediaMetadata) {
            ((Callback)this.mCallback).onMetadataChanged(mediaMetadata);
        }
        
        public void onPlaybackStateChanged(final PlaybackState playbackState) {
            ((Callback)this.mCallback).onPlaybackStateChanged(playbackState);
        }
        
        public void onQueueChanged(final List<MediaSession$QueueItem> list) {
            ((Callback)this.mCallback).onQueueChanged(list);
        }
        
        public void onQueueTitleChanged(final CharSequence charSequence) {
            ((Callback)this.mCallback).onQueueTitleChanged(charSequence);
        }
        
        public void onSessionDestroyed() {
            ((Callback)this.mCallback).onSessionDestroyed();
        }
        
        public void onSessionEvent(final String s, final Bundle bundle) {
            ((Callback)this.mCallback).onSessionEvent(s, bundle);
        }
    }
    
    public static class PlaybackInfo
    {
        private static final int FLAG_SCO = 4;
        private static final int STREAM_BLUETOOTH_SCO = 6;
        private static final int STREAM_SYSTEM_ENFORCED = 7;
        
        public static AudioAttributes getAudioAttributes(final Object o) {
            return ((MediaController$PlaybackInfo)o).getAudioAttributes();
        }
        
        public static int getCurrentVolume(final Object o) {
            return ((MediaController$PlaybackInfo)o).getCurrentVolume();
        }
        
        public static int getLegacyAudioStream(final Object o) {
            return toLegacyStreamType(getAudioAttributes(o));
        }
        
        public static int getMaxVolume(final Object o) {
            return ((MediaController$PlaybackInfo)o).getMaxVolume();
        }
        
        public static int getPlaybackType(final Object o) {
            return ((MediaController$PlaybackInfo)o).getPlaybackType();
        }
        
        public static int getVolumeControl(final Object o) {
            return ((MediaController$PlaybackInfo)o).getVolumeControl();
        }
        
        private static int toLegacyStreamType(final AudioAttributes audioAttributes) {
            final int n = 3;
            int n2;
            if ((audioAttributes.getFlags() & 0x1) == 0x1) {
                n2 = 7;
            }
            else if ((audioAttributes.getFlags() & 0x4) == 0x4) {
                n2 = 6;
            }
            else {
                n2 = n;
                switch (audioAttributes.getUsage()) {
                    case 5:
                    case 7:
                    case 8:
                    case 9:
                    case 10: {
                        n2 = 5;
                        break;
                    }
                    case 6: {
                        n2 = 2;
                        break;
                    }
                    case 4: {
                        n2 = 4;
                        break;
                    }
                    case 3: {
                        n2 = 8;
                        break;
                    }
                    case 13: {
                        n2 = 1;
                        break;
                    }
                    case 2: {
                        n2 = 0;
                    }
                    case 1:
                    case 11:
                    case 12:
                    case 14: {
                        break;
                    }
                    default: {
                        n2 = n;
                        break;
                    }
                }
            }
            return n2;
        }
    }
    
    public static class TransportControls
    {
        public static void fastForward(final Object o) {
            ((MediaController$TransportControls)o).fastForward();
        }
        
        public static void pause(final Object o) {
            ((MediaController$TransportControls)o).pause();
        }
        
        public static void play(final Object o) {
            ((MediaController$TransportControls)o).play();
        }
        
        public static void playFromMediaId(final Object o, final String s, final Bundle bundle) {
            ((MediaController$TransportControls)o).playFromMediaId(s, bundle);
        }
        
        public static void playFromSearch(final Object o, final String s, final Bundle bundle) {
            ((MediaController$TransportControls)o).playFromSearch(s, bundle);
        }
        
        public static void rewind(final Object o) {
            ((MediaController$TransportControls)o).rewind();
        }
        
        public static void seekTo(final Object o, final long n) {
            ((MediaController$TransportControls)o).seekTo(n);
        }
        
        public static void sendCustomAction(final Object o, final String s, final Bundle bundle) {
            ((MediaController$TransportControls)o).sendCustomAction(s, bundle);
        }
        
        public static void setRating(final Object o, final Object o2) {
            ((MediaController$TransportControls)o).setRating((Rating)o2);
        }
        
        public static void skipToNext(final Object o) {
            ((MediaController$TransportControls)o).skipToNext();
        }
        
        public static void skipToPrevious(final Object o) {
            ((MediaController$TransportControls)o).skipToPrevious();
        }
        
        public static void skipToQueueItem(final Object o, final long n) {
            ((MediaController$TransportControls)o).skipToQueueItem(n);
        }
        
        public static void stop(final Object o) {
            ((MediaController$TransportControls)o).stop();
        }
    }
}
