package com.rinnion.archived.network.handlers;

import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parse News from server
 */
public class TournamentListHandler extends JSONObjectHandler {
    @Override
    public Bundle Handle(JSONObject object) throws JSONException {
        boolean status = object.getBoolean("status");
        if (status) {
            JSONArray message = object.getJSONArray("message");
            int[] idArray = new int[message.length()];
            for (int i = 0; i < message.length(); i++) {
                JSONObject o = (JSONObject) message.get(i);
                idArray[i] = o.getInt("id");
            }
            Bundle bundle = new Bundle();
            bundle.putIntArray("ID[]", idArray);
            return  bundle;
        }
        return Bundle.EMPTY;
    }

}
