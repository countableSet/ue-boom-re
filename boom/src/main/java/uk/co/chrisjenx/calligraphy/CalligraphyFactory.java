// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.annotation.TargetApi;
import android.os.Build$VERSION;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

class CalligraphyFactory
{
    private static final String ACTION_BAR_SUBTITLE = "action_bar_subtitle";
    private static final String ACTION_BAR_TITLE = "action_bar_title";
    private final int mAttributeId;
    
    public CalligraphyFactory(final int mAttributeId) {
        this.mAttributeId = mAttributeId;
    }
    
    protected static int[] getStyleForTextView(final TextView textView) {
        final int[] array2;
        final int[] array = array2 = new int[2];
        array2[1] = (array2[0] = -1);
        if (isActionBarTitle(textView)) {
            array[0] = 16843470;
            array[1] = 16843512;
        }
        else if (isActionBarSubTitle(textView)) {
            array[0] = 16843470;
            array[1] = 16843513;
        }
        if (array[0] == -1) {
            int intValue;
            if (CalligraphyConfig.get().getClassStyles().containsKey(textView.getClass())) {
                intValue = CalligraphyConfig.get().getClassStyles().get(textView.getClass());
            }
            else {
                intValue = 16842804;
            }
            array[0] = intValue;
        }
        return array;
    }
    
    @SuppressLint({ "NewApi" })
    protected static boolean isActionBarSubTitle(final TextView textView) {
        return matchesResourceIdName((View)textView, "action_bar_subtitle") || (parentIsToolbarV7((View)textView) && TextUtils.equals(((Toolbar)textView.getParent()).getSubtitle(), textView.getText()));
    }
    
    @SuppressLint({ "NewApi" })
    protected static boolean isActionBarTitle(final TextView textView) {
        return matchesResourceIdName((View)textView, "action_bar_title") || (parentIsToolbarV7((View)textView) && TextUtils.equals(((Toolbar)textView.getParent()).getTitle(), textView.getText()));
    }
    
    protected static boolean matchesResourceIdName(final View view, final String anotherString) {
        return view.getId() != -1 && view.getResources().getResourceEntryName(view.getId()).equalsIgnoreCase(anotherString);
    }
    
    protected static boolean parentIsToolbarV7(final View view) {
        return CalligraphyUtils.canCheckForV7Toolbar() && view.getParent() != null && view.getParent() instanceof Toolbar;
    }
    
    public View onViewCreated(final View view, final Context context, final AttributeSet set) {
        if (view != null && view.getTag(R.id.calligraphy_tag_id) != Boolean.TRUE) {
            this.onViewCreatedInternal(view, context, set);
            view.setTag(R.id.calligraphy_tag_id, (Object)Boolean.TRUE);
        }
        return view;
    }
    
    void onViewCreatedInternal(final View view, final Context context, final AttributeSet set) {
        boolean b = false;
        Label_0157: {
            if (!(view instanceof TextView)) {
                break Label_0157;
            }
            if (!TypefaceUtils.isLoaded(((TextView)view).getTypeface())) {
                String s;
                if (TextUtils.isEmpty((CharSequence)(s = CalligraphyUtils.pullFontPathFromView(context, set, this.mAttributeId)))) {
                    s = CalligraphyUtils.pullFontPathFromStyle(context, set, this.mAttributeId);
                }
                String pullFontPathFromTextAppearance = s;
                if (TextUtils.isEmpty((CharSequence)s)) {
                    pullFontPathFromTextAppearance = CalligraphyUtils.pullFontPathFromTextAppearance(context, set, this.mAttributeId);
                }
                String s2 = pullFontPathFromTextAppearance;
                if (TextUtils.isEmpty((CharSequence)pullFontPathFromTextAppearance)) {
                    final int[] styleForTextView = getStyleForTextView((TextView)view);
                    if (styleForTextView[1] != -1) {
                        s2 = CalligraphyUtils.pullFontPathFromTheme(context, styleForTextView[0], styleForTextView[1], this.mAttributeId);
                    }
                    else {
                        s2 = CalligraphyUtils.pullFontPathFromTheme(context, styleForTextView[0], this.mAttributeId);
                    }
                }
                if (matchesResourceIdName(view, "action_bar_title") || matchesResourceIdName(view, "action_bar_subtitle")) {
                    b = true;
                }
                CalligraphyUtils.applyFontToTextView(context, (TextView)view, CalligraphyConfig.get(), s2, b);
                break Label_0157;
            }
            return;
        }
        if (CalligraphyUtils.canCheckForV7Toolbar() && view instanceof Toolbar) {
            final ViewGroup viewGroup = (ViewGroup)view;
            viewGroup.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                @TargetApi(16)
                public void onGlobalLayout() {
                    final int childCount = viewGroup.getChildCount();
                    if (childCount != 0) {
                        for (int i = 0; i < childCount; ++i) {
                            CalligraphyFactory.this.onViewCreated(viewGroup.getChildAt(i), context, null);
                        }
                    }
                    if (Build$VERSION.SDK_INT < 16) {
                        viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                    else {
                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                }
            });
        }
    }
}
