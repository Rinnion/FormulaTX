package com.rinnion.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import com.rinnion.archived.utils.Log;
import android.view.MenuItem;
import android.widget.ResourceCursorAdapter;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.TournamentCursor;
import com.rinnion.archived.fragment.adapter.GamerAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class ProgramFragment extends ListFragment implements LoaderManager.LoaderCallbacks<TournamentCursor> {

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

        mAdapter = new GamerAdapter(getActivity(), null);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_tournament_program);
        ab.setIcon(R.drawable.ic_action_previous_item);

        super.onCreate(savedInstanceState);
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
    public Loader<TournamentCursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<TournamentCursor> loader, TournamentCursor data) {
        MatrixCursor mc = new MatrixCursor(GamerAdapter.fromSpinner);
        mc.addRow(new Object[]{1, null, "Томаш Бердых", "Чешская Республика", "CHZ", "6",});
        mc.addRow(new Object[]{2, null, "Милош Раонич", "Канада", "CND", "10",});
        mc.addRow(new Object[]{3, null, "Михаил Кукушкин", "Казахстан", "KAZ", "54",});

        mAdapter.swapCursor(mc);
    }

    @Override
    public void onLoaderReset(Loader<TournamentCursor> loader) {

    }
}

