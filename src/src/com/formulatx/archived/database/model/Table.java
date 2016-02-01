package com.formulatx.archived.database.model;

import com.formulatx.archived.network.loaders.cursor.ParserDataCursor;
import com.formulatx.archived.parsers.Match;
import org.json.JSONException;
import org.json.JSONObject;

/**
* Created by tretyakov on 28.01.2016.
*/
public class Table {
    private ParserDataCursor tableCursor;
    public long id;
    public String page;
    public int number;
    public String data;
    public String type;
    public long parser;

    public Match getMatch() {
        try {
            Match match ;
            match = Match.parseJSONObject(new JSONObject(data));
            return match;
        } catch (JSONException e) {
            return null;
        }
    }
}
