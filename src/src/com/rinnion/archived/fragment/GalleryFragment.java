
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.network.loaders.GalleryAsyncLoader;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GalleryFragment extends Fragment {

    private static final int PHOTO_LOADER = 1;
    private static final int VIDEO_LOADER = 2;
    private String TAG = getClass().getCanonicalName();
    private WebView mTextViewAbout;
    private SimpleCursorAdapter mAdapter;

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
        tabSpec = tabHost.newTabSpec("photo");
        tabSpec.setIndicator("Фото");
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("video");
        tabSpec.setIndicator("Видео");
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        // вторая вкладка будет выбрана по умолчанию
        tabHost.setCurrentTabByTag("photo");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("photo")){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(PHOTO_LOADER, bundle, new PhotoLoaderCallback());
                }
                if (tabId.equals("video")){
                    Bundle bundle = new Bundle();
                    getLoaderManager().initLoader(VIDEO_LOADER, bundle, new VideoLoaderCallback());
                }
            }
        });


        MatrixCursor mc = new MatrixCursor(GalleryHelper.COLS);
        mc.addRow(new Object[]{1, R.drawable.general_bg});
        mc.addRow(new Object[]{2, R.drawable.st_lady_bg});
        mc.addRow(new Object[]{3, R.drawable.st_open_bg});

        GridView gv = (GridView) tabHost.findViewById(R.id.gtl_gv_photo);

        String[] names = new String[]{GalleryHelper.COLUMN_URL};
        int[] to = new int[] {R.id.il_iv_image};

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.image_layout, mc, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                v.setImageResource(R.drawable.logo_splash_screen);
            }
        };

        gv.setAdapter(mAdapter);



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


    private class PhotoLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            new GalleryAsyncLoader(getActivity(), 205, "picture");
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }

    private class VideoLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<Object> {
        @Override
        public Loader<Object> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Object> loader, Object data) {

        }

        @Override
        public void onLoaderReset(Loader<Object> loader) {

        }
    }
}

