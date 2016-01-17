
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.network.loaders.GalleryAsyncLoader;
import com.rinnion.archived.network.loaders.GalleryContentAsyncLoader;
import com.rinnion.archived.utils.Log;
import com.squareup.picasso.Picasso;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GalleryContentFragment extends Fragment {

    private static final int PHOTO_LOADER = 1;
    private static final int VIDEO_LOADER = 2;
    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    public static final String PHOTO = "photo";
    public static final String VIDEO = "video";
    public static final String TYPE = "TYPE";
    public static final String GALLERY = "GALLERY";
    private String TAG = getClass().getCanonicalName();

    private WebView mTextViewAbout;
    private SimpleCursorAdapter mPhotoAdapter;
    private SimpleCursorAdapter mVideoAdapter;
    private GalleryHelper galleryHelper;

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
        DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
        galleryHelper = new GalleryHelper(doh        );
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TabHost tabHost = (TabHost) inflater.inflate(R.layout.gallery_tabs_layout, container, false);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec(GalleryContentFragment.PHOTO);
        tabSpec.setIndicator("Фото");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(VIDEO);
        tabSpec.setIndicator("Видео");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(PHOTO)) {
                    getLoaderManager().initLoader(PHOTO_LOADER, getArguments(), new PhotoLoaderCallback());
                }
                if (tabId.equals(VIDEO)) {
                    getLoaderManager().initLoader(VIDEO_LOADER, getArguments(), new VideoLoaderCallback());
                }
            }
        });

        String[] names = new String[]{GalleryHelper.COLUMN_PICTURE};
        int[] to = new int[] {R.id.il_iv_image};


        GridView gvPhoto = (GridView) tabHost.findViewById(R.id.gtl_gv_photo);
        DisplayMetrics dm = ArchivedApplication.getAppContext().getResources().getDisplayMetrics();
        //FIXME: hardcoded values...
        Log.d(TAG, String.format("[wp:%s] [d:%s] [nc:%s]", dm.widthPixels,dm.density ,2));
        final int width = Math.abs(dm.widthPixels / 2);
        Log.i(TAG, String.valueOf(width));
        mPhotoAdapter = new SimpleCursorAdapter(getActivity(), R.layout.image_layout, null, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                Picasso.with(getActivity())
                        .load(value)
                        .resize(width,width).centerCrop()
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
                        .resize(width, width).centerCrop()
                        .placeholder(R.drawable.logo_splash_screen)
                        .into(v);
            }
        };
        gvVideo.setAdapter(mVideoAdapter);

        String type = getArguments().getString(TYPE);
        if (type != null && type.equals(VIDEO)) {
            tabHost.setCurrentTabByTag(VIDEO);
            getLoaderManager().initLoader(VIDEO_LOADER, getArguments(), new VideoLoaderCallback());

        } else {
            tabHost.setCurrentTabByTag(PHOTO);
            getLoaderManager().initLoader(PHOTO_LOADER, getArguments(), new PhotoLoaderCallback());
        }


        return tabHost;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
            Bundle args = getArguments();
            if (args == null) throw new IllegalStateException("No arguments");
            long gid = getArguments().getLong("GALLERY");
            GalleryHelper gh=new GalleryHelper(doh);
            GalleryDescriptionCursor.GalleryDescription gallery = gh.getGallery(gid);
            ab.setTitle(gallery.title);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

    private class PhotoLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return new GalleryContentAsyncLoader(getActivity(), args, GalleryHelper.TYPE_PICTURE);
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mPhotoAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }

    private class VideoLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return new GalleryContentAsyncLoader(getActivity(), args, GalleryHelper.TYPE_VIDEO);
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

