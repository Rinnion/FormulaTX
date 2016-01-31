package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.ArchivedApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Lenovo on 26.12.2015.
 */
public class WeatherHandler extends JSONObjectHandler {

    private String country;

    public WeatherHandler(String county){

        this.country = county;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        long dt = object.getLong("dt");

        JSONObject main = object.getJSONObject("main");
        double temp = main.getDouble("temp");

        JSONArray weather = object.getJSONArray("weather");
        Bundle bundle = new Bundle();
        if (weather.length() != 0){
            JSONObject item = (JSONObject)weather.get(0);

            int id = item.getInt("id");
            String itemMain = item.getString("description");
            String description = item.getString("description");
            String icon = item.getString("icon");

            bundle.putInt("id", id);
            bundle.putString("main", itemMain);
            bundle.putString("description", description);
            bundle.putString("icon", icon);
            bundle.putDouble("temp", temp);
            bundle.putDouble("time", dt);

            JSONObject dbObject = new JSONObject();
            dbObject.put("id", id);
            dbObject.put("main", itemMain);
            dbObject.put("description", description);
            dbObject.put("icon", icon);
            dbObject.put("temp", temp);
            dbObject.put("time", String.valueOf(Calendar.getInstance().getTimeInMillis()));

            ArchivedApplication.setParameter("weather_" + this.country, dbObject.toString());

        }else{
            bundle.putInt("id", 0);
            bundle.putString("main", "");
            bundle.putString("description", "");
            bundle.putString("icon", "");
            bundle.putDouble("temp", 0);
            return null;
        }

        return bundle;

    }
}
