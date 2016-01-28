package com.rinnion.archived.network.loaders.cursor;

import android.database.MatrixCursor;
import android.provider.BaseColumns;
import com.rinnion.archived.database.model.Parser;
import com.rinnion.archived.parsers.Match;
import com.rinnion.archived.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by tretyakov on 15.01.2016.
 * represents program cursor
 */
public class TableCursor extends MatrixCursor{

    public static final String _ID = BaseColumns._ID;
    public static final String PAGE = "page";
    public static final String NUMBER = "number";
    public static final String DATA = "data";
    public static final String TYPE = "type";

    private static final String TAG = "TableCursor";

    private static String[] names = new String[]{PAGE, NUMBER, DATA, TYPE};
    private static String[] columns = new String[]{_ID, PAGE, NUMBER, DATA, TYPE};

    public TableCursor() {
        super(columns);
    }

    public long getId(){
        return getLong(getColumnIndexOrThrow(_ID));
    }

    public String getType(){
        return getString(getColumnIndexOrThrow(TYPE));
    }

    public String getPage() {
        return getString(getColumnIndexOrThrow(PAGE));
    }

    public int getNumber() {
        return getInt(getColumnIndexOrThrow(NUMBER));
    }

    public String getData() {
        return getString(getColumnIndexOrThrow(DATA));
    }

    public void addRow(String page, int number, String type, String data) {
        int count = getCount();
        Object[] columnValues = {count + 1, page, number, data, type};
        Log.d(TAG, Arrays.toString(columnValues));
        super.addRow(columnValues);
    }

    public Match getMatch() {
        try {
            Match match = null;
            match = Match.parseJSONObject(new JSONObject(getData()));
            return match;
        } catch (JSONException e) {
            return null;
        }
    }
}
