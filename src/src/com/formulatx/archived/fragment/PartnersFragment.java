package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class PartnersFragment extends Fragment implements LoaderManager.LoaderCallbacks<PartnerCursor>, AdapterView.OnItemClickListener {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private ListView mListView;
    private View mEmptyView;
    private View mProgresView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new PartnerAdapter(getActivity(), null);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        mEmptyView = view.findViewById(R.id.emptyView);
        mProgresView = view.findViewById(R.id.progressView);

        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(R.id.card_loader, Bundle.EMPTY, this);

        return view;
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
        if (data == null) {
            mProgresView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }else{
            mProgresView.setVisibility(View.GONE);
            if (data.getCount() == 0){
                mEmptyView.setVisibility(View.VISIBLE);
            }else{
                mEmptyView.setVisibility(View.GONE);
            }
        }
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<PartnerCursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            PartnerHelper ch = new PartnerHelper();
            Partner card = ch.getPartner(l);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(card.link));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), FormulaTXApplication.getResourceString(R.string.string_no_webbrowser), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

