package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.GalleryDescriptionCursor;
import com.rinnion.archived.database.cursor.GalleryItemCursor;
import com.rinnion.archived.database.helper.GalleryHelper;
import com.rinnion.archived.database.model.Comment;
import com.rinnion.archived.database.model.GalleryItem;
import com.rinnion.archived.network.loaders.GalleryContentAsyncLoader;
import com.rinnion.archived.network.loaders.PodcastAsyncLoader;
import com.rinnion.archived.utils.Log;

import java.io.File;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class PodcastContentFragment extends Fragment {

    public static String TOURNAMENT_POST_NAME = "tournament id";
    public static String PODCAST = "podcast";
    public static final String GALLERY = GalleryContentAsyncLoader.GALLERY;
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
        String[] names = new String[]{GalleryHelper._ID};
        int[] to = new int[]{R.id.itl_tv_text};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_text_layout, null, names, to, 0);
        getLoaderManager().initLoader(R.id.gallery_loader, getArguments(), new PodcastLoaderCallback());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
            Bundle args = getArguments();
            if (args == null) throw new IllegalStateException("No arguments");
            long gid = getArguments().getLong(GALLERY);
            GalleryHelper gh=new GalleryHelper(doh);
            GalleryDescriptionCursor.GalleryDescription gallery = gh.getGallery(gid);
            ab.setTitle(gallery.title);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.list_layout, container, false);
        view.setAdapter(mAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseOpenHelper doh = ArchivedApplication.getDatabaseOpenHelper();
                GalleryHelper gh=new GalleryHelper(doh);
                GalleryItem item = gh.getItem(l);
                if (item == null) return;

//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                URI uri = URI.create(item.link);
//                File file = new File(uri);
//                intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(item.link));
                startActivity(intent);
            }
        });

        return view;
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

    private class PodcastLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<GalleryItemCursor> {
        @Override
        public Loader<GalleryItemCursor> onCreateLoader(int id, Bundle args) {
            return new GalleryContentAsyncLoader(getActivity(), args, GalleryHelper.TYPE_AUDIO);
        }

        @Override
        public void onLoadFinished(Loader<GalleryItemCursor> loader, GalleryItemCursor data) {
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<GalleryItemCursor> loader) {

        }
    }
}

