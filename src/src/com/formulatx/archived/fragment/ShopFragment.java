package com.formulatx.archived.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import com.formulatx.archived.database.cursor.ProductCursor;
import com.formulatx.archived.database.model.ApiObjects.Product;
import com.rinnion.archived.R;
import com.formulatx.archived.fragment.adapter.ProductAdapter;
import com.formulatx.archived.network.loaders.ProductAsyncLoader;
import com.formulatx.archived.utils.Log;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class ShopFragment extends Fragment implements LoaderManager.LoaderCallbacks<ProductCursor> {

    private String TAG = getClass().getCanonicalName();
    private ListView mListView;
    private View mEmptyView;
    private View mProgresView;

    private ResourceCursorAdapter mAdapter;
    private Bundle mFavorites;
    private MenuItem mMenuItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);

        mEmptyView = view.findViewById(R.id.emptyView);
        mProgresView = view.findViewById(R.id.progressView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product=((ProductCursor) parent.getItemAtPosition(position)).getItem();
                Bundle bundle = new Bundle();
                //bundle.putString(ShopViewFragment.EN_SHOP_ITEM,  product);
                bundle.putSerializable(ShopViewFragment.EN_SHOP_ITEM,  product);


                ShopViewFragment svf = new ShopViewFragment();
                svf.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
                        .replace(R.id.fragment_container, svf)
                        .addToBackStack(null)
                        .commit();
            }
        });
        /**/


         mFavorites = new Bundle();
        mFavorites.putBoolean("favorites", false);
        getLoaderManager().initLoader(R.id.product_loader, mFavorites, this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new ProductAdapter(getActivity(), null);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorite_button, menu);
        mMenuItem = menu.findItem(R.id.fav);
    }

    @Override
    public void onResume() {
        ActionBar ab = getActivity().getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.string_shop);
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
            case R.id.fav:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                boolean favorites = mFavorites.getBoolean("favorites");
                mFavorites.putBoolean("favorites", !favorites);
                mMenuItem.setIcon(favorites ? R.drawable.like_selected_icon : R.drawable.like_noselected_icon);
                getLoaderManager().restartLoader(R.id.product_loader, mFavorites, this);
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<ProductCursor> onCreateLoader(int id, Bundle args) {
        return new ProductAsyncLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<ProductCursor> loader, ProductCursor data) {

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
    public void onLoaderReset(Loader<ProductCursor> loader) {
        mAdapter.swapCursor(null);
    }

}

