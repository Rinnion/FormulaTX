
package com.rinnion.archived.fragment;

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
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.DatabaseOpenHelper;
import com.rinnion.archived.database.cursor.ParserCursor;
import com.rinnion.archived.database.helper.ApiObjectHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.fragment.adapter.TableAdapter;
import com.rinnion.archived.network.loaders.ParserAsyncLoader;
import com.rinnion.archived.network.loaders.cursor.TableCursor;
import com.rinnion.archived.utils.Log;
import com.rinnion.archived.utils.WebViewWithCache;
import org.lorecraft.phparser.SerializedPhpParser;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class TablesFragment extends Fragment {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
    private String TAG = getClass().getCanonicalName();
    private TableAdapter mAdapter;
    private SwipeRefreshLayout view;

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
        view = (SwipeRefreshLayout) inflater.inflate(R.layout.refreshable_list_layout, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);

        mAdapter = new TableAdapter(getActivity(), null);

        listView.setAdapter(mAdapter);
        listView.setDivider(null);

        getLoaderManager().initLoader(R.id.tables_loader, Bundle.EMPTY, new ParserLoaderCallback());

        view.setColorScheme(android.R.color.holo_red_dark, android.R.color.holo_orange_dark, android.R.color.holo_green_dark, android.R.color.holo_blue_dark);
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                view.setRefreshing(true);
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

    private class ParserLoaderCallback implements android.app.LoaderManager.LoaderCallbacks<TableCursor> {
        @Override
        public Loader<TableCursor> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "onCreateLoader");
            return new ParserAsyncLoader(getActivity(), getArguments().getString(TOURNAMENT_POST_NAME), Parser.SPBOPEN_TIMETABLE, "pyatnica-25-09-2015", "live");
        }

        @Override
        public void onLoadFinished(Loader<TableCursor> loader, TableCursor data) {
            Log.d(TAG, "onLoadFinished");
            mAdapter.swapCursor(data);
            view.setRefreshing(false);
        }

        @Override
        public void onLoaderReset(Loader<TableCursor> loader) {

        }
    }

}

