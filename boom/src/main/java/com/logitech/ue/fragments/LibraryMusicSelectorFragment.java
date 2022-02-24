// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.fragments;

import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import com.logitech.ue.Utils;
import com.logitech.ue.other.AlarmMusicType;
import com.logitech.ue.devicedata.AlarmSettings;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.support.annotation.NonNull;
import android.database.CursorWrapper;
import android.widget.AdapterView;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.logitech.ue.App;
import android.provider.MediaStore$Audio$Media;
import android.provider.MediaStore$Audio$Artists;
import android.provider.MediaStore$Audio$Albums;
import android.content.Context;
import android.database.Cursor;
import android.view.View$OnClickListener;
import android.widget.TabHost$TabSpec;
import android.widget.TabHost;
import android.view.View;
import com.logitech.ue.other.MusicSelection;
import android.widget.TextView;
import android.widget.ImageView;
import com.logitech.ue.interfaces.OnItemSelectedListener;
import android.support.v4.view.ViewPager;
import android.widget.TabHost$OnTabChangeListener;
import android.widget.TabHost$TabContentFactory;
import android.widget.AdapterView$OnItemClickListener;

public class LibraryMusicSelectorFragment extends NavigatableFragment implements AdapterView$OnItemClickListener, TabHost$TabContentFactory, TabHost$OnTabChangeListener, OnPageChangeListener, OnItemSelectedListener
{
    private static final int TAB_ALBUMS = 1;
    private static final int TAB_ARTISTS = 0;
    private static final int TAB_TRACKS = 2;
    private static final String TAG;
    ImageView mAlbumIconImage;
    TextView mAlbumLabel;
    ImageView mArtistArrow;
    ImageView mArtistIconImage;
    TextView mArtistLabel;
    SingleSelectionCursorAdapter mCurrentAlbumAdapter;
    SingleSelectionCursorAdapter mCurrentArtistAdapter;
    SingleSelectionCursorAdapter mCurrentTrackAdapter;
    MusicSelection mMusicSelection;
    View mMusicSelectionIndicator;
    MusicPagerAdapter mPageAdapter;
    MusicSelection mPageSelection;
    int mPagesCount;
    TabHost mTabHost;
    ViewPager mViewPager;
    
    static {
        TAG = LibraryMusicSelectorFragment.class.getSimpleName();
    }
    
    public LibraryMusicSelectorFragment() {
        this.mPageSelection = new MusicSelection();
        this.mPagesCount = 3;
    }
    
    private void AddTab(final TabHost tabHost, final TabHost$TabSpec tabHost$TabSpec) {
        tabHost$TabSpec.setContent((TabHost$TabContentFactory)this);
        tabHost.addTab(tabHost$TabSpec);
    }
    
