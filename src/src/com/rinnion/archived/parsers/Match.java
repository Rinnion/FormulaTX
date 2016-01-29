package com.rinnion.archived.parsers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tretyakov on 28.01.2016.
 */
public class Match {
    public String type; //Тип записи (Live, qualification, etc)
    public String header; //Название записи

    public Team team1 = new Team(); //Команда 1
    public Team team2 = new Team(); //Команда 2
    public Team team3 = new Team(); //Команда 2
    public Team team4 = new Team(); //Команда 2

    public JSONObject getJSONObject() throws JSONException {
        JSONObject match = new JSONObject();
        match.put("type", type);
        match.put("header", header);
        match.put("team1", (team1!= null) ? team1.getJSONObject() : null);
        match.put("team2", (team2!= null) ? team2.getJSONObject() : null);
        match.put("team3", (team3!= null) ? team3.getJSONObject() : null);
        match.put("team4", (team4!= null) ? team4.getJSONObject() : null);
        return match;
    }

    public static Match parseJSONObject(JSONObject in) throws JSONException {
        Match match = new Match();
        match.type = in.has("type") ? in.getString("type") : null;
        match.header = in.has("header") ? in.getString("header") : null;
        match.team1 = in.has("team1") ? Team.parseJSONObject(in.getJSONObject("team1")) : null;
        match.team2 = in.has("team2") ? Team.parseJSONObject(in.getJSONObject("team2")) : null;
        match.team3 = in.has("team3") ? Team.parseJSONObject(in.getJSONObject("team3")) : null;
        match.team4 = in.has("team4") ? Team.parseJSONObject(in.getJSONObject("team4")) : null;
        return match;
    }

}