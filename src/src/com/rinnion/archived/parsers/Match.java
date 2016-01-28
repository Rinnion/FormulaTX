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

    public JSONObject getJSONObject() throws JSONException {
        JSONObject match = new JSONObject();
        match.put("type", type);
        match.put("team1", team1.getJSONObject());
        match.put("team2", team2.getJSONObject());
        return match;
    }

}
