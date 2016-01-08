
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.network.loaders.GalleryAsyncLoader;
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
    private SimpleCursorAdapter mPhotoAdapter;
    private SimpleCursorAdapter mVideoAdapter;

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
        TabHost tabHost = (TabHost) inflater.inflate(R.layout.gallery_tabs_layout, container, false);
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

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("photo")){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(INSTAGRAM_LOADER, bundle, new InstagramCallback());
                }
                if (tabId.equals("video")){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(TWITTER_LOADER, bundle, new TwitterCallback());
                }
            }
        });

        tabHost.setCurrentTabByTag(TAB_TAG_TWITTER);

        String[] names = new String[]{GalleryHelper.COLUMN_PICTURE};
        int[] to = new int[] {R.id.il_iv_image};

        GridView gvPhoto = (GridView) tabHost.findViewById(R.id.gtl_gv_photo);
        mPhotoAdapter = new SimpleCursorAdapter(getActivity(), R.layout.image_layout, null, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                Picasso.with(getActivity())
                        .load(value)
                        .resize(350,350).centerCrop()
                        .placeholder(R.drawable.logo_splash_screen)
                        .into(v);
            }
        };
        gvPhoto.setAdapter(mPhotoAdapter);


        GridView gvVideo = (GridView) tabHost.findViewById(R.id.gtl_gv_video);
        mVideoAdapter = new SimpleCursorAdapter(getActivity(), R.layout.image_layout, null, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                Picasso.with(getActivity())
                        .load(value)
                        .resize(350,350).centerCrop()
                        .placeholder(R.drawable.logo_splash_screen)
                        .into(v);
            }
        };
        gvVideo.setAdapter(mVideoAdapter);


        Bundle bundle = new Bundle();
        getLoaderManager().initLoader(INSTAGRAM_LOADER, bundle, new InstagramCallback());

        return tabHost;
    }


    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_gallery);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }


    private class InstagramCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return new GalleryAsyncLoader(getActivity(), 205, GalleryHelper.TYPE_PICTURE);
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mPhotoAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }

    private class TwitterCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return new GalleryAsyncLoader(getActivity(), 205, GalleryHelper.TYPE_VIDEO);
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mVideoAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }
}

