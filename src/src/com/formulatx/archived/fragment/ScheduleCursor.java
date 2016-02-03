package com.formulatx.archived.fragment;

import android.database.MatrixCursor;
import android.provider.BaseColumns;
import com.formulatx.archived.parsers.Match;
import com.formulatx.archived.utils.Log;
import org.json.JSONException;

import java.util.Arrays;

public class ScheduleCursor extends MatrixCursor{

    public static final String MATCH = "match";

    private static final String TAG = "ScheduleCursor";

    private static String[] names = new String[]{MATCH};
    private static String[] columns = new String[]{BaseColumns._ID, MATCH};

    public ScheduleCursor() {
        super(columns);
    }


    public String getMatch() {
        return getString(getColumnIndexOrThrow(MATCH));
    }

    public void addRow(Match match) {
        try {
            int count = getCount();
            Object[] columnValues = new Object[0];
            columnValues = new Object[]{count + 1, match.getJSONObject()};
            Log.d(TAG, Arrays.toString(columnValues));
            super.addRow(columnValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
