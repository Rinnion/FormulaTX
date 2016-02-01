package com.rinnion.archived.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import com.rinnion.archived.network.loaders.WeatherAsyncLoader;
import com.rinnion.archived.network.loaders.cursor.WeatherCursor;
import com.rinnion.archived.utils.Log;
import android.widget.*;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.cursor.ApiObjectCursor;
import com.rinnion.archived.fragment.adapter.NewsAdapter;
import com.rinnion.archived.network.HttpRequester;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.loaders.NewsAsyncLoader;
import com.rinnion.archived.utils.MoveTracker;
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

        tmpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                /*if (currentState != State.EDIT_MOVE) return false;*/

                //LinearLayout params = (FrameLayout.LayoutParams) view.getLayoutParams();
                if(mHeight==0) {
                    mHeight = tmpViewWeather.getMeasuredHeight();

                }
                LinearLayout linearLayout = (LinearLayout) tmpViewTest;
                //Animation slideAnimUp=AnimationUtils.loadAnimation(ArchivedApplication.getAppContext(),R.anim.weather_up);
                //Animation slideAnimDown=AnimationUtils.loadAnimation(ArchivedApplication.getAppContext(),R.anim.weather_down);

                TranslateAnimation translateAnimationUp=new TranslateAnimation(0,0,0,-mHeight);

                translateAnimationUp.setDuration(1000);
                translateAnimationUp.setFillAfter(false);
                translateAnimationUp.setFillEnabled(true);
                //translateAnimationUp.setFillBefore(false);

                translateAnimationUp.setInterpolator(new AccelerateInterpolator());
                translateAnimationUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //tmpViewTest.setEnabled(false);
                      //  tmpViewTest.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // tmpViewTest.setEnabled(true)
                         tmpView.clearAnimation();
                       tmpViewTest.setTranslationY(-mHeight);

                       // tmpView.clearAnimation();
                        //tmpViewTest.requestLayout();
                        //tmpViewNews.layout(tmpViewTest.getLeft(), tmpViewTest.getTop(), tmpViewTest.getRight(), tmpViewTest.getBottom() + mHeight);

                        //tmpViewNews.requestLayout();

                        //tmpViewNews.layout(tmpViewEvents.getLeft(), tmpViewEvents.getTop(), tmpViewEvents.getRight(), tmpViewEvents.getBottom() + mHeight);

                       // tmpViewTest.setVisibility(View.VISIBLE);
                        //tmpViewTest.requestLayout();
                        Point point=new Point();

                        DisplayManagerCompat.getInstance(getActivity()).getDisplay(0).getSize(point);

                        tmpViewNews.getLayoutParams().height=point.y;

                        //int specH=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int)(mHeight+tmpView.getMeasuredHeight()), getResources().getDisplayMetrics());

                        /*tmpViewNews.getLayoutParams().height=tmpViewNews.getLayoutParams().height+specH;*/

                    //Log.d(TAG,"H: " + tmpView.getMeasuredHeight());

                      //  tmpViewTest.getLayoutParams().height=specH;
                        isUpped = true;
                        //tmpViewTest.requestLayout();
                        //tmpViewTest.layout(tmpViewTest.getLeft(), tmpViewTest.getTop(), tmpViewTest.getRight(), tmpViewTest.getBottom());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                TranslateAnimation translateAnimationDown=new TranslateAnimation(0,0,-mHeight,0);
                translateAnimationDown.setDuration(2000);
                translateAnimationDown.setFillAfter(false);
                translateAnimationDown.setFillEnabled(true);
                translateAnimationDown.setFillBefore(false);

                translateAnimationDown.setInterpolator(new AccelerateInterpolator());
                translateAnimationDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //tmpViewTest.setEnabled(false);
                        tmpViewTest.setTranslationY(0);
                        tmpViewTest.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //tmpViewTest.setEnabled(true);
                        tmpViewTest.setTranslationY(0);
                        tmpView.clearAnimation();


                        tmpViewTest.setVisibility(View.VISIBLE);
                        isUpped=false;
                        // tmpViewTest.requestLayout();
                        //tmpViewTest.layout(tmpViewTest.getLeft(), tmpViewTest.getTop(), tmpViewTest.getRight(), tmpViewTest.getBottom());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
                /*if (view.getId() != R.id.weather) return false;*/

                switch (event.getAction()) {


                    case MotionEvent.ACTION_UP:
                        /*params.height = (int) event.getRawY() - view.getHeight();

                        linearLayout.setLayoutParams(params);*/

                        if (moveTracker.Up(event) == MoveTracker.MoveUp) {
                            if(!isUpped)
                            linearLayout.startAnimation(translateAnimationUp);

                            //tmpViewEvents.startAnimation(slideAnimUp);

                        } else {
                            //linearLayout.startAnimation(slideAnimDown);
                            //tmpViewEvents.startAnimation(slideAnimDown);
                            if(isUpped)
                            linearLayout.startAnimation(translateAnimationDown);
                        }
                        Log.d(TAG, "MotionEvent.ACTION_UP");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        moveTracker.Down(event);
                        /*linearLayout.setLayoutParams(params);*/
                        Log.d(TAG, "MotionEvent.ACTION_DOWN");
                        break;
                }

                return true;
            }
        });



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
        //Создаем асинхронный загрузчик
        return new NewsAsyncLoader(getActivity(), null);
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