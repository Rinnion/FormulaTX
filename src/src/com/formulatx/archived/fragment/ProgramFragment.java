package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.*;
import android.os.Bundle;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.model.ApiObjects.Tournament;
import com.formulatx.archived.network.loaders.ProgramAsyncLoader;
import com.formulatx.archived.network.loaders.cursor.ProgramCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.TournamentHelper;
import com.formulatx.archived.fragment.adapter.ProgramAdapter;
import com.formulatx.archived.utils.Log;
import android.view.MenuItem;
import android.widget.ResourceCursorAdapter;
import com.rinnion.archived.R;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class ProgramFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ProgramCursor> {

    public static final String TOURNAMENT_POST_NAME = ApiObjectHelper.COLUMN_POST_NAME;
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

        mAdapter = new ProgramAdapter(getActivity(), null);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.program_loader, Bundle.EMPTY, this);

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
    public void onStart() {
        getListView().setDivider(null);
        super.onStart();
    }

    @Override
    public Loader<ProgramCursor> onCreateLoader(int id, Bundle args) {
        Bundle arguments = getArguments();
        String post_name = arguments.getString(TOURNAMENT_POST_NAME);
        TournamentHelper th = new TournamentHelper(FormulaTXApplication.getDatabaseOpenHelper());
        Tournament byPostName = th.getByPostName(post_name);
        if (byPostName==null) return null;
        return new ProgramAsyncLoader(getActivity(), byPostName.tables);
    }

    @Override
    public void onLoadFinished(Loader<ProgramCursor> loader, ProgramCursor data) {

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<ProgramCursor> loader) {

    }

}

