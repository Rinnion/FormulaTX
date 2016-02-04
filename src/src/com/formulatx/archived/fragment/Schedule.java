package com.formulatx.archived.fragment;

import android.database.Cursor;
import com.formulatx.archived.parsers.Match;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class Schedule {
    public final ArrayList<Cort> Corts = new ArrayList<Cort>();

    public Cort addCort(String name){
        Cort object = new Cort(name);
        Corts.add(object);
        return object;
    }

    public class Cort {
        public String cortName;

        MatchCursor Cursor;

        public Cort(String name) {
            cortName = name;
            Cursor = new MatchCursor();
        }

        public ArrayList<Match> Matches = new ArrayList<Match>();

        public Match addMatch(Match match) {
            try {
                Cursor.add(cortName, Matches.size()+1, match.getJSONObject().toString(), "", "");
                Matches.add(match);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return match;
        }

    }

}
