// 
// Decompiled by Procyon v0.5.36
// 

package android.support.v7.widget;

import android.view.KeyEvent$DispatcherState;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.view.View$BaseSavedState;
import android.os.ResultReceiver;
import java.lang.reflect.Method;
import android.view.View$MeasureSpec;
import android.widget.ListAdapter;
import android.content.ActivityNotFoundException;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.SpannableStringBuilder;
import android.widget.AutoCompleteTextView;
import android.content.ComponentName;
import android.os.Parcelable;
import android.app.PendingIntent;
import android.util.Log;
import android.net.Uri;
import android.content.res.Resources;
import android.graphics.Rect;
import android.annotation.TargetApi;
import android.view.View$OnLayoutChangeListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.Editable;
import android.widget.AdapterView;
import android.widget.TextView;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.database.Cursor;
import android.view.inputmethod.InputMethodManager;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.content.Intent;
import android.text.TextWatcher;
import android.view.View$OnKeyListener;
import android.support.v4.widget.CursorAdapter;
import android.app.SearchableInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable$ConstantState;
import java.util.WeakHashMap;
import android.view.View$OnFocusChangeListener;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.TextView$OnEditorActionListener;
import android.view.View$OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView
{
    private static final boolean DBG = false;
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER;
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    private static final boolean IS_AT_LEAST_FROYO;
    private static final String LOG_TAG = "SearchView";
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    private final ImageView mCloseButton;
    private final ImageView mCollapsedIcon;
    private int mCollapsedImeOptions;
    private final CharSequence mDefaultQueryHint;
    private final AppCompatDrawableManager mDrawableManager;
    private final View mDropDownAnchor;
    private boolean mExpandedInActionView;
    private final ImageView mGoButton;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final View$OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final TextView$OnEditorActionListener mOnEditorActionListener;
    private final AdapterView$OnItemClickListener mOnItemClickListener;
    private final AdapterView$OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    private View$OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private View$OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private Runnable mReleaseCursorRunnable;
    private final ImageView mSearchButton;
    private final View mSearchEditFrame;
    private final Drawable mSearchHintIcon;
    private final View mSearchPlate;
    private final SearchAutoComplete mSearchSrcTextView;
    private SearchableInfo mSearchable;
    private Runnable mShowImeRunnable;
    private final View mSubmitArea;
    private boolean mSubmitButtonEnabled;
    private final int mSuggestionCommitIconResId;
    private final int mSuggestionRowLayout;
    private CursorAdapter mSuggestionsAdapter;
    View$OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private final Runnable mUpdateDrawableStateRunnable;
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    private final ImageView mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;
    
    static {
        IS_AT_LEAST_FROYO = (Build$VERSION.SDK_INT >= 8);
        HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
    }
    
    public SearchView(final Context context) {
        this(context, null);
    }
    
    public SearchView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.searchViewStyle);
    }
    
    public SearchView(final Context context, final AttributeSet set, int inputType) {
        super(context, set, inputType);
        this.mShowImeRunnable = new Runnable() {
            @Override
            public void run() {
                final InputMethodManager inputMethodManager = (InputMethodManager)SearchView.this.getContext().getSystemService("input_method");
                if (inputMethodManager != null) {
                    SearchView.HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(inputMethodManager, (View)SearchView.this, 0);
                }
            }
        };
        this.mUpdateDrawableStateRunnable = new Runnable() {
            @Override
            public void run() {
                SearchView.this.updateFocusedState();
            }
        };
        this.mReleaseCursorRunnable = new Runnable() {
            @Override
            public void run() {
                if (SearchView.this.mSuggestionsAdapter != null && SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
                    SearchView.this.mSuggestionsAdapter.changeCursor(null);
                }
            }
        };
        this.mOutsideDrawablesCache = new WeakHashMap<String, Drawable$ConstantState>();
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (view == SearchView.this.mSearchButton) {
                    SearchView.this.onSearchClicked();
                }
                else if (view == SearchView.this.mCloseButton) {
                    SearchView.this.onCloseClicked();
                }
                else if (view == SearchView.this.mGoButton) {
                    SearchView.this.onSubmitQuery();
                }
                else if (view == SearchView.this.mVoiceButton) {
                    SearchView.this.onVoiceClicked();
                }
                else if (view == SearchView.this.mSearchSrcTextView) {
                    SearchView.this.forceSuggestionQuery();
                }
            }
        };
        this.mTextKeyListener = (View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                final boolean b = false;
                boolean access$1500;
                if (SearchView.this.mSearchable == null) {
                    access$1500 = b;
                }
                else if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1) {
                    access$1500 = SearchView.this.onSuggestionsKey(view, n, keyEvent);
                }
                else {
                    access$1500 = b;
                    if (!SearchView.this.mSearchSrcTextView.isEmpty()) {
                        access$1500 = b;
                        if (KeyEventCompat.hasNoModifiers(keyEvent)) {
                            access$1500 = b;
                            if (keyEvent.getAction() == 1) {
                                access$1500 = b;
                                if (n == 66) {
                                    view.cancelLongPress();
                                    SearchView.this.launchQuerySearch(0, null, SearchView.this.mSearchSrcTextView.getText().toString());
                                    access$1500 = true;
                                }
                            }
                        }
                    }
                }
                return access$1500;
            }
        };
        this.mOnEditorActionListener = (TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                SearchView.this.onSubmitQuery();
                return true;
            }
        };
        this.mOnItemClickListener = (AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SearchView.this.onItemClicked(n, 0, null);
            }
        };
        this.mOnItemSelectedListener = (AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SearchView.this.onItemSelected(n);
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        };
        this.mTextWatcher = (TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                SearchView.this.onTextChanged(charSequence);
            }
        };
        this.mDrawableManager = AppCompatDrawableManager.get();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.SearchView, inputType, 0);
        LayoutInflater.from(context).inflate(obtainStyledAttributes.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view), (ViewGroup)this, true);
        (this.mSearchSrcTextView = (SearchAutoComplete)this.findViewById(R.id.search_src_text)).setSearchView(this);
        this.mSearchEditFrame = this.findViewById(R.id.search_edit_frame);
        this.mSearchPlate = this.findViewById(R.id.search_plate);
        this.mSubmitArea = this.findViewById(R.id.submit_area);
        this.mSearchButton = (ImageView)this.findViewById(R.id.search_button);
        this.mGoButton = (ImageView)this.findViewById(R.id.search_go_btn);
        this.mCloseButton = (ImageView)this.findViewById(R.id.search_close_btn);
        this.mVoiceButton = (ImageView)this.findViewById(R.id.search_voice_btn);
        this.mCollapsedIcon = (ImageView)this.findViewById(R.id.search_mag_icon);
        this.mSearchPlate.setBackgroundDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_queryBackground));
        this.mSubmitArea.setBackgroundDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_submitBackground));
        this.mSearchButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_searchIcon));
        this.mGoButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_goIcon));
        this.mCloseButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_closeIcon));
        this.mVoiceButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_voiceIcon));
        this.mCollapsedIcon.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_searchIcon));
        this.mSearchHintIcon = obtainStyledAttributes.getDrawable(R.styleable.SearchView_searchHintIcon);
        this.mSuggestionRowLayout = obtainStyledAttributes.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
        this.mSuggestionCommitIconResId = obtainStyledAttributes.getResourceId(R.styleable.SearchView_commitIcon, 0);
        this.mSearchButton.setOnClickListener(this.mOnClickListener);
        this.mCloseButton.setOnClickListener(this.mOnClickListener);
        this.mGoButton.setOnClickListener(this.mOnClickListener);
        this.mVoiceButton.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.addTextChangedListener(this.mTextWatcher);
        this.mSearchSrcTextView.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mSearchSrcTextView.setOnItemClickListener(this.mOnItemClickListener);
        this.mSearchSrcTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
        this.mSearchSrcTextView.setOnKeyListener(this.mTextKeyListener);
        this.mSearchSrcTextView.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean b) {
                if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
                    SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange((View)SearchView.this, b);
                }
            }
        });
        this.setIconifiedByDefault(obtainStyledAttributes.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
        inputType = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
        if (inputType != -1) {
            this.setMaxWidth(inputType);
        }
        this.mDefaultQueryHint = obtainStyledAttributes.getText(R.styleable.SearchView_defaultQueryHint);
        this.mQueryHint = obtainStyledAttributes.getText(R.styleable.SearchView_queryHint);
        inputType = obtainStyledAttributes.getInt(R.styleable.SearchView_android_imeOptions, -1);
        if (inputType != -1) {
            this.setImeOptions(inputType);
        }
        inputType = obtainStyledAttributes.getInt(R.styleable.SearchView_android_inputType, -1);
        if (inputType != -1) {
            this.setInputType(inputType);
        }
        this.setFocusable(obtainStyledAttributes.getBoolean(R.styleable.SearchView_android_focusable, true));
        obtainStyledAttributes.recycle();
        (this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH")).addFlags(268435456);
        this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        (this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH")).addFlags(268435456);
        this.mDropDownAnchor = this.findViewById(this.mSearchSrcTextView.getDropDownAnchor());
        if (this.mDropDownAnchor != null) {
            if (Build$VERSION.SDK_INT >= 11) {
                this.addOnLayoutChangeListenerToDropDownAnchorSDK11();
            }
            else {
                this.addOnLayoutChangeListenerToDropDownAnchorBase();
            }
        }
        this.updateViewsVisibility(this.mIconifiedByDefault);
        this.updateQueryHint();
    }
    
    private void addOnLayoutChangeListenerToDropDownAnchorBase() {
        this.mDropDownAnchor.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                SearchView.this.adjustDropDownSizeAndPosition();
            }
        });
    }
    
    @TargetApi(11)
    private void addOnLayoutChangeListenerToDropDownAnchorSDK11() {
        this.mDropDownAnchor.addOnLayoutChangeListener((View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
            public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                SearchView.this.adjustDropDownSizeAndPosition();
            }
        });
    }
    
    private void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            final Resources resources = this.getContext().getResources();
            final int paddingLeft = this.mSearchPlate.getPaddingLeft();
            final Rect rect = new Rect();
            final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
            int n;
            if (this.mIconifiedByDefault) {
                n = resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left);
            }
            else {
                n = 0;
            }
            this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
            int dropDownHorizontalOffset;
            if (layoutRtl) {
                dropDownHorizontalOffset = -rect.left;
            }
            else {
                dropDownHorizontalOffset = paddingLeft - (rect.left + n);
            }
            this.mSearchSrcTextView.setDropDownHorizontalOffset(dropDownHorizontalOffset);
            this.mSearchSrcTextView.setDropDownWidth(this.mDropDownAnchor.getWidth() + rect.left + rect.right + n - paddingLeft);
        }
    }
    
    private Intent createIntent(final String s, final Uri data, final String s2, final String s3, final int n, final String s4) {
        final Intent intent = new Intent(s);
        intent.addFlags(268435456);
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra("user_query", this.mUserQuery);
        if (s3 != null) {
            intent.putExtra("query", s3);
        }
        if (s2 != null) {
            intent.putExtra("intent_extra_data_key", s2);
        }
        if (this.mAppSearchData != null) {
            intent.putExtra("app_data", this.mAppSearchData);
        }
        if (n != 0) {
            intent.putExtra("action_key", n);
            intent.putExtra("action_msg", s4);
        }
        if (SearchView.IS_AT_LEAST_FROYO) {
            intent.setComponent(this.mSearchable.getSearchActivity());
        }
        return intent;
    }
    
    private Intent createIntentFromSuggestion(Cursor intent, int position, final String ex) {
        try {
            String s2;
            final String s = s2 = SuggestionsAdapter.getColumnString((Cursor)intent, "suggest_intent_action");
            if (s == null) {
                s2 = s;
                if (Build$VERSION.SDK_INT >= 8) {
                    s2 = this.mSearchable.getSuggestIntentAction();
                }
            }
            String s3;
            if ((s3 = s2) == null) {
                s3 = "android.intent.action.SEARCH";
            }
            String str;
            final String s4 = str = SuggestionsAdapter.getColumnString((Cursor)intent, "suggest_intent_data");
            if (SearchView.IS_AT_LEAST_FROYO && (str = s4) == null) {
                str = this.mSearchable.getSuggestIntentData();
            }
            String string;
            if ((string = str) != null) {
                final String columnString = SuggestionsAdapter.getColumnString((Cursor)intent, "suggest_intent_data_id");
                string = str;
                if (columnString != null) {
                    string = str + "/" + Uri.encode(columnString);
                }
            }
            Uri parse;
            if (string == null) {
                parse = null;
            }
            else {
                parse = Uri.parse(string);
            }
            intent = this.createIntent(s3, parse, SuggestionsAdapter.getColumnString((Cursor)intent, "suggest_intent_extra_data"), SuggestionsAdapter.getColumnString((Cursor)intent, "suggest_intent_query"), position, (String)ex);
            return (Intent)intent;
        }
        catch (RuntimeException ex) {
            try {
                position = ((Cursor)intent).getPosition();
                Log.w("SearchView", "Search suggestions cursor at row " + position + " returned exception.", (Throwable)ex);
                intent = null;
            }
            catch (RuntimeException ex2) {
                position = -1;
            }
        }
    }
    
    @TargetApi(8)
    private Intent createVoiceAppSearchIntent(final Intent intent, final SearchableInfo searchableInfo) {
        final ComponentName searchActivity = searchableInfo.getSearchActivity();
        final Intent intent2 = new Intent("android.intent.action.SEARCH");
        intent2.setComponent(searchActivity);
        final PendingIntent activity = PendingIntent.getActivity(this.getContext(), 0, intent2, 1073741824);
        final Bundle bundle = new Bundle();
        if (this.mAppSearchData != null) {
            bundle.putParcelable("app_data", (Parcelable)this.mAppSearchData);
        }
        final Intent intent3 = new Intent(intent);
        String string = "free_form";
        String s = null;
        String string2 = null;
        String s2 = null;
        String string3 = null;
        final int n = 1;
        String s3 = string;
        int voiceMaxResults = n;
        if (Build$VERSION.SDK_INT >= 8) {
            final Resources resources = this.getResources();
            if (searchableInfo.getVoiceLanguageModeId() != 0) {
                string = resources.getString(searchableInfo.getVoiceLanguageModeId());
            }
            if (searchableInfo.getVoicePromptTextId() != 0) {
                string2 = resources.getString(searchableInfo.getVoicePromptTextId());
            }
            if (searchableInfo.getVoiceLanguageId() != 0) {
                string3 = resources.getString(searchableInfo.getVoiceLanguageId());
            }
            s2 = string3;
            s3 = string;
            voiceMaxResults = n;
            s = string2;
            if (searchableInfo.getVoiceMaxResults() != 0) {
                voiceMaxResults = searchableInfo.getVoiceMaxResults();
                s = string2;
                s3 = string;
                s2 = string3;
            }
        }
        intent3.putExtra("android.speech.extra.LANGUAGE_MODEL", s3);
        intent3.putExtra("android.speech.extra.PROMPT", s);
        intent3.putExtra("android.speech.extra.LANGUAGE", s2);
        intent3.putExtra("android.speech.extra.MAX_RESULTS", voiceMaxResults);
        String flattenToShortString;
        if (searchActivity == null) {
            flattenToShortString = null;
        }
        else {
            flattenToShortString = searchActivity.flattenToShortString();
        }
        intent3.putExtra("calling_package", flattenToShortString);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)activity);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent3;
    }
    
    @TargetApi(8)
    private Intent createVoiceWebSearchIntent(final Intent intent, final SearchableInfo searchableInfo) {
        final Intent intent2 = new Intent(intent);
        final ComponentName searchActivity = searchableInfo.getSearchActivity();
        String flattenToShortString;
        if (searchActivity == null) {
            flattenToShortString = null;
        }
        else {
            flattenToShortString = searchActivity.flattenToShortString();
        }
        intent2.putExtra("calling_package", flattenToShortString);
        return intent2;
    }
    
    private void dismissSuggestions() {
        this.mSearchSrcTextView.dismissDropDown();
    }
    
    private void forceSuggestionQuery() {
        SearchView.HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
        SearchView.HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
    }
    
    private CharSequence getDecoratedHint(CharSequence charSequence) {
        if (this.mIconifiedByDefault && this.mSearchHintIcon != null) {
            final int n = (int)(this.mSearchSrcTextView.getTextSize() * 1.25);
            this.mSearchHintIcon.setBounds(0, 0, n, n);
            final Object o = new SpannableStringBuilder((CharSequence)"   ");
            ((SpannableStringBuilder)o).setSpan((Object)new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
            ((SpannableStringBuilder)o).append(charSequence);
            charSequence = (CharSequence)o;
        }
        return charSequence;
    }
    
    private int getPreferredWidth() {
        return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
    }
    
    @TargetApi(8)
    private boolean hasVoiceSearch() {
        boolean b2;
        final boolean b = b2 = false;
        if (this.mSearchable != null) {
            b2 = b;
            if (this.mSearchable.getVoiceSearchEnabled()) {
                Intent intent = null;
                if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
                    intent = this.mVoiceWebSearchIntent;
                }
                else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
                    intent = this.mVoiceAppSearchIntent;
                }
                b2 = b;
                if (intent != null) {
                    b2 = b;
                    if (this.getContext().getPackageManager().resolveActivity(intent, 65536) != null) {
                        b2 = true;
                    }
                }
            }
        }
        return b2;
    }
    
    static boolean isLandscapeMode(final Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }
    
    private boolean isSubmitAreaEnabled() {
        return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified();
    }
    
    private void launchIntent(final Intent obj) {
        if (obj != null) {
            try {
                this.getContext().startActivity(obj);
            }
            catch (RuntimeException ex) {
                Log.e("SearchView", "Failed launch activity: " + obj, (Throwable)ex);
            }
        }
    }
    
    private void launchQuerySearch(final int n, final String s, final String s2) {
        this.getContext().startActivity(this.createIntent("android.intent.action.SEARCH", null, null, s2, n, s));
    }
    
    private boolean launchSuggestion(final int n, final int n2, final String s) {
        final Cursor cursor = this.mSuggestionsAdapter.getCursor();
        boolean b;
        if (cursor != null && cursor.moveToPosition(n)) {
            this.launchIntent(this.createIntentFromSuggestion(cursor, n2, s));
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private void onCloseClicked() {
        if (TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
            if (this.mIconifiedByDefault && (this.mOnCloseListener == null || !this.mOnCloseListener.onClose())) {
                this.clearFocus();
                this.updateViewsVisibility(true);
            }
        }
        else {
            this.mSearchSrcTextView.setText((CharSequence)"");
            this.mSearchSrcTextView.requestFocus();
            this.setImeVisibility(true);
        }
    }
    
    private boolean onItemClicked(final int n, final int n2, final String s) {
        boolean b = false;
        if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionClick(n)) {
            this.launchSuggestion(n, 0, null);
            this.setImeVisibility(false);
            this.dismissSuggestions();
            b = true;
        }
        return b;
    }
    
    private boolean onItemSelected(final int n) {
        boolean b;
        if (this.mOnSuggestionListener == null || !this.mOnSuggestionListener.onSuggestionSelect(n)) {
            this.rewriteQueryFromSuggestion(n);
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private void onSearchClicked() {
        this.updateViewsVisibility(false);
        this.mSearchSrcTextView.requestFocus();
        this.setImeVisibility(true);
        if (this.mOnSearchClickListener != null) {
            this.mOnSearchClickListener.onClick((View)this);
        }
    }
    
    private void onSubmitQuery() {
        final Editable text = this.mSearchSrcTextView.getText();
        if (text != null && TextUtils.getTrimmedLength((CharSequence)text) > 0 && (this.mOnQueryChangeListener == null || !this.mOnQueryChangeListener.onQueryTextSubmit(((CharSequence)text).toString()))) {
            if (this.mSearchable != null) {
                this.launchQuerySearch(0, null, ((CharSequence)text).toString());
            }
            this.setImeVisibility(false);
            this.dismissSuggestions();
        }
    }
    
    private boolean onSuggestionsKey(final View view, int length, final KeyEvent keyEvent) {
        final boolean b = false;
        boolean onItemClicked;
        if (this.mSearchable == null) {
            onItemClicked = b;
        }
        else {
            onItemClicked = b;
            if (this.mSuggestionsAdapter != null) {
                onItemClicked = b;
                if (keyEvent.getAction() == 0) {
                    onItemClicked = b;
                    if (KeyEventCompat.hasNoModifiers(keyEvent)) {
                        if (length == 66 || length == 84 || length == 61) {
                            onItemClicked = this.onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, null);
                        }
                        else if (length == 21 || length == 22) {
                            if (length == 21) {
                                length = 0;
                            }
                            else {
                                length = this.mSearchSrcTextView.length();
                            }
                            this.mSearchSrcTextView.setSelection(length);
                            this.mSearchSrcTextView.setListSelection(0);
                            this.mSearchSrcTextView.clearListSelection();
                            SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
                            onItemClicked = true;
                        }
                        else {
                            onItemClicked = b;
                            if (length == 19) {
                                onItemClicked = b;
                                if (this.mSearchSrcTextView.getListSelection() == 0) {
                                    onItemClicked = b;
                                }
                            }
                        }
                    }
                }
            }
        }
        return onItemClicked;
    }
    
    private void onTextChanged(final CharSequence charSequence) {
        final boolean b = true;
        final Editable text = this.mSearchSrcTextView.getText();
        this.mUserQuery = (CharSequence)text;
        final boolean b2 = !TextUtils.isEmpty((CharSequence)text);
        this.updateSubmitButton(b2);
        this.updateVoiceButton(!b2 && b);
        this.updateCloseButton();
        this.updateSubmitArea();
        if (this.mOnQueryChangeListener != null && !TextUtils.equals(charSequence, this.mOldQueryText)) {
            this.mOnQueryChangeListener.onQueryTextChange(charSequence.toString());
        }
        this.mOldQueryText = charSequence.toString();
    }
    
    @TargetApi(8)
    private void onVoiceClicked() {
        if (this.mSearchable != null) {
            final SearchableInfo mSearchable = this.mSearchable;
            Label_0054: {
                try {
                    if (!mSearchable.getVoiceSearchLaunchWebSearch()) {
                        break Label_0054;
                    }
                    this.getContext().startActivity(this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, mSearchable));
                }
                catch (ActivityNotFoundException ex) {
                    Log.w("SearchView", "Could not find voice search activity");
                }
                return;
            }
            if (mSearchable.getVoiceSearchLaunchRecognizer()) {
                this.getContext().startActivity(this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, mSearchable));
            }
        }
    }
    
    private void postUpdateFocusedState() {
        this.post(this.mUpdateDrawableStateRunnable);
    }
    
    private void rewriteQueryFromSuggestion(final int n) {
        final Editable text = this.mSearchSrcTextView.getText();
        final Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor != null) {
            if (cursor.moveToPosition(n)) {
                final CharSequence convertToString = this.mSuggestionsAdapter.convertToString(cursor);
                if (convertToString != null) {
                    this.setQuery(convertToString);
                }
                else {
                    this.setQuery((CharSequence)text);
                }
            }
            else {
                this.setQuery((CharSequence)text);
            }
        }
    }
    
    private void setImeVisibility(final boolean b) {
        if (b) {
            this.post(this.mShowImeRunnable);
        }
        else {
            this.removeCallbacks(this.mShowImeRunnable);
            final InputMethodManager inputMethodManager = (InputMethodManager)this.getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
            }
        }
    }
    
    private void setQuery(final CharSequence text) {
        this.mSearchSrcTextView.setText(text);
        final SearchAutoComplete mSearchSrcTextView = this.mSearchSrcTextView;
        int length;
        if (TextUtils.isEmpty(text)) {
            length = 0;
        }
        else {
            length = text.length();
        }
        mSearchSrcTextView.setSelection(length);
    }
    
    private void updateCloseButton() {
        final boolean b = true;
        final int n = 0;
        boolean b2;
        if (!TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        int n2 = b ? 1 : 0;
        if (!b2) {
            if (this.mIconifiedByDefault && !this.mExpandedInActionView) {
                n2 = (b ? 1 : 0);
            }
            else {
                n2 = 0;
            }
        }
        final ImageView mCloseButton = this.mCloseButton;
        int visibility;
        if (n2 != 0) {
            visibility = n;
        }
        else {
            visibility = 8;
        }
        mCloseButton.setVisibility(visibility);
        final Drawable drawable = this.mCloseButton.getDrawable();
        if (drawable != null) {
            int[] state;
            if (b2) {
                state = SearchView.ENABLED_STATE_SET;
            }
            else {
                state = SearchView.EMPTY_STATE_SET;
            }
            drawable.setState(state);
        }
    }
    
    private void updateFocusedState() {
        int[] array;
        if (this.mSearchSrcTextView.hasFocus()) {
            array = SearchView.FOCUSED_STATE_SET;
        }
        else {
            array = SearchView.EMPTY_STATE_SET;
        }
        final Drawable background = this.mSearchPlate.getBackground();
        if (background != null) {
            background.setState(array);
        }
        final Drawable background2 = this.mSubmitArea.getBackground();
        if (background2 != null) {
            background2.setState(array);
        }
        this.invalidate();
    }
    
    private void updateQueryHint() {
        final CharSequence queryHint = this.getQueryHint();
        final SearchAutoComplete mSearchSrcTextView = this.mSearchSrcTextView;
        CharSequence charSequence = queryHint;
        if (queryHint == null) {
            charSequence = "";
        }
        mSearchSrcTextView.setHint(this.getDecoratedHint(charSequence));
    }
    
    @TargetApi(8)
    private void updateSearchAutoComplete() {
        final int n = 1;
        this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
        int inputType;
        final int n2 = inputType = this.mSearchable.getInputType();
        if ((n2 & 0xF) == 0x1) {
            inputType = (n2 & 0xFFFEFFFF);
            if (this.mSearchable.getSuggestAuthority() != null) {
                inputType = (inputType | 0x10000 | 0x80000);
            }
        }
        this.mSearchSrcTextView.setInputType(inputType);
        if (this.mSuggestionsAdapter != null) {
            this.mSuggestionsAdapter.changeCursor(null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            this.mSuggestionsAdapter = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mSearchSrcTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
            final SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
            int queryRefinement = n;
            if (this.mQueryRefinement) {
                queryRefinement = 2;
            }
            suggestionsAdapter.setQueryRefinement(queryRefinement);
        }
    }
    
    private void updateSubmitArea() {
        int visibility = 8;
        Label_0036: {
            if (this.isSubmitAreaEnabled()) {
                if (this.mGoButton.getVisibility() != 0) {
                    visibility = visibility;
                    if (this.mVoiceButton.getVisibility() != 0) {
                        break Label_0036;
                    }
                }
                visibility = 0;
            }
        }
        this.mSubmitArea.setVisibility(visibility);
    }
    
    private void updateSubmitButton(final boolean b) {
        int visibility;
        final int n = visibility = 8;
        Label_0045: {
            if (this.mSubmitButtonEnabled) {
                visibility = n;
                if (this.isSubmitAreaEnabled()) {
                    visibility = n;
                    if (this.hasFocus()) {
                        if (!b) {
                            visibility = n;
                            if (this.mVoiceButtonEnabled) {
                                break Label_0045;
                            }
                        }
                        visibility = 0;
                    }
                }
            }
        }
        this.mGoButton.setVisibility(visibility);
    }
    
    private void updateViewsVisibility(final boolean mIconified) {
        final int n = 8;
        final boolean b = true;
        this.mIconified = mIconified;
        int visibility;
        if (mIconified) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        final boolean b2 = !TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText());
        this.mSearchButton.setVisibility(visibility);
        this.updateSubmitButton(b2);
        final View mSearchEditFrame = this.mSearchEditFrame;
        int visibility2;
        if (mIconified) {
            visibility2 = n;
        }
        else {
            visibility2 = 0;
        }
        mSearchEditFrame.setVisibility(visibility2);
        int visibility3;
        if (this.mCollapsedIcon.getDrawable() == null || this.mIconifiedByDefault) {
            visibility3 = 8;
        }
        else {
            visibility3 = 0;
        }
        this.mCollapsedIcon.setVisibility(visibility3);
        this.updateCloseButton();
        this.updateVoiceButton(!b2 && b);
        this.updateSubmitArea();
    }
    
    private void updateVoiceButton(final boolean b) {
        int visibility;
        final int n = visibility = 8;
        if (this.mVoiceButtonEnabled) {
            visibility = n;
            if (!this.isIconified()) {
                visibility = n;
                if (b) {
                    visibility = 0;
                    this.mGoButton.setVisibility(8);
                }
            }
        }
        this.mVoiceButton.setVisibility(visibility);
    }
    
    public void clearFocus() {
        this.mClearingFocus = true;
        this.setImeVisibility(false);
        super.clearFocus();
        this.mSearchSrcTextView.clearFocus();
        this.mClearingFocus = false;
    }
    
    public int getImeOptions() {
        return this.mSearchSrcTextView.getImeOptions();
    }
    
    public int getInputType() {
        return this.mSearchSrcTextView.getInputType();
    }
    
    public int getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public CharSequence getQuery() {
        return (CharSequence)this.mSearchSrcTextView.getText();
    }
    
    public CharSequence getQueryHint() {
        CharSequence charSequence;
        if (this.mQueryHint != null) {
            charSequence = this.mQueryHint;
        }
        else if (SearchView.IS_AT_LEAST_FROYO && this.mSearchable != null && this.mSearchable.getHintId() != 0) {
            charSequence = this.getContext().getText(this.mSearchable.getHintId());
        }
        else {
            charSequence = this.mDefaultQueryHint;
        }
        return charSequence;
    }
    
    int getSuggestionCommitIconResId() {
        return this.mSuggestionCommitIconResId;
    }
    
    int getSuggestionRowLayout() {
        return this.mSuggestionRowLayout;
    }
    
    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }
    
    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }
    
    public boolean isIconified() {
        return this.mIconified;
    }
    
    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }
    
    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }
    
    @Override
    public void onActionViewCollapsed() {
        this.setQuery("", false);
        this.clearFocus();
        this.updateViewsVisibility(true);
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }
    
    @Override
    public void onActionViewExpanded() {
        if (!this.mExpandedInActionView) {
            this.mExpandedInActionView = true;
            this.mCollapsedImeOptions = this.mSearchSrcTextView.getImeOptions();
            this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions | 0x2000000);
            this.mSearchSrcTextView.setText((CharSequence)"");
            this.setIconified(false);
        }
    }
    
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mUpdateDrawableStateRunnable);
        this.post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }
    
    @Override
    protected void onMeasure(int n, final int n2) {
        if (this.isIconified()) {
            super.onMeasure(n, n2);
        }
        else {
            final int mode = View$MeasureSpec.getMode(n);
            final int size = View$MeasureSpec.getSize(n);
            switch (mode) {
                default: {
                    n = size;
                    break;
                }
                case Integer.MIN_VALUE: {
                    if (this.mMaxWidth > 0) {
                        n = Math.min(this.mMaxWidth, size);
                        break;
                    }
                    n = Math.min(this.getPreferredWidth(), size);
                    break;
                }
                case 1073741824: {
                    n = size;
                    if (this.mMaxWidth > 0) {
                        n = Math.min(this.mMaxWidth, size);
                        break;
                    }
                    break;
                }
                case 0: {
                    if (this.mMaxWidth > 0) {
                        n = this.mMaxWidth;
                    }
                    else {
                        n = this.getPreferredWidth();
                    }
                    break;
                }
            }
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), n2);
        }
    }
    
    void onQueryRefine(final CharSequence query) {
        this.setQuery(query);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.updateViewsVisibility(savedState.isIconified);
        this.requestLayout();
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isIconified = this.isIconified();
        return (Parcelable)savedState;
    }
    
    void onTextFocusChanged() {
        this.updateViewsVisibility(this.isIconified());
        this.postUpdateFocusedState();
        if (this.mSearchSrcTextView.hasFocus()) {
            this.forceSuggestionQuery();
        }
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        this.postUpdateFocusedState();
    }
    
    public boolean requestFocus(final int n, final Rect rect) {
        boolean b;
        if (this.mClearingFocus) {
            b = false;
        }
        else if (!this.isFocusable()) {
            b = false;
        }
        else if (!this.isIconified()) {
            final boolean b2 = b = this.mSearchSrcTextView.requestFocus(n, rect);
            if (b2) {
                this.updateViewsVisibility(false);
                b = b2;
            }
        }
        else {
            b = super.requestFocus(n, rect);
        }
        return b;
    }
    
    public void setAppSearchData(final Bundle mAppSearchData) {
        this.mAppSearchData = mAppSearchData;
    }
    
    public void setIconified(final boolean b) {
        if (b) {
            this.onCloseClicked();
        }
        else {
            this.onSearchClicked();
        }
    }
    
    public void setIconifiedByDefault(final boolean mIconifiedByDefault) {
        if (this.mIconifiedByDefault != mIconifiedByDefault) {
            this.updateViewsVisibility(this.mIconifiedByDefault = mIconifiedByDefault);
            this.updateQueryHint();
        }
    }
    
    public void setImeOptions(final int imeOptions) {
        this.mSearchSrcTextView.setImeOptions(imeOptions);
    }
    
    public void setInputType(final int inputType) {
        this.mSearchSrcTextView.setInputType(inputType);
    }
    
    public void setMaxWidth(final int mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
        this.requestLayout();
    }
    
    public void setOnCloseListener(final OnCloseListener mOnCloseListener) {
        this.mOnCloseListener = mOnCloseListener;
    }
    
    public void setOnQueryTextFocusChangeListener(final View$OnFocusChangeListener mOnQueryTextFocusChangeListener) {
        this.mOnQueryTextFocusChangeListener = mOnQueryTextFocusChangeListener;
    }
    
    public void setOnQueryTextListener(final OnQueryTextListener mOnQueryChangeListener) {
        this.mOnQueryChangeListener = mOnQueryChangeListener;
    }
    
    public void setOnSearchClickListener(final View$OnClickListener mOnSearchClickListener) {
        this.mOnSearchClickListener = mOnSearchClickListener;
    }
    
    public void setOnSuggestionListener(final OnSuggestionListener mOnSuggestionListener) {
        this.mOnSuggestionListener = mOnSuggestionListener;
    }
    
    public void setQuery(final CharSequence charSequence, final boolean b) {
        this.mSearchSrcTextView.setText(charSequence);
        if (charSequence != null) {
            this.mSearchSrcTextView.setSelection(this.mSearchSrcTextView.length());
            this.mUserQuery = charSequence;
        }
        if (b && !TextUtils.isEmpty(charSequence)) {
            this.onSubmitQuery();
        }
    }
    
    public void setQueryHint(final CharSequence mQueryHint) {
        this.mQueryHint = mQueryHint;
        this.updateQueryHint();
    }
    
    public void setQueryRefinementEnabled(final boolean mQueryRefinement) {
        this.mQueryRefinement = mQueryRefinement;
        if (this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
            final SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
            int queryRefinement;
            if (mQueryRefinement) {
                queryRefinement = 2;
            }
            else {
                queryRefinement = 1;
            }
            suggestionsAdapter.setQueryRefinement(queryRefinement);
        }
    }
    
    public void setSearchableInfo(final SearchableInfo mSearchable) {
        this.mSearchable = mSearchable;
        if (this.mSearchable != null) {
            if (SearchView.IS_AT_LEAST_FROYO) {
                this.updateSearchAutoComplete();
            }
            this.updateQueryHint();
        }
        this.mVoiceButtonEnabled = (SearchView.IS_AT_LEAST_FROYO && this.hasVoiceSearch());
        if (this.mVoiceButtonEnabled) {
            this.mSearchSrcTextView.setPrivateImeOptions("nm");
        }
        this.updateViewsVisibility(this.isIconified());
    }
    
    public void setSubmitButtonEnabled(final boolean mSubmitButtonEnabled) {
        this.mSubmitButtonEnabled = mSubmitButtonEnabled;
        this.updateViewsVisibility(this.isIconified());
    }
    
    public void setSuggestionsAdapter(final CursorAdapter mSuggestionsAdapter) {
        this.mSuggestionsAdapter = mSuggestionsAdapter;
        this.mSearchSrcTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
    }
    
    private static class AutoCompleteTextViewReflector
    {
        private Method doAfterTextChanged;
        private Method doBeforeTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;
        
        AutoCompleteTextViewReflector() {
            while (true) {
                try {
                    (this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", (Class<?>[])new Class[0])).setAccessible(true);
                    try {
                        (this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", (Class<?>[])new Class[0])).setAccessible(true);
                        try {
                            (this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE)).setAccessible(true);
                            try {
                                (this.showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", Integer.TYPE, ResultReceiver.class)).setAccessible(true);
                            }
                            catch (NoSuchMethodException ex) {}
                        }
                        catch (NoSuchMethodException ex2) {}
                    }
                    catch (NoSuchMethodException ex3) {}
                }
                catch (NoSuchMethodException ex4) {
                    continue;
                }
                break;
            }
        }
        
        void doAfterTextChanged(final AutoCompleteTextView obj) {
            if (this.doAfterTextChanged == null) {
                return;
            }
            try {
                this.doAfterTextChanged.invoke(obj, new Object[0]);
            }
            catch (Exception ex) {}
        }
        
        void doBeforeTextChanged(final AutoCompleteTextView obj) {
            if (this.doBeforeTextChanged == null) {
                return;
            }
            try {
                this.doBeforeTextChanged.invoke(obj, new Object[0]);
            }
            catch (Exception ex) {}
        }
        
        void ensureImeVisible(final AutoCompleteTextView obj, final boolean b) {
            if (this.ensureImeVisible == null) {
                return;
            }
            try {
                this.ensureImeVisible.invoke(obj, b);
            }
            catch (Exception ex) {}
        }
        
        void showSoftInputUnchecked(final InputMethodManager obj, final View view, final int i) {
            Label_0034: {
                if (this.showSoftInputUnchecked == null) {
                    break Label_0034;
                }
                try {
                    this.showSoftInputUnchecked.invoke(obj, i, null);
                    return;
                }
                catch (Exception ex) {}
            }
            obj.showSoftInput(view, i);
        }
    }
    
    public interface OnCloseListener
    {
        boolean onClose();
    }
    
    public interface OnQueryTextListener
    {
        boolean onQueryTextChange(final String p0);
        
        boolean onQueryTextSubmit(final String p0);
    }
    
    public interface OnSuggestionListener
    {
        boolean onSuggestionClick(final int p0);
        
        boolean onSuggestionSelect(final int p0);
    }
    
    static class SavedState extends View$BaseSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean isIconified;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            super(parcel);
            this.isIconified = (boolean)parcel.readValue((ClassLoader)null);
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        public String toString() {
            return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.isIconified + "}";
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeValue((Object)this.isIconified);
        }
    }
    
    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView
    {
        private SearchView mSearchView;
        private int mThreshold;
        
        public SearchAutoComplete(final Context context) {
            this(context, null);
        }
        
        public SearchAutoComplete(final Context context, final AttributeSet set) {
            this(context, set, R.attr.autoCompleteTextViewStyle);
        }
        
        public SearchAutoComplete(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
            this.mThreshold = this.getThreshold();
        }
        
        private boolean isEmpty() {
            return TextUtils.getTrimmedLength((CharSequence)this.getText()) == 0;
        }
        
        public boolean enoughToFilter() {
            return this.mThreshold <= 0 || super.enoughToFilter();
        }
        
        protected void onFocusChanged(final boolean b, final int n, final Rect rect) {
            super.onFocusChanged(b, n, rect);
            this.mSearchView.onTextFocusChanged();
        }
        
        public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
            final boolean b = true;
            if (n != 4) {
                return super.onKeyPreIme(n, keyEvent);
            }
            boolean onKeyPreIme;
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                final KeyEvent$DispatcherState keyDispatcherState = this.getKeyDispatcherState();
                onKeyPreIme = b;
                if (keyDispatcherState != null) {
                    keyDispatcherState.startTracking(keyEvent, (Object)this);
                    onKeyPreIme = b;
                }
            }
            else {
                if (keyEvent.getAction() != 1) {
                    return super.onKeyPreIme(n, keyEvent);
                }
                final KeyEvent$DispatcherState keyDispatcherState2 = this.getKeyDispatcherState();
                if (keyDispatcherState2 != null) {
                    keyDispatcherState2.handleUpEvent(keyEvent);
                }
                if (!keyEvent.isTracking() || keyEvent.isCanceled()) {
                    return super.onKeyPreIme(n, keyEvent);
                }
                this.mSearchView.clearFocus();
                this.mSearchView.setImeVisibility(false);
                onKeyPreIme = b;
            }
            return onKeyPreIme;
            onKeyPreIme = super.onKeyPreIme(n, keyEvent);
            return onKeyPreIme;
        }
        
        public void onWindowFocusChanged(final boolean b) {
            super.onWindowFocusChanged(b);
            if (b && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
                ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
                if (SearchView.isLandscapeMode(this.getContext())) {
                    SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }
        
        public void performCompletion() {
        }
        
        protected void replaceText(final CharSequence charSequence) {
        }
        
        void setSearchView(final SearchView mSearchView) {
            this.mSearchView = mSearchView;
        }
        
        public void setThreshold(final int n) {
            super.setThreshold(n);
            this.mThreshold = n;
        }
    }
}
