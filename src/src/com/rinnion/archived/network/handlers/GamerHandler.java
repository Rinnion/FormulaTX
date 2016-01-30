package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class GamerHandler extends JSONObjectHandler {

    private GamerHelper th;

    public GamerHandler(){
        this.th = new GamerHelper();
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Gamer ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getGamer(Long.parseLong(id));
                if (ao == null) ao = new Gamer(Long.parseLong(id));
                if (key.equals("Rating")) {
                    ao.rating = Float.parseFloat(value);
                }
                if (key.equals("Country")) {
                    ao.country = value;
                }
                if (key.equals("Name")) {
                    ao.name = value;
                }
                if (key.equals("Surname")) {
                    ao.surname = value;
                }
            }

            if (ao == null) return Bundle.EMPTY;

            ao.flag = "ru";
            ao.full_name = String.valueOf(ao.name) + " " + String.valueOf(ao.surname);

            th.merge(ao);

            return super.Handle(object);
        }
        return Bundle.EMPTY;
    }
}


