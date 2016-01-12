package com.rinnion.archived.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import com.rinnion.archived.utils.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.fragment.adapter.NewsAdapter;
import com.rinnion.archived.network.HttpRequester;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.loaders.NewsAsyncLoader;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 15.02.14
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class TodayFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ApiObjectCursor>, AdapterView.OnItemClickListener {

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

        getLoaderManager().initLoader(R.id.message_loader, Bundle.EMPTY, this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_layout, container, false);

        final TextView pbTemp = (TextView)view.findViewById(R.id.tl_tv_peter_temp);
        final TextView pbMain = (TextView)view.findViewById(R.id.tl_tv_peter_main);
        final ImageView pbIcon = (ImageView) view.findViewById(R.id.tl_iv_peter);
        final TextView mosTemp = (TextView)view.findViewById(R.id.tl_tv_moscow_temp);
        final TextView mosMain = (TextView)view.findViewById(R.id.tl_tv_moscow_main);
        final ImageView mosIcon = (ImageView) view.findViewById(R.id.tl_iv_moscow);

        getActivity().getActionBar().setTitle(R.string.string_today);
        getActivity().getActionBar().setIcon(R.drawable.menu_icon);

        //MatrixCursor mc = new MatrixCursor(NewsAdapter.fromSpinner);
        //mc.addRow(new Object[]{1, null, "Шарапова встретилась с друзьями", "14 декабря, 10:57"});
        //mc.addRow(new Object[]{2, null, "Раонич и Гаске снялись с IPTL из-за травм спины", "14 декабря, 10:17"});

        ListView mListView = (ListView) view.findViewById(R.id.tl_lv_news);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        //mAdapter.swapCursor(mc);

        LoadWeather(pbTemp, pbMain, pbIcon, "petersburg");
        LoadWeather(mosTemp, mosMain, mosIcon, "moscow");

        return view;
    }

    private void LoadWeather(final TextView pbTemp, final TextView pbMain, final ImageView pbIcon, final String petersburg) {
        String weather_petersburg = ArchivedApplication.getParameter("weather_" + petersburg);
        if (weather_petersburg != null) {
            try{
                JSONObject jsonPetersburg = new JSONObject(weather_petersburg);
                if (jsonPetersburg.getLong("time")+1800000<Calendar.getInstance().getTimeInMillis()){
                    RunWeatherLoader(pbTemp, pbMain, pbIcon, petersburg);
                }else {
                    FitWeather(pbTemp, pbMain, pbIcon, jsonPetersburg);
                }
            }catch (Exception ignored){

            }
        }else {
            RunWeatherLoader(pbTemp, pbMain, pbIcon, petersburg);
        }
    }

    private void RunWeatherLoader(final TextView pbTemp, final TextView pbMain, final ImageView pbIcon, final String petersburg) {
        AsyncTask<Void, Void, Bundle> at = new AsyncTask<Void, Void, Bundle>() {
            @Override
            protected Bundle doInBackground(Void... params) {
                return MyNetwork.queryWeather(petersburg);
            }

            @Override
            protected void onPostExecute(Bundle aBundle) {
                String string = aBundle.getString(HttpRequester.RESULT);
                if (string.equals(HttpRequester.RESULT_HTTP)) {
                    LoadWeather(pbTemp, pbMain, pbIcon, petersburg);
                }
            }
        };
        at.execute();
    }

    private void FitWeather(TextView pbTemp, TextView pbMain, ImageView pbIcon, JSONObject jsonPetersburg) throws JSONException {
        String main = jsonPetersburg.getString("main");
        int temp = (int) Math.round(jsonPetersburg.getDouble("temp"));

        pbTemp.setText(String.valueOf(temp));
        pbMain.setText(String.valueOf(main));

        //FIXME: should work as described link
        //http://openweathermap.org/weather-conditions
        try {
            String strIcon = jsonPetersburg.getString("icon");
            int iIcon = Integer.parseInt(strIcon.substring(0, 2));
            switch (iIcon) {
                case 1:
                    pbIcon.setImageResource(R.drawable.weather_sunshine_icon);
                    break;
                case 2:
                    pbIcon.setImageResource(R.drawable.weather_sun_icon);
                    break;
                case 3:
                case 4:
                    pbIcon.setImageResource(R.drawable.weather_cloud_icon);
                    break;
                case 9:
                case 10:
                case 11:
                    pbIcon.setImageResource(R.drawable.weather_rain_icon);
                    break;
                case 13:
                    pbIcon.setImageResource(R.drawable.weather_snow_icon);
                    break;
                case 50:
                    pbIcon.setImageResource(R.drawable.weather_fog_icon);
                    break;
                default:
                    pbIcon.setImageResource(R.drawable.ic_action_help);
                    break;
            }
        } catch (Exception ex) {
            Log.e(TAG, "error parse weather image", ex);
            pbIcon.setImageResource(R.drawable.ic_action_help);
        }
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
        //Создаем асинхронный загрузчик
        return new NewsAsyncLoader(getActivity());
    }


    @Override
    public void onLoadFinished(Loader<ApiObjectCursor> loader, ApiObjectCursor data) {
        Log.d(TAG, "onLoadFinished");
        //Присваиваем результат в адаптер для отображения
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
}