    private void initialiseTabHost() {
        this.mTabHost.setup();
        this.AddTab(this.mTabHost, this.mTabHost.newTabSpec("Artist").setIndicator((CharSequence)this.getString(2131165211)));
        ((TextView)this.mTabHost.getTabWidget().getChildAt(0).findViewById(16908310)).setTextSize(13.0f);
        this.AddTab(this.mTabHost, this.mTabHost.newTabSpec("Album").setIndicator((CharSequence)this.getString(2131165210)));
        ((TextView)this.mTabHost.getTabWidget().getChildAt(1).findViewById(16908310)).setTextSize(13.0f);
        this.AddTab(this.mTabHost, this.mTabHost.newTabSpec("Track").setIndicator((CharSequence)this.getString(2131165222)));
        ((TextView)this.mTabHost.getTabWidget().getChildAt(2).findViewById(16908310)).setTextSize(13.0f);
        for (int i = 0; i < this.mTabHost.getTabWidget().getChildCount(); ++i) {
            ((TextView)this.mTabHost.getTabWidget().getChildAt(i).findViewById(16908310)).setTag((Object)"#text");
        }
        this.mTabHost.setOnTabChangedListener((TabHost$OnTabChangeListener)this);
        this.mTabHost.getTabWidget().getChildAt(0).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                LibraryMusicSelectorFragment.this.setAllArtistsList();
                LibraryMusicSelectorFragment.this.mTabHost.setCurrentTab(0);
            }
        });
        this.mTabHost.getTabWidget().getChildAt(1).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                LibraryMusicSelectorFragment.this.setAllAlbumsList();
                LibraryMusicSelectorFragment.this.mTabHost.setCurrentTab(1);
            }
        });
        this.mTabHost.getTabWidget().getChildAt(2).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                LibraryMusicSelectorFragment.this.mTabHost.setCurrentTab(2);
            }
        });
    }
    
    private void setAllAlbumsList() {
        if (this.mTabHost.getCurrentTab() == 1) {
            if (this.mPageSelection != null) {
                this.mPageSelection.artistName = null;
                this.mPageSelection.artistKey = null;
                this.mPageSelection.albumName = null;
                this.mPageSelection.albumKey = null;
                this.mPageSelection.titleName = null;
                this.mPageSelection.titleKey = null;
            }
            else {
                this.mPageSelection = new MusicSelection();
            }
            final Cursor swapCursor = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(1)).swapCursor(this.getAllSongsCursor(this.mPageSelection.albumKey, this.mPageSelection.artistKey));
            if (swapCursor != null) {
                swapCursor.close();
            }
            this.mPageAdapter.notifyDataSetChanged();
            this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
            this.mViewPager.setCurrentItem(0);
        }
    }
    
    private void setAllArtistsList() {
        if (this.mTabHost.getCurrentTab() == 0) {
            if (this.mPageSelection != null) {
                this.mPageSelection.artistName = null;
                this.mPageSelection.artistKey = null;
                this.mPageSelection.albumName = null;
                this.mPageSelection.albumKey = null;
                this.mPageSelection.titleName = null;
                this.mPageSelection.titleKey = null;
            }
            else {
                this.mPageSelection = new MusicSelection();
            }
            final Cursor swapCursor = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(1)).swapCursor(this.getAlbumsCursor(this.mPageSelection.artistName));
            if (swapCursor != null) {
                swapCursor.close();
            }
            final Cursor swapCursor2 = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(2)).swapCursor(this.getAllSongsCursor(this.mPageSelection.albumKey, this.mPageSelection.artistKey));
            if (swapCursor2 != null) {
                swapCursor2.close();
            }
            this.mPageAdapter.notifyDataSetChanged();
            this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
            this.mViewPager.setCurrentItem(0);
        }
    }
    
    public View createTabContent(final String s) {
        final View view = new View((Context)this.getActivity());
        view.setMinimumWidth(0);
        view.setMinimumHeight(0);
        return view;
    }
    
    public Cursor getAlbumsCursor(final String s) {
        String s2 = null;
        String[] array = null;
        if (s != null) {
            s2 = "artist=?";
            array = new String[] { s };
        }
        return this.getActivity().getContentResolver().query(MediaStore$Audio$Albums.EXTERNAL_CONTENT_URI, new String[] { "album_key", "album", "artist", "numsongs", "_id" }, s2, array, "album ASC");
    }
    
    public Cursor getAllArtistCursor() {
        return this.getActivity().getContentResolver().query(MediaStore$Audio$Artists.EXTERNAL_CONTENT_URI, new String[] { "artist_key", "artist", "number_of_albums", "number_of_tracks", "_id" }, (String)null, (String[])null, "artist ASC");
    }
    
    public Cursor getAllSongsCursor(final String s, String string) {
        String string2 = "is_music != 0";
        if (string != null) {
            string2 = "is_music != 0" + " AND artist_key= '" + string.replace("'", "''") + "'";
        }
        string = string2;
        if (s != null) {
            string = string2 + " AND album_key='" + s.replace("'", "''") + "'";
        }
        return this.getActivity().getContentResolver().query(MediaStore$Audio$Media.EXTERNAL_CONTENT_URI, new String[] { "title_key", "title", "_data", "album", "album_key", "artist", "artist_key", "duration", "_id" }, string, (String[])null, "title ASC");
    }
    
    public SingleSelectionCursorAdapter getSimpleCursorAdapterAlbums(final String s) {
        final SingleSelectionCursorAdapter singleSelectionCursorAdapter = new SingleSelectionCursorAdapter((Context)this.getActivity(), this.getAlbumsCursor(s), new String[] { "album" }, new int[] { 16908308 }, 2130837629, false);
        singleSelectionCursorAdapter.setOnItemSelectedListener(this);
        return singleSelectionCursorAdapter;
    }
    
    public SingleSelectionCursorAdapter getSimpleCursorAdapterArtists() {
        final SingleSelectionCursorAdapter singleSelectionCursorAdapter = new SingleSelectionCursorAdapter((Context)this.getActivity(), this.getAllArtistCursor(), new String[] { "artist" }, new int[] { 16908308 }, 2130837630, false);
        singleSelectionCursorAdapter.setOnItemSelectedListener(this);
        return singleSelectionCursorAdapter;
    }
    
    public SingleSelectionCursorAdapter getSimpleCursorAdapterTracks(final String s, final String s2) {
        final SingleSelectionCursorAdapter singleSelectionCursorAdapter = new SingleSelectionCursorAdapter((Context)this.getActivity(), this.getAllSongsCursor(s, s2), new String[] { "title" }, new int[] { 16908308 }, 2130837649, true);
        singleSelectionCursorAdapter.setOnItemSelectedListener(this);
        return singleSelectionCursorAdapter;
    }
    
    @Override
    public String getTitle() {
        return App.getInstance().getString(2131165216);
    }
    
    @Override
    public boolean onBack() {
        boolean b = false;
        final int currentItem = this.mViewPager.getCurrentItem();
        if (currentItem > 0) {
            this.mViewPager.setCurrentItem(currentItem - 1);
        }
        else {
            if (this.mMusicSelection == null || this.mMusicSelection.isEmpty()) {
                Toast.makeText((Context)this.getActivity(), 2131165359, 0).show();
            }
            b = true;
        }
        return b;
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(2130968636, viewGroup, false);
    }
    
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        final int currentTab = this.mTabHost.getCurrentTab();
        final int currentItem = this.mViewPager.getCurrentItem();
        Label_0044: {
            switch (currentTab) {
                case 0: {
                    switch (currentItem) {
                        default: {
                            break Label_0044;
                        }
                        case 0: {
                            final CursorWrapper cursorWrapper = (CursorWrapper)((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(0)).getItem(n);
                            this.mPageSelection.artistKey = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist_key"));
                            this.mPageSelection.artistName = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist"));
                            final Cursor swapCursor = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(1)).swapCursor(this.getAlbumsCursor(this.mPageSelection.artistName));
                            if (swapCursor != null) {
                                swapCursor.close();
                            }
                            this.mPageAdapter.notifyDataSetChanged();
                            this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
                            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1);
                            break Label_0044;
                        }
                        case 1: {
                            final CursorWrapper cursorWrapper2 = (CursorWrapper)((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(1)).getItem(n);
                            this.mPageSelection.albumKey = cursorWrapper2.getString(cursorWrapper2.getColumnIndex("album_key"));
                            this.mPageSelection.albumName = cursorWrapper2.getString(cursorWrapper2.getColumnIndex("album"));
                            final Cursor swapCursor2 = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(2)).swapCursor(this.getAllSongsCursor(this.mPageSelection.albumKey, this.mPageSelection.artistKey));
                            if (swapCursor2 != null) {
                                swapCursor2.close();
                            }
                            this.mPageAdapter.notifyDataSetChanged();
                            this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
                            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1);
                            break Label_0044;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (currentItem) {
                        default: {
                            break Label_0044;
                        }
                        case 0: {
                            final CursorWrapper cursorWrapper3 = (CursorWrapper)((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(0)).getItem(n);
                            this.mPageSelection.albumKey = cursorWrapper3.getString(cursorWrapper3.getColumnIndex("album_key"));
                            this.mPageSelection.albumName = cursorWrapper3.getString(cursorWrapper3.getColumnIndex("album"));
                            final Cursor swapCursor3 = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(1)).swapCursor(this.getAllSongsCursor(this.mPageSelection.albumKey, this.mPageSelection.artistKey));
                            if (swapCursor3 != null) {
                                swapCursor3.close();
                            }
                            this.mPageAdapter.notifyDataSetChanged();
                            this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
                            this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1);
                            break Label_0044;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public void onItemSelected(final SingleSelectionCursorAdapter singleSelectionCursorAdapter, final int n) {
        final CursorWrapper cursorWrapper = (CursorWrapper)singleSelectionCursorAdapter.getItem(n);
        final MusicSelection musicSelection = new MusicSelection();
        if (singleSelectionCursorAdapter == this.mCurrentArtistAdapter) {
            musicSelection.artistKey = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist_key"));
            musicSelection.artistName = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist"));
        }
        else if (singleSelectionCursorAdapter == this.mCurrentAlbumAdapter) {
            musicSelection.albumKey = cursorWrapper.getString(cursorWrapper.getColumnIndex("album_key"));
            musicSelection.albumName = cursorWrapper.getString(cursorWrapper.getColumnIndex("album"));
            musicSelection.artistName = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist"));
        }
        else if (singleSelectionCursorAdapter == this.mCurrentTrackAdapter) {
            musicSelection.albumKey = cursorWrapper.getString(cursorWrapper.getColumnIndex("album_key"));
            musicSelection.albumName = cursorWrapper.getString(cursorWrapper.getColumnIndex("album"));
            musicSelection.artistName = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist"));
            musicSelection.artistKey = cursorWrapper.getString(cursorWrapper.getColumnIndex("artist_key"));
            musicSelection.titleName = cursorWrapper.getString(cursorWrapper.getColumnIndex("title"));
            musicSelection.titleKey = cursorWrapper.getString(cursorWrapper.getColumnIndex("title_key"));
        }
        this.selectMusic(musicSelection);
    }
    
    public void onPageScrollStateChanged(final int n) {
    }
    
    public void onPageScrolled(final int n, final float n2, final int n3) {
    }
    
    public void onPageSelected(final int n) {
        Label_0036: {
            if (this.mPageSelection != null) {
                switch (this.mTabHost.getCurrentTab()) {
                    case 0: {
                        switch (n) {
                            default: {
                                break Label_0036;
                            }
                            case 0: {
                                this.setAllArtistsList();
                                break Label_0036;
                            }
                            case 1: {
                                this.mPageSelection.albumName = null;
                                this.mPageSelection.albumKey = null;
                                this.mPageSelection.titleName = null;
                                this.mPageSelection.titleKey = null;
                                final Cursor swapCursor = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(2)).swapCursor(this.getAllSongsCursor(this.mPageSelection.albumKey, this.mPageSelection.artistKey));
                                if (swapCursor != null) {
                                    swapCursor.close();
                                }
                                this.mPageAdapter.notifyDataSetChanged();
                                this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
                                break Label_0036;
                            }
                            case 2: {
                                this.mPageSelection.titleName = null;
                                this.mPageSelection.titleKey = null;
                                break Label_0036;
                            }
                        }
                        break;
                    }
                    case 1: {
                        if (n == 0) {
                            this.mPageSelection.albumName = null;
                            this.mPageSelection.albumKey = null;
                            this.mPageSelection.titleName = null;
                            this.mPageSelection.titleKey = null;
                            final Cursor swapCursor2 = ((SingleSelectionCursorAdapter)this.mPageAdapter.getAdapterList().get(1)).swapCursor(this.getAllSongsCursor(this.mPageSelection.albumKey, this.mPageSelection.artistKey));
                            if (swapCursor2 != null) {
                                swapCursor2.close();
                            }
                            this.mPageAdapter.notifyDataSetChanged();
                            this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
                            break;
                        }
                        break;
                    }
                }
            }
            else {
                this.mPageSelection = new MusicSelection();
            }
        }
        this.updateMusicIndicator(this.mPageSelection.artistName, this.mPageSelection.albumName);
    }
    
    public void onRequestPermissionsResult(final int n, @NonNull final String[] array, @NonNull final int[] array2) {
        switch (n) {
            case 2: {
                if (array2[0] == 0) {
                    this.updatePages(this.mMusicSelection);
                    break;
                }
                break;
            }
        }
    }
    
    public void onResume() {
        super.onResume();
        this.updatePages(this.mPageSelection);
        this.updateMusicIndicator(null, null);
        Log.d(LibraryMusicSelectorFragment.TAG, "Start MusicSelector fragment");
    }
    
    public void onTabChanged(final String s) {
        this.mPagesCount = 3 - this.mTabHost.getCurrentTab();
        this.mPageAdapter = new MusicPagerAdapter((AdapterView$OnItemClickListener)this);
        this.mViewPager.setAdapter(this.mPageAdapter);
        this.mViewPager.setCurrentItem(0);
        this.updatePages(null);
        this.updateMusicIndicator(null, null);
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mTabHost = (TabHost)view.findViewById(16908306);
        this.mViewPager = (ViewPager)view.findViewById(2131624175);
        this.mPageAdapter = new MusicPagerAdapter((AdapterView$OnItemClickListener)this);
        this.mViewPager.setOffscreenPageLimit(2);
        this.mViewPager.setAdapter(this.mPageAdapter);
        this.mViewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)this);
        this.mMusicSelectionIndicator = view.findViewById(2131624174);
        this.mArtistIconImage = (ImageView)this.mMusicSelectionIndicator.findViewById(2131624204);
        this.mArtistLabel = (TextView)this.mMusicSelectionIndicator.findViewById(2131624205);
        this.mArtistArrow = (ImageView)this.mMusicSelectionIndicator.findViewById(2131624206);
        this.mAlbumIconImage = (ImageView)this.mMusicSelectionIndicator.findViewById(2131624207);
        this.mAlbumLabel = (TextView)this.mMusicSelectionIndicator.findViewById(2131624208);
        this.initialiseTabHost();
    }
    
    public void selectMusic(final MusicSelection mMusicSelection) {
        this.mMusicSelection = mMusicSelection;
        final StringBuilder sb = new StringBuilder();
        if (mMusicSelection.titleName != null) {
            sb.append(mMusicSelection.titleName);
        }
        if (mMusicSelection.artistName != null) {
            if (sb.length() != 0) {
                sb.append(" - ");
            }
            sb.append(mMusicSelection.artistName);
        }
        if (mMusicSelection.albumName != null) {
            if (sb.length() != 0) {
                sb.append(" - ");
            }
            sb.append(mMusicSelection.albumName);
        }
        AlarmSettings.setMusicSelection(this.mMusicSelection);
        AlarmMusicType musicType;
        if (this.mMusicSelection.titleKey != null) {
            musicType = AlarmMusicType.SINGLE_SOUND;
        }
        else {
            musicType = AlarmMusicType.MULTI_SOUND;
        }
        AlarmSettings.setMusicType(musicType);
    }
    
    void updateMusicIndicator(final String text, final String text2) {
        if (text == null && text2 == null) {
            this.mMusicSelectionIndicator.setVisibility(8);
        }
        else {
            this.mMusicSelectionIndicator.setVisibility(0);
            if (text != null) {
                this.mArtistIconImage.setVisibility(0);
                this.mArtistLabel.setVisibility(0);
                this.mArtistLabel.setText((CharSequence)text);
            }
            else {
                this.mArtistIconImage.setVisibility(8);
                this.mArtistLabel.setVisibility(8);
            }
            if (text2 != null) {
                this.mAlbumIconImage.setVisibility(0);
                this.mAlbumLabel.setVisibility(0);
                this.mAlbumLabel.setText((CharSequence)text2);
            }
            else {
                this.mAlbumIconImage.setVisibility(8);
                this.mAlbumLabel.setVisibility(8);
            }
            if (text != null && text2 != null) {
                this.mArtistArrow.setVisibility(0);
            }
            else {
                this.mArtistArrow.setVisibility(8);
            }
        }
    }
    
    public void updatePages(final MusicSelection musicSelection) {
        MusicSelection mPageSelection = musicSelection;
        if (musicSelection == null) {
            mPageSelection = new MusicSelection();
        }
        this.mPageSelection = mPageSelection;
        if (!Utils.isReadExternalStoragePermissionGranted()) {
            this.mCurrentArtistAdapter = null;
            this.mCurrentAlbumAdapter = null;
            this.mCurrentTrackAdapter = null;
            this.mPageAdapter.getAdapterList().clear();
            this.mPageAdapter.getAdapterList().add(this.mCurrentArtistAdapter);
            this.mPageAdapter.getAdapterList().add(this.mCurrentAlbumAdapter);
            this.mPageAdapter.getAdapterList().add(this.mCurrentTrackAdapter);
            this.mPageAdapter.notifyDataSetChanged();
        }
        else if (this.mPagesCount == 3) {
            this.mCurrentArtistAdapter = this.getSimpleCursorAdapterArtists();
            this.mCurrentAlbumAdapter = this.getSimpleCursorAdapterAlbums(mPageSelection.artistName);
            this.mCurrentTrackAdapter = this.getSimpleCursorAdapterTracks(mPageSelection.albumKey, mPageSelection.artistKey);
            this.mPageAdapter.getAdapterList().clear();
            this.mPageAdapter.getAdapterList().add(this.mCurrentArtistAdapter);
            this.mPageAdapter.getAdapterList().add(this.mCurrentAlbumAdapter);
            this.mPageAdapter.getAdapterList().add(this.mCurrentTrackAdapter);
            this.mPageAdapter.notifyDataSetChanged();
        }
        else if (this.mPagesCount == 2) {
            this.mCurrentAlbumAdapter = this.getSimpleCursorAdapterAlbums(mPageSelection.artistName);
            this.mCurrentTrackAdapter = this.getSimpleCursorAdapterTracks(mPageSelection.albumKey, mPageSelection.artistKey);
            this.mPageAdapter.getAdapterList().clear();
            this.mPageAdapter.getAdapterList().add(this.mCurrentAlbumAdapter);
            this.mPageAdapter.getAdapterList().add(this.mCurrentTrackAdapter);
            this.mPageAdapter.notifyDataSetChanged();
        }
        else if (this.mPagesCount == 1) {
            this.mCurrentTrackAdapter = this.getSimpleCursorAdapterTracks(mPageSelection.albumKey, mPageSelection.artistKey);
            this.mPageAdapter.getAdapterList().clear();
            this.mPageAdapter.getAdapterList().add(this.mCurrentTrackAdapter);
            this.mPageAdapter.notifyDataSetChanged();
        }
    }
    
    public class MusicPagerAdapter extends PagerAdapter
    {
        AdapterView$OnItemClickListener itemClickListener;
        final ArrayList<SingleSelectionCursorAdapter> mAdapters;
        
        public MusicPagerAdapter(final AdapterView$OnItemClickListener itemClickListener) {
            this.mAdapters = new ArrayList<SingleSelectionCursorAdapter>();
            this.itemClickListener = itemClickListener;
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        public ArrayList<SingleSelectionCursorAdapter> getAdapterList() {
            return this.mAdapters;
        }
        
        @Override
        public int getCount() {
            int size;
            if (this.mAdapters == null) {
                size = 0;
            }
            else {
                size = this.mAdapters.size();
            }
            return size;
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int index) {
            final ListView listView = (ListView)LibraryMusicSelectorFragment.this.getActivity().getLayoutInflater().inflate(2130968653, (ViewGroup)null);
            listView.setAdapter((ListAdapter)this.mAdapters.get(index));
            listView.setOnItemClickListener(this.itemClickListener);
            viewGroup.addView((View)listView, 0);
            return listView;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
    }
    
    public class SingleSelectionCursorAdapter extends SimpleCursorAdapter
    {
        private int checkedPosition;
        private RadioButton mCurrentSelectedItemRadioButton;
        private final int mIconResource;
        private OnItemSelectedListener mOnItemSelectedListener;
        private final boolean mWithAutoSelection;
        
        public SingleSelectionCursorAdapter(final Context context, final Cursor cursor, final String[] array, final int[] array2, final int mIconResource, final boolean mWithAutoSelection) {
            super(context, 2130968655, cursor, array, array2);
            this.checkedPosition = -1;
            this.mIconResource = mIconResource;
            this.mWithAutoSelection = mWithAutoSelection;
        }
        
        public OnItemSelectedListener getOnItemSelectedListener() {
            return this.mOnItemSelectedListener;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            final View view2 = super.getView(n, view, viewGroup);
            ((ImageView)view2.findViewById(16908294)).setImageResource(this.mIconResource);
            final RadioButton radioButton = (RadioButton)view2.findViewById(16908289);
            if (this.mWithAutoSelection) {
                view2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        radioButton.setChecked(true);
                    }
                });
            }
            radioButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new CompoundButton$OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                    if (b) {
                        SingleSelectionCursorAdapter.this.checkedPosition = n;
                        if (SingleSelectionCursorAdapter.this.getOnItemSelectedListener() != null) {
                            SingleSelectionCursorAdapter.this.getOnItemSelectedListener().onItemSelected(SingleSelectionCursorAdapter.this, n);
                        }
                    }
                    SingleSelectionCursorAdapter.this.notifyDataSetChanged();
                }
            });
            radioButton.setChecked(this.checkedPosition == n);
            return view2;
        }
        
        public void setOnItemSelectedListener(final OnItemSelectedListener mOnItemSelectedListener) {
            this.mOnItemSelectedListener = mOnItemSelectedListener;
        }
    }
}
