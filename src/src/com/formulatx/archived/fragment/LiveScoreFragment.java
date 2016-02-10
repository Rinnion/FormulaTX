
package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.fragment.adapter.ScheduleAdapter;
import com.formulatx.archived.network.loaders.LiveAsyncLoader;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class LiveScoreFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private TabHost mTabHost;
    private ScheduleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MatchCursor mSchedule;
    private View mProgresView;
    private View mEmptyView;
    private View mErrorView;

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
        mProgresView = view.findViewById(R.id.progressView);
        mEmptyView = view.findViewById(R.id.emptyView);
        TextView tvEmpty = (TextView) view.findViewById(R.id.emptyViewText);
        tvEmpty.setText(R.string.string_no_matches);
        mErrorView = view.findViewById(R.id.errorView);
        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        ListView mListView = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);
        mAdapter = new ScheduleAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.tables_loader, Bundle.EMPTY, new LiveScoreLoaderCallBack());

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getLoaderManager().restartLoader(R.id.tables_loader, Bundle.EMPTY, new LiveScoreLoaderCallBack());
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_tournament_liveScore);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

    private class LiveScoreLoaderCallBack implements android.app.LoaderManager.LoaderCallbacks<MatchCursor> {
        @Override
        public Loader<MatchCursor> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            return new LiveAsyncLoader(getActivity(), getArguments().getString(TOURNAMENT_POST_NAME));
        }

        @Override
        public void onLoadFinished(Loader<MatchCursor> loader, MatchCursor data) {
            Log.d(TAG, "onLoadFinished");
            mSchedule = data;
            UpdateSchedule();
        }

        @Override
        public void onLoaderReset(Loader<MatchCursor> loader) {
            mSchedule = null;
            UpdateSchedule();
        }
    }

    private void UpdateSchedule() {
        if (mSchedule == null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mAdapter.swapCursor(mSchedule);
            mSwipeRefreshLayout.setRefreshing(false);
            if (mSchedule.getCount() == 0){
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }

    }

}

