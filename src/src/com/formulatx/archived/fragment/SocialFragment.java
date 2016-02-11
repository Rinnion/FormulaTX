
package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import com.formulatx.archived.database.cursor.InstagramItemCursor;
import com.formulatx.archived.database.helper.InstagramHelper;
import com.formulatx.archived.database.model.InstagramItem;
import com.formulatx.archived.database.model.TwitterItem;
import com.formulatx.archived.network.loaders.InstagramAsyncLoader;
import com.formulatx.archived.utils.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.GalleryItemCursor;
import com.formulatx.archived.database.cursor.TwitterItemCursor;
import com.formulatx.archived.database.helper.TwitterHelper;
import com.formulatx.archived.network.loaders.TwitterAsyncLoader;
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
    private SwipeRefreshLayout mViewInstaram;

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
        tabSpec = tabHost.newTabSpec(TAB_TAG_INSTAGRAM);
        tabSpec.setIndicator(getString(R.string.string_instagram));
        tabSpec.setContent(R.id.stl_tab_instagram);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(TAB_TAG_TWITTER);
        tabSpec.setIndicator(getString(R.string.string_twitter));
        tabSpec.setContent(R.id.stl_tab_twitter);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag(TAB_TAG_TWITTER);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAB_TAG_INSTAGRAM)){
                    mViewInstaram.setRefreshing(true);
                    getLoaderManager().restartLoader(INSTAGRAM_LOADER, Bundle.EMPTY, new InstagramCallback());
                }
                if (tabId.equals(TAB_TAG_TWITTER)){
                    mViewTwitter.setRefreshing(true);
                    getLoaderManager().restartLoader(TWITTER_LOADER, Bundle.EMPTY, new TwitterCallback());

                }
            }
        });


        mViewTwitter = (SwipeRefreshLayout) tabHost.findViewById(R.id.stl_tab_twitter);
        mViewTwitter.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mViewTwitter.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewTwitter.setRefreshing(true);
                getLoaderManager().restartLoader(TWITTER_LOADER, Bundle.EMPTY, new TwitterCallback());
            }
        });

        mViewInstaram = (SwipeRefreshLayout) tabHost.findViewById(R.id.stl_tab_instagram);
        mViewInstaram.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mViewInstaram.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewInstaram.setRefreshing(true);
                getLoaderManager().restartLoader(INSTAGRAM_LOADER, Bundle.EMPTY, new InstagramCallback());
            }
        });


        String[] names = new String[]{TwitterHelper.COLUMN_TEXT, TwitterHelper.COLUMN_DATE};
        int[] to = new int[] {R.id.sitl_tv_text, R.id.sitl_tv_date};

        ListView lvTwitter = (ListView) tabHost.findViewById(R.id.stl_lv_twitter);
        mTwitterAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_social_twitter_layout, null, names, to, 0) {};
        lvTwitter.setAdapter(mTwitterAdapter);
        lvTwitter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TwitterItem item = ((TwitterItemCursor) parent.getItemAtPosition(position)).getItem();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                startActivity(myIntent);
            }
        });


        names = new String[]{InstagramHelper.COLUMN_TEXT, InstagramHelper.COLUMN_LINK};
        to = new int[] {R.id.siil_tv_text, R.id.siil_iv_image};
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
        lvInstagram.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InstagramItem item = ((InstagramItemCursor) parent.getItemAtPosition(position)).getItem();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                startActivity(myIntent);
            }
        });


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


    private class InstagramCallback implements android.app.LoaderManager.LoaderCallbacks<InstagramItemCursor> {
        @Override
        public Loader<InstagramItemCursor> onCreateLoader(int id, Bundle args) {
            mViewInstaram.setRefreshing(true);
            return new InstagramAsyncLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<InstagramItemCursor> loader, InstagramItemCursor data) {
            mInstagramAdapter.swapCursor(data);
            mViewInstaram.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<InstagramItemCursor> loader) {

        }
    }

    private class TwitterCallback implements android.app.LoaderManager.LoaderCallbacks<TwitterItemCursor> {
        @Override
        public Loader<TwitterItemCursor> onCreateLoader(int id, Bundle args) {
            mViewTwitter.setRefreshing(true);
            return new TwitterAsyncLoader(getActivity());
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

