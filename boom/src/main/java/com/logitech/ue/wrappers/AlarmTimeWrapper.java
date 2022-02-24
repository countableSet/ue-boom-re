// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.wrappers;

import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.WheelViewAdapter;
import android.view.MotionEvent;
import android.util.TypedValue;
import android.view.View$OnTouchListener;
import android.view.View$OnClickListener;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import android.text.format.DateFormat;
import android.widget.ImageView;
import antistatic.spinnerwheel.WheelVerticalView;
import android.view.View;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;
import android.widget.TextView;

public class AlarmTimeWrapper
{
    public static final int HOURS_MODE_12 = 1;
    public static final int HOURS_MODE_24 = 2;
    private TextView mAMPMLabel;
    int mHourMode;
    private int mHours;
    private NumericWheelAdapter mHoursAdapter;
    private View mHoursButton;
    private TextView mHoursLabel;
    private WheelVerticalView mHoursSpinner;
    boolean mIsAM;
    private boolean mIsOn;
    private boolean mIsRepeatOn;
    private OnAlarmWrapperListener mListener;
    private int mMinutes;
    private NumericWheelAdapter mMinutesAdapter;
    private View mMinutesButton;
    private TextView mMinutesLabel;
    private WheelVerticalView mMinutesSpinner;
    private View mOnButton;
    private ImageView mOnIcon;
    private TextView mOnLabel;
    private View mRepeatButton;
    private ImageView mRepeatIcon;
    private TextView mRepeatLabel;
    private View mRoot;
    private State mState;
    
