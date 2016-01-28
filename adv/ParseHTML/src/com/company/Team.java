package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
* Created by tretyakov on 28.01.2016.
*/
public class Team {
    public String extra; // специальные данные, к примеру WC
    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(); // список игроков команды
    public Boolean shot; // признак подающей команды

    public String count; //очки в текущем гейме
    public String r1; // очки в 1-ом сете
    public String r2; // очки в 2-ом сете
    public String r3; // очки в 3-ем сете

    public JSONObject getJSONObject() throws JSONException {
        JSONObject line = new JSONObject();
        line.put("extra", extra);
        JSONArray gs = new JSONArray();
        for (Gamer g: gamers){
            gs.put(g.getJSONObject());
        }
        line.put("gamers", gs);
        line.put("shot", shot);
        line.put("count", count);
        line.put("r1", r1);
        line.put("r2", r2);
        line.put("r3", r3);
        return line;
    }

}
