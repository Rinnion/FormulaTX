package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import com.formulatx.archived.network.loaders.PodcastAsyncLoader;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.GalleryDescriptionCursor;
import com.formulatx.archived.database.helper.GalleryHelper;
import com.formulatx.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class PodcastFragment extends ListFragment {

    public static String TOURNAMENT_POST_NAME = "tournament id";
    public static String PODCAST = "podcast";
    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        String[] names = new String[]{GalleryHelper.COLUMN_GALLERY_DESCRIPTION_TITLE};
        int[] to = new int[]{R.id.itl_tv_text};

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_text_layout, null, names, to, 0);

        setListAdapter(mAdapter);
        getLoaderManager().initLoader(R.id.gallery_loader, Bundle.EMPTY, new PodcastLoaderCallback());

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_radio);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PodcastContentFragment pcf = new PodcastContentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(PodcastContentFragment.GALLERY, id);
        pcf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, pcf)
                .addToBackStack(null)
                .commit();
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

    private class PodcastLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryDescriptionCursor> {
        @Override
        public Loader<GalleryDescriptionCursor> onCreateLoader(int id, Bundle args) {
            return new PodcastAsyncLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<GalleryDescriptionCursor> loader, GalleryDescriptionCursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryDescriptionCursor> loader) {

        }
    }
}

