package com.formulatx.archived.fragment;

import android.database.MatrixCursor;
import android.provider.BaseColumns;
import com.formulatx.archived.database.helper.ParserMatchHelper;
import com.formulatx.archived.parsers.Match;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lenovo on 04.02.2016.
 */
public class MatchCursor extends MatrixCursor {

    private static final String[] names;

    static {
        names = new String[]{
                BaseColumns._ID,
                ParserMatchHelper.COLUMN_PAGE,
                ParserMatchHelper.COLUMN_NUMBER,
                ParserMatchHelper.COLUMN_DATA,
                ParserMatchHelper.COLUMN_TYPE,
                ParserMatchHelper.COLUMN_PARSER
        };
    }

    public MatchCursor() {
        super(names);
    }

    public void add(String page, int number, String data, String type, String parser) {
        int index = getCount() + 1;
        addRow(new Object[]{index, page, number, data, type, parser});
    }

    public Match getMatch(){
        String string = getString(getColumnIndexOrThrow(ParserMatchHelper.COLUMN_DATA));
        try {
            return Match.parseJSONObject(new JSONObject(string));
        } catch (JSONException e) {
            return new Match();
        }
    }
}
