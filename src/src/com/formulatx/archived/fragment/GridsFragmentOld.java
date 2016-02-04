
package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.Utils;
import com.formulatx.archived.database.DatabaseOpenHelper;
import com.formulatx.archived.database.cursor.ParserCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.ParserHelper;
import com.formulatx.archived.database.helper.ParserMatchHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.database.model.Parser;
import com.formulatx.archived.database.model.Table;
import com.formulatx.archived.fragment.adapter.GridAdapter;
import com.formulatx.archived.network.loaders.ParserAsyncLoader;
import com.formulatx.archived.network.loaders.cursor.ParserDataCursor;
import com.formulatx.archived.utils.Log;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class GridsFragmentOld extends Fragment implements ActionBar.OnNavigationListener, TabHost.OnTabChangeListener {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    public static final String GRIDS_SETTINGS = "_grids_settings";
    public static final String GRIDS_PAGE = "_grids_page";
    private String TAG = getClass().getCanonicalName();
    private GridAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TabHost mTabHost;
    private SimpleCursorAdapter mSpinnerAdapter;
    private View mProgresView;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.dropdown, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabbed_refreshable_list_layout, container, false);
        mProgresView = view.findViewById(R.id.progressView);
        mEmptyView = view.findViewById(R.id.emptyView);

        mTabHost = (TabHost)view.findViewById(R.id.tabHost);
        mTabHost.setOnTabChangedListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        ListView listView = (ListView) mSwipeRefreshLayout.findViewById(R.id.listView);

        mAdapter = new GridAdapter(getActivity(), null);
        listView.setAdapter(mAdapter);
        InitLoader();

        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                RestartLoader(true);
            }
        });

        ActionBar ab = getActivity().getActionBar();
        if (ab != null){
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            String[] strings = new String[]{ParserHelper.COLUMN_TITLE};
            String[] columns = new String[]{BaseColumns._ID, ParserHelper.COLUMN_TITLE};
            int[] ints = new int[]{android.R.id.text1};
            MatrixCursor mc = new MatrixCursor(columns);
            mc.addRow(new Object[]{1, "Загрузка..."});
            mSpinnerAdapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line, null, strings, ints, 0 );
            ab.setListNavigationCallbacks(mSpinnerAdapter, this);
        }

        return view;
    }

    private void RestartLoader(boolean forced) {
        Log.d(TAG, "RestartLoader");

        Bundle bundle= new Bundle();
        //Parser.SPBOPEN_DRAWS, Parser.SPBOPEN_DRAWS_QUALIFICATION, "Первый";

        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);

        String settings = FormulaTXApplication.getStringParameter(post_name + GRIDS_SETTINGS);
        if (settings == null) settings = Parser.SPBOPEN_DRAWS_QUALIFICATION;

        String page = FormulaTXApplication.getStringParameter(post_name + GRIDS_PAGE);
        if (page == null) page = null;

        bundle.putString(ParserAsyncLoader.TYPE, Parser.SPBOPEN_DRAWS);
        bundle.putString(ParserAsyncLoader.SETTING, settings);
        bundle.putString(ParserAsyncLoader.PAGE, page);
        bundle.putBoolean(ParserAsyncLoader.FORCED, forced);

        getLoaderManager().restartLoader(R.id.tables_loader, bundle, new ParserLoaderCallback());
    }

    private void InitLoader() {
        Log.d(TAG, "InitLoader");

        Bundle bundle= new Bundle();
        //Parser.SPBOPEN_DRAWS, Parser.SPBOPEN_DRAWS_QUALIFICATION, "Первый";

        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);

        String settings = FormulaTXApplication.getStringParameter(post_name + GRIDS_SETTINGS);
        if (settings == null) settings = Parser.SPBOPEN_DRAWS_QUALIFICATION;

        String page = FormulaTXApplication.getStringParameter(post_name + GRIDS_PAGE);
        if (page == null) page = null;

        bundle.putString(ParserAsyncLoader.TYPE, Parser.SPBOPEN_DRAWS);
        bundle.putString(ParserAsyncLoader.SETTING, settings);
        bundle.putString(ParserAsyncLoader.PAGE, page);
        bundle.putBoolean(ParserAsyncLoader.FORCED, false);

        getLoaderManager().initLoader(R.id.tables_loader, bundle, new ParserLoaderCallback());
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");

        super.onStart();
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(null);//Grids
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        Log.d(TAG, "onNavigationItemSelected");
        ParserHelper ph = new ParserHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Parser system = ph.get((int) l);
        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        FormulaTXApplication.setParameter(post_name + GRIDS_SETTINGS, system.settings);
        RestartLoader(false);
        return false;
    }

    @Override
    public void onTabChanged(String s) {
        Log.d(TAG, "onTabChanged");
        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        String last = FormulaTXApplication.getStringParameter(post_name + GRIDS_PAGE);
        if (last != null && last.equals(s)) return;
        FormulaTXApplication.setParameter(post_name + GRIDS_PAGE, s);
        RestartLoader(false);
    }

    private class ParserLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<ParserDataCursor> {
        @Override
        public Loader<ParserDataCursor> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            return new ParserAsyncLoader(getActivity(), getArguments().getString(TOURNAMENT_POST_NAME), args);
        }

        @Override
        public void onLoadFinished(Loader<ParserDataCursor> loader, ParserDataCursor data) {
            Log.d(TAG, "onLoadFinished");
            if (data == null) {
                mProgresView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }else{
                mProgresView.setVisibility(View.GONE);
                if (data.getCount()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                }else{
                    mEmptyView.setVisibility(View.GONE);
                }
                FitSpinner();
                FitTabHost();
                mAdapter.swapCursor(data);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onLoaderReset(Loader<ParserDataCursor> loader) {
            Log.d(TAG, "onLoaderReset");
        }
    }

    private void FitSpinner() {
        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament tournament = th.getByPostName(getArguments().getString(TOURNAMENT_POST_NAME));
        int[] intArray;
        intArray = tournament == null ? new int[0] : Utils.getIntListFromJSONArray(tournament.parsers_include);
        ParserHelper ph = new ParserHelper(FormulaTXApplication.getDatabaseOpenHelper());
        ParserCursor system = ph.getAllWithSystem(intArray, Parser.SPBOPEN_DRAWS);
        mSpinnerAdapter.swapCursor(system);
    }

    private void FitTabHost() {
        Log.d(TAG, "FitTabHost" );

        DatabaseOpenHelper doh = FormulaTXApplication.getDatabaseOpenHelper();
        TournamentHelper th = new TournamentHelper(doh);
        Tournament tournament = th.getByPostName(getArguments().getString(TOURNAMENT_POST_NAME));
        int[] intArray;
        intArray = tournament == null ? new int[0] : Utils.getIntListFromJSONArray(tournament.parsers_include);

        String post_name = getArguments().getString(TOURNAMENT_POST_NAME);
        String settings = FormulaTXApplication.getStringParameter(post_name + GRIDS_SETTINGS);
        if (settings == null) settings = Parser.SPBOPEN_DRAWS_QUALIFICATION;
        ParserMatchHelper pmh = new ParserMatchHelper(doh);
        ParserDataCursor pages = pmh.getPages(intArray, Parser.SPBOPEN_DRAWS, settings);

        mTabHost.setOnTabChangedListener(null);
        mTabHost.setup();
        mTabHost.clearAllTabs();
        while (!pages.isAfterLast()) {
            Table item = pages.getItem();
            TabHost.TabSpec ts = mTabHost.newTabSpec(item.page);
            ts.setIndicator(item.page);
            ts.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String s) {
                    return mSwipeRefreshLayout;
                }
            });
            Log.d(TAG, "add tab: '" + item.page + "'");
            mTabHost.addTab(ts);
            pages.moveToNext();
        }
        String pg = FormulaTXApplication.getStringParameter(post_name + GRIDS_PAGE);
        mTabHost.setCurrentTabByTag(pg);
        mTabHost.setOnTabChangedListener(this);
    }
}

