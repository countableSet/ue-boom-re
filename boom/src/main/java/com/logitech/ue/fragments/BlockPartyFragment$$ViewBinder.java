// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.View$OnClickListener;
import butterknife.internal.DebouncingOnClickListener;
import android.view.View;
import butterknife.ButterKnife;

public class BlockPartyFragment$$ViewBinder<T extends BlockPartyFragment> implements ViewBinder<T>
{
    public void bind(final Finder finder, final T t, final Object o) {
        (t.mStartPartyButton = finder.findRequiredView(o, 2131624188, "field 'mStartPartyButton' and method 'onStartClicked'")).setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onStartClicked(view);
            }
        });
        t.mPartyMainContainer = finder.findRequiredView(o, 2131624177, "field 'mPartyMainContainer'");
        t.mPartyIntroContainer = finder.findRequiredView(o, 2131624186, "field 'mPartyIntroContainer'");
        final View view = finder.findRequiredView(o, 2131624182, "field 'mEndPartyButton' and method 'onEndClicked'");
        t.mEndPartyButton = (Button)finder.castView(view, 2131624182, "field 'mEndPartyButton'");
        view.setOnClickListener((View$OnClickListener)new DebouncingOnClickListener() {
            @Override
            public void doClick(final View view) {
                t.onEndClicked(view);
            }
        });
        t.mHorizontalLine = finder.findRequiredView(o, 2131624189, "field 'mHorizontalLine'");
        t.mVerticalLine = finder.findRequiredView(o, 2131624190, "field 'mVerticalLine'");
        t.mVolumeControl = finder.castView(finder.findRequiredView(o, 2131624166, "field 'mVolumeControl'"), 2131624166, "field 'mVolumeControl'");
        (t.mVolumePlusButton = finder.findRequiredView(o, 2131624183, "field 'mVolumePlusButton' and method 'onVolumePlusButtonTouch'")).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return t.onVolumePlusButtonTouch(view, motionEvent);
            }
        });
        (t.mVolumeMinusButton = finder.findRequiredView(o, 2131624184, "field 'mVolumeMinusButton' and method 'onVolumeMinusButtonTouch'")).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return t.onVolumeMinusButtonTouch(view, motionEvent);
            }
        });
        t.mPartyQuoteLabel = finder.castView(finder.findRequiredView(o, 2131624187, "field 'mPartyQuoteLabel'"), 2131624187, "field 'mPartyQuoteLabel'");
        t.mPartyStartHintLabel = finder.castView(finder.findRequiredView(o, 2131624191, "field 'mPartyStartHintLabel'"), 2131624191, "field 'mPartyStartHintLabel'");
        t.mPartyJoinHintLabel = finder.castView(finder.findRequiredView(o, 2131624185, "field 'mPartyJoinHintLabel'"), 2131624185, "field 'mPartyJoinHintLabel'");
    }
    
    public void unbind(final T t) {
        t.mStartPartyButton = null;
        t.mPartyMainContainer = null;
        t.mPartyIntroContainer = null;
        t.mEndPartyButton = null;
        t.mHorizontalLine = null;
        t.mVerticalLine = null;
        t.mVolumeControl = null;
        t.mVolumePlusButton = null;
        t.mVolumeMinusButton = null;
        t.mPartyQuoteLabel = null;
        t.mPartyStartHintLabel = null;
        t.mPartyJoinHintLabel = null;
    }
}
