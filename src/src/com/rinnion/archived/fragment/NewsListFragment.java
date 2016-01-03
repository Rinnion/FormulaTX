package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
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
public class NewsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<TournamentCursor> {

    public static String TOURNAMENT_POST_NAME = "tournament id";
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

        Bundle args = getArguments();
        String post_name = args.getString(TOURNAMENT_POST_NAME);
        NewsHelper nh = new NewsHelper(ArchivedApplication.getDatabaseOpenHelper());
        NewsCursor newsCursor = nh.getByParent(post_name);

        mAdapter = new NewsAdapter(getActivity(), newsCursor);

        setListAdapter(mAdapter);
        //getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        showOtherTournamentFragment(id);
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
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Loader<TournamentCursor> onCreateLoader(int id, Bundle args) {

        return null;
    }

    @Override
    public void onLoadFinished(Loader<TournamentCursor> loader, TournamentCursor data) {

        mAdapter.swapCursor(null);
    }

    @Override
    public void onLoaderReset(Loader<TournamentCursor> loader) {

    }
}

