
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.cursor.TwitterItemCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.TwitterHelper;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.network.loaders.GridsAsyncLoader;
import com.rinnion.archived.network.loaders.TwitterAsyncLoader;
import com.rinnion.archived.utils.Log;
import com.squareup.picasso.Picasso;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GridsFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;

    private static final int TWITTER_LOADER = 2;
    public static final String TAB_TAG_INSTAGRAM = "photo";
    public static final String TAB_TAG_TWITTER = "video";
    private String TAG = getClass().getCanonicalName();

    private SimpleCursorAdapter mTwitterAdapter;
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
                Bundle bundle = new Bundle();
                getLoaderManager().initLoader(TWITTER_LOADER, bundle, new TwitterCallback());
            }
        });

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAB_TAG_INSTAGRAM)){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(R.id.parser_loader, bundle, new TwitterCallback());
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

    private class TwitterCallback implements android.app.LoaderManager.LoaderCallbacks<ParserCursor> {
        @Override
        public Loader<ParserCursor> onCreateLoader(int id, Bundle args) {
            mViewTwitter.setRefreshing(true);
            return new GridsAsyncLoader(getActivity(), getArguments().getString(TOURNAMENT_POST_NAME), Parser.SPBOPEN_DRAWS, Parser.SPBOPEN_DRAWS_QUALIFICATION);
        }

        @Override
        public void onLoadFinished(Loader<ParserCursor> loader, ParserCursor data) {
            mTwitterAdapter.swapCursor(data);
            mViewTwitter.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<ParserCursor> loader) {

        }
    }
}

