package com.formulatx.archived.fragment;

import android.database.Cursor;
import com.formulatx.archived.parsers.Match;

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

    private class Cort {
        public String cortName;

        public Cort(String name) {
            cortName = name;
        }

        public ArrayList<Match> Matches = new ArrayList<Match>();

        public Match addMatch(){
            Match object = new Match();
            Matches.add(object);
            return object;
        }

    }
}
