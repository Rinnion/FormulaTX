package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import com.rinnion.archived.database.helper.AreaHelper;
import com.rinnion.archived.database.helper.GamerHelper;
import com.rinnion.archived.database.model.ApiObjects.Area;
import com.rinnion.archived.database.model.ApiObjects.Gamer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alekseev on 29.12.2015.
 */

public class AreaHandler extends JSONObjectHandler {

    private AreaHelper th;

    public AreaHandler(AreaHelper th){
        this.th = th;
    }

    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");

            Area ao = null;
            for (int i = 0; i < message.length(); i++) {
                JSONObject obj = (JSONObject) message.get(i);
                String id = obj.getString("post_id");
                String key = obj.getString("key");
                String value = obj.getString("value");

                if (ao == null) ao = th.getArea(Long.parseLong(id));
                if (ao == null) ao = new Area(Long.parseLong(id));
                if (key.equals("Adress")) {
                    ao.address = value;
                }
                if (key.equals("Maps")) {
                    ao.map = value;
                }
            }

            if (ao == null) return Bundle.EMPTY;

            th.merge(ao);

            return super.Handle(object);
        }
        return Bundle.EMPTY;
    }
}


