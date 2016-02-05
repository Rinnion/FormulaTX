
package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Settings;
import com.formulatx.archived.database.helper.ParserHelper;
import com.formulatx.archived.fragment.adapter.ScheduleAdapter;
import com.formulatx.archived.network.MyNetworkContentContract;
import com.formulatx.archived.network.loaders.GridsAsyncLoader;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GridsFragment extends Fragment implements ActionBar.OnNavigationListener, TabHost.OnTabChangeListener {

    public static final String TOURNAMENT_POST_NAME = GridsAsyncLoader.TOURNAMENT_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private TabHost mTabHost;
    private ScheduleAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Grids mSchedule;
    private ArrayAdapter<String> mSpinnerAdapter;
    private View mEmptyView;

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
        mEmptyView = view.findViewById(R.id.emptyView);
        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        ListView mListView = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);
        mAdapter = new ScheduleAdapter(getActivity(), null);
        mListView.setAdapter(mAdapter);

        Bundle bundle = new Bundle();
        bundle.putString(TOURNAMENT_POST_NAME, getArguments().getString(TOURNAMENT_POST_NAME));
        bundle.putInt(GridsAsyncLoader.GRID, getOpenedType());
        getLoaderManager().initLoader(R.id.tables_loader, bundle, new GridsLoaderCallBack());

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Bundle bundle = new Bundle();
                bundle.putString(TOURNAMENT_POST_NAME, getArguments().getString(TOURNAMENT_POST_NAME));
                bundle.putInt(GridsAsyncLoader.GRID, getOpenedType());
                getLoaderManager().restartLoader(R.id.tables_loader, bundle, new GridsLoaderCallBack());
            }
        });

        ActionBar ab = getActivity().getActionBar();
        if (ab != null){
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            ab.setTitle(null);
            String[] columns = new String[]{BaseColumns._ID, ParserHelper.COLUMN_TITLE};
            MatrixCursor mc = new MatrixCursor(columns);
            mc.addRow(new Object[]{1, "Загрузка..."});
            mSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line,
                    android.R.id.text1,
                    getResources().getStringArray(R.array.grids_menu));
            ab.setListNavigationCallbacks(mSpinnerAdapter, this);
            ab.setSelectedNavigationItem(getOpenedType());
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        setOpenedType(i);
        mSwipeRefreshLayout.setRefreshing(true);
        Bundle bundle = new Bundle();
        bundle.putString(TOURNAMENT_POST_NAME, getArguments().getString(TOURNAMENT_POST_NAME));
        bundle.putInt(GridsAsyncLoader.GRID, i);
        getLoaderManager().restartLoader(R.id.tables_loader, bundle, new GridsLoaderCallBack());
        return false;
    }

    @Override
    public void onTabChanged(String s) {
        if (mSchedule != null){
            for (int i = 0; i<mSchedule.Rounds.size(); i++){
                Grids.Round round = mSchedule.Rounds.get(i);
                if (round.cortName != null && round.cortName.equals(s)) {
                    mAdapter.swapCursor(round.Cursor);
                    setOpenedPage(round.cortName);
                }
            }
        }
    }

    public void setOpenedPage(String name){
        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        String page_name = Settings.GRIDS;
        String page = "page";
        FormulaTXApplication.setParameter(post_name + page_name + page, name);
    }

    public String getOpenedPage(){
        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        String page_name = Settings.GRIDS;
        String page = "page";
        return FormulaTXApplication.getStringParameter(post_name + page_name + page);
    }

    public void setOpenedType(int type){
        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        String page_name = Settings.GRIDS;
        String page = "type";
        FormulaTXApplication.setParameter(post_name + page_name + page, type);
    }

    public int getOpenedType(){
        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        String page_name = Settings.GRIDS;
        String page = "type";
        return FormulaTXApplication.getIntParameter(post_name + page_name + page, MyNetworkContentContract.FormulaTXApi.Grids.QUALIFICATION);
    }

    private class GridsLoaderCallBack implements android.app.LoaderManager.LoaderCallbacks<Grids> {
        @Override
        public Loader<Grids> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            return new GridsAsyncLoader(getActivity(), args);
        }

        @Override
        public void onLoadFinished(Loader<Grids> loader, Grids data) {
            Log.d(TAG, "onLoadFinished");
            mSchedule = data;
            UpdateSchedule(0);
        }

        @Override
        public void onLoaderReset(Loader<Grids> loader) {
            mSchedule = null;
            UpdateSchedule(0);
        }
    }

    private void UpdateSchedule(int index) {
        if (mSchedule == null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mEmptyView.setVisibility(View.GONE);

        } else {
            mSwipeRefreshLayout.setRefreshing(false);

            FitTabs(mSchedule.Rounds);
            if (mSchedule.Rounds.size()==0) {
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mAdapter.swapCursor(mSchedule.Rounds.get(0).Cursor);
            }
        }

    }

    private void FitTabs(ArrayList<Grids.Round> rounds) {
        mTabHost.setOnTabChangedListener(null);
        mTabHost.setup();
        mTabHost.clearAllTabs();
        for (Grids.Round round:rounds){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(round.cortName);
            tabSpec.setIndicator(round.cortName);
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return mSwipeRefreshLayout;
                }
            });
            mTabHost.addTab(tabSpec);
        }
        String openedPage = getOpenedPage();
        mTabHost.setCurrentTabByTag(openedPage);
        mTabHost.setOnTabChangedListener(this);
    }

}

