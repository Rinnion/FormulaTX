package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.network.loaders.NewsAsyncLoader;
import com.rinnion.archived.utils.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.database.cursor.TournamentCursor;
import com.rinnion.archived.database.helper.NewsHelper;
import com.rinnion.archived.database.helper.TournamentHelper;
import com.rinnion.archived.database.model.ApiObjects.Tournament;
import com.rinnion.archived.fragment.adapter.NewsAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ApiObjectCursor> {

    public static String TOURNAMENT_POST_NAME = "tournament id";
    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private SwipeRefreshLayout view;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        Bundle args = getArguments();

        NewsHelper nh = new NewsHelper(ArchivedApplication.getDatabaseOpenHelper());
        NewsCursor newsCursor = null;
        if (args != null){
            String post_name = args.getString(TOURNAMENT_POST_NAME);
            newsCursor = nh.getByParent(post_name);
        }else{
            newsCursor = nh.getAll();
        }
        mAdapter = new NewsAdapter(getActivity(), newsCursor);
        getLoaderManager().initLoader(R.id.news_loader, Bundle.EMPTY, this);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (SwipeRefreshLayout) inflater.inflate(R.layout.list_layout, container, false);
        view.setColorScheme(android.R.color.holo_red_dark,android.R.color.holo_orange_dark,android.R.color.holo_green_dark,android.R.color.holo_blue_dark );
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                getLoaderManager().initLoader(R.id.news_loader, Bundle.EMPTY, NewsListFragment.this);
            }
        });
        ListView mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showOtherTournamentFragment(l);
            }
        });

        return view;
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

    private void showOtherTournamentFragment(long id) {
        NewsFragment mlf = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(NewsFragment.ID, id);
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Loader<ApiObjectCursor> onCreateLoader(int id, Bundle args) {
        return new NewsAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ApiObjectCursor> loader, ApiObjectCursor data) {
        mAdapter.swapCursor(data);
        view.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<ApiObjectCursor> loader) {
    }
}

