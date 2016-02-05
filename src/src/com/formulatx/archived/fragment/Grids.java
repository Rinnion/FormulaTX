package com.formulatx.archived.fragment;

import com.formulatx.archived.parsers.Match;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Lenovo on 03.02.2016.
 */
public class Grids {
    public final ArrayList<Round> Rounds = new ArrayList<Round>();

    public Round addRound(String name){
        Round object = new Round(name);
        Rounds.add(object);
        return object;
    }

    public class Round {
        public String cortName;

        MatchCursor Cursor;

        public Round(String name) {
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
