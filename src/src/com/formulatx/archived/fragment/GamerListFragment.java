package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import com.parse.ParsePush;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.GamerCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.GamerHelper;
import com.formulatx.archived.database.model.ApiObjects.Gamer;
import com.formulatx.archived.fragment.adapter.GamerAdapter;
import com.formulatx.archived.network.loaders.GamerAsyncLoader;
import com.formulatx.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class GamerListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<GamerCursor> {

    public static final String TYPE = "type";
    public static final String TOURNAMENT_ID = ApiObjectHelper._ID;
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

        mAdapter = new GamerAdapter(getActivity(), null, null);


        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.gamer_loader, getArguments(), this);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_gamers);
        ab.setIcon(R.drawable.ic_action_previous_item);

        super.onCreate(savedInstanceState);
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        GamerHelper gh = new GamerHelper();

        Gamer gamer = gh.getGamer(id);

        Log.d(TAG, String.valueOf(gamer.id));

        String channel = "gamer-" + String.valueOf(gamer.id);
        if (!gamer.favorite) {
            Log.d(TAG, "Subscribe to '" + channel + "'");
            ParsePush.subscribeInBackground(channel);
        }else{
            Log.d(TAG, "Unsubscribe to '" + channel + "'");
            ParsePush.unsubscribeInBackground(channel);
        }

        gamer.favorite = !gamer.favorite;
        gh.merge(gamer);
        long type = getArguments().getLong(TOURNAMENT_ID);
        GamerCursor allByParent = gh.getAllByParent(type);
        mAdapter.swapCursor(allByParent);

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public Loader<GamerCursor> onCreateLoader(int id, Bundle args) {
        long type = args.getLong(TOURNAMENT_ID);
        return new GamerAsyncLoader(getActivity(), type);

    }

    @Override
    public void onLoadFinished(Loader<GamerCursor> loader, GamerCursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<GamerCursor> loader) {

    }

}

