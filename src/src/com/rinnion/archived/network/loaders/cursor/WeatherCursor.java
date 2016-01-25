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
    /*public static final String _ID = BaseColumns._ID;
    public static final String MAIN = "main";
    public static final String TEMP = "temp";
    public static final String ICON = "icon";

    public static final int TYPE_DAY = 0;
    public static final int TYPE_EVT = 1;
    private static final String TAG = "ProgramCursor";

    private static String[] names = new String[]{MAIN, TEMP, ICON};
    private static String[] columns = new String[]{_ID, MAIN, TEMP, ICON};

    public WeatherCursor() {
        super(columns);
    }

    public long getId(){
        return getLong(getColumnIndexOrThrow(_ID));
    }

    public int getMain(){
        return getInt(getColumnIndexOrThrow(MAIN));
    }

    public String getTemp() {
        return getString(getColumnIndexOrThrow(TEMP));
    }

    public String getIcon() {
        return getString(getColumnIndexOrThrow(ICON));
    }


    public void addRow(String main, String temp, String icon) {
        int count = getCount();
        Object[] columnValues = {count+1, main, temp, icon};
        Log.d(TAG, Arrays.toString(columnValues));
        super.addRow(columnValues);
    }
    */
}
