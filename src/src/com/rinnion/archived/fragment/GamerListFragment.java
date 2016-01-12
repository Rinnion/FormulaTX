package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ResourceCursorAdapter;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.GamerCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.ApiObject;
import com.rinnion.archived.fragment.adapter.GamerAdapter;
import com.rinnion.archived.network.loaders.GamerAsyncLoader;
import com.rinnion.archived.utils.Log;

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

        mAdapter = new GamerAdapter(getActivity(), null);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.message_loader, getArguments(), this);

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

