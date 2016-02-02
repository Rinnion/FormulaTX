package com.formulatx.archived.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.formulatx.archived.database.cursor.ApiObjectCursor;
import com.formulatx.archived.fragment.adapter.NewsAdapter;
import com.formulatx.archived.network.loaders.WeatherAsyncLoader;
import com.formulatx.archived.network.loaders.cursor.WeatherCursor;
import com.formulatx.archived.utils.Log;
import com.formulatx.archived.utils.MoveTracker;
import android.widget.*;
import com.rinnion.archived.R;
import com.formulatx.archived.network.loaders.NewsAsyncLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<ApiObjectCursor>, AdapterView.OnItemClickListener {

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

        mAdapter = new NewsAdapter(getActivity(), null);

        getLoaderManager().initLoader(R.id.news_loader, Bundle.EMPTY, this);
        getLoaderManager().initLoader(R.id.weather_loader, Bundle.EMPTY, new WeatherLoader());

        super.onCreate(savedInstanceState);
    }
    public static View tmpViewTest;
    public static View tmpViewWeather;
    public static View tmpViewEvents;
    public static View tmpViewNews;
    public static MoveTracker moveTracker;
    public static boolean isUpped=false;
    public static int mHeight=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_layout, container, false);

        final TextView pbTemp = (TextView)view.findViewById(R.id.tl_tv_peter_temp);
        final TextView pbMain = (TextView)view.findViewById(R.id.tl_tv_peter_main);
        final ImageView pbIcon = (ImageView) view.findViewById(R.id.tl_iv_peter);
        final TextView mosTemp = (TextView)view.findViewById(R.id.tl_tv_moscow_temp);
        final TextView mosMain = (TextView)view.findViewById(R.id.tl_tv_moscow_main);
        final ImageView mosIcon = (ImageView) view.findViewById(R.id.tl_iv_moscow);

        final  View tmpView=(View)view.findViewById(R.id.textView7);
        tmpViewTest=view.findViewById(R.id.main_today);
        tmpViewWeather=view.findViewById(R.id.weather);
        tmpViewEvents=view.findViewById(R.id.events);
        tmpViewNews=view.findViewById(R.id.tl_lv_news);

        moveTracker=new MoveTracker();



        getActivity().getActionBar().setTitle(R.string.string_today);
        getActivity().getActionBar().setIcon(R.drawable.menu_icon);

        ListView mListView = (ListView) view.findViewById(R.id.tl_lv_news);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);


        return view;
    }
    public void showNavigationFragment() {
        NavigationFragment mlf = new NavigationFragment();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.scale_outside_to_bound, R.animator.scale_inside, R.animator.scale_inside_to_bound, R.animator.scale_outside)
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
    public Loader<ApiObjectCursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        return new NewsAsyncLoader(getActivity(), null);
    }


    @Override
    public void onLoadFinished(Loader<ApiObjectCursor> loader, ApiObjectCursor data) {
        Log.d(TAG, "onLoadFinished");
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<ApiObjectCursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showNewsDefaultFragment(id);
    }

    private void showNewsDefaultFragment(long id) {
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

    public class WeatherLoader implements LoaderManager.LoaderCallbacks<WeatherCursor>{

        @Override
        public Loader<WeatherCursor> onCreateLoader(int i, Bundle bundle) {
            return new WeatherAsyncLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<WeatherCursor> loader, WeatherCursor weatherCursor) {
            View view = getView();
            if (weatherCursor != null) {
                View vWeather = view.findViewById(R.id.tf_ll_weather_nest);
                vWeather.setVisibility(View.VISIBLE);
                View tvNotAvailable = view.findViewById(R.id.tf_tv_not_available);
                tvNotAvailable.setVisibility(View.GONE);

                final TextView pbTemp = (TextView) view.findViewById(R.id.tl_tv_peter_temp);
                final TextView pbMain = (TextView) view.findViewById(R.id.tl_tv_peter_main);
                final ImageView pbIcon = (ImageView) view.findViewById(R.id.tl_iv_peter);
                final TextView mosTemp = (TextView) view.findViewById(R.id.tl_tv_moscow_temp);
                final TextView mosMain = (TextView) view.findViewById(R.id.tl_tv_moscow_main);
                final ImageView mosIcon = (ImageView) view.findViewById(R.id.tl_iv_moscow);

                pbTemp.setText(String.valueOf(weatherCursor.Peter.temp));
                pbMain.setText(String.valueOf(weatherCursor.Peter.main));
                pbIcon.setImageResource(weatherCursor.Peter.icon);
                mosTemp.setText(String.valueOf(weatherCursor.Moscow.temp));
                mosMain.setText(String.valueOf(weatherCursor.Moscow.main));
                mosIcon.setImageResource(weatherCursor.Moscow.icon);
            }else{
                View vWeather = view.findViewById(R.id.tf_ll_weather_nest);
                vWeather.setVisibility(View.GONE);
                View tvNotAvailable = view.findViewById(R.id.tf_tv_not_available);
                tvNotAvailable.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<WeatherCursor> loader) {

        }
    }
}