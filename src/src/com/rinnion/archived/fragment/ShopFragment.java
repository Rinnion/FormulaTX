package com.rinnion.archived.fragment;

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
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.cursor.ProductCursor;
import com.rinnion.archived.database.cursor.TournamentCursor;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.fragment.adapter.NewsAdapter;
import com.rinnion.archived.fragment.adapter.ProductAdapter;
import com.rinnion.archived.network.loaders.ProductAsyncLoader;
import com.rinnion.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class ShopFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ProductCursor> {

    public static String TOURNAMENT_POST_NAME = "tournament id";
    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        mAdapter = new ProductAdapter(getActivity(), null);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_news);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
        super.onResume();
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
    public Loader<ProductCursor> onCreateLoader(int id, Bundle args) {
        return new ProductAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ProductCursor> loader, ProductCursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<ProductCursor> loader) {

    }
}

