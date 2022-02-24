// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import com.logitech.ue.views.FadeButton;
import android.widget.RelativeLayout;
import android.view.View;
import butterknife.ButterKnife;

public class BlockPartyFragment$PartyMemberCellHolder$$ViewBinder<T extends BlockPartyFragment.PartyMemberCellHolder> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        t.mHostIndicator = finder.findRequiredView(o, 2131624226, "field 'mHostIndicator'");
        t.mArtworkContainer = finder.castView(finder.findRequiredView(o, 2131624225, "field 'mArtworkContainer'"), 2131624225, "field 'mArtworkContainer'");
        t.mPlayButton = finder.castView(finder.findRequiredView(o, 2131624227, "field 'mPlayButton'"), 2131624227, "field 'mPlayButton'");
        t.mPlayIconImage = finder.castView(finder.findRequiredView(o, 2131624228, "field 'mPlayIconImage'"), 2131624228, "field 'mPlayIconImage'");
        t.mMemberInfoContainer = finder.castView(finder.findRequiredView(o, 2131624229, "field 'mMemberInfoContainer'"), 2131624229, "field 'mMemberInfoContainer'");
        t.mMemberNameLabel = finder.castView(finder.findRequiredView(o, 2131624230, "field 'mMemberNameLabel'"), 2131624230, "field 'mMemberNameLabel'");
        t.mPlaybackPanel = finder.findRequiredView(o, 2131624232, "field 'mPlaybackPanel'");
        t.mPlaybackProgressBar = finder.castView(finder.findRequiredView(o, 2131624235, "field 'mPlaybackProgressBar'"), 2131624235, "field 'mPlaybackProgressBar'");
        t.mTrackLength = finder.castView(finder.findRequiredView(o, 2131624234, "field 'mTrackLength'"), 2131624234, "field 'mTrackLength'");
        t.mTrackElapsed = finder.castView(finder.findRequiredView(o, 2131624233, "field 'mTrackElapsed'"), 2131624233, "field 'mTrackElapsed'");
        t.mTrackTitleLabel = finder.castView(finder.findRequiredView(o, 2131624238, "field 'mTrackTitleLabel'"), 2131624238, "field 'mTrackTitleLabel'");
        t.mTrackArtistAlbumLabel = finder.castView(finder.findRequiredView(o, 2131624236, "field 'mTrackArtistAlbumLabel'"), 2131624236, "field 'mTrackArtistAlbumLabel'");
        t.mKickMemberButton = finder.findRequiredView(o, 2131624231, "field 'mKickMemberButton'");
        t.mSkipButton = finder.findRequiredView(o, 2131624237, "field 'mSkipButton'");
    }
    
    public void unbind(final T t) {
        t.mHostIndicator = null;
        t.mArtworkContainer = null;
        t.mPlayButton = null;
        t.mPlayIconImage = null;
        t.mMemberInfoContainer = null;
        t.mMemberNameLabel = null;
        t.mPlaybackPanel = null;
        t.mPlaybackProgressBar = null;
        t.mTrackLength = null;
        t.mTrackElapsed = null;
        t.mTrackTitleLabel = null;
        t.mTrackArtistAlbumLabel = null;
        t.mKickMemberButton = null;
        t.mSkipButton = null;
    }
}
