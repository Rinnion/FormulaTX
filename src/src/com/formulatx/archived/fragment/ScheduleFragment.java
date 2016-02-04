
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
import com.formulatx.archived.fragment.adapter.ScheduleAdapter;
import com.rinnion.archived.R;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.utils.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class ScheduleFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private TabHost mTabHost;
    private ScheduleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Schedule mSchedule;

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
        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        ListView mListView = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);
        mAdapter = new ScheduleAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.tables_loader, Bundle.EMPTY, new ScheduleLoaderCallBack());

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getLoaderManager().initLoader(R.id.tables_loader, Bundle.EMPTY, new ScheduleLoaderCallBack());
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_tournament_schedule);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

    private class ScheduleLoaderCallBack implements android.app.LoaderManager.LoaderCallbacks<Schedule> {
        @Override
        public Loader<Schedule> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            return new ScheduleAsyncLoader(getActivity(), getArguments().getString(TOURNAMENT_POST_NAME));
        }

        @Override
        public void onLoadFinished(Loader<Schedule> loader, Schedule data) {
            Log.d(TAG, "onLoadFinished");
            mSchedule = data;
            UpdateSchedule();
        }

        @Override
        public void onLoaderReset(Loader<Schedule> loader) {
            mSchedule = null;
            UpdateSchedule();
        }
    }

    private void UpdateSchedule() {
        if (mSchedule == null) {
            mSwipeRefreshLayout.setRefreshing(true);
        } else {
            FitTabs(mSchedule.Corts);
            mAdapter.swapCursor(mSchedule.Corts.get(0).Cursor);
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private void FitTabs(ArrayList<Schedule.Cort> corts) {
        mTabHost.setup();
        mTabHost.clearAllTabs();
        for (Schedule.Cort cort:corts){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(cort.cortName);
            tabSpec.setIndicator(cort.cortName);
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return mSwipeRefreshLayout;
                }
            });
            mTabHost.addTab(tabSpec);
        }
    }

}

