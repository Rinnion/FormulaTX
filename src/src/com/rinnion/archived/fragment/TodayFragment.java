package com.rinnion.archived.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.NewsCursor;
import com.rinnion.archived.fragment.adapter.NewsAdapter;
import com.rinnion.archived.network.loaders.MessageAsyncLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.                                                              np:\\.\pipe\LOCALDB#C9D6BA74\tsql\query
 */
public class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<NewsCursor> {

    private String TAG = getClass().getCanonicalName();
    private ResourceCursorAdapter mAdapter;
    private ListView mListView;
    private AdapterView.OnItemClickListener mListener;
    private View mEmpty;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mAdapter = new NewsAdapter(getActivity(), null);

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_layout, container, false);

        getActivity().getActionBar().setTitle(R.string.string_today);
        getActivity().getActionBar().setIcon(R.drawable.ic_drawer);

        MatrixCursor mc = new MatrixCursor(NewsAdapter.fromSpinner);
        mc.addRow(new Object[]{1, null, "Шарапова встретилась с друзьями", "14 декабря, 10:57"});
        mc.addRow(new Object[]{2, null, "Раонич и Гаске снялись с IPTL из-за травм спины", "14 декабря, 10:17"});

        mListView = (ListView) view.findViewById(R.id.tl_lv_news);
        mListView.setAdapter(mAdapter);

        mAdapter.swapCursor(mc);

        return view;
/*        View view = inflater.inflate(R.layout.message_list_layout, container, false);
        mListView = (ListView) view.findViewById(R.id.ml_lv_list);
        mEmpty = view.findViewById(R.id.ml_tv_empty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //if (mListener != null) mListener.onItemClick(parent, view, position, id);
            }
        });

        mListView.setEmptyView(mEmpty);

        mListView.setAdapter(mAdapter);

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle("Сегодня");

        return view;
*/
    }



    public void showNavigationFragment() {
        NavigationFragment mlf = new NavigationFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mlf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: 'home' selected");
                showNavigationFragment();
                return super.onOptionsItemSelected(item);
            default:
                Log.d(TAG, "onOptionsItemSelected: default section");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<NewsCursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        //Создаем асинхронный загрузчик
        return new MessageAsyncLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<NewsCursor> loader, NewsCursor data) {
        Log.d(TAG, "onLoadFinished");
        //Присваиваем результат в адаптер для отображения
        //mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<NewsCursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }


}