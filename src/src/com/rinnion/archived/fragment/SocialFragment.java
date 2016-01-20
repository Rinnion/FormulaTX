
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import com.rinnion.archived.utils.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.cursor.TwitterItemCursor;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.network.loaders.TwitterAsyncLoader;
import com.squareup.picasso.Picasso;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class SocialFragment extends Fragment {

    private static final int INSTAGRAM_LOADER = 3;
    private static final int TWITTER_LOADER = 2;
    public static final String TAB_TAG_INSTAGRAM = "photo";
    public static final String TAB_TAG_TWITTER = "video";
    private String TAG = getClass().getCanonicalName();

    private WebView mTextViewAbout;
    private SimpleCursorAdapter mTwitterAdapter;
    private SimpleCursorAdapter mInstagramAdapter;
    private SwipeRefreshLayout mViewTwitter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                getActivity().getFragmentManager().popBackStack();
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabHost tabHost = (TabHost) inflater.inflate(R.layout.social_tabs_layout, container, false);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        //tabSpec = tabHost.newTabSpec(TAB_TAG_INSTAGRAM);
        //tabSpec.setIndicator(getString(R.string.string_instagram));
        //tabSpec.setContent(R.id.stl_tab_instagram);
        //tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(TAB_TAG_TWITTER);
        tabSpec.setIndicator(getString(R.string.string_twitter));
        tabSpec.setContent(R.id.stl_tab_twitter);
        tabHost.addTab(tabSpec);
        mViewTwitter = (SwipeRefreshLayout) tabHost.findViewById(R.id.stl_tab_twitter);
        mViewTwitter.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mViewTwitter.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewTwitter.setRefreshing(true);
            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAB_TAG_INSTAGRAM)){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(INSTAGRAM_LOADER, bundle, new InstagramCallback());
                }
                if (tabId.equals(TAB_TAG_TWITTER)){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(TWITTER_LOADER, bundle, new TwitterCallback());
                }
            }
        });

        tabHost.setCurrentTabByTag(TAB_TAG_TWITTER);

        String[] names = new String[]{TwitterHelper.COLUMN_TEXT, TwitterHelper.ALIAS_API_OBJECT_TITLE, TwitterHelper.COLUMN_DATE};
        int[] to = new int[] {R.id.sitl_tv_text, R.id.sitl_tv_caption, R.id.sitl_tv_date};

        ListView lvTwitter = (ListView) tabHost.findViewById(R.id.stl_lv_twitter);
        mTwitterAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_social_twitter_layout, null, names, to, 0) {};
        lvTwitter.setAdapter(mTwitterAdapter);
        lvTwitter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "pressed", Toast.LENGTH_SHORT).show();
                //Object item = parent.getAdapter().getItem(position);
                //Log.d(TAG, "pos:" +position);
            }
        });


        ListView lvInstagram = (ListView) tabHost.findViewById(R.id.stl_lv_instagram);
        mInstagramAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_social_instagram_layout, null, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                Picasso.with(getActivity())
                        .load(value)
                        .resize(350,350).centerCrop()
                        .placeholder(R.drawable.logo_splash_screen)
                        .into(v);
            }
        };
        lvInstagram.setAdapter(mInstagramAdapter);


        Bundle bundle = new Bundle();
        getLoaderManager().initLoader(TWITTER_LOADER, bundle, new TwitterCallback());

        return tabHost;
    }


    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_social_networks);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }


    private class InstagramCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return null;
            //return new GalleryAsyncLoader(getActivity(), 205, GalleryHelper.TYPE_PICTURE);
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mInstagramAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }

    private class TwitterCallback implements android.app.LoaderManager.LoaderCallbacks<TwitterItemCursor> {
        @Override
        public Loader<TwitterItemCursor> onCreateLoader(int id, Bundle args) {
            return new TwitterAsyncLoader(getActivity(), 205);
        }

        @Override
        public void onLoadFinished(Loader<TwitterItemCursor> loader, TwitterItemCursor data) {
            mTwitterAdapter.swapCursor(data);
            mViewTwitter.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<TwitterItemCursor> loader) {

        }
    }
}

