package com.formulatx.archived.network.loaders.cursor;

import android.database.MatrixCursor;
import android.provider.BaseColumns;
import com.formulatx.archived.utils.Log;

import java.util.Arrays;

/**
 * Created by tretyakov on 15.01.2016.
 * represents program cursor
 */
public class ProgramCursor extends MatrixCursor{

    public static final String _ID = BaseColumns._ID;
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String TIME = "time";
    public static final String IN_PAST = "in_past";
    public static final String TODAY = "today";

    public static final int TYPE_DAY = 0;
    public static final int TYPE_EVT = 1;
    private static final String TAG = "ProgramCursor";

    private static String[] names = new String[]{TYPE, NAME, TIME, IN_PAST, TODAY};
    private static String[] columns = new String[]{_ID, TYPE, NAME, TIME, IN_PAST, TODAY};

    public ProgramCursor() {
        super(columns);
    }

    public long getId(){
        return getLong(getColumnIndexOrThrow(_ID));
    }

    public int getType(){
        return getInt(getColumnIndexOrThrow(TYPE));
    }

    public String getTitle() {
        return getString(getColumnIndexOrThrow(NAME));
    }

    public String getTime() {
        return getString(getColumnIndexOrThrow(TIME));
    }

    public boolean getInPast() {
        String value = getString(getColumnIndexOrThrow(IN_PAST));
        return (value!=null && Boolean.TRUE.equals(Boolean.valueOf(value)));
    }
    public boolean getToday() {
        String value = getString(getColumnIndexOrThrow(TODAY));
        return (value!=null && Boolean.TRUE.equals(Boolean.valueOf(value)));
    }

    public void addRow(int type, String name, String time, boolean in_past, boolean b) {
        int count = getCount();
        Object[] columnValues = {count + 1, type, name, time, in_past, b};
        Log.d(TAG, Arrays.toString(columnValues));
        super.addRow(columnValues);
    }

    public void addRow(int type, String name, String time, boolean in_past) {
        int count = getCount();
        Object[] columnValues = {count + 1, type, name, time, in_past, null};
        Log.d(TAG, Arrays.toString(columnValues));
        super.addRow(columnValues);
    }

}
