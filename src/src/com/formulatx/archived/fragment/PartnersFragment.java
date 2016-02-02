package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;
import com.formulatx.archived.FormulaTXApplication;
import com.formulatx.archived.database.helper.PartnerHelper;
import com.formulatx.archived.database.model.ApiObjects.Partner;
import com.formulatx.archived.fragment.adapter.PartnerAdapter;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.PartnerCursor;
import com.formulatx.archived.network.loaders.PartnerAsyncLoader;
import com.formulatx.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class PartnersFragment extends ListFragment implements LoaderManager.LoaderCallbacks<PartnerCursor> {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new PartnerAdapter(getActivity(), null);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(R.id.card_loader, Bundle.EMPTY, this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_navigation_partners);
            ab.setIcon(R.drawable.ic_action_previous_item);
        }
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            PartnerHelper ch = new PartnerHelper();
            Partner card = ch.getPartner(id);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(card.link));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), FormulaTXApplication.getResourceString(R.string.string_no_webbrowser), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        //TODO: open browser for card
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

    @Override
    public Loader<PartnerCursor> onCreateLoader(int id, Bundle args) {
        return new PartnerAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<PartnerCursor> loader, PartnerCursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<PartnerCursor> loader) {

    }
}
