package com.rinnion.archived.network.loaders.cursor;

import android.database.MatrixCursor;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import com.rinnion.archived.utils.Log;

import java.util.Arrays;

/**
* Created by tretyakov on 25.01.2016.
*/
public class WeatherCursor {
    public static String MOSCOW = "moscow";
    public static String PETERSBURG = "petersburg";

    public class City{
        public String main;
        public String temp;
        public int icon;
    }

    public City Peter;
    public City Moscow;

    public WeatherCursor(){
        Peter = new City();
        Moscow = new City();
    }
}
