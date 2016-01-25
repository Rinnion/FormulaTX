package com.rinnion.archived.network.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import com.rinnion.archived.R;
import com.rinnion.archived.database.helper.SettingsHelper;
import com.rinnion.archived.network.HttpRequester;
import com.rinnion.archived.network.MyNetwork;
import com.rinnion.archived.network.loaders.cursor.WeatherCursor;
import com.rinnion.archived.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;

import static java.util.Arrays.sort;

/**
* Created by tretyakov on 18.01.2016.
*/
public class WeatherAsyncLoader extends AsyncTaskLoader<WeatherCursor> {

    private String TAG = getClass().getSimpleName();

    public WeatherAsyncLoader(Context context) {
        super(context);
        Log.d(TAG, ".ctor");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        deliverResult(getProgressWeather());
    }

    @Override
    public WeatherCursor loadInBackground() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MyNetwork.queryWeather(WeatherCursor.MOSCOW);
        MyNetwork.queryWeather(WeatherCursor.PETERSBURG);
        WeatherCursor mc = getWeatherCursor();
        return mc;
    }

    public WeatherCursor getWeatherCursor(){
        WeatherCursor wc = new WeatherCursor();
        if (!FitWeather(wc.Moscow, WeatherCursor.MOSCOW ) || !FitWeather(wc.Peter, WeatherCursor.PETERSBURG)) return null;
        return wc;
    }

    private boolean FitWeather(WeatherCursor.City city, String name) {
        SettingsHelper sh = new SettingsHelper(ArchivedApplication.getDatabaseOpenHelper());
        String weather = sh.getStringParameter(name);
        if (weather == null){
            return false;
        }

        try{
            JSONObject json = new JSONObject(weather);
            String main = json.getString("main");
            String temp = String.valueOf((int) Math.round(json.getDouble("temp")));
            String icon = json.getString("icon");

            city.temp=temp;
            city.main=main;
            city.icon = getWeatherIcon(icon.substring(0, 2));
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    private int getWeatherIcon(String icon) {
        int iIcon = Integer.parseInt(icon.substring(0, 2));
        int drIcon;
        switch (iIcon) {
            case 1:
                drIcon = R.drawable.weather_sunshine_icon;
                break;
            case 2:
                drIcon = R.drawable.weather_sun_icon;
                break;
            case 3:
            case 4:
                drIcon = R.drawable.weather_cloud_icon;
                break;
            case 9:
            case 10:
            case 11:
                drIcon = R.drawable.weather_rain_icon;
                break;
            case 13:
                drIcon = R.drawable.weather_snow_icon;
                break;
            case 50:
                drIcon = R.drawable.weather_fog_icon;
                break;
            default:
                drIcon = R.drawable.ic_action_help;
                break;
        }
        return drIcon;
    }


    public WeatherCursor getProgressWeather() {
        WeatherCursor wc = new WeatherCursor();
        wc.Moscow.main = "main";
        wc.Moscow.temp = "?";
        wc.Moscow.icon = R.drawable.weather_sun_icon;

        wc.Peter.main = "petr";
        wc.Peter.temp = "?";
        wc.Peter.icon = R.drawable.weather_sun_icon;
        return wc;
    }
}
