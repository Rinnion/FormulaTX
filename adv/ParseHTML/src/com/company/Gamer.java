package com.company;

import org.json.JSONException;
import org.json.JSONObject;

/**
* Created by tretyakov on 28.01.2016.
*/
public class Gamer {
    public String photo; // ссылка на фотографи игрока
    public String name; // имя игрока
    public String cc; // country code alpha-2

    public Gamer(String Name){
        name = Name;
    }

    public Gamer(String Name, String cc){
        this.name = Name;
        this.cc = cc;
    }

    public Gamer(String Name, String cc, String photo){
        this.name = Name;
        this.cc = cc;
        this.photo = photo;
    }

    public JSONObject getJSONObject() throws JSONException {
        JSONObject gamer = new JSONObject();
        gamer.put("photo", photo);
        gamer.put("name", name);
        gamer.put("cc", cc);
        return gamer;
    }
}
