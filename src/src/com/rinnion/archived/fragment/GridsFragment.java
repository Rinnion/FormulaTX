
package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.ListView;
import android.widget.TabHost;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.fragment.adapter.GridAdapter;
import com.rinnion.archived.network.loaders.ParserAsyncLoader;
import com.rinnion.archived.network.loaders.cursor.ParserDataCursor;
import com.rinnion.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GridsFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private GridAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabHost mTabHost;

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
        View view = inflater.inflate(R.layout.tabbed_refreshable_list_layout, container, false);
        mTabHost = (TabHost)view.findViewById(R.id.tabHost);


        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        ListView listView = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);

        mAdapter = new GridAdapter(getActivity(), null);

        listView.setAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.tables_loader, Bundle.EMPTY, new ParserLoaderCallback());

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getLoaderManager().initLoader(R.id.tables_loader, Bundle.EMPTY, new ParserLoaderCallback());
            }
        });



        return view;
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

    private class ParserLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<ParserDataCursor> {
        @Override
        public Loader<ParserDataCursor> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            return new ParserAsyncLoader(getActivity(), getArguments().getString(TOURNAMENT_POST_NAME), Parser.SPBOPEN_DRAWS, Parser.SPBOPEN_DRAWS_QUALIFICATION, "Финал");
        }

        @Override
        public void onLoadFinished(Loader<ParserDataCursor> loader, ParserDataCursor data) {
            Log.d(TAG, "onLoadFinished");
            mAdapter.swapCursor(data);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<ParserDataCursor> loader) {

        }
    }
}