    public AlarmTimeWrapper(final View mRoot) {
        this.mIsAM = true;
        this.mHourMode = 1;
        this.mRoot = mRoot;
        this.mAMPMLabel = (TextView)this.mRoot.findViewById(2131624037);
        this.mOnButton = this.mRoot.findViewById(2131624094);
        this.mOnIcon = (ImageView)this.mOnButton.findViewWithTag((Object)"icon");
        this.mOnLabel = (TextView)this.mOnButton.findViewWithTag((Object)"text");
        this.mRepeatButton = this.mRoot.findViewById(2131624095);
        this.mRepeatIcon = (ImageView)this.mRepeatButton.findViewWithTag((Object)"icon");
        this.mRepeatLabel = (TextView)this.mRepeatButton.findViewWithTag((Object)"text");
        this.mHoursSpinner = (WheelVerticalView)this.mRoot.findViewById(2131624089);
        this.mMinutesSpinner = (WheelVerticalView)this.mRoot.findViewById(2131624091);
        this.mHoursButton = this.mRoot.findViewById(2131624090);
        this.mHoursLabel = (TextView)this.mRoot.findViewById(2131624034);
        this.mMinutesButton = this.mRoot.findViewById(2131624092);
        this.mMinutesLabel = (TextView)this.mRoot.findViewById(2131624036);
        if (DateFormat.is24HourFormat(this.mRoot.getContext())) {
            this.mHourMode = 2;
        }
        if (this.mHourMode == 2) {
            this.mHoursAdapter = new NumericWheelAdapter(this.mRoot.getContext(), 0, 23, "%02d");
            this.mAMPMLabel.setVisibility(4);
        }
        else {
            this.mHoursAdapter = new NumericWheelAdapter(this.mRoot.getContext(), 1, 12, "%02d");
            this.setIsAM(this.mHours < 11);
            this.mHoursSpinner.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(final AbstractWheel abstractWheel, int n, int n2) {
                    ++n;
                    ++n2;
                    if ((n == 11 && n2 == 12) || (n == 12 && n2 == 11)) {
                        AlarmTimeWrapper.this.setIsAM(!AlarmTimeWrapper.this.mIsAM);
                    }
                }
            });
            this.mAMPMLabel.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    AlarmTimeWrapper.this.mIsAM = !AlarmTimeWrapper.this.mIsAM;
                    AlarmTimeWrapper.this.setState(State.NORMAL);
                    final int[] calculateTime = AlarmTimeWrapper.this.CalculateTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                    AlarmTimeWrapper.this.setTime(calculateTime[0], calculateTime[1]);
                    if (AlarmTimeWrapper.this.mListener != null) {
                        AlarmTimeWrapper.this.mListener.onTimeValueChanged(calculateTime[0], calculateTime[1]);
                    }
                }
            });
        }
        this.mHoursButton.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            float X = 0.0f;
            float Y = 0.0f;
            boolean itIsClick = true;
            float sqrRadius = TypedValue.applyDimension(1, 4.0f, mRoot.getResources().getDisplayMetrics());
            
            {
                this.sqrRadius = 0.0f;
                this.sqrRadius *= this.sqrRadius;
            }
            
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (AlarmTimeWrapper.this.mState == State.HOURS_SELECTED) {
                    AlarmTimeWrapper.this.mHoursSpinner.onTouchEvent(motionEvent);
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        this.itIsClick = true;
                        this.X = motionEvent.getX();
                        this.Y = motionEvent.getY();
                        break;
                    }
                    case 2: {
                        final float n = motionEvent.getX() - this.X;
                        final float n2 = motionEvent.getY() - this.Y;
                        if (n * n + n2 * n2 > this.sqrRadius) {
                            this.itIsClick = false;
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (AlarmTimeWrapper.this.mState != State.HOURS_SELECTED) {
                            if (AlarmTimeWrapper.this.mState == State.MINUTES_SELECTED && AlarmTimeWrapper.this.mListener != null) {
                                final int[] calculateTime = AlarmTimeWrapper.this.CalculateTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                                AlarmTimeWrapper.this.mListener.onTimeValueChanged(calculateTime[0], calculateTime[1]);
                            }
                            AlarmTimeWrapper.this.setState(State.HOURS_SELECTED);
                            break;
                        }
                        if (this.itIsClick) {
                            AlarmTimeWrapper.this.setState(State.NORMAL);
                            if (AlarmTimeWrapper.this.mListener != null) {
                                final int[] calculateTime2 = AlarmTimeWrapper.this.CalculateTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                                AlarmTimeWrapper.this.mListener.onTimeValueChanged(calculateTime2[0], calculateTime2[1]);
                            }
                            AlarmTimeWrapper.this.setTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                            break;
                        }
                        break;
                    }
                }
                return true;
            }
        });
        this.mMinutesButton.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            float X = 0.0f;
            float Y = 0.0f;
            boolean itIsClick = true;
            float sqrRadius = TypedValue.applyDimension(1, 4.0f, mRoot.getResources().getDisplayMetrics());
            
            {
                this.sqrRadius = 0.0f;
                this.sqrRadius *= this.sqrRadius;
            }
            
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (AlarmTimeWrapper.this.mState == State.MINUTES_SELECTED) {
                    AlarmTimeWrapper.this.mMinutesSpinner.onTouchEvent(motionEvent);
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        this.itIsClick = true;
                        this.X = motionEvent.getX();
                        this.Y = motionEvent.getY();
                        break;
                    }
                    case 2: {
                        final float n = motionEvent.getX() - this.X;
                        final float n2 = motionEvent.getY() - this.Y;
                        if (n * n + n2 * n2 > this.sqrRadius) {
                            this.itIsClick = false;
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (AlarmTimeWrapper.this.mState != State.MINUTES_SELECTED) {
                            if (AlarmTimeWrapper.this.mState == State.HOURS_SELECTED && AlarmTimeWrapper.this.mListener != null) {
                                final int[] calculateTime = AlarmTimeWrapper.this.CalculateTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                                AlarmTimeWrapper.this.mListener.onTimeValueChanged(calculateTime[0], calculateTime[1]);
                            }
                            AlarmTimeWrapper.this.setState(State.MINUTES_SELECTED);
                            break;
                        }
                        if (this.itIsClick) {
                            AlarmTimeWrapper.this.setState(State.NORMAL);
                            if (AlarmTimeWrapper.this.mListener != null) {
                                final int[] calculateTime2 = AlarmTimeWrapper.this.CalculateTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                                AlarmTimeWrapper.this.mListener.onTimeValueChanged(calculateTime2[0], calculateTime2[1]);
                            }
                            AlarmTimeWrapper.this.setTime(AlarmTimeWrapper.this.mHoursSpinner.getCurrentItem(), AlarmTimeWrapper.this.mMinutesSpinner.getCurrentItem());
                            break;
                        }
                        break;
                    }
                }
                return true;
            }
        });
        this.mHoursAdapter.setTextSize(this.mRoot.getResources().getDimensionPixelSize(2131361861) / 6);
        this.mHoursAdapter.setTextGravity(5);
        this.mHoursAdapter.setTextColor(-1);
        this.mHoursSpinner.setBackgroundColor(-16777216);
        this.mHoursSpinner.setCyclic(true);
        this.mHoursSpinner.setViewAdapter(this.mHoursAdapter);
        (this.mMinutesAdapter = new NumericWheelAdapter(this.mRoot.getContext(), 0, 59, "%02d")).setTextSize(this.mRoot.getResources().getDimensionPixelSize(2131361861) / 6);
        this.mMinutesAdapter.setTextGravity(3);
        this.mMinutesAdapter.setTextColor(-1);
        this.mMinutesSpinner.setBackgroundColor(-16777216);
        this.mMinutesSpinner.setCyclic(true);
        this.mMinutesSpinner.setViewAdapter(this.mMinutesAdapter);
        this.mHoursSpinner.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingFinished(final AbstractWheel abstractWheel) {
            }
            
            @Override
            public void onScrollingStarted(final AbstractWheel abstractWheel) {
            }
        });
        this.mMinutesSpinner.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingFinished(final AbstractWheel abstractWheel) {
            }
            
            @Override
            public void onScrollingStarted(final AbstractWheel abstractWheel) {
            }
        });
        this.mOnButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AlarmTimeWrapper.this.setIsOn(!AlarmTimeWrapper.this.mIsOn);
                if (AlarmTimeWrapper.this.mListener != null) {
                    AlarmTimeWrapper.this.mListener.onOnValueChanged(AlarmTimeWrapper.this.mIsOn);
                }
            }
        });
        this.mRepeatButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AlarmTimeWrapper.this.setIsRepeatOn(!AlarmTimeWrapper.this.mIsRepeatOn);
                if (AlarmTimeWrapper.this.mListener != null) {
                    AlarmTimeWrapper.this.mListener.onRepeatValueChanged(AlarmTimeWrapper.this.mIsRepeatOn);
                }
            }
        });
    }
    
    private void setIsAM(final boolean mIsAM) {
        this.mIsAM = mIsAM;
        if (this.mIsAM) {
            this.mAMPMLabel.setText((CharSequence)"AM");
        }
        else {
            this.mAMPMLabel.setText((CharSequence)"PM");
        }
    }
    
    public int[] CalculateTime(final int n, final int n2) {
        int n3 = n;
        if (this.mHourMode == 1) {
            if (this.mIsAM) {
                if (n == 11) {
                    n3 = 0;
                }
                else {
                    n3 = n + 1;
                }
            }
            else if (n == 11) {
                n3 = 12;
            }
            else {
                n3 = n + 13;
            }
        }
        return new int[] { n3, n2 };
    }
    
    public OnAlarmWrapperListener getListener() {
        return this.mListener;
    }
    
    public void setIsOn(final boolean mIsOn) {
        this.mIsOn = mIsOn;
        final ImageView mOnIcon = this.mOnIcon;
        int imageResource;
        if (mIsOn) {
            imageResource = 2130837611;
        }
        else {
            imageResource = 2130837609;
        }
        mOnIcon.setImageResource(imageResource);
        final TextView mOnLabel = this.mOnLabel;
        int text;
        if (mIsOn) {
            text = 2131165219;
        }
        else {
            text = 2131165218;
        }
        mOnLabel.setText(text);
    }
    
    public void setIsRepeatOn(final boolean mIsRepeatOn) {
        this.mIsRepeatOn = mIsRepeatOn;
        final ImageView mRepeatIcon = this.mRepeatIcon;
        int imageResource;
        if (mIsRepeatOn) {
            imageResource = 2130837627;
        }
        else {
            imageResource = 2130837628;
        }
        mRepeatIcon.setImageResource(imageResource);
        final TextView mRepeatLabel = this.mRepeatLabel;
        int text;
        if (mIsRepeatOn) {
            text = 2131165244;
        }
        else {
            text = 2131165243;
        }
        mRepeatLabel.setText(text);
    }
    
    public void setListener(final OnAlarmWrapperListener mListener) {
        this.mListener = mListener;
    }
    
    public void setState(final State mState) {
        switch (mState) {
            case NORMAL: {
                this.mHoursSpinner.stopScrolling();
                this.mHoursSpinner.setVisibility(4);
                this.mHoursLabel.setVisibility(0);
                this.mMinutesSpinner.stopScrolling();
                this.mMinutesSpinner.setVisibility(4);
                this.mMinutesLabel.setVisibility(0);
                break;
            }
            case HOURS_SELECTED: {
                this.mHoursSpinner.setVisibility(0);
                this.mHoursLabel.setVisibility(4);
                this.mMinutesSpinner.stopScrolling();
                this.mMinutesSpinner.setVisibility(4);
                this.mMinutesLabel.setVisibility(0);
                break;
            }
            case MINUTES_SELECTED: {
                this.mMinutesSpinner.setVisibility(0);
                this.mMinutesLabel.setVisibility(4);
                this.mHoursSpinner.stopScrolling();
                this.mHoursSpinner.setVisibility(4);
                this.mHoursLabel.setVisibility(0);
                break;
            }
        }
        this.mState = mState;
    }
    
    public void setTime(int n, final int i) {
        int n2 = 11;
        final int n3 = 12;
        this.mHours = n;
        this.mMinutes = i;
        if (this.mHourMode == 1) {
            if (n < 12) {
                this.setIsAM(true);
                final WheelVerticalView mHoursSpinner = this.mHoursSpinner;
                if (n != 0) {
                    n2 = n - 1;
                }
                mHoursSpinner.setCurrentItem(n2);
                final TextView mHoursLabel = this.mHoursLabel;
                int j;
                if ((j = n) == 0) {
                    j = 12;
                }
                mHoursLabel.setText((CharSequence)String.format("%02d", j));
            }
            else {
                this.setIsAM(false);
                final WheelVerticalView mHoursSpinner2 = this.mHoursSpinner;
                if (n != 12) {
                    n2 = n - 13;
                }
                mHoursSpinner2.setCurrentItem(n2);
                final TextView mHoursLabel2 = this.mHoursLabel;
                if (n == 12) {
                    n = n3;
                }
                else {
                    n -= 12;
                }
                mHoursLabel2.setText((CharSequence)String.format("%02d", n));
            }
        }
        else {
            this.mHoursSpinner.setCurrentItem(n);
            this.mHoursLabel.setText((CharSequence)String.format("%02d", n));
        }
        this.mMinutesSpinner.setCurrentItem(i);
        this.mMinutesLabel.setText((CharSequence)String.format("%02d", i));
    }
    
    public interface OnAlarmWrapperListener
    {
        void onOnValueChanged(final boolean p0);
        
        void onRepeatValueChanged(final boolean p0);
        
        void onTimeValueChanged(final int p0, final int p1);
    }
    
    public enum State
    {
        HOURS_SELECTED, 
        MINUTES_SELECTED, 
        NORMAL;
    }
}
