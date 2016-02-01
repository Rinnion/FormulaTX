
package com.formulatx.archived.fragment;

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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.R;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.network.loaders.GalleryAsyncLoader;
import com.formulatx.archived.utils.Log;
import com.squareup.picasso.Picasso;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.ArrayList;
import java.util.Map;

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
    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    public static final String PHOTO = "photo";
    public static final String VIDEO = "video";
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
        tabSpec = tabHost.newTabSpec(PHOTO);
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
                    Bundle bundle = getArguments();
                    getLoaderManager().initLoader(PHOTO_LOADER, bundle, new PhotoLoaderCallback());
                }
                if (tabId.equals(VIDEO)) {
                    Bundle bundle = getArguments();
                    getLoaderManager().initLoader(VIDEO_LOADER, bundle, new VideoLoaderCallback());
                }
            }
        });

        String[] names = new String[]{GalleryHelper.COLUMN_GALLERY_DESCRIPTION_PICTURE, GalleryHelper.COLUMN_GALLERY_DESCRIPTION_TITLE};
        int[] to = new int[]{R.id.il_iv_image, R.id.il_tv_text};


        GridView gvPhoto = (GridView) tabHost.findViewById(R.id.gtl_gv_photo);
        DisplayMetrics dm = FormulaTXApplication.getAppContext().getResources().getDisplayMetrics();
        //FIXME: hardcoded values...
        Log.d(TAG, String.format("[wp:%s] [d:%s] [nc:%s]", dm.widthPixels, dm.density, 2));
        final int width = Math.abs(dm.widthPixels / 2);
        Log.i(TAG, String.valueOf(width));
        mPhotoAdapter = new SimpleCursorAdapter(getActivity(), R.layout.image_layout, null, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                try {
                    Picasso.with(getActivity())
                            .load(value)
                            .resize(width, width).centerCrop()
                            .placeholder(R.drawable.logo_splash_screen)
                            .into(v);
                }catch(Exception ignored){
                    Log.e(TAG, ignored.getMessage());
                }
            }
        };
        gvPhoto.setAdapter(mPhotoAdapter);
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GalleryContentFragment gcf = new GalleryContentFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(GalleryContentFragment.GALLERY, l);
                bundle.putString("TYPE", PHOTO);
                gcf.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                        .replace(R.id.fragment_container, gcf)
                        .addToBackStack(null)
                        .commit();
            }
        });


        GridView gvVideo = (GridView) tabHost.findViewById(R.id.gtl_gv_video);

        mVideoAdapter = new SimpleCursorAdapter(getActivity(), R.layout.image_layout, null, names, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                try {
                    Picasso.with(getActivity())
                        .load(value)
                        .resize(width, width).centerCrop()
                        .placeholder(R.drawable.logo_splash_screen)
                        .into(v);
            }catch(Exception ignored){
                Log.e(TAG, ignored.getMessage());
            }
            }
        };
        gvVideo.setAdapter(mVideoAdapter);
        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GalleryContentFragment gcf = new GalleryContentFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(GalleryContentFragment.GALLERY, l);
                bundle.putString(GalleryContentFragment.TYPE, GalleryContentFragment.VIDEO);
                gcf.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                        .replace(R.id.fragment_container, gcf)
                        .addToBackStack(null)
                        .commit();
            }
        });

        tabHost.setCurrentTabByTag(PHOTO);

        Bundle bundle = getArguments();
        getLoaderManager().initLoader(PHOTO_LOADER, bundle, new PhotoLoaderCallback());

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




    private class PhotoLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryDescriptionCursor> {
        @Override
        public Loader<GalleryDescriptionCursor> onCreateLoader(int id, Bundle args) {
            int[] galleryArrayFromTournament = getGalleryArrayFromTournament(args);
            Bundle bundle = new Bundle();
            bundle.putIntArray("ints", galleryArrayFromTournament);
            return new GalleryAsyncLoader(getActivity(), bundle, GalleryHelper.TYPE_PICTURE);
        }

        private int[] getGalleryArrayFromTournament(Bundle args) {
            if (args == null) return null;
            String post_name = args.getString(TOURNAMENT_POST_NAME);
            DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
            TournamentHelper th = new TournamentHelper(doh);
            Tournament tournament = th.getByPostName(post_name);
            return Utils.getIntListFromJSONArray(tournament.gallery_include);
        }

        @Override
        public void onLoadFinished(Loader<GalleryDescriptionCursor> loader, GalleryDescriptionCursor data) {
            mPhotoAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryDescriptionCursor> loader) {

        }
    }

    private class VideoLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryDescriptionCursor> {
        @Override
        public Loader<GalleryDescriptionCursor> onCreateLoader(int id, Bundle args) {
            int[] galleryArrayFromTournament = getGalleryArrayFromTournament(args);
            Bundle bundle = new Bundle();
            bundle.putIntArray("ints", galleryArrayFromTournament);
            return new GalleryAsyncLoader(getActivity(), bundle, GalleryHelper.TYPE_VIDEO);
        }
        private int[] getGalleryArrayFromTournament(Bundle args) {
            if (args == null) return null;
            String post_name = args.getString(TOURNAMENT_POST_NAME);
            DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
            TournamentHelper th = new TournamentHelper(doh);
            Tournament tournament = th.getByPostName(post_name);
            ArrayList<Long> intArray = new ArrayList<Long>();
            if (tournament != null) {
                try {
                    String gallery_include = tournament.gallery_include;
                    SerializedPhpParser php = new SerializedPhpParser(gallery_include);
                    Map parse = (Map) php.parse();
                    for (Object item : parse.keySet()) {
                        long l = Long.parseLong(parse.get(item).toString());
                        intArray.add(l);
                    }
                } catch (Exception ignored) {
                    Log.w(TAG, ignored.getMessage());
                }
            }

            int[] ret = new int[intArray.size()];
            for (int i = 0; i < intArray.size(); i++) {
                ret[i] = intArray.get(i).intValue();
            }

            return ret;
        }

        @Override
        public void onLoadFinished(Loader<GalleryDescriptionCursor> loader, GalleryDescriptionCursor data) {
            mVideoAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryDescriptionCursor> loader) {

        }
    }
}

