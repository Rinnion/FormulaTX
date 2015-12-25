package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.TournamentCursor;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class OtherTournamentListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<TournamentCursor> {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private ListView mListView;
    private TextView mTextView;
    private AdapterView.OnItemClickListener mListener;
    private View mEmpty;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        String[] from = new String[]{
                "_id",
                "thumbs",
                "caption"
        };

        int[] to = new int[]{
                R.id.itl_tv_caption,
                R.id.itl_iv_thumbs,
                R.id.itl_tv_caption
        };

        MatrixCursor cursor = new MatrixCursor(from);
        cursor.addRow(new Object[]{1, null, "ФОРМУЛА ТХ"});
        cursor.addRow(new Object[]{2, null, "ST.PETERSBURG HEAD JUNIOR"});
        cursor.addRow(new Object[]{3, null, "WINTER MOSCOW"});

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_tournament_layout, cursor, from, to, 0) {
            @Override
            public void setViewImage(ImageView v, String value) {
                super.setViewImage(v, "");
            }
        };

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_other_tounaments);
        ab.setIcon(R.drawable.ic_action_previous_item);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showOtherTournamentFragment(id);
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

    private void showOtherTournamentFragment(long id) {
        OtherTournamentFragment mlf = new OtherTournamentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(OtherTournamentFragment.TYPE, id);
        mlf.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message, menu);
    }

    @Override
    public Loader<TournamentCursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<TournamentCursor> loader, TournamentCursor data) {

    }

    @Override
    public void onLoaderReset(Loader<TournamentCursor> loader) {

    }
}

