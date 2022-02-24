// 
// Decompiled by Procyon v0.5.36
// 

package uk.co.chrisjenx.calligraphy;

import android.text.TextUtils;
import android.os.Build$VERSION;
import java.util.Collections;
import android.widget.ToggleButton;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.MultiAutoCompleteTextView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Button;
import java.util.HashMap;
import android.widget.TextView;
import java.util.Map;

public class CalligraphyConfig
{
    private static final Map<Class<? extends TextView>, Integer> DEFAULT_STYLES;
    private static CalligraphyConfig sInstance;
    private final int mAttrId;
    private final Map<Class<? extends TextView>, Integer> mClassStyleAttributeMap;
    private final boolean mCustomViewCreation;
    private final String mFontPath;
    private final boolean mIsFontSet;
    private final boolean mReflection;
    
    static {
        (DEFAULT_STYLES = new HashMap<Class<? extends TextView>, Integer>()).put(TextView.class, 16842884);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)Button.class, 16842824);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)EditText.class, 16842862);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)AutoCompleteTextView.class, 16842859);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)MultiAutoCompleteTextView.class, 16842859);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)CheckBox.class, 16842860);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)RadioButton.class, 16842878);
        CalligraphyConfig.DEFAULT_STYLES.put((Class<? extends TextView>)ToggleButton.class, 16842827);
    }
    
    protected CalligraphyConfig(final Builder builder) {
        this.mIsFontSet = builder.isFontSet;
        this.mFontPath = builder.fontAssetPath;
        this.mAttrId = builder.attrId;
        this.mReflection = builder.reflection;
        this.mCustomViewCreation = builder.customViewCreation;
        final HashMap<Class<? extends TextView>, Integer> m = new HashMap<Class<? extends TextView>, Integer>(CalligraphyConfig.DEFAULT_STYLES);
        m.putAll((Map<?, ?>)builder.mStyleClassMap);
        this.mClassStyleAttributeMap = (Map<Class<? extends TextView>, Integer>)Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    public static CalligraphyConfig get() {
        if (CalligraphyConfig.sInstance == null) {
            CalligraphyConfig.sInstance = new CalligraphyConfig(new Builder());
        }
        return CalligraphyConfig.sInstance;
    }
    
    public static void initDefault(final CalligraphyConfig sInstance) {
        CalligraphyConfig.sInstance = sInstance;
    }
    
    public int getAttrId() {
        return this.mAttrId;
    }
    
    Map<Class<? extends TextView>, Integer> getClassStyles() {
        return this.mClassStyleAttributeMap;
    }
    
    public String getFontPath() {
        return this.mFontPath;
    }
    
    public boolean isCustomViewCreation() {
        return this.mCustomViewCreation;
    }
    
    boolean isFontSet() {
        return this.mIsFontSet;
    }
    
    public boolean isReflection() {
        return this.mReflection;
    }
    
    public static class Builder
    {
        public static final int INVALID_ATTR_ID = -1;
        private int attrId;
        private boolean customViewCreation;
        private String fontAssetPath;
        private boolean isFontSet;
        private Map<Class<? extends TextView>, Integer> mStyleClassMap;
        private boolean reflection;
        
        public Builder() {
            this.reflection = (Build$VERSION.SDK_INT >= 11);
            this.customViewCreation = true;
            this.attrId = R.attr.fontPath;
            this.isFontSet = false;
            this.fontAssetPath = null;
            this.mStyleClassMap = new HashMap<Class<? extends TextView>, Integer>();
        }
        
        public Builder addCustomStyle(final Class<? extends TextView> clazz, final int i) {
            if (clazz != null && i != 0) {
                this.mStyleClassMap.put(clazz, i);
            }
            return this;
        }
        
        public CalligraphyConfig build() {
            this.isFontSet = !TextUtils.isEmpty((CharSequence)this.fontAssetPath);
            return new CalligraphyConfig(this);
        }
        
        public Builder disableCustomViewInflation() {
            this.customViewCreation = false;
            return this;
        }
        
        public Builder disablePrivateFactoryInjection() {
            this.reflection = false;
            return this;
        }
        
        public Builder setDefaultFontPath(final String fontAssetPath) {
            this.isFontSet = !TextUtils.isEmpty((CharSequence)fontAssetPath);
            this.fontAssetPath = fontAssetPath;
            return this;
        }
        
        public Builder setFontAttrId(int attrId) {
            if (attrId == -1) {
                attrId = -1;
            }
            this.attrId = attrId;
            return this;
        }
    }
}
