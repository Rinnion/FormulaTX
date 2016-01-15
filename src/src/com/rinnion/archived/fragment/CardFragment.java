package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.CardCursor;
import com.rinnion.archived.database.cursor.ProductCursor;
import com.rinnion.archived.fragment.adapter.CardAdapter;
import com.rinnion.archived.fragment.adapter.ProductAdapter;
import com.rinnion.archived.network.loaders.CardAsyncLoader;
import com.rinnion.archived.network.loaders.ProductAsyncLoader;
import com.rinnion.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class CardFragment extends ListFragment implements LoaderManager.LoaderCallbacks<CardCursor> {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new CardAdapter(getActivity(), null);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_tickets);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //TODO: open browser for card
        //super.onListItemClick(l, v, position, id);
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
    public Loader<CardCursor> onCreateLoader(int id, Bundle args) {
        return new CardAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<CardCursor> loader, CardCursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<CardCursor> loader) {

    }
}

