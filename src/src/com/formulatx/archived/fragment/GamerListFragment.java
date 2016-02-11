package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import com.parse.ParsePush;
import com.rinnion.archived.R;
import com.formulatx.archived.database.cursor.GamerCursor;
import com.formulatx.archived.database.helper.ApiObjectHelper;
import com.formulatx.archived.database.helper.GamerHelper;
import com.formulatx.archived.database.model.ApiObjects.Gamer;
import com.formulatx.archived.fragment.adapter.GamerAdapter;
import com.formulatx.archived.network.loaders.GamerAsyncLoader;
import com.formulatx.archived.utils.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class GamerListFragment extends Fragment implements LoaderManager.LoaderCallbacks<GamerCursor>, AdapterView.OnItemClickListener {


    private String TAG = getClass().getCanonicalName();
    private ListView mListView;
    private View mEmptyView;
    private View mProgresView;
    private ResourceCursorAdapter mAdapter;


    public static final String TYPE = "type";
    public static final String TOURNAMENT_ID = ApiObjectHelper._ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        mEmptyView = view.findViewById(R.id.emptyView);
        mProgresView = view.findViewById(R.id.progressView);

        mListView.setOnItemClickListener(this);

        getLoaderManager().initLoader(R.id.gamer_loader, getArguments(), this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new GamerAdapter(getActivity(), null, null);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.string_gamers);
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
    public Loader<GamerCursor> onCreateLoader(int id, Bundle args) {
        long type = args.getLong(TOURNAMENT_ID);
        return new GamerAsyncLoader(getActivity(), type);

    }

    @Override
    public void onLoadFinished(Loader<GamerCursor> loader, GamerCursor data) {
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
    public void onLoaderReset(Loader<GamerCursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GamerHelper gh = new GamerHelper();

        Gamer gamer = gh.getGamer(l);

        Log.d(TAG, String.valueOf(gamer.id));

        String channel = "gamer-" + String.valueOf(gamer.id);
        if (!gamer.favorite) {
            Log.d(TAG, "Subscribe to '" + channel + "'");
            ParsePush.subscribeInBackground(channel);
        }else{
            Log.d(TAG, "Unsubscribe to '" + channel + "'");
            ParsePush.unsubscribeInBackground(channel);
        }

        gamer.favorite = !gamer.favorite;
        gh.setFavorite(gamer.id, gamer.favorite);
        long type = getArguments().getLong(TOURNAMENT_ID);
        GamerCursor allByParent = gh.getAllByParent(type);
        mAdapter.swapCursor(allByParent);
    }
}

